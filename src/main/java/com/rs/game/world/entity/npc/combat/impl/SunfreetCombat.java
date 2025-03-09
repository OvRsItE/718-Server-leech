package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * 
 * @author Jae
 *
 */

public class SunfreetCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15222 };
	}


	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(1) == 0) {
			switch (Utils.getRandom(10)) {
			case 0:
				npc.animate(new Animation(16317));
			for (Entity t : npc.getPossibleTargets()) {
				if (!t.withinDistance(npc, 18))
					continue;
				int damage = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MAGE, t) / 2;
				if (damage > 0) {
					delayHit(npc, 1, t, getMagicHit(npc, damage));
					//delayHit(npc, 1, t, getMagicHit(npc, damage));
					t.setNextGraphics(new Graphics (3002));
					t.setNextGraphics(new Graphics (3003));
					t.setNextGraphics(new Graphics (3005));
					npc.animate(new Animation(16317));
				}
			}
			npc.animate(new Animation(16318));
			break;
		case 1:
			npc.animate(new Animation(16315));
			for (Entity t : npc.getPossibleTargets()) {
				if (!t.withinDistance(npc, 18))
					continue;
				int damage = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MAGE, t) * 2 + 100;
				if (damage > 0) {
					World.sendProjectile(npc, target, 3004, 60, 32, 50, 50, 0, 0);
					delayHit(npc, 1, t, getMagicHit(npc, damage));
					//delayHit(npc, 1, t, getMagicHit(npc, damage));
					t.setNextGraphics(new Graphics (3002));
					t.setNextGraphics(new Graphics (3003));
					t.setNextGraphics(new Graphics (3005));
				}
			}
			break;
		case 2:
			npc.animate(new Animation(defs.getAttackEmote()));
			npc.setDamageCap(800);
			delayHit(npc,0,target,getMeleeHit(npc,getRandomMaxHit(npc, defs.getMaxHit(),NPCCombatDefinitions.MELEE, target)));
			break;
		case 3:
			npc.animate(new Animation(defs.getAttackEmote()));
			npc.setDamageCap(800);
			delayHit(npc,0,target,getMeleeHit(npc,getRandomMaxHit(npc, defs.getMaxHit(),NPCCombatDefinitions.MELEE, target)));
			break;
		}
		}
		if (Utils.getRandom(4) == 3) {
			npc.setDamageCap(800);
			npc.animate(new Animation(16317));
			npc.animate(new Animation(16318));
			for (@SuppressWarnings("unused") Entity t : npc.getPossibleTargets()) {
				if (!target.withinDistance(npc, 18))
					continue;
							target.applyHit(new Hit(npc, Utils.random(650),
									HitLook.MAGIC_DAMAGE));
					World.sendProjectile(npc, target, 3004, 60, 32, 50, 50, 0, 0);
					target.setNextGraphics(new Graphics (3002));
					target.setNextGraphics(new Graphics (3003));
					npc.animate(new Animation(16317));

		return defs.getAttackDelay();
			}
		}
		int shit = Utils.random(4);
		return shit;
	}
}
