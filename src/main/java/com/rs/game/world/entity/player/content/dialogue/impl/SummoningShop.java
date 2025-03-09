package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class SummoningShop extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT, new String[]{
		NPCDefinitions.getNPCDefinitions(6970).name, "Would you like to see my shops?" }
		, IS_NPC, 6970, 9847);
	}
	
	public void run(int interfaceId, int componentId) {
	    int option;
		if (stage == 1) {
			sendOptionsDialogue("Choose a shop!", "Starter Ingredients", "Intermediate Ingredients", "Master Ingredients");
			stage = 2;
        } else if (stage == 2) {
			if(componentId == OPTION_1) {
		       ShopsHandler.openShop(player, 46);
			   end();
		    }
			if(componentId == OPTION_2) {
		       ShopsHandler.openShop(player, 47);
			   end();
		    }
			if(componentId == OPTION_3) {
		       ShopsHandler.openShop(player, 48);
			   end();
		    }
		  }
	}

	@Override
	public void finish() {

	}

}