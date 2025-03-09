package com.rs.game.world.entity.npc.Polypore;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;


@SuppressWarnings("serial")
public final class FungalMages extends NPC {

	private int fixedCombatType;
	private int[] cachedDamage;
	private int fixedAmount;

	
	
	public FungalMages(int id, Position tile, int mapAreaNameHash,
                       boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		cachedDamage = new int[3];
		
		
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		int damage = hit.getDamage();
		
		if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
			if (damage > 0) {
				damage *= (int) 1.0;
			}
		}
		if (hit.getLook() != HitLook.MAGIC_DAMAGE) {
			setDamageCap(50);
		} else
			setDamageCap(-1);
		
		hit.setDamage(damage);
		super.handleIngoingHit(hit);
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		
		
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {	
					drop();
					reset();
					setLocation(getSpawnPosition());
					finish();
					setRespawnTask();
					stop();
					
				}
				loop++;
			}
		}, 0, 1);
	}

	

	@Override
	public void setRespawnTask() {
		if (!isFinished()) {
			reset();
			setLocation(getSpawnPosition());
			finish();
		}
		final NPC npc = this;
		
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				setFinished(false);
				World.addNPC(npc);
				npc.setLastRegionId(0);
				World.updateEntityRegion(npc);
				loadMapRegions();
				checkMultiArea();
				//shieldTimer = 0;
				fixedCombatType = 0;
				fixedAmount = 0;
			}
		}, getCombatDefinitions().getRespawnDelay() * 250,
				TimeUnit.MILLISECONDS);
	}


	public int getFixedCombatType() {
		return fixedCombatType;
	}

	public void setFixedCombatType(int fixedCombatType) {
		this.fixedCombatType = fixedCombatType;
	}

	public int getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(int fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	
}