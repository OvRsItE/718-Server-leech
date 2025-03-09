package com.rs.network.decoders.handlers;

import com.rs.Constants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.ObjectAction;
import com.rs.game.map.OwnedObjectManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.npc.instances.cerberus.CerberusInstance;
import com.rs.game.world.entity.npc.instances.skotizo.SkotizoInstance;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathInstance;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahInstance;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.CoordsEvent;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.QuestManager.Quests;
import com.rs.game.world.entity.player.actions.BoxAction.HunterEquipment;
import com.rs.game.world.entity.player.actions.BoxAction.HunterNPC;
import com.rs.game.world.entity.player.actions.CowMilkingAction;
import com.rs.game.world.entity.player.actions.LavaFlowMine;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.actions.objects.*;
import com.rs.game.world.entity.player.actions.objects.CrystalChest;
import com.rs.game.world.entity.player.actions.skilling.Bonfire;
import com.rs.game.world.entity.player.actions.skilling.Cooking;
import com.rs.game.world.entity.player.actions.skilling.Cooking.Cookables;
import com.rs.game.world.entity.player.actions.skilling.Smelting.SmeltingBar;
import com.rs.game.world.entity.player.actions.skilling.Smithing.ForgingBar;
import com.rs.game.world.entity.player.actions.skilling.Smithing.ForgingInterface;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.actions.skilling.Woodcutting;
import com.rs.game.world.entity.player.actions.skilling.Woodcutting.TreeDefinitions;
import com.rs.game.world.entity.player.actions.skilling.crafting.FlaxCrafting;
import com.rs.game.world.entity.player.actions.skilling.crafting.FlaxCrafting.Orb;
import com.rs.game.world.entity.player.actions.skilling.farming.Farming;
import com.rs.game.world.entity.player.actions.skilling.mining.EssenceMining;
import com.rs.game.world.entity.player.actions.skilling.mining.EssenceMining.EssenceDefinitions;
import com.rs.game.world.entity.player.actions.skilling.mining.Mining;
import com.rs.game.world.entity.player.actions.skilling.mining.Mining.RockDefinitions;
import com.rs.game.world.entity.player.actions.skilling.mining.MiningBase;
import com.rs.game.world.entity.player.actions.skilling.mining.StarMining;
import com.rs.game.world.entity.player.actions.skilling.runecrafting.SihponActionNodes;
import com.rs.game.world.entity.player.actions.skilling.thieving.RoguesChests;
import com.rs.game.world.entity.player.actions.skilling.thieving.SafeCracking;
import com.rs.game.world.entity.player.actions.skilling.thieving.Thieving;
import com.rs.game.world.entity.player.content.*;
import com.rs.game.world.entity.player.content.BonesOnAltar.Bones;
import com.rs.game.world.entity.player.content.RepairItems.BrokenItems;
import com.rs.game.world.entity.player.content.ShootingStars.StarStages;
import com.rs.game.world.entity.player.content.activities.*;
import com.rs.game.world.entity.player.content.chest.BrimstoneChest;
import com.rs.game.world.entity.player.content.chest.LarranChest;
import com.rs.game.world.entity.player.content.chest.ReaperChest;
import com.rs.game.world.entity.player.content.chest.ToxicChest;
import com.rs.game.world.entity.player.content.collection.ItemCollectionManager;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.dialogue.impl.ConBench;
import com.rs.game.world.entity.player.content.dialogue.impl.LeatherCraftingD;
import com.rs.game.world.entity.player.content.dialogue.impl.MiningGuildDwarf;
import com.rs.game.world.entity.player.content.newclues.CluesManager;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.agility.*;
import com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.puzzles.FishingFerretRoom;
import com.rs.game.world.entity.player.content.skills.farming.CompostBin;
import com.rs.game.world.entity.player.content.skills.farming.PatchConstants;
import com.rs.game.world.entity.player.content.skills.farming.PatchConstants.WorldPatches;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.controller.impl.*;
import com.rs.game.world.entity.player.controller.impl.GodWars;
import com.rs.game.world.entity.player.controller.instancing.TheAlchemistInstance;
import com.rs.game.world.entity.player.cutscenes.impl.MerryChristmas;
import com.rs.game.world.entity.updating.impl.*;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.network.io.InputStream;
import com.rs.utility.Logger;
import com.rs.utility.Misc;
import com.rs.utility.ShopsHandler;
import com.rs.utility.Utils;

import java.util.Arrays;
import java.util.TimerTask;

import static com.rs.game.world.entity.player.content.xeric.dungeon.skilling.CoxHerblore.EMPTY_GOURD_VIAL;
import static com.rs.game.world.entity.player.content.xeric.dungeon.skilling.CoxHerblore.WATER_FILLED_GOURD_VIAL;

public final class ObjectHandler {

	public static String gameMode = "Normal";

	private ObjectHandler() {

	}

	public static void teleportPlayer(Player player, int x, int y, int z) {
		player.setNextPosition(new Position(x, y, z));
		player.stopAll();
	}

	public static void handleOption(final Player player, InputStream stream, int option) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || !player.finishedStarter || player.isLocked()) {
			return;
		}
		if (player.getEmotesManager().getNextEmoteEnd() >= Utils.currentTimeMillis() || player.isLocked()) {
			return;
		}
		boolean forceRun = stream.readUnsignedByte128() == 1;
		final int id = stream.readIntLE();
		int x = stream.readUnsignedShortLE();
		int y = stream.readUnsignedShortLE128();
		int rotation = 0;
		if (player.isAtDynamicRegion()) {
			rotation = World.getRotation(player.getZ(), x, y);
			if (rotation == 1) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
				y += defs.getSizeY() - 1;
			} else if (rotation == 2) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
				x += defs.getSizeY() - 1;
			}
		}
		final Position tile = new Position(x, y, player.getZ());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId)) {
			return;
		}
		WorldObject mapObject = World.getObjectWithId(tile, id);
		if (mapObject == null || mapObject.getId() != id) {
			return;
		}
		if (player.isAtDynamicRegion() && World.getRotation(player.getZ(), x, y) != 0) { // temp fix
			ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
			if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
				for (int xs = 0; xs < defs.getSizeX() + 1 && (mapObject == null || mapObject.getId() != id); xs++) {
					for (int ys = 0; ys < defs.getSizeY() + 1 && (mapObject == null || mapObject.getId() != id); ys++) {
						tile.setLocation(x + xs, y + ys, tile.getZ());
						mapObject = World.getObjectWithId(tile, id);
					}
				}
			}
			if (mapObject == null || mapObject.getId() != id) {
				return;
			}
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(), mapObject.getRotation() + rotation % 4, x, y,
						player.getZ());
		object.actions = mapObject.actions; //temp fix
		object.attributes = mapObject.attributes; //temp fix
		player.stopAll(false);
		if (forceRun) {
			player.setRun(forceRun);
		}
		switch (option) {
		case 1:
			handleOption1(player, object);
			break;
		case 2:
			handleOption2(player, object);
			break;
		case 3:
			handleOption3(player, object);
			break;
		case 4:
			handleOption4(player, object);
			break;
		case 5:
			handleOption5(player, object);
			break;
		case -1:
			handleOptionExamine(player, object);
			break;
		}
	}

	private static void handleOption1(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		final int x = object.getX();
		final int y = object.getY();
		int destX = player.getX();
		int destY = player.getY();
		if (id == 43526) {
			BarbarianOutpostAgility.swingOnRopeSwing(player, object);
		}
		if (id == 69514) {
			player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
				@Override
				public void run() {
					player.faceObject(object);
					if (id == 69514) {
						GnomeAgility.runGnomeBoard(player, object);
					}
				}
			}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
			return;
		}
		if (id >= 4550 && id <= 4559) {
			player.faceObject(object);
			if (player.withinDistance(object, 2)) {
				if (!Agility.hasLevel(player, 35))
					return;
				int direction = (player.getX() < object.getX() ? ForceMovement.EAST : (player.getX() > object.getX() ? ForceMovement.WEST
						: (player.getY() < object.getY() ? ForceMovement.NORTH : ForceMovement.SOUTH)));
				player.setNextForceMovement(new ForceMovement(player, 1, object, 2, direction));
				player.useStairs(-1, object, 1, 2);
				player.animate(new Animation(769));
			}
			return;
		}
		if (id == 66601 || id == 66599) {
			if (player.atWarriorsGuild) {
				player.setNextPosition(new Position(2846, 3535, 2));
				player.atWarriorsGuild = false;
				return;
			}
			if (player.getX() == 2846) {
				if (!player.getInventory().containsItem(8851, 200)) {
					player.getDialogueManager().startDialogue("Kamfreena");
					return;
				}
			} else {
				player.getDialogueManager().startDialogue("Kamfreena");
				return;
			}

		}
		if (id == 50552) {
			player.animate(828);
			player.setNextPosition(new Position(3454, 3725, 0));
		}
		if (id == 37454 || id == 5268) {
			player.sm("You cannot leave this place.");
			return;
		}
		if (id == 70356) {
			if (player.withinDistance(new Position(3623, 2628, 0), 2) && player.getEquipment().getHatId() == 7534 && player.getEquipment().getCapeId() == 7535 || player.getBuffManager().hasBuff(BuffManager.BuffType.UNDERWATERPOTION)) {
					player.animate(3417);
					player.setNextPosition(new Position(2964, 9478, 1));
				} else {
				player.sm("You need some diving equipment to enter.");
			}
			}

		if (id == 6847) {
			player.getCutscenesManager().play(new MerryChristmas());
			return;
		}
		if (id == 21167) {
			player.sm("You cannot leave this place.");
			return;
		}
//		if (id == 79 && player.AdventurerQuestProgress == 8) {
//			long currentTime = Utils.currentTimeMillis();
//
//			if (player.getAttackedByDelay() + 3000 > currentTime) {
//				player.sm("You must be out of combat for 3 seconds to pick the lock.");
//				return;
//			}
//			player.unLocking = true;
//			player.sm("You attempt to unlock the door..");
//			player.faceObject(object);
//			;
//			player.animate(new Animation(2246));
//
//			int random = Utils.getRandom(25);
//
//			if (random == 3) {
//				player.setNextPosition(new Position(2600, 3142, 0));
//				// KharzardLures.handleProgressQuest(player);
//				player.unlockDoor = true;
//				player.sm("You succesfully open the door..");
//				player.unLocking = false;
//				return;
//			}
//			player.sm("Its pretty tight, i must keep trying!");
//			player.unLocking = false;
//			for (NPC npc : World.getNPCs()) {
//				if (npc.getId() == 7528) {
//					npc.setNextForceTalk(new ForceTalk("HES PICKING THE LOCK!!!"));
//					npc.setTarget(player);
//				}
//			}
//		}
//		if (id == 79 && player.AdventurerQuestProgress == 7) {
//			player.sm("I should probably tell Bert before helping Angor.");
//			player.faceObject(object);
//		}
		if (id == 43529 && destX >= 2484 && destY >= 3417 && destX <= 2487 && destY <= 3422 && player.getZ() == 3) {
			GnomeAgility.PreSwing(player, object);
		}
		if (SihponActionNodes.siphon(player, object)) {
			return;
		}
		if (ResourceDungeons.handleObjects(player, id)) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick1(object)) {
					return;
				}
				ObjectAction action = null;
				ObjectAction[] actions = object.actions;
				if (actions != null) {
					action = actions[0];
				}
				if (action == null && (actions = objectDef.actions) != null) {
					action = actions[0];
				}
				if (action != null) {
					action.handle(player, object);
					return;
				}
				if (CastleWars.handleObjects(player, id)) {
					return;
				}
				if (WorldObject.is(29777, true, object)) {
					if (player.getCurrentFriendChat() != null && player.getCurrentFriendChat().getXeric() != null)
						player.getCurrentFriendChat().getXeric().enterDungeon(player);
					else
						player.sendMessage("You must be in a friends chat to go on a raid.");
				}
				if (object.getId() == 19205) {
					Hunter.createLoggedObject(player, object, true);
				}
				if (object.getId() == WorldObject.asOSRS(27044)) {
					player.getDialogueManager().startDialogue("TheOverseer");
				}
				if (object.getId() == WorldObject.asOSRS(27029)) {

				}
				HunterNPC hunterNpc = HunterNPC.forObjectId(id);
				if (id == 2563) {
					if (player.isCompletionist == 0) {
						player.sendMessage("You wonder if you are worthy enough to get this cape.");
						CompletionistCape.CheckCompletionist(player);
						player.lock(2);
					}
				}
				if (id == 55328) {
//					if (player.getInventory().containsItem(29477, 1)) {
//						player.animate(881);
//						player.getInventory().deleteItem(29477, 1);
//						player.getInventory().addItem(29484, 1);
//					} else {
//						player.sm("You need a infernal key to open this chest");
//					}
					ToxicChest.open(player);
				}
				if (id == WorldObject.asOSRS(34660)) {
					BrimstoneChest.open(player);
				}
				if (id == WorldObject.asOSRS(34829)) {
					LarranChest.open(player);
				}
				if (id == 70435) {
					ReaperChest.open(player);
				}
				// events chest
				if (id == 61300) {
					if (player.getInventory().containsItem(29468, 1)) {
						int[][] common = {{29110, 1}, {15273, 100}, {11212, 300}, {11230, 150}, {29112, 3}, {23400, 90}, {23610, 30}, {537, 50}, {1128, 10}, {9194, 60}, {990, 5}, {2364, 60}, {1514, 200}, {1516, 400}, {452, 199}, {1392, 150}, {1248, 10},                                                  };
						int[][] uncommon = {{29110, 1}, {15273, 100}, {11212, 300}, {11230, 150}, {29112, 3}, {23400, 90}, {23610, 30}, {537, 50}, {1128, 10}, {9194, 60}, {990, 5}, {2364, 60}, {1514, 200}, {1516, 400}, {452, 199}, {1392, 150}, {1248, 10},};
						int[][] rare = {{6199, 1}, {18351, 1}, {18349, 1}, {18353, 1}, {18357, 1}, {18355, 1}, {18335, 1}, {18365, 1}, {18367, 1},  {18369, 1}, {11732, 1}, {22361, 1}, {22361, 1}, {22365, 1}, {22369, 1}, {29106, 1},  {29472, 1},       };
						int[][] super_rare = {{28831, 1}, {28825, 1},};
						player.getInventory().deleteItem(29468, 1);
						int rarity = Utils.getRandom(1000);
						if (rarity > 0 && rarity <= 500) {
							int length = common.length;
							length--;
							int reward = Utils.getRandom(length);
							int amount = common[reward][1];;
							String reward2 = ItemDefinitions.getItemDefinitions(common[reward][0]).getName().toLowerCase();
							player.sm("You receive "   + reward2 +  " x"  + amount );
							player.getInventory().addItemDrop(common[reward][0], common[reward][1]);

						}
						if (rarity > 500 && rarity <= 800) {
							int length = uncommon.length;
							length--;
							int reward = Utils.getRandom(length);
							int amount = uncommon[reward][1];;
							player.getInventory().addItemDrop(uncommon[reward][0], uncommon[reward][1]);
							String reward2 = ItemDefinitions.getItemDefinitions(uncommon[reward][0]).getName().toLowerCase();
							player.sm("You receive "   + reward2 +  " x"  + amount );


						}
						if (rarity > 800 && rarity <= 920) {
							int length = rare.length;
							length--;
							int reward = Utils.getRandom(length);
							int amount = rare[reward][1];
							player.getInventory().addItemDrop(rare[reward][0], rare[reward][1]);
							String reward2 = ItemDefinitions.getItemDefinitions(rare[reward][0]).getName().toLowerCase();
							World.sendWorldMessage(
									"<img=19><col=f91500>" + player.getDisplayName() + "<col=f91500> Received<col=f91500> Rare x"
											+ amount + " " + reward2 + "<col=f91500> from a events chest!<img=19>",
									false);
						}
						if (rarity > 920 && rarity <= 998) {
						player.getInventory().addItem(29468, 1);
						}
						if (rarity > 998 && rarity <= 1000) {
							int length = super_rare.length;
							length--;
							int reward = Utils.getRandom(length);
							int amount = super_rare[reward][1];
							player.getInventory().addItemDrop(super_rare[reward][0], super_rare[reward][1]);
							String reward2 = ItemDefinitions.getItemDefinitions(super_rare[reward][0]).getName().toLowerCase();
							player.setNextGraphics(new Graphics(1765));
							player.animate(new Animation(4939));
							World.sendWorldMessage(
									"<img=19><img=19><img=19><col=f91500>" + player.getDisplayName() + "<col=f91500> Received<col=f91500> UltraRare x"
											+ amount + " " + reward2 + "<col=f91500> from a Events chest!<img=19><img=19><img=19>",
									false);
						}
					} else {
					player.sm("You need a event chest key to open this chest");
					}
					}
				if (id == WorldObject.asOSRS(31989)) {
					if (player.getY() < 3700) {
						player.setNextPosition(new Position(2277, 4034));
					} else {
						player.setNextPosition(new Position(2640, 3696));
					}
					return;
				}
				if (id == WorldObject.asOSRS(26757)) { //Rouge's chests
					RoguesChests.openChest(player, object);
					return;
				}
				if (id == 2114) {
					player.getCoalTrucksManager().removeCoal();
					return;
				}
				if (id == WorldObject.asOSRS(27785)) { //Korend dungeon entrance
					player.lock();
					FadingScreen.fade(player, () -> {
						player.setNextPosition(new Position(1665, 10050, 0));
						player.unlock();
					});
					return;
				}
				if (id == 65386) { //Wilderness gates
					handleDoor(player, object);
					return;
				}
				if (id == 14233) {
					handleDoor(player, object);
				}
				if (id == WorldObject.asOSRS(32132)) { //Rune & addy dragons door
					Magic.sendNormalTeleportSpell(player, 0, 0, Constants.HOME_PLAYER_LOCATION[0]);
					return;
				}
				if (id == WorldObject.asOSRS(32153)) { //Rune & addy dragons barriers
					if (player.getX() == 1573) {
						player.addWalkSteps(player.getX() + 2, player.getY(), 3, false);
					} else if (player.getX() == 1575) {
						player.addWalkSteps(player.getX() - 2, player.getY(), 3, false);
					} else if (player.getX() == 1562) {
						player.addWalkSteps(player.getX() - 2, player.getY(), 3, false);
					} else if (player.getX() == 1560) {
						player.addWalkSteps(player.getX() + 2, player.getY(), 3, false);
					} else if (player.getY() == 5087) {
						player.addWalkSteps(player.getX(), player.getY() + 2, 3, false);
					} else if (player.getY() == 5089) {
						player.addWalkSteps(player.getX(), player.getY() - 2, 3, false);
					}
					return;
				}
				if (id == WorldObject.asOSRS(28900)) { //Khourend dungeon altar
					if (player.getInventory().containsItem(29618, 1)) {
						player.getInventory().deleteItem(29618, 1);
						SkotizoInstance.launch(player);
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage", "It looks like you need some sort of key to activate the altar.");
					}
					return;
				}
				if (id == WorldObject.asOSRS(28894)) { //Khorend dungeon exit
					player.useStairs(828, new Position(1639, 3673, 0), 2, 2);
					return;
				}
				if (id == 4880) { //Temple of Marimbo Dungeon entrance
					player.useStairs(827, new Position(2807, 9201), 2, 1);
					return;
				}
				if (id == WorldObject.asOSRS(4881)) { //Temple of Marimbo Dungeon exit
					player.useStairs(828, new Position(2806, 2785), 2, 1);
					return;
				}
				if (id == 47857) {
					player.getDialogueManager().startDialogue("CagedSanta");
					return;
				}
				if (id == 4780) { //Ape atoll dungeon entrance
					player.useStairs(827, new Position(2764, 9103), 2, 1);
					return;
				}
				if (id == 4781) { //Ape atoll dungeon exit
					player.useStairs(828, new Position(2806, 2785), 2, 1);
					return;
				}
				if (id == WorldObject.asOSRS(29156)) {
					player.getDialogueManager().startDialogue("OrnateJewelleryBoxD");
					return;
				}
				if (id == WorldObject.asOSRS(31990)) {
					VorkathInstance.launch(player);
					return;
				}
				if (id == WorldObject.asOSRS(28656)) {
					player.addWalkSteps(2435, 3520, 2, false);
					player.lock();
					FadingScreen.fade(player, () -> {
						player.unlock();
						player.setNextPosition(new Position(2128, 5647, 0));
					});
					return;
				}
				if (id == WorldObject.asOSRS(28686)) { //Demonic gorilla cave entrance
					player.setNextPosition(new Position(2128, 5647, 0));
					return;
				}
				if (id == WorldObject.asOSRS(28687)) { //Demonic gorilla rope up
					player.setNextPosition(new Position(2435, 3519, 0));
					return;
				}
				if (id == 55404) { //taverly entrance
					player.setNextPosition(new Position(2885, 9795, 0));
					return;
				}
				if (id == 66992) { //taverly exit
					player.setNextPosition(new Position(2885, 3395, 0));
					return;
				}
				if (id == WorldObject.asOSRS(27362)) { //Lizardman handholds
					if (player.getX() == 1460)
						player.setNextPosition(new Position(1454, 3690, 0));
					else if (player.getX() == 1454)
						player.setNextPosition(new Position(1460, 3690, 0));
					else if (player.getX() == 1470)
						player.setNextPosition(new Position(1476, 3687, 0));
					else if (player.getX() == 1476)
						player.setNextPosition(new Position(1470, 3687, 0));
					return;
				}
				if (id == WorldObject.asOSRS(26760)) {
					if (player.getX() <= 3607) {
						player.addWalkSteps(3608, 2671, 3, false);
					} else {
						player.addWalkSteps(3607, 2671, 3, false);
					}
					return;
				}
				if (id == 46933) { // Home teleporter
					//player.getDialogueManager().startDialogue("TeleporterDialogue");
					player.getTeleportManager().sendInterface();
					return;
				}
				if (id == WorldObject.asOSRS(23104)) { //Cerberus iron winch
					CerberusInstance.launch(player);
					return;
				}
				if (id == WorldObject.asOSRS(32655)) { // Noticeboard
					player.getDialogueManager().startDialogue("NoticeboardD");
					return;
				}
				if (id == 6) {
					if (OwnedObjectManager.isPlayerObject(player, object))
						DwarfMultiCannon.fire(player);
					else
						player.getPackets().sendGameMessage("This isn't your cannon.");
				}
				if (id == 9)
					DwarfMultiCannon.pickupCannon(player, 3, object);
				else if (id == 8)
					DwarfMultiCannon.pickupCannon(player, 2, object);
				else if (id == 7)
					DwarfMultiCannon.pickupCannon(player, 1, object);
				if (id == WorldObject.asOSRS(10068)) { // Zulrah boat
					ZulrahInstance.launch(player);
					return;
				}
				if (id == 7235) { // Safe cracking
					player.getActionManager().setAction(new SafeCracking());
					return;
				}
				if (id == WorldObject.asOSRS(534)) { // Smoke dungeon exit
					player.sendMessage("The tunnel seems to be blocked by something.");
					return;
				}
				if (id == WorldObject.asOSRS(535)) {
					player.getDialogueManager().startDialogue("SmokedevilInstanceD");
					return;
				}
				if (id == WorldObject.asOSRS(536)) {
					player.setNextPosition(new Position(2891, 2540, 0));
					return;
				}
				if (id == 66518) { // Rougue's den
					player.useStairs(823, new Position(3047, 4971, 0), 1, 1);
					return;
				}
				if (id == WorldObject.asOSRS(538)) {// Kraken out
					player.setNextPosition(new Position(2280, 10016, 0));
					return;
				}
				if (id == WorldObject.asOSRS(537)) {// Kraken in
					player.setNextPosition(new Position(2280, 10022, 0));
					return;
				}
				if (CluesManager.objectSpot(player, object)) {
					return;
				}
				if (id == 46935) {
					if (player.getControlerManager().getControler() instanceof TheAlchemistInstance)
						return;
					player.getControlerManager().startControler("TheAlchemistInstance");
				}
				if (id >= 10851 && id <= 10888) {
					AgilityPyramid.pyramidCourse(player, object);
				}
				if (id == 7837) {
					CompostBin.checkBin(player);
					return;
				}
				if (object.getId() == 45802) {
					player.getDialogueManager().startDialogue("Grim");
					return;
				}
				if (id == 65458) { // Scorpia entrance
					Position[] locations = { new Position(2977, 2524, 0), new Position(2987, 2543, 0),
							new Position(2976, 2543, 0) };
					player.setNextPosition(locations[Utils.random(locations.length)]);
					player.getControlerManager().startControler("Wilderness");
				}
				if (id == WorldObject.asOSRS(26763)) {// Scorpia exit
					Position[] locations = { new Position(3233, 3949, 0), new Position(3232, 3939, 0) };
					player.setNextPosition(locations[Utils.random(locations.length)]);
					player.getControlerManager().startControler("Wilderness");
				}
				if (id == 35390) {
					GodWars.passGiantBoulder(player, object, true);
				}
				if (id == 70812) {
					player.getControlerManager().startControler("QueenBlackDragonControler");
				}
				if (id >= 65616 && id <= 65622) {
					WildernessObelisk.activateObelisk(id, player);
					return;
				}
				WorldPatches patch = WorldPatches.forId(object.getId());
				if (patch != null && player.getFarmings() != null) {
					player.getFarmings().patches[patch.getArrayIndex()].handleOption1(player);
					return;
				}
				if (Ectofuntus.handleObjects(player, object.getId())) {
					return;
				}
				if (id == 26342) {
					player.useStairs(827, new Position(2882, 5310, 2), 1, 2);
					player.getControlerManager().startControler("GodWars");
				}
				if (id == 26439) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2885, 5349, 2));
					player.getControlerManager().startControler("GodWars");
					return;
				}
				if (id == 3634) {
					player.sendMessage("You completed the event! Here have some Loyalty Tokens!");
					player.getInventory().addItem(29980, 100);
					player.getControlerManager().getControler().removeControler();
					teleportPlayer(player, player.tX, player.tY, player.tH);
					return;
				}
				if (id == 2604) {
					player.sendMessage("You find the first part of the map.");
					player.getInventory().addItem(1535, 1);
					return;
				}
				if (id == 12355) {
					player.getDialogueManager().startDialogue("MoneyMakingTP");
				}
				if (object.getId() == 21317) {
					player.setNextPosition(new Position(2385, 10264, 1));
				}
				if (object.getId() == 21316) {
					player.setNextPosition(new Position(2385, 10259, 1));
				}
				if (object.getId() == 21319) {
					player.setNextPosition(new Position(2398, 10258, 1));
				}
				if (object.getId() == 21318) {
					player.setNextPosition(new Position(2393, 10258, 1));
				}
				if (id == 47237) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 90) {
						player.getPackets().sendGameMessage("You need 90 agility to use this shortcut.");
						return;
					}
					if (player.getX() == 1641 && player.getY() == 5260 || player.getX() == 1641 && player.getY() == 5259
							|| player.getX() == 1640 && player.getY() == 5259) {
						player.setNextPosition(new Position(1641, 5268, 0));
					} else {
						player.setNextPosition(new Position(1641, 5260, 0));
					}
				} // doors
				if (id == 3628 || id == 3629) {
					handleDoor(player, object);
				}
				if (id == 70436) {
					ShopsHandler.openShop(player, 64);
				}
				if (id == 68) {
					if (player.getInventory().containsItem(28, 1)) {
						player.faceObject(object);
						player.animate(833);
						player.getInventory().addItem(12156, 1);
						player.sm("You gather some honeycomb");
						return;
					} else {
						player.sm("Bee's swarm you and deal damage.");
						player.applyHit(new Hit(player, 20, HitLook.REGULAR_DAMAGE));

					}
				}
				if (id == 21250) {
					player.getItemCollectionManager().openInterface(ItemCollectionManager.Category.BOSSES);
				}
				if (id == 12477 && player.getSkills().getLevel(Skills.FARMING) > 60) {
					player.animate(2281);
					player.getInventory().addItem(7516, 1);
					player.getSkills().addXp(Skills.FARMING, 30);
				}
				if (id == 2073) {
					if (player.getInventory().getFreeSlots() < 1) {
						player.getPackets().sendGameMessage("Not enough space in your inventory.");
						return;
					}
					player.faceObject(object);
					player.animate(7270);
					player.getInventory().addItem(1963, 1);
					player.sm("You pick some bananas");
					return;

				}
				if (id == 7836) {
					if (player.getInventory().containsItem(1925, 1)) {
						player.getInventory().deleteItem(1925, 1);
						player.faceObject(object);
						player.animate(895);
						player.getInventory().addItem(6032, 1);
						player.sm("You fill the bucket with compost.");
					} else {
						player.sm("You need a bucket todo this.");
						return;

					}
				}
				if (id == 12468) {
					player.setNextPosition(new Position(2975, 9514, 1));
				}
				if (id == 12467) {
					player.setNextPosition(new Position(2974, 9515, 1));
				}
				if (id == 12474) {
					player.setNextPosition(new Position(2964, 9478, 1));
				}
				if (id == 12475) {
						player.setNextPosition(new Position(2964, 9478, 0));
					}
                if (id == 28534 ) {
                	if (player.getInventory().containsItem(28802, 1) && player.getInventory().containsItem(28800, 1) && player.getInventory().containsItem(28798, 1)) {
                		player.animate(899);
                		player.getInventory().deleteItem(28802, 1);
						player.getInventory().deleteItem(28800, 1);
						player.getInventory().deleteItem(28798, 1);
						player.getInventory().addItem(28973, 1);

					}
                	else {
                		player.sm("You need all 3 spear parts to create the darkend spear.");
					}
				}

				if (id == 80321) {
					player.getWorkbench().open();
					return;
				}

				if (id == 12508) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.RED_CORAL));
				}
				if (id == 12497) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.YELLOW_CORAL));
				}
				if (id == 12517) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.BLUE_CORAL));
				}
				if (id == 12519) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.RED_CORAL_LARGE));
				}
				if (id == 12529) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.GREEN_CORAL));
				}
				if (id == 2330) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.SAND_STONE));
				}
				if (id == 2266 || id == 2337 || id == 2339 || id == 34839 || id == 34836) {
					handleDoor(player, object);
				}
				if (id == 8929) {
					player.useStairs(828, new Position(2545, 10143, 0), 1, 2);
				}
				if (object.getId() == 69835 || object.getId() == 69834 || object.getId() == 69833
						|| object.getId() == 69832 || object.getId() == 69831 || object.getId() == 69830
						|| object.getId() == 69829 || object.getId() == 69837 || object.getId() == 69838
						|| object.getId() == 69839 || object.getId() == 69840 || object.getId() == 69841
						|| object.getId() == 69828 || object.getId() == 69827) {
					player.getLodeStones().activateLodestone(object);
				}
				if (id == 2593 || id == 14306 || id == 14304 || id == 2412 || id == 2083 || id == 17404) {
					if (object.getX() == player.getX()) {
						if (player.getY() > object.getY()) {
							player.useStairs(828, new Position(player.getX(), player.getY() - 3, 1), 1, 2);
						} else {
							player.useStairs(828, new Position(player.getX(), player.getY() + 3, 1), 1, 2);
						}
					} else if (object.getY() == player.getY()) {
						if (player.getX() > object.getX()) {
							player.useStairs(828, new Position(player.getX() - 3, player.getY(), 1), 1, 2);
						} else {
							player.useStairs(828, new Position(player.getX() + 3, player.getY(), 1), 1, 2);
						}
					}
					if (player.DS == 5) {
						World.spawnNPC(4475, new Position(3048, 3209, 1), -1, 0, true);
					}

					player.getPackets().sendGameMessage("You cross the gangplank.");
				}
				if (id == 4304) {
					player.getDialogueManager().startDialogue("SmeltingD", object);
				}
				if (id == 2594 || id == 14307 || id == 14305 || id == 2413 || id == 2084 || id == 17405) {
					if (object.getX() == player.getX()) {
						if (player.getY() > object.getY()) {
							player.useStairs(828, new Position(player.getX(), player.getY() - 3, 0), 1, 2);
						} else {
							player.useStairs(828, new Position(player.getX(), player.getY() + 3, 0), 1, 2);
						}
					} else if (object.getY() == player.getY()) {
						if (player.getX() > object.getX()) {
							player.useStairs(828, new Position(player.getX() - 3, player.getY(), 0), 1, 2);
						} else {
							player.useStairs(828, new Position(player.getX() + 3, player.getY(), 0), 1, 2);
						}
					}

					player.getPackets().sendGameMessage("You cross the gangplank.");
				}
				// City/Area Handling
				if (Lumbridge.isObject(object)) {
					Lumbridge.HandleObject(player, object);
				}
				if (TutIsland.isObject(object)) {
					TutIsland.HandleObject(player, object);
				}
				if (BarrowsObjects.isObject(object)) {
					BarrowsObjects.HandleObject(player, object);
				}
				if (SinkHoles.isObject(object)) {
					SinkHoles.HandleObject(player, object);
				}
				if (House.isObject(object)) {
					House.HandleObject(player, object);
				}
				if (Ooglog.isObject(object)) {
					Ooglog.HandleObject(player, object);
				}
				if (Wildy.isObject(object)) {
					Wildy.HandleObject(player, object);
				}
				if (Relekka.isObject(object)) {
					Relekka.HandleObject(player, object);
				}
				if (Burthrope.isObject(object)) {
					Burthrope.HandleObject(player, object);
				}
				if (Falador.isObject(object)) {
					Falador.HandleObject(player, object);
				}
				if (Karamja.isObject(object)) {
					Karamja.HandleObject(player, object);
				}
				if (Draynor.isObject(object)) {
					Draynor.HandleObject(player, object);
				}
				if (Varrock.isObject(object)) {
					Varrock.HandleObject(player, object);
				}
				if (AlKharid.isObject(object)) {
					AlKharid.HandleObject(player, object);
				}
				if (PortSarim.isObject(object)) {
					PortSarim.HandleObject(player, object);
				}
				if (Rimmington.isObject(object)) {
					Rimmington.HandleObject(player, object);
				}
				if (Edgeville.isObject(object)) {
					Edgeville.HandleObject(player, object);
				}
				if (EdgevilleDungeon.isObject(object)) {
					EdgevilleDungeon.HandleObject(player, object);
				}
				if (RodiksZone.isObject(object)) {
					RodiksZone.HandleObject(player, object);
				}
				if (TarnsLair.isObject(object)) {
					TarnsLair.HandleObject(player, object);
				}
				if (MosLeHarmless.isObject(object)) {
					MosLeHarmless.HandleObject(player, object);
				}
				if (PoisonWasteDungeon.isObject(object)) {
					PoisonWasteDungeon.HandleObject(player, object);
				}
				if (EvilTree.isObject(object)) {
					EvilTree.HandleObject(player, object, 1);
				}
				if (id == 4500 && object.getX() == 3077 && object.getY() == 4234) {
					Edgeville.HandleObject(player, object);
				}
				// Dwarf Cannon Quest
				if (id <= 15595 && id >= 15590) {
					DwarfCannon.HandleObject(player, object);
				}
				// Halloween event
				if (id == 27896 || id == 32046 || id == 31747 || id == 30838 || id == 31842 || id == 46567
						|| id == 46568 || id == 31818 || id == 46549 || id == 31819 || id == 46566 || id == 62621) {
					HalloweenObject.HandleObject(player, object);
				}
				if (id == 47232) {
					if (player.getSkills().getLevel(Skills.SLAYER) < 75) {
						player.getPackets().sendGameMessage("You need 75 slayer to enter Kuradal's dungeon.");
						return;
					}
					player.setNextPosition(new Position(1661, 5257, 0));
				}
				if (id == 2971) {
					if (player.getY() > object.getY()) {
						player.addWalkSteps(player.getX(), player.getY() - 1, 1, false);
					} else {
						player.addWalkSteps(player.getX(), player.getY() + 1, 1, false);
					}
				}
				if (id == 47236) {
					if (player.getX() == 1650 && player.getY() == 5281 || player.getX() == 1651 && player.getY() == 5281
							|| player.getX() == 1650 && player.getY() == 5281) {
						player.addWalkSteps(1651, 5280, 1, false);
					}
					if (player.getX() == 1652 && player.getY() == 5280 || player.getX() == 1651 && player.getY() == 5280
							|| player.getX() == 1653 && player.getY() == 5280) {
						player.addWalkSteps(1651, 5281, 1, false);
					}
					if (player.getX() == 1650 && player.getY() == 5301 || player.getX() == 1650 && player.getY() == 5302
							|| player.getX() == 1650 && player.getY() == 5303) {
						player.addWalkSteps(1649, 5302, 1, false);
					}
					if (player.getX() == 1649 && player.getY() == 5303 || player.getX() == 1649 && player.getY() == 5302
							|| player.getX() == 1649 && player.getY() == 5301) {
						player.addWalkSteps(1650, 5302, 1, false);
					}
					if (player.getX() == 1626 && player.getY() == 5301 || player.getX() == 1626 && player.getY() == 5302
							|| player.getX() == 1626 && player.getY() == 5303) {
						player.addWalkSteps(1625, 5302, 1, false);
					}
					if (player.getX() == 1625 && player.getY() == 5301 || player.getX() == 1625 && player.getY() == 5302
							|| player.getX() == 1625 && player.getY() == 5303) {
						player.addWalkSteps(1626, 5302, 1, false);
					}
					if (player.getX() == 1609 && player.getY() == 5289 || player.getX() == 1610 && player.getY() == 5289
							|| player.getX() == 1611 && player.getY() == 5289) {
						player.addWalkSteps(1610, 5288, 1, false);
					}
					if (player.getX() == 1609 && player.getY() == 5288 || player.getX() == 1610 && player.getY() == 5288
							|| player.getX() == 1611 && player.getY() == 5288) {
						player.addWalkSteps(1610, 5289, 1, false);
					}
					if (player.getX() == 1606 && player.getY() == 5265 || player.getX() == 1605 && player.getY() == 5265
							|| player.getX() == 1604 && player.getY() == 5265) {
						player.addWalkSteps(1605, 5264, 1, false);
					}
					if (player.getX() == 1606 && player.getY() == 5264 || player.getX() == 1605 && player.getY() == 5264
							|| player.getX() == 1604 && player.getY() == 5264) {
						player.addWalkSteps(1605, 5265, 1, false);
					}
					if (player.getX() == 1634 && player.getY() == 5254 || player.getX() == 1634 && player.getY() == 5253
							|| player.getX() == 1634 && player.getY() == 5252) {
						player.addWalkSteps(1635, 5253, 1, false);
					}
					if (player.getX() == 1635 && player.getY() == 5254 || player.getX() == 1635 && player.getY() == 5253
							|| player.getX() == 1635 && player.getY() == 5252) {
						player.addWalkSteps(1634, 5253, 1, false);
					}
				}
				if (id == 44339) {
					if (!Agility.hasLevel(player, 81)) {
						return;
					}
					boolean isEast = player.getX() > 2772;
					final Position tile = new Position(isEast ? 2768 : 2775, 10002, 0);
					WorldTasksManager.schedule(new WorldTask() {

						int ticks = -1;

						@Override
						public void run() {
							ticks++;
							if (ticks == 0) {
								player.setNextFacePosition(object);
							} else if (ticks == 1) {
								player.animate(new Animation(10738));
								player.setNextForceMovement(new ForceMovement(player, 0, tile, 5,
										Utils.getAngle(object.getX() - player.getX(), object.getY() - player.getY())));
							} else if (ticks == 3) {
								player.setNextPosition(tile);
							} else if (ticks == 4) {
								player.getPackets().sendGameMessage("Your feet skid as you land floor.");
								stop();
								return;
							}
						}
					}, 0, 0);
				}
				if (id == 77052) {
					if (!Agility.hasLevel(player, 70)) {
						return;
					}
					boolean isEast = player.getX() > 2734;
					final Position tile = new Position(isEast ? 2730 : 2735, 10008, 0);
					WorldTasksManager.schedule(new WorldTask() {

						int ticks = -1;

						@Override
						public void run() {
							ticks++;
							if (ticks == 0) {
								player.setNextFacePosition(object);
							} else if (ticks == 1) {
								player.animate(new Animation(17811));
							} else if (ticks == 9) {
								player.setNextPosition(tile);
							} else if (ticks == 10) {
								stop();
								return;
							}
						}
					}, 0, 0);
				}
				if (id == 15645) {
					player.getControlerManager().getControler().removeControler();
					teleportPlayer(player, player.tX, player.tY, player.tH);
					// WorldTasksManager.removeTask();
					player.gotreward = 0;
				}

				if (object.getId() == 26341) { // godwars dungeon.
					player.useStairs(828, new Position(2881, 5310, 0), 0, 0);
					player.getControlerManager().startControler("GodWars");
					return;
				}

				if (id == 40443) {
					LividFarm.deposit(player);
				}
				if (id == 40486 || id == 40505 || id == 40534 || id == 40492 || id == 40646 || id == 40489
						|| id == 40487 || id == 40532 || id == 40499 || id == 40533 || id == 40504) {
					LividFarm.MakePlants(player);
				}
				if (id == 40444) {
					LividFarm.TakeLogs(player);
				}
				if (id == WorldObject.asOSRS(7489)) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.Coal_Ore));
					return;
				}
				if (id == WorldObject.asOSRS(7491)) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.Gold_Ore));
					return;
				}
				if (id == WorldObject.asOSRS(7493)) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.Adamant_Ore));
					return;
				}
				if (id == WorldObject.asOSRS(7459)) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.Mithril_Ore));
					return;
				}
				if (id == WorldObject.asOSRS(7455)) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.Iron_Ore));
					return;
				}
				if (id == 28296 || id == 28297) {
					player.getPackets().sendGameMessage("You pick some snow off of the ground.");
					player.animate(new Animation(1026));
					World.removeTemporaryObject(object, 60000, false);
					player.getInventory().addItem(11951, 5);
				}
				if (id == 62414) {
					if (player.getUsername().equalsIgnoreCase("Muda") || player.getUsername().equalsIgnoreCase("")
							|| player.getUsername().equalsIgnoreCase("")) {
						World.removeObject(object, true);
					} else {
						player.getPackets().sendGameMessage(
								"It isn't polite to demolish other people's gravestone, they must do it themselves!");
					}
				}

				if (id == 33931) {
					if (player.SOWQUEST == 1) {
						player.SOWQUEST += 1;
						player.sm("You found nothing.. Maybe you should go and talk to Ozan..");
						player.sm("near varrock fountain?");
					}
				}
				if (id == 15482) {
					player.getDialogueManager().startDialogue("HousePortal");
				}
				if (id == 380) {
					player.getDialogueManager().startDialogue("Animations");
				}
				if (id == 28716 || id == 67036 || id == 28734) {
					Summoning.sendInterface(player);
					player.setNextFacePosition(object);
					return;
				}

				if (id == 38660) {
					player.getActionManager().setAction(new StarMining(object, StarStages.SECOND));
					return;
				}
				if (id == 38661) {
					player.getActionManager().setAction(new StarMining(object, StarStages.THIRD));
					return;
				}
				if (id == 38662) {
					player.getActionManager().setAction(new StarMining(object, StarStages.FORTH));
					return;
				}
				if (id == 38663) {
					player.getActionManager().setAction(new StarMining(object, StarStages.FIVE));
					return;
				}
				if (id == 38664) {
					player.getActionManager().setAction(new StarMining(object, StarStages.SIX));
					return;
				}
				if (id == 38665) {
					player.getActionManager().setAction(new StarMining(object, StarStages.SEVEN));
					return;
				}
				if (id == 38666) {
					player.getActionManager().setAction(new StarMining(object, StarStages.EIGHT));
					return;
				}
				if (id == 38667) {
					player.getActionManager().setAction(new StarMining(object, StarStages.NINE));
					return;
				}
				if (id == 38668) {
					player.getActionManager().setAction(new StarMining(object, StarStages.TEN));
					return;
				}
				if (id >= 69785 && id <= 69788) {
					player.getInterfaceManager().sendInterface(135);
				}
				if (id == 29958 || id == 4019 || id == 50205 || id == 50206 || id == 50207 || id == 53883 || id == 54650
						|| id == 55605 || id == 56083 || id == 56084 || id == 56085 || id == 56086) {
					final int maxSummoning = player.getSkills().getLevelForXp(23);
					if (player.getSkills().getLevel(23) < maxSummoning) {
						player.lock(5);
						player.getPackets().sendGameMessage("You feel the obelisk", true);
						player.animate(new Animation(8502));
						player.setNextGraphics(new Graphics(1308));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								player.getSkills().restoreSummoning();
								player.getPackets().sendGameMessage("...and recharge all your skills.", true);
							}
						}, 2);
					} else {
						player.getPackets().sendGameMessage("You already have full summoning.", true);
					}
					return;
				} else if (id == 2350 && object.getX() == 3352 && object.getY() == 3417 && object.getZ() == 0) {
					player.useStairs(832, new Position(3177, 5731, 0), 1, 2);
				} else if (hunterNpc != null) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.animate(hunterNpc.getEquipment().getPickUpAnimation());
						player.getInventory().getItems().addAll(hunterNpc.getItems());
						player.getInventory().addItem(hunterNpc.getEquipment().getId(), 1);
						player.getInventory().refresh();
						if (hunterNpc.getNpcId() == 7010) {
							player.grenwalls++;
						}
						player.getSkills().addXp(Skills.HUNTER, hunterNpc.getXp());
					} else {
						player.getPackets().sendGameMessage("This isn't your trap.");
					}
				} else if (id == HunterEquipment.BOX.getObjectId() || id == 19192) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.animate(new Animation(5208));
						player.getInventory().addItem(HunterEquipment.BOX.getId(), 1);
					} else {
						player.getPackets().sendGameMessage("This isn't your trap.");
					}
				} else if (id == HunterEquipment.BRID_SNARE.getObjectId() || id == 19174) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.animate(new Animation(5207));
						player.getInventory().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
					} else {
						player.getPackets().sendGameMessage("This isn't your trap.");
					}
				} else if (id == 2406) {
					if (player.lostCity == 1) {
						player.setNextGraphics(new Graphics(1118));
						player.setNextPosition(new Position(2452, 4473, player.getZ()));
					} else if (player.getEquipment().getWeaponId() == 772 && player.spokeToWarrior == true
							&& player.spokeToShamus == true && player.spokeToMonk == true) {
						player.setNextGraphics(new Graphics(1118));
						player.setNextPosition(new Position(2452, 4473, player.getZ()));
						player.getInterfaceManager().sendLostCityComplete(player);
					} else if (player.lostCity == 0) {
						player.getPackets().sendGameMessage("You must have completed the Lost City to enter Zanaris.");
					} else {
						player.getPackets().sendGameMessage("You need to wield the dramen staff to enter Zanaris.");
					}
				} else if (object.getId() == 1292) {
					if (player.getSkills().getLevel(Skills.WOODCUTTING) < 36) {
						player.getDialogueManager().startDialogue("SimpleMessage",
								"You need a woodcutting level of 36 and an axe to cut this tree.");
					} else {
						if (player.spokeToShamus == true && player.getInventory().containsItem(1349, 1)
								|| player.getInventory().containsItem(1351, 1)) {
							World.spawnNPC(655, new Position(player.getX(), player.getY() + 1, 0), -1, 0, true, true);
						} else {
							player.getPackets().sendGameMessage(
									"You must have a hatchet in your inventory to chop this tree down.");
						}
					}
				} else if (object.getId() == 2408) {
					player.useStairs(828, new Position(2828, 9767, 0), 1, 2);
				} else if (object.getId() == 2409 && player.spokeToWarrior == true) {
					player.getDialogueManager().startDialogue("Shamus");
				} else if (id == 25337 && object.getX() == 1744 && object.getY() == 5323 && object.getZ() == 0) {
					player.useStairs(832, new Position(1744, 5321, 1), 1, 2);
				} else if (id == 39468 && object.getX() == 1744 && object.getY() == 5322 && object.getZ() == 1) {
					player.useStairs(832, new Position(1745, 5325, 0), 1, 2);
				} else if (id == 25336 && object.getX() == 1770 && object.getY() == 5365 && object.getZ() == 0) {
					player.useStairs(832, new Position(1768, 5366, 1), 1, 2);
				} else if (id == 25338 && object.getX() == 1769 && object.getY() == 5365 && object.getZ() == 1) {
					player.useStairs(832, new Position(1772, 5366, 0), 1, 2);
				} else if (id == 25339 && object.getX() == 1778 && object.getY() == 5344 && object.getZ() == 0) {
					player.useStairs(832, new Position(1778, 5343, 1), 1, 2);
				} else if (id == 25340 && object.getX() == 1778 && object.getY() == 5344 && object.getZ() == 1) {
					player.useStairs(832, new Position(1778, 5346, 0), 1, 2);
				} else if (id == 2353 && object.getX() == 3177 && object.getY() == 5730 && object.getZ() == 0) {
					player.useStairs(828, new Position(3353, 3416, 0), 1, 2);
				} else if (id == 11554 || id == 11552) {
					player.getPackets().sendGameMessage("That rock is currently unavailable.");
				} else if (id == 25214) {
					player.getPackets().sendGameMessage("You cannot enter through this trap door.");
				} else if (id == 70796) {
					player.setNextPosition(new Position(1090, 6360, 0));
				} else if (id == 70797) {
					player.setNextPosition(new Position(1090, 6597, 0));
				} else if (id == 70798) {
					player.setNextPosition(new Position(1340, 6497, 0));
				} else if (id == 70799) {
					if (player.getSkills().getLevelForXp(Skills.AGILITY) <= 59) {
						player.getPackets().sendGameMessage("You need an Agility level of 60 to use this shorcut.");
					} else {
						player.setNextPosition(new Position(1178, 6356, 0));
					}
				} else if (id == 70795) {
					if (player.getSkills().getLevelForXp(Skills.AGILITY) <= 59) {
						player.getPackets().sendGameMessage("You need an Agility level of 60 to use this shorcut.");
					} else {
						player.getDialogueManager().startDialogue("GrotDungeonAgility");
					}
				} else if (id == 38279) {
					player.getDialogueManager().startDialogue("RunespanPortalD");
				} else if (id == 8717) {
					player.getDialogueManager().startDialogue("Loom");
				} else if (id == 15653) {
					if (World.isSpawnedObject(object) || !WarriorsGuild.canEnter(player)) {
						return;
					}
					player.lock(2);
					WorldObject opened = new WorldObject(object.getId(), object.getType(), object.getRotation() - 1,
							object.getX(), object.getY(), object.getZ());
					World.spawnTemporaryObject(opened, 600);
					player.addWalkSteps(object.getX() - 1, player.getY(), 2, false);
				}
