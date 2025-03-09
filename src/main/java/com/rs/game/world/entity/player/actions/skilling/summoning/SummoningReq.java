package com.rs.game.world.entity.player.actions.skilling.summoning;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.entity.player.Player;

public class SummoningReq {
	
	@SuppressWarnings("unused")
	private static int GOLD = 12158, GREEN = 12159, CRIMSON = 12160, ABYSSAL = 12161,
			   TALON = 12162, BLUE = 12163, RAVAGER = 12164, SHIFTER = 12165,
			   SPINNER = 12166, TORCHER = 12167, OBSIDIAN = 12168, SPIRIT = 12183,
			   POUCH = 12155;
	
	public enum Requirements {
		
		
		SpiritWolf(12047, 1, 4.8, GOLD, 1, SPIRIT, 7, POUCH, 1, 2859, 1),
		Dreadfowl(12043, 4, 9.3, GOLD, 1, SPIRIT, 8, POUCH, 1, 2138, 1),
		SpiritSpider(12059, 10, 12.6, GOLD, 1, SPIRIT, 8, POUCH, 1, 6291, 1),
		ThornySnail(12019, 13, 12.6, GOLD, 1, SPIRIT, 9, POUCH, 1, 3363, 1),
		GraniteCrab(12009, 16, 21.6, GOLD, 1, SPIRIT, 7, POUCH, 1, 440, 1),
		SpiritMosquito(12778, 17, 46.5, GOLD, 1, SPIRIT, 1, POUCH, 1, 6319, 1),
		DesertWyrm(12049, 18, 31.2, GREEN, 1, SPIRIT, 45, POUCH, 1, 1783,1),
		SpiritScorpion(12055, 19, 83.2, CRIMSON, 1, SPIRIT, 57, POUCH, 1, 3095, 1),
		SpiritTzKih(12808, 22, 96.8, CRIMSON, 1, SPIRIT, 64, POUCH, 1, 12168, 1),
		AlbinoRat(12067, 23, 202.4, BLUE, 1, SPIRIT, 75, POUCH, 1, 2134, 1),
		SpiritKalphite( 12063, 25, 220.0, BLUE, 1, SPIRIT, 51, POUCH, 1, 3138, 1),
		CompostMound(12091, 28, 49.8, GREEN, 1, SPIRIT, 47, POUCH, 1, 6032, 1),
		GiantChinchompa(12800, 29, 255.2, BLUE, 1, SPIRIT, 84, POUCH, 1, 9976, 1),
		VampyreBat(12053, 31, 136.0, CRIMSON, 1, SPIRIT, 81, POUCH, 1, 3325, 1),
		HoneyBadger(12065, 32, 140.8, CRIMSON, 1, SPIRIT, 84, POUCH, 1, 12156, 1),
		Beaver(12021, 33, 57.6, GREEN, 1, SPIRIT, 72, POUCH, 1, 1519, 1),
		VoidRavager(12818, 34, 59.6, GREEN, 1, SPIRIT, 74, POUCH, 1, 12164, 1),
		VoidSpinner(12780, 34, 59.6, BLUE, 1, SPIRIT, 74, POUCH, 1, 12166, 1),
		VoidTorcher(12798, 34, 59.6, BLUE, 1, SPIRIT, 74, POUCH, 1, 12167, 1),
		VoidShifter(12814, 34, 59.6, BLUE, 1, SPIRIT, 74, POUCH, 1, 12165, 1),
		BronzeMinotaur(12073, 36, 316.8, BLUE, 1, SPIRIT, 102, POUCH, 1, 2349, 1),
		BullAnt(12087, 40, 52.8, GREEN, 1, SPIRIT, 11, POUCH, 1, 6010, 1),
		Macaw(12071, 41, 72.4, CRIMSON, 1, SPIRIT, 78, POUCH, 1, 249, 1),
		EvilTurnip(12051, 42, 184.8, CRIMSON, 1, SPIRIT, 104, POUCH, 1, 12153, 1),
		SpiritCockatrice(12095, 43, 75.2, GREEN, 1, SPIRIT, 88, POUCH, 1, 12109, 1),
		SpiritGuthatrice(12097, 43, 75.2, GREEN, 1, SPIRIT, 88, POUCH, 1, 12111, 1),
		SpiritSaratrice(12099,  43, 75.2, GREEN, 1, SPIRIT, 88, POUCH, 1, 12113, 1),
		SpiritZamatrice(12101, 43, 75.2, GREEN, 1, SPIRIT, 88, POUCH, 1, 12115, 1),
		SpiritPengatrice( 12103, 43, 75.2, GREEN, 1, SPIRIT, 88, POUCH, 1, 12117, 1),
		SpiritCoraxatrice(12105, 43, 75.2, GREEN, 1, SPIRIT, 88, POUCH, 1, 12119, 1),
		IronMinotaur( 12075, 46, 404.8, BLUE, 1, SPIRIT, 125, POUCH, 1, 2351, 1),
		Pyrelord(12816, 46, 202.4, CRIMSON, 1, SPIRIT, 111, POUCH, 1, 590, 1),
		Magpie(12041, 47, 83.2, GREEN, 1, SPIRIT, 88, POUCH, 1, 1635, 1),
		BloatedLeech(12061, 49, 215.2, CRIMSON, 1, SPIRIT, 117, POUCH, 1, 2132, 1),
		SpiritTerrorbird(12007, 52, 68.4, GOLD, 1, SPIRIT, 12, POUCH, 1, 9978, 1),
		AbyssalParasite(12035, 54, 94.8, GREEN, 1, SPIRIT, 106, POUCH, 1, 12161, 1),
		SpiritJelly(12027, 55, 484.0, BLUE, 1, SPIRIT, 151, POUCH, 1, 1937, 1),
		Ibis(12531, 56, 98.8, GREEN, 1, SPIRIT, 109, POUCH, 1, 311, 1),
		SteelMinotaur(12077, 56, 492.8, BLUE, 1, SPIRIT, 151, POUCH, 1, 2353, 1),
		SpiritKyatt(12812, 57, 501.6, BLUE, 1, SPIRIT, 153, POUCH, 1, 10103, 1),
		SpiritLarupia(12784, 57, 501.6, BLUE, 1, SPIRIT, 155, POUCH, 1, 10095, 1),
		SpiritGraahk(12810, 57, 501.6, BLUE, 1, SPIRIT, 154, POUCH, 1, 10099, 1),
		Karamthulhu(12023, 58, 510.4, BLUE, 1, SPIRIT, 144, POUCH, 1, 6667, 1),
		SmokeDevil(12085, 61, 268.0, CRIMSON, 1, SPIRIT, 141, POUCH, 1, 9736, 1),
		AbyssalLurker(12037, 62, 109.6, GREEN, 1, SPIRIT, 119, POUCH, 1, 12161, 1),
		SpiritCobra(12015, 63, 276.8, CRIMSON, 1, SPIRIT, 116, POUCH, 1, 6287, 1),
		StrangerPlant(12045, 64, 281.6, CRIMSON, 1, SPIRIT, 128, POUCH, 1, 8431, 1),
		MithrilMinotaur(12079, 66, 580.0, BLUE, 1, SPIRIT, 152, POUCH, 1, 2359, 1),
		BarkerToad(12123, 66, 580.8, GOLD, 1, SPIRIT, 11, POUCH, 1, 2150, 1),
		WarTortoise(12031, 67, 58.6, GOLD, 1, SPIRIT, 1, POUCH, 1, 7939, 1),
		Bunyip(12029, 68, 119.2, GREEN, 1, SPIRIT, 110, POUCH, 1, 383, 1),
		FuitBat(12033, 69, 121.2, GREEN, 1, SPIRIT, 130, POUCH, 1, 1963, 1),
		RavenousLocust(12820, 70, 132.0, CRIMSON, 1, SPIRIT, 79, POUCH, 1, 1833, 1),
		ArcticBear(12057, 71, 93.2, GOLD, 1, SPIRIT, 14, POUCH, 1, 19117, 1),
		Phoenix(14623, 72, 302.0, CRIMSON, 1, SPIRIT, 165, POUCH, 1, 14616, 1),
		ObsidianGolem(12792, 73, 642.0, BLUE, 1, SPIRIT, 195, POUCH, 1, 12168, 1),
		GraniteLobster(12069, 74, 325.0, CRIMSON, 1, SPIRIT, 166, POUCH, 1, 6979, 1),
		PrayingMantis(12011, 75, 329.0, CRIMSON, 1, SPIRIT, 168, POUCH, 1, 2460, 1),
		ForgeRegent(12782, 76, 50.0, GREEN, 1, SPIRIT, 141, POUCH, 1, 10020, 1),
		AdamantMinotaur(12081, 76, 668.0, BLUE, 1, SPIRIT, 144, POUCH, 1, 2361, 1),
		TalonBeast(12794, 77, 1015, CRIMSON, 1, SPIRIT, 174, POUCH, 1, 12162, 1),
		GiantEnt(12013, 78, 136.8, GREEN, 1, SPIRIT, 124, POUCH, 1, 5933, 1),
		FireTitan(12802, 79, 695.2, BLUE, 1, SPIRIT, 198, POUCH, 1, 1442, 1),
		MossTitan(12804, 79, 695.2, BLUE, 1, SPIRIT, 202, POUCH, 1, 1440, 1),
		IceTitan(12806, 79, 695.2, BLUE, 1, SPIRIT, 198, POUCH, 1, 1438, 1),
		Hydra(12025, 80, 140.8, GREEN, 1, SPIRIT, 128, POUCH, 1, 571, 1),
		SpiritDagannoth(12017, 83, 730.4, CRIMSON, 1, SPIRIT, 1, POUCH, 1, 6155, 1),
		LavaTitan(12788, 83, 374.8, BLUE, 1, SPIRIT, 219, POUCH, 1, 12168, 1),
		SwampTitan(12776, 85, 673.3, CRIMSON, 1, SPIRIT, 150, POUCH, 1, 10149, 1),
		RuneMinotaur(12083, 86, 756.8, BLUE, 1, SPIRIT, 1, POUCH, 1, 2363, 1),
		Unicorn(12039, 88, 154.4, GREEN, 1, SPIRIT, 140, POUCH, 1, 237, 1),
		GeyserTitan(12786, 89, 783.2, BLUE, 1, SPIRIT, 222, POUCH, 1, 1444, 1),
		Wolpertinger(12089, 92, 404.8, CRIMSON, 1, SPIRIT, 222, POUCH, 1, 2859, 1),
		AbyssalTitan(12796, 93, 163.2, GREEN, 1, SPIRIT, 113, POUCH, 1, ABYSSAL, 1),
		IronTitan(12822, 95, 417.6, CRIMSON, 1, SPIRIT, 198, POUCH, 1, 1115, 1),
		PakYak(12093, 96, 422.4, CRIMSON, 1, SPIRIT, 211, POUCH, 1, 10818, 1),
		SteelTitan(12790, 99, 435.2, CRIMSON, 1, SPIRIT, 178, POUCH, 1, 1119, 1);
		
