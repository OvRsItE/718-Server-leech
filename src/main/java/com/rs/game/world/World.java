package com.rs.game.world;

import com.rs.Constants;
import com.rs.Launcher;
import com.rs.cores.CoresManager;
import com.rs.cores.FixedLengthRunnable;
import com.rs.cores.coroutines.CoroutineWorker;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.OwnedObjectManager;
import com.rs.game.map.Region;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.mysql.DatabaseManager;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.EntityList;
import com.rs.game.world.entity.npc.others.DarkDemon;
import com.rs.game.world.entity.npc.FlashMobs.PartyDemon;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.Polypore.FungalMages;
import com.rs.game.world.entity.npc.Polypore.InfestedAxes;
import com.rs.game.world.entity.npc.Polypore.Rodents;
import com.rs.game.world.entity.npc.Polypore.Runts;
import com.rs.game.world.entity.npc.acidwyrm.AcidicStrykewyrm;
import com.rs.game.world.entity.npc.combat.NPCCombat;
import com.rs.game.world.entity.npc.corp.CorporealBeast;
import com.rs.game.world.entity.npc.dagganoth.Spinolyp;
import com.rs.game.world.entity.npc.dragons.AdamantDragon;
import com.rs.game.world.entity.npc.dragons.KingBlackDragon;
import com.rs.game.world.entity.npc.dragons.RuneDragon;
import com.rs.game.world.entity.npc.dummy.impl.MagicCombatDummy;
import com.rs.game.world.entity.npc.dummy.impl.MeleeCombatDummy;
import com.rs.game.world.entity.npc.dummy.impl.RangedCombatDummy;
import com.rs.game.world.entity.npc.ents.EntsNPC;
import com.rs.game.world.entity.npc.eventboss.EventBoss;
import com.rs.game.world.entity.npc.eventboss.EventBoss.Zone;
import com.rs.game.world.entity.npc.glacor.Glacor;
import com.rs.game.world.entity.npc.godwars.GodWarMinion;
import com.rs.game.world.entity.npc.godwars.armadyl.GodwarsArmadylFaction;
import com.rs.game.world.entity.npc.godwars.armadyl.KreeArra;
import com.rs.game.world.entity.npc.godwars.bandos.GeneralGraardor;
import com.rs.game.world.entity.npc.godwars.bandos.GodwarsBandosFaction;
import com.rs.game.world.entity.npc.godwars.saradomin.CommanderZilyana;
import com.rs.game.world.entity.npc.godwars.saradomin.GodwarsSaradominFaction;
import com.rs.game.world.entity.npc.godwars.zammorak.GodwarsZammorakFaction;
import com.rs.game.world.entity.npc.godwars.zammorak.KrilTstsaroth;
import com.rs.game.world.entity.npc.godwars.zaros.Nex;
import com.rs.game.world.entity.npc.godwars.zaros.NexMinion;
import com.rs.game.world.entity.npc.kalph.KalphiteQueen;
import com.rs.game.world.entity.npc.karuulm.DrakeNPC;
import com.rs.game.world.entity.npc.kraken.KrakenNPC;
import com.rs.game.world.entity.npc.nomad.FlameVortex;
import com.rs.game.world.entity.npc.nomad.Nomad;
import com.rs.game.world.entity.npc.others.*;
import com.rs.game.world.entity.npc.randomspawns.RandomSpawns;
import com.rs.game.world.entity.npc.shaman.LizardmanShaman;
import com.rs.game.world.entity.npc.sire.AbyssalSire;
import com.rs.game.world.entity.npc.slayer.*;
import com.rs.game.world.entity.npc.thermo.ThermonuclearSmokeDevil;
import com.rs.game.world.entity.npc.wilderness.*;
import com.rs.game.world.entity.npc.zalcano.Zalcano;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.BoxAction.HunterNPC;
import com.rs.game.world.entity.player.actions.objects.EvilTree;
import com.rs.game.world.entity.player.content.*;
import com.rs.game.world.entity.player.content.activities.GodWarsBosses;
import com.rs.game.world.entity.player.content.activities.WarriorsGuild;
import com.rs.game.world.entity.player.content.activities.ZarosGodwars;
import com.rs.game.world.entity.player.content.activities.clanwars.FfaZone;
import com.rs.game.world.entity.player.content.activities.clanwars.RequestController;
import com.rs.game.world.entity.player.content.activities.dueling.DuelControler;
import com.rs.game.world.entity.player.content.botanybay.BotanyBay;
import com.rs.game.world.entity.player.content.grandexchange.GrandExchange;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonPartyManager;
import com.rs.game.world.entity.player.controller.impl.DungeonController;
import com.rs.game.world.entity.player.controller.impl.StartTutorial;
import com.rs.game.world.entity.player.controller.impl.Wilderness;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.network.security.AntiFlood;
import com.rs.utility.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
//import com.rs.game.player.content.GrandExchange.Offers;

public final class World extends CoroutineWorker {

	NPCCombat combat;
	public static int exiting_delay;
	public static long exiting_start;
	private static DatabaseManager database = new DatabaseManager();
	public static EventBoss eventBoss;

	public static DatabaseManager database() {
		return database;
	}

	private static final EntityList<Player> players = new EntityList<Player>(Constants.PLAYERS_LIMIT);

	private static final EntityList<NPC> npcs = new EntityList<NPC>(Constants.NPCS_LIMIT);
	private static final Map<Integer, Region> regions = Collections.synchronizedMap(new HashMap<Integer, Region>());
	protected static final Position ALKHARID = null;

	public static final boolean containsObjectWithId(Position tile, int id) {
		return getRegion(tile.getRegionId()).containsObjectWithId(tile.getZ(), tile.getXInRegion(),
				tile.getYInRegion(), id);
	}

	public static final WorldObject getObjectWithId(Position tile, int id) {
		return getRegion(tile.getRegionId()).getObjectWithId(tile.getZ(), tile.getXInRegion(), tile.getYInRegion(), id);
	}

	public static NPC getNPC(int npcId) {
		for (NPC npc : getNPCs()) {
			if (npc.getId() == npcId) {
				return npc;
			}
		}
		return null;
	}

	public static NPC getNpc(String name) {
		for (NPC npc : npcs) {
			if (npc.getName().equalsIgnoreCase(name)) {
				return npc;
			}
		}
		return null;
	}

	private static BotanyBay botanyBay;

	public static BotanyBay getBotanyBay() {
		return botanyBay;
	}

