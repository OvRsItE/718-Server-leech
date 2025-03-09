/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.PlayerLook;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * Handles selection of modes when you join the server.
 * @author ReverendDread
 * Aug 7, 2018
 */
public class ModeSelect extends Dialogue {
		
	int mode;
	
	@Override
	public void start() {
		sendOptionsDialogue("Choose your game mode!", "Normal (250x Combat) (35x Skilling)", 
				"Hard (200x Combat) (28x Skilling)", "Extreme (125x Combat) (x17.5 Skilling)", "Elite (25x Combat) (x3.5 Skilling)");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Are you sure you want to play on normal mode?", "Yes, I'm sure.", "No thanks."); //mode = 0;
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue("Are you sure you want to play on hard mode?", "Yes, I'm sure.", "No thanks."); //mode = 1;
			} else if (componentId == OPTION_3) {
				sendOptionsDialogue("Are you sure you want to play on extreme mode?", "Yes, I'm sure.", "No thanks."); //mode = 2;
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("Are you sure you want to play on elite mode?", "Yes, I'm sure.", "No thanks."); //mode = 3;
			}
			stage = 0;
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				//player.gameMode = mode;
				sendOptionsDialogue("Do you wish to be an Iron man?", "Yes please.", "No thanks.");
				stage = 1;
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue("Choose your game mode!", "Normal (250x Combat) (35x Skilling)", 
						"Hard (200x Combat) (28x Skilling)", "Extreme (125x Combat) (x17.5 Skilling)", "Elite (25x Combat) (x3.5 Skilling)");
				stage = -1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Are you sure you want to be an Iron man?", "Yes, I'm sure.", "No thanks.");
				stage = 2;
			} else if (componentId == OPTION_2) {
				sendStarter(player);
				end();
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				sendStarter(player);
				end();
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue("Do you wish to be an Iron man?", "Yes please.", "No thanks.");
				stage = 0;
			}
		}
	}

	private final void sendStarter(Player player) {
		PlayerLook.openCharacterCustomizing(player);
		player.getMoneyPouch().sendDynamicInteraction(1_000_000, false); //1m coins
		player.getInventory().addItem(11818, 1); //Iron set
		player.getInventory().addItem(11838, 1); //Rune set
		player.getInventory().addItem(1323, 1); //Iron scimi
		player.getInventory().addItem(1333, 1); //Rune scimi
		player.getInventory().addItem(24573, 1); //starter scimi
		player.getInventory().addItem(7947, 100); //monkfish
		player.getInventory().addItem(1712, 1); //Glory
		player.getInventory().addItem(3105, 1); //Climbing boots
		player.getInventory().addItem(1381, 1); //Air staff
		player.getInventory().addItem(558, 1000); //Mind runes
		player.getInventory().addItem(556, 1000); //air rune runes
		player.getInventory().addItem(6107, 1); //Robe top
		player.getInventory().addItem(6108, 1); //Robe bottom
		player.getInventory().addItem(6109, 1); //Hood
		player.getInventory().addItem(1169, 1); //Coif
		player.getInventory().addItem(1129, 1); //Leather body
		player.getInventory().addItem(1095, 1); //Leather chaps
		player.getInventory().addItem(841, 1); //Short bow
		player.getInventory().addItem(861, 1); //Magic shortbow
		player.getInventory().addItem(884, 1000); //Iron arrow
		player.getInventory().addItem(1267, 1); //iron pickaxe
		player.getInventory().addItem(1275, 1); //rune pickaxe
		player.getInventory().addItem(1349, 1); //iron pickaxe
		player.getInventory().addItem(1359, 1); //rune pickaxe
		player.finishedStarter = true;
		World.sendWorldMessage("<col=ff0000><img=4>[Announcements]: Everyone welcome " + player.getDisplayName() + " to " + Constants.SERVER_NAME + "!", false);
	}

	@Override
	public void finish() {}

}
