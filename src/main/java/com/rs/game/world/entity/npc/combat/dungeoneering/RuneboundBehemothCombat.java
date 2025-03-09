package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.RuneboundBehemoth;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import java.util.LinkedList;
import java.util.List;

public class RuneboundBehemothCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ "Runebound behemoth" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final RuneboundBehemoth boss = (RuneboundBehemoth) npc;
		final DungeonManager manager = boss.getManager();

		boolean trample = false;
		for (Entity t : npc.getPossibleTargets()) {
			if (Utils.colides(t.getX(), t.getY(), t.getSize(), npc.getX(), npc.getY(), npc.getSize())) {
				trample = true;
				delayHit(npc, 0, t, getRegularHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, t)));
				if (t instanceof Player)
					((Player) t).getPackets().sendGameMessage("The beast tramples you.");
			}
		}
		if (trample) {
			npc.animate(new Animation(14426));
			return 5;
		}

		if (Utils.random(15) == 0) {// Special attack
			final List<Position> explosions = new LinkedList<Position>();
			boss.setNextForceTalk(new ForceTalk("Raaaaaaaaaaaaaaaaaaaaaaaaaawr!"));
			WorldTasksManager.schedule(new WorldTask() {

				int cycles;

				@Override
				public void run() {
					cycles++;
					if (cycles == 1) {
						boss.setNextGraphics(new Graphics(2769));
					} else if (cycles == 4) {
						boss.setNextGraphics(new Graphics(2770));
					} else if (cycles == 5) {
						boss.setNextGraphics(new Graphics(2771));
						for (Entity t : boss.getPossibleTargets()) {
							tileLoop: for (int i = 0; i < 4; i++) {
								Position tile = Utils.getFreeTile(t, 2);
								if (!manager.isAtBossRoom(tile))
									continue tileLoop;
								explosions.add(tile);
								World.sendProjectile(boss, tile, 2414, 120, 0, 20, 0, 20, 0);
							}
						}
					} else if (cycles == 8) {
						for (Position tile : explosions)
							World.sendGraphics(boss, new Graphics(2399), tile);
						for (Entity t : boss.getPossibleTargets()) {
							tileLoop: for (Position tile : explosions) {
								if (t.getX() != tile.getX() || t.getY() != tile.getY())
									continue tileLoop;
								t.applyHit(new Hit(boss, (int) Utils.random(boss.getMaxHit() * .6, boss.getMaxHit()), HitLook.REGULAR_DAMAGE));
							}
						}
						boss.resetTransformation();
						stop();
						return;
					}
				}
			}, 0, 0);
			return 8;
		}
		int[] possibleAttacks = new int[]
		{ 0, 1, 2 };
		if (target instanceof Player) {
			Player player = (Player) target;
			if (player.getPrayer().isMeleeProtecting())
				possibleAttacks = new int[]
				{ 1, 2 };
			else if (player.getPrayer().isRangeProtecting())
				possibleAttacks = new int[]
				{ 0, 1 };
			else if (player.getPrayer().isMageProtecting())
				possibleAttacks = new int[]
				{ 0, 2 };
		}
		boolean distanced = !Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0);
		int attack = possibleAttacks[Utils.random(possibleAttacks.length)];
		if (attack == 0 && distanced)
			attack = possibleAttacks[1];
		switch (attack) {
		case 0://melee
			boss.animate(new Animation(14423));
			delayHit(npc, 0, target, getMeleeHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			break;
		case 1://green exploding blob attack (magic)
			boss.animate(new Animation(14427));
			//boss.setNextGraphics(new Graphics(2413));
			World.sendProjectile(npc, target, 2414, 41, 16, 50, 40, 0, 0);
			delayHit(npc, 1, target, getMagicHit(npc, getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
			target.setNextGraphics(new Graphics(2417, 80, 0));
			break;
		case 2://green blob attack (range)
			boss.animate(new Animation(14424));
			boss.setNextGraphics(new Graphics(2394));
			World.sendProjectile(npc, target, 2395, 41, 16, 50, 40, 0, 2);
			delayHit(npc, 1, target, getRangeHit(npc, getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
			target.setNextGraphics(new Graphics(2396, 80, 0));
			break;
		}
		return 6;
	}
}
