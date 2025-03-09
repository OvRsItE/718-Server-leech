package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;


public class wildyWarn extends Dialogue {

	@Override
	public void start() {
		sendDialogue("<col=8B0000>WARNING!","You are about to use a teleport that takes you"
				+ " to the wilderness, are you Sure you want to continue?");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			sendOptionsDialogue("Select a option","Yes.", "No.");
			stage = 2;
		}else
		if (stage == 2) {
			if (componentId == OPTION_1) {
				end();
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3148, 3874, 0));
			}else
			if (componentId == OPTION_2) {
				end();
			}
		}
	
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
}
