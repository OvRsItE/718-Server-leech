package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.dungeonnering.FamishedEye;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import java.util.LinkedList;
import java.util.List;

public class FamishedEyeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 12436, 12451, 12466 };
	}

	@Override
	public int attack(NPC npc, final Entity target) {
		final FamishedEye eye = (FamishedEye) npc;

		if (eye.isInactive())
			return 0;
		else if (!eye.isFirstHit()) {
			eye.setFirstHit(true);
			return Utils.random(5, 15);
		}

		npc.animate(new Animation(14916));
		WorldTasksManager.schedule(new WorldTask() {

			private List<Position> tiles;
			private Position targetTile;

			int cycles;

			@Override
			public void run() {
				cycles++;
				if (cycles == 1) {
					tiles = new LinkedList<Position>();
					targetTile = new Position(target);
					World.sendProjectile(eye, targetTile, 2849, 35, 30, 41, 0, 15, 0);
				} else if (cycles == 2) {
					for (int x = -1; x < 2; x++) {
						for (int y = -1; y < 2; y++) {
							Position attackedTile = targetTile.transform(x, y, 0);
							if (x != y)
								World.sendProjectile(eye, targetTile, attackedTile, 2851, 35, 0, 26, 40, 16, 0);
							tiles.add(attackedTile);
						}
					}
				} else if (cycles == 3) {
					for (Position tile : tiles) {
						if (!tile.matches(targetTile))
							World.sendGraphics(eye, new Graphics(2852, 35, 5), tile);
						for (Entity t : eye.getPossibleTargets()) {
							if (t.matches(tile))
								t.applyHit(new Hit(eye, (int) Utils.random(eye.getMaxHit() * .25, eye.getMaxHit()), HitLook.REGULAR_DAMAGE));
						}
					}
					tiles.clear();
					stop();
					return;
				}
			}
		}, 0, 0);
		return (int) Utils.random(5, 35);
	}
}
