package com.rs.game.world.entity.npc.godwars.zaros;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.player.controller.impl.GodWars;
import com.rs.game.world.entity.updating.impl.Animation;

@SuppressWarnings("serial")
public class ZarosMinion extends NPC {

    public ZarosMinion(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public void sendDeath(final Entity source) {
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
		    if (source instanceof Player) {
			Player player = (Player) source;
			Controller controler = player.getControlerManager().getControler();
			if (controler != null && controler instanceof GodWars) {
			    GodWars godControler = (GodWars) controler;
			}
		    }
		    drop();
		    reset();
		    setLocation(getSpawnPosition());
		    finish();
		    if (!isSpawned())
			setRespawnTask();
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
    }
}