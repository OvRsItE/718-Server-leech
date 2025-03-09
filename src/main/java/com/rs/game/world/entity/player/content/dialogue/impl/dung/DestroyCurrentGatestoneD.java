package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.DungeonController;

public class DestroyCurrentGatestoneD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue( "Are you sure you'd like to destroy your current gatestone?",
				"Yes, I'm sure", "No, I'll keep it.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getControlerManager().getControler() instanceof DungeonController)
				((DungeonController) player.getControlerManager().getControler()).createGatestone(true);
		}
		end();
	}

	@Override
	public void finish() {
		
	}

}
