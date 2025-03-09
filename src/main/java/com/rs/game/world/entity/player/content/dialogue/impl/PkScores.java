package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.KillStreakRank;
import com.rs.utility.PkRank;


/**
 * @author Justin
 */


public class PkScores extends Dialogue {

	public PkScores() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Chose a HighScore", "Kills-Deaths", "Kill Streaks");
	}

	@Override
	public void run(int interfaceId, int componentId) {
	 if(stage == 1) {
		if(componentId == OPTION_1) {
			player.getInterfaceManager().closeChatBoxInterface();
			PkRank.showRanks(player);
		} else if(componentId == OPTION_2) {
			player.getInterfaceManager().closeChatBoxInterface();
			KillStreakRank.showRanks(player);
		}
	 }
		
	}

	@Override
	public void finish() {
		
	}
	
}