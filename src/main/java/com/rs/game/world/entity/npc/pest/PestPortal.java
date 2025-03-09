package com.rs.game.world.entity.npc.pest;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.content.activities.PestControl;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class PestPortal extends NPC {

    boolean isLocked;
    PestControl control;
    int ticks;

    public PestPortal(int id, boolean canbeAttackedOutOfArea, Position tile, PestControl control) {
	super(id, tile, -1, 0, canbeAttackedOutOfArea, true);
	this.control = control;
	setCantFollowUnderCombat(true);
	setForceMultiArea(true);
	setDamageCap(400);
	isLocked = true;
    }

    public boolean isLocked() {
	return isLocked;
    }

    public void unlock() {
	if (getId() >= 6146) {
	    setNextNPCTransformation(getId() - 4);
	    control.sendTeamMessage(getStringForId() + " portal shield has been dropped!");
	}
	this.isLocked = false;
    }

    private String getStringForId() {
	switch (getId()) {
	    case 6142:
		return "The purple, western";
	    case 6143:
		return "The blue, eastern";
	    case 6144:
		return "The yellow, south-eastern";
	    case 6145:
		return "The red, south-western";
	}
	return "THIS SHOULDN'T EVER HAPPEN.";
    }

    private int getIndexForId() {
	switch (getId()) {
	    case 6146:
	    case 6142:
		return 0;
	    case 6147:
	    case 6143:
		return 1;
	    case 6148:
	    case 6144:
		return 2;
	    case 6149:
	    case 6145:
		return 3;
	    case 3782:
	    case 3784:
	    case 3785:
		return 4;
	}
	return -1;
    }

    @Override
    public void processNPC() {
	super.processNPC();
	ticks++;
	if (ticks % 15 == 0) {
	    if (control.createPestNPC(getIndexForId())) {
		if (Utils.random(5) == 0) // double spawn xD
		    control.createPestNPC(getIndexForId());
	    }
	}
	if (isDead() || isLocked)
	    return;
	cancelFaceEntityNoCheck();
    }

    @Override
    public void sendDeath(Entity source) {
	final NPCCombatDefinitions defs = getCombatDefinitions();
	resetWalkSteps();
	animate(null);
	WorldTasksManager.schedule(new WorldTask() {
	    int loop;

	    @Override
	    public void run() {
		if (loop == 0) {
		    animate(new Animation(defs.getDeathEmote()));
		} else if (loop >= defs.getDeathDelay()) {
		    if (getIndexForId() != 4) {
			control.unlockPortal();
			control.getKnight().heal(500);
		    }
		    finish();
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
    }
}