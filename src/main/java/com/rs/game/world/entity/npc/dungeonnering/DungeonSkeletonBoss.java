package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public class DungeonSkeletonBoss extends DungeonNPC {

	private DivineSkinweaver boss;

	public DungeonSkeletonBoss(int id, Position tile, DungeonManager manager, RoomReference reference, double multiplier) {
		super(id, tile, manager, multiplier);
		setForceAgressive(true);
		//setIntelligentRouteFinder(true);
		setLureDelay(0);
		boss = (DivineSkinweaver) getNPC(10058);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		boss.removeSkeleton(this);
	}

	@Override
	public void drop() {

	}

}
