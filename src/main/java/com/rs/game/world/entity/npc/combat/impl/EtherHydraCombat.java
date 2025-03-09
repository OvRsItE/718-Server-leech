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
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

public class EtherHydraCombat extends CombatScript {
	private static final Graphics MAGIC = Graphics.createOSRS(1662);
	private static final Graphics RANGED = Graphics.createOSRS(1663);
	private static final Projectile MAGIC_PROJECTILE_1 = new Projectile(MAGIC.getId(), 130, 21, 25, 30, 30, 0);
	private static final Projectile RANGED_PROJECTILE_1 = new Projectile(RANGED.getId(), 130, 35, 25, 30, 30, 0);
	private AttackStyle currentStyle = Misc.rollPercent(50) ? AttackStyle.RANGED : AttackStyle.MAGIC;
	@Override
	public Object[] getKeys() {
	
		return new Object[] {16140};
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (currentStyle == AttackStyle.MAGIC)
			magicAttack(npc, target);
		else
			rangedAttack(npc, target);
		switchStyle();
		return defs.getAttackDelay()+2;
	}
	private void magicAttack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.animate(new Animation(28248));
		World.sendProjectile(npc, target, RANGED_PROJECTILE_1);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				delayHit(
						npc,
						1,
						target,
						getRangeHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit()-2,
										NPCCombatDefinitions.RANGE, target)));
			}
		}, 3);
	}
	private void rangedAttack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.animate(new Animation(28248));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				World.sendProjectile(npc, target, MAGIC_PROJECTILE_1);
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
		}, 2);
	}
	private void switchStyle() {
		currentStyle = currentStyle == AttackStyle.MAGIC ? AttackStyle.RANGED : AttackStyle.MAGIC;
	}



}
