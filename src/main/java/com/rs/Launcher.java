package com.rs;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.alex.store.Index;
import com.ea.agentloader.AgentLoader;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemRequirements;
import com.rs.cache.loaders.ItemsEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.item.ItemManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.combat.CombatScriptsHandler;
import com.rs.game.world.entity.player.GameEngine;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.*;
import com.rs.game.world.entity.player.content.botanybay.BotanyBay;
import com.rs.game.world.entity.player.content.collection.CollectionLogLoader;
import com.rs.game.world.entity.player.content.dialogue.DialogueHandler;
import com.rs.game.world.entity.player.content.grandexchange.GrandExchange;
import com.rs.game.world.entity.player.content.social.FriendChatsManager;
import com.rs.game.world.entity.player.content.social.clanchat.ClansManager;
import com.rs.game.world.entity.player.controller.ControlerHandler;
import com.rs.game.world.entity.player.cutscenes.CutscenesHandler;
import com.rs.network.ServerChannelHandler;
import com.rs.utility.*;
import com.rs.utility.Censor;
import com.rs.utility.huffman.Huffman;
import kilim.WeavingClassLoader;
import kilim.agent.KilimAgent;

public final class Launcher {

	public static void main(String[] args) throws Exception {

		if (kilim.tools.Kilim.trampoline(new Object(), true, args))  {
			return;
		}
		WeavingClassLoader classLoader = new WeavingClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		AgentLoader.loadAgentClass(KilimAgent.class.getName(), "");
		KilimAgent.classLoader = classLoader;

		long currentTime = Utils.currentTimeMillis();
		Logger.log("Launcher", "Reading Cache Intake...");
		Cache.init();
		KillStreakRank.init();
		// GrandExchange.init();
		// ItemsEquipIds.init();
		ItemManager.inits();
		ItemSpawns.init();
		Huffman.init();
		Logger.log("Launcher", "Loading Data & Cache");
		// WorldList.init();
		Censor.init();
		DisplayNames.init();
		IPBanL.init();
		IPMute.init();
		MACBanL.init();
		WeightManager.init();
		PkRank.init();
		DTRank.init();
		Logger.log("Launcher", "Loading Minigames & Item Wights.");
		MapArchiveKeys.init();
		MapAreas.init();
		ObjectSpawns.init();
		NPCSpawns.init();
		NPCCombatDefinitionsL.init();
		NPCBonuses.init();
		NPCDrops.init();
		ItemExamines.init();
		ItemBonuses.init();
		StarterMap.init();
		MusicHints.init();
		BotanyBay.init();
		ShopsHandler.init();
		NPCExamines.init();
		NPCSpawning.spawnNPCS();
		FishingSpotsHandler.init();
		GrandExchange.init();
		Logger.log("Launcher", "Loading CB Scripts & Global Spawns");
		CombatScriptsHandler.init();
		// Logger.log("Launcher", "Initing Lending Manager...");
		LendingManager.init();
		Logger.log("Launcher", "Reading Local Managers & Controlers & Handlers.");
		Logger.log("Launcher", "Preparing Game Engine...");
		Logger.log("Launcher", "Preparing Grand Exchange...");
		DialogueHandler.init();
		ControlerHandler.init();
		CutscenesHandler.init();
		FriendChatsManager.init();
		CoresManager.init();
		World.init();
		Logger.log("Launcher", "Loading Region Builder & World.");
		RegionBuilder.init();
		ClansManager.init();
		ShootingStars.spawnStar(false);
		ItemRequirements.init();
		ObjectSpawning.spawnObjects();
		Logger.log(Launcher.class, "Loading collection logs.");
		CollectionLogLoader.load();
		SlimeExchange.intiat();
		HweenExchange.intiat();
		//Logger.log("Launcher", "Creating the Discord Bot.");
		try {
			ServerChannelHandler.init();
		} catch (Throwable e) {
			Logger.handle(e);
			Logger.log("Launcher", "Failed initing Server Channel Handler. Shutting down...");
			System.exit(1);
			return;
		}
		Logger.log("Launcher", "Server took " + (Utils.currentTimeMillis() - currentTime) + " milliseconds to launch.");
		addAccountsSavingTask();
		if (Constants.HOSTED)
			addCleanMemoryTask();
		// HalloweenEvent.startEvent();
		addrecalcPricesTask();
		Logger.log("Launcher", Constants.SERVER_NAME + " is now Online!");
		addUpdatePlayersOnlineTask();
		// new MotivoteHandler(Constants.MOTIVOTE_CONTAINER,
		// Constants.MOTIVOTE_API_KEY); //init mv
		awaitCommand();
		
	}
	
	/**
	 * Waits for a command to be input into the console.
	 * @throws IOException
	 */
	private static void awaitCommand() throws IOException {
		Scanner input = new Scanner(System.in);
		processCommand(input.nextLine().split(" "));
		input.close();
	}
	
	/**
	 * Processes a command.
	 * @param split
	 * 			the split string by spaces
	 * @throws IOException
	 */
	private static void processCommand(String[] split) throws IOException {
		try {
			String command = split[0];
			switch (command) {
			
				case "update":
					int delay = Integer.parseInt(split[1]);
					World.safeShutdown(true, delay);
					System.out.println("Shutting down server in " + delay + " seconds...");
					break;
					
			}
		} catch (Exception e) {
			System.err.println("Invalid command.");
		}
		awaitCommand();
	}

	private static void addrecalcPricesTask() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		int minutes = (int) ((c.getTimeInMillis() - Utils.currentTimeMillis()) / 1000 / 60);
		int halfDay = 12 * 60;
		if (minutes > halfDay)
			minutes -= halfDay;
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					// GrandExchange.recalcPrices();
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, minutes, halfDay, TimeUnit.MINUTES);
	}

	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < Constants.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					saveFiles();
					Logger.log("Online", "There are currently " + (World.getPlayers().size()) + " players playing "
							+ Constants.SERVER_NAME + ".");
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 1, 1, TimeUnit.MINUTES);// can be changed to seconds using "TimeUnit.SECONDS" as of now every one minute
									// it will save the players.
	}

	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.isFinished())
				continue;
			SerializableFilesManager.savePlayer(player);
			DisplayNames.save();
			IPBanL.save();
			IPMute.save();
			MACBanL.save();
			PkRank.save();
			DTRank.save();
			KillStreakRank.save();
			// GrandExchange.save();
			XPWell.save();
		}
	}

	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
		}
		for (Index index : Cache.STORE.getIndexes())
			index.resetCachedFiles();
		CoresManager.fastExecutor.purge();
		System.gc();
	}

	public static void shutdown() {
		try {
			GameEngine.get().shutdown();
			closeServices();
		} finally {
			System.exit(0);
		}
	}

	public static void closeServices() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
		if (Constants.HOSTED) {
			try {
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}

	private static void addUpdatePlayersOnlineTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					//com.everythingrs.playersonline.PlayersOnline.insert(Constants.OSHD_KEY, World.getPlayers().size(), false);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 30, 30, TimeUnit.SECONDS);
	}

	public static void restart() {
		closeServices();
		System.gc();
		try {
			Runtime.getRuntime().exec(
					"java -server -Xms1000m -Xmx1500m -cp bin;/data/libs/netty-3.5.2.Final.jar;/data/libs/FileStore.jar Launcher false false true false");
			System.exit(0);
		} catch (Throwable e) {
			Logger.handle(e);
		}

	}

	private Launcher() {

	}

}
