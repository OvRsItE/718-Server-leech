package com.rs.game.world.entity.npc.vorago;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Vorago extends NPC {

	private VoragoMinion minion;

	public Vorago(int id, Position tile, int mapAreaNameHash,
                  boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setDamageCap(2500);
		setLureDelay(3000);
		setForceTargetDistance(64);
		setForceFollowClose(true);
	}

	public void spawnVoragoMinion() {
		if (minion != null)
			return;
		setDamageCap(0);
		minion = new VoragoMinion(this);
	}

	public void removeVoragoMinion() {
		if (minion == null)
			return;
		minion.finish();
		setDamageCap(2500);
		minion = null;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		Entity target = getCombat().getTarget();
		if (target != null && Utils.isOnRange(target.getX(), target.getY(), target.getSize(), getX(), getY(), getSize(), 4) && Utils.random(40) == 0) 
			sendTeleport(Utils.random(2) == 0 ? target : this);
		if (isDead())
			return;
	}
	
	private void sendTeleport(Entity entity) {
		int entitySize = entity.getSize();
		for (int c = 0; c < 10; c++) {
		    int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
		    if (World.checkWalkStep(entity.getZ(), entity.getX(), entity.getY(), dir, entitySize)) {
			entity.setNextGraphics(new Graphics(409));
			entity.setNextPosition(new Position(getX() + Utils.DIRECTION_DELTA_X[dir], getY() + Utils.DIRECTION_DELTA_Y[dir], getZ()));
			break;
		    }
		}
	    }

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (minion != null)
			minion.sendDeath(source);
	}
}
