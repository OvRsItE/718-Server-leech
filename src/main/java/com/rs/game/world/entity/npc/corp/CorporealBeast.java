package com.rs.game.world.entity.npc.corp;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public class CorporealBeast extends NPC {

	private DarkEnergyCore core;

	public CorporealBeast(int id, Position tile, int mapAreaNameHash,
                          boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setDamageCap(1000);
		setLureDelay(3000);
		setForceTargetDistance(64);
		setForceFollowClose(false);
		setCanBeFrozen(false);
	}

	public void spawnDarkEnergyCore() {
		if (core != null)
			return;
		core = new DarkEnergyCore(this);
	}
	@Override
	public void handleIngoingHit(final Hit hit) {
		reduceHit(hit);
		super.handleIngoingHit(hit);
	}
	public void reduceHit(Hit hit) {
		if (!(hit.getSource() instanceof Player) || (hit.getLook() != Hit.HitLook.MELEE_DAMAGE && hit.getLook() != Hit.HitLook.RANGE_DAMAGE && hit.getLook() != Hit.HitLook.MAGIC_DAMAGE))
			return;
		Player from = (Player) hit.getSource();
		int weaponId = from.getEquipment().getWeaponId();
		String name = weaponId == -1 ? "null" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		if(hit.getLook() != Hit.HitLook.MELEE_DAMAGE || !name.contains("spear") && !name.contains("warhammer") && !name.contains("crossbow") && !name.contains("hasta"))
			hit.setDamage(hit.getDamage() / 2);

	}

	public void removeDarkEnergyCore() {
		if (core == null)
			return;
		core.finish();
		core = null;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		int maxhp = getMaxHitpoints();
		if (maxhp > getHitpoints() && getPossibleTargets().isEmpty())
			setHitpoints(maxhp);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (core != null)
			core.sendDeath(source);
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.1; // 0.6
	}

}
