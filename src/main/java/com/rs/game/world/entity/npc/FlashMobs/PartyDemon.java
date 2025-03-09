package com.rs.game.world.entity.npc.FlashMobs;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class PartyDemon extends NPC {

	public PartyDemon(int id, Position tile, int mapAreaNameHash,
                      boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setForceMultiAttacked(true);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.5;
	}

}