/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl;

import java.util.ArrayList;
import java.util.Arrays;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.shaman.LizardmanShaman;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Handles the lizard shaman combat script.
 * @author ReverendDread
 * Oct 6, 2018
 */
public class LizardShamanCombat extends CombatScript {

	/**
	 * Spitting acid
	 */
	private static final Animation SPITTING = Animation.createOSRS(7193);
	
	/**
	 * Landing on the ground
	 */
	private static final Animation LANDING = Animation.createOSRS(6946);
	
	/**
	 * Jumping up
	 */
	private static final Animation JUMPING = Animation.createOSRS(7152);
	
	/**
	 * Summoning minions
	 */
	private static final Animation SUMMONING = Animation.createOSRS(7157);
	
	/**
	 * Melee attack
	 */
	private static final Animation MELEE = Animation.createOSRS(7192);
	
	/**
	 * Acid spit projectile.
	 */
	private static final Projectile ACID_PROJ = new Projectile(Graphics.createOSRS(1293).getId(), 100, 25, 60, 14, 30, 0);
	
	/**
	 * Normal ranged attack projectile.
	 */
	private static final Projectile RANGED_PROJ = new Projectile(Graphics.createOSRS(1291).getId(), 90, 25, 60, 14, 30, 0);
	
	/**
	 * The lizard shaman.
	 */
	private LizardmanShaman shaman;
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		return new Object[] { "Lizardman shaman" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		this.shaman = (LizardmanShaman) npc;
		int attack = Utils.random(6);
		if (attack < 2 && target.withinDistance(npc.getMiddleTile(), 3)) {
			npc.animate(MELEE);
			delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, 
					npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
			return npc.getCombatDefinitions().getAttackDelay();
		}
		switch (attack) {
		
			case 0: //Jump attack
				final Position tile = target.getPosition();
				WorldTasksManager.schedule(new WorldTask() {

					int ticks;
					
					@Override
					public void run() {
						if (ticks == 0) {
							npc.animate(JUMPING);
						}
						if (ticks == 2) {
							npc.animate(LANDING);
							npc.setNextPosition(tile);
						}
						if (ticks == 3) {
							if (target.getPosition().matches(tile)) {
								target.applyHit(new Hit(npc, Utils.random(200, 250), HitLook.REGULAR_DAMAGE));
							}
							stop();
						}
						ticks++;
					}			
					
				}, 1, 1);
				return 8;
				
			case 1: //Minions
				npc.animate(SUMMONING);
				shaman.spawnMinions(shaman, target);
				break;
		
			case 2: //Acid spitting
			case 3:
				npc.animate(SPITTING);
				final Position floor = target.getPosition();
				World.sendProjectile(npc.getMiddleTile(), target.getPosition(), ACID_PROJ);
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						if (target.getPosition().matches(floor)) {
							boolean shayzien_armor = target.getAsPlayer().getEquipment().wearingShayzianArmor();
							target.applyHit(new Hit(npc, shayzien_armor ? 0 : Utils.random(0, 300), HitLook.POISON_DAMAGE));
							target.getToxin().applyToxin(ToxinType.POISON);
						}
						World.sendGraphics(npc, Graphics.createOSRS(1294), floor);	
					}
					
				}, ACID_PROJ.getHitSync(npc.getMiddleTile(), target.getMiddleTile()));
				break;
				
			case 4: //Normal ranged attack.
			case 5: 
			case 6:
				npc.animate(SPITTING);
				delayHit(npc, target, RANGED_PROJ, new Hit(npc, 
						getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(),
								NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
				break;
				
		}		
		return npc.getCombatDefinitions().getAttackDelay();
	}

}
