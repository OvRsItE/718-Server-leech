package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.others.Bork;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The confirmation dialogue for Bork.
 *
 * @author Arham Siddiqui
 */
public class BorkConfirmation extends Dialogue {
    @Override
    public void start() {
        sendDialogue("WARNING: The boss Bork is accessible from this portal. Are you sure you'd like to go?");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, I am fearless!", "No, I fear I am too weak.");
                stage = 0;
                break;
            case 0:
                switch (componentId) {
                    case OPTION_1:
                        player.closeInterfaces();
                        if (Bork.deadTime > System.currentTimeMillis()) {
                            player.getPackets().sendGameMessage(Bork.convertToTime());
                            return;
                        }
                        player.getControlerManager().startControler("BorkControler", 0,
                                null);
                        break;
                    case OPTION_2:
                        player.closeInterfaces();
                        break;
                }
                break;
            default:
                player.closeInterfaces();
                break;
        }
    }

    @Override
    public void finish() {

    }
}
