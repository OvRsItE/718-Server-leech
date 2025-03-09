package com.rs.game.world.entity.updating.impl;

import com.rs.game.map.Direction;
import com.rs.game.map.Position;

public final class NewForceMovement extends ForceMovement {

	public NewForceMovement(Position toFirstTile, int firstTileTicketDelay,
                            Position toSecondTile, int secondTileTicketDelay, int direction) {
		super(toFirstTile, firstTileTicketDelay, toSecondTile, secondTileTicketDelay,
				direction);
	}

	@Override
	public int getDirection() {
		return direction;
	}

}
