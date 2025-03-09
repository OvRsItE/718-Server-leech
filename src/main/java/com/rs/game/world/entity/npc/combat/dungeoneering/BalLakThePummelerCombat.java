package com.rs.game.world.entity.npc.combat.dungeoneering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.dungeonnering.BalLakThePummeler;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class BalLakThePummelerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ "Bal'lak the Pummeller" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final BalLakThePummeler boss = (BalLakThePummeler) npc;
		final DungeonManager manager = boss.getManager();

		boolean smash = Utils.random(5) == 0 && boss.getPoisionPuddles().size() == 0;
		for (Player player : manager.getParty().getTeam()) {
			if (Utils.colides(player.getX(), player.getY(), player.getSize(), npc.getX(), npc.getY(), npc.getSize())) {
				smash = true;
				delayHit(npc, 0, player, getRegularHit(npc, getMaxHit(npc, NPCCombatDefinitions.MELEE, player)));
			}
		}
		if (smash) {
			npc.animate(new Animation(19561));
			npc.setNextForceTalk(new ForceTalk("Rrrraargh!"));
			//npc.playSoundEffect(3038);
			final Position center = manager.getRoomCenterTile(boss.getReference());
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (int i = 0; i < 3; i++)
						boss.addPoisionBubble(Utils.getFreeTile(center, 6));
				}
			}, 1);
			return npc.getAttackSpeed();
		}

		if (Utils.random(5) == 0) {
			boss.animate(new Animation(19579));
			for (Entity t : boss.getPossibleTargets()) {
				if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), t.getX(), t.getY(), t.getSize(), 0))
					continue;
				int damage = getMaxHit(npc, (int) NPCCombatDefinitions.MELEE, t);
				int damage2 = getMaxHit(npc, (int) NPCCombatDefinitions.MELEE, t);
				if (t instanceof Player) {
					Player player = (Player) t;
					if ((damage > 0 || damage2 > 0)) {
						player.setPrayerDelay(1000);
						player.getPackets().sendGameMessage("You are injured and currently cannot use protection prayers.");
					}
				}
				delayHit(npc, 0, t, getRegularHit(npc, damage), getRegularHit(npc, damage2));
			}
			return npc.getAttackSpeed();
		}

		switch (Utils.random(2)) {
		case 0://reg melee left

			final boolean firstHand = Utils.random(2) == 0;

			boss.animate(new Animation(firstHand ? 19562 : 19563));
			delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.MELEE) * 0.8), NPCCombatDefinitions.MELEE, target)));
			delayHit(npc, 2, target, getMeleeHit(npc, getRandomMaxHit(npc, (int) (npc.getMaxHit(NPCCombatDefinitions.MELEE) * 0.8), NPCCombatDefinitions.MELEE, target)));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					boss.animate(new Animation(firstHand ? 19563 : 19562));
				}

			}, 1);
			break;
		case 1://magic attack multi
			boss.animate(new Animation(19562));
			boss.setNextGraphics(new Graphics(2441));
			for (Entity t : npc.getPossibleTargets()) {
				World.sendProjectile(npc, t, 2872, 50, 30, 41, 40, 0, 0);
				delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, (int) (boss.getMaxHit() * 0.6), NPCCombatDefinitions.MAGE, t)));
			}
			return npc.getAttackSpeed() - 2;
		}

		return npc.getAttackSpeed();
	}
}
