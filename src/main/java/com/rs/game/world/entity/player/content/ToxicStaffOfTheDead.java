package com.rs.game.world.entity.player.content;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;

public class ToxicStaffOfTheDead implements Serializable {

	private static final long serialVersionUID = 813780186603893148L;

	private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");
	
	private transient Player player;
	
	private int scales;
	
	private static final int FULL = 11_000;
	private static final int CHARGED = 29889;
	public static final int UNCHARGED = 29891;
	private static final int MAGIC_FANG = 29935;
	private static final int STAFF = 29893;
	private static final int SCALES = 29898;

	/**
	 * Handles item on item for toxic staff.
	 * @param used
	 * @param usedWith
	 * @return
	 */
	public boolean handleItemOnItem(Item used, Item usedWith) {
		if (usedWith.getId() == MAGIC_FANG && used.getId() == STAFF 
				|| used.getId() == MAGIC_FANG && usedWith.getId() == STAFF) {
			createToxicStaff();
			return true;
		}
		if (used.getId() == 29898) {
			switch (usedWith.getId()) {	
				case CHARGED:
				case UNCHARGED:
					charge(used, usedWith);
					return true;	
			}
		}
		return false;
	}
	
	/**
	 * Dismantles toxic staff (uncharged)
	 */
	public void dismantle() {
		if (player.getInventory().getFreeSlots() >= 2) {
			player.getInventory().deleteItem(UNCHARGED, 1);
			player.getInventory().addItem(STAFF, 1);
			player.getInventory().addItem(MAGIC_FANG, 1);
		} else {
			player.sendMessage("You don't have enough inventory space to dismantle the toxic staff.");
		}
	}
	
	/**
	 * Creates toxic staff (uncharged)
	 */
	private void createToxicStaff() {
		player.getInventory().deleteItem(STAFF, 1);
		player.getInventory().deleteItem(MAGIC_FANG, 1);
		player.getInventory().addItem(UNCHARGED, 1);
		player.getDialogueManager().startDialogue("ItemMessage", "You add the Magic fang ontop of the Staff of the dead, and create a Toxic staff.", UNCHARGED);
	}

	/**
	 * Charges toxic staff
	 * @param used
	 * 		scales	
	 * @param usedWith
	 * 		staff
	 */
	public void charge(Item used, Item usedWith) {
		int added = 0;
		if (used.getAmount() + scales > FULL) {
			added = FULL - scales;
		} else {
			added = used.getAmount();
		}
		player.getInventory().deleteItem(SCALES, added);
		scales = added;
		if (usedWith.getId() == UNCHARGED) {
			usedWith.setId(CHARGED);
			player.getInventory().refresh();
		}
	}
	
	/**
	 * Checks into for toxic staff.
	 */
	public void check() {
		String info = FORMATTER.format(scales * 100.0 / (double) FULL) + "%";
		player.sendMessage("Scales: <col=007F00>" + info + "</col>");
	}
	
	/**
	 * Handles degrading for toxic staff.
	 * @return
	 */
	public boolean degrade() {
		if (scales <= 0) {
			player.sendMessage("You don't have any charges in your toxic staff.");
			player.getEquipment().getItem(Equipment.SLOT_WEAPON).setId(UNCHARGED);
			player.getEquipment().refresh(Equipment.SLOT_WEAPON);
			return false;
		}
		scales--;
		return true;
	}
	
	/**
	 * Uncharges the toxic staff.
	 */
	public void uncharge() {
		if (player.getInventory().containsOneItem(SCALES) || player.getInventory().getFreeSlots() > 0) {
			player.getInventory().deleteItem(CHARGED, 1);
			player.getInventory().addItem(UNCHARGED, 1);
			player.getInventory().addItem(SCALES, scales);
			scales = 0;
		} else {
			player.sendMessage("You don't have enough inventory to uncharge the Toxic staff of the dead.");
		}
	}
	
	public static boolean isToxicStaff(int itemId) {
		return itemId == CHARGED;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
