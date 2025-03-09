package com.rs.game.world.entity.player.content;


import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.network.io.InputStream;
import com.rs.utility.Logger;
import com.rs.utility.Utils;


/**
 *
 * @author Muda
 *
 */



public class RessourceBox {

	public static boolean isBoxSpawned; // Checks if box is spawned.
	public static int randomLocation; // Random location int
	public static Item[] RESSOURCE_BOX = {new Item(15246)}; //Box item id
	static Position place1 = new Position(2567, 3307, 0); // Yanille
	static Position place2 = new Position(2543, 3108, 0); // Yanille - Near Multi PvP bridge (varrock)
	static Position place3 = new Position(2620, 3096, 1); // Yanille - Near Monastry Entrance
	static Position place4 = new Position(3205, 3379, 0); // Varrock - Entrance
	static Position place5 = new Position(3223, 3417, 0); // Varrock - Behind Genstore
	static Position place6 = new Position(3199, 3455, 0);	// Varrock - Castle
	static Position place7 = new Position(2893, 9834, 0); // Taverley Dungeon First Gate
	static Position place8 = new Position(2937, 9848, 0); // Taverley Dungeon Chaos Druids
	static Position place9 = new Position(2870, 9852, 0);	// Taverley Dungeon Hell hounds
	static Position place10 = new Position(2725, 3499, 0); // Camelot Behind bank

	public static Position[] places = {place1, place2, place3, place4,
		place5, place6, place7, place8,
		place9, place10
	};




	public static String boxLocation() {
		if (randomLocation == 10 || randomLocation == 20 || randomLocation == 30) {
			return "Home.";
		}
		if (randomLocation == 40 || randomLocation == 50 || randomLocation == 60) {
			return "Varrock";
		}
		if (randomLocation == 70 || randomLocation == 80 || randomLocation == 90) {
			return "Taverley Dungeon";
		}
		if (randomLocation == 90) {
			return "Camelot";
		}

		return "Nowhere.";

	}
	public static void spawnRessourceBox() {
		if (places[Utils.random(0, 10)] == place1) {
			randomLocation = 10;
			isBoxSpawned = true;

			World.sendWorldMessage("[<col=8B0000>Resource Box]</col>] - A Ressource Box have spawned somewhere at home!", false);
			World.addGroundItem(new Item(15246, 1),new Position(3078, 3516, 0));
		} else if (places[Utils.random(0, 10)] == place2) {
			//Near home entr from varrock
			randomLocation = 20;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere at home!", false);
			World.addGroundItem(new Item(15246, 1), new Position(3120, 3503, 0));
		} else if (places[Utils.random(0, 10)] == place3) {
			randomLocation = 30;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere at home!", false);
			World.addGroundItem(new Item(15246, 1), new Position(3057, 3498, 0));
		} else if (places[Utils.random(0, 10)] == place4) {
			randomLocation = 40;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere at Varrock!", false);
			World.addGroundItem(new Item(15246, 1), new Position(3205, 3379, 0));
		} else if (places[Utils.random(0, 10)] == place5) {
			randomLocation = 50;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere in Varrock!", false);
			World.addGroundItem(new Item(15246, 1), new Position(3223, 3417, 0));
		} else if (places[Utils.random(0, 10)] == place6) {
			randomLocation = 60;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere in Varrock!", false);
			World.addGroundItem(new Item(15246, 1), new Position(3199, 3455, 0));
		} else if (places[Utils.random(0, 10)] == place7) {
			randomLocation = 70;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere in the Taverley Dungeon!", false);
			World.addGroundItem(new Item(15246, 1), new Position(2893, 9834, 0));
		} else if (places[Utils.random(0, 10)] == place8) {
			randomLocation = 80;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere in the Taverley Dungeon!", false);
			World.addGroundItem(new Item(15246, 1), new Position(2937, 9848, 0));
		} else if (places[Utils.random(0, 10)] == place9) {
			randomLocation = 90;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere in the Taverley Dungeon!", false);
			World.addGroundItem(new Item(15246, 1), new Position(2870, 9852, 0));
		} else if (places[Utils.random(0, 10)] == place10) {
			randomLocation = 100;
			isBoxSpawned = true;
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - A Ressource Box have spawned somewhere in Camelot!", false);
			World.addGroundItem(new Item(15246, 1), new Position(2725, 3499, 0));
		}

	}

	public static void handleGroundPick(Player player, InputStream stream) {

		for (Item item : RESSOURCE_BOX) {
			int y = stream.readUnsignedShort();
			int x =  stream.readUnsignedShortLE();
			final Position tile = new Position(x, y, player.getZ());
			final FloorItem item1 = World.getRegion(player.getRegionId()).getGroundItem(item.getId(), tile,player);
			final FloorItem floorItem = World.getRegion(player.getRegionId()).getGroundItem(item.getId(), tile,player);
			if (floorItem == null) {
				return;
			}
			if (item1.getOwner() == player.getUsername()) {
				return;
			} else {
				World.removeGroundItem(player, floorItem);
				randomLocation = 0;
			}


			boolean hasSpace = player.getInventory().getFreeSlots() > 1; // Thanks trey< :3
			if (!hasSpace && isBoxSpawned && randomLocation > 0) {
				World.removeGroundItem(player, floorItem); // FIRS THING -> Delete the box from ground
				player.getBank().addItem(15270, 300, true);
				player.getBank().addItem(383, 300, true); // Rocks
				player.getBank().addItem(1513, 200, true);
				player.getBank().addItem(1515, 200, true);
				player.getBank().addItem(565, 250, true);
				player.getBank().addItem(11237, 200, true);
				player.getBank().addItem(44, 200, true);
				player.getBank().addItem(989, 3, true);
				player.getPackets().sendGameMessage("[<col=8B0000>You don't have enough space, the content have been banked.");
				World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - <col=8B0000>"+player.getDisplayName()+"</col>, just claimed the Ressource box content!", false);
				isBoxSpawned = false;
				randomLocation = 0;
			} else if(hasSpace && isBoxSpawned && randomLocation > 0) {
				World.removeGroundItem(player, floorItem);
			}
			player.getInventory().addItem(15271, 300); // Rocks
			player.getInventory().addItem(384, 300); // Sharks
			player.getInventory().addItem(1514, 200);// Magic logs
			player.getInventory().addItem(1516, 200);// Yew logs
			player.getInventory().addItem(565, 250);// Blood Runes
			player.getInventory().addItem(11237, 200); // Dragon arrowtips
			player.getInventory().addItem(44, 200); // Rune arrowtips
			player.getInventory().addItem(990, 3); // C Keys
			player.getInventory().deleteItem(15246, 1); // Make sure to delete BOX
			World.sendWorldMessage("[<col=8B0000>Resource Box</col>] - <col=8B0000>"+player.getDisplayName()+"</col>, just claimed the Ressource box content!", false);
			isBoxSpawned = false;
			randomLocation = 0;

		}

	}


	public static void SpawnPackage() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				try {
					if (isBoxSpawned && randomLocation > 0) {
						World.sendWorldMessage("<col=8B0000>[Ressource Box]</col> - The Ressource box have not been found yet!", false);
						return;
					} else {
						spawnRessourceBox();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 9000, TimeUnit.SECONDS); // 15 mins
	}

}


