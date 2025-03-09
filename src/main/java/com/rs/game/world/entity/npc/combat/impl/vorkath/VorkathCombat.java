/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.vorkath;

import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.npc.instances.vorkath.attack.MeleeAttack;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

/**
 * Handles Vorkath's combat script.
 * @author ReverendDread
 * Sep 18, 2018
 */
public class VorkathCombat extends CombatScript {
	
	private VorkathNPC vorkath;
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		return new Object[] { "Vorkath" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		vorkath = (VorkathNPC) npc;
		//If zombified spawn is still alive then don't attack the player.
		if (vorkath.getMinion() != null && !vorkath.getMinion().hasExploded()) {
			return 1;
		}
		if (target.withinDistance(vorkath, 2)) {
			int random = Utils.random(2);
			if (random == 1) {
				return vorkath.getNextAttack().attack(vorkath, target.getAsPlayer());
			} else {
				return new MeleeAttack().attack(vorkath, target.getAsPlayer());
			}
		}
		return vorkath.getNextAttack().attack(vorkath, target.getAsPlayer());
	}

}
