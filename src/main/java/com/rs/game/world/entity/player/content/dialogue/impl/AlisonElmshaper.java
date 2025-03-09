package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Fletching tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class AlisonElmshaper extends Dialogue {
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
                sendNPCDialogue(npcId, 9827, "Hello Roman. How do you do? Would you like any help from me, my friend?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Fletching?", "What is this place?", "I am fine, thank you.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Fletching?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 18;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "I am fine, thank you.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9850, "Most certainly my friend!");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9850, "The basics of Fletching. Well, all I ask of you is to get a Knife and a Log.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9850, "With the Knife and the Log, you are able to make your first item!");
                stage = 5;
                break;
            case 5:
                sendNPCDialogue(npcId, 9850, "In simple words, all you need to do is use the Knife on the Logs and an interface will come in your chatbox.");
                stage = 6;
                break;
            case 6:
                sendNPCDialogue(npcId, 9850, "When this interface comes, select the item you would like to make. They will be unstrung.");
                stage = 7;
                break;
            case 7:
                sendNPCDialogue(npcId, 9851, "The higher the Fletching level, the better the items you can get. The higher the Fletching level, the better logs you can fletch.");
                stage = 8;
                break;
            case 8:
                sendNPCDialogue(npcId, 9850, "You may then string the items with a Bowstring. A Bowstring can be made from spinning flax on the Spinning wheel.");
                stage = 9;
                break;
            case 9:
                sendNPCDialogue(npcId, 9850, "Once you get this Bowstring, use it on the unstrung bow to create a fully operable bow!");
                stage = 10;
                break;
            case 10:
                sendNPCDialogue(npcId, 9850, "However, these things I taught you will be available to you at around level 5 Fletching. If you are below that, you can fletch Arrow shafts.");
                stage = 11;
                break;
            case 11:
                sendNPCDialogue(npcId, 9850, "Tell you what, I'll give you a tip.");
                stage = 12;
                break;
            case 12:
                sendNPCDialogue(npcId, 9850, "I would consider this tip fitting seeing as we are near the Central Trading Hub.");
                stage = 13;
                break;
            case 13:
                sendNPCDialogue(npcId, 9850, "Make arrow shafts at the beginning and then make arrows from them. You will be doing yourself a favor in both ways.");
                stage = 14;
                break;
            case 14:
                sendNPCDialogue(npcId, 9850, "You could either use those arrows for your own combat, or sell them for money here, at the Central Trading Hub.");
                stage = 15;
                break;
            case 15:
                sendNPCDialogue(npcId, 9850, "The choice is yours, my friend. I wish you luck at Fletching.");
                stage = 16;
                break;
            case 16:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 17;
                break;
            case 17:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 18;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 18:
                sendNPCDialogue(npcId, 9850, "This is Taverley, my friend. To the South of us is the Central Trading Hub. What a beautiful place this is, isn't it?");
                stage = 19;
                break;
            case 19:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of Fletching?", "Thank you for the information.");
                stage = 20;
                break;
            case 20:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of Fletching?");
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
