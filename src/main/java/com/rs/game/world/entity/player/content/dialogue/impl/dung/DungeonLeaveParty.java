package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class DungeonLeaveParty extends Dialogue {

	@Override
	public void start() {
		//sendDialogue("Warning: If you leave the dungeon, you will not be able to return to it!");$
		sendOptionsDialogue("Leave the dungeon for good?", "Yes.", "No.");
		stage = 0;

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Leave the dungeon for good?", "Yes.", "No.");
			stage = 0;
		} else if (stage == 0) {
			if (componentId == OPTION_1)
				player.getDungeoneeringManager().leaveParty();
			end();
		}
	}

	@Override
	public void finish() {

	}

}
