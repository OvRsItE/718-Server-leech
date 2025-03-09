package com.rs.game.world.entity.npc.dung;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public class Guardian extends NPC {

	private DungeonManager manager;
	private RoomReference reference;

	public Guardian(int id, Position tile, DungeonManager manager,
                    RoomReference reference) {
		super(id, tile, -1, 0, true, true);
		this.manager = manager;
		this.reference = reference;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		manager.updateGuardian(reference);
	}

}
