package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

/**
 * @author Arham Siddiqui
 */
public class ShopOwner extends Dialogue {
    private int npcId;
    private int shopId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        shopId = (int) parameters[1];
        sendPlayerDialogue(9830, "Hello!");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9830, "Hello good sir. Would you like to see what I have for sale?");
                stage = 0;
                break;
            case 0:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, yes I do.", "Never mind, I changed my mind.");
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9830, "Yes, yes I do.");
                        stage = 2;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "Never mind, I changed my mind.");
                        stage = -2;
                        break;
                }
                break;
            case 2:
                player.closeInterfaces();
                ShopsHandler.openShop(player, shopId);
                break;
            default:
                player.closeInterfaces();
                break;
        }
    }

    @Override
    public void finish() {

    }
}
