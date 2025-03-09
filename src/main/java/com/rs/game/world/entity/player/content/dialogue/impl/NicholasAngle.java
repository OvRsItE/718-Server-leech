package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Fishing tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class NicholasAngle extends Dialogue {
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
                sendNPCDialogue(npcId, 9850, "Hello there younglin' Roman. I'm fishin' around here. What do you want me from me?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Fishing?", "What is this place?", "Nothing, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Fishing?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 8;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "Nothing, thank you.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9850, "Eh, fish tutor gotta do his duty. So aight.");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9850, "Catchin' them fish is some hard darn duty.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9851, "What would you know? All you gotta do is click the \"Fishing spot\", the real work is what your player is doing.");
                stage = 5;
                break;
            case 5:
                sendNPCDialogue(npcId, 9850, "The higher your experience gets, the more better fish ye can catch.");
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
                sendNPCDialogue(npcId, 9850, "This is the land of Taverley. Renown for being the homeland of great Rome, having the main Central Trading Hub of Rome, and having Skilling teachers, such as I, we are proud to be one of the best city in Rome.");
                stage = 9;
                break;
            case 9:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Fishing?", "Thank you for the information.");
                stage = 10;
                break;
            case 10:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Fishing?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
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
