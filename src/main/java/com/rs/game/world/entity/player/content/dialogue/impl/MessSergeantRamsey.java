package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

/**
 * The Cooking tutors dialogue
 *
 * @author Jonathan
 *         <p/>
 *         14881 - Mess Sergeant Ramsey
 */

public class MessSergeantRamsey extends Dialogue {

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
                sendNPCDialogue(npcId, 9827, "Bonjour monsieur, how may I help you today?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Cooking?", "What is this place?", "Can I please see your shop?", "Uhh.. I have to go.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Cooking?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 7;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9827, "Can I please see what's inside your shop?");
                        stage = 11;
                        break;
                    case OPTION_4:
                        sendPlayerDialogue(9827, "Uhh.... I have to go.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9827, "Well of course I will! Monsieur the Cooking skill is an unordinary skill.");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9827, "You must use resources from the Fishing Skill to get resources for cooking!");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9827, "Although you can also make Pies, Cakes, and such without Fishing.");
                break;
            case 5:
                sendNPCDialogue(npcId, 9827, "The Cooking guild south of the Grand Exchange is the fastest bank to furnace known in Rome!");
                stage = 5;
                break;
            case 6:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 6;
                break;
            case 7:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 7;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "Thank you for the information!");
                        stage = -2;
                        break;
                }
                break;
            case 8:
                sendNPCDialogue(npcId, 9827, "Why this is Taverly! Here you can learn from the tutors and interact with the community. We also have many shops located around here for you to buy your equipment.");
                stage = 8;
                break;
            case 9:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Cooking?", "Thank you for the information.");
                stage = 9;
                break;
            case 10:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Cooking?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "Thank you for the information!");
                        stage = -2;
                        break;
                }
                break;
            case 11:
                sendNPCDialogue(npcId, 9827, "Why of course monsieur!");
                stage = 12;
                break;
            case 12:
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
