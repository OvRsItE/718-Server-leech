package com.rs.game.world.entity.player.controller.instancing;


import java.util.concurrent.TimeUnit;


import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.Controller;


public class HomeController extends Controller {
	
	private int[] boundChunks;
	
	static int sizeX = 16; // horizontal size
	static int sizeY = 16; // vertical size
	
	static int chunkX = 323; // bottom left chunk x
	static int chunkY = 385; // bottom left chunk y
	
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
						player.setNextPosition(getWorldTile(20, 8));
						player.getCombatDefinitions().resetSpells(true);
						player.stopAll();
						player.unlock();
					}
				});
			}
		}, 3000, TimeUnit.MILLISECONDS);
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