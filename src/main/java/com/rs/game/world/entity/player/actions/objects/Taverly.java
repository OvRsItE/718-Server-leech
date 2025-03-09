package com.rs.game.world.entity.player.actions.objects;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

public class Taverly {
	
	public static void ExitDung(Player player,
			final WorldObject object) {
		player.useStairs(828, new Position(2885, 3395, 0), 1, 2);
	}
	
	public static void EntranceDung(Player player,
			final WorldObject object) {
		player.useStairs(828, new Position(2885, 9795, 0), 1, 2);
	}
	public static void SlayDungExit(Player player,
			final WorldObject object) {
		player.useStairs(828, new Position(2924, 3408, 0), 1, 2);
	}
	public static void SlayDungEnter(Player player,
			final WorldObject object) {
		player.useStairs(828, new Position(2219, 4532, 0), 1, 2);
	}

	public static boolean isObject(final WorldObject object) {
		switch (object.getId()) {
		case 74864:
		case 66991:
		case 67044:
		case 67043:
		return true;
		default:
		return false;
		}
	}
	
	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 74864) { 
			Taverly.ExitDung(player, object); 
		}
		if (id == 66991) { 
			Taverly.EntranceDung(player, object); 
		}
		if (id == 67044) { 
			Taverly.SlayDungExit(player, object); 
		}
		if (id == 67043) { 
			Taverly.SlayDungEnter(player, object); 
		}
		
	}

}
