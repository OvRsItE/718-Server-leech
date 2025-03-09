package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.utility.Utils;

public class BansheeCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
	return new Object[] {"Banshee", "Mighty banshee", "Twisted Banshee"};
    }
    
    
    
    
    @Override
    public int attack(NPC npc, Entity target) {
	NPCCombatDefinitions def = npc.getCombatDefinitions();
	if (!Slayer.hasEarmuffs(target)) {
	    	Player targetPlayer = (Player) target;
			int randomSkill = Utils.random(0, 6);
			int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
			targetPlayer.getSkills().set(randomSkill, currentLevel < 5 ? 0 : currentLevel - 5);
			targetPlayer.getPackets().sendGameMessage("The screams of the banshee make you feel slightly weaker.");
			npc.setNextForceTalk(new ForceTalk("*EEEEHHHAHHH*"));
		delayHit(npc, 0, target, getMeleeHit(npc, targetPlayer.getMaxHitpoints()/10));
		//TODO player emote hands on ears
	}else
	    delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
	npc.animate(new Animation(def.getAttackEmote()));
	return def.getAttackDelay();
    }
}
