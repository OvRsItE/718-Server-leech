/**
 * 
 */
package com.rs.game.world.entity.npc.dragons;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.toxin.ToxinType;

/**
 * @author ReverendDread
 * Sep 14, 2018
 */
public class AdamantDragon extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8831587311793634473L;

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public AdamantDragon(Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(NPC.asOSRS(8090), tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		this.getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		this.getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
	}

}
