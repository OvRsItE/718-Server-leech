package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class GhostDisciple extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Wooo wooo woooooo.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Ok?.. Are you selling anything?", "Can I claim my ectotokens please?", "Ok. I better be going..");
			break;
		case 0:
			if (componentId == OPTION_1) {
				stage = -1;
				end();
			} else if (componentId == OPTION_2) {
				stage = -1;
				if (player.getInventory().hasFreeSlots() && player.unclaimedEctoTokens > 0) {
					//player.getInventory().addItem(Ectofuntus.ECTO_TOKEN, player.unclaimedEctoTokens);
					player.unclaimedEctoTokens = 0;
				}
				sendNPCDialogue(npcId, 9827, "Woo wooooo woo woo woooo woo woo wooo!");
			} else if (componentId == OPTION_3) {
				end();
			}
			break;
		default:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
