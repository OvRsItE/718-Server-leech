package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTasksManager;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.alchemist.TheAlchemist;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.CombatStyle;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class TheAlchemistCombat extends CombatScript {

	private TheAlchemist alchemist;
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 16071, 16080, 16081 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		alchemist = (TheAlchemist) npc;
		if (!(target instanceof Player))
			return attackFamiliar(target);
		if (specialAttack(alchemist.getCombatStyle(), npc, target))
			return 6;
		else if (alchemist.getCombatStyle() == CombatStyle.ALL) {
			int random = Utils.getRandom(3);
			if (random == 1) {
				npc.transformIntoNPC(16080);
				return !target.withinDistance(npc, 2) ? projectileMeleeAttack(npc, target) : meleeAttack(npc, target);
			} else if (random == 2) {
				npc.transformIntoNPC(16071);
				return magicAttack(npc, target);
			} else if (random == 3) {
				npc.transformIntoNPC(16081);
				return rangedAttack(npc, target);		
			} else {
				npc.transformIntoNPC(16080);
				return meleeAttack(npc, target);
			}
		} else if (alchemist.getCombatStyle() == CombatStyle.MELEE) {
			return !target.withinDistance(npc, 2) ? projectileMeleeAttack(npc, target) : meleeAttack(npc, target);
		} else if (alchemist.getCombatStyle() == CombatStyle.MAGIC) {
			return magicAttack(npc, target);
		} else if (alchemist.getCombatStyle() == CombatStyle.RANGE) {
			return rangedAttack(npc, target);
		}
		return 1;
	}
	
	/**
	 * Handles melee attack
	 * @param npc
	 * 			the npc
	 * @param target
	 * 			the target
	 */
	private int meleeAttack(NPC npc, Entity target) {
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		npc.animate(new Animation(440));
		return alchemist.getPhase() == 4 ? 3 : 6;
	}
	
	/**
	 * Handles distance melee attack
	 * @param npc
	 * 			the npc
	 * @param target
	 * 			the target
	 */
	private int projectileMeleeAttack(NPC npc, Entity target) {
		World.sendProjectile(npc, target, 3842, 36, 36, 41, 35, 16, 0);
		npc.animate(new Animation(10501));
		delayHit(npc, 2, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		CoresManager.slowExecutor.schedule(new Runnable() {
			
			@Override
			public void run() {
				World.sendProjectile(target, npc, 3842, 36, 36, 41, 35, 32, 0);
			}
				
		}, (int) Math.ceil((Utils.getDistance(alchemist, target) * 0.10)), TimeUnit.SECONDS);
		return 6;
	}
	
	/**
	 * Handles magic attack
	 * @param npc
	 * 			the npc
	 * @param target
	 * 			the target
	 */
	private int magicAttack(NPC npc, Entity target) {
		World.sendProjectile(npc, target, 3841, 36, 36, 40, 20, 0, 16);
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.RANGE, target), HitLook.MAGIC_DAMAGE));
		npc.animate(new Animation(10516));
		return alchemist.getPhase() == 4 ? 4 : 6;
	}
	
	/**
	 * Handles ranged attack
	 * @param npc
	 * 			the npc
	 * @param target
	 * 			the target
	 */
	private int rangedAttack(NPC npc, Entity target) {
		World.sendProjectile(npc, target, 1224, 60, 36, 40, 30, 0, 16);
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
		npc.animate(new Animation(426));
		return alchemist.getPhase() == 4 ? 4 : 6;
	}
	
	/**
	 * Heals The Alchemist from killing npcs.
	 * @param target
	 */
	private int attackFamiliar(Entity target) {
		World.sendProjectile(alchemist, target, 3841, 36, 36, 10, 20, 0, 16);
		alchemist.animate(new Animation(10516));
		delayHit(alchemist, 3, target, new Hit(alchemist, target.getMaxHitpoints(), HitLook.REGULAR_DAMAGE));
		int total = (alchemist.getMaxHitpoints() - alchemist.getHitpoints());
		int extra = (target.getHitpoints() - total);
		alchemist.heal(total, extra);
		return 6;
	}
	
	/**
	 * Spawns minions
	 * @param alchemist
	 * 			the alchemist
	 * @param target
	 * 			the target.
	 * @return
	 * 			true if minions were spawned, false otherwise.
	 */
	private boolean spawnMinions(TheAlchemist alchemist, Entity target) {	
		if (alchemist.getMinions().size() >= 5)
			return false;
		int spawned = 0;
		alchemist.animate(new Animation(7070));
		while (spawned < 3) {
			Position spawn = new Position(alchemist, 3);
			if (alchemist.canWalkNPC(spawn.getX(), spawn.getY())) {
				World.sendProjectile(alchemist, spawn, 3841, 36, 36, 20, 20, 10, 0);
				CoresManager.slowExecutor.schedule(new Runnable() {
	
					@Override
					public void run() {
						NPC minion = alchemist.addMinion(new NPC(13848, spawn, -1, true));
						minion.setSpawned(true);
						minion.setForceMultiArea(true);
						minion.setTarget(target);
					}
						
				}, (int) Math.ceil((Utils.getDistance(alchemist, spawn) * 0.10)), TimeUnit.SECONDS);
				spawned++;
			}
		}
		return true;
	}
	
	/**
	 * Handles special attacks based on combat style.
	 * @param style
	 * 			the combat style being used.
	 * @param npc
	 * 			the npc combating
	 * @param target
	 * 			the target.
	 * @return
	 * 			true if a special attack was performed, false otherwise.
	 */
	private boolean specialAttack(CombatStyle style, NPC npc, Entity target) {
		if (Utils.getRandom(10) > 1)
			return false;
		switch (style) {
			case MAGIC:
				spawnMinions(alchemist, target);
				if (!(Utils.getRandom(4) > 1))
				knock_back(alchemist, target);
				break;
			case MELEE:
				npc.animate(new Animation(2876));
				World.sendProjectile(npc, target, 1345, 31, 31, 25, 10, 16, 0);
				World.sendProjectile(npc, target, 1345, 31, 31, 25, 20, 16, 0);
				target.getAsPlayer().sendMessage("<col=ff0000>Run from the Alchemist to stop the burning!");
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextGraphics(new Graphics(3841));
						target.applyHit(new Hit(npc, 50, HitLook.REGULAR_DAMAGE));
						if (!target.withinDistance(npc, 3)) {
							stop();
						}
					}	
				}, 3, 1);		
				break;
			case RANGE:
				npc.animate(new Animation(1914));
				npc.setNextGraphics(new Graphics(3095));
				Position tile = target.getPosition();
				WorldTasksManager.schedule(new WorldTask() {
					
					int tick = 0;
					
					@Override
					public void run() {
						tick++;
						if (tick == 2)
							World.sendGraphics(npc, new Graphics(3096), tile);
						if (target.withinDistance(tile, 1) && tick >= 3) {
							target.applyHit(new Hit(npc, (int) (target.getMaxHitpoints() * 0.50), HitLook.REGULAR_DAMAGE));
							stop();
						}
						if (tick >= 3)
							stop();
					}	
					
				}, 0, 1);
				break;
			case ALL:
				specialAttack(CombatStyle.values()[Utils.random(CombatStyle.values().length)], npc, target); //Does random combat style spec
				break;
		}
		return true;
	}
	
	/**
	 * Handles the knockback attack.
	 * @param npc
	 * @param target
	 */
	private void knock_back(NPC npc, Entity target) {
		final Position tile = getKnockbackTile(target, npc);
		if (tile == null || tile.matches(target) || !target.withinDistance(npc, 1))
			return;
		npc.setNextForceTalk(new ForceTalk("Begone!"));
		target.getAsPlayer().lock(2);
		target.animate(new Animation(734));
		target.setNextForceMovement(new ForceMovement(target, 0, tile, 1, Utils.getAngle(tile.getX() - target.getX(), tile.getY() - target.getY())));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.setNextPosition(tile);
				int stun_chance = Utils.random(0, 100);
				if (stun_chance < 15) {
					target.addFreezeDelay(2000, false);
					target.setNextGraphics(new Graphics(80, 5, 60));
				}				
				target.getAsPlayer().setFindTargetDelay(2000);
			}
			
		}, 1);
		delayHit(npc, 2, target, new Hit(npc, 30, HitLook.REGULAR_DAMAGE));		
	}
	
	/**
	 * Gets the target tile from knockback.
	 * @param target
	 * 			the target entity.
	 * @param increment
	 * 			to increment or not.
	 * @return
	 * 			the target tile.
	 */
	public Position getKnockbackTile(Entity target, NPC npc) {
		byte[] dirs = Utils.getDirection(npc.getDirection());
		int distance = Utils.random(3, 5);
		Position destination = new Position(target.getX() + (dirs[0] * distance), target.getY() + (dirs[1] * distance), target.getZ());
		if (target != null && Utils.colides(target.getX(), target.getY(), target.getSize(), destination.getX(), destination.getY(), target.getSize()) 
				|| !World.isTileFree(destination.getZ(), destination.getX(), destination.getY(), target.getSize()))
			return null;
		return destination;
	}

}
