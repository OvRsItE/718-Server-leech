package com.rs.game.world.entity.npc.combat;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Steeltitan;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import java.util.concurrent.TimeUnit;

public abstract class CombatScript {

	/*
	 * Returns ids and names
	 */
	public abstract Object[] getKeys();

	/*
	 * Returns Move Delay
	 */
	public abstract int attack(NPC npc, Entity target);

	public boolean init(NPC npc) { return true; }

	public static void delayHit(NPC npc, int delay, final Entity target, final Hit... hits) {
		npc.getCombat().addAttackedByDelay(target);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				for (Hit hit : hits) {
					NPC npc = (NPC) hit.getSource();
					if (npc.isDead() || npc.isFinished() || target.isDead()
							|| target.isFinished())
						return;
					target.applyHit(hit);
					npc.getCombat().doDefenceEmote(target);
					/*if (!(npc instanceof Nex)
							&& hit.getLook() == HitLook.MAGIC_DAMAGE
							&& hit.getDamage() == 0)
						target.setNextGraphics(new Graphics(85, 0, 100));*/
					if (target instanceof Player) {
						Player p2 = (Player) target;
						p2.closeInterfaces();
						if (p2.getCombatDefinitions().isAutoRelatie()
								&& !p2.getActionManager().hasSkillWorking()
								&& !p2.hasWalkSteps()) 
							p2.getActionManager().setAction(
									new PlayerCombat(npc));
					} else {
						NPC n = (NPC) target;
						if (!n.isUnderCombat()
								|| n.canBeAttackedByAutoRelatie())
							n.setTarget(npc);
					}

				}
			}

		}, delay);
	}
	
	public static void delayHit(NPC npc, final Entity target, final Projectile projectile, final Hit... hits) {
		delayHit(npc, projectile.getHitSync(npc, target), target, hits);
		World.sendProjectile(npc, target, projectile);
	}
	
	public static void syncProjectileHit(Entity source, Entity target, Graphics graphics, Projectile projectile) {
		CoresManager.slowExecutor.schedule(() -> {
			target.setNextGraphics(graphics);
		}, (projectile.getHitSync(source, target) * (600 + projectile.getDelay())), TimeUnit.MILLISECONDS);
	}

	public static Hit getRangeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.RANGE_DAMAGE);
	}

	public static Hit getMagicHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MAGIC_DAMAGE);
	}

	public static Hit getRegularHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.REGULAR_DAMAGE);
	}

	public static Hit getMeleeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MELEE_DAMAGE);
	}

	public static int getMaxHit(NPC npc, int attackType, Entity target) {
		return getRandomMaxHit(npc, npc.getMaxHit(attackType), attackType, target);
	}
	
	public static int getRandomMaxHit(NPC npc, int maxHit, int attackStyle,
			Entity target) {
		int[] bonuses = npc.getBonuses();
		double att = bonuses == null ? 0
				: attackStyle == NPCCombatDefinitions.RANGE ? bonuses[CombatDefinitions.RANGE_ATTACK]
						: attackStyle == NPCCombatDefinitions.MAGE ? bonuses[CombatDefinitions.MAGIC_ATTACK]
								: bonuses[CombatDefinitions.STAB_ATTACK];
		double def;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			def = p2.getSkills().getLevel(Skills.DEFENCE)
					+ (2
					* p2.getCombatDefinitions().getBonuses()[attackStyle == NPCCombatDefinitions.RANGE ? CombatDefinitions.RANGE_DEF
							: attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
									: CombatDefinitions.STAB_DEF]);
			def *= p2.getPrayer().getDefenceMultiplier();
			if (attackStyle == NPCCombatDefinitions.MELEE) {
				if (p2.getFamiliar() instanceof Steeltitan)
					def *= 1.15;
			}
		} else {
			NPC n = (NPC) target;
			def = n.getBonuses() == null ? 0
					: n.getBonuses()[attackStyle == NPCCombatDefinitions.RANGE ? CombatDefinitions.RANGE_DEF
							: attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
									: CombatDefinitions.STAB_DEF];
			def *= 2;
		}
		double prob = att / def;
		if (prob > 0.90) // max, 90% prob hit so even lvl 138 can miss at lvl 3
			prob = 0.90;
		else if (prob < 0.05) // minimun 5% so even lvl 3 can hit lvl 138
			prob = 0.05;
		if (prob < Math.random())
			return 0;
		return Utils.getRandom(maxHit);
	}

	protected final Hit projectileAttack(NPC npc, Entity target, Projectile projectile, Hit hit, int animation) {
		if (animation != -1)
			npc.animate(animation);
		int delay = projectile.send(npc, target);
		target.addEvent(event -> {
			event.delay(delay - 1);
			target.applyHit(hit);
		});
		return hit;
	}


}
