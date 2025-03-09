/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath.attack;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class HDDragonfireAttack implements VorkathAttack {

	private final int MIN_HIT = 840, MAX_HIT = 1150;
	
	private static final Animation HIGH_ATTACK_ANIM = Animation.createOSRS(7960);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.impl.vorkath.attack.VorkathAttack#attack(com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC, com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int attack(VorkathNPC vorkath, Player target) {
		vorkath.animate(HIGH_ATTACK_ANIM);
		final Position position = target.getPosition();
		World.sendProjectile(vorkath, vorkath.getMiddleTile(), target, Graphics.createOSRS(1481).getId(), 40, 10, 35, 70, 58, 4);
		CoresManager.slowExecutor.schedule(() -> {			
			if (position.withinDistance(target, 1)) {
				target.applyHit(new Hit(vorkath, Utils.random(MIN_HIT, MAX_HIT), HitLook.REGULAR_DAMAGE));
			}	
			World.sendGraphics(vorkath, Graphics.createOSRS(1466), position);
		}, 3000, TimeUnit.MILLISECONDS);	
		return vorkath.getCombatDefinitions().getAttackDelay();
	}

}
