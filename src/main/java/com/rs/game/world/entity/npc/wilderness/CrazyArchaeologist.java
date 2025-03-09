/**
 * 
 */
package com.rs.game.world.entity.npc.wilderness;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.ForceTalk;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class CrazyArchaeologist extends NPC {

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public CrazyArchaeologist(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}
	
	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		setNextForceTalk(new ForceTalk("Ow!"));
	}
 
}
