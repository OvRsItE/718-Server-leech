package com.rs.game.world.entity.player.content.social.citadel;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.map.MapBuilder;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Logger;

public class Citadel {
	// Main slots
	// 494, 510 - golden mine
	// 502, 510 - golden cookspot
	// 510, 510 - golden smithing
	// 518, 510 - golden obelisk
	// 488, 510 - iron mine
	// 496, 510 - iron cookspot
	// 504, 510 - iron smithing
	// 512, 510 - iron obelisk
	// 558, 510 - golden night mine
	// 566, 510 - golden night cookspot
	// 574, 510 - golden night smithing
	// 582, 510 - golden night obelisk
	// 576, 510 - iron night obelisk
	// 568, 510 - iron night smithing`
	// 560, 510 - iron night cookspot
	// 552, 510 - iron night mine
	// 552, 506 - iron tree night
	// 488, 506 - iron tree day
	// 558, 506 - golden tree night
	// 494, 506 - golden tree day

	// Different slots
	// 618, 505 - golden tree - width would be 2 and height 1
	// 618, 511 - iron tree - width would be 2 and height 1

	// Slots on both sides of middle
	private static int EastSectionX = 87;
	private static int EastSectionY = 40;
	private static int WestSectionX = 47;
	private static int WestSectionY = 40;

	/* West side Slots */
	private static int WSlot1X = 23;
	private static int WSlot1Y = 48;

	private static int WSlot2X = 23;
	private static int WSlot2Y = 72;

	private static int WSlot3X = 39;
	private static int WSlot3Y = 72;

	private static int WSlot4X = 31;
	private static int WSlot4Y = 96;

	/* East side Slots */
	private static int ESlot1X = 111;
	private static int ESlot1Y = 48;

	private static int ESlot2X = 111;
	private static int ESlot2Y = 72;

	private static int ESlot3X = 95;
	private static int ESlot3Y = 72;

	private static int ESlot4X = 103;
	private static int ESlot4Y = 96;

	/* North slots normally for trees etc */
	private static int NSlot1X = 40;
	private static int NSlot1Y = 104;

	private static int NSlot2X = 72;
	private static int NSlot2Y = 104;

