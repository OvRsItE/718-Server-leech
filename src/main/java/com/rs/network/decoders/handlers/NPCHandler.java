package com.rs.network.decoders.handlers;

import com.rs.Constants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.kraken.KrakenNPC;
import com.rs.game.world.entity.npc.kraken.TentacleNPC;
import com.rs.game.world.entity.npc.others.*;
import com.rs.game.world.entity.npc.others.GraveStone;
import com.rs.game.world.entity.npc.pet.Pet;
import com.rs.game.world.entity.npc.slayer.Strykewyrm;
import com.rs.game.world.entity.player.CoordsEvent;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Listen;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.actions.SheepShearing;
import com.rs.game.world.entity.player.actions.skilling.EntTreeCutting;
import com.rs.game.world.entity.player.actions.skilling.Fishing;
import com.rs.game.world.entity.player.actions.skilling.Fishing.FishingSpots;
import com.rs.game.world.entity.player.actions.skilling.mining.LivingMineralMining;
import com.rs.game.world.entity.player.actions.skilling.mining.MiningBase;
import com.rs.game.world.entity.player.actions.skilling.runecrafting.SiphonActionCreatures;
import com.rs.game.world.entity.player.actions.skilling.thieving.PickPocketAction;
import com.rs.game.world.entity.player.actions.skilling.thieving.PickPocketableNPC;
import com.rs.game.world.entity.player.content.*;
import com.rs.game.world.entity.player.content.Slayer.SlayerMaster;
import com.rs.game.world.entity.player.content.activities.CommendationExchange;
import com.rs.game.world.entity.player.content.dialogue.impl.FremennikShipmaster;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonRewardShop;
import com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.puzzles.SlidingTilesRoom;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.player.controller.impl.SorceressGarden;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.network.io.InputStream;
import com.rs.utility.*;

import java.util.ArrayList;

public class NPCHandler {

	public static void handleExamine(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		if (forceRun) {
			player.setRun(forceRun);
		}
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		//player.getPackets().sendNPCMessage(0, 15263739, npc, NPCExamines.getExamine(npc));
		if (player.isOwner() || player.getRights() == 2) {
			player.getPackets().sendGameMessage("" + npc.getDefinitions().name + " (Id: " + npc.getId() + ", Health: <col=FF0000>" + Utils.formatNumber(npc.getHitpoints()) + "</col>/" + Utils.formatNumber(npc.getMaxHitpoints()) + ".");
			System.out.println(npc.getBonuses()[8]);
		} else {
			player.getPackets().sendNPCMessage(0, 15263739, npc, NPCExamines.getExamine(npc));
			showDropTable(player, npc);
		}
	}

	private static void showDropTable(Player player, NPC npc) {
		if (!npc.getDefinitions().hasAttackOption() || NPCDrops.getDrops(npc.getId()) == null) {
			return;
		}
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "DROPS - " + npc.getName());
		Drop[] drops = NPCDrops.getDrops(npc.getId());
		ArrayList<Drop> always = new ArrayList<Drop>();
		ArrayList<Drop> common = new ArrayList<Drop>();
		ArrayList<Drop> uncommon = new ArrayList<Drop>();
		ArrayList<Drop> rare = new ArrayList<Drop>();
		ArrayList<Drop> veryrare = new ArrayList<Drop>();
		for (Drop drop : drops) {
			if (drop.getRate() == 100) {
				always.add(drop);
			} else if (drop.getRate() > 50 && drop.getRate() < 100) {
				common.add(drop);
			} else if (drop.getRate() >= 10 && drop.getRate() <= 50) {
				uncommon.add(drop);
			} else if (drop.getRate() >= 5 && drop.getRate() < 10) {
				rare.add(drop);
			} else {
				veryrare.add(drop);
			}
		}
		player.getPackets().sendIComponentText(275, 10, "-----------------");
		player.getPackets().sendIComponentText(275, 11, "ALWAYS");
		int beginning = 12;
		int index;
		for (index = beginning; index < always.size() + beginning; index++) {
			Drop drop = always.get(index - beginning);
			ItemDefinitions def = ItemDefinitions.forId(drop.getItemId());
			player.getPackets().sendIComponentText(275, index, "<col=00ff00>" + def.getName() + " " + getDropAmount(drop));
		}
		player.getPackets().sendIComponentText(275, index++, "-----------------");
		player.getPackets().sendIComponentText(275, index++, "COMMON");
		beginning = index;
		for (index = beginning; index < common.size() + beginning; index++) {
			Drop drop = common.get(index - beginning);
			ItemDefinitions def = ItemDefinitions.forId(drop.getItemId());
			player.getPackets().sendIComponentText(275, index, "<col=00ff00>" + def.getName() + " " + getDropAmount(drop) + " " + drop.getRate() + "%");
		}
		player.getPackets().sendIComponentText(275, index++, "-----------------");
		player.getPackets().sendIComponentText(275, index++, "UNCOMMON");
		beginning = index;
		for (index = beginning; index < uncommon.size() + beginning; index++) {
			Drop drop = uncommon.get(index - beginning);
			ItemDefinitions def = ItemDefinitions.forId(drop.getItemId());
			player.getPackets().sendIComponentText(275, index, "<col=ffff00>" + def.getName() + " " + getDropAmount(drop) + " " + drop.getRate() + "%");
		}
		player.getPackets().sendIComponentText(275, index++, "-----------------");
		player.getPackets().sendIComponentText(275, index++, "RARE");
		beginning = index;
		for (index = beginning; index < rare.size() + beginning; index++) {
			Drop drop = rare.get(index - beginning);
			ItemDefinitions def = ItemDefinitions.forId(drop.getItemId());
			player.getPackets().sendIComponentText(275, index, "<col=971919>" + def.getName() + " " + getDropAmount(drop) + " " + drop.getRate() + "%");
		}
		player.getPackets().sendIComponentText(275, index++, "-----------------");
		player.getPackets().sendIComponentText(275, index++, "VERYRARE");
		beginning = index;
		for (index = beginning; index < veryrare.size() + beginning; index++) {
			Drop drop = veryrare.get(index - beginning);
			ItemDefinitions def = ItemDefinitions.forId(drop.getItemId());
			player.getPackets().sendIComponentText(275, index, "<col=ff0000>" + def.getName() + " " + getDropAmount(drop) + " " + drop.getRate() + "%");
		}
		player.getPackets().sendIComponentText(275, index++, "-----------------");
		beginning = index;
		for (index = beginning; index < 300 + 12; index++) {
			player.getPackets().sendIComponentText(275, index, "");
		}
	}

	private static String getDropAmount(Drop drop) {
		return drop.getMinAmount() != drop.getMaxAmount() ? "x" + drop.getMinAmount() + "-" + drop.getMaxAmount() : "x1";
	}

	public static void handleOption1(final Player player, InputStream stream, final Hit... hits) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.isFinished() || !player.finishedStarter
				|| !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		player.stopAll(false);
		if(forceRun) {
			player.setRun(forceRun);
		}
