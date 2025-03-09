/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.teleport.Teleports;

/**
 * @author ReverendDread
 * Aug 4, 2018
 */
public class TeleporterDialogue extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("What would you like to view?", "Training", "Bossing", "Slayer", "Minigames", "Skilling");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
			case OPTION_1:
				player.getDialogueManager().startDialogue("MTLowLevelTraining");
				break;
			case OPTION_2:
				player.getDialogueManager().startDialogue("MTHighLevelBosses");
				break;
			case OPTION_3:
				player.getDialogueManager().startDialogue("MTSlayerDungeons");
				break;
			case OPTION_4:
				player.getDialogueManager().startDialogue("MinigameTeleports");
				break;
			case OPTION_5:
				player.getDialogueManager().startDialogue("SkillingTeleports");
				break;
		}
	}

	@Override
	public void finish() {}

}
