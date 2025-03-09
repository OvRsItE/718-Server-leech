package com.rs.game.world.entity.npc.combat.impl.wilderness;

import com.google.common.collect.Lists;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.game.world.entity.updating.impl.NewForceMovement;
import com.rs.utility.Utils;

import java.util.List;

public class CursedCallistoCombat extends CombatScript {

	
	private static final Projectile PROJECTILE = new Projectile(Graphics.createOSRS(395).getId(), 35, 36, 40, 16, 30, 1);
	private static final int EXPLOSION = 359;
	private static final int HEIGHT = 120;
	private static final int ATTACK = 4925;
	private static final int KNOCKBACK = 734;

	private final List<NPC> minions = Lists.newArrayList();
	
	@Override
	public Object[] getKeys() {
		return new Object[] { "Cursed callisto" };
	}

	public boolean init(NPC npc) {
		npc.deathEndListener = ((entity, killer) -> {

			minions.forEach(NPC::finish);

		});
		npc.setCanBeFrozen(false);
		npc.setCapDamage(750);
		return true;
	}
	@Override
	public int attack(NPC npc, Entity target) {
		List<Entity> targets = npc.getPossibleTargets(false, true);
		minions.removeIf(NPC::isFinished);
		int max_hit = npc.getCombatDefinitions().getMaxHit();
		npc.animate(Animation.createOSRS(ATTACK));
		int random = Utils.random(5);
		if (random == 1 && minions.size() < 4)
			minionspawn(npc, target);
		for (Entity entity : targets) {
			if (entity.isNPC())
				continue;
			if (entity.withinDistance(npc.getMiddleTile(), 3)) {
				if (random != 0) {
					int knockback_chance = Utils.random(0, 19);
					if (knockback_chance == 0) {
						knock_back(npc, entity);
						return npc.getCombatDefinitions().getAttackDelay();
					}
					delayHit(npc, 1, entity, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.STAB_ATTACK, entity), HitLook.MELEE_DAMAGE));
				} else {
					magic_attack(npc, entity, max_hit);
				}
			} else {
				int knockback_chance = Utils.random(0, 4);
				if (knockback_chance == 0) {
					knock_back(npc, entity);
					return npc.getCombatDefinitions().getAttackDelay();
				}
				magic_attack(npc, entity, max_hit);
			}
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}

	public int minionspawn(NPC npc, Entity target) {
		npc.animate(new Animation(ATTACK));
		NPC minion = World.spawnNPC(16124, new Position(3056, 3789 + 2, 0),-1, 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
		minion.setForceAgressive(true);
		minion.setAtMultiArea(true);
		minion.setTarget(target);
		minions.add(minion);
		return npc.getCombatDefinitions().getAttackDelay();
	}

	/**
	 * Callistos knockback attack.
	 * @param npc
	 * 			callisto.
	 * @param target
	 * 			the target.
	 */


	private void knock_back(NPC npc, Entity target) {
		final Position tile = getKnockbackTile(target, npc);
		if (tile == null || tile.matches(target))
			return;
		target.getAsPlayer().lock(2);
		target.animate(Animation.createOSRS(KNOCKBACK));
		target.setNextForceMovement(new NewForceMovement(target, 0, tile, 1, Utils.getAngle(tile.getX() - target.getX(), tile.getY() - target.getY())));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.setNextPosition(tile);
				int stun_chance = Utils.random(0, 100);
				if (stun_chance < 15) {
					target.addFreezeDelay(2000, false);
					target.setNextGraphics(new Graphics(80, 5, 60));
				}
				target.getAsPlayer().getPackets().sendGameMessage("Callisto's roar knocks you backwards.", true);
				target.getAsPlayer().getActionManager().forceStop();
			}

		}, 1);
		delayHit(npc, 2, target, new Hit(npc, 30, HitLook.REGULAR_DAMAGE));
	}


	
	/**
	 * Callistos magic attack.
	 * @param npc
	 * 			callisto.
	 * @param target
	 * 			the target.
	 * @param max_hit
	 * 			callistos max hit.
	 */
	private void magic_attack(NPC npc, Entity target, int max_hit) {
		delayHit(npc, target, PROJECTILE, new Hit(npc, getRandomMaxHit(npc, max_hit, CombatDefinitions.MAGIC_ATTACK, target), HitLook.MAGIC_DAMAGE));
		syncProjectileHit(npc, target, Graphics.createOSRS(EXPLOSION, 0, HEIGHT), PROJECTILE);
	}
	/**
	 * Gets the target tile from knockback.
	 * @param target
	 * 			the target entity.
	 * 			to increment or not.
	 * @return
	 * 			the target tile.
	 */
	public Position getKnockbackTile(Entity target, NPC npc) {
		byte[] dirs = Utils.getDirection(npc.getDirection());
		int distance = Utils.random(3, 5);
		Position destination = new Position(target.getX() + (dirs[0] * distance), target.getY() + (dirs[1] * distance), target.getZ());
		if (Utils.colides(target.getX(), target.getY(), target.getSize(), destination.getX(), destination.getY(), target.getSize()) || !World.isTileFree(destination.getZ(), destination.getX(), destination.getY(), target.getSize()))
			return null;
		return destination;
	}

}
