package com.rs.game.world.entity.npc.kalph;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

@SuppressWarnings("serial")
public class MiniKalphiteQueen extends NPC {

	public MiniKalphiteQueen(int id, Position tile, int mapAreaNameHash,
                             boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setForceAgressive(true);
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		animate(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if(getId() == 14520) {
						setCantInteract(true);
						transformIntoNPC(14538);
						setNextGraphics(new Graphics(1055));
						animate(new Animation(6270));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								reset();
								setCantInteract(false);
							}
							
						}, 5);
					}else{
						drop();
						reset();
						setLocation(getSpawnPosition());
						finish();
						if (!isSpawned())
							setRespawnTask();
						transformIntoNPC(14520);
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
}
