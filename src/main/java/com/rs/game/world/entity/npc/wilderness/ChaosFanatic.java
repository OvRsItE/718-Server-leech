/**
 * 
 */
package com.rs.game.world.entity.npc.wilderness;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class ChaosFanatic extends NPC {

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param faceDirection
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public ChaosFanatic(int id, Position tile, int mapAreaNameHash, int faceDirection,
                        boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
	}

}
