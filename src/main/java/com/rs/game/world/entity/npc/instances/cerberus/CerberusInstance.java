/**
 * 
 */
package com.rs.game.world.entity.npc.instances.cerberus;

import com.rs.game.map.RegionBuilder;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

/**
 * @author ReverendDread
 * Sep 21, 2018
 */
public class CerberusInstance extends RegionInstance {

	private static final long serialVersionUID = -6245163101071088035L;

	public CerberusInstance() {
		super(8, 152, 152);
	}
	
	public static final void launch(final Player player) {
		final CerberusInstance instance = new CerberusInstance();
		instance.constructRegion();
		FadingScreen.fade(player, () -> {
			player.setNextPosition(instance.getLocation(1240, 1226, 0));
			player.getControlerManager().startControler("CerberusController", instance);
		});
	}

}
