package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.utility.Utils;

public class Cape extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option.", "I would like to see the requirements.",
				"I would like the completionist cape please.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.getInterfaceManager().sendCompCape();
		}
		if (componentId == OPTION_2) {
			player.completedCompletionistCape();
			if (!player.isCompletedComp()) {
				player.setNextForceTalk(new ForceTalk("Because im so not worth it!"));
				player.animate(new Animation(767));
			} else if (player.getInventory().containsItem(995, 10000000) && player.isCompletedComp()) {
				player.getInventory().deleteItem(995, 10000000);
				World.sendWorldMessage(Utils.formatPlayerNameForDisplay(player.getUsername())
						+ "<col=800000> Has just achieved the Completionists cape!" + "</col> ", false);
				player.getInventory().addItem(20769, 1);
				player.getInventory().addItem(20770, 1);
			}
			end();
		} else if (componentId == OPTION_3) {
			end();
		}
		end();

	}

	public void sql() {

	}

	@Override
	public void finish() {

	}

}
