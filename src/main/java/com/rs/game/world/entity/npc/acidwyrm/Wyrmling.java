package com.rs.game.world.entity.npc.acidwyrm;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Animation;

@SuppressWarnings("serial")
public class Wyrmling extends NPC {

	private static final int SPAWN_EMOTE = 12795;
	
	public Wyrmling(Position tile, int mapAreaNameHash) {
		super(16026, tile, mapAreaNameHash, 0, true, true);
		setAtMultiArea(true);
		animate(new Animation(SPAWN_EMOTE));
		setForceFollowClose(true);
	}
	
	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
	}

}
