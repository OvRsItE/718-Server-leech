package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class SlayerTutor extends Dialogue {

    private NPC npc;
    private int npcId;

    @Override
    public void start() {
        npc = (NPC) parameters[0];
        npcId = npc.getId();
        sendPlayerDialogue(9850, "Hello.");

    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9827, "Hi there! My name is Jacquelyn Manslaughter and I'm the Slayer tutor here in Rome!");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Teach me about Slayer.", "What is this place?", "Nevermind.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Slayer?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 10;
                        break;
                    case OPTION_3:
                        sendNPCDialogue(npcId, 9827, "Nevermind, I have to go.");
                        stage = -2;
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
        // TODO Auto-generated method stub

    }

}
