package com.rs.game.world.entity.player.content.activities;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.TeleportManager;
import com.rs.utility.Utils;

public class ForbiddenCages {
	

	protected static Position cage = new Position(2472, 9678, 0);

	protected static Item[] helms = {new Item(13896), new Item(4753), new Item(10828), new Item(11665), new Item(20824), new Item(25022), new Item(1163), new Item(1152), new Item(1159), new Item(1161), new Item(1165), new Item(3486), new Item(3751), new Item(3753), new Item(3749), new Item(4745), new Item(10589), new Item(11664), new Item(14494), new Item(13920), new Item(20135), new Item(21736), new Item(21467)};
    protected static Item[] plates = {new Item(13887), new Item(4757), new Item(1127), new Item(8839), new Item(17259), new Item(11724), new Item(1115), new Item(1121), new Item(1123), new Item(1125), new Item(10348), new Item(13884), new Item(14479), new Item(14492), new Item(19413), new Item(20139), new Item(3140), new Item(13911), new Item(13958)};
    protected static Item[] pants = {new Item(13893), new Item(4759), new Item(1079), new Item(8840), new Item(16689), new Item(11726), new Item(1067), new Item(1069), new Item(1071), new Item(1073), new Item(1075), new Item(3483), new Item(6809), new Item(10332), new Item(10346), new Item(13890), new Item(14490), new Item(20143), new Item(21469), new Item(21474)};
    protected static Item[] boots = {new Item(11732), new Item(3105), new Item(2577), new Item(16359), new Item(16359), new Item(11728)};
    protected static Item[] gloves = {new Item(7462), new Item(13006), new Item(2491), new Item(22358), new Item(16293), new Item(25025)};
    protected static Item[] arrows = {new Item(9244, 100)};
    protected static Item[] inventory = {new Item(385, 10), new Item(15272, 10)};
    protected static Item[] weapons = {new Item(18349), new Item(4151), new Item(19784), new Item(4587), new Item(4726), new Item(14484), new Item(16951), new Item(16955), new Item(1277), new Item(1283), new Item(1305), new Item(1317), new Item(6818), new Item(7158), new Item(11696), new Item(11698), new Item(11700), new Item(11730), new Item(13899), new Item(16403), new Item(16909), new Item(22346), new Item(18353), new Item(22494), new Item(24474), new Item(22321), new Item(24433), new Item(11694)};
    protected static Item[] shields = {new Item(13734), new Item(20072), new Item(8850), new Item(13555), new Item(1201), new Item(8844), new Item(8845), new Item(8846), new Item(8847), new Item(8848), new Item(8849), new Item(17273), new Item(1171), new Item(1189), new Item(1191), new Item(1193), new Item(1195), new Item(1197), new Item(1199), new Item(1540), new Item(4224)};
    protected static Item[] capes = {new Item(6570), new Item(23639), new Item(6568)};
    protected static Item[] bows = { new Item(22348), new Item(9185), new Item(9183)};
    protected static Item[] potions = {new Item(3024, Utils.random(3)), new Item(23219, Utils.random(3)), new Item(6685, Utils.random(2))};
    
	public static Integer[] getNpcs = { 13476, 13477, 13478, 13479, 13480,
			13481 };

	public static void enterCage(Player player) {
		player.blockDrops = true;
		player.getInventory().reset();
		player.getEquipment().reset();
		player.setNextPosition(cage);
		player.setCustomSkull(7);
		player.setForceMultiArea(true);
		player.setCanPvp(true);
		provideGear(0, player);
	}

	public static void leaveCage(Player player, boolean logout) {
		player.getHintIconsManager().removeUnsavedHintIcon();
		player.getInventory().reset();
		player.getEquipment().reset();
		player.setForceMultiArea(false);
		player.setCanPvp(false);
		player.removeSkull();
		player.unlock();
		player.blockDrops = false;
		player.getControlerManager().removeControlerWithoutCheck();
		if (!logout)
			player.setNextPosition(TeleportManager.getLocation("cagelobby"));
		else
			player.setLocation(TeleportManager.getLocation("cagelobby"));
	}

