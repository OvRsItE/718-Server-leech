package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;


/**
 * @Author Chaz - Jul 13, 2013
 * <p/>
 * Herquin's Jewl Shop Dialogue.
 */
public class Herquin extends Dialogue {


    int npcId;


    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendEntityDialogue(
                SEND_1_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Do you wish to trade?"},
                IS_NPC, npcId, 9827);
    }


    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                stage = 0;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                        "Sorry, I don't want to talk to you actually.",
                        "Why, yes, this is a jewel shop after all.");
                break;
            case 0:
                switch (componentId) {
                    case OPTION_1:
                        stage = 1;
                        sendPlayerDialogue(9827,
                                "Sorry, I don't want to talk to you actually.");
                        break;
                    case OPTION_2:
                        stage = 2;
                        sendPlayerDialogue(9827,
                                "Why, yes, this is a jewel shop after all.");
                        break;
                }
                break;
            case 1:
            default:
                end();
                break;
            case 2:
                end();
                break;
        }
    }


    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }


}