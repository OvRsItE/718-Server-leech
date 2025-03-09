package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class BarrelChestCombat extends CombatScript {

    @Override
    public int attack(NPC npc, Entity target) {
	final NPCCombatDefinitions defs = npc.getCombatDefinitions();
	final Player player = target instanceof Player ? (Player) target : null;
	int damage;
	switch (1) {
	case 1: // Melee
	    damage = Utils.getRandom(450);
	    npc.animate(new Animation(5895));
	    if (target instanceof Player) {
		player.prayer.drainPrayer(Utils.getRandom(650));
	    }
	    delayHit(npc, 1, target, getMeleeHit(npc, damage));
	    break;
	}
	return defs.getAttackDelay();
    }

    @Override
    public Object[] getKeys() {
	return new Object[] { "Barrelchest" };
    }
}