package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;

public class KrakenCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 16010,16144 };
	}
	// stand = 3989
	// block = 3990
	// attack = 3991
	// death 3993;

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions definition = npc.getCombatDefinitions();
		npc.animate(new Animation(definition.getAttackEmote()));
		World.sendProjectile(npc, target, 2706, 80, 25, 25, 3, 20, 0);
		delayHit(npc, 2, target, getMagicHit(npc, getRandomMaxHit(npc, definition.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
		return definition.getAttackDelay();
	}


}