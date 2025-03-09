package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class SpiritKalphiteCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6995, 6994 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (usingSpecial) {// TODO find special
			npc.animate(new Animation(8519));
			npc.setNextGraphics(new Graphics(8519));
			damage = getRandomMaxHit(npc, 20, NPCCombatDefinitions.MELEE,
					target);
			delayHit(npc, 1, target, getMeleeHit(npc, damage));
		} else {
			npc.animate(new Animation(8519));
			damage = getRandomMaxHit(npc, 50, NPCCombatDefinitions.MELEE,
					target);
			delayHit(npc, 1, target, getMeleeHit(npc, damage));
		}
		return defs.getAttackDelay();
	}
}
