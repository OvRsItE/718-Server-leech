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

public class MetalDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bronze dragon", "Iron dragon", "Steel dragon", "Mithril dragon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = target instanceof Player ? (Player) target : null;
		int damage;
		String protect_message = Combat.getProtectMessage(player);
		switch (Utils.getRandom(1)) {
			case 0:
				if (npc.withinDistance(target, 3)) {
					damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target);
					npc.animate(new Animation(defs.getAttackEmote()));
					delayHit(npc, 0, target, getMeleeHit(npc, damage));
				} else {
					damage = getMaxDamage(player, 0);
					npc.animate(new Animation(13160));
					World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
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
					if (protect_message != null)
						player.getPackets().sendGameMessage(protect_message, true);
					delayHit(npc, 1, target, getRegularHit(npc, Utils.getRandom(damage)));
				}
				break;
			case 1:
				if (npc.withinDistance(target, 3)) {
					damage = getMaxDamage(player, 0);
					npc.animate(new Animation(13164));
					npc.setNextGraphics(new Graphics(2465));
					delayHit(npc, 1, target, getRegularHit(npc, Utils.getRandom(damage)));
				} else {
					damage = getMaxDamage(player, 0);
					npc.animate(new Animation(13160));
					World.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
					delayHit(npc, 1, target, getRegularHit(npc, Utils.getRandom(damage)));
				}
				if (protect_message != null)
					player.getPackets().sendGameMessage(protect_message, true);
				break;
		}
		return defs.getAttackDelay();
	}

	private int getMaxDamage(Player target, int minDamage) {
		return (int) Math.max(650 * (1 - Combat.getDragonfireResistance(target)), minDamage);
	}

}
