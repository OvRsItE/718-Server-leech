package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class KaramelCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3495 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		@SuppressWarnings("unused")
		final Player player = (Player) target;
		if (Utils.getRandom(2) == 0) {
			npc.animate(new Animation(1979));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 230, NPCCombatDefinitions.MAGE, target)));
			 if (Utils.random(5) == 0) {
				 target.setNextGraphics(new Graphics(369));
				 target.addFreezeDelay(5000);
			 }
		} else {
			npc.animate(new Animation(2075));
			delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, 200, NPCCombatDefinitions.RANGE, target)));
		}
		return defs.getAttackDelay();
	}
	
}