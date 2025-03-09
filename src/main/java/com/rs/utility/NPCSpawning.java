package com.rs.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;

public class NPCSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {

		//Start of NEW HOME------- OBJECT DELETION-----


		World.deleteObject(new Position(1497, 5221, 0));
		World.deleteObject(new Position(3082, 3495, 0));
		World.deleteObject(new Position(3082, 3496, 0));
		World.deleteObject(new Position(1496, 5222, 0));
		World.deleteObject(new Position(1496, 5221, 0));
		World.deleteObject(new Position(1495, 5220, 0));
		World.deleteObject(new Position(1495, 5221, 0));
		World.deleteObject(new Position(1495, 5222, 0));
		World.deleteObject(new Position(1495, 5223, 0));
		World.deleteObject(new Position(1494, 5223, 0));
		World.deleteObject(new Position(1494, 5222, 0));
		World.deleteObject(new Position(1494, 5221, 0));
		World.deleteObject(new Position(1494, 5220, 0));
		World.deleteObject(new Position(1493, 5221, 0));
		World.deleteObject(new Position(1493, 5222, 0));
		World.deleteObject(new Position(1493, 5223, 0));
		World.deleteObject(new Position(1492, 5224, 0));
		World.deleteObject(new Position(1492, 5223, 0));
		World.deleteObject(new Position(1492, 5222, 0));
		World.deleteObject(new Position(1494, 5230, 0));
		World.deleteObject(new Position(1493, 5230, 0));
		World.deleteObject(new Position(1493, 5229, 0));
		World.deleteObject(new Position(1493, 5228, 0));
		World.deleteObject(new Position(1494, 5228, 0));
		World.deleteObject(new Position(1494, 5229, 0));
		World.deleteObject(new Position(1495, 5228, 0));
		World.deleteObject(new Position(1494, 5227, 0));
		World.deleteObject(new Position(1492, 5228, 0));
		World.deleteObject(new Position(1492, 5229, 0));
		World.deleteObject(new Position(1491, 5229, 0));
		World.deleteObject(new Position(1490, 5230, 0));
		World.deleteObject(new Position(1490, 5229, 0));
		World.deleteObject(new Position(1490, 5228, 0));
		World.deleteObject(new Position(1490, 5227, 0));
		World.deleteObject(new Position(1490, 5226, 0));
		World.deleteObject(new Position(1489, 5228, 0));
		World.deleteObject(new Position(1489, 5229, 0));
		World.deleteObject(new Position(1489, 5227, 0));
		World.deleteObject(new Position(1488, 5228, 0));
		World.deleteObject(new Position(1492, 5221, 0));
		World.deleteObject(new Position(1493, 5224, 0));
		World.deleteObject(new Position(1502, 5221, 0));
		World.deleteObject(new Position(1503, 5221, 0));
		World.deleteObject(new Position(1504, 5221, 0));
		World.deleteObject(new Position(1504, 5222, 0));
		World.deleteObject(new Position(1503, 5222, 0));
		World.deleteObject(new Position(1502, 5222, 0));
		World.deleteObject(new Position(1503, 5223, 0));
		World.deleteObject(new Position(1504, 5223, 0));
		World.deleteObject(new Position(1504, 5224, 0));
		World.deleteObject(new Position(1503, 5222, 0));
		World.deleteObject(new Position(1500, 5223, 0));
		World.deleteObject(new Position(1501, 5224, 0));
		World.deleteObject(new Position(1499, 5224, 0));
		World.deleteObject(new Position(1500, 5224, 0));
		World.deleteObject(new Position(1500, 5225, 0));
		World.deleteObject(new Position(1499, 5225, 0));
		World.deleteObject(new Position(1499, 5226, 0));
		World.deleteObject(new Position(1500, 5226, 0));
		World.deleteObject(new Position(1498, 5226, 0));
		World.deleteObject(new Position(1498, 5225, 0));
		World.deleteObject(new Position(1504, 5226, 0));
		World.deleteObject(new Position(1503, 5226, 0));
		World.deleteObject(new Position(1503, 5227, 0));
		World.deleteObject(new Position(1504, 5227, 0));
		World.deleteObject(new Position(1506, 5228, 0));
		World.deleteObject(new Position(1505, 5229, 0));
		World.deleteObject(new Position(1506, 5229, 0));
		World.deleteObject(new Position(1507, 5229, 0));
		World.deleteObject(new Position(1508, 5229, 0));
		World.deleteObject(new Position(1508, 5230, 0));
		World.deleteObject(new Position(1507, 5230, 0));
		World.deleteObject(new Position(1506, 5230, 0));
		World.deleteObject(new Position(1507, 5231, 0));
		World.deleteObject(new Position(1508, 5231, 0));
		World.deleteObject(new Position(1501, 5230, 0));
		World.deleteObject(new Position(1502, 5230, 0));
		World.deleteObject(new Position(1500, 5230, 0));
		World.deleteObject(new Position(1500, 5231, 0));
		World.deleteObject(new Position(1501, 5231, 0));
		World.deleteObject(new Position(1502, 5231, 0));
		World.deleteObject(new Position(1502, 5232, 0));
		World.deleteObject(new Position(1501, 5232, 0));
		World.deleteObject(new Position(1503, 5232, 0));
		World.deleteObject(new Position(1502, 5233, 0));
		World.deleteObject(new Position(1501, 5233, 0));
		World.deleteObject(new Position(1501, 5229, 0));
		World.deleteObject(new Position(1503, 5224, 0));
		World.deleteObject(new Position(1506, 5222, 0));
		World.deleteObject(new Position(1507, 5222, 0));
		World.deleteObject(new Position(1507, 5223, 0));
		World.deleteObject(new Position(1507, 5221, 0));
		World.deleteObject(new Position(1517, 5200, 0));
		World.deleteObject(new Position(1517, 5197, 0));
		World.deleteObject(new Position(1516, 5194, 0));
		World.deleteObject(new Position(1521, 5204, 0));
		World.deleteObject(new Position(1522, 5199, 0));
		World.deleteObject(new Position(1525, 5193, 0));
		World.deleteObject(new Position(1520, 5192, 0));
		World.deleteObject(new Position(1527, 5197, 0));
		World.deleteObject(new Position(1526, 5204, 0));
		World.deleteObject(new Position(1531, 5199, 0));
		World.deleteObject(new Position(1519, 5209, 0));
		World.deleteObject(new Position(1522, 5208, 0));
		World.deleteObject(new Position(1521, 5205, 0));
		World.deleteObject(new Position(1522, 5204, 0));
		World.deleteObject(new Position(1522, 5200, 0));
		World.deleteObject(new Position(1521, 5195, 0));
		World.deleteObject(new Position(1524, 5195, 0));
		World.deleteObject(new Position(1525, 5192, 0));
		World.deleteObject(new Position(1526, 5193, 0));
		World.deleteObject(new Position(1524, 5191, 0));
		World.deleteObject(new Position(1528, 5197, 0));
		World.deleteObject(new Position(1526, 5200, 0));
		World.deleteObject(new Position(1526, 5201, 0));
		World.deleteObject(new Position(1527, 5200, 0));
		World.deleteObject(new Position(1532, 5201, 0));
		World.deleteObject(new Position(1527, 5203, 0));
		World.deleteObject(new Position(1527, 5199, 0));
		World.deleteObject(new Position(1481, 5197, 0));
		World.deleteObject(new Position(1481, 5196, 0));
		World.deleteObject(new Position(1482, 5196, 0));
		World.deleteObject(new Position(1482, 5197, 0));
		World.deleteObject(new Position(1482, 5200, 0));
		World.deleteObject(new Position(1482, 5201, 0));
		World.deleteObject(new Position(1481, 5201, 0));
		World.deleteObject(new Position(1483, 5203, 0));
		World.deleteObject(new Position(1483, 5204, 0));
		World.deleteObject(new Position(1482, 5204, 0));
		World.deleteObject(new Position(1482, 5203, 0));
		World.deleteObject(new Position(1479, 5201, 0));
		World.deleteObject(new Position(1478, 5201, 0));
		World.deleteObject(new Position(1478, 5202, 0));
		World.deleteObject(new Position(1479, 5202, 0));
		World.deleteObject(new Position(1480, 5199, 0));
		World.deleteObject(new Position(1479, 5199, 0));
		World.deleteObject(new Position(1476, 5199, 0));
		World.deleteObject(new Position(1475, 5199, 0));
		World.deleteObject(new Position(1475, 5198, 0));
		World.deleteObject(new Position(1476, 5198, 0));
		World.deleteObject(new Position(1477, 5205, 0));
		World.deleteObject(new Position(1476, 5205, 0));
		World.deleteObject(new Position(1476, 5206, 0));
		World.deleteObject(new Position(1477, 5206, 0));
		World.deleteObject(new Position(3097, 3502, 0));
		World.deleteObject(new Position(1500, 5194, 0));
		World.deleteObject(new Position(1499, 5194, 0));
		World.deleteObject(new Position(1498, 5194, 0));
		World.deleteObject(new Position(3097, 3502, 0));

