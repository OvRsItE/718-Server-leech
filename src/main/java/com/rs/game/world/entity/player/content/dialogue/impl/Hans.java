package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * Created by Arham 4 on 1/24/14.
 */
public class Hans extends Dialogue {
    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        sendPlayerDialogue(9827, "Hello sir. May I ask you something?");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9827, "Yes?");
                stage = 0;
                break;
            case 0:
                sendPlayerDialogue(9827, "What do you do here?");
                stage = 1;
                break;
            case 1:
                sendNPCDialogue(npcId, 9830, "I'm on patrol. I've been patrolling this castle for years!");
                stage = 2;
                break;
            case 2:
                sendPlayerDialogue(9827, "You must be old then?");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(npcId, 9851, "Haha, you could say I'm quite the veteran of these lands. Yes, " +
                        "I've been here a fair while...");
                stage = 4;
                break;
            case 4:
                if (player.getMonths() < 3 && player.getYears() < 1) {
                    sendNPCDialogue(npcId, 9827, "You have " + (90 - player.getDays()) + " more days until you are a " +
                            "veteran.");
                    stage = -2;
                } else {
                    sendNPCDialogue(npcId, 9827, "And it looks like you've been here for a decent amount of time too! " +
                            "I can sell you a cape that shows that you are a veteran too, if you'd like - on 50," +
                            "000 coins!");
                    stage = 5;
                }
                break;
            case 5:
                sendOptionsDialogue("Pay 50,000 for a veteran cape?", "I'll take one!", "No, thanks.");
                stage = 3;
                break;
            case 6:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I'll take one!");
                        if (player.getInventory().getNumberOf(995) < 50000) {
                         
                        } else {
                            if (player.getInventory().getFreeSlots() < 2) {
                                stage = 8;
                                break;
                            }
                            stage = 7;
                        }
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9830, "No, thanks.");
                        stage = -2;
                        break;
                }
                break;
            case 7:
                if (player.getInventory().getNumberOf(995) < 50000) {
                    player.getPackets().sendRunScript(5561, 0, 50000);
                    player.money -= 50000;
                    player.refreshMoneyPouch();
                } else {
                    player.getInventory().deleteItem(995, 50000);
                }
                player.getInventory().addItem(new Item(20763));
                player.getInventory().addItem(new Item(20764));
                sendNPCDialogue(npcId, 9830, "Thanks. Enjoy!");
                stage = -2;
                break;
            case 8:
                player.closeInterfaces();
                player.getPackets().sendGameMessage("You don't have enough inventory space.");
                stage = -2;
                break;
        }

    }

    @Override
    public void finish() {

    }
}
