package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MTMediumLevelTraining extends Dialogue {
	
	

	@Override
	public void start() {
		sendOptionsDialogue("Medium Level Training",
				"Lumbridge Catacombs",
				"White Wolf Mountain",
				"Experiments",
				"Mountain Trolls",
				"Other");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3246, 3198, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2846, 3497, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3576, 9927, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2875, 3578, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Mediun Level Training - Pg 2",
						"Chaos Druids", 
						"Ardougne Training Camp",
						"None");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1 ) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2928, 9844, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2517, 3356, 0));
			} else if (componentId == OPTION_3) {
				end();
			}
		}
		 
	}

	@Override
	public void finish() {

	}
}