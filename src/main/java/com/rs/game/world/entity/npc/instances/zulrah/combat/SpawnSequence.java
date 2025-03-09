/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah.combat;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.instances.zulrah.Sequence;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahInstance;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahNPC;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahPosition;
import com.rs.game.world.entity.player.Player;

/**
 * @author ReverendDread
 * Jul 23, 2018
 */
public class SpawnSequence implements Sequence {

	private static final Position[][] TILES = new Position[][] {
		new Position[] { new Position(2269, 3069, 0), new Position(2272, 3070, 0) },
		new Position[] { new Position(2266, 3069, 0), new Position(2263, 3070, 0) },
		new Position[] { new Position(2273, 3072, 0), new Position(2273, 3075, 0) },
		new Position[] { new Position(2263, 3073, 0), new Position(2263, 3076, 0) }
	};
	
	@Override
	public int attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		zulrah.lock();
		zulrah.setCantInteract(true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				target.getPackets().sendResetCamera();
				zulrah.setCantInteract(false);
			}
		}, 4);
		WorldTasksManager.schedule(new WorldTask() {
			private int count;
			@Override
			public void run() {
				if (count == 5) {
					zulrah.setSequence(1);
					stop();
					return;
				} else if (count == 4) {
					new DiveSequence(ZulrahPosition.CENTER, 16642).attack(zulrah, instance, target);
					count++;
					return;
				}
				final Position[] tiles = TILES[count];
				new CloudsSequence(tiles[0], tiles[1], true).attack(zulrah, instance, target);
				count++;
			}
			
		}, 4, 3);
		return 1;
	}
	
}
