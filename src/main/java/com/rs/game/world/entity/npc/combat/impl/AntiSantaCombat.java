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
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class AntiSantaCombat extends CombatScript {
	
	private static final Projectile BOMB_PROJ = new Projectile(Graphics.createOSRS(1453).getId(), 70, 0, 27, 16, 30, 0);
	private static final Graphics BOMB_PROJ_SPLASH = Graphics.createOSRS(1454);
	
	private static final Projectile MAGIC_PROJ = new Projectile(6479, 70, 32, 27, 16, 30, 0);
	private static final Graphics MAGIC_PROJ_SPLASH = Graphics.createOSRS(1480, 0, 92);
	
	private static final Projectile ACID_PROJ = new Projectile(6470, 70, 32, 27, 16, 30, 0);
	private static final Graphics ACID_PROJ_SPLASH = Graphics.createOSRS(1472, 0, 92);
	
	private ArrayList<Position> BOMBS = new ArrayList<Position>();
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 16099 };
	}

	@Override
	public int attack(final NPC npc, Entity target) {
		int attack = Utils.random(6);
		if (attack == 0) {
			npc.animate(10503);
			for (Player player : World.getPlayers()) {
				if (player == null || !player.withinDistance(npc, 16) || player.isDead() || player.isFinished())
					continue;
				final Position location = player.getPosition();
				World.sendProjectile(npc.getMiddleTile(), location, BOMB_PROJ);
				BOMBS.add(location);
				CoresManager.slowExecutor.schedule(() -> {
					for (Position tile : BOMBS) {
						if (player.withinDistance(tile, 1)) {
							player.applyHit(new Hit(npc, 350, HitLook.REGULAR_DAMAGE));
						}
						World.sendGraphics(npc, BOMB_PROJ_SPLASH, tile);
						BOMBS.remove(tile);
					}
				}, BOMB_PROJ.getHitSyncToMillis(npc.getMiddleTile(), location), TimeUnit.MILLISECONDS);
			}
		} else if (attack == 1 || attack == 2 || attack == 3) {
			npc.animate(1979);
			for (Player player : World.getPlayers()) {
				if (player == null || !player.withinDistance(npc, 16) || player.isDead() || player.isFinished())
					continue;
				World.sendProjectile(npc, player, MAGIC_PROJ);
				CoresManager.slowExecutor.schedule(() -> {
					player.applyHit(new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target), HitLook.MAGIC_DAMAGE));
					player.setNextGraphics(MAGIC_PROJ_SPLASH);
				}, MAGIC_PROJ.getHitSyncToMillis(npc.getMiddleTile(), player), TimeUnit.MILLISECONDS);
			}
		} else if (attack == 4 || attack == 5 || attack == 6) {
			npc.animate(10504);
			for (Player player : World.getPlayers()) {
				if (player == null || !player.withinDistance(npc, 16) || player.isDead() || player.isFinished())
					continue;
				World.sendProjectile(npc, player, ACID_PROJ);
				CoresManager.slowExecutor.schedule(() -> {
					player.applyHit(new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.RANGE, target), HitLook.RANGE_DAMAGE));
					player.setNextGraphics(ACID_PROJ_SPLASH);
				}, ACID_PROJ.getHitSyncToMillis(npc.getMiddleTile(), player), TimeUnit.MILLISECONDS);
			}
		}
		return 6;
	}

}