package com.rs.game.world.entity.player.content.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Logger;

public class Lobby {
	
	private static class Timer extends TimerTask {

		@Override
		public void run() {
			try {
				synchronized (lock) {
					if (!isStartedGame()) {
					if (getLobby().size() >= 4) {
						setStartedGame(true);
						passPlayersToArena();
					} else
						endGame();
					} else {
						if (HungerGames.getPlayers().size() == 1) {
							endGame();
						} else if (minutes == 1)
							sendGameMessage("Hit the 1 minute marker. Currently "
									+ HungerGames.getPlayers().size()
									+ " players left!");
						else if (minutes == 2)
							sendGameMessage("Hit the 2 minute marker. Currently "
									+ HungerGames.getPlayers().size()
									+ " players left!");
						else if (minutes == 3)
							sendGameMessage("Hit the 3 minute marker. Currently "
									+ HungerGames.getPlayers().size()
									+ " players left!");
						else if (minutes == 11) {
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									if (!isStartedGame()) {
										stop();
										return;
									}
									for (Player player : HungerGames
											.getPlayers())
										player.applyHit(new Hit(player, 150,
												HitLook.REGULAR_DAMAGE));
								}

							}, 0, 0);
						}
					}
					minutes++;
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}

	public static int minutes;

	private static final List<Player> lobby = new ArrayList<Player>();
	public static final Object lock = new Object();
	private static Timer gameTask = null;

	private static boolean startedGame;

	public static void endGame() {
	 if(gameTask != null)
		gameTask.cancel();
		gameTask = null;
		minutes = 0;
		if (startedGame)
			HungerGames.setWinner();
		startedGame = false;
		sendGameMessage("We have a victor!");
		HungerGames.getPlayers().clear();
		HungerGames.removeEntities();
	}

	private static void enterArena(Player player) {
		HungerGames.placePlayer(player);
	}

	public static List<Player> getLobby() {
		return lobby;
	}

	public static boolean isStartedGame() {
		return startedGame;
	}

	public static void join(Player player) {
		synchronized (lock) {
			player.getControlerManager().startControler("LobbyController");
		}
	}

	public static void leave(Player player) {
		getLobby().remove(player);
		player.setNextPosition(Constants.HOME_PLAYER_LOCATION1);
		player.sm("To scared to compete? You wimp.");
		startGame(false);
	}

	private static void passPlayersToArena() {
		int amount = 0;
		for (Iterator<Player> it = getLobby().iterator(); it.hasNext();) {
			if (amount >= 4)
				return;
			Player player = it.next();
			player.stopAll();
			enterArena(player);
			it.remove();
			amount++;
			getLobby().remove(player);
		}
	}

	private static void sendGameMessage(String message) {
		for (Player player : HungerGames
				.getPlayers())
			player.sm(message);
	}

	public static void setStartedGame(boolean startedGame) {
		Lobby.startedGame = startedGame;
	}

	public static void startGame(boolean end) {
		if (end)
			endGame();
		if (gameTask == null && getLobby().size() >= 4) { // 2players in
			gameTask = new Timer();
			CoresManager.fastExecutor.scheduleAtFixedRate(gameTask, end ? 60000
					: 5000, 60000);
		} else {
			if (getLobby().size() < 1)
				endGame();
		}
	}
}
