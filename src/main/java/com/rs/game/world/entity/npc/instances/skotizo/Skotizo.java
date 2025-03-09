/**
 * 
 */
package com.rs.game.world.entity.npc.instances.skotizo;

import java.util.ArrayList;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class Skotizo extends NPC {

	private boolean spawnedMinions;
	
	/** Minions */
	private ArrayList<ReanimatedSpawn> spawns = new ArrayList<ReanimatedSpawn>();
	
	/** Altars */
	private Altar[] altars = new Altar[4];
	
	private final SkotizoInstance instance;
	
	private ArrayList<WorldObject> ALTAR_OBJECTS = new ArrayList<WorldObject>();
	
	private Position[] ALTAR_POS = {
		new Position(1696, 9871, 0),
		new Position(1714, 9888, 0),
		new Position(1694, 9904, 0),
		new Position(1678, 9888, 0),
	};

	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param faceDirection
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public Skotizo(SkotizoInstance instance, int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, true);
		setForceMultiArea(true);
		setAtMultiArea(true);
		getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		setCanBeFrozen(false);
		this.instance = instance;
		addAltars();
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		super.handleIngoingHit(hit);
		if (getHitpoints() <= (getMaxHitpoints() * 0.50) && !spawnedMinions) {
			spawnMinions(hit.getSource().getAsPlayer());
		}
		if (Utils.random(30) <= 5) {
			
			int position = Utils.random(ALTAR_POS.length);
			
			if (altars[position] == null || altars[position].isDead() || altars[position].isFinished()) {
				altars[position] = new Altar(position, ALTAR_OBJECTS.get(position));
			} else {
				return;
			}
			
			if (hit.getSource() instanceof Player) {
				hit.getSource().getAsPlayer().sendMessage("<col=ff0000>Skotizo has summoned an altar to his aid.");
			}		
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		spawns.forEach((spawn) -> {
			spawn.finish();
		});
		for (Altar altar : altars) {
			if (altar == null || altar.isFinished() || altar.isDead())
				continue;
			altar.finish();
		}
		destroyAltars();
	}
	
	/**
	 * Adds inactive altars to map
	 */
	private void addAltars() {
		for (int index = 0; index < 4; index++) {
			Position loc = instance.getLocation(ALTAR_POS[index]);
			ALTAR_OBJECTS.add(new WorldObject(128924, 10, 0, loc.getX(), loc.getY(), loc.getZ()));
			World.spawnObject(ALTAR_OBJECTS.get(index));
		}
	}
	
	/**
	 * Destorys altars on map.
	 */
	public void destroyAltars() {
		for (int index = 0; index < 4; index++) {
			World.deleteObject(ALTAR_OBJECTS.get(index));
		}
	}
	
	/**
	 * Spawns minions around the player.
	 * @param player
	 */
	private void spawnMinions(final Player player) {
		spawnedMinions = true;
		while (spawns.size() < 3) {
			Position tile = new Position(player, 1, 2);
			if (World.getMask(tile.getZ(), tile.getX(), tile.getY()) != 0)
				continue;
			ReanimatedSpawn spawn = new ReanimatedSpawn(tile);
			spawn.setTarget(player);
			spawns.add(spawn);
		}
	}

}
