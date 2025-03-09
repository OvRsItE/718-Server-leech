package com.rs.game.world.entity.npc.SoulWars;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class Avatars extends NPC{

	public Avatars(int id, Position tile, int mapAreaNameHash,
                   boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setRun(true);
		setForceMultiAttacked(true);
		setLureDelay(0);
	}
}
