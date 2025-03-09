package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;
/**
 * 
 * @author Taylor, the author of all the classes that werent in matrix 718 but now are. But since
 * Eclipse doesn't auto tag i forget sometimes to tag myself but w.e who cares.
 * TODO Neem oil
 *
 */
public class GanoRunts extends CombatScript {
	
	
	private transient Player player;
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 14698, 14699 };
	}
	

	@Override
	public int attack(NPC npc, Entity target) {
		
		/**
		 * Gets the players attack style and npc definitions..
		 */
		int attackStyle = Utils.getRandom(0);
		int onlyMagic = CombatDefinitions.MAGIC_ATTACK; 
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		
		
		/**
		 * Handles the fight system.
		 */
			if (attackStyle == 0) {
				npc.animate(new Animation(15477));
				delayHit(npc, 1,target, getMagicHit(npc,getRandomMaxHit(npc, defs.getMaxHit() ,NPCCombatDefinitions.MAGE, target)));
				World.sendProjectile(npc, target, 2035, 35, 32, 30, 50, 0, 0);
		}
		return 3;
	}

}
