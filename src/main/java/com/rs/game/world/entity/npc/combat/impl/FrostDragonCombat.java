package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class FrostDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Frost dragon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = target instanceof Player ? (Player) target : null;
		int damage;
		switch (Utils.getRandom(3)) {
		case 0: // Melee
			if (npc.withinDistance(target, 3)) {
				damage = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MELEE, target);
				npc.animate(new Animation(defs.getAttackEmote()));
				delayHit(npc, 0, target, getMeleeHit(npc, damage));
			} else {
				damage = Utils.getRandom(650);
				if (player.getBuffManager().hasBuff(BuffManager.BuffType.SUPER_ANTI_FIRE)) {
					player.getPackets().sendGameMessage("Your absorb's all of the dragon's breath!", true);
					damage = 0;
				}
				else if (Combat.hasAntiDragProtection(target) && (player != null
						&& (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7))
						|| player.getBuffManager().hasBuff(BuffManager.BuffType.ANTI_FIRE))) {
					damage = 0;
					player.getPackets()
							.sendGameMessage("Your " + (Combat.hasAntiDragProtection(target) ? "shield" : "prayer")
									+ " absorb's most of the dragon's breath!", true);
				}
				npc.animate(new Animation(13155));
				World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
				delayHit(npc, 1, target, getRegularHit(npc, damage));
			}
			break;
		case 1: // Dragon breath
			if (npc.withinDistance(target, 3)) {
				damage = Utils.getRandom(650);
				if (player.getBuffManager().hasBuff(BuffManager.BuffType.SUPER_ANTI_FIRE)) {
					player.getPackets().sendGameMessage("Your absorb's all of the dragon's breath!", true);
					damage = 0;
				}
				else if (Combat.hasAntiDragProtection(target) && (player != null
						&& (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7))
						|| player.getBuffManager().hasBuff(BuffManager.BuffType.ANTI_FIRE))) {
					damage = 0;
					player.getPackets()
							.sendGameMessage("Your " + (Combat.hasAntiDragProtection(target) ? "shield" : "prayer")
									+ " absorb's most of the dragon's breath!", true);
				}
				npc.animate(new Animation(13152));
				npc.setNextGraphics(new Graphics(2465));
				delayHit(npc, 1, target, getRegularHit(npc, damage));
			} else {
				damage = Utils.getRandom(650);
				if (player.getBuffManager().hasBuff(BuffManager.BuffType.SUPER_ANTI_FIRE)) {
					player.getPackets().sendGameMessage("Your absorb's all of the dragon's breath!", true);
					damage = 0;
				}
				else if (Combat.hasAntiDragProtection(target) && (player != null
						&& (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7))
						|| player.getBuffManager().hasBuff(BuffManager.BuffType.ANTI_FIRE))) {
					damage = 0;
					player.getPackets()
							.sendGameMessage("Your " + (Combat.hasAntiDragProtection(target) ? "shield" : "prayer")
									+ " absorb's most of the dragon's breath!", true);
				}
				npc.animate(new Animation(13155));
				World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
				delayHit(npc, 1, target, getRegularHit(npc, damage));
			}
			break;
		case 2: // Range
			damage = Utils.getRandom(250);
			npc.animate(new Animation(13155));
			World.sendProjectile(npc, target, 2707, 28, 16, 35, 35, 16, 0);
			delayHit(npc, 1, target, getRangeHit(npc, damage));
			break;
		case 3: // Ice arrows range
			damage = Utils.getRandom(250);
			npc.animate(new Animation(13155));
			World.sendProjectile(npc, target, 369, 28, 16, 35, 35, 16, 0);
			delayHit(npc, 1, target, getRangeHit(npc, damage));
			break;
		case 4: // Orb crap
			break;
		}
		return defs.getAttackDelay();
	}

}
