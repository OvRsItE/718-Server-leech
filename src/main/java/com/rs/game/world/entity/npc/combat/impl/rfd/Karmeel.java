package com.rs.game.world.entity.npc.combat.impl.rfd;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

/**
 * 
 * @author Adam
 * @since Aug,2nd.
 *
 */

public class Karmeel extends CombatScript{

	@Override
	public Object[] getKeys() {
		
		return new Object[] {3495};
	}
	
	/**
	 * npc.setNextAnimation(new Animation(1979));
      target.setNextGraphics(new Graphics(369));
      target.addFrozenBlockedDelay(100);
 World.sendProjectile(npc, target, 368, 60, 32, 50, 50, 0, 0);
		target.applyHit(new Hit(target, Utils.random(110, 135), Hit.HitLook.MAGIC_DAMAGE));;// testiing it has more wa
		return defs.getAttackDelay();
	 */

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (Utils.getRandom(4) == 0
						&& target.getFreezeDelay() < System
								.currentTimeMillis()) {
					   int newX =npc.getX() - 3;
		                int newY = npc.getY();
		               npc.addWalkSteps(newX, newY, -1, false);
					 npc.animate(new Animation(1979));
					 npc.setNextForceTalk(new ForceTalk(("Semolina-Go!")));
					target.addFreezeDelay(3000);
					target.setNextGraphics(new Graphics(369));
					target.applyHit(new Hit(target, Utils.random(70, 80), Hit.HitLook.MAGIC_DAMAGE));
					if (target instanceof Player) {
						Player targetPlayer = (Player) target;
						targetPlayer.stopAll();
					}
				} else {
					 npc.animate(new Animation(1979));
					target.applyHit(new Hit(target, Utils.random(70, 80), Hit.HitLook.MAGIC_DAMAGE));
				}
		}}, 1);
		
	
		return defs.getAttackDelay();
	}
}
