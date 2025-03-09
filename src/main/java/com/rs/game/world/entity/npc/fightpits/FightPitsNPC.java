package com.rs.game.world.entity.npc.fightpits;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.FightPits;
import com.rs.game.world.entity.updating.impl.Graphics;

@SuppressWarnings("serial")
public class FightPitsNPC extends NPC {


	public FightPitsNPC(int id, Position tile) {
		super(id, tile, -1, 0, true, true);
		setForceMultiArea(true);
		setNoDistanceCheck(true);
	}
	
	@Override
	public void sendDeath(Entity source) {
		setNextGraphics(new Graphics(2924 + getSize()));
		super.sendDeath(source);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for(Player player : FightPits.arena)
			possibleTarget.add(player);
		return possibleTarget;
	}

}
