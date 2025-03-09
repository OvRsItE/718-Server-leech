package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.activities.Flower;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class FlowerGame extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option",
				"Pick the flowers.", "Leave the flowers.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1 && componentId == OPTION_1) {
				Flower.PickFlowers();
				end();
				return;
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}
}