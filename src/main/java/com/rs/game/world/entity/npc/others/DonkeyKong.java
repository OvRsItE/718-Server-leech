package com.rs.game.world.entity.npc.others;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;


public class DonkeyKong extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 13216 };
	}

	
	@Override
	public int attack(final NPC npc, final Entity target) {
		if(npc.getHitpoints() < npc.getMaxHitpoints()/2 && Utils.random(5) == 0) { //if lower than 50% hp, 1/5 prob of healing 10%
			npc.heal(npc.getHitpoints()/10);
		}
		npc.setCombatLevel(512);
		npc.setName("Leeuni");
		npc.setDamageCap(750);
		
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(2)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk("You will DIE!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("You will not survive!"));
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("You cannot take me!"));
				break;
			}
		}
		if (Utils.getRandom(2) == 0) { // magical attack
			npc.animate(new Animation(15042));
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(npc, 1, t, getMagicHit(npc,getRandomMaxHit(npc, 540,NPCCombatDefinitions.MAGE, t)));
				World.sendProjectile(npc, t, 1002, 41, 16, 41, 35, 16, 0);
			   target.setNextGraphics(new Graphics(3000));;
			}
		} else { // melee attack
			npc.animate(new Animation(15046));
			delayHit(npc,0, target,getMeleeHit(npc,getRandomMaxHit(npc, 350,NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}