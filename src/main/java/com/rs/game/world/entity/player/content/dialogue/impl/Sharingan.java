package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

public class Sharingan extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Sharingan!" );
		player.applyHit(new Hit(player, player.getHitpoints(),
				HitLook.REGULAR_DAMAGE));
	}

	@Override
	public void run(int interfaceId, int componentId) {
		
	}

	@Override
	public void finish() {

	}

}
