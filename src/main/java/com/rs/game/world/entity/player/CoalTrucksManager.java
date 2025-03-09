package com.rs.game.world.entity.player;

import java.io.Serializable;

import lombok.Setter;

/**
 * Handles the coal trucks.
 * @author ReverendDread
 * Dec 14, 2018
 */
public class CoalTrucksManager implements Serializable {

	/** Serial UID */
	private static final long serialVersionUID = 20296244197867311L;

	/** The player */
	@Setter private transient Player player;
	
	/** The amount of coal held in the trucks */
	private int coal;

	/** Max amount of coal the trucks can hold */
	private static final int MAX_COAL = 196;

	/**
	 * Refreshes coal trucks.
	 */
	public void refreshCoalTrucks() {
		player.getVarsManager().sendVar(74, coal);
	}

	/**
	 * Handles investigating the trucks.
	 */
	public void investigate() {
		player.getPackets().sendGameMessage("There are currently " + coal + " coals in the truck.");
	}

	/**
	 * Removes coal from the trucks.
	 */
	public void removeCoal() {
		if (coal == 0)
			return;
		int slots = player.getInventory().getFreeSlots();
		if (slots == 0) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return;
		}
		if (coal < slots)
			slots = coal;
		player.getInventory().addItem(453, slots);
		coal -= slots;
		refreshCoalTrucks();
		player.getPackets().sendGameMessage("You remove some of the coal from the truck.", true);
	}

	/**
	 * Adds coal to the trucks.
	 */
	public void addCoal() {
		if (coal >= MAX_COAL) {
			player.getPackets().sendGameMessage("The coal truck is full.");
			return;
		}
		int addCoal = player.getInventory().getAmountOf(453);
		if (coal + addCoal >= MAX_COAL)
			addCoal = MAX_COAL - coal;
		player.getInventory().deleteItem(453, addCoal);
		coal += addCoal;
		refreshCoalTrucks();
		player.getPackets().sendGameMessage("You put the coal in the truck.", true);
	}

}

