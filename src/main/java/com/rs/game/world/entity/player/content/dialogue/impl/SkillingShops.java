package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class SkillingShops extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Pick a store.", "Herblore Items", "Smithing", "Crafting", "Mining", "Next page");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 7);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 25);
			end();
		}
        if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 33);
			end();
        }
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 28);
			end();
        }
		if(componentId == OPTION_5) {
		stage = 3;
		sendOptionsDialogue("Pick a store.", "Woodcutting", "Runecrafting", "Construction", "Hunter", "Cooking");
        }
		} else if (stage == 3) {
			if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 27);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 29);
			end();
		}
        if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 18);
			end();
        }
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 30);
			end();
        }
		if(componentId == OPTION_5) {
			ShopsHandler.openShop(player, 39);
			end();
        }
	 }
}
	

	@Override
	public void finish() {

	}

}