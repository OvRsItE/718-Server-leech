package com.rs.game.world.entity.player.content.dialogue.impl;


import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;


/**
 * @Author Josh' - Jul 13, 2013
 * <p/>
 * Flynn's Mace Market Dialogue.
 */
public class Flynn extends Dialogue {


    private int npcId;


    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendEntityDialogue(
                SEND_1_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Hello. Do you want to buy or sell any maces?"},
                IS_NPC, npcId, 9827);
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                stage = 0;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "No, thanks.",
                        "Well, I'll have a look, at least.");
                break;
            case 0:
                switch (componentId) {
                    case OPTION_1:
                        stage = 1;
                        sendPlayerDialogue(9827, "No, thanks.");
                        break;
                    case OPTION_2:
                        stage = 2;
                        sendPlayerDialogue(9827, "Well, I'll have a look, at least.");
                        break;
                }
                break;
            case 1:
            default:
                end();
                break;
            case 2:
                end();
        }
    }

    public void finish() {

    }
}