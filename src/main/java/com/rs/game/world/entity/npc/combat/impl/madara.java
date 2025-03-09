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
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class madara extends CombatScript {

	
	@Override
	public Object[] getKeys() {
		return new Object[] {268, 261};
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.getRandom(5);
		if (Utils.getRandom(10) == 0) {
			ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
			final HashMap<String, int[]> tiles = new HashMap<String, int[]>();
			for (Entity t : possibleTargets) {
				String key = t.getX() + "_" + t.getY();
				if (!tiles.containsKey(t.getX() + "_" + t.getY())) {
					tiles.put(key, new int[] { t.getX(), t.getY() });
					World.sendProjectile(npc, new Position(t.getX(), t.getY(),
							npc.getZ()), 1900, 34, 0, 30, 35, 16, 0);
				}
			}
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					ArrayList<Entity> possibleTargets = npc
							.getPossibleTargets();
					for (int[] tile : tiles.values()) {

						World.sendGraphics(null, new Graphics(1896),
								new Position(tile[0], tile[1], 0));
						for (Entity t : possibleTargets)
							if (t.getX() == tile[0] && t.getY() == tile[1])
								t.applyHit(new Hit(npc,
										Utils.getRandom(400) + 400,
										HitLook.REGULAR_DAMAGE));
					}
					stop();
				}

			}, 5);
		} else if (Utils.getRandom(10) == 0) {
			npc.setNextGraphics(new Graphics(2622));

			npc.heal(300);
		}
		if (attackStyle == 0) { // normal mage move
			npc.animate(new Animation(1979));
			delayHit(npc,2,target,getMagicHit(npc,getRandomMaxHit(npc, defs.getMaxHit(),NPCCombatDefinitions.MAGE, target)));
			World.sendProjectile(npc, target, 2986, 34, 16, 40, 35, 16, 0);
		} else if (attackStyle == 1) { // normal mage move
			npc.animate(new Animation(1979));
			npc.setNextGraphics(new Graphics(1898));
			target.setNextGraphics(new Graphics(2954));
			delayHit( npc, 2, target, getRegularHit(npc, target.getMaxHitpoints() - 1 > 900 ? 900 : target.getMaxHitpoints() - 1));
		} else if (attackStyle == 2) {
			npc.animate(new Animation(1979));
			npc.setNextGraphics(new Graphics(1901));
			World.sendProjectile(npc, target, 1899, 34, 16, 30, 95, 16, 0);
			delayHit(npc, 4, target, getMagicHit( npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target)));
		} else if (attackStyle == 3) {
			npc.animate(new Animation(1979));
			final int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
			final Position center = new Position(npc.getX() + Utils.DIRECTION_DELTA_X[dir] * 5, npc.getY() + Utils.DIRECTION_DELTA_Y[dir] * 5, 0);
			npc.setNextForceTalk(new ForceTalk("Off with the trash!!"));
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
						delayHit(npc, 0, player, new Hit(npc, Utils.random(200), HitLook.REGULAR_DAMAGE));
					}
					if(count++ == 4) {
						stop();
						return;
					}
				}
			}, 0, 0);
			World.sendProjectile(npc, center, 2196, 0, 0, 5, 35, 0, 0);
			} else if (attackStyle == 4) {
			npc.animate(new Animation(1979));
			npc.setNextGraphics(new Graphics(2593));
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (Entity t : npc.getPossibleTargets()) {
						t.applyHit(new Hit(npc, (int) (t.getHitpoints() * Math .random()), HitLook.REGULAR_DAMAGE, 0));
						t.setNextGraphics(new Graphics(2954));
					}
					npc.getCombat().addCombatDelay(3);
					npc.setCantInteract(false);
					npc.setTarget(target);
				}

			}, 4);
			return 0;
		} else if (attackStyle == 5) {
			npc.animate(new Animation(1979));
			final Position center = new Position(target);
			World.sendGraphics(npc, new Graphics(2191), center);
			npc.setNextForceTalk(new ForceTalk("Feel my almighty power!!?"));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 0;
				@Override
				public void run() {
					for(Player player : World.getPlayers()) { //lets just loop all players for massive moves
						if(player == null || player.isDead() || player.isFinished())
							continue;
						if(player.withinDistance(center, 1)) {
							delayHit(npc, 0, player, new Hit(npc, Utils.random(100), HitLook.REGULAR_DAMAGE));
						}
					}
					if(count++ == 5) {
						stop();
						return;
					}
				}
			}, 0, 0);
			return defs.getAttackDelay();
		}

		return defs.getAttackDelay();
	
	}

}
