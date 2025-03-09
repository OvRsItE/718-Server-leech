package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeon;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class EliteDungeonEnter extends Dialogue {

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(int interfaceId, int id) {
		if (stage == 2) {
			if (id == OPTION_1) {
				EliteDungeon.launch(player, null);
				player.closeInterfaces();
				end();
			} else if (id == OPTION_2) {
				player.getTemporaryAttributtes().put("EliteDungeon", true);
				player.getPackets().sendInputNameScript("Whos elite dungeon do you want to join?");
				player.closeInterfaces();
				end();
			} else if (id == OPTION_3)
				end();
			else if (stage == 3)
				end();

		}
	}

	@Override
	public void start() {
		sendOptionsDialogue("Select an option", "Enter dungeon",
				"Join elite dungeon", "Nevermind");
		stage = 2;
	}

}
