package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class JAGInformation extends Dialogue {
int npcId;
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] {
						"Account Guardian",
						"Hello adventurer, how may I help you?" }, IS_NPC,
				npcId, 9827);//first dialogue
	}
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
					"Who are you?", "What is JAG?", "I would like to "+(player.hasJAG ? "disable JAG." : "enable JAG."), "Nevermind.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				stage = 1;
				sendPlayerDialogue( 9827, "Who are you?" );
			} else if (componentId == OPTION_2) {
				stage = 2;
				sendPlayerDialogue( 9827, "What is JAG?" );
			} else if (componentId == OPTION_3) {
				stage = 3;
				sendPlayerDialogue( 9827, "I would like to "+(player.hasJAG ? "disable JAG." : "enable JAG."));
			} else if (componentId == OPTION_4) {
				stage = 120;
				sendPlayerDialogue( 9827, "Erm, nevermind..");
			} else if (stage == 120) {
				end();
			}
		} else if (stage == 1) {
			stage = -1;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
					"Account Guardian",
							"I am the Account Guardian, here to let players know about JAG." }, IS_NPC,
					npcId, 9827);
		} else if (stage == 2) {
			stage = -1;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
					"Account Guardian",
							"JAG is a security measure added to make sure nobody is able to hijack your account." }, IS_NPC,
					npcId, 9827);
		} else if (stage == 3) {
			if(player.hasJAG == true) {
				stage = 8;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] {
						"Account Guardian",
								"Okay, just enter your random question and I will disable your JAG services." }, IS_NPC,
						npcId, 9827);
			} else {
			stage = 4;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
					"Account Guardian",
							"Alright, but first you will need to set a question in case you need to register a new device." }, IS_NPC,
					npcId, 9827);
			}
		} else if (stage == 4) {
			stage = 5;
			sendPlayerDialogue( 9827, "Erm.. okay, what's the question?" );
		} else if (stage == 5) {
			stage = 6;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
					"What was your first pet's name?", "What is your favourite color?", "What is your mother's maiden name?");
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				player.getTemporaryAttributtes().put("entering_jag1", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What was your first pet's name?");
			} if (componentId == OPTION_2) {
				player.getTemporaryAttributtes().put("entering_jag2", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What is your favourite color?");
			} if (componentId == OPTION_3) {
				player.getTemporaryAttributtes().put("entering_jag3", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What is your mother's maiden name?");
			}
		} else if (stage == 8) {
			if(player.getJAG().randomQuestion == 1) {
				player.getTemporaryAttributtes().put("removing_jag1", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What was your first pet's name?");
			} else if(player.getJAG().randomQuestion == 2) {
				player.getTemporaryAttributtes().put("removing_jag2", Boolean.TRUE);
				player.getPackets().sendInputNameScript("What is your favourite color?");
			} else if(player.getJAG().randomQuestion == 3) {
				player.getTemporaryAttributtes().put("removing_jag3", Boolean.TRUE);
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