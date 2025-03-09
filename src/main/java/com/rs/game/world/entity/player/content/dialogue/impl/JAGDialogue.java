package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class JAGDialogue extends Dialogue {

	int npcId = 2904;

	@Override
	public void start() {
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] {
				"Account Guardian",
						"Hello again "+player.getDisplayName()+"." }, IS_NPC,
				npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendPlayerDialogue( 9827, "What's going on??" );
		} else if (stage == 0) {
			stage = 1;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
					"Account Guardian",
							"It seems you have logged onto your account with an unregistered device to JAG.", 
							"You will need to register this device before you can continue playing." }, IS_NPC,
					npcId, 9827);
		} else if (stage == 1) {
			stage = 2;
			sendPlayerDialogue( 9827, "Alright, well do you need me to enter my question again?" );
		//	controler.updateProgress();
		} else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
					"Account Guardian",
							"Indeed so." }, IS_NPC,
					npcId, 9827);
		} else if (stage == 3) {
			stage = 4;
			if(player.getJAG().randomQuestion == 1) {
				player.getTemporaryAttributtes().put("proving_jag1", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What was your first pet's name?");
			} else if(player.getJAG().randomQuestion == 2) {
				player.getTemporaryAttributtes().put("proving_jag2", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What is your favourite color?");
			} else if(player.getJAG().randomQuestion == 3) {
				player.getTemporaryAttributtes().put("proving_jag3", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What is your mother's maiden name?");
			}
		} else {
			end();
		}

	}

	@Override
	public void finish() {

	}

}