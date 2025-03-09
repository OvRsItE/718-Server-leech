package com.rs.game.world.entity.npc.nomad;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public class FakeNomad extends NPC {
	
	private Nomad nomad;
	
	public FakeNomad(Position tile, Nomad nomad) {
		super(8529, tile, -1, 0, true, true);
		this.nomad = nomad;
		setForceMultiArea(true);
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		nomad.destroyCopy(this);
	}
	
}
