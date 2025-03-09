package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public final class FirstLoggin extends Dialogue {
	
	
	private int npcId = 10;

	@Override
	public void start() {
		sendNPCDialogue(npcId, NORMAL, "Hello "+player.getDisplayName()+", what can i do for you?");
			stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		/**
		 *  Gives the player 3 Options.
		 */
		if (stage == 1) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "What do you sell?", "Nothing.");
			stage = 2;
		} 
		
		
		/**Handles the Options Options.
		 */
		
		else if (stage == 2) {
			if (componentId == OPTION_1) {
				ShopsHandler.openShop(player, 150);
				end();
			} else if (componentId == OPTION_2) {
				sendPlayerDialogue(NORMAL, "Nothing anyway.");
				stage = 4;
			} 
			
		} else if (stage == 4) {
				end();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
}
