package com.rs.game.world.entity.player.content.skills.construction;

import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * 
 * @author Justin
 *
 */

public class Plants {
	
	public static int WOODENPLANK = 960; 
	public static int BRONZENAILS = 4819;
	public static int BAGGEDPLANT1 = 8431;
	public static int BAGGEDPLANT2 = 8433;
	public static int BAGGEDPLANT3 = 8435;
	public static int WATERCAN = 5340;

	public static void CheckPlant(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 1) {
			player.getPackets().sendGameMessage("You need a construction level of 1 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT1, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 1 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT1, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 2);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 99 && object.getY() == 97) {
				player.small1plant1 = 1;
				player.small1plantX1 = boundChuncks[0]*8 + 35;
				player.small1plantY1 = boundChuncks[1]*8 + 33;
				World.spawnObject(new WorldObject(13431, 10, object.getRotation(), player.small1plantX1, player.small1plantY1, 0), true);
				}
		}
	}
	
	public static void CheckSmallFern(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 6) {
			player.getPackets().sendGameMessage("You need a construction level of 6 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT2, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 2 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT2, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 7);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 99 && object.getY() == 97) {
				player.small1plant1 = 2;
				player.small1plantX1 = boundChuncks[0]*8 + 35;
				player.small1plantY1 = boundChuncks[1]*8 + 33;
				World.spawnObject(new WorldObject(13432, 10, object.getRotation(), player.small1plantX1, player.small1plantY1, 0), true);
				}
		}
	}
	public static void CheckFern(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 12) {
			player.getPackets().sendGameMessage("You need a construction level of 12 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT3, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 3 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT3, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 13);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 99 && object.getY() == 97) {
				player.small1plant1 = 3;
				player.small1plantX1 = boundChuncks[0]*8 + 35;
				player.small1plantY1 = boundChuncks[1]*8 + 33;
				World.spawnObject(new WorldObject(13433, 10, object.getRotation(), player.small1plantX1, player.small1plantY1, 0), true);
				}
		}
	}
	public static void CheckDockLeaf(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 1) {
			player.getPackets().sendGameMessage("You need a construction level of 1 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT1, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 1 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT1, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 2);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 100 && object.getY() == 101) {
				player.small2plant1 = 1;
				player.small2plantX1 = boundChuncks[0]*8 + 36;
				player.small2plantY1 = boundChuncks[1]*8 + 37;
				World.spawnObject(new WorldObject(13434, 10, object.getRotation(), player.small2plantX1, player.small2plantY1, 0), true);
				}
		}
	}
	public static void CheckThistle(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 6) {
			player.getPackets().sendGameMessage("You need a construction level of 6 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT2, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 2 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT2, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 7);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 100 && object.getY() == 101) {
				player.small2plant1 = 2;
				player.small2plantX1 = boundChuncks[0]*8 + 36;
				player.small2plantY1 = boundChuncks[1]*8 + 37;
				World.spawnObject(new WorldObject(13435, 10, object.getRotation(), player.small2plantX1, player.small2plantY1, 0), true);
				}
		}
	}
	public static void CheckReeds(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 12) {
			player.getPackets().sendGameMessage("You need a construction level of 12 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT3, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 3 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT3, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 13);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 100 && object.getY() == 101) {
				player.small2plant1 = 3;
				player.small2plantX1 = boundChuncks[0]*8 + 36;
				player.small2plantY1 = boundChuncks[1]*8 + 37;
				World.spawnObject(new WorldObject(13436, 10, object.getRotation(), player.small2plantX1, player.small2plantY1, 0), true);
				}
		}
	}
	public static void CheckBigFern(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 1) {
			player.getPackets().sendGameMessage("You need a construction level of 1 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT1, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 1 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT1, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 2);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 102 && object.getY() == 96) {
				player.big1plant1 = 1;
				player.big1plantX1 = boundChuncks[0]*8 + 38;
				player.big1plantY1 = boundChuncks[1]*8 + 32;
				World.spawnObject(new WorldObject(13425, 10, object.getRotation(), player.big1plantX1, player.big1plantY1, 0), true);
				}
		}
	}
	public static void CheckSmallBush(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 6) {
			player.getPackets().sendGameMessage("You need a construction level of 6 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT2, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 2 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT2, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 7);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 102 && object.getY() == 96) {
				player.big1plant1 = 2;
				player.big1plantX1 = boundChuncks[0]*8 + 38;
				player.big1plantY1 = boundChuncks[1]*8 + 32;
				World.spawnObject(new WorldObject(13426, 10, object.getRotation(), player.big1plantX1, player.big1plantY1, 0), true);
				}
		}
	}
	public static void CheckTallPlant(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 12) {
			player.getPackets().sendGameMessage("You need a construction level of 12 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT3, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 3 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT3, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 13);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 102 && object.getY() == 96) {
				player.big1plant1 = 3;
				player.big1plantX1 = boundChuncks[0]*8 + 38;
				player.big1plantY1 = boundChuncks[1]*8 + 32;
				World.spawnObject(new WorldObject(13427, 10, object.getRotation(), player.big1plantX1, player.big1plantY1, 0), true);
				}
		}
	}
	public static void CheckShortPlant(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 1) {
			player.getPackets().sendGameMessage("You need a construction level of 1 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT1, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 1 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT1, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 2);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 96 && object.getY() == 96) {
				player.big2plant1 = 1;
				player.big2plantX1 = boundChuncks[0]*8 + 32;
				player.big2plantY1 = boundChuncks[1]*8 + 32;
				World.spawnObject(new WorldObject(13428, 10, object.getRotation(), player.big2plantX1, player.big2plantY1, 0), true);
				}
		}
	}
	public static void CheckLargeLeafBush(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 6) {
			player.getPackets().sendGameMessage("You need a construction level of 6 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT2, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 2 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT2, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 7);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 96 && object.getY() == 96) {
				player.big2plant1 = 2;
				player.big2plantX1 = boundChuncks[0]*8 + 32;
				player.big2plantY1 = boundChuncks[1]*8 + 32;
				World.spawnObject(new WorldObject(13429, 10, object.getRotation(), player.big2plantX1, player.big2plantY1, 0), true);
				}
		}
	}
	public static void CheckHugePlant(Player player, final WorldObject object, final int[] boundChuncks) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 12) {
			player.getPackets().sendGameMessage("You need a construction level of 12 to plant this.");
		} else if (!player.getInventory().containsItem(BAGGEDPLANT3, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 bagged plant 3 to plant this.");
		} else if (!player.getInventory().containsItem(WATERCAN, 1)) {
			player.getPackets().sendGameMessage("You need a watering can to plant this plant.");
		} else {
			player.getInventory().deleteItem(BAGGEDPLANT3, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 13);
			player.animate(new Animation(2293));
			player.getPackets().sendGameMessage("You successfully plant a plant.");
			if (object.getX() == 96 && object.getY() == 96) {
				player.big2plant1 = 3;
				player.big2plantX1 = boundChuncks[0]*8 + 32;
				player.big2plantY1 = boundChuncks[1]*8 + 32;
				World.spawnObject(new WorldObject(13430, 10, object.getRotation(), player.big2plantX1, player.big2plantY1, 0), true);
				}
		}
	}
}