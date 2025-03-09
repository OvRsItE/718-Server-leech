package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.HopeDevourer;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.NewForceMovement;
import com.rs.utility.Utils;

public class HopeDevourerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 12886 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final HopeDevourer boss = (HopeDevourer) npc;
		final DungeonManager manager = boss.getManager();

		boolean stomp = false;
		for (Player player : manager.getParty().getTeam()) {
			if (Utils.colides(player.getX(), player.getY(), player.getSize(), npc.getX(), npc.getY(), npc.getSize())) {
				stomp = true;
				delayHit(npc, 0, player, getRegularHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, player)));
			}
		}
		if (stomp) {
			npc.animate(new Animation(14459));
			return 6;
		}

		if (Utils.random(10) == 0) {
			npc.setNextForceTalk(new ForceTalk("Grrrrrrrrrroooooooooaaaarrrrr"));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					npc.animate(new Animation(14460));
					npc.setNextGraphics(new Graphics(2844, 30, 0));
					int healedDamage = 0;
					for (Entity t : npc.getPossibleTargets()) {
						Player player = (Player) t;
						int damage = (int) Utils.random(npc.getMaxHit(NPCCombatDefinitions.MAGE) * .85, npc.getMaxHit(NPCCombatDefinitions.MAGE));
						/*if (damage > 0 && player.getPrayer().isUsingProtectionPrayer()) {
							healedDamage += damage;
							player.setPrayerDelay(1000);
							t.setNextGraphics(new Graphics(2845, 75, 0));
							delayHit(npc, 0, t, getMagicHit(npc, damage));
						}*/
					}
					npc.heal(healedDamage);
				}
			}, 2);
			return 8;
		}

		if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0))
			return 0;

		if (Utils.random(5) == 0) {
			npc.animate(new Animation(14458));
			final int damage = (int) Utils.random(npc.getMaxHit(NPCCombatDefinitions.MELEE) * .85, npc.getMaxHit(NPCCombatDefinitions.MELEE));
			if (target instanceof Player) {
				Player player = (Player) target;
				player.getSkills().set(Skills.DEFENCE, (int) (player.getSkills().getLevel(Skills.DEFENCE) - (damage * .05)));
			}
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
			WorldTasksManager.schedule(new WorldTask() {
				private int ticks;
				private Position tile;

				@Override
				public void run() {
					ticks++;
					if (ticks == 1) {
						if (target instanceof Player) {
							Player player = (Player) target;
							player.lock(2);
							player.stopAll();
						}
						byte[] dirs = Utils.getDirection(npc.getDirection());
						for (int distance = 2; distance >= 0; distance--) {
							tile = new Position(new Position(target.getX() + (dirs[0] * distance), target.getY() + (dirs[1] * distance), target.getZ()));
							if (World.isFloorFree(tile.getZ(), tile.getX(), tile.getY()) && manager.isAtBossRoom(tile))
								break;
							else if (distance == 0)
								tile = new Position(target);
						}
						target.faceEntity(boss);
						target.animate(new Animation(10070));
						target.setNextForceMovement(new NewForceMovement(target, 0, tile, 2, target.getDirection()));
					} else if (ticks == 2) {
						target.setNextPosition(tile);
						stop();
						return;
					}
				}
			}, 0, 0);
		} else {
			npc.animate(new Animation(14457));
			int damage = (int) Utils.random(npc.getMaxHit(NPCCombatDefinitions.MELEE) * .75, npc.getMaxHit(NPCCombatDefinitions.MELEE));
			if (target instanceof Player) {
				Player player = (Player) target;
				if (player.getPrayer().isMeleeProtecting()) {
					player.getPackets().sendGameMessage("Your prayer completely negates the attack.", true);
					damage = 0;
				}
			}
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
		}
		return 6;
	}
}
