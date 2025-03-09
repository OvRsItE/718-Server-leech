package com.rs.game.world.entity.player.content.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Logger;
import com.rs.utility.Utils;

public class HungerGames {

	private static final List<Player> players = new ArrayList<Player>();
	private static final List<WorldObject> objects = new ArrayList<WorldObject>();
	private static final Item randomItem[] = {new Item(3024, Utils.random(3)), new Item(23219, Utils.random(3)), new Item(6685, Utils.random(2)), new Item(6570), new Item(23639), new Item(6568), new Item(6570), new Item(23639), new Item(6568), new Item(13734), new Item(20072), new Item(8850), new Item(13555), new Item(1201), new Item(8844), new Item(8845), new Item(8846), new Item(8847), new Item(8848), new Item(8849), new Item(17273), new Item(18349), new Item(4151), new Item(19784), new Item(4587), new Item(4726), new Item(14484), new Item(16951), new Item(16955), new Item(1277), new Item(1283), new Item(1305), new Item(1317), new Item(6818), new Item(7158), new Item(11696), new Item(11698), new Item(11700), new Item(11730), new Item(13899), new Item(16403), new Item(16909), new Item(22346), new Item(22348), new Item(18353), new Item(22494), new Item(24474), new Item(22321), new Item(24433), new Item(11694), new Item(385, Utils.random(20)), new Item(15272, Utils.random(20)), new Item(9244, 100), new Item(7462), new Item(13006), new Item(2491), new Item(22358), new Item(16293), new Item(25025), new Item(11732), new Item(3105), new Item(2577), new Item(16359), new Item(16359), new Item(11728), new Item(13896), new Item(4753), new Item(10828), new Item(11665), new Item(20824), new Item(25022), new Item(1163),new Item(13887), new Item(4757), new Item(1127), new Item(8839), new Item(13893), new Item(4759), new Item(1079), new Item(8840), new Item(16689), new Item(11726), new Item(1067), new Item(1069), new Item(1071), new Item(1073), new Item(1075), new Item(3483), new Item(1125), new Item(10348), new Item(13884), new Item(14479), new Item(14492)};
	public static final Object lock = new Object();
	public static final Position OUTSIDE = new Position(3978, 4180, 1);
	static int hungergames2 = 0;
	static String okay = "";
	public static boolean inGame;
	public static final List<Player> getPlayers() {
		return players;
	}
	
	

	public static void leaveArena(Player player) {
		synchronized (lock) {
			player.getInterfaceManager().closeOverlay(false);
			player.getInventory().reset();
			player.getEquipment().reset();
			player.getInventory().refresh();
			player.getEquipment().refresh();
			players.remove(player);
			player.reset();
			player.getControlerManager().removeControlerWithoutCheck();
			player.setCanPvp(false);
			player.setCantTrade(false);
			player.setAtMultiArea(false);
			Entity p2 = player;
			p2.setAtMultiArea(false);
			if(players.size() <= 1) {
				HungerGames.setWinner();
			}
			inGame = false;
			if(Lobby.isStartedGame() && players.size() <= 1)
				Lobby.startGame(true);
		}
		player.setNextPosition(HungerGames.OUTSIDE);
	}
	
	private static Position mapStart(int i) {
		inGame = true;
		return new Position(3801 + i, 3530, 0);
	}
	
