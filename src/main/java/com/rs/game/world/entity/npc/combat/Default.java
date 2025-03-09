package com.rs.game.world.entity.npc.combat;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class Default extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Default" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		if (attackStyle == NPCCombatDefinitions.MELEE) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target)));
		} else {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target);
			int projectileId = defs.getAttackProjectile();
			if (projectileId == -1 && defs.getAttackGfx() == -1 && attackStyle == NPCCombatDefinitions.MAGE)
				projectileId = 2730;
			delayHit(npc, 2, target, attackStyle == NPCCombatDefinitions.RANGE || attackStyle == NPCCombatDefinitions.RANGE_FOLLOW ? getRangeHit(npc, damage) : getMagicHit(npc, damage));
			if (projectileId != -1)
				World.sendProjectile(npc, target, projectileId, 41, 16, 41, 35, 16, 0);
		}
		if (defs.getAttackGfx() != -1)
			npc.setNextGraphics(new Graphics(defs.getAttackGfx(), 0, attackStyle == NPCCombatDefinitions.RANGE ? 100 : 0));
		npc.animate(new Animation(defs.getAttackEmote()));
		return defs.getAttackDelay();
	}
}
