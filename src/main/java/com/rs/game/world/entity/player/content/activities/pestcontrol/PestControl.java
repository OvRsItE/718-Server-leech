package com.rs.game.world.entity.player.content.activities.pestcontrol;

import java.util.LinkedList;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.others.VoidKnight;
import com.rs.game.world.entity.player.DonatorRanks;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

public class PestControl {

	public static boolean isInsidePestGame(Position Position) {
		return (Position.getX() >= 2625 && Position.getY() <= 2624 && Position.getX() <= 2691 && Position.getY() >= 2559);
	}

	public static boolean isInsideLobby(Position Position) {
		return (Position.getX() >= 2660 && Position.getY() <= 2643 && Position.getX() <= 2663 && Position.getY() >= 2638);
	}

	public static void loginCheck(Player player) {
		if (isInsidePestGame(player.getPosition())) {
			player.setNextPosition(OUTSIDE_LOBBY);
		}
	}

	private static final Position VOID_KNIGHT_LOCATION = new Position(2656, 2592, 0);
	public static LinkedList<Player> lobbyPlayers = new LinkedList<Player>();
	public static LinkedList<Player> gamePlayers = new LinkedList<Player>();
	public static Position GAME_LOCATION = new Position(2657, 2610, 0);
	public static Position OUTSIDE_LOBBY = new Position(2657, 2639, 0);
	private static Position INSIDE_LOBBY = new Position(2661, 2639, 0);
	public static NPC[] portal = new NPC[4];
	public static NPC[] portalAlive = new NPC[4];
	public static boolean startedGame = false;
	public static int lastIndex;
	public static final int MINIMUM_PLAYERS = 1;
	public static NPC voidKnight;
	public static boolean restarted = false;
	public static int[] PESTS_NOVICE = { 3772, 3762, 3742, 3732, 3747, 3727, 3752 };
	public static int[] PESTS_OTHER = { 3773, 3764, 3743, 3734, 3748, 3728, 3754, 3774, 3766, 3744, 3736, 3749, 3729, 3756, 3775, 3768, 3745, 3738, 3750, 3730, 3758, 3776, 3770, 3746, 3740, 3751,
			3731, 3760 };
	public static boolean[] PORTAL_STATE = { false, false, false, false };
	
	public static void joinLobby(Player player) {
		if (startedGame) {
			int minutes = PestControler.playTimer / 100;
			int seconds = (int) ((PestControler.playTimer - (minutes * 100)) * 0.6);
			String time = (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
			player.sm("There is already a game in progress, please wait " + time + " before joining the lobby.");
			return;
		}
		lobbyPlayers.add(player);
		player.isInPestControlLobby = true;
		player.isInPestControlGame = false;
		PestControler.startedWaitTimer = true;
		player.setNextPosition(INSIDE_LOBBY);
		player.getControlerManager().startControler("PestControler");
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 407);
		player.sm("You board the lander.");
	}

