package com.rs.game.world.entity.npc.pest;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.content.activities.PestControl;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Shifter extends PestMonsters {

    public Shifter(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, int index, PestControl manager) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned, index, manager);
    }

    @Override
    public void processNPC() {
	super.processNPC();
	Entity target = this.getPossibleTargets().get(0);
	if (this.getCombat().process() && !this.withinDistance(target, 10) || Utils.random(15) == 0)
	    teleportSpinner(target);
    }

    private void teleportSpinner(Position tile) { // def 3902, death 3903
	setNextPosition(tile);
	animate(new Animation(3904));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		setNextGraphics(new Graphics(654));// 1502
	    }
	});
    }
}
