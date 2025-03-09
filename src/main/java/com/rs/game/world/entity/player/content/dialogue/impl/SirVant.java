package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.StartTutorial;

public class SirVant extends Dialogue {

	int npcId;
	StartTutorial controler;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		controler = (StartTutorial) parameters[1];
		if (controler == null) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You now know everything to succeed in this",
							"dangerous world. Go prove what you are worth now!" },
					IS_NPC, npcId, 9827);
			stage = 7;
		} else {
			int s = controler.getStage();
			if (s == 0) {
				sendEntityDialogue(
						SEND_2_TEXT_CHAT,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								"Hey! Let me teach you the about combat.",
								"Go get those supplys over there when were done." },
						IS_NPC, npcId, 9827);
				World.addGroundItem(new Item(1323, 1), new Position(3755, 4787, 0));
				World.addGroundItem(new Item(1191, 1), new Position(3755, 4787, 0));
			} else if (s == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Good, i see your making progress!" }, IS_NPC, npcId,
						9827);
				stage = 5;
			}
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"First step is learning how to equip items.",
							"Then your gonna be on your own!" },
					IS_NPC, npcId, 9827);
			
		} else if (stage == 0) {
			stage = 1;
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Make sure you equip the items i told you get.",
							"You can equip items by clicking the bag icon",
							"and clicking the items. The more items",
							"you have equipped, the more bonus you get." }, IS_NPC,
					npcId, 9827);
		} else if (stage == 1) {
			stage = 2;
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"I have a full set armour, so i get better defence bonus.",
							"If you go attack that crab right now will get xp.",
							"Since your in chop mode you will get attack xp.",
							"now go grab the items and kill the crab." }, IS_NPC, npcId,
					9827);
			controler.updateProgress();
		} else if (stage == 5) {
			stage = 6;
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"To continue the tutorial head to the north",
							"and click on the Tunnel!" }, IS_NPC,
					npcId, 9827);
		} else {

			end();
		}

	}

	@Override
	public void finish() {

	}

}
