/**
 * 
 */
package com.rs.game.world.entity.player.content.toxin;

import lombok.Getter;

/**
 * Enum containing types of toxin and their attributes.
 * @author ReverendDread
 * Jul 14, 2018
 */
public enum ToxinType {

	/** Toxins ordinal value reflects their priority over one another */
	
	VENOM(30, 60, 200, true),
	POISON(25, 60, 0, false);
	
	/** Frequencey of the toxin in ticks. */
	@Getter private int frequencey;
	
	/** The starting damage. */
	@Getter private int start;
	
	/** The cap damage */
	@Getter private int cap;
	
	/** If it increases in damage */
	@Getter private boolean increase;
	
	/**
	 * Creates a toxin type.
	 * @param frequencey
	 *			the frequencey in ticks.
	 * @param start
	 * 			the starting damage.
	 * @param increase
	 * 			how much the toxin increases or decreases damage.
	 */
	private ToxinType(int frequencey, int start, int cap, boolean increase) {
		this.frequencey = frequencey;
		this.start = start;
		this.cap = cap;
		this.increase = increase;
	}
	
	/**
	 * Gets the priority of this {@code ToxinType} based on its ordinal value.
	 * @return
	 * 			the priority.
	 */
	public int getPriority() {
		return this.ordinal();
	}
	
}
