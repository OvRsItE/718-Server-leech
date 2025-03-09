package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class AgilityTeleports extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
		    sendOptionsDialogue("Agility Teleports", "Gnome Agility", "Barbarian Agility");
			stage = 1; 
		}
	}
	
	@SuppressWarnings("unused")
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
	    int option;
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2480, 3437, 0));
			end();
		}
		if(componentId == OPTION_2) {
		    Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2552, 3557, 0));
			end();
		}
	  }
	}

	@Override
	public void finish() {

	}

}