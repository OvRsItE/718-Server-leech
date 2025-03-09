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
public class ChaosFanaticCombat extends CombatScript {

	/** Quotes */
    private static final String[] QUOTES = { "Burn!", "WEUGH!", "Develish Oxen Roll!",
            "All your wilderness are belong to them!", "AhehHeheuhHhahueHuUEehEahAH",
            "I shall call him squidgy and he shall be my squidgy!" };
	
	/** Projectiles */
	private static final Projectile MAGIC = new Projectile(Graphics.createOSRS(1044).getId(), 30, 32, 20, 16, 20, 0);
	private static final Projectile SPECIAL = new Projectile(Graphics.createOSRS(1045).getId(), 36, 0, 20, 16, 10, 0);
    
	/** Splashing */
	private static final Graphics SPLASH = Graphics.createOSRS(140);
	
	/** Attack anim */
	private static final Animation ATTACK = Animation.createOSRS(1979);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#getKeys()
	 */
	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] { "Chaos Fanatic" };
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.combat.CombatScript#attack(com.rs.game.world.entity.npc.NPC, com.rs.game.world.entity.Entity)
	 */
	@Override
	public int attack(NPC npc, Entity target) {		
		int random = Utils.getRandom(10);
		npc.setNextForceTalk(new ForceTalk(getQuote()));
		npc.animate(ATTACK);
		if (random >= 3) {
			delayHit(npc, target, MAGIC, new Hit(npc, getRandomMaxHit(npc, 310, CombatDefinitions.MAGIC_ATTACK, target), HitLook.MAGIC_DAMAGE));
			syncProjectileHit(npc, target, Graphics.createOSRS(131, 0, 150), MAGIC);
		} else { //Special attack
			ArrayList<Position> tiles = new ArrayList<Position>();
			int count = 0;
			while (tiles.size() < 3 && count++ < 30) {
				Position tile = new Position(target, 1);
				if (World.getMask(tile.getZ(), tile.getX(), tile.getY()) != 0) //Checks if theres an object like rocks/trees on the tile
					continue;
				tiles.add(tile);
			}
			tiles.forEach((tile) -> {
				World.sendProjectile(npc, tile, SPECIAL);
				CoresManager.slowExecutor.schedule(() -> {
					World.sendGraphics(npc, SPLASH, tile);
					if (target.withinDistance(tile, 1)) {
						target.applyHit(new Hit(npc, getRandomMaxHit(npc, 310, CombatDefinitions.MAGIC_ATTACK, target), HitLook.REGULAR_DAMAGE));
					}
				}, SPECIAL.getHitSyncToMillis(npc, tile) + 1200, TimeUnit.MILLISECONDS);
			});
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Gets a random quote
	 * @return
	 */
	private final String getQuote() {
		return QUOTES[Utils.random(QUOTES.length)];
	}

}
