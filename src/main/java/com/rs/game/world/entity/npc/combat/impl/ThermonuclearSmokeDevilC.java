/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
public class ThermonuclearSmokeDevilC extends CombatScript {

	private static final Graphics SMOKE = new Graphics(1470);

	@Override
	public Object[] getKeys() {
		return new Object[] { NPC.asOSRS(499) };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions definition = npc.getCombatDefinitions();	
		npc.animate(new Animation(definition.getAttackEmote()));
		if (target instanceof Player) {
			Player player = target.getAsPlayer();
			drainStats(player);
			delayHit(npc, 1, target, new Hit(npc, 
					CombatScript.getRandomMaxHit(npc, definition.getMaxHit(), NPCCombatDefinitions.MAGE, target), HitLook.RANGE_DAMAGE));
			World.sendProjectile(npc, target, SMOKE.getId(), 37, 20, 40, 5, 16, 0);
			CoresManager.slowExecutor.schedule(() -> {
				player.setNextGraphics(SMOKE);
			}, 600, TimeUnit.MILLISECONDS); 
		}
		return definition.getAttackDelay();
	}
	
	private final void drainStats(final Player player) {
		if (!player.getEquipment().wearingFaceMask()) {
			player.getSkills().drainLevel(Skills.ATTACK, Utils.random(3));	
			player.getSkills().drainLevel(Skills.STRENGTH, Utils.random(3));	
			player.getSkills().drainLevel(Skills.DEFENCE, Utils.random(3));	
			player.getSkills().drainLevel(Skills.RANGE, Utils.random(3));	
			player.getSkills().drainLevel(Skills.MAGIC, Utils.random(3));	
		}
	}

}
