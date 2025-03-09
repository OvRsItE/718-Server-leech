package com.rs.utility;

import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.rs.game.world.entity.npc.Drop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class NPCDrops {

	private final static String PACKED_PATH = "data/npcs/packedDrops.d";
	private static final String DIRECTORY = "data/npcs/drops";
	private static HashMap<Integer, Drop[]> npcDrops = Maps.newHashMap();

	public static final void init() {
		loadPackedNPCDrops();
	}

	public static Drop[] getDrops(int npcId) {
		return npcDrops.get(npcId);
	}

	private Map<Integer, ArrayList<Drop>> dropMapx = null;

	public Map<Integer, ArrayList<Drop>> getDropArray() {

		if (dropMapx == null)
			dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		// dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		for (int i : npcDrops.keySet()) {
			int npcId = i;
			ArrayList<Drop> temp = new ArrayList<Drop>();
			for (Drop mainDrop : npcDrops.get(npcId)) {
				temp.add(mainDrop);
			}
			dropMapx.put(i, temp);
		}

		return dropMapx;
	}

	public void insertDrop(int npcID, Drop d) {
		loadPackedNPCDrops();
		Drop[] oldDrop = npcDrops.get(npcID);
		if (oldDrop == null) {
			npcDrops.put(npcID, new Drop[] { d });
		} else {
			int length = oldDrop.length;
			Drop destination[] = new Drop[length + 1];
			System.arraycopy(oldDrop, 0, destination, 0, length);
			destination[length] = d;
			npcDrops.put(npcID, destination);
		}
	}

	private static void loadPackedNPCDrops() {
		try {
//			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
//			FileChannel channel = in.getChannel();
//			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
//					channel.size());
//			int dropSize = buffer.getShort() & 0xffff;
//			npcDrops = new HashMap<>(dropSize);
//			for (int i = 0; i < dropSize; i++) {
//				int npcId = buffer.getShort() & 0xffff;
//				Drop[] drops = new Drop[buffer.getShort() & 0xffff];
//				for (int d = 0; d < drops.length; d++) {
//					if (buffer.get() == 0)
//						drops[d] = new Drop(buffer.getShort() & 0xffff,
//								buffer.getDouble(), buffer.getInt(),
//								buffer.getInt(), false);
//					else
//						drops[d] = new Drop(0, 0, 0, 0, true);
//
//				}
//				npcDrops.put(npcId, drops);
//			}
//			channel.close();
//			in.close();
			File directory = new File(DIRECTORY);
			if (directory.exists()) {
				for (File file : directory.listFiles()) {
					ItemDropTable table = GsonUtils.GSON.fromJson(new FileReader(file), ItemDropTable.class);
					List<Drop> drops = Lists.newArrayList();
					for (ItemDrop item : table.getGuaranteed()) {
						drops.add(new Drop(item.id(), item.rate(), item.min(), item.max(), false));
					}
					for (ItemDrop item : table.getDrops()) {
						drops.add(new Drop(item.id(), item.rate(), item.min(), item.max(), false));
					}
					npcDrops.put(Integer.parseInt(Utils.stripExtension(file.getName(), "json")), drops.toArray(new Drop[0]));
				}
			} else
				System.err.println("drop table directory doesnt exist at " + directory.getAbsolutePath());
			Logger.log("NPCDrops", "Loaded " + npcDrops.size() + " drop tables.");
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static void reload() {
		npcDrops.clear();
		loadPackedNPCDrops();
	}

	public static HashMap<Integer, Drop[]> getDropMap() {
		return npcDrops;
	}

	@Getter
	public static class ItemDropTable {
		private final List<StaticDropItem> guaranteed = Lists.newArrayList();
		private final List<VariableDropItem> drops = Lists.newArrayList();
	}

	protected interface ItemDrop {
		int id();
		int min();
		int max();
		double rate();
	}

	@RequiredArgsConstructor @Getter
	public static class StaticDropItem implements ItemDrop {

		private final int id;
		private final int min, max;

		@Override
		public int id() {
			return id;
		}

		@Override
		public int min() {
			return min;
		}

		@Override
		public int max() {
			return max;
		}

		@Override
		public double rate() {
			return 100;
		}

	}

	@RequiredArgsConstructor @Getter
	public static class VariableDropItem implements ItemDrop {

		private final int id;
		private final int min, max;
		private final double rate;

		@Override
		public int id() {
			return id;
		}

		@Override
		public int min() {
			return min;
		}

		@Override
		public int max() {
			return max;
		}

		@Override
		public double rate() {
			return rate;
		}

	}
}