package com.rs.game.world.entity.player.content.dialogue.impl;


import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;


/**
 * @Author Chaz - Jul 14, 2013
 * <p/>
 * Lumbridge Sage's Dialogue.
 */
public class Phileas extends Dialogue {


    int npcId;


    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendEntityDialogue(
                SEND_1_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Greetings, adventurer. How may I help you?"},
                IS_NPC, npcId, 9827);
    }


    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                stage = 0;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Who are you?",
                        "Tell me about the town of Lumbridge.", "I'm fine for now thanks.");
                break;
            case 0:
                switch (componentId) {
                    case OPTION_1:
                        stage = 1;
                        sendPlayerDialogue(9827, "Who are you?");
                        break;
                    case OPTION_2:
                        stage = 3;
                        sendPlayerDialogue(9827, "Tell me about the town of Lumbridge.");
                        break;
                    case OPTION_3:
                        stage = 4;
                        sendPlayerDialogue(9827, "I'm fine for now thanks.");
                        break;
                }
                break;
            case 1:
                stage = 2;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "I am Phileas, the Lumbridge Sage. In times past, people came "
                                + "from all around to ask me for advice. My renown seems to have "
                                + "diminished somewhat in recent years, though. Can I help you with anything?");
                break;
            case 2:
                stage = 3;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Tell me about the town of Lumbridge.",
                        "I'm fine for now thanks.");
                break;
            case 3:
                stage = 4;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "Lumbridge is one of the older towns in the human-controlled kingdoms. "
                                + "It was founded over two hundred years ago towards the end of the Fourth Age. "
                                + "It's called Lumbridge because of this bridge built over the River Lum. "
                                + "The town is governed by Duke Horacio, who is a good friend of our monarch, King Roald of Misthalin.");
                break;
            case 4:
            default:
                end();
                break;
        }
    }


    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }


}