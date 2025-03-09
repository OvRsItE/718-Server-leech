/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class ZombifiedSpawn extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2912782389765387554L;

	private Entity target;
	
	private boolean exploded;
	
	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public ZombifiedSpawn(int id, Position tile, Entity target) {
		super(id, tile, -1, 0, true, true);
		this.target = target;
		setForceMultiArea(true);
		setFollowing(target);
		setCanBeFrozen(false);
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		if (withinDistance(target, 1) && !exploded) {
			target.applyHit(new Hit(this, Utils.random(220 + getHitpoints()), HitLook.REGULAR_DAMAGE));
			setNextGraphics(Graphics.createOSRS(1460));
			sendDeath(getFollowing());
		}
	}
	
	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		exploded = true;
		target.setFreezeDelay(0);
	}
	
	public boolean hasExploded() {
		return exploded;
	}

}
