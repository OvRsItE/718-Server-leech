package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

@SuppressWarnings("serial")
public class ConditionalDeath extends NPC {

	private int requiredItem;
	private String deathMessage;
	private boolean remove;

	public ConditionalDeath(int requiredItem, String deathMessage, boolean remove, int id, Position tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.requiredItem = requiredItem;
		this.deathMessage = deathMessage;
		this.remove = remove;
	}

	public int getRequiredItem() {
		return requiredItem;
	}

	private boolean removeItem(Player player) {
		if (getHitpoints() < getMaxHitpoints() * 0.1 && (player.getEquipment().getWeaponId() == requiredItem
				|| player.getInventory().containsItem(requiredItem, 1)
				|| player.getToolbelt().containsItem(requiredItem))) {
			if (remove) {
				player.getInventory().deleteItem(requiredItem, 1);
			}
			return true;
		}
		return false;
	}

	public boolean useHammer(Player player) {
		if (removeItem(player)) {
			if (deathMessage != null) {
				player.getPackets().sendGameMessage(deathMessage, true);
			}
			if (getId() == 14849) {
				player.animate(new Animation(15845));
			}
			setHitpoints(0);
			resetCombat();
			super.sendDeath(player);
			return true;
		}
		return false;
	}

	@Override
	public void sendDeath(Entity source) {
		if (source instanceof Player) {
			Player player = (Player) source;
			if ((player.getSlayerManager().hasLearnedQuickBlows() || player.getInventory().contains(requiredItem) >= 1 || player.getEquipment().getWeaponId() == requiredItem || player.getEquipment().getGlovesId() == requiredItem) && useHammer(player)) {
				if ((player.getSlayerManager().hasLearnedQuickBlows() || player.getInventory().contains(requiredItem) >= 1
						|| player.getEquipment().getWeaponId() == requiredItem || player.getEquipment().getGlovesId() == requiredItem) && useHammer(player)) {
					return;
				}
			}
		}
	}
	
}
