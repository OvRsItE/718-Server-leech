/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah;

import com.rs.game.world.entity.player.Player;

/**
 * @author ReverendDread
 * Jul 23, 2018
 */
public interface Sequence {
	
	public int attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target);
	
}
