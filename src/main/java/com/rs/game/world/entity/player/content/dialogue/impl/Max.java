package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;

/**
 * Created by Arham 4 on 1/25/14.
 */
public class Max extends Dialogue {
    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        sendPlayerDialogue(9830, "Hello. Nice cape you got there...");
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case -1:
                sendNPCDialogue(npcId, 9850, "Hello there! And... oh this? Thanks! Its a symbol that I have trained all of my skills (that are trainable) to level 99.");
                stage = 0;
                break;
            case 0:
                boolean elligable = false;
                for (int i = 0; i < Skills.DUNGEONEERING + 1; i++) {
                    if (i == Skills.CONSTRUCTION || i == Skills.DUNGEONEERING || i == Skills.HUNTER || i == Skills.FARMING || i == Skills.SUMMONING) {
                        continue;
                    }
                    if (player.getSkills().getLevel(i) >= 99) {
                        elligable = true;
                    }
                }
                if (!elligable) {
                    sendPlayerDialogue(9850, "I wish I had one...", "One day I will have all 99s! You'll see!");
                    stage = -2;
                } else {
                    sendPlayerDialogue(9850, "So have I!");
                    stage = 1;
                }
                break;
            case 1:
                sendNPCDialogue(npcId, 9850, "Indeed so. Would you like a cape like mine to show that fact off? 2,475,000 coins - 99,000 for each skill!");
                stage = 2;
                break;
            case 2:
                sendOptionsDialogue("Pay 2,475,000 for a max cape?", "I'll take one!", "No, thanks.");
                stage = 3;
                break;
            case 3:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9850, "I'll take one!");
                        if (player.getInventory().getNumberOf(995) < 2475000) {
                            
                        } else {
                            if (player.getInventory().getFreeSlots() < 2) {
                                stage = 5;
                                break;
                            }
                            stage = 4;
                        }
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9830, "No, thanks.");
                        stage = -2;
                        break;
                }
                break;
            case 4:
                if (player.getInventory().getNumberOf(995) < 2475000) {
                    player.getPackets().sendRunScript(5561, 0, 2475000);
                    player.money -= 50000;
                    player.refreshMoneyPouch();
                } else {
                    player.getInventory().deleteItem(995, 2475000);
                }
                player.getInventory().addItem(new Item(20767));
                player.getInventory().addItem(new Item(20768));
                sendNPCDialogue(npcId, 9851, "Thanks. Enjoy!");
                stage = -2;
                break;
            case 5:
                player.closeInterfaces();
                player.getPackets().sendGameMessage("You don't have enough inventory space.");
                stage = -2;
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
