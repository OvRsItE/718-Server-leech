/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.controller.Controller;

import lombok.Getter;

/**
 * @author ReverendDread
 * Jul 24, 2018
 */
public class ZulrahController extends Controller {

	private ZulrahInstance instance;
	@Getter private ZulrahNPC zulrah;
	
	@Override
	public void start() {
		this.instance = (ZulrahInstance) getArguments()[0];
		player.unlock();
		player.getDialogueManager().startDialogue("SimpleMessage", "The priestess rows you to Zulrah's shrine,<br>"
				+ "then hurriedly paddles away.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setForceMultiArea(true);
				player.setLargeSceneView(true);
				player.setAtMultiArea(true);
				zulrah = new ZulrahNPC(instance.getLocation(2266, 3072, 0), player, instance);
				stop();
			}
		}, 2);
	}
	
	@Override
	public boolean login() {
		player.setNextPosition(new Position(2213, 3056));
		return true;
	}
	
	@Override
	public boolean logout() {
		player.setNextPosition(new Position(2213, 3056));
		finish();
		return true;
	}
	
	@Override
	public boolean processItemTeleport(Position to) {
		finish();
		return true;
	}
	
	@Override
	public boolean processMagicTeleport(Position to) {
		finish();
		return true;
	}

	@Override
	public boolean sendDeath() {
		finish();
		return true;
	}
	
	private void finish() {
		player.setForceMultiArea(false);
		player.setLargeSceneView(false);
		player.setAtMultiArea(false);
		instance.destroyRegion();
		removeControler();
	}
	
}
