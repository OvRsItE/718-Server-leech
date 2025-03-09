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
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jul 23, 2018
 */
public class MeleeSequence implements Sequence {

	private static final Animation LEFT_MELEE_ANIM = Animation.createOSRS(5806);
	private static final Animation RIGHT_MELEE_ANIM = Animation.createOSRS(5807);
	
	private static final Graphics STUN_GFX = new Graphics(254, 0, 96);
	
	private static final Position EASTERN_SAFESPOT = new Position(2272, 3072, 0);
	private static final Position WESTERN_SAFESPOT = new Position(2264, 3072, 0);
	
	private static final boolean isSafespot(final ZulrahInstance instance, final Position tile) {
		return instance.getLocation(EASTERN_SAFESPOT).getTileHash() == tile.getTileHash()
				|| instance.getLocation(WESTERN_SAFESPOT).getTileHash() == tile.getTileHash();
	}
	
	@Override
	public int attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		WorldTasksManager.schedule(new WorldTask() {
			private Position tile;
			private int ticks;
			@Override
			public void run() {
				if (zulrah.isDead() || zulrah.isFinished()) {
					stop();
					return;
				}
				if (ticks == 0) {
					tile = new Position(target);
					final Position zul = new Position(zulrah);
					if (tile.getX() < (zul.getX() - 1)) {
						zulrah.setNextFacePosition(instance.getLocation(2263, 3074, 0));
						zulrah.animate(tile.getY() >= instance.getLocation(0, 3074, 0).getY() ? RIGHT_MELEE_ANIM : LEFT_MELEE_ANIM);
					} else if (tile.getX() > (zul.getX() + 5)) {
						zulrah.setNextFacePosition(instance.getLocation(2273, 3074, 0));
						zulrah.animate(tile.getY() >= instance.getLocation(0, 3074, 0).getY() ? LEFT_MELEE_ANIM : RIGHT_MELEE_ANIM);
					} else {
						zulrah.setNextFacePosition(instance.getLocation(2268, 3069, 0));
						zulrah.animate(tile.getX() >= instance.getLocation(2268, 0, 0).getX() ? LEFT_MELEE_ANIM : RIGHT_MELEE_ANIM);
					}
				}
				if (ticks == 3) {
					if (target.withinDistance(tile, 1) && !isSafespot(instance, target)) {
						target.addFreezeDelay(3000);
						target.sendMessage("You have been stunned!");
						target.setFindTargetDelay(3000);
						target.setNextGraphics(STUN_GFX);
						target.applyHit(new Hit(zulrah, Utils.random(410), HitLook.REGULAR_DAMAGE));
						target.getToxin().applyToxin(ToxinType.VENOM, 60);
					}
				}
				if (ticks == 7) {
					Sequence sequence = zulrah.getNextSequence();
					sequence.attack(zulrah, instance, target);
					stop();
				}
				ticks++;
			}
		}, 0, 1);
		return 16;
	}

}
