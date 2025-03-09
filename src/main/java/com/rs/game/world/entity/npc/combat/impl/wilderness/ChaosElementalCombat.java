package com.rs.game.world.entity.npc.combat.impl.wilderness;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.network.decoders.handlers.ButtonHandler;
import com.rs.utility.Utils;
import com.rs.game.world.entity.player.Player;

public class ChaosElementalCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3200 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.random(8);
		if (attackStyle == 0 && (target instanceof NPC || !((Player) target).getInventory().hasFreeSlots()))
			attackStyle = 1 + Utils.random(2);
		npc.animate(new Animation(defs.getAttackEmote()));
		switch (attackStyle) {
		case 0: // remove item
			Projectile removeItem = new Projectile(558, 41, 41, 15, 2, 12, 0);
			World.sendProjectile(npc, target, removeItem);
			
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					Player player = (Player) target;
					int freeSlots = player.getInventory().getFreeSlots();
					if (freeSlots > 4)
						freeSlots = 4;
					for (int i = 0; i < player.getEquipment().getItems().getSize() && freeSlots > 0; i++) {
						Item item = player.getEquipment().getItem(i);
						if (item != null) {
							freeSlots--;
							ButtonHandler.sendRemove(player, i);
						}
					}
				}

			}, Utils.projectileTimeToCycles(removeItem.getDuration(target.getPosition(), npc.getPosition()) - 1));
			break;
		case 1: // teleport
			Projectile teleport = new Projectile(2947, 41, 41, 15, 2, 12, 0);
			World.sendProjectile(npc, target, teleport);
			
			// projectile here
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					while (true) {
						Position tile = new Position(target, 15);
						if (!World.isTileFree(tile.getZ(), tile.getX(), tile.getY(), 1))
							continue;
						target.setNextPosition(tile);
						break;
					}
				}

			}, Utils.projectileTimeToCycles(teleport.getDuration(target.getPosition(), npc.getPosition())) - 1);
			break;
		default: // attack
			int attack = Utils.random(3); // melee range mage
			if (target instanceof Player) {
				Player player = (Player) target;
				int prayer = player.getPrayer().isMeleeProtecting() ? 0 : player.getPrayer().isRangeProtecting() ? 1 : player.getPrayer().isMageProtecting() ? 2 : -1;
				if (prayer == attack)
					attack = (attack - 1) & 0x3; // to make sure its positive
				// between 0 and 2 lol
			}
			int damage =  getRandomMaxHit(npc, npc.getMaxHit(),attack, target);
			Projectile attackSpeel = new Projectile(552, 41, 41, 20, 15, 12, 0);
			World.sendProjectile(npc, target, attackSpeel);
			delayHit(npc, Utils.projectileTimeToCycles(attackSpeel.getDuration(npc.getPosition(), target.getPosition())) - 1, target, attack == NPCCombatDefinitions.MELEE ? getMeleeHit(npc, damage) : attack == NPCCombatDefinitions.RANGE ? getRangeHit(npc, damage) : getMagicHit(npc, damage));
			break;
		}
		return npc.getCombatDefinitions().getAttackDelay();
	}

}
