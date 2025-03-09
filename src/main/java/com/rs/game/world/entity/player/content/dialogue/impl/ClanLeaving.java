package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.social.clanchat.ClansManager;

public class ClanLeaving extends Dialogue {

    @Override
    public void start() {
    	sendDialogue("If you leave the current clan, you will not be able to join back for a week, are you sure you wan't to leave the clan you're currently in?");
    }

    @Override
    public void run(int interfaceId, int componentId) {
		switch (stage) {
		    case -1:
			stage = 0;
			sendOptionsDialogue("Select an Option", "Yes, leave my current clan.", "No, stay in my current clan.");
			break;
		    case 0:
			if (componentId == OPTION_1) {
				ClansManager.leaveClanCompletly(player);
				end();
			}
			else if (componentId == OPTION_2) {
				player.getPackets().sendGameMessage("You decide to stay in the clan you're currently in.");
				end();
			}
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
