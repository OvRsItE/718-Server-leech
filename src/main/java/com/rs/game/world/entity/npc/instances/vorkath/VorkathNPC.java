/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.instances.vorkath.attack.*;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the Vorkath NPC.
 * @author ReverendDread
 * Sep 10, 2018
 */
public class VorkathNPC extends NPC {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -9161784913271847159L;
	
	@Getter private int attack;
	@Getter private int rotation;
	
	@Getter @Setter public ZombifiedSpawn minion;

	@Getter private RegionInstance instance;
	
	private final VorkathAttack[][] ATTACKS = {		
			//Rotation 1
			{
				new DragonfireAttack(),
				new RangedAttack(),
				new HDDragonfireAttack(),
				new DragonfireAttack(),
				new MagicAttack(),
				new DragonfireAttack(),
				new IceDragonfire(),
				new MagicAttack(),
				new DragonfireAttack(),
				new DragonfireAttack(),
				new RangedAttack(),
				new HDDragonfireAttack(),
				new DragonfireAttack(),
				new PoisonPool(),
			}
	};
	
	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public VorkathNPC(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned, RegionInstance instance) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setCantFollowUnderCombat(true);
		setDirection(5);
		setForceMultiArea(true);
		setDamageCap(1000);
		rotation = Utils.random(ATTACKS.length);
		this.instance = instance;
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		setCanBeFrozen(false);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (getMinion() != null && !getMinion().hasExploded())
			hit.setDamage(0);
		super.handleIngoingHit(hit);
	}
	
	@Override
	public void sendDeath(final Entity source) {
		resetWalkSteps();
		getCombat().removeTarget();
		animate(Animation.createOSRS(7949));
		setHitpoints(getMaxHitpoints());
		transformIntoNPC(asOSRS(8058));
		source.getAsPlayer().getActionManager().forceStop();
		setDirection(5);
		attack = 0;
		rotation = Utils.random(ATTACKS.length);
		CoresManager.slowExecutor.schedule(() -> {
			transformIntoNPC(asOSRS(8059));
			getToxin().reset();
			source.getAsPlayer().getBossSlayer().deincrementKills(this);
			source.getAsPlayer().getBossPetsManager().rollBossPet(getId());
			source.getAsPlayer().getKillcountManager().incremenetAndGet(getId());
			getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
			drop();
			drop();
		}, 2400, TimeUnit.MILLISECONDS);
	}

	/**
	 * Gets the next attack for vorkath to perform.
	 * @return
	 */
	public VorkathAttack getNextAttack() {
		if ((attack++) >= (ATTACKS[rotation].length - 1)) {
			attack = 0;
		}
		return ATTACKS[rotation][attack];
	}
	
}
