/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah;

import com.rs.game.map.Position;

import lombok.Getter;

/**
 * @author ReverendDread
 * Jul 23, 2018
 */
public enum ZulrahPosition {

	SOUTH(new Position(2266, 3062, 0), new Position(2268, 3068, 0)),
	WEST(new Position(2257, 3071, 0), new Position(2268, 3073, 0)),
	CENTER(new Position(2266, 3072, 0), new Position(2268, 3065, 0)),
	EAST(new Position(2276, 3072, 0), new Position(2270, 3074, 0));

	@Getter private final Position spawn, face;
	
	private ZulrahPosition(final Position spawn, final Position face) {
		this.spawn = spawn;
		this.face = face;
	}
	
}
