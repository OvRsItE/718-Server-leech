package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.Drop;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public final class ShadowForgerIhlakhizan extends DungeonBoss {

	public ShadowForgerIhlakhizan(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		setCantFollowUnderCombat(true); //force cant walk
	}

	@Override
	public void setNextFaceEntity(Entity entity) {
		//this boss doesnt face
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		super.processNPC();
		for (Player player : getManager().getParty().getTeam()) {
			if (!getManager().isAtBossRoom(player) || clipedProjectile(player, false) || player.getTemporaryAttributtes().get(TemporaryAttributes.Key.SHADOW_FORGER_SHADOW) != null)
				continue;
			player.setNextGraphics(new Graphics(2378));
			player.getTemporaryAttributtes().put(TemporaryAttributes.Key.SHADOW_FORGER_SHADOW, Boolean.TRUE);
			player.applyHit(new Hit(this, com.rs.utility.Utils.random((int) (player.getMaxHitpoints() * 0.1)) + 1, Hit.HitLook.REGULAR_DAMAGE));
		}
	}

	public void setUsedShadow() {
		for (Player player : getManager().getParty().getTeam()) {
			player.getTemporaryAttributtes().put(TemporaryAttributes.Key.SHADOW_FORGER_SHADOW, Boolean.TRUE);
		}
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
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
	public void sendDrop(Player player, Drop drop) {
		Item item = new Item(drop.getItemId());
		player.getInventory().addItemDrop(item.getId(), item.getAmount());
	}

}
