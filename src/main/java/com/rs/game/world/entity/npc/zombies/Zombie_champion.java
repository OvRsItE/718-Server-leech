package com.rs.game.world.entity.npc.zombies;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.content.activities.Zombies;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

/**
 * 
 * @author Adam
 * @since Jully 31st 2012
 *NPC 3066
 *
 *
 */

@SuppressWarnings("serial")
public class Zombie_champion extends ZombiesNPC{

	private boolean spawnedZombies;
	private Zombies controler;
	
	public Zombie_champion(int id, Position tile, Zombies controler) {
		super(id, tile);
		this.controler = controler;
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		if(!spawnedZombies && getHitpoints() < getMaxHitpoints() / 2) {
			spawnedZombies = true;
			controler.spawnZombieMembers();
		}
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
					setNextGraphics(new Graphics(2924 + getSize()));
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					finish();
					controler.win();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	
	
}
