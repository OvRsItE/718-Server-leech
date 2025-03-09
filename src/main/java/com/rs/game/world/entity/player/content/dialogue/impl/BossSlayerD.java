package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class BossSlayerD extends Dialogue {

	private static final int DEATH = 14386;
	
	@Override
	public void start() {
		sendNPCDialogue(DEATH, NORMAL, "Hello my child, what do you need?");
		stage = 0;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, (player.getBossSlayer().getTask() != null ? "Check task" : "Get task"), "Skip Task", "Open Shop");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				if (player.getBossSlayer().getTask() != null) {
					sendNPCDialogue(DEATH, NORMAL, player.getBossSlayer().getTaskMessage());
				} else {
					player.getBossSlayer().assignTask();
					sendNPCDialogue(DEATH, NORMAL, "Your new task is to bring me the souls of " + 
					player.getBossSlayer().getKillsRemaining() + " " + player.getBossSlayer().getTask().getName() + "'s.");
				}
				stage = 2;
			} else if (componentId == OPTION_2) {
				if (player.getBossSlayer().getTask() == null) {
					sendNPCDialogue(DEATH, NORMAL, "You don't have a task to skip child, please begone.");
					stage = 2;
				} else {
					player.getBossSlayer().skipTask();
				}
			} else if (componentId == OPTION_3) {
				ShopsHandler.openShop(player, 666);
				end();
			}
		} else if (stage == 2) {
			end();
		}	
	}

	@Override
	public void finish() {}

}
