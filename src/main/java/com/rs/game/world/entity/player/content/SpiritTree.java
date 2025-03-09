package com.rs.game.world.entity.player.content;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;



public class SpiritTree {


	public static final Position
		TREE_GNOME_VILLAGE = new Position(2542, 3168, 0),
		TREE_GNOME_STRONGHOLD = new Position(2460, 3446, 0),
		KHAZARD_BATTLEFIELD = new Position(2553, 3256, 0),
		GRAND_EXCHANGE = new Position(3186, 3508, 0),
		MOBILISING_ARMIES = new Position(2416, 2848, 0),
		EVIL_TREE = new Position(2455, 2832, 0),
		MAIN_SPIRIT_TREE = new Position(2542, 3168, 0);

		
	public static void handleButtons(final Player player, int slotId) {
		if (slotId == 0) {
			sendSpiritTreeTeleport(player, TREE_GNOME_VILLAGE);
		} else if (slotId == 1) {
			sendSpiritTreeTeleport(player, TREE_GNOME_STRONGHOLD);
		} else if (slotId == 2) {
			sendSpiritTreeTeleport(player, KHAZARD_BATTLEFIELD);
		} else if (slotId == 3) {
			sendSpiritTreeTeleport(player, GRAND_EXCHANGE);
		} else if (slotId == 4) {
			sendSpiritTreeTeleport(player, MOBILISING_ARMIES);
		}

	}

	
	public static void sendSpiritTreeTeleport(final Player player,
			final Position tile) {
		if (!player.getControlerManager().processObjectTeleport(tile))
			return;
		player.closeInterfaces();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(7082));
					player.setNextGraphics(new Graphics(1228));
				} else if (loop == 2) {
					player.setNextPosition(tile);
				} else if (loop == 3) {
					player.animate(new Animation(7084));
					player.setNextGraphics(new Graphics(1229));
				}
				loop++;
			}
		}, 0, 1);
	}
	
	
	public static void sendSpiritTreeInterface(final Player player) {
		player.getInterfaceManager().sendInterface(864);
		for (int id = 0; id < 100; id++) {
			player.getPackets().sendUnlockIComponentOptionSlots(864, id, 0, 100, 0);
		}
	}

}