package com.rs.game.world.entity.npc.combat.impl;


import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class othermagecombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 15173 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final Player player = (Player) target;
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(3) == 0) {
			npc.animate(new Animation(829)); //eating 2 rocktails at a time xd
			npc.heal(400);
		}if (Utils.getRandom(4) == 0) {
			npc.setNextForceTalk(new ForceTalk("Prayer off!"));
			player.setPrayerDelay(Utils.getRandom(30000) + 5);
		}
		if (Utils.getRandom(2) == 0) {
			npc.animate(new Animation(1978));
			for (Entity target1 : npc.getPossibleTargets()) {
				if (!target1.withinDistance(npc, 30))
					continue;
			delayHit(npc,0,target1,getMagicHit( npc,getRandomMaxHit(npc, Utils.random(800), NPCCombatDefinitions.MAGE, target1)));
			World.sendProjectile(npc, target1, 374, 18, 18, 50, 50, 0, 0);
			}
			npc.heal(Utils.random(400));
		} else { // Melee - Whip Attack
			npc.setNextGraphics(new Graphics(2717));
			npc.animate(new Animation(14209));
			npc.animate(new Animation(1978));
			for (Entity target1 : npc.getPossibleTargets()) {
				if (!target1.withinDistance(npc, 30))
					continue;
			delayHit(npc,0,target1,getMagicHit( npc,getRandomMaxHit(npc, Utils.random(800), NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target1, 2722, 18, 18, 50, 50, 0, 0);
			target.setNextGraphics(new Graphics(2727));
		}}
		return defs.getAttackDelay();
	}
}