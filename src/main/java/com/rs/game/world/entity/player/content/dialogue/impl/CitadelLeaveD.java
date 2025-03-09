package com.rs.game.world.entity.player.content.dialogue.impl;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * 
 * @author Josh'
 *
 */
public class CitadelLeaveD extends Dialogue {

	@Override
	public void start() {
		sendPlayerDialogue(9827, "'My time among these clans coming to an end, I returned to the surface world.'");
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Are you sure you would like to leave this Citadel?", "Yes, I wan't to leave.", "No, I wan't to stay!");
			stage = 1;
		}
		else if (stage == 1) {
			end();
		}
		
	}

	@Override
	public void finish() {
		
	}
	
	

}
