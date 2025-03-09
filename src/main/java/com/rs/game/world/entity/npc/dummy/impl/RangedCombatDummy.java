package com.rs.game.world.entity.npc.dummy.impl;

import com.rs.game.map.Position;


import com.rs.game.world.entity.npc.dummy.CombatDummy;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;

/**
 * @author Andy || ReverendDread Aug 2, 2017
 */
public class RangedCombatDummy extends CombatDummy {

	private static final long serialVersionUID = -6699084674820701895L;

	/**
	 * Constructs a new object.
	 * @param tile
	 */
	public RangedCombatDummy(Position tile) {
		super(17001, tile);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		Player player = hit.getSource().getAsPlayer();
		int damage = hit.getDamage();
		damage = getMaxHit(player, player.getEquipment().getWeaponId(), player.getCombatDefinitions().getAttackStyle(), true, false, 1D);
		if (hit.getLook() == Hit.HitLook.MELEE_DAMAGE || hit.getLook() == Hit.HitLook.MAGIC_DAMAGE)
			damage = 0;
		hit.setDamage(damage/10);
		super.handleIngoingHit(hit);
	}

}
