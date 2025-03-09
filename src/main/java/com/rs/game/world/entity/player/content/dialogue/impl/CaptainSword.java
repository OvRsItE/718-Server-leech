package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class CaptainSword extends Dialogue {

	@Override
	public void finish() {
		//end();
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
        if(stage == 1 && componentId == OPTION_2) {
		   end();
		   player.getDialogueManager().startDialogue("Citadel");
        } else if(stage ==1 && componentId == OPTION_3) {
        	end();
		} else if(stage == 1 && componentId == OPTION_1) {
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] {NPCDefinitions.getNPCDefinitions(4657).name,
					"A captain's sword? I remember those I have several of them,",
					"I will spare you one for 100m"}, 
					(byte)1, 4657, 9850);
		stage = 3;
		} else if(stage == 3) {
			sendOptionsDialogue("Select an Option",
					"Sure", "No way");
			stage = 4;
		} else if(stage == 4 && componentId == OPTION_2) {
			end();
		} else if(stage == 4 && componentId == OPTION_1) {
			if(!player.getInventory().containsItem(995, 100000000)) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] {NPCDefinitions.getNPCDefinitions(4657).name,
							"You don't have enough money in your inventory."}, 
							(byte)1, 4657, 9850);
				stage = 100;
				return;
			}
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {NPCDefinitions.getNPCDefinitions(4657).name,
						"Pleasure doing sevice, sucker"}, 
						(byte)1, 4657, 9850);
			   player.getInventory().deleteItem(995, 100000000);
			   player.getInventory().addItemMoneyPouch(24187, 1);
			   end();
		} else if(stage == 100) {
			end();
		}
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Select an Option",
				"I'd like to have another captain's sword", "Ask about Citadels", "Nevermind");
	}
}
