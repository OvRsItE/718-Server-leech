package com.rs.game.world.entity.player.content;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Sceptre implements Serializable {

	private static final long serialVersionUID = -6782346412740267551L;

	private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");
	
	@Setter private transient Player player;
	
	private static final int MAX_ETHER = 16_000;
	private static final int ETHER_ID = 51820;
	public static final int CHARGED_SCEPTRE = 52555;
	private static final int UNCHARGED_SCEPTRE = 52552;
	
	@Getter private int ether;
	
	/**
	 * Handles serp helm item on item
	 * @param used
	 * 			scales
	 * @param usedWith
	 * 			serp helm
	 * @return-
	 * 			if item was handled.
	 */
	public boolean handleItemOnItem(Item used, Item usedWith) {
		if (used.getId() == ETHER_ID) {
			switch (usedWith.getId()) {	
				case CHARGED_SCEPTRE:
				case UNCHARGED_SCEPTRE:
					charge(used, usedWith);
					return true;	
			}
		}
		return false;
	}
	
	/**
	 * Handles creating serp helm.
	 */
	/**
	 * Handles charging serp helm.
	 * @param used
	 * @param usedWith
	 */
	public void charge(Item used, Item usedWith) {
		int added = 0;
		if (used.getAmount() + ether > MAX_ETHER) {
			added = MAX_ETHER - ether;
		} else {
			added = used.getAmount();
		}
		System.out.println("Added: " + added);
		player.getInventory().deleteItem(ETHER_ID, added);
		ether += added;
		if (usedWith.getId() == UNCHARGED_SCEPTRE) {
			usedWith.setId(CHARGED_SCEPTRE);
			player.getInventory().refresh();
		}
	}
	
	/**
	 * Handles uncharing the serp helm.
	 */
	public void uncharge(Item item) {
		if (player.getInventory().containsItem(ETHER_ID, 1) || player.getInventory().hasFreeSlots()) {
			player.getInventory().addItem(ETHER_ID, ether);
			ether = 0;
		} else {
			player.sendMessage("You don't have enough inventory space to uncharge the Thammaron's sceptre.");
		}
		item.setId(UNCHARGED_SCEPTRE);
		player.getInventory().refresh();
	}
	
	/**
	 * Checks if serp helm causes venom.
	 * @return
	 */

	/**
	 * Gets information about serp helm.
	 */
	public void check() {
		String ether = FORMATTER.format(getEther() * 100.0 / (double) MAX_ETHER) + "%";
		player.sendMessage("Ether: <col=007F00>" + ether + "</col>");
	}
	
	/**
	 * Handles degrading for serp helm.
	 */
	public void degrade() {
		if (ether <= 0) {
			player.getEquipment().getItems().get(Equipment.SLOT_WEAPON).setId(UNCHARGED_SCEPTRE);
			player.getEquipment().refresh(Equipment.SLOT_WEAPON);
			player.sendMessage("<col=ff0000>Your Thammarons sceptre has ran out of charges.</col>");
			return;
		}
		ether--;
	}
	
	/**
	 * Checks if player is wearing helm.
	 * @return
	 */

	/**
	 * Checks if an item is a serp helm.
	 * @param itemId
	 * @return
	 */
	public static boolean isSceptre(int itemId) {
		return (itemId == CHARGED_SCEPTRE);
	}
	
}
