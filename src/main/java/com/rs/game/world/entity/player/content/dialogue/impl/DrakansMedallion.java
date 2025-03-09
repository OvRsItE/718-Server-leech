package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

public class DrakansMedallion extends Dialogue {
	
	

	@Override
	public void start() {
		sendOptionsDialogue("Teleport Options",
				"Burgh de Rott",
				"Meiyerditch Dungeon",
				"Myreque Hideout",
				"Barrows",
				"Darkmeyer");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1 ) {
				Magic.sendDrakanTeleportSpell(player, 0, 0, new Position(3497, 3205, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendDrakanTeleportSpell(player, 0, 0, new Position(3633, 9694, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendDrakanTeleportSpell(player, 0, 0, new Position(3626, 9619, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendDrakanTeleportSpell(player, 0, 0, new Position(3565, 3289, 0));
			} else if (componentId == OPTION_5) {
				Magic.sendDrakanTeleportSpell(player, 0, 0, new Position(3638, 3364, 0));
			}
		}
		 
	}

	@Override
	public void finish() {

	}
}