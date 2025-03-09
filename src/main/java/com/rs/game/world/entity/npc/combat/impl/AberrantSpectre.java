package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

public class AberrantSpectre extends CombatScript {

    @Override
    public Object[] getKeys() {
	return new Object[] { "Aberrant spectre", 27402, "Deviant spectre"};
    }
	public static int [] SKILLS = { 1, 2, 4, 5, 6 };

    @Override
    public int attack(NPC npc, Entity target) {
	NPCCombatDefinitions def = npc.getCombatDefinitions();
	if (!Slayer.hasNosepeg(target)) {
	    Player targetPlayer = (Player) target;
		int randomSkill = Misc.random(SKILLS.length);
		int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
		targetPlayer.getSkills().set(randomSkill, currentLevel < 5 ? 0 : currentLevel - 5);
		targetPlayer.getPackets().sendGameMessage("The smell of the abberrant spectre make you feel slightly weaker.");
	    delayHit(npc, 1, target, getMagicHit(npc, targetPlayer.getMaxHitpoints() / 10));
	    // TODO player emote hands on ears
	} else
	    delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
	World.sendProjectile(npc, target, def.getAttackProjectile(), 18, 18, 50, 25, 0, 0);
	npc.animate(new Animation(def.getAttackEmote()));
	return def.getAttackDelay();
    }
}
