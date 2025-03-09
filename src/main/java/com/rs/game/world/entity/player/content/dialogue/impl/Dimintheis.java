package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Dimintheis extends Dialogue {

	private int npcId;
	
	@Override
	public void start() {
		npcId = (int) parameters[0];
		sendNPCDialogue(npcId, NORMAL, "I've found some gloves in my garden, would you like to me lend you them?");
		stage = 0;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		
			case 0:
				sendOptionsDialogue("Select an option", "Yes, please.", "No thanks.");
				stage = 1;
				break;
			case 1:
				if (componentId == OPTION_1) {
					sendItemDialogue(778, "Dimintheis hands you his set of gloves.");
					player.getInventory().addItemDrop(778, 1);
					player.getInventory().addItemDrop(777, 1);
					player.getInventory().addItemDrop(775, 1);
					player.getInventory().addItemDrop(776, 1);
				} else if (componentId == OPTION_2) {
					end();
				}
				break;
				
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
