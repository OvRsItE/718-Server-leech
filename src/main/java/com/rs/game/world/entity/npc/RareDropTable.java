package com.rs.game.world.entity.npc;

import java.util.Random;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.Rarity;
import com.rs.utility.Utils;

/**
 * @author Andy || ReverendDread Dec 7, 2016
 */
public final class RareDropTable {
	
	private static final Random random = new Random();
	
	/**
	 * Drop table containing all items from the "Gem" table on RuneScape 3.
	 */
	private enum Gem {
		
		COINS(Rarity.COMMON, new Item(995, Utils.random(250, 500))),
		LOOP_HALF_OF_A_KEY(Rarity.RARE, new Item(987)),
		TOOTH_HALF_OF_A_KEY(Rarity.RARE, new Item(985)),
		UNCUT_SAPPIRE(Rarity.COMMON, new Item(1624)),
		UNCUT_EMERALD(Rarity.COMMON, new Item(1621)),	
		UNCUT_DIAMOND(Rarity.UNCOMMON, new Item(1617, Utils.random(1, 2))),
		UNCUT_RUBY(Rarity.UNCOMMON, new Item(1619, 1)),
		UNCUT_DRAGONSTONE(Rarity.RARE, new Item(1631)),
		CHAOS_TALISMAN(Rarity.UNCOMMON, new Item(1452)),
		NATURE_TALISMAN(Rarity.UNCOMMON, new Item(1462)),
		RUNE_JAVELIN(Rarity.UNCOMMON, new Item(830, 5));
		
		private Rarity rarity;
		private Item loot;
		
		private Gem(Rarity rarity, Item loot) {
			this.rarity = rarity;
			this.loot = loot;
		}
		
		public Item getLoot() {
			return loot;
		}
		
		public Rarity getRarity() {
			return rarity;
		}
		
		public static Item drop() {
			for (Gem drop : Gem.values()) {
				if ((random.nextInt(99) + 1) 
						<= drop.getRarity().getRate() * 1.0D) {
					return drop.getLoot();
				}
			}
			return null;
		}
		
	}
	
	/**
	 * Drop table containing all items from the "Rare" table on RuneScape 3.
	 */
	private enum Rare {
		
		BIG_BONES_NOTED(Rarity.UNCOMMON, new Item(533, Utils.random(68, 82))),
		FLAX_NOTED(Rarity.UNCOMMON, new Item(1780, Utils.random(450, 550))),
		TEAK_PLANK_NOTED(Rarity.UNCOMMON, new Item(8781, Utils.random(45, 55))),
		MOLTEN_GLASS_NOTED(Rarity.UNCOMMON, new Item(1776, Utils.random(45, 55))),
		RUNE_ARROWHEADS(Rarity.UNCOMMON, new Item(44, Utils.random(113, 230))),
		DRAGON_HELM(Rarity.UNCOMMON, new Item(1149)),
		RUNE_PLATEBODY(Rarity.UNCOMMON, new Item(1127)),
		AGILITY_BRAWLING_GLOVES(Rarity.RARE, new Item(13849)),
		COOKING_BRAWLING_GLVOES(Rarity.RARE, new Item(13857)),
		FISHING_BRAWLING_GLOVES(Rarity.RARE, new Item(13856)),
		FM_BRAWLING_GLOVES(Rarity.RARE, new Item(13851)),
		HUNTER_BRAWLING_GLOVES(Rarity.RARE, new Item(13853)),
		MAGIC_BRAWLING_GLOVES(Rarity.RARE, new Item(13847)),
		MELEE_BRAWLING_GLOVES(Rarity.RARE, new Item(13845)),
		MINING_BRAWLNG_GLOVES(Rarity.RARE, new Item(13852)),
		PRAYER_BRAWLING_GLOVES(Rarity.RARE, new Item(13848)),
		RANGED_BRAWLING_GLOVES(Rarity.RARE, new Item(13846)),
		SMITHING_BRAWLING_GLOVES(Rarity.RARE, new Item(13855)),
		THEIVING_BRAWLING_GLOVES(Rarity.RARE, new Item(13854)),
		WOODCUTTING_BRAWLING_GLOVES(Rarity.RARE, new Item(13850)),
		RUNE_PLATEBODY_NOTED(Rarity.UNCOMMON, new Item(1128, Utils.random(15, 25))),
		ADAMANT_BAR_NOTED(Rarity.UNCOMMON, new Item(2362, Utils.random(14, 16))),
		RUNE_BAR_NOTED(Rarity.COMMON, new Item(2364, 3)),
		YEW_LOGS_NOTED(Rarity.UNCOMMON, new Item(1516, Utils.random(68, 747))),
		GOLD_ORE_NOTED(Rarity.UNCOMMON, new Item(445, Utils.random(90, 110))),
		RAW_LOBSTER_NOTED(Rarity.UNCOMMON, new Item(378, Utils.random(135, 165))),
		DRAGON_LONGSWORD(Rarity.UNCOMMON, new Item(1305));
		
