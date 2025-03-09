package com.rs.game.world.entity.npc.slayer;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.others.ConditionalDeath;

@SuppressWarnings("serial")
public class Gargoyle extends ConditionalDeath {
	
	public Gargoyle(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(4162, "The gargoyle cracks apart.", false, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);


	}

}