//		if (npc instanceof Pet ) {
//				npc.getDefinitions().HideOption();
//
//		}
		if (npc.getId() == 26595) {
			player.faceEntity(npc);
			player.getActionManager().setAction(new EntTreeCutting(npc));
		}

		if (npc.getId() == 745) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 4)) {
				return;
			}
			npc.faceEntity(player);
			player.getDialogueManager().startDialogue("Wormbrain", npc.getId());
			return;
		}
		if (npc.getDefinitions().name.contains("Banker")
				|| npc.getDefinitions().name.contains("banker")) {
			player.faceEntity(npc);
			npc.faceEntity(player);
			player.getDialogueManager().startDialogue("Banker", npc.getId());
			return;
		}
		if (npc.getDefinitions().name.toLowerCase().equals("grand exchange clerk")) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2)) {
				return;
			}	    npc.faceEntity(player);
			player.getDialogueManager().startDialogue("GrandExchange", npc.getId());
			return;
		}
		if (npc.getDefinitions().name.contains("Circus")
				|| npc.getDefinitions().name.contains("circus")) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2)) {
				return;
			}
			npc.faceEntity(player);
			player.getPackets().sendGameMessage("The circus is not in " + Constants.SERVER_NAME + " currently, sorry!");
			return;
		}
		if (npc instanceof KrakenNPC || npc instanceof TentacleNPC) {
			player.getActionManager().setAction(new PlayerCombat(npc));
			player.resetWalkSteps();
			return;
		}
		if(SiphonActionCreatures.siphon(player, npc)) {
			return;
		}
		//dungeoneering puzzle
		if (SlidingTilesRoom.handleSlidingBlock(player, npc))
			return;

		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				player.faceEntity(npc);
				if (npc.getId() == 6601) {
					return;
				}
				if (npc.getId() == 529 && player.getGameMode().isIronman()) {
					player.sm("You cant use this shop while playing Ironman.");
					return;
				}
				if (!player.getControlerManager().processNPCClick1(npc)) {
					return;
				}
				FishingSpots spot = FishingSpots.forId(npc.getId() | 1 << 24);
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return; // its a spot, they wont face us
				}else if (npc.getId() >= 8837 && npc.getId() <= 8839) {
					player.getActionManager().setAction(new LivingMineralMining((LivingRock) npc));
					return;
				} else if (npc instanceof GraveStone) {
					GraveStone grave = (GraveStone) npc;
					grave.sendGraveInscription(player);
					return;
				}
				if (npc.getClickOptionListener()[0] != null) {
					boolean face = npc.getClickOptionListener()[0].handle(player, npc);
					if (!face)
						return;
				}
				npc.faceEntity(player);
				if (npc.getId() == 3832 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("NNC",
							npc.getId());
				} else if (npc.getId() == 555 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Player_Shop_Manager", npc.getId());
				}
				if (npc.getId() == 3709 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("PlayerShopDialogue", npc.getId());
				} else if (npc.getId() == 6892) {
					player.getDialogueManager().startDialogue("BossPetManagerD", npc.getId());
				} else if (npc.getId() == 5532 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SorceressGardenNPCs", npc);
				} else if (npc.getId() == 14620) {
					player.getDialogueManager().startDialogue("PolyMerchant", npc);
				} else if (npc.getId() == 659) {
					player.getDialogueManager().startDialogue("pointShops", npc);
				} else if (npc.getId() == 8540) {
					player.getDialogueManager().startDialogue("SantaClaus", npc);
				} else if (npc.getId() == 2904 && !player.getGameMode().isIronman()) {
					if (player.getTemporaryAttributtes().get("JAGGED") == Boolean.TRUE) {
						player.getDialogueManager().startDialogue("JAGDialogue", npc.getId(), null);
					} else {
						player.getDialogueManager().startDialogue("JAGInformation",
								npc.getId(), null);
					}
				}
				else if (npc.getId() == 6892 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("PetShop", npc.getId());
				} else if (npc.getId() == 2144) {
					player.getDialogueManager().startDialogue("DonaterBoss", npc.getId());
				} else if (npc.getId() == 8556 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("DTRewards", npc.getId(), null);
				} else if (npc.getId() == 14240 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("DonatorShops2", npc.getId(), 0);
				} else if (npc.getId() == 200 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Lshop", npc.getId());
				} else if (npc.getId() == 4657 ) {
					player.getDialogueManager().startDialogue("Customize",npc);
					return;
				}
				else if (npc.getId() == 7444) {
					ShopsHandler.openShop(player, 150);
				} else if (npc.getId() == 12377) {
						ShopsHandler.openShop(player, 167);
				} else if (npc.getId() == 4544) {
					player.getDialogueManager().startDialogue("PrestigeOne");
				} else if (npc.getId() == 3108) {
					player.getDialogueManager().startDialogue("AdventurerQuest");
				} else if (npc.getId() == 3109) {
					player.getDialogueManager().startDialogue("DrunkCaptain");
				} else if (npc.getId() == 7528) {
					player.getDialogueManager().startDialogue("KharzardGuard");
				} else if (npc.getId() == 259) {
					player.getDialogueManager().startDialogue("Angor");
				} else if (npc.getId() == 664) {
					player.getDialogueManager().startDialogue("Dimintheis", npc.getId());
				} else if (npc.getId() == 7550) {
					player.getDialogueManager().startDialogue("LazyGuard");
				} else if (npc.getId() == 14386) {
					player.getDialogueManager().startDialogue("BossSlayerD");
				} else if (npc.getId() == 7551) {
					player.getDialogueManager().startDialogue("GeneralKazhard");
				} else if (npc.getId() == 14854 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 7);
				} else if (npc.getId() == 620) {
					player.getDialogueManager().startDialogue("Well", npc.getId());
				} else if (npc.getId() == 945 && player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("IronmanShop", npc);
				} else if (npc.getId() == 3705) {
					player.getDialogueManager().startDialogue("XPCharges");
				} else if (npc.getId() == 11475) {
				   ShopsHandler.openShop(player, 164);
				} else if (npc.getId() == 209) {
					   ShopsHandler.openShop(player, 166);
				} else if (npc.getId() == 2824 || npc.getId() == 1041) {
					player.getDialogueManager().startDialogue("TanningD", npc.getId());
				} else if (npc.getId() == 5563 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SorceressGardenNPCs", npc);
				} else if (npc.getId() >= 4650 && npc.getId() <= 4656 || npc.getId() == 7077) {
					player.getDialogueManager().startDialogue("Sailing", npc.getId());
				} else if (npc.getId() == 546 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Scavo", npc.getId());
				} else if (npc.getId() == 2290 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 154);
				} else if (npc.getId() == 8085 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 64);
				} else if (npc.getId() == 6537) {
					ShopsHandler.openShop(player, 168);
				} else if (npc.getId() == 14792 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("WiseOldMan", npc.getId());
				} else if (npc.getId() == 6361 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("TrainingCaptain", npc.getId());
				} else if (npc.getId() >= 376 && npc.getId() <= 378) {
					player.getDialogueManager().startDialogue("KaramjaTrip", npc.getId());
				} else if (npc.getId() == 3344 || npc.getId() == 3345) {
					MutatedZygomites.transform(player, npc);
				} else if (npc.getId() == 1304) {
					player.getDialogueManager().startDialogue("Islands", npc.getId());
				} else if (npc.getId() == 3802 || npc.getId() == 6140 || npc.getId() == 6141) {
					player.getDialogueManager().startDialogue("LanderSquire", npc.getId());
				} else if (npc.getId() == 3790 || npc.getId() == 3791 || npc.getId() == 3792) {
					player.getDialogueManager().startDialogue("VoidKnightExchange", npc.getId());
				} else if (npc.getId() == 401 || npc.getId() == 402 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Jungler", npc.getId());
				} else if (npc.getId() == 5563 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SorceressGardenNPCs", npc);
				} else if (npc.getId() == 559 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Brian", npc.getId());
				} else if (npc.getId() == 5915) {
					player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20709);
				} else if (npc.getId() == 13633) {
					player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20708);
				} else if (SlayerMaster.startInteractionForId(player, npc.getId(), 1)) {
					return;
				} else if (npc.getId() == 8091) {
					player.getDialogueManager().startDialogue("StarSprite1");
					return;
				} else if (npc.getId() == 7664 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("cagelobby");
					return;
				}
				else if (npc.getId() == 556 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Grum", npc.getId());
				} else if (npc.getId() == 4246 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Taxidermist", npc.getId());
				} else if (npc.getId() == 558 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Gerrant", npc.getId());
				}else if (npc.getId() == 2902 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("GraveStones", npc.getId());
				} else if (npc.getId() == 557 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Wydin", npc.getId());
				} else if (npc.getId() == 576 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Replenish", npc.getId());
				} else if (npc.getId() == 13790 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Lumber", npc.getId());
				} else if (npc.getId() == 7425 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ImplingManager", npc.getId());
				} else if (npc.getId() == 6667 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("StankyMorgan", npc.getId());
				} else if (PenguinEvent.isPenguin(npc.getId())) {
					PenguinEvent.spotPenguin(player, npc);
				} else if (npc.getId() == 6670 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("StankyMorgan", npc.getId());
				} else if (npc.getDefinitions().name.contains("impling") ) {
					Hunter.captureFlyingEntity(player, npc);
				} else if (npc.getId() == 2690 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("JackSeagull", npc.getId());
				}/* else if (npc.getId() == 9711 && !player.getGameMode().isIronman()) {
					DungeonRewards.openRewardsShop(player);
				}*/ else if (npc.getId() == 14811 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("CombatShops", npc.getId());
				} else if (npc.getId() == 13335 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MineShop", npc.getId());
				} else if (npc.getId() == 1694 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RangeShops", npc.getId());
				} else if (npc.getId() ==  5626 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ZombieMonk", npc.getId(), null);
				} else if (npc.getId() == 303 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 70);
				} else if (npc.getId() == 11571 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 58);
				} else if (npc.getId() == 11577 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 59);
				} else if (npc.getId() == 5559 ) {
					player.sendDeath(npc);
				} else if (npc.getDefinitions().name.contains("ool leprech") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("IrishToolD", npc.getId());
				} else if (npc.getId() == 15451 && npc instanceof FireSpirit) {
					FireSpirit spirit = (FireSpirit) npc;
					spirit.giveReward(player);
				}
				else if (npc.getId() == 949 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("QuestGuide",
							npc.getId(), null);
				} else if (npc.getId() == 7530) {
					LividFarm.CheckforLogs(player);
				} else if (npc.getId() >= 1 && npc.getId() <= 6 || npc.getId() >= 7875 && npc.getId() <= 7884) {
					player.getDialogueManager().startDialogue("Man", npc.getId());
				} else if (npc.getId() == 198 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("GuildMaster", npc.getId());
				} else if (npc.getId() == 747) {
					player.getDialogueManager().startDialogue("Oziach", npc.getId());
				} else if (npc.getId() == 15907 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("OsmanDialogue", npc.getId());
				} else if (npc.getId() == 746 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Oracle", npc.getId());
				} else if (npc.getId() == 918 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Ned1", npc.getId());
				} else if (npc.getId() == 4475 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Ned2", npc.getId());
				} else if (npc.getId() == 583 || npc.getId() == 9395 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Betty", npc.getId());
				} else if (npc.getId() == 285 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Veronica", npc.getId());
				} else if (npc.getId() == 2304 || npc.getId() == 2323 || npc.getId() == 4560 || npc.getId() == 2342 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Farmer", npc.getId());
				} else if (npc.getId() == 286 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ProfessorOddenstein", npc.getId());
				} else if (npc.getId() == 744) {
					if (player.DS <= 2) {
						player.getPackets().sendGameMessage("Klarense has no interest in you at the moment.");
					} else {
						player.getDialogueManager().startDialogue("Klarense", npc.getId());
					}
				} else if (npc.getId() == 9462) {
					Strykewyrm.handleStomping(player, npc);
				} else if (npc.getId() == 9464) {
					Strykewyrm.handleStomping(player, npc);
				} else if (npc.getId() == 9466) {
					Strykewyrm.handleStomping(player, npc);
				} else if (npc.getId() == 16152) {
					Strykewyrm.handleStomping(player, npc);
				} else if (npc.getId() == 2238 || npc.getId() == 7931 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Donie", npc.getId());
				} else if (npc.getId() >= 2811 && npc.getId() <= 2815 && !player.getGameMode().isIronman() || npc.getDefinitions().name.contains("Camel") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Camel", npc.getId());
				} else if (npc.getDefinitions().name.contains("Fisherman") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Fisherman", npc.getId());
				} else if (npc.getId() == 1862 || npc.getId() == 2961 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("AliMorrisane", npc.getId());
				} else if (npc.getId() == 539 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SilkTrader", npc.getId());
				} else if (npc.getId() == 1595 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Saniboch", npc.getId());
				} else if (npc.getId() == 2233 || npc.getId() == 2572 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Olivia", npc.getId());
				} else if (npc.getId() == 540 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("GemTrader", npc.getId());
				} else if (npc.getId() == 1686 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("GhostDisciple", npc.getId());
				} else if (npc.getId() == 707 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("WizardGrayzig", npc.getId());
				} else if (npc.getId() == 9159 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Faruq", npc.getId());
				} else if (npc.getId() == 3671 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Fortunato", npc.getId());
				} else if (npc.getId() == 970 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Diango", npc.getId());
				} else if (npc.getId() >= 2291 && npc.getId() <= 2294 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RugMerchant", false);
				} else if (npc.getId() == 3680 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Leaflet", npc.getId());
				} else if (npc.getDefinitions().name.contains("Monk of Entrana")) {
					player.getDialogueManager().startDialogue("MonkOfEntrana", npc.getId());
				} else if (npc.getId() == 651 || npc.getId() == 649 || npc.getId() == 652) {
					player.getPackets().sendGameMessage("They seem to have no interest in you, maybe I should speak to another.");
				} else if (npc.getId() == 654 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Shamus", npc.getId());
				} else if (npc.getId() == 650 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Warrior", npc.getId());
				} else if (npc.getId() == 2704) {
					npc.setNextForceTalk(new ForceTalk("Zzzzzzzzz"));
					player.sm("The guard appears to be sleeping.");
				} else if (npc.getId() == 663&& !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Avan", npc.getId());
				} else if (npc.getId() == 3331 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("BarfyBill", npc.getId());
				} else if (npc.getId() == 28 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("PenguinRewards");
				} else if (npc.getId() == 9633 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Xenia", npc.getId());
				} else if (npc.getDefinitions().name.contains("Fremennik warrior") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("FremennikWarrior", npc.getId());
				} else if (npc.getId() >= 3809 && npc.getId() <= 3812 || npc.getId() == 1800) {
					player.getDialogueManager().startDialogue("GnomeGlider", npc.getId());
				} else if (npc.getId() == 7872 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Victoria", npc.getId());
				} else if (npc.getId() == 2634 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Schism", npc.getId());
				} else if (npc.getId() == 4250 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SawMillOperator", npc.getId());
				} else if (npc.getId() == 755 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Morgan", npc.getId());
				} else if (npc.getId() == 756 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("DrHarlow", npc.getId());
				} else if (npc.getId() == 7869 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Julian", npc.getId());
				} else if (npc.getId() == 922 || npc.getId() == 8207 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Aggie", npc.getId());
				} else if (npc.getId() == 8078 || npc.getId() == 8203 || npc.getId() == 8204) {
					player.getPackets().sendGameMessage("This NPC will be available on Halloween of 2014.");
				} else if (npc.getId() >= 7885 && npc.getId() <= 7890) {
					int dialogue = Misc.random(2);
					if (dialogue == 1) {
						player.getDialogueManager().startDialogue("Guardsman1", npc.getId());
					} else {
						player.getDialogueManager().startDialogue("Guardsman2", npc.getId());
					}
				}
				else if (npc.getId() == 741 || npc.getId() == 2088 || npc.getId() == 7933) {
					player.getDialogueManager().startDialogue("DukeHoracio", npc.getId());
				} else if (npc.getId() == 9376) {
					if (player.christmas <= 1) {
						player.getPackets().sendGameMessage("The imp has no interest in you.");
					} else {
						player.getDialogueManager().startDialogue("Imp1", npc.getId());
					}
				} else if (npc.getId() == 9377) {
					if (player.christmas <= 2) {
						player.getPackets().sendGameMessage("The imp has no interest in you.");
					} else {
						player.getDialogueManager().startDialogue("Imp2", npc.getId());
					}
				} else if (npc.getId() == 9378) {
					if (player.christmas <= 3) {
						player.getPackets().sendGameMessage("The imp has no interest in you.");
					} else {
						player.getDialogueManager().startDialogue("Imp3", npc.getId());
					}
				} else if (npc.getId() == 9379) {
					if (player.startedEvent) {
						if (player.foundImps == 0) {
							npc.finish();
							npc.sendDeath(npc);
							player.foundImps = 1;
							player.getPackets().sendGameMessage("You have tracked down 1/3 snow imps!");
							return;
						} else
							if (player.foundImps == 1) {
								npc.finish();
								npc.sendDeath(npc);
								player.foundImps = 2;
								player.getPackets().sendGameMessage("You have tracked down 2/3 snow imps!");
								return;
							}else
								if (player.eventProgress == 2) {
									npc.finish();
									npc.sendDeath(npc);
									player.foundImps = 3;
									player.getPackets().sendGameMessage("You have tracked down 3/3 snow imps!");
									player.getPackets().sendGameMessage("I should probably go see Santa.");
									return;
								}
					}
				} else if (npc.getId() == 8536) {
					if (player.christmas <= 5) {
						player.getPackets().sendGameMessage("The imp has no interest in you.");
					} else {
						player.getDialogueManager().startDialogue("Imp5", npc.getId());
					}
				} else if (npc.getId() == 1552) {
					player.getDialogueManager().startDialogue("SantaClaus", npc.getId());
				} else if (npc.getId() == 8517) {
					if (player.snowrealm) {
						player.getDialogueManager().startDialogue("JackFrost", npc.getId());
					} else {
						player.getPackets().sendGameMessage("You must complete the Christmas event to access this npc.");
					}
				} else if (npc.getId() == 11506){
					if (player.starterstage == 0) {
						player.getDialogueManager().startDialogue("Guthix", npc.getId());
					} else if (player.starterstage == 2) {
						player.getDialogueManager().startDialogue("StarterClass", npc.getId());
					} else if (player.starterstage == 3) {
						player.sendMessage("You may leave through the portal now.");
					} else {
						player.getDialogueManager().startDialogue("NewPlayerTutorial", npc.getId());
					}
				} //Gertrude's Cat NPCs
				else if (npc.getId() == 7740) {
					if (player.gertCat < 5){
						player.getPackets().sendGameMessage("You search the crate but find nothing.");
					} else if (player.gertCat == 5) {
						if (npc.getX() == 3298 && npc.getY() == 3514) {
							player.getPackets().sendGameMessage("You search the crates and find Fluff's three kittens!");
							player.getInventory().addItem(13236, 1);
						} else {
							player.getPackets().sendGameMessage("You search the crate but find nothing.");
						}
					} else {
						player.getPackets().sendGameMessage("You already found Fluff's Kittens.");
					}
				} else if (npc.getId() == 780) {
					player.getDialogueManager().startDialogue("Gertrude", npc.getId());

				} else if (npc.getId() == 781 || npc.getId() == 783) {
					player.getDialogueManager().startDialogue("WiloughAndShilop", npc.getId());

				} else if (npc.getId() == 7742) {
					player.getDialogueManager().startDialogue("Fluffs", npc.getId());
				} else if (npc.getId() == 579 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("DrogoDwarf", npc.getId());
				} else if (npc.getId() == 208 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Lawgof");
				} else if (npc.getId() == 209 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Nulodion");
				} else if (npc.getId() == 1916 && !player.getGameMode().isIronman()) {
					player.getPackets().sendGameMessage("My mom told me I shouldn't talk to vampyres...");
				} else if (npc.getId() == 13172 || npc.getId() == 13173) {
					player.getPackets().sendGameMessage("I heard that Leela is a spy, maybe I shouldn't interact with her...");
				} else if (npc.getId() == 12377) {
					player.getDialogueManager().startDialogue("PumpkinPete", npc.getId());
				} else if (npc.getId() == 12378) {
					player.getDialogueManager().startDialogue("PumpkinPete2", npc.getId());
				} else if (npc.getId() == 12375 && player.cake == 0) {
					player.getDialogueManager().startDialogue("Zabeth", npc.getId());
				} else if (npc.getId() == 12375 && player.drink == 0) {
					player.getDialogueManager().startDialogue("Zabeth2", npc.getId());
				} else if (npc.getId() == 12375 && player.drink == 1) {
					player.getDialogueManager().startDialogue("Zabeth3", npc.getId());
				} else if (npc.getId() == 12379 && player.drink == 0) {
					if (player.talked == 0) {
						player.getPackets().sendGameMessage("The Grim Reaper isn't interested in you at the moment.");
					} else {
						player.getDialogueManager().startDialogue("GrimReaper", npc.getId());
					}
				} else if (npc.getId() == 12379 && player.dust1 == 0) {
					player.getDialogueManager().startDialogue("GrimReaper2", npc.getId());
				} else if (npc.getId() == 12379 && player.dust1 == 1 && player.dust2 == 1 && player.dust3 == 1) {
					player.getDialogueManager().startDialogue("GrimReaper3", npc.getId());
				} else if (npc.getId() == 12375 && player.doneevent == 1) {
					player.getDialogueManager().startDialogue("PumpkinPete2", npc.getId());
				} else if (npc.getId() == 12379 && player.doneevent == 1) {
					player.getDialogueManager().startDialogue("PumpkinPete2", npc.getId());
				} else if (npc.getId() == 12392) {
					player.getDialogueManager().startDialogue("PumpkinPete2", npc.getId());
				} else if (npc.getId() == 8266) {
					player.getDialogueManager().startDialogue("Ghommel");
				} else if (npc.getId() == 2237) {
					player.getPackets().sendGameMessage("The annoyed farmer does not bother with you. For some reason he is in a bad mood.");
				} else if (npc.getId() == 13942) {
					player.getPackets().sendGameMessage("Heroes are part of a future update.");
				} else if (npc.getId() == 4585) {
					player.getPackets().sendGameMessage("The gnome is too caught up in his studies to pay attention to you.");
				} else if (npc.getId() == 706 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("WizardMizgog", npc.getId());
				} else if (npc.getId() == 458 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("FatherUrhney");
				} else if (npc.getId() == 300 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Sedridor", npc);
				} else if (npc.getId() == 5913 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Aubury", npc);
				} else if (npc.getDefinitions().name.contains("Musician") || npc.getId() == 3509) {
					player.stopAll();
					player.getActionManager().setAction(new Listen());
				} else if (npc.getDefinitions().name.contains("Tool") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("FarmingShop");
				} else if (npc.getId() == 456) {
					if (player.RG <= 2) {
						player.getDialogueManager().startDialogue("FatherAereck1", npc.getId());
					} else if (player.RG >= 3 && player.RG <= 5) {
						player.getDialogueManager().startDialogue("FatherAereck2", npc.getId());
					} else {
						player.getDialogueManager().startDialogue("FatherAereck", npc.getId());
					}
				} else if (npc.getId() == 457) {
					if (player.RG == 3) {
						if (player.getEquipment().getAmuletId() == 552) {
							player.getDialogueManager().startDialogue("Ghost", npc.getId());
						} else {
							player.getDialogueManager().startDialogue("GhostWo", npc.getId());
						}
					} else if (player.RG >= 4 && player.RG <= 5) {
						player.getDialogueManager().startDialogue("GhostFind", npc.getId());
					} else {
						player.getPackets().sendGameMessage("The ghost does not seem interested in you.");
					}
				} else if (npc.getId() == 278) {
					player.getDialogueManager().startDialogue("LumbridgeCook", npc.getId());
				} else if (npc.getId() == 926) {
					player.getDialogueManager().startDialogue("BorderGuard", npc.getId());
				} else if (npc.getId() == 836) {
					player.getDialogueManager().startDialogue("Shantay", npc.getId());
				} else if (npc.getId() == 881) {
					player.getDialogueManager().startDialogue("Traiborn", npc.getId());
				} else if (npc.getId() == 1263) {
					player.getDialogueManager().startDialogue("Wizard", npc.getId());
				} else if (npc.getId() == 2205) {
					player.getDialogueManager().startDialogue("DwarvenBoatman", npc.getId());
				} else if (npc.getId() == 2180) {
					player.getDialogueManager().startDialogue("Conductor", npc.getId());
				} else if (npc.getId() == 3781) {
					player.getDialogueManager().startDialogue("Squire", npc.getId());
				} else if (npc.getId() == 1843) {
					player.useStairs(-1, new Position(2836, 10143, 0), 3, 4);
				} else if (npc.getId() == 15460) {
					player.getDialogueManager().startDialogue("SirRebrum", npc.getId());
				} else if (npc.getId() == 2208 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Lumdo", npc.getId());
				} else if (npc.getId() == 925 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("BorderGuard", npc.getId());
				} else if (npc.getId() == 7969 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ExplorerJack", npc.getId());
				} else if (npc.getId() == 3777 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Doomsayer", npc.getId());
				} else if (npc.getId() == 2244 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("LumbridgeSage", npc.getId());
				} else if (npc.getDefinitions().name.contains("Shopkeeper")  && !player.getGameMode().isIronman() || npc.getDefinitions().name.contains("Shop assistant")  && !player.getGameMode().isIronman() || npc.getId() == 7048 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 55);
				} else if (npc.getId() == 246 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("BeefyBill", npc.getId());
				} else if (npc.getDefinitions().name.contains("Osman") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Osman", npc.getId());
				} else if (npc.getId() == 542 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("LouieLegs", npc.getId());
				} else if (npc.getId() == 1718 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("JimmyChisel", npc.getId());
				} else if (npc.getId() >= 2897 && npc.getId() <= 2900) {
					player.getDialogueManager().startDialogue("FatherReen", npc.getId());
				} else if (npc.getId() == 3806) {
					player.getDialogueManager().startDialogue("MillieMiller", npc.getId());
				} else if(npc.getId() == 0) {
					player.getDialogueManager().startDialogue("Hans", 0);
				} else if (npc.getId() == 6539 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 54);
				} else if (npc.getId() == 15533 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 56);
				} else if (npc.getId() == 13727) {
					player.getDialogueManager().startDialogue("Xuans", npc.getId());
				} else if (npc.getId() == 9707 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue(
							"FremennikShipmaster", npc.getId(), true);
				} else if (npc.getId() == 9708 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue(
							"FremennikShipmaster", npc.getId(), false);
				} else if (npc.getId() == 8031 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Acanatha",
							npc.getId());
				} else if (npc.getId() == 3801 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Squire",
							npc.getId());
				} else if (npc.getDefinitions().name.contains("H.A.M. Member")) {
					player.getDialogueManager().startDialogue("Ham", npc.getId());
				} else if (npc.getId() ==  5626) {
					player.getDialogueManager().startDialogue("ZombieMonk",
							npc.getId(), null);
				} else if (npc.getId() == 11226) {
					player.getDialogueManager().startDialogue("DungLeaving");
					player.lock(3);
				} /*else if (npc.getId() == 239 && !player.getGameMode().isIronman()) {
					DungeonRewards.openRewardsShop(player);
				}*/ else if (npc.getId() == 6537 && !player.getGameMode().isIronman()) {
					player.sm("This NPC's shop is under construction!");
				} else if (npc.getId() == 8091 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("StarSprite");
				} else if (npc.getId() == 2676) {
					PlayerLook.openMageMakeOver(player);
				} else if (npc.getId() == 15417 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RuneSpanStore", npc.getId());
				} else if (npc.getId() == 8462 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Spria", false);
				} else if (npc.getId() == 895 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("EstateSeller", npc.getId());
				} else if (npc.getId() == 15418) {
					player.getDialogueManager().startDialogue("RuneSpanLeaving", npc.getId());
				} else if (npc.getId() == 8464 ) {
					player.getDialogueManager().startDialogue("Mazchna", false);
				} else if (npc.getId() == 598) {
					player.getDialogueManager().startDialogue("Hairdresser", npc.getId());
				} else if (npc.getId() == 100) {
					player.getDialogueManager().startDialogue("FreakyForester",
							npc.getId());
				} else if(npc.getId() == 2262) {
					player.getDialogueManager().startDialogue("PrestigeOne");
				} else if (npc.getId() == 4247) {
					player.getDialogueManager().startDialogue("EstateAgent", npc.getId());
				} else if (npc.getId() == 15563 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ForumPoint");
				} else if (npc.getId() == 2725 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("OttoGodblessed");
				} else if (npc.getId() == 3218 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("BlueMoonInn");
				} else if (npc.getId() == 3217 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RisingSun2");
				} else if (npc.getId() == 848) {
					player.getDialogueManager().startDialogue("BlurberrysBar");
				} else if (npc.getId() == 735) {
					player.getDialogueManager().startDialogue("DeadMansChest");
				} else if (npc.getId() == 739) {
					player.getDialogueManager().startDialogue("DragonInn");
				} else if (npc.getId() == 738) {
					player.getDialogueManager().startDialogue("FlyingHorseInn");
				} else if (npc.getId() == 737) {
					player.getDialogueManager().startDialogue("ForestersArms");
				} else if (npc.getId() == 731) {
					player.getDialogueManager().startDialogue("JollyBoarInn");
				} else if (npc.getId() == 568) {
					player.getDialogueManager().startDialogue("KaramjaSpiritsBar");
				} else if (npc.getId() == 736) {
					player.getDialogueManager().startDialogue("RisingSunInn");
				} else if (npc.getId() == 734) {
					player.getDialogueManager().startDialogue("RustyAnchorBar");
				} else if (npc.getId() == 519 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Bob", npc.getId());
				} else if (npc.getId() == 15811 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SkillingShops", npc.getId());
				} else if (npc.getId() == 15402 && !player.getGameMode().isIronman()) {
					player.getInventory().addItem(24227, 500);
				} else if(npc.getId() == 2253 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("PrestigeOne");
				} else if (npc.getId() == 7888 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Ozan", npc.getId());
				} else if(npc.getId() == 6524 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("BobBarterD", npc.getId());
				} /*else if (npc.getId() == 9711 && !player.getGameMode().isIronman()) {
					DungeonRewards.openRewardsShop(player);
				}*/ else if (npc.getId() == 548 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Thessalia", npc.getId());
				} else if (npc.getId() == 837 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ShantayGuard", npc.getId());
				} else if(npc.getId() == 659) {
					player.getDialogueManager().startDialogue("PartyPete");
				} else if (npc.getId() == 6988 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SummoningShop", npc.getId());
				} else if (npc.getId() == 14866 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SummoningShop", npc.getId());
				} else if (npc.getId() == 579 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("DrogoDwarf", npc.getId());
				} else if (npc.getId() == 582 && !player.getGameMode().isIronman()) {
					if (player.getGameMode().isIronman()) {
						ShopsHandler.openShop(player, 110);
					} else {
						player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 1);
					}
				} else if (npc.getId() == 528 || npc.getId() == 529) {
					if (player.getGameMode().isIronman()) {
						ShopsHandler.openShop(player, 110);
					} else {
						player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 1);
					}
				} else if (npc.getId() == 522  && !player.getGameMode().isIronman() || npc.getId() == 523 && !player.getGameMode().isIronman()) {
					if (player.getGameMode().isIronman()) {
						ShopsHandler.openShop(player, 110);
					} else {
						player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 1);
					}
				} else if (npc.getId() == 520 && !player.getGameMode().isIronman() || npc.getId() == 521 && !player.getGameMode().isIronman()) {
					if (player.getGameMode().isIronman()) {
						ShopsHandler.openShop(player, 110);
					} else {
						player.getDialogueManager().startDialogue("GeneralStore", npc.getId(), 1);
					}
				} else if (npc.getId() == 594 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Nurmof", npc.getId());
				} else if (npc.getId() == 665 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("BootDwarf", npc.getId());
				} else if (npc.getId() == 382 || npc.getId() == 3294 || npc.getId() == 4316 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), false);
				} else if (npc.getId() == 3295 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), true);
				} else if (npc.getId() == 537 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Scavvo", npc.getId());
				} else if (npc.getId() == 4288 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Ajjat", npc.getId());
				} else if (npc.getId() == 7868 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Iain", npc.getId());
				} else if (npc.getId() == 4904 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ApprenticeSmith", npc.getId());
				} else if (npc.getId() == 4903 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("PriestYauchomi", npc.getId());
				} else if (npc.getId() == 4297 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Sloane", npc.getId());
				} else if (npc.getId() == 705 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MeleeInstructor", npc.getId());
				} else if (npc.getId() == 7870 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Lachtopher", npc.getId());
				} else if (npc.getId() == 1861 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RangedInstructor", npc.getId());
				} else if (npc.getId() == 946  && !player.getGameMode().isIronman() || npc.getId() == 4707 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MagicInstructor", npc.getId());
				} else if (npc.getId() == 682 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("ArmourSalesman", npc.getId());
				} else if (npc.getId() == 802 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("BrotherJered", npc.getId());
				} else if (npc.getId() == 1658 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RobeStoreOwner", npc.getId());
				} else if (npc.getId() == 13632 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Larriar", npc.getId());
				} else if (npc.getId() == 961 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("SurgeonGeneralTafani", npc.getId());
				} else if (npc.getId() == 437 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("CapnIzzyNoBeard", npc.getId());
				} else if (npc.getId() == 455 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Kaqemeex", npc.getId());
				} else if (npc.getId() == 2270 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MartinThwait", npc.getId());
				} else if (npc.getId() == 805 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MasterCrafter", npc.getId());
				} else if (npc.getId() == 575 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Hickton", npc.getId());
				} else if (npc.getId() == 5113 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("HuntingExpert", npc.getId());
				} else if (npc.getId() == 4247 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("EstateAgent", npc.getId());
				} else if (npc.getId() == 604 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Thurgo", npc.getId());
				} else if (npc.getId() == 308 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MasterFisher", npc.getId());
				} else if (npc.getId() == 847 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("HeadChef", npc.getId());
				} else if (npc.getId() == 4946 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("IgnatiusVulcan", npc.getId());
				} else if (npc.getId() == 4906 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Wilfred", npc.getId());
				} else if (npc.getId() == 3299 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MartintheMasterGardener", npc.getId());
				} else if (npc.getId() == 6970 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Pikkupstix", npc.getId());
				} else if (npc.getId() == 536 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Valaine", npc.getId());
				} else if (npc.getId() == 4563 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Hura", npc.getId());
				} else if (npc.getId() == 2617 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("TzHaarMejJal", npc.getId());
				} else if (npc.getId() == 2618 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("TzHaarMejKah", npc.getId());
				} else if(npc.getId() == 15149 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("MasterOfFear", 0);
				} else if (npc.getId() == 2417 ) {
					WildyWyrm.handleStomping(player, npc);
				} else if (npc.getId() == 5160 || npc.getId() == 43 || npc.getId() == 5161
						|| npc.getId() == 5156 || npc.getId() == 8876 || npc.getId() == 5168) {
					player.faceEntity(npc);
					if (!player.withinDistance(npc, 2)) {
						return;
					}
					npc.setNextForceTalk(new ForceTalk("baaaa!"));
					player.getActionManager().setAction(new SheepShearing());
					return;
				} else if (npc instanceof Pet) {
					Pet pet = (Pet) npc;
					if (pet != player.getPet()) {
						player.getPackets().sendGameMessage("This isn't your pet.");
						return;
					}
					player.animate(new Animation(827));
					pet.pickup();

				} else {
					if (Constants.DEBUG) {
						System.out.println("cliked 1 at npc id : "
								+ npc.getId() + ", " + npc.getX() + ", "
								+ npc.getY() + ", " + npc.getZ());
						Logger.logMessage("cliked 1 at npc id : "
								+ npc.getId() + ", " + npc.getX() + ", "
								+ npc.getY() + ", " + npc.getZ());
					}
				}
			}
		}, npc.getSize()));
	}

	public static void handleOption2(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.isFinished() || !player.finishedStarter
				|| !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		player.stopAll(false);
		if(forceRun) {
			player.setRun(forceRun);
		}
		if (npc.getId() == 745) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 4)) {
				return;
			}
			npc.faceEntity(player);
			player.getDialogueManager().startDialogue("Wormbrain", npc.getId());
			return;
		}
		if (npc.getDefinitions().name.toLowerCase().equals("grand exchange clerk")) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2))
				return;
			npc.faceEntity(player);
			player.getGeManager().openGrandExchange();
			return;
		}
		if (npc.getDefinitions().name.contains("Banker")
				|| npc.getDefinitions().name.contains("banker") || npc.getId() == 902) {
			player.faceEntity(npc);
			npc.faceEntity(player);
			player.getBank().openBank();
			return;
		} if (npc.getId() == 6362) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2)) {
				return;
			}
			npc.faceEntity(player);
			player.getBank().openBank();
			return;
		}
		if (npc instanceof GraveStone) {
			GraveStone grave = (GraveStone) npc;
			grave.repair(player, false);
			return;
		}

		if (npc.getClickOptionListener()[1] != null)
			npc.getClickOptionListener()[1].handle(player, npc);

		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				player.faceEntity(npc);
				FishingSpots spot = FishingSpots.forId(npc.getId() | 2 << 24);
				if (spot != null) {
					player.getActionManager().setAction(new Fishing(spot, npc));
					return;
				}
				PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
				if (pocket != null) {
					player.getActionManager().setAction(
							new PickPocketAction(npc, pocket));
					return;
				}
				switch (npc.getDefinitions().name.toLowerCase()) {
				case "void knight":
					CommendationExchange.openExchangeShop(player);
					break;
				}
				if (npc instanceof Familiar) {
					if (npc.getDefinitions().hasOption("store")) {
						if (player.getFamiliar() != npc) {
							player.getPackets().sendGameMessage(
									"That isn't your familiar.");
							return;
						}
						player.getFamiliar().store();
					} else if (npc.getDefinitions().hasOption("cure")) {
						if (player.getFamiliar() != npc) {
							player.getPackets().sendGameMessage(
									"That isn't your familiar.");
							return;
						}
						if (!player.getToxin().poisoned()) {
							player.getPackets().sendGameMessage(
									"Your arent poisoned or diseased.");
							return;
						} else {
							player.getFamiliar().drainSpecial(2);
							player.getToxin().applyImmunity(ToxinType.POISON, 120);
						}
					}
					return;
				}
				npc.faceEntity(player);
				if (!player.getControlerManager().processNPCClick2(npc)) {
					return;
				}
				if(npc.getDefinitions().name.contains("Musician") || npc.getId() == 3509) {
					player.getDialogueManager().startDialogue("Musicians", npc.getId()); //All musicians around the world.
					return;
				} else if (npc.getId() == 9707) {
					FremennikShipmaster.sail(player, true);
				} else if (npc.getId() == 9711) {
					DungeonRewardShop.openRewardsShop(player);
				} else if (npc.getId() == 9708) {
					FremennikShipmaster.sail(player, false);
				} else if (npc.getId() == 659) {
					player.getDialogueManager().startDialogue("pointShops", npc);
				} else if (npc.getId() == 3705) {
					ShopsHandler.openShop(player, 150);
				} else if (npc.getId() == 4650) {
					ShopsHandler.openShop(player, 170);
				} else if (npc.getId() == 36) {
					ShopsHandler.openShop(player, 171);
				} else if (npc.getId() == 4251) {
					ShopsHandler.openShop(player, 173);
				} else if (npc.getId() >= 2291 && npc.getId() <= 2294 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RugMerchant", true);
				} else if (npc.getDefinitions().name.contains("ool leprech") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("seeds", npc.getId());
				} else if (npc.getId() == 528  && !player.getGameMode().isIronman() || npc.getId() == 529 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 1);
				} else if (npc.getId() == 1686) {
					if (player.getInventory().hasFreeSlots() && player.unclaimedEctoTokens > 0) {
						player.unclaimedEctoTokens = 0;
					}

				}
				//shop start

				if (npc.getId() == 5198)//ava's odd and ends
					ShopsHandler.openShop(player, 1224);
				else if (npc.getId() == 4251) //Garden Centre
					ShopsHandler.openShop(player, 1130);
				else if (npc.getId() == 3799)
					ShopsHandler.openShop(player, 1122);
				else if (npc.getId() == 471) // tree gnome village general store
					ShopsHandler.openShop(player, 1124);
				else if (npc.getId() == 9159)//faruq's tools for games
					ShopsHandler.openShop(player, 1123);
				else if (npc.getId() == 2291)
					player.getDialogueManager().startDialogue("RugMerchantD", true, 0);
				else if (npc.getId() == 519)//bobs brilliant axes
					ShopsHandler.openShop(player, 222);
				else if (npc.getId() == 520 || npc.getId() == 521)//lumby gen store
					ShopsHandler.openShop(player, 224);
				else if (npc.getId() == 538)//peksa's helmet store
					ShopsHandler.openShop(player, 226);
				else if (npc.getId() == 522 || npc.getId() == 523)//varrock general store
					ShopsHandler.openShop(player, 228);
				else if (npc.getId() == 797)//happy heroe's h'emporium
					ShopsHandler.openShop(player, 1115);
				else if (npc.getId() == 546)// zaff superior staffs
					ShopsHandler.openShop(player, 2210);
				else if (npc.getId() == 4293)// warriors guild food shop
					ShopsHandler.openShop(player, 1120);
				else if (npc.getId() == 516)// obli's general store
					ShopsHandler.openShop(player, 1125);
				else if (npc.getId() == 517)//fernahei's fishing hut
					ShopsHandler.openShop(player, 1126);
				else if (npc.getId() == 4294)//warriors guild potion shop
					ShopsHandler.openShop(player, 1121);
				else if (npc.getId() == 4295)//warriors guild food shop
					ShopsHandler.openShop(player, 1119);
				else if (npc.getId() == 14620)//polypore dungeon supplies
					ShopsHandler.openShop(player, 1118);
				else if (npc.getId() == 535 || npc.getId() == 534)//zanari's generel store
					ShopsHandler.openShop(player, 2255);
				else if (npc.getId() == 836)//shantay pass shop
					ShopsHandler.openShop(player, 2292);
				else if (npc.getId() == 2352)//culinaromancers chest
					ShopsHandler.openShop(player, 2274);
				else if (npc.getId() == 2259)//battle runes
					ShopsHandler.openShop(player, 1112);
				else if (npc.getId() == 1917)//bandits bargains
					ShopsHandler.openShop(player, 1106);
				else if (npc.getId() == 932)//legends guild general store
					ShopsHandler.openShop(player, 1113);
				else if (npc.getId() == 933)//legends guild shop of useful items
					ShopsHandler.openShop(player, 1114);
				else if (npc.getId() == 747)//oziachs armor
					ShopsHandler.openShop(player, 227);
				else if (npc.getId() == 2353)//lletya seamstress
					ShopsHandler.openShop(player, 2275);
				else if (npc.getId() == 2356)//lletya archery store
					ShopsHandler.openShop(player, 2276);
				else if (npc.getId() == 3166)//dodgy mike's second-hand clothing
					ShopsHandler.openShop(player, 2289);
				else if (npc.getId() == 3161)// two feet charley's fish shop
					ShopsHandler.openShop(player, 2290);
				else if (npc.getId() == 2154)//gunslik's assorted items
					ShopsHandler.openShop(player, 1105);
				else if (npc.getId() == 2160)//pickaxe is mines
					ShopsHandler.openShop(player, 2299);
				else if (npc.getId() == 2151)//vigr's warhammers
					ShopsHandler.openShop(player, 1104);
				else if (npc.getId() == 563)//achein's store
					ShopsHandler.openShop(player, 1108);
				else if (npc.getId() == 576)//harry's fishing shop
					ShopsHandler.openShop(player, 1110);
				else if (npc.getId() == 562)//candle shop
					ShopsHandler.openShop(player, 1107);
				else if (npc.getId() == 575)//hickston's archery emporium
					ShopsHandler.openShop(player, 1109);
				else if (npc.getId() == 2305)//vaness'as farming shop
					ShopsHandler.openShop(player, 1111);
				else if (npc.getId() == 2161)// agmundie guality clothes
					ShopsHandler.openShop(player, 1103);
				else if (npc.getId() == 2152)//guality weapons shop
					ShopsHandler.openShop(player, 1100);
				else if (npc.getId() == 2153)//quality armor shop
					ShopsHandler.openShop(player, 1101);
				else if (npc.getId() == 4248)//kaldagrim stonemason
					ShopsHandler.openShop(player, 1102);
				else if (npc.getId() == 3162)//smithing smiths shop
					ShopsHandler.openShop(player, 2291);
				else if (npc.getId() == 564)//jukat's dragon sword shop
					ShopsHandler.openShop(player, 2253);
				else if (npc.getId() == 4516)// moon clan general store
					ShopsHandler.openShop(player, 2270);
				else if (npc.getId() == 4518)//moon clan fine clothes
					ShopsHandler.openShop(player, 2266);
				else if (npc.getId() == 566)// irksol's ruby rings
					ShopsHandler.openShop(player, 2252);
				else if (npc.getId() == 540)//gem trader
					ShopsHandler.openShop(player, 2261);
				else if (npc.getId() == 541)// zeke's superior scimitars
					ShopsHandler.openShop(player, 2262);
				else if (npc.getId() == 542)// louie's armor legs
					ShopsHandler.openShop(player, 2263);
				else if (npc.getId() == 544)// ranael's super skirt store
					ShopsHandler.openShop(player, 2264);
				else if (npc.getId() == 2620) // TzHaar-Hur-Tel's Equipment
					// Store
					ShopsHandler.openShop(player, 2277);
				else if (npc.getId() == 2622) // TzHaar-Hur-Lek's Ore and Gem
					// Store
					ShopsHandler.openShop(player, 2278);
				else if (npc.getId() == 2623) // TzHaar-Mej-Roh's Rune Store
					ShopsHandler.openShop(player, 2279);
				else if (npc.getId() == 583) // Betty's Magic Emporium
					ShopsHandler.openShop(player, 2267);
				else if (npc.getId() == 587) // Jatix's Herblore Shop
					ShopsHandler.openShop(player, 2269);
				else if (npc.getId() == 545)// domik's crafting store
					ShopsHandler.openShop(player, 2265);
				else if (npc.getId() == 5510)// aleck's hunter emporium
					ShopsHandler.openShop(player, 2256);
				else if (npc.getId() == 11475)//scimitar emporium
					ShopsHandler.openShop(player, 229);
				else if (npc.getId() == 1658)//magic guild store
					ShopsHandler.openShop(player, 2258);
				else if (npc.getId() == 461)//magic guild store
					ShopsHandler.openShop(player, 2259);
				else if (npc.getId() == 593)//cookey shop
					ShopsHandler.openShop(player, 2260);
				else if (npc.getId() == 5113) // Hunter Expert's shop
					ShopsHandler.openShop(player, 2222);
				else if (npc.getId() == 588) // Davon's Amulet Store
					ShopsHandler.openShop(player, 1133);
				else if (npc.getId() == 1860) // Brian's Archery Supplies
					ShopsHandler.openShop(player, 1134);
				else if (npc.getId() == 683) // Dargaud's Bows and Arrows
					ShopsHandler.openShop(player, 1135);
				else if (npc.getId() == 682) // Archery Appendages
					ShopsHandler.openShop(player, 1136);
				else if (npc.getId() == 4558) // Crossbow Shop by Hirko
					ShopsHandler.openShop(player, 2233);
				else if (npc.getId() == 4559) // Crossbow Shop by Holoy
					ShopsHandler.openShop(player, 2233);
				else if (npc.getId() == 581) // Wayne's chains
					ShopsHandler.openShop(player, 1137);
				else if (npc.getId() == 554) // Fancy Clothes Store
					ShopsHandler.openShop(player, 1138);
				else if (npc.getId() == 601) // Rometti's Fine Fashions
					ShopsHandler.openShop(player, 1139);
				else if (npc.getId() == 3921) // Miscellanian Clothes Shop
					ShopsHandler.openShop(player, 1140);
				else if (npc.getId() == 3205) // Pie Shop (Varrock mill)
					ShopsHandler.openShop(player, 1141);
				else if (npc.getId() == 600) // Grand Tree Groceries
					ShopsHandler.openShop(player, 1142);
				else if (npc.getId() == 603) // Funch's Fine Groceries
					ShopsHandler.openShop(player, 1143);
				else if (npc.getId() == 585) // Rommik's Crafty Supplies
					ShopsHandler.openShop(player, 1144);
				else if (npc.getId() == 5268) // Jamila's Craft Stall
					ShopsHandler.openShop(player, 1145);
				else if (npc.getId() == 1437) // Hamab's Crafting Emporium
					ShopsHandler.openShop(player, 1146);
				else if (npc.getId() == 2307) // Alice's Farming Shop
					ShopsHandler.openShop(player, 1147);
				else if (npc.getId() == 14860) // Head Farmer Jones's Farming shop
					ShopsHandler.openShop(player, 1148);
				else if (npc.getId() == 2306) // Richard's Farming Shop
					ShopsHandler.openShop(player, 1149);
				else if (npc.getId() == 2304) // Sarah's Farming Shop
					ShopsHandler.openShop(player, 1150);
				else if (npc.getId() == 8864) // Lumbridge Fishing Supplies
					ShopsHandler.openShop(player, 1151);
				else if (npc.getId() == 14879) // Nicholas Angle's Fishing Shop
					ShopsHandler.openShop(player, 1152);
				else if (npc.getId() == 592) // Fishing Guild Shop
					ShopsHandler.openShop(player, 1153);
				else if (npc.getId() == 1393) // Island Fishmonger
					ShopsHandler.openShop(player, 1155);
				else if (npc.getId() == 1369) // Island Fishmonger (Etcetria)
					ShopsHandler.openShop(player, 1156);
				else if (npc.getId() == 3824) // Arnold's Eclectic Supplies
					ShopsHandler.openShop(player, 1157);
				else if (npc.getId() == 571) // Bakery Stall
					ShopsHandler.openShop(player, 1158);
				else if (npc.getId() == 7054) // Fresh Meat
					ShopsHandler.openShop(player, 1159);
				else if (npc.getId() == 851) // Gianne's Restaurant
					ShopsHandler.openShop(player, 1160);
				else if (npc.getId() == 5487) // Keepa Kettilon's Store
					ShopsHandler.openShop(player, 1161);
				else if (npc.getId() == 3923) // Miscellanian Food Shop
					ShopsHandler.openShop(player, 1162);
				else if (npc.getId() == 5264) // Nathifa's Bake Stall
					ShopsHandler.openShop(player, 1163);
				else if (npc.getId() == 793) // The Shrimp and Parrot
					ShopsHandler.openShop(player, 1164);
				else if (npc.getId() == 1433) // Solihib's Food Stall
					ShopsHandler.openShop(player, 1165);
				else if (npc.getId() == 596) // Tony's Pizza Bases
					ShopsHandler.openShop(player, 1166);
				else if (npc.getId() == 584) // Tony's Pizza Bases
					ShopsHandler.openShop(player, 1167);
				else if (npc.getId() == 570) // Ardougne Gem Stall
					ShopsHandler.openShop(player, 1168);
				else if (npc.getId() == 578) // Frincos's Fabulous Herb Store
					ShopsHandler.openShop(player, 1169);
				else if (npc.getId() == 874) // Grud's Herblore Stall
					ShopsHandler.openShop(player, 1170);
				else if (npc.getId() == 5109) // Nardah Hunter Shop
					ShopsHandler.openShop(player, 1171);
				else if (npc.getId() == 14864) // Ayleth Beaststalker's Hunting Supplies Shop
					ShopsHandler.openShop(player, 1172);
				else if (npc.getId() == 2198) // Kjut's Kebabs
					ShopsHandler.openShop(player, 1173);
				else if (npc.getId() == 580) // Flynn's Mace Market
					ShopsHandler.openShop(player, 1174);
				else if (npc.getId() == 14906) // Carwen Essencebinder Magical Runes Shop
					ShopsHandler.openShop(player, 1175);
				else if (npc.getId() == 4513) // Baba Yaga's Magic Shop
					ShopsHandler.openShop(player, 1176);
				else if (npc.getId() == 1435) // Tutab's Magical Market
					ShopsHandler.openShop(player, 1177);
				else if (npc.getId() == 589) // Zenesha's Platebody Shop
					ShopsHandler.openShop(player, 1178);
				else if (npc.getId() == 3038) // Seddu's Adventurers' Store
					ShopsHandler.openShop(player, 1179);
				else if (npc.getId() == 1434) // Daga's Scimitar Smithy
					ShopsHandler.openShop(player, 1180);
				else if (npc.getId() == 577) // Cassie's Shield Shop
					ShopsHandler.openShop(player, 1181);
				else if (npc.getId() == 569) // Ardougne Silver Stall
					ShopsHandler.openShop(player, 1182);
				else if (npc.getId() == 2159) // Silver Cog Silver Stall
					ShopsHandler.openShop(player, 1183);
				else if (npc.getId() == 1980) // The Spice is Right
					ShopsHandler.openShop(player, 1184);
				else if (npc.getId() == 4472) // summoning supplies
					ShopsHandler.openShop(player, 1185);
				else if (npc.getId() == 5266) // Blades by Urbi
					ShopsHandler.openShop(player, 1186);
				else if (npc.getId() == 586) // Gaius's Two-Handed Shop
					ShopsHandler.openShop(player, 1187);
				else if (npc.getId() == 602) // Gulluck and Sons
					ShopsHandler.openShop(player, 1188);
				else if (npc.getId() == 692) // Authentic Throwing Weapons
					ShopsHandler.openShop(player, 1189);
				else if (npc.getId() == 4312) // Nardok's Bone Weapons
					ShopsHandler.openShop(player, 1190);
				else if (npc.getId() == 1167) // Tamayu's Spear Stall
					ShopsHandler.openShop(player, 1191);
				else if (npc.getId() == 5486) // Weapons Galore
					ShopsHandler.openShop(player, 1192);
				else if (npc.getId() == 1370 || npc.getId() == 1394) // Vegetable stall
					ShopsHandler.openShop(player, 1193);
				else if (npc.getId() == 524 || npc.getId() == 525) // Al Kharid General Store
					ShopsHandler.openShop(player, 1194);
				else if (npc.getId() == 1436) // Ape Atoll General Store
					ShopsHandler.openShop(player, 1195);
				else if (npc.getId() == 590 || npc.getId() == 591) // East Ardougne General Store
					ShopsHandler.openShop(player, 1196);
				else if (npc.getId() == 971) // West ardougne General Store
					ShopsHandler.openShop(player, 1197);
				else if (npc.getId() == 597) // Bandit Duty Free General Store
					ShopsHandler.openShop(player, 1198);
				else if (npc.getId() == 3541) // Aurel's Supplies General Store
					ShopsHandler.openShop(player, 1199);
				else if (npc.getId() == 5798) // Dorgesh-Kaan General Supplies General Store
					ShopsHandler.openShop(player, 1200);
				else if (npc.getId() == 526 || npc.getId() == 527) // Falador General Store
					ShopsHandler.openShop(player, 1201);
				else if (npc.getId() == 11674 || npc.getId() == 11678) // Karamja General Storee
					ShopsHandler.openShop(player, 1202);
				else if (npc.getId() == 1334) // The Lighthouse Store General Store
					ShopsHandler.openShop(player, 1203);
				else if (npc.getId() == 1334) // Trader Sven's Black Market Goods
					ShopsHandler.openShop(player, 1204);
				else if (npc.getId() == 1254) // Razmire General Store
					ShopsHandler.openShop(player, 1205);
				else if (npc.getId() == 2086) // Nardah General Store
					ShopsHandler.openShop(player, 1206);
				else if (npc.getId() == 1866) // Pollnivneach General Store
					ShopsHandler.openShop(player, 1207);
				else if (npc.getId() == 605) // White Knight Master Armoury
					ShopsHandler.openShop(player, 1208);
				else if (npc.getId() == 573) // Fur Trader
					ShopsHandler.openShop(player, 1209);
				else if (npc.getId() == 572) // Ardougne Spice Stall
					ShopsHandler.openShop(player, 1210);
				else if (npc.getId() == 14862) // Alfred Stonemason's Construction Shop
					ShopsHandler.openShop(player, 1211);
				else if (npc.getId() == 14858) // Alison Elmshaper's Flying Arrow Fletching Shop
					ShopsHandler.openShop(player, 1212);
				else if (npc.getId() == 14885) // Will Oakfeller's Woodcutting Supplies Shop
					ShopsHandler.openShop(player, 1213);
				else if (npc.getId() == 14883) // Marcus Everburn's Firemaking Shop
					ShopsHandler.openShop(player, 1214);
				else if (npc.getId() == 14874) // Martin Steelweaver's Smithing Supplies Shop
					ShopsHandler.openShop(player, 1215);
				else if (npc.getId() == 14870) // Tobias Bronzearms's Mining Supplies Shop
					ShopsHandler.openShop(player, 1216);
				else if (npc.getId() == 14877) // Jack Oval's crafting Shop
					ShopsHandler.openShop(player, 1217);
				else if (npc.getId() == 555) // Khazard General Store
					ShopsHandler.openShop(player, 1218);
				else if (npc.getId() == 1699) // Port Phasmatys General Store
					ShopsHandler.openShop(player, 1219);
				else if (npc.getId() == 531 || npc.getId() == 530) // Rimmington General Store
					ShopsHandler.openShop(player, 1220);
				else if (npc.getId() == 560) // Jiminua's Jungle Store
					ShopsHandler.openShop(player, 1221);
				else if (npc.getId() == 4946) // Ignatius's Hot Deals
					ShopsHandler.openShop(player, 1227);
				else if (npc.getId() == 2270) // Martin Thwait's Lost and Found
					ShopsHandler.openShop(player, 1229);
				else if (npc.getId() == 14868) // Jacquelyn Manslaughter
					ShopsHandler.openShop(player, 2229);
				else if (npc.getId() == 1778) // Team capes
					ShopsHandler.openShop(player, 1232);
				else if (npc.getId() == 1779) // Team capes
					ShopsHandler.openShop(player, 1233);
				else if (npc.getId() == 1780) // Team capes
					ShopsHandler.openShop(player, 1234);
				else if (npc.getId() == 1781) // Team capes
					ShopsHandler.openShop(player, 1235);
				else if (npc.getId() == 1782) // Team capes
					ShopsHandler.openShop(player, 1236);
				else if (npc.getId() == 1783) // Team capes
					ShopsHandler.openShop(player, 1237);
				else if (npc.getId() == 1784) // Team capes
					ShopsHandler.openShop(player, 1238);
				else if (npc.getId() == 1785) // Team capes
					ShopsHandler.openShop(player, 1239);
				else if (npc.getId() == 1786) // Team capes
					ShopsHandler.openShop(player, 1240);
				else if (npc.getId() == 1787) // Team capes
					ShopsHandler.openShop(player, 1241);
				else if (npc.getId() == 2961) // Ali's Discount Wares
					ShopsHandler.openShop(player, 1242);
				else if (npc.getId() == 1301)//yra's accountrements
					ShopsHandler.openShop(player, 2250);
				else if (npc.getId() == 551 || npc.getId() == 552)//varrock sword shop
					ShopsHandler.openShop(player, 2213);
				else if (npc.getId() == 550)// lowe's archery emporium
					ShopsHandler.openShop(player, 2214);
				else if (npc.getId() == 549)//horvik's armor shop
					ShopsHandler.openShop(player, 2215);
				else if (npc.getId() == 548)
					ShopsHandler.openShop(player, 2218); // thesalia
				else if (npc.getId() == 2233 || npc.getId() == 3671)
					ShopsHandler.openShop(player, 2220);
				else if (npc.getId() == 970)
					ShopsHandler.openShop(player, 2221);
				else if (npc.getId() == 579) // Drogo's mining Emporium
					ShopsHandler.openShop(player, 2230);
				else if (npc.getId() == 582) // dwarves general store
					ShopsHandler.openShop(player, 2231);
				else if (npc.getId() == 1040)//general store
					ShopsHandler.openShop(player, 2281);
				else if (npc.getId() == 1039)//barker's
					ShopsHandler.openShop(player, 2282);
				else if (npc.getId() == 1038)//rufus meat
					ShopsHandler.openShop(player, 2283);
				else if (npc.getId() == 558) // Gerrant's Fishy Business
					ShopsHandler.openShop(player, 2284);
				else if (npc.getId() == 556) // Grum's Gold Exchange
					ShopsHandler.openShop(player, 2285);
				else if (npc.getId() == 559) // Brian's Battleaxe Bazaar
					ShopsHandler.openShop(player, 2286);
				else if (npc.getId() == 557) // Wydin's Food Store
					ShopsHandler.openShop(player, 2287);
				else if (npc.getId() >= 4650 && npc.getId() <= 4656 || npc.getId() == 7065 || npc.getId() == 7066) // Trader
					// Stan's
					// Trading
					// Post
					ShopsHandler.openShop(player, 2288);
				else if (npc.getId() == 209) // cannon shop
					ShopsHandler.openShop(player, 2234);
				else if (npc.getId() == 1334) // The Lighthouse Store
					ShopsHandler.openShop(player, 2236);
				else if (npc.getId() == 594) // Nurmof's Pickaxe Shop
					ShopsHandler.openShop(player, 2232);
				else if (npc.getId() == 537) // Scavvo's Rune Store
					ShopsHandler.openShop(player, 2212);
				else if (npc.getId() == 536) // Valaine's Shop of Champions
					ShopsHandler.openShop(player, 2217);
				else if (npc.getId() == 4563) // Crossbow Shop
					ShopsHandler.openShop(player, 2233);
				else if (npc.getId() == 6070)
					ShopsHandler.openShop(player, 2254);
				else if (npc.getId() == 904)
					ShopsHandler.openShop(player, 2237);
				else if (npc.getId() == 1303)
					ShopsHandler.openShop(player, 2242);
				else if (npc.getId() == 903)
					ShopsHandler.openShop(player, 2238);
				else if (npc.getId() == 6988)
					ShopsHandler.openShop(player, 2239);
				else if (npc.getId() == 1316)
					ShopsHandler.openShop(player, 2243);
				else if (npc.getId() == 1315 || npc.getId() == 1315 || npc.getId() == 1315)
					ShopsHandler.openShop(player, 2245);
				else if (npc.getId() == 5485)
					ShopsHandler.openShop(player, 2247);
				else if (npc.getId() == 5483)
					ShopsHandler.openShop(player, 2246);
				else if (npc.getId() == 5509)
					ShopsHandler.openShop(player, 2248);
				else if (npc.getId() == 3798)
					ShopsHandler.openShop(player, 2240);
				else if (npc.getId() == 3796)
					ShopsHandler.openShop(player, 2241);
				else if (npc.getId() == 1282)
					ShopsHandler.openShop(player, 2244);
				//shop end
				else if (npc.getId() == 9159) {
					npc.setNextForceTalk(new ForceTalk("I TOLD YOU I HAVE NOTHING TO SELL!!!!"));
				} else if (npc.getId() == 13455 || npc.getId() == 2617 || npc.getId() == 2618
						|| npc.getId() == 15194) {
					player.getBank().openBank();
				} else if (npc.getId() >= 3809 && npc.getId() <= 3812 || npc.getId() == 1800) {
					player.getInterfaceManager().sendInterface(138);
				} else if (npc.getId() == 5915) {
					player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20709);
				} else if (npc.getId() == 13633) {
					player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20708);
				} else if (SlayerMaster.startInteractionForId(player, npc.getId(), 2)) {
					return;
				} else if (npc.getId() == 1595 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Saniboch", npc.getId());
				} else if (npc.getId() == 922 || npc.getId() == 8207) {
					player.getPackets().sendGameMessage("I think I should talk to Aggie first...");
				} else if (npc.getId() == 4250) {
					player.getInterfaceManager().sendInterface(403);
				} else if (npc.getId() >= 376 && npc.getId() <= 378) {
					player.getDialogueManager().startDialogue("KaramjaTrip", npc.getId());
				} else if (npc.getId() == 300) {
					npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
					npc.animate(new Animation(1818));
					npc.faceEntity(player);
					World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
					player.setNextGraphics(new Graphics(110));
					player.setNextPosition(new Position(2910, 4832, 0));
				} else if (npc.getId() == 5913) {
					npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
					npc.animate(new Animation(1818));
					npc.faceEntity(player);
					World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
					player.setNextGraphics(new Graphics(110));
					player.setNextPosition(new Position(2910, 4832, 0));
				}
				else if (npc.getDefinitions().name.contains("Fisherman") && !player.getGameMode().isIronman()) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2854, 3430, 0));
					player.getPackets().sendGameMessage("The Fisherman teleports you to Catherby.");
				}
				else if (npc.getId() == 548 && !player.getGameMode().isIronman() || npc.getId() == 0 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("Capes");//Skillcape shop
				} else if (npc.getId() == 3680) {
					player.getPackets().sendGameMessage("You steal a flier from the poor boy, you are a cruel person.");
					player.getInventory().addItem(956, 1);
				} else if (npc.getDefinitions().name.contains("H.A.M. Guard")) {
					if (player.getEquipment().getAmuletId() == 4306 && player.getEquipment().getChestId() == 4298 && player.getEquipment().getLegsId() == 4300 && player.getEquipment().getHatId() == 4302 && player.getEquipment().getCapeId() == 4304 && player.getEquipment().getGlovesId() == 4308) {
						player.getDialogueManager().startDialogue("Ham", npc.getId());
					} else {
						npc.setNextForceTalk(new ForceTalk("Hey, what are you doing down here?"));
						npc.setTarget(player);
					}

				}
				else if (npc.getId() == 9707) {
					FremennikShipmaster.sail(player, true);
				} else if (npc.getId() == 9708) {
					FremennikShipmaster.sail(player, false);
				} else if (npc.getId() == 659) {
					player.getDialogueManager().startDialogue("pointShops", npc);
				} else if ((npc.getId() == 14849 || npc.getId() == 1610) && npc instanceof ConditionalDeath) {
					((ConditionalDeath) npc).useHammer(player);
				} else if (npc.getId() >= 2291 && npc.getId() <= 2294 && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("RugMerchant", true);
				} else if (npc.getDefinitions().name.contains("ool leprech") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("seeds", npc.getId());
				} else if (npc.getId() == 528  && !player.getGameMode().isIronman() || npc.getId() == 529 && !player.getGameMode().isIronman()) {
					ShopsHandler.openShop(player, 1);
				} else if (npc.getId() == 1686) {
					if (player.getInventory().hasFreeSlots() && player.unclaimedEctoTokens > 0) {
						//	player.getInventory().addItem(Ectofuntus.ECTO_TOKEN, player.unclaimedEctoTokens);
						player.unclaimedEctoTokens = 0;

					} else if ((npc.getId() == 14849 || npc.getId() == 1610) && npc instanceof ConditionalDeath) {
						((ConditionalDeath) npc).useHammer(player);
					} else if (npc.getId() >= 2291 && npc.getId() <= 2294 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("RugMerchant", true);
					} else if (npc.getDefinitions().name.contains("ool leprech") && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("seeds", npc.getId());
					} else if (npc.getId() == 528  && !player.getGameMode().isIronman() || npc.getId() == 529 && !player.getGameMode().isIronman()) {
						ShopsHandler.openShop(player, 1);
					} else if (npc.getId() == 1686) {
						if (player.getInventory().hasFreeSlots() && player.unclaimedEctoTokens > 0) {
							//	player.getInventory().addItem(Ectofuntus.ECTO_TOKEN, player.unclaimedEctoTokens);
							player.unclaimedEctoTokens = 0;
						}
					} else if (npc.getId() == 9159) {
						npc.setNextForceTalk(new ForceTalk("I TOLD YOU I HAVE NOTHING TO SELL!!!!"));
					} else if (npc.getId() == 13455 || npc.getId() == 2617 || npc.getId() == 2618
							|| npc.getId() == 15194) {
						player.getBank().openBank();
					} else if (npc.getId() >= 3809 && npc.getId() <= 3812 || npc.getId() == 1800) {
						player.getInterfaceManager().sendInterface(138);
					} else if (npc.getId() == 5915) {
						player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20709);
					} else if (npc.getId() == 13633) {
						player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20708);
					} else if (SlayerMaster.startInteractionForId(player, npc.getId(), 2)) {
						return;
					} else if (npc.getId() == 1595 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("Saniboch", npc.getId());
					} else if (npc.getId() == 922 || npc.getId() == 8207) {
						player.getPackets().sendGameMessage("I think I should talk to Aggie first...");
					} else if (npc.getId() == 4250) {
						player.getInterfaceManager().sendInterface(403);
					} else if (npc.getId() >= 376 && npc.getId() <= 378) {
						player.getDialogueManager().startDialogue("KaramjaTrip", npc.getId());
					} else if (npc.getId() == 300) {
						npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
						npc.animate(new Animation(1818));
						npc.faceEntity(player);
						World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
						player.setNextGraphics(new Graphics(110));
						player.setNextPosition(new Position(2910, 4832, 0));
					} else if (npc.getId() == 5913) {
						npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
						npc.animate(new Animation(1818));
						npc.faceEntity(player);
						World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
						player.setNextGraphics(new Graphics(110));
						player.setNextPosition(new Position(2910, 4832, 0));
					} else if (npc.getDefinitions().name.contains("Fisherman") && !player.getGameMode().isIronman()) {
						Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2854, 3430, 0));
						player.getPackets().sendGameMessage("The Fisherman teleports you to Catherby.");
					} else if (npc.getId() == 548 && !player.getGameMode().isIronman() || npc.getId() == 0 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("Capes");//Skillcape shop
					} else if (npc.getId() == 3680) {
						player.getPackets().sendGameMessage("You steal a flier from the poor boy, you are a cruel person.");
						player.getInventory().addItem(956, 1);
					} else if (npc.getDefinitions().name.contains("H.A.M. Guard")) {
						if (player.getEquipment().getAmuletId() == 4306 && player.getEquipment().getChestId() == 4298 && player.getEquipment().getLegsId() == 4300 &&
								player.getEquipment().getHatId() == 4302 && player.getEquipment().getCapeId() == 4304 && player.getEquipment().getGlovesId() == 4308) {
							player.getDialogueManager().startDialogue("Ham", npc.getId());
						} else {
							npc.setNextForceTalk(new ForceTalk("Hey, what are you doing down here?"));
							npc.setTarget(player);
						}
					} else if (npc.getId() == 9159) {
						npc.setNextForceTalk(new ForceTalk("I TOLD YOU I HAVE NOTHING TO SELL!!!!"));
					} else if (npc.getId() == 13455 || npc.getId() == 2617 || npc.getId() == 2618
							|| npc.getId() == 15194) {
						player.getBank().openBank();
					} else if (npc.getId() >= 3809 && npc.getId() <= 3812 || npc.getId() == 1800) {
						player.getInterfaceManager().sendInterface(138);
					} else if (npc.getId() == 5915) {
						player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20709);
					} else if (npc.getId() == 13633) {
						player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20708);
					} else if (SlayerMaster.startInteractionForId(player, npc.getId(), 2)) {
						return;
					} else if (npc.getId() == 1595 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("Saniboch", npc.getId());
					} else if (npc.getId() == 922 || npc.getId() == 8207) {
						player.getPackets().sendGameMessage("I think I should talk to Aggie first...");
					} else if (npc.getId() == 4250) {
						player.getInterfaceManager().sendInterface(403);
					} else if (npc.getId() >= 376 && npc.getId() <= 378) {
						player.getDialogueManager().startDialogue("KaramjaTrip", npc.getId());
					} else if (npc.getId() == 300) {
						npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
						npc.animate(new Animation(1818));
						npc.faceEntity(player);
						World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
						player.setNextGraphics(new Graphics(110));
						player.setNextPosition(new Position(2910, 4832, 0));
					} else if (npc.getId() == 5913) {
						npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
						npc.animate(new Animation(1818));
						npc.faceEntity(player);
						World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
						player.setNextGraphics(new Graphics(110));
						player.setNextPosition(new Position(2910, 4832, 0));
					}
					else if (npc.getDefinitions().name.contains("Fisherman") && !player.getGameMode().isIronman()) {
						Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2854, 3430, 0));
						player.getPackets().sendGameMessage("The Fisherman teleports you to Catherby.");

					} else if (npc.getId() == 9159) {
						npc.setNextForceTalk(new ForceTalk("I TOLD YOU I HAVE NOTHING TO SELL!!!!"));
					} else if (npc.getId() == 13455 || npc.getId() == 2617 || npc.getId() == 2618
							|| npc.getId() == 15194) {
						player.getBank().openBank();
					} else if (npc.getId() >= 3809 && npc.getId() <= 3812 || npc.getId() == 1800) {
						player.getInterfaceManager().sendInterface(138);
					} else if (npc.getId() == 5915) {
						player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20709);
					} else if (npc.getId() == 13633) {
						player.getDialogueManager().startDialogue("ClaimClanItem", npc.getId(), 20708);
					} else if (SlayerMaster.startInteractionForId(player, npc.getId(), 2)) {
						return;
					} else if (npc.getId() == 1595 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("Saniboch", npc.getId());
					} else if (npc.getId() == 922 || npc.getId() == 8207) {
						player.getPackets().sendGameMessage("I think I should talk to Aggie first...");
					} else if (npc.getId() == 4250) {
						player.getInterfaceManager().sendInterface(403);
					} else if (npc.getId() >= 376 && npc.getId() <= 378) {
						player.getDialogueManager().startDialogue("KaramjaTrip", npc.getId());
					} else if (npc.getId() == 300) {
						npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
						npc.animate(new Animation(1818));
						npc.faceEntity(player);
						World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
						player.setNextGraphics(new Graphics(110));
						player.setNextPosition(new Position(2910, 4832, 0));
					} else if (npc.getId() == 5913) {
						npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
						npc.animate(new Animation(1818));
						npc.faceEntity(player);
						World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
						player.setNextGraphics(new Graphics(110));
						player.setNextPosition(new Position(2910, 4832, 0));
					}
					else if (npc.getDefinitions().name.contains("Fisherman") && !player.getGameMode().isIronman()) {
						Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2854, 3430, 0));
						player.getPackets().sendGameMessage("The Fisherman teleports you to Catherby.");
					}
					else if (npc.getId() == 548 && !player.getGameMode().isIronman() || npc.getId() == 0 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("Capes");//Skillcape shop
					} else if (npc.getId() == 3680) {
						player.getPackets().sendGameMessage("You steal a flier from the poor boy, you are a cruel person.");
						player.getInventory().addItem(956, 1);
					} else if (npc.getDefinitions().name.contains("H.A.M. Guard")) {
						if (player.getEquipment().getAmuletId() == 4306 && player.getEquipment().getChestId() == 4298 && player.getEquipment().getLegsId() == 4300 && player.getEquipment().getHatId() == 4302 && player.getEquipment().getCapeId() == 4304 && player.getEquipment().getGlovesId() == 4308) {
							player.getDialogueManager().startDialogue("Ham", npc.getId());
						} else {
							npc.setNextForceTalk(new ForceTalk("Hey, what are you doing down here?"));
							npc.setTarget(player);
						}
					} else if (npc.getId() == 548 && !player.getGameMode().isIronman() || npc.getId() == 0 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("Capes");//Skillcape shop
					} else if (npc.getId() == 3680) {
						player.getPackets().sendGameMessage("You steal a flier from the poor boy, you are a cruel person.");
						player.getInventory().addItem(956, 1);
					} else if (npc.getDefinitions().name.contains("H.A.M. Guard")) {
						if (player.getEquipment().getAmuletId() == 4306 && player.getEquipment().getChestId() == 4298 && player.getEquipment().getLegsId() == 4300 && player.getEquipment().getHatId() == 4302 && player.getEquipment().getCapeId() == 4304 && player.getEquipment().getGlovesId() == 4308) {
							player.getDialogueManager().startDialogue("Ham", npc.getId());
						} else {
							npc.setNextForceTalk(new ForceTalk("Hey, what are you doing down here?"));
							npc.setTarget(player);

						}
					}
					else if (npc.getId() == 7888) {
						player.setNextPosition(new Position(2852, 2960, 0));
					} else if (npc.getId() == 3777) {
						player.getDialogueManager().startDialogue("ToggleGraves", npc);
					} else if (npc.getId() == 7868) {
						player.quickWork = true;
						player.getDialogueManager().startDialogue("Iain", npc.getId());
					}
					else if (npc.getId() == 4904) {
						player.quickWork = true;
						player.getDialogueManager().startDialogue("ApprenticeSmith", npc.getId());
					}
					else if (npc.getId() == 7869) {
						player.quickWork = true;
						player.getDialogueManager().startDialogue("Julian", npc.getId());
					}
					else if (npc.getId() == 4903) {
						player.quickWork = true;
						player.getDialogueManager().startDialogue("PriestYauchomi", npc.getId());
					}
					else if(npc.getId() == 15149 && !player.getGameMode().isIronman()) {
						player.getDialogueManager().startDialogue("MasterOfFear", 3);
					} else if (npc.getId() == 2676) {
						PlayerLook.openMageMakeOver(player);
					} else if (npc.getId() == 598) {
						PlayerLook.openHairdresserSalon(player);
					} else if (npc instanceof Pet) {
						if (npc != player.getPet()) {
							player.getPackets().sendGameMessage("This isn't your pet!");
							return;
						}
						Pet pet = player.getPet();
						player.getPackets().sendMessage(99, "Pet [id=" + pet.getId()
								+ ", hunger=" + pet.getDetails().getHunger()
								+ ", growth=" + pet.getDetails().getGrowth()
								+ ", stage=" + pet.getDetails().getStage() + "].", player);
					}
					else {
						//player.getPackets().sendGameMessage(
						//"Nothing interesting happens.");
						if (Constants.DEBUG) {
							System.out.println("cliked 2 at npc id : "
									+ npc.getId() + ", " + npc.getX() + ", "
									+ npc.getY() + ", " + npc.getZ());
							Logger.logMessage("cliked 2 at npc id : "
									+ npc.getId() + ", " + npc.getX() + ", "
									+ npc.getY() + ", " + npc.getZ());
						}
					}
				}
			}
		}, npc.getSize()));
	}

	public static void handleOption3(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.isFinished() || !player.finishedStarter
				|| !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		if (npc.getId() == 745) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 4)) {
				return;
			}
			npc.faceEntity(player);
			player.getDialogueManager().startDialogue("Wormbrain", npc.getId());
			return;
		}
		if (npc.getDefinitions().name.toLowerCase().equals("grand exchange clerk")) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2))
				return;
			npc.faceEntity(player);
			player.getGeManager().openHistory();
			return;
		}
		if (npc.getDefinitions().name.toLowerCase().contains("banker")) {
			player.faceEntity(npc);
			npc.faceEntity(player);
			//  player.getGeManager().openCollectionBox();
			return;
		}
		if (npc instanceof GraveStone) {
			GraveStone grave = (GraveStone) npc;
			grave.repair(player, true);
			return;
		}
		player.stopAll(false);
		if(forceRun) {
			player.setRun(forceRun);
		}

		if (npc.getClickOptionListener()[2] != null)
			npc.getClickOptionListener()[2].handle(player, npc);

		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!player.getControlerManager().processNPCClick3(npc)) {
					return;
				}
				player.faceEntity(npc);

				if (npc instanceof Pet) {
					Pet pet = (Pet) npc;
					if (pet != player.getPet()) {
						player.sendMessage("This isn't your pet.");
						return;
					}
					if (pet.getName().contains("cat") || pet.getName().contains("kitten") || pet.getName().equalsIgnoreCase("kitten")) {
						player.getDialogueManager().startDialogue("CatInteract", pet);
					}
				}

				if (npc.getId() >= 8837 && npc.getId() <= 8839) {
					MiningBase.propect(player, "You examine the remains...", "The remains contain traces of living minerals.");
					return;

				}
				npc.faceEntity(player);

				if (npc.getId() == 9085) {
				}
				if (npc.getId() == 27663) {
				}
				if (npc.getId() == 8462) {
				}
				if (npc.getId() == 8464) {
				}
				if (npc.getId() == 970) {
					player.getPackets().sendGameMessage("This option is not available...");
				}
				else if (npc.getId() == 548) {
					PlayerLook.openThessaliasMakeOver(player);
				} else if (npc.getDefinitions().name.contains("Fisherman")) {
					player.getPackets().sendGameMessage("You deserve no rewards....");
				} else if (npc.getDefinitions().name.contains("H.A.M. Guard")) {
					if (player.getEquipment().getAmuletId() == 4306 && player.getEquipment().getChestId() == 4298 && player.getEquipment().getLegsId() == 4300 && player.getEquipment().getHatId() == 4302 && player.getEquipment().getCapeId() == 4304 && player.getEquipment().getGlovesId() == 4308) {
						player.getDialogueManager().startDialogue("Ham", npc.getId());
					} else {
						npc.setNextForceTalk(new ForceTalk("Hey, what are you doing down here?"));
						npc.setTarget(player);
					}
				}
				//else if(npc.getId() == 8274)
				//    player.getInterfaceManager().sendSlayerShop();
				else if (npc.getId() >= 376 && npc.getId() <= 378) {
					player.getDialogueManager().startDialogue("KaramjaTrip", npc.getId());
				} else if (npc.getId() >= 4650 && npc.getId() <= 4656 || npc.getId() == 7077) {
					player.getDialogueManager().startDialogue("Sailing", npc);
				} else if (npc.getId() == 7868 && !player.getGameMode().isIronman()) {
					player.quickWork = true;
					player.getDialogueManager().startDialogue("Iain", npc.getId());
				}
				else if (npc.getId() == 4904 && !player.getGameMode().isIronman()) {
					player.quickWork = true;
					player.getDialogueManager().startDialogue("ApprenticeSmith", npc.getId());
				}
				else if (npc.getId() == 7869 && !player.getGameMode().isIronman()) {
					player.quickWork = true;
					player.getDialogueManager().startDialogue("Julian", npc.getId());
				}
				else if (npc.getId() == 36) {
					ShopsHandler.openShop(player, 172);
				}
				else if (npc.getId() == 4903 && !player.getGameMode().isIronman()) {
					player.quickWork = true;
					player.getDialogueManager().startDialogue("PriestYauchomi", npc.getId());
				}
				else if (SlayerMaster.startInteractionForId(player, npc.getId(), 3)) {
					ShopsHandler.openShop(player, 9);
				}
				else if (npc.getDefinitions().name.contains("ool leprech") && !player.getGameMode().isIronman()) {
					player.getDialogueManager().startDialogue("IrishToolTeleD");
				} else if (npc.getId() == 5532 && !player.getGameMode().isIronman()) {
					SorceressGarden.teleportToSorceressGardenNPC(npc, player);

				}
				else {
					;
					//player.getPackets().sendGameMessage(
					//"Nothing interesting happens.");
				}
			}

		}, npc.getSize()));
		if (Constants.DEBUG) {
			System.out.println("cliked 3 at npc id : "
					+ npc.getId() + ", " + npc.getX() + ", "
					+ npc.getY() + ", " + npc.getZ());
			Logger.logMessage("cliked 3 at npc id : "
					+ npc.getId() + ", " + npc.getX() + ", "
					+ npc.getY() + ", " + npc.getZ());
		}
	}
	public static void handleOption4(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.isFinished() || !player.finishedStarter
				|| !player.getMapRegionsIds().contains(npc.getRegionId())) {
			return;
		}
		player.stopAll(false);
		if(forceRun) {
			player.setRun(forceRun);
		}

		if (npc.getClickOptionListener()[4] != null)
			npc.getClickOptionListener()[4].handle(player, npc);

		if (npc.getId() == 745) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 4)) {
				return;
			}
			npc.faceEntity(player);
			player.getDialogueManager().startDialogue("Wormbrain", npc.getId());
			return;
		}
		if (npc instanceof GraveStone) {
			GraveStone grave = (GraveStone) npc;
			grave.demolish(player);
			return;
		}
		if (npc.getDefinitions().name.toLowerCase().equals("grand exchange clerk")) {
			player.faceEntity(npc);
			if (!player.withinDistance(npc, 2))
				return;
			npc.faceEntity(player);
			ItemSets.openSets(player);
			return;
		}
