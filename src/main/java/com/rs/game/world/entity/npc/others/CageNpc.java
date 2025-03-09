package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.impl.Cages;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public class CageNpc extends NPC {
	
	private Player target = null;

	public CageNpc(int id, Position tile, Player target) {
		super(id, tile, -1, 0, true, true);
		this.target = target;
		setForceMultiArea(true);
	}
	
	@Override
	public void processNPC() {
		if(!(target.getControlerManager().getControler() instanceof Cages) || target == null || World.getPlayerByDisplayName(target.getDisplayName()) == null)
		   super.sendDeath(this);
		
		super.processNPC();
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
	}
	
}
