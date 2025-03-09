/**
 * 
 */
package com.rs.game.world.entity.npc.wilderness;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Hit;

import lombok.Getter;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
@SuppressWarnings("serial")
public class Scorpia extends NPC {

	private boolean spawnedGuardians;
	
	/** List of guardians spawned by scorpia */
	@Getter private ArrayList<ScorpiaGuardian> guardians = new ArrayList<ScorpiaGuardian>();
	
	/**
	 * Scorpia's constructor.
	 */
	public Scorpia(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setForceMultiArea(true);
		setAtMultiArea(true);
		setForceFollowClose(true);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		super.handleIngoingHit(hit);
		if (!spawnedGuardians && getHitpoints() < (getMaxHitpoints() * 0.5))
			spawnGuardians();
	}
	
	@Override
	public void finish() {
		guardians.forEach((e) -> {
			e.finish();
		});
		super.finish();
	}
	
	private final void spawnGuardians() {
		int spawned = 0;
		while (spawned < 2) {
			Position loc = new Position(this, 3);
			if (!World.canMoveNPC(loc.getZ(), loc.getX(), loc.getY(), 1))
				continue;
			guardians.add(new ScorpiaGuardian(this, loc));
			spawned++;
		}
		spawnedGuardians = true;
	}

}
