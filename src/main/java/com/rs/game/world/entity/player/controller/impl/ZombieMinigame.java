package com.rs.game.world.entity.player.controller.impl;

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
import com.rs.game.world.entity.npc.fightcaves.FightCavesNPC;
import com.rs.game.world.entity.npc.fightcaves.TzKekCaves;
import com.rs.game.world.entity.npc.zombies.SkeletalHorror;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Logger;
import com.rs.utility.Utils;

public class ZombieMinigame extends Controller {

	public static final Position OUTSIDE = new Position(3244, 3195, 0);

	private static final int ZOMBIE_MONK = 5625;

	private static final int[] MUSICS = {1088, 1082, 1086};

	public void playMusic() {
		player.getMusicsManager().playMusic(selectedMusic);
	}

	private final int[][] WAVES = {
			{73,73,73,73,73,73}
			,{73,73,73,73,76,76,76,76,76}
			,{4392,4392,4392,4392,4392}
			,{4392,4392,4392,4393,4393,4393,4393,4393,4393}
			,{4394,4394,4394,4394,4394,4394,4394,4394}
			,{5404,5404,5404,5404,5404,5404}
			,{9176,5404,5404,5404,5404,5404}
			,{9176,5404,5404,4394,5494,5494,5404,5404,5405,5404,5405}
			,{9176,9176}
	};


	private int[] boundChuncks;
	private Stages stage;
	private boolean logoutAtEnd;
	private boolean login;
	public boolean spawned;
	public int selectedMusic;

