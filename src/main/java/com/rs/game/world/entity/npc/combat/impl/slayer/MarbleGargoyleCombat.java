/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.slayer;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 14, 2018
 */
public class MarbleGargoyleCombat extends CombatScript {

	//OSRS Projectile 1453
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] { "Marble gargoyle" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		int attack = Utils.random(100);
		if (npc.withinDistance(target.getAsPlayer(), 1)) {
			if (attack >= 25)
				melee_attack(npc, target.getAsPlayer());
			else 
				special_attack(npc, target.getAsPlayer());
		} else {
			if (attack >= 25) 
				ranged_attack(npc, target.getAsPlayer());
			else
				special_attack(npc, target.getAsPlayer());
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}

	/**
	 * Handles ranged attack.
	 * @param npc
	 * @param target
	 */
	private void ranged_attack(NPC npc, Player target) {
		npc.animate(Animation.createOSRS(7811));
		World.sendProjectile(npc, target, 27, 46, 36, 50, 30, 0, 0);
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
	}

	/**
	 * Handles special attack ball explode thing.
	 * @param npc
	 * @param target
	 */
	private void special_attack(NPC npc, Player target) {
		final Position tile = target.getPosition();
		npc.animate(Animation.createOSRS(7815));
		World.sendProjectile(npc, tile, Graphics.createOSRS(1453).getId(), 46, 0, 50, 30, 16, 0);
		CoresManager.slowExecutor.schedule(() -> {
			if (target.getPosition().matches(tile)) {
				target.applyHit(new Hit(npc, Utils.random(380), HitLook.REGULAR_DAMAGE));
				target.addFreezeDelay(2000, true);
			}
			World.sendGraphics(npc, Graphics.createOSRS(1454), tile);
		}, 1200, TimeUnit.MILLISECONDS);
	}

	/**
	 * Handles melee attack.
	 * @param npc
	 * @param target
	 */
	private void melee_attack(NPC npc, Player target) {
		npc.animate(Animation.createOSRS(7811));
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
	}
	
}
