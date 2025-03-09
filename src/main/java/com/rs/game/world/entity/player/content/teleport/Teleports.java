/**
 * 
 */
package com.rs.game.world.entity.player.content.teleport;

import java.util.ArrayList;

import com.rs.game.map.Position;

import lombok.Getter;

/**
 * @author ReverendDread
 * Sep 21, 2018
 */
public enum Teleports {

	//Training teleports.
	COWS("Cows", new Position(3260, 3263), 12365, 3, new int[] { 2674 }, new String[] { "Low hitpoints and very", "low stats. Perfect for", "low leveled players." }),
	ROCK_CRABS("Rock crabs", new Position(2672, 3711), 1265, 3, new int[] { 2674 }, new String[] { "Moderate hitpoints mixed", "with very low chance to", "hit you. The perfect monster", "for any level of player", " to train on." }),
	YAKS("Yaks", new Position(2322, 3793), 5529, 3, new int[] { 2674 }, new String[] { "Moderate hitpoints mixed with", "medium chance to hit low", "leveled players." }),
	EXPERIMENTS("Experiments", new Position(3559, 9947), 1677, 3, new int[] { 2674 }, new String[] { "Very high hitpoints and", "a moderate chance to hit you."}),
	GORAKS("Goraks", new Position(3038, 5340), 4418, 3, new int[] { 2674 }, new String[] { "High hitpoints and", "hit very consistantly.", "Using prayer is recommended."}),
	BANDIT_CAMP("Desert Bandits", new Position(3172, 2982), 1926, 3, new int[] { 2674 }, new String[] { "Multi combat area", "makes it perfect for", "training afk." }),
	
	//Bossing teleports.
	GENERAL_GRAARDOR("General Graardor", new Position(2854, 5334), 6260, 0, new int[] {}, new String[] { "TODO" }),
	KREE_ARRA("Kree'arra", new Position(2872, 5285), 6222, 0, new int[] {}, new String[] { "Ranged is highly advised", "here." }),
	COMMANDER_ZILYANA("Commander Zilyana", new Position(2909, 5300), 6247, 0, new int[] {}, new String[] { "TODO" }),
	KRIL_TSUTSAROTH("K'ril Tsutsaroth", new Position(2887, 5334), 6203, 0, new int[] {}, new String[] { "TODO" }),
	QUEEN_BLACK_DRAGON("Queen Black Dragon", new Position(1197, 6499), 15454, 0, new int[] {}, new String[] { "TODO" }),
	KING_BLACK_DRAGON("King Black Dragon", new Position(3067, 10254), 50, 0, new int[] {}, new String[] { "TODO" }),
	KALPHITE_QUEEN("Kalphite Queen", new Position(3228, 3106), 1158, 0, new int[] {}, new String[] { "TODO" }),
	DAGANNOTH_KINGS("Dagannoth Kings", new Position(1914, 4368), 2881, 0, new int[] {}, new String[] { "TODO" }),
	THE_ALCHEMIST("The Alchemist", new Position(3825, 4768), 16080, 0, new int[] {}, new String[] { "TODO" }),
	KRAKEN_COVE("Kraken Cove", new Position(3043, 2500), 16010, 0, new int[] {}, new String[] { "TODO" }),
	ZULRAH("Zulrah", new Position(2200, 3056), 16641, 0, new int[] {}, new String[] { "TODO" }),
	VORKATH("Vorkath", new Position(2641, 3697), 28061, 0, new int[] { 2356, 165791, 165908 }, new String[] { "TODO" }),
	CERBERUS("Cerberus", new Position(1310, 1251), 25862, 0, new int[] {}, new String[] { "TODO" }),
	
	;
	
	@Getter private String name;
	@Getter private String[] information;
	@Getter private int catagory;
	@Getter private int[] models;
	@Getter private int monsterId;
	@Getter private String[] stats;
	@Getter private Position location;
	
	private static final int BOSS = 0, SKILLING = 1, SLAYER = 2, TRAINING = 3, MINIGAMES = 4;
	
	private static final Teleports[] VALUES = values();
	
	private Teleports(String name, Position location, int monsterId, int catagory, int[] models, String... information) {
		this.name = name;
		this.location = location;
		this.monsterId = monsterId;
		this.catagory = catagory;
		this.models = models;
		this.information = information;
	}
	
	/**
	 * Gets all elements that pertain to a certain catagory.
	 * @param catagory
	 * 			the catagory.
	 * @return
	 */
	public static Teleports[] forCatagory(int catagory) {
		ArrayList<Teleports> teleports = new ArrayList<Teleports>();
		for (Teleports teleport : VALUES) {
			if (teleport.getCatagory() == catagory) {
				teleports.add(teleport);
			}
		}
		return teleports.toArray(new Teleports[teleports.size()]);
	}
	
	public static String catagoryToString(int catagory) {
		switch (catagory) {
			case 0:
				return "Bossing Teleports";
			case 1:
				return "Skilling Teleports";
			case 2:
				return "Slayer Teleports";
			case 3:
				return "Training Teleports";
			default:
				return "Invalid Catagory";
		}
	}
	
}
