package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class DestroyItemOption extends Dialogue {

	int slotId;
	Item item;

	@Override
	public void start() {
		slotId = (Integer) parameters[0];
		item = (Item) parameters[1];	
		sendOptionsDialogue("Are you sure you want to destory your " + item.getName() + "?", "Yes", "No");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.getInventory().deleteItem(slotId, item);
		}
		end();
	}

	@Override
	public void finish() {

	}

}
