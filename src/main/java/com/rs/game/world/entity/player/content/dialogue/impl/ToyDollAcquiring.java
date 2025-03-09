package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * Created by Arham 4 on 2/1/14.
 * FOR FEBRUARY 3rd TO FREBRUARY 5th
 * FOR THE HOLIDAY COMPITALIA!
 */
public class ToyDollAcquiring extends Dialogue {
    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        sendPlayerDialogue(9830, "Hello.");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9850, "Why hello there Roman! You know, we are celebrating Compitalia here in Rome.");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Compitalia?", "Ooo. Anything I can get as a... gift..?", "Oh, well that's cool.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Compitalia?");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "Oh wow. Anything I can get as a... gift..?");
                        stage = 6;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(9830, "Oh, well that's cool.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                sendNPCDialogue(npcId, 9850, "Yes Roman! Compitalia! You know, the festival that marks the end of the agricultural year in Rome!");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9850, "In the days of Compitalia, we Romans honor the lares compitales, or the protective spirits of neighborhoods.");
                stage = 4;
                break;
            case 4:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Oh wow. Anything I can get as... a gift..?", "Oh, nice.");
                stage = 5;
                break;
            case 5:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "Oh wow. Anything I can get as... a gift..?");
                        stage = 6;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9830, "Oh, nice.");
                        stage = -2;
                        break;
                }
                break;
            case 6:
                sendNPCDialogue(npcId, 9850, "Sure thing friend! Two gifts, you chose one. One is a toy doll, another is a ball of wool. Which would you like?");
                stage = 7;
                break;
            case 7:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "I want the toy doll!", "I want the ball of wool.");
                stage = 8;
                break;
            case 8:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I want the toy doll!");
                        stage = 9;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9850, "I want the ball of wool.");
                        stage = 12;
                        break;
                }
                break;
            case 9:
                sendNPCDialogue(npcId, 9850, "Sure thing Roman! Here you go!");
                stage = 10;
                break;
            case 10:
                sendDialogue("The " + NPCDefinitions.getNPCDefinitions(npcId).name + " hands you a toy doll.");
                player.getInventory().addItem(new Item(7763));
                stage = 11;
                break;
            case 11:
                sendNPCDialogue(npcId, 9850, "Happy day today it is surely Roman!");
                stage = -2;
                break;
            case 12:
                sendNPCDialogue(npcId, 9850, "Sure thing Roman! Here you go!");
                stage = 13;
                break;
            case 13:
                sendDialogue("The " + NPCDefinitions.getNPCDefinitions(npcId).name + " hands you a ball of wool.");
                player.getInventory().addItem(new Item(1759));
                stage = 14;
                break;
            case 14:
                sendNPCDialogue(npcId, 9850, "Happy day today Roman!");
                stage = -2;
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
