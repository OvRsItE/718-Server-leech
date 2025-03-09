/**
 * 
 */
package com.rs.game.world.entity.player.content;

import java.io.Serializable;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ReverendDread
 * Nov 30, 2018
 */
public class Arclight implements Serializable {

	/** Serial UID. */
	private static final long serialVersionUID = 8958145227534972037L;

	/** Charges remaining. */
	@Getter private int charges;
	
	/** The player. */
	@Setter private transient Player player;
	
	/** Ancient shard id */
	private transient static final int ANCIENT_SHARD = 29955, ARCLIGHT = 29965, MAX_CHARGES = 10_000;
	
	/**
	 * Handles charging arclight.
	 * @param shards
	 * 			amount of shards being used.
	 * @return
	 */
	public boolean charge(int shards) {
		if (player.getInventory().contains(new Item(ANCIENT_SHARD, shards), new Item(ARCLIGHT, 1))) {
			if ((charges + 1000) <= MAX_CHARGES) {
				player.getInventory().deleteItem(ANCIENT_SHARD, 3);
				charges += 1000;
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage", "You can only store up to 10,000 charges at one given time.", "Current charges: " + Utils.formatNumber(charges));
			}
			return true;
		}
		return false;
	}
	
	public void degrade(int charges) {
		this.charges -= charges;
		if (this.charges < 0)
			this.charges = 0;
	}
	
}
