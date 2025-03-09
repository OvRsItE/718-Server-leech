/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.skotizo;

import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class SkotizoCombat extends CombatScript {

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		return new Object[] { "Skotizo" };
	}
	
	private static final Projectile MAGIC = new Projectile(141, 45, 32, 0, 16, 20, 0);

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {	
		int random = Utils.random(20);
		if (target.withinDistance(npc.getMiddleTile(), npc.getSize() + 1) && random <= 10) {
			npc.animate(npc.getCombatDefinitions().getAttackEmote());
			delayHit(npc, 1, target, new Hit(npc, 
					getRandomMaxHit(npc, npc.getMaxHit(), CombatDefinitions.STAB_ATTACK, target), HitLook.MELEE_DAMAGE));
		} else {
			npc.animate(69);
			delayHit(npc, target, MAGIC, new Hit(npc, 
				getRandomMaxHit(npc, npc.getMaxHit(), CombatDefinitions.MAGIC_ATTACK, target), HitLook.MAGIC_DAMAGE));
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}

}
