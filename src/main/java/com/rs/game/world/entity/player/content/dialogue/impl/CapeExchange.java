package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class CapeExchange extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        sendOptionsDialogue("TokHaar-Kal Exchange","Tokhaar-Kal", "TokHaar-Kal-Mej", "TokHaar-Kal-Xil");


    }

    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == 1 ) {
            if (componentId == OPTION_1) {
                if (player.getInventory().containsItem(23659, 1)) {
                    player.getInventory().addItem(29886, 1);
                    player.getInventory().deleteItem(23659, 1);
                    end();
                }
            }
            if (componentId == OPTION_2) {
                if (player.getInventory().containsItem(23659, 1)) {
                    player.getInventory().addItem(29887, 1);
                    player.getInventory().deleteItem(23659, 1);
                    end();
                }
            }
            if (componentId == OPTION_3 ) {
                if (player.getInventory().containsItem(23659, 1)) {
                    player.getInventory().addItem(29888, 1);
                    player.getInventory().deleteItem(23659, 1);
                    end();
                }
            }
        }

    }

    @Override
    public void finish() {

    }
}
