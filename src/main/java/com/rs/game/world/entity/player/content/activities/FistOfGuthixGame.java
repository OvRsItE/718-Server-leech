package com.rs.game.world.entity.player.content.activities;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.utility.Utils;

/**
 * Represents the actual Fist of Guthix game.
 * 
 * @author Jae <jae@xiduth.com>
 * @since Nov 30, 2013
 */
public class FistOfGuthixGame extends Controller {

	/**
	 * Necessary stuff.
	 */
	 int[] mapBaseCoords;
	 boolean needDestroy;
	 int[] oldMapBaseCoords;
	 int randomTile;
	 int regionX;
	 int regionY;
	 
	public FistOfGuthixGame(int[] mapBaseCoords, boolean needDestroy, int[] oldMapBaseCoords, 
			int randomTile, int regionX, int regionY) {
		mapBaseCoords = RegionBuilder.findEmptyMap(8, 8);
		needDestroy = mapBaseCoords != null;
		oldMapBaseCoords = mapBaseCoords;
		randomTile = 1 + Utils.random(5);
		regionX = 204;
		regionY = 706;
		this.mapBaseCoords = RegionBuilder.findEmptyMap(8, 8);
		this.needDestroy = needDestroy;	
		this.oldMapBaseCoords = oldMapBaseCoords;
		this.randomTile = 1 + Utils.random(5);
		this.regionX = 204;
		this.regionY = 706;
	}
	
	public int getBaseX() {
		return mapBaseCoords[0] << 3;
	}

	public int getBaseY() {
		return mapBaseCoords[1] << 3;
	}

	/**
	 * Represents the starting sequence after the controller is activated.
	 */
	@Override
	public void start() {
		buildGame();
	}
	
	@Override
	public boolean logout() {
		destroy(player, needDestroy, 0);
		return true;
	}
	
	@Override
	public boolean login(){
		destroy(player, needDestroy, 0);
		return true;
		
	}
	
	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		player.getControlerManager().forceStop();
	}

	/**
	 * Builds the necessary components of the game.
	 */
	public void buildGame() {
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					generateDynamicRegion();
					addPlayerToGame(player);
				} catch (Error e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Generates the Dynamic region.
	 */
	public void generateDynamicRegion() {
		RegionBuilder.copyAllPlanesMap(regionX, regionY, mapBaseCoords[0],
				mapBaseCoords[1], 8);
		if (needDestroy) {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					CoresManager.slowExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								RegionBuilder.destroyMap(oldMapBaseCoords[0],
										oldMapBaseCoords[1], 8, 8);
							} catch (Exception e) {
								e.printStackTrace();
							} catch (Error e) {
								e.printStackTrace();
							}
						}
					});
				}
			});
		}
	}

	/**
	 * Destroys the region and removes player from game.
	 * @param player
	 * @param logout
	 * @param mode
	 */
	public void destroy(Player player, final boolean logout, int mode) {
		if (logout)
			player.setLocation(FistOfGuthixConfiguration.HALL_LOCATION);
		else {
			player.getControlerManager().removeControlerWithoutCheck();
			player.setNextPosition(FistOfGuthixConfiguration.HALL_LOCATION);
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				CoresManager.slowExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							RegionBuilder.destroyMap(mapBaseCoords[0],
									mapBaseCoords[1], 8, 8);
							if (!logout) {
								mapBaseCoords = null;
							}
						} catch (Exception e) {
							e.printStackTrace();
						} catch (Error e) {
							e.printStackTrace();
						}
					}
				});
			}
		}, 1);
	}

	/**
	 * Adds player to the game after the region generation.
	 * @param p
	 */
	public void addPlayerToGame(Player p) {
		p.setNextPosition(new Position(getBaseX() + randomTile, getBaseY() + randomTile, 0));
	}
}
