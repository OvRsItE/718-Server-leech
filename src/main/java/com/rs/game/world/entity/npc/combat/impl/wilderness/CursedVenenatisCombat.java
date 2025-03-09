package com.rs.game.world.entity.npc.combat.impl.wilderness;

import com.google.common.collect.Lists;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.listeners.HitListener;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import java.util.List;

/**
 * @author ReverendDread
 * Apr 24, 2018
 */
public class CursedVenenatisCombat extends CombatScript {

	//171, 172 prayer drain gfx
	//2721, 2726 magic gfx

	private static final int ATTACK = Animation.createOSRS(5319).getIds()[0];

	private final List<NPC> minions = Lists.newArrayList();


	@Override
	public Object[] getKeys() {
		return new Object[] { "Cursed venenatis" };
	}

	public boolean init(NPC npc) {
		npc.deathEndListener = ((entity, killer) -> {

			minions.forEach(NPC::finish);

		});
		npc.setCanBeFrozen(false);
		npc.setCapDamage(750);
		return true;
	}

	@Override
	public int attack(NPC npc, Entity target) {
		List<Entity> targets = npc.getPossibleTargets();
		minions.removeIf(NPC::isFinished);
		int random = Utils.random(6);
		if (random == 0)  //prayer drain
			prayer_drain(npc, targets);
		if (random == 1 && minions.size() < 4)
			minionspawn(npc, target);
		if (target.withinDistance(npc, 1)) {
			return melee_combo_attack(npc, targets);
		} else {
			return magic_combo_attack(npc, targets);
		}
	}
	
	/**
	 * Venenatis melee attack with combo conditional.
	 * @param npc
	 * @param targets
	 * @return
	 */
	public int melee_combo_attack(NPC npc, List<Entity>targets) {
		for (Entity target : targets) {
			if (target.isNPC())
				continue;
			boolean magic_protect = target.getAsPlayer().getPrayer().usingPrayer(0, 17);
			int random = Utils.random(0, 15);
			npc.animate(new Animation(ATTACK));
			if (!magic_protect && random == 0) { //web attack
				World.sendProjectile(npc, target, 2721, 35, 36, 40, 1, 16, 0);
				delayHit(npc, 2, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(),
						NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
			}
			delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(),
					NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		}

		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Venenatis magic attack with combo conditional.
	 * @param npc
	 * @param targets
	 * @return
	 */
	public int magic_combo_attack(NPC npc, List<Entity>targets) {
		for (Entity target : targets) {
			if (target.isNPC())
				continue;
			boolean magic_protect = target.getAsPlayer().getPrayer().usingPrayer(0, 17);
			int random = Utils.random(0, 15);
			npc.animate(new Animation(ATTACK));
			if (!magic_protect && random == 0) { //web attack
				World.sendProjectile(npc, target, 2721, 35, 36, 40, 1, 16, 0);
				delayHit(npc, 2, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(),
						NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
			}
			delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(),
					NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
			World.sendProjectile(npc, target, Graphics.createOSRS(1462).getId(), 35, 36, 40, 1, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					target.setNextGraphics(new Graphics(2726, 0, 150));
				}

			});


		}
		return npc.getCombatDefinitions().getAttackDelay();

	}

	public int minionspawn(NPC npc, Entity target) {
		npc.animate(new Animation(ATTACK));
		NPC minion = World.spawnNPC(16125, new Position(3056, 3789 + 2, 0),-1, 0, npc.canBeAttackFromOutOfArea(), npc.isSpawned());
		minion.setForceAgressive(true);
		minion.setAtMultiArea(true);
		minion.setTarget(target);
		minions.add(minion);
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Venenatis prayer drain attack.
	 * @param npc
	 * @param targets
	 * @return
	 */
	public void prayer_drain(NPC npc, List<Entity>targets) {
		for (Entity target : targets) {
			if (target.isNPC())
				continue;
			World.sendProjectile(npc, target, 171, 35, 36, 20, 2, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					int damage = getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target) + Utils.random(10, 300);
					target.getAsPlayer().getPrayer().drainPrayer((int) Math.floor(damage * 0.35));
					delayHit(npc, 0, target, new Hit(npc, damage, HitLook.MAGIC_DAMAGE));
					target.setNextGraphics(new Graphics(170, 0, 150));
					target.getAsPlayer().getPackets().sendGameMessage("Some of your prayer has been drained by Cursed venenatis's attack!", true);
				}

			}, 2);
		}

	}

}
