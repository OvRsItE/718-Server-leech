package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.activities.ZarosGodwars;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public final class NexEntrance extends Dialogue {

	@Override
	public void start() {
		sendDialogue(
				"The room beyond this point is a prison!",
				"There is no way out other than death or teleport.",
				"Only those who endure dangerous encounters should proceed.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(
					"There are currently " + ZarosGodwars.getPlayersCount()
							+ " people fighting",
					"Enter Prison.", "Stay here.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				player.setNextPosition(new Position(2911, 5203, 0));
				player.getControlerManager().startControler("ZGDControler");
			}
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
