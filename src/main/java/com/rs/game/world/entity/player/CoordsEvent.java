package com.rs.game.world.entity.player;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;

public final class CoordsEvent {

	private Position tile;
	private Runnable event;
	private int sizeX;
	private int sizeY;

	public CoordsEvent(Position tile, Runnable event, int sizeX, int sizeY,
                       int rotation) {
		this.tile = tile;
		this.event = event;
		if (rotation == 1 || rotation == 3) {
			this.sizeX = sizeY;
			this.sizeY = sizeX;
		} else {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}
	}

	public CoordsEvent(Position tile, Runnable event, int sizeX, int sizeY) {
		this(tile, event, sizeX, sizeY, -1);
	}

	public CoordsEvent(Position tile, Runnable event, int size) {
		this(tile, event, size, size);
	}

	/*
	 * returns if done
	 */
	public boolean processEvent(Player player) {
		if (player.getZ() != tile.getZ()) {
			return true;
		}
		int distanceX = player.getX() - tile.getX();
		int distanceY = player.getY() - tile.getY();
		if (distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1) {
			if (player.getX() == 2872 || player.getX() == 2831) {
			} else {
				return cantReach(player);
			}
		}
		if (player.hasWalkSteps()) {
			player.resetWalkSteps();
		}
		event.run();
		return true;
	}

	public boolean cantReach(Player player) {
		if (player.getX() == 2872 && player.getY() == 5280) {
			return false;
		}
		if (player.getX() == 2872 && player.getY() == 5272) {
			return false;
		}
		if (!player.hasWalkSteps() && player.getNextWalkDirection() == -1) {
			player.getPackets().sendGameMessage("You can't reach that.");
			return true;
		}
		return false;
	}

}