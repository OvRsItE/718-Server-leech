package com.rs.game.world.entity.player.controller.impl;

import com.rs.Constants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Smithing;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.agility.Agility;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.network.decoders.handlers.ObjectHandler;
import com.rs.utility.Utils;

public class GodWars extends Controller {

	public static final int EMPTY_SECTOR = -1, BANDOS = 0, ARMADYL = 1, SARADOMIN = 2, ZAMORAK = 3, ZAROS = 4;
	private static final int BANDOS_SECTOR = 4, ARMADYL_SECTOR = 5, SARADOMIN_SECTOR = 6, ZAMORAK_SECTOR = 7, ZAROS_PRE_CHAMBER = 8, ZAROS_SECTOR = 9;
	private static final int ECUMENICAL_KEY_ID = 29476;
	public static final Position[] CHAMBER_TELEPORTS = {
			new Position(2863, 5357, 0), new Position(2864, 5354, 0), // bandos
			new Position(2835, 5295, 0), new Position(2839, 5296, 0), // armadyl
			new Position(2923, 5256, 0), new Position(2907, 5265, 0), // saradomin
			new Position(2925, 5332, 0), new Position(2925, 5331, 0), // zamorak
	};

	private int[] killCount = new int[5];
	private long lastPrayerRecharge;
	private int sector;
	
	@Override
	public void start() {
		sector = EMPTY_SECTOR;// We always start with this :)
		sendInterfaces();
	}

	@Override
	public boolean logout() {
		setArguments(new Object[] { killCount, lastPrayerRecharge, sector });
		return false; // so doesnt remove script
	}

