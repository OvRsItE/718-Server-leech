package com.rs.game.world.entity.npc.combat.impl;

import com.rs.Constants;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class LeatherDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Green dragon", "Blue dragon", "Red dragon", "Black dragon", 742, 14548 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)
			return 0;
		if (Utils.getRandom(3) != 0) { // Melee
			if (npc.getId() >= Constants.OSRS_NPCS_OFFSET)
				npc.animate(Animation.createOSRS(25));
			else
				npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		} else { // Dragonfire
			int damage = getMaxDamage(target.getAsPlayer(), 0);
			if (npc.getId() >= Constants.OSRS_NPCS_OFFSET)
				npc.animate(Animation.createOSRS(81));
			else
				npc.animate(new Animation(12259));
			npc.setNextGraphics(new Graphics(1, 0, 100));
			delayHit(npc, 1, target, getRegularHit(npc, Utils.getRandom(damage)));
		}
		return defs.getAttackDelay();
	}

	private int getMaxDamage(Player target, int minDamage) {
		return (int) Math.max(650 * (1 - Combat.getDragonfireResistance(target)), minDamage);
	}
}
