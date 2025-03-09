package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class DeathslingerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 11206, 11208, 11210, 11212, 11214, 11216, 11218, 11220, 11222, 11224 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int tier = (npc.getId() - 11206) / 2;
		int damage = 0;
		if (usingSpecial) {
			npc.setNextGraphics(new Graphics(2447));
			damage = getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.RANGE) * (1.05 * tier)), NPCCombatDefinitions.RANGE, target);
			//TODO posion
			//if (Utils.random(11 - tier) == 0)
			//		target.getPoison().makePoisoned(100);
		} else
			damage = getMaxHit(npc, NPCCombatDefinitions.RANGE, target);
		npc.animate(new Animation(13615));
		World.sendProjectile(npc, target, 2448, 41, 16, 41, 35, 16, 0);
		delayHit(npc, 2, target, getRangeHit(npc, damage));
		return npc.getAttackSpeed();
	}
}
