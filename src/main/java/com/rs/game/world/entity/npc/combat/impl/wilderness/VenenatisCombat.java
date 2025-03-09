package com.rs.game.world.entity.npc.combat.impl.wilderness;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Apr 24, 2018
 */
public class VenenatisCombat extends CombatScript {

	//171, 172 prayer drain gfx
	//2721, 2726 magic gfx
	
	private static final int ATTACK = Animation.createOSRS(5319).getIds()[0];
	
	@Override
	public Object[] getKeys() {
		return new Object[] { "Venenatis" };
	}

	@Override
	public int attack(NPC npc, Entity target) {	
		int random = Utils.random(6);
		if (random == 0)  //prayer drain
			prayer_drain(npc, target);
		if (target.withinDistance(npc, 1)) {
			return melee_combo_attack(npc, target);		
		} else {
			return magic_combo_attack(npc, target);
		}
	}
	
	/**
	 * Venenatis melee attack with combo conditional.
	 * @param npc
	 * @param target
	 * @return
	 */
	public int melee_combo_attack(NPC npc, Entity target) {
		boolean magic_protect = target.getAsPlayer().getPrayer().usingPrayer(0, 17);
		int random = Utils.random(0, 15);
		npc.animate(new Animation(ATTACK));
		if (!magic_protect && random == 0) { //web attack
			World.sendProjectile(npc, target, 2721, 35, 36, 40, 1, 16, 0);
			delayHit(npc, 2, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
					NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
		}
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Venenatis magic attack with combo conditional.
	 * @param npc
	 * @param target
	 * @return
	 */
	public int magic_combo_attack(NPC npc, Entity target) {
		boolean magic_protect = target.getAsPlayer().getPrayer().usingPrayer(0, 17);
		int random = Utils.random(0, 15);
		npc.animate(new Animation(ATTACK));
		if (!magic_protect && random == 0) { //web attack
			World.sendProjectile(npc, target, 2721, 35, 36, 40, 1, 16, 0);
			delayHit(npc, 2, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
					NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
		}
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
		World.sendProjectile(npc, target, Graphics.createOSRS(1462).getId(), 35, 36, 40, 1, 16, 0);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.setNextGraphics(new Graphics(2726, 0, 150));
			}
			
		});
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Venenatis prayer drain attack.
	 * @param npc
	 * @param target
	 * @return
	 */
	public void prayer_drain(NPC npc, Entity target) {
		World.sendProjectile(npc, target, 171, 35, 36, 20, 2, 16, 0);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				int damage = getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target) + Utils.random(10, 300);
				target.getAsPlayer().getPrayer().drainPrayer((int) Math.floor(damage * 0.35));
				delayHit(npc, 0, target, new Hit(npc, damage, HitLook.MAGIC_DAMAGE));
				target.setNextGraphics(new Graphics(170, 0, 150));
				target.getAsPlayer().getPackets().sendGameMessage("Some of your prayer has been drained by Venenatis's attack!", true);
			}
			
		}, 2);
	}

}
