package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.toxin.ToxinType;

@SuppressWarnings("serial")
public class Bruce extends NPC {

	public Bruce(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		setForceMultiArea(true);
	}


}
