/**
 * 
 */
package com.rs.game.world.entity.npc.instances.skotizo;

import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class SkotizoInstance extends RegionInstance {

	private static final Position[] LOCATIONS = new Position[] {
		new Position(1695, 9872, 0),
		new Position(1713, 9887, 0),
		new Position(1713, 9887, 0),
		new Position(1679, 9888, 0),
	};

	public SkotizoInstance() {
		super(8, 208, 1232);
	}
	
	public static final void launch(final Player player) {
		final SkotizoInstance instance = new SkotizoInstance();
		instance.constructRegion();
		FadingScreen.fade(player, () -> {
			player.setNextPosition(instance.getLocation(getRandomEntrance()));
			player.getControlerManager().startControler("SkotizoController", instance);
		});
	}
	
	/**
	 * Gets a random entrance location.
	 * @return
	 */
	private static final Position getRandomEntrance() {
		return LOCATIONS[Utils.random(LOCATIONS.length)];
	}
	
}
