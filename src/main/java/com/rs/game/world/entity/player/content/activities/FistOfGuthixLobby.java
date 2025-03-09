package com.rs.game.world.entity.player.content.activities;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Logger;

/**
 * Represents the Fist of Guthix lobby.
 * 
 * @author Jae <jae@xiduth.com>
 * @since Nov 28, 2013
 */
public class FistOfGuthixLobby {
	private final CopyOnWriteArrayList<Player> playersInQueue;
	/**
	 * Represents the player queue.
	 */
	public FistOfGuthixLobby(){
		playersInQueue = new CopyOnWriteArrayList<Player>();
	}
	/**
	 * @return Gets the players in the queue.
	 */
	public CopyOnWriteArrayList<Player> getPlayersInLobby() {
		return playersInQueue;
	}
	/**
	 * Adds player to the lobby queue.
	 * @param player
	 */
	public void addPlayerToLobby(Player player) {
		getPlayersInLobby().add(player);
		player.move(new Position(1642, 5600, 0));
		Logger.log("FistOfGuthixLobby", "Added " + player.getDisplayName() + " to the lobby."
				+ " New lobby size: " + playersInQueue.size() + " .");
	/**
	 * Sends the queued players to the game when the size limit is reached.
	 */
		int queueSize = playersInQueue.size();
		int maxSize = 15;
		if (queueSize == maxSize)  {
			sendPlayersToGame();
		}
	}
	/**
	 * Clears player queue and sends the queued players to the game.
	 */
	public void sendPlayersToGame() {
		CopyOnWriteArrayList<Player> playersToSend = new CopyOnWriteArrayList<Player>();
			for (Player player : playersInQueue) {
				playersToSend.add(player);
				player.getControlerManager().forceStop();
				playersInQueue.remove(player);
			}
			for (Player queuedPlayers : playersToSend) {
				queuedPlayers.getControlerManager().startControler("FistOfGuthixGame");
			}
		}

}
