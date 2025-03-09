/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath.attack;

import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class MeleeAttack implements VorkathAttack {

	private static final Animation MELEE_ANIM = Animation.createOSRS(7951);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.impl.vorkath.attack.VorkathAttack#attack(com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC, com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int attack(VorkathNPC vorkath, Player target) {
		vorkath.animate(MELEE_ANIM);
		CombatScript.delayHit(vorkath, 1, target, new Hit(vorkath, 
				CombatScript.getRandomMaxHit(vorkath, vorkath.getCombatDefinitions().getMaxHit(), 
						NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		return vorkath.getCombatDefinitions().getAttackDelay();
	}

}