	public static void addAllDayPlots(Player player) {
		if (player.getEastTypeX() != 0) {
			int x = (player.boundChunks[0] << 3) + EastSectionX;
			int y = (player.boundChunks[1] << 3) + EastSectionY;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getEastTypeX(),
					player.getEastTypeY(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getWestTypeX() != 0) {
			int x = (player.boundChunks[0] << 3) + WestSectionX;
			int y = (player.boundChunks[1] << 3) + WestSectionY;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getWestTypeX(),
					player.getWestTypeY(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getWSlot1X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot1X;
			int y = (player.boundChunks[1] << 3) + WSlot1Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getWSlot1X(),
					player.getWSlot1Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getWSlot2X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot2X;
			int y = (player.boundChunks[1] << 3) + WSlot2Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getWSlot2X(),
					player.getWSlot2Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getWSlot3X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot3X;
			int y = (player.boundChunks[1] << 3) + WSlot3Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getWSlot3X(),
					player.getWSlot3Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getWSlot4X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot4X;
			int y = (player.boundChunks[1] << 3) + WSlot4Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getWSlot4X(),
					player.getWSlot4Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getESlot1X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot1X;
			int y = (player.boundChunks[1] << 3) + ESlot1Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getESlot1X(),
					player.getESlot1Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getESlot2X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot2X;
			int y = (player.boundChunks[1] << 3) + ESlot2Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getESlot2X(),
					player.getESlot2Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getESlot3X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot3X;
			int y = (player.boundChunks[1] << 3) + ESlot3Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getESlot3X(),
					player.getESlot3Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getESlot4X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot4X;
			int y = (player.boundChunks[1] << 3) + ESlot4Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getESlot4X(),
					player.getESlot4Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNSlot1X() != 0) {
			int x = (player.boundChunks[0] << 3) + NSlot1X;
			int y = (player.boundChunks[1] << 3) + NSlot1Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNSlot1X(),
					player.getNSlot1Y(), getChunkX, getChunkY, 2, 1, player);
		}
		if (player.getNSlot2X() != 0) {
			int x = (player.boundChunks[0] << 3) + NSlot2X;
			int y = (player.boundChunks[1] << 3) + NSlot2Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNSlot2X(),
					player.getNSlot2Y(), getChunkX, getChunkY, 2, 1, player);
		}
		player.setForceNextMapLoadRefresh(true);
		player.loadMapRegions();
	}
	public static void addAllNightPlots(Player player) {
		if (player.getEastTypeX() != 0) {
			int x = (player.boundChunks[0] << 3) + EastSectionX;
			int y = (player.boundChunks[1] << 3) + EastSectionY;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getEastNightTypeX(),
					player.getEastTypeY(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getWestTypeX() != 0) {
			int x = (player.boundChunks[0] << 3) + WestSectionX;
			int y = (player.boundChunks[1] << 3) + WestSectionY;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getWestNightTypeX(),
					player.getWestTypeY(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNWSlot1X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot1X;
			int y = (player.boundChunks[1] << 3) + WSlot1Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNWSlot1X(),
					player.getNWSlot1Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNWSlot2X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot2X;
			int y = (player.boundChunks[1] << 3) + WSlot2Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNWSlot2X(),
					player.getNWSlot2Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNWSlot3X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot3X;
			int y = (player.boundChunks[1] << 3) + WSlot3Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNWSlot3X(),
					player.getNWSlot3Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNWSlot4X() != 0) {
			int x = (player.boundChunks[0] << 3) + WSlot4X;
			int y = (player.boundChunks[1] << 3) + WSlot4Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNWSlot4X(),
					player.getNWSlot4Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNESlot1X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot1X;
			int y = (player.boundChunks[1] << 3) + ESlot1Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNESlot1X(),
					player.getNESlot1Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNESlot2X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot2X;
			int y = (player.boundChunks[1] << 3) + ESlot2Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNESlot2X(),
					player.getNESlot2Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNESlot3X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot3X;
			int y = (player.boundChunks[1] << 3) + ESlot3Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNESlot3X(),
					player.getNESlot3Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNESlot4X() != 0) {
			int x = (player.boundChunks[0] << 3) + ESlot4X;
			int y = (player.boundChunks[1] << 3) + ESlot4Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNESlot4X(),
					player.getNESlot4Y(), getChunkX, getChunkY, 1, 2, player);
		}
		if (player.getNNSlot1X() != 0) {
			int x = (player.boundChunks[0] << 3) + NSlot1X;
			int y = (player.boundChunks[1] << 3) + NSlot1Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNNSlot1X(),
					player.getNNSlot1Y(), getChunkX, getChunkY, 2, 1, player);
		}
		if (player.getNNSlot2X() != 0) {
			int x = (player.boundChunks[0] << 3) + NSlot2X;
			int y = (player.boundChunks[1] << 3) + NSlot2Y;
			int getChunkX = (x >> 3);
			int getChunkY = (y >> 3);
			MapBuilder.copyAllPlanesMap(player.getNNSlot2X(),
					player.getNNSlot2Y(), getChunkX, getChunkY, 2, 1, player);
		}
		player.setForceNextMapLoadRefresh(true);
		player.loadMapRegions();
	}
	public static void addNpcs(Player player) {
		Citadel cit = new Citadel();
		cit.npcs.add(new NPC(13932, getWorldTile(11, 12, player), -1, false));
		cit.npcs.add(new NPC(494, getWorldTile(67, 43, player), -1, false));
		cit.npcs.add(new NPC(520, getWorldTile(63, 40, player), -1, false));
		cit.npcs.add(new NPC(9713, getWorldTile(112, 4, player), -1, false));
		for (NPC npc : cit.npcs)
			World.spawnNPC(npc);
	}

	public static void addSigns(Player player) {
		int x = (player.boundChunks[0] << 3);
		int y = (player.boundChunks[1] << 3);
		Citadel cit = new Citadel();
		cit.signs.add(new WorldObject(31297, 10, 1, x + 78, y + 54, 0));// east
		cit.signs.add(new WorldObject(31297, 10, 3, x + 49, y + 41, 0));// west
		if (player.getCitTypeY() == 512) {
			cit.signs.add(new WorldObject(31297, 10, 1, x + 86, y + 73, 0));// east3
			cit.signs.add(new WorldObject(31297, 10, 1, x + 94, y + 97, 0));// east
																			// 4
			cit.signs.add(new WorldObject(31297, 10, 1, x + 102, y + 73, 0));// east2
			cit.signs.add(new WorldObject(31297, 10, 1, x + 102, y + 49, 0));// east1
			cit.signs.add(new WorldObject(31297, 10, 4, x + 73, y + 102, 0));// nSlot2
			cit.signs.add(new WorldObject(31297, 10, 4, x + 41, y + 102, 0));// nSlot1
			cit.signs.add(new WorldObject(31297, 10, 3, x + 25, y + 62, 0));// west1
			cit.signs.add(new WorldObject(31297, 10, 3, x + 25, y + 73, 0));// west2
			cit.signs.add(new WorldObject(31297, 10, 5, x + 30, y + 73, 0));// west3
			cit.signs.add(new WorldObject(31297, 10, 3, x + 33, y + 97, 0));// west4
		}
		for (WorldObject object : cit.signs)
			World.spawnObject(object, true);
	}

