package com.rs.game.world.entity.npc.pest;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.content.activities.PestControl;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Splatter extends PestMonsters {

    public Splatter(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned, index, manager);
    }

    @Override
    public void processNPC() {
	super.processNPC();
    }

    private void sendExplosion() {
	final Splatter splatter = this;
	animate(new Animation(3888));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		animate(new Animation(3889));
		setNextGraphics(new Graphics(649 + (getId() - 3727)));
		WorldTasksManager.schedule(new WorldTask() {

		    @Override
		    public void run() {
			finish();
			for (Entity e : getPossibleTargets())
			    if (e.withinDistance(splatter, 2))
				e.applyHit(new Hit(splatter, Utils.getRandom(400), HitLook.REGULAR_DAMAGE));
		    }
		});
	    }
	});
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
		if (loop == 0)
		    sendExplosion();
		else if (loop >= defs.getDeathDelay()) {
		    reset();
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
    }
}
