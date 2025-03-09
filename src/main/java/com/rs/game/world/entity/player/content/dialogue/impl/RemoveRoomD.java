package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.construction.House.RoomReference;
import com.rs.utility.Utils;

public class RemoveRoomD extends Dialogue {

    private RoomReference room;
    
    @Override
    public void start() {
	this.room = (RoomReference) parameters[0];
	sendOptionsDialogue("Remove the "+Utils.formatPlayerNameForDisplay(room.getRoom().toString())+"?", "Yes.", "No.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
	if(componentId == OPTION_1) 
	    player.getHouse().removeRoom(room);
	end();
    }

    @Override
    public void finish() {
    }

}
