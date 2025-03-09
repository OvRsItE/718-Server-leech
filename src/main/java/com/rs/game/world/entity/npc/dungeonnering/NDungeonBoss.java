package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.skills.dungeoneering.Dungeoneering;

@SuppressWarnings("serial")
public class NDungeonBoss extends NPC {

	private Dungeoneering.Dungeon dungeon;

	public NDungeonBoss(int id, Position tile, Dungeoneering.Dungeon dungeon) {
		super(id, tile, dungeon.getDungeonBossRoomHash(), false, true);
		this.dungeon = dungeon;
		setForceMultiArea(true);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		dungeon.openStairs();
	}

	public Dungeoneering.Dungeon getDungeon() {
		return dungeon;
	}
}
