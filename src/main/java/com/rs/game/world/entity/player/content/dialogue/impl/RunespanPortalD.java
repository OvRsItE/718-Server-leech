package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.RunespanControler;

public class RunespanPortalD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Where would you like to travel to?",
				"The Runecrafting Guild",
				"Low level entrance into the Runespan",
				"High level entrance into the Runespan");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				player.setNextPosition(new Position(1696, 5460, 2));
				end();
			} else{
				RunespanControler.enterRunespan(player, componentId == OPTION_3);
				end();
			} 
		}

	}

	@Override
	public void finish() {

	}

}
