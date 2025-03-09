package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

/**
 * @author Justin
 */


public class FarmingShop extends Dialogue {

	public FarmingShop() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Farming Shops", "Farming Tools", "Fruit/Wood Trees", "Allotment/Herb/Flower", "Hop/Bush/Misc", "None");
	}

	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
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
	 }
		
	}

	@Override
	public void finish() {
		
	}
	
}