package com.rs.game.world.entity.player.content.activities.trollinvasion;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;

public class Dynamite extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Dynamite" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		hit = getRandomMaxHit(npc, 200, NPCCombatDefinitions.MAGE, target);

		delayHit(npc, 2, target, getMagicHit(npc, hit));

		return defs.getAttackDelay();
	}

}
