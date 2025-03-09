package com.rs.game.world.entity.npc.combat.impl.rfd;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

/**
 * 
 * @author Taylor/ Edited Adam.
 *
 */

public class Agrith extends CombatScript{

	@Override
	public Object[] getKeys() {

		return new Object []{3493};
	}

	
	/**private boolean isDistant(NPC npc, Entity target) {
		int size = npc.getSize();
		if(size > target.getX() || target.getY()) {
			return true;
		} brb
		return false;
	}*/
	
	@Override
	public int attack(final NPC npc, final Entity target) {
		
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(4) == 0) {
			
		
						///now it will only do the next one instead of 2 hits thats op brb fix the  the what? like it doesnt do an anim just hits, cant  iwde kg thie anivm..  idk it
			npc.setNextGraphics(new Graphics(129));
			npc.animate(new Animation(3501));
		target.applyHit(new Hit(target, Utils.random(40, 60), Hit.HitLook.REGULAR_DAMAGE));;// testiing it has more wait tho i was lvl 3 wen i got owned fix it
		if (Utils.getRandom(4) == 0) {
			npc.setNextGraphics(new Graphics(129));
			npc.animate(new Animation(3501));
			target.applyHit(new Hit(target, Utils.random(40, 60), Hit.HitLook.REGULAR_DAMAGE));;// testiing it has more wait tho i was lvl
		}
	} else {
		npc.setNextGraphics(new Graphics(129));
		npc.animate(new Animation(3501));
		target.applyHit(new Hit(target, Utils.random(40, 60), Hit.HitLook.REGULAR_DAMAGE));;// testiing it has more wait tho i was lvl 3 wen i got owned fix it
		
	}
		return defs.getAttackDelay();

	}
}