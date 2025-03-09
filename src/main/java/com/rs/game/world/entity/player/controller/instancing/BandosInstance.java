package com.rs.game.world.entity.player.controller.instancing;


import java.util.concurrent.TimeUnit;





import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.Controller;


public class BandosInstance extends Controller {
	
	private int[] boundChunks;
	
	static int sizeX = 100; // horizontal size
	static int sizeY = 100; // vertical size
	
	static int chunkX = 357; // bottom left chunk x
	static int chunkY = 668; // bottom left chunk y
	public NPC npc;
	@Override
	public void start() {
		player.lock();
		final long time = FadingScreen.fade(player);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				boundChunks = RegionBuilder.findEmptyChunkBound(sizeX, sizeY); 
				RegionBuilder.copyAllPlanesMap(chunkX, chunkY, boundChunks[0], boundChunks[1], sizeX, sizeY);
				
				FadingScreen.unfade(player, time, new Runnable() {
					@Override
					public void run() {
						int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
						player.setForceMultiArea(true);
						player.getPrayer().restorePrayer(maxPrayer);
						player.heal(player.getHitpoints() * 10);
						player.setNextPosition(getWorldTile(20, 25));
						player.getCombatDefinitions().resetSpells(true);
						player.stopAll();
						player.unlock();
					}
				});
			}
		}, 3000, TimeUnit.MILLISECONDS);
	}
	
	public void SpawnMonsters(NPC npc) {
		for (Player player : World.getPlayers()) {
		World.spawnNPC(6260,new Position(player.getX() + 5, player.getY() + 5, 0), 0, 0, false);
		}
	}
	
	@Override
	public boolean processMagicTeleport(Position toTile) {
		endDungeon(false);
		removeControler();
		return false;
	}


	@Override
	public boolean processItemTeleport(Position toTile) {
		endDungeon(false);
		removeControler();
		return false;
	}


	@Override
	public boolean processObjectTeleport(Position toTile) {
		endDungeon(false);
		removeControler();
		return false;
	}
	
	@Override
	public boolean logout() {
		endDungeon(true);
		return true;
	}
	
	private void removeMapChunks() {
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				RegionBuilder.destroyMap(boundChunks[0], boundChunks[1], sizeX, sizeY);
			}
		}, 1200, TimeUnit.MILLISECONDS);
	}
	
	public void endDungeon(boolean logout) {
		player.setForceMultiArea(false);
		if (!logout)
			player.setNextPosition(Constants.START_PLAYER_LOCATION);
		else
			player.setLocation(Constants.START_PLAYER_LOCATION);
		removeMapChunks();
	}
	
	public Position getWorldTile(int mapX, int mapY) {
		return new Position(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
	}
	
}