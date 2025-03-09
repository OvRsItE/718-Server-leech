package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The warning message for entering Skeletal Wyverns.
 *
 * @author Arham Siddiqui
 */
public class SkeletalWyverns extends Dialogue {
    @Override
    public void start() {
        sendDialogue("STOP! The creatures in this cave are VERY dangerous. Are you sure you want to enter?");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, I'm not afraid of death!", "No thanks, I don't want to die!");
                stage = 0;
                break;
            case 0:
                switch (componentId) {
                    case OPTION_1:
                        player.closeInterfaces();
                        player.setNextPosition(new Position(3056, 9555, 0));
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
