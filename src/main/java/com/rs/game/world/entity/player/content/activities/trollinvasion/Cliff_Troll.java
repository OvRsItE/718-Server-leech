package com.rs.game.world.entity.player.content.activities.trollinvasion;


import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;

@SuppressWarnings("serial")
public class Cliff_Troll extends NPC{
	
	private TrollInvasion controler;

	public Cliff_Troll(int id, Position tile, TrollInvasion controler) {
		super(id, tile, -1, 0, true, true);
		this.controler = controler;
		setForceMultiArea(true);
		setNoDistanceCheck(true);
		setForceAgressive(true);
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		controler.addKill();
		getCombat().removeTarget();
		animate(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}


}
