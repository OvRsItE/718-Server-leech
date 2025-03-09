package com.rs.game.world.entity.player.content.activities;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class ZombieMonk extends Dialogue {

	@Override
	public void start() {
		stage = 2;
		sendOptionsDialogue("What would you like to do?", "Enter Zombies ritual.", "Nothing");
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			if (componentId == OPTION_1)
				Zombies.enterZombies(player);
			end();
			}
		if (componentId == OPTION_2 ) {
			end();
		}
		}
		
	

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
