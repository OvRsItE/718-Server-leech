package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class Ravager extends NPC {
	
	boolean destroyingObject = false;

	public Ravager(int id, Position tile, int mapAreaNameHash,
                   boolean canBeAttackFromOutOfArea) {
		super(id, tile, -1, 0, false, false);
	}
	
	@Override
	public void processNPC() {
	}

}
