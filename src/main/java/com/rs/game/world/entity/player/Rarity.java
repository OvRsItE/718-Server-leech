package com.rs.game.world.entity.player;

/**
 * @author Andy || ReverendDread Jul 19, 2017
 * 
 * Represents a rarity.
 */
public enum Rarity {
	
	COMMON(52D), UNCOMMON(34D), RARE(12D), VERY_RARE(3D), ULTRA_RARE(0.2D);
	
	private double rarity;
	
	private Rarity(double rarity) {
		this.rarity = rarity;
	}
	
	/**
	 * Gets the rarity.
	 * @return
	 */
	public double getRate() {
		return rarity;
	}
	
}
