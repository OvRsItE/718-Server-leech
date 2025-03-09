package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class Scavo extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Magic Shops", "Runes & runecrafting", "Magic Equipment");
			stage = 2;
		} else if (stage == 2) {
			if(componentId == OPTION_1) {
				ShopsHandler.openShop(player, 31);
			}
			if(componentId == OPTION_2) {
				ShopsHandler.openShop(player, 32);			
			}
			end();
		}
	}

	@Override
	public void finish() {

	}

}