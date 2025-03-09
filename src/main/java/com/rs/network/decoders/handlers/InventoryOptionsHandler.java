package com.rs.network.decoders.handlers;

import com.rs.Constants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.cores.WorldThread;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.impl.AmethystDragonCombat;
import com.rs.game.world.entity.npc.combat.impl.eventboss.EnragedOlm;
import com.rs.game.world.entity.npc.combat.impl.eventboss.PumpkinBruteCombat;
import com.rs.game.world.entity.npc.combat.impl.wilderness.KingLavaDragon;
import com.rs.game.world.entity.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.world.entity.npc.others.ConditionalDeath;
import com.rs.game.world.entity.npc.pet.Pet;
import com.rs.game.world.entity.npc.randomspawns.WildyBossSpawns;
import com.rs.game.world.entity.player.BuffManager.Buff;
import com.rs.game.world.entity.player.BuffManager.BuffType;
import com.rs.game.world.entity.player.*;
import com.rs.game.world.entity.player.actions.BoltTipFletching;
import com.rs.game.world.entity.player.actions.BoltTipFletching.BoltTips;
import com.rs.game.world.entity.player.actions.BoxAction;
import com.rs.game.world.entity.player.actions.BoxAction.HunterEquipment;
import com.rs.game.world.entity.player.actions.Crushing;
import com.rs.game.world.entity.player.actions.Crushing.Items;
import com.rs.game.world.entity.player.actions.skilling.*;
import com.rs.game.world.entity.player.actions.skilling.Fletching.Fletch;
import com.rs.game.world.entity.player.actions.skilling.Summoning.Pouches;
import com.rs.game.world.entity.player.actions.skilling.crafting.AmuletStringing;
import com.rs.game.world.entity.player.actions.skilling.crafting.AmuletStringing.Amulets;
import com.rs.game.world.entity.player.actions.skilling.crafting.GemCutting;
import com.rs.game.world.entity.player.actions.skilling.crafting.GemCutting.Gem;
import com.rs.game.world.entity.player.content.*;
import com.rs.game.world.entity.player.content.ArmourSets.Sets;
import com.rs.game.world.entity.player.content.Hunter;
import com.rs.game.world.entity.player.content.RepairItems.BrokenItems;
import com.rs.game.world.entity.player.content.Scattering.Ash;
import com.rs.game.world.entity.player.content.clues.CasketHandler;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.dialogue.impl.CapeExchange;
import com.rs.game.world.entity.player.content.dialogue.impl.LeatherCraftingD;
import com.rs.game.world.entity.player.content.dialogue.impl.SqirkFruitSqueeze;
import com.rs.game.world.entity.player.content.dialogue.impl.SqirkFruitSqueeze.SqirkFruit;
import com.rs.game.world.entity.player.content.newclues.Clues;
import com.rs.game.world.entity.player.content.newclues.CluesManager;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungFoods;
import com.rs.game.world.entity.player.content.skills.farming.PatchConstants;
import com.rs.game.world.entity.player.content.skills.magic.Alchemy;
import com.rs.game.world.entity.player.content.skills.magic.Enchanting;
import com.rs.game.world.entity.player.content.skills.magic.Lunars;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.content.skills.prayer.Burying.Bone;
import com.rs.game.world.entity.player.content.trident.impl.TridentOfTheSeas;
import com.rs.game.world.entity.player.content.trident.impl.TridentOfTheSwamp;
import com.rs.game.world.entity.player.content.xeric.dungeon.skilling.CoxHerblore;
import com.rs.game.world.entity.player.controller.impl.Barrows;
import com.rs.game.world.entity.player.controller.impl.FightKiln;
import com.rs.game.world.entity.player.controller.impl.SorceressGarden;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.network.io.InputStream;
import com.rs.utility.Logger;
import com.rs.utility.Utils;
import com.rs.game.world.entity.player.content.interfaces.spin.MysteryBox;

import java.util.List;
import java.util.TimerTask;

public class InventoryOptionsHandler {

	public static void handleItemOption2(final Player player, final int slotId, final int itemId, Item item) {
		//System.out.println("opt1");
		if (Firemaking.isFiremaking(player, itemId)) {
			return;
		}

		if (itemId == 4155) {
			player.getSlayerManager().checkKillsLeft();
		}


		if (itemId == 6583 || itemId == 7927) {
			JewllerySmithing.ringTransformation(player, itemId);
		}

		if (itemId == 18338) {
			if (player.sapphires <= 0 && player.rubies <= 0 && player.emeralds <= 0 && player.diamonds <= 0) {
				player.sm("Your gem pouch is currently empty.");
			} else {
				player.getInventory().addItem(1622, player.emeralds);
				player.getInventory().addItem(1624, player.sapphires);
				player.getInventory().addItem(1620, player.rubies);
				player.getInventory().addItem(1619, player.diamonds);
				player.emeralds = 0;
				player.sapphires = 0;
				player.rubies = 0;
				player.diamonds = 0;
				player.sm("You have successfully emptied your gem bag.");
			}
		}
		
		if (itemId == 18339) {
			if (player.coal <= 0) {
				player.sm("Your coal pouch is currently empty.");
			} else {
				player.getInventory().addItem(453, 1);
				player.coal--;
				player.sm("You have successfully taken a piece of coal from your coal bag.");
			}
		}

		if (MysteryBox.validBoxesList.contains(itemId)) {
			player.getTemporaryAttributtes().put(MysteryBox.MYSTERY_SELECTED, itemId);
			player.getMysteryBoxes().openAllBoxes();
		}

		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509) {
				pouch = 0;
			}
			if (itemId == 5510) {
				pouch = 1;
			}
			if (itemId == 5512) {
				pouch = 2;
			}
			if (itemId == 5514) {
				pouch = 3;
			}
			Runecrafting.emptyPouch(player, pouch);
			player.stopAll(false);
		} else if (itemId >= 15086 && itemId <= 15100) {
			Dicing.handleRoll(player, itemId, true);
			return;
		} else if (itemId == 15262) {
			ItemSets.openSkillPack(player, itemId, 12183, 5000, player.getInventory().getAmountOf(itemId));
		} else if (itemId == 15362) {
			ItemSets.openSkillPack(player, itemId, 230, 50, player.getInventory().getAmountOf(itemId));
		} else if (itemId == 15363) {
			ItemSets.openSkillPack(player, itemId, 228, 50, player.getInventory().getAmountOf(itemId));
		} else if (itemId == 15364) {
			ItemSets.openSkillPack(player, itemId, 222, 50, player.getInventory().getAmountOf(itemId));
		} else if (itemId == 15365) {
			ItemSets.openSkillPack(player, itemId, 9979, 50, player.getInventory().getAmountOf(itemId));
		} else {
			if (player.isEquipDisabled()) {
				return;
			}
			long passedTime = Utils.currentTimeMillis() - WorldThread.LAST_CYCLE_CTM;
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					List<Integer> slots = player.getSwitchItemCache();
					int[] slot = new int[slots.size()];
					for (int i = 0; i < slot.length; i++) {
						slot[i] = slots.get(i);
					}
					player.getSwitchItemCache().clear();
					ButtonHandler.sendWear(player, slot);
					player.stopAll(false, true, false);
				}
			}, passedTime >= 600 ? 0 : passedTime > 330 ? 1 : 0);
			if (player.getSwitchItemCache().contains(slotId)) {
				return;
			}
			player.getSwitchItemCache().add(slotId);
		}

	}

	private final static int[] FLOWERS = { 2980, 2986, 2987, 2988, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2981, 2982,
			2982, 2982, 2982, 2982, 2982, 2982, 2982, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2983, 2984, 2984, 2984,
			2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2981, 2981, 2981, 2981, 2981,
			2981, 2981, 2981, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2982, 2983, 2983, 2983, 2983, 2983, 2983, 2983,
			2983, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2984, 2985, 2985, 2985, 2985, 2985, 2985, 2985, 2985 };

	public static void dig(final Player player) {
		player.resetWalkSteps();
		player.animate(new Animation(830));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				if (Barrows.digIntoGrave(player)) {
					return;
				}
				if (player.getX() == 3005 && player.getY() == 3376 || player.getX() == 2999 && player.getY() == 3375
						|| player.getX() == 2996 && player.getY() == 3377
						|| player.getX() == 2989 && player.getY() == 3378
						|| player.getX() == 2987 && player.getY() == 3387
						|| player.getX() == 2984 && player.getY() == 3387) {
					// mole
					player.setNextPosition(new Position(1752, 5137, 0));
					player.getPackets()
							.sendGameMessage("You seem to have dropped down into a network of mole tunnels.");
					return;

				}
				if (player.getX() >= 2747 && player.getX() <= 2750 && player.getY() >= 3733 && player.getY() <= 3736) {
					// Dungeon brine rats
					player.setNextPosition(new Position(2697, 10120, 0));
					player.getPackets().sendGameMessage("You seem to have dropped down into a strange cave.");
					return;

				}
				if (CluesManager.digSpot(player)) {
					return;
				}
				player.getPackets().sendGameMessage("You find nothing.");
			}

		});
	}

	public static void handleItemOption1(Player player, final int slotId, final int itemId, Item item) {
		//System.out.println("opt1");
		if (itemId == 952) {// spade
			dig(player);
			return;
		}
		
		if (Clues.getClue(itemId) != null) {
			if (CluesManager.sendSteps(player, Clues.getClue(itemId))) {
				return;
			}
		}
		
		for (Crushing.Items items : Items.values()) {
			if (itemId == items.getRaw()) {
				Crushing.crushItem(player, item);
			}
		}
		if (SerpentineHelm.usingSerptentineHelm(item.getId())) {
			player.getSerpentineHelm().check();
		}

		CoxHerblore.Herbs coxHerb = CoxHerblore.Herbs.forItemId(itemId);
		if (coxHerb != null) {
			coxHerb.clean(player);
			return;
		}

		if (MysteryBox.validBoxesList.contains(itemId)) {
			player.getTemporaryAttributtes().put(MysteryBox.MYSTERY_SELECTED, itemId);
			player.getMysteryBoxes().openBox();
		}

		if (DungFoods.eat(player, item, slotId))
			return;
		if (itemId == 7509) {
			if (player.getHitpoints() <= 10) {
				player.getPackets().sendGameMessage("You need more than 10 Hitpoints to consume this.");
				return;
			}
			player.getInventory().containsItem(7509, 1);
			player.setNextForceTalk(new ForceTalk("Ow! I nearly broke a tooth!"));
			player.applyHit(new Hit(player, 100, HitLook.REGULAR_DAMAGE, 0));
		}
		if (itemId == 29994 && player.TriviaBoost > 0) {
			player.sm("You must wait to your current boost runs out.");
		}


		if (itemId == 15707 || item.getDefinitions().isRingOfKinship()) {
			player.getDungeoneeringManager().openPartyInterface();
			return;
		}
		if (itemId == 23752) {
			player.getSkills().addXp(Skills.DUNGEONEERING, 5000);
			player.getInventory().deleteItem(23752, 1);
		}
		if (itemId == 28832) {
			Lunars.handleVengeance(player);
		}
		if (itemId == 28833) {

			if (KingLavaDragon.getSpawned() == false) {
				World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: The King lava dragon as appeared north of the red dragon isle!", false);
				World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						"<col=ff0000> has awakend the king lava dragon. <col=ff0000>", false);
				new NPC(16112, Position.of(3202, 3883, 0), -1, true, true);
				player.getInventory().deleteItem(28833, 1);
				KingLavaDragon.isSpawned = true;
			} else {
				player.sm("the king lava dragon is still alive");

			}
		}

		if (itemId == 28712) {

			if (EnragedOlm.getSpawned() == false) {
				World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: A enraged olm has spawned in the burnt areana!", false);
				World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						"<col=ff0000> has awakend the enraged olm. <col=ff0000>", false);
				new NPC(16083, Position.of(3681, 4450, 0), -1, true, true);
				player.getInventory().deleteItem(28712, 1);
				EnragedOlm.isSpawned = true;
			} else {
				player.sm("the enraged olm is still alive");

			}
		}

		if (itemId == 28708) {

			if (AmethystDragonCombat.getSpawned() == false) {
				World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: The Amethyst dragon has appeared in its lair!", false);
				World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						"<col=ff0000> has awakend the amethyst dragon. <col=ff0000>", false);
				new NPC(16103, Position.of(3812, 2527, 0), -1, true, true);
				player.getInventory().deleteItem(28708, 1);
				AmethystDragonCombat.isSpawned = true;
			} else {
				player.sm("the amethyst dragon is still alive");

			}
		}

		if (itemId == 28707) {

			if (PumpkinBruteCombat.getSpawned() == false) {
				World.sendWorldMessage("<col=ff0000><img=19>[EventManager]: A pumpkin brute has spawned!", false);
				World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						"<col=ff0000> has awakend the pumpkin brute. <col=ff0000>", false);
				new NPC(16138, Position.of(3553, 3233, 0), -1, true, true);
				player.getInventory().deleteItem(28707, 1);
				PumpkinBruteCombat.isSpawned = true;
			} else {
				player.sm("the pumpkin brute is still alive");

			}
		}
		if (itemId == 28711) {
			if (WildyBossSpawns.getSpawnedv() == false) {
				World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						"<col=ff0000> has awakend a cursed venenatis. <col=ff0000>", false);
				WildyBossSpawns.SpawnWildyBoss();
				player.getInventory().deleteItem(28711, 1);
			} else {
				player.sm("the cursed venenatis is still alive");

			}
		}

		if (itemId == 28710) {
			if (WildyBossSpawns.getSpawnedc() == false) {
				World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						"<col=ff0000> has awakend a cursed vet'ion. <col=ff0000>", false);
				WildyBossSpawns.SpawnCursedCallsito();
				player.getInventory().deleteItem(28710, 1);
			} else {
				player.sm("the cursed callisto is still alive");

			}
		}

		if (itemId == 28709) {
			if (WildyBossSpawns.getSpawnedvv() == false) {
				World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
						"<col=ff0000> has awakend a cursed vet'ion. <col=ff0000>", false);
				WildyBossSpawns.SpawnCursedvetion();
				player.getInventory().deleteItem(28709, 1);
			} else {
				player.sm("the cursed vetion is still alive");

			}
		}



		if (itemId == 21819) {
			if (player.foundImps == 0) {
				player.sm("<col=8B0000>[TRACKER]</col> - A Snow imp is located somewhere in Varrock.");
			} else if (player.foundImps == 1) {
				player.sm("<col=8B0000>[TRACKER]</col> - A Snow imp is located somewhere at the Ice Mountain.");
			} else if (player.foundImps == 2) {
				player.sm("<col=8B0000>[TRACKER]</col> - A Snow imp is located somewhere in the Kalphite Lair.");
			} else if (player.foundImps == 3) {
				player.sm("<col=8B0000>[TRACKER]</col> - Could not locate any imps..");
			}
		}
		if (itemId == 6) {
			DwarfMultiCannon.setUp(player);
		}
		if (itemId == 29994 && !player.TriviaBoostActive) {
			player.getInventory().deleteItem(29994, 1);
			player.TriviaBoostActive = true;
			player.TriviaBoost = 3600;
			Timer.startTriviaBoost(player);
			player.sm("<col=8B0000>You activated your 1 hour ticket boost!");
			player.sm("<col=8B0000>use ::timeleft to see how much time you have left!");
		}
		if (itemId == 20670) {
			player.sm("Please let us know through forums of how you got that note..");
		}
		if (itemId == 29745) {
			if (!player.isUnlockedAugury()) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You've now unlocked the ability to use the Augury prayer.");
				player.setUnlockedAugury(true);
				player.getInventory().deleteItem(itemId, 1);
				return;
			}
			player.getDialogueManager().startDialogue("SimpleMessage", "You've already unlocked the Augury prayer.");
			return;
		}
		if (itemId == 29747) {
			if (!player.isUnlockedRigour()) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You've now unlocked the ability to use the Rigour prayer.");
				player.setUnlockedRigour(true);
				player.getInventory().deleteItem(itemId, 1);
				return;
			}
			player.getDialogueManager().startDialogue("SimpleMessage", "You've already unlocked the Rigour prayer.");
			return;
		}
		if (itemId == 29542) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You search through Santa's large filled sack and find some items.");
			player.getInventory().deleteItem(item);
			player.getInventory().addItemDrop(new Item(14595), new Item(14602), new Item(14603), new Item(14605),
					new Item(6856), new Item(6857), new Item(6858), new Item(6859), new Item(6860), new Item(6861), new Item(6862), 
						new Item(6863), new Item(12204), new Item(15426), new Item(15422), new Item(15423), new Item(15425), new Item(20077), 
							new Item(22973), new Item(22985) , new Item(29475) , new Item(29473));
			return;
		}
		if (itemId == 2714) { // Easy caskets
			CasketHandler.openEasy(player);
			return;
		}
		if (itemId == 2802) { //Medium caskets
			CasketHandler.openMedium(player);
			return;
		}
		if (itemId == 2724) { //Hard caskets
			CasketHandler.openHard(player);
			return;
		}
//		if (itemId == 9477) {
//			player.getInventory().addItem(28881, 250000);
//			player.getInventory().addItem(28831, 5);
//			player.getInventory().addItem(29464, 1000000);
//			player.getInventory().addItem(28825, 1);
//			player.getInventory().addItem(28816, 1);
//			player.getInventory().addItem(28814, 1);
//			player.getInventory().addItem(11212, 10000);
//			player.getInventory().deleteItem(9477, 1);
//			player.getInventory().deleteItem(9477, 1);
//
//			return;
//		}
		if (itemId == 19039) { //Elite caskets
			CasketHandler.openElite(player);
			return;
		}
		//bonds
		if (itemId == 28756) {
			player.getInventory().deleteItem(28756, 1);
			player.getInventory().addItem(8800, 5);
			player.getDonationManager().increment(5);

		}
		if (itemId == 28755) {
			player.getInventory().deleteItem(28755, 1);
			player.getInventory().addItem(8800, 10);
			player.getDonationManager().increment(10);

		}
		if (itemId == 28754) {
			player.getInventory().deleteItem(28754, 1);
			player.getInventory().addItem(8800, 20);
			player.getDonationManager().increment(20);

		}
		if (itemId == 28753) {
			player.getInventory().deleteItem(28753, 1);
			player.getInventory().addItem(8800, 30);
			player.getDonationManager().increment(30);

		}
		if (itemId == 28752) {
			player.getInventory().deleteItem(28752, 1);
			player.getInventory().addItem(8800, 50);
			player.getDonationManager().increment(50);

		}
		if (itemId == 28751) {
			player.getInventory().deleteItem(28751, 1);
			player.getInventory().addItem(8800, 100);
			player.getDonationManager().increment(100);

		}
		//bonds end
        if (itemId == 19705) {
			if (player.UnlockedSuperiorBosses == 0) {
				player.UnlockedSuperiorBosses = 1;
				player.getInventory().deleteItem(19705, 1);
				player.sm("You have unlocked superior bosses");
			} else {
				player.sm("You have already unlocked superior bosses");

			}
		}

		if (itemId == 28882) {
			if (player.getBuffManager().applyBuff(new Buff(BuffType.OVERLOADED_HEART, 500, true)))
				return;
		}
		if (itemId == 29698) { //Imbued heart
			if (player.getBuffManager().applyBuff(new Buff(BuffType.IMBUED_HEART, 500, true)))
				return;
		}
		if (itemId == 28788) { //underwater potion
			if (player.getBuffManager().applyBuff(new Buff(BuffType.UNDERWATERPOTION, 2000, true)))
				player.getInventory().deleteItem(28788, 1);
				return;
		}
		if (itemId == 28780) { //xp potion
			if (player.getBuffManager().applyBuff(new Buff(BuffType.XPPOTION, 2000, true)))
				player.getInventory().deleteItem(28780, 1);
			return;
		}
		if (itemId == 28784) { //drop rate potion
			if (player.getBuffManager().applyBuff(new Buff(BuffType.DROPRATE_POTION, 2000, true)))
				player.getInventory().deleteItem(28784, 1);
			return;
		}
		if (itemId == 28786) { //Greed potion
			if (player.getBuffManager().applyBuff(new Buff(BuffType.GREED_POTION, 2000, true)))
				player.getInventory().deleteItem(28786, 1);
			return;
		}
		if (itemId == 28782) { //health boost potion
			if (player.getBuffManager().applyBuff(new Buff(BuffType.HEALTH_BOOST_POTION, 2000, true)))
				player.getInventory().deleteItem(28782, 1);
			return;
		}
		if (itemId == 28704) { //health boost potion
			if (player.getBuffManager().applyBuff(new Buff(BuffType.XP_BOOST, 3000, true)))
				player.getInventory().deleteItem(28704, 1);
			return;
		}
		if (itemId == 2396) {
			if (player.getSlayerManager().getCurrentTask() != null && player.getSlayerManager().getCurrentTask().getMonsterLocation() != null) {
				player.getInventory().deleteItem(2396, 1);
				player.setNextPosition(player.getSlayerManager().getCurrentTask().getMonsterLocation());
			} else
				player.sm("You don't have a task currently.");
		}
		if (itemId == 28673) {
			if (!player.getBuffManager().hasBuff(BuffManager.BuffType.DROPRATE_BUFF)) {
				World.DropRateBoost(player);
				World.sendWorldMessage("<col=ff0000>[Global buffs] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " has just acitvated a global drop rate boost!", false);
				player.getInventory().deleteItem(28673, 1);
			} else {
				player.sm("Global droprate boost already active");
			}
		}
		if (itemId == 28672) {
			if (!player.getBuffManager().hasBuff(BuffType.XP_BOOST)) {
				World.xpBoost(player);
				World.sendWorldMessage("<col=ff0000>[Global buffs] " + Utils.formatPlayerNameForDisplay(player.getUsername()) + " has just acitvated a global xp boost!", false);
				player.getInventory().deleteItem(28672, 1);
			} else {
				player.sm("Global xp Boost already active");
			}
		}
		if (itemId == 29995) {
			player.getInventory().deleteItem(29995, 1);
			player.getInventory().addItem(18831, 50);
			Dialogue.sendItemDialogueNoContinue(player, 18830, 250, "You open the box and claim 50x Frost bones.");

			int points = player.getSlayerManager().getPoints();
			player.getSlayerManager().setPoints(points - 1);
		}
		if (itemId == 15246) {
			player.getInventory().deleteItem(15246, 1);
			player.getInventory().addItem(384, 300); // Sharks
			player.getInventory().addItem(1514, 200);// Magic logs
			player.getInventory().addItem(1516, 200);// Yew logs
			player.getInventory().addItem(565, 250);// Blood Runes
			player.getInventory().addItem(11237, 200); // Dragon arrowtips
			player.getInventory().addItem(44, 200); // Rune arrowtips
			player.getInventory().addItem(990, 2); // C Keys
		}
		if (itemId == 10833) {
			player.getDialogueManager().startDialogue("SmallCashBag");
		}

		if (itemId == 10834) {
			player.getDialogueManager().startDialogue("MedCashBag");
		}
		if (itemId == 10835) {
			player.getDialogueManager().startDialogue("LargeCashBag");
		}
		if (itemId == 7774) {
			player.getInventory().deleteItem(7774, 1);
			player.setPvmPoints(player.getPvmPoints() + 300);
			player.getPackets().sendGameMessage(
					"You have claimed the scroll, and recieve <shad=a50b00>300</shad> Venomite points.");
		}
		if (itemId == 7775) {
			player.getInventory().deleteItem(7775, 1);
			player.setPvmPoints(player.getPvmPoints() + 600);
			player.getPackets().sendGameMessage(
					"You have claimed the scroll, and recieve <shad=a50b00>600</shad> Venomite points.");
		}
		if (itemId == 7776) {
			player.getInventory().deleteItem(7776, 1);
			player.setPvmPoints(player.getPvmPoints() + 1000);
			player.getPackets().sendGameMessage(
					"You have claimed the scroll, and recieve <shad=a50b00>1000</shad> Venomite points.");
		}

		if (itemId == 20667) {
			Magic.VecnaSkull(player);
		}
		if (com.rs.game.world.entity.player.content.MysteryBox.isBox(itemId, player)) {
			return;
		}
		if (item.getName().toLowerCase().contains("impling")) {
			Hunter.openJar(player, null, itemId);
			return;
		}
		if (itemId == 11238) {
			Hunter.openJar(player, null, itemId);
		}
		if (itemId == 42793) {
			if (player.getInventory().containsItem(42793, 1)) {
				if (player.getInventory().getFreeSlots() < 10) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
			player.getInventory().deleteItem(42793, 1);
			player.getInventory().addItem(5073, 10);
			return;


			}

		}
		if (itemId == 42794) {
			if (player.getInventory().containsItem(42794, 1)) {
				if (player.getInventory().getFreeSlots() < 10) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.");
					return;
				}
				player.getInventory().deleteItem(42794, 1);
				player.getInventory().addItem(5074, 10);

				return;


			}

		}
		if (itemId >= 5070 && itemId <= 5074) {
			BirdNests.searchNest(player, itemId);
		}
		if (itemId == 2700 || itemId == 13080 || itemId == 13010 || itemId == 19064) {
			if (!player.finishedClue) {
				if (itemId == 2700) {
					player.clueLevel = 0;
				} else if (itemId == 13080) {
					player.clueLevel = 1;
				} else if (itemId == 13010) {
					player.clueLevel = 2;
				} else if (itemId == 19064) {
					player.clueLevel = 3;
				}
				player.getInventory().deleteItem(itemId, 1);
				player.getInventory().addItem(2717, 1);
				player.clueChance = 0;
				player.finishedClue = true;
			} else {
				player.sm("You must finish your current clue in order to start a new one.");
			}
			return;
		}
		if (itemId == 21776) {
			if (player.getInventory().containsItem(21776, 200)) {
				player.getInventory().deleteItem(21776, 200);
				player.getInventory().addItem(21777, 1);
				player.getPackets().sendGameMessage("You create an Armadyl battlestaff");
				return;
			} else {
				player.getPackets().sendGameMessage("You need 200 shards of armadyl to make a battlestaff!"); // shard
			}
		}
		Ash ash = Ash.forId(itemId);
		if (ash != null) {
			Ash.scatter(player, slotId);
			return;
		}
		int leatherIndex = LeatherCraftingD.getIndex(item.getId());
		if (leatherIndex != -1) {
			player.getDialogueManager().startDialogue("LeatherCraftingD", leatherIndex);
			return;
		}
		long time = Utils.currentTimeMillis();
		if (player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);
		if (Foods.eat(player, item, slotId)) {
			return;
		}
		if (itemId >= 15086 && itemId <= 15100) {
			Dicing.handleRoll(player, itemId, false);
			return;
		}
		if (itemId == 4613) {
			player.animate(new Animation(1902));
			player.spinTimer = 8;
			World.spinPlate(player);
		}
		if (itemId == 29478 || itemId == 29479 || itemId == 29480) {
			if (player.getInventory().containsItem(29478, 1) && player.getInventory().containsItem(29479, 1)
					&& player.getInventory().containsItem(29480, 1)) {
				player.getInventory().deleteItem(29478, 1);
				player.getInventory().deleteItem(29479, 1);
				player.getInventory().deleteItem(29480, 1);
				player.getInventory().addItem(29477, 1);
				player.getPackets().sendGameMessage("You combine all pieces to make a infernal key.");
			} else {
				player.getPackets().sendGameMessage("You need all 3 pieces to combine the key.");
			}
		}
		if (itemId == 20121 || itemId == 20122 || itemId == 20123 || itemId == 20124) {
			if (player.getInventory().containsItem(20121, 1) && player.getInventory().containsItem(20122, 1)
					&& player.getInventory().containsItem(20123, 1) && player.getInventory().containsItem(20124, 1)) {
				player.getInventory().deleteItem(20121, 1);
				player.getInventory().deleteItem(20122, 1);
				player.getInventory().deleteItem(20123, 1);
				player.getInventory().deleteItem(20124, 1);
				player.getInventory().addItem(20120, 1);
				player.getPackets().sendGameMessage("You place the parts together to create a Frozen Key.");
			} else {
				player.getPackets().sendGameMessage("You need all four parts to create the key.");
			}
		}
		if (itemId == 10952) {
			if (Slayer.isUsingBell(player)) {
				return;
			}
		}

		if (itemId == 12844) {
			player.animate(new Animation(8990, 0));
		}
		if (itemId == 2520 || itemId == 2522 || itemId == 2524 || itemId == 2526) {
			ToyHorsey.play(player);
		}
		if (itemId == 6950) {
			player.getDialogueManager().startDialogue("LividOrb");
		}
		if (itemId == 18336) {
			player.sm("Keep this in your inventory while farming for a chance to recieve seeds back.");
		}
		if (itemId == 19670) {
			player.sm("Keep this in your inventory while smithing for a chance on recieving an extra bar.");
		}
		if (itemId == 19890) {
			player.sm("Keep this in your inventory while doing herblore for a chance of not using your secondary.");
		}
		if (itemId == 15262) {
			if (!player.getInventory().containsOneItem(12183) && !player.getInventory().hasFreeSlots()) {
				player.getPackets().sendGameMessage("You don't have enough space in your inventory.");
				return;
			}
			player.getInventory().deleteItem(15262, 1);
			player.getInventory().addItem(12183, 5000);
		}
		if (itemId == 20704) {
			LividFarm.bunchPlants(player);
		}
		if (itemId == 771) {// Dramen branch
			player.getInventory().deleteItem(771, 1);
			player.getInventory().addItem(772, 1);
			player.getInventory().refresh();
			return;
		}
		if (itemId == 18338) {
			player.sm("You currently have:");
			player.sm("" + player.sapphires + "xSapphires");
			player.sm("" + player.emeralds + "xEmeralds");
			player.sm("" + player.rubies + "xRubies");
			player.sm("" + player.diamonds + "xDiamonds");
		}
		if (itemId == 18339) {
			player.sm("You currently have " + player.coal + " pieces of coal in your coal bag.");
		}
		if (Pots.pot(player, item, slotId)) {
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509) {
				pouch = 0;
			}
			if (itemId == 5510) {
				pouch = 1;
			}
			if (itemId == 5512) {
				pouch = 2;
			}
			if (itemId == 5514) {
				pouch = 3;
			}
			Runecrafting.fillPouch(player, pouch);
			return;
		}
		if (itemId == 22370) {
			Summoning.openDreadnipInterface(player);
		}
		if (itemId == 11949) { // GLOBE SNOWGLOBE
			player.lock(3);
			World.spawnObject(new WorldObject(28297, 10, 0, player.getX(), player.getY(), player.getZ()), true);
			player.animate(new Animation(1745));
			player.getInterfaceManager().sendInterface(659);
			player.getInventory().addItem(11951, 27);
			player.sm("The snow globe fills your inventory with snow!");
		}
		if (itemId == 1535 || itemId == 1536 || itemId == 1537) { // Map Pieces
			if (player.getInventory().containsItem(1535, 1) && player.getInventory().containsItem(1536, 1)
					&& player.getInventory().containsItem(1537, 1)) {
				player.getInventory().deleteItem(1535, 1);
				player.getInventory().deleteItem(1536, 1);
				player.getInventory().deleteItem(1537, 1);
				player.getInventory().addItem(1538, 1);
				player.sm("You fit the pieces together and make map!");
			} else {
				player.sm("You need all three pieces to create a map!");
			}
		}
		if (itemId == 29536) { //seed box
			if (player.getInventory().containsItem(29536, 1)) {
				int[][] common = { {5313, 1}, {5314, 1}, {5315, 1}, {5316, 1}, {5318, 100}, {5319, 10}, {5320, 10}, {5321, 10}, {5322, 10}, {5323, 10}, {5324, 10}, {5283, 1}, {5285, 1}, {5286, 1}, {5287, 1}, {5288, 1}, {5291, 10}, {5292, 10}, {5293, 10}, {5294, 10}, {5295, 2}, {5296, 2}, {5297, 10}, {5298, 10}, {5299, 10}, {5300, 2}, {5301, 10}, {5302, 10}, {5303, 10}, {5304, 2}}; //Other ids go in there as well
				int length = common.length;
				length--;
				player.getInventory().deleteItem(29536, 1);
				for (int index = 0; index < 3; index++) {
					int reward = Utils.getRandom(length);
					player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
				}
			}
		}
		if (itemId == 28703) { //charm box
			if (player.getInventory().containsItem(28703, 1)) {
				int[][] common = { {18349, 1}, {18351, 1}, {18353, 1}, {18355, 1}, {18357, 1}, {18359, 1}}; //Other ids go in there as well
				int length = common.length;
				length--;
				player.getInventory().deleteItem(28703, 1);
				for (int index = 0; index < 1; index++) {
					int reward = Utils.getRandom(length);
					player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
				}
			}
		}
		if (itemId == 28951) { //charm box
			if (player.getInventory().containsItem(28951, 1)) {
				int[][] common = { {12158, 100}, {12163, 100}, {12159, 100}, {12160, 190}}; //Other ids go in there as well
				int length = common.length;
				length--;
				player.getInventory().deleteItem(28951, 1);
				for (int index = 0; index < 1; index++) {
					int reward = Utils.getRandom(length);
					player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
				}
			}
		}
		if (itemId == 29535) { //herb box
			if (player.getInventory().containsItem(29535, 1)) {
				int[][] common = { {200, 5}, {202, 5}, {204, 5}, {206, 5}, {208, 5}, {210, 5}, {212, 5}, {214, 5}, {216, 5}, {218, 5}, {220, 5}}; //Other ids go in there as well
				int length = common.length;
				length--;
				player.getInventory().deleteItem(29535, 1);
				for (int index = 0; index < 6; index++) {
					int reward = Utils.getRandom(length);
					player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
				}
			}
		}
		if (itemId == 29466) {
	    if (player.getInventory().containsItem(29466, 1)) {
    	int[][] common = {{29758, 1}, {29756, 1}, {29760, 1}, {29749, 1}};
 	    int[][] uncommon = { {29786, 1}, {29790, 1}, {52486, 1}};
 	    player.getInventory().deleteItem(29466, 1);
	    int rarity = Utils.getRandom(50);
	    if (rarity > 0 && rarity <= 48) {
			int length = common.length;
			length--;
			int reward = Utils.getRandom(length);
			int amount = common[reward][1];;
			String reward2 = ItemDefinitions.getItemDefinitions(common[reward][0]).getName().toLowerCase();
			player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
			World.sendWorldMessage(
					"<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> x"
							+ amount + " " + reward2 + "<col=F39407> from a Raids 2 chest!<img=23>",
					false);
		}
	    if (rarity > 48  && rarity <= 50) {
			int length = uncommon.length;
			length--;
			int reward = Utils.getRandom(length);
			int amount = uncommon[reward][1];;
			player.getInventory().addItemDrop(uncommon[reward][0], uncommon[reward][1]);
			String reward2 = ItemDefinitions.getItemDefinitions(uncommon[reward][0]).getName().toLowerCase();
			World.sendWorldMessage(
					"<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> Rare x"
							+ amount + " " + reward2 + "<col=F39407> from a Raids 2 chest!<img=23>",
						false);
	    }
	    }
		}
		
		
		if (itemId == 29467) {
	    if (player.getInventory().containsItem(29467, 1)) {
	    int[][] common = {{29747, 1}, {29745, 1}};
	    int[][] uncommon = { {29739, 1}, {29737, 100}, {29870, 1}};
	    int[][] rare = {{29864, 1}, {29897, 1}};
	    int[][] very_rare = {{29743, 1}, {29489, 1}, {29491, 1}, {29493, 1}, {14484, 1}};
	    int[][] legendary = {{29943, 1}, {51003, 1}, {29868, 1}, };
	    player.getInventory().deleteItem(29467, 1);
	    int rarity = Utils.getRandom(50);	
	    if (rarity > 0 && rarity <= 35) {
			int length = common.length;
			length--;
			int reward = Utils.getRandom(length);
			int amount = common[reward][1];;
			String reward2 = ItemDefinitions.getItemDefinitions(common[reward][0]).getName().toLowerCase();
			player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
			World.sendWorldMessage(
					"<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> x"
							+ amount + " " + reward2 + "<col=F39407> from a Raids chest!<img=23>",
					false);
		}
	    if (rarity > 35 && rarity <= 40) {
			int length = uncommon.length;
			length--;
			int reward = Utils.getRandom(length);
			int amount = uncommon[reward][1];;
			player.getInventory().addItemDrop(uncommon[reward][0], uncommon[reward][1]);
			String reward2 = ItemDefinitions.getItemDefinitions(uncommon[reward][0]).getName().toLowerCase();
			World.sendWorldMessage(
					"<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> x"
							+ amount + " " + reward2 + "<col=F39407> from a Raids chest!<img=23>",
						false);
	    }
	    if (rarity > 40 && rarity <= 45) {
			int length = rare.length;
			length--;
			int reward = Utils.getRandom(length);
			int amount = rare[reward][1];
			player.getInventory().addItemDrop(rare[reward][0], rare[reward][1]);
			String reward2 = ItemDefinitions.getItemDefinitions(rare[reward][0]).getName().toLowerCase();
			World.sendWorldMessage(
					"<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> x"
							+ amount + " " + reward2 + "<col=F39407> from a Raids chest!<img=23>",
						false);
		}
	    if (rarity > 45 && rarity <= 48) {
			int length = very_rare.length;
			length--;
			int reward = Utils.getRandom(length);
			int amount = very_rare[reward][1];
			player.getInventory().addItemDrop(very_rare[reward][0], very_rare[reward][1]);
			String reward2 = ItemDefinitions.getItemDefinitions(very_rare[reward][0]).getName().toLowerCase();
			World.sendWorldMessage(
					"<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> x"
							+ amount + " " + reward2 + "<col=F39407> from a Raids chest!<img=23>",
						false);
		}
	    if (rarity > 48 && rarity <= 50) {
			int length = legendary.length;
			length--;
			int reward = Utils.getRandom(length);
			int amount = legendary[reward][1];
			player.getInventory().addItemDrop(legendary[reward][0], legendary[reward][1]);
			String reward2 = ItemDefinitions.getItemDefinitions(legendary[reward][0]).getName().toLowerCase();
			World.sendWorldMessage(
					"<img=23><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> Rare x"
							+ amount + " " + reward2 + "<col=F39407> from a Raids chest!<img=23>",
						false);
		}
			
		}
	    }
		
		
		
		
