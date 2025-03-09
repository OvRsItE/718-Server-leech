package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;


public class SmokeDungeonEntrance extends Dialogue {
	
		
	
	/*FaceProtection = 4164, 13277, 13263, 14636, 14637, 15492, 
							15496, 15497, 22528, 22530, 22532, 22534,
							22536, 22538, 22540, 22542, 22544, 22546,
							22548, 22550;*/

	@Override
	public void start() {
		
		
		sendOptionsDialogue("Warning! You'll take damage without face protection.",
				"Proceed anyway",
				"Stay out");
		stage = -1;
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1 ) {
				player.setNextPosition(new Position(3206, 9379, 0));
				end();
			} else if (componentId == OPTION_2) {
				end();
			}
		}
		 
	}

	@Override
	public void finish() {

	}
}
