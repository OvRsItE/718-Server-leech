package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.PartyRoom;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.TutorialIsland;

public class DungeonT extends Dialogue {

	int npcId;
	TutorialIsland controler;
	public static final int DG_TUTOR = 9712;

	@Override
	public void start() {
		sendEntityDialogue(
				SEND_3_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(DG_TUTOR).name,
						"Dungeoneering in AutoScape is really simple, all you need to do",
						"is to have the Ring of Kinship, keep it always in your inventory",
						"before you start to train dungeoneering" }, IS_NPC,
				DG_TUTOR, 9843);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendPlayerDialogue(9827,
					"Why should I have the Ring of Kinship with me?");
		} else if (stage == 0) {
			stage = 1;
			sendNPCDialogue(9712, 9827, "Are you stupid? It will help you.",
					"You should really use it, but you don't have to.",
					"By using this magical ring you'll gain access to the dungeoneering area.");
		} else if (stage == 1) {
			stage = 2;
			sendNPCDialogue(
					9712,
					9827,
					"Any monster in the dungeon will give you XP for dungeoneering.",
					"Have fun, you'll also receive rusty coins with them you can buy",
					"some cool chaotics items at the shop of daemonhiem,",
					"sadly you must figure out the shop yourself, i'm not here to waste my time anymore.");
		} else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { "",
					"The dungeoneering tutor gives you Ring of Kinship." },
					IS_ITEM, 15707, 1);
			player.getInventory().addItem(15707, 1);
} else {
			player.getPackets().sendGameMessage("<img=1><col=ff0000>You have received a Ring of Kinship, goodluck dungeoneering!");;
			end();
		}
	}

	@Override
	public void finish() {

	}

}
