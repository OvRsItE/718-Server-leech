/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath.attack;

import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.player.Player;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public interface VorkathAttack {

	/**
	 * Handles attacks for Vorkath.
	 * @param vorkath
	 * 			vorkath.
	 * @param target
	 * 			the player.
	 * @return
	 * 			the delay till next attack is avaliable.
	 */
	public abstract int attack(VorkathNPC vorkath, Player target);
	
}
