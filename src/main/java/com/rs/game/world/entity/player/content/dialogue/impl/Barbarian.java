package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.Utils;

/**
 * Talking to barbarians.
 * Created by Arham 4 on 3/13/14.
 */
public class Barbarian extends Dialogue {
    private int npcId;
    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendPlayerDialogue(9830, "Hello barbarian.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                int random = Utils.random(3);
                switch (random) {
                    case 0:
                        sendNPCDialogue(npcId, 9781, "Hello Roman pest.");
                        stage = 0;
                        break;
                    case 1:
                        sendNPCDialogue(npcId, 9789, "I AM PART OF THE CHERUSCI! WE HATE ROMANS LIKE YOU!");
                        stage = 0;
                        break;
                    default:
                        sendNPCDialogue(npcId, 9830, "Roman...");
                        stage = 0;
                        break;
                }
                break;
            case 0:
                sendPlayerDialogue(9804, "Okay...");
                stage = -2;
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
