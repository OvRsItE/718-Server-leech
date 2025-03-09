package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class GrotDungeonAgility extends Dialogue {

	@Override
	public void start() {
		sendDialogue("This shorcut leads to the deepest level of the dungeon. The worms in that area a significantly more dangerous.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Slide down the Worm Burrow?", "Yes.", "No.");
			stage = 1;		
		} else if (stage == 1) {
			if (componentId == OPTION_1)
				player.setNextPosition(new Position(1206, 6507, 0));
			end();
			if (componentId == OPTION_2)
				end();
		}
	}

	@Override
	public void finish() {

	}
}