package com.rs.game.world.entity.npc.combat.impl;


import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class otherRangecombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15176 };
	}

	@SuppressWarnings("unused")
	@Override
	public int attack(final NPC npc, final Entity target) {
		final Player player = (Player) target;
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(3) == 0) {
			npc.animate(new Animation(829)); //eating 2 rocktails at a time xd
			npc.heal(350);
		}if (Utils.getRandom(4) == 0) {
			npc.setNextForceTalk(new ForceTalk("Poison!!"));
			target.getToxin().applyToxin(ToxinType.POISON, 175);
			npc.heal(Utils.random(200));
		} else { // Melee - Whip Attack
			npc.setNextGraphics(new Graphics(426));
			World.sendProjectile(npc, target, 1099, 41, 16, 45, 35, 16, 0);
			World.sendProjectile(npc, target, 1099, 41, 16, 32, 35, 21, 0);
			delayHit(npc,0,target,getRangeHit( npc,getRandomMaxHit(npc, Utils.random(370), NPCCombatDefinitions.RANGE, target)));
			delayHit(npc,0,target,getRangeHit( npc,getRandomMaxHit(npc, Utils.random(350), NPCCombatDefinitions.RANGE, target)));

		}
		return defs.getAttackDelay();
	}
}