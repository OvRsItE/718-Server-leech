package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class SettingsOptions extends Dialogue {

	
	/**
	 * Sends options
	 */
	@Override
	public void start() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Account Messages", "World Messages", "Switch Items Look", "Close");
		stage = 1;
	}
	
	public void backPlayer() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Toggle 99's Messages", "Toggle Drop Messages", "Toggle Guide Messages", "Toggle Event Messages");
		stage = 3;
	}
	public void backWorld() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Toggle Login Messages", "Toggle Yell Messages", "Toggle Level Message", "Back");
		stage = 2;
	}
	
	/**
	 * Handles the stages
	 */

	@Override
	public void run(int interfaceId, int componentId) {
		/**
		 * Handles stage 1 (first options)
		 */
		if (stage == 1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Toggle Login Messages", "Toggle Yell Messages", "Toggle Level Message", "Back");
				stage = 2;
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Toggle 99's Messages", "Toggle Drop Messages", "Toggle Guide Messages", "Toggle Event Messages");
				stage = 3;
			} else if (componentId == OPTION_3) {
				player.switchItemsLook();
				sendDialogue("You are now playing with " + (player.isOldItemsLook() ? "old" : "new") + " item looks.");
				stage = 1;
			} else if (componentId == OPTION_4) {
				end();
			}
	} else if (stage == 2) {
			if (componentId == OPTION_1) {
				player.setToggleLoginMsg(player.hasToggledLoginMsg() ? false : true);
				player.sm("You have "+(player.hasToggledYellMsg() ? "<col=8B0000>DISABLED</col>" : "<col=006600>ENABLED</col>")+" yell chat!");
				end();
			} else if (componentId == OPTION_2) {
				player.setToggleYellMsg(player.hasToggledYellMsg() ? false : true);
				sendDialogue("You have "+(player.hasToggledYellMsg() ? "DISABLED" : "ENABLED")+" yell chat!");
				end();
			} else if (componentId == OPTION_3) {
				player.setToggleLvLMsg(player.hasToggledLvLMsg() ? false : true);
				player.sm("You have "+(player.hasToggledLvLMsg() ? "<col=8B0000>DISABLED</col>" : "<col=006600>ENABLED</col>")+" level pop-up notifications!");		
				end();
			} else if (componentId == OPTION_4) {
				start();
			}
		} else if (stage == 3) {
				if (componentId == OPTION_1) {
					player.setToggleMaxMsg(player.hasToggledMaxMsg() ? false : true);
					player.sm("You have "+(player.hasToggledMaxMsg() ? "<col=8B0000>DISABLED</col>" : "<col=006600>ENABLED</col>")+" 99 global notifications!");
					end();
			} else if (componentId == OPTION_2) {
					player.setToggleDropMsg(player.hasToggledDropMsg() ? false : true);
					player.sm("You have "+(player.hasToggledDropMsg() ? "<col=8B0000>DISABLED</col>" : "<col=006600>ENABLED</col>")+" global drop notifications!");
					end();
			} else if (componentId == OPTION_3) {
					player.setToggleGuideMsg(player.hasToggledGuideMsg() ? false : true);
					player.sm("You have "+(player.hasToggledGuideMsg() ? "<col=8B0000>DISABLED</col>" : "<col=006600>ENABLED</col>")+" guide messages!");
					end();
			} else if (componentId == OPTION_4) {
					player.setToggleEventMsg(player.hasToggledEventMsg() ? false : true);
					player.sm("You have "+(player.hasToggledEventMsg() ? "<col=8B0000>DISABLED</col>" : "<col=006600>ENABLED</col>")+" event notifications!");
					end();
		
			}
		}
			
	}

	@Override
	public void finish() {

	}

}
