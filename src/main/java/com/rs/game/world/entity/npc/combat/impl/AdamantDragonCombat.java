/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Handles adamant dragon combat.
 * @author ReverendDread
 * Aug 6, 2018
 */
public class AdamantDragonCombat extends CombatScript {

	/** The ranged dragonfire attack animation */
	private static final Animation RANGED_ATTACK = Animation.createOSRS(81);
	
	/** The close dragonfire attack animation */
	private static final Animation CLOSE_FIRE = Animation.createOSRS(81);
	
	/** Melee attack animation */
	private static final Animation MELEE_ATTACK = Animation.createOSRS(25);
	
	/** Fire projectile */
	private static final Projectile FIRE = new Projectile(Graphics.createOSRS(54).getId(), 28, 16, 50, 10, 35, 0);
	
	/** Poison projectile */
	private static final Projectile POISON = new Projectile(Graphics.createOSRS(1486).getId(), 28, 16, 50, 10, 35, 0);
	
	/** Bolt projectile */
	private static final Projectile BOLT = new Projectile(27, 28, 16, 50, 10, 35, 0);
	
	@Override
	public Object[] getKeys() {
		return new Object[] { "Adamant dragon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions definition = npc.getCombatDefinitions();
		Player player = target.getAsPlayer();
		int attack = npc.withinDistance(player, 2) ? Utils.random(2) == 1 ? 0 : 10 : Utils.random(1, 10);
		if (attack == 0) { //Up close
			boolean fireAttack = Utils.random(2) == 1;
			if (fireAttack) { //Dragonfire
				return dragonfire(npc, player, true);
			} else { //Melee
				if (Utils.random(2) == 1) {
					npc.animate(MELEE_ATTACK);
					delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, definition.getMaxHit(), NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
				} else {
					return boltAttack(npc, player);
				}
			}
		} else if (attack >= 1 && attack <= 3) { //Ranged dragonfire
			return dragonfire(npc, player, false);
		} else if (attack == 4) { //Bolt attack
			return rubyBoltAttack(npc, player);
		} else if (attack >= 5 && attack <= 6) { //Poison attack
			return poisonAttack(npc, player);
		} else if (attack >= 7) {
			boltAttack(npc, player);
		}
 		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Handles dragonfire attack.
	 * @param npc
	 * 			the npc.
	 * @param player
	 * 			the target.
	 * @param close
	 * 			if dragonfire is in melee distance.
	 * @return
	 * 			next attack delay.
	 */
	private static final int dragonfire(NPC npc, Player player, boolean close) {
		npc.animate(close ? CLOSE_FIRE : RANGED_ATTACK);
		if (close)
			npc.setNextGraphics(2465);
		int damage = getMaxDamage(player, 0);
		if (!close) {
			World.sendProjectile(npc, player, FIRE);
		}
		delayHit(npc, FIRE.getHitSync(npc, player), player, new Hit(npc, Utils.getRandom(damage), HitLook.REGULAR_DAMAGE));
		String protect_message = Combat.getProtectMessage(player);
		if (protect_message != null)
			player.getPackets().sendGameMessage(protect_message, true);
		return 6;
	}
	
	/**
	 * Handles regular bolt attack.
	 * @param npc
	 * 			the npc.
	 * @param player
	 * 			the player.
	 * @return
	 * 			next attack delay.
	 */
	private static final int boltAttack(NPC npc, Player player) {
		npc.animate(RANGED_ATTACK);
		int damage = getRandomMaxHit(npc, (int) npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.RANGE, player);
		World.sendProjectile(npc, player, BOLT);
		delayHit(npc, BOLT.getHitSync(npc, player) + 1, player, new Hit(npc, damage, HitLook.RANGE_DAMAGE));
		return 6;
	}
	
	/**
	 * Handles ruby bolt attack,
	 * @param npc
	 * 			the npc.
	 * @param player
	 * 			the player.
	 * @return
	 * 			next attack delay.
	 */
	private static final int rubyBoltAttack(NPC npc, Player player) {
		npc.animate(RANGED_ATTACK);
		int damage = (int) (player.getHitpoints() * .2);
		World.sendProjectile(npc, player, BOLT);
		CoresManager.slowExecutor.schedule(() -> {
			player.applyHit(new Hit(npc, damage, HitLook.REGULAR_DAMAGE));
			player.setNextGraphics(754);
		}, BOLT.getHitSyncToMillis(npc, player) + 600, TimeUnit.MILLISECONDS);
		npc.applyHit(new Hit(npc, npc.getHitpoints() > 20 ? (int) (npc.getHitpoints() * 0.1) : 1, HitLook.REGULAR_DAMAGE));
		return 6;
	}
	
	/**
	 * Handles poison attack.
	 * @param npc
	 * 			the npc.
	 * @param player
	 * 			the target.
	 * @return
	 * 			next attack delay.
	 */
	private static final int poisonAttack(NPC npc, Player player) {
		npc.animate(RANGED_ATTACK);
		Position targetTile = player.getPosition();
		World.sendProjectile(npc, targetTile, POISON);
		boolean hasVenomImmune = player.getToxin().isImmune(ToxinType.VENOM);
		CoresManager.slowExecutor.schedule(() -> {
			World.sendGraphics(npc, Graphics.createOSRS(1487), targetTile);
			if (player.withinDistance(targetTile, 1)) {
				player.getToxin().applyToxin(ToxinType.VENOM, 75, 250, 25, true);			
				int damage = Utils.random(hasVenomImmune ? 25 : 75, hasVenomImmune ? 100 : 225);
				player.applyHit(new Hit(npc, damage, HitLook.POISON_DAMAGE));
			}
			ArrayList<Position> tiles = new ArrayList<Position>();
			int tries = 0;
			while (tiles.size() < 2 && tries < 50) {
				Position tile = new Position(targetTile, 2);
				if (World.getMask(tile.getZ(), tile.getX(), tile.getY()) != 0)
					continue;
				tiles.add(tile);
			}
			tiles.forEach((tile) -> {
				World.sendProjectile(targetTile, tile, POISON);
				CoresManager.slowExecutor.schedule(() -> {
					World.sendGraphics(npc, Graphics.createOSRS(1487), tile);
					if (player.withinDistance(targetTile, 1)) {
						player.getToxin().applyToxin(ToxinType.VENOM, 75, 250, 25, true);			
						int damage = Utils.random(hasVenomImmune ? 25 : 75, hasVenomImmune ? 100 : 225);
						player.applyHit(new Hit(npc, damage, HitLook.POISON_DAMAGE));
					}
				}, POISON.getHitSyncToMillis(targetTile, tile) + 600, TimeUnit.MILLISECONDS);
				
			});
		}, POISON.getHitSyncToMillis(npc, player) + 600, TimeUnit.MILLISECONDS);
		return 6;
	}

	private static int getMaxDamage(Player target, int minDamage) {
		return (int) Math.max(500 * (1 - Combat.getDragonfireResistance(target)), minDamage);
	}
	
}
