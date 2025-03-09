/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author ReverendDread
 * Aug 4, 2018
 */
public class NoticeboardD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("What would you like to view?", "Account Information", "Active Events", "Nevermind");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {		
			case OPTION_1:
				showInformation();
				break;			
			case OPTION_2:
				showActiveEvents();
				break;
		}
		end();
	}

	@Override
	public void finish() {}
	
	
	private final void showInformation() {
		String startTime = "00:00";
		int h = player.onlinetime / 60 + Integer.valueOf(startTime.substring(0, 1));
		int m = player.onlinetime % 60 + Integer.valueOf(startTime.substring(3, 4));
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Account Information");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "--- General Information ---");
		player.getPackets().sendIComponentText(275, 12, "Gamemode: " + player.getGameMode().getName());
		player.getPackets().sendIComponentText(275, 13, "Time Played: " + h + ":" + m);
		player.getPackets().sendIComponentText(275, 14, "");
		player.getPackets().sendIComponentText(275, 15, "--- PvP Statistics ---");
		player.getPackets().sendIComponentText(275, 16, "Total Kills: " + player.getKillCount());
		player.getPackets().sendIComponentText(275, 17, "Total Deaths: " + player.getDeathCount());
		if (player.getKillCount() > 0)
			player.getPackets().sendIComponentText(275, 18, "KDR: " + (float) (player.getKillCount() / player.getDeathCount()));
		else 
			player.getPackets().sendIComponentText(275, 18, "KDR: NaN");
		player.getPackets().sendIComponentText(275, 19, "");
		player.getPackets().sendIComponentText(275, 20, "--- Points Information ---");
		player.getPackets().sendIComponentText(275, 21, "Reaper Points: " + player.getBossSlayer().getReaperPoints());
		player.getPackets().sendIComponentText(275, 22, "Slayer Points: " + player.getSlayerManager().getPoints());
		player.getPackets().sendIComponentText(275, 23, "Trivia Points: " + player.getTriviaPoints());
		player.getPackets().sendIComponentText(275, 24, "Valius Points: " + player.getPvmPoints());
		player.getPackets().sendIComponentText(275, 25, "Vote Points: " + player.getVotePoints());
		player.getPackets().sendIComponentText(275, 26, "PvP Points: " + player.getPvPPoints());
		player.getPackets().sendIComponentText(275, 27, "Loyalty Points: " + player.getLoyaltyPoints());
		for (int i = 28; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}
	
	private final void showActiveEvents() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Active events");
		for (int i = 10; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

}
