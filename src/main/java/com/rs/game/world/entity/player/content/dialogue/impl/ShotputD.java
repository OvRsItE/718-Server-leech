package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.content.activities.WarriorsGuild;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;

public class ShotputD extends Dialogue {

    private boolean is18LB;

    @Override
    public void start() {
	is18LB = (boolean) this.parameters[0];
	player.animate(new Animation(827));
	WorldTasksManager.schedule(new WorldTask() {

	    @Override
	    public void run() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Standing Throw.", "Step and throw.", "Spin and throw.");
	    }
	});
    }

    @Override
    public void run(int interfaceId, int componentId) {
	Controller controler = player.getControlerManager().getControler();
	if (controler == null || !(controler instanceof WarriorsGuild)) {
	    end();
	    return;
	}
	WarriorsGuild currentGuild = (WarriorsGuild) controler;
	if (componentId == OPTION_1) {
	    currentGuild.prepareShotput((byte) 0, is18LB);
	    player.animate(new Animation(15079));
	} else if (componentId == OPTION_2) {
	    currentGuild.prepareShotput((byte) 1, is18LB);
	    player.animate(new Animation(15080));
	} else {
	    currentGuild.prepareShotput((byte) 2, is18LB);
	    player.animate(new Animation(15078));
	}
	end();
    }

    @Override
    public void finish() {

    }
}
