package com.rs.game.world.entity.player;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ReverendDread
 * June 9, 2018
 */
public class DonationManager implements Serializable {

	/**
	 * The serialUID
	 */
	private static final long serialVersionUID = 3015098517284990929L;
	
	/**
	 * The player
	 */
	@Getter @Setter private transient Player player;
	
	/**
	 * The players donator rank.
	 */
	@Getter private DonatorRanks rank = DonatorRanks.NONE;
	
	/**
	 * Total amount of dollars donated.
	 */
	private int dollars;
	
	/**
	 * DonationManager constuctor.
	 */
	public DonationManager() {
		update();
	}
	
	/**
	 * Updates the players donator rank.
	 */
	public void update() {
		for (DonatorRanks rank : DonatorRanks.values()) {
			if (dollars >= rank.getMinimum())
				this.rank = rank;
		}
	}
	
	/**
	 * Gets the players total donation amount.
	 * @return
	 */
	public int getDonationTotal() {
		return this.dollars;
	}
	
	/**
	 * Increments how much the player has donated.
	 * @param dollars
	 */
	public void increment(int dollars) {
		this.dollars += dollars;
		update();
	}
	
	/**
	 * Deincrements the total amount donated.
	 * @param dollars
	 */
	public void deincrement(int dollars) {
		this.dollars -= dollars;
		update();
	}
	
	/**
	 * Checks if the player has the specified rank or above.
	 * @param rank
	 * @return
	 */
	public boolean hasRank(DonatorRanks rank) {
		return this.rank.ordinal() >= rank.ordinal();
	}
	
	
	/**
	 * Sets the rank received in parameter to the player
	 * @param rank
	 */
	public void setRank(DonatorRanks rank) {
		this.rank = rank;
	}
	
}
