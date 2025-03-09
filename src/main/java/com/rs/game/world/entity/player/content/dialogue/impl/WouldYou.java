package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;


public class WouldYou extends Dialogue {
	
	public WouldYou() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Would you like to go to " + Constants.SERVER_NAME + "?", "Yes Please", "No Thanks");
	}


	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2852, 2960, 0));
				player.sm("<col=FF0000>Welcome to " + Constants.SERVER_NAME + " Realityx!");
				player.getInterfaceManager().closeChatBoxInterface();
			} else if(componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		 }
			
		}

	@Override
	public void finish() {

	}
}
