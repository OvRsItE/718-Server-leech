package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class RangeShops extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
		sendOptionsDialogue("Shop Manager", "Ranging Gear", "Ranging Accessories", "Bows & Arrows", "Crossbows & Bolts");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 34);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 35);
			end();
		}
        	if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 36);
			end();
        	}
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 37);
			end();
        }
		} else if (stage == 3) {
			sendOptionsDialogue("Shop Manager", "Herblore shop", "Crafting shop", "Fletching shop", "All-in-one shop");
			stage = 2;
	  }
	}

	@Override
	public void finish() {

	}

}