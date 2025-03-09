package com.rs.game.world.entity.player.controller;

import com.rs.game.world.entity.npc.instances.Corp.CorpController;
import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeonController;
import com.rs.game.world.entity.npc.instances.Godwars.ArmadylController;
import com.rs.game.world.entity.npc.instances.Godwars.BandosController;
import com.rs.game.world.entity.npc.instances.Godwars.SaradominController;
import com.rs.game.world.entity.npc.instances.Godwars.ZamorakController;
import com.rs.game.world.entity.npc.instances.TheHive.TheHiveController;
import com.rs.game.world.entity.npc.instances.cerberus.CerberusController;
import com.rs.game.world.entity.npc.instances.hydra.HydraController;
import com.rs.game.world.entity.npc.instances.newbosses.AcidicController;
import com.rs.game.world.entity.npc.instances.newbosses.AvatarController;
import com.rs.game.world.entity.npc.instances.newbosses.SlayerController;
import com.rs.game.world.entity.npc.instances.newbosses.SunfreetController;
import com.rs.game.world.entity.npc.instances.skotizo.SkotizoController;
import com.rs.game.world.entity.npc.instances.smokedevil.SmokedevilController;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathController;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahController;
import com.rs.game.world.entity.player.content.activities.*;
import com.rs.game.world.entity.player.content.activities.clanwars.FfaZone;
import com.rs.game.world.entity.player.content.activities.clanwars.RequestController;
import com.rs.game.world.entity.player.content.activities.clanwars.WarControler;
import com.rs.game.world.entity.player.content.activities.dueling.DuelArena;
import com.rs.game.world.entity.player.content.activities.dueling.DuelControler;
import com.rs.game.world.entity.player.content.activities.pestcontrol.PestControlGame;
import com.rs.game.world.entity.player.content.activities.pestcontrol.PestControlLobby;
import com.rs.game.world.entity.player.content.activities.trollinvasion.TrollInvasion;
import com.rs.game.world.entity.player.content.botanybay.BotanyBay;
import com.rs.game.world.entity.player.content.social.citadel.CitadelControler;
import com.rs.game.world.entity.player.content.social.citadel.Vistor;
import com.rs.game.world.entity.player.content.xeric.ChambersController;
import com.rs.game.world.entity.player.controller.impl.*;
import com.rs.game.world.entity.player.controller.instancing.*;
import com.rs.utility.Logger;

import java.util.HashMap;

public class ControlerHandler {

