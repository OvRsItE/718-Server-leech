package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.utility.Utils;

public class RosDharokCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2026 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		int attackStyle = Utils.getRandom(5);
		if (attackStyle == 0) {
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, 400, NPCCombatDefinitions.MELEE, target)));
			npc.animate(new Animation(2066));
		}
		else if (attackStyle == 1) {
			int damage = 400;// normal
			for (Entity e : npc.getPossibleTargets()) {
				if (e instanceof Player
						&& (((Player) e).getPrayer().usingPrayer(0, 19) || ((Player) e)
								.getPrayer().usingPrayer(1, 9))) {
					damage = 450;
					npc.animate(new Animation(2067));
					npc.setNextForceTalk(new ForceTalk("PETTY MORTAL!"));
					player.getPrayer().drainPrayer((Math.round(damage / 20)));
					player.setPrayerDelay(Utils.getRandom(5) + 5);
					delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 380, NPCCombatDefinitions.MELEE, target)));
					player.getPackets().sendGameMessage("Dharok manages to turn off your prayers, leaving you feeling drained.");
				}
			}
		}
		return 6;
	}
	
	
}