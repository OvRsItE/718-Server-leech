package com.rs.game.world.entity.player.content;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ViturScythe implements Serializable {

	private static final long serialVersionUID = 8059133655724421078L;

	private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");
	
	@Setter private transient Player player;
	
	private static final int MAX_BLOOD_RUNE = 16_000;
	private static final int Bood_rune_id = 565;
	public static final int CHARGED_SCYTHE = 52325;
	private static final int UNCHARGED_SCYTHE = 52486;
	
	@Getter private int blood_rune;
	
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
		if (used.getId() == Bood_rune_id) {
			switch (usedWith.getId()) {	
				case CHARGED_SCYTHE:
				case UNCHARGED_SCYTHE:
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
		if (used.getAmount() + blood_rune > MAX_BLOOD_RUNE) {
			added = MAX_BLOOD_RUNE - blood_rune;
		} else {
			added = used.getAmount();
		}
		System.out.println("Added: " + added);
		player.getInventory().deleteItem(Bood_rune_id, added);
		blood_rune += added;
		if (usedWith.getId() == UNCHARGED_SCYTHE) {
			usedWith.setId(CHARGED_SCYTHE);
			player.getInventory().refresh();
		}
	}
	
	/**
	 * Handles uncharing the serp helm.
	 */
	public void uncharge(Item item) {
		if (player.getInventory().containsItem(Bood_rune_id, 1) || player.getInventory().hasFreeSlots()) {
			player.getInventory().addItem(Bood_rune_id, blood_rune);
			blood_rune = 0;
		} else {
			player.sendMessage("You don't have enough inventory space to uncharge the sanguinesti staff.");
		}
		item.setId(UNCHARGED_SCYTHE);
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
		String ether = FORMATTER.format(getBlood_rune() * 100.0 / (double) MAX_BLOOD_RUNE) + "%";
		player.sendMessage("Blood rune: <col=007F00>" + ether + "</col>");
	}
	
	/**
	 * Handles degrading for serp helm.
	 */
	public void degrade() {
		if (blood_rune <= 0) {
			player.getEquipment().getItems().get(Equipment.SLOT_WEAPON).setId(UNCHARGED_SCYTHE);
			player.getEquipment().refresh(Equipment.SLOT_WEAPON);
			player.sendMessage("<col=ff0000>Your sanguinesti staff has ran out of charges.</col>");
			return;
		}
		blood_rune--;
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
	public static boolean isViturScythe(int itemId) {
		return (itemId == CHARGED_SCYTHE);
	}

	
}
