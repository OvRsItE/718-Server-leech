package com.rs.game.world.entity.player.content;

import java.util.ArrayList;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

public class ItemSearch {

	public static int[] COMPONENTS = { 30, 32, 34, 36, 38, 49, 51, 53, 55, 57, 59, 62, 64, 66, 68, 70, 72, 74, 76, 190, 79, 81, 83, 85, 88, 90, 92, 94, 97, 99, 101, 104, 106, 108, 110, 115, 117, 119, 121, 123, 125, 131, 127, 129, 2, 173, 175, 177, 182, 184, 186, 188 };

	public static void searchForItem(Player player, String itemName) {
		ArrayList<String> ITEMS = new ArrayList<String>();

		player.getInterfaceManager().sendInterface(1082);
		
		for (int i = 0; i < Utils.getInterfaceDefinitionsComponentsSize(1082); i++) {
			player.getPackets().sendIComponentText(1082, i, "");
		}
		
		player.getPackets().sendIComponentText(1082, 41, "Item Name");
		player.getPackets().sendIComponentText(1082, 42, "ItemId");
		
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			Item item = new Item(i);
			if (item.getDefinitions().getName().toLowerCase().contains(itemName.toLowerCase())) {
				ITEMS.add(item.getName() + (item.getDefinitions().isNoted() ? "(noted)," : ",") + item.getId());
			}
		}
		
		if (ITEMS.size() < 53) {
			String name = "NULL";
			String id = "-1";
			for (int i = 0; i < ITEMS.size(); i++) {
				name = ITEMS.get(i).split(",")[0];
				id = ITEMS.get(i).split(",")[1];
				player.getPackets().sendIComponentText(1082, COMPONENTS[i], Utils.formatPlayerNameForDisplay(name));
				player.getPackets().sendIComponentText(1082, COMPONENTS[i] + 1, id);
			}
			player.getPackets().sendIComponentText(1082, 11, "Found " + ITEMS.size() + " results for the item " + Utils.formatPlayerNameForDisplay(itemName) + ".");
		} else {
			player.getPackets().sendIComponentText(1082, 11, "Found to many results for the item " + Utils.formatPlayerNameForDisplay(itemName) + ". Refine your search.");
		}
	}

}
