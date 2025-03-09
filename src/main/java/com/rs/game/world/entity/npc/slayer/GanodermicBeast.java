package com.rs.game.world.entity.npc.slayer;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;



@SuppressWarnings("serial")
public class GanodermicBeast extends NPC {

	private boolean sprayed;

	public boolean isSprayed() {
		return sprayed;
	}

	public void setSprayed(boolean spray) {
		sprayed = spray;
	}

	public GanodermicBeast(int id, Position tile, int mapAreaNameHash,
                           boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		int damage = hit.getDamage();
		
		if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
			if (damage > 0) {
				damage *= (int) 1.75;
			}
		}
		if (hit.getLook() != HitLook.MAGIC_DAMAGE) {
			setDamageCap(50);
		} else
			setDamageCap(-1);
		
		hit.setDamage(damage);
		super.handleIngoingHit(hit);
	}

	public void startSpray(Player player) {
		if (isSprayed()) {
			player.getPackets().sendGameMessage(
					"This NPC has already been sprayed with neem oil.");
			return;
		}
		NPC before = this;
		transformIntoNPC(14697);
		setSprayed(true);
		this.setHitpoints(before.getHitpoints());
		setNextForceTalk(new ForceTalk("Rarghh"));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (isDead()) {
					stop();
				}
				transformIntoNPC(14696);
				setSprayed(false);
			}
		}, 120);
	}

	@Override
	public void sendDeath(Entity source) {
		transformIntoNPC(14696);
		setSprayed(false);
		resetCombat();
		super.sendDeath(source);
	}
}