package com.rs.game.world.entity.player.content;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;


public class GnomeGlider {


	public static final Position
		MOUNTAIN = new Position(2846, 3499, 0),
		GRAND_TREE = new Position(2465, 3501, 3),
		CASTLE = new Position(3321, 3427, 0),
		DESERT = new Position(3283, 3213, 0),
		CRASH_ISLAND = new Position(2894, 2730, 0),
		OGRE = new Position(2544, 2970, 0);

		
	public static void handleButtons(final Player player, int componentId) {
		if (componentId == 24) {
			sendGnomeGliderTeleport(player, MOUNTAIN);
		} else if (componentId == 23) {
			sendGnomeGliderTeleport(player, GRAND_TREE);
		} else if (componentId == 25) {
			sendGnomeGliderTeleport(player, CASTLE);
		} else if (componentId == 26) {
			sendGnomeGliderTeleport(player, DESERT);
		} else if (componentId == 27) {
			sendGnomeGliderTeleport(player, OGRE);
		} else if (componentId == 22) {
			sendGnomeGliderTeleport(player, CRASH_ISLAND);
		}

	}

	
	public static void sendGnomeGliderTeleport(final Player player,
			final Position tile) {
		if (!player.getControlerManager().processObjectTeleport(tile))
			return;
		player.setNextPosition(tile);
		player.closeInterfaces();
		player.getPackets().sendGameMessage("You travel using the gnome glider.");
	}
	
	

}