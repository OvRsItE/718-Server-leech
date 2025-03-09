package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.ItemConstants;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public class BloodRevenantCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 16106,16107,16108,16109,16110,16111};
	}

	public int getMagicAnimation(NPC npc) {
		switch(npc.getId()) {
		case 16106: return 7515;
		case 16107: return 7498;
		case 16108: return 7505;
		case 16109: return 7515;
		case 16110: return 7508;
		case 16111:
			default:
				//melee emote, better than 0
				return npc.getCombatDefinitions().getAttackEmote(); 
		}
	}
	
	public int getRangeAnimation(NPC npc) {
		switch(npc.getId()) {
		case 16106: return 7501;
		case 16107: return 7512;
		case 16108: return 7518;
		case 16109: return 7514;
		case 16110: return 7522;
		case 16111:
			default:
				//melee emote, better than 0
				return npc.getCombatDefinitions().getAttackEmote(); 
		}
	}
	
	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if(npc.getHitpoints() < npc.getMaxHitpoints()/2 && Utils.random(5) == 0)  //if lower than 50% hp, 1/5 prob of healing 10%
			npc.heal(10);
		
		int attackStyle = Utils.random(3);
		if (attackStyle == 2) { //checks if can melee
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			int size = npc.getSize();
			if((distanceX > size || distanceX < -1 || distanceY > size|| distanceY < -1)) 
				attackStyle = Utils.random(2);
		}
		if(attackStyle != 2 && target instanceof Player) {
			Player targetPlayer = (Player) target;
			targetPlayer.getPackets().sendSound(202, 0, 1);
		}
		int damage;
		switch(attackStyle) {
		case 0: //magic
			damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MAGE, target);
			if (target instanceof Player) {
				if (negatesDamage(target.getAsPlayer())) {
					damage = 0;
				}
			}
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			World.sendProjectile(npc, target, 4014, 34, 16, 30, 35, 16, 0);
			if(damage > 0) {
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						target.setNextGraphics(new Graphics(4014, 0, 100));
						if(Utils.random(5) == 0) { //1/5 prob freezing while maging
							target.setNextGraphics(new Graphics(363));
							target.addFreezeDelay(30000);
						}
					}

				}, 2);
			}
			npc.animate(new Animation(getMagicAnimation(npc)));
		break;
		case 1: //range
			damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.RANGE, target);
			if (target instanceof Player) {
				if (negatesDamage(target.getAsPlayer())) {
					damage = 0;
				}
			}
			delayHit(npc, 2, target, getRangeHit(npc, damage));
			World.sendProjectile(npc, target, 4014, 34, 16, 30, 35, 16, 0);
			npc.animate(new Animation(getRangeAnimation(npc)));
		break;
		case 2: //melee
			damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target);
			if (target instanceof Player) {
				if (negatesDamage(target.getAsPlayer())) {
					damage = 0;
				}
			}
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
			npc.animate(new Animation(defs.getAttackEmote()));
		break;
		}
		return defs.getAttackDelay();
	}
	
	private boolean negatesDamage(final Player player) {
		Integer charges = player.getCharges().getCharges().get(29258);
		if (player.getEquipment().getGlovesId() == 29258 && charges != null) {
			if (charges.intValue() > 0) {
				player.getCharges().degrade(29258, ItemConstants.getItemDefaultCharges(29258), Equipment.SLOT_HANDS);
				return true;
			}
		}	
		return false;
	}
}
