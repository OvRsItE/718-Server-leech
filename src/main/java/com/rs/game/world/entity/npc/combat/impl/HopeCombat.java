package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class HopeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 12900 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.getRandom(5);
		int size = npc.getSize();

		if (attackStyle == 0) {
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (distanceX > size || distanceX < -1 || distanceY > size
					|| distanceY < -1)
				attackStyle = Utils.getRandom(4) + 1;
			else {
				delayHit(
						npc,
						0,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MELEE, target)));
				npc.animate(new Animation(defs.getAttackEmote()));
				return defs.getAttackDelay();
			}
		} else if (attackStyle == 1 || attackStyle == 2) {
			int damage = Utils.getRandom(650);
			final Player player = target instanceof Player ? (Player) target
					: null;
			npc.animate(new Animation(14458));
			npc.setNextGraphics(new Graphics(1898, 0, 100));
			World.sendProjectile(npc, target, 3364, 60, 16, 65, 47, 16, 0);
			if (Combat.hasHopeProtection(target)
					|| (player != null && (player.Ass = false)))
				damage = 0;
			if (player != null
					&& player.getFireImmune() > Utils.currentTimeMillis()) {
				if (damage != 0)
					damage = Utils.getRandom(164);
			} else if (damage == 0)
				damage = Utils.getRandom(164);
			else if (player != null) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getPackets().sendGameMessage(
								"You are hit by the Hope devourer's drain!",
								true);
						player.setNextGraphics(new Graphics(3240, 50, 0));
					}
				}, 0);
				delayHit(npc, 2, target, getRegularHit(npc, damage));
			}
		} else if (attackStyle == 3) {
			int damage;
			final Player player = target instanceof Player ? (Player) target
					: null;
			npc.animate(new Animation(14458));
			npc.setNextGraphics(new Graphics(1739, 0, 100));
			World.sendProjectile(npc, target, 3200, 60, 16, 65, 35, 16, 0);
			if (Combat.hasHopeProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.getPackets()
							.sendGameMessage(
									"Your amulet absorbs most of the Hope Devourer's soul drain!",
									true);
			} else if (player != null
					&& (player.Ass = true)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.getPackets()
							.sendGameMessage(
									"Your prayer absorbs most of the Hope Devourer's soul drain!",
									true);
			} else {
				damage = Utils.getRandom(750);
				if (player != null) {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
						player.setNextGraphics(new Graphics(3240)); 
						}
					}, 0);
				}
}
			if (Utils.getRandom(2) == 0)
				target.getToxin().applyToxin(ToxinType.POISON);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
		} else if (attackStyle == 4) {
			int damage;
			final Player player = target instanceof Player ? (Player) target
					: null;
			npc.animate(new Animation(14458));
			npc.setNextGraphics(new Graphics(1960, 0, 100));
			World.sendProjectile(npc, target, 3439, 60, 16, 100, 45, 16, 0);
			if (Combat.hasAntiDragProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.getPackets()
							.sendGameMessage(
									"Your shield absorbs most of the dragon's freezing breath!",
									true);
			} else if (player != null
					&& (player.Ass = true)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.getPackets()
							.sendGameMessage(
									"Your prayer absorbs most of the hope devourer's breath!",
									true);
			} else {
				damage = Utils.getRandom(650);
				if (player != null) {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getPackets()
									.sendGameMessage(
											"You are hit by the Hope Devourer's soul drain! Try purchasing a mad necklace from the Slayer Shop",
											true);
							player.setNextGraphics(new Graphics(3440, 50, 0));
						}
					}, 1);
				}
			}
			if (Utils.getRandom(2) == 0)
				target.addFreezeDelay(15000);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
		} else {
			int damage;
			final Player player = target instanceof Player ? (Player) target
					: null;
			npc.animate(new Animation(14460));
			npc.setNextGraphics(new Graphics(2452, 0, 100));
			World.sendProjectile(npc, target, 2452, 60, 16, 120, 35, 16, 0);
			if (Combat.hasHopeProtection(target)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.getPackets()
							.sendGameMessage(
									"Your shield absorbs most of the Hope Devourer's attack!",
									true);
			} else if (player != null
					&& (player.Ass = true)) {
				damage = getRandomMaxHit(npc, 164, NPCCombatDefinitions.MAGE,
						target);
				if (player != null)
					player.getPackets()
							.sendGameMessage(
									"Your prayer absorbs most of the Hope Devourer's attack!",
									true);
			} else {
				damage = Utils.getRandom(650);
				if (player != null) {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getPackets()
									.sendGameMessage(
											"You are hit by the Hope Devourer's soul drain! Try purchasing a mad necklace from the Slayer Shop",
											true);
							player.setNextGraphics(new Graphics(3434, 25, 0));
						}
					}, 0);
				}
			}
			delayHit(npc, 2, target, getRegularHit(npc, damage));
		}
		return defs.getAttackDelay();
	}
}