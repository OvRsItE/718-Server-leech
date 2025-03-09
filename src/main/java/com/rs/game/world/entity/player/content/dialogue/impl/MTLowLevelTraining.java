package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MTLowLevelTraining extends Dialogue {
	
	@Override
	public void start() {
		sendOptionsDialogue("Training - Page 1", "Cows", "Yaks", "Rock Crabs", "Sand crabs", "-- Next Page --");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) { //Cows
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3259, 3263));
			} else if (componentId == OPTION_2) { //Yaks
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2324, 3798));
			} else if (componentId == OPTION_3) { //Rock crabs
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2680, 3719));
			} else if (componentId == OPTION_4) { //Sand crabs
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1809, 3457));
			} else if (componentId == OPTION_5) { //Next page
				sendOptionsDialogue("Training - Page 2", "Experiments", "Desert Bandits", "-- Back to Beginning --");
				stage = 0;
			}
		} else if (stage == 0) {
			if (componentId == OPTION_1) { //Experiments
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3559, 9947));
			} else if (componentId == OPTION_2) { //Desert bandits
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3163, 2984));
			} else if (componentId == OPTION_3) { //Beginning
				sendOptionsDialogue("Training - Page 1", "Cows", "Yaks", "Rock Crabs", "Experiments", "-- Next Page --");
				stage = -1;
			}
		}
	}

	@Override
	public void finish() {

	}
}