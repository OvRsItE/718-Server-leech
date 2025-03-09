package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

public class FlambeedCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3494 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		@SuppressWarnings("unused")
		final Player player = (Player) target;
		npc.animate(new Animation(1750));
		delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 250, NPCCombatDefinitions.MELEE, target)));
		return defs.getAttackDelay();
	}
	
}