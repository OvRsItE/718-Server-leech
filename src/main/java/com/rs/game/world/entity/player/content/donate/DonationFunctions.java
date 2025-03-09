/**
 * 
 */
package com.rs.game.world.entity.player.content.donate;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

public class DonationFunctions {
	
	private static final String DONATE_MESSAGE_PREFIX = "<img=4>[<col=00ff00>DonationManager</col>]:";
	
	/**
	 * Calculates the amount of bonus tokens the player will get based on donation amount
	 * @param tokenCount The amount of tokens donated for
	 * @return The amount of bonus tokens to award
	 */
	public static int calculateBonusTokens(int tokenCount) {
		double bonus = tokenCount >= 100 ? 0.20 : tokenCount <= 10 ? tokenCount <= 5 ? 0 : 0.10 : 0.15; 
		return (int) (tokenCount >= 75 ? Math.ceil(bonus * tokenCount) : Math.round(bonus * tokenCount));
	}
	
	/**
	 * Gives the player extra rewards depending on donation amount
	 * @param player The player claiming the reward
	 * @param tokenCount The amount of tokens donated for (before bonus tokens are added)
	 */
	public static void giveBonusRewards(Player player, int tokenCount) {
		int box = Utils.random(10);
//		if (box <= 5 && tokenCount >= 50) {
//			player.getBank().addItem(13346, 1, true);
//			World.sendWorldMessage(DONATE_MESSAGE_PREFIX + " " + player.getDisplayName() + " has just received a FREE Raid Mystery Box for donating $50+", false);
//		}
		if (box >= 6  && tokenCount >= 5) {
			player.getBank().addItem(6199, 1, true);
			World.sendWorldMessage(DONATE_MESSAGE_PREFIX + " <col=00ff00>" + player.getDisplayName() + "</col> has just received a FREE Mystery Box for donating $5+", true);
		}
		if (tokenCount >= 1 && tokenCount <= 5) {
			player.doubleExperienceTimer += 3000;
			player.sendMessage("<col=ff0000>You receive 30 minutes of bonus XP for donating $1 to $5");
		}
		if (tokenCount >= 6 && tokenCount <= 10) {
			player.doubleExperienceTimer += 6000;
			player.sendMessage("<col=ff0000>You receive 1 hour of bonus XP for donating $6 to $10");
		}
		if (tokenCount >= 11 && tokenCount <= 20) {
			player.doubleExperienceTimer += 12000;
			player.sendMessage("<col=ff0000>You receive 2 hours of bonus XP for donating $11 to $20");
		}
		if (tokenCount >= 21 && tokenCount <= 50) {
			player.doubleExperienceTimer += 30000;
			player.sendMessage("<col=ff0000>You receive 5 hours of bonus XP for donating $21 to $50");
		}
		if (tokenCount >= 51 && tokenCount <= 149) {
			player.doubleExperienceTimer += 90000;
			player.sendMessage("<col=ff0000>You receive  15 hours of bonus XP for donating $51 to $150");
		}
		if (tokenCount >= 150) {
			player.doubleExperienceTimer += 144000;
			player.sendMessage("<col=ff0000>You receive  24 hours of bonus XP for donating more than $150");
		}
		World.sendWorldMessage(DONATE_MESSAGE_PREFIX + " <col=00ff00>" + Utils.formatPlayerNameForDisplay(player.getDisplayName()) + "</col> has just donated for <col=00ff00>" + tokenCount + "</col> Valius Tokens! Thank you!", false);
		
	}
}
