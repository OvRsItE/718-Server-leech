package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.social.clanchat.ClansManager;

public class LeaveClan extends Dialogue {

    @Override
    public void start() {
	sendDialogue("If you leave the clan, you will need to be invited before you  can join again, and must wait a week before you contribute to clan resources.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
	switch (stage) {
	    case -1:
		stage = 0;
		sendOptionsDialogue("Really leave the clan?", "Yes, leave the clan.", "No, I will remain in the clan.");
		break;
	    case 0:
		if (componentId == OPTION_1)
		    ClansManager.leaveClanCompletly(player);
		end();
		break;
	    default:
		end();
		break;
	}

    }

    @Override
    public void finish() {

    }

}
