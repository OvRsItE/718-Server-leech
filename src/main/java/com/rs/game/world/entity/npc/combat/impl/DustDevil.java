package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class DustDevil extends CombatScript {

    @Override
    public Object[] getKeys() {
	return new Object[] { 1624, 27404 };
    }

    @Override
    public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		if (!Slayer.hasMask(target)) {
		    Player targetPlayer = (Player) target;
		    int randomSkill = Utils.random(0, 6);
		    int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
		    targetPlayer.getSkills().set(randomSkill, currentLevel < 5 ? 0 : currentLevel - Utils.random(20));
		    targetPlayer.getPackets().sendGameMessage("The dust devil's smoke suffocates you.");
		    delayHit(npc, 1, target, getMeleeHit(npc, targetPlayer.getMaxHitpoints() / 4));
		} else
		    delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
		npc.animate(new Animation(def.getAttackEmote()));
		return def.getAttackDelay();
    }

}
