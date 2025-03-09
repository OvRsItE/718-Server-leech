package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;


public class ShadowSpiderCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Shadow Spider", 58};
	}

	@Override
	public int attack(NPC npc, Entity target) {//Shadow Spider prayer drain
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = 0;
		
		npc.animate(new Animation(defs.getAttackEmote()));
		damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target);
		if (target instanceof Player)
			((Player) target).getPrayer().drainPrayer(495);
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		return defs.getAttackDelay();
	}
}
