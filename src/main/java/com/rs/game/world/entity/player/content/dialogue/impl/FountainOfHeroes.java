package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author Arham Siddiqui
 */
public class FountainOfHeroes extends Dialogue {
    @Override
    public void start() {
        int itemId = (int) parameters[0];
        sendDialogue("You feel a power emanating from the fountain as it recharges all your amulets. You can now rub the amulets to teleport and wear them to give more gems whilst mining.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        player.closeInterfaces();
    }

    @Override
    public void finish() {

    }
}
