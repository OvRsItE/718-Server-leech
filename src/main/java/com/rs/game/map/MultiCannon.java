/**
 * 
 */
package com.rs.game.map;

import com.rs.game.world.entity.player.Player;

import lombok.Getter;

/**
 * @author ReverendDread
 * Aug 17, 2018
 */
public class MultiCannon extends WorldObject {

	@Getter private transient Player player;
	
	private static final long serialVersionUID = -7694629672813820173L;

	public MultiCannon(int id, int type, int rotation, final Player player) {
		super(id, type, rotation, player.getX(), player.getY(), player.getZ());
	}

}
