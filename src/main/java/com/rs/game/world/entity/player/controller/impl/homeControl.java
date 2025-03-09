package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.controller.Controller;


public class homeControl extends Controller {

	@Override
	public void start() {
		player.getDialogueManager().startDialogue("realityXTour");
		
	}
	
	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getPackets().sendGameMessage("You can't teleport untill you're done with the tutorial.");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getPackets().sendGameMessage("You can't teleport untill you're done with the tutorial.");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		return false;
	}
	
	
}