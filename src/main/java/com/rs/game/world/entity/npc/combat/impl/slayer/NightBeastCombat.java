/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.slayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 14, 2018
 */
public class NightBeastCombat extends CombatScript {

	private static final int PROJECTILE = 2729;
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] { "Night Beast" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {	
		int attack = Utils.random(100);	
		npc.animate(npc.getCombatDefinitions().getAttackEmote());
		if (attack >= 25) {
			melee_attack(npc, target.getAsPlayer());
		} else {
			special_attack(npc, target.getAsPlayer());
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}

	/**
	 * Handles the normal melee attack.
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 */
	private void melee_attack(NPC npc, Player target) {
		delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), 
				NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
	}
	
	/**
	 * Handles the magic special attack.
	 * @param npc
	 * 			the npc.
	 * @param target
	 * 			the target.
	 */
	private void special_attack(NPC npc, Player target) {
		ArrayList<Position> tiles = new ArrayList<Position>();
		tiles.addAll(Arrays.asList(Utils.getAdjacentTiles(target)));
		tiles.forEach((tile) -> {
			World.sendProjectile(npc, tile, PROJECTILE, 0, 0, 50, 30, 0, 16);
			CoresManager.slowExecutor.schedule(() -> {
				if (target.getPosition().matches(tile)) {
					int damage = (int) (target.getHitpoints() * 0.20);
					if (damage > target.getHitpoints())
						return;
					target.applyHit(new Hit(npc, (int) (target.getHitpoints() * 0.20), HitLook.MAGIC_DAMAGE));
				}
				World.sendGraphics(npc, new Graphics(2737), tile);
			}, 600, TimeUnit.MILLISECONDS);
		});
	}

}
