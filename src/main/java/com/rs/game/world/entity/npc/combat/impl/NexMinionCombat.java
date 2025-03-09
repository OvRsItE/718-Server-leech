package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Graphics;

public class NexMinionCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Fumus", "Umbra", "Cruor", "Glacies" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextGraphics(new Graphics(1549));
		return defs.getDefenceEmote();
	}
	
}