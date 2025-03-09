package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.activities.pestcontrol.PestControl;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class LanderDialouge extends Dialogue {

	@Override
	public void start() {
		sendDialogue("Are you sure you would like to leave the lander?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, get me out of here!", "No, I want to stay.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				player.setNextPosition(PestControl.OUTSIDE_LOBBY);
				player.getControlerManager().forceStop();
			}
			end();
		}
	}

	@Override
	public void finish() {

	}

}
