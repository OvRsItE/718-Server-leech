package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Islands extends Dialogue {


    int npcId;
    
    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827, "Hello, which island would you like to travel to?");
    }


    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
        case -1:
        	stage = 0;
        	sendOptionsDialogue("Pick an Island.", "Waterbirth", "Miscellania", "Lunar", "Neitznot", "Jatizso");
        	break;
        case 0:
        	if(componentId == OPTION_1) {
        		player.setNextPosition(new Position(2547, 3761, 0));
        		sendNPCDialogue(npcId, 9827, "Good luck on your travels adventurer.");
        		stage = 4;
                break;
        	} else if(componentId == OPTION_2) {
    			player.setNextPosition(new Position(2581, 3845, 0));
    			sendNPCDialogue(npcId, 9827, "Good luck on your travels adventurer.");
    			stage = 4;
            break;
        	} else if(componentId == OPTION_3) {
    			player.setNextPosition(new Position(2118, 3895, 0));
    			sendNPCDialogue(npcId, 9827, "Good luck on your travels adventurer.");
    			stage = 4;
            break;
        	} else if(componentId == OPTION_4) {
    			player.setNextPosition(new Position(2311, 3780, 0));
    			sendNPCDialogue(npcId, 9827, "Good luck on your travels adventurer.");
    			stage = 4;
            break;
        	} else if(componentId == OPTION_5) {
    			player.setNextPosition(new Position(2422, 3780, 0));
    			sendNPCDialogue(npcId, 9827, "Good luck on your travels adventurer.");
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