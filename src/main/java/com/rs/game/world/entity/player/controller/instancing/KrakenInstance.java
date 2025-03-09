/**
 * 
 */
package com.rs.game.world.entity.player.controller.instancing;

import java.util.concurrent.TimeUnit;

import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.kraken.KrakenNPC;
import com.rs.game.world.entity.npc.kraken.TentacleNPC;
import com.rs.game.world.entity.npc.randomspawns.SuperiorBosses;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jul 22, 2018
 */
public class KrakenInstance extends Controller {

	private KrakenNPC kraken;
	
	private int[] chunks;

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.controller.Controller#start()
	 */
	@Override
	public void start() {
		chunks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(280, 1248, chunks[0], chunks[1], 8);
		FadingScreen.fade(player, new Runnable() {
			
			@Override
			public void run() {
				kraken = new KrakenNPC(16027, getLocalTile(40, 52, 0), -1, true, true);
				player.setNextPosition(getLocalTile(40, 38, 0));
				player.setForceMultiArea(true);
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
	public boolean processNPCClick1(NPC npc) {
		player.faceEntity(npc);
		player.resetWalkSteps();
		if (npc instanceof KrakenNPC) {
			KrakenNPC kraken = (KrakenNPC) npc;
			player.getActionManager().setAction(new PlayerCombat(kraken));
		}
		if (npc instanceof TentacleNPC) {
			TentacleNPC tentacle = (TentacleNPC) npc;
			player.getActionManager().setAction(new PlayerCombat(tentacle));
		}
		return false;
	}
	
	@Override
	public boolean processNPCClick2(NPC npc) {
		player.faceEntity(npc);
		player.resetWalkSteps();
		if (npc instanceof KrakenNPC) {
			KrakenNPC kraken = (KrakenNPC) npc;
			player.getActionManager().setAction(new PlayerCombat(kraken));
		}
		if (npc instanceof TentacleNPC) {
			TentacleNPC tentacle = (TentacleNPC) npc;
			player.getActionManager().setAction(new PlayerCombat(tentacle));
		}
		return false;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		if (id == 200538) {//Kraken out
			player.setNextPosition(new Position(2280, 10016, 0));
			destroy(0);
			player.getControlerManager().removeControlerWithoutCheck();
		}
		return false;
	}
	
	@Override
	public boolean sendDeath() {
		destroy(0);
		return true;
	}

	@Override
	public void processNPCDeath(NPC npc) {
		if (npc.getId() == 16027  &&  Utils.random(180) == 1 &&  player.UnlockedSuperiorBosses == 1) {
			SuperiorBosses.SpawnKraken(player, npc);
			World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
					"<col=ff0000> has spawned a superior kraken boss they have 5 minutes to kill it. <col=ff0000>", false);
			kraken.setSpawned(true);
		}
		if (npc.getId() == 16144) {
			kraken.setSpawned(false);
			kraken.setRespawnTask();
		}
		super.processNPCDeath(npc);
	}

	/**
	 * Destorys this instance safely.
	 * @param type
	 */
	private void destroy(int type) {
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				RegionBuilder.destroyMap(chunks[0], chunks[1], 8, 8);		
				kraken.finish();
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
		return new Position((chunks[0] * 8) + x, (chunks[1] * 8) + y, plane);
	}
	
	
}
