package com.rs.game.world.entity.npc.dummy;

import java.util.Random;

import com.rs.game.item.Item;

import com.rs.game.map.Position;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.CombatDefinitions;

import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.content.skills.Skills;

/**
 * @author Andy || ReverendDread Aug 2, 2017
 */
public class CombatDummy extends NPC {

	private int spellcasterGloves;
	private int max_hit;
	
	/**
	 * Constructs a new object.
	 * @param id
	 * @param tile
	 */
	public CombatDummy(int id, Position tile) {
		super(id, tile, -1, true, true);
		setCantFollowUnderCombat(true);
		setCantWalk(true);
		setCanRetaliate(false);
		setRandomWalk(false);
		setCanBeFrozen(false);
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		if (isUnderCombat()) {
			heal(10000);
		}
		setRandomWalk(false);
	}

	/**
	 * Get's the player's max hit with their current combat style based on paramaters, used for RANGE & MELEE weapons.
	 * @param player
	 * 				{@link Player} the player.
	 * @param weaponId
	 * 				the weapon's id.
	 * @param attackStyle
	 * 				the current attack style.
	 * @param ranging
	 * 				is the player ranging; if so true.
	 * @param usingSpec
	 * 				is the player specing; if so true.
	 * @param specMultiplier
	 * 				the special attack damage multiplier.
	 * @return
	 */
	public final int getMaxHit(Player player, int weaponId, int attackStyle, boolean ranging, boolean usingSpec, double specMultiplier) {
		if (!ranging) {
			double strengthLvl = player.getSkills().getLevel(Skills.STRENGTH);
			int xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
			double styleBonus = xpStyle == Skills.STRENGTH ? 3 : xpStyle == CombatDefinitions.SHARED ? 1 : 0;
			double otherBonus = 1;
			if (PlayerCombat.fullDharokEquipped(player)) {
				double hp = player.getHitpoints();
				double maxhp = player.getMaxHitpoints();
				double d = hp / maxhp;
				otherBonus = 2 - d;
			}
			double effectiveStrength = 8 + Math.floor((strengthLvl * player.getPrayer().getStrengthMultiplier()) + styleBonus);
			if (PlayerCombat.fullVoidEquipped(player, 11665, 11676))
				effectiveStrength = Math.floor(effectiveStrength * 1.1);
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS];
			double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
			return (int) Math.floor(baseDamage * specMultiplier * otherBonus);
		} else {
			double rangedLvl = player.getSkills().getLevel(Skills.RANGE);
			double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
			double effectiveStrenght = Math.floor(rangedLvl	* player.getPrayer().getRangeMultiplier()) + styleBonus;
			if (PlayerCombat.fullVoidEquipped(player, 11664, 11675))
				effectiveStrenght += Math.floor((player.getSkills().getLevelForXp(Skills.RANGE) / 5) + 1.6);
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGED_STR_BONUS];
			double baseDamage = 5 + (((effectiveStrenght + 8) * (strengthBonus + 64)) / 64);
			return (int) Math.floor(baseDamage * specMultiplier);
		}
	}
		
	/**
	 * Get's the players max magic hit.
	 * @param player
	 * 				{@link Player} the player.
	 * @param baseDamage
	 * 				the attack base damage.
	 * @return
	 */
	public final int getMagicMaxHit(Player player, int baseDamage) {
		Item gloves = player.getEquipment().getItem(Equipment.SLOT_HANDS);
		int spellId = player.getCombatDefinitions().getSpellId();
		
		spellcasterGloves = gloves != null && gloves.getDefinitions().getName().contains("Spellcaster glove") 
				&& player.getEquipment().getWeaponId() == -1 && new Random().nextInt(30) == 0 ? spellId : -1;

		double EA = (Math.round(player.getSkills().getLevel(Skills.MAGIC) * player.getPrayer().getMageMultiplier()) + 11);
		if (PlayerCombat.fullVoidEquipped(player, new int[] { 11663, 11674 }))
			EA *= 1.3;
		EA *= player.getAuraManager().getMagicAccurayMultiplier();
		double ED = 0, DB;
		DB = ED = this.getBonuses() != null ? this.getBonuses()[CombatDefinitions.MAGIC_DEF] / 1.6 : 0;
		double A = (EA * (1 + player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK]) / 64);
		double D = (ED * (1 + DB)) / 64;
		double accuracy = 0.05;
		if (A < D)
			accuracy = (A - 1) / (2 * D);
		else if (A > D)
			accuracy = 1 - (D + 1) / (2 * A);
		if (accuracy < Math.random()) {
			return 0;
		}
		max_hit = baseDamage;
		double boost = 1 + ((player.getSkills().getLevel(Skills.MAGIC) - player.getSkills().getLevelForXp(Skills.MAGIC)) * 0.03);
		if (boost > 1)
			max_hit *= boost;
		double magicPerc = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE];
		if (spellcasterGloves > 0)
			if (baseDamage > 60 || spellcasterGloves == 28 || spellcasterGloves == 25)
				magicPerc += 17;
		boost = magicPerc / 100 + 1;
		max_hit *= boost;
		return (int) Math.floor(max_hit);
	}
	
	public final int getSpellBaseDamage(Player player, int spellId) {
		int spellBook = player.getCombatDefinitions().getSpellBook();
		if (spellBook == 192) { //normal spells
			switch (spellId) {
				/*******Strike*******/	
				case 25: //air strike
					return 90;
				case 28: //water strike
					return 100;
				case 30: //earth strike
					return 110;
				case 32: //fire strike	
					return 120;
				/********Bolt*******/	
				case 34: //air bolt
					return 120;
				case 39: //water bolt
					return 130;
				case 42: //earth bolt
					return 140;
				case 45: //fire bolt
					return 150;
				/*******Blast********/	
				case 49: //air blast
					return 130;				
				case 52: //water blast
					return 140;
				case 58: //earth blast
					return 150;			
				case 63: //fire blast
					return 160;	
				/*******Wave********/
				case 70: //air wave
					return 170;
				case 73: //water wave
					return 180;
				case 77: //earth wave
					return 190;
				case 80: //fire wave
					return 200;
				/*******Surge******/	
				case 84: //air surge
					return 220;
				case 87: //water surge
					return 240;
				case 89: //earth surge
					return 260;
				case 91: //fire surge
					return 280;
				/****God Spells*****/
				case 66: //Saradomin strike
				case 67: //Claws of guthix
				case 68: //Flames of zamorak
					return 300;	
					
				/******Special******/
				case 86: //teleblock
					return 30;		
				case 36: //bind
					return 20;
				case 55: //snare
					return 30;
				case 81: //entangle
					return 50;
				case 99: //armadyl storm
					int boost = (player.getSkills().getLevelForXp(Skills.MAGIC) - 77) * 5;
					return 280 + boost;
					
				default:
					return 0;
			}
		} else if (spellBook == 193) { //ancient spells
			switch (spellId) {
				/********Rush********/
				case 20: //ice rush
					return 180;
				case 28: //smoke rush
					return 150;
				case 24: //blood rush
					return 170;
				case 32: //shadow rush
					return 160;
				case 36: //Miasmic rush
					return 200;
				/*******Burst*******/
				case 30: //smoke burst
					return 190;
				case 34: //shadow burst
					return 200;
				case 26: //blood burst
					return 210;
				case 22: //ice burst
					return 220;
				case 38: //Miasmic burst
					return 240;
				/*******Blitz*******/
				case 29: //smoke blitz
					return 230;
				case 33: //shadow blitz
					return 240;
				case 25: //blood blitz
					return 250;
				case 21: //ice blitz
					return 260;
				case 37: //Miasmic blitz
					return 280;
				/******Barrage******/
				case 31: //smoke barrage
					return 270;
				case 35: //shadow barrage
					return 280;
				case 27: //blood barrage
					return 290;
				case 23: //ice barrage
					return 300;
				case 39: //Miasmic barage
					return 320;
				default:
					return 0;
			}
		} else {
			return 0;
		}
	}
}
