package com.rs.game.world.entity.player.content.skills;

import com.rs.Constants;
import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.BuffManager;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.DoubleXpManager;
import com.rs.game.world.entity.player.content.dialogue.impl.LevelUp;
import com.rs.game.world.entity.player.controller.impl.Wilderness;
import com.rs.utility.Utils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class Skills implements Serializable {

	private static final long serialVersionUID = -7086829989489745985L;


	private boolean[] enabledSkillsTargets;
	private boolean[] skillsTargetsUsingLevelMode;
	private int[] skillsTargetsValues;


	public static final double MAXIMUM_EXP = 200000000;
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6,
			COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13,
			MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20,
			CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23, DUNGEONEERING = 24;

	public static final int ASSASSIN = 0, ASSASSIN_CALL = 1, FINAL_BLOW = 2, SWIFT_SPEED = 3, STEALTH_MOVES = 4;

	public static final String[] SKILL_NAME_ASSASSIN = { "Assassin", "Assassin Call", "Final Blow", "Swift Speed",
	"Stealth Moves" };

	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Constitution", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
			"Summoning", "Dungeoneering" };

	public short level[];
	private double xp[];
	private double[] xpTracks;
	private boolean[] trackSkills;
	private byte[] trackSkillsIds;
	private boolean xpDisplay, xpPopup;


	private transient int currentCounter;
	private transient Player player;

	private boolean virtualLevels;

	public void passLevels(Player p) {
		level = p.getSkills().level;
		xp = p.getSkills().xp;
	}

	/*This method allows you to add as many skills to check for 200mxp as you want*/
	public boolean hasMasterStat(int... skillId) {
		for (int id : skillId) {
			return getXp()[id] >= 104273167;
		}
		return false;
	}

	public long getTotalXp() {
		long totalxp = 0;
		for (double xp : getXp()) {
			totalxp += xp;
		}
		return totalxp;
	}

	public Skills() {
		level = new short[25];
		xp = new double[25];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
		enabledSkillsTargets = new boolean[25];
		skillsTargetsUsingLevelMode = new boolean[25];
		skillsTargetsValues = new int[25];
		xpPopup = true;
		xpTracks = new double[3];
		trackSkills = new boolean[3];
		trackSkillsIds = new byte[3];
		trackSkills[0] = true;
		for (int i = 0; i < trackSkillsIds.length; i++) {
			trackSkillsIds[i] = 30;
		}

	}

	public int getTargetIdByComponentId(int componentId) {
		switch (componentId) {
		case 150: // Attack
			return 0;
		case 9: // Strength
			return 1;
		case 40: // Range
			return 2;
		case 71: // Magic
			return 3;
		case 22: // Defence
			return 4;
		case 145: // Constitution
			return 5;
		case 58: // Prayer
			return 6;
		case 15: // Agility
			return 7;
		case 28: // Herblore
			return 8;
		case 46: // Theiving
			return 9;
		case 64: // Crafting
			return 10;
		case 84: // Runecrafting
			return 11;
		case 140: // Mining
			return 12;
		case 135: // Smithing
			return 13;
		case 34: // Fishing
			return 14;
		case 52: // Cooking
			return 15;
		case 130: // Firemaking
			return 16;
		case 125: // Woodcutting
			return 17;
		case 77: // Fletching
			return 18;
		case 90: // Slayer
			return 19;
		case 96: // Farming
			return 20;
		case 102: // Construction
			return 21;
		case 108: // Hunter
			return 22;
		case 114: // Summoning
			return 23;
		case 120: // Dungeoneering
			return 24;
		default:
			return -1;
		}
	}

	public int getSkillIdByTargetId(int targetId) {
		System.out.println(targetId);
		switch (targetId) {
		case 0: // Attack
			return ATTACK;
		case 1: // Strength
			return STRENGTH;
		case 2: // Range
			return RANGE;
		case 3: // Magic
			return MAGIC;
		case 4: // Defence
			return DEFENCE;
		case 5: // Constitution
			return HITPOINTS;
		case 6: // Prayer
			return PRAYER;
		case 7: // Agility
			return AGILITY;
		case 8: // Herblore
			return HERBLORE;
		case 9: // Thieving
			return THIEVING;
		case 10: // Crafting
			return CRAFTING;
		case 11: // Runecrafting
			return RUNECRAFTING;
		case 12: // Mining
			return MINING;
		case 13: // Smithing
			return SMITHING;
		case 14: // Fishing
			return FISHING;
		case 15: // Cooking
			return COOKING;
		case 16: // Firemaking
			return FIREMAKING;
		case 17: // Woodcutting
			return WOODCUTTING;
		case 18: // Fletching
			return FLETCHING;
		case 19: // Slayer
			return SLAYER;
		case 20: // Farming
			return FARMING;
		case 21: // Construction
			return CONSTRUCTION;
		case 22: // Hunter
			return HUNTER;
		case 23: // Summoning
			return SUMMONING;
		case 24: // Dungeoneering
			return DUNGEONEERING;
		default:
			return -1;
		}
	}

	public void refreshEnabledSkillsTargets() {

		//int value = Utils.get32BitValue(enabledSkillsTargets, true);

		// int value = 0; if (enabledSkillsTargets[0]) // Attack. value += 2; if (enabledSkillsTargets[1]) // Strength. value += 4; if (enabledSkillsTargets[2]) // Range. value += 8; if (enabledSkillsTargets[3]) // Magic. value += 16; if (enabledSkillsTargets[4]) // Defence. value += 32; if (enabledSkillsTargets[5]) // Constitution. value += 64; if (enabledSkillsTargets[6]) // Prayer. value += 128; if (enabledSkillsTargets[7]) // Agility. value += 256; if (enabledSkillsTargets[8]) // Herblore. value += 512; if (enabledSkillsTargets[9]) // Theiving. value += 1024; if (enabledSkillsTargets[10]) // Crafting. value += 2048; if (enabledSkillsTargets[11]) // Runecrafting. value += 4096; if (enabledSkillsTargets[12]) // Mining. value += 8192; if (enabledSkillsTargets[13]) // Smithing. value += 16384; if (enabledSkillsTargets[14]) // Fishing. value += 32768; if (enabledSkillsTargets[15]) // Cooking. value += 65536; if (enabledSkillsTargets[16]) // Firemaking. value += 131072; if (enabledSkillsTargets[17]) // Woodcutting. value += 262144; if (enabledSkillsTargets[18]) // Fletching. value += 524288; if (enabledSkillsTargets[19]) // Slayer. value += 1048576; if (enabledSkillsTargets[20]) // Farming. value += 2097152; if (enabledSkillsTargets[21]) // Construction. value += 4194304; if (enabledSkillsTargets[22]) // Hunter. value += 8388608; if (enabledSkillsTargets[23]) // Summoning. value += 16777216; if (enabledSkillsTargets[24]) // Dungeoneering. value += 33554432;

		//player.getPackets().sendConfig(1966, value);
	}

	public void refreshUsingLevelTargets() {
		//int value = Utils.get32BitValue(skillsTargetsUsingLevelMode, true);
		/*
		 * int value = 0; if (skillsTargetsUsingLevelMode[0]) // Attack. value += 2; if (skillsTargetsUsingLevelMode[1]) // Strength. value += 4; if (skillsTargetsUsingLevelMode[2]) // Range. value += 8; if (skillsTargetsUsingLevelMode[3]) // Magic. value += 16; if (skillsTargetsUsingLevelMode[4]) // Defence. value += 32; if (skillsTargetsUsingLevelMode[5]) // Constitution. value += 64; if (skillsTargetsUsingLevelMode[6]) // Prayer. value += 128; if (skillsTargetsUsingLevelMode[7]) // Agility. value += 256; if (skillsTargetsUsingLevelMode[8]) // Herblore. value += 512; if (skillsTargetsUsingLevelMode[9]) // Theiving. value += 1024; if (skillsTargetsUsingLevelMode[10]) // Crafting. value += 2048; if (skillsTargetsUsingLevelMode[11]) // Runecrafting. value += 4096; if (skillsTargetsUsingLevelMode[12]) // Mining. value += 8192; if (skillsTargetsUsingLevelMode[13]) // Smithing. value += 16384; if (skillsTargetsUsingLevelMode[14]) // Fishing. value += 32768; if (skillsTargetsUsingLevelMode[15]) // Cooking. value += 65536; if (skillsTargetsUsingLevelMode[16]) // Firemaking. value += 131072; if (skillsTargetsUsingLevelMode[17]) // Woodcutting. value += 262144; if (skillsTargetsUsingLevelMode[18]) // Fletching. value += 524288; if (skillsTargetsUsingLevelMode[19]) // Slayer. value += 1048576; if (skillsTargetsUsingLevelMode[20]) // Farming. value += 2097152; if (skillsTargetsUsingLevelMode[21]) // Construction. value += 4194304; if (skillsTargetsUsingLevelMode[22]) // Hunter. value += 8388608; if (skillsTargetsUsingLevelMode[23]) // Summoning. value += 16777216; if (skillsTargetsUsingLevelMode[24]) // Dungeoneering. value += 33554432;
		 */
		//player.getPackets().sendConfig(1968, value);
	}

	public void refreshSkillsTargetsValues() {
		for (int i = 0; i < 25; i++) {
			player.getPackets().sendConfig(1969 + i, skillsTargetsValues[i]);
		}
	}

	public void setSkillTargetEnabled(int id, boolean enabled) {
		enabledSkillsTargets[id] = enabled;
		refreshEnabledSkillsTargets();
	}

	public void setSkillTargetUsingLevelMode(int id, boolean using) {
		skillsTargetsUsingLevelMode[id] = using;
		refreshUsingLevelTargets();
	}

	public void setSkillTargetValue(int skillId, int value) {
		skillsTargetsValues[skillId] = value;
		refreshSkillsTargetsValues();
	}

	public void setSkillTarget(boolean usingLevel, int skillId, int target) {
		setSkillTargetEnabled(skillId, true);
		setSkillTargetUsingLevelMode(skillId, usingLevel);
		setSkillTargetValue(skillId, target);

	}

	public void sendXPDisplay() {
		for (int i = 0; i < trackSkills.length; i++) {
			player.getPackets().sendConfigByFile(10444 + i, trackSkills[i] ? 1 : 0);
			player.getPackets().sendConfigByFile(10440 + i, trackSkillsIds[i] + 1);
			refreshCounterXp(i);
		}
	}

	public void setupXPCounter() {
		player.getInterfaceManager().sendXPDisplay(1214);
	}

	public void refreshCurrentCounter() {
		player.getPackets().sendConfig(2478, currentCounter + 1);
	}

	public void setCurrentCounter(int counter) {
		if (counter != currentCounter) {
			currentCounter = counter;
			refreshCurrentCounter();
		}
	}

	public void switchTrackCounter() {
		trackSkills[currentCounter] = !trackSkills[currentCounter];
		player.getPackets().sendConfigByFile(10444 + currentCounter, trackSkills[currentCounter] ? 1 : 0);
	}

	public void resetCounterXP() {
		xpTracks[currentCounter] = 0;
		refreshCounterXp(currentCounter);
	}

	public void setCounterSkill(int skill) {
		xpTracks[currentCounter] = 0;
		trackSkillsIds[currentCounter] = (byte) skill;
		player.getPackets().sendConfigByFile(10440 + currentCounter, trackSkillsIds[currentCounter] + 1);
		refreshCounterXp(currentCounter);
	}

	public void refreshCounterXp(int counter) {
		player.getPackets().sendConfig(counter == 0 ? 1801 : 2474 + counter, (int) (xpTracks[counter] * 10));
	}

	public void handleSetupXPCounter(int componentId) {
		if (componentId == 18) {
			player.getInterfaceManager().sendXPDisplay();
		} else if (componentId >= 22 && componentId <= 24) {
			setCurrentCounter(componentId - 22);
		} else if (componentId == 27) {
			switchTrackCounter();
		} else if (componentId == 61) {
			resetCounterXP();
		} else if (componentId >= 31 && componentId <= 57) {
			if (componentId == 33) {
				setCounterSkill(4);
			} else if (componentId == 34) {
				setCounterSkill(2);
			} else if (componentId == 35) {
				setCounterSkill(3);
			} else if (componentId == 42) {
				setCounterSkill(18);
			} else if (componentId == 49) {
				setCounterSkill(11);
			} else {
				setCounterSkill(componentId >= 56 ? componentId - 27 : componentId - 31);
			}
		}

	}

	public void restoreSummoning() {
		level[23] = (short) getLevelForXp(23);
		refresh(23);
	}

	public void sendInterfaces() {
		if (xpDisplay) {
			player.getInterfaceManager().sendXPDisplay();
		}
		if (xpPopup) {
			player.getInterfaceManager().sendXPPopup();
		}
	}

	public void switchXPDisplay() {
		xpDisplay = !xpDisplay;
		if (xpDisplay) {
			player.getInterfaceManager().sendXPDisplay();
		} else {
			player.getInterfaceManager().closeXPDisplay();
		}
	}

	public void switchXPPopup() {
		xpPopup = !xpPopup;
		player.getPackets().sendGameMessage("XP pop-ups are now " + (xpPopup ? "en" : "dis") + "abled.");
		if (xpPopup) {
			player.getInterfaceManager().sendXPPopup();
		} else {
			player.getInterfaceManager().closeXPPopup();
		}
	}

	public void restoreSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		// temporary
		if (xpTracks == null) {
			xpPopup = true;
			xpTracks = new double[3];
			trackSkills = new boolean[3];
			trackSkillsIds = new byte[3];
			trackSkills[0] = true;
			for (int i = 0; i < trackSkillsIds.length; i++) {
				trackSkillsIds[i] = 30;
			}
		}
	}

	public short[] getLevels() {
		return level;
	}

	public double[] getXp() {
		return xp;
	}

	public int getLevel(int skill) {
		return level[skill];
	}

	public double getXp(int skill) {
		return xp[skill];
	}

	public boolean hasRequiriments(int... skills) {
		for (int i = 0; i < skills.length; i += 2) {
			int skillId = skills[i];
			if (skillId == CONSTRUCTION || skillId == FARMING) {
				continue;
			}
			int skillLevel = skills[i + 1];
			if (getLevelForXp(skillId) < skillLevel) {
				return false;
			}

		}
		return true;
	}

	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}

	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}

	public void set(int skill, int newLevel, boolean realLevel) {
		level[skill] = (short) newLevel;
		if(realLevel) {
			setXp(skill, getXPForLevel(newLevel));
		}
		refresh(skill);
	}

	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public void drain(int skill, double percent) {
		level[skill] = (short) Math.max(0, level[skill] * percent);
		refresh(skill);
	}

	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}

	public int getSummoningCombatLevel() {
		return getLevelForXp(Skills.SUMMONING) / 8;
	}

	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0) {
			return;
		}
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output - 1 >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}
	
	public int getVirtualLevel(int skill) {
		double experience = xp[skill];
		int total = 0;
		int output_experience = 0;
		for (int level = 1; level <= 120; level++) {
			total += Math.floor(level + 300.0 * Math.pow(2.0, level / 7.0));
			output_experience = (int) Math.floor(total / 4);
			if (output_experience - 1 >= experience) {
				return level;
			}
		}
		int actual_level = getLevel(skill);
		return actual_level > 120 ? actual_level : (actual_level < 120 && actual_level > 99 ? actual_level : 120);
	}

	public void init() {
		for (int skill = 0; skill < level.length; skill++) {
			refresh(skill);
		}
		sendXPDisplay();
		//refreshEnabledSkillsTargets();
		//refreshUsingLevelTargets();
		//refreshSkillsTargetsValues();
	}

	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
	}

	/*
	 * if(componentId == 33) setCounterSkill(4); else if(componentId == 34)
	 * setCounterSkill(2); else if(componentId == 35) setCounterSkill(3); else
	 * if(componentId == 42) setCounterSkill(18); else if(componentId == 49)
	 * setCounterSkill(11);
	 */

	public int getCounterSkill(int skill) {
		switch (skill) {
		case ATTACK:
			return 0;
		case STRENGTH:
			return 1;
		case DEFENCE:
			return 4;
		case RANGE:
			return 2;
		case HITPOINTS:
			return 5;
		case PRAYER:
			return 6;
		case AGILITY:
			return 7;
		case HERBLORE:
			return 8;
		case THIEVING:
			return 9;
		case CRAFTING:
			return 10;
		case MINING:
			return 12;
		case SMITHING:
			return 13;
		case FISHING:
			return 14;
		case COOKING:
			return 15;
		case FIREMAKING:
			return 16;
		case WOODCUTTING:
			return 17;
		case SLAYER:
			return 19;
		case FARMING:
			return 20;
		case CONSTRUCTION:
			return 21;
		case HUNTER:
			return 22;
		case SUMMONING:
			return 23;
		case DUNGEONEERING:
			return 24;
		case MAGIC:
			return 3;
		case FLETCHING:
			return 18;
		case RUNECRAFTING:
			return 11;
		default:
			return -1;
		}

	}

	public void addXp(int skill, double exp) {
		
		if (player.isXpLocked())
			return;

		//calculate modes first
		if (isCombatSkill(skill)) {
			exp *= player.getGameMode().getCombatMulti();
		} else {
			exp *= player.getGameMode().getSkillMulti();
		}

//		if (Utils.random(20) == 1) {
//			if (player.petManager.getNpcId() == 16139) {
//				int amount1 = Utils.random(1, 248);
//				player.getInventory().addItemOrBank(28739, amount1);
//				player.getPackets().sendGameMessage("<col=fff00>You recieve " +amount1+ " h'ween candy from doing a skilling or combat activity with your pet giving you a 25% bonus.");
//			} else {
//				int amount = Utils.random(1, 200);
//				player.getInventory().addItemOrBank(28739, amount);
//				player.getPackets().sendGameMessage("<col=fff00>You recieve " +amount+ " h'ween candy from doing a skilling or combat activity.");
//			}
//		}

//		if (Utils.random(200) == 1) {
//			player.sendMessage("<col=ff0000>You've received a festive cracker for earning some experience.");
//			player.getInventory().addItemDrop(29543, 1);
//		}
		
		if (player.doubleExperienceTimer > 0) {
			exp *= 2;
		}
		if (player.getBuffManager().hasBuff(BuffManager.BuffType.XP_BOOST)) {
			exp *= 2.00;
		}

		if (player.getBuffManager().hasBuff(BuffManager.BuffType.XPPOTION)) {
			exp *= 1.10;
		}
		if (isCombatSkill(skill) && player.getEquipment().getChestId() == 28636) {
			exp *= 1.03;

		}
		if (isCombatSkill(skill) && player.getEquipment().getLegsId() == 28634) {
			exp *= 1.03;

		}
		if (isCombatSkill(skill) && player.getEquipment().getHatId() == 28638) {
			exp *= 1.03;

		}
		
		if (player.getEquipment().getRingId() == 29997) {
			if (player.xpRingCharges > 0) {
				exp *= 2;
				player.xpRingCharges--;
			} else {
				player.getEquipment().deleteItem(29997, 1);
				player.activatedCharges = false;
				player.getPackets().sendGameMessage("<col=8B0000>[XP Ring]</col> - Your ring is out of charges, and dissapears.");
			}
		}

		if (player.getEquipment().getShieldId() == 15440) {
			if (player.horn > 0) {
				exp *= 2;
				player.horn--;
			} else {
				player.getInventory().deleteItem(15440, 1);
				player.getInventory().addItem(15439, 1);
				player.getPackets()
				.sendGameMessage("Your penance horn is out of charges and does no longer give a bonus.");
			}
		}

		if (player.hasAgileSet(player) && skill == AGILITY) {
			exp *= 2;
		}
		if (player.getEquipment().getHatId() == 24427 && skill == FISHING) {
			exp *= 1.01;
		}
		if (player.getEquipment().getLegsId() == 24429 && skill == FISHING) {
			exp *= 1.01;
		}
		if (player.getEquipment().getChestId() == 24428 && skill == FISHING) {
			exp *= 1.01;
		}
		if (player.getEquipment().getBootsId() == 24430 && skill == FISHING) {
			exp *= 1.01;
		}
		if (player.getEquipment().getHatId() == 21485 && skill == RUNECRAFTING) {
			exp *= 1.01;
		}
		if (player.getEquipment().getLegsId() == 21486 && skill == RUNECRAFTING) {
			exp *= 1.01;
		}
		if (player.getEquipment().getChestId() == 21484 && skill == RUNECRAFTING) {
			exp *= 1.01;
		}
		if (player.getEquipment().getBootsId() == 21486 && skill == RUNECRAFTING) {
			exp *= 1.01;
		}
		
		if (World.moreprayer && skill == PRAYER) {
			exp *= 2;
		}

		if (DoubleXpManager.isWeekend()) {
			exp *= 2;
		}

		if (player.getAuraManager().usingWisdom()) {
			exp *= 1.025;
		}

		
		exp *= player.getCooldownManager().hasCooldown("vote_boosts") ? 1 : 1.1;
		
		exp *= player.getDonationManager().getRank().getExperienceBoost();
		

		player.getControlerManager().trackXP(skill, (int) exp);
		//player.getPackets().sendConfig(2044, (int) (exp * 50) / 2);
		
		player.getSkillingPetManager().rollSkillingPet(skill, exp);


		int oldLevel = getLevelForXp(skill);
		int oldXP = (int) xp[skill];
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| trackSkillsIds[i] == 29
						&& (skill == Skills.ATTACK || skill == Skills.DEFENCE || skill == Skills.STRENGTH
						|| skill == Skills.MAGIC || skill == Skills.RANGE || skill == Skills.HITPOINTS)
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}

		if (oldXP < 104273167 && xp[skill] > 104273167){
			LevelUp.send104m(player, skill);
		}
		if (oldXP < 200000000 && xp[skill] > 200000000){
			LevelUp.send200m(player, skill);
		}
		
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);

			if (skill == SUMMONING || skill >= ATTACK && skill <= MAGIC) {
				player.getAppearence().generateAppearenceData();
				if (skill == HITPOINTS) {
					player.heal(levelDiff * 10);
				} else if (skill == PRAYER) {
					player.getPrayer().restorePrayer(levelDiff * 10);
				}
			}
			player.getQuestManager().checkCompleted();
		}
		refresh(skill);
	}

	public static int getTotalLevel(Player player) {
		int totallevel = 0;
		for (int i = 0; i <= 24; i++) {
			totallevel += player.getSkills().getLevelForXp(i);
		}
		return totallevel;
	}

	public static int getSkillId(String name) {
		for (int i = 0; i < SKILL_NAME.length; i++) {
			if (name.equalsIgnoreCase(SKILL_NAME[i])) {
				return i;
			}
		}
		return -1;
	}

	public static String getTotalXp(Player player) {
		double totalxp = 0;
		for (double xp : player.getSkills().getXp()) {
			totalxp += xp;
		}
		NumberFormat formatter = new DecimalFormat("#######");
		return formatter.format(totalxp);
	}

	public boolean isCombatSkill(int skill) {
		return (skill != 5 && skill < 7) || skill == 23;
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}

	public int getTotalLevel() {
		int totalLevel = 0;
		for (int i = 0; i < level.length; i++) {
			totalLevel += getLevelForXp(i);
		}
		return totalLevel;
	}
	
	public void switchVirtualLevels() {
		virtualLevels = !virtualLevels;
		player.sendMessage("You're now playing " + (virtualLevels ? "with" : "without") + " virtual leveling.");
		init();
	}
	
	public boolean virtualLeveling() {
		return virtualLevels;
	}

	public double addXpLamp(int skill, double exp) {
		player.getControlerManager().trackXP(skill, (int) exp);
		if (player.isXpLocked()) {
			return 0;
		}
		exp *= 1;
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| trackSkillsIds[i] == 29
						&& (skill == Skills.ATTACK || skill == Skills.DEFENCE || skill == Skills.STRENGTH
						|| skill == Skills.MAGIC || skill == Skills.RANGE || skill == Skills.HITPOINTS)
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if (skill == SUMMONING || skill >= ATTACK && skill <= MAGIC) {
				player.getAppearence().generateAppearenceData();
				if (skill == HITPOINTS) {
					player.heal(levelDiff * 10);
				} else if (skill == PRAYER) {
					player.getPrayer().restorePrayer(levelDiff * 10);
				}
			}
			player.getQuestManager().checkCompleted();
		}
		refresh(skill);
		return exp;
	}

	public static int getConstruction() {
		return CONSTRUCTION;
	}
	
	public int getHighestSkillLevel() {
		int maxLevel = 1;
		for (int skill = 0; skill < level.length; skill++) {
			int level = getLevelForXp(skill);
			if (level > maxLevel)
				maxLevel = level;
		}
		return maxLevel;
	}
}