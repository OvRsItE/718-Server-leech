package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class Lidio extends Dialogue {

    private int npcId = 4293;

    @Override
    public void start() {
	sendNPCDialogue(npcId, NORMAL, "Greetings Warrior, how can I fill your stomach today?");
    }

    @Override
    public void run(int interfaceId, int componentId) {
	stage++;
	if (stage == 0)
	    sendPlayerDialogue(NORMAL, "With food preferably.");
	else if (stage == 1) {
	    end();

	}
    }

    @Override
    public void finish() {

    }
}