//		if (itemId == 29484) {
//		if (player.getInventory().containsItem(29484, 1))	{
//		int[][] common = { { 995, 20_000_000 }, { 4732, 1 }, { 4734, 1 }, { 4736, 1 }, { 4738, 1 }, { 4708, 1 },
//				{ 4710, 1 }, { 4712, 1 }, { 4714, 1 }, { 4745, 1 }, { 4747, 1 }, { 4749, 1 }, { 4751, 1 },
//				{ 4753, 1 }, { 4755, 1 }, { 4757, 1 }, { 4759, 1 }, { 4724, 1 }, { 4726, 1 }, { 4728, 1 },
//				{ 4730, 1 }, { 4716, 1 }, { 4718, 1 }, { 4720, 1 }, { 4722, 1 }, { 21736, 1 }, { 21744, 1 },
//				{ 21752, 1 }, { 21760, 1 }, { 22361, 1 }, { 22369, 1 }, { 22365, 1 }, { 6199, 1 } };
//	    int[][] uncommon = { { 995, 20_000_000 }, { 4732, 1 }, { 4734, 1 }, { 4736, 1 }, { 4738, 1 }, { 4708, 1 },
//				{ 4710, 1 }, { 4712, 1 }, { 4714, 1 }, { 4745, 1 }, { 4747, 1 }, { 4749, 1 }, { 4751, 1 },
//				{ 4753, 1 }, { 4755, 1 }, { 4757, 1 }, { 4759, 1 }, { 4724, 1 }, { 4726, 1 }, { 4728, 1 },
//				{ 4730, 1 }, { 4716, 1 }, { 4718, 1 }, { 4720, 1 }, { 4722, 1 }, { 21736, 1 }, { 21744, 1 },
//				{ 21752, 1 }, { 21760, 1 }, { 22361, 1 }, { 22369, 1 }, { 22365, 1 }, { 6199, 1 }
//				, { 15220, 1 }, { 15017, 1 } , { 15019, 1 }, { 15020, 1 }, { 15018, 1 }};
//	    int[][] rare = { { 11724, 1 }, { 11726, 1 }, { 11728, 1 }, { 11696, 1 }
//	                   , { 11718, 1 }, { 11720, 1 }, { 11722, 1 } , { 25010, 1 }, { 25016, 1 }, { 25013, 1 }
//	                   , { 11694, 1 }, { 14484, 1 }, { 11730, 1 }, { 11716, 1 }, { 24995, 1 }
//	                   , { 29931, 1 }, { 29841, 1 }, { 29935, 1 }
//	                   , { 6571, 1 }, { 29521, 1 }, { 29523, 1 } , { 21787, 1 }, { 21790, 1 }, { 21793, 1 }
//	                   , { 29737, 100 }, { 29864, 1 }, { 29745, 1 }
//	                   , { 29747, 1 }, { 29739, 1 }, { 29870, 1 } , { 29730, 1 }, {29734, 1}, { 29732, 1 }
//	                   , { 29153, 1 }, { 29155, 1 }, { 29157, 1 } , { 29159, 1 }, { 29835, 1 }, { 29529, 1 }
//	                   , { 29639, 1 }, { 29804, 1 }, { 29805, 1 } , { 11286, 1 }, { 24338, 1 }, { 29953, 1 }
//	                   , { 29946, 1 }, { 29562, 1 }, { 29563, 1 } , { 29564, 1 }, { 29565, 1 } , { 29631, 1 }, { 29633, 1 } , { 29939, 1 }
//	                   , { 995, 30_000_000 } , { 13744, 1 }, { 13738, 1 } , { 13742, 1 }, { 13740, 1 } , { 1038, 1 }, { 1040, 1 } , { 1042, 1 }, { 1044, 1 }
//	                   , { 1046, 1 }, { 1048, 1 } , { 962, 1 }, { 1053, 1 }	, { 1055, 1 }, { 1057, 1 } , { 1050, 1 }, { 1037, 1 }																																										};
//	    int[][] super_rare = { { 29172, 1 }, { 29246, 1 }, { 29128, 1 } , { 29180, 1 }, { 29134, 1 }, { 29000, 1 }
//	                         , { 29136, 1 }, { 29130, 1 }, { 29174, 1 }	, { 29248, 1 }, { 29182, 1 }, { 29941, 1 }
//	                         , { 29178, 1 }, { 29126, 1 }, { 29244, 1 }	, { 29132, 1 }, { 29002, 1 }, { 29170, 1 }
//	                         , { 962, 1 }, { 29834, 1 }, { 29166, 1 } , { 29164, 1 }, { 29176, 1 }, { 29168, 1 }
//	                         , { 29228, 1 }, { 29493, 1 }, { 29489, 1 }	, { 29491, 1 }, { 29895, 1 } , { 29788, 1 }	, { 29868, 1 }, { 29570, 1 }
//	                         , { 29568, 1 }	, { 29572, 1 }, { 29574, 1 } , { 29566, 1 }	, { 29797, 1 }, { 29833, 1 }
//	                         , { 29943, 1 }	, { 29799, 1 }, { 29800, 1 } , { 29798, 1 }	, { 29758, 1 }, { 29760, 1 }
//	                         , { 29756, 1 }	, { 29806, 1 }, { 29791, 1 } , { 29749, 1 }	, { 29666, 1 }, { 29790, 1 }
//	                         , { 29786, 1 }	, { 29534, 1 }, { 29864, 1 } , { 29793, 1 }	, { 29669, 1 }, { 29670, 1 }
//	                         , {29885, 1 }	, { 29883, 1 } , { 995, 50_000_000 }, { 29100, 1 }	, { 29103, 1 }, { 29097, 1 }};
//	    player.getInventory().deleteItem(29484, 1);
//	    boolean jackpot = Utils.getRandom(500) == 1;
//	    boolean jackpot1 = Utils.getRandom(500) == 1;
//	    boolean jackpot2 = Utils.getRandom(500) == 1;
//	    boolean jackpot3 = Utils.getRandom(500) == 1;
//		int rarity = Utils.getRandom(1000);
//		if (rarity > 0 && rarity <= 500) {
//			int length = common.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = common[reward][1];;
//			String reward2 = ItemDefinitions.getItemDefinitions(common[reward][0]).getName().toLowerCase();
//			player.sm("You receive "   + reward2 +  " x"  + amount );
//			player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
//
//		}
//		if (jackpot) {
//			int length = common.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = common[reward][1];;
//			String reward2 = ItemDefinitions.getItemDefinitions(common[reward][0]).getName().toLowerCase();
//			player.sm("You receive "   + reward2 +  " x"  + amount );
//			player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
//			player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
//		}
//		if (rarity > 500 && rarity <= 800) {
//			int length = uncommon.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = uncommon[reward][1];;
//			player.getInventory().addItemDrop(uncommon[reward][0], uncommon[reward][1]);
//			String reward2 = ItemDefinitions.getItemDefinitions(uncommon[reward][0]).getName().toLowerCase();
//			player.sm("You receive "   + reward2 +  " x"  + amount );
//
//
//		}
//	    if (jackpot1) {
//			int length = uncommon.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = uncommon[reward][1];;
//			player.getInventory().addItemDrop(uncommon[reward][0], uncommon[reward][1]);
//			player.getInventory().addItemDrop(uncommon[reward][0], uncommon[reward][1]);
//			String reward2 = ItemDefinitions.getItemDefinitions(uncommon[reward][0]).getName().toLowerCase();
//			player.sm("You receive "   + reward2 +  " x"  + amount );
//		}
//		if (rarity > 800 && rarity <= 920) {
//			int length = rare.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = rare[reward][1];
//			player.getInventory().addItemDrop(rare[reward][0], rare[reward][1]);
//			String reward2 = ItemDefinitions.getItemDefinitions(rare[reward][0]).getName().toLowerCase();
//			World.sendWorldMessage(
//					"<img=7><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> Rare x"
//							+ amount + " " + reward2 + "<col=F39407> from a Infernal Mystery Box!<img=7>",
//					false);
//		}
//		if (jackpot2) {
//			int length = rare.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = rare[reward][1];
//			player.getInventory().addItemDrop(rare[reward][0], rare[reward][1]);
//			player.getInventory().addItemDrop(rare[reward][0], rare[reward][1]);
//			String reward2 = ItemDefinitions.getItemDefinitions(rare[reward][0]).getName().toLowerCase();
//			World.sendWorldMessage(
//					"<img=7><col=F39407>" + player.getDisplayName() + "<col=F39407> Hit jackpot and Received double <col=F39407> Rare x"
//							+ amount + " " + reward2 + "<col=F39407> from a Infernal Mystery Box!<img=7>",
//					false);
//		}
//		if (rarity > 920 && rarity <= 930) {
//			int length = super_rare.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = super_rare[reward][1];
//			player.getInventory().addItemDrop(super_rare[reward][0], super_rare[reward][1]);
//			String reward2 = ItemDefinitions.getItemDefinitions(super_rare[reward][0]).getName().toLowerCase();
//			player.setNextGraphics(new Graphics(1765));
//			player.setNextAnimation(new Animation(4939));
//			World.sendWorldMessage(
//					"<img=7><img=7><img=7><col=F39407>" + player.getDisplayName() + "<col=F39407> Received<col=F39407> UltraRare x"
//							+ amount + " " + reward2 + "<col=F39407> from a Infernal Mystery Box!<img=7><img=7><img=7>",
//					false);
//		}
//		if (jackpot3) {
//			int length = super_rare.length;
//			length--;
//			int reward = Utils.getRandom(length);
//			int amount = super_rare[reward][1];
//			player.getInventory().addItemDrop(super_rare[reward][0], super_rare[reward][1]);
//			player.getInventory().addItemDrop(super_rare[reward][0], super_rare[reward][1]);
//			String reward2 = ItemDefinitions.getItemDefinitions(super_rare[reward][0]).getName().toLowerCase();
//			player.setNextGraphics(new Graphics(1765));
//			player.setNextAnimation(new Animation(4939));
//			World.sendWorldMessage(
//					"<img=7><img=7><img=7><col=F39407>" + player.getDisplayName() + "<col=F39407> Hit the Jackpot and Received<col=F39407> UltraRare x"
//							+ amount + " " + reward2 + "<col=F39407> from a Infernal Mystery Box!<img=7><img=7><img=7>",
//					false);
//		}
//
//		}
//		}
//		if (itemId == 6199) {
//			if (player.getInventory().containsItem(6199, 1)) {
//				int[][] common = { { 995, 10_000_000 }, { 4732, 1 }, { 4734, 1 }, { 4736, 1 }, { 4738, 1 }, { 4708, 1 },
//						{ 4710, 1 }, { 4712, 1 }, { 4714, 1 }, { 4745, 1 }, { 4747, 1 }, { 4749, 1 }, { 4751, 1 },
//						{ 4753, 1 }, { 4755, 1 }, { 4757, 1 }, { 4759, 1 }, { 4724, 1 }, { 4726, 1 }, { 4728, 1 },
//						{ 4730, 1 }, { 4716, 1 }, { 4718, 1 }, { 4720, 1 }, { 4722, 1 }, { 21736, 1 }, { 21744, 1 },
//						{ 21752, 1 }, { 21760, 1 } }; // dont forget to put "," between item id's!
//				int[][] uncommon = { { 995, 20_000_000 }, { 15486, 1 }, { 6737, 1 }, { 6733, 1 }, { 6731, 1 },
//						{ 6735, 1 }, { 2581, 1 }, { 6739, 1 }, { 22358, 1 }, { 22362, 1 }, { 22366, 1 }, { 20072, 1 },
//						{ 6585, 1 }, { 11235, 1 }, { 11212, 1000 }, { 6920, 1 }, { 15602, 1 }, { 15608, 1 },
//						{ 15614, 1 }, { 15620, 1 }, { 15600, 1 }, { 15606, 1 }, { 15612, 1 }, { 15618, 1 },
//						{ 15604, 1 }, { 15610, 1 }, { 15616, 1 }, { 15622, 1 } };
//				int[][] rare = { { 995, 30_000_000 }, { 23679, 1 }, { 23680, 1 }, { 23681, 1 }, { 23682, 1 },
//				        { 23684, 1 }, { 23685, 1 }, { 23686, 1 }, { 23687, 1 }, { 23688, 1 },
//					    { 23690, 1 }, { 23691, 1 }, { 23694, 1 },
//						{ 23695, 1 }, { 23697, 1 }, { 23698, 1 }, { 23699, 1 }, { 23700, 1 },
//						{ 995, 40_000_000 }, { 11283, 1 }, { 1038, 1 }, { 1040, 1 }, { 1042, 1 }, { 1044, 1 },
//						{ 1046, 1 }, { 1048, 1 }, { 1053, 1 }, { 1055, 1 }, { 1057, 1 }, { 29966, 1 }, { 29947, 1 },
//						{ 29946, 1 }, { 29939, 1 }, { 962, 1 }, { 29935, 1 }, { 29929, 1 }, { 29927, 1 }, { 29926, 1 },
//						{ 29893 }, { 29886, 1 } };
//				int[][] super_rare = { { 995, 30_000_000 }, { 962, 1 }, { 23679, 1 }, { 23680, 1 }, { 23681, 1 },
//						{ 23682, 1 }, { 23684, 1 }, { 23685, 1 }, { 23686, 1 }, { 23687, 1 },
//						{ 23688, 1 }, { 23690, 1 }, { 23691, 1 },
//						{ 23694, 1 }, { 23695, 1 }, { 23697, 1 }, { 23698, 1 }, { 23699, 1 },
//						{ 23700, 1 }, { 995, 40_000_000 }, { 11283, 1 }, { 1038, 1 }, { 1040, 1 }, { 1042, 1 },
//						{ 1044, 1 }, { 1046, 1 }, { 1048, 1 }, { 1053, 1 }, { 1055, 1 }, { 1057, 1 }, { 29966, 1 },
//						{ 29947, 1 }, { 29946, 1 }, { 29941, 1 }, { 29939, 1 }, { 29935, 1 }, { 29929, 1 },
//						{ 29927, 1 }, { 29926, 1 }, { 29893 }, { 29886, 1 } };
//				int[][] legendary = { { 995, 80_000_000 }, { 29941, 1 }, { 29895, 1 }, { 29897, 1 }, { 29943, 1 },
//						{ 29000, 1 }, { 29002 }, { 29012 }, { 29885, 1 }, { 29879, 1 }, { 29877, 1 }, { 29872, 1 },
//						{ 29870, 1 }, { 29866, 1 }, { 29864, 1 } };
//				player.getInventory().deleteItem(6199, 1);
//				int rarity = Utils.getRandom(1000);
//				if (rarity > 0 && rarity <= 500) {
//					int length = common.length;
//					length--;
//					int reward = Utils.getRandom(length);
//					player.getInventory().addItemDrop(common[reward][0], common[reward][1]);
//				}
//				if (rarity > 500 && rarity <= 800) {
//					int length = uncommon.length;
//					length--;
//					int reward = Utils.getRandom(length);
//					player.getInventory().addItemDrop(uncommon[reward][0], uncommon[reward][1]);
//				}
//				if (rarity > 800 && rarity <= 920) {
//					int length = rare.length;
//					length--;
//					int reward = Utils.getRandom(length);
//					int amount = rare[reward][1];
//					player.getInventory().addItemDrop(rare[reward][0], rare[reward][1]);
//					String reward2 = ItemDefinitions.getItemDefinitions(rare[reward][0]).getName().toLowerCase();
//					World.sendWorldMessage(
//							"<img=6><col=ff0808>" + player.getDisplayName() + "<col=ff0808> Received<col=ff0808> Rare x"
//									+ amount + " " + reward2 + "<col=ff0808> from a Mystery Box!<img=6>",
//							false);
//				}
//				if (rarity > 920 && rarity <= 930) {
//					int length = super_rare.length;
//					length--;
//					int reward = Utils.getRandom(length);
//					int amount = super_rare[reward][1];
//					player.getInventory().addItemDrop(super_rare[reward][0], super_rare[reward][1]);
//					String reward2 = ItemDefinitions.getItemDefinitions(super_rare[reward][0]).getName().toLowerCase();
//					;
//					World.sendWorldMessage(
//							"<img=6><col=ff0808>" + player.getDisplayName() + "<col=ff0808> Received<col=ff0808> Rare x"
//									+ amount + " " + reward2 + "<col=ff0808> from a Mystery Box!<img=6>",
//							false);
//				}
//				if (rarity > 970 && rarity <= 997) {
//					player.getInventory().addItemDrop(6199, 1);
//				}
//				if (rarity > 997 && rarity <= 1000) {
//					int length = legendary.length;
//					length--;
//					int reward = Utils.getRandom(length);
//					int amount = legendary[reward][1];
//					player.getInventory().addItemDrop(legendary[reward][0], legendary[reward][1]);
//					String reward2 = ItemDefinitions.getItemDefinitions(legendary[reward][0]).getName().toLowerCase();
//					World.sendWorldMessage("<img=6><col=ff0808>" + player.getDisplayName()
//							+ "<col=ff0808> Received<col=ff0808> UltraRare x" + amount + " " + reward2
//							+ "<col=ff0808> from a Mystery Box!<img=6>", false);
//				}
//				return;
//			}
//		}
		if (item.getId() == 29988) {
			if (player.getSkills().getXp(Skills.AGILITY) < 104273167) {
				player.getPackets()
						.sendGameMessage("To wear this cape you need 104,273,167 experience in the following: ");
			}
			player.getPackets().sendGameMessage("Agility");
			return;
		}
		if (item.getId() == 29111) {
			if (player.getInventory().containsItem(29111, 1)) {
				player.getInventory().deleteItem(29111, 1);
				player.getInventory().addItem(15273, 100);
			}
		}

		if (item.getId() == 29110) {
			if (player.getInventory().containsItem(29110, 1) && player.getInventory().hasFreeSlots()) {
				player.getInventory().deleteItem(29110, 1);
				player.getInventory().addItem(15332, 10);
			}
		}
		if (item.getId() == 29113) {
			if (player.getInventory().containsItem(29113, 1)) {
				player.getInventory().deleteItem(29113, 1);
				player.getInventory().addItem(23400, 30);
			}
		}
		if (item.getId() == 29112) {
			if (player.getInventory().containsItem(29112, 1)) {
				player.getInventory().deleteItem(29112, 1);
				player.getInventory().addItem(23352, 30);
			}
		}
		if (HerbCleaning.clean(player, item, slotId)) {
			return;
		}
		Bone bone = Bone.forId(itemId);
		if (bone != null) {
			Bone.bury(player, slotId);
			return;
		}
		if (itemId == 4597) { //Santas note
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendIComponentText(275, 1, "Santa's note");
			player.getPackets().sendIComponentText(275, 11, (player.getInventory().contains(29541) != -1 ? "<str>" : "") + "Satan: Lumbridge swamp");
			player.getPackets().sendIComponentText(275, 13, (player.getInventory().contains(6542) != -1 ? "<str>" : "") + "Imp #1: Varrock palace");
			player.getPackets().sendIComponentText(275, 14, (player.getInventory().contains(22991) != -1 ? "<str>" : "") + "Imp #2: Daemonheim");
			player.getPackets().sendIComponentText(275, 15, (player.getInventory().contains(15420) != -1 ? "<str>" : "") + "Imp #3: Vorkath");
		}
		if (itemId == 299) {
			if (player.isLocked()) {
				return;
			}
			if (World.getObject(new Position(player), 10) != null) {
				player.getPackets().sendGameMessage("You cannot plant flowers here..");
				return;
			}
			final Player thisman = player;
			final double random = Utils.getRandomDouble(100);
			final Position tile = new Position(player);
			int flower = Utils.random(2980, 2987);
			if (random < 0.2) {
				flower = Utils.random(2987, 2989);
			}
			if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1)) {
				if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1)) {
					if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1)) {
						player.addWalkSteps(player.getX(), player.getY() - 1, 1);
					}
				}
			}
			player.getInventory().deleteItem(299, 1);
			final WorldObject flowerObject = new WorldObject(FLOWERS[Utils.random(FLOWERS.length)], 10,
					Utils.getRandom(4), tile.getX(), tile.getY(), tile.getZ());
			World.spawnTemporaryObject(flowerObject, 45000);
			player.lock();
			WorldTasksManager.schedule(new WorldTask() {
				int step;

				@Override
				public void run() {
					if (thisman == null || thisman.isFinished()) {
						stop();
					}
					if (step == 1) {
						thisman.getDialogueManager().startDialogue("FlowerPickup", flowerObject);
						thisman.setNextFacePosition(tile);
						thisman.unlock();
						stop();
					}
					step++;
				}
			}, 0, 0);

		}
		if (itemId == 19832) {
			player.setNextPosition(new Position(3048, 5837, 1));
		}
		if (Magic.useTabTeleport(player, itemId)) {
			return;
		}
		if (itemId == AncientEffigies.SATED_ANCIENT_EFFIGY || itemId == AncientEffigies.GORGED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.NOURISHED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.STARVED_ANCIENT_EFFIGY) {
			player.getDialogueManager().startDialogue("AncientEffigiesD", itemId);
		} else if (itemId == 6099) {
			if (player.getJailed() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("No, no... I stay...");
			} else {
				player.monsterPageOne = true;
				player.monsterPageTwo = false;
				/*
				 * if (player.crystalcharges <= 0) { player.getPackets().
				 * sendGameMessage("You must recharge your crystal with the '::buycharges' with Loyalty Tokens."
				 * ); player.getPackets().
				 * sendGameMessage("Don't worry, everything is accessible from our world." );
				 * player.getPackets().
				 * sendGameMessage("You can reach every monster, location, minigame, etc." );
				 * player.getPackets().
				 * sendGameMessage("Our interactive world provides sailing, enchanted jewlery, gnome gliders, and more!"
				 * ); } else { player.crystalcharges--;
				 * player.getPackets().sendGameMessage("You now have "+player.
				 * crystalcharges+" left in your crystal teleport.");
				 */
				player.getDialogueManager().startDialogue("TeleportCrystal");
				// }
			}
		} else if (itemId == 4251) {
			player.getEctophial().ProcessTeleportation(player);
		}

		else if (item.getDefinitions().containsOption(0, "Craft")
				|| item.getDefinitions().containsOption(0, "Fletch")) {
			if (player.getInventory().containsItemToolBelt(946, 1)) {
				Fletch fletch = Fletching.isFletching(item, new Item(946));
				if (fletch != null) {
					player.getDialogueManager().startDialogue("FletchingD", fletch);
					return;
				}
			} else if (player.getInventory().containsItemToolBelt(1755, 1)) {
				Fletch fletch = Fletching.isFletching(item, new Item(1755));
				if (fletch != null) {
					player.getDialogueManager().startDialogue("FletchingD", fletch);
					return;
				}
				Gem gem = Gem.forId(item.getId());
				if (gem != null) {
					player.getDialogueManager().startDialogue("GemCuttingD", gem);
					return;
				}
				BoltTips tip = BoltTips.forId(item.getId());
				if (tip != null) {
					player.getDialogueManager().startDialogue("BoltTipMaking", tip);
					return;
				}
			} else {
				player.getDialogueManager().startDialogue("ItemMessage",
						"You need a knife or chisle to complete the action.", 946);
			}
		} else if (itemId == 455) {
			player.finishBarCrawl();
			player.getInterfaceManager().sendBarcrawl();

		} else if (itemId == 4155) {
			player.getDialogueManager().startDialogue("EnchantedGemDialouge",
					player.getSlayerManager().getCurrentMaster().getNPCId());
		} else if (itemId == 14057) {
			player.animate(new Animation(10532));
		} else if (itemId == SqirkFruitSqueeze.SqirkFruit.AUTUMM.getFruitId()) {
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.AUTUMM);
		} else if (itemId == SqirkFruitSqueeze.SqirkFruit.SPRING.getFruitId()) {
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.SPRING);
		} else if (itemId == SqirkFruitSqueeze.SqirkFruit.SUMMER.getFruitId()) {
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.SUMMER);
		} else if (itemId == SqirkFruitSqueeze.SqirkFruit.WINTER.getFruitId()) {
			player.getDialogueManager().startDialogue("SqirkFruitSqueeze", SqirkFruit.WINTER);
		} else if (itemId == 15262) {
			ItemSets.openSkillPack(player, itemId, 12183, 5000, 1);
		} else if (itemId == 15362) {
			ItemSets.openSkillPack(player, itemId, 230, 50, 1);
		} else if (itemId == 15363) {
			ItemSets.openSkillPack(player, itemId, 228, 50, 1);
		} else if (itemId == 15364) {
			ItemSets.openSkillPack(player, itemId, 222, 50, 1);
		} else if (itemId == 15365) {
			ItemSets.openSkillPack(player, itemId, 9979, 50, 1);
		} else if (itemId == 1917) {
			player.getPackets().sendGameMessage("You Drink the Beer.");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(1917, 1);

		} else if (itemId == 5763) {
			player.getPackets().sendGameMessage("You Drink the Cider.");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(5763, 1);

		} else if (itemId == 1905) {
			player.getPackets().sendGameMessage("You Drink the Asgarnian Ale.");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(1905, 1);
			player.getAppearence().setRenderEmote(52);

		} else if (itemId == 1909) {
			player.getPackets().sendGameMessage("You Drink the Greenman's Ale.");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(1909, 1);
			player.getAppearence().setRenderEmote(52);

		} else if (itemId == 5755) {
			player.getPackets().sendGameMessage("You Drink the Chef's Delight.");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(5755, 1);
			player.getAppearence().setRenderEmote(52);

		} else if (itemId == 1911) {
			player.getPackets().sendGameMessage("You Drink the Dragon Bitter.");
			player.setNextForceTalk(new ForceTalk("Holy shit that was intense!"));
			player.animate(new Animation(829));
			player.getInventory().deleteItem(1911, 1);
			player.getAppearence().setRenderEmote(290);

		} else if (itemId == 1907) {
			player.getPackets().sendGameMessage("You Drink the Wizard's MindBomb");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(1907, 1);

		} else if (itemId == 3801) {
			player.getPackets().sendGameMessage("You Drink the Keg of Beer...And feel Quite Drunk...");
			player.animate(new Animation(1330));
			player.getInventory().deleteItem(3801, 1);

		} else if (itemId == 3803) {
			player.getPackets().sendGameMessage("You drink the Beer");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(3803, 1);

		} else if (itemId == 431) {
			player.getPackets().sendGameMessage("You drink the rum");
			player.animate(new Animation(829));
			player.getInventory().deleteItem(431, 1);

		} else if (itemId == 21576) {
			player.getDialogueManager().startDialogue("DrakansMedallion");
		} else if (itemId == 11328) {
			player.getInventory().deleteItem(11328, 1);
			player.getInventory().addItem(11324, 1);
		} else if (itemId == 11330) {
			player.getInventory().deleteItem(11330, 1);
			player.getInventory().addItem(11324, 1);
		} else if (itemId == 11332) {
			player.getInventory().deleteItem(11332, 1);
			player.getInventory().addItem(11324, 1);
		}

		// Armour sets Opening
		else if (itemId == 11814) {// Bronze (l)
			player.getInventory().deleteItem(11814, 1);
			player.getInventory().addItem(1075, 1);
			player.getInventory().addItem(1103, 1);
			player.getInventory().addItem(1155, 1);
			player.getInventory().addItem(1189, 1);
		} else if (itemId == 11818) {// iron (l)
			player.getInventory().deleteItem(11818, 1);
			player.getInventory().addItem(1067, 1);
			player.getInventory().addItem(1115, 1);
			player.getInventory().addItem(1153, 1);
			player.getInventory().addItem(1191, 1);
		} else if (itemId == 11822) {// steel (l)
			player.getInventory().deleteItem(11822, 1);
			player.getInventory().addItem(1069, 1);
			player.getInventory().addItem(1119, 1);
			player.getInventory().addItem(1157, 1);
			player.getInventory().addItem(1193, 1);
		} else if (itemId == 11826) {// black (l)
			player.getInventory().deleteItem(11826, 1);
			player.getInventory().addItem(1077, 1);
			player.getInventory().addItem(1125, 1);
			player.getInventory().addItem(1165, 1);
			player.getInventory().addItem(1195, 1);
		} else if (itemId == 11830) {// mithril (l)
			player.getInventory().deleteItem(11830, 1);
			player.getInventory().addItem(1071, 1);
			player.getInventory().addItem(1121, 1);
			player.getInventory().addItem(1159, 1);
			player.getInventory().addItem(1197, 1);
		} else if (itemId == 11834) {// adamant (l)
			player.getInventory().deleteItem(11834, 1);
			player.getInventory().addItem(1073, 1);
			player.getInventory().addItem(1123, 1);
			player.getInventory().addItem(1161, 1);
			player.getInventory().addItem(1199, 1);
		} else if (itemId == 11838) {// Rune (l)
			player.getInventory().deleteItem(11838, 1);
			player.getInventory().addItem(1079, 1);
			player.getInventory().addItem(1127, 1);
			player.getInventory().addItem(1163, 1);
			player.getInventory().addItem(1201, 1);
		} else if (itemId == 14527) {// elite black night
			player.getInventory().deleteItem(14527, 1);
			player.getInventory().addItem(14490, 1);
			player.getInventory().addItem(14492, 1);
			player.getInventory().addItem(14494, 1);
		} else if (itemId == 11942) {// Rockshell
			player.getInventory().deleteItem(11942, 1);
			player.getInventory().addItem(6128, 1);
			player.getInventory().addItem(6129, 1);
			player.getInventory().addItem(6130, 1);
			player.getInventory().addItem(6145, 1);
			player.getInventory().addItem(6151, 1);
		} else if (itemId == 11842) {// dragon chain-mail (l)
			player.getInventory().deleteItem(11842, 1);
			player.getInventory().addItem(4087, 1);
			player.getInventory().addItem(3140, 1);
			player.getInventory().addItem(1149, 1);
		} else if (itemId == 11844) {// dragon chain-mail (sk)
			player.getInventory().deleteItem(11844, 1);
			player.getInventory().addItem(4585, 1);
			player.getInventory().addItem(3140, 1);
			player.getInventory().addItem(1149, 1);
		} else if (itemId == 14529) {// dragon Plate armour (l)
			player.getInventory().deleteItem(14529, 1);
			player.getInventory().addItem(4087, 1);
			player.getInventory().addItem(14479, 1);
			player.getInventory().addItem(11335, 1);
		} else if (itemId == 14529) {// dragon Plate armour (sk)
			player.getInventory().deleteItem(14529, 1);
			player.getInventory().addItem(4585, 1);
			player.getInventory().addItem(14479, 1);
			player.getInventory().addItem(11335, 1);
		}

		// Dhide armour

		else if (itemId == 11864) {// green dhide
			player.getInventory().deleteItem(11864, 1);
			player.getInventory().addItem(1135, 1);
			player.getInventory().addItem(1099, 1);
			player.getInventory().addItem(1065, 1);
		} else if (itemId == 11866) {// blue dhide
			player.getInventory().deleteItem(11866, 1);
			player.getInventory().addItem(2499, 1);
			player.getInventory().addItem(2493, 1);
			player.getInventory().addItem(2487, 1);
		} else if (itemId == 11868) {// Red dhide
			player.getInventory().deleteItem(11868, 1);
			player.getInventory().addItem(2501, 1);
			player.getInventory().addItem(2495, 1);
			player.getInventory().addItem(2489, 1);
		} else if (itemId == 11870) {// Black Dhide
			player.getInventory().deleteItem(11870, 1);
			player.getInventory().addItem(2503, 1);
			player.getInventory().addItem(2497, 1);
			player.getInventory().addItem(2491, 1);
		} else if (itemId == 11944) {// Spined armour set
			player.getInventory().deleteItem(11944, 1);
			player.getInventory().addItem(6131, 1);
			player.getInventory().addItem(6133, 1);
			player.getInventory().addItem(6135, 1);
			player.getInventory().addItem(6143, 1);
			player.getInventory().addItem(6149, 1);
		} else if (itemId == 11920) {// Blessed Green dhide
			player.getInventory().deleteItem(11920, 1);
			player.getInventory().addItem(10376, 1);
			player.getInventory().addItem(10378, 1);
			player.getInventory().addItem(10380, 1);
			player.getInventory().addItem(10382, 1);
		} else if (itemId == 11922) {// Blessed Blue dhide
			player.getInventory().deleteItem(11922, 1);
			player.getInventory().addItem(10384, 1);
			player.getInventory().addItem(10386, 1);
			player.getInventory().addItem(10388, 1);
			player.getInventory().addItem(10390, 1);
		} else if (itemId == 11924) {// Blessed red dhide
			player.getInventory().deleteItem(11924, 1);
			player.getInventory().addItem(10368, 1);
			player.getInventory().addItem(10370, 1);
			player.getInventory().addItem(10372, 1);
			player.getInventory().addItem(10374, 1);
		} else if (itemId == 19582) {// Blessed dyed brown dhide
			player.getInventory().deleteItem(19582, 1);
			player.getInventory().addItem(19451, 1);
			player.getInventory().addItem(19453, 1);
			player.getInventory().addItem(19455, 1);
			player.getInventory().addItem(19457, 1);
		} else if (itemId == 19584) {// Blessed dyed purple dhide
			player.getInventory().deleteItem(19584, 1);
			player.getInventory().addItem(19443, 1);
			player.getInventory().addItem(19445, 1);
			player.getInventory().addItem(19447, 1);
			player.getInventory().addItem(19449, 1);
		} else if (itemId == 19586) {// Blessed dyed silver dhide
			player.getInventory().deleteItem(19586, 1);
			player.getInventory().addItem(19459, 1);
			player.getInventory().addItem(19461, 1);
			player.getInventory().addItem(19463, 1);
			player.getInventory().addItem(19465, 1);
		} else if (itemId == 24386) {// Royal Dhide
			player.getInventory().deleteItem(24386, 1);
			player.getInventory().addItem(24382, 1);
			player.getInventory().addItem(24379, 1);
			player.getInventory().addItem(24376, 1);
		}

		// Mage
		else if (itemId == 11902) {// Enchanted
			player.getInventory().deleteItem(11902, 1);
			player.getInventory().addItem(7398, 1);
			player.getInventory().addItem(7399, 1);
			player.getInventory().addItem(7400, 1);
		} else if (itemId == 11874) {// Infinity robes
			player.getInventory().deleteItem(11874, 1);
			player.getInventory().addItem(6916, 1);
			player.getInventory().addItem(6918, 1);
			player.getInventory().addItem(6920, 1);
			player.getInventory().addItem(6922, 1);
			player.getInventory().addItem(6924, 1);
		} else if (itemId == 14525) {// Dragon 'hai
			player.getInventory().deleteItem(14525, 1);
			player.getInventory().addItem(14497, 1);
			player.getInventory().addItem(14499, 1);
			player.getInventory().addItem(14501, 1);
		} else if (itemId == 11876) {// Splitbark
			player.getInventory().deleteItem(11876, 1);
			player.getInventory().addItem(3385, 1);
			player.getInventory().addItem(3387, 1);
			player.getInventory().addItem(3389, 1);
			player.getInventory().addItem(3391, 1);
			player.getInventory().addItem(3393, 1);
		} else if (itemId == 11946) {// skeletal
			player.getInventory().deleteItem(11946, 1);
			player.getInventory().addItem(6137, 1);
			player.getInventory().addItem(6139, 1);
			player.getInventory().addItem(6141, 1);
			player.getInventory().addItem(6147, 1);
			player.getInventory().addItem(6153, 1);
		}

		else if (itemId == 11872) {// Blue mystic
			player.getInventory().deleteItem(11872, 1);
			player.getInventory().addItem(4089, 1);
			player.getInventory().addItem(4091, 1);
			player.getInventory().addItem(4093, 1);
			player.getInventory().addItem(4095, 1);
			player.getInventory().addItem(4097, 1);
		}

		else if (itemId == 11962) {// dark mystic
			player.getInventory().deleteItem(11962, 1);
			player.getInventory().addItem(4099, 1);
			player.getInventory().addItem(4101, 1);
			player.getInventory().addItem(4103, 1);
			player.getInventory().addItem(4105, 1);
			player.getInventory().addItem(4107, 1);
		} else if (itemId == 11960) {// Light Mystic
			player.getInventory().deleteItem(11960, 1);
			player.getInventory().addItem(4109, 1);
			player.getInventory().addItem(4111, 1);
			player.getInventory().addItem(4113, 1);
			player.getInventory().addItem(4115, 1);
			player.getInventory().addItem(4117, 1);
		}

		// Third age armour

		else if (itemId == 11858) {// Melee
			player.getInventory().deleteItem(11858, 1);
			player.getInventory().addItem(10350, 1);
			player.getInventory().addItem(10348, 1);
			player.getInventory().addItem(10346, 1);
			player.getInventory().addItem(10352, 1);
		} else if (itemId == 11860) {// Range
			player.getInventory().deleteItem(11860, 1);
			player.getInventory().addItem(10334, 1);
			player.getInventory().addItem(10330, 1);
			player.getInventory().addItem(10332, 1);
			player.getInventory().addItem(10336, 1);
		} else if (itemId == 11862) {// mage
			player.getInventory().deleteItem(11862, 1);
			player.getInventory().addItem(10342, 1);
			player.getInventory().addItem(10338, 1);
			player.getInventory().addItem(10340, 1);
			player.getInventory().addItem(10344, 1);
		} else if (itemId == 19580) {// Druidic
			player.getInventory().deleteItem(19580, 1);
			player.getInventory().addItem(19309, 1);
			player.getInventory().addItem(19311, 1);
			player.getInventory().addItem(19314, 1);
			player.getInventory().addItem(19317, 1);
			player.getInventory().addItem(19320, 1);
		}

		// Barrows sets
		if (itemId == 11846) {// Ahrims
			player.getPackets().sendGameMessage("You open The barrows set and claim the armour.");
			player.getInventory().deleteItem(11846, 1);
			player.getInventory().addItem(4708, 1);
			player.getInventory().addItem(4710, 1);
			player.getInventory().addItem(4712, 1);
			player.getInventory().addItem(4714, 1);
		} else if (itemId == 11848) {// Dharoks
			player.getPackets().sendGameMessage("You open The barrows set and claim the armour.");
			player.getInventory().deleteItem(11848, 1);
			player.getInventory().addItem(4716, 1);
			player.getInventory().addItem(4718, 1);
			player.getInventory().addItem(4720, 1);
			player.getInventory().addItem(4722, 1);
		} else if (itemId == 11850) {// Guthans
			player.getPackets().sendGameMessage("You open The barrows set and claim the armour.");
			player.getInventory().deleteItem(11850, 1);
			player.getInventory().addItem(4724, 1);
			player.getInventory().addItem(4726, 1);
			player.getInventory().addItem(4728, 1);
			player.getInventory().addItem(4730, 1);
		} else if (itemId == 11852) {// Karils
			player.getPackets().sendGameMessage("You open The barrows set and claim the armour.");
			player.getInventory().deleteItem(11852, 1);
			player.getInventory().addItem(4732, 1);
			player.getInventory().addItem(4734, 1);
			player.getInventory().addItem(4736, 1);
			player.getInventory().addItem(4738, 1);
		} else if (itemId == 11854) {// Torags
			player.getPackets().sendGameMessage("You open The barrows set and claim the armour.");
			player.getInventory().deleteItem(11854, 1);
			player.getInventory().addItem(4745, 1);
			player.getInventory().addItem(4747, 1);
			player.getInventory().addItem(4749, 1);
			player.getInventory().addItem(4751, 1);
		} else if (itemId == 11856) {// Veracs
			player.getPackets().sendGameMessage("You open The barrows set and claim the armour.");
			player.getInventory().deleteItem(11856, 1);
			player.getInventory().addItem(4753, 1);
			player.getInventory().addItem(4755, 1);
			player.getInventory().addItem(4757, 1);
			player.getInventory().addItem(4759, 1);
		} else if (itemId == 21768) {// Akrisaes
			player.getPackets().sendGameMessage("You open The barrows set and claim the armour.");
			player.getInventory().deleteItem(21768, 1);
			player.getInventory().addItem(21736, 1);
			player.getInventory().addItem(21744, 1);
			player.getInventory().addItem(21752, 1);
			player.getInventory().addItem(21760, 1);
		} else if (itemId >= 23653 && itemId <= 23658) {
			FightKiln.useCrystal(player, itemId);
		}
		if (itemId == 405) {
			int[] reward = { 20000, 30000, 40000, 50000, 75000 };
			int won = reward[Utils.random(reward.length - 1)];
			player.getInventory().deleteItem(405, 1);
			player.getInventory().addItemMoneyPouch(new Item(995, won));
			player.getPackets().sendGameMessage("The casket slowly opens... You receive " + won + " coins!");
		} else if (itemId == 24154 || itemId == 24155) {
			player.getSquealOfFortune().processItemClick(slotId, itemId, item);
			/*
			 * } else if (itemId == 15389) { player.getSkills().addXp(Skills.ATTACK, 20000);
			 * player.getSkills().addXp(Skills.STRENGTH, 20000);
			 * player.getSkills().addXp(Skills.DEFENCE, 20000);
			 * player.getSkills().addXp(Skills.RANGE, 20000);
			 * player.getSkills().addXp(Skills.MAGIC, 20000);
			 * player.getSkills().addXp(Skills.PRAYER, 20000);
			 * player.getSkills().addXp(Skills.RUNECRAFTING, 20000);
			 * player.getSkills().addXp(Skills.CONSTRUCTION, 20000);
			 * player.getSkills().addXp(Skills.DUNGEONEERING, 20000);
			 * player.getSkills().addXp(Skills.HITPOINTS, 20000);
			 * player.getSkills().addXp(Skills.AGILITY, 20000);
			 * player.getSkills().addXp(Skills.HERBLORE, 20000);
			 * player.getSkills().addXp(Skills.THIEVING, 20000);
			 * player.getSkills().addXp(Skills.CRAFTING, 20000);
			 * player.getSkills().addXp(Skills.FLETCHING, 20000);
			 * player.getSkills().addXp(Skills.SLAYER, 20000);
			 * player.getSkills().addXp(Skills.HUNTER, 20000);
			 * player.getSkills().addXp(Skills.MINING, 20000);
			 * player.getSkills().addXp(Skills.SMITHING, 20000);
			 * player.getSkills().addXp(Skills.FISHING, 20000);
			 * player.getSkills().addXp(Skills.COOKING, 20000);
			 * player.getSkills().addXp(Skills.FIREMAKING, 20000);
			 * player.getSkills().addXp(Skills.FARMING, 20000);
			 * player.getSkills().addXp(Skills.WOODCUTTING, 20000);
			 * player.getSkills().addXp(Skills.SUMMONING, 20000);
			 * player.getInventory().deleteItem(15389, 1); } else if (itemId == 23717) {
			 * player.getSkills().addXp(Skills.ATTACK, 250);
			 * player.getInventory().deleteItem(23717, 1); } else if (itemId == 23721) {
			 * player.getSkills().addXp(Skills.STRENGTH, 250);
			 * player.getInventory().deleteItem(23721, 1); } else if (itemId == 23725) {
			 * player.getSkills().addXp(Skills.DEFENCE, 250);
			 * player.getInventory().deleteItem(23725, 1); } else if (itemId == 23729) {
			 * player.getSkills().addXp(Skills.RANGE, 250);
			 * player.getInventory().deleteItem(23729, 1); } else if (itemId == 23733) {
			 * player.getSkills().addXp(Skills.MAGIC, 250);
			 * player.getInventory().deleteItem(23733, 1); } else if (itemId == 23737) {
			 * player.getSkills().addXp(Skills.PRAYER, 250);
			 * player.getInventory().deleteItem(23737, 1); } else if (itemId == 23741) {
			 * player.getSkills().addXp(Skills.RUNECRAFTING, 250);
			 * player.getInventory().deleteItem(23741, 1); } else if (itemId == 23745) {
			 * player.getSkills().addXp(Skills.CONSTRUCTION, 250);
			 * player.getInventory().deleteItem(23745, 1); } else if (itemId == 23749) {
			 * player.getSkills().addXp(Skills.DUNGEONEERING, 250);
			 * player.getInventory().deleteItem(23749, 1); } else if (itemId == 23753) {
			 * player.getSkills().addXp(Skills.HITPOINTS, 250);
			 * player.getInventory().deleteItem(23753, 1); } else if (itemId == 23757) {
			 * player.getSkills().addXp(Skills.AGILITY, 250);
			 * player.getInventory().deleteItem(23757, 1); } else if (itemId == 23761) {
			 * player.getSkills().addXp(Skills.HERBLORE, 250);
			 * player.getInventory().deleteItem(23761, 1); } else if (itemId == 23765) {
			 * player.getSkills().addXp(Skills.THIEVING, 250);
			 * player.getInventory().deleteItem(23765, 1); } else if (itemId == 23769) {
			 * player.getSkills().addXp(Skills.CRAFTING, 250);
			 * player.getInventory().deleteItem(23769, 1); } else if (itemId == 23774) {
			 * player.getSkills().addXp(Skills.FLETCHING, 250);
			 * player.getInventory().deleteItem(23774, 1); } else if (itemId == 23778) {
			 * player.getSkills().addXp(Skills.SLAYER, 250);
			 * player.getInventory().deleteItem(23778, 1); } else if (itemId == 23782) {
			 * player.getSkills().addXp(Skills.HUNTER, 250);
			 * player.getInventory().deleteItem(23782, 1); } else if (itemId == 23786) {
			 * player.getSkills().addXp(Skills.MINING, 250);
			 * player.getInventory().deleteItem(23786, 1); } else if (itemId == 23790) {
			 * player.getSkills().addXp(Skills.SMITHING, 250);
			 * player.getInventory().deleteItem(23790, 1); } else if (itemId == 23794) {
			 * player.getSkills().addXp(Skills.FISHING, 250);
			 * player.getInventory().deleteItem(23794, 1); } else if (itemId == 23798) {
			 * player.getSkills().addXp(Skills.COOKING, 250);
			 * player.getInventory().deleteItem(23798, 1); } else if (itemId == 23802) {
			 * player.getSkills().addXp(Skills.FIREMAKING, 250);
			 * player.getInventory().deleteItem(23802, 1); } else if (itemId == 23806) {
			 * player.getSkills().addXp(Skills.WOODCUTTING, 250);
			 * player.getInventory().deleteItem(23806, 1); } else if (itemId == 23810) {
			 * player.getSkills().addXp(Skills.FARMING, 250);
			 * player.getInventory().deleteItem(23810, 1); } else if (itemId == 23814) {
			 * player.getSkills().addXp(Skills.SUMMONING, 250);
			 * player.getInventory().deleteItem(23814, 1); } else if (itemId == 23718) {
			 * player.getSkills().addXp(Skills.ATTACK, 500);
			 * player.getInventory().deleteItem(23718, 1); } else if (itemId == 23722) {
			 * player.getSkills().addXp(Skills.STRENGTH, 500);
			 * player.getInventory().deleteItem(23722, 1); } else if (itemId == 23726) {
			 * player.getSkills().addXp(Skills.DEFENCE, 500);
			 * player.getInventory().deleteItem(23726, 1); } else if (itemId == 23730) {
			 * player.getSkills().addXp(Skills.RANGE, 500);
			 * player.getInventory().deleteItem(23730, 1); } else if (itemId == 23734) {
			 * player.getSkills().addXp(Skills.MAGIC, 500);
			 * player.getInventory().deleteItem(23734, 1); } else if (itemId == 23738) {
			 * player.getSkills().addXp(Skills.PRAYER, 500);
			 * player.getInventory().deleteItem(23738, 1); } else if (itemId == 23742) {
			 * player.getSkills().addXp(Skills.RUNECRAFTING, 500);
			 * player.getInventory().deleteItem(23742, 1); } else if (itemId == 23746) {
			 * player.getSkills().addXp(Skills.CONSTRUCTION, 500);
			 * player.getInventory().deleteItem(23746, 1); } else if (itemId == 23750) {
			 * player.getSkills().addXp(Skills.DUNGEONEERING, 500);
			 * player.getInventory().deleteItem(23750, 1); } else if (itemId == 23754) {
			 * player.getSkills().addXp(Skills.HITPOINTS, 500);
			 * player.getInventory().deleteItem(23754, 1); } else if (itemId == 23758) {
			 * player.getSkills().addXp(Skills.AGILITY, 500);
			 * player.getInventory().deleteItem(23758, 1); } else if (itemId == 23762) {
			 * player.getSkills().addXp(Skills.HERBLORE, 500);
			 * player.getInventory().deleteItem(23762, 1); } else if (itemId == 23766) {
			 * player.getSkills().addXp(Skills.THIEVING, 500);
			 * player.getInventory().deleteItem(23766, 1); } else if (itemId == 23770) {
			 * player.getSkills().addXp(Skills.CRAFTING, 500);
			 * player.getInventory().deleteItem(23770, 1); } else if (itemId == 23775) {
			 * player.getSkills().addXp(Skills.FLETCHING, 500);
			 * player.getInventory().deleteItem(23775, 1); } else if (itemId == 23779) {
			 * player.getSkills().addXp(Skills.SLAYER, 500);
			 * player.getInventory().deleteItem(23779, 1); } else if (itemId == 23783) {
			 * player.getSkills().addXp(Skills.HUNTER, 500);
			 * player.getInventory().deleteItem(23783, 1); } else if (itemId == 23787) {
			 * player.getSkills().addXp(Skills.MINING, 500);
			 * player.getInventory().deleteItem(23787, 1); } else if (itemId == 23791) {
			 * player.getSkills().addXp(Skills.SMITHING, 500);
			 * player.getInventory().deleteItem(23791, 1); } else if (itemId == 23795) {
			 * player.getSkills().addXp(Skills.FISHING, 500);
			 * player.getInventory().deleteItem(23795, 1); } else if (itemId == 23799) {
			 * player.getSkills().addXp(Skills.COOKING, 500);
			 * player.getInventory().deleteItem(23799, 1); } else if (itemId == 23803) {
			 * player.getSkills().addXp(Skills.FIREMAKING, 500);
			 * player.getInventory().deleteItem(23803, 1); } else if (itemId == 23807) {
			 * player.getSkills().addXp(Skills.WOODCUTTING, 500);
			 * player.getInventory().deleteItem(23807, 1); } else if (itemId == 23811) {
			 * player.getSkills().addXp(Skills.FARMING, 500);
			 * player.getInventory().deleteItem(23811, 1); } else if (itemId == 23815) {
			 * player.getSkills().addXp(Skills.SUMMONING, 500);
			 * player.getInventory().deleteItem(23815, 1); } else if (itemId == 23719) {
			 * player.getSkills().addXp(Skills.ATTACK, 750);
			 * player.getInventory().deleteItem(23719, 1); } else if (itemId == 23723) {
			 * player.getSkills().addXp(Skills.STRENGTH, 750);
			 * player.getInventory().deleteItem(23723, 1); } else if (itemId == 23727) {
			 * player.getSkills().addXp(Skills.DEFENCE, 750);
			 * player.getInventory().deleteItem(23727, 1); } else if (itemId == 23731) {
			 * player.getSkills().addXp(Skills.RANGE, 750);
			 * player.getInventory().deleteItem(23731, 1); } else if (itemId == 23735) {
			 * player.getSkills().addXp(Skills.MAGIC, 750);
			 * player.getInventory().deleteItem(23735, 1); } else if (itemId == 23739) {
			 * player.getSkills().addXp(Skills.PRAYER, 750);
			 * player.getInventory().deleteItem(23739, 1); } else if (itemId == 23743) {
			 * player.getSkills().addXp(Skills.RUNECRAFTING, 750);
			 * player.getInventory().deleteItem(23743, 1); } else if (itemId == 23747) {
			 * player.getSkills().addXp(Skills.CONSTRUCTION, 750);
			 * player.getInventory().deleteItem(23747, 1); } else if (itemId == 23751) {
			 * player.getSkills().addXp(Skills.DUNGEONEERING, 750);
			 * player.getInventory().deleteItem(23751, 1); } else if (itemId == 23755) {
			 * player.getSkills().addXp(Skills.HITPOINTS, 750);
			 * player.getInventory().deleteItem(23755, 1); } else if (itemId == 23759) {
			 * player.getSkills().addXp(Skills.AGILITY, 750);
			 * player.getInventory().deleteItem(23759, 1); } else if (itemId == 23763) {
			 * player.getSkills().addXp(Skills.HERBLORE, 750);
			 * player.getInventory().deleteItem(23763, 1); } else if (itemId == 23767) {
			 * player.getSkills().addXp(Skills.THIEVING, 750);
			 * player.getInventory().deleteItem(23767, 1); } else if (itemId == 23771) {
			 * player.getSkills().addXp(Skills.CRAFTING, 750);
			 * player.getInventory().deleteItem(23771, 1); } else if (itemId == 23776) {
			 * player.getSkills().addXp(Skills.FLETCHING, 750);
			 * player.getInventory().deleteItem(23776, 1); } else if (itemId == 23780) {
			 * player.getSkills().addXp(Skills.SLAYER, 750);
			 * player.getInventory().deleteItem(23780, 1); } else if (itemId == 23784) {
			 * player.getSkills().addXp(Skills.HUNTER, 750);
			 * player.getInventory().deleteItem(23784, 1); } else if (itemId == 23788) {
			 * player.getSkills().addXp(Skills.MINING, 750);
			 * player.getInventory().deleteItem(23788, 1); } else if (itemId == 23792) {
			 * player.getSkills().addXp(Skills.SMITHING, 750);
			 * player.getInventory().deleteItem(23792, 1); } else if (itemId == 23796) {
			 * player.getSkills().addXp(Skills.FISHING, 750);
			 * player.getInventory().deleteItem(23796, 1); } else if (itemId == 23800) {
			 * player.getSkills().addXp(Skills.COOKING, 750);
			 * player.getInventory().deleteItem(23800, 1); } else if (itemId == 23804) {
			 * player.getSkills().addXp(Skills.FIREMAKING, 750);
			 * player.getInventory().deleteItem(23804, 1); } else if (itemId == 23808) {
			 * player.getSkills().addXp(Skills.WOODCUTTING, 750);
			 * player.getInventory().deleteItem(23808, 1); } else if (itemId == 23812) {
			 * player.getSkills().addXp(Skills.FARMING, 750);
			 * player.getInventory().deleteItem(23812, 1); } else if (itemId == 23816) {
			 * player.getSkills().addXp(Skills.SUMMONING, 750);
			 * player.getInventory().deleteItem(23816, 1); } else if (itemId == 23720) {
			 * player.getSkills().addXp(Skills.ATTACK, 1000);
			 * player.getInventory().deleteItem(23720, 1); } else if (itemId == 23724) {
			 * player.getSkills().addXp(Skills.STRENGTH, 1000);
			 * player.getInventory().deleteItem(23724, 1); } else if (itemId == 23728) {
			 * player.getSkills().addXp(Skills.DEFENCE, 1000);
			 * player.getInventory().deleteItem(23728, 1); } else if (itemId == 23732) {
			 * player.getSkills().addXp(Skills.RANGE, 1000);
			 * player.getInventory().deleteItem(23732, 1); } else if (itemId == 23736) {
			 * player.getSkills().addXp(Skills.MAGIC, 1000);
			 * player.getInventory().deleteItem(23736, 1); } else if (itemId == 23740) {
			 * player.getSkills().addXp(Skills.PRAYER, 1000);
			 * player.getInventory().deleteItem(23740, 1); } else if (itemId == 23744) {
			 * player.getSkills().addXp(Skills.RUNECRAFTING, 1000);
			 * player.getInventory().deleteItem(23744, 1); } else if (itemId == 23748) {
			 * player.getSkills().addXp(Skills.CONSTRUCTION, 1000);
			 * player.getInventory().deleteItem(23748, 1); } else if (itemId == 23752) {
			 * player.getSkills().addXp(Skills.DUNGEONEERING, 1000);
			 * player.getInventory().deleteItem(23752, 1); } else if (itemId == 23756) {
			 * player.getSkills().addXp(Skills.HITPOINTS, 1000);
			 * player.getInventory().deleteItem(23756, 1); } else if (itemId == 23760) {
			 * player.getSkills().addXp(Skills.AGILITY, 1000);
			 * player.getInventory().deleteItem(23760, 1); } else if (itemId == 23764) {
			 * player.getSkills().addXp(Skills.HERBLORE, 1000);
			 * player.getInventory().deleteItem(23764, 1); } else if (itemId == 23768) {
			 * player.getSkills().addXp(Skills.THIEVING, 1000);
			 * player.getInventory().deleteItem(23768, 1); } else if (itemId == 23772) {
			 * player.getSkills().addXp(Skills.CRAFTING, 1000);
			 * player.getInventory().deleteItem(23772, 1); } else if (itemId == 23777) {
			 * player.getSkills().addXp(Skills.FLETCHING, 1000);
			 * player.getInventory().deleteItem(23777, 1); } else if (itemId == 23781) {
			 * player.getSkills().addXp(Skills.SLAYER, 1000);
			 * player.getInventory().deleteItem(23781, 1); } else if (itemId == 23785) {
			 * player.getSkills().addXp(Skills.HUNTER, 1000);
			 * player.getInventory().deleteItem(23785, 1); } else if (itemId == 23789) {
			 * player.getSkills().addXp(Skills.MINING, 1000);
			 * player.getInventory().deleteItem(23789, 1); } else if (itemId == 23793) {
			 * player.getSkills().addXp(Skills.SMITHING, 1000);
			 * player.getInventory().deleteItem(23793, 1); } else if (itemId == 23797) {
			 * player.getSkills().addXp(Skills.FISHING, 1000);
			 * player.getInventory().deleteItem(23797, 1); } else if (itemId == 23801) {
			 * player.getSkills().addXp(Skills.COOKING, 1000);
			 * player.getInventory().deleteItem(23801, 1); } else if (itemId == 23805) {
			 * player.getSkills().addXp(Skills.FIREMAKING, 1000);
			 * player.getInventory().deleteItem(23805, 1); } else if (itemId == 23809) {
			 * player.getSkills().addXp(Skills.WOODCUTTING, 1000);
			 * player.getInventory().deleteItem(23809, 1); } else if (itemId == 23813) {
			 * player.getSkills().addXp(Skills.FARMING, 1000);
			 * player.getInventory().deleteItem(23813, 1); } else if (itemId == 23817) {
			 * player.getSkills().addXp(Skills.SUMMONING, 1000);
			 * player.getInventory().deleteItem(23817, 1); } else if (itemId == 24300) {
			 * player.getSkills().addXp(Skills.ATTACK, 200000);
			 * player.getSkills().addXp(Skills.STRENGTH, 200000);
			 * player.getSkills().addXp(Skills.DEFENCE, 200000);
			 * player.getSkills().addXp(Skills.RANGE, 200000);
			 * player.getSkills().addXp(Skills.MAGIC, 200000);
			 * player.getSkills().addXp(Skills.PRAYER, 200000);
			 * player.getSkills().addXp(Skills.RUNECRAFTING, 200000);
			 * player.getSkills().addXp(Skills.CONSTRUCTION, 200000);
			 * player.getSkills().addXp(Skills.DUNGEONEERING, 200000);
			 * player.getSkills().addXp(Skills.HITPOINTS, 200000);
			 * player.getSkills().addXp(Skills.AGILITY, 200000);
			 * player.getSkills().addXp(Skills.HERBLORE, 200000);
			 * player.getSkills().addXp(Skills.THIEVING, 200000);
			 * player.getSkills().addXp(Skills.CRAFTING, 200000);
			 * player.getSkills().addXp(Skills.FLETCHING, 200000);
			 * player.getSkills().addXp(Skills.SLAYER, 200000);
			 * player.getSkills().addXp(Skills.HUNTER, 200000);
			 * player.getSkills().addXp(Skills.MINING, 200000);
			 * player.getSkills().addXp(Skills.SMITHING, 200000);
			 * player.getSkills().addXp(Skills.FISHING, 200000);
			 * player.getSkills().addXp(Skills.COOKING, 200000);
			 * player.getSkills().addXp(Skills.FIREMAKING, 200000);
			 * player.getSkills().addXp(Skills.WOODCUTTING, 200000);
			 * player.getSkills().addXp(Skills.FARMING, 200000);
			 * player.getSkills().addXp(Skills.SUMMONING, 200000);
			 * player.getInventory().deleteItem(24300, 1);
			 */
		} else if (itemId == HunterEquipment.BOX.getId()) {// almost done
			player.getActionManager().setAction(new BoxAction(HunterEquipment.BOX));
		} else if (itemId == HunterEquipment.BRID_SNARE.getId()) {
			player.getActionManager().setAction(new BoxAction(HunterEquipment.BRID_SNARE));
		} else if (item.getDefinitions().getName().startsWith("Master Thieving Cape")
				|| item.getDefinitions().getName().startsWith("Master_Thieving_Cape")) {
			if (player.getSkills().getXp(Skills.AGILITY) < 104273167) {
				player.getPackets()
						.sendGameMessage("To wear this cape you need 104,273,167 experience in the following: ");
				player.getPackets().sendGameMessage("Agility");
				return;
			}
		} else if (Lamps.isSelectable(itemId) || Lamps.isSkillLamp(itemId)) {
			Lamps.processLampClick(player, slotId, itemId);
		} else if (itemId == 1856) {// Information Book
			player.getInterfaceManager().sendHelpBook();
		} else if (item.getDefinitions().getName().startsWith("Burnt")) {
			player.getDialogueManager().startDialogue("SimplePlayerMessage", "Ugh, this is inedible.");
		}

	}

	/*
	 * returns the other
	 */
	public static Item contains(int id1, Item item1, Item item2) {
		if (item1.getId() == id1) {
			return item2;
		}
		if (item2.getId() == id1) {
			return item1;
		}
		return null;
	}

	public static boolean contains(int id1, int id2, Item... items) {
		boolean containsId1 = false;
		boolean containsId2 = false;
		for (Item item : items) {
			if (item.getId() == id1) {
				containsId1 = true;
			} else if (item.getId() == id2) {
				containsId2 = true;
			}
		}
		return containsId1 && containsId2;
	}

	public static void handleItemOnItem(final Player player, InputStream stream) {
		/*
		 * int itemUsedWithId = stream.readShort(); int toSlot =
		 * stream.readShortLE128(); int interfaceId = stream.readInt() >> 16; int
		 * interfaceId2 = stream.readInt() >> 16; int fromSlot = stream.readShort(); int
		 * itemUsedId = stream.readShortLE128();
		 */
		int itemUsedWithId = stream.readInt();
		int toSlot = stream.readShortLE128();
		int hash1 = stream.readInt();
		int hash2 = stream.readInt();
		int interfaceId = hash1 >> 16;
		int interfaceId2 = hash2 >> 16;
		int comp1 = hash1 & 0xFFFF;
		int fromSlot = stream.readShort();
		int itemUsedId = stream.readInt();
		if (interfaceId == 192 && interfaceId2 == 679) {
			Item item = player.getInventory().getItem(toSlot);
			if (item == null) {
				return;
			}
			switch (comp1) {
			case 59:
				Alchemy.handleAlchemy(player, item, false);
				break;
			case 38:
				Alchemy.handleAlchemy(player, item, true);
				break;
			case 50:
				Alchemy.handleSuperheat(player, item);
				break;
			case 29:
			case 41:
			case 53:
			case 61:
			case 76:
			case 88:
				Enchanting.processMagicEnchantSpell(player, toSlot, Enchanting.getJewleryIndex(comp1));
			default:
				if (player.getRights() == 2) {
					// player.getPackets().sendGameMessage("Unhandled spell:
					// component1: "+comp1+" slotId: "+toSlot);
				}
				break;
			}
			return;
		}
		if (interfaceId == 430 && interfaceId2 == 679) {
			Item item = player.getInventory().getItem(toSlot);
			if (item == null) {
				return;
			}
			switch (comp1) {
			case 33:
				Lunars.handlePlankMake(player, item);
				break;
			case 50:
				Lunars.handleRestorePotionShare(player, item);
				break;
			case 72:
				Lunars.handleLeatherMake(player, item);
				break;
			case 49:
				Lunars.handleBoostPotionShare(player, item);
				break;
			default:
				if (player.getRights() == 2) {
					// player.getPackets().sendGameMessage("Unhandled lunar
					// spell: component1: "+comp1+" slotId: "+toSlot);
				}
				break;
			}
			return;
		}
		System.out.println("Interface: " + interfaceId + ", Interface2: " + interfaceId2);
		if ((interfaceId2 == 662 || interfaceId2 == 679) && interfaceId == 747) {
			System.out.println("toSlot: " + toSlot);
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn()) {
						player.getFamiliar().submitSpecial(toSlot);
						System.out.println("Sumbmitted");
					}
				}
			}
			return;
		}
		if (interfaceId == Inventory.INVENTORY_INTERFACE && interfaceId == interfaceId2
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28) {
				return;
			}
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null || itemUsed.getId() != itemUsedId
					|| usedWith.getId() != itemUsedWithId) {
				return;
			}
			player.stopAll();
			if (!player.getControlerManager().canUseItemOnItem(itemUsed, usedWith)) {
				return;
			}
			Fletch fletch = Fletching.isFletching(usedWith, itemUsed);
			if (fletch != null) {
				player.getDialogueManager().startDialogue("FletchingD", fletch);
				return;
			} else if (Pots.mixPot(player, itemUsed, usedWith, fromSlot, toSlot, true) != -1)
				return;
			if (itemUsed.getId() == 985 && usedWith.getId() == 987
					|| itemUsed.getId() == 987 && usedWith.getId() == 985) {
				if (player.getInventory().containsItem(985, 1) && player.getInventory().containsItem(987, 1)) {
					player.getInventory().deleteItem(985, 1);
					player.getInventory().deleteItem(987, 1);
					player.getInventory().addItem(989, 1);
					player.sendMessage("You succesfully make a crytal key.");
				} else {
					player.sendMessage("You need both parts to make this key.");
				}
				return;
			}
			
			//Arclight charging
			if ((itemUsed.getId() == 29955 && usedWith.getId() == 29965) && player.getArclight().charge(3)) {
				return;
			}
			
			//Dyeing items
			if (DyeHandler.usingDye(player, itemUsed, usedWith))
				return;
			
			//Dragonfire ward
			if (itemUsed.getId() == 29532 && usedWith.getId() == 1540) {
				if (player.getInventory().contains(new int[] { 29532, 1540 })) {
					if (player.getSkills().getLevel(Skills.SMITHING) < 90) {
						player.sendMessage("You need at least level 90 in Smithing to create that.");
						return;
					}
					player.getInventory().deleteItem(29532, 1);
					player.getInventory().deleteItem(1540, 1);
					player.getSkills().addXp(Skills.SMITHING, 2_000);
					player.getInventory().addItem(29736, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the Skeletal visage with your Anti-dragon shield, and create a Dragonfire ward.");
				}
			}
			
			//Primordial boots
			if (itemUsed.getId() == 29734 && usedWith.getId() == 11732) {
				if (player.getSkills().getLevel(Skills.MAGIC) < 60 || player.getSkills().getLevel(Skills.RUNECRAFTING) < 60) {
					player.sendMessage("You need at least level 60 in both Runecrafting and Magic to create these.");
					return;
				}
				if (player.getInventory().contains(new int[] { 29734, 11732 })) {
					player.getInventory().deleteItem(29734, 1);
					player.getInventory().deleteItem(11732, 1);
					player.getInventory().addItem(29726, 1);
					player.getSkills().addXp(Skills.MAGIC, 200);
					player.getSkills().addXp(Skills.RUNECRAFTING, 200);
					player.sendMessage("You upgrade your Dragon boots into Primoridal boots.");
				}
			}
			
			//Eternal boots
			if (itemUsed.getId() == 6920 && usedWith.getId() == 29732) {
				if (player.getSkills().getLevel(Skills.MAGIC) < 60 || player.getSkills().getLevel(Skills.RUNECRAFTING) < 60) {
					player.sendMessage("You need at least level 60 in both Runecrafting and Magic to create these.");
					return;
				}
				if (player.getInventory().contains(new int[] { 29732, 6920 })) {
					player.getInventory().deleteItem(29732, 1);
					player.getInventory().deleteItem(6920, 1);
					player.getInventory().addItem(29722, 1);
					player.getSkills().addXp(Skills.MAGIC, 200);
					player.getSkills().addXp(Skills.RUNECRAFTING, 200);
					player.sendMessage("You upgrade your infinity boots into Eternal boots.");
				}
			}
			
			//Pegasian boots
			if (itemUsed.getId() == 2577 && usedWith.getId() == 29730) {
				if (player.getSkills().getLevel(Skills.MAGIC) < 60 || player.getSkills().getLevel(Skills.RUNECRAFTING) < 60) {
					player.sendMessage("You need at least level 60 in both Runecrafting and Magic to create these.");
					return;
				}
				if (player.getInventory().contains(new int[] { 29730, 2577 })) {
					player.getInventory().deleteItem(29730, 1);
					player.getInventory().deleteItem(2577, 1);
					player.getInventory().addItem(29724, 1);
					player.getSkills().addXp(Skills.MAGIC, 200);
					player.getSkills().addXp(Skills.RUNECRAFTING, 200);
					player.sendMessage("You upgrade your ranger boots into Pegasian boots.");
				}
			}
			
			//Creating incomplete light ballista
			if (itemUsed.getId() == 29651 && usedWith.getId() == 29645) {	
				if (player.getSkills().getLevel(Skills.FLETCHING) < 47) {
					player.sendMessage("You don't have the required Fletching level to make that.");
					return;
				}
				if (player.getInventory().contains(itemUsed, usedWith)) {
					player.getInventory().deleteItem(29645, 1);
					player.getInventory().deleteItem(29651, 1);
					player.getInventory().addItem(29578, 1);	
					player.getSkills().addXp(Skills.FLETCHING, 15);
					player.getPackets().sendGameMessage("You attatch the ballista limbs to your light frame.", true);
					return;
				}
			}
			
			//Creating unstrung heavy ballista
			if (itemUsed.getId() == 29649 && usedWith.getId() == 29578) {
				if (player.getSkills().getLevel(Skills.FLETCHING) < 47) {
					player.sendMessage("You don't have the required Fletching level to make that.");
					return;
				}
				if (player.getInventory().contains(itemUsed, usedWith)) {
					player.getInventory().deleteItem(29578, 1);
					player.getInventory().deleteItem(29649, 1);
					player.getInventory().addItem(29576, 1);	
					player.getSkills().addXp(Skills.FLETCHING, 15);
					player.getPackets().sendGameMessage("You attatch the ballista spring to your incomplete light ballista.", true);
					return;
				}
			}
			
			//Creating light ballista
			if (itemUsed.getId() == 29643 && usedWith.getId() == 29577) {
				if (player.getSkills().getLevel(Skills.FLETCHING) < 47) {
					player.sendMessage("You don't have the required Fletching level to make that.");
					return;
				}
				if (player.getInventory().contains(itemUsed, usedWith)) {
					player.getInventory().deleteItem(29576, 1);
					player.getInventory().deleteItem(29643, 1);
					player.getInventory().addItem(29631, 1);	
					player.getSkills().addXp(Skills.FLETCHING, 15);
					player.getPackets().sendGameMessage("You attatch the monkey tail to your unstrung light ballista.", true);
					return;
				}
			}
			
			//Creating incomplete heavy ballista
			if (itemUsed.getId() == 29651 && usedWith.getId() == 29647) {	
				if (player.getSkills().getLevel(Skills.FLETCHING) < 72) {
					player.sendMessage("You don't have the required Fletching level to make that.");
					return;
				}
				if (player.getInventory().contains(itemUsed, usedWith)) {
					player.getInventory().deleteItem(29647, 1);
					player.getInventory().deleteItem(29651, 1);
					player.getInventory().addItem(29579, 1);	
					player.getSkills().addXp(Skills.FLETCHING, 30);
					player.getPackets().sendGameMessage("You attatch the ballista limbs to your heavy frame.", true);
					return;
				}
			}	
			
			//Creating unstrung heavy ballista
			if (itemUsed.getId() == 29649 && usedWith.getId() == 29579) {
				if (player.getSkills().getLevel(Skills.FLETCHING) < 72) {
					player.sendMessage("You don't have the required Fletching level to make that.");
					return;
				}
				if (player.getInventory().contains(itemUsed, usedWith)) {
					player.getInventory().deleteItem(29579, 1);
					player.getInventory().deleteItem(29649, 1);
					player.getInventory().addItem(29577, 1);	
					player.getSkills().addXp(Skills.FLETCHING, 30);
					player.getPackets().sendGameMessage("You attatch the ballista spring to your incomplete heavy ballista.", true);
					return;
				}
			}
			
			//Creating heavy ballista
			if (itemUsed.getId() == 29643 && usedWith.getId() == 29577) {
				if (player.getSkills().getLevel(Skills.FLETCHING) < 72) {
					player.sendMessage("You don't have the required Fletching level to make that.");
					return;
				}
				if (player.getInventory().contains(itemUsed, usedWith)) {
					player.getInventory().deleteItem(29577, 1);
					player.getInventory().deleteItem(29643, 1);
					player.getInventory().addItem(29633, 1);	
					player.getSkills().addXp(Skills.FLETCHING, 30);
					player.getPackets().sendGameMessage("You attatch the monkey tail to your unstrung heavy ballista.", true);
					return;
				}
			}
			
			//Dark totem
			if (itemUsed.getId() == 29621 && (usedWith.getId() == 29620 || usedWith.getId() == 29619)) {
				if (player.getInventory().contains(new int[] {29621, 29620, 29619})) {
					player.getInventory().deleteItem(29619, 1);
					player.getInventory().deleteItem(29620, 1);
					player.getInventory().deleteItem(29621, 1);
					player.getInventory().addItemDrop(29618, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You bring the totem pieces together with a click.", "It looks kind of like an arcane key and pulses with dark power in your hand.");
					return;
				}
			}
			if (itemUsed.getId() == 52973 && (usedWith.getId() == 52971 || usedWith.getId() == 52969)) {
				if (player.getInventory().contains(new int[] {52973, 52971, 52969})) {
					player.getInventory().deleteItem(52973, 1);
					player.getInventory().deleteItem(52971, 1);
					player.getInventory().deleteItem(52969, 1);
					player.getInventory().addItemDrop(52975, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the hydra pieces together.", "creating a brimstone ring.");
					return;
				}
			}
			if (itemUsed.getId() == 52966 && (usedWith.getId() == 11716)) {
				if (player.getInventory().contains(new int[] {52966, 11716})) {
					player.getInventory().deleteItem(52966, 1);
					player.getInventory().deleteItem(11716, 1);
					player.getInventory().addItemDrop(52978, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine claw and spear.", "creating a dragon hunter lance.");
					return;
				}
			}
			if (itemUsed.getId() == 52983 && (usedWith.getId() == 7462)) {
				if (player.getInventory().contains(new int[] {52983, 7462})) {
					player.getInventory().deleteItem(52983, 1);
					player.getInventory().deleteItem(7462, 1);
					player.getInventory().addItemDrop(28749, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the gloves and hydra leather.", "creating a ferocious gloves.");
					return;
				}
			}
			//hween dye start

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 29491)) {
				if (player.getInventory().contains(new int[] {28717, 29491})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(29491, 1);
					player.getInventory().addItemDrop(28723, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and ancestral hat.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 29493)) {
				if (player.getInventory().contains(new int[] {28717, 29493})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(29493, 1);
					player.getInventory().addItemDrop(28721, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and ancestral robe top.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 29489)) {
				if (player.getInventory().contains(new int[] {28717, 29489})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(29489, 1);
					player.getInventory().addItemDrop(28719, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and ancestral robe bottom.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 29756)) {
				if (player.getInventory().contains(new int[] {28717, 29756})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(29756, 1);
					player.getInventory().addItemDrop(28729, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and justiciar helm.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 29758)) {
				if (player.getInventory().contains(new int[] {28717, 29758})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(29758, 1);
					player.getInventory().addItemDrop(28727, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and justiciar platebody.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 29760)) {
				if (player.getInventory().contains(new int[] {28717, 29760})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(29760, 1);
					player.getInventory().addItemDrop(28725, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and justiciar platelegs.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 4151)) {
				if (player.getInventory().contains(new int[] {28717, 4151})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(4151, 1);
					player.getInventory().addItemDrop(28715, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and abyssal whip.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 29943)) {
				if (player.getInventory().contains(new int[] {28717, 29943})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(29943, 1);
					player.getInventory().addItemDrop(28737, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and twisted bow.", "creating a h'ween dyed item.");
					return;
				}
			}

			if (itemUsed.getId() == 28717 && (usedWith.getId() == 52486)) {
				if (player.getInventory().contains(new int[] {28717, 52486})) {
					player.getInventory().deleteItem(28717, 1);
					player.getInventory().deleteItem(52486, 1);
					player.getInventory().addItemDrop(28735, 1);
					player.getDialogueManager().startDialogue("SimpleMessage", "You combine the dye and scythe of vitur.", "creating a h'ween dyed item.");
					return;
				}
			}







			int leatherIndex = LeatherCraftingD.getIndex(itemUsedId) == -1 ? LeatherCraftingD.getIndex(usedWith.getId())
					: LeatherCraftingD.getIndex(itemUsedId);
			if (leatherIndex != -1 && (itemUsedId == 1733 || usedWith.getId() == 1733
					|| LeatherCraftingD.isExtraItem(usedWith.getId()) || LeatherCraftingD.isExtraItem(itemUsedId))) {
				player.getDialogueManager().startDialogue("LeatherCraftingD", leatherIndex);
				return;
			}
			if (itemUsed.getId() == 187 || itemUsed.getId() == 5937 || itemUsed.getId() == 5940
					|| usedWith.getId() == 187 || usedWith.getId() == 5937 || usedWith.getId() == 5940) {
				WeaponPoison.handleItemInteract(player, itemUsed, usedWith);
			}
			if (itemUsed.getId() >= 1617 && itemUsed.getId() <= 1624 && usedWith.getId() == 18338) {
				if (player.gembagspace < 100) {
					if (itemUsed.getId() == 1617) {
						player.diamonds++;
						player.getInventory().deleteItem(1617, 1);
					} else if (itemUsed.getId() == 1619) {
						player.rubies++;
						player.getInventory().deleteItem(1619, 1);
					} else if (itemUsed.getId() == 1621) {
						player.emeralds++;
						player.getInventory().deleteItem(1621, 1);
					} else if (itemUsed.getId() == 1623) {
						player.sapphires++;
						player.getInventory().deleteItem(1623, 1);
					}
					player.gembagspace++;
				} else {
					player.sm("Your gem bag is too full to carry anymore uncut gems.");
				}
			}
			Amulets amulet = Amulets.forId(usedWith.getId());
			if (amulet != null) {
				player.getActionManager().setAction(new AmuletStringing(amulet));
				return;
			}
			if (itemUsed.getId() == 453 && usedWith.getId() == 18339) {
				if (player.coal < 27) {
					player.getInventory().deleteItem(453, 1);
					player.coal++;
					player.sm("You add a piece of coal to your coal bag.");
				} else {
					player.sm("Your coal bag is too full to carry anymore coal.");
				}
			}

			// Amulet of souls.
			if (usedWith.getId() == 29118 || usedWith.getId() == 29102 && itemUsed.getId() == 6573) {
				player.getInventory().deleteItem(itemUsed.getId(), 1);
				usedWith.setId(29100);
				player.getInventory().refresh();
				player.sendMessage("You use the Onyx on your amulet of souls, and charge it to full.");
			}

			// Ring of death
			if (usedWith.getId() == 29117 || usedWith.getId() == 29099 && itemUsed.getId() == 6573) {
				player.getInventory().deleteItem(itemUsed.getId(), 1);
				usedWith.setId(29097);
				player.getInventory().refresh();
				player.sendMessage("You use the Onyx on your ring of death, and charge it to full.");
			}

			// Reaper necklace
			if (usedWith.getId() == 29119 || usedWith.getId() == 29105 && itemUsed.getId() == 6573) {
				player.getInventory().deleteItem(itemUsed.getId(), 1);
				usedWith.setId(29103);
				player.getInventory().refresh();
				player.sendMessage("You use the Onyx on your ring of death, and charge it to full.");
			}

			// Book of death
			if (usedWith.getId() == 29803 && itemUsed.getId() == 29802) {
				player.getInventory().deleteItem(itemUsed.getId(), itemUsed.getAmount());
				player.death_notes += itemUsed.getAmount() * 125;
				player.sendMessage("Your book of death now contains " + player.death_notes + " charges.");
			}
			if (itemUsedId == 29794 || itemUsedWithId == 29795 || itemUsedWithId == 29796) {
				if (player.getInventory().containsItem(29794, 1) && player.getInventory().containsItem(29795, 1)
						&& player.getInventory().containsItem(29796, 1)) {
					player.getInventory().deleteItem(29794, 1);
					player.getInventory().deleteItem(29795, 1);
					player.getInventory().deleteItem(29796, 1);
					player.getInventory().addItem(29797, 1);
					// World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+"
					// just assembled a spider leg", false);
					return;
				}
			}
			if (itemUsedId == 51820 || itemUsedWithId == 29250) {
				if (player.getInventory().containsItem(29250, 1) && player.getInventory().containsItem(51820, 1000)) {
					player.getInventory().deleteItem(29250, 1);
					player.getInventory().deleteItem(51820, 1000);
					player.getInventory().addItem(29258, 1);
					player.sm("You add 1000 charges to your bracelet of ethereum.");
					return;

				}
			}
			//odium ward
			if (itemUsedId == 41928 || itemUsedWithId == 41929 || itemUsedWithId == 41930) {
				if (player.getInventory().containsItem(41928, 1) && player.getInventory().containsItem(41929, 1)
						&& player.getInventory().containsItem(41930, 1)) {
					player.getInventory().deleteItem(41928, 1);
					player.getInventory().deleteItem(41929, 1);
					player.getInventory().deleteItem(41930, 1);
					player.getInventory().addItem(41926, 1);
					player.sm("You attach the shards together to create a odium ward.");
					return;
				}
			}
			//malediction ward
			if (itemUsedId == 41931 || itemUsedWithId == 41932 || itemUsedWithId == 41933) {
				if (player.getInventory().containsItem(41931, 1) && player.getInventory().containsItem(41932, 1)
						&& player.getInventory().containsItem(41933, 1)) {
					player.getInventory().deleteItem(41931, 1);
					player.getInventory().deleteItem(41932, 1);
					player.getInventory().deleteItem(41933, 1);
					player.getInventory().addItem(41924, 1);
					player.sm("You attach the shards together to create a malediction ward.");
					return;
				}
			}
			//cursed ward
			if (itemUsedId == 29513 || itemUsedWithId == 29511 || itemUsedWithId == 29509) {
				if (player.getInventory().containsItem(29513, 1) && player.getInventory().containsItem(29511, 1)
						&& player.getInventory().containsItem(29509, 1)) {
					player.getInventory().deleteItem(29513, 1);
					player.getInventory().deleteItem(29511, 1);
					player.getInventory().deleteItem(29509, 1);
					player.getInventory().addItem(29521, 1);
					player.sm("You attach the shards together to create a cursed ward.");
					return;
				}
			}

			if (itemUsedId == 28774 || itemUsedWithId == 20159) {
				if (player.getInventory().containsItem(28774, 1) && player.getInventory().containsItem(20159, 1)) {
					player.getInventory().deleteItem(28774, 1);
					player.getInventory().deleteItem(20159, 1);
					player.getInventory().addItem(28770, 1);
					player.sm("You attach the shards together to create a shark skin hat.");
					return;
				}
			}
			if (itemUsedId == 28774 || itemUsedWithId == 20163) {
				if (player.getInventory().containsItem(28774, 1) && player.getInventory().containsItem(20163, 1)) {
					player.getInventory().deleteItem(28774, 1);
					player.getInventory().deleteItem(20163, 1);
					player.getInventory().addItem(28772, 1);
					player.sm("You attach the shards together to create a shark skin body.");
					return;
				}
			}
			if (itemUsedId == 28774 || itemUsedWithId == 20167) {
				if (player.getInventory().containsItem(28774, 1) && player.getInventory().containsItem(20167, 1)) {
					player.getInventory().deleteItem(28774, 1);
					player.getInventory().deleteItem(20167, 1);
					player.getInventory().addItem(28768, 1);
					player.sm("You attach the shards together to create a shark skin robe.");
					return;
				}
			}
			//javelin
			if (itemUsedWithId == 28924 || itemUsedWithId == 28922) {
				if (player.getInventory().containsItem(28924, 1) && player.getInventory().containsItem(28922, 1)) {
					player.getInventory().deleteItem(28922, 1);
					player.getInventory().deleteItem(28924, 1);
					player.getInventory().addItem(28931, 1);
					player.sm("You attach the javelin pieces together to make a icicle javelin.");

				}
			}
			//ring of wealth (i)
			if (itemUsedWithId == 2572 || itemUsedWithId == 42783) {
				if (player.getInventory().containsItem(2572, 1) && player.getInventory().containsItem(42783, 1)) {
					player.getInventory().deleteItem(42783, 1);
					player.getInventory().deleteItem(2572, 1);
					player.getInventory().addItem(29463, 1);
					player.sm("You use the scroll on the ring of wealth to imbue it.");

				}
			}
			//ring of the gods (i)
			if (itemUsedWithId == 29958 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(29958, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(29958, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(29956, 1);
					player.sm("You use the scroll on the ring of the gods to imbue it.");

				}
			}
			//tyranical ring (i)
			if (itemUsedWithId == 29961 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(29961, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(29961, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(29959, 1);
					player.sm("You use the scroll on the tyranical ring to imbue it.");

				}
			}
			//treasonous ring (i)
			if (itemUsedWithId == 29964 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(29964, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(29964, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(29962, 1);
					player.sm("You use the scroll on the treasonous ring to imbue it.");

				}
			}
			//suffering (i)
			if (itemUsedWithId == 29157 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(29157, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(29157, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(29161, 1);
					player.sm("You use the scroll on the ring of suffering to imbue it.");

				}
			}
			//berserker ring (i)
			if (itemUsedWithId == 6737 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(6737, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(6737, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(15220, 1);
					player.sm("You use the scroll on the berseker ring to imbue it.");

				}
			}
			//warrior ring (i)
			if (itemUsedWithId == 6735 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(6735, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(6735, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(15020, 1);
					player.sm("You use the scroll on the warrior ring to imbue it.");

				}
			}
			if (itemUsedWithId == 6585 || itemUsedWithId == 54777) {
				if (player.getInventory().containsItem(6585, 1) && player.getInventory().containsItem(54777, 1)) {
					player.getInventory().deleteItem(6585, 1);
					player.getInventory().deleteItem(54777, 1);
					player.getInventory().addItem(28630, 1);
					player.sm("You attach the blood shard to the amulet of fury.");

				}
			}
			//archers ring (i)
			if (itemUsedWithId == 6733 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(6733, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(6733, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(15019, 1);
					player.sm("You use the scroll on the archer's ring to imbue it.");

				}
			}
			//seers ring (i)
			if (itemUsedWithId == 6731 || itemUsedWithId == 29106) {
				if (player.getInventory().containsItem(6731, 1) && player.getInventory().containsItem(29106, 1)) {
					player.getInventory().deleteItem(6731, 1);
					player.getInventory().deleteItem(29106, 1);
					player.getInventory().addItem(15018, 1);
					player.sm("You use the scroll on the seer's ring to imbue it.");

				}
			}
			if (itemUsedWithId == 29798 || itemUsedWithId == 29797) {
				if (player.getInventory().containsItem(29798, 1) && player.getInventory().containsItem(29797, 1)) {
					player.getInventory().deleteItem(29798, 1);
					player.getInventory().deleteItem(29797, 1);
					player.getInventory().addItem(29881, 1);

				}
			}
			if (itemUsedWithId == 29799 || itemUsedWithId == 29797) {
				if (player.getInventory().containsItem(29799, 1) && player.getInventory().containsItem(29797, 1)) {
					player.getInventory().deleteItem(29799, 1);
					player.getInventory().deleteItem(29797, 1);
					player.getInventory().addItem(29879, 1);
					// World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+"
					// just assembled a noxious staff", false);
				}
			}
			if (itemUsedWithId == 29800 || itemUsedWithId == 29797) {
				if (player.getInventory().containsItem(29800, 1) && player.getInventory().containsItem(29797, 1)) {
					player.getInventory().deleteItem(29800, 1);
					player.getInventory().deleteItem(29797, 1);
					player.getInventory().addItem(29877, 1);
					// World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+"
					// just strung a noxious longbow", false);
				}
			}
			if (itemUsedWithId == 29946 || itemUsedWithId == 4151) {
				if (player.getInventory().containsItem(29946, 1) && player.getInventory().containsItem(4151, 1)) {
					player.getInventory().deleteItem(29946, 1);
					player.getInventory().deleteItem(4151, 1);
					player.getInventory().addItem(29945, 1);
					// World.sendWorldMessage("<img=7><col=ff0000>News: "+player.getDisplayName()+"
					// just strung a noxious longbow", false);
				}
			}

			/**
			 * potions
			 */

			if (itemUsed.getId() == 143 && usedWith.getId() == 143) { // Prayer
				player.getInventory().deleteItem(143, 2);
				player.getInventory().addItem(141, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}

			if (itemUsed.getId() == 141 && usedWith.getId() == 143
					|| itemUsed.getId() == 143 && usedWith.getId() == 141) { // Prayer
				player.getInventory().deleteItem(143, 1);
				player.getInventory().deleteItem(141, 1);
				player.getInventory().addItem(139, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 141 && usedWith.getId() == 141) { // Prayer
				player.getInventory().deleteItem(141, 2);
				player.getInventory().addItem(2434, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 139 && usedWith.getId() == 143
					|| itemUsed.getId() == 143 && usedWith.getId() == 139) { // Prayer
				player.getInventory().deleteItem(139, 1);
				player.getInventory().deleteItem(143, 1);
				player.getInventory().addItem(2434, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			} // Prayer potions done
			if (itemUsed.getId() == 119 && usedWith.getId() == 119) { // Strength
				player.getInventory().deleteItem(119, 2);
				player.getInventory().addItem(117, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 119 && usedWith.getId() == 117
					|| itemUsed.getId() == 117 && usedWith.getId() == 119) {
				player.getInventory().deleteItem(119, 1);
				player.getInventory().deleteItem(117, 1);
				player.getInventory().addItem(115, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 117 && usedWith.getId() == 117) {
				player.getInventory().deleteItem(117, 2);
				player.getInventory().addItem(113, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 115 && usedWith.getId() == 119
					|| itemUsed.getId() == 119 && usedWith.getId() == 115) {
				player.getInventory().deleteItem(115, 1);
				player.getInventory().deleteItem(119, 1);
				player.getInventory().addItem(113, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			} // Strength done
			if (itemUsed.getId() == 161 && usedWith.getId() == 161) { // Super
				// strength
				player.getInventory().deleteItem(161, 2);
				player.getInventory().addItem(159, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 159 && usedWith.getId() == 159) { // Super
				// strength
				player.getInventory().deleteItem(159, 2);
				player.getInventory().addItem(2440, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 161 && usedWith.getId() == 159
					|| itemUsed.getId() == 159 && usedWith.getId() == 161) {
				player.getInventory().deleteItem(161, 1);
				player.getInventory().deleteItem(159, 1);
				player.getInventory().addItem(157, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 157 && usedWith.getId() == 161
					|| itemUsed.getId() == 161 && usedWith.getId() == 157) {
				player.getInventory().deleteItem(157, 1);
				player.getInventory().deleteItem(161, 1);
				player.getInventory().addItem(2440, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			} // Super strength done
			if (itemUsed.getId() == 149 && usedWith.getId() == 149) { // Super
				// Attack
				player.getInventory().deleteItem(149, 2);
				player.getInventory().addItem(147, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 147 && usedWith.getId() == 149
					|| itemUsed.getId() == 149 && usedWith.getId() == 147) {
				player.getInventory().deleteItem(147, 1);
				player.getInventory().deleteItem(149, 1);
				player.getInventory().addItem(145, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 147 && usedWith.getId() == 147) {
				player.getInventory().deleteItem(147, 2);
				player.getInventory().addItem(2436, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 145 && usedWith.getId() == 149
					|| itemUsed.getId() == 149 && usedWith.getId() == 145) {
				player.getInventory().deleteItem(145, 1);
				player.getInventory().deleteItem(149, 1);
				player.getInventory().addItem(2436, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			} // Attack done
			if (itemUsed.getId() == 173 && usedWith.getId() == 173) { // STARTRANGE
				player.getInventory().deleteItem(173, 2);
				player.getInventory().addItem(171, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 171 && usedWith.getId() == 171) {
				player.getInventory().deleteItem(171, 2);
				player.getInventory().addItem(2444, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 173 && usedWith.getId() == 171
					|| itemUsed.getId() == 171 && usedWith.getId() == 173) {
				player.getInventory().deleteItem(173, 1);
				player.getInventory().deleteItem(171, 1);
				player.getInventory().addItem(169, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 169 && usedWith.getId() == 173
					|| itemUsed.getId() == 173 && usedWith.getId() == 169) {
				player.getInventory().deleteItem(169, 1);
				player.getInventory().deleteItem(173, 1);
				player.getInventory().addItem(2444, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			} // end of range
			if (itemUsed.getId() == 6691 && usedWith.getId() == 6691) { // SARADOMIN
				player.getInventory().deleteItem(6691, 2);
				player.getInventory().addItem(6689, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 6689 && usedWith.getId() == 6689) { // SARADOMIN
				player.getInventory().deleteItem(6689, 2);
				player.getInventory().addItem(6685, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 6691 && usedWith.getId() == 6689
					|| itemUsed.getId() == 6689 && usedWith.getId() == 6691) {
				player.getInventory().deleteItem(6691, 1);
				player.getInventory().deleteItem(6689, 1);
				player.getInventory().addItem(6687, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 6687 && usedWith.getId() == 6691
					|| itemUsed.getId() == 6691 && usedWith.getId() == 6687) {
				player.getInventory().deleteItem(6691, 1);
				player.getInventory().deleteItem(6687, 1);
				player.getInventory().addItem(6685, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			} // end of saradomin
			if (itemUsed.getId() == 131 && usedWith.getId() == 131) { // RESTORE
				player.getInventory().deleteItem(131, 2);
				player.getInventory().addItem(129, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 129 && usedWith.getId() == 129) { // RESTORE
				player.getInventory().deleteItem(129, 2);
				player.getInventory().addItem(2430, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 129 && usedWith.getId() == 131
					|| itemUsed.getId() == 131 && usedWith.getId() == 129) {
				player.getInventory().deleteItem(129, 1);
				player.getInventory().deleteItem(131, 1);
				player.getInventory().addItem(127, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			}
			if (itemUsed.getId() == 127 && usedWith.getId() == 131
					|| itemUsed.getId() == 131 && usedWith.getId() == 127) {
				player.getInventory().deleteItem(129, 1);
				player.getInventory().deleteItem(131, 1);
				player.getInventory().addItem(2430, 1);
				player.getInventory().addItem(229, 1);
				player.sm("You combined your " + itemUsed.getName() + " potion with your " + usedWith.getName() + ".");
			} // RESTORE END

			/**
			 * Toxic trident
			 */
			if (itemUsed.getId() == 29935 && usedWith.getId() == 29953
					|| usedWith.getId() == 29935 && itemUsed.getId() == 29953) {
				player.getInventory().deleteItem(29935, 1);
				player.getInventory().deleteItem(29953, 1);
				player.getInventory().addItem(29925, 1);
				player.getDialogueManager().startDialogue("ItemMessage",
						"You create a Toxic trident by combining your items.", 29925);
			}

			if (itemUsed.getId() == 1785 && usedWith.getId() == 18330
					|| itemUsed.getId() == 18330 && usedWith.getId() == 1785) {
				player.getInventory().deleteItem(18330, 1);
				player.getInventory().deleteItem(851, 1);
				player.getInventory().addItem(18331, 1);
				player.sm("You attach the two pieces together to create a maple sheildbow (sighted).");
			}
			if (itemUsed.getId() == 6570 && usedWith.getId() == 20767
					|| itemUsed.getId() == 20767 && usedWith.getId() == 6570) {
				player.getInventory().deleteItem(6570, 1);
				player.getInventory().deleteItem(20767, 1);
				player.getInventory().addItem(29875, 1);
				player.sm("You fuse the firecape with the max cape.");
			}
			//kits
			if (itemUsedWithId == 28879 || itemUsedWithId == 29491) {
				if (player.getInventory().containsItem(28879, 1) && player.getInventory().containsItem(29491, 1)) {
					player.getInventory().deleteItem(28879, 1);
					player.getInventory().deleteItem(29491, 1);
					player.getInventory().addItem(28867, 1);
				}
			}
			if (itemUsedWithId == 28875 || itemUsedWithId == 29493) {
				if (player.getInventory().containsItem(28875, 1) && player.getInventory().containsItem(29493, 1)) {
					player.getInventory().deleteItem(28875, 1);
					player.getInventory().deleteItem(29493, 1);
					player.getInventory().addItem(28865, 1);
				}
			}
			if (itemUsedWithId == 28877 || itemUsedWithId == 29489) {
				if (player.getInventory().containsItem(28877, 1) && player.getInventory().containsItem(29489, 1)) {
					player.getInventory().deleteItem(28877, 1);
					player.getInventory().deleteItem(29489, 1);
					player.getInventory().addItem(28863, 1);
				}
			}
			if (itemUsedWithId == 28871 || itemUsedWithId == 52486) {
				if (player.getInventory().containsItem(28871, 1) && player.getInventory().containsItem(52486, 1)) {
					player.getInventory().deleteItem(28871, 1);
					player.getInventory().deleteItem(52486, 1);
					player.getInventory().addItem(29764, 1);
				}
			}
			if (itemUsedWithId == 28869 || itemUsedWithId == 29943) {
				if (player.getInventory().containsItem(28869, 1) && player.getInventory().containsItem(29943, 1)) {
					player.getInventory().deleteItem(28869, 1);
					player.getInventory().deleteItem(29943, 1);
					player.getInventory().addItem(28861, 1);
				}
			}
			if (itemUsedWithId == 28873 || itemUsedWithId == 51003) {
				if (player.getInventory().containsItem(28873, 1) && player.getInventory().containsItem(51003, 1)) {
					player.getInventory().deleteItem(28873, 1);
					player.getInventory().deleteItem(51003, 1);
					player.getInventory().addItem(29895, 1);
				}
			}




			//kits end
			if (itemUsed.getId() == 851 && usedWith.getId() == 23193
					|| itemUsed.getId() == 23193 && usedWith.getId() == 851) {
				if (player.getSkills().getLevel(Skills.CRAFTING) >= 89) {
					player.getInventory().deleteItem(23193, 1);
					player.getInventory().addItem(23191, 1);
					player.getSkills().addXp(Skills.CRAFTING, 40);
					player.animate(new Animation(884));
					player.sm("You succesfully create an empty potion flask.");
				} else {
					player.sm("You need a crafting level of atleast 89 to create a potion flask.");
				}
			}
			if (itemUsed.getId() == 4164 && usedWith.getId() == 4166
					|| itemUsed.getId() == 4164 && usedWith.getId() == 4168
					|| itemUsed.getId() == 4164 && usedWith.getId() == 4551
					|| itemUsed.getId() == 4164 && usedWith.getId() == 8921
					|| itemUsed.getId() == 4166 && usedWith.getId() == 4164
					|| itemUsed.getId() == 4166 && usedWith.getId() == 4168
					|| itemUsed.getId() == 4166 && usedWith.getId() == 4551
					|| itemUsed.getId() == 4166 && usedWith.getId() == 8921
					|| itemUsed.getId() == 4168 && usedWith.getId() == 4164
					|| itemUsed.getId() == 4168 && usedWith.getId() == 4166
					|| itemUsed.getId() == 4168 && usedWith.getId() == 4551
					|| itemUsed.getId() == 4168 && usedWith.getId() == 8921
					|| itemUsed.getId() == 4551 && usedWith.getId() == 4164
					|| itemUsed.getId() == 4551 && usedWith.getId() == 4166
					|| itemUsed.getId() == 4551 && usedWith.getId() == 4168
					|| itemUsed.getId() == 4551 && usedWith.getId() == 8921
					|| itemUsed.getId() == 8921 && usedWith.getId() == 4164
					|| itemUsed.getId() == 8921 && usedWith.getId() == 4166
					|| itemUsed.getId() == 8921 && usedWith.getId() == 4168
					|| itemUsed.getId() == 8921 && usedWith.getId() == 4551) {
				if (player.getInventory().containsItem(4164, 1) && player.getInventory().containsItem(4166, 1)
						&& player.getInventory().containsItem(4168, 1) && player.getInventory().containsItem(4551, 1)
						&& player.getInventory().containsItem(8921, 1)) {
					if (player.getSkills().getLevel(Skills.CRAFTING) < 55) {
						player.sendMessage("Level 55 Crafting is required to make this.");
						return;
					}
					player.getInventory().deleteItem(4164, 1);
					player.getInventory().deleteItem(4166, 1);
					player.getInventory().deleteItem(4168, 1);
					player.getInventory().deleteItem(4551, 1);
					player.getInventory().deleteItem(8921, 1);
					player.getInventory().addItem(13263, 1);
					player.sendMessage("You combine the items to form a superior helmet.");
					return;
				} else {
					player.sendMessage("You don't have the required items to do this.");
				}
			}
			if (itemUsed.getId() == 13263 && usedWith.getId() == 15488
					|| itemUsed.getId() == 13263 && usedWith.getId() == 15490
					|| itemUsed.getId() == 15488 && usedWith.getId() == 13263
					|| itemUsed.getId() == 15488 && usedWith.getId() == 15490
					|| itemUsed.getId() == 15490 && usedWith.getId() == 13263
					|| itemUsed.getId() == 15490 && usedWith.getId() == 15488) {
				if (player.getInventory().containsItem(13263, 1) && player.getInventory().containsItem(15490, 1)
						&& player.getInventory().containsItem(15488, 1)) {
					if (player.getSkills().getLevel(Skills.CRAFTING) < 55) {
						player.sendMessage("Level 55 Crafting is required to make this.");
						return;
					}
					player.getInventory().deleteItem(13263, 1);
					player.getInventory().deleteItem(15488, 1);
					player.getInventory().deleteItem(15490, 1);
					player.getInventory().addItem(15492, 1);
					player.sendMessage("You attach the items onto your helmet.");
				} else {
					player.sendMessage("You don't have the required items to do this.");
				}
				return;
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 237) {
				player.getInventory().deleteItem(237, 1);
				player.getInventory().addItem(235, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 1973) {
				player.getInventory().deleteItem(1973, 1);
				player.getInventory().addItem(1975, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 10109) {
				player.getInventory().deleteItem(10109, 1);
				player.getInventory().addItem(1975, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 5075) {
				player.getInventory().deleteItem(5075, 1);
				player.getInventory().addItem(6693, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 9735) {
				player.getInventory().deleteItem(9735, 1);
				player.getInventory().addItem(9736, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 243) {
				player.getInventory().deleteItem(243, 1);
				player.getInventory().addItem(241, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 4698) {
				player.getInventory().deleteItem(4698, 1);
				player.getInventory().addItem(9594, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 9016) {
				player.getInventory().deleteItem(9016, 1);
				player.getInventory().addItem(9018, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 973) {
				player.getInventory().deleteItem(973, 1);
				player.getInventory().addItem(704, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 6466) {
				player.getInventory().deleteItem(6466, 1);
				player.getInventory().addItem(6467, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 592) {
				player.getInventory().deleteItem(592, 1);
				player.getInventory().addItem(8865, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 3146) {
				player.getInventory().deleteItem(3146, 1);
				player.getInventory().addItem(3152, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 9079) {
				player.getInventory().deleteItem(9079, 1);
				player.getInventory().addItem(9082, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 313) {
				player.getInventory().deleteItem(313, 1);
				player.getInventory().addItem(12129, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 14703) {
				player.getInventory().deleteItem(14703, 1);
				player.getInventory().addItem(14704, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 4620) {
				player.getInventory().deleteItem(4620, 1);
				player.getInventory().addItem(4622, 1);
			}
			if (itemUsed.getId() == 233 && usedWith.getId() == 6812) {
				player.getInventory().deleteItem(6812, 1);
				player.getInventory().addItem(6810, 1);
			}
			if (itemUsed.getId() == 573 && usedWith.getId() == 1379) { // air
				// battlestaff
				player.getInventory().deleteItem(573, 1);
				player.getInventory().deleteItem(1379, 1);
				player.getInventory().addItem(28976, 1);
			}
			if (itemUsed.getId() == 575 && usedWith.getId() == 1379) {
				player.getInventory().deleteItem(575, 1);
				player.getInventory().deleteItem(1379, 1);
				player.getInventory().addItem(28978, 1);
			}
			if (itemUsed.getId() == 569 && usedWith.getId() == 1379) {
				player.getInventory().deleteItem(569, 1);
				player.getInventory().deleteItem(1379, 1);
				player.getInventory().addItem(28979, 1);
			}
			if (itemUsed.getId() == 571 && usedWith.getId() == 1379) {
				player.getInventory().deleteItem(571, 1);
				player.getInventory().deleteItem(1379, 1);
				player.getInventory().addItem(28977, 1);
			}
			/**
			 * Flask Making
			 */

			// Attack (4)
			if (usedWith.getId() == 121 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(121, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(121, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23201, 1);
				}
			} else
			// Attack (3)
			if (usedWith.getId() == 121 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(121, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(121, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23201, 1);
				}
			} else
			// Attack (2)
			if (usedWith.getId() == 123 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(123, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(123, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23203, 1);
				}
			} else
			// Attack (1)
			if (usedWith.getId() == 125 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(125, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(125, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23205, 1);
				}
			} else
			// Super Attack (1) into Super Attack Flask (5)
			if (usedWith.getId() == 149 || usedWith.getId() == 23257) {
				if (player.getInventory().containsItem(149, 1) && player.getInventory().containsItem(23257, 1)) {
					player.getInventory().deleteItem(149, 1);
					player.getInventory().deleteItem(23257, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23255, 1);
				}
			} else
			// Super Attack (1) into Super Attack Flask (4)
			if (usedWith.getId() == 149 || usedWith.getId() == 23259) {
				if (player.getInventory().containsItem(149, 1) && player.getInventory().containsItem(23259, 1)) {
					player.getInventory().deleteItem(149, 1);
					player.getInventory().deleteItem(23259, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23257, 1);
				}
			} else
			// Super Attack (1) into Super Attack Flask (3)
			if (usedWith.getId() == 149 || usedWith.getId() == 23261) {
				if (player.getInventory().containsItem(149, 1) && player.getInventory().containsItem(23261, 1)) {
					player.getInventory().deleteItem(149, 1);
					player.getInventory().deleteItem(23261, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23259, 1);
				}
			} else
			// Super Attack (1) into Super Attack Flask (2)
			if (usedWith.getId() == 149 || usedWith.getId() == 23263) {
				if (player.getInventory().containsItem(149, 1) && player.getInventory().containsItem(23263, 1)) {
					player.getInventory().deleteItem(149, 1);
					player.getInventory().deleteItem(23263, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23261, 1);
				}
			} else
			// Super Attack (1) into Super Attack Flask (1)
			if (usedWith.getId() == 149 || usedWith.getId() == 23265) {
				if (player.getInventory().containsItem(149, 1) && player.getInventory().containsItem(23265, 1)) {
					player.getInventory().deleteItem(149, 1);
					player.getInventory().deleteItem(23265, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23263, 1);
				}
			} else
			// Super Attack (2) into Super Attack Flask (5)
			if (usedWith.getId() == 147 || usedWith.getId() == 23257) {
				if (player.getInventory().containsItem(147, 1) && player.getInventory().containsItem(23257, 1)) {
					player.getInventory().deleteItem(147, 1);
					player.getInventory().deleteItem(23257, 1);
					player.getInventory().addItem(149, 1);
					player.getInventory().addItem(23255, 1);
				}
			} else
			// Super Attack (2) into Super Attack Flask (4)
			if (usedWith.getId() == 147 || usedWith.getId() == 23259) {
				if (player.getInventory().containsItem(147, 1) && player.getInventory().containsItem(23259, 1)) {
					player.getInventory().deleteItem(147, 1);
					player.getInventory().deleteItem(23259, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23255, 1);
				}
			} else
			// Super Attack (2) into Super Attack Flask (3)
			if (usedWith.getId() == 147 || usedWith.getId() == 23261) {
				if (player.getInventory().containsItem(147, 1) && player.getInventory().containsItem(23261, 1)) {
					player.getInventory().deleteItem(147, 1);
					player.getInventory().deleteItem(23261, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23257, 1);
				}
			}
			// Super Attack (2) into Super Attack Flask (2)
			if (usedWith.getId() == 147 || usedWith.getId() == 23263) {
				if (player.getInventory().containsItem(147, 1) && player.getInventory().containsItem(23263, 1)) {
					player.getInventory().deleteItem(147, 1);
					player.getInventory().deleteItem(23263, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23259, 1);
				}
			}
			// Super Attack (2) into Super Attack Flask (1)
			if (usedWith.getId() == 147 || usedWith.getId() == 23265) {
				if (player.getInventory().containsItem(147, 1) && player.getInventory().containsItem(23265, 1)) {
					player.getInventory().deleteItem(147, 1);
					player.getInventory().deleteItem(23265, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23261, 1);
				}
			} else
			// Super Attack (3) into Super Attack Flask (5)
			if (usedWith.getId() == 145 || usedWith.getId() == 23257) {
				if (player.getInventory().containsItem(145, 1) && player.getInventory().containsItem(23257, 1)) {
					player.getInventory().deleteItem(145, 1);
					player.getInventory().deleteItem(23257, 1);
					player.getInventory().addItem(147, 1);
					player.getInventory().addItem(23255, 1);
				}
			} else
			// Super Attack (3) into Super Attack Flask (4)
			if (usedWith.getId() == 145 || usedWith.getId() == 23259) {
				if (player.getInventory().containsItem(145, 1) && player.getInventory().containsItem(23259, 1)) {
					player.getInventory().deleteItem(145, 1);
					player.getInventory().deleteItem(23259, 1);
					player.getInventory().addItem(149, 1);
					player.getInventory().addItem(23255, 1);
				}
			} else
			// Super Attack (3) into Super Attack Flask (3)
			if (usedWith.getId() == 145 || usedWith.getId() == 23261) {
				if (player.getInventory().containsItem(145, 1) && player.getInventory().containsItem(23261, 1)) {
					player.getInventory().deleteItem(145, 1);
					player.getInventory().deleteItem(23261, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23255, 1);
				}
			}
			// Super Attack (3) into Super Attack Flask (2)
			if (usedWith.getId() == 145 || usedWith.getId() == 23263) {
				if (player.getInventory().containsItem(145, 1) && player.getInventory().containsItem(23263, 1)) {
					player.getInventory().deleteItem(145, 1);
					player.getInventory().deleteItem(23263, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23257, 1);
				}
			}
			// Super Attack (3) into Super Attack Flask (1)
			if (usedWith.getId() == 145 || usedWith.getId() == 23265) {
				if (player.getInventory().containsItem(145, 1) && player.getInventory().containsItem(23265, 1)) {
					player.getInventory().deleteItem(145, 1);
					player.getInventory().deleteItem(23265, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23259, 1);
				}
			} else
			// Super Attack (4) into Super Attack Flask (5)
			if (usedWith.getId() == 2436 || usedWith.getId() == 23257) {
				if (player.getInventory().containsItem(2436, 1) && player.getInventory().containsItem(23257, 1)) {
					player.getInventory().deleteItem(2436, 1);
					player.getInventory().deleteItem(23257, 1);
					player.getInventory().addItem(145, 1);
					player.getInventory().addItem(23255, 1);
				}
			} else
			// Super Attack (4) into Super Attack Flask (4)
			if (usedWith.getId() == 2436 || usedWith.getId() == 23259) {
				if (player.getInventory().containsItem(2436, 1) && player.getInventory().containsItem(23259, 1)) {
					player.getInventory().deleteItem(2436, 1);
					player.getInventory().deleteItem(23259, 1);
					player.getInventory().addItem(147, 1);
					player.getInventory().addItem(23255, 1);
				}
			}
			// Super Attack (4) into Super Attack Flask (3)
			if (usedWith.getId() == 2436 || usedWith.getId() == 23261) {
				if (player.getInventory().containsItem(2436, 1) && player.getInventory().containsItem(23261, 1)) {
					player.getInventory().deleteItem(2436, 1);
					player.getInventory().deleteItem(23261, 1);
					player.getInventory().addItem(149, 1);
					player.getInventory().addItem(23255, 1);
				}
			}
			// Super Attack (4) into Super Attack Flask (2)
			if (usedWith.getId() == 2436 || usedWith.getId() == 23263) {
				if (player.getInventory().containsItem(2436, 1) && player.getInventory().containsItem(23263, 1)) {
					player.getInventory().deleteItem(2436, 1);
					player.getInventory().deleteItem(23263, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23255, 1);
				}
			} else
			// Super Attack (4) into Super Attack Flask (1)
			if (usedWith.getId() == 2436 || usedWith.getId() == 23265) {
				if (player.getInventory().containsItem(2436, 1) && player.getInventory().containsItem(23265, 1)) {
					player.getInventory().deleteItem(2436, 1);
					player.getInventory().deleteItem(23265, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23257, 1);
				}
			} else
			// Super Attack (4) into Empty Flask
			if (usedWith.getId() == 2436 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(2436, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(2436, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23259, 1);
				}
			} else
			// Super Attack (3) into Empty Flask
			if (usedWith.getId() == 145 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(145, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(145, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23261, 1);
				}
			} else
			// Super Attack (2) into Empty Flask
			if (usedWith.getId() == 147 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(147, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(147, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23263, 1);
				}
			} else
			// Super Attack (1) into Empty Flask
			if (usedWith.getId() == 149 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(149, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(149, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23265, 1);
				}
			} else
			// Super Strength (1) into Super Strength Flask (5)
			if (usedWith.getId() == 161 || usedWith.getId() == 23281) {
				if (player.getInventory().containsItem(161, 1) && player.getInventory().containsItem(23281, 1)) {
					player.getInventory().deleteItem(161, 1);
					player.getInventory().deleteItem(23281, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23279, 1);
				}
			} else
			// Super Strength (1) into Super Strength Flask (4)
			if (usedWith.getId() == 161 || usedWith.getId() == 23283) {
				if (player.getInventory().containsItem(161, 1) && player.getInventory().containsItem(23283, 1)) {
					player.getInventory().deleteItem(161, 1);
					player.getInventory().deleteItem(23283, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23281, 1);
				}
			}
			// Super Strength (1) into Super Strength Flask (3)
			if (usedWith.getId() == 161 || usedWith.getId() == 23285) {
				if (player.getInventory().containsItem(161, 1) && player.getInventory().containsItem(23285, 1)) {
					player.getInventory().deleteItem(161, 1);
					player.getInventory().deleteItem(23285, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23283, 1);
				}
			}
			// Super Strength (1) into Super Strength Flask (2)
			if (usedWith.getId() == 161 || usedWith.getId() == 23287) {
				if (player.getInventory().containsItem(161, 1) && player.getInventory().containsItem(23287, 1)) {
					player.getInventory().deleteItem(161, 1);
					player.getInventory().deleteItem(23287, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23285, 1);
				}
			} else
			// Super Strength (1) into Super Strength Flask (1)
			if (usedWith.getId() == 161 || usedWith.getId() == 23289) {
				if (player.getInventory().containsItem(161, 1) && player.getInventory().containsItem(23289, 1)) {
					player.getInventory().deleteItem(161, 1);
					player.getInventory().deleteItem(23289, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23287, 1);
				}
			}
			// Super Strength (2) into Super Strength Flask (5)
			if (usedWith.getId() == 159 || usedWith.getId() == 23281) {
				if (player.getInventory().containsItem(159, 1) && player.getInventory().containsItem(23281, 1)) {
					player.getInventory().deleteItem(159, 1);
					player.getInventory().deleteItem(23281, 1);
					player.getInventory().addItem(161, 1);
					player.getInventory().addItem(23279, 1);
				}
			}
			// Super Strength (2) into Super Strength Flask (4)
			if (usedWith.getId() == 159 || usedWith.getId() == 23283) {
				if (player.getInventory().containsItem(159, 1) && player.getInventory().containsItem(23283, 1)) {
					player.getInventory().deleteItem(159, 1);
					player.getInventory().deleteItem(23283, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23279, 1);
				}
			} else
			// Super Strength (2) into Super Strength Flask (3)
			if (usedWith.getId() == 159 || usedWith.getId() == 23285) {
				if (player.getInventory().containsItem(159, 1) && player.getInventory().containsItem(23285, 1)) {
					player.getInventory().deleteItem(159, 1);
					player.getInventory().deleteItem(23285, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23281, 1);
				}
			}
			// Super Strength (2) into Super Strength Flask (2)
			if (usedWith.getId() == 159 || usedWith.getId() == 23287) {
				if (player.getInventory().containsItem(159, 1) && player.getInventory().containsItem(23287, 1)) {
					player.getInventory().deleteItem(159, 1);
					player.getInventory().deleteItem(23287, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23283, 1);
				}
			}
			// Super Strength (2) into Super Strength Flask (1)
			if (usedWith.getId() == 159 || usedWith.getId() == 23289) {
				if (player.getInventory().containsItem(159, 1) && player.getInventory().containsItem(23289, 1)) {
					player.getInventory().deleteItem(159, 1);
					player.getInventory().deleteItem(23289, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23285, 1);
				}
			} else
			// Super Strength (3) into Super Strength Flask (5)
			if (usedWith.getId() == 157 || usedWith.getId() == 23281) {
				if (player.getInventory().containsItem(157, 1) && player.getInventory().containsItem(23281, 1)) {
					player.getInventory().deleteItem(157, 1);
					player.getInventory().deleteItem(23281, 1);
					player.getInventory().addItem(159, 1);
					player.getInventory().addItem(23279, 1);
				}
			}
			// Super Strength (3) into Super Strength Flask (4)
			if (usedWith.getId() == 157 || usedWith.getId() == 23283) {
				if (player.getInventory().containsItem(157, 1) && player.getInventory().containsItem(23283, 1)) {
					player.getInventory().deleteItem(157, 1);
					player.getInventory().deleteItem(23283, 1);
					player.getInventory().addItem(161, 1);
					player.getInventory().addItem(23279, 1);
				}
			}
			// Super Strength (3) into Super Strength Flask (3)
			if (usedWith.getId() == 157 || usedWith.getId() == 23285) {
				if (player.getInventory().containsItem(157, 1) && player.getInventory().containsItem(23285, 1)) {
					player.getInventory().deleteItem(157, 1);
					player.getInventory().deleteItem(23285, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23279, 1);
				}
			} else
			// Super Strength (3) into Super Strength Flask (2)
			if (usedWith.getId() == 157 || usedWith.getId() == 23287) {
				if (player.getInventory().containsItem(157, 1) && player.getInventory().containsItem(23287, 1)) {
					player.getInventory().deleteItem(157, 1);
					player.getInventory().deleteItem(23287, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23281, 1);
				}
			}
			// Super Strength (3) into Super Strength Flask (1)
			if (usedWith.getId() == 157 || usedWith.getId() == 23289) {
				if (player.getInventory().containsItem(157, 1) && player.getInventory().containsItem(23289, 1)) {
					player.getInventory().deleteItem(157, 1);
					player.getInventory().deleteItem(23289, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23283, 1);
				}
			}
			// Super Strength (4) into Super Strength Flask (5)
			if (usedWith.getId() == 2440 || usedWith.getId() == 23281) {
				if (player.getInventory().containsItem(2440, 1) && player.getInventory().containsItem(23281, 1)) {
					player.getInventory().deleteItem(2440, 1);
					player.getInventory().deleteItem(23281, 1);
					player.getInventory().addItem(157, 1);
					player.getInventory().addItem(23279, 1);
				}
			} else
			// Super Strength (4) into Super Strength Flask (4)
			if (usedWith.getId() == 2440 || usedWith.getId() == 23283) {
				if (player.getInventory().containsItem(2440, 1) && player.getInventory().containsItem(23283, 1)) {
					player.getInventory().deleteItem(2440, 1);
					player.getInventory().deleteItem(23283, 1);
					player.getInventory().addItem(159, 1);
					player.getInventory().addItem(23279, 1);
				}
			} else
			// Super Strength (4) into Super Strength Flask (3)
			if (usedWith.getId() == 2440 || usedWith.getId() == 23285) {
				if (player.getInventory().containsItem(2440, 1) && player.getInventory().containsItem(23285, 1)) {
					player.getInventory().deleteItem(2440, 1);
					player.getInventory().deleteItem(23285, 1);
					player.getInventory().addItem(161, 1);
					player.getInventory().addItem(23279, 1);
				}
			} else
			// Super Strength (4) into Super Strength Flask (2)
			if (usedWith.getId() == 2440 || usedWith.getId() == 23287) {
				if (player.getInventory().containsItem(2440, 1) && player.getInventory().containsItem(23287, 1)) {
					player.getInventory().deleteItem(2440, 1);
					player.getInventory().deleteItem(23287, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23279, 1);
				}
			}
			// Super Strength (4) into Super Strength Flask (1)
			if (usedWith.getId() == 2440 || usedWith.getId() == 23289) {
				if (player.getInventory().containsItem(2440, 1) && player.getInventory().containsItem(23289, 1)) {
					player.getInventory().deleteItem(2440, 1);
					player.getInventory().deleteItem(23289, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23281, 1);
				}
			}
			// Super Strength (4) into Empty Flask
			if (usedWith.getId() == 2440 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(2440, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(2440, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23283, 1);
				}
			} else
			// Super Strength (3) into Empty Flask
			if (usedWith.getId() == 157 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(157, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(157, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23285, 1);
				}
			}
			// Super Strength (2) into Empty Flask
			if (usedWith.getId() == 159 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(159, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(159, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23287, 1);
				}
			}
			// Super Strength (1) into Empty Flask
			if (usedWith.getId() == 161 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(161, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(161, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23289, 1);
				}
			} else
			// Super Defence (1) into Super Defence Flask (5)
			if (usedWith.getId() == 167 || usedWith.getId() == 23293) {
				if (player.getInventory().containsItem(167, 1) && player.getInventory().containsItem(23293, 1)) {
					player.getInventory().deleteItem(167, 1);
					player.getInventory().deleteItem(23293, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23291, 1);
				}
			} else
			// Super Defence (1) into Super Defence Flask (4)
			if (usedWith.getId() == 167 || usedWith.getId() == 23295) {
				if (player.getInventory().containsItem(167, 1) && player.getInventory().containsItem(23295, 1)) {
					player.getInventory().deleteItem(167, 1);
					player.getInventory().deleteItem(23295, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23293, 1);
				}
			} else
			// Super Defence (1) into Super Defence Flask (3)
			if (usedWith.getId() == 167 || usedWith.getId() == 23297) {
				if (player.getInventory().containsItem(167, 1) && player.getInventory().containsItem(23297, 1)) {
					player.getInventory().deleteItem(167, 1);
					player.getInventory().deleteItem(23297, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23295, 1);
				}
			}
			// Super Defence (1) into Super Defence Flask (2)
			if (usedWith.getId() == 167 || usedWith.getId() == 23299) {
				if (player.getInventory().containsItem(167, 1) && player.getInventory().containsItem(23299, 1)) {
					player.getInventory().deleteItem(167, 1);
					player.getInventory().deleteItem(23299, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23297, 1);
				}
			}
			// Super Defence (1) into Super Defence Flask (1)
			if (usedWith.getId() == 167 || usedWith.getId() == 23301) {
				if (player.getInventory().containsItem(167, 1) && player.getInventory().containsItem(23301, 1)) {
					player.getInventory().deleteItem(167, 1);
					player.getInventory().deleteItem(23301, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23299, 1);
				}
			} else
			// Super Defence (2) into Super Defence Flask (5)
			if (usedWith.getId() == 165 || usedWith.getId() == 23293) {
				if (player.getInventory().containsItem(165, 1) && player.getInventory().containsItem(23293, 1)) {
					player.getInventory().deleteItem(165, 1);
					player.getInventory().deleteItem(23293, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23291, 1);
				}
			} else
			// Super Defence (2) into Super Defence Flask (4)
			if (usedWith.getId() == 165 || usedWith.getId() == 23295) {
				if (player.getInventory().containsItem(165, 1) && player.getInventory().containsItem(23295, 1)) {
					player.getInventory().deleteItem(165, 1);
					player.getInventory().deleteItem(23295, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23291, 1);
				}
			}
			// Super Defence (2) into Super Defence Flask (3)
			if (usedWith.getId() == 165 || usedWith.getId() == 23297) {
				if (player.getInventory().containsItem(165, 1) && player.getInventory().containsItem(23297, 1)) {
					player.getInventory().deleteItem(165, 1);
					player.getInventory().deleteItem(23297, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23293, 1);
				}
			}
			// Super Defence (2) into Super Defence Flask (2)
			if (usedWith.getId() == 165 || usedWith.getId() == 23299) {
				if (player.getInventory().containsItem(165, 1) && player.getInventory().containsItem(23299, 1)) {
					player.getInventory().deleteItem(165, 1);
					player.getInventory().deleteItem(23299, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23295, 1);
				}
			} else
			// Super Defence (2) into Super Defence Flask (1)
			if (usedWith.getId() == 165 || usedWith.getId() == 23301) {
				if (player.getInventory().containsItem(165, 1) && player.getInventory().containsItem(23301, 1)) {
					player.getInventory().deleteItem(165, 1);
					player.getInventory().deleteItem(23301, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23297, 1);
				}
			} else
			// Super Defence (3) into Super Defence Flask (5)
			if (usedWith.getId() == 163 || usedWith.getId() == 23293) {
				if (player.getInventory().containsItem(163, 1) && player.getInventory().containsItem(23293, 1)) {
					player.getInventory().deleteItem(163, 1);
					player.getInventory().deleteItem(23293, 1);
					player.getInventory().addItem(165, 1);
					player.getInventory().addItem(23291, 1);
				}
			} else
			// Super Defence (3) into Super Defence Flask (4)
			if (usedWith.getId() == 163 || usedWith.getId() == 23295) {
				if (player.getInventory().containsItem(163, 1) && player.getInventory().containsItem(23295, 1)) {
					player.getInventory().deleteItem(163, 1);
					player.getInventory().deleteItem(23295, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23291, 1);
				}
			} else
			// Super Defence (3) into Super Defence Flask (3)
			if (usedWith.getId() == 163 || usedWith.getId() == 23297) {
				if (player.getInventory().containsItem(163, 1) && player.getInventory().containsItem(23297, 1)) {
					player.getInventory().deleteItem(163, 1);
					player.getInventory().deleteItem(23297, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23291, 1);
				}
			}
			// Super Defence (3) into Super Defence Flask (2)
			if (usedWith.getId() == 163 || usedWith.getId() == 23299) {
				if (player.getInventory().containsItem(163, 1) && player.getInventory().containsItem(23299, 1)) {
					player.getInventory().deleteItem(163, 1);
					player.getInventory().deleteItem(23299, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23293, 1);
				}
			}
			// Super Defence (3) into Super Defence Flask (1)
			if (usedWith.getId() == 163 || usedWith.getId() == 23301) {
				if (player.getInventory().containsItem(163, 1) && player.getInventory().containsItem(23301, 1)) {
					player.getInventory().deleteItem(163, 1);
					player.getInventory().deleteItem(23301, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23295, 1);
				}
			} else
			// Super Defence (4) into Super Defence Flask (5)
			if (usedWith.getId() == 2442 || usedWith.getId() == 23293) {
				if (player.getInventory().containsItem(2442, 1) && player.getInventory().containsItem(23293, 1)) {
					player.getInventory().deleteItem(2442, 1);
					player.getInventory().deleteItem(23293, 1);
					player.getInventory().addItem(163, 1);
					player.getInventory().addItem(23291, 1);
				}
			}
			// Super Defence (4) into Super Defence Flask (4)
			if (usedWith.getId() == 2442 || usedWith.getId() == 23295) {
				if (player.getInventory().containsItem(2442, 1) && player.getInventory().containsItem(23295, 1)) {
					player.getInventory().deleteItem(2442, 1);
					player.getInventory().deleteItem(23295, 1);
					player.getInventory().addItem(165, 1);
					player.getInventory().addItem(23291, 1);
				}
			}
			// Super Defence (4) into Super Defence Flask (3)
			if (usedWith.getId() == 2442 || usedWith.getId() == 23297) {
				if (player.getInventory().containsItem(2442, 1) && player.getInventory().containsItem(23297, 1)) {
					player.getInventory().deleteItem(2442, 1);
					player.getInventory().deleteItem(23297, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23291, 1);
				}
			} else
			// Super Defence (4) into Super Defence Flask (2)
			if (usedWith.getId() == 2442 || usedWith.getId() == 23299) {
				if (player.getInventory().containsItem(2442, 1) && player.getInventory().containsItem(23299, 1)) {
					player.getInventory().deleteItem(2442, 1);
					player.getInventory().deleteItem(23299, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23291, 1);
				}
			}
			// Super Defence (4) into Super Defence Flask (1)
			if (usedWith.getId() == 2442 || usedWith.getId() == 23301) {
				if (player.getInventory().containsItem(2442, 1) && player.getInventory().containsItem(23301, 1)) {
					player.getInventory().deleteItem(2442, 1);
					player.getInventory().deleteItem(23301, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23293, 1);
				}
			}
			// Super Defence (4)
			if (usedWith.getId() == 2442 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(2442, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(2442, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23295, 1);
				}
			} else
			// Super Defence (3)
			if (usedWith.getId() == 163 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(163, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(163, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23297, 1);
				}
			} else
			// Super Defence (2)
			if (usedWith.getId() == 165 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(165, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(165, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23299, 1);
				}
			} else
			// Super Defence (1)
			if (usedWith.getId() == 167 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(167, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(167, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23301, 1);
				}
			}

			/**
			 * Overloads
			 */

			// Overload (1) into Overload Flask (5)
			if (usedWith.getId() == 15335 || usedWith.getId() == 23532) {
				if (player.getInventory().containsItem(15335, 1) && player.getInventory().containsItem(26751, 1)) {
					player.getInventory().deleteItem(15335, 1);
					player.getInventory().deleteItem(23532, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (1) into Overload Flask (4)
			if (usedWith.getId() == 15335 || usedWith.getId() == 23533) {
				if (player.getInventory().containsItem(15335, 1) && player.getInventory().containsItem(23533, 1)) {
					player.getInventory().deleteItem(15335, 1);
					player.getInventory().deleteItem(23533, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23532, 1); // id for (5)
				}
			} else
			// Overload (1) into Overload Flask (3)
			if (usedWith.getId() == 15335 || usedWith.getId() == 23534) {
				if (player.getInventory().containsItem(15335, 1) && player.getInventory().containsItem(23534, 1)) {
					player.getInventory().deleteItem(15335, 1);
					player.getInventory().deleteItem(23534, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23533, 1); // id for (4)
				}
			} else
			// Overload (1) into Overload Flask (2)
			if (usedWith.getId() == 15335 || usedWith.getId() == 23535) {
				if (player.getInventory().containsItem(15335, 1) && player.getInventory().containsItem(23535, 1)) {
					player.getInventory().deleteItem(15335, 1);
					player.getInventory().deleteItem(23535, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23534, 1); // id for (3)
				}
			} else
			// Overload (1) into Overload Flask (1)
			if (usedWith.getId() == 15335 || usedWith.getId() == 23536) {
				if (player.getInventory().containsItem(15335, 1) && player.getInventory().containsItem(23536, 1)) {
					player.getInventory().deleteItem(15335, 1);
					player.getInventory().deleteItem(23536, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23535, 1); // id for (2)
				}
			} else
			// Overload (2) into Overload Flask (5)
			if (usedWith.getId() == 15334 || usedWith.getId() == 23532) {
				if (player.getInventory().containsItem(15334, 1) && player.getInventory().containsItem(23532, 1)) {
					player.getInventory().deleteItem(15334, 1);
					player.getInventory().deleteItem(23532, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (2) into Overload Flask (4)
			if (usedWith.getId() == 15334 || usedWith.getId() == 23533) {
				if (player.getInventory().containsItem(15334, 1) && player.getInventory().containsItem(23533, 1)) {
					player.getInventory().deleteItem(15334, 1);
					player.getInventory().deleteItem(23533, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (2) into Overload Flask (3)
			if (usedWith.getId() == 15334 || usedWith.getId() == 23534) {
				if (player.getInventory().containsItem(15334, 1) && player.getInventory().containsItem(23534, 1)) {
					player.getInventory().deleteItem(15334, 1);
					player.getInventory().deleteItem(23534, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23532, 1); // id for (5)
				}
			} else
			// Overload (2) into Overload Flask (2)
			if (usedWith.getId() == 15334 || usedWith.getId() == 23535) {
				if (player.getInventory().containsItem(15334, 1) && player.getInventory().containsItem(23535, 1)) {
					player.getInventory().deleteItem(15334, 1);
					player.getInventory().deleteItem(23535, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23533, 1); // id for (4)
				}
			} else
			// Overload (2) into Overload Flask (1)
			if (usedWith.getId() == 15334 || usedWith.getId() == 23536) {
				if (player.getInventory().containsItem(15334, 1) && player.getInventory().containsItem(23536, 1)) {
					player.getInventory().deleteItem(15334, 1);
					player.getInventory().deleteItem(23536, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23534, 1); // id for (3)
				}
			} else
			// Overload (3) into Overload Flask (5)
			if (usedWith.getId() == 15333 || usedWith.getId() == 23532) {
				if (player.getInventory().containsItem(15333, 1) && player.getInventory().containsItem(23532, 1)) {
					player.getInventory().deleteItem(15333, 1);
					player.getInventory().deleteItem(23532, 1);
					player.getInventory().addItem(15333, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (3) into Overload Flask (4)
			if (usedWith.getId() == 15333 || usedWith.getId() == 23533) {
				if (player.getInventory().containsItem(15333, 1) && player.getInventory().containsItem(23533, 1)) {
					player.getInventory().deleteItem(15333, 1);
					player.getInventory().deleteItem(23533, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (3) into Overload Flask (3)
			if (usedWith.getId() == 15333 || usedWith.getId() == 23534) {
				if (player.getInventory().containsItem(15333, 1) && player.getInventory().containsItem(23534, 1)) {
					player.getInventory().deleteItem(15333, 1);
					player.getInventory().deleteItem(23534, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (3) into Overload Flask (2)
			if (usedWith.getId() == 15333 || usedWith.getId() == 23535) {
				if (player.getInventory().containsItem(15333, 1) && player.getInventory().containsItem(23535, 1)) {
					player.getInventory().deleteItem(15333, 1);
					player.getInventory().deleteItem(23535, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23532, 1); // id for (5)
				}
			} else
			// Overload (3) into Overload Flask (1)
			if (usedWith.getId() == 15333 || usedWith.getId() == 23536) {
				if (player.getInventory().containsItem(15333, 1) && player.getInventory().containsItem(23536, 1)) {
					player.getInventory().deleteItem(15333, 1);
					player.getInventory().deleteItem(23536, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23533, 1); // id for (4)
				}
			} else
			// Overload (4) into Overload Flask (5)
			if (usedWith.getId() == 15332 || usedWith.getId() == 23532) {
				if (player.getInventory().containsItem(15332, 1) && player.getInventory().containsItem(23532, 1)) {
					player.getInventory().deleteItem(15332, 1);
					player.getInventory().deleteItem(23532, 1);
					player.getInventory().addItem(163, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (4) into Overload Flask (4)
			if (usedWith.getId() == 15332 || usedWith.getId() == 23533) {
				if (player.getInventory().containsItem(15332, 1) && player.getInventory().containsItem(23533, 1)) {
					player.getInventory().deleteItem(15332, 1);
					player.getInventory().deleteItem(23533, 1);
					player.getInventory().addItem(165, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (4) into Overload Flask (3)
			if (usedWith.getId() == 15332 || usedWith.getId() == 23534) {
				if (player.getInventory().containsItem(15332, 1) && player.getInventory().containsItem(23534, 1)) {
					player.getInventory().deleteItem(15332, 1);
					player.getInventory().deleteItem(23534, 1);
					player.getInventory().addItem(167, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (4) into Overload Flask (2)
			if (usedWith.getId() == 15332 || usedWith.getId() == 23535) {
				if (player.getInventory().containsItem(15332, 1) && player.getInventory().containsItem(23535, 1)) {
					player.getInventory().deleteItem(15332, 1);
					player.getInventory().deleteItem(23535, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23531, 1); // id for (6)
				}
			} else
			// Overload (4) into Overload Flask (1)
			if (usedWith.getId() == 15332 || usedWith.getId() == 23536) {
				if (player.getInventory().containsItem(15332, 1) && player.getInventory().containsItem(23536, 1)) {
					player.getInventory().deleteItem(15332, 1);
					player.getInventory().deleteItem(23536, 1);
					player.getInventory().addItem(229, 1);
					player.getInventory().addItem(23532, 1); // id for (5)
				}
			} else
			// Overload (1)
			if (usedWith.getId() == 15335 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(15335, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(15335, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23536, 1);
				}
			} else
			// Overload (2)
			if (usedWith.getId() == 15334 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(15334, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(15334, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23535, 1);
				}
			} else
			// Overload (3)
			if (usedWith.getId() == 15333 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(15333, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(15333, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23534, 1);
				}
			} else
			// Overload (4)
			if (usedWith.getId() == 15332 || usedWith.getId() == 23191) {
				if (player.getInventory().containsItem(15332, 1) && player.getInventory().containsItem(23191, 1)) {
					player.getInventory().deleteItem(15332, 1);
					player.getInventory().deleteItem(23191, 1);
					player.getInventory().addItem(23533, 1);
				}
			}
			// Dragon Sq shield
			if (itemUsed.getId() == 2368 || usedWith.getId() == 2366) {
				if (player.getInventory().containsItem(2366, 1) && player.getInventory().containsItem(2368, 1)) {
					player.getInventory().deleteItem(2366, 1);
					player.getInventory().deleteItem(2368, 1);
					player.getInventory().addItem(1187, 1);
					player.getPackets().sendGameMessage("You create a Dragon sq shield.");
				}
			}
			if (itemUsed.getId() == 2366 || usedWith.getId() == 2368) {
				if (player.getInventory().containsItem(2366, 1) && player.getInventory().containsItem(2368, 1)) {
					player.getInventory().deleteItem(2366, 1);
					player.getInventory().deleteItem(2368, 1);
					player.getInventory().addItem(1187, 1);
					player.getPackets().sendGameMessage("You create a Dragon sq shield.");
				}
			}
			// doogle sardine
			if (itemUsed.getId() == 327 || usedWith.getId() == 1573) {
				if (player.getInventory().containsItem(1573, 1) && player.getInventory().containsItem(327, 1)) {
					player.getInventory().deleteItem(1573, 1);
					player.getInventory().deleteItem(327, 1);
					player.getInventory().addItem(1552, 1);
					player.getPackets().sendGameMessage("You wrap the sardine in the doogle leaf.");
				}
			}
			if (itemUsed.getId() == 1573 || usedWith.getId() == 327) {
				if (player.getInventory().containsItem(1573, 1) && player.getInventory().containsItem(327, 1)) {
					player.getInventory().deleteItem(1573, 1);
					player.getInventory().deleteItem(327, 1);
					player.getInventory().addItem(1552, 1);
					player.getPackets().sendGameMessage("You wrap the sardine in the doogle leaf.");
				}
			}

			//

			// Dragonfire shield
			if (itemUsed.getId() == 11286 || usedWith.getId() == 1540) {
				if (player.getInventory().containsItem(11286, 1) && player.getInventory().containsItem(1540, 1)) {
					player.getInventory().deleteItem(11286, 1);
					player.getInventory().deleteItem(1540, 1);
					player.getInventory().addItem(11283, 1);
					player.getPackets().sendGameMessage("You create a Dragonfire Shield!");
				}
			}
			if (itemUsed.getId() == 1540 || usedWith.getId() == 11286) {
				if (player.getInventory().containsItem(11286, 1) && player.getInventory().containsItem(1540, 1)) {
					player.getInventory().deleteItem(11286, 1);
					player.getInventory().deleteItem(1540, 1);
					player.getInventory().addItem(11283, 1);
					player.getPackets().sendGameMessage("You create a Dragonfire Shield!");
				}
			}
			// Fire Arrows
			if (itemUsed.getId() == 1485 || usedWith.getId() == 882) {
				if (player.getInventory().containsItem(1485, 1) && player.getInventory().containsItem(882, 1)) {
					player.getInventory().deleteItem(1485, 1);
					player.getInventory().deleteItem(882, 1);
					player.getInventory().addItem(942, 1);
					player.getPackets().sendGameMessage("You create a bronze fire arrow.");
				}
			}
			if (itemUsed.getId() == 882 || usedWith.getId() == 1485) {
				if (player.getInventory().containsItem(1485, 1) && player.getInventory().containsItem(882, 1)) {
					player.getInventory().deleteItem(1485, 1);
					player.getInventory().deleteItem(882, 1);
					player.getInventory().addItem(942, 1);
					player.getPackets().sendGameMessage("You create a bronze fire arrow.");
				}
			}
			// Roe
			if (itemUsed.getId() == 946 || usedWith.getId() == 11328) {
				if (player.getInventory().containsItem(946, 1) && player.getInventory().containsItem(11328, 1)) {

					player.getInventory().deleteItem(11328, 1);
					player.getInventory().addItem(11324, 1);
				}
			}
			if (itemUsed.getId() == 11328 || usedWith.getId() == 946) {
				if (player.getInventory().containsItem(946, 1) && player.getInventory().containsItem(11328, 1)) {

					player.getInventory().deleteItem(11328, 1);
					player.getInventory().addItem(11324, 1);
				}
			}

			if (itemUsed.getId() == 946 || usedWith.getId() == 11330) {
				if (player.getInventory().containsItem(946, 1) && player.getInventory().containsItem(11330, 1)) {

					player.getInventory().deleteItem(11330, 1);
					player.getInventory().addItem(11324, 1);
				}
			}
			if (itemUsed.getId() == 29749 || usedWith.getId() == 20072) {
				if (player.getInventory().containsItem(29749, 1) && player.getInventory().containsItem(20072, 1)) {

					player.getInventory().deleteItem(29749, 1);
					player.getInventory().deleteItem(20072, 1);
					player.getInventory().addItem(29750, 1);
				}
			}
			if (itemUsed.getId() == 11330 || usedWith.getId() == 946) {
				if (player.getInventory().containsItem(946, 1) && player.getInventory().containsItem(11330, 1)) {

					player.getInventory().deleteItem(11330, 1);
					player.getInventory().addItem(11324, 1);
				}
			}
			if (contains(272, 273, itemUsed, usedWith) || contains(273, 272, itemUsed, usedWith)) {
				player.getInventory().deleteItem(272, 1);
				player.getInventory().deleteItem(273, 1);
				player.getInventory().addItem(274, 1);
			}
			if (itemUsed.getId() == 946 || usedWith.getId() == 11332) {
				if (player.getInventory().containsItem(946, 1) && player.getInventory().containsItem(11332, 1)) {

					player.getInventory().deleteItem(11332, 1);
					player.getInventory().addItem(11324, 1);
				}
			}
			if (itemUsed.getId() == 11332 || usedWith.getId() == 946) {
				if (player.getInventory().containsItem(946, 1) && player.getInventory().containsItem(11332, 1)) {

					player.getInventory().deleteItem(11332, 1);
					player.getInventory().addItem(11324, 1);
				}
			}
			// SS Godswords
			if (itemUsed.getId() == 11690 || usedWith.getId() == 13746) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(13746, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(13746, 1);
					player.getInventory().addItem(28876, 1);
					player.getPackets()
							.sendGameMessage("You attach the sigil to the blade and create an Arcane godsword.");
				}
			}
			if (itemUsed.getId() == 11690 || usedWith.getId() == 13748) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(13748, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(13748, 1);
					player.getInventory().addItem(28877, 1);
					player.getPackets()
							.sendGameMessage("You attach the sigil to the blade and create a Divine godsword.");
				}
			}
			if (itemUsed.getId() == 11690 || usedWith.getId() == 13750) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(13750, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(13750, 1);
					player.getInventory().addItem(28878, 1);
					player.getPackets()
							.sendGameMessage("You attach the sigil to the blade and create an Elysian godsword.");
				}
			}
			if (itemUsed.getId() == 11690 || usedWith.getId() == 13752) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(13752, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(13752, 1);
					player.getInventory().addItem(28879, 1);
					player.getPackets()
							.sendGameMessage("You attach the sigil to the blade and create a Spectral godsword.");
				}
			}
			// Godswords
			if (itemUsed.getId() == 11710 || usedWith.getId() == 11712 || usedWith.getId() == 11714) {
				if (player.getInventory().containsItem(11710, 1) && player.getInventory().containsItem(11712, 1)
						&& player.getInventory().containsItem(11714, 1)) {
					player.getInventory().deleteItem(11710, 1);
					player.getInventory().deleteItem(11712, 1);
					player.getInventory().deleteItem(11714, 1);
					player.getInventory().addItem(11690, 1);
					player.getPackets().sendGameMessage("You made a godsword blade.");
				}
			}
			if (itemUsed.getId() == 11690 || usedWith.getId() == 11702) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(11702, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(11702, 1);
					player.getInventory().addItem(11694, 1);
					player.getPackets()
							.sendGameMessage("You attach the hilt to the blade and make an Armadyl godsword.");
				}
			}
			if (itemUsed.getId() == 11690 || usedWith.getId() == 11704) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(11704, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(11704, 1);
					player.getInventory().addItem(11696, 1);
					player.getPackets()
							.sendGameMessage("You attach the hilt to the blade and make an Bandos godsword.");
				}
			}
			if (itemUsed.getId() == 11690 || usedWith.getId() == 11706) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(11706, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(11706, 1);
					player.getInventory().addItem(11698, 1);
					player.getPackets()
							.sendGameMessage("You attach the hilt to the blade and make an Saradomin godsword.");
				}
			}
			if (itemUsed.getId() == 11690 || usedWith.getId() == 11708) {
				if (player.getInventory().containsItem(11690, 1) && player.getInventory().containsItem(11708, 1)) {
					player.getInventory().deleteItem(11690, 1);
					player.getInventory().deleteItem(11708, 1);
					player.getInventory().addItem(11700, 1);
					player.getPackets()
							.sendGameMessage("You attach the hilt to the blade and make an Zamorak godsword.");
				}
			}

			if (itemUsed.getId() == 1755 || usedWith.getId() == 29810) {
				if (player.getInventory().containsItem(1755, 1) && player.getInventory().containsItem(29810, 1)) {
					player.getInventory().deleteItem(29810, 1);
					player.getInventory().addItem(20159, 1);
					player.getInventory().addItem(20163, 1);
					player.getInventory().addItem(20167, 1);
					player.getInventory().addItem(24980, 1);
					player.getInventory().addItem(24986, 1);
					player.getPackets().sendGameMessage("You open the virtus set.");
				}
			}
			if (itemUsed.getId() == 1755 || usedWith.getId() == 29811) {
				if (player.getInventory().containsItem(1755, 1) && player.getInventory().containsItem(29811, 1)) {
					player.getInventory().deleteItem(29811, 1);
					player.getInventory().addItem(20135, 1);
					player.getInventory().addItem(20139, 1);
					player.getInventory().addItem(20143, 1);
					player.getInventory().addItem(24977, 1);
					player.getInventory().addItem(24983, 1);
					player.getPackets().sendGameMessage("You open the torva set.");
				}
			}
			if (itemUsed.getId() == 1755 || usedWith.getId() == 29812) {
				if (player.getInventory().containsItem(1755, 1) && player.getInventory().containsItem(29812, 1)) {
					player.getInventory().deleteItem(29812, 1);
					player.getInventory().addItem(20147, 1);
					player.getInventory().addItem(20151, 1);
					player.getInventory().addItem(20155, 1);
					player.getInventory().addItem(24974, 1);
					player.getInventory().addItem(24989, 1);
					player.getPackets().sendGameMessage("You open the pernix set.");
				}
			}

			// Blowpipe
			if (player.getToxicBlowpipe().handleItemOnItem(usedWith, toSlot, itemUsed)) {
				return;
			}

			// Serpentine helm
			if (player.getSerpentineHelm().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}
			if (player.getChainMace().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}
			if (player.getSangStaff().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}
			if (player.getViturScythe().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}
			if (player.getSceptre().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}
			if (player.getCrawsBow().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}

			// Toxic trident
			if (player.getTridentOfTheSwamp().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}

			// Trident of the seas
			if (player.getTridentOfTheSeas().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}

			// Toxic staff of the dead
			if (player.getToxicStaff().handleItemOnItem(itemUsed, usedWith)) {
				return;
			}

			// Polypore staff
			if (itemUsed.getId() == 22498 || usedWith.getId() == 22448 || usedWith.getId() == 554) {
				if (player.getInventory().containsItem(22498, 1) && player.getInventory().containsItem(22448, 3000)
						&& player.getInventory().containsItem(554, 15000)
						&& player.getSkills().getLevel(Skills.FARMING) > 79) {
					if (player.getInventory().containsItem(22494, 1)) {
						player.animate(new Animation(15434));
						player.setNextGraphics(new Graphics(2032));
						player.setPolyCharges(3000);
						player.getInventory().deleteItem(22498, 1);
						player.getInventory().deleteItem(22448, 3000);
						player.getInventory().deleteItem(554, 15000);
						// player.getInventory().addItem(22494, 1);
						player.getPackets().sendGameMessage("You charge your Polypore staff.");
						return;
					}
					player.animate(new Animation(15434));
					player.setNextGraphics(new Graphics(2032));
					player.setPolyCharges(3000);
					player.getInventory().deleteItem(22498, 1);
					player.getInventory().deleteItem(22448, 3000);
					player.getInventory().deleteItem(554, 15000);
					player.getInventory().addItem(22494, 1);
					player.getPackets().sendGameMessage("You craft and charge your Polypore staff with 3000 charges.");
					return;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You must have 80 farming, 15k fire runes, 3k polypore spore and 1 polypore stick to make this.");
					return;
				}
			}
			if (itemUsed.getId() == 22448 || usedWith.getId() == 22498 || usedWith.getId() == 554) {
				if (player.getInventory().containsItem(22498, 1) && player.getInventory().containsItem(22448, 3000)
						&& player.getInventory().containsItem(554, 15000)
						&& player.getSkills().getLevel(Skills.FARMING) > 79) {
					if (player.getInventory().containsItem(22494, 1)) {
						player.animate(new Animation(15434));
						player.setNextGraphics(new Graphics(2032));
						player.setPolyCharges(3000);
						player.getInventory().deleteItem(22498, 1);
						player.getInventory().deleteItem(22448, 3000);
						player.getInventory().deleteItem(554, 15000);
						// player.getInventory().addItem(22494, 1);
						player.getPackets().sendGameMessage("You charge your Polypore staff.");
						return;
					}
					player.animate(new Animation(15434));
					player.setNextGraphics(new Graphics(2032));
					player.setPolyCharges(3000);
					player.getInventory().deleteItem(22498, 1);
					player.getInventory().deleteItem(22448, 3000);
					player.getInventory().deleteItem(554, 15000);
					player.getInventory().addItem(22494, 1);
					player.getPackets().sendGameMessage("You craft and charge your Polypore staff with 3000 charges.");
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You must have 80 farming, 15k fire runes, 3k polypore spore and 1 polypore stick to make this.");
					return;
				}
			}
			// Ganodermic robes
			if (itemUsed.getId() == 22451 && usedWith.getId() == 22452) {// Visor
				player.getDialogueManager().startDialogue("PolyVisor");
			}
			if (itemUsed.getId() == 22451 && usedWith.getId() == 22454) {// Leggings
				player.getDialogueManager().startDialogue("PolyLeggings");
			}
			if (itemUsed.getId() == 22451 && usedWith.getId() == 22456) {// poncho
				player.getDialogueManager().startDialogue("PolyPoncho");
			}
			// Grifolic Robes
			if (itemUsed.getId() == 22450 && usedWith.getId() == 22452) {// Visor
				player.getDialogueManager().startDialogue("PolyVisor");
			}
			if (itemUsed.getId() == 22450 && usedWith.getId() == 22454) {// Leggings
				player.getDialogueManager().startDialogue("PolyLeggings");
			}
			if (itemUsed.getId() == 22450 && usedWith.getId() == 22456) {// poncho
				player.getDialogueManager().startDialogue("PolyPoncho");
			}
			// Fungal Robes
			if (itemUsed.getId() == 22449 && usedWith.getId() == 22452) {// Visor
				player.getDialogueManager().startDialogue("PolyVisor");
			}
			if (itemUsed.getId() == 22449 && usedWith.getId() == 22454) {// Leggings
				player.getDialogueManager().startDialogue("PolyLeggings");
			}
			if (itemUsed.getId() == 22449 && usedWith.getId() == 22456) {// poncho
				player.getDialogueManager().startDialogue("PolyPoncho");
			}
			int herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
			int herblore1 = Herblore.isHerbloreSkill(usedWith, itemUsed);
			if (herblore > -1) {
				player.getDialogueManager().startDialogue("HerbloreD", herblore, itemUsed, usedWith);
				return;
			}
			if (herblore1 > -1) {
				player.getDialogueManager().startDialogue("HerbloreD", herblore, usedWith, itemUsed);
				return;
			}
			if (itemUsed.getId() == LeatherCrafting.NEEDLE.getId()
					|| usedWith.getId() == LeatherCrafting.NEEDLE.getId()) {
				if (LeatherCrafting.handleItemOnItem(player, itemUsed, usedWith)) {
					return;
				}
			}
			Sets set = ArmourSets.getArmourSet(itemUsedId, itemUsedWithId);
			if (set != null) {
				ArmourSets.exchangeSets(player, set);
				return;
			}
			if (Firemaking.isFiremaking(player, itemUsed, usedWith)) {
				return;
			} else if (contains(1755, Gem.LIMESTONE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.LIMESTONE);
			} else if (contains(1755, Gem.OPAL.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.OPAL);
			} else if (contains(1755, Gem.JADE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.JADE);
			} else if (contains(1755, Gem.RED_TOPAZ.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.RED_TOPAZ);
			} else if (contains(1755, Gem.SAPPHIRE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.SAPPHIRE);
			} else if (contains(1755, Gem.EMERALD.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.EMERALD);
			} else if (contains(1755, Gem.RUBY.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.RUBY);
			} else if (contains(1755, Gem.DIAMOND.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.DIAMOND);
			} else if (contains(1755, Gem.DRAGONSTONE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.DRAGONSTONE);
			} else if (contains(1755, Gem.ONYX.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.ONYX);
			} else if (contains(1755, BoltTips.OPAL.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.OPAL);
			} else if (contains(1755, BoltTips.JADE.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.JADE);
			} else if (contains(1755, BoltTips.RED_TOPAZ.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.RED_TOPAZ);
			} else if (contains(1755, BoltTips.SAPPHIRE.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.SAPPHIRE);
			} else if (contains(1755, BoltTips.EMERALD.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.EMERALD);
			} else if (contains(1755, BoltTips.RUBY.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.RUBY);
			} else if (contains(1755, BoltTips.DIAMOND.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.DIAMOND);
			} else if (contains(1755, BoltTips.DRAGONSTONE.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.DRAGONSTONE);
			} else if (contains(1755, BoltTips.ONYX.getGemName(), itemUsed, usedWith)) {
				BoltTipFletching.boltFletch(player, BoltTips.ONYX);
			} else if (itemUsed.getId() == 21369 && usedWith.getId() == 4151) {
				player.getInventory().deleteItem(21369, 1);
				player.getInventory().deleteItem(4151, 1);
				player.getInventory().addItem(21371, 1);
				player.getPackets()
						.sendGameMessage("Good job, you have succesfully combined a whip and vine into a vine whip.");
			} else if (itemUsed.getId() == 4151 && usedWith.getId() == 21369) {
				player.getInventory().deleteItem(21369, 1);
				player.getInventory().deleteItem(4151, 1);
				player.getInventory().addItem(21371, 1);
				player.getPackets()
						.sendGameMessage("Good job, you have succesfully combined a whip and vine into a vine whip.");
			} else if (itemUsed.getId() == 13734 && usedWith.getId() == 13754) {
				player.getInventory().deleteItem(13734, 1);
				player.getInventory().deleteItem(13754, 1);
				player.getInventory().addItem(13736, 1);
				player.getPackets().sendGameMessage(
						"You have poured the holy elixir on a spirit shield making it unleash Blessed powers.");
			} else if (itemUsed.getId() == 13754 && usedWith.getId() == 13734) {
				player.getInventory().deleteItem(13734, 1);
				player.getInventory().deleteItem(13754, 1);
				player.getInventory().addItem(13736, 1);
				player.getPackets().sendGameMessage(
						"You have poured the holy elixir on a spirit shield making it unleash Blessed powers.");
			} else if (itemUsed.getId() == 13736 && usedWith.getId() == 13748) {
				player.getInventory().deleteItem(13736, 1);
				player.getInventory().deleteItem(13748, 1);
				player.getInventory().addItem(13740, 1);
				player.getPackets().sendGameMessage(
						"You force the sigil upon the blessed spirit shield making it unleash Divine Powers.");
			} else if (itemUsed.getId() == 13736 && usedWith.getId() == 13750) {
				player.getInventory().deleteItem(13736, 1);
				player.getInventory().deleteItem(13750, 1);
				player.getInventory().addItem(13742, 1);
				player.getPackets().sendGameMessage(
						"You force the sigil upon the blessed spirit shield making it unleash Elysian Powers.");
			} else if (itemUsed.getId() == 13736 && usedWith.getId() == 13746) {
				player.getInventory().deleteItem(13736, 1);
				player.getInventory().deleteItem(13746, 1);
				player.getInventory().addItem(13738, 1);
				player.getPackets().sendGameMessage(
						"You force the sigil upon the blessed spirit shield making it unleash Arcane Powers.");
			} else if (itemUsed.getId() == 13746 && usedWith.getId() == 13736) {
				player.getInventory().deleteItem(13736, 1);
				player.getInventory().deleteItem(13746, 1);
				player.getInventory().addItem(13738, 1);
				player.getPackets().sendGameMessage(
						"You force the sigil upon the blessed spirit shield making it unleash Arcane Powers.");
			} else if (itemUsed.getId() == 13736 && usedWith.getId() == 13752) {
				player.getInventory().deleteItem(13736, 1);
				player.getInventory().deleteItem(13752, 1);
				player.getInventory().addItem(13744, 1);
				player.getPackets().sendGameMessage(
						"You force the sigil upon the blessed spirit shield making it unleash Spectral Powers.");
			} else if (itemUsed.getId() == 13752 && usedWith.getId() == 13736) {
				player.getInventory().deleteItem(13736, 1);
				player.getInventory().deleteItem(13752, 1);
				player.getInventory().addItem(13744, 1);
				player.getPackets().sendGameMessage(
						"You force the sigil upon the blessed spirit shield making it unleash Spectral Powers.");
			} else {
				;
			}
			// player.getPackets().sendGameMessage(
			// "Nothing interesting happens.");
			if (Constants.DEBUG) {
				Logger.log("ItemHandler", "Used:" + itemUsed.getId() + ", With:" + usedWith.getId());
			}
		}
	}

	public static void handleItemOption3(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);

		if (SerpentineHelm.usingSerptentineHelm(itemId)) {
			player.getSerpentineHelm().check();

		} else if (MysteryBox.validBoxesList.contains(itemId)) {
			player.getTemporaryAttributtes().put(MysteryBox.MYSTERY_SELECTED, itemId);
			player.getMysteryBoxes().openBox();
		} else if (itemId == 20767 || itemId == 20769 || itemId == 20771) {
			SkillCapeCustomizer.startCustomizing(player, itemId);
		} else if (itemId >= 15084 && itemId <= 15100) {
			player.getDialogueManager().startDialogue("DiceBag", itemId);
		} else if (itemId == 29997) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"There is " + player.getxpRingCharges() + " charges left.");
		} else if (itemId == 11283) {
			player.sm("Your shield has " + player.getFirePoints() + " fire charges.");
		} else if (itemId == 22494) {
			player.sm("There is currently : " + player.getPolyCharges() + " charges.");
		} else if (ToxicBlowpipe.isBlowpipe(itemId)) {
			player.getToxicBlowpipe().check();
			return;
		} else if (ChainMace.isChainMace(itemId)) {
			player.getChainMace().check();
			return;
		} else if (SangStaff.isSangStaff(itemId)) {
			player.getSangStaff().check();
			return;
		} else if (ViturScythe.isViturScythe(itemId)) {
			player.getViturScythe().check();
			return;
		} else if (Sceptre.isSceptre(itemId)) {
			player.getSceptre().check();
			return;
		} else if (CrawsBow.isCrawsBow(itemId)) {
			player.getCrawsBow().check();
			return;
		} else if (itemId == 19748) {
			player.getSkills().restoreSummoning();
		} else if (TridentOfTheSwamp.isToxicTrident(itemId)) {
			player.getTridentOfTheSwamp().check();
			return;
		} else if (TridentOfTheSeas.isTrident(itemId)) {
			player.getTridentOfTheSeas().check();
			return;
		} else if (itemId == 29965) {
			player.sendMessage("You have " + Utils.formatNumber(player.getArclight().getCharges()) + " charges remaining.");
			return;
		} else if (ToxicStaffOfTheDead.isToxicStaff(itemId)) {
			player.getToxicStaff().check();
			return;
		} else if (itemId == 29996) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You should go speak to Max at home to charge the ring.");
		} else if (itemId >= 24714 && itemId <= 24762) {
			player.sm("You have used [<col=8B0000>" + player.pendantTime + "</col>] minutes out of 1 hour!");
		} else if (itemId == 4155) {
			player.getSlayerManager().checkKillsLeft();
		} else if (itemId == 24437 || itemId == 24439 || itemId == 24440 || itemId == 24441) {
			player.getDialogueManager().startDialogue("FlamingSkull", item, slotId);
		} else if (Equipment.getItemSlot(itemId) == Equipment.SLOT_AURA) {
			player.getAuraManager().sendTimeRemaining(itemId);
		} else if (itemId == 2963) {
			if (player.getPrayer().getPrayerpoints() >= 60) {
				player.getPrayer().drainPrayer(60);
				player.lock(2);
				player.animate(new Animation(9104));
				for (WorldObject object : World.getRegion(player.getRegionId()).getAllObjects()) {
					if (object.withinDistance(player, 1)) {
						switch (object.getId()) {
						case 3512:
						case 3510:
						case 3508:
							World.spawnTemporaryObject(new WorldObject(object.getId() + 1, object.getType(),
									object.getRotation(), object.getX(), object.getY(), object.getZ()), 30000);
							World.sendGraphics(player, new Graphics(263), object);
							break;
						}
					}
				}
				return;
			}
			player.sm("You need more prayer points to do this.");
		} else if (ImplingLoot.isJar(itemId)) {
			ImplingLoot.HandleItem(player, itemId);
			player.implingCount++;
			return;
		} else if (itemId == 18338) {
			if (player.sapphires <= 0 && player.rubies <= 0 && player.emeralds <= 0 && player.diamonds <= 0) {
				player.sm("Your gem pouch is currently empty.");
			} else {
				player.emeralds = 0;
				player.sapphires = 0;
				player.rubies = 0;
				player.diamonds = 0;
				player.sm("You have successfully emptied your gem bag.");
			}
		} else if (itemId == 18339) {
			if (player.coal <= 0) {
				player.sm("Your coal pouch is currently empty.");
			} else {
				player.getInventory().addItem(454, player.coal);
				player.coal = 0;
				player.sm("You have successfully emptied your coal bag.");
			}
		} else if (itemId == 21576) {

			player.sm("The medallion currently has: " + player.drakanCharges + " charges.");

		} else if (itemId == 15440 || itemId == 15439) {
			if (player.horn == 0) {
				player.getPackets().sendGameMessage("Your penance horn is out of charges.");
			} else {
				player.getPackets().sendGameMessage("Your penance horn currently holds " + player.horn + " charges.");
			}
		} else if (itemId == 4151) {
			if (player.getInventory().containsItem(29932, 1)) {
				player.getInventory().deleteItem(29932, 1);
				player.getInventory().deleteItem(4151, 1);
				player.getInventory().addItem(29974, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have one upgrade token to upgrade this weapon.");
			}
		} else if (itemId == 1275) {
			if (player.getInventory().containsItem(29932, 1)) {
				player.getInventory().deleteItem(29932, 1);
				player.getInventory().deleteItem(1275, 1);
				player.getInventory().addItem(29925, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have one upgrade token to upgrade this weapon.");
			}
		}
		if (itemId == 23659) {
			player.getDialogueManager().startDialogue("CapeExchange");
		}

		else if (itemId == 14484) {
			if (player.getInventory().containsItem(29932, 2)) {
				player.getInventory().deleteItem(29932, 2);
				player.getInventory().deleteItem(14484, 1);
				player.getInventory().addItem(29969, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have two upgrade token to upgrade this weapon.");
			}
		}
	}


	public static void handleItemOption4(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);

		if (itemId == 21371) {
			if (player.getInventory().containsItem(29932, 3)) {
				player.getInventory().deleteItem(29932, 3);
				player.getInventory().deleteItem(21371, 1);
				player.getInventory().addItem(29962, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have three upgrade token to upgrade this weapon.");
			}
		} else if (itemId == 11694) {
			if (player.getInventory().containsItem(29932, 2)) {
				player.getInventory().deleteItem(29932, 2);
				player.getInventory().deleteItem(11694, 1);
				player.getInventory().addItem(29973, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have two upgrade token to upgrade this weapon.");
			}
		} else if (itemId == 11696) {
			if (player.getInventory().containsItem(29932, 2)) {
				player.getInventory().deleteItem(29932, 2);
				player.getInventory().deleteItem(11696, 1);
				player.getInventory().addItem(29972, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have two upgrade token to upgrade this weapon.");
			}
		} else if (itemId == 11698) {
			if (player.getInventory().containsItem(29932, 2)) {
				player.getInventory().deleteItem(29932, 2);
				player.getInventory().deleteItem(11698, 1);
				player.getInventory().addItem(29971, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have two upgrade token to upgrade this weapon.");
			}
		} else if (itemId == 11700) {
			if (player.getInventory().containsItem(29932, 2)) {
				player.getInventory().deleteItem(29932, 2);
				player.getInventory().deleteItem(11700, 1);
				player.getInventory().addItem(29970, 1);
				player.getPackets().sendGameMessage("Your weapon seems to be a lot more powerful.");
			} else {
				player.getPackets().sendGameMessage("You must have two upgrade token to upgrade this weapon.");
			}
		}
	}

	public static void handleItemOption5(Player player, int slotId, int itemId, Item item) {
		System.out.println("Item: " + item.getId() + " Option 5.");
		Logger.logMessage("Item: " + item.getId() + " Option 5.");


	}

	public static void handleItemOption6(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		player.stopAll(false);
		if (player.getToolbelt().addItem(slotId, item)) {
			return;
		}
		if (itemId == 8901) {
			if (player.getInventory().containsItem(8901, 1)) {
				player.getInventory().deleteItem(8901, 1);
				player.getInventory().addItem(8921, 1);
				player.getPackets().sendGameMessage("You remove the charges from the mask.");

			}
		}
		if (MysteryBox.validBoxesList.contains(itemId)) {
			player.getTemporaryAttributtes().put(MysteryBox.MYSTERY_SELECTED, itemId);
			player.getMysteryBoxes().openAllBoxes();
		}

		if (itemId == 29966) {
			player.getToxicBlowpipe().unchargeDialogue(slotId);
		}
		if (itemId == 14057) {
			SorceressGarden.teleportToSocreressGarden(player, true);
		}
		if (Pots.emptyPot(player, item, slotId))
			return;
		if (SerpentineHelm.usingSerptentineHelm(item.getId())) {
			player.getSerpentineHelm().uncharge(item);
			return;
		}
		if (TridentOfTheSwamp.isToxicTrident(item.getId())) {
			player.getTridentOfTheSwamp().uncharge(item);
			return;
		}
		if (TridentOfTheSeas.isTrident(item.getId())) {
			player.getTridentOfTheSeas().uncharge(item);
			return;
		}
		if (ToxicStaffOfTheDead.isToxicStaff(item.getId())) {
			player.getToxicStaff().uncharge();
			return;
		}
		if (ToxicStaffOfTheDead.UNCHARGED == item.getId()) {
			player.getToxicStaff().dismantle();
			return;
		}
		Pouches pouches = Pouches.forId(itemId);
		if (pouches != null) {
			Summoning.spawnFamiliar(player, pouches);
		} else if (itemId == 13263) {
			if (player.getInventory().getFreeSlots() < 4) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
			if (player.getInventory().containsItem(13263, 1)) {
				player.getInventory().deleteItem(13263, 1);
				player.getInventory().addItem(4164, 1);
				player.getInventory().addItem(4166, 1);
				player.getInventory().addItem(4168, 1);
				player.getInventory().addItem(4551, 1);
				player.getInventory().addItem(8921, 1);
				player.sendMessage("You've dismantled your Slayer helmet.");
				return;
			}
		} else if (itemId == 15492) {
			if (player.getInventory().getFreeSlots() < 2) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
			if (player.getInventory().containsItem(15492, 1)) {
				player.getInventory().deleteItem(15492, 1);
				player.getInventory().addItem(13263, 1);
				player.getInventory().addItem(15488, 1);
				player.getInventory().addItem(15490, 1);
				player.sendMessage("You've dismantled your Full slayer helmet.");
				return;
			}
		}

		else if (itemId == 1438) {
			Runecrafting.locate(player, 3127, 3405);
		} else if (itemId == 1440) {
			Runecrafting.locate(player, 3306, 3474);
		} else if (itemId == 1442) {
			Runecrafting.locate(player, 3313, 3255);
		} else if (itemId == 1444) {
			Runecrafting.locate(player, 3185, 3165);
		} else if (itemId == 1446) {
			Runecrafting.locate(player, 3053, 3445);
		} else if (itemId == 1448) {
			Runecrafting.locate(player, 2982, 3514);
		} else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354 && itemId <= 10362) {
			player.getDialogueManager().startDialogue("Transportation", "Edgeville", new Position(3087, 3496, 0),
					"Karamja", new Position(2918, 3176, 0), "Draynor Village", new Position(3105, 3251, 0),
					"Al Kharid", new Position(3293, 3163, 0), itemId);
		} else if (itemId == 995) {
			player.getMoneyPouch().sendDynamicInteraction(item.getAmount(), false, MoneyPouch.TYPE_POUCH_INVENTORY);
		} else if (itemId == 1704 || itemId == 10352) {
			player.getPackets().sendGameMessage(
					"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		} else if (itemId >= 3853 && itemId <= 3867) {
			player.getDialogueManager().startDialogue("Transportation", "Burthrope Games Room",
					new Position(2880, 3559, 0), "Barbarian Outpost", new Position(2519, 3571, 0), "Gamers' Grotto",
					new Position(2970, 9679, 0), "Corporeal Beast", new Position(2886, 4377, 0), itemId);
		} else if (itemId >= 2552 && itemId <= 2566) {
			player.getDialogueManager().startDialogue("Transportation", "Duel Arena", new Position(3366, 3267, 0),
					"Castle Wars", new Position(2443, 3088, 0), "Fist Of Guthix", new Position(1701, 5600, 0),
					"Mobilising Armies", new Position(2414, 2842, 0), itemId);
		} else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354 && itemId <= 10362) {
			player.getDialogueManager().startDialogue("Transportation", "Sumona", new Position(3361, 2998, 0),
					"Slayer Tower", new Position(3429, 3534, 0), "Fremennik Slayer Dungeon",
					new Position(2793, 3616, 0), "Tarn's Lair", new Position(2773, 4540, 0), itemId);
		} else if (itemId >= 11105 && itemId <= 11111) {
			player.getDialogueManager().startDialogue("SkillsNeck");
			if (item.getId() >= 3853 && item.getId() <= 3865 || item.getId() >= 10354 && item.getId() <= 10361
					|| item.getId() >= 11105 && item.getId() <= 11111) {
				item.setId(item.getId() + 2);
			}
			player.getInventory().refresh(player.getInventory().getItems().getThisItemSlot(item));
		} else if (itemId >= 11118 && itemId <= 11127) {
			player.getDialogueManager().startDialogue("Transportation", "Warrior's Guild", new Position(2867, 3543, 0),
					"Champion's Guild", new Position(3190, 3361, 0), "Monastery", new Position(3052, 3490, 0),
					"Ranging Guild", new Position(2664, 3433, 0), itemId);
		}
	}

	public static void drop(Player player, int slotId, int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getEmotesManager().getNextEmoteEnd() >= time) {
			return;
		}
		if (ChainMace.isChainMace(item.getId())) {
			player.getChainMace().uncharge(item);
			return;
		}
		if (SangStaff.isSangStaff(item.getId())) {
			player.getSangStaff().uncharge(item);
			return;
		}
		if (ViturScythe.isViturScythe(item.getId())) {
			player.getViturScythe().uncharge(item);
			return;
		}
		if (CrawsBow.isCrawsBow(item.getId())) {
			player.getCrawsBow().uncharge(item);
			return;
		}
		if (Sceptre.isSceptre(item.getId())) {
			player.getSceptre().uncharge(item);
			return;
		}

		if (!player.getControlerManager().canDropItem(item)) {
			return;
		}

		player.stopAll(false);

		if (item.getDefinitions().isDestroyItem()) {
			player.getDialogueManager().startDialogue("DestroyItemOption", slotId, item);
			return;
		}

		if (LendingManager.isLendedItem(player, item)) {
			Lend lend;
			if ((lend = LendingManager.getLend(player)) != null) {
				if (lend.getItem().getDefinitions().getLendId() == item.getId()) {
					player.getDialogueManager().startDialogue("DiscardLend", lend);
					return;
				}
			}
			return;
		}

		if (player.isBurying) {
			player.getPackets().sendGameMessage("You can't drop items while your burying.");
			return;
		}

		if (player.getPetManager().spawnPet(itemId, true)) {
			return;
		}

		player.getInventory().deleteItem(slotId, item);

		if (player.getCharges().degradeCompletly(item)) {
			return;
		}

		World.addGroundItem(item, new Position(player), player, true, 60);
		player.getPackets().sendSound(2739, 0, 1);
	}

	public static void handleItemOption8(Player player, int slotId, int itemId, Item item) {
		int id = item.getDefinitions().isNoted() ? item.getDefinitions().certId : itemId;
		player.getInventory().sendExamine(slotId);
		player.sm("Item High Alch : <col=FF0000>" + Shop.getSellPrice(item) / 2 + "</col>. Low Alch : <col=FF0000>"
				+ Shop.getSellPrice(item) / 5 + " </col>SlimePrice: <col=FF0000>" + SlimeExchange.prices.getOrDefault(id, 0) + "</col>.");
		if (player.getUsername().equalsIgnoreCase("andy") || player.getUsername().equalsIgnoreCase("corey")
				|| player.getUsername().equalsIgnoreCase("delu")) {
			player.sendMessage("ItemID: " + item.getId());
		}
	}

	public static void handleItemOnPlayer(final Player player, final Player usedOn, final int itemId) {
		player.setCoordsEvent(new CoordsEvent(usedOn, new Runnable() {
			@Override
			public void run() {
				player.faceEntity(usedOn);
				if (usedOn.getInterfaceManager().containsScreenInter()) {
					player.sendMessage(usedOn.getDisplayName() + " is busy.");
					return;
				}
				if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
					player.sm("You can't do this during combat.");
					return;
				}
				if (usedOn.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
					player.sm("You cannot send a request to a player in combat.");
					return;
				}
				switch (itemId) {

				case 962: //Christmas cracker

					int[] partyhats = { 1038, 1040, 1042, 1044, 1046, 1048 };
					int random = Utils.getRandom(partyhats.length - 1);
					Item partyhat = new Item(partyhats[random], 1);

					int[] secondarys = { 1897, 1635, 1718, 950, 1969, 1973, 2355, 441, 563, 1217 };
					int random2 = Utils.getRandom(secondarys.length - 1);
					Item secondary = new Item(secondarys[random2], 1);

					Player winner = Utils.getRandom(1) == 0 ? player : usedOn;

					player.getDialogueManager().startDialogue(new Dialogue() {

						Player winner;

						@Override
						public void start() {
							winner = (Player) parameters[0];
							sendOptionsDialogue("If you pull the cracker, it will be destroyed.",
									"That's okay, I might get a party hat!", "Stop. I want to keep my cracker.");
						}

						@Override
						public void run(int interfaceId, int componentId) {
							if (componentId == OPTION_1) {
								if (!player.getInventory().containsItem(962, 1)) {
									player.getDialogueManager().startDialogue("SimpleMessage", "Nice try faggot.");
								}
								player.faceEntity(usedOn);
								usedOn.faceEntity(player);
								player.animate(new Animation(15153));
								usedOn.animate(new Animation(15153));
								winner.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
								player.getInventory().deleteItem(962, 1);
								if (player.getGameMode().isIronman())
									winner = player;
								winner.getInventory().addItemDrop(partyhat.getId(), partyhat.getAmount());
								winner.getInventory().addItemDrop(secondary.getId(), secondary.getAmount());
								end();
							} else if (componentId == OPTION_2) {
								end();
							}
						}

						@Override
						public void finish() {

						}

					}, winner);
					break;
					
				case 29834: //Custom cracker

					int[] partyhats1 = { 29014, 29016, 29018, 29020, 29022, 29024, 29026, 29029, 29031, 29033, 29035, 29037, 29039, 29041, 29043, 29046, 29048, 29050, 29052, 29054 };
					int random1 = Utils.getRandom(partyhats1.length - 1);
					Item partyhat1 = new Item(partyhats1[random1], 1);

					int[] secondarys1 = { 1897, 1635, 1718, 950, 1969, 1973, 2355, 441, 563, 1217 };
					int random3 = Utils.getRandom(secondarys1.length - 1);
					Item secondary1 = new Item(secondarys1[random3], 1);

					Player winner1 = Utils.getRandom(1) == 0 ? player : usedOn;

					player.getDialogueManager().startDialogue(new Dialogue() {

						Player winner;

						@Override
						public void start() {
							winner = (Player) parameters[0];
							sendOptionsDialogue("If you pull the cracker, it will be destroyed.",
									"That's okay, I might get a custom rare!", "Stop. I want to keep my cracker.");
						}

						@Override
						public void run(int interfaceId, int componentId) {
							if (componentId == OPTION_1) {
								if (!player.getInventory().containsItem(29834, 1)) {
									player.getDialogueManager().startDialogue("SimpleMessage", "Nice try faggot.");
								}
								player.faceEntity(usedOn);
								usedOn.faceEntity(player);
								player.animate(new Animation(15153));
								usedOn.animate(new Animation(15153));
								winner.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
								player.getInventory().deleteItem(29834, 1);
								if (player.getGameMode().isIronman())
									winner = player;
								winner.getInventory().addItemDrop(partyhat1.getId(), partyhat1.getAmount());
								winner.getInventory().addItemDrop(secondary1.getId(), secondary1.getAmount());
								end();
							} else if (componentId == OPTION_2) {
								end();
							}
						}

						@Override
						public void finish() {

						}

					}, winner1);
					break;
					
				case 29543:
					int[][] rewards = { { 1635, 1 }, { 1601, 1 }, { 15430, 1 }, { 1615, 1 }, { 5972, 1 }, { 1127, 1 }, {1514, 5}, {454, 50}, {15431, 1}  };
					random = Utils.getRandom(rewards.length);
					winner = Utils.getRandom(1) == 0 ? player : usedOn;
					boolean black_santa = Utils.getRandom(500) == 0;
					player.getDialogueManager().startDialogue(new Dialogue() {

						Player winner;

						@Override
						public void start() {
							winner = (Player) parameters[0];
							sendOptionsDialogue("If you pull the cracker, it will be destroyed.",
									"That's okay, I might get a black santa hat!", "Stop. I want to keep my cracker.");
						}

						@Override
						public void run(int interfaceId, int componentId) {
							if (componentId == OPTION_1) {
								if (!player.getInventory().containsItem(29543, 1)) {
									player.getDialogueManager().startDialogue("SimpleMessage", "Nice try faggot.");
								}
								player.faceEntity(usedOn);
								usedOn.faceEntity(player);
								player.animate(new Animation(15153));
								usedOn.animate(new Animation(15153));
								winner.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
								player.getInventory().deleteItem(29543, 1);
								if (player.getGameMode().isIronman())
									winner = player;
								if (black_santa) {
									World.sendWorldMessage("<col=ff0000><img=4>[News]:</col> " + winner.getDisplayName() + " <col=ff0000>has just received a</col> Black santa hat <col=ff0000>from a</col> Festive cracker<col=ff0000>!", false);
									winner.getInventory().addItemDrop(29941, 1);
								}
								winner.getInventory().addItemDrop(rewards[random][0], rewards[random][1]);
								end();
							} else if (componentId == OPTION_2) {
								end();
							}
						}

						@Override
						public void finish() {

						}

					}, winner);
					break;

				case 4155:
					player.getSlayerManager().invitePlayer(usedOn);
					break;
				case 11951:
					player.getInventory().deleteItem(11951, 1);
					player.faceEntity(usedOn);
					player.animate(new Animation(7530));
					World.sendProjectile(player, player, usedOn, 1281, 21, 21, 90, 65, 50, 0);
					CoresManager.fastExecutor.schedule(new TimerTask() {
						int snowballtime = 3;

						@Override
						public void run() {
							try {
								if (snowballtime == 1) {
									usedOn.setNextGraphics(new Graphics(1282));
								}
								if (this == null || snowballtime <= 0) {
									cancel();
									return;
								}
								if (snowballtime >= 0) {
									snowballtime--;
								}
							} catch (Throwable e) {
								Logger.handle(e);
							}
						}
					}, 0, 600);
					break;
				default:
					// player.sendMessage("Nothing interesting happens.");
					break;
				}
			}
		}, usedOn.getSize()));
	}

	public static void handleItemOnNPC(final Player player, final NPC npc, final Item item) {
		if (item == null) {
			return;
		}
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
					return;
				}
				if (npc instanceof Pet) {
					player.faceEntity(npc);
					player.getPetManager().eat(item.getId(), (Pet) npc);
					return;
				}
				if (npc.getDefinitions().name.contains("ool leprech")) {
					for (int produceId : PatchConstants.produces) {
						if (produceId == item.getId()) {
							int num = player.getInventory().getNumberOf(produceId);
							player.getInventory().deleteItem(produceId, num);
							player.getInventory()
									.addItem(new Item(ItemDefinitions.getItemDefinitions(produceId).getCertId(), num));
							return;
						}
					}
					player.getPackets().sendGameMessage("The leprechaun cannot note that item for you.");
				}
				if (npc instanceof ConditionalDeath) {
					((ConditionalDeath) npc).useHammer(player);
					return;
				}
				if (npc.getId() == 6892) {
					player.getBossPetsManager().insurePet(item.getId());
					return;
				}
				if (npc.getId() == 519) {
					if (BrokenItems.forId(item.getId()) == null) {
						player.getDialogueManager().startDialogue("SimpleMessage", "You cant repair this item.");
						return;
					}
					player.getDialogueManager().startDialogue("Repair", 945, item.getId());

					return;
				}
			}
		}, npc.getSize()));
	}
}