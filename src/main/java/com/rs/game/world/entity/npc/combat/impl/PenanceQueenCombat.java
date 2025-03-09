package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

public class PenanceQueenCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 5247 };
	}
	
	/*
	 * Attack ID: 5411
	 * Magic Attack ID: 5413
	 * Death ID: 5412
	 * Block ID: 5408
	 */

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int size = npc.getSize();
		final int hit = Utils.getRandom(300) + 20;
		if (Utils.getRandom(10) == 0) {
			switch (Utils.getRandom(2)) { //Vorago Taunts
			case 0:
				npc.setNextForceTalk(new ForceTalk("Hsssssssssss!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Blurgh!"));
				break;
			}
		}
		if (Utils.getRandom(6) == 0) {
				npc.animate(new Animation(5413));
				World.sendProjectile(npc, target, 2707, 34, 16, 40, 35, 16, 0);
				delayHit(npc, 1, target, new Hit[] {
						getMagicHit(npc, hit)
				});
				if (hit > 0) {
					target.setNextGraphics(new Graphics(2712));
				} else {
					target.setNextGraphics(new Graphics(85));
				}
			}
		else if (Utils.getRandom(6) == 0) {
			npc.animate(new Animation(5413));
			World.sendProjectile(npc, target, 475, 34, 16, 40, 35, 16, 0); 
			delayHit(npc, 1, target, new Hit[] {
					getRangeHit(npc, hit)
			});
		} else {
			npc.animate(new Animation(5411));
			delayHit(npc, 0, target, getMeleeHit( npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}