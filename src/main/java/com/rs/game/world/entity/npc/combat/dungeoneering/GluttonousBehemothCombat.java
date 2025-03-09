package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.DungeonBoss;
import com.rs.game.world.entity.npc.dungeonnering.GluttonousBehemoth;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class GluttonousBehemothCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ "Gluttonous behemoth" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		DungeonBoss boss = (DungeonBoss) npc;
		DungeonManager manager = boss.getManager();
		boolean lessThanHalf = npc.getHitpoints() < npc.getMaxHitpoints() * .5;
		if (lessThanHalf && npc.getTemporaryAttributtes().get(TemporaryAttributes.Key.GLUTTONOUS_HEALING) == null) {
			RoomReference reference = manager.getCurrentRoomReference(npc);
			WorldObject food1 = manager.getObject(reference, 49283, 0, 11);
			WorldObject food2 = manager.getParty().getTeam().size() <= 1 ? null : manager.getObject(reference, 49283, 11, 11);
			WorldObject food = food1;
			if (food1 != null) {
				for (Player player : manager.getParty().getTeam()) {
					if (player.withinDistance(food1, food1.getDefinitions().getSizeX() + 1)) {
						food = null;
						break;
					}
				}
			}
			if (food == null && food2 != null) {
				food = food2;
				for (Player player : manager.getParty().getTeam()) {
					if (player.withinDistance(food2, food1.getDefinitions().getSizeX() + 1)) {
						food = null;
						break;
					}
				}
			}
			if (food != null) {
				npc.getTemporaryAttributtes().put(TemporaryAttributes.Key.GLUTTONOUS_HEALING, true);
				((GluttonousBehemoth) npc).setHeal(food);
				return 0;
			}
		}
		boolean stomp = false;
		for (Player player : manager.getParty().getTeam()) {
			if (Utils.colides(player.getX(), player.getY(), player.getSize(), npc.getX(), npc.getY(), npc.getSize())) {
				stomp = true;
				delayHit(npc, 0, player, getRegularHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, player)));
			}
		}
		if (stomp) {
			npc.animate(new Animation(13718));
			return npc.getAttackSpeed();
		}
		int attackStyle = Utils.getRandom(2);
		if (attackStyle == 2) {
			if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0))
				attackStyle = Utils.getRandom(1);
			else {
				npc.animate(new Animation(defs.getAttackEmote()));
				delayHit(npc, 0, target, getMeleeHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
				return npc.getAttackSpeed();
			}
		}
		//TODO projectile
		if (attackStyle == 0) {
			npc.animate(new Animation(13719));
			//World.sendProjectile(new NewProjectile(/*npc*/new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), 0), target, 2612, 5, 16, 35, 16, 41, 2));
			int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, target);
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			if (damage != 0) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextGraphics(new Graphics(2613));
					}
				}, 1);
			}
		} else if (attackStyle == 1) {
			npc.animate(new Animation(13721));
			//World.sendProjectile(new NewProjectile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), 0), target, 2610, 10, 16, 35, 16, 41, 2));
			delayHit(npc, 2, target, getRangeHit(npc, getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					target.setNextGraphics(new Graphics(2611));
				}
			}, 1);
		}
		return npc.getAttackSpeed();
	}
}
