/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;

/**
 * @author ReverendDread
 * Jul 23, 2018
 */
public final class VenomCloud extends WorldObject {

	public VenomCloud(final Position position) {
		super(WorldObject.asOSRS(11700), 10, 0, position.getX(), position.getY(), position.getZ());
	}

}
