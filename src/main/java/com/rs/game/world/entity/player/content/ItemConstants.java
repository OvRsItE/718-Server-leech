
package com.rs.game.world.entity.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;

public class ItemConstants {

	public static int[] charmIds = new int[] { 12158, 12159, 12160, 12163 };
	
	public static int getRepairCostPerCharge(int id) {
		if (id == 29711 || id == 29713 || id == 29714 || id == 29715 || id == 29708 || id == 29709 || id == 29250 || id == 29258)
			return 1;
		return -1;
	}
	
	public static int getRepairItem(int id) {
		if (id == 29711 || id == 29713 || id == 29714 || id == 29715 || id == 29708 || id == 29709 || id == 29250 || id == 29258)
			return 29253;
		return 995;
	}
	
	public static int getDegradeItemWhenWear(int id) {
		// pvp armors
		if (id == 13958 || id == 13961 || id == 13964 || id == 13967
				|| id == 13970 || id == 13973 || id == 13858 || id == 13861
				|| id == 13864 || id == 13867 || id == 13870 || id == 13873
				|| id == 13876 || id == 13884 || id == 13887 || id == 13890
				|| id == 13893 || id == 13896 || id == 13899 || id == 13902
				|| id == 13905 || id == 13908 || id == 13911 || id == 13914
				|| id == 13917 || id == 13920 || id == 13923 || id == 13926
				|| id == 13929 || id == 13932 || id == 13935 || id == 13938
				|| id == 13941 || id == 13944 || id == 13947 || id == 13950
				|| id == 13958)
			return id + 2;
		if (id == 29097)
			return 29117;
		if (id == 29100)
			return 29118;
		if (id == 29103)
			return 29119;
		return -1;
	}
	
	public static int getRepairedItem(int id) {
		if (id == 29713 || id == 29711) //Craw's bow
			return 29711;
		if (id == 29715 || id == 29714) //Viggora's chainmace
			return 29714;
		if (id == 29709 || id == 29708) //Thammaron's sceptre
			return 29708;
		if (id == 29250 || id == 29258) //Bracelet of ethereum
			return 29250;
		return -1;
	}
	
	public static boolean degradesWhenUsed(int id) {
		if (id == 29711 || id == 29714 || id == 29708) //Revenant weapons
			return true;
		return false;
	}

	public static boolean isDungItem(int itemId) {
		if (itemId >= 15750 && itemId <= 18329) {
			return true;
		}
		return false;
	}

	// return amt of charges
	public static int getItemDefaultCharges(int id) {
		if (id == 13910 || id == 13913 || id == 13916 || id == 13919
				|| id == 13922 || id == 13925 || id == 13928 || id == 13931
				|| id == 13934 || id == 13937 || id == 13940 || id == 13943
				|| id == 13946 || id == 13949 || id == 13952)
			return 6000;
		if (id == 13960 || id == 13963 || id == 13966 || id == 13969
				|| id == 13972 || id == 13975)
			return 3000;
		if (id == 13860 || id == 13863 || id == 13866 || id == 13869
				|| id == 13872 || id == 13875 || id == 13878 || id == 13886
				|| id == 13889 || id == 13892 || id == 13901 || id == 13904 
				|| id == 13907 || id == 13960)
			return 24000;
		if (id == 29117 || id == 29118 || id == 29119)
			return 100_000;
		if (id == 29711 || id == 29714 || id == 29708) //Revenant weapons
			return 16_000;
		if (id == 29258) //Revenant weapons
			return 16000;
		if (id == 24573) //Revenant weapons
			return 16000;
		return -1;
	}

	// return what id it degrades to, -1 for disappear which is default so we
	// don't add -1
	public static int getItemDegrade(int id) {
		if (id == 11285) 
			return 11283;	
		if (id == 18349) 
			return 18350;
		if (id == 18351) 
			return 18352;	
		if (id == 18353) 
			return 18354;	
		if (id == 18355) 
			return 18356;		
		if (id == 18357) 
			return 18358;		
		if (id == 18359) 
			return 18360;	
		if (id == 18361) 
			return 18362;	
		if (id == 18363) 
			return 18364;		
		if (id == 29100)
			return 29118;
		if (id == 29118)
			return 29102;
		if (id == 29103)
			return 29119;
		if (id == 29119)
			return 29105;
		if (id == 29097)
			return 29117;
		if (id == 29117)
			return 29099;
		if (id == 29711) //Craw's bow
			return 29713;
		if (id == 29714) //Viggora's chainmace
			return 29715;
		if (id == 29708) //Thammaron's sceptre
			return 29709;
		if (id == 29258) //Bracelet of ethereum
			return 29250;
		return -1;
	}

