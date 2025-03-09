/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah.combat;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.instances.zulrah.Sequence;
import com.rs.game.world.entity.npc.instances.zulrah.SnakelingNPC;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahInstance;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jul 23, 2018
 */
public class SnakelingSequence implements Sequence {

	private static final Animation ANIM = Animation.createOSRS(5069);
	private static final Graphics SNAKLING_PROJ = Graphics.createOSRS(1047);

	public SnakelingSequence(final Position tile) {
		this.tile = tile;
	}

	private final Position tile;

	@Override
	public int attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		if (zulrah.isDead() || target.isFinished())
			return -1;
		final Position tile = instance.getLocation(this.tile);
		zulrah.setNextFacePosition(tile);
		zulrah.animate(ANIM);
		World.sendProjectile(zulrah, tile, SNAKLING_PROJ.getId(), 65, 10, 40, 15, 16, 0);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				if (zulrah.isDead() || zulrah.isFinished())
					return;
				zulrah.getSnakelings().add(new SnakelingNPC(zulrah, target, tile));
				Sequence sequence = zulrah.getNextSequence();
				sequence.attack(zulrah, instance, target);
			}
			
		}, (int) Math.ceil((Utils.getDistance(zulrah, target) * 0.10)));
		return 4;
	}

}
