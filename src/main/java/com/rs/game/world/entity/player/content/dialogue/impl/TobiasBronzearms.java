package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The mining tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class TobiasBronzearms extends Dialogue {
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
                sendNPCDialogue(npcId, 9850, "Hello there Roman. My name is Tobias, Tobias Bronzearms. I am the Mining tutor around here. May I help you?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Mining?", "What is the place?", "I am fine, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Mining?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 9;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "I am fine, thank you.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9850, "I see fellow Roman. Mining is a skill requiring power, and tons of it.");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9850, "My skill will not only benefit you to make armour for Martin's skill, but for life in Rome.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9850, "Mining will teach you how to control your power to get ore. The more experience you get, the more ore.");
                stage = 5;
                break;
            case 5:
                sendNPCDialogue(npcId, 9850, "It's very simple. All you need to do is click an ore. They are basically rock-looking. Once you do, you will start mining.");
                stage = 6;
                break;
            case 6:
                sendNPCDialogue(npcId, 9850, "The rock will, as time passes, run out. Depending on the ore, the time can vary from short to very long.");
                stage = 7;
                break;
            case 7:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 8;
                break;
            case 8:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 9;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 9:
                sendNPCDialogue(npcId, 9850, "You are currently in the magnificent, epicenter of Rome. We have tutors for all types of skills, all here for your purpose. Trading here? Not a problem! We have that handled at the Central Trading Hub not too far from here.");
                stage = 10;
                break;
            case 10:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Mining?", "Thank you for the information.");
                stage = 11;
                break;
            case 11:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Mining?");
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
