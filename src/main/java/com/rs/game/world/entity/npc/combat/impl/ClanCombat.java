package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class ClanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 13858, 18348 };
	}
						//npc.transformIntoNPC(id - 1);
						//npc.transformIntoNPC(id);
	@SuppressWarnings("unused")
	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(6)) {
			case 0:
						npc.transformIntoNPC(13848);
				npc.setNextForceTalk(new ForceTalk("Hybridding switch 1!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Stop safing!"));
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("Awwwww"));
				break;
			case 3:
						npc.transformIntoNPC(13858);
				npc.setNextForceTalk(new ForceTalk("Hybridding switch 2?"));
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk("Bodied kid!"));
				break;
			case 5:
				npc.setNextForceTalk(new ForceTalk("Food left?"));
				break;
			case 6:
				npc.setNextForceTalk(new ForceTalk("Pray off noob!"));
				break;
			}
		}
		if (Utils.getRandom(3) == 0) {
			npc.animate(new Animation(829)); //eating 2 rocktails at a time xd
			npc.heal(460);
		}
		if (Utils.getRandom(2) == 0) { // Melee - Special Attack
			npc.animate(new Animation(1062)); //i know this is a dds special attack and he isn't wearing it...
			npc.setNextGraphics(new Graphics(252, 0, 100)); //don't make it hit higher then 300, could end very very badly
			npc.playSound(2537, 1);
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(
						npc,
						1,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, 345,
										NPCCombatDefinitions.MELEE, target)),
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, 345,
										NPCCombatDefinitions.MELEE, target)));
			}
		} else { // Melee - Whip Attack
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}