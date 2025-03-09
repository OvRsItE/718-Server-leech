package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class PestQueenCombat extends CombatScript {
	


	@Override
	public Object[] getKeys() {
		return new Object[] { 6358 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.random(5);
		if(attackStyle == 0) {
			npc.animate(new Animation(14801));
			for (@SuppressWarnings("unused") Entity t : npc.getPossibleTargets()) {
				delayHit(
						npc,
						1,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, 500,
										NPCCombatDefinitions.MELEE, target)));
			}
		}else if(attackStyle == 1) {
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}else if(attackStyle == 2) {
			npc.animate(new Animation(1979));
			final int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
			final Position center = new Position(npc.getX() + Utils.DIRECTION_DELTA_X[dir] * 5, npc.getY() + Utils.DIRECTION_DELTA_Y[dir] * 5, 0);
			WorldTasksManager.schedule(new WorldTask() {
				int count = 0;
				@Override
				public void run() {
					for(Player player : World.getPlayers()) { //lets just loop all players for massive moves
						if(Utils.DIRECTION_DELTA_X[dir] == 0) {
							if(player.getX() != center.getX())
								continue;
						}
						if(Utils.DIRECTION_DELTA_Y[dir] == 0) {
							if(player.getY() != center.getY())
								continue;
						}
						if(Utils.DIRECTION_DELTA_X[dir] != 0) {
							if(Math.abs(player.getX() - center.getX()) > 5)
								continue;
						}
						if(Utils.DIRECTION_DELTA_Y[dir] != 0) {
							if(Math.abs(player.getY() - center.getY()) > 5)
								continue;
						}
						delayHit(npc, 0, player, new Hit(npc, Utils.random(150), HitLook.REGULAR_DAMAGE));
					}
					if(count++ == 5) {
						stop();
						return;
					}
				}
			}, 0, 0);
			World.sendProjectile(npc, center, 2196, 0, 0, 5, 35, 0, 0);
		}else if(attackStyle == 3) {
			delayHit(
					npc,
					2,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, Utils.random(300),
									NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 2873, 34, 16, 40, 35, 16, 0);
			npc.animate(new Animation(14802));
		}else if(attackStyle == 4) {
			npc.animate(new Animation(14803));
			npc.heal(500);
		}
		return 5;
	}
}
