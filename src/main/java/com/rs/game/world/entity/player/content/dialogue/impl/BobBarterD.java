package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.content.BobBarter;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * 
 * @author Tyler
 *
 */
public class BobBarterD extends Dialogue {
	int npcId;

	@Override
	public void start() {
		npcId = (int) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Good day, How can I help you?" }, IS_NPC, npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {

		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue("What would you like to say?",
					"Decant my potions...", "Nevermind.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				sendNPCDialogueNoContinue(player, npcId, 9827, "Decanting...");
				player.lock(5);
				end();
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						BobBarter decanting = new BobBarter(player);
						decanting.decant();
						sendNPCDialogueNoContinue(player, npcId, 9827,
								"There yer go, chum!");
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								closeNoContinueDialogue(player);
								end();
							}
						}, 2);
					}
				}, 5);
			} else {
				end();
			}

		}

	}

	@Override
	public void finish() {

	}

}

