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
import com.rs.game.world.entity.npc.others.ElectricitySpawn;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Aug 6, 2018
 */
public class RuneDragonCombat extends CombatScript {
	
	/** Electricity cooldown */
	private static long cooldown;
	
	/** The ranged dragonfire attack animation */
	private static final Animation RANGED_ATTACK = Animation.createOSRS(81);
	
	/** The close dragonfire attack animation */
	private static final Animation CLOSE_FIRE = Animation.createOSRS(81);
	
	/** Melee attack animation */
	private static final Animation MELEE_ATTACK = Animation.createOSRS(25);
	
	/** Fire projectile */
	private static final Projectile FIRE = new Projectile(Graphics.createOSRS(54).getId(), 28, 16, 50, 10, 35, 0);
	
	/** Bolt projectile */
	private static final Projectile BOLT = new Projectile(27, 28, 16, 50, 10, 35, 0);
	
	/** Electricity projectile */
	private static final Projectile ELECTRICITY = new Projectile(Graphics.createOSRS(1488).getId(), 28, 0, 50, 10, 35, 0);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] { "Rune dragon" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		Player player = target.getAsPlayer();
		int attack = Utils.getRandom(6);
		if (player.withinDistance(npc.getMiddleTile(), 3)) {
			if (attack >= 0 && attack <= 2) {
				dragonfire_attack(npc, player, false);
			} else if (attack >= 3 && attack <= 5) {
				melee_attack(npc, player);
			} else if (!onCooldown()) {
				electric_attack(npc, player);
			}
		} else if (attack <= 3) {
			dragonfire_attack(npc, player, true);
		} else if (attack >= 4 && attack <= 5) {
			onyx_bolt_attack(npc, player);
		} else if (attack == 6) {
			electric_attack(npc, player);
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Handles onyx bolt attack.
	 * @param npc
	 * 			the npc.
	 * @param player
	 * 			the player.
	 */
	private static void onyx_bolt_attack(NPC npc, Player player) {
		npc.animate(RANGED_ATTACK);
		World.sendProjectile(npc, player, BOLT);
		int damage = getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.RANGE, player);
		delayHit(npc, BOLT.getHitSync(npc, player) + 1, player, new Hit(npc, damage, HitLook.RANGE_DAMAGE));
		npc.heal(damage);
	}
	
	/**
	 * Handles the dragonfire attack.
	 * @param npc
	 * @param player
	 */
	private static void dragonfire_attack(NPC npc, Player player, boolean ranged) {
		int damage = getMaxDamage(player, 0);
		npc.animate(ranged ? RANGED_ATTACK : CLOSE_FIRE);
		if (ranged)
			World.sendProjectile(npc, player, FIRE);
		else 
			npc.setNextGraphics(2465);
		delayHit(npc, FIRE.getHitSync(npc, player) + 1, player, new Hit(npc, Utils.getRandom(damage), HitLook.REGULAR_DAMAGE));
		String protect_message = Combat.getProtectMessage(player);
		if (protect_message != null)
			player.getPackets().sendGameMessage(protect_message, true);
	}

	/**
	 * Handles the melee attack.
	 * @param npc
	 * @param player
	 */
	private static void melee_attack(NPC npc, Player player) {
		npc.animate(MELEE_ATTACK);
		delayHit(npc, 1, player, new Hit(npc, getRandomMaxHit(npc, 
				npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MELEE, player), HitLook.MELEE_DAMAGE));
	}
	
	/**
	 * Handles the electric bolt attack.
	 * @param npc
	 * 			the npc.
	 * @param player
	 * 			the target.
	 */
	private static void electric_attack(NPC npc, Player player) {	
		
		cooldown = System.currentTimeMillis();
		
		npc.animate(RANGED_ATTACK);
		
		Position[] tiles = getFreeTiles(player);
		
		World.sendProjectile(npc.getMiddleTile(), tiles[0], ELECTRICITY);
		World.sendProjectile(npc.getMiddleTile(), tiles[1], ELECTRICITY);
		
		final Position destination = player.getPosition();
		//First electricity spawn walking & spawning
		CoresManager.slowExecutor.schedule(() -> {
			ElectricitySpawn spawn = new ElectricitySpawn(28032, tiles[0], player);
			spawn.addWalkSteps(destination.getX(), destination.getY(), 5, true);
		}, ELECTRICITY.getHitSyncToMillis(npc.getMiddleTile(), tiles[0]) + 600, TimeUnit.MILLISECONDS);	
		//Second electricity spawn walking & spawning
		CoresManager.slowExecutor.schedule(() -> {
			ElectricitySpawn spawn = new ElectricitySpawn(28032, tiles[1], player);
			spawn.addWalkSteps(destination.getX(), destination.getY(), 5, true);
		}, ELECTRICITY.getHitSyncToMillis(npc.getMiddleTile(), tiles[1]) + 600, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Gets two free tiles around the player, that arn't obstructed by objects.
	 * @param player
	 * 			the player.
	 * @return
	 * 			the tiles.
	 */
	public static final Position[] getFreeTiles(Player player) {
		ArrayList<Position> tiles = new ArrayList<Position>();
		while (tiles.size() < 2) {
			Position tile = new Position(player, 3, 5);
			if (World.getMask(player.getZ(), tile.getX(), tile.getY()) != 0)
				continue;
			tiles.add(tile);
		}
		return tiles.toArray(new Position[tiles.size()]);
	}

	/**
	 * Checks electricity cooldown.
	 * @return
	 */
	public boolean onCooldown() {
		long time = System.currentTimeMillis();
		return ((cooldown + TimeUnit.MILLISECONDS.toSeconds(10)) - time) >= time;
	}

	private static int getMaxDamage(Player target, int minDamage) {
		return (int) Math.max(50 * (1 - Combat.getDragonfireResistance(target)), minDamage);
	}
	
}
