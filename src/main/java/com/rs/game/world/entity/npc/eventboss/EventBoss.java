/**
 * 
 */
package com.rs.game.world.entity.npc.eventboss;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * @author ReverendDread
 * Dec 29, 2018
 */
public class EventBoss extends NPC {
	
	public enum Zone {
		
		PIRATES_HIDEOUT("Pirates' Hideout", new Position(3022, 3939)),
		COMBAT_TRAINING_CAMP("the Combat Training Camp", new Position(2521, 3350)),
		EDGEVILLE_MONASTERY("Edgeville Monastery", new Position(3068, 3470)),
		MAGE_TRAINING_ARENA("Mage Training Arena", new Position(3327, 3293)),
		RUINS_OF_UZER("the Ruins of Uzer", new Position(3453, 3093)),
		RANGING_GUILD("the Ranging Guild", new Position(2700, 3442)),
		FELDIP_HILLS("Feldip Hills", new Position(2584, 2976)),
		
		;
		
		@Getter private String name;
		@Getter private Position tile;
		
		public static final Zone[] VALUES = values();
		
		private Zone(Position tile) {
			this(null, tile);
		}
		
		private Zone(String name, Position tile) {
			this.name = name;
			this.tile = tile;
		}
		
		public String getName() {
			if (name == null)
				return Utils.getFormattedEnumName(name);
			return name;
		}
		
	}
	
	public static final int[] BOSSES = {
		9039,
	};
	
	@Getter private Zone zone;
	
	/**
	 * Constructor
	 * @param id
	 * @param tile
	 */
	public EventBoss(int id, Zone zone) {
		super(id, zone.getTile(), -1, 0, true, true);
		this.zone = zone;
		setForceMultiAttacked(true);
		setAtMultiArea(true);
		setForceMultiArea(true);
		setForceTargetDistance(10);
		setCanBeFrozen(false);
	}
	
	/**
	 * Constructor
	 * @param id
	 * @param tile
	 */
	public EventBoss(int id, Position tile) {
		super(id, tile, -1, 0, true, true);
		setForceMultiAttacked(true);
		setAtMultiArea(true);
		setForceMultiArea(true);
		setForceTargetDistance(10);
		setCanBeFrozen(false);
	}
	
	@Override
	public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		animate(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {				
					for (Entity entity : getReceivedDamage().keySet()) {
						if (entity == null || entity.isFinished() || !(entity instanceof Player))
							continue;									
						World.addGroundItem(new Item(29468, 1), entity, entity.getAsPlayer(), true, 180);
					}
					reset();
					finish();
					spawnChest();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	private void spawnChest() {
		CoresManager.slowExecutor.schedule(() -> {
			final Position middle = getMiddleTile();
			World.spawnTemporaryObject(new WorldObject(61300, 10, Utils.random(4), middle.getX(), middle.getY(), middle.getZ()), 120_000);
		}, 1, TimeUnit.SECONDS);
	}

	public static final int getRandomBoss() {
		return BOSSES[Utils.random(BOSSES.length)];
	}
	
	public static final Zone getRandomZone() {
		return Zone.VALUES[Utils.random(Zone.VALUES.length)];
	}

}
