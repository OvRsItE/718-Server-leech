/**
 * 
 */
package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Represents a demonic gorilla.
 * @author ReverendDread
 * Nov 26, 2018
 */
public class DemonicGorilla extends NPC {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 6705671686779364332L;
	
	/** Style switching anim */
	private static final Animation OVERHEAD_SWITCH = Animation.createOSRS(7228);
	
	/** The ids of each prayer type */
	private static final int MELEE_ID = 27147, RANGED_ID = 27148, MAGIC_ID = 27149;
	
	/** Damage received value */
	private int received;
	
	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public DemonicGorilla(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if ((hit.getLook() == HitLook.MELEE_DAMAGE && getId() == MELEE_ID)
				|| (hit.getLook() == HitLook.MAGIC_DAMAGE && getId() == MAGIC_ID)
				|| (hit.getLook() == HitLook.RANGE_DAMAGE && getId() == RANGED_ID)) {
			hit.setDamage(0);
		}
		received += hit.getDamage();
		if (received > 500) {
			switchOverhead();
			received = 0;
		}
	}

	/**
	 * Handles switching prayer overheads.
	 * @param npc
	 * @param id
	 */
	public void switchOverhead() {	
		animate(OVERHEAD_SWITCH);
		setNextNPCTransformation(getRandomId());	
	}
	
	/**
	 * Gets a random next id, excludes existing id.
	 * @return
	 */
	public int getRandomId() {
		int ids[] = new int[] { MELEE_ID, MAGIC_ID, RANGED_ID };
		int id = ids[Utils.random(ids.length)];
		if (id != getId())
			return id;
		else 
			return getRandomId();
	}
	
}
