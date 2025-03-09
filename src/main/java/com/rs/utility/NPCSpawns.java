package com.rs.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;

public final class NPCSpawns {

	private static final Object lock = new Object();
	@SuppressWarnings("resource")
	public static boolean addSpawn(String username, int id, Position tile) throws Throwable {
		synchronized(lock) {
			File file = new File("data/npcs/spawns.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write("// "+NPCDefinitions.getNPCDefinitions(id).name+", "+NPCDefinitions.getNPCDefinitions(id).combatLevel+", added by: "+username);
			writer.newLine();
			writer.flush();
			writer.write(id+" - "+tile.getX()+" "+tile.getY()+" "+tile.getZ());
			writer.newLine();
			writer.flush();
			World.spawnNPC(id, tile, -1, 0, true);
			return true;
		}

	}

	@SuppressWarnings("resource")
	public static boolean removeSpawn(NPC npc) throws Throwable {
		synchronized(lock) {
			List<String> page = new ArrayList<>();
			File file = new File("data/npcs/spawns.txt");
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			boolean removed = false;
			int id =  npc.getId();
			Position tile = npc.getSpawnPosition();
			while((line = in.readLine()) != null)  {
				if(line.equals(id+" - "+tile.getX()+" "+tile.getY()+" "+tile.getZ())) {
					page.remove(page.get(page.size()-1)); //description
					removed = true;
					continue;
				}
				page.add(line);
			}
			if(!removed)
				return false;
			file.delete();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(String l : page) {
				writer.write(l);
				writer.newLine();
				writer.flush();
			}
			npc.finish();
			return true;
		}
	}

	public static final void init() {
		if (!new File("data/npcs/packedSpawns").exists())
			packNPCSpawns();
	}

	@SuppressWarnings("resource")
	public static final void packNPCSpawns() {
		Logger.log("NPCSpawns", "Packing npc spawns...");
		if (!new File("data/npcs/packedSpawns").mkdir())
			throw new RuntimeException(
					"Couldn't create packedSpawns directory.");
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"data/npcs/unpackedSpawnsList.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid NPC Spawn line: " + line + "");
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 5);
				if (splitedLine2.length != 3 && splitedLine2.length != 4)
					throw new RuntimeException("Invalid NPC Spawn line: " + line + "");
				Position tile = new Position(
						Integer.parseInt(splitedLine2[0]),
						Integer.parseInt(splitedLine2[1]),
						Integer.parseInt(splitedLine2[2]));
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				int faceDirection = 0;
				if (splitedLine2.length == 4)
					faceDirection = Integer.parseInt(splitedLine2[3]);
				addNPCSpawn(npcId, tile.getRegionId(), tile, mapAreaNameHash, canBeAttackFromOutOfArea, faceDirection);
			}
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadNPCSpawns(int regionId) {
		File file = new File("data/npcs/packedSpawns/" + regionId + ".ns");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				int npcId = buffer.getShort() & 0xffff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				boolean hashExtraInformation = buffer.get() == 1;
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				int faceDirection = 0;
				if (hashExtraInformation) {
					faceDirection = buffer.get() & 0xff;
				}
				World.spawnNPC(npcId, new Position(x, y, plane), mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static final void addNPCSpawn(int npcId, int regionId,
                                          Position tile, int mapAreaNameHash,
                                          boolean canBeAttackFromOutOfArea, int faceDirection) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					"data/npcs/packedSpawns/" + regionId + ".ns", true));
			out.writeShort(npcId);
			out.writeByte(tile.getZ());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(faceDirection != 0);
			if (faceDirection != 0) {
				out.writeByte(faceDirection);
			}
			out.flush();
			out.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private NPCSpawns() {
	}
}
