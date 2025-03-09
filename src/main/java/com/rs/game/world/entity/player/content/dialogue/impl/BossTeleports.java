package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class BossTeleports extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
		    sendOptionsDialogue("PvM Teleports", "Godwars", "Skeletal Wyvern ", "Frost Dragons", "More Options...");
			stage = 1; 
		}
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
	    int option;
		if(componentId == OPTION_1) {
            Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2916, 3746, 0));
			end();
		}
		if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3035, 9554, 0));
			end();
		}
        if(componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2894, 3895, 0));
			end();
		}
		if(componentId == OPTION_4) {

		}
		if(componentId == OPTION_5) {
		    stage = 2;
		    sendOptionsDialogue("Pvm Teleports", "King Black Dragon", "Tormented Demons", "Queen Black Dragon", "Corporeal Beast", "Back...");
		}
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3067, 10254, 0));
			player.getPackets().sendGameMessage("Careful, make sure to have an anti-dragon shield, you're going to need it!");
			end();
		}
		if(componentId == OPTION_2) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2562, 5739, 0));
			end();
		}
        	if(componentId == OPTION_3) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1197, 6499, 0));
			end();
		}
		if(componentId == OPTION_4) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2966, 4383, 2));
			end();
		}
		if(componentId == OPTION_5) {
		    stage = 1; 
		   sendOptionsDialogue("Pvm Teleports", "Godwars", "Skeletal Wyvern ", "Frost Dragons", "Null", "More Options...");
		}
	  }
	}

	@Override
	public void finish() {

	}

}