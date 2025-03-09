package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The dialogue that is the default for every NPC.
 *
 * @author Arham Siddiqui
 */
public class DefaultDialogue extends Dialogue {
    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        sendPlayerDialogue(9830, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9830, "Hello Roman!");
                stage = -2;
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
