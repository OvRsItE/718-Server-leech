package com.rs.game.world.entity.npc.combat.impl.wilderness;

import com.google.common.collect.Lists;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import java.util.List;

public class CursedVetionCombat extends CombatScript {

	// MELEE/MAGIC ANIM - 5499
	// LIGHTING ANIM - 775
	private final List<NPC> minions = Lists.newArrayList();
	@Override
	public Object[] getKeys() {
		return new Object[] { 16011 };
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
		int random = Utils.random(10);
		if (random == 1 && minions.size() < 4)
			minionspawn(npc, target);
		for (Entity entity : targets) {
			if (entity.isNPC())
				continue;
			if (random == 0)  //prayer drain
				earthquake(npc, entity);
			if (random == 1 && minions.size() < 4)
				minionspawn(npc, entity);
			npc.animate(new Animation(5499));
			if (random == 5)
				cast_lighting(npc, entity);
			if (random == 6 && !entity.withinDistance(npc, 3)) {
				cast_lighting(npc, entity);
			} else {
				delayHit(npc, 0, entity, new Hit(npc, getRandomMaxHit(npc, 450, NPCCombatDefinitions.MELEE, entity), HitLook.MELEE_DAMAGE));
			}
		}

		return npc.getCombatDefinitions().getAttackDelay();
	}

	public int minionspawn(NPC npc, Entity target) {
		npc.animate(new Animation(5499));
		NPC minion = World.spawnNPC(16123, new Position(3056, 3789 + 2, 0),-1, 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
		minion.setForceAgressive(true);
		minion.setAtMultiArea(true);
		minion.setTarget(target);
		minions.add(minion);
		return npc.getCombatDefinitions().getAttackDelay();
	}

	private void earthquake(NPC npc, Entity target) {
		npc.animate(new Animation(5499));
		if (Utils.getDistance(npc, target) <= 11) {
			target.applyHit(new Hit(npc, 250 + Utils.random(150), HitLook.REGULAR_DAMAGE));
			target.sm("Vet'ion pummels the ground sending a shattering earthquake shockwave through you.");
		}
	}
	
	/**
	 * Handles Vet'ion's lighting attack.
	 * @param npc
	 * 			the source {@code NPC}.
	 * @param target
	 * 			the target {@code Entity}.
	 */
	private void cast_lighting(NPC npc, Entity target) {	
		for (int index = 0; index < 3; index++) {
			Position end = new Position(target.getX(), target.getY(), target.getZ());
			World.sendProjectile(npc, npc, end, 500, 65, 35, 16, 1, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					if (Utils.getDistance(target, end) == 0) {
						target.applyHit(new Hit(npc, Utils.random(300), HitLook.MAGIC_DAMAGE));
						target.setNextForceTalk(new ForceTalk("OUCH!"));
					}
				}

			}, 3);
		}		
	}
	
	
	
}
