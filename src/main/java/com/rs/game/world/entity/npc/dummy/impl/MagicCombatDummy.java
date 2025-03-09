package com.rs.game.world.entity.npc.dummy.impl;


import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.dummy.CombatDummy;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Hit;

/**
 * @author Andy || ReverendDread Aug 2, 2017
 */
public class MagicCombatDummy extends CombatDummy {

	private static final long serialVersionUID = -2766420343547495581L;

	/**
	 * Constructs a new object.
	 * @param tile
	 */
	public MagicCombatDummy(Position tile) {
		super(17002, tile);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		Player player = hit.getSource().getAsPlayer();
		int damage = hit.getDamage();
		damage = getMagicMaxHit(player, getSpellBaseDamage(player, player.getCombatDefinitions().getAutoCastSpell() != -1 ? player.getCombatDefinitions().getAutoCastSpell() : player.getCombatDefinitions().getSpellId()));
		if (hit.getLook() == Hit.HitLook.MELEE_DAMAGE || hit.getLook() == Hit.HitLook.RANGE_DAMAGE)
			damage = 0;
		hit.setDamage(damage/10);
		super.handleIngoingHit(hit);
	}

}
