package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Miazrqa.
 *
 * @author Arham Siddiqui
 */
public class Miazrqa extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        sendPlayerDialogue(9850, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == -1) {
            stage = 0;
            player.getPackets().sendVoice(11901);
            sendNPCDialogue(npcId, 9770, "Sorry friend, I am a little busy at the moment.");
        } else
            end();
    }

    @Override
    public void finish() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
