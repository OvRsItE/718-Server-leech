package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class MiniCommanderZilyanaCombat extends CombatScript {
	@Override
	public Object[] getKeys() {
		return new Object[] { 8131 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(9)) {
			case 0:
				npc.setNextForceTalk(new ForceTalk(
						"Death to the enemies of the light!"));
				npc.playSound(3247, 2);
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Slay the evil ones!"));
				npc.playSound(3242, 2);
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk(
						"Saradomin lend me strength!"));
				npc.playSound(3263, 2);
				break;
			case 3:
				npc.setNextForceTalk(new ForceTalk("By the power of Saradomin!"));
				npc.playSound(3262, 2);
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk("May Saradomin be my sword."));
				npc.playSound(3251, 2);
				break;
			case 5:
				npc.setNextForceTalk(new ForceTalk("Good will always triumph!"));
				npc.playSound(3260, 2);
				break;
			case 6:
				npc.setNextForceTalk(new ForceTalk(
						"Forward! Our allies are with us!"));
				npc.playSound(3245, 2);
				break;
			case 7:
				npc.setNextForceTalk(new ForceTalk("Saradomin is with us!"));
				npc.playSound(3266, 2);
				break;
			case 8:
				npc.setNextForceTalk(new ForceTalk("In the name of Saradomin!"));
				npc.playSound(3250, 2);
				break;
			case 9:
				npc.setNextForceTalk(new ForceTalk("Attack! Find the Godsword!"));
				npc.playSound(3258, 2);
				break;
			}
		}
		if (Utils.getRandom(1) == 0) { // mage magical attack
			npc.animate(new Animation(6967));
				int damage = getRandomMaxHit(npc, defs.getMaxHit(),
						NPCCombatDefinitions.MAGE, target);
				if (damage > 0) {
					delayHit(npc, 1, target, getMagicHit(npc, damage));
					target.setNextGraphics(new Graphics(1194));
				}

		} else { // melee attack
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