//		if (SlayerMaster.startInteractionForId(player, npc.getId(), 4)) {
//			player.getSlayerManager().sendSlayerInterface(SlayerManager.BUY_INTERFACE);
//		}

		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!player.getControlerManager().processNPCClick4(npc)) {
					return;
				}
				player.faceEntity(npc);
				npc.faceEntity(player);
				/*if (npc.getId() == 9085) {
						if (player.getSlayerPoints() >= 400) {
							player.getInventory().addItem(22528, 1);
							player.setSlayerPoints(player.getSlayerPoints() - 400);
						} else
							player.sendMessage("You need 400 Slayer Points to purchase the Full Slayer Helmet.");
					}
					if (npc.getId() == 8462) {
						if (player.getSlayerPoints() >= 400) {
							player.getInventory().addItem(22528, 1);
							player.setSlayerPoints(player.getSlayerPoints() - 400);
						} else
							player.sendMessage("You need 400 Slayer Points to purchase the Full Slayer Helmet.");
					}*/
				if (npc.getId() == 5913) {
					npc.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
					npc.animate(new Animation(1818));
					npc.faceEntity(player);
					World.sendProjectile(npc, player, 110, 1, 1, 1, 1, 1, 1);
					player.setNextGraphics(new Graphics(110));
					player.setNextPosition(new Position(2910, 4832, 0));
				}
				if (npc.getId() == 970) {
					player.getPackets().sendGameMessage("This option is not available...");
				}
				if (SlayerMaster.startInteractionForId(player, npc.getId(), 4)) {
					player.getSlayerManager().sendSlayerInterface(SlayerManager.BUY_INTERFACE);
				}
				 else if (npc.getId() == 5532) {
					npc.setNextForceTalk(new ForceTalk("Senventior Disthinte Molesko!"));
					player.getControlerManager().startControler("SorceressGarden");
				}
				else {
					;
					//player.getPackets().sendGameMessage(
					//		"Nothing interesting happens.");
				}
			}

		}, npc.getSize()));
		if (Constants.DEBUG) {
			System.out.println("cliked 4 at npc id : "
					+ npc.getId() + ", " + npc.getX() + ", "
					+ npc.getY() + ", " + npc.getZ());
			Logger.logMessage("cliked 4 at npc id : "
					+ npc.getId() + ", " + npc.getX() + ", "
					+ npc.getY() + ", " + npc.getZ());
		}
	}
}
