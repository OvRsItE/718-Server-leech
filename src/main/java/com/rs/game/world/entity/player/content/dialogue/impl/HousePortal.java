package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class HousePortal extends Dialogue {

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(int interfaceId, int id) {
		if (stage == 2) {
			if (id == OPTION_1)
				if (player.hasHouse) {
					player.getHouse().enterMyHouse();
			        end();
				}
				else {
					sendDialogue("You don't own a home, please purchase one from an estate agent.");
					stage = 3;
				}
			else if (id == OPTION_2) {
				player.getTemporaryAttributtes().put("viewhouse", true);
				player.getPackets().sendInputNameScript("Whose house do you want to visit?");
				end();
			} else if (id == OPTION_3)
				end();
		} else if(stage == 3)
			end();

	}

	@Override
	public void start() {
		sendOptionsDialogue("Select an option", "Enter my house",
				"Join friends house", "Nevermind");
		stage = 2;
	}

}
