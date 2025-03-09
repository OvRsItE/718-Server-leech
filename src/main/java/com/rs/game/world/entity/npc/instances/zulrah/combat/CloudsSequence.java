/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah.combat;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.instances.zulrah.Sequence;
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
public class CloudsSequence implements Sequence {

	private static final Animation ANIM = Animation.createOSRS(5069);
	private static final Graphics FUMES_PROJ = Graphics.createOSRS(1045);
	
	public CloudsSequence(final Position primaryTile, final Position secondaryTile, boolean starting) {
		this.primaryTile = primaryTile;
		this.secondaryTile = secondaryTile;
		this.starting = starting;
	}
	
	private final Position primaryTile, secondaryTile;
	private final boolean starting;

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.zulrah.Sequence#attack(com.rs.game.world.entity.npc.zulrah.ZulrahNPC, com.rs.game.world.entity.npc.zulrah.ZulrahInstance, com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int attack(ZulrahNPC zulrah, ZulrahInstance instance, Player target) {
		if (!zulrah.isLocked()) {
			zulrah.lock(3);
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				if (zulrah.isDead() || target.isFinished()) {
					stop();
					return;
				}
				final Position primary = instance.getLocation(primaryTile);
				final Position secondary = instance.getLocation(secondaryTile);
				final Position faceTile = new Position((primary.getX() + secondary.getX()) / 2, (primary.getY() + secondary.getY()) / 2, zulrah.getZ());
				zulrah.setNextFacePosition(faceTile);
				zulrah.animate(ANIM);
				World.sendProjectile(zulrah, primary, FUMES_PROJ.getId(), 65, 10, 40, 15, 8, 0);
				World.sendProjectile(zulrah, secondary, FUMES_PROJ.getId(), 65, 10, 40, 15, 12, 0);
				zulrah.addCloud(primary, (int) Math.ceil((Utils.getDistance(zulrah, primary) * 0.10)));
				zulrah.addCloud(secondary, (int) Math.ceil((Utils.getDistance(zulrah, secondary) * 0.10)));
				if (!starting) {
					Sequence sequence = zulrah.getNextSequence();
					sequence.attack(zulrah, instance, target);
				}
				stop();
			}
			
		}, 3);
		return 3;
	}
	
}
