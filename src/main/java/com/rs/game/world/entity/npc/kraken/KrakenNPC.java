/**
 * 
 */
package com.rs.game.world.entity.npc.kraken;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;

import lombok.Getter;

/**
 * Handles kraken related things.
 * @author ReverendDread
 * Jul 22, 2018
 */
@SuppressWarnings("serial")
public class KrakenNPC extends NPC {

	/** ID's for states of being disturbed */
	private static final int WHIRLPOOL = 16027, KRAKEN = 16010;
	
	/** The animation for when Kraken is first disturbed */
	private static final Animation DISTURB_ANIM = new Animation(3992);
	
	/** List of Krakens tentacles */
	@Getter private ArrayList<TentacleNPC> tentacles = new ArrayList<TentacleNPC>();
	
	/**
	 * Kraken constructor
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public KrakenNPC(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, false);
		setCantFollowUnderCombat(true);
		setNoDistanceCheck(true);
		setCantInteract(true);
		setAtMultiArea(true);
		addTentacles();
		getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
	}
	
	@Override
	public void processNPC() {
		if (getId() == WHIRLPOOL && ready()) {
			setCantInteract(false); //Cheap workaround for main whirlpool going argo after a hit.
		}
		if (getCombat().getTarget() == null) {
			transformIntoNPC(WHIRLPOOL);
		}
		super.processNPC();
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (ready()) {
			transformIntoNPC(KRAKEN);
			animate(DISTURB_ANIM);
		} else {
			setHitpoints(getMaxHitpoints());
			getCombat().setTarget(null);
			return;
		}
		super.handleIngoingHit(hit);
	}
	
	@Override
	public boolean clipedProjectile(Position pos, boolean checkclose) {
		return true;
	}
	
	/**
	 * Adds tentacles to tentacles list.
	 */
	private final void addTentacles() {
		tentacles.add(new TentacleNPC(this, TentacleNPC.WHIRLPOOL, new Position(getX() - 4, getY() - 2, getZ())));
		tentacles.add(new TentacleNPC(this, TentacleNPC.WHIRLPOOL, new Position(getX() - 4, getY() + 2, getZ())));
		tentacles.add(new TentacleNPC(this, TentacleNPC.WHIRLPOOL, new Position(getX() + 4, getY() - 2, getZ())));
		tentacles.add(new TentacleNPC(this, TentacleNPC.WHIRLPOOL, new Position(getX() + 4, getY() + 2, getZ())));
	}
	
	/**
	 * Checks if Kraken is ready to be disturbable.
	 * @return
	 */
	private final boolean ready() {
		for (TentacleNPC tentacle : tentacles) {
			if (tentacle == null || !tentacle.isDisturbed())
				return false;
		}
		return true;
	}
	
	@Override
	public void finish() {
		transformIntoNPC(WHIRLPOOL);
		tentacles.forEach((e) -> {
			e.finish(); 
		});
		super.finish();
	}
	
	@Override
	public void spawn() {
		super.spawn();
		addTentacles();
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.5;
	}

}
