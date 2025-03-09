package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Slayer;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

public class CockatriceCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
	return new Object[] { 1620, 27393 };
    }
    public static int [] SKILLS = { 1, 2, 4, 5, 6 };
    @Override
    public int attack(NPC npc, final Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		if (!Slayer.hasReflectiveEquipment(target)) {
		    Player targetPlayer = (Player) target;
		    int randomSkill = Misc.random(SKILLS.length);
		    int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
		    targetPlayer.getSkills().set(randomSkill, currentLevel < 3 ? 0 : currentLevel / 4);
		    delayHit(npc, 1, target, getMagicHit(npc, targetPlayer.getMaxHitpoints() / 10));
		    npc.animate(new Animation(7766));
		    npc.setNextGraphics(new Graphics(1467));
		    World.sendProjectile(npc, target, 1468, 34, 16, 30, 35, 16, 0);
		} else
		    delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, npc.getMaxHit(), def.getAttackStyle(), target)));
		npc.animate(new Animation(def.getAttackEmote()));
		return def.getAttackDelay();
    }
}
