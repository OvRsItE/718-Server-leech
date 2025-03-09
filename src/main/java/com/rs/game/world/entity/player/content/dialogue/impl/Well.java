package com.rs.game.world.entity.player.content.dialogue.impl;



import com.rs.game.world.entity.player.content.WellOfFortune;
import com.rs.game.world.entity.player.content.XPWell;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;



public class Well extends Dialogue {
	

	
	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Donate to", "The XP Well", "The Drop Well");
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if(componentId == OPTION_1) {
			XPWell.give(player);	
			end();
		}
			if(componentId == OPTION_2) {
			player.sm("This feature is being reworked, and is currently disabled.");
			end();
		}
		}
	}
		
	
	@Override
	public void finish() {
		
		}
	}
