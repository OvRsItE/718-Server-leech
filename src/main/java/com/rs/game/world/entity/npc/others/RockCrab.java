package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

@SuppressWarnings("serial")
public final class RockCrab extends NPC {
		
	public RockCrab(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setForceMultiArea(true);
	}
	
	@Override
	public void processNPC() {
		checkPlayersInArea();
		if (getCombat().getTarget() == null) {
			if (getSpawnPosition() == getPosition()) {
				transformIntoNPC(1266);	
			} else if (getId() == 1265 && (getSpawnPosition() != getPosition())) {
				forceWalkRespawnTile();
			}
		}
		super.processNPC();
	}

	@Override
	public void finish() {
		transformIntoNPC(1266);
		super.finish();
	}
	
	/**
	 * Checks for players in the area of the rock crab.
	 * @return
	 */
	public void checkPlayersInArea() {
		for (Player player : World.getPlayers()) {
			if (!player.withinDistance(this, 2))
				continue;
			transformIntoNPC(1265);
			setTarget(player);
		}
	}
	
}