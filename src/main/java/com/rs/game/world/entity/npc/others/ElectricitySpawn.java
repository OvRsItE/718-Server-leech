/**
 * 
 */
package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Represents an electricity spawn for rune dragons.
 * @author ReverendDread
 * Dec 5, 2018
 */
public class ElectricitySpawn extends NPC {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 8555326457266518725L;

	/** The {@code Player} target */
	private transient final Player target;
	
	/** Insulated boots id */
	private static final int INSULATED_BOOTS = 7159;
	
	/**
	 * Constructor
	 * @param id
	 * 			the id.
	 * @param tile
	 * 			the spawn tile.
	 * @param target
	 * 			the target.
	 */
	public ElectricitySpawn(int id, Position tile, Player target) {
		super(id, tile, -1, 0, true, true);
		this.target = target;
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		if (target.withinDistance(this, 1)) {
			boolean insulated = target.getEquipment().getBootsId() == INSULATED_BOOTS;
			int min = insulated ? 10 : 40;
			int max = insulated ? 20 : 70;
			target.applyHit(new Hit(this, Utils.random(min, max), HitLook.REGULAR_DAMAGE));
		}
		if (!hasWalkSteps()) {
			finish();
		}
	}

}
