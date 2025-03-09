package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MinigameTeleports extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Page - Page 1", "Dominion Tower", "Fight Caves", "Barrows", "Duel Arena", "-- Next Page --");
		stage = 0;
	}

	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3373, 3090, 0));
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4613, 5129, 0));
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3563, 3288, 0));
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3365, 3275, 0));
			}
			if (componentId == OPTION_5) {
				sendOptionsDialogue("Page - Page 2", "Fight kiln", "Pest Control", "Nevermind");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4743, 5170, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2658, 2660, 0));
			} else if (componentId == OPTION_3) {
				end();
			}
		}
	}

	@Override
	public void finish() {}

}