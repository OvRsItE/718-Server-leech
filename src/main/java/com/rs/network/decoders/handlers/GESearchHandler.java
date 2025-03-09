package com.rs.network.decoders.handlers;

import com.rs.game.world.entity.player.Player;
import com.rs.network.io.InputStream;

public class GESearchHandler {

	public static void handlePacket(Player player, InputStream packet) {
		int itemId = packet.readShort();
		//player.grandExchange().set(itemId);
	}

}
