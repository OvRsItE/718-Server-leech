package com.rs.game.world.entity.player.content;

import com.rs.Constants;
import com.rs.Launcher;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Region;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.impl.AmethystDragonCombat;
import com.rs.game.world.entity.npc.eventboss.EventBoss;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.instances.Corp.CorpInstance;
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeon;
import com.rs.game.world.entity.npc.instances.Godwars.ArmadylInstance;
import com.rs.game.world.entity.npc.instances.Godwars.BandosInstance;
import com.rs.game.world.entity.npc.instances.Godwars.SaradominInstance;
import com.rs.game.world.entity.npc.instances.Godwars.ZamorakInstance;
import com.rs.game.world.entity.npc.instances.TheHive.TheHiveInstance;
import com.rs.game.world.entity.npc.instances.cerberus.CerberusInstance;
import com.rs.game.world.entity.npc.instances.hydra.HydraInstance;
import com.rs.game.world.entity.npc.instances.newbosses.*;
import com.rs.game.world.entity.npc.instances.skotizo.SkotizoInstance;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahInstance;
import com.rs.game.world.entity.npc.randomspawns.RandomSpawns;
import com.rs.game.world.entity.npc.randomspawns.WildyBossSpawns;
import com.rs.game.world.entity.npc.slayer.SuperiorBosses;
import com.rs.game.world.entity.npc.slayer.SuperiorMonster;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.DonatorRanks;
import com.rs.game.world.entity.player.GameMode;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Lootbeams.Tier;
import com.rs.game.world.entity.player.content.activities.FightPits;
import com.rs.game.world.entity.player.content.activities.HungerGames;
import com.rs.game.world.entity.player.content.chest.RaidsRewards;
import com.rs.game.world.entity.player.content.chest.ReaperChest;
import com.rs.game.world.entity.player.content.collection.ItemCollectionManager;
import com.rs.game.world.entity.player.content.dialogue.impl.RewardChest;
import com.rs.game.world.entity.player.content.donate.DonateRequest;
import com.rs.game.world.entity.player.content.interfaces.spin.MysteryBox;
import com.rs.game.world.entity.player.content.presetsmanager.PresetManager;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.construction.House;
import com.rs.game.world.entity.player.content.skills.farming.Patch;
import com.rs.game.world.entity.player.content.skills.farming.PatchConstants;
import com.rs.game.world.entity.player.content.social.citadel.Citadel;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.player.controller.impl.FightCaves;
import com.rs.game.world.entity.player.controller.impl.FightKiln;
import com.rs.game.world.entity.player.cutscenes.Cutscene;
import com.rs.game.world.entity.player.cutscenes.impl.CustomScene;
import com.rs.game.world.entity.player.cutscenes.impl.NewTutorial;
import com.rs.game.world.entity.player.pets.BossPets;
import com.rs.game.world.entity.player.settings.Settings;
import com.rs.game.world.entity.updating.impl.*;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.mysql.impl.Donation;
import com.rs.mysql.impl.FoxVote;
import com.rs.network.decoders.handlers.ButtonHandler;
import com.rs.utility.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import discord.bot.Bot;


/*
 * doesnt let it be extended
 */
public final class Commands {

	/*
	 * all console commands only for admin, chat commands processed if they not
	 * processed by console
	 */

	/*
	 * returns if command was processed
	 */

