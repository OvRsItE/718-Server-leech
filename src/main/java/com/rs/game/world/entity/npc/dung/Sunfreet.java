package com.rs.game.world.entity.npc.dung;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Sunfreet extends NPC {

	public static final int[] HITSOUNDS = new int[] { 3005, 3006, 3010, 3014,
			3048, 2978 };

	public Sunfreet(int id, Position tile, int mapAreaNameHash,
                    boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setForceMultiAttacked(true);
		setForceAgressive(true);
		setLureDelay(0);
		setRun(true);

	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (hit.getSource() != null) {
			int recoil = (int) (hit.getDamage() * 0.1);
			if (recoil > 0) {
				this.playSound(HITSOUNDS[Utils.random(HITSOUNDS.length)], 2);
				hit.getSource().applyHit(
						new Hit(this, recoil, HitLook.REFLECTED_DAMAGE));
			}
		}
	}
}