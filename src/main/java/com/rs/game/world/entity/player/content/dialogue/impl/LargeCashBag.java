package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.Misc;

public class LargeCashBag extends Dialogue {

	int largeBag = Misc.random(10000000);

	@Override
	public void start() {
		player.getInterfaceManager().sendChatBoxInterface(1189);
		player.getInventory().deleteItem(10835, 1);
		player.getInventory().addItem(995,largeBag);
		player.getPackets().sendItemOnIComponent(1189, 1, 10835, 1);
		player.getPackets().sendIComponentText(1189, 4, "You open the large bag and find " + largeBag + " coins.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == 1189 && componentId == 9) {
		}
		end();
	}

	@Override
	public void finish() {

	}

}