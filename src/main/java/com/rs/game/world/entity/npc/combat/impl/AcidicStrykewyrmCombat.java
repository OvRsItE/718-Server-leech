package com.rs.game.world.entity.npc.combat.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.acidwyrm.AcidicStrykewyrm;
import com.rs.game.world.entity.npc.acidwyrm.Wyrmling;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class AcidicStrykewyrmCombat extends CombatScript {
	
	private static final int PROJECTILE_ATTACK = 12794;
	private static final int MELEE_ATTACK = 12791;

	
	@Override
	public Object[] getKeys() {
		return new Object[] { 16023, 16024, 16025 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		if (target.withinDistance(npc, 2) && Utils.getRandom(2) == 0) {
			//Melee chance for all 3 wyrms if target is in range.
			npc.animate(new Animation(MELEE_ATTACK));
			delayHit(npc, 1, target, new Hit(npc, getRandomMaxHit(npc, 
					npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MELEE, target), HitLook.MELEE_DAMAGE));
		} else {
			npc.animate(new Animation(PROJECTILE_ATTACK));
			World.sendProjectile(npc, npc, target, getProjectileId(npc.getId()), 40, 35, 40, 3, 16, 0);
			delayHit(npc, (int) Math.ceil((Utils.getDistance(npc, target) * 0.10)), target, new Hit(npc, 
					getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), NPCCombatDefinitions.MAGE, target), getDamageType(npc.getId())));	
			if (Utils.getRandom(3) == 0) //Poison target
				target.getToxin().applyToxin(ToxinType.VENOM);
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}
	
	/**
	 * Gets
	 * @return
	 */
	private Position getMinionTile(Entity target) {
		Position tile = new Position(target, Utils.random(1, 4));
		if (World.isNotCliped(tile.getZ(), tile.getX(), tile.getY(), 1))
			return tile;
		return getMinionTile(target);
	}
	
	/**
	 * Gets the main projectile id for the strykewyrm phase.
	 * @param id
	 * 			the strykewyrm id.
	 * @return
	 */
	private int getProjectileId(int id) {
		return id == 16024 ? 970 : 2735;
	}
	
	/**
	 * Gets the {@code HitLook} damage type for the wyrms.
	 * @param id
	 * 			the strykewyrm id.
	 * @return
	 * 			the {@code HitLook}.
	 */
	private HitLook getDamageType(int id) {
		return id == 16024 ? HitLook.RANGE_DAMAGE : HitLook.MAGIC_DAMAGE;
	}

}
