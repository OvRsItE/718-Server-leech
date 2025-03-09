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
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author Kris
 * @author ReverendDread
 * Jul 23, 2018
 */
public class RangedSequence implements Sequence {

	private static final Animation ANIM = Animation.createOSRS(5069);

	public RangedSequence(final int amount) {
		this.amount = amount;
	}

	private final int amount;

	@Override
	public int attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		zulrah.faceEntity(target);
		WorldTasksManager.schedule(new WorldTask() {
			private int num = amount;

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
				World.sendProjectile(zulrah, target, Graphics.createOSRS(1044).getId(), 65, 10, 40, 15, 18, 0);
				zulrah.delayHit((int) Math.ceil((Utils.getDistance(zulrah, target) * 0.10)), new Hit(zulrah, CombatScript.getRandomMaxHit(zulrah, 410, NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
				if (--num <= 0) {
					Sequence sequence = zulrah.getNextSequence();
					sequence.attack(zulrah, instance, target);
					stop();
				}
			}
		}, 0, 3);
		return 3;
	}
	
}
