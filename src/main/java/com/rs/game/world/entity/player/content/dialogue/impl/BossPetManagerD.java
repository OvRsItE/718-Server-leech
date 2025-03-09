/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import java.util.ArrayList;
import java.util.Iterator;

import com.rs.Constants;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.pets.BossPets;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Aug 7, 2018
 */
public class BossPetManagerD extends Dialogue {

	int npcId;
	
	ArrayList<BossPets> pets;
	
	private static final int RECLAIM_PRICE = 1_000_000;
	
	@Override
	public void start() {
		npcId = (int) parameters[0];
		pets = player.getBossPetsManager().getReclaimablePets();
		sendNPCDialogue(npcId, NORMAL, "Hello I'm " + Constants.SERVER_NAME + "'s pet manager, I can insure or sell you your pets back.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Claim my pets back.", "Insure one of my pets.");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				if (pets.size() == 0) {
					sendNPCDialogue(npcId, NORMAL, "I'm sorry but you don't have any pets to claim right now.");
					stage = 100;
				} else {
					sendNPCDialogue(npcId, NORMAL, "Are you sure you wish to buy back " + pets.size() + " pets for " + (Utils.formatNumber(RECLAIM_PRICE * pets.size())) + " gold?");
					stage = 2;
				}
			} else if (componentId == OPTION_2) {
				sendNPCDialogue(npcId, NORMAL, "Sure thing, just use your pet on me so i can insure it for you.");
				stage = 100;
			}
		} else if (stage == 2) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes, please.", "No thanks.");
			stage = 3;
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				sendNPCDialogue(npcId, HAPPY, "Here you are, enjoy!");
				player.getBossPetsManager().reclaimPets(pets);
				stage = 100;
			} else if (componentId == OPTION_2) {
				stage = 100;
			}
		} else if (stage == 100) {
			end();
		}
	}

	@Override
	public void finish() {}
	
}