//				if (object.getId() == 65203 && object.getX() == 3118)
//					player.setNextPosition(new Position(3247, 5491, 0));
//
//				if (object.getId() == 65203 && object.getX() == 3129)
//					player.setNextPosition(new Position(3235, 5560, 0));
//
//				if (object.getId() == 65203 && object.getX() == 3164)
//					player.setNextPosition(new Position(3291, 5480, 0));
//
//				if (object.getId() == 65203 && object.getX() == 3176)
//					player.setNextPosition(new Position(3291, 5538, 0));
//
//				if (object.getId() == 65203 && object.getX() == 3058)
//					player.setNextPosition(new Position(3184, 5469, 0));
//
//				// Entrance to wilderness from chaos tunnel ropes
//				if (object.getId() == 28782 && object.getX() == 3248) {// level
//					// 7
//					player.setNextPosition(new Position(3118, 3569, 0));
//					player.getControlerManager().startControler("WildernessControler");
//				}
//
//				if (object.getId() == 28782 && object.getX() == 3234) {// level
//					// 9
//					player.setNextPosition(new Position(3129, 3586, 0));
//					player.getControlerManager().startControler("WildernessControler");
//				}
//
//				if (object.getId() == 28782 && object.getX() == 3292) {// to lvl
//					// 5-6
//					// (bot
//					// tunnel
//					// from
//					// rs)
//					player.setNextPosition(new Position(3165, 3562, 0));
//					player.getControlerManager().startControler("WildernessControler");
//				}
//
//				if (object.getId() == 28782 && object.getX() == 3291) {// to lvl
//					// 9
//					player.setNextPosition(new Position(3176, 3584, 0));
//					player.getControlerManager().startControler("WildernessControler");
//				}
//
//				if (object.getId() == 28782 && object.getX() == 3183) {// to lvl
//					// 4
//					player.setNextPosition(new Position(3057, 3551, 0));
//					player.getControlerManager().startControler("WildernessControler");
//				}
//				if (object.getId() == 77745 || object.getId() == 28779 || object.getId() == 29537) {
//
//					if (x == 3254 && y == 5451) {
//						player.setNextPosition(new Position(3250, 5448, 0));
//					}
//					if (x == 3250 && y == 5448) {
//						player.setNextPosition(new Position(3254, 5451, 0));
//					}
//					if (x == 3241 && y == 5445) {
//						player.setNextPosition(new Position(3233, 5445, 0));
//					}
//					if (x == 3233 && y == 5445) {
//						player.setNextPosition(new Position(3241, 5445, 0));
//					}
//					if (x == 3259 && y == 5446) {
//						player.setNextPosition(new Position(3265, 5491, 0));
//					}
//					if (x == 3265 && y == 5491) {
//						player.setNextPosition(new Position(3259, 5446, 0));
//					}
//					if (x == 3260 && y == 5491) {
//						player.setNextPosition(new Position(3266, 5446, 0));
//					}
//					if (x == 3266 && y == 5446) {
//						player.setNextPosition(new Position(3260, 5491, 0));
//					}
//					if (x == 3241 && y == 5469) {
//						player.setNextPosition(new Position(3233, 5470, 0));
//					}
//					if (x == 3233 && y == 5470) {
//						player.setNextPosition(new Position(3241, 5469, 0));
//					}
//					if (x == 3235 && y == 5457) {
//						player.setNextPosition(new Position(3229, 5454, 0));
//					}
//					if (x == 3229 && y == 5454) {
//						player.setNextPosition(new Position(3235, 5457, 0));
//					}
//					if (x == 3280 && y == 5460) {
//						player.setNextPosition(new Position(3273, 5460, 0));
//					}
//					if (x == 3273 && y == 5460) {
//						player.setNextPosition(new Position(3280, 5460, 0));
//					}
//					if (x == 3283 && y == 5448) {
//						player.setNextPosition(new Position(3287, 5448, 0));
//					}
//					if (x == 3287 && y == 5448) {
//						player.setNextPosition(new Position(3283, 5448, 0));
//					}
//					if (x == 3244 && y == 5495) {
//						player.setNextPosition(new Position(3239, 5498, 0));
//					}
//					if (x == 3239 && y == 5498) {
//						player.setNextPosition(new Position(3244, 5495, 0));
//					}
//					if (x == 3232 && y == 5501) {
//						player.setNextPosition(new Position(3238, 5507, 0));
//					}
//					if (x == 3238 && y == 5507) {
//						player.setNextPosition(new Position(3232, 5501, 0));
//					}
//					if (x == 3218 && y == 5497) {
//						player.setNextPosition(new Position(3222, 5488, 0));
//					}
//					if (x == 3222 && y == 5488) {
//						player.setNextPosition(new Position(3218, 5497, 0));
//					}
//					if (x == 3218 && y == 5478) {
//						player.setNextPosition(new Position(3215, 5475, 0));
//					}
//					if (x == 3215 && y == 5475) {
//						player.setNextPosition(new Position(3218, 5478, 0));
//					}
//					if (x == 3224 && y == 5479) {
//						player.setNextPosition(new Position(3222, 5474, 0));
//					}
//					if (x == 3222 && y == 5474) {
//						player.setNextPosition(new Position(3224, 5479, 0));
//					}
//					if (x == 3208 && y == 5471) {
//						player.setNextPosition(new Position(3210, 5477, 0));
//					}
//					if (x == 3210 && y == 5477) {
//						player.setNextPosition(new Position(3208, 5471, 0));
//					}
//					if (x == 3214 && y == 5456) {
//						player.setNextPosition(new Position(3212, 5452, 0));
//					}
//					if (x == 3212 && y == 5452) {
//						player.setNextPosition(new Position(3214, 5456, 0));
//					}
//					if (x == 3204 && y == 5445) {
//						player.setNextPosition(new Position(3197, 5448, 0));
//					}
//					if (x == 3197 && y == 5448) {
//						player.setNextPosition(new Position(3204, 5445, 0));
//					}
//					if (x == 3189 && y == 5444) {
//						player.setNextPosition(new Position(3187, 5460, 0));
//					}
//					if (x == 3187 && y == 5460) {
//						player.setNextPosition(new Position(3189, 5444, 0));
//					}
//					if (x == 3192 && y == 5472) {
//						player.setNextPosition(new Position(3186, 5472, 0));
//					}
//					if (x == 3186 && y == 5472) {
//						player.setNextPosition(new Position(3192, 5472, 0));
//					}
//					if (x == 3185 && y == 5478) {
//						player.setNextPosition(new Position(3191, 5482, 0));
//					}
//					if (x == 3191 && y == 5482) {
//						player.setNextPosition(new Position(3185, 5478, 0));
//					}
//					if (x == 3171 && y == 5473) {
//						player.setNextPosition(new Position(3167, 5471, 0));
//					}
//					if (x == 3167 && y == 5471) {
//						player.setNextPosition(new Position(3171, 5473, 0));
//					}
//					if (x == 3171 && y == 5478) {
//						player.setNextPosition(new Position(3167, 5478, 0));
//					}
//					if (x == 3167 && y == 5478) {
//						player.setNextPosition(new Position(3171, 5478, 0));
//					}
//					if (x == 3168 && y == 5456) {
//						player.setNextPosition(new Position(3178, 5460, 0));
//					}
//					if (x == 3178 && y == 5460) {
//						player.setNextPosition(new Position(3168, 5456, 0));
//					}
//					if (x == 3191 && y == 5495) {
//						player.setNextPosition(new Position(3194, 5490, 0));
//					}
//					if (x == 3194 && y == 5490) {
//						player.setNextPosition(new Position(3191, 5495, 0));
//					}
//					if (x == 3141 && y == 5480) {
//						player.setNextPosition(new Position(3142, 5489, 0));
//					}
//					if (x == 3142 && y == 5489) {
//						player.setNextPosition(new Position(3141, 5480, 0));
//					}
//					if (x == 3142 && y == 5462) {
//						player.setNextPosition(new Position(3154, 5462, 0));
//					}
//					if (x == 3154 && y == 5462) {
//						player.setNextPosition(new Position(3142, 5462, 0));
//					}
//					if (x == 3143 && y == 5443) {
//						player.setNextPosition(new Position(3155, 5449, 0));
//					}
//					if (x == 3155 && y == 5449) {
//						player.setNextPosition(new Position(3143, 5443, 0));
//					}
//					if (x == 3307 && y == 5496) {
//						player.setNextPosition(new Position(3317, 5496, 0));
//					}
//					if (x == 3317 && y == 5496) {
//						player.setNextPosition(new Position(3307, 5496, 0));
//					}
//					if (x == 3318 && y == 5481) {
//						player.setNextPosition(new Position(3322, 5480, 0));
//					}
//					if (x == 3322 && y == 5480) {
//						player.setNextPosition(new Position(3318, 5481, 0));
//					}
//					if (x == 3299 && y == 5484) {
//						player.setNextPosition(new Position(3303, 5477, 0));
//					}
//					if (x == 3303 && y == 5477) {
//						player.setNextPosition(new Position(3299, 5484, 0));
//					}
//					if (x == 3286 && y == 5470) {
//						player.setNextPosition(new Position(3285, 5474, 0));
//					}
//					if (x == 3285 && y == 5474) {
//						player.setNextPosition(new Position(3286, 5470, 0));
//					}
//					if (x == 3290 && y == 5463) {
//						player.setNextPosition(new Position(3302, 5469, 0));
//					}
//					if (x == 3302 && y == 5469) {
//						player.setNextPosition(new Position(3290, 5463, 0));
//					}
//					if (x == 3296 && y == 5455) {
//						player.setNextPosition(new Position(3299, 5450, 0));
//					}
//					if (x == 3299 && y == 5450) {
//						player.setNextPosition(new Position(3296, 5455, 0));
//					}
//					if (x == 3280 && y == 5501) {
//						player.setNextPosition(new Position(3285, 5508, 0));
//					}
//					if (x == 3285 && y == 5508) {
//						player.setNextPosition(new Position(3280, 5501, 0));
//					}
//					if (x == 3300 && y == 5514) {
//						player.setNextPosition(new Position(3297, 5510, 0));
//					}
//					if (x == 3297 && y == 5510) {
//						player.setNextPosition(new Position(3300, 5514, 0));
//					}
//					if (x == 3289 && y == 5533) {
//						player.setNextPosition(new Position(3288, 5536, 0));
//					}
//					if (x == 3288 && y == 5536) {
//						player.setNextPosition(new Position(3289, 5533, 0));
//					}
//					if (x == 3285 && y == 5527) {
//						player.setNextPosition(new Position(3282, 5531, 0));
//					}
//					if (x == 3282 && y == 5531) {
//						player.setNextPosition(new Position(3285, 5527, 0));
//					}
//					if (x == 3325 && y == 5518) {
//						player.setNextPosition(new Position(3323, 5531, 0));
//					}
//					if (x == 3323 && y == 5531) {
//						player.setNextPosition(new Position(3325, 5518, 0));
//					}
//					if (x == 3299 && y == 5533) {
//						player.setNextPosition(new Position(3297, 5536, 0));
//					}
//					if (x == 3297 && y == 5538) {
//						player.setNextPosition(new Position(3299, 5533, 0));
//					}
//					if (x == 3321 && y == 5554) {
//						player.setNextPosition(new Position(3315, 5552, 0));
//					}
//					if (x == 3315 && y == 5552) {
//						player.setNextPosition(new Position(3321, 5554, 0));
//					}
//					if (x == 3291 && y == 5555) {
//						player.setNextPosition(new Position(3285, 5556, 0));
//					}
//					if (x == 3285 && y == 5556) {
//						player.setNextPosition(new Position(3291, 5555, 0));
//					}
//					if (x == 3266 && y == 5552) {
//						player.setNextPosition(new Position(3262, 5552, 0));
//					}
//					if (x == 3262 && y == 5552) {
//						player.setNextPosition(new Position(3266, 5552, 0));
//					}
//					if (x == 3256 && y == 5561) {
//						player.setNextPosition(new Position(3253, 5561, 0));
//					}
//					if (x == 3253 && y == 5561) {
//						player.setNextPosition(new Position(3256, 5561, 0));
//					}
//					if (x == 3249 && y == 5546) {
//						player.setNextPosition(new Position(3252, 5543, 0));
//					}
//					if (x == 3252 && y == 5543) {
//						player.setNextPosition(new Position(3249, 5546, 0));
//					}
//					if (x == 3261 && y == 5536) {
//						player.setNextPosition(new Position(3268, 5534, 0));
//					}
//					if (x == 3268 && y == 5534) {
//						player.setNextPosition(new Position(3261, 5536, 0));
//					}
//					if (x == 3243 && y == 5526) {
//						player.setNextPosition(new Position(3241, 5529, 0));
//					}
//					if (x == 3241 && y == 5529) {
//						player.setNextPosition(new Position(3243, 5526, 0));
//					}
//					if (x == 3230 && y == 5547) {
//						player.setNextPosition(new Position(3226, 5553, 0));
//					}
//					if (x == 3226 && y == 5553) {
//						player.setNextPosition(new Position(3230, 5547, 0));
//					}
//					if (x == 3206 && y == 5553) {
//						player.setNextPosition(new Position(3204, 5546, 0));
//					}
//					if (x == 3204 && y == 5546) {
//						player.setNextPosition(new Position(3206, 5553, 0));
//					}
//					if (x == 3211 && y == 5533) {
//						player.setNextPosition(new Position(3214, 5533, 0));
//					}
//					if (x == 3214 && y == 5533) {
//						player.setNextPosition(new Position(3211, 5533, 0));
//					}
//					if (x == 3208 && y == 5527) {
//						player.setNextPosition(new Position(3211, 5523, 0));
//					}
//					if (x == 3211 && y == 5523) {
//						player.setNextPosition(new Position(3208, 5527, 0));
//					}
//					if (x == 3201 && y == 5531) {
//						player.setNextPosition(new Position(3197, 5529, 0));
//					}
//					if (x == 3197 && y == 5529) {
//						player.setNextPosition(new Position(3201, 5531, 0));
//					}
//					if (x == 3202 && y == 5515) {
//						player.setNextPosition(new Position(3196, 5512, 0));
//					}
//					if (x == 3196 && y == 5512) {
//						player.setNextPosition(new Position(3202, 5515, 0));
//					}
//					if (x == 3190 && y == 5515) {
//						player.setNextPosition(new Position(3190, 5519, 0));
//					}
//					if (x == 3190 && y == 5519) {
//						player.setNextPosition(new Position(3190, 5515, 0));
//					}
//					if (x == 3185 && y == 5518) {
//						player.setNextPosition(new Position(3181, 5517, 0));
//					}
//					if (x == 3181 && y == 5517) {
//						player.setNextPosition(new Position(3185, 5518, 0));
//					}
//					if (x == 3187 && y == 5531) {
//						player.setNextPosition(new Position(3182, 5530, 0));
//					}
//					if (x == 3182 && y == 5530) {
//						player.setNextPosition(new Position(3187, 5531, 0));
//					}
//					if (x == 3169 && y == 5510) {
//						player.setNextPosition(new Position(3159, 5501, 0));
//					}
//					if (x == 3159 && y == 5501) {
//						player.setNextPosition(new Position(3169, 5510, 0));
//					}
//					if (x == 3165 && y == 5515) {
//						player.setNextPosition(new Position(3173, 5530, 0));
//					}
//					if (x == 3173 && y == 5530) {
//						player.setNextPosition(new Position(3165, 5515, 0));
//					}
//					if (x == 3156 && y == 5523) {
//						player.setNextPosition(new Position(3152, 5520, 0));
//					}
//					if (x == 3152 && y == 5520) {
//						player.setNextPosition(new Position(3156, 5523, 0));
//					}
//					if (x == 3148 && y == 5533) {
//						player.setNextPosition(new Position(3153, 5537, 0));
//					}
//					if (x == 3153 && y == 5537) {
//						player.setNextPosition(new Position(3148, 5533, 0));
//					}
//					if (x == 3143 && y == 5535) {
//						player.setNextPosition(new Position(3147, 5541, 0));
//					}
//					if (x == 3147 && y == 5541) {
//						player.setNextPosition(new Position(3143, 5535, 0));
//					}
//					if (x == 3168 && y == 5541) {
//						player.setNextPosition(new Position(3171, 5542, 0));
//					}
//					if (x == 3171 && y == 5542) {
//						player.setNextPosition(new Position(3168, 5541, 0));
//					}
//					if (x == 3190 && y == 5549) {
//						player.setNextPosition(new Position(3190, 5554, 0));
//					}
//					if (x == 3190 && y == 5554) {
//						player.setNextPosition(new Position(3190, 5549, 0));
//					}
//					if (x == 3180 && y == 5557) {
//						player.setNextPosition(new Position(3174, 5558, 0));
//					}
//					if (x == 3174 && y == 5558) {
//						player.setNextPosition(new Position(3180, 5557, 0));
//					}
//					if (x == 3162 && y == 5557) {
//						player.setNextPosition(new Position(3158, 5561, 0));
//					}
//					if (x == 3158 && y == 5561) {
//						player.setNextPosition(new Position(3162, 5557, 0));
//					}
//					if (x == 3166 && y == 5553) {
//						player.setNextPosition(new Position(3162, 5545, 0));
//					}
//					if (x == 3162 && y == 5545) {
//						player.setNextPosition(new Position(3166, 5553, 0));
//					}
//					if (x == 3142 && y == 5545) {
//						if (player.getRights() == 2) {
//							player.setNextPosition(new Position(3115, 5528, 0));
//						} else {
//							player.sm("Sorry this zone is unavalible for players at the moment.");// bork
//							return;
//						}
//					}
//					if (x == 3115 && y == 5528) {
//						player.setNextPosition(new Position(3142, 5545, 0));
//						// player.setNextGraphics(new Graphics(6));
//					}
//					player.setNextGraphics(new Graphics(2646));
//					return;
//				}
				// BarbarianOutpostAgility start
				else if (id == 20210) {
					BarbarianOutpostAgility.enterObstaclePipe(player, object);
				} else if (id == 43595 && x == 2550 && y == 3546) {
					BarbarianOutpostAgility.walkAcrossLogBalance(player, object);
				} else if (id == 20211 && x == 2538 && y == 3545) {
					BarbarianOutpostAgility.climbObstacleNet(player, object);
				} else if (id == 2302 && x == 2535 && y == 3547) {
					BarbarianOutpostAgility.walkAcrossBalancingLedge(player, object);
				} else if (id == 1948) {
					BarbarianOutpostAgility.climbOverCrumblingWall(player, object);
				} else if (id == 43533) {
					BarbarianOutpostAgility.runUpWall(player, object);
				} else if (id == 43597) {
					BarbarianOutpostAgility.climbUpWall(player, object);
				} else if (id == 43587) {
					BarbarianOutpostAgility.fireSpringDevice(player, object);
				} else if (id == 43527) {
					BarbarianOutpostAgility.crossBalanceBeam(player, object);
				} else if (id == 43531) {
					BarbarianOutpostAgility.jumpOverGap(player, object);
				} else if (id == 43532) {
					BarbarianOutpostAgility.slideDownRoof(player, object);
				} else if (id == 64698) {
					WildernessAgility.walkAcrossLogBalance(player, object);
				} else if (id == 64699) {
					WildernessAgility.jumpSteppingStones(player, object);
				} else if (id == 65362) {
					WildernessAgility.enterWildernessPipe(player, object.getX(), object.getY());
				} else if (id == 65734) {
					WildernessAgility.climbUpWall(player, object);
				} else if (id == 64696) {
					WildernessAgility.swingOnRopeSwing(player, object);
				} else if (id == 65365) {
					WildernessAgility.enterWildernessCourse(player);
				} else if (id == 65367) {
					WildernessAgility.exitWildernessCourse(player);
				} else if (id == 2491) {
					player.getActionManager()
							.setAction(new EssenceMining(object,
									player.getSkills().getLevel(Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
											: EssenceDefinitions.Pure_Essence));
				} else if (id == 2478) {
					Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2, 22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77,
							88, 9, 99, 10);
				} else if (id == 2479) {
					Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14, 2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98,
							8);
				} else if (id == 2480) {
					Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2, 38, 3, 57, 4, 76, 5, 95, 6);
				} else if (id == 2481) {
					Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26, 2, 52, 3, 78, 4);
				} else if (id == 2482) {
					Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2, 70, 3);
				} else if (id == 2483) {
					Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46, 2, 92, 3);
				} else if (id == 2484) {
					Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
				} else if (id == 2487) {
					Runecrafting.craftEssence(player, 562, 35, 8.5, true, 74, 2);
				} else if (id == 17010) {
					Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82, 2);
				} else if (id == 2486) {
					Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
				} else if (id == 2485) {
					Runecrafting.craftEssence(player, 563, 50, 9.5, true);
				} else if (id == 2488) {
					Runecrafting.craftEssence(player, 560, 65, 10, true);
				} else if (id == 30624) {
					Runecrafting.craftEssence(player, 565, 77, 10.5, true);
				} else if (id == 26847) {
					Runecrafting.craftEssence(player, 566, 90, 13.2, true);
				} else if (id == 2452) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.AIR_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1438, 1)) {
						Runecrafting.enterAirAltar(player);
					}
				} else if (id == 2455) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.EARTH_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1440, 1)) {
						Runecrafting.enterEarthAltar(player);
					}
				} else if (id == 2456) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.FIRE_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1442, 1)) {
						Runecrafting.enterFireAltar(player);
					}
				} else if (id == 2454) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.WATER_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1444, 1)) {
						Runecrafting.enterWaterAltar(player);
					}
				} else if (id == 2457) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.BODY_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1446, 1)) {
						Runecrafting.enterBodyAltar(player);
					}
				} else if (id == 2453) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.MIND_TIARA || hatId == Runecrafting.OMNI_TIARA
							|| player.getInventory().containsItem(1448, 1)) {
						Runecrafting.enterMindAltar(player);
					}
				} else if (id == 47120) { // zaros altar
					// recharge if needed
					if (player.getPrayer().getPrayerpoints() < player.getSkills().getLevelForXp(Skills.PRAYER) * 10) {
						player.lock(12);
						player.animate(new Animation(12563));
						player.getPrayer()
								.setPrayerpoints((int) (player.getSkills().getLevelForXp(Skills.PRAYER) * 10 * 1.15));
						player.getPrayer().refreshPrayerPoints();
					}
					player.getDialogueManager().startDialogue("ZarosAltar");
				} else if (id == WorldObject.asOSRS(29422)) {
					SpiritTree.sendSpiritTreeInterface(player);
				} else if (id == 1317 || id == 68973) {
					player.getDialogueManager().startDialogue("SpiritTreeDialogue", object.getId());
				} else if (id == 68974) {
					player.getDialogueManager().startDialogue("MainSpiritTreeDialogue", object.getId());
				} else if (id == 19222) {
					Falconry.beginFalconry(player);
				} else if (id == 36786) {
					player.getDialogueManager().startDialogue("Banker", 4907);
				} else if (id == 42377 || id == 42378) {
					player.getDialogueManager().startDialogue("Banker", 2759);
				} else if (id == 42217 || id == 782 || id == 34752) {
					player.getDialogueManager().startDialogue("Banker", 553);
				} else if (id == 57437) {
					player.getBank().openBank();
				} else if (id == 9319 && object.getX() == 3422 && object.getY() == 3550) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 60) {
						player.useStairs(828, new Position(player.getX(), player.getY(), player.getZ() + 1), 2, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 61 agility to climb this.");
					}
				} else if (id == 4493 && object.getX() == 3434 && object.getY() == 3537) {
					player.setNextPosition(new Position(3433, 3538, 1));
				} else if (id == 5282) {
					player.getEctophial().refillEctophial(player);
				}
				// StrongHold of security
				else if (id == 16048 || id == 16065 || id == 16089 || id == 16090 || id == 16066 || id == 16043
						|| id == 16044 || id == 16154 || id == 16148 || id == 16152 || id == 16124 || id == 16123
						|| id == 16135 || id == 16077 || id == 16118 || id == 16047 || id == 16149 || id == 16080
						|| id == 16081 || id == 16114 || id == 16115 || id == 16049 || id == 16150 || id == 16082
						|| id == 16116 || id == 16050) {
					StrongHold.HandleObject(player, object);
				}

				/**
				 * PolyPore Dungeon
				 *
				 */
				else if (id == 2473) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4626, 5457, 0));

				} else if (id == 2473) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4626, 5457, 0));

				} else if (id == 63094) {
					player.setNextPosition(new Position(3410, 3328, 0));
				} else if (id == 63093) {
					player.getDialogueManager().startDialogue("enterPolypore");
				} else if (id == 64360 && object.getX() == 4629 && object.getY() == 5453) {
					player.useStairs(827, new Position(4629, 5451, 2), 1, 2);

				} else if (id == 64361 && object.getX() == 4629 && object.getY() == 5452) {
					player.useStairs(828, new Position(4629, 5454, 3), 1, 2);

				}

				else if (id == 64359 && object.getX() == 4691 && object.getY() == 5469) {
					player.useStairs(827, new Position(4691, 5469, 2), 1, 2);

				} else if (id == 64361 && object.getX() == 4691 && object.getY() == 5468) {
					player.useStairs(828, new Position(4691, 5470, 3), 1, 2);

				}

				else if (id == 64359 && object.getX() == 4698 && object.getY() == 5459) {
					player.useStairs(827, new Position(4698, 5459, 2), 1, 2);

				} else if (id == 64361 && object.getX() == 4699 && object.getY() == 5459) {
					player.useStairs(828, new Position(4697, 5459, 3), 1, 2);

				}

				else if (id == 64359 && object.getX() == 4632 && object.getY() == 5409) {
					player.useStairs(827, new Position(4632, 5409, 2), 1, 2);

				} else if (id == 64361 && object.getX() == 4633 && object.getY() == 5409) {
					player.useStairs(828, new Position(4631, 5409, 3), 1, 2);

				}
				// resource dungeon
				else if (id == 64360 && object.getX() == 4696 && object.getY() == 5618) {
					player.useStairs(827, new Position(4695, 5618, 2), 1, 2);

				} else if (id == 64361 && object.getX() == 4696 && object.getY() == 5617) {
					player.useStairs(828, new Position(4696, 5619, 3), 1, 2);

				}

				else if (id == 64359 && object.getX() == 4684 && object.getY() == 5586) {
					player.useStairs(827, new Position(4684, 5586, 2), 1, 2);

				} else if (id == 64361 && object.getX() == 4684 && object.getY() == 5587) {
					player.useStairs(828, new Position(4684, 5585, 3), 1, 2);

				}

				// next level
				else if (id == 64359 && object.getX() == 4632 && object.getY() == 5443) {
					player.useStairs(827, new Position(4632, 5443, 1), 1, 2);

				} else if (id == 64361 && object.getX() == 4632 && object.getY() == 5442) {
					player.useStairs(828, new Position(4632, 5444, 2), 1, 2);

				}

				else if (id == 64359 && object.getX() == 4642 && object.getY() == 5389) {
					player.useStairs(827, new Position(4642, 5389, 1), 1, 2);

				} else if (id == 64361 && object.getX() == 4643 && object.getY() == 5389) {
					player.useStairs(828, new Position(4641, 5389, 2), 1, 2);

				}

				else if (id == 64359 && object.getX() == 4705 && object.getY() == 5460) {
					player.useStairs(827, new Position(4705, 5460, 1), 1, 2);

				} else if (id == 64361 && object.getX() == 4705 && object.getY() == 5461) {
					player.useStairs(828, new Position(4705, 5459, 2), 1, 2);

				}

				else if (id == 64359 && object.getX() == 4689 && object.getY() == 5479) {
					player.useStairs(827, new Position(4689, 5479, 1), 1, 2);

				} else if (id == 64361 && object.getX() == 4689 && object.getY() == 5480) {
					player.useStairs(828, new Position(4689, 5478, 2), 1, 2);

				} else if (id == 64359 && object.getX() == 4699 && object.getY() == 5617) {
					player.useStairs(827, new Position(4699, 5617, 1), 1, 2);

				} else if (id == 64361 && object.getX() == 4698 && object.getY() == 5617) {
					player.useStairs(828, new Position(4700, 5617, 2), 1, 2);

				} else if (id == 64359 && object.getX() == 4721 && object.getY() == 5602) {
					player.useStairs(827, new Position(4721, 5602, 1), 1, 2);

				} else if (id == 64361 && object.getX() == 4720 && object.getY() == 5602) {
					player.useStairs(828, new Position(4722, 5602, 2), 1, 2);

				} else if (id == 64359 && object.getX() == 4718 && object.getY() == 5467) {
					player.useStairs(827, new Position(4718, 5467, 0), 1, 2);

				} else if (id == 64361 && object.getX() == 4718 && object.getY() == 5466) {
					player.useStairs(828, new Position(4718, 5468, 1), 1, 2);

				} else if (id == 64359 && object.getX() == 4652 && object.getY() == 5388) {
					player.useStairs(827, new Position(4717, 5465, 0), 1, 2);

				} else if (id == 64362 && object.getX() == 4652 && object.getY() == 5387) {
					player.useStairs(828, new Position(4652, 5389, 1), 1, 2);

				} else if (id == 64359 && object.getX() == 4702 && object.getY() == 5612) {
					player.useStairs(827, new Position(4702, 5612, 0), 1, 2);

				} else if (id == 64361 && object.getX() == 4702 && object.getY() == 5611) {
					player.useStairs(828, new Position(4702, 5613, 1), 1, 2);

				}

				// Shortcut
				else if (id == 64294 && object.getX() == 4659 && object.getY() == 5476) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 72) {
						player.useStairs(3067, new Position(4663, 5476, 3), 1, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 73 agility to jump this.");
					}
				} else if (id == 64295 && object.getX() == 4661 && object.getY() == 5476) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 72) {
						player.useStairs(3067, new Position(4658, 5476, 3), 1, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 73 agility to jump this.");
					}
				}
				// 2nd shortcut
				else if (id == 64295 && object.getX() == 4682 && object.getY() == 5476) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 72) {
						player.useStairs(3067, new Position(4685, 5476, 3), 1, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 73 agility to jump this.");
					}
				} else if (id == 64294 && object.getX() == 4684 && object.getY() == 5476) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 72) {
						player.useStairs(3067, new Position(4681, 5476, 3), 1, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 73 agility to jump this.");
					}
				}

				else if (id == 23585 && object.getX() == 2826 && object.getY() == 2998) {
					player.useStairs(827, new Position(2838, 9387, 0), 1, 2);

				} else if (id == 23584 && object.getX() == 2838 && object.getY() == 9388) {
					player.useStairs(828, new Position(2826, 2997, 0), 1, 2);

				}
				// Brimhaven Agil shortcuts
				else if (id == 77424) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 34) {
						player.getPackets().sendGameMessage("You need an Agility level of 34 to use this obstacle.",
								true);
						return;
					}
					int y = player.getY() == 9492 ? 9499 : 9492;
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							player.animate(new Animation(844));
							if (count++ == 1) {
								stop();
							}
						}

					}, 0, 0);
					player.setNextForceMovement(
							new ForceMovement(new Position(2698, y, 0), 3, player.getY() == 9492 ? 1 : 3));
					player.useStairs(844, new Position(2698, y, 0), 3, 4);
				} else if (id == 29375 && (object.getX() == 3120 || object.getX() == 3119) && object.getY() == 9964) {
					player.setNextPosition(new Position(3121, 9970, 0));
					player.sm("You safely get over the bar.");

				} else if (id == 29375 && (object.getX() == 3120 || object.getX() == 3119) && object.getY() == 9969) {
					player.setNextPosition(new Position(3120, 9963, 0));
					player.sm("You safely get over the bar.");
				} else if (id == 77423) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 22) {
						player.getPackets().sendGameMessage("You need an Agility level of 22 to use this obstacle.",
								true);
						return;
					}
					int y = player.getY() == 9566 ? 9573 : 9566;

					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							player.animate(new Animation(844));
							if (count++ == 1) {
								stop();
							}
						}

					}, 0, 0);
					player.setNextForceMovement(
							new ForceMovement(new Position(2655, y, 0), 3, player.getY() == 9566 ? 1 : 3));
					player.useStairs(844, new Position(2655, y, 0), 3, 4);
				}

				else if (id == 77574 || id == 77573) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 30) {
						player.getPackets().sendGameMessage("You need an agility level of 30 to use this obstacle.",
								true);
						return;
					}
					if (object.getX() > player.getX() && object.getY() == player.getY()) {
						player.addWalkSteps(player.getX() + 5, player.getY(), -1, false);
					} else if (object.getX() < player.getX() && object.getY() == player.getY()) {
						player.addWalkSteps(player.getX() - 5, player.getY(), -1, false);
					}

				} else if (id == 77570) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 12) {
						player.getPackets().sendGameMessage("You need an agility level of 12 to use this obstacle.",
								true);
						return;
					}
					player.setNextPosition(new Position(2647, 9557, 0));
				} else if (id == 77572) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 12) {
						player.getPackets().sendGameMessage("You need an agility level of 12 to use this obstacle.",
								true);
						return;
					}
					player.setNextPosition(new Position(2649, 9562, 0));
				}

				/**
				 * Ardougne Walls
				 *
				 */

				else if (id == 9738 || id == 9330) {
					if (object.getX() < player.getX() && object.getY() == player.getY()) {
						player.setNextPosition(new Position(player.getX() - 2, player.getY(), 0));

					} else if (object.getX() > player.getX() && object.getY() == player.getY()) {
						player.setNextPosition(new Position(player.getX() + 2, player.getY(), 0));
					} else {
						player.getPackets().sendGameMessage("You can't reach that.");
						return;
					}
				} else if (id == 77506) {
					player.setNextPosition(new Position(2636, 9509, 2));
				} else if (id == 77507) {
					player.setNextPosition(new Position(2636, 9518, 0));
				} else if (id == 77508) {
					player.setNextPosition(new Position(2643, 9594, 2));
				} else if (id == 77509) {
					player.setNextPosition(new Position(2649, 9591, 0));
				} else if (id == 77371 || id == 77373 || id == 77375 || id == 77377 || id == 77379) {
					if (object.getX() > player.getX() && object.getY() == player.getY()) {
						player.addWalkSteps(player.getX() + 2, player.getY(), -1, false);

					} else if (object.getX() < player.getX() && object.getY() == player.getY()) {
						player.addWalkSteps(player.getX() - 2, player.getY(), -1, false);

					} else if (object.getY() > player.getY()) {
						player.addWalkSteps(player.getX(), player.getY() + 2, -1, false);

					} else if (object.getY() < player.getY()) {
						player.addWalkSteps(player.getX(), player.getY() - 2, -1, false);

					} else {
						player.getPackets().sendGameMessage("You cannot pass through at this angle.");
						return;
					}
				}
				/* Waterfall */
				else if (id == 1987) {
					if (object.getX() == 2509 && object.getY() == 3493) {
						player.getPackets().sendGameMessage("You hop on the log raft..");
						player.useStairs(-1, new Position(2512, 3481, 0), 3, 4);
						player.getPackets().sendGameMessage("..and crash on a small island!");
					}
				} else if (id == 10283) {
					if (object.getX() == 2512 && object.getY() == 3475) {
						if (player.getInventory().containsItem(954, 1)) {
							player.useStairs(-1, new Position(2511, 3467, 0), 3, 4);
						} else {
							player.useStairs(-1, new Position(2527, 3413, 0), 3, 4);
							player.getPackets().sendGameMessage("The waterfall washes you down to the river.");
							player.getPackets().sendGameMessage("Be glad you're still in one piece.");
						}
					}
				} else if (id == 2020) {
					if (object.getX() == 2512 && object.getY() == 3465) {
						if (player.getInventory().containsItem(954, 1)) {
							player.useStairs(-1, new Position(2511, 3463, 0), 3, 4);
						} else {
							player.useStairs(-1, new Position(2527, 3413, 0), 3, 4);
							player.getPackets().sendGameMessage("The waterfall washes you down to the river.");
							player.getPackets().sendGameMessage("Be glad you're still in one piece.");
						}
					}
				} else if (id == 2022) {
					if (object.getX() == 2512 && object.getY() == 3463) {
						player.getPackets().sendGameMessage("You get inside the barrel..");
						player.useStairs(-1, new Position(2527, 3413, 0), 3, 4);
						player.getPackets().sendGameMessage("The waterfall washes you down to the river.");
						player.getPackets().sendGameMessage("Be glad you're still in one piece.");
					}
				} else if (id == 37247) {
					if (object.getX() == 2511 && object.getY() == 3464) {
						player.useStairs(-1, new Position(2575, 9861, 0), 1, 2);
					}
				} else if (id == 32711) {
					if (object.getX() == 2574 && object.getY() == 9860) {
						player.useStairs(-1, new Position(2511, 3463, 0), 1, 2);
					}
				}

				/* ZMI Alter */
				else if (id == 26849 && object.getX() == 2452 && object.getY() == 3231) {
					player.useStairs(827, new Position(3271, 4861, 0), 1, 2);

				} else if (id == 26850 && object.getX() == 3271 && object.getY() == 4862) {
					player.useStairs(828, new Position(2452, 3232, 0), 1, 2);

				}

				else if (id == 1533 && object.getX() == 2551 && object.getY() == 3082) {
					handleDoor(player, object);
				} else if (id == 1600 || id == 1601) {
					handleDoor(player, object);
				}

				if (id == 24991) {
					PuroPuro.enterPuro(player);
				} else if (id == 67966) {
					if (player.barCrawl > 4) {
						player.getPackets().sendGameMessage("You jump into the whirlpool.");
						player.setNextPosition(new Position(1763, 5365, 1));

					} else if (player.searchBox < 5) {
						player.getDialogueManager().startDialogue("SimpleMessage",
								"You should go talk to Otto, south-west of here first.");
						return;
					}

				}
				/* Box of fishing supplies */

				else if (id == 1 && object.getX() == 2502 && object.getY() == 3496) {
					if (player.barCrawl > 2) {
						player.getPackets().sendGameMessage("You find a barbarian rod and some feathers.");
						player.getInventory().addItem(11323, 1);
						player.getInventory().addItem(314, 5);

					} else if (player.searchBox < 3) {
						player.getDialogueManager().startDialogue("SimpleMessage",
								"These supplies don't belong to you.");
						return;
					}

				}

				else if (id == 69197 && player.getX() == 2465 && player.getY() == 3491) {
					player.addWalkSteps(2465, 3493, -1, false);
				} else if (id == 69197 && player.getX() == 2465 && player.getY() == 3493) {
					player.addWalkSteps(2465, 3491, -1, false);
				} else if (id == 69198 && player.getX() == 2466 && player.getY() == 3491) {
					player.addWalkSteps(2466, 3493, -1, false);

				} else if (id == 69198 && player.getX() == 2466 && player.getY() == 3493) {
					player.addWalkSteps(2466, 3491, -1, false);

				}

				// Underground Passage
				else if (id == 36000 && object.getX() == 2433 && object.getY() == 3313) {
					player.useStairs(844, new Position(2496, 9715, 0), 1, 2); // Underground pass entrance
					player.getBank().depositAllEquipment(false);
					player.getBank().depositAllInventory(false);
					player.getPackets().sendGameMessage("You Enter The Underground Passage Minigame");
					player.searchBox = 0;
				} else if (id == 3214 && object.getX() == 2497 && object.getY() == 9714) {
					player.useStairs(844, new Position(2438, 3315, 0), 1, 2); // Underground pass Exit
					player.getInventory().reset();
					player.getEquipment().reset();
					player.getPackets().sendGameMessage("You leave the minigame.");
					player.searchBox = 0;
				}

				// Crate
				else if (id == 1 && object.getX() == 2495 && object.getY() == 9713) {// Basic Supplies Crate

					if (player.searchBox == 0) {
						player.getPackets().sendGameMessage("You find some basic Supplies that may be useful");
						player.getInventory().addItem(952, 1);
						player.getInventory().addItem(954, 3);
						player.getInventory().addItem(361, 2);
						player.getInventory().addItem(841, 1);
						player.getInventory().addItem(882, 45);
						player.getInventory().addItem(1485, 2);
						player.searchBox = 1;
					} else if (player.searchBox == 1) {
						player.getDialogueManager().startDialogue("SimpleMessage", "You have already taken Supplies.");
						return;
					}

				} else if (id == 9662) {
					player.getInventory().addItem(952, 1);

				}
				if (id == 40760) {
					player.sm("This feature is being reworked!");
					// player.getDialogueManager().startDialogue("Noticeboard");
				}
				if (object.getId() == 2563) {
					player.getDialogueManager().startDialogue("Cape");
				}

				/*
				 * Underground pass various objects
				 *
				 */

				else if (id == 3295 && object.getX() == 2480 && object.getY() == 9721) {

					player.getDialogueManager().startDialogue("SimpleMessage", "The script is too old to understand.");
				}
				// rockslide 1
				else if (id == 3309 && object.getX() == 2478 && object.getY() == 9721 && player.getX() == 2479
						&& player.getY() == 9721) {
					player.useStairs(839, new Position(2477, 9721, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2478 && object.getY() == 9721 && player.getX() == 2477
						&& player.getY() == 9721) {
					player.useStairs(839, new Position(2479, 9721, 0), 1, 2); // shortcut
				}
				// rockslide 2
				else if (id == 3309 && object.getX() == 2485 && object.getY() == 9721 && player.getX() == 2485
						&& player.getY() == 9720) {
					player.useStairs(839, new Position(2485, 9722, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2485 && object.getY() == 9721 && player.getX() == 2485
						&& player.getY() == 9722) {
					player.useStairs(839, new Position(2485, 9720, 0), 1, 2); // shortcut
				}
				// Rockslide 3
				else if (id == 3309 && object.getX() == 2478 && object.getY() == 9724 && player.getX() == 2479
						&& player.getY() == 9724) {
					player.useStairs(839, new Position(2477, 9724, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2478 && object.getY() == 9724 && player.getX() == 2477
						&& player.getY() == 9724) {
					player.useStairs(839, new Position(2479, 9724, 0), 1, 2); // shortcut
				}
				// Rockslide 4
				else if (id == 3309 && object.getX() == 2467 && object.getY() == 9723 && player.getX() == 2468
						&& player.getY() == 9723) {
					player.useStairs(839, new Position(2466, 9723, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2467 && object.getY() == 9723 && player.getX() == 2466
						&& player.getY() == 9723) {
					player.useStairs(839, new Position(2468, 9723, 0), 1, 2); // shortcut
				}

				// Rockslide 5
				else if (id == 3309 && object.getX() == 2460 && object.getY() == 9720 && player.getX() == 2460
						&& player.getY() == 9721) {
					player.useStairs(839, new Position(2460, 9719, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2460 && object.getY() == 9720 && player.getX() == 2460
						&& player.getY() == 9719) {
					player.useStairs(839, new Position(2460, 9721, 0), 1, 2); // shortcut
				}
				// Rockslide 6
				else if (id == 3309 && object.getX() == 2458 && object.getY() == 9712 && player.getX() == 2458
						&& player.getY() == 9713) {
					player.useStairs(839, new Position(2458, 9711, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2458 && object.getY() == 9712 && player.getX() == 2458
						&& player.getY() == 9711) {
					player.useStairs(839, new Position(2458, 9713, 0), 1, 2); // shortcut
				}

				else if (id == 3309 && object.getX() == 2457 && object.getY() == 9712 && player.getX() == 2457
						&& player.getY() == 9713) {
					player.useStairs(839, new Position(2457, 9711, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2457 && object.getY() == 9712 && player.getX() == 2457
						&& player.getY() == 9711) {
					player.useStairs(839, new Position(2457, 9713, 0), 1, 2); // shortcut
				}

				// rockslide 7
				else if (id == 3309 && object.getX() == 2471 && object.getY() == 9706 && player.getX() == 2470
						&& player.getY() == 9706) {
					player.useStairs(839, new Position(2472, 9706, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2471 && object.getY() == 9706 && player.getX() == 2472
						&& player.getY() == 9706) {
					player.useStairs(839, new Position(2470, 9706, 0), 1, 2); // shortcut
				}
				// rockslide 8
				else if (id == 3309 && object.getX() == 2480 && object.getY() == 9713 && player.getX() == 2480
						&& player.getY() == 9712) {
					player.useStairs(839, new Position(2480, 9714, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2480 && object.getY() == 9713 && player.getX() == 2480
						&& player.getY() == 9714) {
					player.useStairs(839, new Position(2480, 9712, 0), 1, 2); // shortcut
				}
				// Rockslide 9
				else if (id == 3309 && object.getX() == 2491 && object.getY() == 9691 && player.getX() == 2491
						&& player.getY() == 9692) {
					player.useStairs(839, new Position(2491, 9690, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2491 && object.getY() == 9691 && player.getX() == 2491
						&& player.getY() == 9690) {
					player.useStairs(839, new Position(2491, 9692, 0), 1, 2); // shortcut
				}
				// Rockslide 10
				else if (id == 3309 && object.getX() == 2482 && object.getY() == 9679 && player.getX() == 2483
						&& player.getY() == 9679) {
					player.useStairs(839, new Position(2481, 9679, 0), 1, 2); // shortcut
				} else if (id == 3309 && object.getX() == 2482 && object.getY() == 9679 && player.getX() == 2481
						&& player.getY() == 9679) {
					player.useStairs(839, new Position(2483, 9679, 0), 1, 2); // shortcut
				}

				// Lever gate
				else if (id == 3337 && object.getX() == 2466 && object.getY() == 9672) {
					player.useStairs(2140, new Position(2464, 9677, 0), 1, 2); // lever
					player.getPackets().sendGameMessage("You pull the lever and it teleports you through the gate.");
				}

				/*
				 * Swamp
				 */

				else if (id == 3263) {
					player.getPackets().sendGameMessage("If You Go In Me...You Die.");
				}

				// Bridge (fix it so its better)
				else if (id == 3241 && object.getX() == 2436 && object.getY() == 9716) {
					player.useStairs(2140, new Position(2449, 9716, 0), 1, 2); // lever
					player.getPackets().sendGameMessage("You pull the lever and teleport across");
				} else if (id == 3241 && object.getX() == 2448 && object.getY() == 9717) {
					player.useStairs(2140, new Position(2442, 9716, 0), 1, 2); // lever
					player.getPackets().sendGameMessage("You pull the lever and teleport across");
				}

				else if (id == 36746 && object.getX() == 2461 && object.getY() == 9692) {

					if (player.getX() != 2464) {
						player.getPackets().sendGameMessage("You'll need to get closer to make this jump.");
						return;
					}
					player.lock(4);
					player.animate(new Animation(751));
					World.sendObjectAnimation(player, object, new Animation(497));
					final Position toTile = new Position(2460, object.getY(), object.getZ());
					player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.WEST));

					player.getPackets().sendGameMessage("You skilfully swing across.", true);
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							player.setNextPosition(toTile);
							// setStage(player, 0);
						}

					}, 1);
				}

				// box
				else if (id == 2620) {
					Thieving.handleStalls(player, object);
					player.getPackets().sendGameMessage("you search le box and find le coin.");

				}
				/*
				 * else if ((id == 1)) { player.applyHit(new Hit(player, 2000,
				 * HitLook.REGULAR_DAMAGE));
				 * player.getPackets().sendGameMessage("You try to search the box and die."); }
				 */

				/* Temple of light + Dark beasts */
				else if (id == 10015 && object.getX() == 1902 && object.getY() == 4638) {
					player.setNextPosition(new Position(1901, 4639, 1));
				} else if (id == 10016 && object.getX() == 1902 && object.getY() == 4638) {
					player.setNextPosition(new Position(1905, 4639, 0));
				} else if (id == 10016 && object.getX() == 1890 && object.getY() == 4635) {
					player.setNextPosition(new Position(1891, 4638, 1));
				} else if (id == 10015 && object.getX() == 1890 && object.getY() == 4635) {
					player.setNextPosition(new Position(1891, 4634, 2));
				} else if (id == 10015 && object.getX() == 1890 && object.getY() == 4641) {
					player.setNextPosition(new Position(1891, 4644, 2));
				} else if (id == 10016 && object.getX() == 1890 && object.getY() == 4641) {
					player.setNextPosition(new Position(1891, 4640, 1));
				} else if (id == 10016 && object.getX() == 1887 && object.getY() == 4638) {
					player.setNextPosition(new Position(1886, 4639, 0));
				} else if (id == 10015 && object.getX() == 1887 && object.getY() == 4638) {
					player.setNextPosition(new Position(1890, 4639, 1));
				}

				/* Zogre Area + Slash Bash */
				else if (id == 6881 || id == 6841 || id == 6842 || id == 6897 || id == 6848 || id == 6871
						|| id == 6872) {
					ZogreArea.HandleObject(player, object);
				}

				// entrance
				else if (id == 8785 && object.getX() == 2044 && object.getY() == 4650) {
					player.useStairs(828, new Position(2543, 3327, 0), 1, 2); // entrance to temple
				} else if (id == 8783 && object.getX() == 2542 && object.getY() == 3327) {
					player.useStairs(827, new Position(2044, 4649, 0), 1, 2); //
				}

				/* Haunted Mine */
				else if (id == 12776 && object.getX() == 3474 && object.getY() == 3221 && player.getX() == 3474
						&& player.getY() == 3221) {
					player.useStairs(839, new Position(3473, 3221, 0), 1, 2); // shortcut in burgh
				} else if (id == 12776 && object.getX() == 3474 && object.getY() == 3221 && player.getX() == 3473
						&& player.getY() == 3221) {
					player.useStairs(839, new Position(3474, 3221, 0), 1, 2); // shortcut in burgh
				} else if (id == 4923 && object.getX() == 2790 && object.getY() == 4589) {
					player.setNextPosition(new Position(3453, 3242, 0));
				} else if (id == 4919 && object.getX() == 3452 && object.getY() == 3243) {
					player.setNextPosition(new Position(2791, 4592, 0));
				} else if (id == 4964 && object.getX() == 2794 && object.getY() == 4594) {
					player.getPackets().sendGameMessage("The door does not open...");
				} else if (id == 4963 && object.getX() == 2789 && object.getY() == 4593) {
					player.getPackets().sendGameMessage("The door does not open...");
				} else if (id == 4963 && object.getX() == 2794 && object.getY() == 4595) {
					player.getPackets().sendGameMessage("The door does not open...");
				} else if (id == 4964 && object.getX() == 2789 && object.getY() == 4594) {
					player.getPackets().sendGameMessage("The door does not open...");
				}

				// Minecarts in haunted mine
				else if (id == 4918 && object.getX() == 3445 && object.getY() == 3236 && player.getX() == 3446
						&& player.getY() == 3236) {
					player.useStairs(839, new Position(3444, 3236, 0), 1, 2); // shortcut in burgh
				} else if (id == 4918 && object.getX() == 3445 && object.getY() == 3236 && player.getX() == 3444
						&& player.getY() == 3236) {
					player.useStairs(839, new Position(3446, 3236, 0), 1, 2); // shortcut in burgh
				}
				// Haunted mine entrance
				else if (id == 4913 && object.getX() == 3440 && object.getY() == 3232) {
					player.useStairs(844, new Position(3436, 9637, 0), 1, 2); // minecart passage
				} else if (id == 4920 && object.getX() == 3437 && object.getY() == 9637) {
					player.useStairs(844, new Position(3441, 3232, 0), 1, 2); // minecart passage exit
				} else if (id == 4921 && object.getX() == 3404 && object.getY() == 9631) {
					player.useStairs(844, new Position(3429, 3233, 0), 1, 2); // minecart passage exit
				} else if (id == 4914 && object.getX() == 3430 && object.getY() == 3233) {
					player.useStairs(844, new Position(3405, 9631, 0), 1, 2); // minecart passage
				} else if (id == 4915 && object.getX() == 3429 && object.getY() == 3225) {
					player.useStairs(844, new Position(3409, 9623, 0), 1, 2); // minecart passage
				} else if (id == 20524 && object.getX() == 3408 && object.getY() == 9623) {
					player.useStairs(844, new Position(3428, 3225, 0), 1, 2); // minecart passage
				} else if (id == 4965 && object.getX() == 3413 && object.getY() == 9633) {
					player.useStairs(827, new Position(2772, 4577, 0), 1, 2);
				} else if (id == 4966 && object.getX() == 2773 && object.getY() == 4577) {
					player.useStairs(828, new Position(3412, 9633, 0), 1, 2);
				} else if (id == 4965 && object.getX() == 3422 && object.getY() == 9625) {
					player.useStairs(827, new Position(2783, 4569, 0), 1, 2);
				} else if (id == 4966 && object.getX() == 2782 && object.getY() == 4569) {
					player.useStairs(828, new Position(3423, 9625, 0), 1, 2);
				}
				// ladders in haunted mine
				else if (id == 4969 && object.getX() == 2797 && object.getY() == 4599) {
					player.useStairs(827, new Position(2733, 4534, 0), 1, 2);
				} else if (id == 4970 && object.getX() == 2733 && object.getY() == 4535) {
					player.useStairs(828, new Position(2797, 4598, 0), 1, 2);
				} else if (id == 4969 && object.getX() == 2798 && object.getY() == 4567) {
					player.useStairs(827, new Position(2733, 4503, 0), 1, 2);
				} else if (id == 4970 && object.getX() == 2734 && object.getY() == 4503) {
					player.useStairs(828, new Position(2797, 4567, 0), 1, 2);
				} else if (id == 4967 && object.getX() == 2725 && object.getY() == 4486) {
					player.useStairs(827, new Position(2788, 4486, 0), 1, 2);
				} else if (id == 4968 && object.getX() == 2789 && object.getY() == 4486) {
					player.useStairs(828, new Position(2724, 4486, 0), 1, 2);
				} else if (id == 4967 && object.getX() == 2732 && object.getY() == 4529) {
					player.useStairs(827, new Position(2797, 4529, 0), 1, 2);
				} else if (id == 4968 && object.getX() == 2796 && object.getY() == 4529) {
					player.useStairs(828, new Position(2733, 4529, 0), 1, 2);
				} else if (id == 4967 && object.getX() == 2710 && object.getY() == 4540) {
					player.useStairs(827, new Position(2773, 4540, 0), 1, 2);
				} else if (id == 4968 && object.getX() == 2774 && object.getY() == 4540) {
					player.useStairs(828, new Position(2709, 4540, 0), 1, 2);
				} else if (id == 4967 && object.getX() == 2696 && object.getY() == 4497) {
					player.useStairs(827, new Position(2760, 4496, 0), 1, 2);
				} else if (id == 4968 && object.getX() == 2760 && object.getY() == 4497) {
					player.useStairs(828, new Position(2696, 4496, 0), 1, 2);

				}

				else if (id == 2324 && object.getX() == 2511 && object.getY() == 3090) {
					player.setNextPosition(new Position(2511, 3092, 0));

				} else if (id == 2790 && object.getX() == 2508 && object.getY() == 3804) {

					// player.getInventory().addItemMoneyPouch(new Item(995, 100000000));
					// player.getInventory().addItem(4278, 100000000);

					return;
				}

				// Haunted mine final staircases
				else if (id == 4971 && object.getX() == 2746 && object.getY() == 4436) {
					player.setNextPosition(new Position(2811, 4453, 0));
					player.getPackets().sendGameMessage("You walk down the big stairs.");
				} else if (id == 4973 && object.getX() == 2812 && object.getY() == 4452) {
					player.setNextPosition(new Position(2750, 4437, 0));
					player.getPackets().sendGameMessage("You walk up the big stairs");
				} else if (id == 4971 && object.getX() == 2692 && object.getY() == 4436) {
					player.setNextPosition(new Position(2758, 4453, 0));
					player.getPackets().sendGameMessage("You walk down the big stairs.");
				} else if (id == 4973 && object.getX() == 2755 && object.getY() == 4452) {
					player.setNextPosition(new Position(2691, 4437, 0));
					player.getPackets().sendGameMessage("You walk up the big stairs");
					// Start of Runecrafting Abyss Entrances
				} else if (id == 7133) { // nature rift
					player.setNextPosition(new Position(2398, 4841, 0));
				} else if (id == 7132) { // cosmic rift
					player.setNextPosition(new Position(2162, 4833, 0));
				} else if (id == 7141) { // blood rift
					player.setNextPosition(new Position(2462, 4891, 1));
				} else if (id == 7129) { // fire rift
					player.setNextPosition(new Position(2584, 4836, 0));
				} else if (id == 7130) { // earth rift
					player.setNextPosition(new Position(2660, 4839, 0));
				} else if (id == 7131) { // body rift
					player.setNextPosition(new Position(2527, 4833, 0));
				} else if (id == 7140) { // mind rift
					player.setNextPosition(new Position(2794, 4830, 0));
				} else if (id == 7139) { // air rift
					player.setNextPosition(new Position(2845, 4832, 0));
				} else if (id == 7137) { // water rift
					player.setNextPosition(new Position(3482, 4836, 0));
				} else if (id == 7136) { // death rift
					player.setNextPosition(new Position(2207, 4836, 0));
				} else if (id == 7135) { // law rift
					player.setNextPosition(new Position(2464, 4834, 0));
				} else if (id == 7134) { // chaotic rift
					player.setNextPosition(new Position(2269, 4843, 0));
					// End of Runecrafting Abyss Exits
				}
				/* Haunted mine boss fight and salve amulet */
				else if (id == 4962 && object.getX() == 2799 && object.getY() == 4453) {
					player.getPackets().sendGameMessage("This minigame is not complete yet.");
				}

				// haunted mine glowing fungus
				else if (id == 4933) {
					player.getInventory().addItem(4075, 1);
					player.getPackets().sendGameMessage("You pick some glowing fungus.");
				}
				// haunted mine lift
				else if (id == 4938 && object.getX() == 2807 && object.getY() == 4492) {
					if (player.getInventory().containsItem(4075, 1)) {
						player.getPackets()
								.sendGameMessage("The lift breaks as it hits the bottom, and you swim to land.");
						player.setNextPosition(new Position(2724, 4452, 0));
					} else if (!player.getInventory().containsItem(4075, 1)) {
						player.getDialogueManager().startDialogue("SimpleMessage", "It looks too dark to go down.");
						player.getPackets()
								.sendGameMessage("Perhaps you should find a light source somewhere before proceeding.");
						return;
					}
				}
				//player.sm(""+id);
				 if (id == 48496) {
					if (player.getFamiliar() != null || player.getPet() != null || Summoning.hasPouch(player)
							|| Pets.hasPet(player)) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage", 1,
								"No familiars or pets allowed!");
						return;
					} else {
						//player.sm("xDD");
						player.getDungeoneeringManager().enterDungeon(true, false, false);
					}
				} else if (id == 4937 && object.getX() == 2807 && object.getY() == 4494) {
					if (player.getInventory().containsItem(4075, 1)) {
						player.getPackets()
								.sendGameMessage("The lift breaks as it hits the bottom, and you swim to land.");
						player.setNextPosition(new Position(2724, 4452, 0));
					} else if (!player.getInventory().containsItem(4075, 1)) {
						player.getDialogueManager().startDialogue("SimpleMessage", "It looks too dark to go down.");
						player.getPackets()
								.sendGameMessage("Perhaps you should find a light source somewhere before proceeding.");
						return;
					}
				}
				// start elite guilds
				else if (object.getId() == 16089 && object.getY() == 3386) {
					if (player.getSkills().getLevel(Skills.FISHING) < 99) {
						player.getPackets().sendGameMessage("You must have 99 Fishing to enter this Elite Guild.");
					} else {
						player.setNextPosition(new Position(2614, 3389, 0));
					}
					return;
				} else if (object.getId() == 2113) {
					if (player.getSkills().getLevel(Skills.MINING) < 99) {
						player.getPackets().sendGameMessage("You must have 99 Mining to enter this Elite Guild.");
					} else {
						player.useStairs(-1, new Position(3021, 9739, 0), 0, 1);
					}
					return;
				} else if (object.getId() == 2647) {
					if (player.getSkills().getLevel(Skills.CRAFTING) < 99) {
						player.getPackets().sendGameMessage("You must have 99 Crafting to enter this Elite Guild.");
					} else {
						player.useStairs(-1, new Position(2933, 3288, 0), 0, 1);
					}
					return;
				} else if (object.getId() == 2273) {
					if (player.getSkills().getLevel(Skills.COOKING) < 99) {
						player.getPackets().sendGameMessage("You must have 99 Cooking to enter this Elite Guild.");
					} else {
						player.useStairs(-1, new Position(3143, 3444, 0), 0, 1);
					}
					return;
				}
				// end elite guilds
				// Flax shit
				else if (id == 2644) {
					FlaxCrafting.make(player, Orb.AIR_ORB);
				} else if (id == 2646) {
					World.removeTemporaryObject(object, 50000, true);
					player.getInventory().addItem(1779, 1);
					player.animate(new Animation(827));
					player.lock(2);
				}
				// End Flax Shit

				/* Experiments dungeon @ frankenstein's castle */
				else if (id == 5167 && object.getX() == 3578 && object.getY() == 3527) {
					player.useStairs(7321, new Position(3577, 9927, 0), 3, 4);

				} else if (id == 1757 && object.getX() == 3578 && object.getY() == 9927) {
					player.useStairs(828, new Position(3578, 3526, 0), 1, 2);

				} else if (id == 5170 && object.getX() == 3510 && object.getY() == 9957) {
					handleDoor(player, object);

				} else if (id == 1757 && object.getX() == 3504 && object.getY() == 9970) {
					player.useStairs(828, new Position(3504, 3571, 0), 1, 2);

				} else if (id == 5167 && object.getX() == 3505 && object.getY() == 3571) {
					player.useStairs(827, new Position(3504, 9969, 0), 1, 2);

				}
				/* Werewolf agility and skullball */
				else if (id == 5132 && object.getX() == 3543 && object.getY() == 3462) {
					player.useStairs(827, new Position(3549, 9865, 0), 1, 2);

				} else if (id == 5130 && object.getX() == 3549 && object.getY() == 9864) {
					player.useStairs(828, new Position(3543, 3463, 0), 1, 2);

				}
				/* Drezel's church, vyre cremating */
				else if (id == 30572 && object.getX() == 3405 && object.getY() == 3507) {
					player.useStairs(827, new Position(3405, 9906, 0), 1, 2);
				} else if (id == 30575 && object.getX() == 3405 && object.getY() == 9907) {
					player.useStairs(828, new Position(3405, 3506, 0), 1, 2);
				} else if (id == 30574 && object.getX() == 3422 && object.getY() == 3484) {
					player.useStairs(827, new Position(3440, 9887, 0), 1, 2);

				} else if (id == 3443 && object.getX() == 3440 && object.getY() == 9886) {
					player.useStairs(844, new Position(3423, 3484, 0), 1, 2);

				} else if (id == 30723 && object.getX() == 3415 && object.getY() == 3486) {
					player.useStairs(827, new Position(3414, 3486, 0), 1, 2);

				} else if (id == 30725 && object.getX() == 3415 && object.getY() == 3491) {
					player.useStairs(827, new Position(3414, 3491, 0), 1, 2);

				}

				else if (id == 9334 && object.getX() == 3424 && object.getY() == 3476
						&& player.getSkills().getLevel(Skills.AGILITY) >= 65) {

					player.setNextPosition(new Position(3425, 3483, 0));
				} else if (id == 9334 && object.getX() == 3424 && object.getY() == 3476
						&& player.getSkills().getLevel(Skills.AGILITY) < 65) {

					player.getPackets().sendGameMessage("You need at least 65 agility to use this");
				} else if (id == 9337 && object.getX() == 3425 && object.getY() == 3483
						&& player.getSkills().getLevel(Skills.AGILITY) < 65) {
					player.getPackets().sendGameMessage("You need at least 65 agility to use this");
				} else if (id == 9337 && object.getX() == 3425 && object.getY() == 3483
						&& player.getSkills().getLevel(Skills.AGILITY) >= 65) {
					player.setNextPosition(new Position(3423, 3476, 0));
				}
				// vyre cremating

				else if (id == 12765 && object.getX() == 3443 && object.getY() == 9898) {
					player.getPackets().sendGameMessage("you trigger a trapdoor and fall down some stairs.");
					player.setNextPosition(new Position(3422, 9965, 0));
				} else if (id == 30534 && object.getX() == 3422 && object.getY() == 9966) {
					player.getPackets().sendGameMessage("You climb up the stairs.");
					player.setNextPosition(new Position(3441, 9899, 0));
				}

				/* Rift with anger monsters */
				else if (id == 13969) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13968) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13967) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13974) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13975) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13978) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13976) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13971) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13973) {
					player.useStairs(827, new Position(3297, 9824, 0), 1, 2);
				} else if (id == 13999 && object.getX() == 3297 && object.getY() == 9823) {
					player.useStairs(828, new Position(3309, 3452, 0), 1, 2);
				}

				/* vampire city maze! */
				else if (id == 18071 && object.getX() == 3598 && object.getY() == 3203) {
					player.setNextPosition(new Position(3598, 3201, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18070 && object.getX() == 3598 && object.getY() == 3201) {
					player.setNextPosition(new Position(3598, 3203, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18073 && object.getX() == 3599 && object.getY() == 3200) {
					player.setNextPosition(new Position(3601, 3200, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18072 && object.getX() == 3601 && object.getY() == 3200) {
					player.setNextPosition(new Position(3599, 3200, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 17975 && object.getX() == 3603 && object.getY() == 3202) {
					player.getPackets().sendGameMessage("The ladder looks too dangerious to climb.");
				} else if (id == 17974 && object.getX() == 3603 && object.getY() == 3202) {
					player.getPackets().sendGameMessage("The ladder looks too dangerous to climb.");
				} else if (id == 18128 && object.getX() == 3605 && object.getY() == 3204) {
					player.setNextPosition(new Position(3605, 3206, 1));
					player.getPackets().sendGameMessage("You push the wall and jump across but the wall slams back.");
					player.getPackets().sendGameMessage("It does not look like you can jump back...");
				} else if (id == 18078 && object.getX() == 3606 && object.getY() == 3208 && player.getX() == 3606
						&& player.getY() == 3207) {
					player.setNextPosition(new Position(3606, 3208, 1));
					player.getPackets().sendGameMessage("You crawl underneith the wall...");

				} else if (id == 18078 && object.getX() == 3606 && object.getY() == 3208 && player.getX() == 3606
						&& player.getY() == 3208) {
					player.getPackets().sendGameMessage("You crawl underneith the wall...");
					player.setNextPosition(new Position(3606, 3207, 1));

				} else if (id == 17979 && object.getX() == 3608 && object.getY() == 3209) {
					player.getPackets().sendGameMessage("These stairs are too dangerous to walk down on.");
				} else if (id == 18131 && object.getX() == 3602 && object.getY() == 3214) {
					player.setNextPosition(new Position(3601, 3214, 1));
					player.getPackets().sendGameMessage("You push the wall and jump across but the wall slams back.");
					player.getPackets().sendGameMessage("It does not look like you can jump back...");
				} else if (id == 17973 && object.getX() == 3600 && object.getY() == 3213) {
					player.getPackets().sendGameMessage("The door does not seem to budge.");
				} else if (id == 18083 && object.getX() == 3598 && object.getY() == 3216) {
					player.setNextPosition(new Position(3598, 3220, 0));
					player.getPackets().sendGameMessage("You uncover a secret tunel and squeeze into it");
				} else if (id == 18085 && object.getX() == 3598 && object.getY() == 3219) {
					player.setNextPosition(new Position(3598, 3215, 0));
					player.getPackets().sendGameMessage("You attempt to squeez into the tunnel..and make it.");
				} else if (id == 18057 && object.getX() == 3594 && object.getY() == 3219) {
					player.getPackets().sendGameMessage("You cannot find a way to open the door.");
				} else if (id == 18086 && object.getX() == 3594 && object.getY() == 3223) {
					player.useStairs(828, new Position(3594, 3223, 1), 1, 2);

				} else if (id == 18087 && object.getX() == 3594 && object.getY() == 3223) {
					player.useStairs(828, new Position(3594, 3223, 0), 1, 2);

				} else if (id == 18088 && object.getX() == 3596 && object.getY() == 3223 && player.getX() == 3596
						&& player.getY() == 3223) {
					player.setNextPosition(new Position(3597, 3223, 1));
					player.getPackets().sendGameMessage("You crawl underneith the wall...");

				} else if (id == 18088 && object.getX() == 3596 && object.getY() == 3223 && player.getX() == 3597
						&& player.getY() == 3223) {
					player.getPackets().sendGameMessage("You crawl underneith the wall...");
					player.setNextPosition(new Position(3596, 3223, 1));

				} else if (id == 18090 && object.getX() == 3598 && object.getY() == 3222) {
					player.setNextPosition(new Position(3601, 3222, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18089 && object.getX() == 3601 && object.getY() == 3222) {
					player.setNextPosition(new Position(3598, 3222, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 17600 && object.getX() == 3618 && object.getY() == 3223) {
					player.getPackets().sendGameMessage("This door is locked.");
				} else if (id == 18094 && object.getX() == 3615 && object.getY() == 3218) {
					player.setNextPosition(new Position(3615, 3216, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18093 && object.getX() == 3615 && object.getY() == 3216) {
					player.setNextPosition(new Position(3615, 3218, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 17973 && object.getX() == 3614 && object.getY() == 3204) {
					player.getPackets().sendGameMessage("The door is jammed shut.");
				} else if (id == 17978 && object.getX() == 3632 && object.getY() == 3203) {
					player.setNextPosition(new Position(3632, 3205, 0));
				} else if (id == 17976 && object.getX() == 3632 && object.getY() == 3203) {
					player.setNextPosition(new Position(3632, 3202, 1));
				} else if (id == 17974 && object.getX() == 3612 && object.getY() == 3210) {
					player.useStairs(828, new Position(3612, 3211, 1), 1, 2);
				} else if (id == 18057 && object.getX() == 3625 && object.getY() == 3223) {
					player.getPackets().sendGameMessage("The door is jammed shut.");
				} else if (id == 18095 && object.getX() == 3615 && object.getY() == 3210) {
					player.useStairs(828, new Position(3614, 3210, 2), 1, 2);
				} else if (id == 18096 && object.getX() == 3615 && object.getY() == 3210) {
					player.useStairs(828, new Position(3615, 3210, 1), 1, 2);
				} else if (id == 18098 && object.getX() == 3613 && object.getY() == 3208) {
					player.setNextPosition(new Position(3613, 3205, 3));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18097 && object.getX() == 3613 && object.getY() == 3205) {
					player.setNextPosition(new Position(3613, 3208, 3));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18099 && object.getX() == 3617 && object.getY() == 3202) {
					if (player.getX() != 3616 || player.getY() != 3202 || player.getZ() != 2) {
						return;
					}
					final boolean running = player.getRun();
					player.setRunHidden(false);
					player.lock(7);
					player.addWalkSteps(3622, 3202, -1, false);
					WorldTasksManager.schedule(new WorldTask() {
						boolean secondloop;

						@Override
						public void run() {
							if (!secondloop) {
								secondloop = true;
								player.getAppearence().setRenderEmote(155);
							} else {
								player.getAppearence().setRenderEmote(-1);
								player.setRunHidden(running);
								player.getPackets().sendGameMessage("You passed the clothing line succesfully.", true);
								stop();
							}
						}
					}, 0, 5);
				} else if (id == 18100 && object.getX() == 3621 && object.getY() == 3202) {
					if (player.getX() != 3622 || player.getY() != 3202 || player.getZ() != 2) {
						return;
					}
					final boolean running = player.getRun();
					player.setRunHidden(false);
					player.lock(7);
					player.addWalkSteps(3616, 3202, -1, false);
					WorldTasksManager.schedule(new WorldTask() {
						boolean secondloop;

						@Override
						public void run() {
							if (!secondloop) {
								secondloop = true;
								player.getAppearence().setRenderEmote(155);
							} else {
								player.getAppearence().setRenderEmote(-1);
								player.setRunHidden(running);
								player.getPackets().sendGameMessage("You passed the clothing line succesfully.", true);
								stop();
							}
						}
					}, 0, 5);
				} else if (id == 17974 && object.getX() == 3625 && object.getY() == 3203) {
					player.useStairs(828, new Position(3625, 3202, 2), 1, 2);
				} else if (id == 17975 && object.getX() == 3625 && object.getY() == 3203) {
					player.useStairs(828, new Position(3625, 3204, 1), 1, 2);
				} else if (id == 18134 && object.getX() == 3623 && object.getY() == 3208) {
					player.setNextPosition(new Position(3623, 3210, 1));
					player.getPackets().sendGameMessage("You push the wall and jump across but the wall slams back.");
					player.getPackets().sendGameMessage("It does not look like you can jump back...");
				} else if (id == 18105 && object.getX() == 3623 && object.getY() == 3217) {
					player.useStairs(828, new Position(3623, 3218, 2), 1, 2);
				} else if (id == 18106 && object.getX() == 3623 && object.getY() == 3217) {
					player.useStairs(828, new Position(3623, 3217, 1), 1, 2);
				} else if (id == 18107 && object.getX() == 3626 && object.getY() == 3221) {
					player.useStairs(828, new Position(3626, 3221, 1), 1, 2);
				} else if (id == 18108 && object.getX() == 3626 && object.getY() == 3221) {
					player.useStairs(828, new Position(3625, 3221, 2), 1, 2);
				} else if (id == 18110 && object.getX() == 3623 && object.getY() == 3223) {
					player.setNextPosition(new Position(3623, 3226, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18109 && object.getX() == 3623 && object.getY() == 3226) {
					player.setNextPosition(new Position(3623, 3223, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18112 && object.getX() == 3622 && object.getY() == 3230) {
					player.setNextPosition(new Position(3622, 3232, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18111 && object.getX() == 3622 && object.getY() == 3232) {
					player.setNextPosition(new Position(3622, 3230, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18114 && object.getX() == 3624 && object.getY() == 3240) {
					player.setNextPosition(new Position(3626, 3240, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18113 && object.getX() == 3626 && object.getY() == 3240) {
					player.setNextPosition(new Position(3624, 3240, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 17974 && object.getX() == 3630 && object.getY() == 3239) {
					player.useStairs(828, new Position(3631, 3239, 2), 1, 2);
				} else if (id == 17975 && object.getX() == 3630 && object.getY() == 3239) {
					player.useStairs(828, new Position(3629, 3239, 1), 1, 2);
				} else if (id == 18115 && object.getX() == 3625 && object.getY() == 3240) {
					player.getPackets().sendGameMessage("You search the wall and find a part of a ladder.");
					player.getInventory().addItem(9655, 1);
				} else if (id == 18116 && object.getX() == 3629 && object.getY() == 3240) {
					if (player.getInventory().containsItem(9655, 1)) {
						player.getInventory().deleteItem(9655, 1);
						player.getPackets().sendGameMessage("You fix the ladder and climb down.");
						player.useStairs(828, new Position(3630, 3240, 0), 1, 2);
					} else if (!player.getInventory().containsItem(9655, 1)) {
						player.getDialogueManager().startDialogue("SimpleMessage", "This ladder needs to be fixed.");
						return;
					}
				} else if (id == 17974 && object.getX() == 3629 && object.getY() == 3240) {
					player.useStairs(828, new Position(3628, 3240, 1), 1, 2);
				} else if (id == 17601 && object.getX() == 3637 && object.getY() == 3243) {
					player.getDialogueManager().startDialogue("SimpleMessage", "This door is locked.");
				} else if (id == 61129 && object.getX() == 3630 && object.getY() == 3328) {
					player.setNextPosition(new Position(3630, 3331, 0));
					player.getPackets().sendGameMessage("You crawl through the grate.");
				} else if (id == 61137 && object.getX() == 3630 && object.getY() == 3330) {
					player.setNextPosition(new Position(3630, 3327, 0));
					player.getPackets().sendGameMessage("You crawl through the grate.");
				} else if (id == 17600 && object.getX() == 3630 && object.getY() == 326) {
					player.getPackets().sendGameMessage("The door does not budge.");
				} else if (id == 18118 && object.getX() == 3633 && object.getY() == 3256) {
					player.setNextPosition(new Position(3636, 3256, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 18117 && object.getX() == 3636 && object.getY() == 3256) {
					player.setNextPosition(new Position(3633, 3256, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 17978 && object.getX() == 3639 && object.getY() == 3256) {
					player.setNextPosition(new Position(3639, 3258, 0));
					player.getPackets().sendGameMessage("You climb down the sketchy stairs.");
				} else if (id == 17976 && object.getX() == 3639 && object.getY() == 3256) {
					player.setNextPosition(new Position(3639, 3255, 1));
					player.getPackets().sendGameMessage("You climb up the sketchy stairs.");
				} else if (id == 17973 && object.getX() == 3636 && object.getY() == 3254) {
					player.getPackets().sendGameMessage("The door does not budge.");
				} else if (id == 17980 && object.getX() == 3640 && object.getY() == 3253 && player.getX() == 3640
						&& player.getY() == 3253) {
					player.setNextPosition(new Position(3640, 3252, 0));
					player.getPackets().sendGameMessage("You push through the wall.");
				} else if (id == 17980 && object.getX() == 3640 && object.getY() == 3253 && player.getX() == 3640
						&& player.getY() == 3252) {
					player.setNextPosition(new Position(3640, 3253, 0));
					player.getPackets().sendGameMessage("You push through the wall.");
				} else if (id == 18146 && object.getX() == 3638 && object.getY() == 3251) {
					player.setNextPosition(new Position(3626, 9618, 0));
					player.getPackets().sendGameMessage("You push the button and fall through a trapdoor.");
				} else if (id == 17974 && object.getX() == 3626 && object.getY() == 3251) {
					player.useStairs(828, new Position(3627, 3251, 1), 1, 2);
				} else if (id == 18039 && object.getX() == 3627 && object.getY() == 3253) {
					player.getPackets().sendGameMessage("This fireplace has an interesting design.");
				} else if (id == 61294 && object.getX() == 3622 && object.getY() == 3252) {
					player.getPackets().sendGameMessage("You jump over the wall...");
					player.setNextPosition(new Position(3621, 3252, 1));
				} else if (id == 61294 && object.getX() == 3621 && object.getY() == 3252) {
					player.getPackets().sendGameMessage("You jump over the wall...");
					player.setNextPosition(new Position(3623, 3252, 1));
				} else if (id == 17601 && object.getX() == 3596 && object.getY() == 3251) {
					player.getPackets().sendGameMessage("The door does not budge.");
				} else if (id == 17976 && object.getX() == 3630 && object.getY() == 3270) {
					player.setNextPosition(new Position(3632, 3270, 1));
				} else if (id == 17978 && object.getX() == 3630 && object.getY() == 3270) {
					player.setNextPosition(new Position(3629, 3270, 0));
				} else if (id == 17974 && object.getX() == 3632 && object.getY() == 3274) {
					player.useStairs(828, new Position(3633, 3274, 2), 1, 2);
				} else if (id == 17600 && object.getX() == 3633 && object.getY() == 3280) {
					player.getPackets().sendGameMessage("The door does not budge.");
				} else if (id == 18125 && object.getX() == 3638 && object.getY() == 3304 && player.getX() == 3638
						&& player.getY() == 3304) {
					player.setNextPosition(new Position(3638, 3305, 0));
					player.getPackets().sendGameMessage("You slash through the tapestry.");
				} else if (id == 18125 && object.getX() == 3638 && object.getY() == 3304 && player.getX() == 3638
						&& player.getY() == 3305) {
					player.setNextPosition(new Position(3638, 3304, 0));
					player.getPackets().sendGameMessage("You slash through the tapestry");
				} else if (id == 18047 && object.getX() == 3641 && object.getY() == 3307 && player.getX() == 3641
						&& player.getY() == 3307) {
					player.getPackets().sendGameMessage("You go through the door.");
					player.setNextPosition(new Position(3642, 3307, 0));
				} else if (id == 18047 && object.getX() == 3641 && object.getY() == 3307 && player.getX() == 3642
						&& player.getY() == 3307) {
					player.getPackets().sendGameMessage("You go through the door.");
					player.setNextPosition(new Position(3641, 3307, 0));
				} else if (id == 18049 && object.getX() == 3643 && object.getY() == 3304) {
					player.useStairs(828, new Position(3637, 9695, 0), 1, 2);
				} else if (id == 18050 && object.getX() == 3637 && object.getY() == 9696) {
					player.useStairs(828, new Position(3643, 3306, 0), 1, 2);
				} else if (id == 30523 && object.getX() == 3630 && object.getY() == 9680 && player.getX() == 3630
						&& player.getY() == 9681) {
					player.setNextPosition(new Position(3630, 9680, 0));
					player.getPackets().sendGameMessage("You go through the door.");
				} else if (id == 30523 && object.getX() == 3630 && object.getY() == 9680 && player.getX() == 3630
						&& player.getY() == 9680) {
					player.setNextPosition(new Position(3630, 9681, 0));
					player.getPackets().sendGameMessage("You go through the door.");
				} else if (id == 7127 && object.getX() == 3629 && object.getY() == 9680 && player.getX() == 3629
						&& player.getY() == 9681) {
					player.setNextPosition(new Position(3629, 9679, 0));
					player.getPackets().sendGameMessage("You go through the door.");
				} else if (id == 7127 && object.getX() == 3629 && object.getY() == 9680 && player.getX() == 3629
						&& player.getY() == 9680) {
					player.setNextPosition(new Position(3629, 9681, 0));
					player.getPackets().sendGameMessage("You go through the door.");
				} else if (id == 3489 && object.getX() == 3636 && object.getY() == 3324) {
					player.setNextPosition(new Position(3639, 3324, 1));
					player.getPackets().sendGameMessage("You jump to the floor boards");
				} else if (id == 30501 && object.getX() == 3639 && object.getY() == 3324) {
					player.setNextPosition(new Position(3636, 3324, 1));
					player.getPackets().sendGameMessage("You jump to the broken wall");
				} else if (id == 30497 && object.getX() == 3639 && object.getY() == 3320) {
					player.setNextPosition(new Position(3639, 3318, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 30496 && object.getX() == 3639 && object.getY() == 3318) {
					player.setNextPosition(new Position(3639, 3320, 1));
					player.getPackets().sendGameMessage("You jump across...");
				} else if (id == 30623 && object.getX() == 3636 && object.getY() == 3323) {
					player.getPackets().sendGameMessage("You find some coal.");
					player.getInventory().addItem(453, 1);
				} else if (id == 30521 && object.getX() == 3635 && object.getY() == 3324) {
					player.getPackets().sendGameMessage("The trough is empty.");
				} else if (id == 61336 && object.getX() == 3590 && object.getY() == 3373) {

				} else if (id == 17986 && object.getX() == 3626 && object.getY() == 9617) {
					player.useStairs(828, new Position(3638, 3251, 0), 1, 2);
				} else if (id == 12743 && object.getX() == 3490 && object.getY() == 3232) {
					player.useStairs(827, new Position(3490, 9631, 0), 1, 2);
				} else if (id == 12779 && object.getX() == 3490 && object.getY() == 9632) {
					player.useStairs(828, new Position(3491, 3232, 0), 1, 2);
				} else if (id == 12737 && object.getX() == 3491 && object.getY() == 3230 && player.getX() == 3491
						&& player.getY() == 3231) {
					player.useStairs(839, new Position(3491, 3230, 0), 1, 2);
				} else if (id == 12737 && object.getX() == 3491 && object.getY() == 3230 && player.getX() == 3491
						&& player.getY() == 3230) {
					player.useStairs(839, new Position(3491, 3231, 0), 1, 2);
				} else if (id == 12944 && object.getX() == 3523 && object.getY() == 3177) {
					player.getPackets().sendGameMessage("The next area is currently unavailable");
				} else if (id == 61091 && object.getX() == 2274 && object.getY() == 5152) {
					player.useStairs(828, new Position(3497, 3205, 0), 1, 2);
				} else if (id == 59921 && object.getX() == 3496 && object.getY() == 3203) {
					player.useStairs(827, new Position(2273, 5152, 0), 1, 2);
				} else if (id == 18038 && object.getX() == 3591 && object.getY() == 3180) {
					player.setNextPosition(new Position(3588, 3180, 0));
				} else if (id == 18037 && object.getX() == 3589 && object.getY() == 3180) {
					player.setNextPosition(new Position(3592, 3180, 0));
				} else if (id == 18122 && object.getX() == 3589 && object.getY() == 3173) {
					player.setNextPosition(new Position(3589, 3174, 0));
				} else if (id == 18054 && object.getX() == 3589 && object.getY() == 3215) {
					handleDoor(player, object);
				} else if (id == 18002 && object.getX() == 3588 && object.getY() == 3251) {
					player.useStairs(828, new Position(3588, 3252, 2), 1, 2);
				} else if (id == 18002 && object.getX() == 3588 && object.getY() == 3259) {
					player.useStairs(828, new Position(3588, 3258, 2), 1, 2);
				} else if (id == 18001 && object.getX() == 3588 && object.getY() == 3251) {
					player.useStairs(828, new Position(3588, 3250, 1), 1, 2);
				} else if (id == 18001 && object.getX() == 3588 && object.getY() == 3259) {
					player.useStairs(828, new Position(3588, 3260, 1), 1, 2);
				} else if (id == 61197 && object.getX() == 3593 && object.getY() == 3311) {
					player.useStairs(828, new Position(3593, 3313, 0), 1, 2);
				} else if (id == 61198 && object.getX() == 3593 && object.getY() == 3313) {
					player.useStairs(828, new Position(3593, 3310, 1), 1, 2);
				} else if (id == 17974 && object.getX() == 3626 && object.getY() == 3251) {
					player.useStairs(828, new Position(3627, 3240, 1), 1, 2);
				} else if (id == 5005 && object.getX() == 3502 && object.getY() == 3431 && player.getX() == 3502
						&& player.getY() == 3432) {
					player.useStairs(828, new Position(3502, 3430, 0), 1, 2);
				} else if (id == 5005 && object.getX() == 3502 && object.getY() == 3431 && player.getX() == 3502
						&& player.getY() == 3430) {
					player.useStairs(828, new Position(3502, 3432, 0), 1, 2);
				} else if (id == 5005 && object.getX() == 3502 && object.getY() == 3426 && player.getX() == 3502
						&& player.getY() == 3427) {
					player.useStairs(828, new Position(3502, 3425, 0), 1, 2);
				} else if (id == 5005 && object.getX() == 3502 && object.getY() == 3426 && player.getX() == 3502
						&& player.getY() == 3425) {
					player.useStairs(828, new Position(3502, 3427, 0), 1, 2);
				} else if (id == 6970 && object.getX() == 3498 && object.getY() == 3377) {
					player.setNextPosition(new Position(3523, 3283, 0));
				} else if (id == 6969 && object.getX() == 3524 && object.getY() == 3283) {
					player.setNextPosition(new Position(3499, 3380, 0));
				} else if (id == 17955 && object.getX() == 3523 && object.getY() == 3169) {
					player.setNextPosition(new Position(3593, 3180, 0));
				} else if (id == 17955 && object.getX() == 3593 && object.getY() == 3178) {
					player.setNextPosition(new Position(3525, 3170, 0));
				} else if (id == 17757 && object.getX() == 3485 && object.getY() == 3244 && player.getY() == 3244) {
					player.setNextPosition(new Position(3485, 3243, 0));
				} else if (id == 17757 && object.getX() == 3485 && object.getY() == 3244 && player.getY() == 3243) {
					player.setNextPosition(new Position(3485, 3244, 0));
				} else if (id == 17760 && object.getX() == 3484 && object.getY() == 3244 && player.getY() == 3244) {
					player.setNextPosition(new Position(3484, 3243, 0));
				} else if (id == 17760 && object.getX() == 3484 && object.getY() == 3244 && player.getY() == 3243) {
					player.setNextPosition(new Position(3484, 3244, 0));
				} else if (id == 5055 && object.getX() == 3495 && object.getY() == 3465) {
					player.useStairs(827, new Position(3477, 9845, 0), 1, 2);
				} else if (id == 5054 && object.getX() == 3477 && object.getY() == 9846) {
					player.useStairs(828, new Position(3495, 3466, 0), 1, 2);
				} else if (id == 5052 && object.getX() == 3480 && object.getY() == 9837) {
					handleDoor(player, object);
				} else if (id == 30262 && object.getX() == 3501 && object.getY() == 9813) {
					player.useStairs(828, new Position(3509, 3448, 0), 1, 2);
				} else if (id == 30261 && object.getX() == 3500 && object.getY() == 9813) {
					player.useStairs(828, new Position(3509, 3448, 0), 1, 2);
				} else if (id == 30265 && object.getX() == 3508 && object.getY() == 3444) {
					player.useStairs(828, new Position(3501, 9812, 0), 1, 2);
				} else if (id == 5050 && object.getX() == 3492 && object.getY() == 9824) {
					player.setNextPosition(new Position(3505, 9832, 0));
				} else if (id == 5046 && object.getX() == 3492 && object.getY() == 9823) {
					player.setNextPosition(new Position(3505, 9832, 0));
				} else if (id == 5046 && object.getX() == 3505 && object.getY() == 9831) {
					player.setNextPosition(new Position(3491, 9824, 0));
				} else if (id == 32015 && object.getX() == 3008 && object.getY() == 9550) {
					player.setNextPosition(new Position(2842, 3423, 0));
				} else if (id == 9472 && object.getX() == 3008 && object.getY() == 3150) {
					player.setNextPosition(new Position(3007, 9550, 0));
				} else if (id == 1756 && object.getX() == 2842 && object.getY() == 3424) {
					player.setNextPosition(new Position(2842, 9825, 0));
				} else if (id == 33173 && object.getX() == 3055 && object.getY() == 9560) {
					player.setNextPosition(new Position(3055, 9556, 0));
				} else if (id == 33174 && object.getX() == 3055 && object.getY() == 9556) {
					player.setNextPosition(new Position(3056, 9562, 0));
				} else if (id == 37212) {
					handleStaircases(player, object, 1);
				} else if (id == 48333) {
					handleDoor(player, object);
				} else if (id == 23156) {
					handleDoor(player, object);
				} else if (id == 61560 || id == 61558) {
					handleDoor(player, object);
				} else if (id == 44208 && object.getX() == 2839 && object.getY() == 3855) {
					player.setNextPosition(new Position(2812, 10262, 0));
				} else if (id == 44207 && object.getX() == 2812 && object.getY() == 10263) {
					player.setNextPosition(new Position(2839, 3854, 0));
				} else if (id == 44254 && object.getX() == 2836 && object.getY() == 3866) {
					player.setNextPosition(new Position(2836, 3868, 1));
				} else if (id == 10229 && object.getX() == 2899 && object.getY() == 4449) {
					player.sm("You safely climb the rusty ladder.");
					player.setNextPosition(new Position(1912, 4367, 0));
					return;
				} else if (id == 44253 && object.getX() == 2829 && object.getY() == 3866) {
					player.setNextPosition(new Position(2829, 3868, 1));
				} else if (id == 44255 && object.getX() == 2829 && object.getY() == 3867) {
					player.setNextPosition(new Position(2829, 3865, 0));
				} else if (id == 44255 && object.getX() == 2836 && object.getY() == 3867) {
					player.setNextPosition(new Position(2836, 3865, 0));
				} else if (id == 9320 && object.getX() == 3422 && object.getY() == 3550) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 70) {
						player.useStairs(827, new Position(player.getX(), player.getY(), player.getZ() - 1), 2, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 61 agility to climb this.");
					}
				} else if (id == 9320 && object.getX() == 3447 && object.getY() == 3576) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 70) {
						player.useStairs(828, new Position(player.getX(), player.getY(), player.getZ() - 1), 2, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 71 agility to cross this.");
					}
				} else if (id == 9319 && object.getX() == 3422 && object.getY() == 3550) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 70) {
						player.useStairs(828, new Position(player.getX(), player.getY(), player.getZ() + 1), 2, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 61 agility to cross this.");
					}
				} else if (id == 9319 && object.getX() == 3447 && object.getY() == 3576) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 70) {
						player.useStairs(828, new Position(player.getX(), player.getY(), player.getZ() + 1), 2, 2);
					} else {
						player.getPackets().sendGameMessage("You need at least 71 agility to cross this.");
					}
				} else if (id == 4494 && object.getX() == 3434 && object.getY() == 3537) {
					player.setNextPosition(new Position(3438, 3538, 0));
				} else if (id == 10529 && object.getX() == 3427 && object.getY() == 3555) {
					Magic.pushLeverTeleport(player, new Position(3426, 3555, 1));
				} else if (id == 10527 && object.getX() == 3426 && object.getY() == 3555) {
					Magic.pushLeverTeleport(player, new Position(3426, 3556, 1));
				} else if (id == 4495 && object.getX() == 3413 && object.getY() == 3540) {
					player.setNextPosition(new Position(3417, 3540, 2));
					return;
				} else if (id == 4496) {
					player.setNextPosition(new Position(3412, 3541, 1));
				} else if (id == 9320 && object.getX() == 3447 && object.getY() == 3576) {
					if (player.getSkills().getLevel(Skills.AGILITY) > 70) {
						player.setNextPosition(new Position(3422, 3549, 0));
					} else {
						player.getPackets().sendGameMessage("You need at least 71 agility to climb this.");
					}
				} else if (id == 42425 && object.getX() == 3220 && object.getY() == 3222) { // zaros portal
					player.useStairs(10256, new Position(3353, 3416, 0), 4, 5,
							"And you find yourself into a digsite.");
					player.addWalkSteps(3222, 3223, -1, false);
					player.getPackets().sendGameMessage("You examine portal and it absorbs you...");
				} else if (id == 9356) {
					FightCaves.enterFightCaves(player);
				} else if (id == 68107) {
					FightKiln.enterFightKiln(player, false);
				} else if (id == 26898) {
					PestInvasion.enterPestInvasion(player);
				} else if (id == 68223) {
					FightPits.enterLobby(player, false);
				} else if (id == 57171) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 57180) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 57169) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 57179) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 57172) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 57170) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 57176) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 57177) {
					player.getActionManager().setAction(new LavaFlowMine(object));
				} else if (id == 46500 && object.getX() == 3173 && object.getY() == 3875) { // zaros portal
					int i;
					if (player.isPker) {
						i = 1;
					} else {
						i = 0;
					}
					player.useStairs(-1, Constants.RESPAWN_PLAYER_LOCATION[i], 2, 3,
							"You found your way back to home.");
					player.addWalkSteps(3351, 3415, -1, false);
				} else if (id == 46500 && object.getX() == 3351 && object.getY() == 3415) { // zaros portal
					int i;
					if (player.isPker) {
						i = 1;
					} else {
						i = 0;
					}
					player.useStairs(-1, Constants.RESPAWN_PLAYER_LOCATION[i], 2, 3,
							"You found your way back to home.");
					player.addWalkSteps(3351, 3415, -1, false);
				} else if (id == 9293) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
						player.getPackets().sendGameMessage("You need an agility level of 70 to use this obstacle.",
								true);
						return;
					}
					int x = player.getX() == 2886 ? 2892 : 2886;
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							player.animate(new Animation(844));
							if (count++ == 1) {
								stop();
							}
						}

					}, 0, 0);
					player.setNextForceMovement(
							new ForceMovement(new Position(x, 9799, 0), 3, player.getX() == 2886 ? 1 : 3));
					player.useStairs(-1, new Position(x, 9799, 0), 3, 4);
				} else if (id == 29370 && (object.getX() == 3150 || object.getX() == 3153) && object.getY() == 9906) { // edgeville
																														// dungeon
																														// cut
					if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
						player.getPackets().sendGameMessage("You need an agility level of 53 to use this obstacle.");
						return;
					}
					final boolean running = player.getRun();
					player.setRunHidden(false);
					player.lock(8);
					player.addWalkSteps(x == 3150 ? 3155 : 3149, 9906, -1, false);
					player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
					WorldTasksManager.schedule(new WorldTask() {
						boolean secondloop;

						@Override
						public void run() {
							if (!secondloop) {
								secondloop = true;
								player.getAppearence().setRenderEmote(295);
							} else {
								player.getAppearence().setRenderEmote(-1);
								player.setRunHidden(running);
								player.getSkills().addXp(Skills.AGILITY, 7);
								stop();
							}
						}
					}, 0, 5);
				}
				// start dagganoth lair
				else if (id == 8930) {
					player.setNextPosition(new Position(1975, 4409, 3));
				} else if (id == 10177) {
					player.setNextPosition(new Position(1798, 4407, 3));
				} else if (id == 10193) {
					player.setNextPosition(new Position(2545, 10143, 0));
				} else if (id == 10194) {
					player.setNextPosition(new Position(2544, 3741, 0));
				} else if (id == 10195) {
					// player.setNextWorldTile(new WorldTile(1812, 4406, 2));
					Magic.pushLeverTeleport(player, new Position(1812, 4406, 2));
				} else if (id == 10197) {
					player.setNextPosition(new Position(1823, 4404, 2));
				} else if (id == 10198) {
					// player.setNextWorldTile(new WorldTile(1825, 4404, 3));
					Magic.pushLeverTeleport(player, new Position(1825, 4404, 3));
				} else if (id == 10199) {
					// player.setNextWorldTile(new WorldTile(1834, 4388, 2));
					Magic.pushLeverTeleport(player, new Position(1834, 4388, 2));
				} else if (id == 10200) {
					player.setNextPosition(new Position(1834, 4390, 3));
				} else if (id == 10201) {
					player.setNextPosition(new Position(1811, 4394, 1));
				} else if (id == 10202) {
					player.setNextPosition(new Position(1812, 4394, 2));
				} else if (id == 10203) {
					player.setNextPosition(new Position(1799, 4386, 2));
				} else if (id == 10204) {
					player.setNextPosition(new Position(1799, 4388, 1));
				} else if (id == 10205) {
					player.setNextPosition(new Position(1796, 4382, 1));
				} else if (id == 10206) {
					player.setNextPosition(new Position(1796, 4382, 2));
				} else if (id == 10207) {
					player.setNextPosition(new Position(1800, 4369, 2));
				} else if (id == 10208) {
					player.setNextPosition(new Position(1802, 4370, 1));
				} else if (id == 10209) {
					player.setNextPosition(new Position(1827, 4362, 1));
				} else if (id == 10210) {
					player.setNextPosition(new Position(1825, 4362, 2));
				} else if (id == 10211) {
					player.setNextPosition(new Position(1863, 4373, 2));
				} else if (id == 10212) {
					player.setNextPosition(new Position(1863, 4371, 1));
				} else if (id == 10213) {
					player.setNextPosition(new Position(1864, 4389, 1));
				} else if (id == 10214) {
					player.setNextPosition(new Position(1864, 4387, 2));
				} else if (id == 10215) {
					player.setNextPosition(new Position(1890, 4407, 0));
				} else if (id == 10216) {
					player.setNextPosition(new Position(1890, 4406, 1));
				} else if (id == 10217) {
					player.setNextPosition(new Position(1957, 4373, 1));
				} else if (id == 10218) {
					player.setNextPosition(new Position(1957, 4371, 0));
				} else if (id == 10219) {
					player.setNextPosition(new Position(1824, 4379, 3));
				} else if (id == 10220) {
					player.setNextPosition(new Position(1824, 4381, 2));
				} else if (id == 10221) {
					player.setNextPosition(new Position(1838, 4375, 2));
				} else if (id == 10222) {
					player.setNextPosition(new Position(1838, 4377, 3));
				} else if (id == 10223) {
					player.setNextPosition(new Position(1850, 4386, 1));
				} else if (id == 10224) {
					player.setNextPosition(new Position(1850, 4387, 2));
				} else if (id == 10225) {
					player.setNextPosition(new Position(1932, 4378, 1));
				} else if (id == 10226) {
					player.setNextPosition(new Position(1932, 4380, 2));
				} else if (id == 10227) {
					if (object.getX() == 1961 && object.getY() == 4392) {
						player.setNextPosition(new Position(1961, 4392, 2));
					} else {
						player.setNextPosition(new Position(1932, 4377, 1));
					}
				} else if (id == 10228) {
					player.setNextPosition(new Position(1961, 4393, 3));
				} else if (id == 2380) {
					CrystalChest.open(player);
				} else if (id == 10230) {
					player.setNextPosition(new Position(2899, 4449, 0));
				} else if (id == 18341 && object.getX() == 3036 && object.getY() == 10172) {
					player.useStairs(-1, new Position(3039, 3765, 0), 0, 1);
				} else if (id == 20599 && object.getX() == 3038 && object.getY() == 3761) {
					player.useStairs(-1, new Position(3037, 10171, 0), 0, 1);
				} else if (id == 18342 && object.getX() == 3075 && object.getY() == 10057) {
					player.useStairs(-1, new Position(3071, 3649, 0), 0, 1);
				} else if (id == 20600 && object.getX() == 3072 && object.getY() == 3648) {
					player.useStairs(-1, new Position(3077, 10058, 0), 0, 1);
				} else if (id == 18425 && !player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
					NomadsRequiem.enterNomadsRequiem(player);
				} else if (id == 42219) {
					player.useStairs(-1, new Position(1886, 3178, 0), 0, 1);
					if (player.getQuestManager().getQuestStage(Quests.NOMADS_REQUIEM) == -2) {
						player.getQuestManager().setQuestStageAndRefresh(Quests.NOMADS_REQUIEM, 0);
					}
				} else if (id == WorldObject.asOSRS(29241)) { //Ornate rejuvnation pool
					player.animate(833);
					player.setHitpoints(player.getMaxHitpoints());
					player.refreshHitPoints();
					player.setRunEnergy(100);
					player.restorePrayer();
					player.getCombatDefinitions().restoreSpecialAttack(100);
					player.getSkills().restoreSkills();
					player.getToxin().setToxin(null);
					player.getToxin().refresh();
				} else if (id == 8689) {
					player.getActionManager().setAction(new CowMilkingAction());
				} else if (id == 42220) {
					player.useStairs(-1, new Position(3082, 3475, 0), 0, 1);
				} else if (id == 30942 && object.getX() == 3019 && object.getY() == 3450) {
					player.useStairs(828, new Position(3020, 9850, 0), 1, 2);
				} else if (id == 6226 && object.getX() == 3019 && object.getY() == 9850) {
					player.useStairs(833, new Position(3018, 3450, 0), 1, 2);
				} else if (id == 31002 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS)) {
					player.useStairs(833, new Position(2998, 3452, 0), 1, 2);
				} else if (id == 31012 && player.getQuestManager().completedQuest(Quests.PERIL_OF_ICE_MONTAINS)) {
					player.useStairs(828, new Position(2996, 9845, 0), 1, 2);
				} else if (id == 30943 && object.getX() == 3059 && object.getY() == 9776) {
					player.useStairs(-1, new Position(3061, 3376, 0), 0, 1);
				} else if (id == 30944 && object.getX() == 3059 && object.getY() == 3376) {
					player.useStairs(-1, new Position(3058, 9776, 0), 0, 1);
				} else if (id == 2112 && object.getX() == 3046 && object.getY() == 9756) {
					if (player.getSkills().getLevelForXp(Skills.MINING) < 60) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage",
								MiningGuildDwarf.getClosestDwarfID(player),
								"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() - 1,
							object.getX(), object.getY() + 1, object.getZ());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(3046, player.getY() > object.getY() ? object.getY() : object.getY() + 1, -1,
								false);
					}
				} else if (id == 6226 && object.getX() == 3019 && object.getY() == 9740) {
					player.useStairs(828, new Position(3019, 3341, 0), 1, 2);
				} else if (id == 6226 && object.getX() == 3019 && object.getY() == 9738) {
					player.useStairs(828, new Position(3019, 3337, 0), 1, 2);
				} else if (id == 6226 && object.getX() == 3018 && object.getY() == 9739) {
					player.useStairs(828, new Position(3017, 3339, 0), 1, 2);
				} else if (id == 6226 && object.getX() == 3020 && object.getY() == 9739) {
					player.useStairs(828, new Position(3021, 3339, 0), 1, 2);
				} else if (id == 30963) {
					player.getBank().openBank();
				} else if (id == 12798) {
					player.getBank().openBank();
				} else if (id == 6045) {
					player.getPackets().sendGameMessage("You search the cart but find nothing.");
				} else if (id == 5906) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
						player.getPackets().sendGameMessage("You need an agility level of 42 to use this obstacle.");
						return;
					}
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							if (count == 0) {
								player.animate(new Animation(2594));
								Position tile = new Position(object.getX() + (object.getRotation() == 2 ? -2 : +2),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 2) {
								Position tile = new Position(object.getX() + (object.getRotation() == 2 ? -2 : +2),
										object.getY(), 0);
								player.setNextPosition(tile);
							} else if (count == 5) {
								player.animate(new Animation(2590));
								Position tile = new Position(object.getX() + (object.getRotation() == 2 ? -5 : +5),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 7) {
								Position tile = new Position(object.getX() + (object.getRotation() == 2 ? -5 : +5),
										object.getY(), 0);
								player.setNextPosition(tile);
							} else if (count == 10) {
								player.animate(new Animation(2595));
								Position tile = new Position(object.getX() + (object.getRotation() == 2 ? -6 : +6),
										object.getY(), 0);
								player.setNextForceMovement(new ForceMovement(tile, 4, Utils
										.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
							} else if (count == 12) {
								Position tile = new Position(object.getX() + (object.getRotation() == 2 ? -6 : +6),
										object.getY(), 0);
								player.setNextPosition(tile);
							} else if (count == 14) {
								stop();
								player.unlock();
							}
							count++;
						}

					}, 0, 0);

					// rock living caverns
				}
				if (object.getId() == 39508) {
					StealingCreation.enterTeamLobby(player, true);
					return;
				} else if (object.getId() == 39509) {
					StealingCreation.enterTeamLobby(player, false);
					return;

				}
				if (id == 45077) {
					player.lock();
					if (player.getX() != object.getX() || player.getY() != object.getY()) {
						player.addWalkSteps(object.getX(), object.getY(), -1, false);
					}
					WorldTasksManager.schedule(new WorldTask() {

						private int count;

						@Override
						public void run() {
							if (count == 0) {
								player.setNextFacePosition(new Position(object.getX() - 1, object.getY(), 0));
								player.animate(new Animation(12216));
								player.unlock();
							} else if (count == 2) {
								player.setNextPosition(new Position(3651, 5122, 0));
								player.setNextFacePosition(new Position(3651, 5121, 0));
								player.animate(new Animation(12217));
							} else if (count == 3) {
								// TODO find emote
								// player.getPackets().sendObjectAnimation(new WorldObject(45078, 0, 3, 3651,
								// 5123, 0), new Animation(12220));
							} else if (count == 5) {
								player.unlock();
								stop();
							}
							count++;
						}

					}, 1, 0);
				} else if (id == 45076) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Gold_Ore));
				} else if (id == 5999) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LRC_Coal_Ore));
				} else if (id == 4027) {
					player.getActionManager().setAction(new Mining(object, RockDefinitions.LIMESTONE));
				} else if (id == 45078) {
					player.useStairs(2413, new Position(3012, 9832, 0), 2, 2);
				} else if (id == 45079) {
					player.getBank().openDepositBox();
				} else if (id == 24357 && object.getX() == 3188 && object.getY() == 3355) {
					player.useStairs(-1, new Position(3189, 3354, 1), 0, 1);
				} else if (id == 24359 && object.getX() == 3188 && object.getY() == 3355) {
					player.useStairs(-1, new Position(3189, 3358, 0), 0, 1);
				} else if (id == 1805 && object.getX() == 3191 && object.getY() == 3363) {
					WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() - 1,
							object.getX(), object.getY(), object.getZ());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(3191, player.getY() >= object.getY() ? object.getY() - 1 : object.getY(),
								-1, false);
						if (player.getY() >= object.getY()) {
							player.getDialogueManager().startDialogue("SimpleNPCMessage", 198,
									"Greetings bolt adventurer. Welcome to the guild of", "Champions.");
						}
					}
				}
				// start of varrock dungeon
				else if (id == 29355 && object.getX() == 3230 && object.getY() == 9904) {
					player.useStairs(828, new Position(3229, 3503, 0), 1, 2);
				} else if (id == 24264) {
					player.useStairs(833, new Position(3229, 9904, 0), 1, 2);
				} else if (id == 24366) {
					player.useStairs(828, new Position(3237, 3459, 0), 1, 2);
				} else if (id == 882 && object.getX() == 3237 && object.getY() == 3458) {
					player.useStairs(833, new Position(3237, 9858, 0), 1, 2);
				} else if (id == 29355 && object.getX() == 3097 && object.getY() == 9868) {
					player.useStairs(828, new Position(3096, 3468, 0), 1, 2);
				} else if (id == 26934) {
					player.useStairs(833, new Position(3097, 9868, 0), 1, 2);
				} else if (id == 29355 && object.getX() == 3088 && object.getY() == 9971) {
					player.useStairs(828, new Position(3087, 3571, 0), 1, 2);
				} else if (id == 65453) {
					player.useStairs(833, new Position(3089, 9971, 0), 1, 2);
				} else if (id == 12389 && object.getX() == 3116 && object.getY() == 3452) {
					player.useStairs(833, new Position(3117, 9852, 0), 1, 2);
				} else if (id == 29355 && object.getX() == 3116 && object.getY() == 9852) {
					player.useStairs(833, new Position(3115, 3452, 0), 1, 2);
				} else if (id == 69526) {
					GnomeAgility.walkGnomeLog(player);
				} else if (id == 69383) {
					GnomeAgility.climbGnomeObstacleNet(player);
				} else if (id == 69508) {
					GnomeAgility.climbUpGnomeTreeBranch(player);
				} else if (id == 2312) {
					GnomeAgility.walkGnomeRope(player);
				} else if (id == 4059) {
					GnomeAgility.walkBackGnomeRope(player);
				} else if (id == 69507) {
					GnomeAgility.climbDownGnomeTreeBranch(player);
				} else if (id == 69384) {
					GnomeAgility.climbGnomeObstacleNet2(player);
				} else if (id == 69377 || id == 69378) {
					GnomeAgility.enterGnomePipe(player, object.getX(), object.getY());
				} else if (id == 69389) {
					GnomeAgility.jumpDown(player, object);
				} else if (id == 69506) {
					GnomeAgility.climbUpTree(player);
				} else if (Wilderness.isDitch(id)) {// wild ditch
					player.getDialogueManager().startDialogue("WildernessDitch", object);
				} else if (id == 42611) {// Magic Portal
					player.getDialogueManager().startDialogue("MagicPortal");
				} else if (object.getDefinitions().name.equalsIgnoreCase("Obelisk") && object.getY() > 3525) {
					// Who the fuck removed the controler class and the code from SONIC!!!!!!!!!!
					// That was an hour of collecting coords :fp: Now ima kill myself.
				} else if (id == 27254) {// Edgeville portal
					player.getPackets().sendGameMessage("You enter the portal...");
					player.useStairs(10584, new Position(3087, 3488, 0), 2, 3, "..and are transported to Edgeville.");
					player.addWalkSteps(1598, 4506, -1, false);
				} else if (id == 12202) {// mole entrance
					if (!player.getInventory().containsItemToolBelt(952)) {
						player.getPackets().sendGameMessage("You need a spade to dig this.");
						return;
					}
					if (player.getX() != object.getX() || player.getY() != object.getY()) {
						player.lock();
						player.addWalkSteps(object.getX(), object.getY());
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								InventoryOptionsHandler.dig(player);
							}

						}, 1);
					} else {
						InventoryOptionsHandler.dig(player);
					}
				} else if (id == 12230 && object.getX() == 1752 && object.getY() == 5136) {// mole exit
					player.setNextPosition(new Position(2986, 3316, 0));
				} else if (id == 15522) {// portal sign
					if (player.withinDistance(new Position(1598, 4504, 0), 1)) {// PORTAL
						// 1
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Edgeville");
						player.getPackets().sendIComponentText(327, 14, "This portal will take you to edgeville. There "
								+ "you can multi pk once past the wilderness ditch.");
					}
					if (player.withinDistance(new Position(1598, 4508, 0), 1)) {// PORTAL
						// 2
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Mage Bank");
						player.getPackets().sendIComponentText(327, 14, "This portal will take you to the mage bank. "
								+ "The mage bank is a 1v1 deep wilderness area.");
					}
					if (player.withinDistance(new Position(1598, 4513, 0), 1)) {// PORTAL
						// 3
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13, "Magic's Portal");
						player.getPackets().sendIComponentText(327, 14,
								"This portal will allow you to teleport to areas that "
										+ "will allow you to change your magic spell book.");
					}
				} else if (id == 38811 || id == 37929) {// corp beast
					if (object.getX() == 2971 && object.getY() == 4382) {
						player.getInterfaceManager().sendInterface(650);
					} else if (object.getX() == 2918 && object.getY() == 4382) {
						player.stopAll();
						player.setNextPosition(
								new Position(player.getX() == 2921 ? 2917 : 2921, player.getY(), player.getZ()));
					}
				}

				else if (id == 37928 && object.getX() == 2883 && object.getY() == 4370) {
					player.stopAll();
					player.setNextPosition(new Position(3214, 3782, 0));
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 38815 && object.getX() == 3209 && object.getY() == 3780 && object.getZ() == 0) {
					if (player.getSkills().getLevelForXp(Skills.WOODCUTTING) < 37
							|| player.getSkills().getLevelForXp(Skills.MINING) < 45
							|| player.getSkills().getLevelForXp(Skills.SUMMONING) < 23
							|| player.getSkills().getLevelForXp(Skills.FIREMAKING) < 47
							|| player.getSkills().getLevelForXp(Skills.PRAYER) < 55) {
						player.getPackets().sendGameMessage(
								"You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
						return;
					}
					player.stopAll();
					player.setNextPosition(new Position(2885, 4372, 2));
					player.getControlerManager().forceStop();
					// TODO all reqs, skills not added

				} else if (id == 48803 && player.isKalphiteLairSetted()) {
					player.setNextPosition(new Position(3508, 9494, 0));
				} else if (id == 48802 && player.isKalphiteLairEntranceSetted()) {
					player.setNextPosition(new Position(3401, 9485, 0));
				} else if (id == 3829) {
					if (object.getX() == 3419 && object.getY() == 9510) {
						player.useStairs(828, new Position(3226, 3108, 0), 1, 2);
					}
				} else if (id == 3832) {
					if (object.getX() == 3508 && object.getY() == 9494) {
						player.useStairs(828, new Position(3445, 9497, 0), 1, 2);
					}
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				} else if (id == 54019 || id == 54020 || id == 55301) {
					player.getDialogueManager().startDialogue("DTClaimRewards");
				} else if (id == 1817 && object.getX() == 2273 && object.getY() == 4680) { // kbd lever
					Magic.pushLeverTeleport(player, new Position(3067, 10254, 0));
				} else if (id == 1816 && object.getX() == 3067 && object.getY() == 10252) { // kbd out lever
					Magic.pushLeverTeleport(player, new Position(2273, 4681, 0));
				} else if (id == 32015 && object.getX() == 3069 && object.getY() == 10256) { // kbd stairs
					player.useStairs(828, new Position(3017, 3848, 0), 1, 2);
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 1765 && object.getX() == 3017 && object.getY() == 3849) { // kbd out stairs
					player.stopAll();
					player.setNextPosition(new Position(3069, 10255, 0));
					player.getControlerManager().forceStop();
				} else if (id == 14315) {
					if (Lander.canEnter(player, 0)) {
						return;
					}
				} else if (id == 25631) {
					if (Lander.canEnter(player, 1)) {
						return;
					}
				} else if (id == 25632) {
					if (Lander.canEnter(player, 2)) {
						return;
					}
				} else if (id == 5959) {
					Magic.pushLeverTeleport(player, new Position(2539, 4712, 0));
				} else if (id == 5960) {
					Magic.pushLeverTeleport(player, new Position(3089, 3957, 0));
				} else if (id == 1814) {
					Magic.pushLeverTeleport(player, new Position(3155, 3923, 0));
				} else if (id == 1815) {
					Magic.pushLeverTeleport(player, new Position(2561, 3311, 0));
				} else if (id == 62675) {
					player.getCutscenesManager().play("DTPreview");
				} else if (id == 62681) {
					player.getDominionTower().viewScoreBoard();
				} else if (id == 62678 || id == 62679) {
					player.getDominionTower().openModes();
				} else if (id == 62688) {
					player.getDialogueManager().startDialogue("DTClaimRewards");
				} else if (id == 62677) {
					player.getDominionTower().talkToFace();
				} else if (id == 62680) {
					player.getDominionTower().openBankChest();
				} else if (id == 48797) {
					player.useStairs(-1, new Position(3877, 5526, 1), 0, 1);
				} else if (id == 48798) {
					player.useStairs(-1, new Position(3246, 3198, 0), 0, 1);
				} else if (id == 48678 && x == 3858 && y == 5533) {
					player.useStairs(-1, new Position(3861, 5533, 0), 0, 1);
				} else if (id == 48678 && x == 3858 && y == 5543) {
					player.useStairs(-1, new Position(3861, 5543, 0), 0, 1);
				} else if (id == 48678 && x == 3858 && y == 5533) {
					player.useStairs(-1, new Position(3861, 5533, 0), 0, 1);
				} else if (id == 48677 && x == 3858 && y == 5543) {
					player.useStairs(-1, new Position(3856, 5543, 1), 0, 1);
				} else if (id == 48677 && x == 3858 && y == 5533) {
					player.useStairs(-1, new Position(3856, 5533, 1), 0, 1);
				} else if (id == 48679) {
					player.useStairs(-1, new Position(3875, 5527, 1), 0, 1);
				} else if (id == 48688) {
					player.useStairs(-1, new Position(3972, 5565, 0), 0, 1);
				} else if (id == 48683) {
					player.useStairs(-1, new Position(3868, 5524, 0), 0, 1);
				} else if (id == 48682) {
					player.useStairs(-1, new Position(3869, 5524, 0), 0, 1);
				} else if (id == 62676) { // dominion exit
					player.useStairs(-1, new Position(3374, 3093, 0), 0, 1);
				} else if (id == 62674) { // dominion entrance
					player.useStairs(-1, new Position(3744, 6405, 0), 0, 1);
				} else if (id == 3192) {
					player.getDialogueManager().startDialogue("PkScores");
				} else if (id == 65349) {
					player.useStairs(-1, new Position(3044, 10325, 0), 0, 1);
				} else if (id == 32048 && object.getX() == 3043 && object.getY() == 10328) {
					player.useStairs(-1, new Position(3045, 3927, 0), 0, 1);
				} else if (id == 26194) {
					player.getDialogueManager().startDialogue("PartyRoomLever");
				} else if (id == 61190 || id == 61191 || id == 61192 || id == 61193) {
					if (objectDef.containsOption(0, "Chop down")) {
						player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL, false));
					}
				} else if (id == 20573) {
					player.getControlerManager().startControler("RefugeOfFear");
				} else if (id == 67050) {
					player.useStairs(-1, new Position(3359, 6110, 0), 0, 1);
				} else if (id == 67053) {
					player.useStairs(-1, new Position(3120, 3519, 0), 0, 1);
				} else if (id == 67051) {
					player.getDialogueManager().startDialogue("Marv", false);
				} else if (id == 67052) {
					Crucible.enterCrucibleEntrance(player);
				} else if (id == WorldObject.asOSRS(31923)) {
					player.getDialogueManager().startDialogue("AncientAltar");
				} else if (id == 172) {
					CrystalChest.open(player);
					return;
				} else if (id == 170) {
					if (player.getInventory().containsItem(991, 1)) {
						int[][] common = { {1620, 10}, {2360, 10}, {1210, 10}, {2298, 10}, {995, 5000}, {563, 200}, {560, 200}, {562, 1000}, {29464, 250}}; //Other ids go in there as well
						int length = common.length;
						length--;
						player.getInventory().deleteItem(991, 1);
						int reward = Utils.getRandom(length);
						player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
					} else {
						player.sm("You need a muddy key to open this chest");
					}
					return;
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "trapdoor":
					case "manhole":
						if (objectDef.containsOption(0, "Open")) {
							WorldObject openedHole = new WorldObject(object.getId() + 1, object.getType(),
									object.getRotation(), object.getX(), object.getY(), object.getZ());
							// if (World.removeTemporaryObject(object, 60000, true)) {
							player.faceObject(openedHole);
							World.spawnTemporaryObject(openedHole, 60000, true);
							// }
						}
						break;
					case "chest":
					case "closed chest":
						if (Falador.isObject(object)) {
							break;
						}
						if (objectDef.containsOption(0, "Open")) {
							if (object.getId() == 6910) {
								player.animate(new Animation(536));
								player.lock(4);
								WorldObject openedChest = new WorldObject(37010, object.getType(), object.getRotation(),
										object.getX(), object.getY(), object.getZ());
								player.faceObject(openedChest);
								World.spawnTemporaryObject(openedChest, 60000, true);
								break;
							} else {
								player.animate(new Animation(536));
								player.lock(2);
								WorldObject openedChest = new WorldObject(object.getId() + 1, object.getType(),
										object.getRotation(), object.getX(), object.getY(), object.getZ());
								// if (World.removeTemporaryObject(object, 60000, true)) {
								player.faceObject(openedChest);
								World.spawnTemporaryObject(openedChest, 60000, true);
							}
						}
						break;
					case "open chest":
						if (objectDef.containsOption(0, "Search")) {
							player.getPackets().sendGameMessage("You search the chest but find nothing.");
						}
						break;
					case "spinning wheel":
						player.getInterfaceManager().sendSpinningWheel();
						player.spinningWheel = true;

						break;
					case "crate":
					case "crates":
						player.getPackets().sendGameMessage("You search the crate but find nothing.");
						break;
					case "box":
					case "boxes":
						player.getPackets().sendGameMessage("You search the box but find nothing.");
						break;
					case "hay bale":
					case "hay bales":
						int needle = Misc.random(100);
						if (needle == 1) {
							player.getInventory().addItem(1733, 1);
							player.getPackets().sendGameMessage("You search the hay bale, and you find a needle!");
							break;
						} else {
							player.getPackets().sendGameMessage("You search the hay bale but find nothing...");
							break;
						}
					case "sack":
					case "sacks":
						player.getPackets().sendGameMessage("You search the sack but find nothing.");
						break;
					case "wardrobe":
						player.getPackets().sendGameMessage("You search the wardrobe but find nothing.");
						break;
					case "cabinet":
					case "cabinets":
						player.getPackets().sendGameMessage("You search the cabinet but find nothing.");
						break;
					case "shelf":
					case "shelves":
						player.getPackets().sendGameMessage("You search the shelf but find nothing.");
						break;
					case "footlocker":
						player.getPackets().sendGameMessage("You search the footlocker but find nothing.");
						break;
					case "body":
						player.getPackets().sendGameMessage("You inspect the body... OMG IT'S DEAD!!!!!");
						break;
					case "bed":
						player.getPackets().sendGameMessage("You search the bed but find nothing.");
						break;
					case "bookcase":
					case "bookshelf":
					case "bookshelves":
						player.getPackets().sendGameMessage("You search the book case but find nothing.");
						break;
					case "drawer":
					case "drawers":
						player.getPackets().sendGameMessage("You search the drawers but find nothing.");
						break;
					case "spiderweb":
						if (object.getRotation() == 2) {
							player.lock(2);
							if (Utils.getRandom(1) == 0) {
								player.addWalkSteps(player.getX(),
										player.getY() < y ? object.getY() + 2 : object.getY() - 1, -1, false);
								player.getPackets().sendGameMessage("You squeeze though the web.");
							} else {
								player.getPackets().sendGameMessage(
										"You fail to squeeze though the web; perhaps you should try again.");
							}
						}
						break;
					case "web":
						if (objectDef.containsOption(0, "Slash")) {
							player.animate(
									new Animation(PlayerCombat.getWeaponAttackEmote(player.getEquipment().getWeaponId(),
											player.getCombatDefinitions().getAttackStyle())));
							slashWeb(player, object);
						}
						break;
					case "anvil":
						if (objectDef.containsOption(0, "Smith")) {
							ForgingBar bar = ForgingBar.getBar(player);
							if (bar != null) {
								ForgingInterface.sendSmithingInterface(player, bar);
							} else {
								player.getPackets()
										.sendGameMessage("You have no bars which you have smithing level to use.");
							}
						}
						break;
					case "tin ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Tin_Ore));
						break;
					case "gold ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Gold_Ore));
						break;
					case "iron ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Iron_Ore));
						break;
					case "gem rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.GEM_ROCK));
						break;
					case "silver ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Silver_Ore));
						break;
					case "coal rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Coal_Ore));
						break;
					case "clay rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Clay_Ore));
						break;
					case "copper ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Copper_Ore));
						break;
					case "adamantite ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Adamant_Ore));
						break;
					case "runite ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Runite_Ore));
						break;
					case "granite rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Granite_Ore));
						break;
					case "fairy ring":
						player.getFairyRing().handleObjects(object);
						break;
					case "sandstone rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Sandstone_Ore));
						break;
					case "mithril ore rocks":
						player.getActionManager().setAction(new Mining(object, RockDefinitions.Mithril_Ore));
						break;
					case "bank deposit box":
					case "deposit box":
						if (objectDef.containsOption(0, "Deposit")) {
							player.getBank().openDepositBox();
						}
						if (objectDef.containsOption(1, "Deposit-all")) {
							player.getBank().depositAllInventory(false);
							player.getBank().depositAllMoneyPouch(false);
							player.getBank().depositAllEquipment(false);
							player.getBank().depositAllBob(false);
							player.getPackets().sendGameMessage("You deposit all of your items into the deposit box");
						}
						break;
					case "potter's wheel":
						player.getDialogueManager().startDialogue("PotteryWheel");
						break;
					case "pottery oven":
						player.getDialogueManager().startDialogue("PotteryFurnace");
						break;
					case "furnace":
						player.getDialogueManager().startDialogue("SmeltingD", object);
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(0, "Bank") || objectDef.containsOption(0, "Use")) {
							player.getBank().openBank();
						}
						break;
					// Woodcutting start
					case "tree":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL, false));
						}
						break;
					case "evergreen":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager()
									.setAction(new Woodcutting(object, TreeDefinitions.EVERGREEN, false));
						}
						break;
					case "dead tree":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.DEAD, false));
						}
						break;
					case "oak":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.OAK, false));
						}
						break;
					case "willow":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.WILLOW, false));
						}
						break;
					case "maple tree":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAPLE, false));
						}
						break;
					case "ivy":
						if (objectDef.containsOption(0, "Chop")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.IVY, false));
						}
						break;
					case "yew":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.YEW, false));
						}
						break;
					case "magic tree":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC, false));
						}
						break;
					case "cursed magic tree":
						if (objectDef.containsOption(0, "Chop down")) {
							player.getActionManager()
									.setAction(new Woodcutting(object, TreeDefinitions.CURSED_MAGIC, false));
						}
						break;
					// Woodcutting end
					case "gate":
					case "large door":
					case "metal door":
					case "arboretum door":
					case "church door":
					case "palace entrance":
					case "bamboo gate":
						if (Draynor.isObject(object)) {
							break;
						}
						if (object.getType() == 0 && (objectDef.containsOption(0, "Open")
								|| objectDef.containsOption(0, "Pass-through"))) {
							if (!handleGate(player, object)) {
								handleDoor(player, object);
							}
						}
						break;

					case "exit door":
						if (!(player.getX() == 2924 && player.getY() == 9654)
								&& !(player.getX() == 2927 && player.getY() == 9649)
								&& !(player.getX() == 2931 && player.getY() == 9640)
								&& !(player.getX() == 2938 && player.getY() == 3252)) {
							player.getPackets().sendGameMessage("You cannot enter this way.");
							break;
						} else {
							handleDoor(player, object);
							break;
						}
					case "door":
						if (Draynor.isObject(object)) {
							break;
						}
						if (object.getId() >= 2595 && object.getId() <= 2601) {
							player.getPackets()
									.sendGameMessage("You need to use the correct key on this door to unlock it.");
							break;
						}
						if (object.getType() == 0
								&& (objectDef.containsOption(0, "Open") || objectDef.containsOption(0, "Unlock"))) {
							handleDoor(player, object);
						}
						break;

					case "ladder":
						if (object.getX() == 2924 && object.getY() == 9649) {
							player.useStairs(828, new Position(2925, 3250, 0), 1, 2);
							player.getPackets().sendGameMessage("You climb up the ladder.");
							break;
						}
						if (object.getId() == 29355 || Lumbridge.isObject(object) || Varrock.isObject(object)
								|| Draynor.isObject(object) || PoisonWasteDungeon.isObject(object)
								|| TutIsland.isObject(object)) {
							break;
						} else if (object.getId() == 1752) {
							player.getPackets().sendGameMessage("You can't climb up this ladder, it is broken.");
							break;
						} else if (object.getId() == 2605) {
							player.useStairs(828, new Position(2932, 9642, 0), 1, 2);
							player.getPackets().sendGameMessage("You climb down the ladder.");
							break;
						} else if (object.getId() == 1754 && object.getX() == 2924 && object.getY() == 3250) {
							player.useStairs(828, new Position(2924, 9649, 0), 1, 2);
							player.getPackets().sendGameMessage("You climb down the ladder.");
							break;
						} else if (object.getId() == 1754 && object.getX() == 2939 && object.getY() == 3257) {
							player.useStairs(828, new Position(2939, 9656, 0), 1, 2);
							player.getPackets().sendGameMessage("You climb down the ladder.");
							break;
						} else if (object.getId() == 32015) {
							player.useStairs(828, new Position(2924, 9649, 0), 1, 2);
							player.getPackets().sendGameMessage("You climb up the ladder.");
							break;
						} else if (object.getX() == 2928 && object.getY() == 9658) {
							player.useStairs(828, new Position(2938, 3257, 0), 1, 2);
							player.getPackets().sendGameMessage("You climb up the ladder.");
							break;
						} else {
							handleLadder(player, object, 1);
							break;
						}
					case "stile":
					case "stiles":
						if (object.getX() == player.getX()) {
							if (player.getY() > object.getY()) {
								player.addWalkSteps(player.getX(), player.getY() - 3, -1, false);
							} else {
								player.addWalkSteps(player.getX(), player.getY() + 3, -1, false);
							}
						} else if (object.getY() == player.getY()) {
							if (player.getX() > object.getX()) {
								player.addWalkSteps(player.getX() - 3, player.getY(), -1, false);
							} else {
								player.addWalkSteps(player.getX() + 3, player.getY(), -1, false);
							}
						}
						player.animate(new Animation(839));
						break;
					case "staircase":
					case "stairs":
						if (Lumbridge.isObject(object) || MosLeHarmless.isObject(object) || AlKharid.isObject(object)
								|| Draynor.isObject(object) || Camelot.isObject(object) || Varrock.isObject(object)) {
							break;
						} else if (id == 10857) {
							break;
						} else {
							handleStaircases(player, object, 1);
							break;
						}
					case "dairy churn":
						player.getDialogueManager().startDialogue("Churning");
						break;
					case "loom":
						player.getDialogueManager().startDialogue("Loom");
						break;
					case "small obelisk":
						if (objectDef.containsOption(0, "Renew-points")) {
							int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
							if (player.getSkills().getLevel(Skills.SUMMONING) < summonLevel) {
								player.lock(3);
								player.animate(new Animation(8502));
								player.getSkills().set(Skills.SUMMONING, summonLevel);
								player.getPackets().sendGameMessage("You have recharged your Summoning points.", true);
							} else {
								player.getPackets().sendGameMessage("You already have full Summoning points.");
							}
						}
						break;
					case "bandos altar":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage("You pray to the gods...", true);
								player.animate(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(maxPrayer);
										player.getPackets().sendGameMessage("...and recharged your prayer.", true);
									}
								}, 2);
							} else {
								player.getPackets().sendGameMessage("You already have full prayer.");
							}
							if (id == 6552) {
								player.getDialogueManager().startDialogue("AncientAltar");
							}
						}
						break;
					case "armadyl altar":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage("You pray to the gods...", true);
								player.animate(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(maxPrayer);
										player.getPackets().sendGameMessage("...and recharged your prayer.", true);
									}
								}, 2);
							} else {
								player.getPackets().sendGameMessage("You already have full prayer.");
							}
							if (id == 6552) {
								player.getDialogueManager().startDialogue("AncientAltar");
							}
						}
						break;
					case "saradomin altar":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage("You pray to the gods...", true);
								player.animate(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(maxPrayer);
										player.getPackets().sendGameMessage("...and recharged your prayer.", true);
									}
								}, 2);
							} else {
								player.getPackets().sendGameMessage("You already have full prayer.");
							}
							if (id == 6552) {
								player.getDialogueManager().startDialogue("AncientAltar");
							}
						}
						break;
					case "zamorak altar":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage("You pray to the gods...", true);
								player.animate(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(maxPrayer);
										player.getPackets().sendGameMessage("...and recharged your prayer.", true);
									}
								}, 2);
							} else {
								player.getPackets().sendGameMessage("You already have full prayer.");
							}
							if (id == 6552) {
								player.getDialogueManager().startDialogue("AncientAltar");
							}
						}
						break;
					case "altar":
					case "gorilla statue":
						if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.lock(5);
								player.getPackets().sendGameMessage("You pray to the gods...", true);
								player.animate(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(maxPrayer);
										player.getPackets().sendGameMessage("...and recharged your prayer.", true);
									}
								}, 2);
							} else {
								player.getPackets().sendGameMessage("You already have full prayer.");
							}
							if (id == 6552) {
								player.getDialogueManager().startDialogue("AncientAltar");
							} else if (id == 409 || WorldObject.asOSRS(409) == object.getId()) {
								player.getDialogueManager().startDialogue("PrayerSwitch");
							}
						}
						break;
					}
				}
				if (Constants.DEBUG) {
					player.sm("ObjectHandler Clicked 1 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getZ());
					Logger.log("ObjectHandler", "Options: " + Arrays.toString(objectDef.options));
				}
			}
		}, objectDef.getSizeX(), Wilderness.isDitch(id) ? 4 : objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption2(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				WorldPatches patch = WorldPatches.forId(object.getId());
				if (!player.getControlerManager().processObjectClick2(object))
					return;
				ObjectAction action = null;
				ObjectAction[] actions = object.actions;
				if (actions != null) {
					action = actions[1];
				}
				if (action == null && (actions = objectDef.actions) != null) {
					action = actions[1];
				}
				if (action != null) {
					action.handle(player, object);
					return;
				}
				if (Pickables.handlePickable(player, object)) {
					return;
				} else if (object.getDefinitions().name.equalsIgnoreCase("furnace") || id == 4304) {
					player.getDialogueManager().startDialogue("SmeltingD", object);
				} else if (object.getDefinitions().name.toLowerCase().contains("spinning")) {
					player.getDialogueManager().startDialogue("SpinningD");
				} else if (id == 17010) {
					player.getDialogueManager().startDialogue("LunarAltar");
				} else if (id == 62677) {
					//player.getDominionTower().openRewards();
				} else if (id == WorldObject.asOSRS(29422)) {
					player.getFairyRing().handleObjects(object);
				} else if (id == 3628 || id == 3629) {
					handleDoor(player, object);
				} else if (id == 35390) {
					GodWars.passGiantBoulder(player, object, false);
				} else if (patch != null && player.getFarmings() != null) {
					player.getFarmings().patches[patch.getArrayIndex()].handleInspection(player);
				} else if (id == 24821) {
					ArtisanWorkshop.TakeBronzeIngots(player);
				} else if (id == 200537) {
					player.getDialogueManager().startDialogue("KrakenInstanceD");
				} else if (id == 24823) {
					ArtisanWorkshop.TakeSteelIngots(player);
				} else if (id == WorldObject.asOSRS (10060)) {
					player.getBank().openBank();
				} else if (id == 2114) {
					player.getCoalTrucksManager().investigate();
				} else if (id == 24822) {
					ArtisanWorkshop.TakeIronIngots(player);
				} else if (id == 29394) {
					ArtisanWorkshop.DepositIngots(player);
				} else if (id == 6) {
					DwarfMultiCannon.pickupCannon(player, 4, object);
				} else if (id == 29395) {
					ArtisanWorkshop.DepositIngots(player);
				} else if (EvilTree.isObject(object)) {
					EvilTree.HandleObject(player, object, 2);
				} else if (id == 80321) {
					for (int i = 0; i < 300; i++) {
						player.getPackets().sendIComponentText(275, i, "");
					}
					player.getInterfaceManager().sendInterface(275);
					player.getPackets().sendIComponentText(275, 1,"<Slime Exchange Rates");
					player.getPackets().sendIComponentText(275, 10, "Dragon Full Helmet - 75");
					player.getPackets().sendIComponentText(275, 11, "Dragon Platebody - 75");
					player.getPackets().sendIComponentText(275, 12, "Dragon Platelegs - 75");
					player.getPackets().sendIComponentText(275, 13, "Dragon Plateskirt - 75");
					player.getPackets().sendIComponentText(275, 14, "Dragon Chainbody - 75");
					player.getPackets().sendIComponentText(275, 15, "Archer's Ring - 75");
					player.getPackets().sendIComponentText(275, 16, "Berserker Ring - 75");
					player.getPackets().sendIComponentText(275, 17, "Warrior Ring - 75");
					player.getPackets().sendIComponentText(275, 18, "Seers Ring - 75");
					player.getPackets().sendIComponentText(275, 19, "Mud Battlestaff - 75");
					player.getPackets().sendIComponentText(275, 20, "Dragon Hatchet - 75");
					player.getPackets().sendIComponentText(275, 21, "Seercull - 75");
					player.getPackets().sendIComponentText(275, 22, "Godsword Shard 1 - 150");
					player.getPackets().sendIComponentText(275, 23, "Godsword Shard 2 - 150");
					player.getPackets().sendIComponentText(275, 24, "Godsword Shard 3 - 150");
					player.getPackets().sendIComponentText(275, 25, "Abyssal Whip - 150");
					player.getPackets().sendIComponentText(275, 26, "Dark Bow - 150");
					player.getPackets().sendIComponentText(275, 27, "Dragonfire Shield - 150");
					player.getPackets().sendIComponentText(275, 28, "Armadyl Hilt - 150");
					player.getPackets().sendIComponentText(275, 29, "Bandos Hilt - 150");
					player.getPackets().sendIComponentText(275, 30, "Saradomin Hilt - 150");
					player.getPackets().sendIComponentText(275, 31, "Zamorak Hilt - 150");
					player.getPackets().sendIComponentText(275, 32, "Zenyte Shard - 150");
					player.getPackets().sendIComponentText(275, 33, "Kraken Tentacle - 150");
					player.getPackets().sendIComponentText(275, 34, "Occult Necklace - 150");
					player.getPackets().sendIComponentText(275, 35, "Trident Of The Seas - 150");
					player.getPackets().sendIComponentText(275, 36, "Ring Of The Gods - 150");
					player.getPackets().sendIComponentText(275, 37, "Tyranical Ring - 150");
					player.getPackets().sendIComponentText(275, 38, "Treasnous Ring - 150");
					player.getPackets().sendIComponentText(275, 39, "Draconic Visage - 150");
					player.getPackets().sendIComponentText(275, 40, "Armadyl Godsword - 300");
					player.getPackets().sendIComponentText(275, 41, "Bandos Godsword - 300");
					player.getPackets().sendIComponentText(275, 42, "Saradomin Godsword - 300");
					player.getPackets().sendIComponentText(275, 43, "Zamorak Godsword - 300");
					player.getPackets().sendIComponentText(275, 44, "Staff Of The Dead - 300");
					player.getPackets().sendIComponentText(275, 45, "Armadyl Helmet - 300");
					player.getPackets().sendIComponentText(275, 46, "Armadyl Chestplate - 300");
					player.getPackets().sendIComponentText(275, 47, "Armadyl Chainskirt - 300");
					player.getPackets().sendIComponentText(275, 48, "Bandos Chestplate - 300");
					player.getPackets().sendIComponentText(275, 49, "Bandos Tassets - 300");
					player.getPackets().sendIComponentText(275, 50, "Ring Of The Gods (i) - 300");
					player.getPackets().sendIComponentText(275, 51, "Treasnous Ring (i) - 300");
					player.getPackets().sendIComponentText(275, 52, "Tyranical Ring (i) - 300");
					player.getPackets().sendIComponentText(275, 53, "Spirit Shield - 300");
					player.getPackets().sendIComponentText(275, 54, "Pegasin Crystal - 600");
					player.getPackets().sendIComponentText(275, 55, "Eternal Crystal - 600");
					player.getPackets().sendIComponentText(275, 56, "Primordial Crystal - 600");
					player.getPackets().sendIComponentText(275, 57, "Smouldering Crystal - 600");
					player.getPackets().sendIComponentText(275, 58, "Abyssal bludgeon - 600");
					player.getPackets().sendIComponentText(275, 59, "Abyssal Dagger - 600");
					player.getPackets().sendIComponentText(275, 60, "Royal Crossbow Pieces - 600");
					player.getPackets().sendIComponentText(275, 61, "Blessed Spirit Shield - 600");
					player.getPackets().sendIComponentText(275, 62, "Primordial Boots - 800");
					player.getPackets().sendIComponentText(275, 63, "Pegasian Boots - 800");
					player.getPackets().sendIComponentText(275, 64, "Eternal Boots - 800");
					player.getPackets().sendIComponentText(275, 65, "Divine Sigil - 800");
					player.getPackets().sendIComponentText(275, 66, "Elysian Sigil - 800");
					player.getPackets().sendIComponentText(275, 67, "Arcane Sigil - 800");
					player.getPackets().sendIComponentText(275, 68, "Spectral Sigil - 800");
					player.getPackets().sendIComponentText(275, 69, "Tanazite Fang - 1600");
					player.getPackets().sendIComponentText(275, 70, "Magic Fang - 1600");
					player.getPackets().sendIComponentText(275, 71, "Serpentine Visage - 1600");
					player.getPackets().sendIComponentText(275, 72, "Divine Spirit Shield - 1600");
					player.getPackets().sendIComponentText(275, 73, "Elysian Spirit Shield - 1600");
					player.getPackets().sendIComponentText(275, 74, "Arcane Spirit Shield - 1600");
					player.getPackets().sendIComponentText(275, 75, "Spectral Spirit Shield - 1600");
					player.getPackets().sendIComponentText(275, 76, "Pernix Cowl - 1600");
					player.getPackets().sendIComponentText(275, 77, "Pernix Body - 1600");
					player.getPackets().sendIComponentText(275, 78, "Pernix Chaps - 1600");
					player.getPackets().sendIComponentText(275, 79, "Torva Full Helm - 1600");
					player.getPackets().sendIComponentText(275, 80, "Torva Platebody - 1600");
					player.getPackets().sendIComponentText(275, 81, "Torva Platelegs - 1600");
					player.getPackets().sendIComponentText(275, 82, "Virtus Mask - 1600");
					player.getPackets().sendIComponentText(275, 83, "Virtus Robe Top - 1600");
					player.getPackets().sendIComponentText(275, 84, "Virtus Robe Legs - 1600");
					player.getPackets().sendIComponentText(275, 85, "Zaryte Bow - 1600");
					player.getPackets().sendIComponentText(275, 86, "Noxious Staff - 1600");
					player.getPackets().sendIComponentText(275, 87, "Noxious LongBow - 1600");
					player.getPackets().sendIComponentText(275, 88, "Noxious Scythe - 1600");






				} else if (id == 35390) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 60) {
						player.getPackets()
								.sendGameMessage("You must have an agility level of 60 to slide under this boulder.");
					} else {
						player.addWalkSteps(2907, 3713, -1, false);
					}
				} else if (id == 3509) {
					if (player.getInventory().getFreeSlots() > 0) {
						player.animate(new Animation(9064));
						player.getInventory().addItem(new Item(2970));
						World.spawnObject(new WorldObject(object.getId() - 1, object.getType(), object.getRotation(),
								object.getX(), object.getY(), object.getZ()));
						player.lock(2);
						return;
					}
					player.sm("You need more inventory space to do this.");
				} else if (id == 3511) {
					if (player.getInventory().getFreeSlots() > 0) {
						player.animate(new Animation(9064));
						player.getInventory().addItem(new Item(2972));
						World.spawnObject(new WorldObject(object.getId() - 1, object.getType(), object.getRotation(),
								object.getX(), object.getY(), object.getZ()));
						player.lock(2);
						return;
					}
					player.sm("You need more inventory space to do this.");
				} else if (id == 3513) {
					if (player.getInventory().getFreeSlots() > 0) {
						player.animate(new Animation(9064));
						player.getInventory().addItem(new Item(2974));
						World.spawnObject(new WorldObject(object.getId() - 1, object.getType(), object.getRotation(),
								object.getX(), object.getY(), object.getZ()));
						player.lock(2);
						return;
					}
					player.sm("You need more inventory space to do this.");
				} else if (id == 2145) {
					World.spawnNPC(457, new Position(3250, 3194, 0), -1, 0, true);
					player.getPackets().sendGameMessage("A ghost appears, I should talk to him.");
				} else if (id == 5492) {
					if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
						int success = Utils.random(8);
						if (success == 1) {
							WorldObject openedDoor = new WorldObject(object.getId(), object.getType(),
									object.getRotation() - 1, object.getX(), object.getY(), object.getZ());
							if (World.removeTemporaryObject(object, 1200, false)) {
								World.spawnTemporaryObject(openedDoor, 1200, false);
								player.lock(2);
								player.stopAll();
								player.addWalkSteps(3041,
										player.getY() >= object.getY() ? object.getY() - 1 : object.getY(), -1, false);
								if (player.getY() >= object.getY()) {
									player.getPackets().sendGameMessage("You succesfully pick-locked the door.");
									player.useStairs(828, new Position(3149, 9652, 0), 1, 2);
								}
							}
						} else if (success >= 2 && success <= 5) {
							player.getPackets().sendGameMessage("You fail to pick the lock and hurt yourself.");
							player.animate(new Animation(981));
							int damage = Utils.random(100, 250);
							player.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
						} else {
							player.getPackets().sendGameMessage("You fail to pick the lock.");
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a thieving level of atleast 28 to attempt to pick this lock.");
					}
				} else if (id >= 38659 && id <= 38668) {
					ShootingStars.prospect(player);
				} else if (id == 15410) {
					player.getSkills().addXp(Skills.CONSTRUCTION, 1);
				} else if (id == 62688) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have a Dominion Factor of " + player.getDominionTower().getDominionFactor() + ".");
				} else if (id == 68107) {
					FightKiln.enterFightKiln(player, true);
				} else if (id == 34384 || id == 34383 || id == 14011 || id == 7053 || id == 34387 || id == 34386
						|| id == 34385 || id == 4878 || id == 4877 || id == 4876 || id == 4875 || id == 4874) {
					Thieving.handleStalls(player, object);
				} else if (id == 2418) {
					PartyRoom.openPartyChest(player);
				} else if (id == 40444) {
					LividFarm.takemoreLogs(player);
				} else if (id == 2646) {
					World.removeTemporaryObject(object, 50000, true);
					player.getInventory().addItem(1779, 1);
				} else if (id == 67051) {
					player.getDialogueManager().startDialogue("Marv", true);
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "cabbage":
						if (objectDef.containsOption(1, "Pick") && player.getInventory().addItem(1965, 1)) {
							player.animate(new Animation(827));
							player.lock(2);
							World.removeTemporaryObject(object, 60000, false);
						}
						break;
					case "bank":
					case "bank chest":
					case "bank booth":
					case "counter":
						if (objectDef.containsOption(1, "Bank")) {
							player.getBank().openBank();
						}
						break;
					case "gates":
					case "gate":
					case "metal door":
					case "arboretum door":
						if (object.getType() == 0 && objectDef.containsOption(1, "Open")) {
							handleGate(player, object);
						}
						break;
					case "door":
						if (id == 21167 && object.getY() == 2639 && object.getX() == 10424) {
							player.sm("You can't leave this area.");
							return;
						}
						if (object.getType() == 0 && objectDef.containsOption(1, "Open")) {
							handleDoor(player, object);
						}
						break;
					case "ladder":
						if (object.getId() == 200195) {
							break;
						}
						if (Lumbridge.isObject(object)) {
							break;
						} else {
							handleLadder(player, object, 2);
							break;
						}
					case "staircase":
						handleStaircases(player, object, 2);
						break;
					}
				}
				if (Constants.DEBUG) {
					Logger.log("ObjectHandler", "Clicked 2 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getZ());
					Logger.log("ObjectHandler", "Options: " + Arrays.toString(objectDef.options));
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption3(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		if (Constants.DEBUG) {
			System.out.println("cliked 3 at object id : " + object.getId() + ", " + object.getX() + ", " + object.getY()
					+ ", " + object.getZ());
			Logger.logMessage("cliked 3 at object id : " + object.getId() + ", " + object.getX() + ", " + object.getY()
					+ ", " + object.getZ());
		}
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				WorldPatches patch = WorldPatches.forId(object.getId());
				if (!player.getControlerManager().processObjectClick3(object)) {
					return;
				}
				ObjectAction action = null;
				ObjectAction[] actions = object.actions;
				if (actions != null) {
					action = actions[2];
				}
				if (action == null && (actions = objectDef.actions) != null) {
					action = actions[2];
				}
				if (action != null) {
					action.handle(player, object);
					return;
				}
				switch (objectDef.name.toLowerCase()) {
				case "gate":
				case "metal door":
					if (object.getType() == 0 && objectDef.containsOption(2, "Open")) {
						handleGate(player, object);
					}
					break;
				case "door":
					if (object.getType() == 0 && objectDef.containsOption(2, "Open")) {
						handleDoor(player, object);
					}
					break;
				case "ladder":
					if (Lumbridge.isObject(object)) {
						break;
					} else {
						handleLadder(player, object, 3);
						break;
					}
				case "staircase":
					handleStaircases(player, object, 3);
					break;
				case "bank":
				case "bank chest":
				case "bank booth":
				case "counter":
					player.getGeManager().openCollectionBox();
					break;
				}
				if (EvilTree.isObject(object)) {
					EvilTree.HandleObject(player, object, 3);
				}
				if (patch != null && player.getFarmings() != null) {
					player.getFarmings().patches[patch.getArrayIndex()].handleClear(player);
					return;
				}
				if (id == WorldObject.asOSRS(29422)) {
					player.getFairyRing().openInterface();
					return;
				}
				if (id == 5492) {
					if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
						int success = Utils.random(8);
						if (success == 1) {
							WorldObject openedDoor = new WorldObject(object.getId(), object.getType(),
									object.getRotation() - 1, object.getX(), object.getY(), object.getZ());
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(3041,
									player.getY() >= object.getY() ? object.getY() - 1 : object.getY(), -1, false);
							player.getPackets().sendGameMessage("You succesfully pick-locked the door.");
							player.useStairs(828, new Position(3149, 9652, 0), 1, 2);
						} else if (success >= 2 && success <= 5) {
							player.animate(new Animation(2244));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock and hurt yourself.");
							player.animate(new Animation(981));
							int damage = Utils.random(100, 250);
							player.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
						} else {
							player.animate(new Animation(2244));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock.");
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a thieving level of atleast 28 to attempt to pick this lock.");
					}
				} else if (id == 3628 || id == 3629) {
					handleDoor(player, object);
				} else if (id == 5501) {
					if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
						int success = Utils.random(8);
						if (success == 1) {
							player.animate(new Animation(2246));
							player.lock(2);
							player.stopAll();
							handleDoor(player, object, 60000);
							player.addWalkSteps(3182, 9611, -1, false);
							player.getPackets().sendGameMessage("You succesfully pick-locked the door.");
						} else {
							player.animate(new Animation(2246));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock.");
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a thieving level of atleast 28 to attempt to pick this lock.");
					}
				}
				if (Constants.DEBUG) {
					Logger.log("ObjectHandler", "cliked 3 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getZ() + ", ");
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption4(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick4(object)) {
					return;
				}
				ObjectAction action = null;
				ObjectAction[] actions = object.actions;
				if (actions != null) {
					action = actions[3];
				}
				if (action == null && (actions = objectDef.actions) != null) {
					action = actions[3];
				}
				if (action != null) {
					action.handle(player, object);
					return;
				}
				// living rock Caverns
				if (id == 45076) {
					MiningBase.propect(player, "This rock contains a large concentration of gold.");
				} else if (id == 5999) {
					MiningBase.propect(player, "This rock contains a large concentration of coal.");
				} else if (id == WorldObject.asOSRS(29422)) {
					player.getFairyRing().teleport(player.getFairyRing().getLastTeleport());	
				} else if (id == 5492) {
					if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
						int success = Utils.random(8);
						if (success == 1) {
							WorldObject openedDoor = new WorldObject(object.getId(), object.getType(),
									object.getRotation() - 1, object.getX(), object.getY(), object.getZ());
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(3041,
									player.getY() >= object.getY() ? object.getY() - 1 : object.getY(), -1, false);
							player.getPackets().sendGameMessage("You succesfully pick-locked the door.");
							player.useStairs(828, new Position(3149, 9652, 0), 1, 2);
						} else if (success >= 2 && success <= 5) {
							player.animate(new Animation(2244));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock and hurt yourself.");
							player.animate(new Animation(981));
							int damage = Utils.random(100, 250);
							player.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
						} else {
							player.animate(new Animation(2244));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock.");
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a thieving level of atleast 28 to attempt to pick this lock.");
					}
				} else if (id == 3628 || id == 3629) {
					handleDoor(player, object);
				} else if (id == 5501) {
					if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
						int success = Utils.random(8);
						if (success == 1) {
							player.animate(new Animation(2246));
							player.lock(2);
							player.stopAll();
							handleDoor(player, object, 60000);
							player.addWalkSteps(3182, 9611, -1, false);
							player.getPackets().sendGameMessage("You succesfully pick-locked the door.");
						} else {
							player.animate(new Animation(2246));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock.");
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a thieving level of atleast 28 to attempt to pick this lock.");
					}
				} else {
					switch (objectDef.name.toLowerCase()) {
					default:
						break;
					}
				}
				if (Constants.DEBUG) {
					Logger.log("ObjectHandler", "cliked 4 at object id : " + id + ", " + object.getX() + ", "
							+ object.getY() + ", " + object.getZ() + ", ");
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOption5(final Player player, final WorldObject object) {
		final ObjectDefinitions objectDef = object.getDefinitions();
		final int id = object.getId();
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				if (!player.getControlerManager().processObjectClick5(object)) {
					return;
				}
				ObjectAction action = null;
				ObjectAction[] actions = object.actions;
				if (actions != null) {
					action = actions[4];
				}
				if (action == null && (actions = objectDef.actions) != null) {
					action = actions[4];
				}
				if (action != null) {
					action.handle(player, object);
					return;
				}
				if (id == -1) {
					// unused
				} else if (id == 5492) {
					if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
						int success = Utils.random(8);
						if (success == 1) {
							WorldObject openedDoor = new WorldObject(object.getId(), object.getType(),
									object.getRotation() - 1, object.getX(), object.getY(), object.getZ());
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(3041,
									player.getY() >= object.getY() ? object.getY() - 1 : object.getY(), -1, false);
							player.getPackets().sendGameMessage("You succesfully pick-locked the door.");
							player.useStairs(828, new Position(3149, 9652, 0), 1, 2);
						} else if (success >= 2 && success <= 5) {
							player.animate(new Animation(2244));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock and hurt yourself.");
							player.animate(new Animation(981));
							int damage = Utils.random(100, 250);
							player.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
						} else {
							player.animate(new Animation(2244));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock.");
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a thieving level of atleast 28 to attempt to pick this lock.");
					}
				} else if (id == 5501) {
					if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
						int success = Utils.random(8);
						if (success == 1) {
							player.animate(new Animation(2246));
							player.lock(2);
							player.stopAll();
							handleDoor(player, object, 60000);
							player.addWalkSteps(3182, 9611, -1, false);
							player.getPackets().sendGameMessage("You succesfully pick-locked the door.");
						} else {
							player.animate(new Animation(2246));
							player.lock(5);
							player.stopAll();
							player.getPackets().sendGameMessage("You fail to pick the lock.");
						}
					} else {
						player.getPackets().sendGameMessage(
								"You must have a thieving level of atleast 28 to attempt to pick this lock.");
					}
					/*
					 * } else if (object.getId() == 15412 || object.getId() == 15410 ||
					 * object.getId() == 15411) {
					 * player.getDialogueManager().startDialogue("MakeChair"); } else if
					 * (object.getId() == 15406 || object.getId() == 15407 || object.getId() ==
					 * 15408) { player.getDialogueManager().startDialogue("MakePortal"); } else if
					 * (object.getId() == 13637 || object.getId() == 13636) { player.portalFrame =
					 * 0; player.
					 * out("You remove the portal frames, re-enter your house for it to take effect."
					 * ); } else if (object.getId() == 13581 || object.getId() == 13583 ||
					 * object.getId() == 13584 || object.getId() == 13587) { player.chair = 0;
					 * player.
					 * out("You remove the chairs, re-enter your house for it to take effect."); }
					 * else if (id == 13416 || id == 13413 || id == 13414 || id == 13417) {
					 * player.tree = 0;
					 * player.out("You remove the trees, re-enter your house for it to take effect."
					 * ); } else if (id == 13671 || id == 13670 || id == 13665) { player.throne = 0;
					 * player.
					 * out("You remove the throne, re-enter your house for it to take effect."); }
					 * else if ((id == 25029 || id == 25018)) { if (object.getX() > player.getX() &&
					 * object.getY() == player.getY()) { player.addWalkSteps((player.getX() + 2),
					 * (player.getY()), -1, false);
					 * 
					 * 
					 * } else if (object.getX() < player.getX() && object.getY() == player.getY()) {
					 * player.addWalkSteps((player.getX() - 2), (player.getY()), -1, false);
					 * 
					 * } else if (object.getX() == player.getX() && object.getY() > player.getY()) {
					 * player.addWalkSteps((player.getX()), (player.getY() + 2), -1, false);
					 * 
					 * } else if (object.getX() == player.getX() && object.getY() < player.getY()) {
					 * player.addWalkSteps((player.getX()), (player.getY() - 2), -1, false); } else
					 * { player.getPackets().
					 * sendGameMessage("You can only pass through wheat that you are in front of.");
					 * return; } } else if (id == 13613 || id == 13611 || id == 13609) {
					 * player.firePlace = 0; player.
					 * out("You remove the fireplace, re-enter your house for it to take effect.");
					 * } else if (id == 13599) { player.bookcase = 0; player.
					 * out("You remove the bookcase, re-enter your house for it to take effect."); }
					 * else if (object.getId() == 15270) { player.out("Altar"); } else if (id ==
					 * 3628 || id == 3629) { handleDoor(player, object); } else if (object.getId()
					 * == 15416) { House.makeWardrobe(object, player); } else if (object.getId() ==
					 * 15270) { House.makeAltar(object, player); } else if (object.getId() == 15260)
					 * { House.makeBed(object, player); } else if (object.getId() == 15361) {
					 * House.buildPortal(object, player); } else if (object.getId() == 13581) {
					 * House.whiteChair(object, player); } else if (object.getId() == 15361) {
					 * player.getDialogueManager().startDialogue("CreatePortal"); } else if
					 * (object.getId() == 15426) {
					 * player.getDialogueManager().startDialogue("MakeThrone"); } else if (id ==
					 * 15362) { player.getDialogueManager().startDialogue("MakeTrees"); } else if
					 * (id == 15416 || id == 15397) {
					 * player.getDialogueManager().startDialogue("MakeBook"); } else if (id ==
					 * 15418) { player.getDialogueManager().startDialogue("MakeFire"); } else if (id
					 * == 15313) { // player.getDialogueManager().startDialogue("RoomCreation"); }
					 * else if (id == 13622 && player.portal1 == true) { player.portalTele1 = 0;
					 * player.
					 * out("You remove the left portal, Changes take effect one you re-enter your house."
					 * ); player.portal1 = false; } else if (id == 13622 && player.portal2 == true)
					 * { player.portalTele2 = 0; player.
					 * out("You remove the center portal, Changes take effect one you re-enter your house."
					 * ); player.portal2 = false; } else if (id == 13622 && player.portal3 == true)
					 * { player.portalTele3 = 0; player.
					 * out("You remove the right portal, Changes take effect one you re-enter your house."
					 * ); player.portal3 = false; } else if (id == 13623 && player.portal1 == true)
					 * { player.portalTele1 = 0; player.
					 * out("You remove the left portal, Changes take effect one you re-enter your house."
					 * ); player.portal1 = false; } else if (id == 13623 && player.portal2 == true)
					 * { player.portalTele2 = 0; player.
					 * out("You remove the center portal, Changes take effect one you re-enter your house."
					 * ); player.portal2 = false; } else if (id == 13623 && player.portal3 == true)
					 * { player.portalTele3 = 0; player.
					 * out("You remove the right portal, Changes take effect one you re-enter your house."
					 * ); player.portal3 = false; } else if (id == 13624 && player.portal1 == true)
					 * { player.portalTele1 = 0; player.
					 * out("You remove the left portal, Changes take effect one you re-enter your house."
					 * ); player.portal1 = false; } else if (id == 13624 && player.portal2 == true)
					 * { player.portalTele2 = 0; player.
					 * out("You remove the center portal, Changes take effect one you re-enter your house."
					 * ); player.portal2 = false; } else if (id == 13624 && player.portal3 == true)
					 * { player.portalTele3 = 0; player.
					 * out("You remove the right portal, Changes take effect one you re-enter your house."
					 * ); player.portal3 = false; } else if (id == 13625 && player.portal1 == true)
					 * { player.portalTele1 = 0; player.
					 * out("You remove the left portal, Changes take effect one you re-enter your house."
					 * ); player.portal1 = false; } else if (id == 13625 && player.portal2 == true)
					 * { player.portalTele2 = 0; player.
					 * out("You remove the center portal, Changes take effect one you re-enter your house."
					 * ); player.portal2 = false; } else if (id == 13625 && player.portal3 == true)
					 * { player.portalTele3 = 0; player.
					 * out("You remove the right portal, Changes take effect one you re-enter your house."
					 * ); player.portal3 = false; } else if (id == 13627 && player.portal1 == true)
					 * { player.portalTele1 = 0; player.
					 * out("You remove the left portal, Changes take effect one you re-enter your house."
					 * ); player.portal1 = false; } else if (id == 13627 && player.portal2 == true)
					 * { player.portalTele2 = 0; player.
					 * out("You remove the center portal, Changes take effect one you re-enter your house."
					 * ); player.portal2 = false; } else if (id == 13627 && player.portal3 == true)
					 * { player.portalTele3 = 0; player.
					 * out("You remove the right portal, Changes take effect one you re-enter your house."
					 * ); player.portal3 = false;
					 */

				} else {
					switch (objectDef.name.toLowerCase()) {
					case "door hotspot":
						// player.getInterfaceManager().sendInterface(402);
						break;
					case "repair space":
						player.getInterfaceManager().sendInterface(397);
					case "bed space":
						if (!player.getInventory().containsItem(8778, 4)) {
							player.getPackets().sendGameMessage("You need 4 oak planks to make a bed");
						} else {
							player.getInventory().deleteItem(8778, 4);
							player.getSkills().addXp(Skills.CONSTRUCTION, 10000);
							player.animate(new Animation(898));
							player.getPackets().sendGameMessage("You make a bed");

						}
						// case "chair space":
						// House.makeChair(object, player);
						// case "chair":
						// House.removeChair(object, player);

						// case "chair space":
						// if(!player.getInventory().containsItem(960, 4)) {
						// player.getPackets().sendGameMessage("You need 4 planks to make a chair");
						// } else {
						// player.getInventory().deleteItem(960, 4);
						// player.getSkills().addXp(Skills.CONSTRUCTION, 2000);
						// player.setNextAnimation(new Animation(898));
						// player.getPackets().sendGameMessage("You make a chair");
						// }
						// case "chair":
						// World.spawnObject(
						// new WorldObject(15412, 10, 2,
						// player.getX() +1, player.getY(), player
						// .getPlane()), true);

						/*
						 * case "bookcase space": if(!player.getInventory().containsItem(8780, 4)) {
						 * //player.getPackets().
						 * sendGameMessage("You need 4 teak planks to make a bookcase"); } else {
						 * player.getInventory().deleteItem(8780, 4);
						 * player.getSkills().addXp(Skills.CONSTRUCTION, 10000);
						 * player.setNextAnimation(new Animation(898));
						 * player.getPackets().sendGameMessage("You make a bookcase"); } case
						 * "rug space": if(!player.getInventory().containsItem(8790, 4)) {
						 * //player.getPackets().
						 * sendGameMessage("You need 4 bolts of cloth to make a rug"); } else {
						 * player.getInventory().deleteItem(8790, 4);
						 * player.getSkills().addXp(Skills.CONSTRUCTION, 2000);
						 * player.setNextAnimation(new Animation(898));
						 * player.getPackets().sendGameMessage("You make a rug"); } case
						 * "wardrobe space": if(!player.getInventory().containsItem(8782, 5)) {
						 * //player.getPackets().
						 * sendGameMessage("You need 5 mahogany planks to make a wardrobe"); } else {
						 * player.getInventory().deleteItem(8782, 4);
						 * player.getSkills().addXp(Skills.CONSTRUCTION, 12000);
						 * player.setNextAnimation(new Animation(898));
						 * player.getPackets().sendGameMessage("You make a wardrobe"); }
						 */

					}
					switch (objectDef.name.toLowerCase()) {
					case "fire":
						if (objectDef.containsOption(4, "Add-logs")) {
							Bonfire.addLogs(player, object);
						}
						break;
					default:
						// player.getPackets().sendGameMessage(
						// "Nothing interesting happens.");
						break;
					}
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	private static void handleOptionExamine(final Player player, final WorldObject object) {
		if (player.isOwner()) {
			player.sendMessage("");
			player.sendMessage("Name: " + object.getDefinitions().getName() + ", ID: " + object.getId() + ", X: "
					+ object.getX() + ", Y: " + object.getY() + ", Type: " + object.getType() + ", Rot: "
					+ object.getRotation());
			player.sendMessage("ConfigByFile: " + object.getDefinitions().configFileId);
			player.sendMessage("Options: " + Arrays.toString(object.getDefinitions().options));
			player.sendMessage("Actions: " + (object.actions == null ? "null" : object.actions.length));
			player.sendMessage("");
		}
		if (player.getRights() == 0) {
			player.getPackets().sendGameMessage("It's an " + object.getDefinitions().name + ".");
		}
	}

	private static void slashWeb(Player player, WorldObject object) {

		if (Utils.getRandom(1) == 0) {
			World.spawnTemporaryObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(),
					object.getX(), object.getY(), object.getZ()), 60000, true);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else {
			player.getPackets().sendGameMessage("You fail to cut through the web.");
		}
	}

	public static boolean handleGate(Player player, WorldObject object) {
		if (World.isSpawnedObject(object)) {
			return false;
		}
		if (object.getRotation() == 0) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new Position(object.getX(), object.getY() + 1, object.getZ()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new Position(object.getX(), object.getY() - 1, object.getZ()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
					return false;
				}
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getZ());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new Position(object.getX(), object.getY() + 1, object.getZ()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new Position(object.getX(), object.getY() - 1, object.getZ()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
					return false;
				}
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getZ());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new Position(object.getX() - 1, object.getY(), object.getZ()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new Position(object.getX() + 1, object.getY(), object.getZ()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
					return false;
				}
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getZ());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new Position(object.getX() - 1, object.getY(), object.getZ()),
					object.getType());
			if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
				otherDoor = World.getObject(new Position(object.getX() + 1, object.getY(), object.getZ()),
						object.getType());
				if (otherDoor == null || otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object.getDefinitions().name)) {
					return false;
				}
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY(), object.getZ());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(), otherDoor.getType(),
					otherDoor.getRotation() + 1, otherDoor.getX(), otherDoor.getY(), otherDoor.getZ());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		}
		return false;
	}

	public static boolean handleDoor(Player player, WorldObject object, long timer) {
		if (World.isSpawnedObject(object)) {
			return false;
		}
		if (Lumbridge.isObject(object)) {
			return false;
		}
		WorldObject openedDoor = new WorldObject(object.getId(), object.getType(), object.getRotation() + 1,
				object.getX(), object.getY(), object.getZ());
		if (object.getRotation() == 0) {
			openedDoor.moveLocation(-1, 0, 0);
		} else if (object.getRotation() == 1) {
			openedDoor.moveLocation(0, 1, 0);
		} else if (object.getRotation() == 2) {
			openedDoor.moveLocation(1, 0, 0);
		} else if (object.getRotation() == 3) {
			openedDoor.moveLocation(0, -1, 0);
		}
		if (World.removeTemporaryObject(object, timer, true)) {
			player.faceObject(openedDoor);
			World.spawnTemporaryObject(openedDoor, timer, true);
			return true;
		}
		return false;
	}

	private static boolean handleDoor(Player player, WorldObject object) {
		return handleDoor(player, object, 60000);
	}

	private static boolean handleStaircases(Player player, WorldObject object, int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (Lumbridge.isObject(object)) {
			return false;
		}
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getZ() == 3) {
				return false;
			}
			player.useStairs(-1, new Position(player.getX(), player.getY(), player.getZ() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getZ() == 0) {
				return false;
			}
			player.useStairs(-1, new Position(player.getX(), player.getY(), player.getZ() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getZ() == 3 || player.getZ() == 0) {
				return false;
			}
			player.getDialogueManager().startDialogue("ClimbNoEmoteStairs",
					new Position(player.getX(), player.getY(), player.getZ() + 1),
					new Position(player.getX(), player.getY(), player.getZ() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else {
			return false;
		}
		return false;
	}

	public static boolean handleLadder(Player player, WorldObject object, int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (object.getId() == 200195) {
			return false;
		}
		if (Lumbridge.isObject(object)) {
			return false;
		}
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getZ() == 3) {
				return false;
			}
			player.useStairs(828, new Position(player.getX(), player.getY(), player.getZ() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getZ() == 0) {
				return false;
			}
			player.useStairs(828, new Position(player.getX(), player.getY(), player.getZ() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getZ() == 3 || player.getZ() == 0) {
				return false;
			}
			player.getDialogueManager().startDialogue("ClimbEmoteStairs",
					new Position(player.getX(), player.getY(), player.getZ() + 1),
					new Position(player.getX(), player.getY(), player.getZ() - 1), "Climb up the ladder.",
					"Climb down the ladder.", 828);
		} else {
			return false;
		}
		return true;
	}

	public static void handleItemOnObject(final Player player, final WorldObject object, final int interfaceId,
			final Item item) {
		final int itemId = item.getId();
		final ObjectDefinitions objectDef = object.getDefinitions();
		if (!player.getControlerManager().handleItemOnObject(object, item)) {
			return;
		}
		if (object.getId() == 26945 && itemId == 995) {
			player.getDialogueManager().startDialogue("Well");
		}
		if (itemId == 1079 || itemId == 1127 || itemId == 1163 || itemId == 1153 || itemId == 1115 || itemId == 1067
				|| itemId == 1155 || itemId == 1117 || itemId == 1075 || itemId == 1157 || itemId == 1119
				|| itemId == 1069 || itemId == 1159 || itemId == 1121 || itemId == 1071 || itemId == 1161
				|| itemId == 1123 || itemId == 1073 && object.getId() == 15621) {
			WGuildControler.handleItemOnObject(player, object, item);
		}
		String objName = object.getDefinitions().getName().toLowerCase();
		if (objName.contains("tile") || objName.contains("hole") || objName.contains("pressure plate")) {
			FishingFerretRoom.handleFerretThrow(player, object, item);
			return;
		}
		int id = item.getDefinitions().isNoted() ? item.getDefinitions().certId : itemId;
		if (SlimeExchange.prices.containsKey(id) && object.getId() == 80321) {
			int ExchangeId = SlimeExchange.prices.get(id);
			player.getInventory().deleteItem(itemId, item.getAmount());
			player.getInventory().addItem(28881, ExchangeId * item.getAmount());
		}
		if (HweenExchange.prices.containsKey(id) && object.getId() == 205107) {
			int ExchangeId = HweenExchange.prices.get(id);
			player.getInventory().deleteItem(itemId, item.getAmount());
			player.getInventory().addItem(28739, ExchangeId * item.getAmount());
		}
		if (itemId == 1925 && object.getId() == 10814) {
			player.getInventory().deleteItem(1925, 1);
			player.faceObject(object);
			player.animate(new Animation(895));
			player.getInventory().addItem(1783, 1);
			player.sm("You fill the bucket with sand");

		}

		if (itemId == 6660 && object.getId() >= 10087 && object.getId() <= 10089) {
			player.sm("You throw the fishing explosive into the water...");
			player.getInventory().deleteItem(6660, 1);
			player.faceObject(object);
			player.animate(new Animation(7530));
			World.sendProjectile(player, player, object, 1281, 21, 21, 90, 65, 50, 0);
			CoresManager.fastExecutor.schedule(new TimerTask() {
				int explosive = 3;

				@Override
				public void run() {
					try {
						if (explosive == 1) {
							World.spawnNPC(114, player, -1, 0, true);
							player.sm("You have lured a mogre out of the water!");
						}
						if (this == null || explosive <= 0) {
							cancel();
							return;
						}
						if (explosive >= 0) {
							explosive--;
						}
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 0, 6000);
			return;
		}
		player.setCoordsEvent(new CoordsEvent(object, new Runnable() {
			@Override
			public void run() {
				if (!player.getControlerManager().handleItemOnObject(object, item)) {
					return;
				}
				player.faceObject(object);
				switch (objectDef.name.toLowerCase()) {
					case "furnace":
						if (item.getId() == 29143) {
							player.getDialogueManager().startDialogue("ZenyteCrafting", object, null);
						} else if (item.getId() == 2353 || item.getId() == 4) {
							player.getDialogueManager().startDialogue("SingleSmithingD", object, SmeltingBar.CANNON_BALLS);
						} else if (itemId == 2357) {
							JewllerySmithing.openInterface(player);
						}
						break;
					case "altar":
						Bones bone = BonesOnAltar.isGood(item);
						if (bone != null) {
							player.getDialogueManager().startDialogue("PrayerD", bone, object);
							break;
						} else {
							player.getPackets().sendGameMessage("Nothing interesting happens.");
							break;
						}
					case "sink":
					case "fountain":
					case "well":
					case "pump":
					case "water trough":
						if (itemId == 229) {
							player.getInventory().addItem(227, 1);
							player.getInventory().deleteItem(229, 1);
							player.out("You fill the vial with water.");
						} else if (itemId == 1925) {
							player.getInventory().addItem(1929, 1);
							player.getInventory().deleteItem(1925, 1);
							player.out("You fill the bucket with water.");
						} else if (itemId == 1825) {
							player.getInventory().addItem(1823, 1);
							player.getInventory().deleteItem(1825, 1);
							player.out("You fill the waterskin with water.");
						} else if (itemId == 1827) {
							player.getInventory().addItem(1823, 1);
							player.getInventory().deleteItem(1827, 1);
							player.out("You fill the waterskin with water.");
						} else if (itemId == 1829) {
							player.getInventory().addItem(1823, 1);
							player.getInventory().deleteItem(1829, 1);
							player.out("You fill the waterskin with water.");
						} else if (itemId == 1831) {
							player.getInventory().addItem(1823, 1);
							player.getInventory().deleteItem(1831, 1);
							player.out("You fill the waterskin with water.");
						} else if (itemId == 1923) {
							player.getInventory().addItem(1921, 1);
							player.getInventory().deleteItem(1923, 1);
							player.out("You fill the bowl with water.");
						} else if (itemId == 1935) {
							player.getInventory().addItem(1937, 1);
							player.getInventory().deleteItem(1935, 1);
							player.out("You fill the jug with water.");
						} else if (itemId == 7728) {
							player.getInventory().addItem(4458, 1);
							player.getInventory().deleteItem(7728, 1);
							player.out("You fill the cup with water.");
						} else if (itemId == 5333) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5333, 1);
							player.out("You fill the watering can with water.");
						} else if (itemId == 5331) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5331, 1);
							player.out("You fill the watering can with water.");
						} else if (itemId == 5334) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5334, 1);
							player.out("You fill the watering can with water.");
						} else if (itemId == 5335) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5335, 1);
							player.out("You fill the watering can with water.");
						} else if (itemId == 5336) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5336, 1);
							player.out("You fill the watering can with water.");
						} else if (itemId == 5337) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5337, 1);
							player.out("You fill the watering can with water.");
						} else if (itemId == 5338) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5338, 1);
							player.out("You fill the watering can with water.");
						} else if (itemId == 5339) {
							player.getInventory().addItem(5340, 1);
							player.getInventory().deleteItem(5339, 1);
							player.out("You fill the watering can with water.");
						} else {
							player.out("You cannot fill this item with water.");
						}
						break;
				}
				if (Ectofuntus.handleItemOnObject(player, itemId, object.getId()))
					return;
				if (itemId == 8778 || itemId == 960 || itemId == 8782 && object.getId() == 13705) {
					int PlankIndex = ConBench.getIndex(item.getId());
					if (PlankIndex != -1) {
						player.getDialogueManager().startDialogue("ConBench", PlankIndex);
					}

				}
				if (itemId == Item.asOSRS(13273) && object.getId() == WorldObject.asOSRS(27029)) {
					player.startEvent(event -> {
						player.lock();
						event.delay(1);
						player.getDialogueManager().startDialogue("ItemMessage", "You place the Unsired into the Font of Consumption...", Item.asOSRS(13273));
						player.animate(827);
						World.sendGraphics(player, Graphics.createOSRS(1276, 0, 50), Position.of(3039, 4774));
						event.delay(2);
						player.getInventory().deleteItem(item);
						int reward = Item.asOSRS(Unsired.roll(player));
						player.getInventory().addItem(reward, 1);
						player.getDialogueManager().startDialogue("ItemMessage", "The Font consumes the Unsired and returns your a reward.", reward);
						player.unlock();
					});
				} else if (itemId == Item.asOSRS(20800) && WorldObject.is(29878, true, object)) {
					player.startEvent(event -> {
						player.getPackets().sendGameMessage("You fill the gourd vial.");
						while (true) {
							if (!player.getInventory().hasItem(Item.asOSRS(EMPTY_GOURD_VIAL)))
								return;
							player.animate(832);
							player.getInventory().deleteItem(Item.asOSRS(EMPTY_GOURD_VIAL), 1);
							player.getInventory().addItem(Item.asOSRS(WATER_FILLED_GOURD_VIAL), 1);
							event.delay(2);
						}
					});
				} else if (itemId == 1925 && object.getId() == 7837) {
					CompostBin.handleCompost(player);
				} else if (itemId == 6470 || itemId == 6472 || itemId == 6474 || itemId == 6476 && object.getId() == 7837) {
					CompostBin.handleSuperCompost(player, item);
				} else if (itemId == 453 && object.getId() == 2114) {
					player.getCoalTrucksManager().addCoal();
				} else if (itemId != 6055 && object.getId() == 7837) {
					CompostBin.wrongItems(player);
				} else if (itemId == 6055 && object.getId() == 7837) {
					CompostBin.handleBin(player);
				} else if (item.getId() == PatchConstants.SPADE) {
					WorldPatches patch = WorldPatches.forId(object.getId());
					if (patch != null && player.getFarmings() != null) {
						player.getFarmings().patches[patch.getArrayIndex()].handleClear(player);
					}
				} else if (item.getDefinitions().getName().toLowerCase().contains("plant cure")) {
					WorldPatches patch = WorldPatches.forId(object.getId());
					if (patch != null && player.getFarmings() != null) {
						player.getFarmings().patches[patch.getArrayIndex()].handleCuring(player, itemId);
					}
				} else if (item.getDefinitions().getName().toLowerCase().contains("compost")) {
					WorldPatches patch = WorldPatches.forId(object.getId());
					if (patch != null && player.getFarmings() != null) {
						player.getFarmings().patches[patch.getArrayIndex()].handleCompost(player, itemId);
					}
				} else if (item.getDefinitions().getName().toLowerCase().contains("watering can")) {
					WorldPatches patch = WorldPatches.forId(object.getId());
					if (patch != null && player.getFarmings() != null) {
						player.getFarmings().patches[patch.getArrayIndex()].handleWatering(player, itemId);
					}
				} else if (item.getDefinitions().getName().toLowerCase().contains("seed")
						|| item.getDefinitions().getName().toLowerCase().contains("mushroom spore")
						|| item.getDefinitions().getName().toLowerCase().contains("acorn")) {
					WorldPatches patch = WorldPatches.forId(object.getId());
					if (patch != null && player.getFarmings() != null) {
						player.getFarmings().patches[patch.getArrayIndex()].handlePlanting(player, itemId);
					}
				} else if (itemId == 1438 && object.getId() == 2452) {
					Runecrafting.enterAirAltar(player);
				} else if (itemId == 1440 && object.getId() == 2455) {
					Runecrafting.enterEarthAltar(player);
				} else if (itemId == 1442 && object.getId() == 2456) {
					Runecrafting.enterFireAltar(player);
				} else if (itemId == 1444 && object.getId() == 2454) {
					Runecrafting.enterWaterAltar(player);
				} else if (itemId == 1446 && object.getId() == 2457) {
					Runecrafting.enterBodyAltar(player);
				} else if (itemId == 1448 && object.getId() == 2453) {
					Runecrafting.enterMindAltar(player);
				} else if (item.getId() == 1947 && object.getId() == 70034) {
					if (player.hasGrainInHopper) {
						player.getPackets().sendGameMessage(
								"You already have grain placed in the hopper. Try using the hopper controls.");
					} else if (!player.hasGrainInHopper) {
						player.hasGrainInHopper = true;
						player.getPackets().sendGameMessage("You place the grain into the hopper.");
						player.getInventory().deleteItem(1947, 1);
					}
				} else if ((object.getId() == 104765 || object.getId() == 104766) && item.getId() == 29139) {
					if (player.getInventory().contains(item, new Item(6573))) {
						player.animate(3243);
						player.getInventory().deleteItem(29139, 1);
						player.getInventory().deleteItem(6573, 1);
						player.getInventory().addItem(29141, 1);
						player.sendMessage("You fuse the Zenyte shard with your Onyx, and create an Uncut zenyte.");
					}
				} else if (object.getId() == 2595 && itemId == 1542) {
					player.getPackets().sendGameMessage("You use the maze key on the door and unlock it.");
					handleDoor(player, object);
				} else if (object.getId() == 2596 && itemId == 1543) {
					player.getPackets().sendGameMessage("You use the red key on the door and unlock it.");
					handleDoor(player, object);
				} else if (object.getId() == 2597 && itemId == 1544) {
					player.getPackets().sendGameMessage("You use the orange key on the door and unlock it.");
					handleDoor(player, object);
				} else if (object.getId() == 2598 && itemId == 1545) {
					player.getPackets().sendGameMessage("You use the yellow key on the door and unlock it.");
					handleDoor(player, object);
				} else if (object.getId() == 2599 && itemId == 1546) {
					player.getPackets().sendGameMessage("You use the blue key on the door and unlock it.");
					handleDoor(player, object);
				} else if (object.getId() == 2600 && itemId == 1547) {
					player.getPackets().sendGameMessage("You use the magenta key on the door and unlock it.");
					handleDoor(player, object);
				} else if (object.getId() == 2601 && itemId == 1548) {
					player.getPackets().sendGameMessage("You use the green key on the door and unlock it.");
					handleDoor(player, object);
				} else if (object.getId() == 25115) {
					if (itemId == 1791) {
						if (player.bowl) {
							player.getPackets().sendGameMessage("You already used the unfired bowl on the door.");
						} else {
							player.getPackets()
									.sendGameMessage("You use the unfired bowl on the door and a barrier is removed.");
							player.bowl = true;
							player.getInventory().deleteItem(1791, 1);
						}
					} else if (itemId == 1907) {
						if (player.bomb) {
							player.getPackets().sendGameMessage("You already used the wizard's mind bomb on the door.");
						} else {
							player.getPackets().sendGameMessage(
									"You use the wizard's mind bomb on the door and a barrier is removed.");
							player.bomb = true;
							player.getInventory().deleteItem(1907, 1);
						}
					} else if (itemId == 301) {
						if (player.pot) {
							player.getPackets().sendGameMessage("You already used the lobster pot on the door.");
						} else {
							player.getPackets()
									.sendGameMessage("You use the lobster pot on the door and a barrier is removed.");
							player.pot = true;
							player.getInventory().deleteItem(301, 1);
						}
					} else if (itemId == 950) {
						if (player.silk) {
							player.getPackets().sendGameMessage("You already used the silk on the door.");
						} else {
							player.getPackets()
									.sendGameMessage("You use the silk on the door and a barrier is removed.");
							player.silk = true;
							player.getInventory().deleteItem(950, 1);
						}
					}

				} else if (itemId == 553 && object.getId() == 2145) {
					if (player.RG == 5) {
						player.RG = 6;
						player.getInterfaceManager().handleRestlessCompleted(player);
						player.getInterfaceManager().handleRestlessCompleteInterface(player);
					} else {
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					}
				} else if (object.getId() == 2670) {
					int water = Misc.random(3);
					if (itemId == 1823) {
						player.getPackets().sendGameMessage("Your waterskin is already full.");
					} else if (itemId == 1825 || itemId == 1827 || itemId == 1829 || itemId == 1831) {
						WorldObject dryCactus = new WorldObject(2671, object.getType(), object.getRotation(),
								object.getX(), object.getY(), object.getZ());
						player.faceObject(dryCactus);
						World.spawnTemporaryObject(dryCactus, 200000, true);
						player.getSkills().addXp(Skills.WOODCUTTING, 70);
						if (water == 1) {
							player.getInventory().deleteItem(itemId, 1);
							player.getInventory().addItem(1823, 1);
							player.getPackets().sendGameMessage("You fill your waterskin up with water.");
						} else {
							player.getPackets().sendGameMessage("You were unable to get water from the cactus.");
						}
					} else {
						player.getPackets().sendGameMessage("You can't use this item on a cactus.");
					}
				} else if (object.getId() == 733 || object.getId() == 64729) {
					player.animate(new Animation(PlayerCombat.getWeaponAttackEmote(-1, 0)));
					slashWeb(player, object);
				} else if (object.getId() == 409) {
					Bones bone = BonesOnAltar.isGood(item);
					if (bone != null) {
						player.getDialogueManager().startDialogue("PrayerD", bone, object);
					} else {
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					}
				} else if (object.getId() == 32766 && itemId == 954) {

					if (player.getX() != 2462 || player.getY() != 9699) {
						return;
					}
					final boolean running = player.getRun();
					player.setRunHidden(false);
					player.lock(7);
					player.addWalkSteps(2466, 9699, -1, false);
					player.getInventory().deleteItem(954, 1);
					WorldTasksManager.schedule(new WorldTask() {
						boolean secondloop;

						@Override
						public void run() {
							if (!secondloop) {
								secondloop = true;
								player.getAppearence().setRenderEmote(155);
							} else {
								player.getAppearence().setRenderEmote(-1);
								player.setRunHidden(running);
								player.getPackets().sendGameMessage("You walk across the rope.", true);
								stop();
							}
						}
					}, 0, 5);

				} else if (object.getId() == 48803 && itemId == 954) {
					if (player.isKalphiteLairSetted()) {
						return;
					}
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLair();
				} else if (object.getId() == 13715) {
					if (BrokenItems.forId(itemId) == null) {
						player.getDialogueManager().startDialogue("SimpleMessage", "You cant repair this item.");
						return;
					}
					player.getDialogueManager().startDialogue("Repair", 945, itemId);
				} else if (object.getId() == 48802 && itemId == 954) {
					if (player.isKalphiteLairEntranceSetted()) {
						return;
					}
					player.getInventory().deleteItem(954, 1);
					player.setKalphiteLairEntrance();
				} else if (object.getId() == 128900 && itemId == 6746) { //Arclight making
					if (player.getInventory().containsItems(new int[]{6746, 29955}, new int[]{1, 3})) {

						player.getDialogueManager().startDialogue(new Dialogue() {

							@Override
							public void start() {
								sendOptionsDialogue("Combine Darklight with three ancient shards?", "Yes.", "No.");
							}

							@Override
							public void run(int interfaceId, int componentId) {
								if (componentId == OPTION_1) {
									sendItemDialogue(29965, "You combine Darklight with the shards and it seems to", "glow in your hands creating Arclight. Your Arclight has", "1000 charges.");
									player.getInventory().deleteItem(29955, 5);
									player.getInventory().deleteItem(6746, 1);
									player.getInventory().addItem(29965, 1);
									player.getArclight().charge(3);
								} else if (componentId == OPTION_2) {
									end();
								} else {
									end();
								}
							}

							@Override
							public void finish() {
								// TODO Auto-generated method stub						
							}

						});
					} else {
						player.sendMessage("You don't have the required materials to create Arclight.");
					}
				} else {
					switch (objectDef.name.toLowerCase()) {
					case "anvil":
						ForgingBar bar = ForgingBar.forId(itemId);
						if (bar != null) {
							ForgingInterface.sendSmithingInterface(player, bar);
						}
						break;
					case "fire":
						if (objectDef.containsOption(4, "Add-logs") && Bonfire.addLog(player, object, item)) {
							return;
						}
					case "range":
					case "cooking range":
					case "stove":
					case "clay fireplace":
					case "stone fireplace":
					case "marble fireplace":
						Cookables cook = Cooking.isCookingSkill(item);
						if (cook != null) {
							player.getDialogueManager().startDialogue("CookingD", cook, object);
							return;
						}
						player.getDialogueManager().startDialogue("SimpleMessage",
								"You can't cook that on a " + (objectDef.name.equals("Fire") ? "fire" : "range") + ".");
						break;
					}
					if (Constants.DEBUG) {
						System.out.println("Item on object: " + object.getId());
						Logger.logMessage("Item on object: " + object.getId());
					}
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
}
