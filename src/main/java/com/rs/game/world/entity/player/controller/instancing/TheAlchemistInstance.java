package com.rs.game.world.entity.player.controller.instancing;

import java.util.concurrent.TimeUnit;

import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.alchemist.TheAlchemist;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.controller.Controller;

public class TheAlchemistInstance extends Controller {

	private int[] bound_chunks;
	
	private TheAlchemist the_alchemist; //33, 44, 0
	
	@Override
	public void start() {
		bound_chunks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(472, 584, bound_chunks[0], bound_chunks[1], 8);
		FadingScreen.fade(player, new Runnable() {
			
			@Override
			public void run() {
				player.setNextPosition(getLocalTile(33, 5, 0));
				player.setForceMultiArea(true);
				player.setAtMultiArea(true);
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
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 66313) {
			FadingScreen.fade(player, new Runnable() { @Override public void run() { /* empty */ }});
			Magic.sendNormalTeleportSpell(player, 1, 0, Constants.HOME_PLAYER_LOCATION[0]);
			player.getControlerManager().removeControlerWithoutCheck();
			destroy(0);
		}
		if (object.getId() == 66205 && player.getY() < getLocalTile(33, 22, 0).getY()) {
			player.useStairs(823, getLocalTile(33, 25, 0), 0, 0);
			if (the_alchemist == null) {
				spawnAlchemist();
			}
		}
		if (object.getId() == 66205 && player.getY() > getLocalTile(33, 22, 0).getY()) {
			if (the_alchemist.isFinished()) {
				player.getDialogueManager().startDialogue(new Dialogue() {
	
					@Override
					public void start() {
						sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Start another fight", "Leave");
					}
	
					@Override
					public void run(int interfaceId, int componentId) {
						if (componentId == OPTION_1) {
							spawnAlchemist();
						} else if (componentId == OPTION_2) {
							player.getControlerManager().removeControlerWithoutCheck();
							destroy(0);
							Magic.sendNormalTeleportSpell(player, 1, 0, Constants.HOME_PLAYER_LOCATION[0]);
						}
						end();
					}
	
					@Override
					public void finish() {}
					
				});
			}
		}
		return false;
	}
	
	private void spawnAlchemist() {
		the_alchemist = new TheAlchemist(16080, getLocalTile(33, 44, 0), -1, true, player.alchemist_enrage);
		the_alchemist.setForceAgressive(true);
		the_alchemist.setForceMultiArea(true);
		the_alchemist.setAtMultiArea(true);
		the_alchemist.setSpawned(true);
		the_alchemist.setTarget(player);
	}
	
	@Override
	public boolean sendDeath() {
		destroy(0);
		return true;
	}
	
	/**
	 * Destorys this instance safely.
	 * @param type
	 */
	private void destroy(int type) {
		player.setForceMultiArea(false);
		player.getControlerManager().removeControlerWithoutCheck();
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				RegionBuilder.destroyMap(bound_chunks[0], bound_chunks[1], 1, 1);
				if (the_alchemist != null && !the_alchemist.isFinished())
				the_alchemist.finish();
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
		return new Position((bound_chunks[0] * 8) + x, (bound_chunks[1] * 8) + y, plane);
	}

}