		private static final Map<Integer, Requirements> requires = new HashMap<Integer, Requirements>();

		static {
			for (Requirements req : Requirements.values()) {
				requires.put(req.pouchId, req);
			}
		}

		public static Requirements forId(int id) {
			return requires.get(id);
		}
		
		private int pouchId;
		private int lvlReq;
		private double exp;
		private int[] reqs;
		
		private Requirements(int pouchId, int lvlReq, double exp, int... reqs) {
			this.pouchId = pouchId;
			this.lvlReq = lvlReq;
			this.exp = exp;
			this.reqs = reqs;
		}
		
		public int getPouchId() {
			return this.pouchId;
		}
		
		public int getLevel() {
			return this.lvlReq;
		}
		
		public double getExp() {
			return this.exp;
		}
		
		public int[] getReq() {
			return this.reqs;
		}
		
		
	}
	public static void main(String[] args) {
		try {
			Cache.init();
			@SuppressWarnings("unused")
			String information = ClientScriptMap.getMap(1186).getStringValue(12029);
		//	System.out.println(information);
		} catch(Exception e) {
			
		}
	}
	public static void sendRequirements(Player player, int id) {
		Requirements req = Requirements.forId(id);
		String itemName = "";
		int item = 0;
		int amount = 0;
		for(int i = 0; i < req.getReq().length; i += 2) {
				item = req.getReq()[i];
				amount = req.getReq()[i + 1];
				itemName = ItemDefinitions.getItemDefinitions(item).getName();
				player.sendMessage("You need to have a " + itemName + " x " + amount);
		}
	}
	
	
}
