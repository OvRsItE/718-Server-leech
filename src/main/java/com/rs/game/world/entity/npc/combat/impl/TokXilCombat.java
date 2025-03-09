package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class TokXilCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] {"Tok-Xil", 15205};
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		int hit = 0;
		int attackStyle = Utils.random(2);
		if (attackStyle == 0 && (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1)) {
			attackStyle = 1;
		}
		switch(attackStyle) {
		case 0:
			hit = getRandomMaxHit(npc, defs.getMaxHit() - 36, NPCCombatDefinitions.MELEE, target);
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, hit));
			break;
		case 1:
			hit = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target);
			npc.animate(new Animation(16132));
			World.sendProjectile(npc, target, 2993, 34, 16, 30, 35, 16, 0);
			delayHit(npc, 2, target, getRangeHit(npc, hit));
			break;
		}
		return defs.getAttackDelay();
	}
}
