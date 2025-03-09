/**
 * 
 */
package com.rs.game.world.entity.npc.combat.impl.wilderness;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class CrazyArchaeologistCombat extends CombatScript {

	/** Attack quotes */
	private static final String[] QUOTES = { "I'm Bellock - respect me!", "Get off my site!", "No-one messes with Bellock's dig!", "These ruins are mine!", "Taste my knowledge!", "You belong in a museum!" };
	
	/** Attack animations */
	private static final Animation MELEE = Animation.createOSRS(423);
	private static final Animation RANGED = Animation.createOSRS(2614);
	
	/** Projectiles */
	private static final Projectile BOOKS = new Projectile(Graphics.createOSRS(1260).getId(), 30, 0, 20, 16, 10, 0);
	private static final Projectile RANGE = new Projectile(Graphics.createOSRS(1259).getId(), 36, 32, 20, 16, 20, 0);
	
	/** Hit graphics */
	private static final Graphics BOOKS_END = Graphics.createOSRS(131);
	private static final Graphics RANGE_END = Graphics.createOSRS(140, 0, 150);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		return new Object[] { "Crazy archaeologist" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		int random = Utils.getRandom(10);
		if (target.withinDistance(npc, 1) && random < 3) { //Melee attack
			npc.setNextForceTalk(new ForceTalk(getQuote()));
			npc.animate(MELEE);
			delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, 150, CombatDefinitions.STAB_ATTACK, target), HitLook.MELEE_DAMAGE));
		} else {
			npc.animate(RANGED);
			if (random >= 3) { //Ranged attack
				npc.setNextForceTalk(new ForceTalk(getQuote()));
				delayHit(npc, target, RANGE, new Hit(npc, getRandomMaxHit(npc, 150, CombatDefinitions.RANGE_ATTACK, target), HitLook.RANGE_DAMAGE));
				syncProjectileHit(npc, target, RANGE_END, RANGE);
			} else { //Special attack
				npc.setNextForceTalk(new ForceTalk("Rain of knowledge!"));
				ArrayList<Position> tiles = new ArrayList<Position>();
				int count = 0;
				while (tiles.size() < 3 && count++ < 30) {
					Position tile = new Position(target, 1);
					if (World.getMask(tile.getZ(), tile.getX(), tile.getY()) != 0) //Checks if theres an object like rocks/trees on the tile
						continue;
					tiles.add(tile);
				}
				tiles.forEach((tile) -> {
					World.sendProjectile(npc, tile, BOOKS);
					CoresManager.slowExecutor.schedule(() -> {
						World.sendGraphics(npc, BOOKS_END, tile);
						if (target.withinDistance(tile, 1)) {
							target.applyHit(new Hit(npc, getRandomMaxHit(npc, 240, CombatDefinitions.MAGIC_ATTACK, target), HitLook.REGULAR_DAMAGE));
						}
					}, BOOKS.getHitSyncToMillis(npc, tile) + 1200, TimeUnit.MILLISECONDS);
				});
			}
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Gets a random quote.
	 * @return
	 */
	private final String getQuote() {
		return QUOTES[Utils.random(QUOTES.length)];
	}

}
