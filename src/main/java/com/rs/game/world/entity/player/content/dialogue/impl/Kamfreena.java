package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Kamfreena extends Dialogue {


	@Override
	public void start() {
		if (player.getInventory().containsItem(8851, 200)) {
			sendNPCDialogue(4289, HAPPY, "Ahh! i see you have 200 tokens in your inventory! You may enter!");
			player.setNextPosition(new Position(2847, 3536, 2));
			player.atWarriorsGuild = true;
			player.getInventory().deleteItem(8851, 10);
			stage = 0;
		} else {
			sendNPCDialogue(4289, HAPPY, "You must have 200 tokens, before you can enter.");
			stage = 0;
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}