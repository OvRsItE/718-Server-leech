/**
 * 
 */
package com.rs.game.world.entity.player.content.clues.impl;

import com.rs.game.item.Item;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * @author ReverendDread
 * Sep 29, 2018
 */
public enum EliteTable {
	//misc
	ZAMORAK_PAGE1(6, 3831, 1, 3),
	ZAMORAK_PAGE2(6, 3832, 1, 3),
	ZAMORAK_PAGE3(6, 3833, 1, 3),
	ZAMORAK_PAGE4(6, 3834, 1, 3),
	GUTHIX_PAGE1(6, 3835, 1, 3),
	GUTHIX_PAGE2(6, 3836, 1, 3),
	GUTHIX_PAGE3(6, 3837, 1, 3),
	GUTHIX_PAGE4(6, 3838, 1, 3),
	SARADOMIN_PAGE1(6, 3827, 1, 3),
	SARADOMIN_PAGE2(6, 3828, 1, 3),
	SARADOMIN_PAGE3(6, 3829, 1, 3),
	SARADOMIN_PAGE4(6, 3830, 1, 3),
	ARMADYL_PAGE1(6, 19604, 1, 3),
	ARMADYL_PAGE2(6, 19605, 1, 3),
	ARMADYL_PAGE3(6, 19606, 1, 3),
	ARMADYL_PAGE4(6, 19607, 1, 3),
	BANDOS_PAGE1(6, 19600, 1, 3),
	BANDOS_PAGE2(6, 19601, 1, 3),
	BANDOS_PAGE3(6, 19602, 1, 3),
	BANDOS_PAGE4(6, 19603, 1, 3),
	ANCIENT_PAGE1(6, 19608, 1, 3),
	ANCIENT_PAGE2(6, 19609, 1, 3),
	ANCIENT_PAGE3(6, 19610, 1, 3),
	ANCIENT_PAGE4(6, 19611, 1, 3),
	PURPLEFIRELIGHTER(6, 10326, 1, 3),
	REDFIRELIGHTER(6, 7329, 1, 3),
	GREENFIRELIGHTER(6, 7330, 1, 3),
	BLUEFIRELIGHTER(6, 7331, 1, 3),
	WHITEFIRELIGHTER(6, 10327, 1, 3),
	SARADOMINARROWS(6, 19152, 20, 100),
	GUTHIXARROWS(6, 19157, 20, 100),
	ZAMORAKARROWS(6, 19162, 20, 100),
	MEERKATPOUCHES(6, 19623, 1, 5),
	FETCHCASKETSCROLLS(6, 19621, 10, 30),
	MISCTELEPORT(6, 19477, 1, 10),
	LUMBERYARDTELEPORT(6, 19480, 1, 10),
	BANDITCAMPTELEPORT(6, 19476, 1, 10),
	PLAIRTELEPORT(6, 19478, 1, 10),
	TAIBOWTELEPORT(6, 19479, 1, 10),
	COINS(6, 995, 2500, 50000),
	BISCUITS(6, 19467, 2, 27),
	PURPLESWEETS(6, 10476, 2, 27),
	//common
	RUNEPLATEBODY(10, 1127, 1, 1),
	COURTSUMMONS(10, 18757, 1, 1),
	PALMTREESEED(10, 5289, 1, 1),
	PAPAYATREESEED(10, 5288, 1, 1),
	YEWSEED(10, 5315, 1, 1),
	DWARFWEEDSEED(10, 5303, 2, 2),
	LANTADYMESEED(10, 5302, 2, 2),
	SUPERRESTORE(10, 3025, 9, 9),
	PRAYERPOTION(10, 2435, 9, 9),
	ANTIFIRE(10, 2453, 9, 9),
	WATERTALISMAN(10, 1445, 8, 8),
	SWAMPLIZARD(10, 10163, 15, 15),
	RUNEBAR(10, 2364, 5, 5),
	UNICORNHORN(10, 238, 10, 10),
	ROYALDRAGONHIDE(10, 24373, 15, 15),
	ONYXBOLTSTIP(10, 9194, 12, 12),
	UNCUTDRAGONSTONE(10, 1632, 2, 2),
	BATTLESTAFF(10, 1392, 8, 8),
	MAHOGANYPLANK(10, 8783, 40, 40),
	DRAGONMEDHELM(10, 1149, 1, 1),
	//rare2
	ARMADYLSTOLE(2, 19392, 1, 1),
	BANDOSSTOLE(2, 19394, 1, 1),
	ANCIENTSTOLE(2, 19396, 1, 1),
	ARMADYLPLATEBODY(2, 19413, 1, 1),
	ARMADYLPLATELEGS(2, 19416, 1, 1),
	ARMADYLPLATESKIRT(2, 19419, 1, 1),
	ARMADYLFULLHELM(2, 19422, 1, 1),
	ARMADYLKITESHIELD(2, 19425, 1, 1),
	BANDOSPLATEBODY(2, 19428, 1, 1),
	BANDOSPLATELEGS(2, 19431, 1, 1),
	BANDOSPLAESKIRT(2, 19434, 1, 1),
	BANDOSFULLHELM(2, 19437, 1, 1),
	BANDOSKITESHIELD(2, 19440, 1, 1),
	ANCIENTPLATEBODY(2, 19398, 1, 1),
	ANCIENTPLATELEGS(2, 19401, 1, 1),
	ANCIENTPLATESKIRT(2, 19404, 1, 1),
	ANCIENTFULLHELM(2, 19407, 1, 1),
	ANCIENTKITESHIELD(2, 19410, 1, 1),
	ARMADYLVAMPS(2, 19459, 1, 1),
	ARMADYLBODY(2, 19461, 1, 1),
	ARMADYLCHAPS(2, 19463, 1, 1),
	ARMADYLCOIF(2, 19465, 1, 1),
	BANDOSVAMPS(2, 19451, 1, 1),
	BANDOSBODY(2, 19453, 1, 1),
	BANDOSCHAPS(2, 19455, 1, 1),
	BANDOSCOIF(2, 19457, 1, 1),
	ANCEINTVAMPS(2, 19443, 1, 1),
	ANCIENTBODY(2, 19445, 1, 1),
	ANCIENTCHAPS(2, 19447, 1, 1),
	ANCIENTCOIF(2, 19449, 1, 1),
	ARMADYLCROZIER(2, 19362, 1, 1),
	BANDOSCROZIER(2, 19364, 1, 1),
	ANCIENTCROZIER(2, 19366, 1, 1),
	FURYORNAMENTKIT(2, 19333, 1, 1),
	DRAGONFULLHELMOR(2, 19346, 1, 1),
	DRAGONPLATEBODYOR(2, 19350, 1, 1),
	DRAGONPLATELEGSOR(2, 19348, 1, 1),
	DRAGONSQOR(2, 19352, 1, 1),
	DRAGONFULLHELMSP(2, 19354, 1, 1),
	DRAGONPLATELEGSSP(2, 19356, 1, 1),
	DRAGONPLATEBODYSP(2, 19358, 1, 1),
	DRAGONSQSP(2, 19360, 1, 1),
	BATSTAFF(2, 19327, 1, 1),
	PENGUINSTAFF(2, 19325, 1, 1),
	WOLFSTAFF(2, 19329, 1, 1),
	CATSTAFF(2, 19331, 1, 1),
	DWAGONSTAFF(2, 19323, 1, 1),
	//very rare1
	BARROWDYE(0.06, 29804, 1, 1),
	SHADOWDYE(0.04, 29805, 1, 1),
	THIRDAGEDYE(0.02, 29806, 1, 1),
	STARVEDANCIENTEFFIGY(0.2, 18778, 1, 1),
	BLACKDRAGONMASK(1, 19290, 1, 1),
	FROSTDRAGONMASK(1, 19293, 1, 1),
	BRONZEDRAGONMASK(1, 19296, 1, 1),
	IRONDRAGONMASK(1, 19299, 1, 1),
	STEELDRAGONMASK(1, 19302, 1, 1),
	MITHIRILDRAGONMASK(1, 19305, 1, 1),
	SARADOMINBOW(1, 19143, 1, 1),
	GUTHIXBOW(1, 19146, 1, 1),
	ZAMORAKBOW(1, 19149, 1, 1),
	THIRDAGEDRUDICSTAFF(0.4, 19308, 1, 1),
	THIRDAGEDRUDICCLOAK(0.4, 19311, 1, 1),
	THIRDAGEWREATH(0.04, 19314, 1, 1),
	THIRDAGEROBETOP(0.04, 19317, 1, 1),
	THIRDAGEDRUDICROBE(0.04, 19320, 1, 1),
	;
	
	@Getter private int id, min, max;
	@Getter private double weight;
	
	private static final EliteTable[] VALUES = values();
	
	private EliteTable(double weight, int id, int min, int max) {
		this.weight = weight;
		this.id = id;
		this.min = min;
	}
	
	public static Item roll() {
		double total_weight = 0.0D;
		for (EliteTable table : VALUES) {
			total_weight += table.getWeight();
		}
		double random = Math.random() * total_weight;
		double weight = 0.0D;
		for (EliteTable table : VALUES) {
			weight += table.getWeight();
			if (weight >= random) {
				return new Item(table.getId(), Utils.random(table.getMin(), table.getMax()));
			}
		}
		return null;
	}

}
