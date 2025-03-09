package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Herblore tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class Poletax extends Dialogue {
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
                sendNPCDialogue(npcId, 9827, "Ahaha! Hello Roman. How may I help you in this fine day?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of the Herblore skill?", "What is this place?", "I am fine, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of the Herblore skill?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 14;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "I am fine, thank you.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9850, "Sure lad!");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9850, "There are two parts to Herblore in Rome. First is cleaning the herb, the other way is to make a potion.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9850, "The first way is very easy. All you need to do is acquire an herb and click it.");
                stage = 5;
                break;
            case 5:
                sendNPCDialogue(npcId, 9850, "The other way requires you to have an herb, a secondary ingredient, and a vial of water.");
                stage = 6;
                break;
            case 6:
                sendNPCDialogue(npcId, 9850, "To find out all the types of potions and their ingredients, try the Skill Advance Guide.");
                stage = 7;
                break;
            case 7:
                sendNPCDialogue(npcId, 9850, "You can open the Skill Advance Guide by clicking the skills tab and then clicking the Herblore tab.");
                stage = 8;
                break;
            case 8:
                sendNPCDialogue(npcId, 9850, "Once you acquire the needed ingredients, its time to make the potion. If you have a Vial but not a Vial of water, use that Vial on water.");
                stage = 9;
                break;
            case 9:
                sendNPCDialogue(npcId, 9850, "Water sources can be found throughout Rome. Some of the ones I can think of are in the province of Varrock and of Lumbridge.");
                stage = 10;
                break;
            case 10:
                sendNPCDialogue(npcId, 9850, "First, you must use the herb on the Vial of water, and then use the secondary ingredient on the new 'thing' in the Vial.");
                stage = 11;
                break;
            case 11:
                sendNPCDialogue(npcId, 9850, "You will now have a new potion, made by you!");
                stage = 12;
                break;
            case 12:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 13;
                break;
            case 13:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 14;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 14:
                sendNPCDialogue(npcId, 9850, "This is the Roman's superpower province, Taverley. Here, we hold the supremacy by having us teachers here and our Guards there. Our Central Trading Hub marks our supremacy around the world!");
                stage = 15;
                break;
            case 15:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of the Herblore skill?", "Thank you for the information.");
                stage = 16;
                break;
            case 16:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of the Herblore skill?");
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