	@Override
	public boolean login() {
		if (this.getArguments().length != 3) {
			player.setNextPosition(Constants.START_PLAYER_LOCATION);
			return true;
		}
		killCount = (int[]) this.getArguments()[0];
		lastPrayerRecharge = (long) this.getArguments()[1];
		sector = (int) this.getArguments()[2];
		sendInterfaces();
		return false; // so doesnt remove script
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 57225) {
			player.getDialogueManager().startDialogue("NexEntrance");
			return false;
		} else if (object.getId() == 26287 || object.getId() == 26286 || object.getId() == 26288 || object.getId() == 26289) {
			if (lastPrayerRecharge >= Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You must wait a total of 10 minutes before being able to recharge your prayer points.");
				return false;
			} else if (player.getAttackedByDelay() >= Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You cannot recharge your prayer while engaged in combat.");
				return false;
			}
			player.getPrayer().restorePrayer(player.getSkills().getLevelForXp(Skills.PRAYER) * 10);
			player.animate(new Animation(645));
			player.getPackets().sendGameMessage("Your prayer points feel rejuvinated.");
			lastPrayerRecharge = 600000 + Utils.currentTimeMillis();
			return false;
		} else if (object.getId() == 57264) {
			//player.getDialogueManager().startDialogue("SimpleMessage", "Cjay0091 was here. *TEEHEE*");
			return false;
		} else if (object.getId() == 57258) {
			if (sector == ZAROS) {
				player.getPackets().sendGameMessage("The door will not open in this direction.");
				return false;
			}

			boolean hasCerimonial = hasFullCerimonial(player);
			int requiredKc = 0;
			if (killCount[4] >= requiredKc || hasCerimonial) {
				if (hasCerimonial)
					player.getPackets().sendGameMessage("The door recognises your familiarity with the area and allows you to pass through.");
				if (killCount[4] >= requiredKc)
					killCount[4] -= requiredKc;
				sector = ZAROS;
				player.addWalkSteps(2900, 5203, -1, false);
				refresh();
			} else
				player.getPackets().sendGameMessage("You don't have enough kills to enter the lair of Zaros.");
			return false;
		} else if (object.getId() == 57234) {
			final boolean travelingEast = sector == ZAROS_PRE_CHAMBER;
			player.animate(new Animation(1133));
			final Position tile = new Position(2863 + (travelingEast ? 0 : -3), 5219, 0);
			player.setNextForceMovement(new ForceMovement(tile, 1, travelingEast ? ForceMovement.EAST : ForceMovement.WEST));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					sector = travelingEast ? ZAROS_SECTOR : ZAROS_PRE_CHAMBER;
					player.setNextPosition(tile);
					sendInterfaces();
				}
			}, 1);
			return false;
		} else if (object.getId() == 75089) {
			if (sector == ZAROS_PRE_CHAMBER) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You pull out your key once more but the door doesn't respond.");
				return false;
			}
			if (player.getInventory().containsItem(20120, 1)) {
				player.getPackets().sendGameMessage("You flash the key in front of the door");
				player.useStairs(1133, new Position(2887, 5278, 0), 1, 2, "...and a strange force flings you in.");
				sector = ZAROS_PRE_CHAMBER;
			} else
				player.getDialogueManager().startDialogue("SimpleMessage", "You try to push the door open, but it wont budge.... It looks like there is some kind of key hole.");
			return false;
		} else if (object.getId() == 57256) {
			player.useStairs(-1, new Position(2855, 5222, 0), 1, 2, "You climb down the stairs.");
			return false;
		} else if (object.getId() == 57260) {
			player.useStairs(-1, new Position(2887, 5276, 0), 1, 2, "You climb up the stairs.");
			return false;
		} else if (object.getId() == 26293) {
			player.useStairs(828, new Position(2913, 3741, 0), 1, 2);
			player.getControlerManager().forceStop();
			return false;
		} else if (object.getId() == 26384) { // bandos
			if (!player.getInventory().containsItemToolBelt(Smithing.HAMMER)) {
				player.getPackets().sendGameMessage("You look at the door but find no knob, maybe it opens some other way.");
				return false;
			}
			if (player.getSkills().getLevel(Skills.STRENGTH) < 70) {
				player.getPackets().sendGameMessage("You attempt to hit the door, but realize that you are not yet experienced enough.");
				return false;
			}
			final boolean withinBandos = sector == BANDOS_SECTOR;
			if (!withinBandos)
				player.animate(new Animation(7002));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					ObjectHandler.handleDoor(player, object, 1000);
					player.addWalkSteps(withinBandos ? 2851 : 2850, 5334, -1, false);
					sector = withinBandos ? EMPTY_SECTOR : BANDOS_SECTOR;
				}
			}, withinBandos ? 0 : 1);
			return false;
		} else if (object.getId() == 75463) {
			if (object.withinDistance(player, 7)) {
				final boolean withinArmadyl = sector == ARMADYL_SECTOR;
				final Position tile = new Position(2872, withinArmadyl ? 5280 : 5272, 0);
				WorldTasksManager.schedule(new WorldTask() {

					int ticks = 0;

					@Override
					public void run() {
						ticks++;
						if (ticks == 1) {
							player.animate(new Animation(827));
							player.setNextFacePosition(tile);
							player.lock();
						} else if (ticks == 3)
							player.animate(new Animation(385));
						else if (ticks == 5) {
							player.animate(new Animation(16635));
						} else if (ticks == 6) {
							player.getAppearence().setHidden(true);
							World.sendProjectile(player, tile, 2699, 18, 18, 20, 50, 175, 0);
							player.setNextForceMovement(new ForceMovement(player, 1, tile, 6, withinArmadyl ? ForceMovement.NORTH : ForceMovement.SOUTH));
						} else if (ticks == 9) {
							player.getAppearence().setHidden(false);
							player.animate(new Animation(16672));
							player.setNextPosition(tile);
							player.unlock();
							player.resetReceivedDamage();
							sector = withinArmadyl ? EMPTY_SECTOR : ARMADYL_SECTOR;
							stop();
							return;
						}
					}
				}, 0, 1);
			}
			return false;
		} else if (object.getId() == 26439) {
			final boolean withinZamorak = sector == ZAMORAK_SECTOR;
			final Position tile = new Position(2887, withinZamorak ? 5336 : 5346, 0);
			player.lock();
			player.setNextPosition(object);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.animate(new Animation(17454));
					player.setNextFacePosition(tile);
					if (!withinZamorak) {
						player.getPrayer().drainPrayer();
						sector = ZAMORAK_SECTOR;
					} else
						sector = EMPTY_SECTOR;
					sendInterfaces();
				}
			}, 1);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.unlock();
					player.resetReceivedDamage();
					player.animate(new Animation(-1));
					player.setNextPosition(tile);
				}
			}, 5);
			return false;
		} else if (object.getId() == 75462) {
			if (object.getX() == 2912 && (object.getY() == 5298 || object.getY() == 5299)) {
				sector = SARADOMIN_SECTOR;
				useAgilityStones(player, object, new Position(2915, object.getY(), 0), 70, 15239, 7);
			} else if (object.getX() == 2914 && (object.getY() == 5298 || object.getY() == 5299)) {
				sector = EMPTY_SECTOR;
				useAgilityStones(player, object, new Position(2911, object.getY(), 0), 70, 3378, 7);
			} else if ((object.getX() == 2919 || object.getX() == 2920) && object.getY() == 5278)
				useAgilityStones(player, object, new Position(object.getX(), 5275, 0), 70, 15239, 7);
			else if ((object.getX() == 2920 || object.getX() == 2919) && object.getY() == 5276)
				useAgilityStones(player, object, new Position(object.getX(), 5279, 0), 70, 3378, 7);
			return false;
		} else if (object.getId() >= 26425 && object.getId() <= 26428) {
			int index = object.getId() - 26425;
			int requiredKc = 0;
			if (player.getRights() == 2 || killCount[index] >= requiredKc || player.getInventory().containsItem(ECUMENICAL_KEY_ID, 1)) {
				Position tile = CHAMBER_TELEPORTS[index * 2];
				player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
				if (player.getInventory().containsItem(ECUMENICAL_KEY_ID, 1)) {
					player.getInventory().deleteItem(ECUMENICAL_KEY_ID, 1);
					player.sendMessage("Your ecumenical key consumes itself as you open the door.");
				} else {
					killCount[index] -= requiredKc;
				}
				killCount[index] -= requiredKc;
				sector = index;
				refresh();
			} else
				player.getPackets().sendGameMessage("You don't have enough kills to enter the lair of the gods.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(WorldObject object) {
		if (object.getId() == 75462) {
			player.useStairs(827, new Position(2914, 5300, 1), 1, 2);
			sector = EMPTY_SECTOR;
			player.resetReceivedDamage();
			return false;
		} else if (object.getId() == 75462) {
			player.useStairs(827, new Position(2920, 5274, 0), 1, 2);
			sector = SARADOMIN_SECTOR;
			player.resetReceivedDamage();
			return false;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 34 : 8, 601);
	}

	@Override
	public void magicTeleported(int type) {
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 34 : 8);
		removeControler();
	}
	
	public void processNPCDeath(NPC npc) {
		int id = npc.getId();
		if (id == 6230 || id == 6231 || id == 6229 || id == 6232 || id == 6240
				|| id == 6241 || id == 6242 || id == 6233 || id == 6234 || id == 6243
				|| id == 6244 || id == 6245 || id == 6246 || id == 6238 || id == 6239
				|| id == 6227 || id == 6625 || id == 6223 || id == 6222) {
			incrementKillCount(ARMADYL);
		}
		if (id == 6278 || id == 6277 || id == 6276 || id == 6283 || id == 6282
				|| id == 6280 || id == 6281 || id == 6279 || id == 6275 || id == 6271
				|| id == 6272 || id == 6273 || id == 6274 || id == 6269 || id == 6270
				|| id == 6268 || id == 6265 || id == 6263 || id == 6261 || id == 6260) {
			incrementKillCount(BANDOS);
		}
		if (id == 6257 || id == 6255 || id == 6256 || id == 6258 || id == 6259
				|| id == 6254 || id == 6252 || id == 6250 || id == 6248 || id == 6247) {
			incrementKillCount(SARADOMIN);
		}
		if (id == 6221 || id == 6219 || id == 6220 || id == 6217 || id == 6216
				|| id == 6215 || id == 6214 || id == 6213 || id == 6212 || id == 6211
				|| id == 6218 || id == 6208 || id == 6206 || id == 6204 || id == 6203) {
			incrementKillCount(ZAMORAK);
		}
	}

	@Override
	public void forceClose() {
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 34 : 8);
	}

	public void incrementKillCount(int index) {
		killCount[index]++;
		refresh();
	}

	public void resetKillCount(int index) {
		killCount[index] = 0;
		refresh();
		player.getPackets().sendGameMessage("The power of all those you slew in the dungeon drains from your body.");
	}

	public void refresh() {	
		player.getPackets().sendIComponentText(601, 8,  "" + killCount[ARMADYL]);
		player.getPackets().sendIComponentText(601, 9,  "" + killCount[BANDOS]);
		player.getPackets().sendIComponentText(601, 10,  "" + killCount[SARADOMIN]);
		player.getPackets().sendIComponentText(601, 11,  "" + killCount[ZAMORAK]);
		player.getPackets().sendIComponentText(601, 12, "" + killCount[ZAROS]);
	}

	public static void useAgilityStones(final Player player, final WorldObject object, final Position tile, int levelRequired, final int emote, final int delay) {
		if (!Agility.hasLevel(player, levelRequired))
			return;
		player.faceObject(object);
		player.addWalkSteps(object.getX(), object.getY());
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.resetReceivedDamage();
				player.useStairs(emote, tile, delay, delay + 1);
			}
		}, 1);
	}

	@Override
	public boolean sendDeath() {
		player.getControlerManager().forceStop();
		return true;
	}

	/**
	 * 
	 * @param player
	 * @param object
	 * @param liftBoulder
	 */
	public static void passGiantBoulder(Player player, WorldObject object, boolean liftBoulder) {
		if (player.getSkills().getLevel(liftBoulder ? Skills.STRENGTH : Skills.AGILITY) < 60) {
			player.getPackets().sendGameMessage("You need a " + (liftBoulder ? "Agility" : "Strength") + " of 60 in order to " + (liftBoulder ? "lift" : "squeeze past") + "this boulder.");
			return;
		}
		if (liftBoulder)
			World.sendObjectAnimation(object, new Animation(318));
		player.faceObject(object);
		boolean isReturning = player.getY() >= 3709;
		int baseAnimation = liftBoulder ? 3725 : 3466;
		player.setRunEnergy(0);
		player.useStairs(isReturning ? baseAnimation-- : baseAnimation, new Position(player.getX(), player.getY() + (isReturning ? -4 : 4), 0), liftBoulder ? 10 : 5, liftBoulder ? 11 : 6, null);
	}

	/**
	 * Checks if the player is wearing full cerimonial armor.
	 * @param player
	 * @return
	 */
	private static boolean hasFullCerimonial(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int bootsId = player.getEquipment().getBootsId();
		int glovesId = player.getEquipment().getGlovesId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || bootsId == -1 || glovesId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Ancient ceremonial") && ItemDefinitions.getItemDefinitions(chestId).getName().contains("Ancient ceremonial") && ItemDefinitions.getItemDefinitions(legsId).getName().contains("Ancient ceremonial") && ItemDefinitions.getItemDefinitions(bootsId).getName().contains("Ancient ceremonial") && ItemDefinitions.getItemDefinitions(glovesId).getName().contains("Ancient ceremonial");
	}
	
	private static int getRequiredKC(Player player) {
		int requiredKC;
		switch (player.getDonationManager().getRank()) {
		case ADAMANT:
			requiredKC = 10;
			break;
		case RUNE:
			requiredKC = 5;
			break;
		case DRAGON:
			requiredKC = 0;
			break;
		default:
			requiredKC = 40;
			break;
		}
		if (player.isOwner()) {
			requiredKC = 0;
		}
		return requiredKC;
	}

}
