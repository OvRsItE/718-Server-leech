package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class CollectGEItems extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Take Items", "Return");
		stage = 0;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			/*if (componentId == OPTION_1) {
				Offer offer = (Offer) parameters[0];
				if (offer == null)
					return;

				// TODO need to make this again from scratch.
			} else if (componentId == OPTION_2) {
				end();
			}*/
		}
	}

	@Override
	public void finish() {

	}

}
