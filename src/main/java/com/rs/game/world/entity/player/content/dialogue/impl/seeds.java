package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class seeds extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		//npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Farming Shops", "Farming Tools", "Fruit/Wood Trees", "Allotment/Herb/Flower", "Hop/Bush/Misc", "None");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 49);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 50);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_3) {
			ShopsHandler.openShop(player, 51);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_4) {
			ShopsHandler.openShop(player, 52);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_5) {
			player.getInterfaceManager().closeChatBoxInterface();
		}
		} else if (stage == 3) {
			sendOptionsDialogue("Shop Manager", "<shad=00FF00>Herblore shop", "<shad=FD3EDA>Crafting shop", "<shad=05F7FF>Fletching shop", "<shad=FFCD05>All-in-one shop");
			stage = 2;
	  }
	}

	@Override
	public void finish() {

	}

}