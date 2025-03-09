package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class MentiosManager extends NPC {

	public MentiosManager(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		// setCombatLevel(1337);
		setName("Rune-Evo Manager");
	}
}