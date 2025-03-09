package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class DungeoneeringStartD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Would you like to start the dungeon?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1)
			player.getDungeoneeringManager().enterDungeon(false, true, true);
		end();
	}

	@Override
	public void finish() {
		
	}

}
