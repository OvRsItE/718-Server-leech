package com.rs.game.world.entity.player.content.skills.construction;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * 
 * @author Justin
 *
 */

public class Clock {
	
	public static int OAKPLANK = 8778;
	public static int CLOCKWORK = 8792;
	public static int HAMMER = 2347;
	
	public static void CheckClock(Player player) {
		if (player.getSkills().getLevelForXp(Skills.CONSTRUCTION) < 25) {
			player.getPackets().sendGameMessage("You need a construction level of 25 to build this.");
		} else if (!player.getInventory().containsItem(OAKPLANK, 2)) {
			player.getPackets().sendGameMessage("You need atleast 2 oak planks to build this clock.");
		} else if (!player.getInventory().containsItem(CLOCKWORK, 1)) {
			player.getPackets().sendGameMessage("You need atleast 1 clockwork to build this clock.");
		} else if (!player.getInventory().containsItem(HAMMER, 1)) {
			player.getPackets().sendGameMessage("You need a hammer to build this clock.");
		} else {
			player.getInventory().deleteItem(OAKPLANK, 2);
			player.getInventory().deleteItem(CLOCKWORK, 1);
			player.getSkills().addXp(Skills.CONSTRUCTION, 26);
			player.animate(new Animation(898));
			player.getPackets().sendGameMessage("You successfully build a clock.");
		}
	}
}