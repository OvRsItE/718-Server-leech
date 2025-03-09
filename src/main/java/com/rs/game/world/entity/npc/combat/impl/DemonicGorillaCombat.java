/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Nov 26, 2018
 */
public class DemonicGorillaCombat extends CombatScript {

	/** The ids of each prayer type */
	private static final int MELEE_ID = 27147, RANGED_ID = 27148, MAGIC_ID = 27149;
	
	/** Melee attack anim */
	private static final Animation MELEE = Animation.createOSRS(7226);
	
	/** Magic attack anim */
	private static final Animation MAGIC = Animation.createOSRS(7225);
	
	/** Ranged attack anim */
	private static final Animation RANGED = Animation.createOSRS(7227);
	
	/** Ranged projectile */
	private static final Projectile SMALL_BOULDER = new Projectile(Graphics.createOSRS(1302).getId(), 36, 32, 40, 16, 20, 0);
	
	/** Magic projectile */
	private static final Projectile GREEN_ORB = new Projectile(Graphics.createOSRS(1304).getId(), 32, 10, 40, 16, 20, 0);
	
	/** Boulder attack */
	private static final Projectile BOULDER = new Projectile(Graphics.createOSRS(856).getId(), 150, 0, 30, 16, 5, 2);
	
	/** Boulder hit gfx */
	private static final Graphics BOULDER_HIT = Graphics.createOSRS(305);
	
	/** Style switching anim */
	private static final Animation OVERHEAD_SWITCH = Animation.createOSRS(7228);
	
	/** Magic hit gfx */
	private static final Graphics MAGIC_HIT = Graphics.createOSRS(1305);
	
	/** Ranged hit gfx */
	private static final Graphics RANGED_HIT = Graphics.createOSRS(1303);
	
	//Ranged = 7227
	//Magic = 7238
	//Explosive rock = 7228
	//Melee = 7226
	//Block = 7224
	//Death= 7229
	//
	//1305 - mage gfx
	//1304 - mage hit gfx
	//1303 - range hit gfx
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		return new String[] { "Demonic gorilla" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		//Combat definitions
		NPCCombatDefinitions definitions = npc.getCombatDefinitions();		
		//Random attack chance
		int attack = Utils.random(10);

		if (npc.getMiddleTile().withinDistance(target, 1)) {
			npc.animate(MELEE);
			delayHit(npc, 1, target, new Hit(npc,
					getRandomMaxHit(npc, definitions.getMaxHit(), NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
			return definitions.getAttackDelay();
		}
		
		if (attack == 0 && (npc.getId() == MAGIC_ID || npc.getId() == RANGED_ID)) { //Boulder attack
			npc.animate(OVERHEAD_SWITCH);
			final Position destination = target.getPosition();
			World.sendProjectile(new Position(destination.getX() + 2, destination.getY(), destination.getZ()), target.getMiddleTile(), BOULDER);
			CoresManager.slowExecutor.schedule(() -> {
				if (target.getPosition().withinDistance(new Position(destination.getX(), destination.getY(), destination.getZ()), 1)) {
					target.applyHit(new Hit(npc, Utils.random(360), HitLook.REGULAR_DAMAGE));
				}
				World.sendGraphics(npc, BOULDER_HIT, destination);
			}, 1800, TimeUnit.MILLISECONDS);
		} else if (attack >= 1 && attack <= 5) { //Ranged attack
			return rangedAttack(npc, target);
		} else if (attack >= 6 && attack <= 10) { //Magic attack
			return magicAttack(npc, target);
		} else {
			return magicAttack(npc, target);
		}
		
		return 15;
	}
	
	/**
	 * Handles ranged attack.
	 * @param npc
	 * @param target
	 * @return
	 */
	public int rangedAttack(NPC npc, Entity target) {
		npc.animate(RANGED);
		delayHit(npc, target, SMALL_BOULDER, new Hit(
				npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
		CoresManager.slowExecutor.schedule(() -> {
			target.setNextGraphics(RANGED_HIT);
		}, SMALL_BOULDER.getHitSyncToMillis(npc, target) + 600, TimeUnit.MILLISECONDS);
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Handles magic attack
	 * @param npc
	 * @param target
	 * @return
	 */
	public int magicAttack(NPC npc, Entity target) {
		npc.animate(MAGIC);
		delayHit(npc, target, GREEN_ORB, new Hit(
				npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
		CoresManager.slowExecutor.schedule(() -> {
			target.setNextGraphics(MAGIC_HIT);
		}, GREEN_ORB.getHitSyncToMillis(npc, target) + 600, TimeUnit.MILLISECONDS);
		return npc.getCombatDefinitions().getAttackDelay();
	}

}
