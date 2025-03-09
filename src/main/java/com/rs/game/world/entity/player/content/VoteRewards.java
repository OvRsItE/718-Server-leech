package com.rs.game.world.entity.player.content;

import java.util.ArrayList;
import java.util.List;

import com.rs.Launcher;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Misc;

/**
 * 
 * @author Josh'
 *
 */
public class VoteRewards {
	
	private static String winner;

	private static List<String> voters = new ArrayList<String>();
	
	public static void addVoterToList(String username) {
		voters.add(username);
		//Player.getPackets().sendGameMessage("<col=ff00ff>You have been entered into the raffle good luck!.");
	}
	
	public static String chooseRandomVoter() {
		int voteListSize = voters.size();
		int randomVoter = Misc.random(voteListSize - 1);
		String voterUsername = voters.get(randomVoter);
		winner = voterUsername;
		appendReward(winner);
		voters.clear();
		return voterUsername;
	}
	
	public static void appendReward(String winner) {
		for (Player p : World.getPlayers()) {
			if (p == null)
				continue;
			Player player = (Player) p;
			if (player.getUsername().equalsIgnoreCase(winner) && player != null) {
				player.getPackets().sendGameMessage("<col=ff0000>You have won the voting prize today, your reward has been added to your inventory.");
				player.getInventory().addItem(995, 1);//change to whatever u want
			}
			player.getPackets().sendGameMessage("<col=ff00ff>The voting prize has been drawn for today, " + winner + " is the winner!");
			player.getPackets().sendGameMessage("<col=ff00ff>If you wish to be entered in the voting raffle, make sure you ::vote!");
			player.getPackets().sendGameMessage("<col=ff00ff>The voting raffle is drawn every 24 hours.");

		}
	}

}
