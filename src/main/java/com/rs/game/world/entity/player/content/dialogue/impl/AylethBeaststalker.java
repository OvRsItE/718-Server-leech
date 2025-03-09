package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Hunter tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class AylethBeaststalker extends Dialogue {
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
                sendNPCDialogue(npcId, 9850, "Hello Roman! Keep this fast, I'm looking for that pesky bird!");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Hunter?", "What is this place?", "There are birds all around though...", "I am fine, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Hunter?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 8;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9827, "There are birds all around though...");
                        stage = 11;
                        break;
                    case OPTION_4:
                        sendPlayerDialogue(9850, "I am fine, thank you.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9850, "Sure thing! The last thing we need are people dying from starvation because they can't hunt...");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9850, "You must have the keen sense to get a bird at first. Setting up a snare, a bird trap, or any sort of trap with grab those pests.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9850, "Try your best to set them up and wait for the enemy. When they go in the trap, take'em by clicking on your set trap.");
                stage = 5;
                break;
            case 5:
                sendNPCDialogue(npcId, 9851, "Enjoy your monster's raw meat!");
                stage = 6;
                break;
            case 6:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 7;
                break;
            case 7:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 8;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 8:
                sendNPCDialogue(npcId, 9850, "This is the main attraction of Rome, Taverley. Here, we Hunters see this as Raw bird meat heaven.");
                stage = 9;
                break;
            case 9:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Hunter?", "Thank you for the information.");
                stage = 10;
                break;
            case 10:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Hunter?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 11:
                sendNPCDialogue(npcId, 9850, "Yes, but I am looking for a specific bird! It has the most tender meat!");
                stage = 12;
                break;
            case 12:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Hunter?", "What is this place?", "Okay...");
                stage = 13;
                break;
            case 13:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Hunter?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 8;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9838, "Okay...");
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
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
