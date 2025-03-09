package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class PestPortal extends NPC {

	public PestPortal(int id, Position tile) {
		super(id, tile, -1, 0, true, true);
		setCantFollowUnderCombat(true);
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		cancelFaceEntityNoCheck();
	}

	@Override
	public void sendDeath(Entity killer) {
		resetWalkSteps();
		getCombat().removeTarget();
		super.sendDeath(killer);
	}
}