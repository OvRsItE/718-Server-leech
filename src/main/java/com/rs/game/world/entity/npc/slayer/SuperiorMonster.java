/**
 * 
 */
package com.rs.game.world.entity.npc.slayer;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

import lombok.Getter;

/**
 * Represents a superior slayer monster.
 * @author ReverendDread
 * Sep 14, 2018
 */
public class SuperiorMonster extends NPC {

	private long spawnTimer; //The time when the creature was spawned.
	
	@Getter private Player owner; //The owner if the creature.
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 8741933624165805875L;

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public SuperiorMonster(Player owner, int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		this.owner = owner;
		this.spawnTimer = System.currentTimeMillis();
		setForceMultiArea(true);
		setAtMultiArea(true);
	}
	
	@Override
	public void processNPC() {
		if (owner.isDead() || owner.isFinished() || isDead() || isFinished())
			finish();
		if ((spawnTimer + 120_000 <= System.currentTimeMillis()) && getCombat().getTarget() == null)
			finish();
		super.processNPC();
	}

}
