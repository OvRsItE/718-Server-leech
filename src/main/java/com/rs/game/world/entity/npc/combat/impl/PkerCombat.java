package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class PkerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15174 };
	}

	@SuppressWarnings("unused")
	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		
		if (Utils.getRandom(3) == 0) {
			npc.animate(new Animation(829));
			npc.heal(10);
		}
		if (Utils.getRandom(4) == 0) {
			npc.setNextForceTalk(new ForceTalk("Prayer drain!"));
			player.getPrayer().drainPrayer(10);
		}
		if (Utils.getRandom(2) == 0) { 
			npc.animate(new Animation(1062));
			npc.setNextGraphics(new Graphics(252, 0, 100));
			npc.playSound(2537, 1);
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(npc,1,target,getMeleeHit(npc,getRandomMaxHit(npc, Utils.random(100),NPCCombatDefinitions.MELEE, target)),getMeleeHit(npc,getRandomMaxHit(npc, Utils.random(300),NPCCombatDefinitions.MELEE, target)));
			}
		} else { // Melee - Whip Attack
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc,0,target,getMeleeHit( npc,getRandomMaxHit(npc, Utils.random(100), NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}