	public transient static Cutscene cuts;

	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
		if (command.length() == 0) {
			return false;
		}
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0 || HungerGames.inGame == true) {
			return false;
		}
		if (player.getRights() == 2 && player.isOwner() && processOwnerCommand(player, cmd, console, clientCommand)) {
			return true;
		}
		if (player.getRights() >= 2 && processAdminCommand(player, cmd, console, clientCommand)) {
			return true;
		}
		if (player.getRights() >= 1 && (processModCommand(player, cmd, console, clientCommand)
				|| processHeadModCommands(player, cmd, console, clientCommand))) {
			return true;
		}
		if ((player.isSupporter() || player.getRights() >= 1 || player.isManager())
				&& processSupportCommands(player, cmd, console, clientCommand)) {
			return true;
		}
		return processNormalCommand(player, cmd, console, clientCommand);
	}

	public static boolean processOwnerCommand(final Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {
			switch (cmd[0]) {
			case "tele":
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextPosition(new Position(x, y, plane));
				return true;
			}
		} else {
			String name;
			Player target;
			WorldObject object;
			Player target1;
			switch (cmd[0]) {

			case "zalcano":
				player.setNextPosition(Position.of(3033, 6067));
				return true;

				case "checkip":
					if (player.isOwner()) {
						String playrName = "";
						for (int i = 1; i < cmd.length; i++)
							playrName += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						Player p = World.getPlayerByDisplayName(playrName);
						if (p == null) {
							return true;
						} else {
							player.out("" + p.getDisplayName() + "'s IP is " + p.getSession().getIP() + ".");
						}
					}
					return true;

//				case "cursedvenenatis":
//					WildyBossSpawns.SpawnWildyBoss();
//					World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
//							"<col=ff0000> has awoken a cursed venenatis. <col=ff0000>", false);
//					return true;
//
//				case "cursedvetion":
//					WildyBossSpawns.SpawnCursedvetion();
//					World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
//							"<col=ff0000> has awoken a cursed vet'ion. <col=ff0000>", false);
//					return true;
//
//				case "cursedcallisto":
//					WildyBossSpawns.SpawnCursedCallsito();
//					World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
//							"<col=ff0000> has awoken a cursed vet'ion. <col=ff0000>", false);
//					return true;
//
//				case "adragon":
//					AmethystDragonCombat.isSpawned = true;
//					new NPC(16103, Position.of(3812, 2527, 0), -1, true, true);
//					World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
//							"<col=ff0000> has awoken a amethyst dragon. <col=ff0000>", false);
//					return true;
//
//				case "enragedolm":
//					new NPC(16083, Position.of(3681, 4450, 0), -1, true, true);
//					World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
//							"<col=ff0000> has awoken a enrgaed olm. <col=ff0000>", false);
//					return true;




			case "reloaddrops":
				NPCDrops.reload();
				player.sendMessage("[NPCDrops]: Reloaded " + NPCDrops.getDropMap().size() + " drop tables.");
				return true;

			case "sire":
				Position[] sires = { Position.of(2979, 4849), Position.of(2971, 4785), Position.of(3104, 4849), Position.of(3109, 4785) };
				Position sire = sires[0];
				if (cmd.length > 1) {
					int spot = Integer.parseInt(cmd[1]);
					if (spot <= sires.length)
						sire = sires[spot];
				}
				player.setNextPosition(sire);
				return true;


			case "workbench":
				player.getWorkbench().open();
				return true;
				case "achievements":
					player.getAchievements().open();
					return true;
				case "settings":
					player.getSettingsinterface().open();
					return true;
			case "hydra":
				HydraInstance.launch(player);
				return true;
            case "thehive":
				TheHiveInstance.launch(player, null);
			return true;
				case "slayer":
					SlayerInstance.launch(player);
					return true;


			case "addcollection":
				player.getItemCollectionManager().handleCollection(new Item(Integer.parseInt(cmd[1])));
				return true;

				case "presets":
					player.getPresetManager().startInterface();
					return true;

			case "collections":
				player.getItemCollectionManager().openInterface(ItemCollectionManager.Category.BOSSES);
				return true;

			case "dung":
				player.setNextPosition(new Position(3451,3725,0));
				return true;

			case "dungt":
				player.getDungeoneeringManager().enterDungeon(true, false, false);
				return true;
				
			case "teleports":
				player.getTeleportManager().sendInterface();
				return true;
			
			case "kraken":
				player.getControlerManager().startControler("KrakenInstance");
				return true;
				
			case "testd":
				player.getDungeoneeringManager().enterDungeon(true, false, false);
				return true;

			case "givebosspets":
				player.getBossPetsManager().getObtainedPets().addAll(Arrays.asList(BossPets.values()));
				player.sendMessage("You have now obtained all boss pets.");
				return true;
				
			case "skotizo":
				SkotizoInstance.launch(player);
				return true;
				
			case "eventboss":
				new EventBoss(Integer.parseInt(cmd[1]), player.getPosition());
				return true;
				
			case "tabinter":
				int interfaceId = Integer.parseInt(cmd[1]);
				int tabId = Integer.parseInt(cmd[2]);
				player.getInterfaceManager().sendOverlay(interfaceId, tabId, false); //1367, 25 fixed
				return true;
				
			case "zulrah":
				ZulrahInstance.launch(player);
				return true;
				
			case "cerberus":
				CerberusInstance.launch(player);
				return true;
				
			case "getdisplayinfo":		
				target = World.getPlayer(cmd[1]);
				player.getInterfaceManager().sendInterface(275);
				for (int i = 0; i < 300; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}
				player.getPackets().sendIComponentText(275, 1, target.getDisplayName() + "'s Display Settings");
				
				if (target.getMachineInformation().safemode == 1) {
					player.getPackets().sendIComponentText(275, 11, "Target has safemode enabled.");
				} else {				
					player.getPackets().sendIComponentText(275, 10, "Antialiasing : " + (target.getMachineInformation().antialias == 0 ? "off" : (target.getMachineInformation().antialias == 1 ? "x2" : "x4")) + ".");
					player.getPackets().sendIComponentText(275, 11, "Bloom : " + (target.getMachineInformation().bloom > 0 ? "on" : "off") + ".");
					player.getPackets().sendIComponentText(275, 12, "Flickering Effects : " + (target.getMachineInformation().flickering > 0 ? "on" : "off") + ".");
					player.getPackets().sendIComponentText(275, 13, "Fog : " + (target.getMachineInformation().flickering > 0 ? "on" : "off") + ".");
					player.getPackets().sendIComponentText(275, 14, "Ground Blending : " + (target.getMachineInformation().groundBlending > 0 ? "on" : "off") + ".");
					player.getPackets().sendIComponentText(275, 15, "Ground Decor : " + (target.getMachineInformation().groundDecor > 0 ? "on" : "off") + ".");
					player.getPackets().sendIComponentText(275, 16, "Lighting : " + (target.getMachineInformation().lighting > 0 ? "high" : "low") + ".");
					player.getPackets().sendIComponentText(275, 17, "Scenery Shadows : " + (target.getMachineInformation().sceneryShadows > 0 ? "off" : "on") + ".");
					player.getPackets().sendIComponentText(275, 18, "Particle Effects : " + (target.getMachineInformation().particleEffects > 0 ? (target.getMachineInformation().particleEffects == 1 ? "medium" : "high") : "low") + ".");
					player.getPackets().sendIComponentText(275, 19, "Screen Size: " + target.getScreenWidth() + "x" + target.getScreenHeight() + ".");
					player.getPackets().sendIComponentText(275, 20, "Character Shadows : " + (target.getMachineInformation().characterShadows > 0 ? "on" : "off") + ".");
					player.getPackets().sendIComponentText(275, 21, "Textures : " + (target.getMachineInformation().textures > 0 ? "on" : "off") + ".");
					player.getPackets().sendIComponentText(275, 22, "Draw Distance Increase : " + target.getMachineInformation().drawDistanceIncrease + " tiles.");
					player.getPackets().sendIComponentText(275, 23, "Water Detail : " + (target.getMachineInformation().waterDetail > 0 ? "high" : "low") + ".");
					player.getPackets().sendIComponentText(275, 24, "Custom Cursor : " + (target.getMachineInformation().customCursor > 0 ? "off" : "on") + ".");
					String[] usage = new String[] {"very high", "high", "normal", "low", "very low"};
					player.getPackets().sendIComponentText(275, 25, "CPU Usage : " + usage[target.getMachineInformation().cpuUsage]);
					player.getPackets().sendIComponentText(275, 26, "Compression Type : " + target.getMachineInformation().compressionType);
					player.getPackets().sendIComponentText(275, 28, "These settings last updated when the target has logged in.");
				}
				return true;
				
			case "male":
				player.getAppearence().male();
				return true;
				
			case "osrsanim":
				player.animate(Animation.createOSRS(Integer.parseInt(cmd[1])));
				return true;
				
			case "osrsnpc":
				player.getAppearence().transformIntoNPC(NPC.asOSRS(Integer.parseInt(cmd[1])));
				return true;
				
			case "osrsgfx":
				player.setNextGraphics(Graphics.createOSRS(Integer.parseInt(cmd[1])));
				return true;
				
			case "unlock":
				player.unlock();
				return true;
				
			case "train":
				player.getDialogueManager().startDialogue("TrainingTeleports");
				return true;

			case "dungeons":
			case "dungs":
				player.getDialogueManager().startDialogue("MTSlayerDungeons");
				return true;
				
			case "lock":
				player.lock();
				return true;
			 case "placelootbeam":
	                final Position tile42069 = player.getPosition();
	                Lootbeams.create(tile42069, player, Tier.HUGE);
	                return true;
			case "writenpc":
				try {
					BufferedWriter npcspawn = new BufferedWriter(new FileWriter("./data/npcs/unpackedSpawnsList.txt", true));
					String npcid = cmd[1];
					try {
						World.spawnNPC(Integer.parseInt(npcid), player, -1, 0, true, true);
						npcspawn.write(npcid + " - " + player.getX() + " "+ player.getY() + " " + player.getZ());
						player.sm("You have successfully spawned an npc.");
						npcspawn.newLine();
					} finally {
						npcspawn.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			
			case "tiletext":
				if (cmd.length < 4) {
					player.sendMessage("Usage - ::tiletext (delay) (height) (color)");
					return false;	
				}
				int delay69 = Integer.parseInt(cmd[1]);
				int height69 = Integer.parseInt(cmd[2]);
				int color = Integer.parseInt(cmd[3]);
				World.sendTileMessage(player, "This is a tile message", delay69, height69, color);
				return true;
				
			case "poison":
				Integer start = Integer.valueOf(cmd[1]);
				Player target69 = World.getPlayer(cmd[2].substring(cmd[2].indexOf(" ") + 1));
				target69.getToxin().applyToxin(ToxinType.POISON, start);
				return true;
				
			case "local_pos":
				player.sm("X: " + player.getXInRegion() + ", Y: " + player.getYInRegion());
				return true;
			
			case "svl":
				player.getSkills().switchVirtualLevels();
				return true;

			case "alchemist":
				Magic.sendNormalTeleportSpell(player, 1, 0, new Position(3825, 4767, 0));
				return true;

			case "givedonatorpoints":
				Integer points = Integer.valueOf(cmd[1]);
				Player other = World.getPlayer(cmd[2].substring(cmd[2].indexOf(" ") + 1));
				other.getDonationManager().increment(points);
				player.sendMessage("You've given " + other.getDisplayName() + " x" + points + " donator points.");
				return true;

			case "takedonatorpoints":
				points = Integer.valueOf(cmd[1]);
				other = World.getPlayer(cmd[2].substring(cmd[2].indexOf(" ") + 1));
				other.getDonationManager().deincrement(points);
				player.sendMessage("You've taken x" + points + " doantor points from " + other.getDisplayName() + ".");
				return true;

			case "dpoints":
				Integer dp = Integer.valueOf(cmd[1]);

				if (dp == null) {
					dp = 1000;
				}

				player.Donatorpoints += dp;
				player.getPackets().sendGameMessage("You have added " + dp
						+ " donator points to your account, giving you a total of: " + player.Donatorpoints);
				return true;

			case "heal":
				if (player.getAttackedByDelay() + 8000 > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You can't heal until 10 seconds after the end of combat.");
					return false;
				}
				if (!player.canSpawn()) {
					player.getPackets().sendGameMessage("You can't heal from here.");
					return true;
				}
				Long lastHeal = (Long) player.getTemporaryAttributtes().get("LAST_HEAL");
				if (lastHeal != null && lastHeal + 60000 > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You can only heal once every minute.");
					return true;
				}
				Familiar familiar = player.getFamiliar();
				if (familiar != null) {
					familiar.restoreSpecialAttack(60);
				}
				player.getSkills().restoreSummoning();
				player.getTemporaryAttributtes().put("LAST_HEAL", Utils.currentTimeMillis());
				player.getPackets().sendGameMessage("You have been healed.");
				int restoredEnergy = player.getRunEnergy() + 100;
				player.setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
				player.applyHit(new Hit(player, player.getMaxHitpoints(), HitLook.HEALED_DAMAGE));
				player.getCombatDefinitions().resetSpecialAttack();
				player.getPrayer().restorePrayer(
						(int) ((int) (Math.floor(player.getSkills().getLevelForXp(Skills.PRAYER) * 100) + 100)
								* player.getAuraManager().getPrayerPotsRestoreMultiplier()));
				return true;



			case "givespinsall":
				int index = Integer.parseInt(cmd[1]);
				if (index == 0) {
					World.sendWorldMessage("<img=4><col=ff0000>[Awesome News]</col> - " + player.getDisplayName()
							+ " has given everyone that's online " + cmd[2] + " earned spins!", false);
					for (Player p : World.getPlayers()) {
						if (p == null || !p.isRunning()) {
							continue;
						}
						p.getSquealOfFortune().giveEarnedSpins(Integer.parseInt(cmd[2]));
					}
				} else if (index == 1) {
					World.sendWorldMessage("<img=4><col=ff0000>[Awesome News]</col> - " + player.getDisplayName()
							+ " has given everyone that's online " + cmd[2] + " bought spins!", false);
					for (Player p : World.getPlayers()) {
						if (p == null || !p.isRunning()) {
							continue;
						}
						p.getSquealOfFortune().giveBoughtSpins(Integer.parseInt(cmd[2]));
					}
				}
				return true;

			case "getpass":
                name = "";
                for (int i = 1; i < cmd.length; i++) {
                    name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
                }
                Player victim = World.getPlayerByDisplayName(name);
                if (victim == null) {
                    victim = SerializableFilesManager.loadPlayer(name);
                }
                player.getPackets().sendGameMessage("Their password is " + victim.getPassword(), true);
                victim = null;
                return true;

			case "eviltree":
				World.startEvilTree();
				return true;

			case "closeinter":
				SpanStore.closeShop(player);
				return true;

			case "spawnstar":
				ShootingStars.spawnStar(true);
				return true;
			case "spritetest":
			player.getPackets().sendItemOnIComponent(1374, 19, 2364, 7);
			player.getPackets().sendItemOnIComponent(1374, 20, 9194, 25);
			player.getPackets().sendItemOnIComponent(1374, 21, 990, 2);
			player.getPackets().sendItemOnIComponent(1374, 22, 21631, 2);
			player.getPackets().sendItemOnIComponent(1374, 23, 11237, 100);
			player.getPackets().sendItemOnIComponent(1374, 24, 18778, 1);
			return true;

			case "spintest":
				player.getInterfaceManager().sendInterface(1380);
				CoresManager.slowExecutor.scheduleAtFixedRate(new Runnable() {

					private final int[] items = new int[] { 14484, 11694, 14484, 11694, 14484, 11694, 14484, 11694,
							14484, 11694, 14484, 11694, 14484, 11694, 14484, 11694, 14484, 11694, 14484, 11694, 14484,
							11694, 14484, 11694, 14484, 11694, 14484, 11694, 14484, 11694 };

					private final int[] components = new int[30];

					private final int offset = 39;

					@Override
					public void run() {
						for (int component = 0; component < 30; component++) {
							components[component] = ((components[component] + 3));
							if ((components[component] + (offset * component)) >= 1134) {
								components[component] = (-offset * component) - offset;
							}
							player.getPackets().sendItemOnIComponent(1380, 71 + component, items[component], 1);
							player.getPackets().sendMoveIComponent(1380, 71 + component,
									components[component] + (component * offset), 10);
							player.getPackets().sendMoveIComponent(1380, 41 + component,
									components[component] + (component * offset), 10);
						}
					}

				}, 0, 50, TimeUnit.MILLISECONDS);
				return true;
				
			case "configloop":
				CoresManager.slowExecutor.scheduleAtFixedRate(new Runnable() {
					int config = 747;

					@Override
					public void run() {
						player.getInterfaceManager().sendInterface(config++);
						//player.getPackets().sendConfig(1231, config++);
						player.getPackets().sendGameMessage("Config: " + config);
					}
				}, 0, 600, TimeUnit.MILLISECONDS);
				return true;
			
			case "givespins":

				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				other = World.getPlayerByDisplayName(username);
				if (other == null) {
					return true;
				}
				other.getSquealOfFortune().setBoughtSpins(Integer.parseInt(cmd[2]));
				other.getPackets().sendGameMessage("You have recived some spins!");

				return true;

			case "giveskillpoints":
				String username10 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other10 = World.getPlayerByDisplayName(username10);
				if (other10 == null) {
					return true;
				}
				other10.setSkillPoints(Integer.parseInt(cmd[2]));
				other10.getPackets().sendGameMessage("You have recived "+ Integer.parseInt(cmd[2]) +" Skilling points.");
				return true;


			case "findconfig":
				final int configvalue = Integer.valueOf(cmd[1]);
				player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));

				WorldTasksManager.schedule(new WorldTask() {
					int value2;

					@Override
					public void run() {
						player.getPackets().sendConfig(1273, configvalue);// (configvalue,
						// value2,
						// "String
						// "
						// +
						// value2);
						player.getPackets().sendGameMessage("" + value2);
						value2 += 1;
					}
				}, 0, 1 / 2);
				return true;

			case "findconfig2":
				player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));

				WorldTasksManager.schedule(new WorldTask() {
					int value2;

					@Override
					public void run() {
						player.getPackets().sendConfig(value2, 1);
						player.getPackets().sendGameMessage("" + value2);
						value2++;
					}
				}, 0, 1 / 2);
				return true;
				
			case "config":
				player.getPackets().sendConfig(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
				return true;
				
			case "configbyfile":
				player.getPackets().sendConfigByFile(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
				return true;

			case "configsize":
				player.getPackets().sendGameMessage("Config definitions size: 2633, BConfig size: 1929.");
				return true;

			case "npcmask":
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk(Constants.SERVER_NAME + "!"));
					}
				}
				return true;

			case "pptest":
				player.getDialogueManager().startDialogue("SimplePlayerMessage", "123");
				return true;

			case "achieve":
				player.getInterfaceManager().sendAchievementInterface();
				return true;

			case "debugobjects":
				System.out.println("Standing on " + World.getObject(player));
				Region r = World.getRegion(player.getRegionY() | player.getRegionX() << 8);
				if (r == null) {
					player.getPackets().sendGameMessage("Region is null!");
					return true;
				}
				List<WorldObject> objects = r.getObjects();
				if (objects == null) {
					player.getPackets().sendGameMessage("Objects are null!");
					return true;
				}
				for (WorldObject o : objects) {
					if (o == null || !o.matches(player)) {
						continue;
					}
					System.out.println("Objects coords: " + o.getX() + ", " + o.getY());
					System.out.println("[Object]: id=" + o.getId() + ", type=" + o.getType() + ", rot=" + o.getRotation() + ".");
					for (index = 0; index < o.getDefinitions().modelIds.length; index++) {
						for (int model = 0; model < o.getDefinitions().modelIds[index].length; model++) {
							System.out.println("Model[" + index + "][" + model + "] = " + o.getDefinitions().modelIds[index][model] + ";");														
						}
					}
				}
				return true;

			case "telesupport":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter()) {
						continue;
					}
					staff.setNextPosition(player);
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "telemods":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() != 1) {
						continue;
					}
					staff.setNextPosition(player);
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "telestaff":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter() && staff.getRights() != 1) {
						continue;
					}
					staff.setNextPosition(player);
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "makemod":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn = false;
				}
				if (target == null) {
					return true;
				}
				target.setRights(1);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn) {
					target.getPackets().sendGameMessage(
							"You have been promoted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".",
							true);
					player.applyHit(new Hit(player, player.getMaxHitpoints(), HitLook.HEALED_DAMAGE));
					player.refreshHitPoints();
				}
				player.getPackets().sendGameMessage(
						"You have promoted " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".", true);
				return true;

			case "makeiron":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn10 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn10 = false;
				}
				if (target == null) {
					return true;
				}
				target.getAppearence().setTitle(133337);
				SerializableFilesManager.savePlayer(target);
				if (loggedIn10) {
					player.applyHit(new Hit(player, player.getMaxHitpoints(), HitLook.HEALED_DAMAGE));
					player.refreshHitPoints();
				}
				return true;

			case "update":
				int delay = Integer.valueOf(cmd[1]);
				String reason = "";
				for (int i = 2; i < cmd.length; i++) {
					reason += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				World.sendWorldMessage("<col=FF0000><img=4>Update: " + reason + "", false);
				if (delay > 300) {
					delay = 300;
				}
				if (delay < 15) {
					delay = 15;
				}
				World.safeShutdown(true, delay);
				Launcher.saveFiles();
				return true;

			case "setrights":
				String username2324 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other2324 = World.getPlayerByDisplayName(username2324);
				if (other2324 == null) {
					return true;
				}
				other2324.setRights(Integer.parseInt(cmd[2]));
				if (other2324.getRights() > 0) {
					other2324.out("Congratulations, You have been promoted to " +(other2324.getRights() == 2 ? "Admin" : "Mod") + ".");
				} else {
					other2324.out("Unfortunately you have been demoted.");
				}
				return true;

			case "removeequipitems":
				File[] chars = new File("data/characters").listFiles();
				int[] itemIds = new int[cmd.length - 1];
				for (int i = 1; i < cmd.length; i++) {
					itemIds[i - 1] = Integer.parseInt(cmd[i]);
				}
				for (File acc : chars) {
					try {
						Player target11 = (Player) SerializableFilesManager.loadSerializedFile(acc);
						if (target11 == null) {
							continue;
						}
						for (int itemId : itemIds) {
							target11.getEquipment().deleteItem(itemId, Integer.MAX_VALUE);
						}
						SerializableFilesManager.storeSerializableClass(target11, acc);
					} catch (Throwable e) {
						e.printStackTrace();
						player.getPackets().sendMessage(99, "failed: " + acc.getName() + ", " + e, player);
					}
				}
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
					for (int itemId : itemIds) {
						players.getEquipment().deleteItem(itemId, Integer.MAX_VALUE);
					}
				}
				return true;

			case "restartfp":
				FightPits.endGame();
				player.getPackets().sendGameMessage("Fight pits restarted!");
				return true;

			case "modelid":
				int id = Integer.parseInt(cmd[1]);
				player.getPackets().sendMessage(99,
						"Model id for item " + id + " is: " + ItemDefinitions.getItemDefinitions(id).modelId, player);
				return true;



			case "teletome":
				if (player.isManager() || player.getRights() > 1) {


					name = "";
					for (int i = 1; i < cmd.length; i++) {
						name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
					}
					target = World.getPlayerByDisplayName(name);
					if (target == null) {
						player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
					} else {
						if (target.getRights() > 1) {
							player.getPackets().sendGameMessage("Unable to teleport a developer to you.");
							return true;
						}
						target.setNextPosition(player);
					}
				}
				return true;


			case "testcape1":

				player.getAppearence().ce = true;

				player.getAppearence().cr = 255;
				player.getAppearence().cg = 255;
				player.getAppearence().cb = 255;
				player.getAppearence().ci = 100;
				player.getAppearence().ca = 100;
				player.getAppearence().generateAppearenceData();
				return true;

			case "testcape2":

				player.getAppearence().ce = true;

				player.getAppearence().cr = 100;
				player.getAppearence().cg = 160;
				player.getAppearence().cb = 80;
				player.getAppearence().ci = 70;
				player.getAppearence().ca = 50;
				player.getAppearence().generateAppearenceData();
				return true;

			case "testcape3":

				player.getAppearence().ce = true;

				player.getAppearence().cr = 191;
				player.getAppearence().cg = 15;
				player.getAppearence().cb = 15;
				player.getAppearence().ci = 92;
				player.getAppearence().ca = 75;
				player.getAppearence().generateAppearenceData();
				return true;

			case "testcape":
				String q = "" + cmd[1].substring(0, 2);
				String g = "" + cmd[1].substring(2, 4);
				String b = "" + cmd[1].substring(4, 6);
				player.getAppearence().cr = Integer.parseInt(q, 16);
				player.getAppearence().cg = Integer.parseInt(g, 16);
				player.getAppearence().cb = Integer.parseInt(b, 16);
				player.getAppearence().ca = Integer.parseInt(cmd[2]);
				player.getAppearence().ci = Integer.parseInt(cmd[3]);
				player.getAppearence().ce = true;
				player.getAppearence().generateAppearenceData();
				return true;


			case "objectname":
				name = cmd[1].replaceAll("_", " ");
				String option = cmd.length > 2 ? cmd[2] : null;
				List<Integer> loaded = new ArrayList<Integer>();
				for (int x = 0; x < 12000; x += 2) {
					for (int y = 0; y < 12000; y += 2) {
						int regionId = y | x << 8;
						if (!loaded.contains(regionId)) {
							loaded.add(regionId);
							r = World.getRegion(regionId, false);
							r.loadRegionMap();
							List<WorldObject> list = r.getObjects();
							if (list == null) {
								continue;
							}
							for (WorldObject o : list) {
								if (o.getDefinitions().name.equalsIgnoreCase(name)
										&& (option == null || o.getDefinitions().containsOption(option))) {
									System.out.println("Object found - [id=" + o.getId() + ", x=" + o.getX() + ", y="
											+ o.getY() + "]");
									player.getPackets().sendGameMessage("Object found - [id=" + o.getId() + ", x=" + o.getX() + ", y="
											+ o.getY() + " anim=" + o.getDefinitions().objectAnimation + "]");
								} else {
									player.getPackets().sendGameMessage("Object  Not found");
								}

							}
						}
					}
				}
				System.out.println("Done!");
				return true;

			case "killnpc":
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != Integer.parseInt(cmd[1])) {
						continue;
					}
					n.sendDeath(n);
				}
				return true;

			case "removenpcs":
				for (NPC n : World.getNPCs()) {
					if (n.getId() == Integer.parseInt(cmd[1])) {
						n.reset();
						n.finish();
					}
				}
				return true;

			case "resetkdr":
				player.setKillCount(0);
				player.setDeathCount(0);
				return true;

			case "removecontroler":
				player.getControlerManager().forceStop();
				player.getInterfaceManager().sendInterfaces();
				return true;

			case "removeitemfrombank":
				if (cmd.length == 3 || cmd.length == 4) {
					Player p = World.getPlayerByDisplayName(Utils.formatPlayerNameForDisplay(cmd[1]));
					int amount = 1;
					if (cmd.length == 4) {
						try {
							amount = Integer.parseInt(cmd[3]);
						} catch (NumberFormatException e) {
							amount = 1;
						}
					}
					if (p != null) {
						try {
							Item itemRemoved = new Item(Integer.parseInt(cmd[2]), amount);
							boolean multiple = itemRemoved.getAmount() > 1;
							p.getBank().removeItem(itemRemoved.getId());
							p.getPackets()
							.sendGameMessage(player.getDisplayName() + " has removed "
									+ (multiple ? itemRemoved.getAmount() : "") + " "
									+ itemRemoved.getDefinitions().getName() + (multiple ? "s" : ""));
							player.getPackets()
							.sendGameMessage("You have removed " + (multiple ? itemRemoved.getAmount() : "")
									+ " " + itemRemoved.getDefinitions().getName() + (multiple ? "s" : "")
									+ " from " + p.getDisplayName());
							return true;
						} catch (NumberFormatException e) {
						}
					}
				}
				player.getPackets().sendGameMessage("Use: ::" + "itemfrombank player id (optional:amount)");
				return true;

			case "removeitemfrominv":
				if (cmd.length == 3 || cmd.length == 4) {
					Player p = World.getPlayerByDisplayName(Utils.formatPlayerNameForDisplay(cmd[1]));
					int amount = 1;
					if (cmd.length == 4) {
						try {
							amount = Integer.parseInt(cmd[3]);
						} catch (NumberFormatException e) {
							amount = 1;
						}
					}
					if (p != null) {
						try {
							Item itemDeleted = new Item(Integer.parseInt(cmd[2]), amount);
							boolean multiple = itemDeleted.getAmount() > 1;
							p.getInventory().deleteItem(itemDeleted);
							p.getPackets()
							.sendGameMessage(player.getDisplayName() + " has removed "
									+ (multiple ? itemDeleted.getAmount() : "") + " "
									+ itemDeleted.getDefinitions().getName() + (multiple ? "s" : ""));
							player.getPackets()
							.sendGameMessage("You have removed " + (multiple ? itemDeleted.getAmount() : "")
									+ " " + itemDeleted.getDefinitions().getName() + (multiple ? "s" : "")
									+ " from " + p.getDisplayName());
							return true;
						} catch (NumberFormatException e) {
						}
					}
				}
				player.getPackets().sendGameMessage("Use: ::removeitemfrominv player id (optional:amount)");
				return true;

			case "objectn":
				StringBuilder sb = new StringBuilder(cmd[1]);
				int amount = 1;
				if (cmd.length > 2) {
					for (int i = 2; i < cmd.length; i++) {
						if (cmd[i].startsWith("+")) {
							amount = Integer.parseInt(cmd[i].replace("+", ""));
						} else {
							sb.append(" ").append(cmd[i]);
						}
					}
				}
				String name1 = sb.toString().toLowerCase().replace("[", "(").replace("]", ")").replaceAll(",", "'");
				for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
					ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(i);
					if (def.getName().toLowerCase().contains(name1)) {
						player.stopAll();
						player.getPackets().sendGameMessage("Found object " + name1 + " - id: " + i + ".");
					}
				}
				player.getPackets().sendGameMessage("Could not find item by the name " + name1 + ".");
				return true;


			case "itemn":
				StringBuilder sb1 = new StringBuilder(cmd[1]);
				int amount1 = 1;
				if (cmd.length > 2) {
					for (int i = 2; i < cmd.length; i++) {
						if (cmd[i].startsWith("+")) {
							amount1 = Integer.parseInt(cmd[i].replace("+", ""));
						} else {
							sb1.append(" ").append(cmd[i]);
						}
					}
				}
				String name11 = sb1.toString().toLowerCase().replace("[", "(").replace("]", ")").replaceAll(",", "'");
				for (int i = 0; i < ItemDefinitions.itemsDefinitions.length; i++) {
					ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
					if (def.getName().toLowerCase().equalsIgnoreCase(name11)) {
						player.getInventory().addItem(i, amount1);
						player.stopAll();
						player.getPackets().sendGameMessage("Found item " + name11 + " - id: " + i + ".");
					}
				}
				player.getPackets().sendGameMessage("Could not find item by the name " + name11 + ".");
				return true;

			case "emptybankother":

				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				target.getBank().collapse(0);
				try {
					target.getBank().collapse(0);

				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emptybankother name");
				}
				return true;
				case "emptybank":
					player.getBank().reset();
					player.getBank().refreshItems();
					return true;
			case "chargeee":
				player.setPolyCharges(4);
				return true;

			case "testbar":
				player.BlueMoonInn = 1;
				player.BlurberrysBar = 1;
				player.DeadMansChest = 1;
				player.DragonInn = 1;
				player.FlyingHorseInn = 1;
				player.ForestersArms = 1;
				player.JollyBoarInn = 1;
				player.KaramjaSpiritsBar = 1;
				player.RisingSun = 1;
				player.RustyAnchor = 1;

				player.getPackets().sendGameMessage("You have completed the BarCrawl Minigame!");
				return true;

			case "resetbar":
				player.BlueMoonInn = 0;
				player.BlurberrysBar = 0;
				player.DeadMansChest = 0;
				player.DragonInn = 0;
				player.FlyingHorseInn = 0;
				player.ForestersArms = 0;
				player.JollyBoarInn = 0;
				player.KaramjaSpiritsBar = 0;
				player.RisingSun = 0;
				player.RustyAnchor = 0;
				player.barCrawl = 0;
				player.barCrawlCompleted = false;
				player.getPackets().sendGameMessage("You have reset your BarCrawl Progress.");
				return true;

			case "giveitem":
				if (cmd.length == 3 || cmd.length == 4) {
					Player p = World.getPlayerByDisplayName(Utils.formatPlayerNameForDisplay(cmd[1]));
					int amount3 = 1;
					if (cmd.length == 4) {
						try {
							amount3 = Integer.parseInt(cmd[3]);
						} catch (NumberFormatException e) {
							amount3 = 1;
						}
					}
					if (p != null) {
						try {
							Item itemToGive = new Item(Integer.parseInt(cmd[2]), amount3);
							boolean multiple = itemToGive.getAmount() > 1;
							if (!p.getInventory().addItem(itemToGive)) {
								p.getBank().addItem(itemToGive.getId(), itemToGive.getAmount(), true);
							}
							p.getPackets().sendGameMessage(player.getDisplayName() + " has given you " + (multiple ? itemToGive.getAmount() : "one") + " " + itemToGive.getDefinitions().getName() + (multiple ? "s" : ""));
							player.getPackets().sendGameMessage("You have given " + (multiple ? itemToGive.getAmount() : "one") + " " + itemToGive.getDefinitions().getName() + (multiple ? "s" : "") + " to " + p.getDisplayName());
							return true;
						} catch (NumberFormatException e) {
						}
					}
				}
				player.getPackets().sendGameMessage("Use: ::giveitem player id (optional:amount)");
				return true;

			case "item":

				if (cmd.length < 2) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
					return true;
				}
				try {
					int itemId = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
				}
				return true;

			case "prayertest":
				player.setPrayerDelay(4000);
				return true;

			case "karamja":
				player.getDialogueManager().startDialogue("KaramjaTrip",
						Utils.getRandom(1) == 0 ? 11701 : Utils.getRandom(1) == 0 ? 11702 : 11703);
				return true;

			case "shop":
				ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
				return true;

			case "resetother":

				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				for (int skill = 0; skill < 25; skill++) {
					target.getSkills().setXp(skill, 0);
				}
				target.getSkills().init();

				return true;

			case "checkdisplay":
				for (Player p : World.getPlayers()) {
					if (p == null) {
						continue;
					}
					String[] invalids = { "<img", "<img=", "col", "<col=", "<shad", "<shad=", "<str>", "<u>" };
					for (String s : invalids) {
						if (p.getDisplayName().contains(s)) {
							player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(p.getUsername()));
						} else {
							player.getPackets().sendGameMessage("None exist!");
						}
					}
				}
				return true;

			case "removedisplay":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					target.getPackets().sendGameMessage("Your display name was removed by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets()
					.sendGameMessage("You have removed display name of " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setDisplayName(Utils.formatPlayerNameForDisplay(target.getUsername()));
					player.getPackets()
					.sendGameMessage("You have removed display name of " + target.getDisplayName() + ".");
					SerializableFilesManager.storeSerializableClass(target, acc1);
				}
				return true;

			case "penguin":
				SinkHoles.startEvent();
				return true;

			case "mypos":
			case "coords":
				player.getPackets()
				.sendPanelBoxMessage("Coords: " + player.getX() + ", " + player.getY() + ", "
						+ player.getZ() + ", regionId: " + player.getRegionId() + ", rx: "
						+ player.getChunkX() + ", ry: " + player.getChunkY() + ", xinc: " + player.getXInChunk() + ", yinc: " + player.getYInChunk());
				return true;
			case "x":
				Position tile2 = new Position(player.getX(), player.getY(), player.getZ());
				player.sm("Region X : "+player.getLocalX(tile2)+", RegionY : "+player.getLocalY(tile2)+"");
				return true;

			case "cooords1":
				Position tile1 = new Position(player.getX(), player.getY(), player.getZ());
				player.sm("Region X : "+tile1.getLocalX(tile1)+", RegionY : "+tile1.getLocalY(tile1)+"");
				return true;

			case "cooords":
				Position tile = new Position(player.getX(), player.getY(), player.getZ());
				int cX = tile.getChunkX();
				int cY = tile.getChunkY();
				player.sm("Local x: "+ player.getXInRegion() +", y, "+player.getYInRegion()+", id, "+player.getRegionId()+".");
				player.sendMessage("Chunk X: "+cX+", ChunkY: "+cY+"");
				return true;

			case "cutscene":
				player.getCutscenesManager().play("HomeCutScene");
				return true;

			case "copy":
				String name111111111111 = "";
				for (int i = 1; i < cmd.length; i++) {
					name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				Player p2 = World.getPlayerByDisplayName(name111111111111);
				if (p2 == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name111111111111 + ".");
					return true;
				}
				Item[] items = p2.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null) {
						continue;
					}
					HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0) {
								continue;
							}
							int level = requiriments.get(skillId);
							if (level < 0 || level > 120) {
								continue;
							}

						}
					}
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearence().generateAppearenceData();
				return true;

			case "trade":

				if (!player.isOwner()) {
					return false;
				}

				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++) {
					name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}

				target = World.getPlayerByDisplayName(name111111111111);
				if (target != null) {
					player.getTrade().openTrade(target);
					target.getTrade().openTrade(player);
				}
				return true;

			case "testpackage":
				RessourceBox.SpawnPackage();
				player.sm("package test");
				return true;

			case "resetpackage":
				RessourceBox.isBoxSpawned = false;
				RessourceBox.randomLocation = 0;
				player.sm("You succesfully reset the Ressource box.");
				return true;

			case "checkfire":
				player.sm("you have "+player.getFirePoints()+" charges for your dfs.");
				return true;

			case "addfire":
				player.canSpec = false;
				player.DFS += 100;
				return true;

			case "resetfire":
				player.canSpec = false;
				player.DFS = 0;
				return true;

			case "setlevel":

				if (cmd.length < 3) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 99) {
						player.getPackets().sendGameMessage("Please choose a valid level.");
						return true;
					}
					player.getSkills().set(skill, level);
					player.getSkills().setXp(skill, Skills.getXPForLevel(level));
					player.getAppearence().generateAppearenceData();
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
				}

				return true;

			case "npc":
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, 0, true, true);
				} catch (Throwable t) {
					t.printStackTrace();

				}
				return true;

			case "npcmass":
				if (player.isOwner()) {
					if (Integer.parseInt(cmd[2]) > 1000) {
						player.sm("You can't spawn over 1,000 npcs!");
						return false;
					}
					try {
						for (int i = 0; i < Integer.parseInt(cmd[2]); i++) {
							World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, 0, true, true);
						}
						return true;
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage(
								"Use: ::npc id amount");
					}
				}
				return true;

			case "object":
				try {
					int objectId = Integer.valueOf(cmd[1]);
					int objectType = cmd.length > 2 ? Integer.valueOf(cmd[2]) : 10;
					int rotation = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
					World.spawnObject(new WorldObject(objectId, objectType, rotation, player.getX(), 
							player.getY(), player.getZ()), objectId > 0 ? true : false);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return true;

			case "tab":
				try {
					player.getInterfaceManager().sendTab(Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: tab id inter");
				}
				return true;

			case "killme":
				player.applyHit(new Hit(player, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
				return true;

			case "1hp":
				player.applyHit(new Hit(player, 989, HitLook.REGULAR_DAMAGE));
				return true;

			case "setleveloplayer":
			case "setlevelother":

				String username144 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other1 = World.getPlayer(username144);
				if (other1 != null) {
					int skill = Integer.parseInt(cmd[2]);
					int level = Integer.parseInt(cmd[3]);
					other1.getSkills().set(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
					other1.getSkills().set(skill, level);
					other1.getSkills().setXp(skill, Skills.getXPForLevel(level));
					other1.getAppearence().generateAppearenceData();
					other1.getPackets().sendGameMessage("Your " + Skills.SKILL_NAME[skill] + " level has been set to " + level + " by " + player.getDisplayName() + ".");
					player.getPackets().sendGameMessage("You have set "+other1.getDisplayName()+"'s " + Skills.SKILL_NAME[skill] + " Level to: " + level + ".");

				}
				return true;

			case "maxplayer":
				String username143374 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other1fbgd44tr = World.getPlayer(username143374);
				if (other1fbgd44tr != null) {
					int skill = Integer.parseInt(cmd[2]);
					other1fbgd44tr.getSkills().set(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[2]));
					other1fbgd44tr.getSkills().setXp(skill, 200000000);
				}
				return true;

			case "maxplayer1":
				String username1443374 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other1fb5gd44tr = World.getPlayer(username1443374);
				if (other1fb5gd44tr != null) {
					int skill = Integer.parseInt(cmd[2]);
					// other1fb5gd44tr.getSkills().set(Integer.parseInt(cmd[2]));
					other1fb5gd44tr.getSkills().set(skill, 99);
					other1fb5gd44tr.getSkills().setXp(skill, 200000000);
					other1fb5gd44tr.getPackets().sendGameMessage("DONE." + skill);
				}
				return true;

			case "setprestige":
				if (!player.hasStaffPin) {
					player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin" });
				} else {
					String username1gggff5567744 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player otherfhfghfgghfg1 = World.getPlayer(username1gggff5567744);
					if (otherfhfghfgghfg1 != null) {
						int level = Integer.parseInt(cmd[2]);
						Integer.parseInt(cmd[2]);
						otherfhfghfgghfg1.SetprestigePoints(level);
						otherfhfghfgghfg1.getPackets().sendGameMessage("You are now prestige " + level + ".");
						player.getPackets().sendGameMessage("You have set their prestige to " + level + ".");
					}
				}
				return true;

			case "allvote":
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
					// players.getPackets().sendOpenURL("http://Zamron.net/vote/");
					players.getPackets().sendGameMessage("Vote! Vote Vote!");
				}
				return true;

			case "changepassother":
				name = cmd[1];
				File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPassword(cmd[2]);
				player.getPackets().sendGameMessage("You changed their password!");
				SerializableFilesManager.storeSerializableClass(target, acc1);
				return true;

			case "forcemovement":
				Position toTile = player.transform(0, 5, 0);
				player.setNextForceMovement(
						new ForceMovement(new Position(player), 1, toTile, 2, ForceMovement.NORTH));

				return true;

			case "hit":
				for (int i = 0; i < 5; i++) {
					player.applyHit(new Hit(player, Utils.getRandom(3), HitLook.HEALED_DAMAGE));
				}
				return true;

			case "objectanim":
				object = cmd.length == 4
				? World.getObject(
						new Position(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player.getZ()))
						: World.getObject(
								new Position(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player.getZ()),
								Integer.parseInt(cmd[3]));
						if (object == null) {
							player.getPackets().sendPanelBoxMessage("No object was found.");
							return true;
						}
						player.getPackets().sendObjectAnimation(object,
								new Animation(Integer.parseInt(cmd[cmd.length == 4 ? 3 : 4])));
						return true;

			case "unmuteall":
				for (Player targets : World.getPlayers()) {
					if (player == null) {
						continue;
					}
					targets.setMuted(0);
				}
				return true;

			case "bconfigloop":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						if (i >= 1929) {
							break;
						}
						player.getPackets().sendGlobalConfig(i, Integer.valueOf(cmd[3]));
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;

			case "resetmaster":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++) {
						player.getSkills().setXp(skill, 0);
					}
					player.getSkills().init();
					return true;
				}
				try {
					player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
					player.getSkills().set(Integer.valueOf(cmd[1]), 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;

			case "masterme":
			case "master":
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++) {
						player.getSkills().addXp(skill, 15000000);
					}
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]), 15000000);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;

			case "masterother":
				String usernamedfgddggd = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player otherfdsfs = World.getPlayerByDisplayName(usernamedfgddggd);
				if (otherfdsfs == null) {
					return true;
				}
				for (int skill = 0; skill < 25; skill++) {
					otherfdsfs.getSkills().addXp(skill, 150000000);
				}
				return true;

			case "mastercape":
				player.setCompletedFightCaves();
				player.setCompletedFightKiln();
				player.sm("You master your requirements.");
				return true;
				
			case "givecomp":
					String username22 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other33 = World.getPlayerByDisplayName(username22);
					if (other33 == null)
						return true;
		
					other33.setCompletedComp();
					other33.getPackets().sendGameMessage("You've been awarded the Completionists cape.");
					return true;
				
			case "window":
				player.getPackets().sendWindowsPane(1253, 0);
				return true;

			case "bconfig":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
				}
				return true;

			case "tonpc":
			case "pnpc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
				return true;

			case "inter":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "killwithin":
				List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
				for (index = 0; index < npcs.size() + 1; index++) {
					World.getNPCs().get(npcs.get(index)).sendDeath(player);
				}
				return true;

			case "itemid":
				String name55555 = "";
				for (int i = 1; i < cmd.length; i++) {
					name55555 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				ItemSearch.searchForItem(player, name55555);
				return true;

			case "overlay":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				int child = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 28;
				try {
					player.getPackets().sendInterface(true, 746, child, Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "setroll":
				String rollnumber = "";
				for (int i = 1; i < cmd.length; i++) {
					rollnumber += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				rollnumber = Utils.formatPlayerNameForDisplay(rollnumber);
				if (rollnumber.length() < 1 || rollnumber.length() > 2) {
					player.getPackets()
					.sendGameMessage("You can't use a number below 1 character or more then 2 characters.");
				}
				player.getPackets().sendGameMessage("Rolling...");
				player.setNextGraphics(new Graphics(2075));
				player.animate(new Animation(11900));
				player.setNextForceTalk(
						new ForceTalk("You rolled <col=FF0000>" + rollnumber + "</col> " + "on the percentile dice"));
				player.getPackets()
				.sendGameMessage("rolled <col=FF0000>" + rollnumber + "</col> " + "on the percentile dice");
				return true;

			case "empty":
				player.getInventory().reset();
				return true;

			case "interh":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentModel(interId, componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "inters":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "npcsay":
				String message = "";
				for (int i = 1; i < cmd.length; i++) {
					message += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk(message));
					}
				}
				return true;

			case "makesupport":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++) {
					name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn1 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null) {
						target1.setUsername(Utils.formatPlayerNameForProtocol(name111111111111));
					}
					loggedIn1 = false;
				}
				if (target1 == null) {
					return true;
				}
				target1.setSupporter(true);
				//target1.getAppearence().setTitle(789);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn1) {
					target1.getPackets().sendGameMessage("You have been given supporter rank by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				}
				player.getPackets().sendGameMessage(
						"You gave supporter rank to " + Utils.formatPlayerNameForDisplay(target1.getUsername()), true);

				return true;

				case "makeyoutber":
					name111111111111 = "";
					for (int i = 1; i < cmd.length; i++) {
						name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
					}
					target1 = World.getPlayerByDisplayName(name111111111111);
					boolean loggedIn11 = true;
					if (target1 == null) {
						target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
						if (target1 != null) {
							target1.setUsername(Utils.formatPlayerNameForProtocol(name111111111111));
						}
						loggedIn11 = false;
					}
					if (target1 == null) {
						return true;
					}
					target1.setYoutuber(true);
					//target1.getAppearence().setTitle(789);
					SerializableFilesManager.savePlayer(target1);
					if (loggedIn11) {
						target1.getPackets().sendGameMessage("You have been given youtuber rank by "
								+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
					}
					player.getPackets().sendGameMessage(
							"You gave youtuber rank to " + Utils.formatPlayerNameForDisplay(target1.getUsername()), true);

					return true;



			case "removesupport":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++) {
					name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn2 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null) {
						target1.setUsername(Utils.formatPlayerNameForProtocol(name111111111111));
					}
					loggedIn2 = false;
				}
				if (target1 == null) {
					return true;
				}
				target1.setSupporter(false);
				target1.getAppearence().setTitle(-1);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn2) {
					target1.getPackets().sendGameMessage("Your supporter rank was removed by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				}
				player.getPackets().sendGameMessage(
						"You removed supporter rank of " + Utils.formatPlayerNameForDisplay(target1.getUsername()),
						true);
				return true;


			case "demote":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++) {
					name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target1 = World.getPlayerByDisplayName(name111111111111);
				boolean loggedIn1115 = true;
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
					if (target1 != null) {
						target1.setUsername(Utils.formatPlayerNameForProtocol(name111111111111));
					}
					loggedIn1115 = false;
				}
				if (target1 == null) {
					return true;
				}
				target1.setRights(0);
				SerializableFilesManager.savePlayer(target1);
				if (loggedIn1115) {
					target1.getPackets().sendGameMessage(
							"You where demoted by " + Utils.formatPlayerNameForDisplay(player.getUsername()), true);
				}
				player.getPackets().sendGameMessage(
						"You demoted " + Utils.formatPlayerNameForDisplay(target1.getUsername()), true);
				return true;


			case "reloadfiles":
				IPBanL.init();
				PkRank.init();
				return true;

			case "tele":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextPosition(new Position(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getZ()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY plane");
				}

				return true;

			case "shutdown":
				int delay2 = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage("Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
				}
				World.safeShutdown(false, delay2);
				return true;

			case "emote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.animate(new Animation(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "remote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "quake":
				player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
						Integer.valueOf(cmd[3]), Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
				return true;

				case "droprate":
					World.DropRateBoost(player);
					return true;


			case "getrender":
				player.getPackets().sendGameMessage("Testing renders");
				for (int i = 0; i < 3000; i++) {
					try {
						player.getAppearence().setRenderEmote(i);
						player.getPackets().sendGameMessage("Testing " + i);
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return true;

				case "hweentest":
					for (int i = 0; i < 30000; i++) {
						player.getSkills().addXp(Skills.CRAFTING, 10);
						//player.getPackets().sendGameMessage("" +i+ "");
					}
					return true;

				case "raidstest":
					for (int i = 0; i < 10000; i++) {
						RaidsRewards.open(player);
					}
					return true;

				case "reapertest":
					for (int i = 0; i < 1000; i++) {
						ReaperChest.open(player);
					}
					return true;

				case "donationtest":
						player.promoreward+= 110;
					if (player.promoreward >= 500) {
						player.getInventory().addItem(6199, 1);
						player.getInventory().addItem(6199, 1);
						player.getInventory().addItem(6199, 1);
							player.promoreward-= 500;
					}
					System.out.println(player.promoreward);
					return true;

				case "seteasy":
					name111111111111 = "";
					for (int i = 1; i < cmd.length; i++) {
						name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
					}
					target1 = World.getPlayerByDisplayName(name111111111111);
					boolean loggedIn1111 = true;
					if (target1 == null) {
						target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
						if (target1 != null) {
							target1.setUsername(Utils.formatPlayerNameForProtocol(name111111111111));
						}
						loggedIn1111 = false;
					}
					if (target1 == null) {
						return true;
					}
					target1.setGameMode(GameMode.EASY);
					SerializableFilesManager.savePlayer(target1);
					if (loggedIn1111) {
						target1.getPackets().sendGameMessage("You gamemode has been changed to easy by "
								+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
					}
					player.getPackets().sendGameMessage(
							"You changed gamemode of " + Utils.formatPlayerNameForDisplay(target1.getUsername()), true);

					return true;


				case "setnormal":
					name111111111111 = "";
					for (int i = 1; i < cmd.length; i++) {
						name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
					}
					target1 = World.getPlayerByDisplayName(name111111111111);
					boolean loggedIn11111 = true;
					if (target1 == null) {
						target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
						if (target1 != null) {
							target1.setUsername(Utils.formatPlayerNameForProtocol(name111111111111));
						}
						loggedIn11111 = false;
					}
					if (target1 == null) {
						return true;
					}
					target1.setGameMode(GameMode.NORMAL);
					SerializableFilesManager.savePlayer(target1);
					if (loggedIn11111) {
						target1.getPackets().sendGameMessage("You gamemode has been changed to normal by "
								+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
					}
					player.getPackets().sendGameMessage(
							"You changed gamemode of " + Utils.formatPlayerNameForDisplay(target1.getUsername()), true);

					return true;

				case "sethardcore":
					name111111111111 = "";
					for (int i = 1; i < cmd.length; i++) {
						name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
					}
					target1 = World.getPlayerByDisplayName(name111111111111);
					boolean loggedIn111111 = true;
					if (target1 == null) {
						target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
						if (target1 != null) {
							target1.setUsername(Utils.formatPlayerNameForProtocol(name111111111111));
						}
						loggedIn111111 = false;
					}
					if (target1 == null) {
						return true;
					}
					target1.setGameMode(GameMode.HARDCORE_IRONMAN);
					SerializableFilesManager.savePlayer(target1);
					if (loggedIn111111) {
						target1.getPackets().sendGameMessage("You gamemode has been changed to normal by "
								+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
					}
					player.getPackets().sendGameMessage(
							"You changed gamemode of " + Utils.formatPlayerNameForDisplay(target1.getUsername()), true);

					return true;

			case "spec":
				player.getCombatDefinitions().resetSpecialAttack();
				return true;

				case "droptest":
					for (int i = 0; i < 10000; i++) {
						NPC npc = new NPC(Integer.valueOf(cmd[1]), player, -1, true);
						npc.sendDeath(player);
						npc.applyHit(new Hit(player, 40000, HitLook.REGULAR_DAMAGE));
						npc.setSpawned(true);
					}
				return true;
				case "untradetest":

						return true;

			case "tryinter":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1;

					@Override
					public void run() {
						if (player.isFinished()) {
							stop();
						}
						player.getInterfaceManager().sendInterface(i);
						System.out.println("Inter - " + i);
						i++;
					}
				}, 0, 1);
				return true;

			case "tryanim":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 16700;

					@Override
					public void run() {
						if (i >= Utils.getAnimationDefinitionsSize()) {
							stop();
							return;
						}
						if (player.getLastAnimationEnd() > System.currentTimeMillis()) {
							player.animate(new Animation(-1));
						}
						if (player.isFinished()) {
							stop();
						}
						player.animate(new Animation(i));
						System.out.println("Anim - " + i);
						i++;
					}
				}, 0, 3);
				return true;

			case "animcount":
				System.out.println(Utils.getAnimationDefinitionsSize() + " anims.");
				return true;

			case "trygfx":
				WorldTasksManager.schedule(new WorldTask() {
					int i = 1500;

					@Override
					public void run() {
						if (i >= Utils.getGraphicDefinitionsSize()) {
							stop();
						}
						if (player.isFinished()) {
							stop();
						}
						player.setNextGraphics(new Graphics(i));
						System.out.println("GFX - " + i);
						i++;
					}
				}, 0, 3);
				return true;

			case "gfx":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true;

			case "sync":
				int animId = Integer.parseInt(cmd[1]);
				int gfxId = Integer.parseInt(cmd[2]);
				int height = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
				player.animate(new Animation(animId));
				player.setNextGraphics(new Graphics(gfxId, 0, height));
				return true;

			case "mess":
				player.getPackets().sendMessage(Integer.valueOf(cmd[1]), "", player);
				return true;

			case "staffmeeting":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0) {
						continue;
					}
					staff.setNextPosition(new Position(2675, 10418, 0));
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "fightkiln":
				FightKiln.enterFightKiln(player, true);
				return true;

			case "setpitswinner":
				name111111111111 = "";
				for (int i = 1; i < cmd.length; i++) {
					name111111111111 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target1 = World.getPlayerByDisplayName(name111111111111);
				if (target1 == null) {
					target1 = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name111111111111));
				}
				if (target1 != null) {
					target1.setWonFightPits();
					target1.setCompletedFightCaves();
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name111111111111 + ".");
				}
				SerializableFilesManager.savePlayer(target1);
				return true;
			}
		}
		return false;
	}

	public static boolean processAdminCommand(final Player player, String[] cmd, boolean console,
			boolean clientCommand) {
		if (clientCommand) {
			switch (cmd[0]) {
			case "tele":
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextPosition(new Position(x, y, plane));
				return true;
			}
		} else {
			String name;
			Player target;
			WorldObject object;
			Player target1;
			switch (cmd[0]) {

			case "achieve":
				player.getInterfaceManager().sendAchievementInterface();
				return true;

			case "poison":
				player.getToxin().applyToxin(ToxinType.POISON);
				break;
				
			case "envenom":
				player.getToxin().applyToxin(ToxinType.VENOM);
				break;

			case "telesupport":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter()) {
						continue;
					}
					staff.setNextPosition(player);
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "telemods":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() != 1) {
						continue;
					}
					staff.setNextPosition(player);
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "god":
				player.setHitpoints(Short.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(Short.MAX_VALUE - 990);
				for (int i = 0; i < 10; i++) {
					player.getCombatDefinitions().getBonuses()[i] = 100000;
				}
				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++) {
					player.getCombatDefinitions().getBonuses()[i] = 100000;
				}
				return true;

			case "itemn":
				StringBuilder sb1 = new StringBuilder(cmd[1]);
				int amount1 = 1;
				if (cmd.length > 2) {
					for (int i = 2; i < cmd.length; i++) {
						if (cmd[i].startsWith("+")) {
							amount1 = Integer.parseInt(cmd[i].replace("+", ""));
						} else {
							sb1.append(" ").append(cmd[i]);
						}
					}
				}
				String name11 = sb1.toString().toLowerCase().replace("[", "(").replace("]", ")").replaceAll(",", "'");
				for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
					ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
					if (def.getName().toLowerCase().equalsIgnoreCase(name11)) {
						player.getInventory().addItem(i, amount1);
						player.stopAll();
						player.getPackets().sendGameMessage("Found item " + name11 + " - id: " + i + ".");
					}
				}
				player.getPackets().sendGameMessage("Could not find item by the name " + name11 + ".");
				return true;

			case "createcit":
				Citadel.createTestCitadel(player);
				return true;
			case "joincit":
				if (String.valueOf(cmd[1]).equals(player.getUsername())) {
					return true;
				}
				player.getControlerManager().startControler("visitor", World.getPlayer(String.valueOf(cmd[1])));
				return true;

			case "npc":

				if (!player.hasStaffPin) {
					player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin" });
				} else {
					try {

						World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, 0, true, true);
						BufferedWriter bw = new BufferedWriter(new FileWriter("./data/npcs/spawns.txt", true));
						bw.write("//" + NPCDefinitions.getNPCDefinitions(Integer.parseInt(cmd[1])).name + " spawned by "
								+ player.getUsername());
						bw.newLine();
						bw.write(Integer.parseInt(cmd[1]) + " - " + player.getX() + " " + player.getY() + " "
								+ player.getZ());
						bw.flush();
						bw.newLine();
						bw.close();


					} catch (Throwable t) {
						t.printStackTrace();
					}
				}

				return true;

			case "telestaff":
				for (Player staff : World.getPlayers()) {
					if (!staff.isSupporter() && staff.getRights() != 1) {
						continue;
					}
					staff.setNextPosition(player);
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			case "kill":
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					return true;
				}
				other.applyHit(new Hit(other, 32767, HitLook.REGULAR_DAMAGE));
				other.stopAll();
				return true;

			case "restartfp":
				FightPits.endGame();
				player.getPackets().sendGameMessage("Fight pits restarted!");
				return true;

			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else {
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage("Unable to teleport a developer to you.");
						return true;
					}
					target.setNextPosition(player);
				}
				return true;

			case "pos":
				try {
					File file = new File("data/positions.txt");
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					writer.write("|| player.getX() == " + player.getX() + " && player.getY() == " + player.getY() + "");
					writer.newLine();
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case "mypos":
			case "coords":
				player.getPackets()
				.sendPanelBoxMessage("Coords: " + player.getX() + ", " + player.getY() + ", "
						+ player.getZ() + ", regionId: " + player.getRegionId() + ", rx: "
						+ player.getChunkX() + ", ry: " + player.getChunkY());
				return true;
				case "mapfiles":

					int regionId = player.getRegionId();
					int regionX = (regionId >> 8) * 64;
					int regionY = (regionId & 0xff) * 64;
					int mapArchiveId = Cache.STORE.getIndexes()[5].getArchiveId("m" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
					int landscapeArchiveId = Cache.STORE.getIndexes()[5].getArchiveId("l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));

					System.out.println("RegionId: "+cmd[1]);
					System.out.println("landArchive: "+landscapeArchiveId);
					System.out.println("mapArchive: "+mapArchiveId);
					return false;


			case "tab":
				try {
					player.getInterfaceManager().sendTab(Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: tab id inter");
				}
				return true;

			case "killme":
				player.applyHit(new Hit(player, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
				return true;

			case "1hp":
				player.applyHit(new Hit(player, 989, HitLook.REGULAR_DAMAGE));
				return true;

			case "allvote":
				for (Player players : World.getPlayers()) {
					if (players == null) {
						continue;
					}
				}
				return true;

			case "unmuteall":
				for (Player targets : World.getPlayers()) {
					if (player == null) {
						continue;
					}
					targets.setMuted(0);
				}
				return true;

			case "tonpc":
			case "pnpc":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
				return true;

			case "item":
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
					return true;
				}
				try {
					int itemId = Integer.valueOf(cmd[1]);
					player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
					player.stopAll();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
				}
				return true;

			case "inter":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "killwithin":
				List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
				for (int index = 0; index < npcs.size() + 1; index++) {
					World.getNPCs().get(npcs.get(index)).sendDeath(player);
				}
				return true;

			case "itemid":
				String name55555 = "";
				for (int i = 1; i < cmd.length; i++) {
					name55555 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				ItemSearch.searchForItem(player, name55555);
				return true;

			case "empty":
				player.getInventory().reset();
				return true;

			case "npcsay":
				String message = "";
				for (int i = 1; i < cmd.length; i++) {
					message += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				for (NPC n : World.getNPCs()) {
					if (n != null && Utils.getDistance(player, n) < 9) {
						n.setNextForceTalk(new ForceTalk(message));
					}
				}
				return true;

				/*
				 * case "bank": if (player.isLocked() ||
				 * player.getControlerManager().getControler() != null) {
				 * player.getPackets().
				 * sendGameMessage("You can't use any commands right now!"); return
				 * true;
				 *
				 * }else { player.getBank().openBank(); return true; }
				 */
			case "bank":
				player.getBank().openBank();
				return true;



			case "tele":
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextPosition(new Position(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getZ()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY plane");
				}

				return true;

			case "emote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.animate(new Animation(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "remote":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;

			case "gfx":
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]), 0, 0));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true;

			case "staffmeeting":
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0) {
						continue;
					}
					staff.setNextPosition(new Position(2675, 10418, 0));
					staff.getPackets()
					.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
				return true;

			}
		}
		return false;
	}

	public static boolean processHeadModCommands(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String name;
			Player target;

			switch (cmd[0]) {

			case "bann":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn11111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2) {
						return true;
					}
					player.getPackets().sendGameMessage(
							"You've permanently bannished " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
					World.getBotanyBay().trialBot(target, 0);
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;

			case "checkinvy": {
				if (!player.hasStaffPin) {
					player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin" });
				} else {
					name = "";
					for (int i = 1; i < cmd.length; i++) {
						name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
					}
					Player target1 = World.getPlayerByDisplayName(name);
					try {
						player.getInterfaceManager().sendInventoryInterface(670);
						player.getPackets().sendItems(93, target1.getInventory().getItems());
					} catch (Exception e) {
					}
				}
			}
			return true;

			case "unban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: " + target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "resettaskother":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				player.sm("You have reset the task of " + target + ".");
				target.getSlayerManager().skipCurrentTask();
				return true;

			case "joinhouse":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				House.enterHouse(player, name);
				return true;
				case "houset":
					player.getHouse().enterMyHouse();
					return true;
			case "permban":
			case "ban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
					player.getPackets().sendGameMessage("<col=FF0000>Unable to Ban a Co-Owner/Owner.");

					return true;
				}
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: " + target.getDisplayName() + ".");
					Player.printLog(player, name);
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					Player.printLog(player, name);
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "packshops":
				ShopsHandler.loadUnpackedShops();
				player.sm("You have succesfully packed the shops!");
				return true;

			case "packnpcs":
				NPCBonuses.loadUnpackedNPCBonuses();
				NPCCombatDefinitionsL.loadUnpackedNPCCombatDefinitions();
				player.sm("You have succesfully packed the NPC bonuses & definitions!");
				return true;

			case "packspawns":
				NPCSpawns.packNPCSpawns();
				player.sm("You have succesfully packed the spawns!!");
				return true;

			case "tradeban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1115111 = true;
				if (target == null) {
					return true;
				}
				if (target.getUsername() == player.getUsername()) {
					player.sendMessage("<col=FF0000>You can't trade lock yourself!");
					return true;
				}
				target.setTradeLock();
				SerializableFilesManager.savePlayer(target);
				player.getPackets().sendGameMessage("" + target.getDisplayName() + "'s trade status is now "
						+ (target.isTradeLocked() ? "locked" : "unlocked") + ".", true);
				if (loggedIn1115111) {
					target.getPackets().sendGameMessage("Your trade status has been set to: "
							+ (target.isTradeLocked() ? "locked" : "unlocked") + ".", true);
				}
				return true;



			case "ipmute":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn111110 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn111110 = false;
				}
				if (target != null) {
					if (target.getRights() == 2) {
						return true;
					}
					IPMute.mute(target, loggedIn111110);
					player.getPackets().sendGameMessage(
							"You've permanently ipMuted " + (loggedIn111110 ? target.getDisplayName() : name) + ".");
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;



			case "unipmute":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111101 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn1111101 = false;
				}
				if (target != null) {
					if (target.getRights() == 2) {
						return true;
					}
					IPMute.unmute(target);
					player.getPackets().sendGameMessage(
							"You've permanently ipMuted " + (loggedIn1111101 ? target.getDisplayName() : name) + ".");
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;

			case "botany":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn1111111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2) {
						return true;
					}
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned " + (loggedIn1111111 ? target.getDisplayName() : name) + ".");
					World.getBotanyBay().trialBot(target, 0);
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;

			case "unipban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.getPackets().sendGameMessage(
						"You've unipbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
				SerializableFilesManager.storeSerializableClass(target, acc11);
				return true;

			case "checkinv":
				NumberFormat nf = NumberFormat.getInstance(Locale.US);
				String amount;
				Player player2 = World.getPlayer(cmd[1]);

				int player2freeslots = player2.getInventory().getFreeSlots();
				int player2usedslots = 28 - player2freeslots;

				player.getPackets().sendGameMessage("----- Inventory Information -----");
				player.getPackets()
				.sendGameMessage("<col=DF7401>" + Utils.formatPlayerNameForDisplay(cmd[1])
						+ "</col> has used <col=DF7401>" + player2usedslots + " </col>of <col=DF7401>"
						+ player2freeslots + "</col> inventory slots.");
				player.getPackets().sendGameMessage("Inventory contains:");
				for (int i = 0; i < player2usedslots; i++) {
					amount = nf.format(player2.getInventory().getItems()
							.getNumberOf(player2.getInventory().getItems().get(i).getId()));
					player.getPackets()
					.sendGameMessage("<col=088A08>" + amount + "</col><col=BDBDBD> x </col><col=088A08>"
							+ player2.getInventory().getItems().get(i).getName());

					player.getPackets().sendGameMessage("--------------------------------");
				}
				return true;

			case "checkbank": {

				String name1 = "";
				for (int i = 1; i < cmd.length; i++) {
					name1 += cmd[i] + (i == cmd.length - 1 ? "" : " ");

					Player Other = World.getPlayerByDisplayName(name1);
					try {
						player.getPackets().sendItems(95, Other.getBank().getContainerCopy());
						player.getBank().openPlayerBank(Other);
					} catch (Exception e) {
					}
				}
			}
			return true;

			case "ipban":

				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				Player.ipbans(player, name);
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn1111 = false;

					if (target != null) {
						if (target.getRights() == 2) {
							return true;
						}
						IPBanL.ban(target, loggedIn1111);
						player.getPackets().sendGameMessage(
								"You've permanently ipbanned " + (loggedIn1111 ? target.getDisplayName() : name) + ".");
					} else {
						player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
					}
					return true;
				}
			}
		}
		return false;
	}

	public static boolean processModCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			switch (cmd[0]) {
			case "unmute":
				String name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				Player target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.getPackets().sendGameMessage(
							"You've been unmuted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unmuted: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.getPackets()
					.sendGameMessage("You have unmuted: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.storeSerializableClass(target, acc1);
				}
				return true;



			case "banhammer":
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					return true;
				}
				Magic.sendTrialTeleportSpell(other, 0, 0.0D, new Position(3680, 3616, 0), new int[0]);
				other.stopAll();
				other.lock();
				return true;

//			case "death1":
//				String username1 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				Player other1 = World.getPlayerByDisplayName(username1);
//				if (other1 == null) {
//					return true;
//				}
//				other1.animate(new Animation(17532));
//				other1.setNextGraphics(new Graphics(3397));
//				other1.stopAll();
//				other1.applyHit(new Hit(other1, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
//				other1.stopAll();
//				other1.unlock();
//				return true;
//
//			case "death2":
//				String username11 = cmd[1].substring(cmd[1].indexOf(" ") + 1);
//				Player other11 = World.getPlayerByDisplayName(username11);
//				if (other11 == null) {
//					return true;
//				}
//				other11.animate(new Animation(17523));
//				other11.setNextGraphics(new Graphics(3396));
//				other11.stopAll();
//				other11.applyHit(new Hit(other11, player.getHitpoints(), HitLook.REGULAR_DAMAGE));
//				other11.stopAll();
//				other11.unlock();
//				return true;

			case "ipban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn1111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					Player.printLog(player, name);
					if (target != null) {
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					}
					loggedIn1111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2) {
						return true;
					}
					IPBanL.ban(target, loggedIn1111);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned " + (loggedIn1111 ? target.getDisplayName() : name) + ".");
					Player.printLog(player, name);
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
				return true;

			case "unipban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.getPackets().sendGameMessage(
						"You've unipbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
				SerializableFilesManager.storeSerializableClass(target, acc11);
				return true;

			case "permban":
			case "ban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
					player.getPackets().sendGameMessage("<col=FF0000>Unable to Ban a Co-Owner/Owner.");


					return true;
				}
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: " + target.getDisplayName() + ".");
					Player.printLog(player, name);
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					Player.printLog(player, name);
					SerializableFilesManager.savePlayer(target);
				}
				return true;


			case "jail":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
					player.getPackets().sendGameMessage("<col=FF0000>Unable to jail a Co-Owner/Owner.");
					player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
					return true;
				}
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000);
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage("You've been Jailed for 24 hours by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have Jailed 24 hours: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000);
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.storeSerializableClass(target, acc1);
				}
				return true;


			case "kick":
			case "boot":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(name) + " is not logged in.");
					return true;
				}
				if (target.getRights() == 7) {
					player.getPackets().sendGameMessage("<col=FF0000>Unable to kick a Co-Owner/Owner.");
					return true;
				}
				target.getSession().getChannel().close();
				player.getPackets().sendGameMessage("You have kicked: " + target.getDisplayName() + ".");
				return true;

			case "staffyell":
				String message222 = "";
				for (int i = 1; i < cmd.length; i++) {
					message222 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				sendYell(player, Utils.fixChatMessage(message222));
				return true;

			case "hide":
				if (player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot hide in a public event!");
					return true;
				}

				player.getAppearence().switchHidden();
				player.getPackets().sendGameMessage("Hidden? " + player.getAppearence().isHidden());
				return true;
				case "testoverlay":
					player.getInterfaceManager().sendTab(238, 1099);
					return true;
				case "testcs":
					int startPercentage = (int) ((((double) player.getHitpoints() / (double) player.getMaxHitpoints())) * 125.00);


					int endPercentage =  (int) ((((double) (player.getHitpoints() - 20 ) / (double) player.getMaxHitpoints())) * 125.00);
					player.getPackets().sendRunScript(5469, 14, startPercentage);
					player.getPackets().sendRunScript(5468, 14, startPercentage);
					player.getPackets().sendIComponentText(1099, 11, player.getHitpoints()  + "/" + player.getMaxHitpoints());

					player.getPackets().sendRunScript(5469, 14, endPercentage -1);
					return true;
			case "unjail":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unjailed: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();                                                                                        					}
					target.setJailed(0);
					player.getPackets()
					.sendGameMessage("You have unjailed: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.storeSerializableClass(target, acc1);
				}
				return true;

			case "teleto":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else {
					player.setNextPosition(target);
				}
				return true;

			case "teletome":

				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else {
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage("Unable to teleport a developer to you.");
						return true;
					}
					target.setNextPosition(player);
				}
				return true;

			case "ticket":
				TicketSystem.answerTicket(player);
				return true;

			case "finishticket":
				TicketSystem.removeTicket(player);
				return true;

			case "swapbook":
				player.getDialogueManager().startDialogue("SwapSpellBook");
				return true;

			case "unnull":
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else {
					target.unlock();
					target.getControlerManager().forceStop();
					if (target.getNextPosition() == null) { // if controler
						// wont tele the
						// player
						target.setNextPosition(Constants.HOME_PLAYER_LOCATION1);
					}
					player.getPackets().sendGameMessage("You have unnulled: " + target.getDisplayName() + ".");
					return true;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean processSupportCommands(Player player, String[] cmd, boolean console, boolean clientCommand) {
		String name;
		Player target;
		if (clientCommand) {

		} else {
			switch (cmd[0]) {

			case "teleto":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else {
					player.setNextPosition(target);
				}
				return true;
			case "hide":
				if (player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot hide in a public event!");
					return true;
				}
				if (player.isManager()) {
					player.getAppearence().switchHidden();
				}
				player.getPackets().sendGameMessage("Hidden? " + player.getAppearence().isHidden());
				return true;

			case "unjail":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unjailed: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.getPackets()
					.sendGameMessage("You have unjailed: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.storeSerializableClass(target, acc1);
				}
				return true;

			case "unmute":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.getPackets().sendGameMessage(
							"You've been unmuted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unmuted: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.getPackets()
					.sendGameMessage("You have unmuted: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.storeSerializableClass(target, acc1);
				}
//				return true;
//			case "ban":
//				name = "";
//				for (int i = 1; i < cmd.length; i++) {
//					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
//				}
//				target = World.getPlayerByDisplayName(name);
//				if (target.getRights() == 7) {
//					player.getPackets().sendGameMessage("<col=FF0000>Unable to Ban a Co-Owner/Owner.");
//					return true;
//				}
//				if (target != null) {
//					target.setBanned(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
//					target.getSession().getChannel().close();
//					player.getPackets().sendGameMessage("You have banned 48 hours: " + target.getDisplayName() + ".");
//					Player.printLog(player, name);
//				} else {
//					name = Utils.formatPlayerNameForProtocol(name);
//					if (!SerializableFilesManager.containsPlayer(name)) {
//						player.getPackets().sendGameMessage(
//								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
//						return true;
//					}
//					target = SerializableFilesManager.loadPlayer(name);
//					target.setUsername(name);
//					target.setBanned(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
//					player.getPackets().sendGameMessage(
//							"You have banned 48 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
//					Player.printLog(player, name);
//					SerializableFilesManager.savePlayer(target);
//				}
//				return true;

			case "jail":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
					player.getPackets().sendGameMessage("<col=FF0000>Unable to jail a Co-Owner/Owner.");
					player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
					return true;
				}
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000);
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage("You've been Jailed for 24 hours by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have Jailed 24 hours: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis() + 24 * 60 * 60 * 1000);
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.storeSerializableClass(target, acc1);
				}
				return true;


			case "kick":
			case "boot":
			case "forcekick":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);

				if (target == null) {
					player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(name) + " is not logged in.");
					return true;
				}

				if (target.getRights() == 7) {
					player.getPackets().sendGameMessage("<col=FF0000>Unable to kick a Co-Owner/Owner.");
					player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
					return true;
				}


				target.forceLogout();
				player.getPackets().sendGameMessage("You have kicked: " + target.getDisplayName() + ".");

				return true;

			case "unban":
			case "unpermban":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: " + target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					IPBanL.unban(target);
					player.getPackets().sendGameMessage("You have unbanned: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "unnull":
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else {
					target.unlock();
					target.getControlerManager().forceStop();
					if (target.getNextPosition() == null) {// if controler wont
						// tele the player
						int i;
						i = 0;
						target.setNextPosition(Constants.HOME_PLAYER_LOCATION1);
					}
					player.getPackets().sendGameMessage("You have unnulled: " + target.getDisplayName() + ".");
					return true;
				}
				return true;

			case "staffyell":
				String message = "";
				for (int i = 1; i < cmd.length; i++) {
					message += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				sendYell(player, Utils.fixChatMessage(message));
				return true;

			case "ticket":
				TicketSystem.answerTicket(player);
				return true;

			case "Warn":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					return true;
				}
				target.blackMark++;
				player.warnLog(player, name);
				player.getPackets().sendGameMessage("You have warned " + target.getDisplayName() + ". They now have "
						+ target.blackMark + " black marks.");
				target.getPackets().sendGameMessage("You have received a black mark from " + player.getDisplayName()
						+ ". You now have " + target.blackMark + " black marks.");
				target.setNextForceTalk(
						new ForceTalk("I have been warned. I am now on " + target.blackMark + " black marks."));
				if (target.blackMark >= 3) {
					player.setNextForceTalk(new ForceTalk(
							target.getDisplayName() + " has been warned 3 times and has been muted for 48 hours."));
					player.getPackets().sendGameMessage(
							"You have warned: " + target.getDisplayName() + " they are now on: " + target.blackMark);
					target.setMuted(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
					target.getSession().getChannel().close();
				}
				if (target.blackMark >= 6) {
					player.setNextForceTalk(new ForceTalk(
							target.getDisplayName() + " has been warned 3 times and has been banned for 48 hours."));
					player.getPackets().sendGameMessage(
							"You have warned: " + target.getDisplayName() + " they are now on: " + target.blackMark);
					target.setBanned(Utils.currentTimeMillis() + 48 * 60 * 60 * 1000);
					target.getSession().getChannel().close();
				}
				return true;

//			case "testprayer":
//				//player.getPrayer().refresh();
//				player.getPrayer().switchPrayer(17);
//				player.sm("lsls");
//				return true;
//
//			case "spin":
//				player.getTemporaryAttributtes().put(MysteryBox.MYSTERY_SELECTED, 6831);
//				player.getMysteryBoxes().openBox();
//				return true;

			case "takemarks":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					return true;
				}
				if (target.blackMark == 0) {
					player.out("You cannot go into negative numbers.");
					return true;
				}
				target.blackMark--;
				target.getPackets().sendGameMessage("You now have " + player.blackMark + " black marks.");
				player.getPackets().sendGameMessage("You remove a black mark from " + target.getDisplayName()
						+ ". They are now on " + target.blackMark + " black marks.");

			case "finishticket":
				TicketSystem.removeTicket(player);
				return true;

			case "mute":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				target = World.getPlayerByDisplayName(name);
				if (target.getRights() == 7) {
					player.getPackets().sendGameMessage("<col=FF0000>Unable to mute a Co-Owner/Owner.");
					player.getTemporaryAttributtes().put("ban_player", Boolean.FALSE);
					return true;
				}
				Player.mutes(player, name);
				if (target != null) {
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? 48 * 60 * 60 * 1000 : 1 * 60 * 60 * 1000));
					target.getPackets()
					.sendGameMessage("You've been muted for "
							+ (player.getRights() >= 1 ? " 48 hours by " : "2 days by ")
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "2 days by by ")
							+ target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? 48 * 60 * 60 * 1000 : 1 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ")
							+ target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;
			}
		}
		return false;
	}

	public static void sendYell(Player player, String message) {
		for (Player p2 : World.getPlayers()) {
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;

			}
			if (p2.hasToggledYellMsg()) {
				return;
			}
			if (player.getUsername().equalsIgnoreCase("corey") || player.getUsername().equalsIgnoreCase("justice")) {
				p2.getPackets().sendGameMessage("<img=1><col=8b0000>[Developer/Owner] :</col></shad> " + ""
						+ player.getDisplayName() + ": " + message + "</col>");
			} else if (player.getUsername().equalsIgnoreCase("")) {
				p2.getPackets().sendGameMessage("<col=962ded><img=12>[Community Manager] :</col></shad> " + ""
						+ player.getDisplayName() + ": " + message + "</col>");
			} else if (player.getRights() == 2) {
				p2.getPackets().sendGameMessage("<img=1><col=FFDF00>[Admin] :</col></shad> "
						+ "" + player.getDisplayName() + ": <col=FFDF00>" + message + "</col>");
			} else if (player.isYoutuber()) {
				p2.getPackets().sendGameMessage("<img=26><col=FF0000>[YouTuber] :</col></shad> "
						+ "" + player.getDisplayName() + ": " + message + "</col>");
			} else if (player.getRights() == 1) {
				p2.getPackets().sendGameMessage("<img=0><col=A9A9A9>[Moderator] :</col></shad> "
						+ "" + player.getDisplayName() + ": <col=FFDF00>" + message + "</col>");
			} else if (player.isSupporter()) {
				p2.getPackets().sendGameMessage("<img=13><col=001377>[Support] :</col></shad> "
						+ "" + player.getDisplayName() + ": " + message + "</col>");
			} else if (player.getDonationManager().hasRank(DonatorRanks.BRONZE)) {
				p2.getPackets().sendGameMessage("<img=" + player.getDonationManager().getRank().getCrown() + "><col=" + player.getDonationManager().getRank().getColoring() + ">" + "[" +
						player.getDonationManager().getRank().getName() + "] :</col></shad> "
						+ "" + player.getDisplayName() + ": <col=" + player.getDonationManager().getRank().getColoring() + ">" + message + "</col>");

			} else {
				return;
			}

		}
	}

	public static boolean processNormalCommand(final Player player, final String[] cmd, boolean console,
			boolean clientCommand) {
		if (clientCommand) {

		} else {
			String message;
			String message1;
			String name;
			switch (cmd[0]) {

//			case "answerpm":
//				Long user = Bot.getIdByName(player.getDiscordPMs(), cmd[1]);
//				
//				if (user != 1L) {
//					StringBuilder builder = new StringBuilder();
//					for (String string : cmd) {
//						if (string == cmd[0] || string == cmd[1]) {
//							continue;
//						} else {
//							builder.append(string);
//							builder.append(" ");
//						}
//					}
//					Bot.sendPrivateMessage(builder.toString(), user);
//					player.sm("Sent !");
//				} else {
//					player.sm("Error in attempt to pm.");
//				}
//				return true;

//			case "scpm":
//				Long user2 = Bot.getIdByName(player.getDiscordPMs(), cmd[1]);
//				
//				if (user2 != 1L) {
//					StringBuilder builder = new StringBuilder();
//					for (String string : cmd) {
//						if (string == cmd[0] || string == cmd[1]) {
//							continue;
//						} else {
//							builder.append(string);
//							builder.append(" ");
//						}
//					}
//					Bot.sendPrivateMessageWithMedia(builder.toString(), user2, player);
//					player.sm("Sent !");
//				} else {
//					player.sm("Error in attempt to pm.");
//				}
//				return true;

			case "togglelogins":
				player.setToggleLoginNotifications(player.hasToggledLoginNotifications() ? false : true);
				player.getPackets().sendGameMessage("You have <col=8B0000>"+(player.hasToggledLoginNotifications() ? "DISABLED" : "ENABLED")+"</col> staff login notifications!");
				return true;

			case "togglehovers":
				player.hasMessageHovers = !player.hasMessageHovers;
				player.sendMessage("Hover messages are now " + (player.hasMessageHovers ? "enabled" : "disabled") + ".");
				return true;

				case "collections":
					player.getItemCollectionManager().openInterface(ItemCollectionManager.Category.BOSSES);
					return true;

				case "superiorboss":
					if (player.UnlockedSuperiorBosses == 1) {
						Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2203, 4263, 1));
					} else {
						player.sm("You don't have boss superiors unlocked");
					}
					return true;

				case "events":
						Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2857, 2585, 0));
					return true;

			case "dailytask":

				/**
				 * Checks day before opening inter
				 */

				if (Player.isMonday()) {

					player.getInterfaceManager().sendInterface(275);
					if (player.BandosKilled >= 20 && player.SharksFished >= 100 && player.CKeysused >= 3) {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Monday");
						player.getPackets().sendIComponentText(275, 11, "You have already finished the daily task!");
						player.getPackets().sendIComponentText(275, 12, "Please come back tomorrow for a new task!");
					} else {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Monday");
						player.getPackets().sendIComponentText(275, 11, "Today is <col=8B0000>Monday</col>.");
						if (player.BandosKilled >= 20 && player.SharksFished <= 99 && player.CKeysused <= 2) {
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Bandos boss "+player.BandosKilled+"/20 times.");
							player.getPackets().sendIComponentText(275, 13, "I must fish "+player.SharksFished+"/100 Sharks.");
							player.getPackets().sendIComponentText(275, 14, "I must use "+player.CKeysused+"/3 Crystal keys.");
						} else {
							player.getPackets().sendIComponentText(275, 12, "I must kill the Bandos boss "+player.BandosKilled+"/20 times.");
						}
						if (player.BandosKilled <= 19 && player.SharksFished >= 100 && player.CKeysused <= 2) {
							player.getPackets().sendIComponentText(275, 14, "I must kill the Bandos boss "+player.BandosKilled+"/20 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must fish "+player.SharksFished+"/100 Sharks.");
							player.getPackets().sendIComponentText(275, 14, "I must use "+player.CKeysused+"/3 Crystal keys.");
						} else {
							player.getPackets().sendIComponentText(275, 13, "I must finish "+player.SharksFished+"/100 Sharks.");
						}
						if (player.BandosKilled  <= 19 && player.SharksFished <= 99 && player.CKeysused >= 3){
							player.getPackets().sendIComponentText(275, 12, "I must kill the Bandos boss "+player.BandosKilled+"/20 times.");
							player.getPackets().sendIComponentText(275, 13, "I must fish "+player.SharksFished+"/100 Sharks.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must use "+player.CKeysused+"/3 Crystal keys.");
						} else if (player.BandosKilled >= 20 && player.SharksFished <= 99 && player.CKeysused >= 3){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Bandos boss "+player.BandosKilled+"/20 times.");
							player.getPackets().sendIComponentText(275, 13, "I must fish "+player.SharksFished+"/100 Sharks.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must use "+player.CKeysused+"/3 Crystal keys.");
						} else if (player.BandosKilled >= 20 && player.SharksFished >= 100 && player.CKeysused <= 2){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Bandos boss "+player.BandosKilled+"/20 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must fish "+player.SharksFished+"/100 Sharks.");
							player.getPackets().sendIComponentText(275, 14, "I must use "+player.CKeysused+"/3 Crystal keys.");
						} else if (player.BandosKilled <= 19 && player.SharksFished >= 100 && player.CKeysused >= 3){
							player.getPackets().sendIComponentText(275, 12, "I must kill the Bandos boss "+player.BandosKilled+"/20 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must fish "+player.SharksFished+"/100 Sharks.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must use "+player.CKeysused+"/3 Crystal keys.");
						} else {
							player.getPackets().sendIComponentText(275, 14, "I must use "+player.CKeysused+"/3 Crystal keys.");
						}

						player.getPackets().sendIComponentText(275, 16, "<col=8B0000>You will be rewarded the following :");
						player.getPackets().sendIComponentText(275, 17, "100 Skill Points & 200 Ruby Bolts (E) + 2 Spins");
					}
				}
				if (Player.isThursday()) {

					player.getInterfaceManager().sendInterface(275);
					if (player.Corpskilled >= 15 && player.TasksFinished >= 5 && player.AddToWell >= 5) {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Thursday");
						player.getPackets().sendIComponentText(275, 11, "You have already finished the daily task!");
						player.getPackets().sendIComponentText(275, 12, "Please come back tomorrow for a new task!");
					} else {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Thursday");
						player.getPackets().sendIComponentText(275, 11, "Today is <col=8B0000>Thursday</col>.");
						if (player.getCorpsKilled() >= 15 && player.getTasksFinished() <= 4 && player.getAddedWell() <= 4) {
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Corpreal beast "+player.getCorpsKilled()+"/15 times.");
							player.getPackets().sendIComponentText(275, 13, "I must finish "+player.getTasksFinished()+"/5 Slayer tasks.");
							player.getPackets().sendIComponentText(275, 14, "I must have contributed 5M to the XP Well.");
						} else {
							player.getPackets().sendIComponentText(275, 12, "I must kill the Corpreal beast "+player.getCorpsKilled()+"/15 times.");
						}
						if (player.getTasksFinished() >= 5 && player.getCorpsKilled() <= 14 && player.getAddedWell() <= 4) {
							player.getPackets().sendIComponentText(275, 14, "I must kill the Corpreal beast "+player.getCorpsKilled()+"/15 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must finish 5 Slayer tasks.");
							player.getPackets().sendIComponentText(275, 14, "I must have contributed 5M to the XP Well.");
						} else {
							player.getPackets().sendIComponentText(275, 13, "I must finish "+player.getTasksFinished()+"/5 Slayer tasks.");
						}
						if (player.getAddedWell()  >= 5 && player.getCorpsKilled() <= 14 && player.getTasksFinished() <= 4){
							player.getPackets().sendIComponentText(275, 12, "I must kill the Corpreal beast "+player.getCorpsKilled()+"/15 times.");
							player.getPackets().sendIComponentText(275, 13, "I must finish "+player.getTasksFinished()+"/5 Slayer tasks.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must have contributed 5M to the XP Well.");
						} else if (player.getAddedWell() >= 5 && player.getCorpsKilled() >= 15 && player.getTasksFinished() <= 4){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Corpreal beast "+player.getCorpsKilled()+"/15 times.");
							player.getPackets().sendIComponentText(275, 13, "I must finish "+player.getTasksFinished()+"/5 Slayer tasks.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must have contributed 5M to the XP Well.");
						} else if (player.getAddedWell() <= 4 && player.getCorpsKilled() >= 15 && player.getTasksFinished() >= 5){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Corpreal beast "+player.getCorpsKilled()+"/15 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must finish "+player.getTasksFinished()+"/5 Slayer tasks.");
							player.getPackets().sendIComponentText(275, 14, "I must have contributed 5M to the XP Well.");
						} else if (player.getAddedWell() >= 5 && player.getCorpsKilled() <= 14 && player.getTasksFinished() >= 5){
							player.getPackets().sendIComponentText(275, 12, "I must kill the Corpreal beast "+player.getCorpsKilled()+"/15 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must finish "+player.getTasksFinished()+"/5 Slayer tasks.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must have contributed 5M to the XP Well.");
						} else {
							player.getPackets().sendIComponentText(275, 14, "I must have contributed 5M to the XP Well.");
						}

						player.getPackets().sendIComponentText(275, 16, "<col=8B0000>You will be rewarded the following :");
						player.getPackets().sendIComponentText(275, 17, "100 Skill Points & 200 Onyx Bolts + SOF 3 Spins");
					}
				} else if (Player.isFriday()) {
					player.getInterfaceManager().sendInterface(275);
					if (player.SaradominKilled >= 25 && player.cuttedGems >= 150 && player.zombieGame >= 1) {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Friday");
						player.getPackets().sendIComponentText(275, 11, "You have already finished the daily task!");
						player.getPackets().sendIComponentText(275, 12, "Please come back tomorrow for a new task!");
					} else {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Friday");
						player.getPackets().sendIComponentText(275, 11, "Today is <col=8B0000>Friday</col>.");
						if (player.SaradominKilled >= 25 && player.cuttedGems <= 149 && player.zombieGame == 0) {
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Saradomin boss "+player.SaradominKilled+"/25 times.");
							player.getPackets().sendIComponentText(275, 13, "I must cut "+player.cuttedGems+"/150 of any type of gems.");
							player.getPackets().sendIComponentText(275, 14, "I must have completed the Zombie minigame.");
						} else {
							player.getPackets().sendIComponentText(275, 12, "I must kill the Saradomin boss "+player.SaradominKilled+"/25 times.");
						}
						if (player.cuttedGems >= 150 && player.SaradominKilled <= 24 && player.zombieGame == 0) {
							player.getPackets().sendIComponentText(275, 14, "I must kill the Saradomin boss "+player.SaradominKilled+"/25 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must cut "+player.cuttedGems+"/150 of any type of gems.");
							player.getPackets().sendIComponentText(275, 14, "I must have completed the Zombie minigame.");
						} else {
							player.getPackets().sendIComponentText(275, 13, "I must cut "+player.cuttedGems+"/150 of any type of gems.");
						}
						if (player.zombieGame >= 1 && player.SaradominKilled <= 24 && player.cuttedGems <= 149){
							player.getPackets().sendIComponentText(275, 12, "I must kill the Saradomin boss "+player.SaradominKilled+"/25 times.");
							player.getPackets().sendIComponentText(275, 13, "I must cut "+player.cuttedGems+"/150 of any type of gems.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must have completed the Zombie minigame.");
						} else if (player.cuttedGems >= 150 && player.SaradominKilled >= 25 && player.zombieGame == 0){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Saradomin boss "+player.SaradominKilled+"/25 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must cut "+player.cuttedGems+"/150 of any type of gems.");
							player.getPackets().sendIComponentText(275, 14, "I must have completed the Zombie minigame.");
						} else if (player.cuttedGems <= 149 && player.SaradominKilled >= 25 && player.zombieGame >= 1){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill the Saradomin boss "+player.SaradominKilled+"/25 times.");
							player.getPackets().sendIComponentText(275, 13, "I must cut "+player.cuttedGems+"/150 of any type of gems.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must have completed the Zombie minigame.");
						} else if (player.cuttedGems >= 150 && player.SaradominKilled <= 24 && player.zombieGame >= 1){
							player.getPackets().sendIComponentText(275, 12, "I must kill the Saradomin boss "+player.SaradominKilled+"/25 times.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must cut "+player.cuttedGems+"/150 of any type of gems.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must have completed the Zombie minigame.");
						} else {
							player.getPackets().sendIComponentText(275, 14, "I must have completed the Zombie minigame.");
						}

						player.getPackets().sendIComponentText(275, 16, "<col=8B0000>You will be rewarded the following :");
						player.getPackets().sendIComponentText(275, 17, "100 Skill Points & 400 Ruby bolt tips + 5M");
					}
				}else  if (Player.isSaturday()) {
					player.getInterfaceManager().sendInterface(275);
					if (player.GlacorsKilled >= 50 && player.killedKbds >= 5 && player.FrostsBuried >= 100) {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Saturday");
						player.getPackets().sendIComponentText(275, 11, "You have already finished the daily task!");
						player.getPackets().sendIComponentText(275, 12, "Please come back tomorrow for a new task!");
					} else {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Saturday");
						player.getPackets().sendIComponentText(275, 11, "Today is <col=8B0000>Saturday</col>.");
						if (player.GlacorsKilled >= 50 && player.killedKbds <= 4 && player.FrostsBuried <= 99) {
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill "+player.GlacorsKilled+"/50 Glacors.");
							player.getPackets().sendIComponentText(275, 13, "I must kill the King Black Dragon "+player.killedKbds+"/5 times.");
							player.getPackets().sendIComponentText(275, 14, "I must bury "+player.FrostsBuried+"/100 Forst bones.");
						} else {
							player.getPackets().sendIComponentText(275, 12, "I must kill "+player.GlacorsKilled+"/50 Glacors.");
						}
						if (player.GlacorsKilled <= 49 && player.killedKbds >= 5 && player.FrostsBuried <= 99) {
							player.getPackets().sendIComponentText(275, 14, "I must kill "+player.GlacorsKilled+"/50 Glacors.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must kill the King Black Dragon "+player.killedKbds+"/5 times.");
							player.getPackets().sendIComponentText(275, 14, "I must bury "+player.FrostsBuried+"/100 Forst bones.");
						} else {
							player.getPackets().sendIComponentText(275, 13, "I must kill the King Black Dragon "+player.killedKbds+"/5 times.");
						}
						if (player.GlacorsKilled <= 49 && player.killedKbds <= 4 && player.FrostsBuried >= 100){
							player.getPackets().sendIComponentText(275, 12, "I must kill "+player.GlacorsKilled+"/50 Glacors.");
							player.getPackets().sendIComponentText(275, 13, "I must kill the King Black Dragon "+player.killedKbds+"/5 times.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must bury "+player.FrostsBuried+"/100 Forst bones.");
						} else if (player.GlacorsKilled >= 50 && player.killedKbds >= 5 && player.FrostsBuried <= 99){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill "+player.GlacorsKilled+"/50 Glacors.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must kill the King Black Dragon "+player.killedKbds+"/5 times.");
							player.getPackets().sendIComponentText(275, 14, "I must bury "+player.FrostsBuried+"/100 Forst bones.");
						} else if (player.GlacorsKilled >= 50 && player.killedKbds <= 4 && player.FrostsBuried >= 100){
							player.getPackets().sendIComponentText(275, 12, "<str>I must kill "+player.GlacorsKilled+"/50 Glacors.");
							player.getPackets().sendIComponentText(275, 13, "I must kill the King Black Dragon "+player.killedKbds+"/5 times.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must bury "+player.FrostsBuried+"/100 Forst bones.");
						} else if (player.GlacorsKilled <= 49 && player.killedKbds >= 5 && player.FrostsBuried >= 100){
							player.getPackets().sendIComponentText(275, 12, "I must kill "+player.GlacorsKilled+"/50 Glacors.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must kill the King Black Dragon "+player.killedKbds+"/5 times.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must bury "+player.FrostsBuried+"/100 Forst bones.");
						} else {
							player.getPackets().sendIComponentText(275, 14, "I must bury "+player.FrostsBuried+"/100 Forst bones.");
						}

						player.getPackets().sendIComponentText(275, 16, "<col=8B0000>You will be rewarded the following :");
						player.getPackets().sendIComponentText(275, 17, "100 Skill Points & 2 Spins + 5M");
					}
				} else if (Player.isSunday()) {
					player.getInterfaceManager().sendInterface(275);
					if (player.foundBox && player.answerTrivia >= 10 && player.oresMined >= 50) {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Sunday");
						player.getPackets().sendIComponentText(275, 11, "You have already finished the daily task!");
						player.getPackets().sendIComponentText(275, 12, "Please come back tomorrow for a new task!");
					} else {
						for (int i = 0; i < 309; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Sunday");
						player.getPackets().sendIComponentText(275, 11, "Today is <col=8B0000>Sunday</col>.");
						if (player.foundBox && player.answerTrivia <= 9 && player.oresMined <= 49) {
							player.getPackets().sendIComponentText(275, 12, "<str>I must find the Resource box.");
							player.getPackets().sendIComponentText(275, 13, "I must answer the trivia "+player.answerTrivia+"/10 times.");
							player.getPackets().sendIComponentText(275, 14, "I must mine "+player.oresMined+"/50 Rune ores.");
						} else {
							player.getPackets().sendIComponentText(275, 12, "I must find the Resource box.");
						}
						if (!player.foundBox && player.answerTrivia >= 10 && player.oresMined <= 49) {
							player.getPackets().sendIComponentText(275, 14, "I must find the Resource box.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must answer the trivia "+player.answerTrivia+"/10 times.");
							player.getPackets().sendIComponentText(275, 14, "I must mine "+player.oresMined+"/50 Rune ores.");
						} else {
							player.getPackets().sendIComponentText(275, 13, "I must answer the trivia "+player.answerTrivia+"/10 times.");
						}
						if (!player.foundBox && player.answerTrivia <= 9 && player.oresMined >= 50){
							player.getPackets().sendIComponentText(275, 12, "I must find the Resource box.");
							player.getPackets().sendIComponentText(275, 13, "I must answer the trivia "+player.answerTrivia+"/10 times.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must mine "+player.oresMined+"/50 Rune ores.");
						} else if (player.foundBox && player.answerTrivia >= 10 && player.oresMined <= 49){
							player.getPackets().sendIComponentText(275, 12, "<str>I must find the Resource box.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must answer the trivia "+player.answerTrivia+"/10 times.");
							player.getPackets().sendIComponentText(275, 14, "I must mine "+player.oresMined+"/50 Rune ores.");
						} else if (player.foundBox&& player.answerTrivia <= 9 && player.oresMined >= 50){
							player.getPackets().sendIComponentText(275, 12, "<str>I must find the Resource box.");
							player.getPackets().sendIComponentText(275, 13, "I must answer the trivia "+player.answerTrivia+"/10 times.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must mine "+player.oresMined+"/50 Rune ores.");
						} else if (!player.foundBox && player.answerTrivia >= 10 && player.oresMined >= 50){
							player.getPackets().sendIComponentText(275, 12, "I must find the Resource box.");
							player.getPackets().sendIComponentText(275, 13, "<str>I must answer the trivia "+player.answerTrivia+"/10 times.");
							player.getPackets().sendIComponentText(275, 14, "<str>I must mine "+player.oresMined+"/50 Rune ores.");
						} else {
							player.getPackets().sendIComponentText(275, 14, "I must mine "+player.oresMined+"/50 Rune ores.");
						}

						player.getPackets().sendIComponentText(275, 16, "<col=8B0000>You will be rewarded the following :");
						player.getPackets().sendIComponentText(275, 17, "100 Skill Points & 5 SOF Spins.");
					}
				}
				return true;



//			case "startbox":
//				RessourceBox.SpawnPackage();
//				return true;

			case "svl":
				player.getSkills().switchVirtualLevels();
				return true;

			case "dailyreq":
				player.CKeysused = 2;
				player.BandosKilled = 25;
				player.SharksFished = 200;
				player.sm("You have finished the daily task!");
				return true;
			case "glacorreq":
				player.GlacorsKilled = 49;
				player.sm("You have finished the daily task!");
				return true;
			case "dfsreq":
				player.setFirePoints(player.getFirePoints() + 19);
				player.sm("You have finished the daily task!");
				return true;
			case "bonesreq":
				player.FrostsBuried = 100;
				player.sm("You have finished the daily task!");
				return true;

			case "kbdreq":
				player.killedKbds = 5;
				player.sm("You have finished the daily task!");
				return true;

			case "resetdailyreq":
				player.BandosKilled = 0;
				player.SharksFished = 0;
				player.CKeysused = 0;
				player.finishedTask = false;
				player.startedTask = false;
				player.sm("You have reset the daily task requirements!");
				return true;


			case "bandosi":
				player.getControlerManager().startControler("BandosInstance");
				return true;

			case "taskinfo":
				if (player.getSlayerManager().getCurrentTask() != null) {

					String[] tips = player.getSlayerManager().getCurrentTask().getTips();

					for (int i = 10; i < 300; i++) {
						player.getPackets().sendIComponentText(275, i, "");
					}
					for (String tip : tips) {
						if (tip != null) {

							if (tip.contains("TO DO") || tip.contains("TODO")) {
								player.getPackets().sendGameMessage("<col=8B0000>[NOTICE</col>] - No data found for this NPC! report this on the forums.");
							} else {
								player.getInterfaceManager().sendInterface(275);
								player.getPackets().sendIComponentText(275, 1, ""+player.getSlayerManager().getCurrentTask()+"");
								player.getPackets().sendIComponentText(275, 11, tips[0]);
							}
						}
					}
					return true;
				}
				player.getPackets().sendGameMessage("You can only use this command when you have a slayer task!");
				return true;

			case "testtt":
				player.getDialogueManager().startDialogue("XPCharges");
				return true;

			case "setyellcolor":
			case "changeyellcolor":
			case "yellcolor":
				if (player.getDonationManager().hasRank(DonatorRanks.IRON) || player.getRights() == 7 || player.getRights() == 2 || player.getRights() == 1 || player.isSupporter()) {
					player.getPackets().sendRunScript(109,
							new Object[] { "Please enter the yell color in HEX format." });
					player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
					return true;
				}
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You've to be a Extreme Donator+ to use this feature.");
				return true;

			case "security":
				player.getPackets().sendRunScript(109, new Object[] { "Please enter your security pin." });
				player.getTemporaryAttributtes().put("security", Boolean.TRUE);
				return true;

			case "setcode":

				player.getPackets().sendRunScript(109, new Object[] { "You must enter your secutiry code. It must only contain numbers." });
				player.getTemporaryAttributtes().put("addCode", Boolean.TRUE);

				return true;
			case "showcode":
				player.sm("your current security code is : "+player.SecurityCode+".");
				return true;



			case "dhub":
			case "donatorhub":
				if (player.getDonationManager().hasRank(DonatorRanks.BRONZE)|| player.getRights() == 7
				|| player.isManager() || player.getRights() == 2 || player.getRights() == 1) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1299, 2526, 0));
					return true;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You must be atleast a Bronze donator+ to use this feature!");

					return true;
				}

			case "tasktp":
				if (player.getDonationManager().hasRank(DonatorRanks.ADAMANT))
				Slayer.teleportToTask(player);
				else {
					player.sm("You must be atleast a Adamant donator+ to use this feature!");
				}
				return true;

			case "resettask":
				player.sm("You succesfully reset your slayer task.");
				player.getSlayerManager().skipCurrentTask();
				return true;

			case "dice":
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4382, 5920, 0));
				//player.getPackets().sendGameMessage("<img=0>Only bet on Trusted Dicers!<img=1>");
				return true;

			case "dh":
				player.applyHit(new Hit(player, 50, HitLook.REGULAR_DAMAGE));
				return true;


			case "testquest":
				player.getDialogueManager().startDialogue("FirstQuest");
				return true;




				/*
				 * case "unbug": player.getControlerManager().forceStop();
				 * if(player.getNextWorldTile() == null) { //if controler wont tele
				 * the player
				 * player.setNextWorldTile(Settings.HOME_PLAYER_LOCATION1); return
				 * true; }
				 */

			case "comp": {
				try {
					int level1 = Integer.parseInt(cmd[1]);
					int level2 = Integer.parseInt(cmd[2]);
					int level3 = Integer.parseInt(cmd[3]);
					player.getAppearence().ce = true;
					player.getAppearence().cr = level1;
					player.getAppearence().cg = level2;
					player.getAppearence().cb = level3;
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"Use: ::comp Red(0-255) Green(0-255) Blue(0-255) Intensity(0-127) Alpha(0-127)");
				}
				return true;
			}

			case "compnormal":
				player.getAppearence().ce = false;
				player.getAppearence().generateAppearenceData();
				return true;

			case "sit":
				player.animate(new Animation(4110));
				return true;

			case "showevent":
				for (int i = 0; i < 300; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}
				player.getInterfaceManager().sendInterface(275);
				player.getPackets().sendIComponentText(275, 1,"<col=8B0000>Event locations</col>");
				player.getPackets().sendIComponentText(275, 10, "<col=8B0000>Resource box - " + RessourceBox.boxLocation() + "</col>");
				return true;

			case "commands":
				for (int i = 0; i < 300; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}
				player.getInterfaceManager().sendInterface(275);
				player.getPackets().sendIComponentText(275, 1,"<col=8B0000>" + Constants.SERVER_NAME + " - Player Commands</col>");
				player.getPackets().sendIComponentText(275, 10, "::resettask - Resets your current slayer task.");
				player.getPackets().sendIComponentText(275, 11, "::home - Teleports you home.");
				player.getPackets().sendIComponentText(275, 12, "::empty - Empties your inventory.");
				player.getPackets().sendIComponentText(275, 13, "::vote - Loads up the voting page!");
				player.getPackets().sendIComponentText(275, 14, "::forums - Loads up the forum.");
				player.getPackets().sendIComponentText(275, 15, "::taskinfo - Gives you information about your task.");
				player.getPackets().sendIComponentText(275, 17, "::dung - Teleports you to dung.");
				player.getPackets().sendIComponentText(275, 18, "::lockexp - Locks your exp.");
				player.getPackets().sendIComponentText(275, 19, "::changepass - Changes your password.");
				player.getPackets().sendIComponentText(275, 20, "::makeover - Opens the makeover interface.");
				player.getPackets().sendIComponentText(275, 21, "::settings - Brings up the settings menu.");
				player.getPackets().sendIComponentText(275, 22, "::bosskc - Views your overall boss kills.");
				player.getPackets().sendIComponentText(275, 25, "Use the forums, for more guides!");

				player.getPackets().sendGameMessage("These are the normal player commands!");

				return true;

			case "donatorcommands":
				for (int i = 0; i < 316; i++) {
					player.getPackets().sendIComponentText(1245, i, "");
				}
				player.getInterfaceManager().sendInterface(1245);
				player.getPackets().sendIComponentText(1245, 330,
						"<col=8000ff><shad=000000><img=6>" + Constants.SERVER_NAME + " - Donator Commands<img=6></col></shad>");
				player.getPackets().sendIComponentText(1245, 13, "::bz - Teleports you to the bronze zone.");
				player.getPackets().sendIComponentText(1245, 14, "::title - You can use ::title 1-60");
				player.getPackets().sendIComponentText(1245, 15, "::greenskin/blueskin - Turns your skin green/blue.");
				player.getPackets().sendIComponentText(1245, 16, "::yell - Global messaging.");
				player.getPackets().sendIComponentText(1245, 17, "::bankitems - Banks all your inventory items.");

				player.getPackets().sendGameMessage("<img=6>These are the Bronze donator+ commands!");

				return true;

			case "swimming":
				if (player.isLocked()
						|| player.getControlerManager().getControler() instanceof FightCaves
						|| player.getControlerManager().getControler() instanceof FightKiln) {
					//player.getPackets().sendGameMessage("No.");
					return true;
				} else if (player.getDonationManager().hasRank(DonatorRanks.BRONZE) || player.getRights() == 7
						|| player.getRights() == 2 || player.getRights() == 1) {
					player.getAppearence().setRenderEmote(846);
					player.sm("Have fun swimming!");
					return true;
				} else {
					player.getPackets().sendGameMessage("You must be donator+ to swim");
					return true;
				}

			case "recanswer":
				if (player.getRecovQuestion() == null) {
					player.getPackets().sendGameMessage("Please set your recovery question first.");
					return true;
				}
				if (player.getRecovAnswer() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You can only set recovery answer once.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++) {
					message += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				player.setRecovAnswer(message);
				player.getPackets().sendGameMessage(
						"Your recovery answer has been set to - " + Utils.fixChatMessage(player.getRecovAnswer()));
				return true;

			case "recquestion":
				if (player.getRecovQuestion() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You already have a recovery question set.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++) {
					message += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				player.setRecovQuestion(message);
				player.getPackets().sendGameMessage(
						"Your recovery question has been set to - " + Utils.fixChatMessage(player.getRecovQuestion()));
				return true;

			case "empty":
				player.getInventory().reset();
				return true;

			case "ticket":
				if (player.getMuted() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
					return true;
				}
				TicketSystem.requestTicket(player);
				return true;

			case "score":
			case "kdr":
				double kill = player.getKillCount();
				double death = player.getDeathCount();
				double dr = kill / death;
				player.setNextForceTalk(new ForceTalk("<col=ff0000>I'VE KILLED " + player.getKillCount()
						+ " PLAYERS AND BEEN SLAYED " + player.getDeathCount() + " TIMES. DR: " + dr));
				return true;

//			case "help":
//				player.getInventory().addItem(1856, 1);
//				player.getPackets().sendGameMessage("You receive the official guide book!");
//				return true;


			case "title":
				if (player.getDonationManager().hasRank(DonatorRanks.BRONZE) || player.getRights() == 1
				|| player.getRights() == 2 || player.getRights() == 7) {
					if (cmd.length < 2) {
						player.getPackets().sendGameMessage("Use: ::title id");
						return true;
					}
					try {
						player.getAppearence().setTitle(Integer.valueOf(cmd[1]));
					} catch (NumberFormatException e) {
						player.getPackets().sendGameMessage("Use: ::title id");
					}
					return true;
				} else {
					player.getPackets().sendGameMessage("You must be a Bronze donator+ to use this command.");
					return true;
				}
			case "requirements":
				player.getInterfaceManager().sendCompCape();
				return true;

			case "setdisplay":
				if (player.getDonationManager().hasRank(DonatorRanks.MITHRIL) || player.getRights() == 1
				|| player.getRights() == 2 || player.getRights() == 7) {
					player.getTemporaryAttributtes().put("setdisplay", Boolean.TRUE);
					player.getPackets().sendInputNameScript("Enter the display name you wish:");
					return true;
				}
				player.getPackets().sendGameMessage("You need to be Mithril donator+ to use this command.");
				return true;

			case "removedisplay":
				player.getPackets().sendGameMessage("Removed Display Name: " + DisplayNames.removeDisplayName(player));
				return true;


			case "bank":
				if (player.isLocked() || player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You can't use any commands right now!");
					return true;
				} else if (player.getDonationManager().hasRank(DonatorRanks.IRON) || player.isSupporter() || player.isManager() || player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7) {
					player.getBank().openBank();
				} else {
					player.sendMessage("You need to be Iron donator+ to use this command.");
				}
				return true;

			case "blueskin":
				if (player.getDonationManager().hasRank(DonatorRanks.BRONZE)
						|| player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7) {
					player.getAppearence().setSkinColor(12);
					player.getAppearence().generateAppearenceData();
					return true;
				} else {
					player.getPackets().sendGameMessage("You need to be Bronze donator+ to use this command.");
					return true;
				}

			case "color":
			case "chatcolor":
				if (player.getDonationManager().hasRank(DonatorRanks.MITHRIL) || player.getRights() == 1 || player.getRights() == 2
				||	player.getRights() == 7) {
					int colorID = Integer.parseInt(cmd[1]);
					if (colorID > 5) {
						player.getPackets().sendGameMessage("Invalid Color Id.");
						return false;
					}
					player.setColorID(colorID);
					return true;
				} else {
					player.getPackets().sendGameMessage("You need to be Mithril donator+ to use this command.");
				}


			case "quest1":
				player.AdventurerQuestProgress++;

				return true;

			case "questprogress":
				if (player.finishedAdventurerQuest) {
					KharzardLures.handleQuestCompletionTabInterface(player);
				}else
					if (!player.startedAdventurerQuest) {
						KharzardLures.handleQuestStartInterface(player);
					} else {
						KharzardLures.handleProgressQuestInterface(player);
					}
				return true;
			case "timeleft":
			case "boostleft":
				player.sm("There is "+World.getTriviaTimer(player)+" left till your trivia boost ends.");
				player.getDialogueManager().startDialogue("SimpleMessage", "There is "+World.getTriviaTimer(player)+" left till your trivia boost ends.");
				return true;

			case "pendanttime":
				player.sm("There is "+player.pendantTime+" left till your pendant depletes.");;
				return true;

			case "pendantreset":
				player.pendantTime = 0;
				player.sm("sucess");;
				return true;

			case "resettimer":
				player.TriviaBoost = 0;
				return true;

//			case "rules":
//				player.getPackets()
//				.sendOpenURL(Constants.RULES_LINK);
//				return true;

//			case "hs":
//			case "highscores":
//				player.getPackets().sendOpenURL(Constants.HISCORES_LINK);
//				return true;

			case "forum":
			case "forums":
				player.getPackets().sendOpenURL(Constants.DISCORD_URL);
				return true;

			case "greenskin":
				if (player.getDonationManager().hasRank(DonatorRanks.BRONZE) || player.isGraphicDesigner()
						|| player.getRights() == 1 || player.getRights() == 2 || player.getRights() == 7
						|| player.isSupporter()) {
					player.getAppearence().setSkinColor(13);
					player.getAppearence().generateAppearenceData();
					return true;
				} else
					player.getPackets().sendGameMessage("You need to be Bronze donator+ to use this command.");
				return true;


			case "settitle":
			case "titlename":
			case "titlestring":
			case "customtitle":
				if (player.getDonationManager().hasRank(DonatorRanks.BRONZE) || player.getRights() == 1 || player.getRights() == 2
				|| player.getRights() == 7) {
					player.getPackets().sendRunScript(109, new Object[] { "Please enter the title you would like." });
					player.getTemporaryAttributtes().put("customtitle", Boolean.TRUE);
					return true;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You must be Bronze donator+ to use this command.");
					return true;
				}
			case "settitlecolor":
			case "titlecolor":
				if (player.getDonationManager().hasRank(DonatorRanks.IRON) || player.getRights() == 1
				|| player.getRights() == 2 || player.getRights() == 7) {
					player.getPackets().sendRunScript(109,
							new Object[] { "Please enter the title color in HEX format." });
					player.getTemporaryAttributtes().put("titlecolor", Boolean.TRUE);
					return true;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You must be Iron donator+ to use this command.");
					return true;
				}
			case "settitleshade":
			case "titleshade":
				if (player.getDonationManager().hasRank(DonatorRanks.IRON) || player.getRights() == 1 || player.getRights() == 2
				|| player.getRights() == 7) {
					player.getPackets().sendRunScript(109,
							new Object[] { "Please enter the title color in HEX format." });
					player.getTemporaryAttributtes().put("titleshade", Boolean.TRUE);
					return true;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You must be a Iron donator+ to use this command.");
					return true;
				}

			case "lockxp":
				player.setXpLocked(!player.isXpLocked());
				player.getPackets()
				.sendGameMessage("You have " + (player.isXpLocked() ? "LOCKED" : "UNLOCKED") + " yourself from gaining experiecne.");
				return true;

//			case "gender":
//				if (player.getAppearence().isMale()) {
//					player.getAppearence().female();
//					player.sm("You are now a female.");
//				} else {
//					player.getAppearence().isMale();
//					player.sm("You are now a male.");
//				}
//				return true;

			case "changepass":
				message1 = "";
				for (int i = 1; i < cmd.length; i++) {
					message1 += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				if (message1.length() > 15 || message1.length() < 5) {
					player.getPackets().sendGameMessage("You cannot set your password to over 15 chars.");
					return true;
				}
				player.setPassword(cmd[1]);
				player.getPackets().sendGameMessage("You changed your password! Your password is " + cmd[1] + ".");
				return true;

			case "togglehits":
				player.getSettingsManager().update(Settings.OLD_HITS,
						(player.getSettingsManager().getSettings().get(Settings.OLD_HITS) == null ? true : !(player.getSettingsManager().getSettings().get(Settings.OLD_HITS))));
				return true;

				case "slayer":
					SlayerInstance.launch(player);
					return true;


				case "disc":
			case "discord":
				player.getPackets().sendOpenURL(Constants.DISCORD_URL);
				player.sm("Discord invite is loading.");
				return true;

			case "claimtitle":
			case "gettitle":
				if (player.getRights() == 2) {
					player.getAppearence().setTitle(799);
				}else
					if (player.getRights() == 1) {
						player.getAppearence().setTitle(345);
					}else
						if (player.isSupporter()) {
							player.getAppearence().setTitle(789);
						}
				return true;

			case "yell":
				if (player.getDonationManager().hasRank(DonatorRanks.BRONZE) || player.isManager() || player.isSupporter() || player.isYoutuber() || player.getRights() >= 1) {
					message = "";
					for (int i = 1; i < cmd.length; i++) {
						message += cmd[i] + (i == cmd.length - 1 ? "" : " ");
					}
					sendYell(player, Utils.fixChatMessage(message));
					return true;
				} else {
					player.getPackets().sendGameMessage(
							"You must be a donator or staff to use this command, instead talk in the " + Constants.SERVER_NAME + " Friends Chat.");
					return true;
				}

			case "answer":
				name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				TriviaBot.verifyAnswer(player, name);
				return true;

//			case "shl":
//				if (player.getSettingsManager().getSettings().get(Settings.OLD_HITS) != null) {
//					boolean value = player.getSettingsManager().getSettings().get(Settings.OLD_HITS);
//					player.getSettingsManager().update(Settings.OLD_HITS, !value);
//					player.getPackets().sendGameMessage("You're now playing with " + (!value ? "new" : "old") + " hit looks.", true);
//				} else {
//					player.getSettingsManager().update(Settings.OLD_HITS, true);
//				}
//				return true;

			case "playernames":
				if (player.getSettingsManager().getSettings().get(Settings.PLAYER_NAMES) != null) {
					boolean value = player.getSettingsManager().getSettings().get(Settings.PLAYER_NAMES);
					player.getSettingsManager().update(Settings.PLAYER_NAMES, !value);
				} else {
					player.getSettingsManager().update(Settings.PLAYER_NAMES, true);
				}
				return true;

			case "npcnames":
				if (player.getSettingsManager().getSettings().get(Settings.NPC_NAMES) != null) {
					boolean value = player.getSettingsManager().getSettings().get(Settings.NPC_NAMES);
					player.getSettingsManager().update(Settings.NPC_NAMES, !value);
				} else {
					player.getSettingsManager().update(Settings.NPC_NAMES, true);
				}
				return true;

				case "grounditems":
					if (player.getSettingsManager().getSettings().get(Settings.GROUND_ITEMS) != null) {
						boolean value = player.getSettingsManager().getSettings().get(Settings.GROUND_ITEMS);
						player.getSettingsManager().update(Settings.GROUND_ITEMS, !value);
					} else {
						player.getSettingsManager().update(Settings.GROUND_ITEMS, true);
					}

			case "playertitles":
				if (player.getSettingsManager().getSettings().get(Settings.TITLES) != null) {
					boolean value = player.getSettingsManager().getSettings().get(Settings.TITLES);
					player.getSettingsManager().update(Settings.TITLES, !value);
				} else {
					player.getSettingsManager().update(Settings.TITLES, true);
				}
				return true;


			case "vote":
				player.getPackets().sendOpenURL(Constants.VOTE_LINK);
				player.sm("Loading page, please wait...");
				return true;

			case "donate":
				player.getPackets().sendOpenURL(Constants.DONATE_LINK);
				player.sm("Loading page, please wait...");
				return true;


			case "home":
				Magic.sendNormalTeleportSpell(player, 0, 0, Constants.HOME_PLAYER_LOCATION[0]);
				return true;

				case "settings":
					player.getSettingsinterface().open();
					return true;

			case "bosskc":
			case "bossinglog":
				player.getInterfaceManager().sendInterface(275);
				for (int i = 0; i < 300; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}

				player.getPackets().sendIComponentText(275, 1,"<col=8B0000>Boss Killing Log</col>");
				player.getPackets().sendIComponentText(275, 10, "Corporeal Beast : <col=8B0000>"+player.corpKills+"");
				player.getPackets().sendIComponentText(275, 11, "King Black Dragon : <col=8B0000>"+player.kbdKills+"");
				player.getPackets().sendIComponentText(275, 12, "General Graador : <col=8B0000>"+player.bandosKills+"");
				player.getPackets().sendIComponentText(275, 13, "Armardyl : <col=8B0000>"+player.armaKills+"");
				player.getPackets().sendIComponentText(275, 14, "Saradomin : <col=8B0000>"+player.saraKills+"");
				player.getPackets().sendIComponentText(275, 15, "Zamorak : <col=8B0000>"+player.zammyKills+"");
				player.getPackets().sendIComponentText(275, 16, "Blink : <col=8B0000>"+player.blinkKills+"");
				player.getPackets().sendIComponentText(275, 17, "Queen Black Dragon : <col=8B0000>"+player.qbdKills+" ");
				player.getPackets().sendIComponentText(275, 18, "Kalphite Queen : <col=8B0000>"+player.kalphiteKills+"");
				player.getPackets().sendIComponentText(275, 19, "Bork : <col=8B0000>"+player.borkKills+"");
				player.getPackets().sendIComponentText(275, 20, "Wildy Wyrm : <col=8B0000>"+player.wildyWyrmKills+" ");
				player.getPackets().sendIComponentText(275, 21, "Kraken : <col=8B0000>"+player.krakenKills+" ");
				player.getPackets().sendIComponentText(275, 22, "Merc Mage : <col=8B0000>"+player.mercKills+"");
				player.getPackets().sendIComponentText(275, 23, "Sea Troll Queen : "+player.seaKills+" ");
				player.getPackets().sendIComponentText(275, 24, "Lucien : <col=8B0000>"+player.lucienKills+"");
				player.getPackets().sendIComponentText(275, 24, "Leeuni : <col=8B0000>"+player.leeuniKills+"");
				player.getPackets().sendIComponentText(275, 25, "Nomad : <col=8B0000>"+player.nomadKills+"");
				player.getPackets().sendIComponentText(275, 26, "Yk'Lagor : <col=8B0000>"+player.lagorKills+"");
				player.getPackets().sendIComponentText(275, 27, "Sunfreet : <col=8B0000>"+player.sunfreetKills+"");
				player.getPackets().sendIComponentText(275, 27, "Dagannoth Supreme : <col=8B0000>"+player.supremeKills+"");
				player.getPackets().sendIComponentText(275, 27, "Dagannoth Rx : <col=8B0000>"+player.rexKills+"");
				player.getPackets().sendIComponentText(275, 27, "Dagannoth Prime : <col=8B0000>"+player.primeKills+"");
				player.getPackets().sendIComponentText(275, 27, "Avatar of creation : <col=8B0000>"+player.avatarKills+"");
				return true;

			case "train":
				player.getDialogueManager().startDialogue("TrainingTeleports");
				return true;

			case "dungeons":
			case "dungs":
				player.getDialogueManager().startDialogue("MTSlayerDungeons");
				return true;

			case "minigames":
				player.getDialogueManager().startDialogue("MinigamesTele");
				return true;

			case "bosses":
			case "bossing":
				player.getDialogueManager().startDialogue("MTHighLevelBosses");
				return true;

			case "skilling":
			case "skills":
				player.getDialogueManager().startDialogue("SkillingTeleports");
				return true;

			case "bosspets":
				player.getDialogueManager().startDialogue("BossPets");
				//player.sm("This feature is ready soon.");
				return true;

			case "ros":
				player.getPackets().sendGameMessage("This feature has been removed and is under development!");
				return true;

			case "statistics":
				player.getInterfaceManager().sendStatistics();
				return true;

			case "claim":
			case "donated":
			case "claimdonation":
				new Thread(new Donation(player)).start();
				return true;

				case "claimvote":
					new Thread(new FoxVote(player)).start();
				return true;


//			case "myinfo":
//				player.getPackets().sendGameMessage("Prestige: <col=00688B>" + player.prestigeNumber + "</col>");
//				player.getPackets()
//				.sendGameMessage("Loyalty points: <col=00688B>" + player.getLoyaltyPoints() + "</col>");
//				player.getPackets().sendGameMessage("PvM points: <col=00688B>" + player.getPvmPoints() + "</col>");
//				player.getPackets().sendGameMessage("Dung. tokens: <col=00688B>" + player.getDungTokens() + "</col>");
//				player.getInterfaceManager().sendInterface(410); // THE
//				// INTERFACE
//				// IT OPENS
//				player.getPackets().sendIComponentText(410, 9, "~My points~"); // Title
//				player.getPackets().sendIComponentText(410, 5, "Prestige: " + player.prestigeNumber + "");
//				player.getPackets().sendIComponentText(410, 6, "Loyalty Points: " + player.getLoyaltyPoints() + "");
//				player.getPackets().sendIComponentText(410, 7, "PvM Points: " + player.getPvmPoints() + "");
//				player.getPackets().sendIComponentText(410, 8, "Dungeoneering Tokens: " + player.getDungTokens() + "");
//				return true;

			case "farmingstatus":
			case "checkfarming":
			case "plots":
			case "patches":
				if (player.getDonationManager().hasRank(DonatorRanks.STEEL) ||
						player.getRights() == 1 || player.getRights() == 2
						|| player.getRights() == 7) {

					Patch patch = null;
					int[] names = new int[] { 30, 32, 34, 36, 38, 49, 51, 53, 55, 57, 59, 62, 64, 66, 68, 70, 72, 74,
							76, 190, 79, 81, 83, 85, 88, 90, 92, 94, 97, 99, 101, 104, 106, 108, 110, 115, 117, 119,
							121, 123, 125, 131, 127, 129, 2, 173, 175, 177, 182, 184, 186, 188 };
					player.getInterfaceManager().sendInterface(1082);
					for (int i = 0; i < names.length; i++) {
						if (i < PatchConstants.WorldPatches.values().length) {
							player.getPackets().sendIComponentText(1082, names[i],
									PatchConstants.WorldPatches.values()[i].name().replace("_", " ").toLowerCase());
						} else {
							player.getPackets().sendIComponentText(1082, names[i], "");
						}
					}
					for (int i = 0; i < names.length; i++) {
						if (i < player.getFarmings().patches.length) {
							patch = player.getFarmings().patches[i];
							if (patch != null) {
								if (!patch.raked) {
									player.getPackets().sendIComponentText(1082, names[i] + 1, "Full of weeds");
								} else if (patch.diseased) {
									player.getPackets().sendIComponentText(1082, names[i] + 1,
											"<col=FF0000>Is diseased!");
								} else if (patch.dead) {
									player.getPackets().sendIComponentText(1082, names[i] + 1, "<col=8f13b5>Is dead!");
								} else if (patch.healthChecked) {
									player.getPackets().sendIComponentText(1082, names[i] + 1,
											"<col=00FF00>Is ready for health check");
								} else if (patch.grown && patch.yield > 0) {
									player.getPackets().sendIComponentText(1082, names[i] + 1,
											"<col=00FF00>Is fully grown with produce available");
								} else if (patch.grown) {
									player.getPackets().sendIComponentText(1082, names[i] + 1,
											"<col=00FF00>Is fully grown with no produce available");
								} else if (patch.raked) {
									player.getPackets().sendIComponentText(1082, names[i] + 1, "Is empty");
								}
							} else {
								player.getPackets().sendIComponentText(1082, names[i] + 1, "");
							}
						} else {
							player.getPackets().sendIComponentText(1082, names[i] + 1, "");
						}
					}
					return true;
				} else {
					player.sm("You must be an Supreme Donator to use this command.");
				}
//
//			case "switchitemslook":
//				player.switchItemsLook();
//				player.getPackets().sendGameMessage(
//						"You are now playing with " + (player.isOldItemsLook() ? "old" : "new") + " item looks.");
//				return true;

//			case "spamsettings":
//			case "settings":
//				player.getDialogueManager().startDialogue("SettingsOptions");
//				return true;

			case "fly":
				if (player.getDonationManager().hasRank(DonatorRanks.STEEL) || player.getRights() == 7 || player.getRights() == 2
				|| player.getRights() == 1 || player.isGraphicDesigner()) {
					player.animate(new Animation(1914));
					player.setNextGraphics(new Graphics(92));
					player.getAppearence().setRenderEmote(1666);
				} else {
					player.sm("You must be an supreme Donator+ to use this command.");
				}
				return true;

//			case "unlend":
//				LendingManager.process();
//				return true;

			case "land":
				player.getAppearence().setRenderEmote(-1);
				return true;

//			case "makeover":
//			case "looks":
//			case "makeovermage":
//				PlayerLook.openCharacterCustomizing(player);
//				return true;

			}

		}

		return true;
	}

	public static void archiveLogs(Player player, String[] cmd) {
		try {
			if (player.getRights() <= 1) {
				return;
			}
			String location = "";
			if (player.getRights() == 7) {
				location = "data/playersaves/logs/commandlogs/owner/" + player.getUsername() + ".txt";
			} else if (player.getRights() == 2) {
				location = "data/playersaves/logs/commandlogs/admin/" + player.getUsername() + ".txt";
			} else if (player.getRights() == 1) {
				location = "data/playersaves/logs/commandlogs/mod/" + player.getUsername() + ".txt";
			} else if (player.isSupporter()) {
				location = "data/playersaves/logs/commandlogs/helper/" + player.getUsername() + ".txt";
			} else if (player.getRights() == 0) {
				location = "data/playersaves/logs/commandlogs/user/" + player.getUsername() + ".txt";
			} else if (player.getDonationManager().hasRank(DonatorRanks.BRONZE)) {
				location = "data/playersaves/logs/commandlogs/donator/" + player.getUsername() + ".txt";
			}
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++) {
				afterCMD += cmd[i] + (i == cmd.length - 1 ? "" : " ");
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
			writer.write("[" + now("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::" + cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	/*
	 * doesnt let it be instanced
	 */
	private Commands() {

	}
}