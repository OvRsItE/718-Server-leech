/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * @author ReverendDread
 * Sep 10, 2018
 */
@SuppressWarnings("serial")
public class VorkathInstance extends RegionInstance {

	public VorkathInstance() {
		super(8, 280, 504);
	}
	
	public static final void launch(final Player player) {
		final VorkathInstance instance = new VorkathInstance();
		instance.constructRegion();
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if (ticks == 0) {
					player.animate(new Animation(839));
				} else if (ticks == 1) {
					player.setNextPosition(instance.getLocation(2272, 4054, 0));
					player.getControlerManager().startControler("VorkathController", instance);
					stop();
				}
				ticks++;
			}
			
		}, 1, 0);
	}

}
