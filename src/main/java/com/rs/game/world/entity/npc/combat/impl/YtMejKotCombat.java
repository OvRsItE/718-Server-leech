package com.rs.game.world.entity.npc.combat.impl;

import java.util.List;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

public class YtMejKotCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] {"Yt-MejKot"};
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.animate(new Animation(defs.getAttackEmote()));
		delayHit(
				npc,
				0,
				target,
				getMeleeHit(
						npc,
						getRandomMaxHit(npc, defs.getMaxHit(), defs.getAttackStyle(),
								target)));
		if(npc.getHitpoints() < npc.getMaxHitpoints()/2) {
			if(npc.getTemporaryAttributtes().remove("Heal") != null) {
				npc.setNextGraphics(new Graphics(2980, 0, 100));
				List<Integer> npcIndexes = World.getRegion(npc.getRegionId()).getNPCsIndexes();
				if(npcIndexes != null) {
					for(int npcIndex : npcIndexes) {
						NPC n = World.getNPCs().get(npcIndex);
						if(n == null || n.isDead() || n.isFinished())
							continue;
						n.heal(100);
					}
				}
			}else
				npc.getTemporaryAttributtes().put("Heal", Boolean.TRUE);
		}
		return defs.getAttackDelay();
	}
}
