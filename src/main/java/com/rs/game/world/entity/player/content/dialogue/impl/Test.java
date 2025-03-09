package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * Test dialogue.
 *
 * @author Arham Siddiqui
 */
public class Test extends Dialogue {
    @Override
    public void start() {
        int animation = (int) parameters[0];
        sendPlayerDialogue(animation, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        end();
    }

    @Override
    public void finish() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
