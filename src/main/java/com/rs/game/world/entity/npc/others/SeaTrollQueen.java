package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class SeaTrollQueen extends NPC {


	public SeaTrollQueen(int id, Position tile, int mapAreaNameHash,
                         boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setDamageCap(1000);
		setNoDistanceCheck(true);
		setCantFollowUnderCombat(true);
	}


	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead()) {
			return;
		}
		int maxhp = getMaxHitpoints();
		if (maxhp > getHitpoints() && getPossibleTargets().isEmpty()) {
			setHitpoints(maxhp);
		}
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.3;
	}

}
