package com.rs.game.world.entity.player.content.activities.trollinvasion;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;


public class TrollGeneral extends CombatScript {

	@Override
	public Object[] getKeys() {

		return new Object[] { "Troll general", 12291 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		npc.animate(new Animation(13788));
	
			hit = getRandomMaxHit(npc, Utils.random(300, 400), NPCCombatDefinitions.MELEE, target);
			delayHit(npc, 1, target, getMeleeHit(npc, hit));
		return defs.getAttackDelay();
	}

}
