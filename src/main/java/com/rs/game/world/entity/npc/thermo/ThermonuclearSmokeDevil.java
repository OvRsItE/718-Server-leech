/**
 * 
 */
package com.rs.game.world.entity.npc.thermo;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
@SuppressWarnings("serial")
public class ThermonuclearSmokeDevil extends NPC {

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public ThermonuclearSmokeDevil(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getLook() == HitLook.RANGE_DAMAGE)
			hit.setDamage(0);
		super.handleIngoingHit(hit);
	}
	
	@Override
	public double getRangePrayerMultiplier() {
		return 1;
	}

}
