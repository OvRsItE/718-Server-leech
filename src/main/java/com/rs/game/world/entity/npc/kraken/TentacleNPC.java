/**
 * 
 */
package com.rs.game.world.entity.npc.kraken;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;

import lombok.Getter;

/**
 * @author ReverendDread
 * Jul 22, 2018
 */
@SuppressWarnings("serial")
public class TentacleNPC extends NPC {

	/**
	 * ID's for states of being disturbed.
	 */
	public static final int WHIRLPOOL = 16028, TENTACLE = 16013;
	
	/**
	 * The animation when a tentacle is first disturbed.
	 */
	private static final Animation DISTURB_ANIM = new Animation(3615);
	
	/**
	 * The owner of this tentacle.
	 */
	@Getter private KrakenNPC owner;
	
	/**
	 * If the tentacle has been disturbed or not.
	 */
	@Getter private boolean disturbed;
	
	/**
	 * Tentacles constructor
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public TentacleNPC(KrakenNPC owner, int id, Position tile) {
		super(id, tile, owner.getMapAreaNameHash(), owner.canBeAttackFromOutOfArea());
		this.owner = owner;
		setForceMultiArea(true);
		setCantFollowUnderCombat(true);
		setAtMultiArea(true);
	}
	
	@Override
	public boolean clipedProjectile(Position pos, boolean checkclose) {
		return true;
	}
	
	@Override
	public void processNPC() {
		if (getCombat().getTarget() == null) {
			transformIntoNPC(WHIRLPOOL);
			disturbed = false;
		}
		super.processNPC();
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (getId() == WHIRLPOOL) {
			animate(DISTURB_ANIM);
			transformIntoNPC(TENTACLE);
			disturbed = true;
		}
		super.handleIngoingHit(hit);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.1;
	}

}
