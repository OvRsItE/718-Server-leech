package com.rs.game.world.entity.player.content.trident.impl;

import java.io.Serializable;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;

public class TridentOfTheSeas implements Serializable {

	private static final long serialVersionUID = -5358882959130806885L;
	
	private transient Player player;
	
	public static final int FULL = 2_500;
	
	public int charges;

	/**
	 * Handles item on item for trident.
	 * @param used
	 * 			items used on trident.
	 * @param usedWith
	 * 			the trident.
	 * @return
	 * 			if item on item was handled here.
	 */
	public boolean handleItemOnItem(Item used, Item usedWith) {
		if (usedWith.getId() == 29953 || usedWith.getId() == 29947) {			
			switch(used.getId()) {		
				case 554:
				case 560:
				case 562:
				case 29898:					
					if (player.getInventory().containsItems(new Item[] { new Item(560, 1), new Item(562, 1), new Item(554, 5), new Item(995, 500)} )) {
						int min = Integer.MAX_VALUE;						
						for (Item item : player.getInventory().getItems().getItems()) {
							if (item != null) {
								switch (item.getId()) {
								case 554:
								case 560:
								case 562:
								case 995:
									if (item.getAmount() / (item.getId() == 995 ? 500 : item.getId() == 554 ? 5 : 1) < min) {
										min = item.getAmount() / (item.getId() == 995 ? 500 : item.getId() == 554 ? 5 : 1);
									}
									break;
								}
							}
						}
						System.out.println("Pre-Charges: " + min);
						if (min + charges > FULL) {
							min = FULL - charges;
						}
						System.out.println("Post-Charges: " + min);
						player.getInventory().deleteItem(554, 5 * min);
						player.getInventory().deleteItem(560, min);
						player.getInventory().deleteItem(562, min);
						player.getInventory().deleteItem(995, 500 * min);
						charges = min + charges;
						if (usedWith.getId() == 29953) {
							usedWith.setId(29947);
							player.getInventory().refresh();
						}
						check();
					} else {
						player.sendMessage("You need to have at least 1 death rune, 1 chaos rune, 5 fire runes, and 500 coins to charge this.");
					}
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Handles uncharing the trident.
	 * @param trident
	 * 				the trident {@code Item}.
	 * @return
	 */
	public boolean uncharge(Item trident) {
		if (player.getInventory().containsOneItem(554, 560, 562) || player.getInventory().getFreeSlots() >= 3) {
			player.getInventory().addItem(554, charges * 5);
			player.getInventory().addItem(560, charges);
			player.getInventory().addItem(562, charges);
			trident.setId(29953);
			player.getInventory().refresh();
			player.sendMessage("You have uncharged your trident.");
			charges = 0;
			return true;
		}
		return false;
	}
	
	/**
	 * Handles degrading for tridents.
	 * @return
	 */
	public boolean degrade() {
		if (charges <= 0) {
			player.sendMessage("You do not have any charges left to use your trident.");
			player.getEquipment().getItems().get(Equipment.SLOT_WEAPON).setId(29953);
			player.getEquipment().refresh(Equipment.SLOT_WEAPON);
			return false;
		}
		charges--;
		return true;
	}

	/**
	 * Checks information for tridents.
	 */
	public void check() {
		if (charges > 0)
			player.sendMessage("Your trident has " + charges + " charge" + (charges > 1 ? "s" : "") + ".");
		else 
			player.sendMessage("Your trident has no charges.");	
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Checks if an item is a trident of the seas.
	 * @param itemId
	 * @return
	 */
	public static boolean isTrident(int itemId) {
		return itemId == 29953 || itemId == 29947;
	}

}
