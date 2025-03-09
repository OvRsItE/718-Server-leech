package com.rs.game.world.entity.player.content.activities;

import com.rs.game.map.Position;

/**
 * Represents necessities of the game.
 * 
 * @author Jae <jae@xiduth.com>
 * @since Nov 28, 2013
 */
public class FistOfGuthixConfiguration {
	public static Position LOBBY_LOCATION;
	public static Position HALL_LOCATION;
	public static Position GAME_LOCATION;
public FistOfGuthixConfiguration(){
	LOBBY_LOCATION = new Position(1642,5600, 0);
	GAME_LOCATION = new Position(1663,5695, 0);
	HALL_LOCATION = new Position(1675,5599, 0);
}
}
