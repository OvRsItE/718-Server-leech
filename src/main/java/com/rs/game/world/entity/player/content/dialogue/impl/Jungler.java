package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Jungler extends Dialogue {


    int npcId;
    
    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827, "Hello, we can get you through this jungle",
        		"for a cost of 50,000. There is a great training",
        		"area located in there.");
    }


    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
        case -1:
        	stage = 0;
        	sendOptionsDialogue("Make this deal?", "Yes", "No");
        	break;
        case 0:
        	if(componentId == OPTION_1) {
        		if (player.getInventory().getCoinsAmount() >= 50000) {
        			player.getInventory().removeItemMoneyPouch(new Item(995, 50000));
        			player.setNextPosition(new Position(2865, 2933, 0));
        			 sendNPCDialogue(npcId, 9827, "Pleasure doing business with you.");
        		} else {
        			 sendNPCDialogue(npcId, 9827, "I'm sorry but you do not have enough money.");
        		}
        		stage = 4;
                break;
        	} else if(componentId == OPTION_2) {
                sendPlayerDialogue(9827, "No thanks.");
                stage = 4;
                break;
            }
        case 4:
            default:
                end();
            break;
        }
    }
    
    public void finish() {
        
    }
}