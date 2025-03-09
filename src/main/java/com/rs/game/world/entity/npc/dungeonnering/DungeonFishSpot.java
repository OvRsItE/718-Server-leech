package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.DungeoneeringFishing;

@SuppressWarnings("serial")
public class DungeonFishSpot extends DungeonNPC {

	private DungeoneeringFishing.Fish fish;
	private int fishes;

	public DungeonFishSpot(int id, Position tile, DungeonManager manager, DungeoneeringFishing.Fish fish) {
		super(id, tile, manager, 1);
		this.fish = fish;
		setName(com.rs.utility.Utils.formatPlayerNameForDisplay(fish.toString()));
		fishes = 14;
	}

	@Override
	public void processNPC() {

	}

	public DungeoneeringFishing.Fish getFish() {
		return fish;
	}

	public int desecreaseFishes() {
		return fishes--;
	}

	public void addFishes() {
		fishes += com.rs.utility.Utils.random(5, 10);
	}
}
