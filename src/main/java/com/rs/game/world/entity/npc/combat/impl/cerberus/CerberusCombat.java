/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.cerberus;

import java.util.ArrayList;
import java.util.List;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.instances.cerberus.CerberusNPC;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Handles cerberus's combat.
 * @author ReverendDread
 * Sep 21, 2018
 */
public class CerberusCombat extends CombatScript {

	private CerberusNPC cerberus;
	
	private static final Graphics LAVA = Graphics.createOSRS(1246);
	
	private static final Animation SPECIAL = Animation.createOSRS(4492);
	
	private static final Animation MELEE = Animation.createOSRS(4491);
	
	private static final Animation RANGED = Animation.createOSRS(4490);
	
	private static final Animation MAGIC = Animation.createOSRS(4490);
	
	private static final Projectile RANGED_PROJ = new Projectile(6245, 43, 31, 50, 16, 50, 0);
	
	private static final Projectile MAGIC_PROJ = new Projectile(6242, 43, 31, 50, 16, 50, 0);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		return new Object[] { "Cerberus" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		cerberus = (CerberusNPC) npc;	
		//Special attack every 40 seconds.
		if ((cerberus.cooldown + 40_000) < System.currentTimeMillis()) {
			return special_attack(cerberus, target);
		}
		int random = Utils.random(4);
		if (random == 0) {
			cerberus.animate(MELEE);
			delayHit(cerberus, 1, target, new Hit(cerberus, getRandomMaxHit(npc, 
					cerberus.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		} else if (random == 1) {
			cerberus.animate(RANGED);
			delayHit(cerberus, target, RANGED_PROJ, new Hit(cerberus, getRandomMaxHit(npc, 
					cerberus.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
			syncProjectileHit(cerberus, target, Graphics.createOSRS(1244), RANGED_PROJ);
		} else if (random == 2) {
			cerberus.animate(MAGIC);
			delayHit(cerberus, target, MAGIC_PROJ, new Hit(cerberus, getRandomMaxHit(npc, 
					cerberus.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
			syncProjectileHit(cerberus, target, Graphics.createOSRS(1243), MAGIC_PROJ);
		} else if (random == 3) {
			if ((cerberus.getHitpoints()) <= (cerberus.getMaxHitpoints() * .30))
				lava_pools(cerberus, target);
			else {
				cerberus.animate(RANGED);
				delayHit(cerberus, target, RANGED_PROJ, new Hit(cerberus, getRandomMaxHit(npc, 
						cerberus.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
				syncProjectileHit(cerberus, target, Graphics.createOSRS(1244), RANGED_PROJ);
			}
		}
		return cerberus.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Handles cerberus's lava pool attack.
	 * @param cerberus
	 * 			cerberus.
	 * @param target
	 * 			the target.
	 * @return
	 * 			delay till next attack.
	 */
	private int lava_pools(CerberusNPC cerberus, Entity target) {
		List<Position> pools = new ArrayList<Position>();
		cerberus.animate(SPECIAL);
		cerberus.setNextForceTalk(new ForceTalk("Grrrrrrrrrrrrrr"));
		final Position targetTile = target.getPosition();
		pools.add(targetTile);
		while (pools.size() < 3) {
			Position tile = new Position(target, 3);
			if (tile == target.getPosition() || World.getRegion(target.getRegionId()).getMask(0, tile.getXInRegion(), tile.getYInRegion()) != 0)
				continue;
			pools.add(tile);
			World.sendGraphics(cerberus, LAVA, tile);
		}
		WorldTasksManager.schedule(new WorldTask() {

			int ticks = 0;
			
			@Override
			public void run() {
				if (ticks >= 9 || cerberus.isFinished()) {
					stop();
				} else {
					pools.forEach((tile) -> {
						if (target.getPosition().matches(tile)) {
							target.applyHit(new Hit(cerberus, 150, HitLook.REGULAR_DAMAGE));
						} else if (target.getPosition().nextTo(tile)) {
							target.applyHit(new Hit(cerberus, 70, HitLook.REGULAR_DAMAGE));
						}
					});
				}
				ticks++;
			}
			
		}, 1, 1);
		return cerberus.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Handles cerberus's triple attack every 40 seconds.
	 * @param cerberus
	 * 			cerberus.
	 * @param target
	 * 			the target.
	 * @return
	 * 			delay till next attack.
	 */
	private int special_attack(CerberusNPC cerberus, Entity target) {
		cerberus.cooldown = System.currentTimeMillis();
		WorldTasksManager.schedule(new WorldTask() {

			int ticks = 0;
			
			@Override
			public void run() {
				if (ticks == 1) {
					cerberus.animate(MAGIC);
					delayHit(cerberus, target, MAGIC_PROJ, new Hit(cerberus, getRandomMaxHit(cerberus, 
							cerberus.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
					syncProjectileHit(cerberus, target, Graphics.createOSRS(1243), MAGIC_PROJ);
				} else if (ticks == 2) {
					cerberus.animate(RANGED);
					delayHit(cerberus, target, RANGED_PROJ, new Hit(cerberus, getRandomMaxHit(cerberus, 
							cerberus.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
					syncProjectileHit(cerberus, target, Graphics.createOSRS(1244), RANGED_PROJ);
				} else if (ticks == 4) {
					cerberus.animate(MELEE);
					delayHit(cerberus, 1, target, new Hit(cerberus, getRandomMaxHit(cerberus, 
							cerberus.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
				} else if (ticks == 5) {
					stop();
				}
				ticks++;
			}
			
		}, 0, 1);
		return 8;
	}

}
