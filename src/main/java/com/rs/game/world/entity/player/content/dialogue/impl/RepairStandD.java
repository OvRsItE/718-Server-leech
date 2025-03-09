package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.RepairStand.RepairableItem;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class RepairStandD extends Dialogue {

	RepairableItem item;
	int itemId;
	
	@Override
	public void start() {
		item = (RepairableItem) parameters[0];
		itemId = (Integer) parameters[1];
		if (itemId == 20120) {
			player.getInterfaceManager().sendChatBoxInterface(1183);
			player.getPackets().sendIComponentText(1183, 12, "It will cost "+ item.getCost()*(5-player.frozenKeyUses) +" to repair "+item.getName()+".");
			player.getPackets().sendItemOnIComponent(1183, 13, item.getFixedId(), 1);
			player.getPackets().sendIComponentText(1183, 7, "Repair this item fully for "+item.getCost()*(5-player.frozenKeyUses)+" coins?");
			player.getPackets().sendIComponentText(1183, 22, "Confirm repair");
		} else {
			player.getInterfaceManager().sendChatBoxInterface(1183);
			player.getPackets().sendIComponentText(1183, 12, "It will cost "+ item.getCost() +" to repair "+item.getName()+".");
			player.getPackets().sendItemOnIComponent(1183, 13, item.getFixedId(), 1);
			player.getPackets().sendIComponentText(1183, 7, "Repair this item fully for "+item.getCost()+" coins?");
			player.getPackets().sendIComponentText(1183, 22, "Confirm repair");
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == 9) {
			if (player.getInventory().containsItem(itemId, 1)) {
				if (itemId == 20120) {
					if (player.getInventory().containsItem(995, item.getCost()*(5-player.frozenKeyUses))) {
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(item.getFixedId(), 1);
						player.getInventory().deleteItem(995, item.getCost()*(5-player.frozenKeyUses));
						player.frozenKeyUses = 5;
					} else {
						player.getPackets().sendGameMessage("You don't have enough coins.");
					}
				} else {
					if (player.getInventory().containsItem(995, item.getCost())) {
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(item.getFixedId(), 1);
						player.getInventory().deleteItem(995, item.getCost());
					} else {
						player.getPackets().sendGameMessage("You don't have enough coins.");
					}
				}
			} else {
				player.getPackets().sendGameMessage("Hackers these days..");
			}
		}
		end();
	}

	@Override
	public void finish() {

	}

}
