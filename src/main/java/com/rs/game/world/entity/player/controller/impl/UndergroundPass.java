package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.map.Position;

public class UndergroundPass {

	
	public boolean processMagicTeleport(Position toTile) {
		//player.getPackets().sendGameMessage("You can't teleport out of this!");
		return false;
	}

	
	public boolean processItemTeleport(Position toTile) {
	//	player.getPackets().sendGameMessage("You can't teleport out of the arena!");
		return false;
	}

	
	public boolean processObjectTeleport(Position toTile) {
	//	player.getPackets().sendGameMessage("You can't teleport out of the arena!");
		return false;
	}
	
}
