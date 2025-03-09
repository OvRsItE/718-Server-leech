package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 *@Author Justin
 */

public class Ned2 extends Dialogue {

	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Ah, It's good to be on board a ship again! No matter",
				"how long I live on land, a ship will always seem better.",
				"Are you ready to depart?");
	}
	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, let's go!", 
					"No, I'm not quite ready yet.");
			break;
		case 0:
			if(componentId == OPTION_1) {
				stage = 1;
				sendNPCDialogue(npcId, 9827, "Okay let's go!");
				player.DS = 6;
				break;
			} else if(componentId == OPTION_2) {
				stage = 11;
				break;
			} 
		case 1:
			stage = 11;
			sendNPCDialogue(npcId, 9827, "AHHH THE SHIP HAS CRASHED AND I DON'T HAVE INSURANCE!");
			player.setNextPosition(new Position(2850, 3235, 0));
			break;
			
		case 11:
			end();
			break;
		}
	}

	@Override
	public void finish() {
		
	}
}