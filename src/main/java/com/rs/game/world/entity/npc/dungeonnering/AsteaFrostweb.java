package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public final class AsteaFrostweb extends DungeonBoss {

	private int meleeNPCId;
	private int switchPrayersDelay;
	private int spawnedSpiders;
	private NPC[] spiders;

	public AsteaFrostweb(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		spiders = new NPC[6];
		meleeNPCId = id;
		resetSwitchPrayersDelay();
	}

	public void resetSwitchPrayersDelay() {
		switchPrayersDelay = 35; // 25sec
	}

	public void switchPrayers() {
		setNextNPCTransformation(getId() == meleeNPCId + 2 ? meleeNPCId : getId() + 1);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		if (switchPrayersDelay > 0)
			switchPrayersDelay--;
		else {
			switchPrayers();
			resetSwitchPrayersDelay();
		}
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		for (NPC minion : spiders) {
			if (minion == null)
				continue;
			minion.sendDeath(this);
		}
	}

	public void spawnSpider() {
		if (spawnedSpiders >= spiders.length)
			return;
		for (int tryI = 0; tryI < 10; tryI++) {
			Position tile = new Position(this, 2);
			if (World.isTileFree(0, tile.getX(), tile.getY(), 1)) {
				NPC spider = spiders[spawnedSpiders++] = new NPC(64, tile, -1, true, true);
				spider.setForceAgressive(true);
				spider.setForceMultiArea(true);
				break;
			}
		}
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		super.handleIngoingHit(hit);
		if (getId() == meleeNPCId) {
			if (hit.getLook() == Hit.HitLook.MELEE_DAMAGE)
				hit.setDamage(0);
		} else if (getId() == meleeNPCId + 1) {
			if (hit.getLook() == Hit.HitLook.MAGIC_DAMAGE)
				hit.setDamage(0);
		} else if (hit.getLook() == Hit.HitLook.RANGE_DAMAGE)
			hit.setDamage(0);
	}

}
