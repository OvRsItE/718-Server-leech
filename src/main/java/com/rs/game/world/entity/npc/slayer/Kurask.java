package com.rs.game.world.entity.npc.slayer;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

@SuppressWarnings("serial")
public class Kurask extends NPC {
	
	public Kurask(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getSource() instanceof Player) {
			Player player = (Player) hit.getSource();
			int weaponId = player.getEquipment().getWeaponId();
			if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (player.getEquipment().getAmmoId() != 4160 && player.getEquipment().getAmmoId() != 13280) {
					player.sendMessage("You need to use magic dart, leaf-bladed weapons or broad ammo to damage this creature.");
					hit.setDamage(0);
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (player.getCombatDefinitions().getSpellId() != 56) {
					player.sendMessage("You need to use magic dart, leaf-bladed weapons or broad ammo to damage this creature.");
					hit.setDamage(0);
				}
			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (weaponId != 13290 && weaponId != 4158) {
					player.sendMessage("You need to use magic dart, leaf-bladed weapons or broad ammo to damage this creature.");
					hit.setDamage(0);
				}
			}	
		}
		super.handleIngoingHit(hit);
	}
 	
}
