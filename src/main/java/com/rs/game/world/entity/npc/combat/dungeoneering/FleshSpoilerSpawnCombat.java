package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class FleshSpoilerSpawnCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 11910 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		npc.animate(new Animation(Utils.random(3) == 0 ? 14474 : 14475));
		delayHit(npc, 0, target, getMeleeHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
		return 3;
	}
}
