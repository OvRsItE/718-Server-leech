package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class XPRingCharges extends Dialogue {
	
	
	@Override
	public void start() {
		sendItemDialogueNoContinue(player, 29997, 1, "There is "+player.xpRingCharges+" charges left.");
		stage = 1;
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			end();
		}
	}

	@Override
	public void finish() {

	}

}