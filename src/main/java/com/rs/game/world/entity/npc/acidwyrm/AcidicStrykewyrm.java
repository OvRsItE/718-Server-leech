package com.rs.game.world.entity.npc.acidwyrm;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Animation;

@SuppressWarnings("serial")
public class AcidicStrykewyrm extends NPC {

	private static final int SPAWN_EMOTE = 12795;

	/**
	 * {@code ArrayList} containing spawned wyrmlings.
	 */
	private ArrayList<Wyrmling> wyrmlings = new ArrayList<Wyrmling>();

	public AcidicStrykewyrm(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, false);
		setAtMultiArea(true);
		animate(new Animation(SPAWN_EMOTE));
		setFindTargetDelay(3000);
	
	}

	@Override
	public void finish() {
		clear_minions();
		super.finish();
	}

	/**
	 * Kills all of the minions spawn by this strykewyrm
	 */
	private void clear_minions() {
		for (Wyrmling wyrm : wyrmlings) {
			wyrm.sendDeath(null);
		}
	}

	/**
	 * Adds a {@code Wyrmling} to spawned wyrmlings.
	 * @param wyrm
	 * 			the spawned wyrmling
	 */
	public Wyrmling addWyrmling(Wyrmling wyrm) {
		wyrmlings.add(wyrm);
		return wyrm;
	}

}