	public static void createBasicCitadel(Player player) {
		player.boundChunks = MapBuilder.findEmptyChunkBound(100, 100);
		// 488, 592 - iron citadel
		// 488, 512 - golden citadel
		final Calendar c = Calendar.getInstance();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("US/Central"));
		c.setTimeZone(now.getTimeZone());
		if (c.get(Calendar.HOUR) >= 8 && c.get(Calendar.AM_PM) == Calendar.PM
				|| c.get(Calendar.HOUR) >= 1 && c.get(Calendar.HOUR) < 6
				&& c.get(Calendar.AM_PM) == Calendar.AM) {
			MapBuilder.copyAllPlanesMap(player.getCitNightTypeX(),
					player.getCitNightTypeY(), player.boundChunks[0],
					player.boundChunks[1], 16, 16, player);
			addAllNightPlots(player);
		} else {
			MapBuilder.copyAllPlanesMap(player.getCitTypeX(),
					player.getCitTypeY(), player.boundChunks[0],
					player.boundChunks[1], 16, 16, player);
			addAllDayPlots(player);
		}
		player.setNextPosition(new Position(getWorldTile(113, 10, player)));
		addSigns(player);
		addNpcs(player);
	}

	public static void createBattleField(Player player) {
		player.boundChunks2 = MapBuilder.findEmptyChunkBound(16, 16);
		MapBuilder.copyAllPlanesMap(600, 480, player.boundChunks2[0],
				player.boundChunks2[1], 16, 16, player);
	}

	// basic must be created
	public static void createDayCitadel(Player player) {
		int x = (player.boundChunks[0] << 3) + 0;
		int y = (player.boundChunks[1] << 3) + 4;
		int getChunkX = (x >> 3);
		int getChunkY = (y >> 3);
		MapBuilder.copyAllPlanesMap(player.getCitTypeX(),
				player.getCitTypeY(), getChunkX, getChunkY, 16, 16, player);
		addAllDayPlots(player);
		addSigns(player);
	}

	// basic must be created
	public static void createNightCitadel(Player player) {
		int x = (player.boundChunks[0] << 3) + 0;
		int y = (player.boundChunks[1] << 3) + 4;
		int getChunkX = (x >> 3);
		int getChunkY = (y >> 3);
		// night golden 504, 512
		MapBuilder
				.copyAllPlanesMap(player.getCitNightTypeX(),
						player.getCitNightTypeY(), getChunkX, getChunkY, 16,
						16, player);
		addAllNightPlots(player);
		addSigns(player);
	}

	public static void createTestCitadel(Player player) {
		player.boundChunks = MapBuilder.findEmptyChunkBound(16, 16);
		// 488, 592 - iron citadel
		// 488, 512 - golden citadel
		final Calendar c = Calendar.getInstance();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("US/Central"));
		c.setTimeZone(now.getTimeZone());
		if (c.get(Calendar.HOUR) >= 8 && c.get(Calendar.AM_PM) == Calendar.PM
				|| c.get(Calendar.HOUR) >= 1 && c.get(Calendar.HOUR) < 6
				&& c.get(Calendar.AM_PM) == Calendar.AM) {
			MapBuilder.copyAllPlanesMap(player.getCitNightTypeX(),
					player.getCitNightTypeY(), player.boundChunks[0],
					player.boundChunks[1], 16, 16);
			// addAllNightPlots(player);
		} else {
			MapBuilder.copyAllPlanesMap(player.getCitTypeX(),
					player.getCitTypeY(), player.boundChunks[0],
					player.boundChunks[1], 16, 16, player);
			// addAllDayPlots(player);
		}
		player.setNextPosition(new Position(getWorldTile(113, 10, player)));
		addSigns(player);
	}

	public static void deleteBattleField(Player player) {
		MapBuilder.destroyMap(player.boundChunks2[0],
				player.boundChunks2[1], 16, 16);
	}

	public static void enterBattleField(Player player, Player owner) {
		player.setNextPosition(new Position(getBWorldTile(31, 6, owner)));
		player.setCanPvp(true);
		player.inBattleField = true;

	}

	public static Position getBWorldTile(int mapX, int mapY, Player player) {
		return new Position((player.boundChunks2[0] << 3) + mapX,
				(player.boundChunks2[1] << 3) + mapY, 1);
	}

	public static Position getWorldTile(int mapX, int mapY, Player player) {
		return new Position((player.boundChunks[0] << 3) + mapX,
				(player.boundChunks[1] << 3) + mapY, 0);
	}

	public static void removeMap(final Player player) {
		player.citadelOpen = false;
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					MapBuilder.destroyMap(player.boundChunks[0],
							player.boundChunks[1], 16, 16);
					if (player.citadelBattleGround) {
						player.citadelBattleGround = false;
						Citadel.deleteBattleField(player);
					}
					Citadel cit = new Citadel();
					for (WorldObject object : cit.signs)
						World.destroySpawnedObject(object, false);
					for (WorldObject object : cit.removeObjects)
						World.destroySpawnedObject(object, false);
					for (NPC npc : cit.npcs)
						npc.finish();
					cit.npcs.clear();
					cit.signs.clear();
					cit.removeObjects.clear();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 5000);
	}

	public final LinkedList<WorldObject> removeObjects = new LinkedList<WorldObject>();

	private final LinkedList<WorldObject> signs = new LinkedList<WorldObject>();

	private final LinkedList<NPC> npcs = new LinkedList<NPC>();

	public Citadel() {

	}
}
