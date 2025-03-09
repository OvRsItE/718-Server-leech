package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class SeaQueen extends CombatScript {


	@Override
	public Object[] getKeys() {
		return new Object[] { 3847 };
	}

	//stand = 3989
	//block = 3990
	//attack = 3991
	// death 3993;

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (target.withinDistance(npc, 1)) {
			npc.animate(new Animation(3991));
			delayHit(npc, 0, target,
					getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		} else {
			specialAttack(npc, target);
		}
		return defs.getAttackDelay();
	}

	public int specialAttack(NPC n, Entity t) {
		final NPCCombatDefinitions defs = n.getCombatDefinitions();
		if (Utils.random(8) == 0) {
			t.getPrayer().drainPrayer(Utils.random(15));
			t.sm("The sea creature drains some of your prayer.");
		}
		n.animate(new Animation(3992));
		World.sendProjectile(n, t, 500, 80, 25, 25, 5, 20, -1);
		delayHit(n, 2, t, getMagicHit(n, getRandomMaxHit(n, defs.getMaxHit(), NPCCombatDefinitions.MAGE, t)));
		return defs.getAttackDelay();
	}
}