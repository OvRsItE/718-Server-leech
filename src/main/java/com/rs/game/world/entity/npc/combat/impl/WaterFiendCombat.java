package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class WaterFiendCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 5361 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1) {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			World.sendProjectile(npc, target, 2707, 34, 16, 40, 35, 16, 0);
			if (Utils.getRandom(1) == 0) {
				if (distanceX > size || distanceX < -1 || distanceY > size
						|| distanceY < -1) {
					delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
					npc.animate(new Animation(defs.getAttackEmote()));
			 	}
			}
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			if (Utils.getRandom(1) == 0) {
				if (distanceX > size || distanceX < -1 || distanceY > size
						|| distanceY < -1) {
					delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
					npc.animate(new Animation(defs.getAttackEmote()));
			 	}
			}
		}
		return defs.getAttackDelay();
	}
}