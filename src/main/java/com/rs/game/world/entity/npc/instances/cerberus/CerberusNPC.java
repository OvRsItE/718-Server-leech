/**
 * 
 */
package com.rs.game.world.entity.npc.instances.cerberus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;

import lombok.Getter;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class CerberusNPC extends NPC {

	private static final long serialVersionUID = -7656548207200629936L;

	private static final int SITTING = 25863, ATTACKING = 25862;
	
	private CerberusInstance instance;
	
	private Player target;
	
	@Getter protected SummonedSoul[] minions = new SummonedSoul[3];
	
	private Integer[] GHOST_IDS = new Integer[] { 25869, 25867, 25868 }; 
	
	protected List<Integer> ghosts = Arrays.asList(GHOST_IDS);
	
	protected static final int MELEE = 25869, RANGED = 25867, MAGIC = 25868;
	
	protected int stage = 0;
	
	private boolean spawned_flames, spawned_minions;
	
	public long cooldown;

	public CerberusNPC(int id, Player target, Position tile, CerberusInstance instance) {
		super(id, tile, -1, 0, true, false);
		this.instance = instance;
		this.target = target;
		setForceMultiArea(true);
		setForceFollowClose(true);
		getToxin().applyImmunity(ToxinType.VENOM, Integer.MAX_VALUE);
		getToxin().applyImmunity(ToxinType.POISON, Integer.MAX_VALUE);
		setCanBeFrozen(false);
	}
	
	@Override
	public void processNPC() {		
		if (!(spawned_minions) && ((getHitpoints()) < (getMaxHitpoints() * .60)) && (!isDead() || isFinished())) {
			Collections.shuffle(ghosts);
			minions[0] = new SummonedSoul(this, target, ghosts.get(0), instance.getLocation(1239, 1267, 0), -1, true, true);
			minions[1] = new SummonedSoul(this, target, ghosts.get(1), instance.getLocation(1240, 1267, 0), -1, true, true);
			minions[2] = new SummonedSoul(this, target, ghosts.get(2), instance.getLocation(1241, 1267, 0), -1, true, true);
			spawned_minions = true;
		}
		if (target.getY() >= instance.getLocation(1231, 1243, 0).getY()) {
			if (!spawned_flames) {
				World.spawnObject(new WorldObject(123105, 10, 0, instance.getLocation(1239, 1242, 0)), true);
				World.spawnObject(new WorldObject(123105, 10, 0, instance.getLocation(1240, 1242, 0)), true);
				World.spawnObject(new WorldObject(123105, 10, 0, instance.getLocation(1241, 1242, 0)), true);
				spawned_flames = true;
				animate(Animation.createOSRS(4295));
			}
			transformIntoNPC(ATTACKING);
			setTarget(target);
		} else {
			getCombat().removeTarget();
			reset();
			transformIntoNPC(SITTING);
			destroy();
		}
		super.processNPC();
	}
	
	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		destroy();
	}
	
	public void destroy() {
		spawned_minions = false;
		stage = 0;
		cooldown = 0;
	}

}
