package com.rs.game.world.entity.npc.barrelchest;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

@SuppressWarnings("serial")
public class BarrelNPC extends NPC {

    public BarrelNPC(int id, Position tile) {
	super(id, tile, -1, 0, true, true);
	setForceMultiArea(true);
	setNoDistanceCheck(true);
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
	ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
	List<Integer> playerIndexes = World.getRegion(getRegionId())
		.getPlayerIndexes();
	if (playerIndexes != null) {
	    for (int npcIndex : playerIndexes) {
		Player player = World.getPlayers().get(npcIndex);
		if (player == null || player.isDead() || player.isFinished()
			|| !player.isRunning())
		    continue;
		possibleTarget.add(player);
	    }
	}
	return possibleTarget;
    }

    @Override
    public void sendDeath(Entity source) {
	super.sendDeath(source);
    }
}