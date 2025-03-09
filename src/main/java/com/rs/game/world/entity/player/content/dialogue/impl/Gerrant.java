package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class Gerrant extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(
				SEND_2_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Welcome! You can buy fishing equipment at my store. "
								+ "We'll also buy anything you catch off you." },
				IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
					"Let's see what you've got then.",
					"Sorry, I'm not interested.");
			break;
		case 0:
			switch (componentId) {
			case OPTION_1:
				stage = 1;
				sendPlayerDialogue(9827, "Let's see what you've got then.");
				break;
			case OPTION_2:
				stage = 2;
				sendPlayerDialogue(9827, "Sorry, I'm not interested.");
				break;
			}
			break;
		case 1:

			end();
			break;
		case 2:
		default:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
