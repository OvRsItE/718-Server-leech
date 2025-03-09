package com.rs.game.world.entity.npc.combat.impl.rfd;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

/**
 * 
 * @author Adam
 * @since Aug, 2nd.
 *
 */

public class Dessourt extends CombatScript{
	@Override
	public Object[] getKeys() {
		
		return new Object[] {3496};
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(3) == 0) {
	npc.animate(new Animation(3508));
		target.applyHit(new Hit(target, Utils.random(80, 90), Hit.HitLook.REGULAR_DAMAGE));;// testiing it has more wa
		npc.setNextGraphics(new Graphics(550));
		npc.setNextForceTalk(new ForceTalk("Hssssssssssss"));
		}
		if (Utils.getRandom(3) == 0) {
			npc.animate(new Animation(3508));
			target.applyHit(new Hit(target, Utils.random(80, 90), Hit.HitLook.MAGIC_DAMAGE));// testiing it has more wa
		}
		return defs.getAttackDelay();
}
}
