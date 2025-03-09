package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.ShopsHandler;

public class EstateAgent extends Dialogue {
	
	public static int SKILLCAPE = 9748;
	public static int SKILLHOOD = 9749;
	public static int ONE = 1;

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
			sendEntityDialogue(
				SEND_2_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello " + player.getUsername() + " how can I help you?"}, IS_NPC, npcId,
				9760);
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Select a Option", "Can I purchase a house?",
					"What have you got for sale?", "Change house location");
			stage = 1;
		} else if (stage == 1) {
		if (componentId == OPTION_1) {
			if (player.hasHouse) {
				sendEntityDialogue(
						SEND_2_TEXT_CHAT,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								"You already own a house!"}, IS_NPC, npcId,
						9760);
				player.getInterfaceManager().closeChatBoxInterface();
			} else {
				sendEntityDialogue(
						SEND_2_TEXT_CHAT,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								"That will be 5m ."}, IS_NPC, npcId,
						9760);
				stage = 2; 
			}
	  } else if (componentId == OPTION_2) {
		  ShopsHandler.openShop(player, 18);
		  end();
			}
		} else if (stage == 2) {
			sendOptionsDialogue("Select a Option", "Sure!",
					"No, thats too expensive!");
			stage = 3;
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				if (player.getInventory().containsItem(995, 5000000)) {
					sendEntityDialogue(
							SEND_2_TEXT_CHAT,
							new String[] {
									NPCDefinitions.getNPCDefinitions(npcId).name,
									" Congratulations, you now own a house!"}, IS_NPC, npcId,
							9760);
					player.getInventory().removeItemMoneyPouch(995, 5000000);
					player.hasHouse = true;
					end();
				} else {
					sendEntityDialogue(
							SEND_2_TEXT_CHAT,
							new String[] {
									NPCDefinitions.getNPCDefinitions(npcId).name,
									" I'm sorry but you do not have enough money."}, IS_NPC, npcId,
							9760);
							end();
				}
		  } else if (componentId == OPTION_2) {
			  sendEntityDialogue(
						SEND_2_TEXT_CHAT,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								" You are truely missing out."}, IS_NPC, npcId,
						9760);
						end();
				}
		}
	}

	@Override
	public void finish() {

	}

}
