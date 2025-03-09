package com.rs.utility;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemManager;
import com.rs.game.world.entity.player.content.ItemConstants;

public final class EconomyPrices {

	public static int getPrice(int itemId) {
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		if (defs.isNoted())
			itemId = defs.getCertId();
		else if (defs.isLended())
			itemId = defs.getLendId();
		if (!ItemConstants.isTradeable(new Item(itemId, 1)))
			return 0;
		
		/**
		 * General Items
		 */
		if (itemId == 4151 || itemId == 4152)// Whip
				return 4472022; 
		if (itemId == 6585 || itemId == 6586) // Amulet Of Fury
				return 6594000;
		if (itemId ==  4587 || itemId == 4588) // Dragon scimitar
				return 268126;
		if (itemId ==  4153 || itemId == 4154) // Granite Maul
				return 968126;
		if (itemId ==  6737 || itemId == 6738) // Berserker ring
				return 398126;
		if (itemId ==  6733 || itemId == 6734) // Archer's ring
				return 318126;
		if (itemId ==  6731 || itemId == 6732) // Seers's ring
				return 1213400;
		if (itemId ==  6735 || itemId == 6736) // Warriors ring
				return 1416321;
		if (itemId == 1215 || itemId == 1216) // Dragon dagger
				return 86112;
		if (itemId == 4087 || itemId == 4088) // Dragon legs
				return 532000;
		if (itemId == 3140 || itemId == 3141) // Dragon Chain
				return 132000;
		if (itemId == 1149 || itemId == 1150) // Dragon Med Helm
				return 384000;
		if (itemId == 11335 || itemId == 11336) // Dragon Full helm
				return 34243000;
		if (itemId == 11479 || itemId == 11480) // Dragon Platebody
				return 27543000;
		if (itemId == 24365 || itemId == 24366) // Dragon Kiteshield
				return 19543000;
		
		/**
		 * Rune items
		 */
		if (itemId == 1127 || itemId == 1128) // Rune Platebody
				return 97100;
		if (itemId == 1079 || itemId == 1080) // Rune Platelegs
				return 79600;
		if (itemId == 1163 || itemId == 1164) // Rune full helm
				return 64100;
		if (itemId == 1201 || itemId == 1202) // Rune Kite shield
				return 57100;
		if (itemId == 1333 || itemId == 1334) // Rune scimitar
				return 46500;
		
		/**
		 * End Of rune items
		 */
		
		
		/**
		 *  Start of bows / Arrows
		 */
		if (itemId == 861 || itemId == 862) // Magic shortbow
				return 3785;
		if (itemId == 11128 || itemId == 11129) // Beresrker neckalce
				return 6784100;
		
		/**
		 * End Of Arrows / bows
		 */
		
		
		
		/**
		 * 
		 * Teleportal tablets
		 */
		if (itemId == 11128 || itemId == 11129) // Beresrker neckalce
		
		
		
		/**
		 * Potions / Herblorer
		 */
		if (itemId == 15332) //  Overload (4)
				return 12765;
		if (itemId == 15333) //  Overload (2)
				return 9765;
		if (itemId == 15334) //  Overload (2)
				return 6102;
		if (itemId == 15335) //  Overload (1)
				return 2640;
		if (itemId == 2440 || itemId == 2441) //  Super Strength (4)
			return 6721;
		if (itemId == 157 || itemId == 158) //  Super Strength (3)
			return 4865;
		if (itemId == 159 || itemId == 160) //  Super Strength (2)
			return 2995;
		if (itemId == 161 || itemId == 162) //  Super Strength (1)
			return 1225;
		if (itemId == 2436 || itemId == 2437) //  Super Attack (4)
			return 6426;
		if (itemId == 145 || itemId == 146) //  Super attack (3)
			return 4426;
		if (itemId == 147 || itemId == 148) //  Super attack (2)
			return 2350;
		if (itemId == 149 || itemId == 150) //  Super attack (1)
			return 1250;
		if (itemId == 2442 || itemId == 2443) //  Super defence (4)
			return 5210;
		if (itemId == 163 || itemId == 164) //  Super defence (3)
			return 4103;
		if (itemId == 165 || itemId == 166) //  Super defence (2)
			return 2400;
		if (itemId == 167 || itemId == 168) //  Super defence (1)
			return 1005;
		if (itemId == 2434 || itemId == 2435) //  Prayer potion (4)
			return 11300;
		if (itemId == 139 || itemId == 140) //  Prayer potion (3)
			return 9800;
		if (itemId == 141 || itemId == 142) //  Prayer potion (2)
			return 7980;
		if (itemId == 143 || itemId == 144) //  Prayer potion (1)
			return 3451;
		
		/**
		 * End of Potions & Herbs
		 */
		
		
		
		
		/**
		 * Start of runes & Staffs
		 */
		if (itemId == 554) //  Fire Rune
			return 202;
		if (itemId == 555) // Water Runes
			return 232;
		if (itemId == 556) //  Air Runes
			return 206;
		if (itemId == 557) //  Earth Runes
			return 289;
		if (itemId == 558) //  Mind Runes
			return 132;
		if (itemId == 560) //  Death Runes
			return 311;
		if (itemId == 561) //  Nature Runes
			return 364;
		if (itemId == 562) // Chaos runes
			return 315;
		if (itemId == 563) // Law rune
			return 331;
		if (itemId == 564) // Cosmetic rune
			return 231;
		if (itemId == 9075) // Astral Rune
			return 478;
		if (itemId == 1381 || itemId == 1382) // Air Staff
			return 6974;
		if (itemId == 1383 || itemId == 1384) // Water Staff
			return 7312;
		if (itemId == 1385 || itemId == 1386) // Earth Staff
			return 7264;
		if (itemId == 1387 || itemId == 1388) // Fire Staff
			return 7102;
		/**
		 * End of runes / staffs
		 */
		
		/**
		 *  High end quality items
		 */
		
		if (itemId == 13748 || itemId == 13749) //  Divine Sigil
			return 210210000;
		if (itemId == 13746 || itemId == 13747) //  Arcane Sigil
			return 9800;
		if (itemId == 11694 || itemId == 11695) // Armadyl godsword
				return 74320333;
		if (itemId == 11710 || itemId == 11711) // Godsword Shard 1
				return 9430333;
		if (itemId == 11712 || itemId == 11713) // Godsword Shard 2
				return 11303330;
		if (itemId == 11714 || itemId == 11715) // Godsword Shard 3
				return 7436074;
		if (itemId == 11702 || itemId == 11703) // Armadyl Hilt
				return 56521000;
		if (itemId == 11718 || itemId == 11719) // Armadyl Helmet
				return 47201300;
		if (itemId == 11720 || itemId == 11721) // Armadyl Chest
				return 68100900;
		if (itemId == 11722 || itemId == 11723) // Armadyl plateskirt 
				return 52331046;
		if (itemId == 25037 || itemId == 25038) // Amradyl Crossbow
				return 58231000;
		if (itemId == 11696 || itemId == 11697) // Bandos Godsword 
				return 53022000;
		if (itemId == 11704 || itemId == 11705) // Bandos Hilt 
				return 34764100;
		if (itemId == 11726 || itemId == 11727) // Bandos tassets 
				return 39314000;
		if (itemId == 11728 || itemId == 11729) // Bandos Boots
				return 12400000;
		if (itemId == 14484 || itemId == 14485) // Dragon Claws
				return 91446101;
		if (itemId == 21787 || itemId == 21788) // Steadfast boots
				return 21301400;
		if (itemId == 21790 || itemId == 217891) // Glavien boots
				return 24346320;
		if (itemId == 21793 || itemId == 217894) // Ragefire boots
				return 27304400;
		if (itemId == 11730 || itemId == 11731) //Saradomin sword
				return 41304400;
		if (itemId == 10033) // Chinchompas
				return 9461;
		if (itemId == 10034) //Red Chinchompas
				return 19400;

		/**
		 * Start of barrows armor
		 * 
		 */
		
		if (itemId == 4716 || itemId == 4717) //Dharoks helm
			return 11654101;
		
		if (itemId == 4718 || itemId == 4719) // Dharoks Axe
			return 234000;
		
		if (itemId == 4720 || itemId == 4721) // Dharks Platebody
			return 18546700;
		
		if (itemId == 4722 || itemId == 4723) // Dharks legs
			return 12546700;
		
		// Start of Ahrims
		
		if (itemId == 4708 || itemId == 4709) // Ahrims Hood
			return 125467000;
		
		
		
		
		/**		
		 * Bones 
		 */
		
		if (itemId == 18830 || itemId == 18831) // Frost Bones
				return 9611;
		if (itemId == 536 || itemId == 537)  // D Bones
				return 5477;
		/**
		 * 
		 *  Fishess
		 */
		
		if (itemId == 21521) // Tiger Fish
			return 11344;
		if (itemId == 21520) // Raw Tiger Fish
			return 12974;
		if (itemId == 15272 || itemId == 15273) // Rocktail
				return 9887;
		if (itemId == 15270 || itemId == 15271) // Raw Tails
				return 10674;
		if (itemId == 391 || itemId == 392) // Manta Ray
				return 7231;
		if (itemId == 389 || itemId == 390) // Raw Manta Ray
				return 8400;
		if (itemId == 385 || itemId == 386) // Sharks
				return 6160;
		if (itemId == 383 || itemId == 384) // Raw Sharks
				return 7340;
		if (itemId == 15266 || itemId == 15267) // Cavefish
				return 8210;
		if (itemId == 15264 || itemId == 15265) // Raw Cavefish
				return 9112;
		if (itemId == 379 || itemId == 380) // Lobster
				return 3741;
		if (itemId == 377 || itemId == 378) // Raw lobs
				return 4310;
		
		
		// End of fishes
		
		/**
		 * 
		 *  Logs 
		 */
		if (itemId == 1513 || itemId == 1514) // Magic Logs
				return 11310;
		if (itemId == 1515 || itemId == 1516) // Yew Logs
				return 9064;
		if (itemId == 1517 || itemId == 1518) // Maple Logs
				return 6034;
		if (itemId == 1519 || itemId == 1520) // Willow Logs
				return 2912;
		
		// END LOGS
	
		
		
		return ItemManager.getPrice(itemId); // TODO get price from real item from saved
									// prices from ge
	}

	private EconomyPrices() {

	}
}
