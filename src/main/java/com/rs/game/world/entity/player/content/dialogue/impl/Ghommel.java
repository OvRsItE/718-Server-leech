package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Ghommel extends Dialogue {

    private int npcId = 8266;

    @Override
    public void start() {
	sendNPCDialogue(npcId, NORMAL, "Ghommel welcome you to Warrior Guild!");
    }

    @Override
    public void run(int interfaceId, int componentId) {
	stage++;
	if (stage == 0)
	    this.sendPlayerDialogue(NORMAL, "Uhmm... thank you, I think.");
	else if (stage == 1)
	    end();
    }

    @Override
    public void finish() {

    }
}
