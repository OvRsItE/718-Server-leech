package com.rs.game.world.entity.npc.dummy.impl;


import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.dummy.CombatDummy;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;

/**
 * @author Andy || ReverendDread Aug 2, 2017
 */
public class MeleeCombatDummy extends CombatDummy {

	private static final long serialVersionUID = -6286711682048888539L;

	/**
	 * Constructs a new object.
	 * @param tile
	 */
	public MeleeCombatDummy(Position tile) {
		super(17003, tile);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		Player player = hit.getSource().getAsPlayer();
		int damage = hit.getDamage();
		damage = getMaxHit(player, player.getEquipment().getWeaponId(), player.getCombatDefinitions().getAttackStyle(), false, false, 1D);
		if (hit.getLook() == Hit.HitLook.MAGIC_DAMAGE || hit.getLook() == Hit.HitLook.RANGE_DAMAGE)
			damage = 0;
		hit.setDamage(damage/10);
		super.handleIngoingHit(hit);
	}

}
