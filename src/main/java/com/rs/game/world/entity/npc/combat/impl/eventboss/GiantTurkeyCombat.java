/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.eventboss;

import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Handles giant turkey event boss combat.
 * @author ReverendDread
 * Dec 29, 2018
 */
public class GiantTurkeyCombat extends CombatScript {

	private static final int MELEE_ANIM = 11000;
	private static final int RANGED_ANIM = 15442;
	
	private static final Projectile DRUM_STICK = new Projectile(4021, 50, 31, 30, 16, 20, 1);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] { "Giant turkey" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		int random = Utils.getRandom(2);	
		boolean ranged = random >= 2;
		for (Entity player : npc.getPossibleTargets()) {
			if (ranged) {
				npc.animate(RANGED_ANIM);
				CombatScript.delayHit(npc, player, DRUM_STICK, new Hit(npc, getRandomMaxHit(npc, 350, CombatDefinitions.RANGE_ATTACK, player), HitLook.RANGE_DAMAGE));
			} else {
				npc.animate(MELEE_ANIM);
				player.applyHit(new Hit(npc, getRandomMaxHit(npc, 350, CombatDefinitions.CRUSH_ATTACK, player), HitLook.MELEE_DAMAGE));
			}		
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}

}