package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Mining extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		sendOptionsDialogue("Mining teleport", "Dwarf Mine", "Coal", "LRC", "Lava flow", "more");

	}
	
	public void run(int interfaceId, int componentId) {

	    int option;
		stage = 2;
		if (stage == 2) {
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3020, 9847, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2585, 3478, 0));
			end();
		}
        if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3655, 5114, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2179, 5663, 0));
			end();
        }
		if(componentId == OPTION_5) {
		//stage = 3;
		//sendOptionsDialogue("Mining teleport", "<shad=00FF00>Fishing shop", "<shad=FD3EDA>Woodcutting & Mining Shop", "<shad=05F7FF>Farming shop", "<shad=FFCD05>All-in-one shop");
        }
		} else if (stage == 3) {
			if(componentId == OPTION_1) {

			end();
		}
		if(componentId == OPTION_2) {

			end();
		}
        if(componentId == OPTION_3) {
 
			end();
        }
		if(componentId == OPTION_4) {

			end();
        }
		if(componentId == OPTION_5) {
		stage = 3;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Fishing shop", "<shad=FD3EDA>Crafting shop", "<shad=05F7FF>Fletching shop", "<shad=FFCD05>All-in-one shop");
        }
	  }
	}

	@Override
	public void finish() {

	}

}