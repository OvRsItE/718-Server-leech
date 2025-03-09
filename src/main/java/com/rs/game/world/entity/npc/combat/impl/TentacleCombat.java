package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;

public class TentacleCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 16013 };
	}
	// stand = 3989
	// block = 3990
	// attack = 3991
	// death 3993;

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions definition = npc.getCombatDefinitions();
		npc.animate(new Animation(definition.getAttackEmote()));
		delayHit(npc, 2, target, getMagicHit(npc, getRandomMaxHit(npc, definition.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
		World.sendProjectile(npc, target, 2704, 80, 25, 25, 5, 20, -1);
		return definition.getAttackDelay();
	}

}