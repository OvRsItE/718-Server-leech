package com.rs.game.world.entity.player.controller.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class JailControler extends Controller {

	@Override
	public void start() {
		if (player.getJailed() > Utils.currentTimeMillis())
			player.sendRandomJail(player);
	}

	@Override
	public void process() {
		if (player.getJailed() <= Utils.currentTimeMillis()) {
			player.getControlerManager().getControler().removeControler();
			player.getPackets().sendGameMessage(
					"Your account has been unjailed.", true);
			player.setNextPosition(new Position(2677, 10379, 0));
		}
	}

	public static void stopControler(Player p) {
		p.getControlerManager().getControler().removeControler();
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.animate(new Animation(-1));
					player.reset();
					player.setCanPvp(false);
					player.sendRandomJail(player);
					player.unlock();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {

		return false;
	}

	@Override
	public boolean logout() {

		return false;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getPackets().sendGameMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getPackets().sendGameMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		player.getPackets().sendGameMessage(
				"You cannot do any activities while being jailed.");
		return false;
	}

}
