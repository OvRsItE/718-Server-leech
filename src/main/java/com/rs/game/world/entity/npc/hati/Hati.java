package com.rs.game.world.entity.npc.hati;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class Hati extends NPC {

	public Hati(int id, Position tile, int mapAreaNameHash,
                boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
	}

}
