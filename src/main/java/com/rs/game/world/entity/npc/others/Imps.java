package com.rs.game.world.entity.npc.others;

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


@SuppressWarnings("serial")
public final class Imps extends NPC {

	private int fixedCombatType;

	
	public Imps(int id, Position tile, int mapAreaNameHash,
                boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		animate(null);
		//shieldTimer = 0;
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
			}
		}, 1,
				TimeUnit.MINUTES);
	}

	public static boolean atTD(Position tile) {
		if ((tile.getX() >= 2560 && tile.getX() <= 2630)
				&& (tile.getY() >= 5710 && tile.getY() <= 5753))
			return true;
		return false;
	}

	public int getFixedCombatType() {
		return fixedCombatType;
	}

	public void setFixedCombatType(int fixedCombatType) {
		this.fixedCombatType = fixedCombatType;
	}

}