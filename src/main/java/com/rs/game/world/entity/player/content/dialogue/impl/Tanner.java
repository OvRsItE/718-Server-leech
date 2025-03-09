package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * Created by Arham 4 on 3/8/14.
 */
public class Tanner extends Dialogue {
    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendPlayerDialogue(9830, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9850, "Hello! How may I help you today?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Talk about tanning.", "Never mind.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Could you tan the hide I have?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9830, "Uh, never mind.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9850, "Ah! I am the tanner of this area. I can tan your hide for... a fee.");
                stage = 3;
                break;
            case 3:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Sounds good to me!", "How much?", "Hah! I can do better!");
                stage = 4;
                break;
            case 4:
                switch (componentId) {
                    case OPTION_1:
                        end();
                        player.getDialogueManager().startDialogue("TanningD", npcId);
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "How much are your services?");
                        stage = 5;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9840, "Hah! I can do better!");
                        stage = -2;
                        break;
                }
                break;
            case 5:
                sendNPCDialogue(npcId, 9850, "Soft leather is for free. Hard leather is for 3 coins. Everything else is 20 coins. I can from snakeskin to royal dragon leather.");
                stage = 6;
                break;
            case 6:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Sounds good to me!", "Hah! I can do better!");
                stage = 7;
                break;
            case 7:
                switch (componentId) {
                    case OPTION_1:
                        end();
                        player.getDialogueManager().startDialogue("TanningD", npcId);
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9840, "Hah! I can do better!");
                        stage = -2;
                        break;
                }
                break;
            default:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }
}
