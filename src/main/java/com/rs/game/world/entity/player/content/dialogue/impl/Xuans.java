package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class Xuans extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hey " + player.getUsername() + ", Im Xuan. Im here to sell auras for Loyalty Points, im just telling you this to let you know :) Well what would you like to ask?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Xuan", "Show me your Shop", "How much points do i have?", "How do i get Loyalty Points?");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 53);
			end();
		}
		if(componentId == OPTION_2) {
		    sendNPCDialogue(npcId, 9827, "You currently have " + player.getLoyaltyPoints() + " Loyalty Points." );
			stage = 3;
		}
        if(componentId == OPTION_3) {
		    sendNPCDialogue(npcId, 9827, "The only way to get Loyalty Points is by playing " + Constants.SERVER_NAME + " for 30 minutes." );
			stage = 3;
		}
		} else if (stage == 3) {
		end();
	  }
	}

	@Override
	public void finish() {

	}

}