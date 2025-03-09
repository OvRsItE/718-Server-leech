package com.rs.game.world.entity.player.content;

import com.rs.game.world.entity.player.Player;
/**
 * 
 * @author Jazzy Ya | Nexon | Fuzen Seth
 *
 */
public class SerenityOptions {

	public static void TogglePlayerInfo(Player player) {
		player.sm("We will tell you if new player joins now.");
	}
	public static void unTogglePlayerInfo(Player player) {
		player.sm("We will NOT tell you if new player joins.");
	}
	public static void setNewsNegative(Player player) {
		player.setnews = false;
		
		player.sm("You have removed news upon login.");
	}
	public static void setNewsPositive(Player player) {
		player.setnews = true;
		player.sm("You have set news active upon login.");
	}
}
