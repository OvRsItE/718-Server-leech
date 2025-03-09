package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.Blink;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

import java.util.LinkedList;
import java.util.List;



public class BlinkCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 12865 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final Blink boss = (Blink) npc;
		DungeonManager manager = boss.getManager();

		if (Utils.random(10) == 0 || boss.isSpecialRequired()) {
			boss.setSpecialRequired(false);
			boss.setNextForceTalk(new ForceTalk("H...Here it comes..."));
			//boss.playSoundEffect(2989);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					boss.animate(new Animation(14956));
					boss.setNextForceTalk(new ForceTalk("Kapow!!"));
					//boss.playSoundEffect(3002);
					for (Entity t : boss.getPossibleTargets()) {
						if (t instanceof Player)
							((Player) t).getPackets().sendGameMessage("You are hit by a powerful magical blast.");
						t.setNextGraphics(new Graphics(2855, 0, 50));
						delayHit(boss, 0, t, new Hit(boss, (int) Utils.random(boss.getMaxHit() * .6D, boss.getMaxHit()), HitLook.MAGIC_DAMAGE));
					}
				}
			}, 5);
			return 8;
		}

		boolean atDistance = !Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0);
		if (atDistance || Utils.random(3) == 0) {
			boolean rangeAttack = Utils.random(3) == 0;

			if (rangeAttack) {
				if (manager.getParty().getTeam().size() > 1 || Utils.random(3) == 0) {
					Position beginningTile = boss.getNextPath();
					boss.animate(new Animation(14949));
					boss.resetCombat();
					boss.setNextFaceEntity(null);
					boss.setNextFacePosition(beginningTile);//Faces the direction it throws into
					World.sendProjectile(boss, beginningTile, 2853, 18, 18, 50, 50, 0, 0);
					WorldTasksManager.schedule(new WorldTask() {

						private List<Position> knifeTargets;
						private int cycles;

						@Override
						public void run() {
							cycles++;
							if (cycles == 1) {
								knifeTargets = new LinkedList<Position>();
								for (Entity t : boss.getPossibleTargets()) {
									Position center = new Position(t);
									for (int i = 0; i < 3; i++)
										knifeTargets.add(i == 0 ? center : Utils.getFreeTile(center, 1));
								}
							} else if (cycles == 2) {
								for (Position tile : knifeTargets) {
									//outdated method projectile
									int delay = 3;
									entityLoop: for (Entity t : boss.getPossibleTargets()) {
										if (!t.matches(tile))
											continue entityLoop;
										World.sendProjectile(beginningTile, t, 2853, 18, 18, 50, 50, 0, 0);
										delayHit(boss, delay, t, getRangeHit(boss, getRandomMaxHit(boss, boss.getMaxHit(), NPCCombatDefinitions.RANGE, t)));
									}
								}
								npc.setTarget(target);
								npc.faceEntity(target);
								stop();
								return;
							}
						}
					}, 0, 0);
				} else {
					boss.animate(new Animation(14949));
					World.sendProjectile(boss, target, 2853, 18, 18, 50, 50, 0, 0);
					delayHit(boss, 1, target, getRangeHit(boss, getRandomMaxHit(boss, boss.getMaxHit(), NPCCombatDefinitions.RANGE, target)));
				}
			} else {
				if (Utils.random(7) == 0) {
					boss.setNextForceTalk(new ForceTalk("Magicinyaface!"));
					//boss.playSoundEffect(3022);//MAGIC IN YA FACE
				}
				boss.animate(new Animation(14956));
				boss.setNextGraphics(new Graphics(2854));
				target.setNextGraphics(new Graphics(2854, 5, 0));
				int damage = getRandomMaxHit(boss, boss.getMaxHit(), NPCCombatDefinitions.MAGE, target);
				if (target instanceof Player) {
					if (((Player) target).getPrayer().isMageProtecting())
						damage *= .5D;
				}
				delayHit(boss, 1, target, getMagicHit(boss, damage));
			}
			return 5;
		} else {
			boss.animate(new Animation(12310));
			delayHit(boss, 0, target, getMeleeHit(boss, getRandomMaxHit(boss, boss.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
			return 4;
		}
	}
}
