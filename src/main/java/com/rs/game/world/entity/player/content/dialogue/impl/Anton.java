package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class Anton extends Dialogue {

	private int npcId = 4295;

	@Override
	public void start() {
		sendNPCDialogue(npcId, NORMAL, "Ahhhh, hello there. How can I help?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		stage++;
		if (stage == 0)
			sendPlayerDialogue(NORMAL, "Looks like you have a good selection of weapons around here...");
		else if (stage == 1)
			sendNPCDialogue(npcId, NORMAL,
					"Indeed so, specially imported from the finest smiths around the lands, take a look at my wares.");
		else if (stage == 2) {
			end();
		}
	}

	@Override
	public void finish() {

	}
}