	private static final HashMap<Object, Class<Controller>> handledControlers = new HashMap<Object, Class<Controller>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Controller> value1 = (Class<Controller>) Class
					.forName(Wilderness.class.getCanonicalName());
			handledControlers.put("Wilderness", value1);
			Class<Controller> value2 = (Class<Controller>) Class
					.forName(Kalaboss.class.getCanonicalName());
			handledControlers.put("Kalaboss", value2);
			Class<Controller> value4 = (Class<Controller>) Class
					.forName(GodWars.class.getCanonicalName());
			handledControlers.put("GodWars", value4);
			Class<Controller> value5 = (Class<Controller>) Class
					.forName(ZGDControler.class.getCanonicalName());
			handledControlers.put("ZGDControler", value5);
			Class<Controller> value6 = (Class<Controller>) Class
					.forName(TutorialIsland.class.getCanonicalName());
			handledControlers.put("TutorialIsland", value6);
			Class<Controller> value7 = (Class<Controller>) Class
					.forName(StartTutorial.class.getCanonicalName());
			handledControlers.put("StartTutorial", value7);
			Class<Controller> value9 = (Class<Controller>) Class
					.forName(DuelArena.class.getCanonicalName());
			handledControlers.put("DuelArena", value9);
			Class<Controller> value10 = (Class<Controller>) Class
					.forName(DuelControler.class.getCanonicalName());
			handledControlers.put("DuelControler", value10);
			Class<Controller> value11 = (Class<Controller>) Class
					.forName(CorpBeastControler.class.getCanonicalName());
			handledControlers.put("CorpBeastControler", value11);
			Class<Controller> value14 = (Class<Controller>) Class
					.forName(DTControler.class.getCanonicalName());
			handledControlers.put("DTControler", value14);
			Class<Controller> value15 = (Class<Controller>) Class
					.forName(JailControler.class.getCanonicalName());
			handledControlers.put("JailControler", value15);
			Class<Controller> value17 = (Class<Controller>) Class
					.forName(CastleWarsPlaying.class.getCanonicalName());
			handledControlers.put("CastleWarsPlaying", value17);
			Class<Controller> value18 = (Class<Controller>) Class
					.forName(CastleWarsWaiting.class.getCanonicalName());
			handledControlers.put("CastleWarsWaiting", value18);
			Class<Controller> value20 = (Class<Controller>) Class
					.forName(NewHomeControler.class.getCanonicalName());
			handledControlers.put("NewHomeControler", value20);
			handledControlers.put("DarkInvasion", (Class<Controller>) Class
					.forName(DarkInvasion.class.getCanonicalName()));
			handledControlers.put("HouseControler", (Class<Controller>) Class.forName(HouseControler.class.getCanonicalName()));
			handledControlers.put("PestControlGame", (Class<Controller>) Class.forName(PestControlGame.class.getCanonicalName()));
			handledControlers.put("PestControlLobby", (Class<Controller>) Class.forName(PestControlLobby.class.getCanonicalName()));
			Class<Controller> value30 = (Class<Controller>) Class.forName(RandomEvent.class.getCanonicalName());
			Class<Controller> value31 = (Class<Controller>) Class.forName(QuizEvent.class.getCanonicalName());
			handledControlers.put("RandomEvent", value30);
			handledControlers.put("TrollInvasion", (Class<Controller>) Class.forName(TrollInvasion.class.getCanonicalName()));
			handledControlers.put("clan_wars_request", (Class<Controller>) Class.forName(RequestController.class.getCanonicalName()));
			handledControlers.put("clan_war", (Class<Controller>) Class.forName(WarControler.class.getCanonicalName()));
			handledControlers.put("clan_wars_ffa", (Class<Controller>) Class.forName(FfaZone.class.getCanonicalName()));
			handledControlers.put("NomadsRequiem", (Class<Controller>) Class.forName(NomadsRequiem.class.getCanonicalName()));
			handledControlers.put("BorkControler", (Class<Controller>) Class.forName(BorkControler.class.getCanonicalName()));
			handledControlers.put("BrimhavenAgility", (Class<Controller>) Class.forName(BrimhavenAgility.class.getCanonicalName()));
			handledControlers.put("FightCavesControler", (Class<Controller>) Class.forName(FightCaves.class.getCanonicalName()));
			handledControlers.put("PuroPuro", (Class<Controller>) Class.forName(PuroPuro.class.getCanonicalName()));
			handledControlers.put("PestInvasionControler", (Class<Controller>) Class.forName(PestInvasion.class.getCanonicalName()));
			handledControlers.put("FightKilnControler", (Class<Controller>) Class.forName(FightKiln.class.getCanonicalName()));
			handledControlers.put("FightPitsLobby", (Class<Controller>) Class.forName(FightPitsLobby.class.getCanonicalName()));
			handledControlers.put("FightPitsArena", (Class<Controller>) Class.forName(FightPitsArena.class.getCanonicalName()));
			handledControlers.put("Barrows", (Class<Controller>) Class.forName(Barrows.class.getCanonicalName()));
			handledControlers.put("RefugeOfFear", (Class<Controller>) Class.forName(RefugeOfFear.class.getCanonicalName()));
			handledControlers.put("Falconry", (Class<Controller>) Class.forName(Falconry.class.getCanonicalName()));
			handledControlers.put("QueenBlackDragonControler", (Class<Controller>) Class.forName(QueenBlackDragonController.class.getCanonicalName()));
			handledControlers.put("RunespanControler", (Class<Controller>) Class.forName(RunespanControler.class.getCanonicalName()));
			handledControlers.put("SorceressGarden", (Class<Controller>) Class.forName(SorceressGarden.class.getCanonicalName()));
			handledControlers.put("CrucibleControler", (Class<Controller>) Class.forName(CrucibleControler.class.getCanonicalName()));
			handledControlers.put("StealingCreationsGame", (Class<Controller>) Class.forName(StealingCreationGame.class.getCanonicalName()));
			handledControlers.put("StealingCreationsLobby", (Class<Controller>) Class.forName(StealingCreationLobby.class.getCanonicalName()));
			handledControlers.put("DungeonController", (Class<Controller>) Class.forName(DungeonController.class.getCanonicalName()));
			handledControlers.put("WarriorsGuild",
					(Class<Controller>) Class.forName(WGuildControler.class.getCanonicalName()));
			handledControlers.put("BotanyBay", (Class<Controller>) Class.forName(BotanyBay.class.getCanonicalName()));
			handledControlers.put("RecipeforDisaster", (Class<Controller>) Class.forName(RecipeforDisaster.class.getCanonicalName()));
			handledControlers.put("ZombiesController", (Class<Controller>) Class.forName(Zombies.class.getCanonicalName()));
			//handledControlers.put("startControl", (Class<Controler>) Class.forName(RomeIntroduction.class.getCanonicalName()));
			handledControlers.put("Introduction", (Class<Controller>) Class.forName(Introduction.class.getCanonicalName()));
			handledControlers.put("HungerGames", (Class<Controller>) Class.forName(GameControler.class.getCanonicalName()));
			handledControlers.put("LobbyController", (Class<Controller>) Class.forName(LobbyController.class.getCanonicalName()));
			handledControlers.put("cages", (Class<Controller>) Class.forName(Cages.class.getCanonicalName()));
			handledControlers.put("citadel", (Class<Controller>) Class.forName(CitadelControler.class.getCanonicalName()));
			handledControlers.put("visitor", (Class<Controller>) Class.forName(Vistor.class.getCanonicalName()));
			handledControlers.put("RomeIntroduction", (Class<Controller>) Class.forName(Vistor.class.getCanonicalName()));
			handledControlers.put("HomeController", (Class<Controller>) Class.forName(HomeController.class.getCanonicalName()));
			handledControlers.put("BandosInstance", (Class<Controller>) Class.forName(BandosInstance.class.getCanonicalName()));
			handledControlers.put("AcidicStrykewyrmInstance", (Class<Controller>) Class.forName(AcidicStrykewyrmInstance.class.getCanonicalName()));
			handledControlers.put("StarterTutorial", (Class<Controller>) Class.forName(StarterTutorial.class.getCanonicalName()));
			handledControlers.put("TheAlchemistInstance", (Class<Controller>) Class.forName(TheAlchemistInstance.class.getCanonicalName()));
			handledControlers.put("KrakenInstance", (Class<Controller>) Class.forName(KrakenInstance.class.getCanonicalName()));
			handledControlers.put("ZulrahController", (Class<Controller>) Class.forName(ZulrahController.class.getCanonicalName()));
			handledControlers.put("SmokeDungeonController", (Class<Controller>) Class.forName(SmokeDungeonController.class.getCanonicalName()));
			handledControlers.put("VorkathController", (Class<Controller>) Class.forName(VorkathController.class.getCanonicalName()));
			handledControlers.put("CerberusController", (Class<Controller>) Class.forName(CerberusController.class.getCanonicalName()));
			handledControlers.put("SkotizoController", (Class<Controller>) Class.forName(SkotizoController.class.getCanonicalName()));
			handledControlers.put("DungeonController", (Class<Controller>) Class.forName(DungeonController.class.getCanonicalName()));
			handledControlers.put("BandosController", (Class<Controller>) Class.forName(BandosController.class.getCanonicalName()));
			handledControlers.put("ZamorakController", (Class<Controller>) Class.forName(ZamorakController.class.getCanonicalName()));
			handledControlers.put("SaradominController", (Class<Controller>) Class.forName(SaradominController.class.getCanonicalName()));
			handledControlers.put("ArmadylController", (Class<Controller>) Class.forName(ArmadylController.class.getCanonicalName()));
			handledControlers.put("CorpController", (Class<Controller>) Class.forName(CorpController.class.getCanonicalName()));
			handledControlers.put("SunfreetController", (Class<Controller>) Class.forName(SunfreetController.class.getCanonicalName()));
			handledControlers.put("SlayerController", (Class<Controller>) Class.forName(SlayerController.class.getCanonicalName()));
			handledControlers.put("AcidicController", (Class<Controller>) Class.forName(AcidicController.class.getCanonicalName()));
			handledControlers.put("AvatarController", (Class<Controller>) Class.forName(AvatarController.class.getCanonicalName()));
			handledControlers.put("TheHiveController", (Class<Controller>) Class.forName(TheHiveController.class.getCanonicalName()));
			handledControlers.put("SmokedevilController", (Class<Controller>) Class.forName(SmokedevilController.class.getCanonicalName()));
			handledControlers.put("HydraController", (Class<Controller>) Class.forName(HydraController.class.getCanonicalName()));
			handledControlers.put("EliteDungeonController", (Class<Controller>) Class.forName(EliteDungeonController.class.getCanonicalName()));
			handledControlers.put("KaruulmController", (Class<Controller>) Class.forName(KaruulmController.class.getCanonicalName()));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void reload() {
		handledControlers.clear();
		init();
	}

	public static final Controller getControler(Object key) {
		if (key instanceof Controller) {
			return (Controller) key;
		}
		Class<Controller> classC = handledControlers.get(key);
		if (classC == null) {
			return null;
		}
		try {
			return classC.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
}
