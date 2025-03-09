package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

/**
 * @author Justin
 */


public class Stryke extends Dialogue {

	public Stryke() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Strykewyrms", "Ice Strykewyrms", "Desert Strykewyrms", "Jungle Strykewyrms" );
	}

	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
		if(componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3424, 5667, 0));
			player.sm("<col=FF0000>Welcome to the Ice Strykwyrms!");
			player.getInterfaceManager().closeChatBoxInterface();
			
		} else if(componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3373, 3160, 0));
			player.sm("<col=FF0000>Welcome to the Desert Strykwyrms!");
			player.getInterfaceManager().closeChatBoxInterface();
			
		} else if(componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2521, 2845, 0));
			player.sm("<col=FF0000>Welcome to the Jungle Strykwyrms!");
			player.getInterfaceManager().closeChatBoxInterface();
		}
	 }
		
	}

	@Override
	public void finish() {
		
	}
	
}