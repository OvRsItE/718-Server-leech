package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.utility.Utils;

public class CulinaromancerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3491 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		@SuppressWarnings("unused")
		final Player player = (Player) target;
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(3)) { //Cul taunts
			case 0:
				npc.setNextForceTalk(new ForceTalk("I will be the death of you!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("The pastry will be mine!"));
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("Meat your despear!"));
				break;
			}
		}
		npc.animate(new Animation(1979));
		delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 380, NPCCombatDefinitions.MAGE, target)));
		return defs.getAttackDelay();
	}
}