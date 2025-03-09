package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.skills.construction.House;
import com.rs.game.world.entity.player.controller.Controller;

public class ConstructionControler extends Controller {
	
	private House house;
	private int[] boundChuncks;
	
	@Override
	public void start() {
		//house = new House();
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8); 
		//house.initHouse(boundChuncks, true);
		player.setNextPosition(new Position(boundChuncks[0]*8 + 35, boundChuncks[1]*8 + 35,0));
		player.getPackets().sendGameMessage("Welcome to your house! Make a bed or wardrobe!");
	}
	
	boolean remove = true;
}