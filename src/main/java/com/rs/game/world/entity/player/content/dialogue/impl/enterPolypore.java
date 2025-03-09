package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;


public class enterPolypore extends Dialogue {

	@Override
	public void start() {
		sendDialogue("<col=8B0000>CAUTION!","You are about to enter the Polypore dungeon, every creature"
				+ " is capped at '50' health points, unless you are using mage.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			sendOptionsDialogue("Enter Dungeon?","Yes.", "No.");
			stage = 2;
		}else
		if (stage == 2) {
			if (componentId == OPTION_1) {
				end();
				player.setNextPosition(new Position(4620, 5457, 3));
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
