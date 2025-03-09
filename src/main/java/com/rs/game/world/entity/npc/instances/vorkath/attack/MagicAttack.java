/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath.attack;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.world.Projectile;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class MagicAttack implements VorkathAttack {

	private static final Animation BASIC_ATTACK_ANIM = Animation.createOSRS(7952);
	
	private static final Projectile MAGIC_PROJ = new Projectile(Graphics.createOSRS(1479).getId(), 30, 25, 27, 15, 30, 0);
	private static final Graphics MAGIC_PROJ_SPLASH = Graphics.createOSRS(1480, 0, 92);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.impl.vorkath.attack.VorkathAttack#attack(com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC, com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int attack(VorkathNPC vorkath, Player target) {
		vorkath.animate(BASIC_ATTACK_ANIM);
		int damage = CombatScript.getRandomMaxHit(vorkath, vorkath.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.MAGE, target);		
		CombatScript.delayHit(vorkath, target, MAGIC_PROJ, new Hit(vorkath, damage, HitLook.MAGIC_DAMAGE));
		CoresManager.slowExecutor.schedule(() -> {
			target.setNextGraphics(MAGIC_PROJ_SPLASH);
		}, (MAGIC_PROJ.getHitSync(vorkath.getMiddleTile(), target.getMiddleTile()) * 600), TimeUnit.MILLISECONDS);
		return vorkath.getCombatDefinitions().getAttackDelay();
	}

}
