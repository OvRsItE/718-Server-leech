package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class AbyssalDemon extends CombatScript {

	/**
	 * Author that little turkish noob Savions Sw
	 */

	@Override
	public Object[] getKeys() {
		return new Object[] { 1615, 979, 27390, 16101 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.animate(new Animation(defs.getAttackEmote()));
		delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), -1, target)));
		if (Utils.getRandom(2) == 0) {
			TeleportAway(npc, target);
		}
		return defs.getAttackDelay();
	}

	/*
	 * Special attack of the NPC
	 */

	private void TeleportAway(NPC npc, Entity target) {
		Position teleTile;
		for (int trycount = 0; trycount < 10; trycount++) {
			boolean target_tele = Utils.getRandom(1) == 0;
			teleTile = new Position(target_tele ? target : npc, 1);
			if (!World.canMoveNPC(target.getZ(), teleTile.getX(), teleTile.getY(), target.getSize())) {
				continue;
			}
			if (target_tele) {
				target.setNextPosition(teleTile);
				target.setNextGraphics(new Graphics(409));
			} else {
				npc.setNextPosition(teleTile);
				npc.setNextGraphics(new Graphics(409));
			}
			return;
		}
	}
	
}