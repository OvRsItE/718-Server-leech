package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

/**
 * @author Justin
 */


public class IronmanShop extends Dialogue {

	public IronmanShop() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Ironman Shops", "Ironman shop 1", "Ironman shop 2" ,"None");
	}

	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 110);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 163);
			player.getInterfaceManager().closeChatBoxInterface();
		} else if(componentId == OPTION_3) {
			player.getInterfaceManager().closeChatBoxInterface();
		}
	 }
		
	}

	@Override
	public void finish() {
		
	}
	
}