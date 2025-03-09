package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class ClanCreation extends Dialogue {

    @Override
    public void start() {
	sendDialogue("You must be a member of a clan in order to join their channel.", "Would you like to create a clan?");

    }

    @Override
    public void run(int interfaceId, int componentId) {
		if (stage == -1) {
		    player.getTemporaryAttributtes().put("setclan", Boolean.TRUE);
		    end();
		    player.getInterfaceManager().sendChatBoxInterface(1094);
		}
		    
    }

    @Override
    public void finish() {

    }

}