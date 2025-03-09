package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Lunars;
import com.rs.utility.ShopsHandler;

public class IrishToolD extends Dialogue {

private int npcId;

	@Override
	public void start() {
		sendNPCDialogue(3021, 9827, "Hey, you need any help with farmin mate?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
					"Yes let me see your farming shop please.",
					"Yes, I would like to know a bit about it and how to access it.",
					"Show me my farming patch statuses.",
					"No, I know what I am doing.");
			break;
		case 0:
			if (componentId == OPTION_1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"What shop would you like?" }, IS_NPC, npcId, 9827);
				player.getDialogueManager().startDialogue("seeds");
			} else if (componentId == OPTION_2) {
				stage = 1;
				sendNPCDialogue(3021, 9827,
						"Ahh! Well, I am glad to see another avid farmer like meself.",
						"You can click the teleport option on me and I would be glad",
						"to take you to one of 4 farming areas.");
			} else if (componentId == OPTION_3) {
				Lunars.openRemoteFarm(player);
			} else {
				end();
			}
			break;
		case 1:
			stage = -2;
			sendNPCDialogue(3021, 9827,
					"Here's the thing though, your crops will not grow",
					"unless you are online, they will save their current",
					"growing state, but will not advance unless you are",
					"logged in.");
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