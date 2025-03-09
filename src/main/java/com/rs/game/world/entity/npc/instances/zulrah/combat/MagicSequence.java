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
public class MagicSequence implements Sequence {

	private static final Animation ANIM = Animation.createOSRS(5069);

	public MagicSequence(final int amount) {
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
				if (Utils.random(6) == 0) {
					World.sendProjectile(zulrah, target, Graphics.createOSRS(1044).getId(), 65, 10, 40, 15, 16, 0);
					target.getToxin().applyToxin(ToxinType.VENOM, 6);
					CombatScript.delayHit(zulrah, (int) Math.ceil((Utils.getDistance(zulrah, target) * 0.10)), target,
							new Hit(zulrah, CombatScript.getRandomMaxHit(zulrah, 410, NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
				} else {
					World.sendProjectile(zulrah, target, Graphics.createOSRS(1046).getId(), 65, 10, 40, 15, 16, 0);
					target.getToxin().applyToxin(ToxinType.VENOM, 6);
					CombatScript.delayHit(zulrah, (int) Math.ceil((Utils.getDistance(zulrah, target) * 0.10)), target,
							new Hit(zulrah, CombatScript.getRandomMaxHit(zulrah, 410, NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
				}
				if (--num <= 0) {
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
