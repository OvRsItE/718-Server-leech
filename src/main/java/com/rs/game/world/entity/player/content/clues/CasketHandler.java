/**
 * 
 */
package com.rs.game.world.entity.player.content.clues;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Shop;
import com.rs.game.world.entity.player.content.clues.impl.EasyTable;
import com.rs.game.world.entity.player.content.clues.impl.EliteTable;
import com.rs.game.world.entity.player.content.clues.impl.HardTable;
import com.rs.game.world.entity.player.content.clues.impl.MediumTable;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Aug 4, 2018
 */
public class CasketHandler {

	//(casket id), (min level), (max level), (drop rate)
	private static final int[][] CASKETS = {
			{2714, 2, 32, 5}, //Easy caskets
			{2802, 33, 75, 3}, //Medium caskets
			{2724, 76, 321, 2}, //Hard caskets
			{19039, 322, 785, 1}, //Elite caskets
	};

	public static void dropCasket(final Player player, final NPC npc) {
		for (int[] casket : CASKETS) {			
			int id = casket[0];
			int min = casket[1];
			int max = casket[2];
			int rate = casket[3];
			if (npc.getCombatLevel() < min || npc.getCombatLevel() > max)
				continue;
			int roll = Utils.random(200);
			if (roll <= rate) {
				Item drop = new Item(id);
				if (npc.getId() == 16027 || npc.getId() == 20492) {
					player.getPackets().sendGameMessage("<col=ff0000>A casket falls to the ground as you slay your opponent.</col>");
					World.addGroundItem(drop, new Position(player), player, true, 60);
				} else {
					World.addGroundItem(drop, new Position(
									npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getZ()),
							player, true, 120);
					player.getPackets().sendGameMessage("<col=ff0000>A casket falls to the ground as you slay your opponent.</col>");
				}
			}
		}
	}
	
	public static Item containsRareItem(Item... items) {
		for (Item item : items) {
			if (item == null)
				continue;
			String name = item.getDefinitions().getName();
			if (name.contains("Third-age ") || name.contains(" dye")) {
					return item;
			}
		}
		return null;
	}
	
	public static void openEasy(Player player) {
		Item[] rewards = new Item[Utils.random(4, 6)];
		for (int index = 0; index < (rewards.length - 1); index++) {
			rewards[index] = EasyTable.roll();
		}
		player.getInventory().deleteItem(2714, 1);
		sendReward(player, rewards);
		Item reward = CasketHandler.containsRareItem(rewards);
		if (reward != null)
			World.sendWorldMessage("<col=ff0000>News: " + player.getDisplayName() + " has just recieved a " 
		+ reward.getName() + " from an easy casket.", false);
		player.getInventory().addItemDrop(rewards);
		player.setLastCasketTier(CasketTier.EASY);
		player.completedEasyClues++;
	}
	
	public static void openMedium(Player player) {
		Item[] rewards = new Item[Utils.random(4, 6)];
		for (int index = 0; index < (rewards.length - 1); index++) {
			rewards[index] = MediumTable.roll();
		}
		player.getInventory().deleteItem(2802, 1);
		sendReward(player, rewards);
		Item reward = CasketHandler.containsRareItem(rewards);
		if (reward != null)
			World.sendWorldMessage("<col=ff0000>News: " + player.getDisplayName() + " has just recieved a " 
		+ reward.getName() + " from a medium casket.", false);
		player.getInventory().addItemDrop(rewards);
		player.setLastCasketTier(CasketTier.MEDIUM);
		player.completedMediumClues++;
	}
	
	public static void openHard(Player player) {
		Item[] rewards = new Item[Utils.random(4, 6)];
		for (int index = 0; index < (rewards.length - 1); index++) {
			rewards[index] = HardTable.roll();
		}
		player.getInventory().deleteItem(2724, 1);
		sendReward(player, rewards);
		Item reward = CasketHandler.containsRareItem(rewards);
		if (reward != null)
			World.sendWorldMessage("<col=ff0000>News: " + player.getDisplayName() + " has just recieved a " 
		+ reward.getName() + " from a hard casket.", false);
		player.getInventory().addItemDrop(rewards);
		player.setLastCasketTier(CasketTier.HARD);
		player.completedHardClues++;
	}
	
	public static void openElite(Player player) {
		Item[] rewards = new Item[Utils.random(4, 6)];
		for (int index = 0; index < (rewards.length - 1); index++) {
			rewards[index] = EliteTable.roll();
		}
		player.getInventory().deleteItem(19039, 1);
		CasketHandler.sendReward(player, rewards);
		Item reward = CasketHandler.containsRareItem(rewards);
		if (reward != null)
			World.sendWorldMessage("<col=ff0000>News: " + player.getDisplayName() + " has just recieved a " 
		+ reward.getName() + " from an elite casket.", false);
		player.getInventory().addItemDrop(rewards);
		player.setLastCasketTier(CasketTier.ELITE);
		player.completedEliteClues++;
	}
	
	
	public static void sendReward(final Player player, Item... items) {
		player.getInterfaceManager().sendInterface(1368);
		for (int i = 19; i <= 24; i++) {
			player.getPackets().sendIComponentSprite(1368, i, -1);
			player.getPackets().sendIComponentSprite(1368, 30, -1); 
		}
		
		for (int i = 19, item = 0; i < 19 + items.length; i++, item++) {
			if (items[item] == null) {
				continue;
			}
			player.getPackets().sendItemOnIComponent(1368, i, items[item].getId(), items[item].getAmount());
		}
		
		int value = 0;
		for (Item item : items) {
			if (item == null) {
				continue;
			}
			value += Shop.getSellPrice(item);
		}
		player.getPackets().sendIComponentText(1368, 25, "Current reward value: " + Utils.formatNumber(value) + " coins!");
		incrementRerolls(player);
		player.getPackets().sendIComponentText(1368, 26, "Reroll progress - " + player.getCasketRerolls() + "/3");
	}
	
	public static void handleButtons(Player player, int interfaceId, int buttonId) {
		if (interfaceId == 1368) {
			switch (buttonId) {
			case 9:
				player.getInterfaceManager().closeScreenInterface();
				break;
			case 27:
				if (player.getCasketRerolls() == 3) {
					switch (player.getLastCasketTier()) {
					case EASY:
						player.setCasketRerolls(0);
						openEasy(player);
						break;
					case MEDIUM:
						player.setCasketRerolls(0);
						openMedium(player);
						break;
					case HARD:
						player.setCasketRerolls(0);
						openHard(player);
						break;
					case ELITE:
						player.setCasketRerolls(0);
						openElite(player);
						break;
					default:
						break;
					}
				} else {
					player.sm("You do not have enough rerolls.");
				}
				break;
			}
		}
	}
	
	public static void incrementRerolls(Player player) {
		player.casketRerolls++;
		if (player.getCasketRerolls() > 3) {
			player.setCasketRerolls(3);
		}
	}
	
}
