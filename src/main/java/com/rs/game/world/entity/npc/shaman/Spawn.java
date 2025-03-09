/**
 * 
 */
package com.rs.game.world.entity.npc.shaman;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Oct 6, 2018
 */
public class Spawn extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1360367514653575030L;

	/**
	 * 
	 */
	private LizardmanShaman owner;
	
	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param faceDirection
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public Spawn(LizardmanShaman shaman, int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, true);
	}
	
	/**
	 * Handles suicide bomb.
	 */
	public void init() {
		CoresManager.slowExecutor.schedule(() -> {
			if (getFollowing() != null && getFollowing().withinDistance(this, 2)) {
				getFollowing().applyHit(new Hit(this, Utils.random(50, 100), HitLook.REGULAR_DAMAGE));
			}
			sendDeath(this);
		}, Utils.random(3, 5), TimeUnit.SECONDS);
	}
	
	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		setNextGraphics(Graphics.createOSRS(1295, 0, 30));
		owner.getMinions().remove(this);
	}

}
