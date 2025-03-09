package com.rs.game.world.entity.updating.impl;

import com.rs.game.map.Direction;
import com.rs.game.map.Position;
import com.rs.utility.Utils;

public class ForceMovement {

	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
	
	private Position toFirstTile;
	private Position toSecondTile;
	private int firstTileTicketDelay;
	private int secondTileTicketDelay;
	protected int direction;

	/*
	 * USE: moves to firsttile firstTileTicketDelay: the delay in game tickets
	 * between your tile and first tile the direction
	 */
	public ForceMovement(Position toFirstTile, int firstTileTicketDelay,
                         int direction) {
		this(toFirstTile, firstTileTicketDelay, null, 0, direction);
	}


	/*
	 * USE: moves to firsttile and from first tile to second tile
	 * firstTileTicketDelay: the delay in game tickets between your tile and
	 * first tile secondTileTicketDelay: the delay in game tickets between first
	 * tile and second tile the direction
	 */
	public ForceMovement(Position toFirstTile, int firstTileTicketDelay,
                         Position toSecondTile, int secondTileTicketDelay, int direction) {
		this.toFirstTile = toFirstTile;
		this.firstTileTicketDelay = firstTileTicketDelay;
		this.toSecondTile = toSecondTile;
		this.secondTileTicketDelay = secondTileTicketDelay;
		this.direction = direction;
	}

	public ForceMovement(Position firstTile, int firstDelay, Position secondTile, int secondDelay, Direction direction) {
		this(firstTile, firstDelay, secondTile, secondDelay, direction.ordinal());
	}
	
	public int getDirection() {
		switch(direction) {
		case NORTH:
			return Utils.getFaceDirection(0, 1);
		case EAST:
			return Utils.getFaceDirection(1, 0);
		case SOUTH:
			return Utils.getFaceDirection(0, -1);
		case WEST:
		default:
			return Utils.getFaceDirection(-1, 0);
		}
	}
	
	public Position getToFirstTile() {
		return toFirstTile;
	}

	public Position getToSecondTile() {
		return toSecondTile;
	}

	public int getFirstTileTicketDelay() {
		return firstTileTicketDelay;
	}

	public int getSecondTileTicketDelay() {
		return secondTileTicketDelay;
	}

}
