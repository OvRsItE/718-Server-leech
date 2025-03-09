package com.rs.game.world.entity.npc.wilderness;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.toxin.ToxinType;

@SuppressWarnings("serial")
public class Venenatis extends NPC {

	public Venenatis(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		setForceMultiArea(true);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.2;
	}

}
