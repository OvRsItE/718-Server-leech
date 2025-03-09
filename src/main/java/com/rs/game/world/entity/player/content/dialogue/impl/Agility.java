package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

/**
 * @author Justin
 */


public class Agility extends Dialogue {

	public Agility() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Agility Courses", "Gnome Basic/Advanced", "Barbarian Basic/Advanced", "Wilderness Course", "None" );
	}

	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
			if (componentId == OPTION_1) { 
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2468, 3437, 0));
				player.sm("Welcome to the Gnome Agility Course.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2552, 3557, 0));
				player.sm("Welcome to the Barbarian Agility Course.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2998, 3933, 0));
				player.sm("Welcome to the Wilderness Agility Course.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_4) {
			player.sm("Coming soon..");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
	}
	}
		
	}

	@Override
	public void finish() {
		
	}
	
}