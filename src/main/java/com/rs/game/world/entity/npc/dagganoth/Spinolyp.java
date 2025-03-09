package com.rs.game.world.entity.npc.dagganoth;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class Spinolyp extends NPC {

	public Spinolyp(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setNoDistanceCheck(true);
		setCantFollowUnderCombat(true);
		canBeAttackFromOutOfArea = true;
	}
}