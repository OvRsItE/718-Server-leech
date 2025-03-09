package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class DonatorShops extends Dialogue {

	private int npcId;

	@Override
	public void start() {
	
		sendOptionsDialogue("Donator Shops",
				"Donator Shops", "Exclusive Shops",
				"None");
		stage = 1;

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) { //General Store
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);

			} else if (componentId == OPTION_2) {//Combat
				sendOptionsDialogue("Exclusive Shops", "Potion Flasks",
						"Steel Titan");
				stage = 2;
			} else if (componentId == OPTION_3) {//Skilling
				end();
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) { //Flasks
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);

			} else if (componentId == OPTION_2) {//Steel titan scrolls
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);

			}
		}
			
			
			/**
			 * 
			 */
			
			
		
	}
			@Override
			public void finish() {
			}
	

}