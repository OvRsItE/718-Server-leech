/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author ReverendDread
 * Nov 30, 2018
 */
public class CagedSanta extends Dialogue {

	private static final int CHAT_HEAD = 8540;
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#start()
	 */
	@Override
	public void start() {
		if (player.completedChristmasEvent) {
			sendNPCDialogue(CHAT_HEAD, NORMAL, "Hello adventurer, thank you so much for helping me get the childrens presents back,", "I will be forever in your debt.");
			stage = 3;
		} else if (player.talkedToSanta) {
			sendNPCDialogue(CHAT_HEAD, NORMAL, "Hello adventurer, have you returned with all of the", "presents I asked you to retrieve?");
			stage = 4;
		} else {
			sendNPCDialogue(CHAT_HEAD, NORMAL, "Oh hohoho adventurer, could you help me get out of this cage?", "I seem to be quite stuck.");
			stage = 0;
		}
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#run(int, int)
	 */
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			sendNPCDialogue(CHAT_HEAD, NORMAL, "Anti-santa has locked me in here and is planning to steal", 
					"all of the presents from all of the children of " + Constants.SERVER_NAME + ".");
			stage = 1;
		} else if (stage == 1) {
			sendNPCDialogue(CHAT_HEAD, NORMAL, "You'll need to defeat him and his minions to get all of the presents back.", 
					"You can find his minions around Vorkath, Deamonheim, and Varrock. The dirty little things.");
			stage = 2;
		} else if (stage == 2) {
			sendNPCDialogue(CHAT_HEAD, NORMAL, "Return to me when you have all of the presents, and have defeated Anti-santa.", "Heres a note of the location of Anti-santa.");
			player.getInventory().addItemDrop(4597, 1);
			player.talkedToSanta = true;
			stage = 3;
		} else if (stage == 3) {
			end();
		} else if (stage == 4) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes.", "No.");
			stage = 5;
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				if (player.killedAntiSanta && player.getInventory().contains(new int[] { 6542, 15420, 22991, 29541 })) {
					sendPlayerDialogue(NORMAL, "I've defeated Anti-santa and have all of your presents here Santa.");
					stage = 6;
				} else {
					sendPlayerDialogue(NORMAL, "No, sorry I haven't. I'll get around to it when I can.");
					stage = 3;
				}
			} else if (componentId == OPTION_2) { 
				sendPlayerDialogue(NORMAL, "No, sorry I haven't. I'll get around to it when I can.");
				stage = 3;
			}
		} else if (stage == 6) {
			if (player.getInventory().contains(new int[] { 15420, 22991, 6542, 29541 })) {
				sendNPCDialogue(CHAT_HEAD, NORMAL, "That's great to hear that adventurer, here's a present for me helping out.");
				player.getInventory().deleteItem(6542, 1);
				player.getInventory().deleteItem(15420, 1);
				player.getInventory().deleteItem(22991, 1);
				player.getInventory().deleteItem(29541, 1);
				player.getInventory().addItemDrop(29542, 1);
				player.completedChristmasEvent = true;
				stage = 3;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}

}
