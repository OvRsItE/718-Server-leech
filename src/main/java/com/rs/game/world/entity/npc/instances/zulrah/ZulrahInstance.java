/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.Position;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author Kris
 * @author ReverendDread
 * Jul 24, 2018
 */
public class ZulrahInstance extends RegionInstance {
	
	public ZulrahInstance() {
		super(8, 281, 381);
	}
	
	public static final void launch(final Player player) {
		player.getDialogueManager().startDialogue(new Dialogue() {

			@Override
			public void start() {
				sendOptionsDialogue("Return to Zulrah's Shine?", "Yes.", "No.");
			}

			@Override
			public void run(int interfaceId, int componentId) {
				switch (componentId) {
				
					case OPTION_1:
						final ZulrahInstance instance = new ZulrahInstance();
						instance.constructRegion();
						WorldTasksManager.schedule(new WorldTask() {

							private int ticks;
							
							@Override
							public void run() {
								if (ticks == 2) {
									player.setNextPosition(instance.getLocation(2268, 3068, 0));
								} else if (ticks == 3) {
									final Position lastLoaded = player.getLastLoadedMapRegionTile();
									final Position camPos = instance.getLocation(2256, 3064, 0);
									player.getPackets().sendCameraPos(camPos.getLocalX(lastLoaded), camPos.getLocalY(lastLoaded), 3000, -128, 0);
									final Position camLook = instance.getLocation(2275, 3080, 0);
									player.getPackets().sendCameraLook(camLook.getLocalX(lastLoaded), camLook.getLocalY(lastLoaded), 3000, -128, 0);
									stop();
								}
								ticks++;
							}
							
						}, 1, 0);
						FadingScreen.fade(player, () -> {
							player.getControlerManager().startControler("ZulrahController", instance);
						});
						break;
				
				}
				end();
			}

			@Override
			public void finish() {}
			
		});
	}

}
