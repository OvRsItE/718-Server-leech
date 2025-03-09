package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.combat.AttackStyle;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.instances.vorkath.attack.MagicAttack;
import com.rs.game.world.entity.npc.instances.vorkath.attack.RangedAttack;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

public class UnderworldCerberusCombat extends CombatScript {

	private static final Animation MELEE = Animation.createOSRS(4491);

	private static final Animation RANGED = Animation.createOSRS(4490);

	private static final Animation MAGIC = Animation.createOSRS(4490);

	private static final Projectile RANGED_PROJ = new Projectile(6245, 43, 31, 50, 16, 50, 0);

	private static final Projectile MAGIC_PROJ = new Projectile(6242, 43, 31, 50, 16, 50, 0);


	private AttackStyle currentStyle = Misc.rollPercent(50) ? AttackStyle.RANGED : AttackStyle.MAGIC;
	@Override
	public Object[] getKeys() {
	
		return new Object[] {16146};
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.random(3);
		if (attackStyle == 1) {
			MeleeAttack(npc, target);

		}
		if (attackStyle == 2) {
			rangedAttack(npc, target);

		}
		if (attackStyle == 3) {
			magicAttack(npc, target);

		}
		if (attackStyle == 0) {
			magicAttack(npc, target);

		}
		return defs.getAttackDelay();
	}


	private void magicAttack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.animate(new Animation(24490));
		World.sendProjectile(npc, target, RANGED_PROJ);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						delayHit(
								npc,
								0,
								target,
								getRangeHit(
										npc,
										getRandomMaxHit(npc, defs.getMaxHit() - 2,
												NPCCombatDefinitions.RANGE, target)));
					}
				}, 1);
			}
		}, 1);
	}

	private void MeleeAttack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					2,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}

	private void rangedAttack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.animate(new Animation(24490));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				World.sendProjectile(npc, target, MAGIC_PROJ);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						delayHit(
								npc,
								0,
								target,
								getMagicHit(
										npc,
										getRandomMaxHit(npc, defs.getMaxHit() - 2,
												NPCCombatDefinitions.MAGE, target)));
					}

				}, 1);
			}
		}, 1);
	}
	private void switchStyle() {
		currentStyle = currentStyle == AttackStyle.MAGIC ? AttackStyle.RANGED : AttackStyle.MAGIC;
	}



}
