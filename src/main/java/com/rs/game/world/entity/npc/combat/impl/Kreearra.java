package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class Kreearra extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6222 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (!npc.isUnderCombat()) {
			npc.animate(new Animation(17396));
			delayHit(
					npc,
					1,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, 260,
									NPCCombatDefinitions.MELEE, target)));
			return defs.getAttackDelay();
		}
		npc.animate(new Animation(17397));
		for (Entity t : npc.getPossibleTargets()) {
			if (Utils.getRandom(2) == 0)
				sendMagicAttack(npc, t);
			else {
				delayHit(
						npc,
						1,
						t,
						getRangeHit(
								npc,
								getRandomMaxHit(npc, 720,
										NPCCombatDefinitions.RANGE, t)));
				World.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
				Position teleTile = t;
				for (int trycount = 0; trycount < 10; trycount++) {
					teleTile = new Position(t, 2);
					if (World.canMoveNPC(t.getZ(), teleTile.getX(),
							teleTile.getY(), t.getSize()))
						break;
				}
				t.setNextPosition(teleTile);
			}
		}
		return defs.getAttackDelay();
	}

	private void sendMagicAttack(NPC npc, Entity target) {
		npc.animate(new Animation(17397));
		for (Entity t : npc.getPossibleTargets()) {
			delayHit(
					npc,
					1,
					t,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 210,
									NPCCombatDefinitions.MAGE, t)));
			World.sendProjectile(npc, t, 1198, 41, 16, 41, 35, 16, 0);
			target.setNextGraphics(new Graphics(1196));
		}
	}
}
