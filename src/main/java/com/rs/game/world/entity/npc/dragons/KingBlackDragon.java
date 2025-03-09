package com.rs.game.world.entity.npc.dragons;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class KingBlackDragon extends NPC {

	public KingBlackDragon(int id, Position tile, int mapAreaNameHash,
                           boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
	}

	public static boolean atKBD(Position tile) {
		if ((tile.getX() >= 2250 && tile.getX() <= 2292)
				&& (tile.getY() >= 4675 && tile.getY() <= 4710))
			return true;
		return false;
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.1; // 0.6
	}

}
