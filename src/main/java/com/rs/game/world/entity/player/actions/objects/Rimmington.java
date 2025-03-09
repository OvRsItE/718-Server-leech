package com.rs.game.world.entity.player.actions.objects;

import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.Player;

/*
 * @Author Justin
 * Rimmington
 */

public class Rimmington {
	
	public static void Example(Player player,
			final WorldObject object) {
		//Insert what the object does here
	}

	public static boolean isObject(final WorldObject object) {
		switch (object.getId()) {
		default:
		return false;
		}
	}
	
	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 16154) { //Object ID
			Rimmington.Example(player, object); //Method of Action
		}
		
	}

}
