package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Prayer tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class ChaplainSarah extends Dialogue {
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
                sendNPCDialogue(npcId, 9827, "All praise be to Saradomin! How may I help you?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Prayer?", "What is this place?", "I am fine, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Prayer?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 12;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "I am fine, thank you.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9775, "Saradomin, saradomin, saradomin! Step one is to never accept Zamorak!");
                stage = 3;
                break;
            case 3:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What if I accept Zamorak?", "Okay, I got it!");
                stage = 4;
                break;
            case 4:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What if I accept Zamorak?");
                        stage = 5;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Okay, I got it!");
                        stage = 6;
                        break;
                }
                break;
            case 5:
                sendNPCDialogue(npcId, 9790, "Then be gone from my sight!");
                stage = -2;
                break;
            case 6:
                sendNPCDialogue(npcId, 9850, "Thank goodness you accept Saradomin Roman. Most people from here accept filthy Zamorak...");
                stage = 7;
                break;
            case 7:
                sendNPCDialogue(npcId, 9850, "To train your sincerity to Saradomin, or what you know as Prayer, you must bury bones in honor of he.");
                stage = 8;
                break;
            case 8:
                sendNPCDialogue(npcId, 9850, "With each bone, think of what Saradomin has blessed you with here.");
                stage = 9;
                break;
            case 9:
                sendNPCDialogue(npcId, 9850, "Use Altars, such as this one behind me, to recharge your Prayer. The Altar is a direct conversation with Saradomin, remember that!");
                stage = 10;
                break;
            case 10:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 11;
                break;
            case 11:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 12;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 12:
                sendNPCDialogue(npcId, 9850, "This is the center of Rome. Here, sadly, most people are followers of Zamorak...");
                stage = 13;
                break;
            case 13:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Prayer?", "Thank you for the information.");
                stage = 14;
                break;
            case 14:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Prayer?");
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