	public static void enterZombieGame(Player player) {
		if(player.getFamiliar() != null || player.getPet() != null || Summoning.hasPouch(player) || Pets.hasPet(player)) {
			player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE_MONK, "No Familiars in the Zombie Minigame!");
			return;
		}
		player.getControlerManager().startControler("ZombieMinigameControler", 1); //start at wave 1
	}

	private static enum Stages {
		LOADING,
		RUNNING,
		DESTROYING
	}

	@Override
	public void start() {
		loadCave(false);
	}
	
	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage","A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if(stage != Stages.RUNNING)
			return false;
		if(interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			if(!logoutAtEnd) {
				logoutAtEnd = true;
				player.getPackets().sendGameMessage("<col=ff0000>You will be logged out automatically at the end of this wave.");
				player.getPackets().sendGameMessage("<col=ff0000>If you log out sooner, you will have to repeat this wave.");
			}else
				player.forceLogout();
			return false;
		}
		return true;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if(object.getId() == 82) {
			if(stage != Stages.RUNNING)
				return false;
			exitCave(1);
			return false;
		}
		return true;
	}


	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean login() {
		loadCave(true);
		return false;
	}


	public void loadCave(final boolean login) {
		this.login = login;
		stage = Stages.LOADING;
		player.lock(); //locks player
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//finds empty map bounds
				boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8); 
				//copys real map into the empty map
				//552 640
				RegionBuilder.copyAllPlanesMap(322, 393, boundChuncks[0], boundChuncks[1], 8);
				//selects a music
				selectedMusic = MUSICS[Utils.random(MUSICS.length)];
				player.setNextPosition(!login ? getWorldTile(11, 10) : getWorldTile(11, 10) );
				//1delay because player cant walk while teleing :p, + possible issues avoid
				WorldTasksManager.schedule(new WorldTask()  {
					@Override
					public void run() {
						if(!login) {
							Position walkTo = getWorldTile(11, 10);
							player.addWalkSteps(walkTo.getX(), walkTo.getY());
						}
						player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE_MONK, "Goodluck on this mission mate! <br>Take out as many zombies as you can! To escape use the door east");
						player.getPackets().sendGameMessage("To escape the minigame you must use the door at the bottom east corner.");
						player.setForceMultiArea(true);
						playMusic();
						player.unlock(); //unlocks player
						stage = Stages.RUNNING;
					}

				}, 1);
				if(!login) {
					/*
					 * lets stress less the worldthread, also fastexecutor used for mini stuff
					 */
					CoresManager.fastExecutor.schedule(new TimerTask() {

						@Override
						public void run() {
							if(stage != Stages.RUNNING)
								return;
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

	public Position getSpawnTile() {
		switch(Utils.random(5)) {
		case 0:
			return getWorldTile(11, 9);
		case 1:
			return getWorldTile(9, 10);
		case 2:
			return getWorldTile(9, 10);
		case 3:
			return getWorldTile(11, 10);
		case 4:
		default:
			return getWorldTile(11, 10);
		}
	}

	@Override
	public void moved() {
		if(stage != Stages.RUNNING || !login)
			return;
		login = false;
		setWaveEvent();
	}

	public void startWave() {
		int currentWave = getCurrentWave();
		if(currentWave > WAVES.length) {
			win();
			return;
		}
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0, 316);
		player.getPackets().sendConfig(639, currentWave);
		if(stage != Stages.RUNNING)
			return;
		for(int id : WAVES[currentWave-1]) {
			if(id == 2736)
				new TzKekCaves(id, getSpawnTile());
			else if (id == 9176)
				new SkeletalHorror(id, getSpawnTile(), this);
			else
				new FightCavesNPC(id, getSpawnTile());
		}
		spawned = true;
	}

	public void spawnHealers() {
		if(stage != Stages.RUNNING)
			return;
		for(int i = 0; i < 4; i++)
			new FightCavesNPC(2746, getSpawnTile());
	}

	public void win() {
		if(stage != Stages.RUNNING)
			return;
		exitCave(4);
	}


	public void nextWave() {
		playMusic();
		setCurrentWave(getCurrentWave()+1);
		if(logoutAtEnd) {
			player.forceLogout();
			return;
		}
		setWaveEvent();
	}

	public void setWaveEvent() {
		if(getCurrentWave() == 9) 
			player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE_MONK, "Look out, here comes double Skeletal Horror!");
		CoresManager.fastExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if(stage != Stages.RUNNING)
						return;
					startWave();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 600);
	}

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
					player.getPackets().sendGameMessage("You have been defeated!");
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


	@Override
	public void magicTeleported(int type) {
		exitCave(2);
	}

	/*
	 * logout or not. if didnt logout means lost, 0 logout, 1, normal,  2 tele
	 */
	public void exitCave(int type) {
		stage = Stages.DESTROYING;
		Position outside = new Position(OUTSIDE, 2); //radomizes alil
		if(type == 0 || type == 2)
			player.setLocation(outside);
		else {
			player.setForceMultiArea(false);
			player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ?  11 : 0);
			if(type == 1 || type == 4) {
				player.setNextPosition(outside);
				if(type == 4) {
					player.reset();
					player.zombie += 1;
					player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE_MONK, "You have defeated every wave! You have recieved 5 Crystal keys and a zombie point! You now have "+player.zombie+" zombie points.");
					player.getPackets().sendGameMessage("You were victorious!");
				}else if(getCurrentWave() == 1) 
					player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE_MONK, "Well I suppose you tried... better luck next time.");
				else{
				//	int tokkul = getCurrentWave() * 8032 / WAVES.length;
					//tokkul *= Settings.DROP_RATE; //10x more
					//if(!player.getInventory().addItem(6529, tokkul)) 
					//	World.addGroundItem(new Item(6529, tokkul), new WorldTile(player), player, true, 180,true);
					player.getDialogueManager().startDialogue("SimpleNPCMessage", ZOMBIE_MONK, "Well done in the cave, too bad you didnt complete it.");
					//TODO tokens
				}
			}
			removeControler();
		}
		/*
		 * 1200 delay because of leaving
		 */
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}

	/*
	 * gets worldtile inside the map
	 */
	public Position getWorldTile(int mapX, int mapY) {
		return new Position(boundChuncks[0]*8 + mapX, boundChuncks[1]*8 + mapY, 0);
	}


	/*
	 * return false so wont remove script
	 */
	@Override
	public boolean logout() {
		/*
		 * only can happen if dungeon is loading and system update happens
		 */
		if(stage != Stages.RUNNING)
			return false;
		exitCave(0);
		return false;

	}

	public int getCurrentWave() {
		if (getArguments() == null || getArguments().length == 0) 
			return 0;
		return (Integer) getArguments()[0];
	}

	public void setCurrentWave(int wave) {
		if(getArguments() == null || getArguments().length == 0)
			this.setArguments(new Object[1]);
		getArguments()[0] = wave;
	}

	@Override
	public void forceClose() {
		/*
		 * shouldnt happen
		 */
		if(stage != Stages.RUNNING)
			return;
		exitCave(2);
	}
}