	private static void placeObjects(Position mapStart) {
		if (mapStart != null) {
			WorldObject obj = new WorldObject(4411, 22, -1, mapStart.getX(),
					mapStart.getY(), mapStart.getZ());
			World.spawnObject(obj, true);
			objects.add(obj);
		} else {
			for(int i = 0; i < 20; i++) {
				Position changingTile = null;
				if(i == 0)
				      changingTile = new Position(3784, 3550, 0);
				else if(i == 1)
				      changingTile = new Position(3790, 3553, 0);
				else if(i == 2)
					  changingTile = new Position(3798, 3554, 0);
				else if(i == 3)
					  changingTile = new Position(3803, 3550, 0);
				else if(i == 4)
					  changingTile = new Position(3810, 3551, 0);
				else if(i == 5)
					  changingTile = new Position(3810, 3557, 0);
				else if(i == 6)
					  changingTile = new Position(3813, 3562, 0);
				else if(i == 7)
					  changingTile = new Position(3819, 3565, 0);
				else if(i == 8)
					  changingTile = new Position(3813, 3571, 0);
				else if(i == 9)
					  changingTile = new Position(3813, 3532, 0);
				else if(i == 10)
					  changingTile = new Position(3817, 3529, 0);
				else if(i == 11)
					  changingTile = new Position(3809, 3544, 0);
				else if(i == 12)
					  changingTile = new Position(3816, 3547, 0);
				else if(i == 13)
					  changingTile = new Position(3818, 3558, 0);
				else if(i == 14)
					  changingTile = new Position(3806, 3560, 0);
				else if(i == 15)
					  changingTile = new Position(3797, 3558, 0);
				else if(i == 16)
					  changingTile = new Position(3795, 3549, 0);
				else if(i == 17)
					  changingTile = new Position(3787, 3557, 0);
				else if(i == 18)
					  changingTile = new Position(3824, 3550, 0);
				else if(i == 19)
					  changingTile = new Position(3817, 3527, 0);
				else if(i == 20)
					  changingTile = new Position(3808, 3526, 0);
				Position randomTile = new Position(changingTile, 2);
				if (!World.canMoveNPC(randomTile.getZ(), randomTile.getX(),
						randomTile.getY(), 1))
					return;
				World.spawnGroundItem(randomItem[Utils.random(randomItem.length -1)], randomTile);
			}
			
		}
	}
	
	public static void placePlayer(final Player player) {
		players.add(player);
		for (int i = 0; players.size() > i; i++)
			if (players.get(i) == player) {
				placeObjects(mapStart(i));
				player.setNextPosition(mapStart(i));
			}
		player.getControlerManager().getControler().removeControler();
		player.getControlerManager().startControler("HungerGames");
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					player.getCutscenesManager().play("HungerGames");
					placeObjects(null);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 10, TimeUnit.SECONDS);
	}
	

	public static void removeEntities() {
		if(objects != null)
		for (Iterator<WorldObject> it = objects.iterator(); it.hasNext();) {
				WorldObject obj = it.next();
				World.destroySpawnedObject(obj, true);
				it.remove();
		}
		objects.clear();
		if(World.getRegion(15159).forceGetFloorItems() != null)
		for (Iterator<FloorItem> it = World.getRegion(15159).forceGetFloorItems().iterator(); it.hasNext();) {
			FloorItem item = it.next();
			World.removeItems(item, 0);
	    }
		
	}

	public static void setWinner() {
		if(players.isEmpty())
			return;
		hungergames2++;
		String okay2 = "";
		if (hungergames2 == 1 || hungergames2 == 21 || hungergames2 == 31 || hungergames2 == 41 || hungergames2 == 51 || hungergames2 == 61 || hungergames2 == 71 || hungergames2 == 81)
		{
			 okay2 = "st";
		} else 	if (hungergames2 == 2 || hungergames2 == 22 || hungergames2 == 32 || hungergames2 == 42 || hungergames2 == 52 || hungergames2 == 62 || hungergames2 == 72 || hungergames2 == 82)
		{
			 okay2 = "nd";
		} else 	if (hungergames2 == 3 || hungergames2 == 23 || hungergames2 == 33 || hungergames2 == 43 || hungergames2 == 53 || hungergames2 == 63 || hungergames2 == 73 || hungergames2 == 83)
		{
			 okay2 = "rd";
		} else {
			okay2 = "th";
		}
		Player winner = players.get(0);
		World.sendWorldMessage("<col=ff8c38><img=6>News: "+ winner.getDisplayName() + " has just won the " + hungergames2 + okay2 +" hunger games!"+ "</col> ", false);		
		winner.sm("Congrats on winning the hunger games. Now leave before we throw you in a different game with the other winners");
		leaveArena(winner);
	}


}
