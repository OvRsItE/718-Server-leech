package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class MithrilDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 5363 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if(Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0) && Utils.random(2) == 0) {
		    npc.animate(new Animation(defs.getAttackEmote()));
		    delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		}
		else if (Utils.getRandom(1) == 0) {
				npc.animate(new Animation(13160));
				delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
			}
		else if (Utils.getRandom(1) == 0) {
				npc.animate(new Animation(13160));
				delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
			}
			
		if (Utils.getRandom(5) == 0) {
			int damage = Utils.getRandom(650);
			npc.animate(new Animation(13160));
			npc.setNextGraphics(new Graphics(1, 0, 100));
			final Player player = target instanceof Player ? (Player) target
					: null;
			if (Combat.hasAntiDragProtection(target)
					|| (player != null && (player.getPrayer()
							.usingPrayer(0, 17) || player.getPrayer()
							.usingPrayer(1, 7)))) {
				damage = 0;
				player.getPackets()
						.sendGameMessage(
								"Your "
										+ (Combat.hasAntiDragProtection(target) ? "shield"
												: "prayer")
										+ " absorb's most of the dragon's breath!",
								true);
			}
			if (player != null
					&& player.getFireImmune() > Utils.currentTimeMillis()) {
				if (damage != 0)
					damage = Utils.getRandom(50);
			} else if (damage == 0)
				damage = Utils.getRandom(50);
			else if (player != null)
				player.getPackets().sendGameMessage(
						"You are hit by the dragon's fiery breath!", true);
			delayHit(npc, 1, target, getRegularHit(npc, damage));
		}
		return defs.getAttackDelay();
	}
}