	public static int getDegradeItemWhenCombating(int id) {
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 29118)
			return true;
		return false;
	}

	public static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		return false;
	}

	public static boolean itemDegradesWhileCombating(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		return false;
	}

	public static boolean canWear(Item item, Player player) {
		String itemName = ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase();

		if (item.getId() == 14640 || item.getId() == 14645 || item.getId() == 15433 || item.getId() == 15430
				|| item.getId() == 14639
				|| item.getId() == 15432
				|| item.getId() == 15434) {
			player.getPackets().sendGameMessage("You need to have completed Nomad's Requiem miniquest to use this cape.");
			return false;
		}
		
		if (item.getId() == 29966) {
			if (player.getSkills().getLevel(Skills.RANGE) < 75) {
				player.getPackets().sendGameMessage("You must have a ranged level of 75 to wield this.");
				return false;
			}
		}

		return true;
	}
	public static boolean isTradeable(Item item) {
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (item.getId() == 18830 ) {
			return true;
		}
//		if (item.getDefinitions().getName().toLowerCase().contains("flaming skull")) {
//			return false;
//		}
//		String name = ItemDefinitions.getItemDefinitions(item.getId()).getName().toLowerCase();
//		if (name.contains("flaming skull") || item.getDefinitions().getName().toLowerCase().equals("top hat") || item.getDefinitions().getName().toLowerCase().equals("pet ") || name.contains("gravite")
//				|| name.contains("arcane stream") || name.contains("arcane blast") || name.contains("arcane pulse") || name.contains("veteran cape") || name.contains("twisted bird skull")
//				|| name.contains("bonecrusher") || name.contains("chaotic") || name.contains("frozen key") || name.contains("herbicide") || name.contains("split dragontooth") || name.contains("demon horn necklace") || name.contains("baron shark") || name.contains("(i)") ||name.contains("(deg") || name.contains("(clas") || name.contains("null") || name.contains("overload") || name.contains("extreme") || name.contains("jericho") || name.contains("dreadnip") || name.contains("max hood") || name.contains("max cape") || name.contains("dungeoneering") || name.contains("vine whip") || name.contains("tokhaar-ka") || name.contains("culinaromancer") || name.contains("spin ticket") || name.contains("tokkul") || name.contains("fighter") || name.contains("fire cape") || name.contains("lamp") || name.contains("completion") || name.contains("arcane stream") || name.contains("arcane pulse") || name.contains("arcane blast") || name.contains("magical blastbox") || name.contains("cape (t)") || name.contains("penance") || name.contains("defender") || name.contains("charm") || name.contains("keenblade") || name.contains("quickbow") || name.contains("mindspike") || name.contains("baby troll") || name.contains("aura") || name.contains("clue scroll") ||  name.contains(" pet") || name.contains("pet ") || name.contains("completionist") || name.contains("max cape") || name.contains("skill cape") || name.contains("defender") || name.contains("tokhaar-kal")) {
//			return false;
//		}
//        if (item.getId() >= 29586 && item.getId() <= 29615) {
//			return false;
//		}
//		switch (item.getId()) {
//		case 6529: //tokkul
//		case 24155:
//		case 24154:
//		case 15098:
//		case 15099:
//		case 23679:
//		case 23680:
//		case 23681:
//		case 28872:
//		case 28873:
//		case 28874:
//		case 29052:
//		case 29053:
//		case 29054:
//		case 29055:
//		case 29056:
//		case 29057:
//		case 29058:
//		case 29059:
//		case 29060:
//		case 29026:
//		case 29027:
//		case 28953:
//		case 28955:
//		case 28824:
//		case 28825:
//		case 28827:
//		case 28828:
//		case 28865:
//		case 28801:
//		case 28802:
//		case 28803:
//		case 28804:
//		case 28805:
//		case 23682:
//		case 23683:
//		case 23684:
//		case 23685:
//		case 23686:
//		case 23687:
//		case 20786:
//		case 23688:
//		case 23689:
//		case 23690:
//		case 23691:
//		case 23692:
//		case 23693:
//		case 23694:
//		case 23695:
//		case 23696:
//		case 23697:
//		case 23698:
//		case 23699:
//		case 23700:
//		case 29803:
//		case 29117:
//		case 29119:
//		case 29118:
//		case 29966:
//		case 29965:
//		case 29945:
//		case 29947:
//		case 29929:
//		case 29886:
//		case 29887:
//		case 29888:
//		case 29875:
//		case 29862:
//		case 29856:
//		case 29855:
//		case 29845:
//		case 29842:
//		case 29792:
//		case 29793:
//		case 29717:
//		case 29670:
//		case 29669:
//		case 29668:
//		case 29667:
//		case 29621:
//		case 29620:
//		case 29619:
//		case 29618:
//		case 29542:
//		case 29541:
//		case 2704:
//		case 2802:
//		case 2724:
//		case 19039:
//		case 1419:
//		case 19706:
//		case 19707:
//		case 19708:
//		case 22321:
//		case 4566:
//		case 14729:
//		case 10867:
//		case 24145:
//		case 4084:
//		case 8871:
//		case 8864:
//		case 29221:
//		case 29223:
//		case 29225:
//		case 15501:
//		case 29216:
//		case 29218:
//		case 29214:
//		case 29207:
//		case 29209:
//		case 29211:
//		case 29184:
//		case 29192:
//		case 29190:
//		case 29188:
//		case 29186:
//		case 25112:
//		case 25351:
//		case 25352:
//		case 25353:
//		case 25211:
//		case 25273:
//		case 25275:
//		case 25277:
//		case 25279:
//		case 25281:
//		case 25283:
//		case 25110:
//		case 25114:
//		case 25115:
//		case 25124:
//		case 24930:
//		case 24849:
//		case 24850:
//		case 24851:
//		case 24852:
//		case 24439:
//		case 24440:
//		case 24298:
//		case 24294:
//		case 24149:
////			return false;
//		default:
//			return true;
//		}
		return item.getDefinitions().grandExchange;
	}

	public static int getAssassinModels(int slot, boolean male) {
		switch (slot) {
		case Equipment.SLOT_HANDS:
			return male ? 71809 : 71864;
		case Equipment.SLOT_FEET:
			return male ? 71804 : 71862;
		case Equipment.SLOT_LEGS:
			return male ? 71821 : 71876;
		case Equipment.SLOT_CHEST:
			return male ? 71832 : 71893;
		case Equipment.SLOT_HAT:
			return male ? 71816 : 75135;
		case Equipment.SLOT_CAPE:
			return male ? 58 : 58;
		case Equipment.SLOT_WEAPON:
			return male ? 71853 : 71853;
		case Equipment.SLOT_SHIELD:
			return male ? 71853 : 71853;
		}
		return -1;
	}

	public static int getDemonFleshModels(int slot, boolean male) {
		switch (slot) {
		case Equipment.SLOT_HANDS:
			return male ? 82777 : 82777;
		case Equipment.SLOT_FEET:
			return male ? 82854 : 82778;
		case Equipment.SLOT_LEGS:
			return male ? 82853 : 82852;
		case Equipment.SLOT_CHEST:
			return male ? 82801 : 82800;
		case Equipment.SLOT_HAT:
			return male ? 82892 : 82892;
		case Equipment.SLOT_CAPE:
			return male ? 82756 : 82756;
		}
		return -1;
	}

	public static int getSwashbuclerModels(int slot, boolean male) {
		switch (slot) {
		case Equipment.SLOT_FEET:
			return male ? 71803 : 71861;
		case Equipment.SLOT_LEGS:
			return male ? 71819 : 71875;
		case Equipment.SLOT_CHEST:
			return male ? 71831 : 71890;
		case Equipment.SLOT_HAT:
			return male ? 71814 : 71872;
		case Equipment.SLOT_CAPE:
			return male ? 245 : 245;
		}
		return -1;
	}

	public static int getColoseumModels(int slot, boolean male) {
		switch (slot) {
		case Equipment.SLOT_FEET:
			return male ? 71802 : 71860;
		case Equipment.SLOT_LEGS:
			return male ? 71818 : 71879;
		case Equipment.SLOT_CHEST:
			return male ? 71835 : 71892;
		case Equipment.SLOT_HAT:
			return male ? 71813 : 71873;
		}
		return -1;
	}

	public static int getKalphiteSentimentelModels(int slot, boolean male) {
		switch (slot) {
		case Equipment.SLOT_HANDS:
			return male ? 82915 : 82915;
		case Equipment.SLOT_FEET:
			return male ? 82818 : 82818;
		case Equipment.SLOT_LEGS:
			return male ? 82912 : 82911;
		case Equipment.SLOT_CHEST:
			return male ? 82910 : 82917;
		case Equipment.SLOT_HAT:
			return male ? 82918 : 82918;
		case Equipment.SLOT_CAPE:
			return male ? 82829 : 82830;
		}
		return -1;
	}

	public static int getTokhaarWarlordModels(int slot, boolean male) {
		switch (slot) {
		case Equipment.SLOT_HANDS:
			return male ? 81106 : 81106;
		case Equipment.SLOT_FEET:
			return male ? 81099 : 81099;
		case Equipment.SLOT_LEGS:
			return male ? 81124 : 81160;
		case Equipment.SLOT_CHEST:
			return male ? 81137 : 81174;
		case Equipment.SLOT_HAT:
			return male ? 81115 : 81153;
		}
		return -1;
	}

	public static int getKrilGoldcrusherModels(int slot, boolean male) {
		switch (slot) {
		case Equipment.SLOT_HANDS:
			return male ? 78586 : 78586;
		case Equipment.SLOT_FEET:
			return male ? 78584 : 78584;
		case Equipment.SLOT_LEGS:
			return male ? 78593 : 78621;
		case Equipment.SLOT_CHEST:
			return male ? 78601 : 78622;
		case Equipment.SLOT_HAT:
			return male ? 78592 : 78617;
		}
		return -1;
	}


	public static boolean isDestroy(Item item) {
		return item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended();
	}
}
