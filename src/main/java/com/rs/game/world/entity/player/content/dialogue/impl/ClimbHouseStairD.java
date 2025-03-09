package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class ClimbHouseStairD extends Dialogue {

    private WorldObject object;
    
    @Override
    public void finish() {

    }
    
    @Override
    public void run(int interfaceId, int componentId) {
	end();
	if(componentId != OPTION_3)
	    player.getHouse().climbStaircase(object, componentId == OPTION_1, player);

    }

    @Override
    public void start() {
	this.object = (WorldObject) parameters[0];
	sendOptionsDialogue("Select a option.", "Climb up.", "Climb down.", "Cancel");

    }

}
