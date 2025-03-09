/**
 * 
 */
package com.rs.game.world.entity.npc.shaman;

import java.util.ArrayList;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.utility.Utils;

/**
 * Represents a lizard shaman.
 * @author ReverendDread
 * Oct 6, 2018
 */
public class LizardmanShaman extends NPC {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1221443498929702446L;
	
	/**
	 * Spawns minion list.
	 */
	private ArrayList<Spawn> minions = new ArrayList<Spawn>();
	
	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 */
	public LizardmanShaman(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setCanBeFrozen(false);
		getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
	}
	
	/**
	 * Handles spawning & setup of spawns (minions).
	 */
	public void spawnMinions(LizardmanShaman owner, Entity target) {
		Position[] tiles = Utils.getAdjacentTiles(target);
		WorldTasksManager.schedule(new WorldTask() {

			int ticks;
			final LizardmanShaman shaman = owner;
			
			@Override
			public void run() {
				if (ticks == 0 || ticks == 1 || ticks == 2) {
					Position spawnPoint = null;
					for (Position tile : tiles) {
						if (World.getMask(tile.getZ(), tile.getX(), tile.getY()) == 0) {
							spawnPoint = tile;
							continue;
						}
					}
					if (spawnPoint != null) {
						Spawn spawn = new Spawn(shaman, 26768, spawnPoint, -1, 0, true, true);
						minions.add(spawn);
						spawn.setFollowing(target);
						spawn.init();
					}
				}
				ticks++;
			}
			
		}, 1, 1);
	}
	
	public ArrayList<Spawn> getMinions() {
		return minions;
	}

}
