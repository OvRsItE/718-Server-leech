package com.rs.game.world.entity.player.controller.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * Handles Random Event Controler
 * 
 * @author Demon Dylan
 *
 */

public class QuizEvent extends Controller {


	@Override
	public void start() {
	//todo
	}
	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					player.setNextPosition(new Position(2602, 4775, 0));
					player.animate(new Animation(-1));
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}


	@Override
	public boolean login() {
		player.getDialogueManager().startDialogue("QuizMaster", 2477);
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getPackets().sendGameMessage("You can't teleport in this area!");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getPackets().sendGameMessage("You can't teleport in this area!");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 15645)
			return true;
		return false;
	}

}