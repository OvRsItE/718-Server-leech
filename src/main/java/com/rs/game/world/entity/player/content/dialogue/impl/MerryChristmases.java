package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.cutscenes.impl.MerryChristmas;
import com.rs.game.world.entity.player.cutscenes.impl.NexCutScene;
import com.rs.utility.Utils;

public class MerryChristmases extends Dialogue {

	int npcId = 1552;

	public static int[] Gifts = {989, 24155, 24154, 6199, 18768, 6199, 7776};
//	int itemId = defs.getName()
	//ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
	int itemId = getLength();
	ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
	
	
	@Override
	public void start() {
		int roll = Utils.getRandom(10);
		if (roll < 8) {
			player.getInventory().addItem(Gifts[Utils.random(getLength()- 1)], 1);
			sendItemDialogue(itemId, "Christmas is comming! Therefor every player will get a gift! You have been gifted "+ defs.getName() +"! Merry Christmas!");
		}
//		sendItemDialogueNoContinue(player, 24155, 1, "Christmas is near! and every fellow player is rewarded with a double spin scroll! Enjoy and remember Christmas is only one time in a year!");
		
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case 1:
			sendNPCDialogue(npcId, HAPPY, "Merry Christmas "+player.getUsername()+"! I would like to inform you that the Christmas will end when we are in to the new year!");
			player.getCutscenesManager().play(new MerryChristmas());
		break;
		
		default:
			end();
		break;
			}
		}
	
	public int getLength() {
		return Gifts.length;
	}
	

	@Override
	public void finish() {

	}

}
