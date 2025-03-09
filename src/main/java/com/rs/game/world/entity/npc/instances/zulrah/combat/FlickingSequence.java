/**
 * 
 */
package com.rs.game.world.entity.npc.instances.zulrah.combat;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.instances.zulrah.Sequence;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahInstance;
import com.rs.game.world.entity.npc.instances.zulrah.ZulrahNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.Prayer;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

/**
 * @author ReverendDread
 * Jul 23, 2018
 */
public final class FlickingSequence implements Sequence {

	private static final Animation ANIM = Animation.createOSRS(5069);

	public FlickingSequence(final boolean magic) {
		this.magic = magic;
	}

	private final boolean magic;
	private int amount;

	@Override
	public int attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		zulrah.lock();
		zulrah.faceEntity(target);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (zulrah.isDead() || zulrah.isFinished()) {
					stop();
					return;
				}
				if (zulrah.getLastFaceEntity() < 0) {
					zulrah.faceEntity(target);
				}
				zulrah.animate(ANIM);
				final boolean mage = ((amount + (magic ? 1 : 0)) & 0x1) == 0;
				final Hit hit = new Hit(zulrah, target.getPrayer().usingPrayer(Prayer.NORMAL_PRAYERS, 
						(mage ? Prayer.PROTECT_FROM_MAGIC : Prayer.PROTECT_FROM_MISSILES)) ? 0 : CombatScript.getRandomMaxHit(zulrah, 410, mage ? NPCCombatDefinitions.MAGE : NPCCombatDefinitions.RANGE, target), HitLook.REGULAR_DAMAGE);
				if (mage) {
					World.sendProjectile(zulrah, target, Graphics.createOSRS(1046).getId(), 65, 10, 40, 15, 16, 0);
					zulrah.delayHit(2, hit);
				} else {
					World.sendProjectile(zulrah, target, Graphics.createOSRS(1044).getId(), 65, 10, 40, 15, 16, 0);
					zulrah.delayHit(2, hit);
				}
				if (++amount >= 10) {
					zulrah.lock(3);
					Sequence sequence = zulrah.getNextSequence();
					sequence.attack(zulrah, instance, target);
					stop();
				}
			}
		}, 0, 3);
		return 3;
	}

}
