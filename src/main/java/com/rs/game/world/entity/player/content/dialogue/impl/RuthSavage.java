package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;

/**
 * The Thieving tutors dialogue
 *
 * @author Jonathan
 *         <p/>
 *         TODO:Correct Facial Animation
 *         TODO:Fairy Ring Teleport to Rogues Den
 */
public class RuthSavage extends Dialogue {

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
                sendNPCDialogue(npcId, 9827, "Ruth's the name, Thieving's the game. What can I help you with Roman?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Thieving?", "What is this place?", "I am fine, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Thieving?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 21;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9830, "I am fine, thank you.");
                        stage = -2;
                        break;

                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9827, "Thieving is a very easy skill Roman, the key part is not to get caught!");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9827, "There are many ways to do this, although you must build onto the skill to obtain these other opportunities.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9827, "For instance below me there is an area called Rogue's Den. There you can steal from a wall safe for loot.");
                stage = 5;
                break;
            case 5:
                sendNPCDialogue(npcId, 9827, "In order to go there you must have a Thieving level of 40 or greater! ");
                stage = 6;
                break;
            case 6:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "How can I train as a low level?", "How do I get to Rogues Den?", "What type of loot can I recieve?", "Thank you for the information.");
                stage = 7;
                break;
            case 7:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "How can I train Thieving at such a low level?");
                        stage = 8;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "How do I get to Rogues Den?");
                        stage = 13;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9827, "What types of loot can I recieve at Rogues Den?");
                        stage = 17;
                        break;
                    case OPTION_4:
                        sendPlayerDialogue(9827, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 8:
                sendNPCDialogue(npcId, 9827, "For starters, I would suggest pickpocketing Men or Farmers depending on your level.");
                stage = 9;
                break;
            case 9:
                sendNPCDialogue(npcId, 9827, "There is also the Ardougne Thieving Center, of which you can Thieve from stalls to train your Thieving.");
                stage = 11;
                break;
            case 11:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "How do I get to Rogues Den?", "What type of loot can I recieve?", "No ma'am.");
                stage = 12;
                break;
            case 12:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "How do I get to Rogues Den?");
                        stage = 13;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What types of loot can I recieve at Rogues Den?");
                        stage = 17;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9827, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 13:
                sendNPCDialogue(npcId, 9827, "To get to Rogues Den, one must have 40 Thieving as said before. The Rogue's Den is directly below me.");
                stage = 15;
                break;
            case 15:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "How can I train as a low level?", "What type of loot can I recieve?", "No ma'am.");
                stage = 16;
                break;
            case 16:
                switch (componentId) {
                    case OPTION_1:
                        if (player.getSkills().getLevel(Skills.THIEVING) < 5)
                            sendPlayerDialogue(9827, "How can I train Thieving at such a low level?");
                        else
                            sendPlayerDialogue(9827, "How can I train Thieving at a low level?");
                        stage = 8;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What types of loot can I recieve at Rogues Den?");
                        stage = 17;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9827, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 17:
                sendNPCDialogue(npcId, 9827, "Loot at Rogues Den are usually Uncut Gems and Coins.");
                stage = 18;
                break;
            case 18:
                sendNPCDialogue(npcId, 9827, "You should also have food in your inventory just in case!");
                stage = 19;
                break;
            case 19:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "How can I train as a low level?", "How do I get to Rogues Den?", "No ma'am.");
                stage = 20;
                break;
            case 20:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "How can I train Thieving at such a low level?");
                        stage = 8;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "How do I get to Rogues Den?");
                        stage = 13;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9830, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 21:
                sendNPCDialogue(npcId, 9827, "This is Taverly, the central area of Rome.");
                stage = 22;
                break;
            case 22:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Thieving?", "Thank you for the information.");
                stage = 23;
                break;
            case 23:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Thieving?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9830, "Thank you for the information.");
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
