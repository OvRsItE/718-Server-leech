package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.BuffManager.BuffType;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class BrutalDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Brutal green dragon", "Brutal blue dragon", "Brutal red dragon", "Brutal black dragon" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(1) == 0) {
			npc.animate(npc.getId() >= 20_000 ? Animation.createOSRS(84) : new Animation(12259));
			delayHit(npc, 0, target,
					getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 2707, 34, 16, 40, 35, 16, 0);
		}
		if (Utils.getRandom(5) == 0) {
			int damage = Utils.getRandom(650);
			npc.animate(npc.getId() >= 20_000 ? Animation.createOSRS(84) : new Animation(12259));
			npc.setNextGraphics(new Graphics(1, 0, 100));
			final Player player = target instanceof Player ? (Player) target : null;
			if (player.getBuffManager().hasBuff(BuffType.SUPER_ANTI_FIRE)) {
				player.getPackets().sendGameMessage("Your absorb's all of the dragon's breath!", true);
				damage = 0;
			}
			else if (Combat.hasAntiDragProtection(target) && (player != null
					&& (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)) 
					|| player.getBuffManager().hasBuff(BuffType.ANTI_FIRE))) {
				damage = 0;
				player.getPackets()
						.sendGameMessage("Your " + (Combat.hasAntiDragProtection(target) ? "shield" : "prayer")
								+ " absorb's most of the dragon's breath!", true);
			}
			else if (player != null && player.getBuffManager().hasBuff(BuffType.ANTI_FIRE)) {
				if (damage != 0)
					damage = Utils.getRandom(50);
			} else if (damage == 0)
				damage = Utils.getRandom(50);
			else if (player != null)
				player.getPackets().sendGameMessage("You are hit by the dragon's fiery breath!", true);
			delayHit(npc, 1, target, getRegularHit(npc, damage));
		} else { //Melee
			npc.animate(
					npc.getId() >= 20_000 ? Animation.createOSRS(80) : new Animation(defs.getAttackEmote()));
			npc.playSound(408, 1);
			delayHit(npc, 0, target,
					getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
	
}
