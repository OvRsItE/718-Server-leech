package com.rs.game.world.entity.npc.SoulWars;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class AvatarsCombat extends CombatScript {
	
	int damage = Utils.getRandom(800);

	@Override
	public Object[] getKeys() {
		return new Object[] { "Avatar of " };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (target instanceof Player)
		((Player) target).getPrayer().drainPrayer(damage + 260);
		npc.animate(new Animation(defs.getAttackEmote()));
		delayHit(npc, 1, target, getMeleeHit(npc, Utils.getRandom(960)));
		return defs.getAttackDelay();
     } 
}