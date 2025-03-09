package com.rs.game.world.entity.player.content;

import java.io.Serializable;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;

public class Toolbelt implements Serializable {

	private static final long serialVersionUID = -7244573478285647954L;

	private static final int[] TOOLBET_ITEMS = new int[] { 946, 1735, 1595, 1755, 1599, 1597, 1733, 1592, 5523, 13431,
			307, 309, 311, 301, 303, 1265, 2347, 1351, 590, -1, 8794, 4, 9434, 11065, 1785, 2976, 1594, 5343, 5325,
			5341, 5329, 233, 952, 305, 975, 11323, 2575, 2576, 13153, 10150 };
	private static final int[] CONFIG_IDS = new int[] { 2438, 2439 };
	private boolean[] items;
	private transient Player player;
	private boolean[] tools;

	public Toolbelt() {
		items = new boolean[TOOLBET_ITEMS.length];
	}

	public void setPlayer(Player player) {
		this.player = player;
		/*
		 * if(items.length <= TOOLBET_ITEMS.length) //if we add a new item items =
		 * Arrays.copyOf(items, TOOLBET_ITEMS.length);
		 */
	}

	public void init() {
		setAllItems();
		refreshConfigs();
	}

	private int getConfigIndex(int i) {
		return i / 20;
	}

	private void refreshConfigs() {
		int[] configValues = new int[CONFIG_IDS.length];
		for (int i = 0; i < items.length; i++) {
			if (items[i]) {
				int index = getConfigIndex(i);
				configValues[index] |= 1 << (i - (index * 20));
			}
		}
		for (int i = 0; i < CONFIG_IDS.length; i++)
			player.getPackets().sendConfig(CONFIG_IDS[i], configValues[i]);
	}

	private int getItemSlot(int id) {
		for (int i = 0; i < TOOLBET_ITEMS.length; i++)
			if (TOOLBET_ITEMS[i] == id)
				return i;
		return -1;
	}

	public boolean addItem(int invSlot, Item item) {
		int slot = getItemSlot(item.getId());
		if (slot == -1)
			return false;
		if (items[slot])
			player.getPackets().sendInventoryMessage(0, invSlot, "That is already on your tool belt.");
		else {
			items[slot] = true;
			player.getInventory().deleteItem(invSlot, item);
			refreshConfigs();
			player.getPackets()
					.sendGameMessage("You add the " + item.getDefinitions().getName() + " to your tool belt.");
		}
		return true;
	}
	
	public void setAllItems() {
		for (int i = 0; i < items.length; i++) {
			items[i] = true;
		}
	}

	public boolean containsItem(int id) {
		int slot = getItemSlot(id);
		return slot == -1 ? false : items[slot];
	}

	public boolean hasTool(int toolId) {
		int slot = getItemSlot(toolId);
		boolean onBelt = slot != -1 && items[slot];
		return onBelt || player.getInventory().containsItem(toolId, 1) || player.getEquipment().getWeaponId() == toolId;
	}

}
