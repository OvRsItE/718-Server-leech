package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class GrimReaper3 extends Dialogue {
	
	/**
	 * @author Mario (AlterOPS)
	 **/

	private int npcId;

  @Override
	public void start() {
	npcId = (Integer) parameters[0];
			stage = -1;
			sendNPCDialogue(npcId, 9827, "Oh you have the Vampyre dust! Thank you!");
	}

  @Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 1;
			sendPlayerDialogue(9827, "Can i get my drink?");
			break;
		case 1:
			stage = 2;
			sendNPCDialogue(npcId, 9827, "Of course.");
			player.getInventory().deleteItem(3325, 3);
			player.getInventory().addItem(22329, 1);
			player.drink = 1;
			player.dust1 = 4;
			player.dust2 = 4;
			player.dust3 = 4;
			break;
		case 2:
			stage = 3;
			sendPlayerDialogue(9827, "Bye");
			break;
		case 3:
			stage = 4;
			sendNPCDialogue(npcId, 9827, "Bye");
			
			end();
			break;
		}
	}

  @Override
	public void finish() {

	}

}