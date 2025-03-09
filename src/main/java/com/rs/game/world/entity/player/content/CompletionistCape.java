package com.rs.game.world.entity.player.content;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;

public class CompletionistCape {
	
	public static int ONE = 1;
	public static int COMPLETIONISTCAPE = 20769;
	public static int COMPLETIONISTHOOD = 20770;
	public static int TRIMMEDCAPE = 20771;
	public static int TRIMMEDHOOD = 20772;

	public static void CheckCompletionist(Player player) {
		if (player.getSkills().getLevelForXp(Skills.ATTACK) >= 99
				&& player.getSkills().getLevelForXp(Skills.STRENGTH) >= 99
				&& player.getSkills().getLevelForXp(Skills.DEFENCE) >= 99
				&& player.getSkills().getLevelForXp(Skills.HITPOINTS) >= 99
				&& player.getSkills().getLevelForXp(Skills.RANGE) >= 99
				&& player.getSkills().getLevelForXp(Skills.MAGIC) >= 99
				&& player.getSkills().getLevelForXp(Skills.RUNECRAFTING) >= 99
				&& player.getSkills().getLevelForXp(Skills.FISHING) >= 99
				&& player.getSkills().getLevelForXp(Skills.AGILITY) >= 99
				&& player.getSkills().getLevelForXp(Skills.COOKING) >= 99
				&& player.getSkills().getLevelForXp(Skills.PRAYER) >= 99
				&& player.getSkills().getLevelForXp(Skills.THIEVING) >= 99
				&& player.getSkills().getLevelForXp(Skills.MINING) >= 99
				&& player.getSkills().getLevelForXp(Skills.SMITHING) >= 99
				&& player.getSkills().getLevelForXp(Skills.SUMMONING) >= 99
				&& player.getSkills().getLevelForXp(Skills.SLAYER) >= 99
				&& player.getSkills().getLevelForXp(Skills.CRAFTING) >= 99
				&& player.getSkills().getLevelForXp(Skills.WOODCUTTING) >= 99
				&& player.getSkills().getLevelForXp(Skills.FIREMAKING) >= 99
				&& player.getSkills().getLevelForXp(Skills.FLETCHING) >= 99
				&& player.getSkills().getLevelForXp(Skills.HERBLORE) >= 99
				&& player.getSkills().getLevelForXp(Skills.CONSTRUCTION) >= 99
				&& player.getSkills().getLevelForXp(Skills.FARMING) >= 99
				&& player.getSkills().getLevelForXp(Skills.HUNTER) >= 99
				&& player.getSkills().getLevelForXp(Skills.DUNGEONEERING) >= 120 && player.isCompletedFightCaves()
				// && player.isCompletedFightKiln()
				&& player.isCompletionist == 0) {
			player.isCompletionist = 1;
			World.sendWorldMessage("<col=<col=ff8c38>><img=4>News: " + player.getDisplayName()
					+ " has just been awarded the Completionist Cape!" + "</col> ", false);
			player.sm("Congratulations, you have been awarded the Completionist Cape!");
			player.getInventory().addItem(COMPLETIONISTCAPE, ONE);
			player.getInventory().addItem(COMPLETIONISTHOOD, ONE);
		}
	}
}
