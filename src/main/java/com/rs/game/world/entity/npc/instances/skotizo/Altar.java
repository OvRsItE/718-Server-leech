/**
 * 
 */
package com.rs.game.world.entity.npc.instances.skotizo;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class Altar extends NPC {

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param faceDirection
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public Altar(int index, Position tile) {
		super(27288 + (index * 2), tile, -1, 0, true, true);
		setForceMultiArea(true);
		setAtMultiArea(true);
		setCantFollowUnderCombat(true);
		World.spawnObject(new WorldObject(128923, 10, 0, getX(), getY(), getZ()));
	}
	
	@Override
	public void finish() {
		World.spawnObject(new WorldObject(128924, 10, 0, getX(), getY(), getZ()));
		super.finish();
	}

}
