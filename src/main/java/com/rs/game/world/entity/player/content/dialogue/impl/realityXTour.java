package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class realityXTour extends Dialogue {

	private int npcId = 945;



	/**
	 *  Handles the first dialogue upon logging in.
	 */

	@Override
	public void start() {
		player.lock();
		sendNPCDialogue(npcId, HAPPY, "What a wonderfull choice you have made to join " + Constants.SERVER_NAME + "!" + " Would you like to go through a short tutorial?");
		stage = 1;
	}

	/**
	 * Asks for a tour before starting, incase player want to cancel.
	 */

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			sendOptionsDialogue("Start Tutorial?", "Yes", "No");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				player.startStages = 1;
				player.getControlerManager().startControler("StarterTutorial");

				end();
			} else if (componentId == OPTION_2) {
				sendNPCDialogue(npcId, HAPPY, "You will find it very simple to get around on here anyways adventurer!" + " You just have to pick a game mod then you're ready!");
				stage = 3;
			}
		}
		if (stage == 3) {
			player.getDialogueManager().startDialogue("newStarter");
		}
	}

	@Override
	public void finish() {

	}
}
