package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.Stomp;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class StompCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ "Stomp" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();

		Stomp stomp = (Stomp) npc;

		if (npc.getHitpoints() <= 10 || npc.getHitpoints() < (npc.getMaxHitpoints() * (2 - stomp.getStage()) * 0.33)) {
			stomp.charge();
			return npc.getAttackSpeed();
		}
		// 0 - first 33%
		//1 - 66-33%
		//2 - 33-0%
		//3 - 0%

		if (stomp.getStage() > 1 && Utils.random(10) == 0) {
			final Position tile = new Position(target);
			World.sendGraphics(npc, new Graphics(2400), tile);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (Entity target : npc.getPossibleTargets()) {
						if (target.getX() == tile.getX() && target.getY() == tile.getY())
							target.applyHit(new Hit(npc, (int) (target.getMaxHitpoints() * 0.25), HitLook.RANGE_DAMAGE));
					}
				}
			}, 4);
		}

		int attackStyle = Utils.random(/*stomp.getStage() > 1 ? 4 : */stomp.getStage() > 0 ? 3 : 2);
		if (attackStyle == 0 && !Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0))
			attackStyle = 1;

		switch (attackStyle) {
		case 0:
			npc.animate(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			break;
		case 1:
			npc.animate(new Animation(13449));
			npc.setNextGraphics(new Graphics(2401));
			for (Entity t : npc.getPossibleTargets()) {
				World.sendProjectile(npc, t, 2402, 16, 16, 41, 30, 0, 0);
				t.setNextGraphics(new Graphics(2403, 70, 0));
				delayHit(npc, 1, t, getRangeHit(npc, getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
			}
			break;
		case 2:
			npc.animate(new Animation(13450));
			npc.setNextGraphics(new Graphics(2404));
			World.sendProjectile(npc, target, 2405, 30, 16, 41, 65, 0, 0);
			target.setNextGraphics(new Graphics(2406, 120, 0));
			delayHit(npc, 2, target, getMagicHit(npc, getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
			break;

		}
		return npc.getAttackSpeed();
	}
}
