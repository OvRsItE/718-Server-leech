/**
 * 
 */
package com.rs.game.world.entity.npc.slayer;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.instances.hydra.HydraController;
import com.rs.game.world.entity.player.Player;
import lombok.Getter;

/**
 * Represents a superior slayer monster.
 * @author ReverendDread
 * Sep 14, 2018
 */
public class SuperiorBosses extends NPC {


	private long spawnTimer; //The time when the creature was spawned.
	@Getter private Player owner; //The owner if the creature.



	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 7604249980291847916L;

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public SuperiorBosses(Player owner, int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		this.owner = owner;
		this.spawnTimer = System.currentTimeMillis();
	}
	
	@Override
	public void processNPC() {
		if (owner.isDead() || owner.isFinished() || isDead() || isFinished())
			sendDeath(getAsNPC());
		if ((spawnTimer + 300000 <= System.currentTimeMillis()))
			sendDeath(getAsNPC());
		super.processNPC();


	}

}
