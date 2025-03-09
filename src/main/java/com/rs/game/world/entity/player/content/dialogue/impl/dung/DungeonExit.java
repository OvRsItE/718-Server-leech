package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.DungeonController;

public class DungeonExit extends Dialogue {

	private DungeonController dungeon;

	@Override
	public void start() {
		dungeon = (DungeonController) parameters[0];
		sendOptionsDialogue("Leave the dungeon and return to the surface?", "Yes.", "No.");
		stage = 0;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Leave the dungeon and return to the surface?", "Yes.", "No.");
			stage = 0;
		} else if (stage == 0) {
			if (componentId == OPTION_1)
				dungeon.leaveDungeon();
			end();
		}
	}

	@Override
	public void finish() {

	}

}
