/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.world.Projectile;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
public class SmokeDevilCombat extends CombatScript {

	private static final Projectile PROJ = new Projectile(1470, 36, 32, 40, 20, 20, 0);
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 16021, 27406, 16142 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions definition = npc.getCombatDefinitions();
		if (target instanceof Player) {
			Player player = target.getAsPlayer();
			npc.animate(definition.getAttackEmote());
			drainStats(player);
			delayHit(npc, target, PROJ, new Hit(npc, getRandomMaxHit(npc, definition.getMaxHit(),
						NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
			CoresManager.slowExecutor.schedule(() -> {
				player.setNextGraphics(1470);
			}, PROJ.getHitSyncToMillis(npc, target), TimeUnit.MILLISECONDS);
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
