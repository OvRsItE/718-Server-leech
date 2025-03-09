package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Woodcutting extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		sendOptionsDialogue("Woodcutting teleport", "Lumby", "Draynor", "Seers", "Magics", "Ivy");
	}
	
	public void run(int interfaceId, int componentId) {

	    int option;
		stage = 2;
		if (stage == 2) {
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3194, 3229, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3091, 3235, 0));
			end();
		}
        if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2723, 3468, 0));
			end();
        }
		if(componentId == OPTION_4) {
        	Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2702, 3392, 0));
			end();
        }
		if(componentId == OPTION_5) {
		 Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3216, 3501, 0));
			end();
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