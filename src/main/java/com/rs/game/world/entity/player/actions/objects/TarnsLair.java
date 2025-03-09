package com.rs.game.world.entity.player.actions.objects;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

/*
 * @Author Adam
 * RodiksZone
 */

public class TarnsLair {
	
	
	public static void HandlePassage(Player player,
			final WorldObject object) {
		if (object.getId() == 20871){
			player.setNextPosition(new Position(3149, 4644, 0));
		}
		if (object.getId() == 20466){
			player.setNextPosition(new Position(3185, 4601, 0));
		}
		if (object.getId() == 20572){
			player.getPackets().sendGameMessage("There seems to be something blocking this passage. You cannot pass through.");
		}
		if (object.getId() == 20575){
			player.getPackets().sendGameMessage("There seems to be something blocking this passage. You cannot pass through.");
		}
	}
	
	
	
	

	public static boolean isObject(final WorldObject object) {
		switch (object.getId()) {
		case 20871:
		case 20466:
		case 20572:
		case 20575:
		return true;
		default:
		return false;
		}
	}
	
	public static void HandleObject(Player player, final WorldObject object) {
		final int id = object.getId();
		if (id == 20871 || id == 20466 || id == 20572 || id == 20575) { 
			TarnsLair.HandlePassage(player, object);
		}
		
		
	}

}