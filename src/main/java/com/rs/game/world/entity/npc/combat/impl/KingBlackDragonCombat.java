package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;


public class KingBlackDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 50 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.getRandom(5);
		if (attackStyle == 0) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
			npc.animate(new Animation(defs.getAttackEmote()));
			return defs.getAttackDelay();
		} else if (attackStyle == 1 || attackStyle == 2) {
			final Player player = target instanceof Player ? (Player) target : null;
			int damage = Utils.getRandom(getMaxDamage(player, 0));
			npc.animate(new Animation(17784));
			npc.setNextGraphics(new Graphics(3441, 0, 100));
			World.sendProjectile(npc, target, 3442, 60, 16, 65, 47, 16, 0);
			if (player != null){
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if(player == null) {
							return;
						}
						if (player !=null) {
							if (damage == 0) {
								player.getPackets().sendGameMessage(Combat.getProtectMessage(player));
							} else {
								player.getPackets().sendGameMessage("You are hit by the dragon's fiery breath!", true);
								player.setNextGraphics(new Graphics(3443, 50, 0));
							}
						}
					}
				}, 0);
				delayHit(npc, 2, target, getRegularHit(npc, damage));
			}
		} else if (attackStyle == 3) {
			final Player player = target instanceof Player ? (Player) target : null;
			int damage = Utils.getRandom(getMaxDamage(player, 100));
			npc.animate(new Animation(17783));
			npc.setNextGraphics(new Graphics(3435, 0, 100));
			World.sendProjectile(npc, target, 3436, 60, 16, 65, 35, 16, 0);
			if (player != null) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if(player == null) {
							return;
						}
						if (player !=null) {
							player.getPackets().sendGameMessage("You are hit by the dragon's poisonous breath!", true);
							player.setNextGraphics(new Graphics(3437, 50, 0));
						}
					}
				}, 0);
			}
			if (Utils.getRandom(2) == 0)
				target.getToxin().applyToxin(ToxinType.POISON, 80);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
		} else if (attackStyle == 4) {
			final Player player = target instanceof Player ? (Player) target : null;
			int damage = Utils.getRandom(getMaxDamage(player, 100));
			npc.animate(new Animation(17784));
			npc.setNextGraphics(new Graphics(3438, 0, 100));
			World.sendProjectile(npc, target, 3439, 60, 16, 100, 45, 16, 0);
			if (player != null){
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if(player == null) {
							return;
						}
						if (player != null) {
							player.getPackets().sendGameMessage("You are hit by the dragon's freezing breath!", true);
							player.setNextGraphics(new Graphics(3440, 50, 0));
						}
					}
				}, 1);
			}
			if (Utils.getRandom(2) == 0)
				target.addFreezeDelay(15000);
			delayHit(npc, 2, target, getRegularHit(npc, damage));
		} else {
			final Player player = target instanceof Player ? (Player) target : null;
			int damage = Utils.getRandom(getMaxDamage(player, 100));
			npc.animate(new Animation(17785));
			npc.setNextGraphics(new Graphics(3432, 0, 100));
			World.sendProjectile(npc, target, 3433, 60, 16, 120, 35, 16, 0);
			if (player != null){
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if(player == null) {
							return;
						}
						if (player != null) {
							player.getPackets().sendGameMessage("You are hit by the dragon's shocking breath!", true);
							player.setNextGraphics(new Graphics(3434, 25, 0));
						}
					}
				}, 0);
			}
			delayHit(npc, 2, target, getRegularHit(npc, damage));
		}
		return defs.getAttackDelay();
	}

	private int getMaxDamage(Player target, int minDamage) {
		if (target == null) {
			return Math.max(650, minDamage);
		}
		if (target.getBuffManager().hasBuff(BuffManager.BuffType.SUPER_ANTI_FIRE)) {
			target.getPackets().sendGameMessage("Your absorb's all of the dragon's breath!", true);
			return Math.max(0, minDamage);
		}

		return (int) Math.max(650 * (1 - Combat.getDragonfireResistance(target)), minDamage);
	}

}