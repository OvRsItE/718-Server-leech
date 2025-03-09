/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.wilderness;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
public class ScorpiaCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 16009 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions definition = npc.getCombatDefinitions();
		delayHit(npc, 1, target, new Hit(npc, CombatScript.getRandomMaxHit(npc, 
				definition.getMaxHit(), NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		npc.animate(definition.getAttackEmote());
		return definition.getAttackDelay();
	}

}
