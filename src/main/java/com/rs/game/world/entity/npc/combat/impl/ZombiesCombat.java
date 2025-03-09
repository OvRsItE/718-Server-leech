package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

public class ZombiesCombat extends CombatScript{

	@Override
	public Object[] getKeys() {
		return new Object[] {};
	}


	@Override
	public int attack(final NPC npc, final Entity target) {
		if(npc.getHitpoints() < npc.getMaxHitpoints()/2 && Utils.random(5) == 0) { //if lower than 50% hp, 1/5 prob of healing 10%
			npc.heal(5);
			npc.setNextForceTalk(new ForceTalk("I feel a tingley feeling.. MY HEALTH IS BOOSTED!"));
		}
		npc.setCombatLevel(50);
		npc.setHitpoints(290);
		npc.setName("Zombie");



		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(3)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk("WE WILL EAT YOUR BRAINS!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("The night will fall soon and we shall have meat in our hands."));
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("Flesh we need flesh.."));
				break;
			}
		}
		if (Utils.getRandom(2) == 0) { // range magical attack
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(
						npc,
						1,
						t,
						getRangeHit(
								npc,
								getRandomMaxHit(npc, 355,
										NPCCombatDefinitions.RANGE, t)));
				World.sendProjectile(npc, t, 1200, 41, 16, 41, 35, 16, 0);
				target.applyHit(new Hit(target, Utils.random(50, 67), Hit.HitLook.REGULAR_DAMAGE));
			}
		} else { // melee attack
			npc.animate(new Animation(defs.getAttackEmote()));
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
