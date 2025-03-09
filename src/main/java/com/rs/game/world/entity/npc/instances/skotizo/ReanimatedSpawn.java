/**
 * 
 */
package com.rs.game.world.entity.npc.instances.skotizo;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class ReanimatedSpawn extends NPC {
	
	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param faceDirection
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public ReanimatedSpawn(Position tile) {
		super(27287, tile, -1, 0, true, false);
		setForceMultiArea(true);
		setAtMultiArea(true);
		setForceFollowClose(true);
	}

}
