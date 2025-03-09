package com.rs.game.world.entity.player.controller.instancing;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.acidwyrm.AcidicStrykewyrm;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.controller.Controller;

public class AcidicStrykewyrmInstance extends Controller {
	
	private int[] boundChunks;
	
	private ArrayList<AcidicStrykewyrm> wyrms = new ArrayList<AcidicStrykewyrm>();
	private boolean stop_fight;
	
	@Override
	public void start() {
		boundChunks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(272, 384, boundChunks[0], boundChunks[1], 8);
		FadingScreen.fade(player, new Runnable() {
			
			@Override
			public void run() {
				player.setNextPosition(getLocalTile(36, 25, 0));
				player.setForceMultiArea(true);
				startWyrmTimer();
			}
			
		});
	}

	@Override
	public boolean logout() {
		player.setLocation(Constants.HOME_PLAYER_LOCATION[0]);
		destroy(0);
		return true;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		destroy(0);
		return true;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		destroy(0);
		return true;
	}
	
	@Override
	public boolean sendDeath() {
		destroy(0);
		return true;
	}
	
	/**
	 * Spawns the first wyrm.
	 */
	private void startWyrmTimer() {
		player.sendMessage("You think you hear something coming from the water...");	
		WorldTasksManager.schedule(new WorldTask() {

			int spawn = 0;
			
			@Override
			public void run() {
				if (stop_fight) {
					stop();
					return;
				}
				if (spawn < 3) {
					wyrms.add(new AcidicStrykewyrm(16023 + spawn, getSpawnTile(spawn), -1, true));
					wyrms.get(spawn).setTarget(player);		
				} else
					stop();				
				spawn++;
			}
			
		}, 10, 100);
	}
	
	/**
	 * Gets the wyrms spawn tiles.
	 * @param spawn
	 * @return
	 */
	private Position getSpawnTile(int spawn) {
		switch (spawn) {		
			case 0: //Melee
				return getLocalTile(33, 21, 0);
			case 1: //Range
				return getLocalTile(36, 22, 0);
			case 2: //Mage
				return getLocalTile(31, 22, 0);
			default:
				return null;
		}
	}
	
	/**
	 * Destorys this instance safely.
	 * @param type
	 */
	private void destroy(int type) {
		stop_fight = true;
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], 1, 1);		
				for (AcidicStrykewyrm wyrm : wyrms) {
					if (Objects.isNull(wyrm))
						continue;
					wyrm.finish();
				}
			}
			
		}, 10, TimeUnit.SECONDS);
	}
	
	/**
	 * Gets a tile in this instance.
	 * @param x
	 * @param y
	 * @param plane
	 * @return
	 */
	private Position getLocalTile(int x, int y, int plane) {
		return new Position((boundChunks[0] * 8) + x, (boundChunks[1] * 8) + y, plane);
	}

}
