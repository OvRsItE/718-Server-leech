package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The smithing tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class MartinSteelweaver extends Dialogue {
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
                sendNPCDialogue(npcId, 9850, "Hello there Roman! My name is Martin. Is there any way I can assist you?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Smithing?", "What is this place?", "I am fine, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Smithing?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 19;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "I am fine, thank you.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9851, "Smithing? You have those puny little arms and think you can smith?");
                stage = 3;
                break;
            case 3:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "You also have puny little arms! In fact, you are shorter!", "Come on! Teach me!");
                stage = 4;
                break;
            case 4:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9790, "You also have puny little arms! In fact, you are shorter than me!");
                        stage = 5;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9760, "Come on! Teach me!");
                        stage = 6;
                        break;
                }
                break;
            case 5:
                sendNPCDialogue(npcId, 9780, "Oh my! I am sorry! I was just joking!");
                stage = 7;
                break;
            case 6:
                sendNPCDialogue(npcId, 9851, "Hmm. I guess I will fellow Roman.");
                stage = 7;
                break;
            case 7:
                sendNPCDialogue(npcId, 9850, "There are two ways to smith in Rome. Both are linked to each other.");
                stage = 8;
                break;
            case 8:
                sendNPCDialogue(npcId, 9850, "For the first way, you require ore. Go and ask Tobias, my lad, there for more information about mining.");
                stage = 9;
                break;
            case 9:
                sendNPCDialogue(npcId, 9850, "Alright, so once you get ore, you must click on the Furnace. Once you do, select the type of bar you would like to make.");
                stage = 10;
                break;
            case 10:
                sendNPCDialogue(npcId, 9850, "To find what ore should be used with another to make a certain bar, try checking out the Skill Advance Guides.");
                stage = 11;
                break;
            case 11:
                sendNPCDialogue(npcId, 9850, "To view the Skill Advance Guide, you must click the Smithing icon in the Skills tab.");
                stage = 12;
                break;
            case 12:
                sendNPCDialogue(npcId, 9851, "That is one way. The other one, in fact, required the first one way's product, a bar!");
                stage = 13;
                break;
            case 13:
                sendNPCDialogue(npcId, 9850, "All you have to do is click on the Anval and an interface with pop up.");
                stage = 14;
                break;
            case 14:
                sendNPCDialogue(npcId, 9850, "In this interface, you must select an item you want to smith.");
                stage = 15;
                break;
            case 15:
                sendNPCDialogue(npcId, 9850, "Different types of items require more or less bars or smithing level.");
                stage = 16;
                break;
            case 16:
                sendNPCDialogue(npcId, 9850, "Generally, daggers, swords, and other things require less bars and smithing level. However, more advanced armour, such as platebodies and platelegs, require more bars and levels.");
                stage = 17;
                break;
            case 17:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 18;
                break;
            case 18:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 19;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 19:
                sendNPCDialogue(npcId, 9850, "Well lad, you are in the city of Taverley. Central Trading Hub, the proclaimed 'main city' of Rome, we are renown for all those good stuff. Our teaching services for skilling are top class, and free.");
                stage = 20;
                break;
            case 20:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Smithing?", "Thank you for the information.");
                stage = 21;
                break;
            case 21:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Smithing?");
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
