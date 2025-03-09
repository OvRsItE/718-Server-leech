package com.rs.game.world.entity.player.content.activities;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.zombies.ZombieCaves;
import com.rs.game.world.entity.npc.zombies.ZombiesNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Logger;
import com.rs.utility.Utils;


/**
 * @author Adam
 * @since Jully 31st 2012
 */

public class Zombies extends Controller{

	/**
	 * Data
	 */

	private int[] regionChucks;
	private ZombieStages stage;
	private boolean logoutAtEnd;
	private boolean login;
	public boolean spawned;
	public static boolean canpray = false;
	private static final int ZOMBIE = 5626;





	/**
	 * Holds the Zombies
	 */



	private final int[][] ZOMBIES = {
			{73, 73,}
			,{75, 75, 75}
			,{76, 1466}
			, {2837, 73}
			,{2837, 73, 75,73, 75, 76}
			, {2837, 73, 75, 76,73, 75, 76}
			,{2837, 73, 75, 76,73, 75, 76}
			,{2837, 73, 75, 76, 73, 1466,73, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 75, 76}
			,{15504,15504,15504,15504,15504,15504,15504,15504,15504}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339, 14281, 73, 75,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339, 14281, 73, 75, 14281, 14339, 14281,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339, 14281, 73, 75, 14281, 14339, 14281, 14431,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339, 14281, 73, 75, 14281, 14339, 14281, 14431, 14431, 14431,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{ 3066,2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339, 14281, 73, 75, 14281, 14339, 14281, 14431, 14431, 14431, 73, 75, 14281, 14339, 14281, 14431, 14431, 14431,2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
			,{3066,2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339, 14281, 73, 75, 14281, 14339, 14281, 14431, 14431, 73, 75, 14281, 14339, 14281, 14431, 14431, 73, 75, 14281, 14339, 14281, 14431, 14431, 14431, 73, 75, 14281, 14339, 14281, 14431, 14431, 14431,2837, 73, 75, 76, 73, 1466, 1826, 75, 76, 1826, 8149, 14281, 14339,2837, 73, 75, 76, 73, 1466, 1826, 75, 76}
	};

	/*
	 * 14281//135
	 * 14339//85
	 */



	/**
	 * 
	 * @author Adam
	 *
	 */
	private static enum ZombieStages {
		LOADING,
		RUNNING,
		DESTROYING
	}



	/**
	 * Starts game
	 */
	@Override
	public void start() {
		startGame(false);

	}





	/**
	 * Starts the game & loads the map.
	 * @param login
	 */

	public void fade (final Player player) {
		final long time = FadingScreen.fade(player);
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					FadingScreen.unfade(player, time, new Runnable() {
						@Override
						public void run() {




						}
					});
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		});
	}



	public void startGame(final boolean login) {

		fade(player);
		this.login = login;
		stage = ZombieStages.LOADING;
		player.lock(); //locks player
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//regionChucks = RegionBuilder.findEmptyChunkBound(9, 9);
				// RegionBuilder.copyAllPlanesMap(456, 439, regionChucks[0],// mhmk ima eat icecream have fun
				//		regionChucks[1], 9);
				regionChucks = RegionBuilder.findEmptyChunkBound(8, 8);
				RegionBuilder.copyAllPlanesMap(456, 439, regionChucks[0], regionChucks[1], 8);//is this rightno urs is abovethes

				player.setNextPosition(!login ? getWorldTile(9, 10) : getWorldTile(9, 10) );
				WorldTasksManager.schedule(new WorldTask()  {
					@Override
					public void run() {
						if(!login) {
							Position walkTo = getWorldTile(32, 32);
							player.addWalkSteps(walkTo.getX(), walkTo.getY());
						}
						player.getPrayer().setPrayerpoints(0);
						player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "<br>Muahha! Prepare to die!");
						player.setForceMultiArea(true);
						player.unlock();
						stage = ZombieStages.RUNNING;
					}

				}, 1);
				if(!login) {
					CoresManager.fastExecutor.schedule(new TimerTask() {

						@Override
						public void run() {
							if(stage != ZombieStages.RUNNING) {
								return;
							}
							try {
								startWave();
							} catch (Throwable t) {
								Logger.handle(t);
							}
						}
					}, 6000);
				}
			}
		});
	}




	/**
	 * 
	 * @return
	 */



	public Position getSpawnTile() {
		switch(Utils.random(7)) {
		case 0:
			//player.out("0");
			return getWorldTile(13, 18);
		case 1:
			//player.out("1");
			return getWorldTile(11, 16);
		case 2:
			//player.out("2");
			return getWorldTile(11, 18);
		case 3:
			//	player.out("3");
			return getWorldTile(10, 13);
		case 4:
			return getWorldTile(6, 10);
		case 5:
			return getWorldTile(12, 20);
		default:
			//player.out("4");
			return getWorldTile(11, 16);
		}
	}




	/**
	 * 
	 * @param player
	 */




	public static void enterZombies(Player player) {
		if(player.getFamiliar() != null || player.getPet() != null  || Pets.hasPet(player)) {
			player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "No little friends in here, my family will destroy it quicker then you!");
			return;
		}
		canpray = true;
		player.getControlerManager().startControler("ZombiesController", 1);
	}





	/**
	 * Handles the buttons.
	 */




	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if(stage != ZombieStages.RUNNING) {
			return false;
		}
		if(interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			if(!logoutAtEnd) {
				logoutAtEnd = true;
				player.getPackets().sendGameMessage("<col=ff0000>You will be logged out automatically at the end of this wave.");
				player.getPackets().sendGameMessage("<col=ff0000>If you log out sooner, you will have to repeat this wave.");
			} else {
				player.forceLogout();
			}

			return false;
		}
		return true;
	}








	/**
	 * return process normaly
	 */




	@Override
	public boolean processObjectClick1(WorldObject object) {
		if(object.getId() == 9357) {
			if(stage != ZombieStages.RUNNING) {
				return false;
			}
			exitCave(1);
			return false;
		}
		return true;
	}

	@Override
	public void moved() {
		if(stage != ZombieStages.RUNNING || !login) {
			return;
		}
		login = false;
		setWaveEvent();
	}

	public void win() {
		if(stage != ZombieStages.RUNNING) {
			return;
		}
		exitCave(4);
	}


	public void startWave() {
		int currentWave = getCurrentWave();
		if(currentWave > ZOMBIES.length) {
			win();
			return;
		}
		player.out("Starting wave.. [" + currentWave +"];.");
		if(stage != ZombieStages.RUNNING) {
			return;
		}
		for(int id : ZOMBIES[currentWave-1]) {
			new ZombiesNPC(id, getSpawnTile());
		}
		spawned = true;
	}


	/**
	 * 
	 */
	public void setWaveEvent() {
		if (getCurrentWave() == 10) {
			player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "Now for the powerful monkeys!!");
		}
		player.setNextGraphics(new Graphics(1904));
		player.setNextGraphics(new Graphics(1904));
		player.setNextGraphics(new Graphics(1904));
		player.setNextGraphics(new Graphics(1904));
		player.setNextGraphics(new Graphics(1904));
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if(stage != ZombieStages.RUNNING) {
						return;
					}
					startWave();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 600);
	}




	/**
	 * Processing.
	 */


	@Override
	public void process() {
		if(spawned) {
			List<Integer> npcs = World.getRegion(player.getRegionId()).getNPCsIndexes();
			if(npcs == null || npcs.isEmpty())  {
				spawned = false;
				nextWave();
			}
		}
	}


	/**
	 * Sets the next wave.
	 */

	public void nextWave() {
		setCurrentWave(getCurrentWave()+1);
		if(logoutAtEnd) {
			player.forceLogout();
			return;
		}
		setWaveEvent();
	}


	/**
	 * Death method.
	 */
	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("They have eaten your brain!");
				} else if (loop == 3) {
					player.reset();
					exitCave(1);
					player.animate(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}



	/**
	 * 
	 */

	@Override
	public void magicTeleported(int type) {
		exitCave(1);
	}

	/*
	 * logout or not. if didnt logout means lost, 0 logout, 1, normal,  2 tele
	 */
	public void exitCave(int type) {
		stage = ZombieStages.DESTROYING;

		Position outside = new Position(3087, 3503, 0);
		if(type == 0 || type == 2) {
			if (Player.isFriday() && !player.finishedTask) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "Well I suppose you tried... better luck next time.");
				player.zombieGame++;
				if (player.zombieGame >= 1 && !player.sentZombieMSG) {
					player.sm("You finished a game of zombies for the daily task!");
					player.sentZombieMSG = true;
				} else if (player.zombieGame >= 1 && player.SaradominKilled >= 25 && player.cuttedGems >= 150 && !player.finishedTask) {
					player.sm("You have finished the daily task and claimed your reward!");
					player.setSkillPoints(player.getSkillPoints() + 100);
					player.getInventory().addItem(995, 5000000);
					player.getInventory().addItem(9191, 400);
					player.finishedTask = true;
					player.startedTask = false;
				}
				player.setLocation(outside);
			} else {
				player.setLocation(outside);
			}
		} else {
			player.setForceMultiArea(false);
			if(type == 1 || type == 4) {
				if (Player.isFriday() && !player.finishedTask) {
					player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "Well I suppose you tried... better luck next time.");
					player.zombieGame++;
					if (player.zombieGame >= 1 && !player.sentZombieMSG) {
						player.sm("You finished a game of zombies for the daily task!");
						player.sentZombieMSG = true;
					} else if (player.zombieGame >= 1 && player.SaradominKilled >= 25 && player.cuttedGems >= 150 && !player.finishedTask) {
						player.sm("You have finished the daily task and claimed your reward!");
						player.setSkillPoints(player.getSkillPoints() + 100);
						player.getInventory().addItem(995, 5000000);
						player.getInventory().addItem(9191, 400);
						player.finishedTask = true;
						player.startedTask = false;
					}
				}
				player.setNextPosition(outside);
				if(type == 4) {
					fade(player);
					canpray = false;
					player.reset();
					player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "You defeated my family, even my grandpa! i am impressed...");
					player.getPackets().sendGameMessage("You were victorious in the brutal fight against the zombie's!!!");
					player.out("You are awarded a Squeal of Fortune spin, and a zombie outfit.");
					if (player.getInventory().getFreeSlots() < 7) {
						player.getPackets().sendGameMessage("<col=00FFCC>Your inventory is full. Check the bank for them.");
						player.getBank().addItem(7594, 1, true);
						player.getBank().addItem(7592, 1, true);
						player.getBank().addItem(7593, 1, true);
						player.getBank().addItem(7595, 1, true);
						player.getBank().addItem(24154, 1, true);
						player.getBank().addItem(7596, 1, true);

						removeControler();
						return;
					}
					player.getInventory().addItem(7594, 1);
					player.getInventory().addItem(7592, 1);
					player.getInventory().addItem(7593, 1);
					player.getInventory().addItem(7595, 1);
					player.getInventory().addItem(7596, 1);
					player.getInventory().addItem(24154, 1);
				}else if(getCurrentWave() == 1) {
					if (Player.isFriday() && !player.finishedTask) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "Well I suppose you tried... better luck next time.");
						player.zombieGame++;
						if (player.zombieGame >= 1 && !player.sentZombieMSG) {
							player.sm("You finished a game of zombies for the daily task!");
							player.sentZombieMSG = true;
						} else if (player.zombieGame >= 1 && player.SaradominKilled >= 25 && player.cuttedGems >= 150 && !player.finishedTask) {
							player.sm("You have finished the daily task and claimed your reward!");
							player.setSkillPoints(player.getSkillPoints() + 100);
							player.getInventory().addItem(995, 5000000);
							player.getInventory().addItem(9191, 400);
							player.finishedTask = true;
							player.startedTask = false;
						}
					} else if (!Player.isFriday()) {
						player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "Well I suppose you tried... better luck next time.");
					}else{
						player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE, "You left the fight... discrace.");
					}
				}
			}
			removeControler();
		}

		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(regionChucks[0], regionChucks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);

	}





	/*
	 * gets worldtile inside the map
	 */
	public Position getWorldTile(int mapX, int mapY) {
		return new Position(regionChucks[0]*8 + mapX, regionChucks[1]*8 + mapY, 0);
	}




	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean logout() {
		/*
		 * only can happen if dungeon is loading and system update happens
		 */
		if(stage != ZombieStages.RUNNING) {
			return false;
		}
		exitCave(0);
		return false;

	}



	/**
	 * 
	 * @return
	 */

	public int getCurrentWave() {
		if (getArguments() == null || getArguments().length == 0) {
			return 0;
		}
		return (Integer) getArguments()[0];
	}





	/**
	 * 
	 * @param wave
	 */

	public void setCurrentWave(int wave) {
		if(getArguments() == null || getArguments().length == 0) {
			setArguments(new Object[1]);
		}
		getArguments()[0] = wave;
	}

	@Override
	public void forceClose() {
		/*
		 * shouldnt happen
		 */
		if(stage != ZombieStages.RUNNING) {
			return;
		}
		exitCave(2);
	}



	public void spawnZombieMembers() {
		if(stage != ZombieStages.RUNNING) {
			return;
		}
		for(int i = 0; i < 4; i++) {
			new ZombieCaves(2746, getSpawnTile());
		}
	}

}



