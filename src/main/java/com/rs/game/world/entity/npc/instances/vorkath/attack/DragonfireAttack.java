/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath.attack;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class DragonfireAttack implements VorkathAttack {

	private static final int MAX_HIT = 450;
	private static final Animation BASIC_ATTACK_ANIM = Animation.createOSRS(7952);
	
	private static final Projectile DRAGONFIRE_PROJ = new Projectile(Graphics.createOSRS(393).getId(), 30, 25, 27, 15, 30, 0);
	private static final Graphics DRAGONFIRE_SPLASH = Graphics.createOSRS(157, 0, 92);
	
	private static final Projectile PINK_DFIRE_PROJ = new Projectile(Graphics.createOSRS(1471).getId(), 30, 25, 27, 15, 30, 0);
	private static final Graphics PINK_DFIRE_PROJ_SPLASH = Graphics.createOSRS(1473, 0, 92);
	
	private static final Projectile ACID_PROJ = new Projectile(Graphics.createOSRS(1470).getId(), 30, 30, 27, 15, 30, 0);
	private static final Graphics ACID_PROJ_SPLASH = Graphics.createOSRS(1472, 0, 92);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.impl.vorkath.attack.VorkathAttack#attack(com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC, com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int attack(VorkathNPC vorkath, Player target) {
		int style = Utils.random(3);
		vorkath.animate(BASIC_ATTACK_ANIM);
		World.sendProjectile(vorkath, target, (style == 0 ? DRAGONFIRE_PROJ : style == 1 ? PINK_DFIRE_PROJ : ACID_PROJ));
		int damage = (int) getReducedDamage(Utils.random(MAX_HIT), target);
		CoresManager.slowExecutor.schedule(() -> {
			target.setNextGraphics((style == 0 ? DRAGONFIRE_SPLASH : style == 1 ? PINK_DFIRE_PROJ_SPLASH : ACID_PROJ_SPLASH));
			target.applyHit(new Hit(vorkath,  damage, HitLook.REGULAR_DAMAGE));
			if (style == 1) {
				target.getPackets().sendGameMessage("You're prayers have been turned off.", true);
				target.getPrayer().closeAllPrayers(); //TODO make it disable protection prayers only
			} else if (style == 2) {
				int chance = Utils.random(100);
				if (chance < 10) {
					if (!target.getToxin().isImmune(ToxinType.VENOM))
						target.getPackets().sendGameMessage("<col=00ff00>You've been inflicted with venom.", true);
					target.getToxin().applyToxin(ToxinType.VENOM);
				}
			}		
		}, ((style == 0 ? DRAGONFIRE_PROJ : style == 1 ? PINK_DFIRE_PROJ : 
			ACID_PROJ).getHitSync(vorkath.getMiddleTile(), target) * 600), TimeUnit.MILLISECONDS);
		return vorkath.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Gets the reduced hit from dragonfire attacks.
	 * @param hit
	 * 			the original damage
	 * @param player
	 * 			the player.
	 * @return
	 * 			the reduced damage.
	 */
	public double getReducedDamage(int hit, Player player) {
		if (Combat.hasDFS(player) && player.getSuperFireImmune() > 0) {
			return 0;
		} else if (Combat.hasDFS(player) && !(player.getSuperFireImmune() > 0)
				|| (!Combat.hasDFS(player) && (player.getSuperFireImmune() > 0))
				|| (player.getPrayer().isMageProtecting() && (player.getSuperFireImmune() > 0))) {
			return (hit * 0.5);
		}
		return hit;
	}

}