	private static final void addShootingStarMessageEvent() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				try {
					if (ShootingStars.locationName != null && !ShootingStars.locationName.isEmpty()) {
						World.sendWorldMessage("[<col=8B0000>Shooting Star</col>] - A Shooting star is currently in "
								+ Character.toUpperCase(ShootingStars.locationName.charAt(0))
								+ ShootingStars.locationName.substring(1), false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e) {
					e.printStackTrace();
				}
			}

		}, 0, 20, TimeUnit.MINUTES);
	}

	private static final void addEventBossTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				if (eventBoss == null || eventBoss.isDead() || eventBoss.isFinished()) {
					Zone zone = EventBoss.getRandomZone();
					eventBoss = new EventBoss(EventBoss.getRandomBoss(), zone);
					World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: An event boss has spawned near " + zone.getName() + "!", false);
				} else {
					World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: Theirs still an event boss near " + eventBoss.getZone().getName() + "!", false);
				}
			}

		}, 10, 60, TimeUnit.MINUTES);
	}

	private static void growPatchesTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player player : players) {
						if (player != null && player.getFarmings() != null && !player.isFinished()) {
							player.getFarmings().growAllPatches(player);
						}
					}
				} catch (Throwable e) {
				}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}

	public static final void addTileMessages() {
		CoresManager.slowExecutor.scheduleAtFixedRate(() -> {
			for (Player player : World.getPlayers()) {
				if (player.getRegionId() == 14377 && player.hasMessageHovers) {
//					player.getPackets().sendTileMessage("Shops / Slayer Masters", new Position(3614, 2645, 0), Color.white.getRGB());
//					player.getPackets().sendTileMessage("Teleportation", new Position(3628, 2655, 0), Color.white.getRGB());
//					player.getPackets().sendTileMessage("Skilling Zone", new Position(3631, 2673, 0), Color.white.getRGB());
//					player.getPackets().sendTileMessage("Spirit Tree / Fairy Rings", new Position(3625, 2662, 0), Color.white.getRGB());
//					player.getPackets().sendTileMessage("Spellbooks & Prayers", new Position(3622, 2655, 0), Color.white.getRGB());
				}
			}
		}, 0, 3, TimeUnit.SECONDS);
	}

	private static int calcuateAmount = 250000000;
	private static int wellAmount;
	private static boolean wellActive = false;
	public static int WellTimer = 0;

	public static int getWellAmount() {
		return wellAmount;
	}

	public static void addWellAmount(String displayName, int amount) {

		wellAmount += amount;
		sendWorldMessage(
				"[<col=8B0000>XP Well</col>] - <col=8B0000>" + displayName + "</col> has contributed <col=8B0000>"
						+ NumberFormat.getNumberInstance(Locale.US).format(amount) + "</col> GP to the XP well!",
				false);
		sendWorldMessage("[<col=8B0000>XP Well</col>] - There is now a total of [<col=8B0000>" + World.getWellAmount()
				+ "</col>] GP in the XP well!", true);
	}

	private static void setWellAmount(int amount) {
		wellAmount = amount;
	}

	public static void resetWell() {
		WellTimer = 0;
		wellAmount = 0;
		sendWorldMessage("[<col=8B0000>XP Well</col>] - The XP well has been reset!", false);
	}

	public static int getCalcAmount() {
		return calcuateAmount;
	}

	public static boolean isWellActive() {
		return wellActive;
	}

	public static void setWellActive(boolean wellActive) {
		World.wellActive = wellActive;
	}

	public static void loadWell() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("./data/well/data.txt"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] args = line.split(" ");
			if (args[0].contains("true")) {
				World.setWellActive(true);
				WellTimer = 1800000;
				XPWell.taskTime = Integer.parseInt(args[1]);
				XPWell.taskAmount = Integer.parseInt(args[1]);
				XPWell.setWellTask();
			} else {
				setWellAmount(Integer.parseInt(args[1]));
			}
		}
	}
	public static boolean dropRateTimer = false;

	public static void DropRateBoost(final Player player) {
		if (dropRateTimer) {
			player.sm("Droprate boost already active");
			return;
		}
		dropRateTimer = true;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				dropRateTimer = false;
				stop();

			}
		},6000);
		WorldTasksManager.schedule(new WorldTask(){

			@Override
			public void run() {
				if (dropRateTimer) {
					for (Player targets : World.getPlayers()) {
						if (!targets.getBuffManager().hasBuff(BuffManager.BuffType.DROPRATE_BUFF)) {

							if (targets == null) {
								continue;
							}
							targets.getBuffManager().applyBuff(new BuffManager.Buff(BuffManager.BuffType.DROPRATE_BUFF, 6000, true));
						}
					}

				} else if (dropRateTimer = false) {
					stop();
				}
			}
		}, 1, 1);

	}

	public static boolean xpBoostTimer = false;

	public static void xpBoost(final Player player) {
		if (xpBoostTimer) {
			player.sm("Xp boost boost already active");
			return;
		}
		xpBoostTimer = true;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				xpBoostTimer = false;
				stop();

			}
		},6000);
		WorldTasksManager.schedule(new WorldTask(){

			@Override
			public void run() {
				if (xpBoostTimer) {

					for (Player targets : World.getPlayers()) {
						if (!targets.getBuffManager().hasBuff(BuffManager.BuffType.XP_BOOST)) {
							if (targets == null) {
								continue;
							}
							targets.getBuffManager().applyBuff(new BuffManager.Buff(BuffManager.BuffType.XP_BOOST, 6000, true));
						}
					}

				} else if (xpBoostTimer = false) {
					stop();
				}
			}
		}, 1, 1);

	}

	public static void spinPlate(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (player.spinTimer > 0) {
						player.spinTimer--;
					}
					if (player.spinTimer == 1) {
						if (Misc.random(2) == 1) {
							player.animate(new Animation(1906));
							player.getInventory().deleteItem(4613, 1);
							addGroundItem(new Item(4613, 1),
									new Position(player.getX(), player.getY(), player.getZ()), player, false, 180,
									true);
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1000);
	}

	public static void addTime(final Player player) {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					player.onlinetime++;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1, TimeUnit.MINUTES);
	}

	/*
	 * public static void AgressiveTimer(final Player player) {
	 * CoresManager.fastExecutor.schedule(new TimerTask() {
	 *
	 * @Override public void run() { for (NPC npc : getNPCs()) { if
	 * (npc.getCombatDefinitions().getAgressivenessType() ==
	 * getCombatDefinitions().AGRESSIVE) }
	 *
	 *
	 * } }, 0, 60000); }
	 */

	public boolean isAtCrabs;

	public static final boolean isAtCrabs(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2692 && destX <= 2724 && destY >= 3709 && destY <= 3734 && tile.getZ() == 0; // Rock Crabs
																											// Zone

	}

	public static final boolean isAtSea(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2902 && destX <= 2910 && destY >= 3273 && destY <= 3302 && tile.getZ() == 0; // Rock Crabs
																											// Zone

	}

	public static void depletePendant() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player.getPendant().hasAmulet()) {
							player.pendantTime++;
						} else if (player.pendantTime == 55 && player.getPendant().hasAmulet()) {
							player.sm("You have 5 more minutes until your pendant depletes.");
						} else if (player.pendantTime == 55 && !player.getPendant().hasAmulet()) {
							return;
						} else if (player.pendantTime == 60 && !player.getPendant().hasAmulet()) {
							return;
						} else if (player.pendantTime == 60 && player.getPendant().hasAmulet()) {

							player.getEquipment().getItems().set(2, new Item(24712, 1));
							player.getEquipment().refresh(Equipment.SLOT_AMULET);
							player.sm("Your pendant has been depleted of its power.");
							player.pendantTime = 0;
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 0, 60000);
	}
	public static void startUnderwater(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (player.getEquipment().getHatId() == 7534 && player.getEquipment().getCapeId() == 7535 || player.getBuffManager().hasBuff(BuffManager.BuffType.UNDERWATERPOTION)) {
						//player.getPackets().sendGameMessage("Your equipment protects you from the the water.");
					} else {
							player.applyHit(new Hit(player, 120, HitLook.REGULAR_DAMAGE));
						//	player.getPackets().sendGameMessage("You are drowning.");
					}
					if (!player.isAtUnderwater()) {
						cancel();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 12000);
	}

	public static void startSmoke(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (player.getEquipment().getHatId() == 4164 || player.getEquipment().getHatId() == 13277
							|| player.getEquipment().getHatId() == 13263 || player.getEquipment().getHatId() == 14636
							|| player.getEquipment().getHatId() == 14637 || player.getEquipment().getHatId() == 15492
							|| player.getEquipment().getHatId() == 15496 || player.getEquipment().getHatId() == 15497
							|| player.getEquipment().getHatId() == 22528 || player.getEquipment().getHatId() == 22530
							|| player.getEquipment().getHatId() == 22532 || player.getEquipment().getHatId() == 22534
							|| player.getEquipment().getHatId() == 22536 || player.getEquipment().getHatId() == 22538
							|| player.getEquipment().getHatId() == 22540 || player.getEquipment().getHatId() == 22542
							|| player.getEquipment().getHatId() == 22544 || player.getEquipment().getHatId() == 22546
							|| player.getEquipment().getHatId() == 22548 || player.getEquipment().getHatId() == 22550) {
						player.getPackets().sendGameMessage("Your equipment protects you from the smoke.");
					} else {
						if (player.getHitpoints() > 120) {
							player.applyHit(new Hit(player, 120, HitLook.REGULAR_DAMAGE));
							player.getPackets().sendGameMessage("You take damage from the smoke.");
						}
					}
					if (!player.isAtSmokeyArea()) {
						cancel();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 12000);
	}

	public static boolean killedTree;
	public static boolean treeEvent;
	public static boolean eventStarted;

	public static String onGoingEvilTree() {
		if (eventStarted) {
			return "Alive";
		}
		return "Dead";
	}

	public static void startEvilTree() {
		WorldObject evilTree = new WorldObject(11922, 10, 0, 2608, 3121, 0);
		final WorldObject deadTree = new WorldObject(12715, 10, 0, 2456, 2835, 0);
		spawnObject(evilTree, true);
		EvilTree.health = 500;
		eventStarted = true;
		treeEvent = true;
		killedTree = false;
		sendWorldMessage("[col=8B0000>Evil Tree</col>] - An Evil Tree has appeared just north of home!", false);
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (EvilTree.health <= 0) {
						spawnTemporaryObject(deadTree, 600000, true);
						killedTree = true;
						sendWorldMessage("[col=8B0000>Evil Tree</col>] - The Evil Tree have been defeated!", false);
						executeTree();
						cancel();
						WorldTasksManager.schedule(new WorldTask() {
							int loop = 0;

							@Override
							public void run() {
								if (loop == 600) {
									treeEvent = false;
									killedTree = false;
									cancel();
								}
								loop++;
							}
						}, 0, 15000);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 3600000); // Once each hour.
	}

	public static void teleportNPC() {
		// boolean doingQuest = true;
		World.removeNPC(getNPC(259));
		World.spawnNPC(259, new Position(2594, 3143, 0), 1, 0, false);

	}

	public static void executeAfterLoadRegion(final int regionId, final Runnable event) {
		executeAfterLoadRegion(regionId, 0, event);
	}

	public static void executeAfterLoadRegion(final int regionId, long startTime, final Runnable event) {
		executeAfterLoadRegion(regionId, startTime, 10000, event);
	}

	public static void executeAfterLoadRegion(final int regionId, long startTime, final long expireTime,
											  final Runnable event) {
		final long start = Utils.currentTimeMillis();
		World.getRegion(regionId, true); // forces check load if not loaded
		CoresManager.scheduleRepeatedTask(new FixedLengthRunnable() {

			@Override
			public boolean repeat() {
				try {
					if (!World.isRegionLoaded(regionId) && Utils.currentTimeMillis() - start < expireTime)
						return true;
					event.run();
					return false;
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return false;
			}

		}, startTime, 600, TimeUnit.MILLISECONDS);
	}

	public static boolean isRegionLoaded(int regionId) {
		Region region = getRegion(regionId);
		if (region == null)
			return false;
		return region.getLoadMapStage() == 2;
	}

	public static void executeTree() {
		final int time = 60000;
		CoresManager.fastExecutor.schedule(new TimerTask() {
			int loop = 0;

			@Override
			public void run() {
				try {
					if (loop == time) {
						startEvilTree();
						cancel();
					}
					loop++;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1);
	}

	public static void startDesert(final Player player) {
		int mili = 90000;
		if (player.getEquipment().getHatId() == 6382
				&& (player.getEquipment().getChestId() == 6384 || player.getEquipment().getChestId() == 6388)
				&& (player.getEquipment().getLegsId() == 6390 || player.getEquipment().getLegsId() == 6386)
				|| player.getEquipment().getChestId() == 1833 && player.getEquipment().getLegsId() == 1835
						&& player.getEquipment().getBootsId() == 1837
				|| (player.getEquipment().getHatId() == 6392 || player.getEquipment().getHatId() == 6400)
						&& (player.getEquipment().getChestId() == 6394 || player.getEquipment().getChestId() == 6402)
						&& (player.getEquipment().getLegsId() == 6396 || player.getEquipment().getLegsId() == 6398
								|| player.getEquipment().getLegsId() == 6404
								|| player.getEquipment().getLegsId() == 6406)
				|| player.getEquipment().getChestId() == 1844 && player.getEquipment().getLegsId() == 1845
						&& player.getEquipment().getBootsId() == 1846) {
			mili = 120000;
		}
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (!player.isAtDesertArea()) {
						cancel();
					}
					for (int i = 0; i <= 28; i++) {
						evaporate(player);
					}
					if (player.isAtDesertArea()) {
						if (player.getInventory().containsItem(1823, 1)) {
							player.animate(new Animation(829));
							player.getInventory().deleteItem(1823, 1);
							player.getInventory().addItem(1825, 1);
							player.getPackets().sendGameMessage("You drink from the waterskin.");
						} else if (player.getInventory().containsItem(1825, 1)) {
							player.animate(new Animation(829));
							player.getInventory().deleteItem(1825, 1);
							player.getInventory().addItem(1827, 1);
							player.getPackets().sendGameMessage("You drink from the waterskin.");
						} else if (player.getInventory().containsItem(1827, 1)) {
							player.animate(new Animation(829));
							player.getInventory().deleteItem(1827, 1);
							player.getInventory().addItem(1829, 1);
							player.getPackets().sendGameMessage("You drink from the waterskin.");
						} else if (player.getInventory().containsItem(1829, 1)) {
							player.animate(new Animation(829));
							player.getInventory().deleteItem(1829, 1);
							player.getInventory().addItem(1831, 1);
							player.getPackets().sendGameMessage("You drink from the waterskin.");
						} else if (player.getInventory().containsItem(6794, 1)) {
							player.animate(new Animation(829));
							player.getInventory().deleteItem(6794, 1);
							player.getPackets().sendGameMessage("You eat one of your choc-ices.");
							player.heal(70);
						} else {
							int damage = Misc.random(100, 300);
							if (player.getEquipment().getShieldId() == 18346) {
								player.applyHit(new Hit(player, damage - 50, HitLook.REGULAR_DAMAGE));
							} else {
								player.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
							}
							player.getPackets().sendGameMessage("You take damage from the desert heat.");
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, mili);
	}

	public static void evaporate(final Player player) {
		if (player.getInventory().containsItem(227, 1)) {
			player.getInventory().deleteItem(227, 1);
			player.getInventory().addItem(229, 1);
		} else if (player.getInventory().containsItem(1921, 1)) {
			player.getInventory().deleteItem(1921, 1);
			player.getInventory().addItem(1923, 1);
		} else if (player.getInventory().containsItem(1929, 1)) {
			player.getInventory().deleteItem(1929, 1);
			player.getInventory().addItem(1925, 1);
		} else if (player.getInventory().containsItem(1937, 1)) {
			player.getInventory().deleteItem(1937, 1);
			player.getInventory().addItem(1935, 1);
		} else if (player.getInventory().containsItem(4458, 1)) {
			player.getInventory().deleteItem(4458, 1);
			player.getInventory().addItem(1980, 1);
		}

	}

	public static final void init() {
		addShootingStarMessageEvent();
		if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 24) {
			VoteRewards.chooseRandomVoter();
		}
		spawnStar();
		startEvilTree();
		ServerMessages();
		//RessourceBox.SpawnPackage();
		// depleteTokens();
		sinkHoles();
		growPatchesTask();
		//autoEvent();
		addTriviaBotTask();
		depletePendant();
		addRestoreRunEnergyTask();
		addDrainPrayerTask();
		addRestoreHitPointsTask();
		addRestoreSkillsTask();
		penguinHS();
		addRestoreSpecialAttackTask();
		addRestoreShopItemsTask();
		addSummoningEffectTask();
		addOwnedObjectsTask();
		LivingRockCavern.init();
		addListUpdateTask();
		WarriorsGuild.init();
		addTileMessages();
		addEventBossTask();
		for (int index = 0; index < 4; index++) {
			RandomSpawns.SpawnRev();
		}
	}

	public static List<Position> restrictedTiles = new ArrayList<Position>();

	public static void deleteObject(Position tile) {
		restrictedTiles.add(tile);
	}

	private static void addOwnedObjectsTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					OwnedObjectManager.processAll();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 600, TimeUnit.MILLISECONDS);
	}

	public static void ServerMessages() {
			CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {

				int message;

				@Override
				public void run() {
					if (message == 0) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] - Do not ask for staff, staff ranks are earned.",
								false);
					}
					if (message == 1) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] - The game is best experienced in resizeable mode.",
								false);
					}
					if (message == 2) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] - Support the server by doing ::vote!", false);
					}
					if (message == 3) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] - View the latest updates @ ::discord!",
								false);
					}
					if (message == 4) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] -  Only donate to corey or justice!",
								false);
					}
					if (message == 5) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] - Dont forget to vote every 12 hours.", false);
					}
					if (message == 6) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] - Found a bug? post it on the discord.", false);
					}
					if (message == 7) {
						sendWorldMessage(
								"<img=4>[<col=0966d9>Server Guide</col>] - Need help? use the friendschat 'help'.",
								false);
						message = 0;
					}
					message++;

				}

			}, 0, 5, TimeUnit.MINUTES);
		}


	public static int star = 0;

	private static final void addListUpdateTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()) {
							continue;
						}
						player.getPackets().sendIComponentText(751, 16,
								"Players Online: <col=00FF00>" + getPlayers().size());
						if (!(player.getControlerManager().getControler() instanceof DungeonController)
								&& player.isInDung()) {
							for (Item item : player.getInventory().getItems().toArray()) {
								if (item == null) {
									continue;
								}
								player.getInventory().deleteItem(item);
								player.setNextPosition(new Position(3450, 3718, 0));
							}
							for (Item item : player.getEquipment().getItems().toArray()) {
								if (item == null) {
									continue;
								}
								player.getEquipment().deleteItem(item.getId(), item.getAmount());
							}
							if (player.getFamiliar() != null) {
								if (player.getFamiliar().getBob() != null) {
									player.getFamiliar().getBob().getBeastItems().clear();
								}
								player.getFamiliar().dismissFamiliar(false);
							}
							player.setInDung(false);
						}
					}

				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 10);
	}

	// Automated Events
	public static boolean bandos;
	public static boolean armadyl;
	public static boolean zamorak;
	public static boolean saradomin;
	public static boolean dungeoneering;
	public static boolean cannonball;
	public static boolean doubleexp;
	public static boolean nex;
	public static boolean sunfreet;
	public static boolean corp;
	public static boolean doubledrops;
	public static boolean slayerpoints;
	public static boolean moreprayer;
	public static boolean quadcharms;

	public static void autoEvent() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				bandos = false;
				armadyl = false;
				zamorak = false;
				saradomin = false;
				dungeoneering = false;
				cannonball = false;
				doubleexp = false;
				nex = false;
				sunfreet = false;
				corp = false;
				doubledrops = false;
				slayerpoints = false;
				moreprayer = false;
				quadcharms = false;
				try {
					for (Player player : getPlayers()) {

						int event = Misc.random(225);
						if (event >= 1 && event <= 15) {
							bandos = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Godwars Event</col>] - Event at Bandos! No KC required!", false);
								return;
							}
						} else if (event >= 16 && event <= 30) {
							armadyl = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Godwars Event</col>] - Event at Armadyl! No KC required!", false);
								return;
							}
						} else if (event >= 31 && event <= 45) {
							zamorak = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Godwars Event</col>] - Event at Zamorak! No KC required!", false);
								return;
							}
						} else if (event >= 46 && event <= 60) {
							saradomin = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Godwars Event</col>] - Event at Saradomin! No KC required!",
										false);
								return;
							}
						} else if (event >= 61 && event <= 65) {
							dungeoneering = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Dung Event</col>] - Dungeoneering event! Come explore Dungeons with Double EXP and Tokens!",
										false);
								return;
							}
						} else if (event >= 81 && event <= 100) {
							nex = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Godwars Event</col>] -  Event at Nex! Get your torva now!",
										false);
							}
						} else if (event >= 101 && event <= 120) {
							sunfreet = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Sunfreet Event</col>] - Event at Sunfreet! Get your riches now!",
										false);
							}
						} else if (event >= 121 && event <= 145) {
							corp = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Corporeal Event</col>] - Event at Corp! Get your sigils now!",
										false);
							}
						} else if (event == 146) {
							doubledrops = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Drop Event</col>] - Double Drops are now on, take advantage and gain double the wealth!",
										false);
							}
						} else if (event >= 161 && event <= 180) {
							moreprayer = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage("[<col=e50000>Prayer Event</col>] - Double Prayer experience!",
										false);
							}
						} else if (event >= 181 && event <= 190) {
							quadcharms = true;
							if (player.hasToggledEventMsg()) {
								return;
							} else {
								World.sendWorldMessage(
										"[<col=e50000>Charms Event</col>] - Monsters are now dropping double the amounts of charms!",
										false);
							}
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 7000000);
	}

	public static void sinkHoles() {
		final int time = Misc.random(300000, 1500000);
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					SinkHoles.startEvent();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, time);
	}

	public static void penguinHS() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player players : World.getPlayers()) {
						if (players == null) {
							continue;
						}
						players.penguin = false;
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8104) {
							continue;
						}
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8105) {
							continue;
						}
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8107) {
							continue;
						}
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8108) {
							continue;
						}
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8109) {
							continue;
						}
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8110) {
							continue;
						}
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 14766) {
							continue;
						}
						n.sendDeath(n);
					}
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 14415) {
							continue;
						}
						n.sendDeath(n);
					}
					PenguinEvent.startEvent();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 5000000);
	}

	public static void crashedStar() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					star = 0;
					World.sendWorldMessage(
							"[<col=e50000>Shooting Star</col>] -  A Shooting Star has just struck Falador!", false);
					World.spawnObject(new WorldObject(38660, 10, 0, 3028, 3365, 0), true);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 2400000);
	}

	public static void spawnStar() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 1200) {
					star = 0;
					ShootingStar.spawnRandomStar();
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void removeStarSprite(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 50) {
					for (NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8091) {
							continue;
						}
						n.sendDeath(n);
					}
				}
				loop++;
			}
		}, 0, 1);
	}

	private static void addRestoreShopItemsTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					ShopsHandler.restoreShops();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 3, TimeUnit.SECONDS);
	}

	/**
	 * Lobby Stuff
	 */

	private static final EntityList<Player> lobbyPlayers = new EntityList<Player>(Constants.PLAYERS_LIMIT);

	public static final Player getLobbyPlayerByDisplayName(String username) {
		String formatedUsername = Utils.formatPlayerNameForDisplay(username);
		for (Player player : getLobbyPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equalsIgnoreCase(formatedUsername)
					|| player.getDisplayName().equalsIgnoreCase(formatedUsername)) {
				return player;
			}
		}
		return null;
	}

	public static final EntityList<Player> getLobbyPlayers() {
		return lobbyPlayers;
	}

	public static final void addPlayer(Player player) {
		players.add(player);
		if (World.containsLobbyPlayer(player.getUsername())) {
			World.removeLobbyPlayer(player);
			AntiFlood.remove(player.getSession().getIP());
		}
		AntiFlood.add(player.getSession().getIP());
	}

	public static final void addLobbyPlayer(Player player) {
		lobbyPlayers.add(player);
		AntiFlood.add(player.getSession().getIP());
	}

	public static final boolean containsLobbyPlayer(String username) {
		for (Player p2 : lobbyPlayers) {
			if (p2 == null) {
				continue;
			}
			if (p2.getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	public static void removeLobbyPlayer(Player player) {
		for (Player p : lobbyPlayers) {
			if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
				if (player.getCurrentFriendChat() != null) {
					player.getCurrentFriendChat().leaveChat(player, true);
				}
				lobbyPlayers.remove(p);
			}
		}
		AntiFlood.remove(player.getSession().getIP());
	}

	public static void removePlayer(Player player) {
		for (Player p : players) {
			if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
				players.remove(p);
			}
		}
		AntiFlood.remove(player.getSession().getIP());
	}

	private static final void addSummoningEffectTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.getFamiliar() == null || player.isDead() || !player.isFinished()) {
							continue;
						}
						if (player.getFamiliar().getOriginalId() == 6814) {
							player.heal(20);
							player.setNextGraphics(new Graphics(1507));
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 15, TimeUnit.SECONDS);
	}

	private static final void addRestoreSpecialAttackTask() {

		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()) {
							continue;
						}
						player.getCombatDefinitions().restoreSpecialAttack();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30000);
	}

	private static boolean checkAgility;
	public static Object deleteObject;
	public static Position lucienSpot = new Position(3035, 3681, 0);
	public static boolean spawnedPackage;

	public static String getWellTimer(final Player player) {
		long HR = XPWell.TimeLeft / 3600;
		long MIN = XPWell.TimeLeft / 60 - HR * 60;
		long SEC = XPWell.TimeLeft - HR * 3600 - MIN * 60;
		return "Hr: " + HR + " Min: " + MIN + " Sec: " + SEC;
	}

	public static String getTriviaTimer(final Player player) {
		long MIN = player.getTriviaTime() / 60;
		long SEC = player.getTriviaTime() - MIN * 60;
		return "Min: " + MIN + " Sec: " + SEC;
	}

	private static final void addTriviaBotTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					TriviaBot.Run();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 420000);
	}

	private static final void addRestoreRunEnergyTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()
								|| checkAgility && player.getSkills().getLevel(Skills.AGILITY) < 70) {
							continue;
						}
						player.restoreRunEnergy();
					}
					checkAgility = !checkAgility;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 900);
	}

	private static final void addDrainPrayerTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()) {
							continue;
						}
						player.getPrayer().processPrayerDrain();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 600);
	}

	private static final void addRestoreHitPointsTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || player.isDead() || !player.isRunning()) {
							continue;
						}
						player.restoreHitPoints();
					}
					for (NPC npc : npcs) {
						if (npc == null || npc.isDead() || npc.isFinished()) {
							continue;
						}
						npc.restoreHitPoints();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 60000);
	}

	private static final void addRestoreSkillsTask() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					for (Player player : getPlayers()) {
						if (player == null || !player.isRunning()) {
							continue;
						}
						int ammountTimes = player.getPrayer().usingPrayer(0, 8) ? 2 : 1;
						if (player.isResting()) {
							ammountTimes += 1;
						}
						boolean berserker = player.getPrayer().usingPrayer(1, 5);
						for (int skill = 0; skill < 25; skill++) {
							if (skill == Skills.SUMMONING) {
								continue;
							}
							for (int time = 0; time < ammountTimes; time++) {
								int currentLevel = player.getSkills().getLevel(skill);
								int normalLevel = player.getSkills().getLevelForXp(skill);
								if (currentLevel > normalLevel) {
									if (skill == Skills.ATTACK || skill == Skills.STRENGTH || skill == Skills.DEFENCE
											|| skill == Skills.RANGE || skill == Skills.MAGIC) {
										if (berserker && Utils.getRandom(100) <= 15) {
											continue;
										}
									}
									player.getSkills().set(skill, currentLevel - 1);
								} else if (currentLevel < normalLevel) {
									player.getSkills().set(skill, currentLevel + 1);
								} else {
									break;
								}
							}
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 30000);

	}

	public static final Map<Integer, Region> getRegions() {
		// synchronized (lock) {
		return regions;
		// }
	}

	public static final Region getRegion(int id) {
		return getRegion(id, false);
	}

	public static final Region getRegion(int id, boolean load) {
		// synchronized (lock) {
		Region region = regions.get(id);
		if (region == null) {
			region = new Region(id);
			regions.put(id, region);
		}
		if (load) {
			region.checkLoadMap();
		}
		return region;
		// }
	}

	public static final void addNPC(NPC npc) {
		npcs.add(npc);
	}

	public static final void removeNPC(NPC npc) {
		npcs.remove(npc);
	}

	public static final NPC spawnNPC(int id, Position tile, int mapAreaNameHash, int faceDirection,
									 boolean canBeAttackFromOutOfArea, boolean spawned) {
		NPC n = null;
		HunterNPC hunterNPCs = HunterNPC.forId(id);
		if (hunterNPCs != null) {
			if (id == hunterNPCs.getNpcId()) {
				n = new ItemHunterNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
			}
		} else if (id >= 5533 && id <= 5558) {
			n = new Elemental(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14301) {
			n = new Glacor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 7134) {
			n = new Bork(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id >= 6026 && id <= 6045) {
			n = new Werewolf(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 9441) {
			n = new FlameVortex(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id >= 8832 && id <= 8834) {
			n = new LivingRock(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id >= 13465 && id <= 13481 || (id >= 16106 && id <= 16111)) {
			n = new Revenant(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 1158 || id == 1160) {
			n = new KalphiteQueen(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id >= 8528 && id <= 8532) {
			n = new Nomad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6215 || id == 6211 || id == 3406 || id == 6216 || id == 6214 || id == 6215 || id == 6212
				|| id == 6219 || id == 6221 || id == 6218) {
			n = new GodwarsZammorakFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6254 && id == 6259) {
			n = new GodwarsSaradominFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6246 || id == 6236 || id == 6232 || id == 6240 || id == 6241 || id == 6242 || id == 6235
				|| id == 6234 || id == 6243 || id == 6236 || id == 6244 || id == 6237 || id == 6246 || id == 6238
				|| id == 6239 || id == 6230) {
			n = new GodwarsArmadylFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6281 || id == 6282 || id == 6275 || id == 6279 || id == 9184 || id == 6268 || id == 6270
				|| id == 6274 || id == 6277 || id == 6276 || id == 6278) {
			n = new GodwarsBandosFaction(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6261 || id == 6263 || id == 6265) {
			n = GodWarsBosses.graardorMinions[(id - 6261) / 2] = new GodWarMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6260) {
			n = new GeneralGraardor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6222) {
			n = new KreeArra(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6223 || id == 6225 || id == 6227) {
			n = GodWarsBosses.armadylMinions[(id - 6223) / 2] = new GodWarMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6203) {
			n = new KrilTstsaroth(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6204 || id == 6206 || id == 6208) {
			n = GodWarsBosses.zamorakMinions[(id - 6204) / 2] = new GodWarMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 50 || id == 2642) {
			n = new KingBlackDragon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id >= 9462 && id <= 9468 || id == 16152) {
			n = new Strykewyrm(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 16027 || id == 16010) {
			n = new KrakenNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 9379) {
			n = new Imps(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 2892) {
			n = new Spinolyp(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 1268 || id == 1266) {
			n = new RockCrab(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 25936) {
			n = new SandCrab(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14696) {
			n = new GanodermicBeast(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14692) {
			n = new Rodents(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14690) {
			n = new FungalMages(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14694) {
			n = new InfestedAxes(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14698) {
			n = new Runts(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14688) {
			n = new Runts(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 1610) {
			n = new Gargoyle(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 1609) {
			n = new Kurask(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 1627) {
			n = new Turoth(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 3847) {
			n = new SeaTrollQueen(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 6248 || id == 6250 || id == 6252) {
			n = GodWarsBosses.commanderMinions[(id - 6248) / 2] = new GodWarMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6247) {
			n = new CommanderZilyana(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 8133) {
			n = new CorporealBeast(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 13447) {
			n = ZarosGodwars.nex = new Nex(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 13451) {
			n = ZarosGodwars.fumus = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 13452) {
			n = ZarosGodwars.umbra = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 13453) {
			n = ZarosGodwars.cruor = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 13454) {
			n = ZarosGodwars.glacies = new NexMinion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 14256) {
			n = new Lucien(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 8335) {
			n = new MercenaryMage(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 8349 || id == 8450 || id == 8451) {
			n = new TormentedDemon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 15149) {
			n = new MasterOfFear(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 26504) {
			n = new Venenatis(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 16008) {
			n = new CursedVenenatis(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 16136) {
			n = new Bruce(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 16112) {
			n = new KingLavaDragon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 26611 || id == 26612) {
			n = new Vetion(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 17000)
			n = new MeleeCombatDummy(tile);
		else if (id == 17001)
			n = new RangedCombatDummy(tile);
		else if (id == 17002) {
			n = new MagicCombatDummy(tile);
		} else if (id == 16023 || id == 16024 || id == 16025) {
			n = new AcidicStrykewyrm(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 16015) {
			n = new ThermonuclearSmokeDevil(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 16009) {
			n = new Scorpia(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 16068) {
			n = new AdamantDragon(tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 16069) {
			n = new RuneDragon(tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 26766) {
			n = new LizardmanShaman(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 27147 || id == 27148 || id == 27149) {
			n = new DemonicGorilla(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 15881) {
			n = new PartyDemon(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == 26618) {
			n = new CrazyArchaeologist(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		} else if (id == 26619) {
			n = new ChaosFanatic(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
		} else if (id == 27234 || id == 26594) {
			n = new EntsNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		} else if (id == NPC.asOSRS(5886) || id == NPC.asOSRS(5887) || id == NPC.asOSRS(5888) || id == NPC.asOSRS(5889) || id == NPC.asOSRS(5890) || id == NPC.asOSRS(5891) || id == NPC.asOSRS(5908)) {
			n = new AbyssalSire(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
		} else if (id == NPC.asOSRS(9049) || id == NPC.asOSRS(9050)) {
			n = new Zalcano(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
		} else if (id == NPC.asOSRS(8612) || id == NPC.asOSRS(8613)) {
			n = new DrakeNPC(id, tile ,mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
		} else if (id == 16089) {
			n = new DarkDemon(id, tile ,mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
		} else {
			n = new NPC(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
		} 
		return n;
	}

	public static final NPC spawnNPC(int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea) {
		return spawnNPC(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, false);
	}

	/*
	 * check if the entity region changed because moved or teled then we update it
	 */
	public static final void updateEntityRegion(Entity entity) {
		if (entity.isFinished()) {
			if (entity instanceof Player) {
				getRegion(entity.getLastRegionId()).removePlayerIndex(entity.getIndex());
			} else {
				getRegion(entity.getLastRegionId()).removeNPCIndex(entity.getIndex());
			}
			return;
		}
		int regionId = entity.getRegionId();
		if (entity.getLastRegionId() != regionId) { // map region entity at
			// changed
			if (entity instanceof Player) {
				if (entity.getLastRegionId() > 0) {
					getRegion(entity.getLastRegionId()).removePlayerIndex(entity.getIndex());
				}
				Region region = getRegion(regionId);
				region.addPlayerIndex(entity.getIndex());
				Player player = (Player) entity;
				int musicId = region.getMusicId();
				if (musicId != -1) {
					player.getMusicsManager().checkMusic(musicId);
				}
				player.getControlerManager().moved();
				if (player.hasStarted()) {
					checkControlersAtMove(player);
				}
			} else {
				if (entity.getLastRegionId() > 0) {
					getRegion(entity.getLastRegionId()).removeNPCIndex(entity.getIndex());
				}
				getRegion(regionId).addNPCIndex(entity.getIndex());
			}
			entity.checkMultiArea();
			entity.checkSmokeyArea();
			entity.checkUnderwaterArea();
			entity.checkDesertArea();
			entity.checkSinkArea();
			entity.checkMorytaniaArea();
			entity.setLastRegionId(regionId);
		} else {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				player.getControlerManager().moved();
				if (player.hasStarted()) {
					checkControlersAtMove(player);
				}
			}
			entity.checkMultiArea();
			entity.checkSmokeyArea();
			entity.checkUnderwaterArea();
			entity.checkDesertArea();
			entity.checkSinkArea();
			entity.checkMorytaniaArea();
		}
	}

	private static void checkControlersAtMove(Player player) {
		if (!(player.getControlerManager().getControler() instanceof RequestController)
				&& RequestController.inWarRequest(player)) {
			if (player.isPublicWildEnabled()) {
				player.switchPvpModes();
			}
			player.getControlerManager().startControler("clan_wars_request");

		} else if (DuelControler.isAtDuelArena(player)) {
			if (player.getControlerManager().getControler() instanceof StartTutorial
					|| player.getControlerManager().getControler() instanceof Wilderness) {
				return;
			}
			player.getControlerManager().startControler("DuelControler");
		} else if (FfaZone.inArea(player)) {
			if (player.isPublicWildEnabled()) {
				player.switchPvpModes();
			}
			player.getControlerManager().startControler("clan_wars_ffa");
		} else if (player.isPublicWildEnabled()
				&& player.getControlerManager().getControler() instanceof Wilderness == false) {
			player.getControlerManager().startControler("Wilderness");
			player.setCanPvp(true);
		}
	}

	/*
	 * checks clip
	 */
	public static boolean canMoveNPC(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (getMask(plane, tileX, tileY) != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * checks clip
	 */
	public static boolean isNotCliped(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if ((getMask(plane, tileX, tileY) & 2097152) != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public static int getMask(int plane, int x, int y) {
		Position tile = new Position(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return -1;
		}
		int baseLocalX = x - (regionId >> 8) * 64;
		int baseLocalY = y - (regionId & 0xff) * 64;
		return region.getMask(tile.getZ(), baseLocalX, baseLocalY);
	}

	public static void setMask(int plane, int x, int y, int mask) {
		Position tile = new Position(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return;
		}
		int baseLocalX = x - (regionId >> 8) * 64;
		int baseLocalY = y - (regionId & 0xff) * 64;
		region.setMask(tile.getZ(), baseLocalX, baseLocalY, mask);
	}

	public static int getRotation(int plane, int x, int y) {
		Position tile = new Position(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return 0;
		}
		// int baseLocalX = x - ((regionId >> 8) * 64);
		// int baseLocalY = y - ((regionId & 0xff) * 64);
		// return region.getRotation(tile.getPlane(), baseLocalX, baseLocalY);
		return 0;
	}

	private static int getClipedOnlyMask(int plane, int x, int y) {
		Position tile = new Position(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return -1;
		}
		int baseLocalX = x - (regionId >> 8) * 64;
		int baseLocalY = y - (regionId & 0xff) * 64;
		return region.getMaskClipedOnly(tile.getZ(), baseLocalX, baseLocalY);
	}

	public static final boolean checkProjectileStep(int plane, int x, int y, int dir, int size) {
		int xOffset = Utils.DIRECTION_DELTA_X[dir];
		int yOffset = Utils.DIRECTION_DELTA_Y[dir];
		/*
		 * int rotation = getRotation(plane,x+xOffset,y+yOffset); if(rotation != 0) {
		 * dir += rotation; if(dir >= Utils.DIRECTION_DELTA_X.length) dir = dir -
		 * (Utils.DIRECTION_DELTA_X.length-1); xOffset = Utils.DIRECTION_DELTA_X[dir];
		 * yOffset = Utils.DIRECTION_DELTA_Y[dir]; }
		 */
		if (size == 1) {
			int mask = getClipedOnlyMask(plane, x + Utils.DIRECTION_DELTA_X[dir], y + Utils.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & 0x42240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & 0x60240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & 0x40a40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & 0x48240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0 && (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0 && (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0 && (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0) {
				return (getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (getClipedOnlyMask(plane, x + 2, y) & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (getClipedOnlyMask(plane, x, y + 2) & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (getClipedOnlyMask(plane, x - 1, y) & 0x4fa40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x63e40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (getClipedOnlyMask(plane, x + 1, y - 1) & 0x63e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y - 1) & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y) & 0x78e40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 2) & 0x7e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (getClipedOnlyMask(plane, x + 1, y + 2) & 0x7e240000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y + 2) & 0x78240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
			}
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x - 1, -1 + y + size) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getClipedOnlyMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getClipedOnlyMask(plane, x + size - 1, y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x - 1, y + -1 + sizeOffset) & 0x4fa40000) != 0
							|| (getClipedOnlyMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x + size, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + size, sizeOffset + -1 + y) & 0x78e40000) != 0
							|| (getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x - 1, y + size) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
							|| (getClipedOnlyMask(plane, -1 + x + sizeOffset, y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x + size, y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
							|| (getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static final boolean checkWalkStep(int plane, int x, int y, int dir, int size) {
		int xOffset = Utils.DIRECTION_DELTA_X[dir];
		int yOffset = Utils.DIRECTION_DELTA_Y[dir];
		int rotation = getRotation(plane, x + xOffset, y + yOffset);
		if (rotation != 0) {
			for (int rotate = 0; rotate < 4 - rotation; rotate++) {
				int fakeChunckX = xOffset;
				int fakeChunckY = yOffset;
				xOffset = fakeChunckY;
				yOffset = 0 - fakeChunckX;
			}
		}

		if (size == 1) {
			int mask = getMask(plane, x + Utils.DIRECTION_DELTA_X[dir], y + Utils.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & 0x42240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & 0x60240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & 0x40a40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & 0x48240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0
						&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0
						&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0) {
				return (getMask(plane, x - 1, y) & 0x43a40000) == 0 && (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (getMask(plane, x + 2, y) & 0x60e40000) == 0 && (getMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (getMask(plane, x, y - 1) & 0x43a40000) == 0 && (getMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (getMask(plane, x, y + 2) & 0x4e240000) == 0 && (getMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (getMask(plane, x - 1, y) & 0x4fa40000) == 0 && (getMask(plane, x - 1, y - 1) & 0x43a40000) == 0
						&& (getMask(plane, x, y - 1) & 0x63e40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (getMask(plane, x + 1, y - 1) & 0x63e40000) == 0
						&& (getMask(plane, x + 2, y - 1) & 0x60e40000) == 0
						&& (getMask(plane, x + 2, y) & 0x78e40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (getMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
						&& (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0
						&& (getMask(plane, x, y + 2) & 0x7e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (getMask(plane, x + 1, y + 2) & 0x7e240000) == 0
						&& (getMask(plane, x + 2, y + 2) & 0x78240000) == 0
						&& (getMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
			}
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getMask(plane, x - 1, -1 + y + size) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getMask(plane, x + size - 1, y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getMask(plane, x - 1, y - 1) & 0x43a40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x - 1, y + -1 + sizeOffset) & 0x4fa40000) != 0
							|| (getMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getMask(plane, x + size, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x + size, sizeOffset + -1 + y) & 0x78e40000) != 0
							|| (getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getMask(plane, x - 1, y + size) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
							|| (getMask(plane, -1 + x + sizeOffset, y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getMask(plane, x + size, y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
							|| (getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static final boolean containsPlayer(String username) {
		for (Player p2 : players) {
			if (p2 == null) {
				continue;
			}
			if (p2.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	public static Player getstaffOn(String username) {
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(username) && player.getRights() > 0) {
				return player;
			}
		}
		return null;
	}

	public static Player getPlayer(String username) {
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}

	public static final Player getPlayerByDisplayName(String username) {
		String formatedUsername = Utils.formatPlayerNameForDisplay(username);
		for (Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equalsIgnoreCase(formatedUsername)
					|| player.getDisplayName().equalsIgnoreCase(formatedUsername)) {
				return player;
			}
		}
		return null;
	}

	public static final EntityList<Player> getPlayers() {
		return players;
	}

	public static final EntityList<NPC> getNPCs() {
		return npcs;
	}

	private World() {

	}

	public static final void safeShutdown(final boolean restart, int delay) {
		if (exiting_start != 0) {
			return;
		}
		exiting_start = Utils.currentTimeMillis();
		exiting_delay = delay;
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.isFinished()) {
				continue;
			}
			player.getPackets().sendSystemUpdate(delay);
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player player : World.getPlayers()) {
						if (player == null || !player.hasStarted()) {
							continue;
						}
						player.realFinish();

						player.getControlerManager().removeControlerWithoutCheck();
						if (player.getControlerManager().getControler() instanceof DungeonController) {
							player.getControlerManager().forceStop();
							player.getControlerManager().removeControlerWithoutCheck();
							player.lock(10);
							for (Item item : player.getInventory().getItems().toArray()) {
								if (item == null) {
									continue;
								}
								player.getInventory().deleteItem(item);
								player.setNextPosition(new Position(3450, 3718, 0));
							}
						}
						GrandExchange.save();
						IPBanL.save();
						IPMute.save();
						PkRank.save();
						KillStreakRank.save();
					}
					// Offers.saveOffers();
					if (restart) {
						Launcher.restart();
					} else {
						Launcher.shutdown();
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, delay, TimeUnit.SECONDS);
	}

	/*
	 * by default doesnt changeClipData
	 */
	public static final void spawnTemporaryObject(final WorldObject object, long time) {
		spawnTemporaryObject(object, time, false);
	}

	public static final void spawnTemporaryObject(final WorldObject object, long time, final boolean clip) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (!World.isSpawnedObject(object)) {
						return;
					}
					removeObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static final boolean isSpawnedObject(WorldObject object) {
		return getRegion(object.getRegionId()).getSpawnedObjects().contains(object);
	}

	public static final boolean removeTemporaryObject(final WorldObject object, long time, final boolean clip) {
		removeObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawnObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
		return true;
	}
	
	public static final boolean temporarilyReplaceObject(final WorldObject object, final WorldObject replace, long time, final boolean clip) {
		spawnObject(replace);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawnObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
		return true;
	}

	public static final void removeObject(WorldObject object, boolean clip) {
		getRegion(object.getRegionId()).removeObject(object, object.getZ(), object.getXInRegion(),
				object.getYInRegion());
	}

	public static final WorldObject getObject(Position tile) {
		return getRegion(tile.getRegionId()).getStandartObject(tile.getZ(), tile.getXInRegion(),
				tile.getYInRegion());
	}

	public static final WorldObject getObject(Position tile, int type) {
		return getRegion(tile.getRegionId()).getObjectWithType(tile.getZ(), tile.getXInRegion(),
				tile.getYInRegion(), type);
	}

	public static final void spawnObject(WorldObject object, boolean clip) {
		getRegion(object.getRegionId()).spawnObject(object, object.getZ(), object.getXInRegion(), object.getYInRegion(), false);
	}

	public static final void spawnObjectSpawns(WorldObject object, boolean clip) {
		getRegion(object.getRegionId()).spawnObject(object, object.getZ(), object.getXInRegion(), object.getYInRegion(), true);
	}

	public static final void addGroundItem(final Item item, final Position tile) {
		final FloorItem floorItem = new FloorItem(item, tile, null, false, false);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.isFinished() || player.getZ() != tile.getZ()
					|| !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
	}

	public static final void addGroundItem(final Item item, final Position tile, final Player owner,
										   final boolean underGrave, long hiddenTime, boolean invisible) {
		addGroundItem(item, tile, owner, underGrave, hiddenTime, invisible, false, 180);
	}

	public static final void addGroundItem(final Item item, final Position tile, final Player owner,
										   final boolean underGrave, long hiddenTime, boolean invisible, boolean intoGold) {
		addGroundItem(item, tile, owner, underGrave, hiddenTime, invisible, intoGold, 180);
	}

	public static final void addGroundItem(final Item item, final Position tile,
			final Player owner/* null for default */, final boolean underGrave, long hiddenTime/* default 3minutes */,
			boolean invisible, boolean intoGold, final int publicTime) {
		final FloorItem floorItem = new FloorItem(item, tile, owner, owner == null ? false : underGrave, invisible);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		if (invisible && hiddenTime != -1) {
			if (owner != null) {
				owner.getPackets().sendGroundItem(floorItem);
			}
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						if (!region.forceGetFloorItems().contains(floorItem)) {
							return;
						}
						int regionId = tile.getRegionId();
						if (underGrave || !ItemConstants.isTradeable(floorItem)) {
							region.forceGetFloorItems().remove(floorItem);
							if (owner != null) {
								if (owner.getMapRegionsIds().contains(regionId)
										&& owner.getZ() == tile.getZ()) {
									owner.getPackets().sendRemoveGroundItem(floorItem);
									owner.getPackets().sendGraphics(new Graphics(-1), floorItem.getTile());
								}
							}
							return;
						}
						floorItem.setInvisible(false);
						for (Player player : players) {
							if (player == null || player == owner || !player.hasStarted() || player.isFinished()
									|| player.getZ() != tile.getZ()
									|| !player.getMapRegionsIds().contains(regionId)) {
								continue;
							}
							player.getPackets().sendGroundItem(floorItem);
						}
						removeGroundItem(floorItem, publicTime);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, hiddenTime, TimeUnit.SECONDS);
			return;
		}
		int regionId = tile.getRegionId();
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.isFinished() || player.getZ() != tile.getZ()
					|| !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
		removeGroundItem(floorItem, publicTime);
	}

	@Deprecated
	public static final void addGroundItemForever(Item item, final Position tile) {
		int regionId = tile.getRegionId();
		final FloorItem floorItem = new FloorItem(item, tile, true);
		final Region region = getRegion(tile.getRegionId());
		region.getGroundItemsSafe().add(floorItem);
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.isFinished()
					|| player.getZ() != floorItem.getTile().getZ()
					|| !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
	}

	public static final void updateGroundItem(Item item, final Position tile, final Player owner) {
		final FloorItem floorItem = World.getRegion(tile.getRegionId()).getGroundItem(item.getId(), tile, owner);
		if (floorItem == null) {
			addGroundItem(item, tile, owner, true, 360);
			return;
		}
		floorItem.setAmount(floorItem.getAmount() + item.getAmount());
		owner.getPackets().sendRemoveGroundItem(floorItem);
		owner.getPackets().sendGroundItem(floorItem);
	}

	private static final void removeGroundItem(final FloorItem floorItem, long publicTime) {
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					int regionId = floorItem.getTile().getRegionId();
					Region region = getRegion(regionId);
					if (!region.getGroundItemsSafe().contains(floorItem)) {
						return;
					}
					region.getGroundItemsSafe().remove(floorItem);
					for (Player player : World.getPlayers()) {
						if (player == null || !player.hasStarted() || player.isFinished()
								|| player.getZ() != floorItem.getTile().getZ()
								|| !player.getMapRegionsIds().contains(regionId)) {
							continue;
						}
						player.getPackets().sendRemoveGroundItem(floorItem);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, publicTime, TimeUnit.SECONDS);
	}

	public static final boolean removeGroundItem(Player player, FloorItem floorItem) {
		return removeGroundItem(player, floorItem, true);
	}

	public static final boolean removeGroundItem(Player player, final FloorItem floorItem, boolean add) {
		int regionId = floorItem.getTile().getRegionId();
		Region region = getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(floorItem)) {
			return false;
		}
		if (player.getInventory().getFreeSlots() == 0 && (!floorItem.getDefinitions().isStackable()
				|| !player.getInventory().containsItem(floorItem.getId(), 1))) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		region.getGroundItemsSafe().remove(floorItem);
		if (add) {
			player.getInventory().addItemMoneyPouch(new Item(floorItem.getId(), floorItem.getAmount()));
		}
		player.getPackets().sendGraphics(new Graphics(-1), floorItem.getTile());
		if (floorItem.isInvisible()) {
			player.getPackets().sendRemoveGroundItem(floorItem);
			return true;
		} else {
			for (Player p2 : World.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.isFinished() || p2.getZ() != floorItem.getTile().getZ()
						|| !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendRemoveGroundItem(floorItem);
			}
			if (floorItem.isForever()) {
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							addGroundItemForever(floorItem, floorItem.getTile());
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}
				}, 60, TimeUnit.SECONDS);
			}
			return true;
		}
	}

	public static final void sendObjectAnimation(WorldObject object, Animation animation) {
		sendObjectAnimation(null, object, animation);
	}

	public static final void sendObjectAnimation(Entity creator, WorldObject object, Animation animation) {
		if (creator == null) {
			for (Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(object)) {
					continue;
				}
				player.getPackets().sendObjectAnimation(object, animation);
			}
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
				if (playersIndexes == null) {
					continue;
				}
				for (Integer playerIndex : playersIndexes) {
					Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted() || player.isFinished()
							|| !player.withinDistance(object)) {
						continue;
					}
					player.getPackets().sendObjectAnimation(object, animation);
				}
			}
		}
	}

	public static final void sendGraphics(Entity creator, Graphics graphics, Position tile) {
		if (creator == null) {
			if (graphics.getDelay() > 0) {
				World.startEvent(event -> {
					event.delay(graphics.getDelay());
					for (Player player : World.getPlayers()) {
						if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(tile)) {
							continue;
						}
						player.getPackets().sendGraphics(graphics, tile);
					}
				});
			} else {
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(tile)) {
						continue;
					}
					player.getPackets().sendGraphics(graphics, tile);
				}
			}
		} else {
			if (graphics.getDelay() > 0) {
				World.startEvent(event -> {
					event.delay(graphics.getDelay());
					for (int regionId : creator.getMapRegionsIds()) {
						List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
						if (playersIndexes == null) {
							continue;
						}
						for (Integer playerIndex : playersIndexes) {
							Player player = players.get(playerIndex);
							if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(tile)) {
								continue;
							}
							player.getPackets().sendGraphics(graphics, tile);
						}
					}
				});
			} else {
				for (int regionId : creator.getMapRegionsIds()) {
					List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
					if (playersIndexes == null) {
						continue;
					}
					for (Integer playerIndex : playersIndexes) {
						Player player = players.get(playerIndex);
						if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(tile)) {
							continue;
						}
						player.getPackets().sendGraphics(graphics, tile);
					}
				}
			}
		}
	}

	public static final void sendProjectile(Entity shooter, Position startTile, Position receiver, int gfxId,
											int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.hasStarted() || player.isFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				player.getPackets().sendProjectile(null, startTile, receiver, gfxId, startHeight, endHeight, speed,
						delay, curve, startDistanceOffset, 1);
			}
		}
	}

	public static final void sendProjectile(Position shooter, Entity receiver, int gfxId, int startHeight,
											int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : receiver.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.hasStarted() || player.isFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				player.getPackets().sendProjectile(null, shooter, receiver, gfxId, startHeight, endHeight, speed, delay,
						curve, startDistanceOffset, 1);
			}
		}
	}

	public static final void sendProjectile(Entity shooter, Position receiver, int gfxId, int startHeight,
											int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.hasStarted() || player.isFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				player.getPackets().sendProjectile(null, shooter, receiver, gfxId, startHeight, endHeight, speed, delay,
						curve, startDistanceOffset, shooter.getSize());
			}
		}
	}

	public static final void sendProjectile(Entity shooter, Entity receiver, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.hasStarted() || player.isFinished()
						|| !player.withinDistance(shooter) && !player.withinDistance(receiver)) {
					continue;
				}
				int size = shooter.getSize();
				player.getPackets().sendProjectile(receiver,
						new Position(shooter.getCoordFaceX(size), shooter.getCoordFaceY(size), shooter.getZ()),
						receiver, gfxId, startHeight, endHeight, speed, delay, curve, startDistanceOffset, size);
			}
		}
	}

	public static final boolean isAtWarriors(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		int regionId = tile.getRegionId();
		return destX >= 2837 && destX <= 3532 && destY >= 2876 && destY <= 3552 && regionId == 11319;
	}

	public static final boolean isMultiArea(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		int regionId = tile.getRegionId();
		return destX >= 3462 && destX <= 3511 && destY >= 9481 && destY <= 9521 && tile.getZ() == 0 // kalphite
																										// queen lair
				|| destX >= 4540 && destX <= 4799 && destY >= 5052 && destY <= 5183 && tile.getZ() == 0 // thzaar
																											// city
				|| destX >= 1721 && destX <= 1791 && destY >= 5123 && destY <= 5249 // mole
				|| destX >= 3029 && destX <= 3374 && destY >= 3759 && destY <= 3903// wild
				|| destX >= 2250 && destX <= 2280 && destY >= 4670 && destY <= 4720 // Black Drag
				|| destX >= 1363 && destX <= 1385 && destY >= 6613 && destY <= 6635// Blink
				|| destX >= 2517 && destX <= 2538 && destY >= 5227 && destY <= 5243// Yk Lagoor
				|| destX >= 3198 && destX <= 3380 && destY >= 3904 && destY <= 3970 // Deep wild Mage Arena
				|| destX >= 3191 && destX <= 3326 && destY >= 3510 && destY <= 3759 // Edge?
				|| destX >= 2987 && destX <= 3006 && destY >= 3912 && destY <= 3937
				|| destX >= 2382 && destX <= 2424 && destY >= 2422 && destY <= 2383
				|| destX >= 2894 && destX <= 2948 && destY >= 4423 && destY <= 4479
				|| destX >= 2245 && destX <= 2295 && destY >= 4675 && destY <= 4720
				|| destX >= 2382 && destX <= 2423 && destY >= 3845 && destY <= 3870 // Rock Crabs @ Train
				|| destX >= 2692 && destX <= 2724 && destY >= 3709 && destY <= 3734 // Rock Crabs @ Train
				|| destX >= 3006 && destX <= 3071 && destY >= 3602 && destY <= 3710
				|| destX >= 3134 && destX <= 3192 && destY >= 3519 && destY <= 3646
				|| destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375// wild
				|| destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230 // godwars
				|| destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699 // zaros
				|| destX >= 3834 && destY >= 4758 && destX <= 3814 && destY <= 4782
				|| destX >= 3814 && destY >= 4782 && destX <= 3834 && destY <= 4758
				|| destX >= 3778 && destY >= 3546 && destX <= 3827 && destY <= 3584 // Kraken
				|| destX >= 2899 && destY >= 2914 && destX <= 3261 && destY <= 3304 // Sea queen
				|| destX >= 3841 && destY >= 4781 && destX <= 3814 && destY <= 4758
				|| destX >= 3814 && destY >= 4758 && destX <= 3841 && destY <= 4781
				//event boss area
				|| destX >= 3666 && destY >= 4445 && destX <= 3699 && destY <= 4466
				// godwars
				|| KingBlackDragon.atKBD(tile) // King Black Dragon lair
				|| TormentedDemon.atTD(tile) // Tormented demon's area
				|| Bork.atBork(tile) // Bork's area
				|| destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400// corp
				|| destX >= 3195 && destX <= 3327 && destY >= 3520 && destY <= 3970
				|| destX >= 2376 && 5127 >= destY && destX <= 2422 && 5168 <= destY
				|| destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168 // pits
				|| destX >= 2864 && destY >= 2981 && destX <= 2878 && destY <= 2995 // boss raids
				|| destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752 // torms
				|| destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135 // castlewars
				|| destX >= 3780 && destY >= 3542 && destX <= 3834 && destY <= 3578 // Sunfreet
				// out
				|| destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532 // castlewars
				|| destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631
				|| destX >= 3008 && destY >= 2496 && destX <= 3071 && destY <= 2559
				|| destX >= 226 && destY >= 5408 && destX <= 187 && destY <= 5372 // pits
				|| destX >= 187 && destY >= 5372 && destX <= 226 && destY <= 5408 // pits
				|| destX >= 228 && destY >= 5370 && destX <= 186 && destY <= 5415 // pits
				|| destX >= 186 && destY >= 5415 && destX <= 228 && destY <= 5370 // pits
				|| destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631 // Safe ffa
				|| destX >= 1490 && destX <= 1515 && destY >= 4696 && destY <= 4714 // chaos dwarf battlefield
				|| destX >= 3333 && destX <= 3383 && destY >= 9345 && destY <= 9450 // Smokey well 1
				|| destX >= 3072 && destX <= 3136 && destY >= 4287 && destY <= 4416 // Smokey well 2
				|| destX >= 3140 && destX <= 3331 && destY >= 5441 && destY <= 5568 // CHAOS TUNNELS
				|| destX >= 1986 && destX <= 2045 && destY >= 4162 && destY <= 4286 || regionId == 16729 // glacors
				|| destX >= 3261 && destX <= 3329 && destY >= 4287 && destY <= 4416 // smokey well 3
				|| destX >= 2483 && destX <= 2499 && destY >= 2847 && destY <= 2861 // Boss Raids
				|| destX >= 2648 && destX <= 2733 && destY >= 3712 && destY <= 3736 // Relaka rock crabs
				|| destX >= 2944 && destY >= 2496 && destX <= 3007 && destY <= 2559 // Scorpia
				|| destX >= 2067 && destY >= 5640 && destX <= 2088 && destY <= 5667
				|| destX >= 1411 && destY >= 3726 && destX <= 1458 && destY <= 3687 //Lizardman Shaman
				|| destX >= 2065 && destY >= 5640 && destX <= 2087 && destY <= 5686 //Demonic gorillas
				|| destX >= 2065 && destY >= 5666 && destX <= 2164 && destY <= 5686 //Demonic gorillas
				|| destX >= 1592 && destY >= 10113 && destX <= 1729 && destY <= 9982
				|| tile.getX() >= 3011 && tile.getX() <= 3132 && tile.getY() >= 10052 && tile.getY() <= 10175 && (tile.getY() >= 10066 || tile.getX() >= 3094) // fortihrny dungeon
				|| destX >= 2944 && destY >= 4736 && destX <= 3135 && destY <= 4863 //abyss / sire
				|| destX >= 3840 && destY >= 2496 && destX <= 3903 && destY <= 2559 //dark demon
				|| destX >= 3712 && destY >= 2496 && destX <= 3775 && destY <= 2559 //lord of lightning
				|| destX >= 3776 && destY >= 2496 && destX <= 3839 && destY <= 2559 //amethyst dragon
				|| destX >= 2948 && destY >= 9474 && destX <= 2993 && destY <= 9531 //amethyst dragon
				|| destX >= 1152 && destY >= 2625 && destX <= 1279 && destY <= 2687 //amethyst dragon
				|| regionId == 14130 //amethyst dragon
				|| regionId == 9116
				|| regionId == 8770
				|| regionId == 11304
				|| regionId == 6557
				|| regionId == 6813
				|| regionId == 6556
				|| regionId == 6812
				|| regionId == 4962
				|| regionId == 7955
				|| regionId == 7954
				|| regionId == 8211


//				|| destX <= 2948  && destX >= 2993  && destY <= 9474 && destY >= 9531 //underwater
		;
		// in

		// multi
	}

	public static boolean isRestrictedCannon(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2250 && destX <= 2302 && destY >= 4673 && destY <= 4725 // King Black Dragon
				|| destX >= 3082 && destX <= 3122 && destY >= 5512 && destY <= 5560 // Bork
				|| destX >= 3462 && destX <= 3512 && destY >= 9473 && destY <= 9525 // Kalphite Queen
				|| destX >= 2894 && destX <= 2948 && destY >= 4423 && destY <= 4479 // Dagganoth Kings
				|| destX >= 2574 && destX <= 2634 && destY >= 5692 && destY <= 5759 // Tormented Demons
				|| destX >= 2969 && destX <= 3005 && destY >= 4357 && destY <= 4405 // Corporeal Beast
				|| destX >= 2898 && destX <= 2946 && destY >= 5181 && destY <= 5226 // Nex
				|| destX >= 2822 && destX <= 2936 && destY >= 5238 && destY <= 5379 // God Wars
				|| destX >= 2943 && destX <= 3561 && destY >= 3521 && destY <= 4052 // Wilderness
				|| destX >= 2518 && destX <= 2543 && destY >= 5226 && destY <= 5241 // Yklagor
				|| destX >= 2844 && destX <= 2868 && destY >= 9625 && destY <= 9650 // Sunfreet
				|| destX >= 1365 && destX <= 1387 && destY >= 6613 && destY <= 6636 // Blink
		;
	}

	public static final boolean isMorytaniaArea(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 3423 && destX <= 3463 && destY >= 3205 && destY <= 3268 // haunted mine
				|| destX >= 3464 && destX <= 3722 && destY >= 3160 && destY <= 3602 // Rest of mory
				|| destX >= 3413 && destX <= 3463 && destY >= 3206 && destY <= 3346 // Snail maze
				|| destX >= 3399 && destX <= 3463 && destY >= 3347 && destY <= 3450 // Part of swamp
				|| destX >= 3413 && destX <= 3463 && destY >= 3451 && destY <= 3467 // Part of swamp
				|| destX >= 3420 && destX <= 3463 && destY >= 3468 && destY <= 3607 // Part of swamp
				|| destX >= 3398 && destX <= 3463 && destY >= 3508 && destY <= 3607 // Part of upper part
				|| destX >= 3409 && destX <= 3419 && destY >= 3499 && destY <= 3507 // Part of upper part
				|| destX >= 3397 && destX <= 3660 && destY >= 9607 && destY <= 9852 // morytania underground
				|| destX >= 3466 && destX <= 3588 && destY >= 9857 && destY <= 9978 // werewolf agil, experiments
				|| destX >= 3643 && destX <= 3728 && destY >= 9847 && destY <= 9935 // Ectofun
				|| destX >= 2254 && destX <= 2287 && destY >= 5142 && destY <= 5162 // Drakan tomb
				|| destX >= 2686 && destX <= 2818 && destY >= 4416 && destY <= 4606 // haunted mine floors
				|| destX >= 3106 && destX <= 3218 && destY >= 4540 && destY <= 4680 // Tarn's lair
		;
	}

	public static final boolean isSmokeyArea(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 3199 && destX <= 3330 && destY >= 9341 && destY <= 9406 // Smoke dungeon
				|| destX >= 3333 && destX <= 3383 && destY >= 9345 && destY <= 9450 // Smokey well 1
				|| destX >= 3072 && destX <= 3136 && destY >= 4287 && destY <= 4416 // Smokey well 2
				|| destX >= 3261 && destX <= 3329 && destY >= 4287 && destY <= 4416 // Smokey well 3
		;
	}

	public static final boolean isDesertArea(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX <= 3294 && destX >= 3133 && destY <= 3132 && destY >= 2614
				|| destX <= 3566 && destX >= 3295 && destY <= 3115 && destY >= 2585
				|| destX <= 3512 && destX >= 3315 && destY <= 3132 && destY >= 3110
				|| destX <= 3355 && destX >= 3327 && destY <= 3156 && destY >= 3131;
	}

	public static final boolean isUnderwaterArea (Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX >= 2948 && destY >= 9474 && destX <= 2993 && destY <= 9531;
	}

	public static final boolean isSinkArea(Position tile) {
		int destX = tile.getX();
		int destY = tile.getY();
		return destX <= 1613 && destX >= 1534 && destY <= 4425 && destY >= 4346;
	}

	public static final boolean isPvpArea(Position tile) {
		return Wilderness.isAtWild(tile) || tile.withinArea(3587, 2662, 3607, 2684);
	}

	public static void destroySpawnedObject(WorldObject object, boolean clip) {
		getRegion(object.getRegionId()).removeObject(object, object.getZ(), object.getXInRegion(),
				object.getYInRegion());
	}

	public static void destroySpawnedObject(WorldObject object) {
		getRegion(object.getRegionId()).removeObject(object, object.getZ(), object.getXInRegion(),
				object.getYInRegion());
	}

	public static final void spawnTempGroundObject(final WorldObject object, final int replaceId, long time) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					removeObject(object);
					addGroundItem(new Item(replaceId), object, null, false, 180);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, time, TimeUnit.MILLISECONDS);
	}

	public static void sendWorldMessage(String message, boolean forStaff) {
		for (Player p : World.getPlayers()) {
			if (p == null || !p.isRunning() || p.isYellOff() || forStaff && p.getRights() == 0) {
				continue;
			}
			p.getPackets().sendGameMessage(message);
		}
	}

	public static final void sendProjectile(WorldObject object, Position startTile, Position endTile, int gfxId,
											int startHeight, int endHeight, int speed, int delay, int curve, int startOffset) {
		for (Player pl : players) {
			if (pl == null || !pl.withinDistance(object, 20)) {
				continue;
			}
			pl.getPackets().sendProjectile(null, startTile, endTile, gfxId, startHeight, endHeight, speed, delay, curve,
					startOffset, 1);
		}
	}

	public static final void sendProjectile(Entity from, Entity to, Projectile projectile) {
		for (Player player : players) {
			if (player == null || !player.withinDistance(from, 20))
				continue;
			player.getPackets().sendProjectile(to, from.getMiddleTile(), to, projectile.getGraphicsId(),
					projectile.getStartHeight(), projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(), projectile.getDistanceOffset(), (int) Math.ceil(from.getSize() / 2));
//			player.getPackets().sendProjectile(to, from.getMiddleTile(), to, projectile.getGraphicsId(), projectile.getStartHeight(),
//					projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(),
//						projectile.getDistanceOffset(), (int) Math.ceil(from.getSize() / 2));
		}
	}

	public static final void sendProjectile(Position from, Position to, Projectile projectile) {
		for (Player player : players) {
			if (player == null || !player.withinDistance(from, 20))
				continue;
			player.getPackets().sendProjectile(null, from, to, projectile.getGraphicsId(),
					projectile.getStartHeight(), projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(), projectile.getDistanceOffset(), 1);
//			player.getPackets().sendProjectile(null, from, to, projectile.getGraphicsId(), projectile.getStartHeight(),
//					projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(),
//						projectile.getDistanceOffset(), 1);
		}
	}

	public static final void sendProjectile(Entity from, Position to, Projectile projectile) {
		for (Player player : players) {
			if (player == null || !player.withinDistance(from, 20))
				continue;
			player.getPackets().sendProjectile(null, from.getMiddleTile(), to, projectile.getGraphicsId(),
					projectile.getStartHeight(), projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(), projectile.getDistanceOffset(), from.getSize());
//			player.getPackets().sendProjectile(null, from, to, projectile.getGraphicsId(), projectile.getStartHeight(),
//					projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(),
//						projectile.getDistanceOffset(), 1);
		}
	}

	public static final void sendProjectile(Position from, Entity to, Projectile projectile) {
		for (Player player : players) {
			if (player == null || !player.withinDistance(from, 20))
				continue;
			player.getPackets().sendProjectile(to, from, to, projectile.getGraphicsId(),
					projectile.getStartHeight(), projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(), projectile.getDistanceOffset(), 1);
//			player.getPackets().sendProjectile(null, from, to, projectile.getGraphicsId(), projectile.getStartHeight(),
//					projectile.getEndHeight(), projectile.getSpeed(), projectile.getDelay(), projectile.getAngle(),
//						projectile.getDistanceOffset(), 1);
		}
	}

	public static void deleteObject(int i, int j, boolean b) {
		// TODO Auto-generated method stub

	}

	public static void spawnObject(int i, Position position, int j, boolean b) {
		// TODO Auto-generated method stub

	}

	public static final void turnPublic(FloorItem floorItem, int publicTime) {
		if (!floorItem.isInvisible()) {
			return;
		}
		int regionId = floorItem.getTile().getRegionId();
		final Region region = getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(floorItem)) {
			return;
		}
		Player realOwner = floorItem.hasOwner() ? World.getPlayer(floorItem.getOwner()) : null;
		if (!ItemConstants.isTradeable(floorItem)) {
			region.getGroundItemsSafe().remove(floorItem);
			if (realOwner != null) {
				if (realOwner.getMapRegionsIds().contains(regionId)
						&& realOwner.getZ() == floorItem.getTile().getZ()) {
					realOwner.getPackets().sendRemoveGroundItem(floorItem);
				}
			}
			return;
		}
		floorItem.setInvisible(false);
		for (Player player : players) {
			if (player == null || player == realOwner || !player.hasStarted() || player.isFinished()
					|| player.getZ() != floorItem.getTile().getZ()
					|| !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
		// disapears after this time
		if (publicTime != -1) {
			removeGroundItem(floorItem, publicTime);
		}
	}

	public static final void addGroundItem(final Item item, final Position tile, final Player owner/*
																									 * null for default
																									 */,
										   boolean invisible, long hiddenTime/*
												 * default 3 minutes
												 */) {
		addGroundItem(item, tile, owner, invisible, hiddenTime, 2, 150);
	}

	public static final FloorItem addGroundItem(final Item item, final Position tile,
			final Player owner/*
								 * null for default
								 */, boolean invisible, long hiddenTime/*
																		 * default 3 minutes
																		 */, int type) {
		return addGroundItem(item, tile, owner, invisible, hiddenTime, type, 150);
	}

	/*
	 * type 0 - gold if not tradeable type 1 - gold if destroyable type 2 - no gold
	 */
	public static final FloorItem addGroundItem(final Item item, final Position tile, final Player owner,
												boolean invisible, long hiddenTime/*
												 * default 3 minutes
												 * 
												 * 
												 * 
												 * 
												 * 
												 * 
												 */, int type, final int publicTime) {
		/*
		 * if (type != 2) { if ((type == 0 && !ItemConstants.isTradeable(item)) || type
		 * == 1 && ItemConstants.isDestroy(item)) {
		 * 
		 * int price = item.getDefinitions().getValue(); if (price <= 0) return null;
		 * item.setId(995); item.setAmount(price); } }
		 */
		final FloorItem floorItem = new FloorItem(item, tile, owner, owner != null, invisible);
		final Region region = getRegion(tile.getRegionId());
		region.getGroundItemsSafe().add(floorItem);
		if (invisible) {
			if (owner != null) {
				owner.getPackets().sendGroundItem(floorItem);
			}
			// becomes visible after x time
			if (hiddenTime != -1) {
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							turnPublic(floorItem, publicTime);
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}
				}, hiddenTime, TimeUnit.SECONDS);
			}
		} else {
			// visible
			int regionId = tile.getRegionId();
			for (Player player : players) {
				if (player == null || !player.hasStarted() || player.isFinished()
						|| player.getZ() != tile.getZ() || !player.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				player.getPackets().sendGroundItem(floorItem);
			}
			// disapears after this time
			if (publicTime != -1) {
				removeGroundItem(floorItem, publicTime);
			}
		}
		return floorItem;
	}

	public static final void spawnObject(WorldObject object) {
		getRegion(object.getRegionId()).spawnObject(object, object.getZ(), object.getXInRegion(),
				object.getYInRegion(), false);
	}

	public static final void removeObject(WorldObject object) {
		getRegion(object.getRegionId()).removeObject(object, object.getZ(), object.getXInRegion(),
				object.getYInRegion());
	}

	public static final WorldObject getObjectWithSlot(Position tile, int slot) {
		return getRegion(tile.getRegionId()).getObjectWithSlot(tile.getZ(), tile.getXInRegion(),
				tile.getYInRegion(), slot);
	}

	public static boolean isTileFree(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (!isFloorFree(plane, tileX, tileY) || !isWallsFree(plane, tileX, tileY)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isFloorFree(int plane, int x, int y) {
		return (getMask(plane, x, y) & (0x200000 | 0x40000 | 0x100)) == 0;
	}

	public static boolean isWallsFree(int plane, int x, int y) {
		return (getMask(plane, x, y) & (0x4 | 0x1 | 0x10 | 0x40 | 0x8 | 0x2 | 0x20 | 0x80)) == 0;
	}

	public static void spawnNPC(NPC npc) {
		spawnNPC(npc.getId(), new Position(npc.getX(), npc.getY(), npc.getZ()), -1,
				0, npc.canBeAttackFromOutOfArea());

	}

	public static final WorldObject getSpawnedObject(int x, int y, int plane) {
		return getObject(new Position(x, y, plane));
	}

	public static final WorldObject getRemovedOrginalObject(int plane, int x, int y, int type) {
		return getRegion(new Position(x, y, plane).getRegionId()).getRemovedObjectWithSlot(plane, x, y, type);
	}
	
	public static final void addDungeoneeringGroundItem(final DungeonPartyManager manager, final Item item,
														final Position tile, boolean lootbeam) {
		addGroundItem(item, tile, manager.getLeaderPlayer(), false, -1, 2, -1);
	}

	/*
	 * by default doesnt changeClipData
	 */

	public static final void spawnTemporaryObject(final WorldObject object, long time, final boolean clip,
			final WorldObject actualObject) {
		World.spawnObjectTemporary(object, time);
	}

	public static final void spawnObjectTemporary(final WorldObject object, long time) {
		spawnObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if (!World.isSpawnedObject(object)) {
						return;
					}
					removeObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	/*
	 * by default doesnt changeClipData
	 */
	public static final void spawnTemporaryObject(final WorldObject object, long time, final WorldObject actualObject) {
		spawnTemporaryObject(object, time, false, actualObject);
	}

	public static final void removeItems(final FloorItem floorItem, final int publicTime) {
		int regionId = floorItem.getTile().getRegionId();
		final Region region = getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(floorItem)) {
			return;
		}
		// disapears after this time
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					World.removeGroundItem(floorItem, 0);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, publicTime, TimeUnit.SECONDS);
	}

	public static final void spawnGroundItem(final Item item, final Position tile) {
		World.removeAllGroundItem(item, tile);
		World.addGroundItem(item, tile);
	}

	public static final void removeAllGroundItem(final Item item, final Position tile) {
		final FloorItem floorItem = new FloorItem(item, tile, null, false, false);
		final Region region = getRegion(tile.getRegionId());
		int getAmount = floorItem.getAmount();
		for (Player player : players) {
			for (int i = 0; i < getAmount; i++) {
				region.forceGetFloorItems().remove(floorItem);
				player.getPackets().sendRemoveGroundItem(floorItem);
			}
		}
	}

	public static final void createTemporaryConfig(final int config, final int configType, long time,
			final Player player) {
		for (Player p2 : players) {
			if (p2 == null || !p2.hasStarted() || p2.isFinished()
					|| !p2.getMapRegionsIds().contains(player.getRegionId())) {
				continue;
			}
			p2.getPackets().sendConfigByFile(config, configType);
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					for (Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.isFinished()
								|| !p2.getMapRegionsIds().contains(player.getRegionId())) {
							continue;
						}
						p2.getPackets().sendConfigByFile(config, 0);
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static final WorldObject getObjectWithType(Position tile, int type) {
		return getRegion(tile.getRegionId()).getObjectWithType(tile.getZ(),
				tile.getXInRegion(), tile.getYInRegion(), type);
	}
	public static final boolean removeGroundItem(final FloorItem floorItem) {
		int regionId = floorItem.getTile().getRegionId();
		Region region = getRegion(regionId);
		if (!region.getGroundItemsSafe().contains(floorItem))
			return false;
		region.getGroundItemsSafe().remove(floorItem);
		for (Player player : World.getPlayers()) {
			if (player == null  || player.hasFinished()
					|| !player.getMapRegionsIds().contains(regionId))
				continue;
			player.getPackets().sendRemoveGroundItem(floorItem);
		}
		return true;
	}

	public static final WorldObject getStandartObject(Position tile) {
		return getRegion(tile.getRegionId()).getStandartObject(tile.getZ(),
				tile.getXInRegion(), tile.getYInRegion());
	}

	public static void sendTileMessage(Position tile, String message, int delay, int height, int color) {
		for (Player player : World.getPlayers()) {
			if (player == null || player.isFinished() || player.hasStarted() || !player.withinDistance(tile))
				continue;
			player.getPackets().sendTileMessage(message, tile, delay, height, color);
		}
	}

	public static final boolean removeObjectTemporary(final WorldObject object,
													  long time) {
		removeObject(object);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					addObject(object);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
		return true;
	}

	public static final void addObject(WorldObject object) {
		getRegion(object.getRegionId()).addObject(object, object.getZ(),
				object.getXInRegion(), object.getYInRegion(), false);
	}

	public static final void removeObject(Player player, WorldObject object) {
		getRegion(player.getRegionId()).removeObject( object, object.getZ(), object.getXInRegion(), object.getYInRegion());
	}


	public static final void spawnObject(Player player, WorldObject object) {
		getRegion(player.getRegionId()).spawnObject( object, object.getZ(), object.getXInRegion(),
				object.getYInRegion(), false);
	}
	public static final void spawnTemporaryObjectWithReplacement(final Player player, final WorldObject primary,
																 final WorldObject replacement, final long time) {
		spawnObject(player, primary);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					removeObject(player, replacement);
					spawnObject(player, replacement);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	
	
}
