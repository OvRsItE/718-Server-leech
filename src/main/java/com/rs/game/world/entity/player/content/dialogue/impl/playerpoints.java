package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class playerpoints extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", What store would you like to access?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>PvP Store", "<shad=FD3EDA>PvM", "<shad=00FF00>Trivia Shop", "<shad=FD3EDA>Vote", "How many PvM Points do I have?");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 10);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 19);
			end();
		}if(componentId == OPTION_3) {
		    ShopsHandler.openShop(player, 65);
			end();
		}if(componentId == OPTION_4) {
		    ShopsHandler.openShop(player, 54);
			end();
		}if(componentId == OPTION_5) {
		    sendNPCDialogue(npcId, 9827, "You currently have " + player.getPvmPoints() + " PVM Points." );
			stage = 3;
		}
		} 
	}

	@Override
	public void finish() {

	}

}