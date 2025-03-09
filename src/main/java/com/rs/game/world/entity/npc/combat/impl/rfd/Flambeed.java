package com.rs.game.world.entity.npc.combat.impl.rfd;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

public class Flambeed extends CombatScript{

	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] {3494};
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		/**
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
				delayHit(
						npc,
						0,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MELEE, target)));
				*///now it will only do the next one instead of 2 hits thats op brb fix the  the what? like it doesnt do an anim just hits, cant  iwde kg thie anivm..  idk it
				target.applyHit(new Hit(target, Utils.random(60, 70), Hit.HitLook.REGULAR_DAMAGE));;// testiing it has more wait tho i was lvl 3 wen i got owned fix it
				npc.animate(new Animation(1750));
		return defs.getAttackDelay();

	}
}