	public static void provideGear(int stage, Player player) {
		if(player.isLocked() || player.cantWalk || player.isDead())
			return;
		player.getInventory().reset();
		player.getEquipment().reset();
		player.getInventory().addItemMoneyPouch(
				inventory[Utils.getRandom(inventory.length - 1)]);
		player.getInventory().addItemMoneyPouch(
				potions[Utils.getRandom(potions.length - 1)]);
		if (stage >= 0) {
			player.setCustomSkull(7);
			int random = Utils.random(100);
			if (random >=  60) {
				player.getEquipment()
				.getItems()
				.set(Equipment.SLOT_ARROWS,
						arrows[Utils.getRandom(arrows.length - 1)]);
				player.getEquipment()
				.getItems()
				.set(Equipment.SLOT_WEAPON,
						bows[Utils.getRandom(bows.length - 1)]);
				player.getInventory().addItemMoneyPouch(2444, 1);
			} else {
			player.getEquipment()
					.getItems()
					.set(Equipment.SLOT_WEAPON,
							weapons[Utils.getRandom(weapons.length - 1)]);
			}
		    player.getInventory().addItemMoneyPouch(2436, 1);
			player.getInventory().addItemMoneyPouch(2436, 1);
			player.getInventory().addItemMoneyPouch(2440, 1);
		}
		if (stage >= 1) {
			player.setCustomSkull(6);
			player.getEquipment()
					.getItems()
					.set(Equipment.SLOT_HAT,
							helms[Utils.getRandom(helms.length - 1)]);
		}
	    if (stage >= 2) {
			player.setCustomSkull(5);
			player.getEquipment()
					.getItems()
					.set(Equipment.SLOT_CHEST,
							plates[Utils.getRandom(plates.length - 1)]);
			player.getEquipment()
			.getItems()
			.set(Equipment.SLOT_CAPE,
					capes[Utils.getRandom(capes.length - 1)]);
		}
		if (stage >= 3) {
			player.setCustomSkull(4);
			player.getEquipment()
					.getItems()
					.set(Equipment.SLOT_LEGS,
							pants[Utils.getRandom(pants.length - 1)]);
		}
		if (stage >= 4) {
			player.setCustomSkull(3);
			player.getEquipment()
					.getItems()
					.set(Equipment.SLOT_FEET,
							boots[Utils.getRandom(boots.length - 1)]);
		}
		if (stage >= 5) {
			player.setCustomSkull(2);
			player.getEquipment()
					.getItems()
					.set(Equipment.SLOT_HANDS,
							gloves[Utils.getRandom(gloves.length - 1)]);
		}
		if (stage >= 6) {
			player.setCustomSkull(1);
			if (!Equipment.isTwoHandedWeapon(new Item(player.getEquipment()
					.getWeaponId())))
				player.getEquipment()
						.getItems()
						.set(Equipment.SLOT_SHIELD,
								shields[Utils.getRandom(shields.length - 1)]);
		}
		player.getEquipment().refresh(Equipment.SLOT_CHEST,
				Equipment.SLOT_LEGS, Equipment.SLOT_FEET,
				Equipment.SLOT_AMULET, Equipment.SLOT_SHIELD,
				Equipment.SLOT_WEAPON, Equipment.SLOT_HAT, Equipment.SLOT_RING,
				Equipment.SLOT_ARROWS, Equipment.SLOT_CAPE);
	}

	public static void sendKill(final Player loser, Player winner, int stage) {
		if (winner.cageStage++ > 6)
			winner.cageStage++;
	    //NPC npc = new CageNpc(49, winner.getLastWorldTile(), winner);
		//npc.setTarget(winner);
		//npc.setNextForceTalk(new ForceTalk("You're becoming to strong I must stop you "+winner.getDisplayName()));
		//winner.getHintIconsManager().addHintIcon(npc, 1, -1, false);
		winner.cagePoints ++;
		winner.sm("You recieve some reward points for your efforts. As of now you have "+winner.cagePoints);
		leaveCage(loser, false);
		provideGear(winner.cageStage, winner);
	}

}
