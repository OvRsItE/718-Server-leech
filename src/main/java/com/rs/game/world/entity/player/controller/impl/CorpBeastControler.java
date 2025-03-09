package com.rs.game.world.entity.player.controller.impl;

import com.rs.Constants;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;

public class CorpBeastControler extends Controller {

	@Override
	public void start() {

	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 37929 || object.getId() == 38811) {
			removeControler();
			player.stopAll();
			player.setNextPosition(new Position(2970, 4384, player.getZ()));
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
	}

//	@Override
//	public boolean sendDeath() {
//		WorldTasksManager.schedule(new WorldTask() {
//			int loop;
//
//			@Override
//			public void run() {
//				if (loop == 0) {
//					player.animate(new Animation(836));
//				} else if (loop == 1) {
//					player.getPackets().sendGameMessage("Oh dear, you have died.");
//				} else if (loop == 3) {
//					Player killer = player.getMostDamageReceivedSourcePlayer();
//					if (killer != null) {
//						killer.removeDamage(player);
//						killer.increaseKillCount(player);
//					}
//					player.sendItemsOnDeath(player);
//					player.getEquipment().init();
//					player.getInventory().init();
//					player.sendDeath(player);
//					player.reset();
//					int i;
//					if (player.isPker)
//						i = 1;
//					else
//						i = 0;
//					player.setNextPosition(new Position(Constants.RESPAWN_PLAYER_LOCATION[i]));
//					player.animate(new Animation(-1));
//				} else if (loop == 4) {
//					removeControler();
//					player.getPackets().sendMusicEffect(90);
//					stop();
//				}
//				loop++;
//			}
//		}, 0, 1);
//		return false;
//	}

	@Override
	public boolean login() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

}