	public static void restart(final Player player) {
		if (player.getBoneDelay() > Utils.currentTimeMillis())
			return;
		player.setBoneDelay(100000);
		startedGame = false;
		PestControler.waitTimer = 50;
		PestControler.startedWaitTimer = true;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getControlerManager().startControler("PestControler");

			}
		});
	}

	public static void reJoinLobby(final Player player) {
		if (player.getBoneDelay() > Utils.currentTimeMillis())
			return;
		player.setBoneDelay(10);
		startedGame = false;
		PestControler.startedWaitTimer = true;
		PestControler.waitTimer = 50;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getControlerManager().startControler("PestControler");
				player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 407);
			}
		});
	}

	public static void leaveLobby(Player player) {
		if (lobbyPlayers.contains(player)) {
			lobbyPlayers.remove(player);
		}
		player.getControlerManager().forceStop();
		player.isInPestControlLobby = false;
		player.isInPestControlGame = false;
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
		player.setNextPosition(OUTSIDE_LOBBY);
	}

	public static void startGame(Player player) {
		if (lobbyPlayers.contains(player)) {
			lobbyPlayers.remove(player);
			gamePlayers.add(player);
		}
		PestControl.lobbyPlayers.remove(player);
		PestControler.startedWaitTimer = false;
		PestControler.waitTimer = 400;
		PestControler.playTimer = 400;
		PestControler.startedPlayTimer = true;
		PestControl.startedGame = true;
		player.getControlerManager().forceStop();
		player.isInPestControlLobby = false;
		player.isInPestControlGame = true;
		startedGame = true;
		player.getControlerManager().startControler("PestControler");
		spawnMonsters(player);
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 408);
		player.setNextPosition(GAME_LOCATION);
	}

	public static void endGame(Player player) {
		if (gamePlayers.contains(player)) {
			gamePlayers.remove(player);
		}
		player.isInPestControlLobby = false;
		player.isInPestControlGame = false;
		startedGame = false;
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
		player.setNextPosition(OUTSIDE_LOBBY);
		lobbyPlayers.remove(player);
		gamePlayers.remove(player);
		player.getControlerManager().forceStop();
		if (portal[1].isDead() && portal[2].isDead() && portal[3].isDead() && portal[4].isDead()) {
			player.sm("You won the Pest Control game and are awarded 5 pest control points!");
			player.pestPoints += 5 * (player.getDonationManager().hasRank(DonatorRanks.DRAGON) ? 2 : 1);
			player.pestWins++;
		} else {
			player.sm("You lost the Pest Control game.");
		}
		player.getControlerManager().forceStop();
	}

	public static void spawnMonsters(Player player) {
		voidKnight = new VoidKnight(3782, VOID_KNIGHT_LOCATION, 1, true, true);
		portal[0] = new NPC(6154, new Position(2628, 2591, 0), -1, 0, true, true);
		portal[1] = new NPC(6157, new Position(2645, 2569, 0), -1, 0, true, true);
		portal[2] = new NPC(6156, new Position(2669, 2570, 0), -1, 0, true, true);
		portal[3] = new NPC(6155, new Position(2680, 2588, 0), -1, 0, true, true);
		monsterTimer(player);
		for (int i = 0; i < 4; i++) {
		}
	}

	public static void spawnOpenedPortals(int id) {
		switch (id) {
		case 0:
			World.spawnNPC(6142, new Position(portal[0].getPosition()), -1, 0, true);
			portal[0].sendDeath(portal[0]);
			break;
		case 1:
			World.spawnNPC(6145, new Position(portal[1].getPosition()), -1, 0, true);
			portal[1].sendDeath(portal[1]);
			break;
		case 2:
			World.spawnNPC(6144, new Position(portal[2].getPosition()), -1, 0, true);
			portal[2].sendDeath(portal[2]);
			break;
		case 3:
			World.spawnNPC(6143, new Position(portal[3].getPosition()), -1, 0, true);
			portal[3].sendDeath(portal[3]);
			break;
		}
	}

	public static void monsterTimer(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (lastIndex == 7) {
					lastIndex = 0;
				}
				NPC monsters = new NPC(PESTS_NOVICE[lastIndex], getMonstersWorldTile(), -1, 0, true, true);
				System.out.println("Just spawned PEST:" + monsters);
				player.getControlerManager().startControler("PestControler");
				if (startedGame == false) {
					this.stop();
				}
			}
		});
	}

	public static Position getMonstersWorldTile() {
		if (getPortals(0).isDead()) {
			World.spawnNPC(PESTS_NOVICE[lastIndex], new Position(2634, 2593, 0), -1, 0, false);
			return new Position(2634, 2593, 0);
		} else if (getPortals(1).isDead()) {
			World.spawnNPC(PESTS_NOVICE[lastIndex], new Position(2670, 2576, 0), -1, 0, false);
			return new Position(2634, 2593, 0);
		} else if (getPortals(2).isDead()) {
			World.spawnNPC(PESTS_NOVICE[lastIndex], new Position(2646, 2574, 0), -1, 0, false);
			return new Position(2646, 2574, 0);
		} else if (getPortals(3).isDead()) {
			World.spawnNPC(PESTS_NOVICE[lastIndex], new Position(2676, 2589, 0), -1, 0, false);
			return new Position(2676, 2589, 0);
		}
		lastIndex++;
		return new Position(2643, 2592, 0);
	}

	public static void leaveGame(Player player) {
		if (gamePlayers.contains(player)) {
			gamePlayers.remove(player);
		}
		player.isInPestControlLobby = false;
		player.isInPestControlGame = false;
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
		player.setNextPosition(OUTSIDE_LOBBY);
		player.getControlerManager().forceStop();
	}
	

	public static NPC getPortals(int portalIndex) {
		return portal[portalIndex];
	}

}