package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.WarpedGulega;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import java.util.LinkedList;
import java.util.List;

public class WarpedGulegaCombat extends CombatScript {

	private static final Graphics MELEE = new Graphics(2878);

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 12737 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final WarpedGulega boss = (WarpedGulega) npc;

		int style = Utils.random(4);
		switch (style) {
		case 3://reg aeo melee
			npc.animate(new Animation(15004));

			final List<Position> attackTiles = new LinkedList<Position>();
			for (Entity t : boss.getPossibleTargets(true, true))
				attackTiles.add(new Position(t));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (Position tile : attackTiles)
						World.sendGraphics(npc, MELEE, tile);
					for (Entity t : boss.getPossibleTargets(true, true)) {
						tileLoop: for (Position tile : attackTiles) {
							if (t.getX() == tile.getX() && t.getY() == tile.getY()) {
								delayHit(npc, 0, t, getMeleeHit(npc, getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.MELEE) * 0.75), NPCCombatDefinitions.MELEE, t)));
								break tileLoop;
							}
						}
					}
				}
			});
			break;
		case 1://reg range aeo
			npc.animate(new Animation(15001));
			npc.setNextGraphics(new Graphics(2882));
			for (Entity t : npc.getPossibleTargets(true, true)) {
				World.sendProjectile(npc, t, 2883, 75, 25, 30, 20, 15, 3);
				t.setNextGraphics(new Graphics(2884, 90, 0));
				delayHit(npc, 2, t, getRangeHit(npc, getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.MELEE) * 0.75), NPCCombatDefinitions.RANGE, t)));
			}
			break;
		case 2://reg magic aeo
			npc.animate(new Animation(15007));
			for (Entity t : npc.getPossibleTargets(true, true)) {
				World.sendProjectile(npc, t, 2880, 150, 75, 30, 35, 15, 1);
				t.setNextGraphics(new Graphics(2881, 90, 0));
				delayHit(npc, 2, t, getMagicHit(npc, getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.MELEE) * 0.75), NPCCombatDefinitions.MAGE, t)));
			}
			break;
		case 0:
			npc.animate(new Animation(15004));
			WorldTasksManager.schedule(new WorldTask() {

				Position center;
				int cycles;

				@Override
				public void run() {
					cycles++;

					if (cycles == 1) {
						center = new Position(target);
						sendTenticals(boss, center, 2);
					} else if (cycles == 3)
						sendTenticals(boss, center, 1);
					else if (cycles == 5)
						sendTenticals(boss, center, 0);
					else if (cycles == 6) {
						for (Entity t : npc.getPossibleTargets(true, true)) {
							if (t.getX() == center.getX() && t.getY() == center.getY())
								t.applyHit(new Hit(npc, t.getHitpoints() - 1, HitLook.REGULAR_DAMAGE));
						}
						stop();
						return;
					}
				}
			}, 0, 0);
			return 7;
		}
		return 4;
	}

	private void sendTenticals(NPC npc, Position center, int stage) {
		if (stage == 0) {
			World.sendGraphics(npc, MELEE, center);
		} else if (stage == 2 || stage == 1) {
			World.sendGraphics(npc, MELEE, center.transform(-stage, stage, 0));
			World.sendGraphics(npc, MELEE, center.transform(stage, stage, 0));
			World.sendGraphics(npc, MELEE, center.transform(-stage, -stage, 0));
			World.sendGraphics(npc, MELEE, center.transform(stage, -stage, 0));
		}
	}
}
