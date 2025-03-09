package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class marry extends Dialogue {
	
	private Player usedOn;

	@Override
	public void start() {
		usedOn = (Player) parameters[0];
		sendOptionsDialogue(player.getUsername() +"Has just asked if you will marry them!",
				"Oh my god yes! I love them",
				"No thank youm I'm not ready");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
		case OPTION_1:
		//todo
			break;
		default:
			end();
			break;
		}
	}


	@Override
	public void finish() {
	}

}