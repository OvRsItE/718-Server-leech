package com.rs.game.world.entity.player;

import lombok.Getter;

/**
 * Enum containing DonatorRank information.
 * @author ReverendDread
 * May 7, 2018
 */
@Getter
public enum DonatorRanks {
	
	NONE(0, 1, 1, "", -1),
	BRONZE(10, 1.1, 1.005, "5a4141", 14),
	IRON(20, 1.2, 1.010, "646467", 15),
	STEEL(40, 1.3, 1.015, "a6a6b2", 18),
	MITHRIL(60, 1.4, 1.020, "494cb5", 16),
	ADAMANT(80, 1.5, 1.025, "3a782b", 8),
	RUNE(100, 1.6, 1.030, "46728e", 17),
	DRAGON(200, 1.7, 1.035, "ff0000", 6);
	
	int minimum, crown;
	double experienceBoost;
	double luckBoost;
	String coloring;
	
	DonatorRanks(int minimum, double experience_boost, double drop_boost, String coloring, int crown) {
		this.minimum = minimum;
		this.experienceBoost = experience_boost;
		this.luckBoost = drop_boost;
		this.coloring = coloring;
		this.crown = crown;
	}
	
	/**
	 * Gets the name of the enum value.
	 * @return
	 */
	public String getName() {
		String lower = name().toLowerCase() + " donator";
		return lower.substring(0, 1).toUpperCase() + lower.substring(1);
	}
	
	/**
	 * Gets the yell title for this rank.
	 * @return
	 */
	public String getYellTitle() {
		return "<img=" + getCrown() + "><col=" + getColoring() + ">" + getName() + "</col>";
	}
	
}