		private final Rarity rarity;
		private final Item loot;
		
		private Rare(Rarity rarity, Item loot) {
			this.rarity = rarity;
			this.loot = loot;		
		}
		
		public Item getLoot() {
			return loot;
		}	
		
		public Rarity getRarity() {
			return rarity;
		}
		
		public static Item drop() {
			for (Rare drop : Rare.values()) {
				if ((random.nextInt(99) + 1) 
						<= drop.getRarity().getRate() * 1.0D) {
					return drop.getLoot();
				}
			}
			return null;
		}
		
	}
	
	/**
	 * Drop table containing all items from the "Super Rare" table on RuneScape 3.
	 */
	private enum SuperRare {
		
		MAHOGANY_PLANK_NOTED(Rarity.UNCOMMON, new Item(8783, Utils.random(270, 330))),
		BIG_BONES_NOTED(Rarity.UNCOMMON, new Item(533, Utils.random(68, 82))),
		DRAGON_BONES_NOTED(Rarity.UNCOMMON, new Item(537, Utils.random(180, 220))),
		SOFT_CLAY_NOTED(Rarity.UNCOMMON, new Item(1762, Utils.random(450, 550))),
		ONYX_BOLTS(Rarity.UNCOMMON, new Item(9342, Utils.random(135, 165))),
		SHIELD_LEFT_HALF(Rarity.UNCOMMON, new Item(2366)),
		PRAYER_POTION_NOTED(Rarity.UNCOMMON, new Item(2435, Utils.random(45, 55))),
		SUPER_RESTORE_NOTED(Rarity.UNCOMMON, new Item(3025, Utils.random(45, 55))),
		RUNITE_ORE_NOTED(Rarity.UNCOMMON, new Item(452, Utils.random(90, 110))),
		COAL_NOTED(Rarity.UNCOMMON, new Item(454, Utils.random(200, 1100))),
		GRIMY_SNAPDRAGON_NOTED(Rarity.UNCOMMON, new Item(3052, Utils.random(90, 110))),
		GRIMY_TORSTOL_NOTED(Rarity.UNCOMMON, new Item(220, Utils.random(90, 110))),
		RAW_SHARK_NOTED(Rarity.UNCOMMON, new Item(384, Utils.random(225, 275))),
		DWARF_WEED_SEED(Rarity.UNCOMMON, new Item(5303, Utils.random(14, 16))),
		LANTADYME_SEED(Rarity.UNCOMMON, new Item(5302, Utils.random(14, 16))),
		MAGIC_SEED(Rarity.UNCOMMON, new Item(5316, Utils.random(3, 7))),
		PALM_TREE_SEED(Rarity.UNCOMMON, new Item(5289, 10)),
		UNCUT_DIAMOND_NOTED(Rarity.UNCOMMON, new Item(1618, Utils.random(45, 55))),
		UNCUT_DRAGONSTONE_NOTED(Rarity.COMMON, new Item(1632, Utils.random(45, 55))),
		EARTH_TALISMAN(Rarity.UNCOMMON, new Item(1441, Utils.random(65, 82))),
		FIRE_TALISMAN(Rarity.UNCOMMON, new Item(1443, Utils.random(25, 35))),
		WATER_TALISMAN(Rarity.UNCOMMON, new Item(1445, Utils.random(65, 82))),
		BATTLESTAFF(Rarity.UNCOMMON, new Item(1392, Utils.random(180, 220))),
		LOOP_HALF_OF_A_KEY(Rarity.UNCOMMON, new Item(987)),
		DRAGON_SPEAR(Rarity.UNCOMMON, new Item(1249)),
		VECNA_SKULL(Rarity.RARE, new Item(20667));
		//HAZELMERES_SIGNET_RING(Rarity.RARE, new Item(29107));
		
		private final Rarity rarity;
		private final Item loot;
		
		private SuperRare(Rarity rarity, Item loot) {
			this.rarity = rarity;
			this.loot = loot;
		}
		
		public Item getLoot() {
			return loot;
		}

		public Rarity getRarity() {
			return rarity;
		}

		public static Item drop() {
			for (SuperRare drop : SuperRare.values()) {
				if ((random.nextInt(99) + 1) 
						<= drop.getRarity().getRate() * 1.0D) {
					return drop.getLoot();

				}
			}
			return null;
		}
	
	}
	
	/**
	 * Rolls for the rare drop table.
	 * @param entry
	 * 			the rate at which to enter rare drop table.
	 * @return {@link Item}
	 * 			the item drop.
	 */
	public static Item roll(Player player, int entry) {
		switch(random.nextInt(entry)) {
			case 1: {
				switch (random.nextInt(5)) {
					case 1: {
						switch (random.nextInt(10)) {
							case 1: {
								return SuperRare.drop();
							}
							default: { 						
								return Rare.drop();
							}
						}
					}
					default: {
						return Gem.drop();
					}
				}
			}
			default: return null;
		}
	}
	
}
