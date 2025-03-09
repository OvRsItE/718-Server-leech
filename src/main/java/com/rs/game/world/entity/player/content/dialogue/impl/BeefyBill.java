package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

/**
 *@Author Justin
 */

public class BeefyBill extends Dialogue {

	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello, what are you doing here?");
	}
	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 0;
			sendPlayerDialogue(9827, "I'm looking to buy some new things, do you have anything for sale?");
			break;
		case 0:
			stage = 1;
			sendPlayerDialogue(9827, "Yes, I have a shop, would you like to view it?");
			break;
		case 1:
			stage = 2;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes", 
					"No");
			break;
		case 2:
			if(componentId == OPTION_1) {
				end();

				break;
			} else if(componentId == OPTION_2) {
				end();
				break;
			}
		}
	}

	@Override
	public void finish() {
		
	}
}