		World.deleteObject(new Position(3083, 3500, 0)); // Remove well at ege
		World.deleteObject(new Position(3084, 3500, 0));
		World.deleteObject(new Position(3084, 3501, 0));
		World.deleteObject(new Position(3083, 3501, 0));
		World.deleteObject(new Position(2772, 3688, 0));

		/**
		 * For the Adventurers Quest (Custom quest for RealityX #ONLY!)
		 */

		World.deleteObject(new Position(3164, 3475, 0));
		World.deleteObject(new Position(1499, 5201, 0));
		World.deleteObject(new Position(1498, 5202, 0));
		//lz bankers
		World.spawnNPC(44, new Position(2833, 3867, 3), 0, 0, false);
		World.spawnNPC(44, new Position(2832, 3867, 3), 0, 0, false);
		World.spawnNPC(44, new Position(2831, 3867, 3), 0, 0, false);
		//lz shops
		World.spawnNPC(11571, new Position(2834, 3850, 3), -4, 0, false);
		World.spawnNPC(11577, new Position(2833, 3850, 3), -4, 0, false);
		//Christmas event
		World.spawnNPC(1552, new Position(2604, 3108, 0), 3, 0, false);
		//dz shop
		World.spawnNPC(2144, new Position(2651, 10425, 0), -4, 0, false);
		//tokens @ dice
		//World.spawnNPC(6988, new WorldTile(2615, 3087, 0), -4, false);

		World.spawnNPC(11506, new Position(2592, 5605, 1), -4, 0, false);
		//World.spawnNPC(14620, new WorldTile(4623, 5453, 3), -4, true);
		// New home // @ Mudaa
		World.spawnNPC(1268, new Position(2708, 3723, 0), -4, 0, false); // Farming
		World.spawnNPC(6988, new Position(2609, 3098, 0), -4, 0, false); // Summoning
		//World.spawnNPC(303, new WorldTile(3081, 3487, 0), -4, false);
		// Supplies
		World.spawnNPC(528, new Position(2612, 3098, 0), -4, 0, false); // General Supplies
		// shop
		World.spawnNPC(4247, new Position(2541, 3097, 0), -4, 0, false);
		World.spawnNPC(4544, new Position(2598, 3102, 0), -4, 0, false);

		//World.spawnNPC(259, new WorldTile(2597, 3143, 0), -4, true);
		//World.spawnNPC(7552, new WorldTile(2613, 3147, 1), -4, true);
	}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/spawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
				.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
				.parseInt(spawn[3]), faceDir = Integer
				.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new Position(x, y, z), -1, 0, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					Position spawnLoc = new Position(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, 0, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}