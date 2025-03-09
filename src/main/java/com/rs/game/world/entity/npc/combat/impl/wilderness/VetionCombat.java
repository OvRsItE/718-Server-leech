package com.rs.game.world.entity.npc.combat.impl.wilderness;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class VetionCombat extends CombatScript {

	// MELEE/MAGIC ANIM - 5499
	// LIGHTING ANIM - 775
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 26611, 26612 };
	}

	@Override
	public int attack(NPC npc, Entity target) {	
		npc.animate(new Animation(5499));
		if (!target.withinDistance(npc, 3)) {
			cast_lighting(npc, target);
		} else {
			delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, 450, NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		}	
		return npc.getCombatDefinitions().getAttackDelay();
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
