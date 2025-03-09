package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The Woodcutting tutor's dialogue.
 *
 * @author Arham Siddiqui
 */
public class WillOakfeller extends Dialogue {
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
                sendNPCDialogue(npcId, 9827, "Hello Roman! What brings you to me at this time? I was trading with Merchants here at the Central Trading Hub!");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of the Woodcutting skill?", "What is this place?", "Nothing, nothing at all.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of the Woodcutting skill?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "What is this place?");
                        stage = 7;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9850, "Nothing, nothing at all.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9827, "Hmm. I guess I shall.");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9850, "There is really nothing to it Roman. All you need to do is get an axe for your appropriate level.");
                stage = 4;
                break;
            case 4:
                sendNPCDialogue(npcId, 9850, "Woodcutting teaches you the power of the tree. The more higher your experience becomes, the more higher likelihood you will inherit the products of the trees.");
                stage = 5;
                break;
            case 5:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "What is this place?", "Thank you for the information.");
                stage = 6;
                break;
            case 6:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "What is this place?");
                        stage = 7;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Thank you for the information.");
                        stage = -2;
                        break;
                }
                break;
            case 7:
                sendNPCDialogue(npcId, 9850, "This is the city of Taverley, the supremacy of Rome! Over here is the magnificent Central Trading Hub, where you can bargain with other Romans for items.");
                stage = 8;
                break;
            case 8:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you teach me the basics of the Woodcutting skill?", "Thank you for the information.");
                stage = 9;
                break;
            case 9:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Can you teach me the basics of the Woodcutting skill?");
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
