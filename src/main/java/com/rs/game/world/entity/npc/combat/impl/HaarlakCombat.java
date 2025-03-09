package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class HaarlakCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 9911 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(1) == 0) { // mage attack
			npc.setDamageCap(1000);
			npc.animate(new Animation(14371));
			npc.setNextForceTalk(new ForceTalk("Feel the Pain of Water!"));
			for (Entity t : npc.getPossibleTargets()) {
				if (!t.withinDistance(npc, 18))
					continue;
				int damage = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MAGE, t);
				if (damage > 0) {
					delayHit(npc, 1, t, getMagicHit(npc, damage));
					World.sendProjectile(npc, t, 500, 50, 16, 41, 35, 16, 0);
					t.setNextGraphics(new Graphics(502));
				}
			}

		} else { // melee attack
			npc.animate(new Animation(defs.getAttackEmote()));
			npc.setDamageCap(1000);
			target.setNextGraphics(new Graphics(556));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}