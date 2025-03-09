package com.rs.game.world.entity.player.content.activities.trollinvasion;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;


public class TrollWizard extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 12435 };

	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		npc.animate(new Animation(1938));
		hit = getRandomMaxHit(npc, 300, NPCCombatDefinitions.MELEE, target);
		delayHit(npc, 2, target, getMeleeHit(npc, hit));
		return defs.getAttackDelay();
	}

}
