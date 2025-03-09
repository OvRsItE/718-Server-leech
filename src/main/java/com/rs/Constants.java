package com.rs;

import java.math.BigInteger;

import com.rs.game.map.Position;

public final class Constants {

	/**
	 * Everything RS
	 */
//	public static final String VALIUS_OLD = "6h1iskj01admm8b1cd0ahbbj4is4lll0wcd7wvkn48b71cbx1orl37ukchv74ym9sctrvdzhbyb9";
//	public static final String OSHD_KEY = "ut4jvhjhdlj0z3rxchjl40a4i6f2oib0td0tzmwrntgwzkkpgb95ylfmxjlzccy4zml46ye97ldi";

	/**
	 * MYSQL Database settings.
	 */
	public static final String DB_HOST = "http://www.realityx718.com";
	public static final String DB_USER = "realityx_hscore";
	public static final String DB_PASS = "mohammed1+++";
	public static final String DB_NAME = "realityx_hscore";

	public static final boolean SQUEAL_OF_FORTUNE_ENABLED = true; // if not, people will be able to spin but not claim
	public static final String BUY_SPINS_LINK = "";

	public static final double[] SOF_CHANCES = new double[] { 0.9D, 0.40D, 0.045D, 0.035D };

	public static final int[] SOF_COMMON_CASH_AMOUNTS = new int[] { 100, 250, 500, 1000, 5000 };
	public static final int[] SOF_UNCOMMON_CASH_AMOUNTS = new int[] { 10000, 25000, 50000, 100000, 500000 };
	public static final int[] SOF_RARE_CASH_AMOUNTS = new int[] { 1000000, 2500000, 5000000, 10000000, 25000000 };
	public static final int[] SOF_JACKPOT_CASH_AMOUNTS = new int[] { 50 * 1000000, 50 * 1000000, 75 * 1000000,
			100 * 1000000 };
	public static final int[] SOF_COMMON_LAMPS = new int[] { 23713, 23717, 23721, 23725, 23729, 23737, 23733, 23741,
			23745, 23749, 23753, 23757, 23761, 23765, 23769, 23778, 23774, 23786, 23782, 23794, 23790, 23802, 23798,
			23810, 23806, 23814 };
	public static final int[] SOF_UNCOMMON_LAMPS = new int[] { 23714, 23718, 23722, 23726, 23730, 23738, 23734, 23742,
			23746, 23750, 23754, 23758, 23762, 23766, 23770, 23779, 23775, 23787, 23783, 23795, 23791, 23803, 23799,
			23811, 23807, 23815 };
	public static final int[] SOF_RARE_LAMPS = new int[] { 23715, 23719, 23723, 23727, 23731, 23739, 23735, 23743,
			23747, 23751, 23755, 23759, 23763, 23767, 23771, 23780, 23776, 23788, 23784, 23796, 23792, 23804, 23800,
			23812, 23808, 23816 };
	public static final int[] SOF_JACKPOT_LAMPS = new int[] { 23716, 23720, 23724, 23728, 23732, 23740, 23736, 23744,
			23748, 23752, 23756, 23760, 23764, 23768, 23773, 23781, 23777, 23789, 23785, 23797, 23793, 23805, 23801,
			23813, 23809, 23817 };
	public static final int[] SOF_COMMON_OTHERS = new int[] { 2437, 1512, 2447, 439, 3041, 2441, 2445, 11212, 3025,
			2435, 6686, 437, 386, 15273, 454, 452, 441, 450, 448, 2506, 2510, 2508, 1746, 6694, 2350, 2352, 2354, 2360,
			2364, 1516, 1522, 1520, 1514, 537};
	public static final int[] SOF_UNCOMMON_OTHERS = new int[] { 24154, 21446, 10939, 10933, 10940, 10941, 21482, 21483,
			21532, 21533, 25195, 25196, 25197, 25198, 25199, 21517, 21518, 21516, 21519, 20787, 20788, 20789, 20790,
			20791, 24427, 24428, 24429, 24430, 21447, 25180, 25181, 25182, 25183, 25184, 21448, 21449, 21450, 25190,
			25191, 25192, 25193, 25194, 24154, 24155, 24155,};
	public static final int[] SOF_RARE_OTHERS = new int[] { 995, 995, 995, 995, 23665, 23666, 23667, 23668, 23669,
			23670, 23671, 23672, 23673, 23674, 23675, 23676, 23677, 23678, 23679, 23680, 23681, 23682, 23691, 23692,
			23693, 23694, 23695, 23696, 23687, 23688, 23689, 23684, 23686, 23685, 23697, 23690, 23699, 23700, 23683,
			23698 };
	public static final int[] SOF_JACKPOT_OTHERS = new int[] { 995, 995, 995, 995, 20929, 23671, 23672, 29035, 29016,
			29048 };
	/**
	 * Custom Sof items (noteds)
	 */

	/**
	 * General client and server settings.
	 */

	public static final String SERVER_NAME = "Venomite";
	public static final int PORT_ID = 43595;
	public static final String LASTEST_UPDATE = "<col=7E2217>Check the forums for the latest updates!";
	public static final String CACHE_PATH = "data/cache/";
	public static final int RECEIVE_DATA_LIMIT = 7500;
	public static final int PACKET_SIZE_LIMIT = 7500;
	public static final int CLIENT_BUILD = 742;
	public static final int CUSTOM_CLIENT_BUILD = 46;
	public static final String LOG_PATH = "data/logs/";
	public static final int MAX_STARTER_COUNT = 1;

	/**
	 * Link settings
	 */

	public static final String[] DEVELOPERS = { "" };

	public static final String OWNER[] = { "corey", "justice" };

    public static boolean isDeveloper(String name) {
		for (String s : DEVELOPERS) {
			if (name.toLowerCase().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOwner(String name) {
		for (String s : OWNER) {
			if (name.toLowerCase().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	/** Links */
	public static final String WEBSITE_LINK = "https://venomite.io/";
	public static final String ITEMLIST_LINK = "";
	public static final String ITEMDB_LINK = "";
	public static final String VOTE_LINK = "https://venomite.io/vote/";
	public static final String DONATE_LINK = "https://venomite.io/store/";
	public static final String HISCORES_LINK = "";
	public static final String RULES_LINK = "";
	public static final String FORUMS_LINK = "";
	public static final String DISCORD_URL = "https://discord.com/invite/ank8UUFk9z";

	public static boolean DEBUG = false;
	public static boolean GUIMODE;
	public static boolean HOSTED = true;
	public static boolean ECONOMY = true;

	/**
	 * If the use of the managment server is enabled.
	 */

	public static boolean MANAGMENT_SERVER_ENABLED = true;

	/**
	 * Graphical User Interface settings
	 */

	public static final String GUI_SIGN = "Matrix GUI";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * XP well related.
	 */
	public static final int WELL_MAX_AMOUNT = 100000000;

	/**
	 * Player settings
	 */

	public static final int START_PLAYER_HITPOINTS = 100;
	public static final Position START_PLAYER_LOCATION = new Position(3613, 2654, 0);
	public static final Position RESPAWN_DEATH_LOCATION = new Position(3613, 2654, 0);
	public static final Position[] RESPAWN_PLAYER_LOCATION = { new Position(3613, 2654, 0),
			new Position(3613, 2654, 0) };
	public static final Position[] HOME_PLAYER_LOCATION = { new Position(3613, 2654, 0),
			new Position(3613, 2654, 0) };
	public static final Position HOME_PLAYER_LOCATION1 = new Position(3613, 2654, 0);
	public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000;
	public static int DROP_RATE = 1;
	public static double LAMP_XP_RATE = 8;

	/**
	 * XP Rates
	 */
	public static int ATTACK_XP_RATE = 250;
	public static int STRENGTH_XP_RATE = 250;
	public static int DEFENCE_XP_RATE = 250;
	public static int HITPOINTS_XP_RATE = 250;
	public static int RANGE_XP_RATE = 250;
	public static int MAGIC_XP_RATE = 250;
	public static int PRAYER_XP_RATE = 100;
	public static int SLAYER_XP_RATE = 35;
	public static int FARMING_XP_RATE = 35;
	public static int AGILITY_XP_RATE = 35;
	public static int HERBLORE_XP_RATE = 35;
	public static int THIEVING_XP_RATE = 35;
	public static int CRAFTING_XP_RATE = 35;
	public static int MINING_XP_RATE = 35;
	public static int SMITHING_XP_RATE = 35;
	public static int FISHING_XP_RATE = 35;
	public static int COOKING_XP_RATE = 35;
	public static int FIREMAKING_XP_RATE = 35;
	public static int WOODCUTTING_XP_RATE = 35;
	public static int CONSTRUCTION_XP_RATE = 35;
	public static int HUNTER_XP_RATE = 35;
	public static int SUMMONING_XP_RATE = 35;
	public static int DUNGEONEERING_XP_RATE = 35;
	public static int FLETCHING_RATE = 35;
	public static int RUNECRAFTING_XP_RATE = 35;

	/**
	 * World settings
	 */

	public static final int WORLD_CYCLE_TIME = 600;

	/**
	 * Music & Emote settings
	 */

	public static final int AIR_GUITAR_MUSICS_COUNT = 1;

	/**
	 * Quest settings
	 */

	public static final int QUESTS = 183;

	/**
	 * Goblin Raids
	 */
	public static boolean GOBLINRAID;

	/**
	 * Memory settings
	 */

	public static final int PLAYERS_LIMIT = 2000;
	public static final int LOCAL_PLAYERS_LIMIT = 250;
	public static final int NPCS_LIMIT = Short.MAX_VALUE;
	public static final int LOCAL_NPCS_LIMIT = 250;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000;

	/**
	 * Game constants
	 */

	public static final int[] MAP_SIZES = { 104, 120, 136, 168, 72, 208 }; //104

	public static final String GRAB_SERVER_TOKEN = "hAJWGrsaETglRjuwxMwnlA/d5W6EgYWx";
	public static final int[] GRAB_SERVER_KEYS = { 1441, 78700, 44880, 39771, 363186, 44375, 0, 16140, 7316, 271148,
			810710, 216189, 379672, 454149, 933950, 21006, 25367, 17247, 1244, 1, 14856, 1494, 119, 882901, 1818764,
			3963, 3618 };

	public static final BigInteger GRAB_SERVER_PRIVATE_EXPONENT = new BigInteger(
			"95776340111155337321344029627634178888626101791582245228586750697996713454019354716577077577558156976177994479837760989691356438974879647293064177555518187567327659793331431421153203931914933858526857396428052266926507860603166705084302845740310178306001400777670591958466653637275131498866778592148380588481");
	public static final BigInteger GRAB_SERVER_MODULUS = new BigInteger(
			"119555331260995530494627322191654816613155476612603817103079689925995402263457895890829148093414135342420807287820032417458428763496565605970163936696811485500553506743979521465489801746973392901885588777462023165252483988431877411021816445058706597607453280166045122971960003629860919338852061972113876035333");

	public static final BigInteger PRIVATE_EXPONENT = new BigInteger(
			"90587072701551327129007891668787349509347630408215045082807628285770049664232156776755654198505412956586289981306433146503308411067358680117206732091608088418458220580479081111360656446804397560752455367862620370537461050334224448167071367743407184852057833323917170323302797356352672118595769338616589092625");
	public static final BigInteger MODULUS = new BigInteger(
			"102876637271116124732338500663639643113504464789339249490399312659674772039314875904176809267475033772367707882873773291786014475222178654932442254125731622781524413208523465520758537060408541610254619166907142593731337618490879831401461945679478046811438574041131738117063340726565226753787565780501845348613");

	public static String[] UNTRADEABLE_ITEMS = { "(deg", "(class", "sacred clay", "null", "flaming skull", "overload",
			"extreme", "dreadnip", "max hood", "max cape", "dungeoneering", "tokhaar-kal", "culinaromancer",
			"spin ticket", "tokkul", "fighter", "fire cape", "lamp", "completion", "magical blastbox", "cape (t)",
			"penance", "defender", "charm", "keenblade", "quickbow", "mindspike", "baby troll", "aura ",
			"completionist", "max cape", "skill cape", " defender", "fire cape", "tokhaar-kal", "lucky", "dominion",
			"Master Cape", "RxP scroll", "costume", "sled", "christmas", "pet", "candy cane", "master" };

	// public static String[] RARE_DROPS = { "torva", "pernix", "virtus", "sigil",
	// "bandos", "armadyl", "vesta", "visage", "drygore", "shield", "abbysal",
	// "deathbringer", "soulstealer", "cataclysm", "legacy", "veren", "wrath",
	// "defensive", "valkyrie", "tanks", "demon", "old" };

	// Player Item Settings
	public static String[] MASTERITEMS = { "Master Herblore Cape", "Master Attack Cape", "Master Strength Cape",
			"Master Runecrafting Cape", "Master Hunter Cape", "Master Constitution Cape", "Master Prayer Cape",
			"Master Ranged Cape", "Master Woodcutting Cape", "Master Cooking Cape", "Master Fletching Cape",
			"Master Slayer Cape", "Master Herblore Cape", "Master Mining Cape", "Master Smithing Cape",
			"Master Summoning Cape", "Master Defence Cape", "Master Magic Cape", "Master Fishing Cape",
			"Master Firemaking Cape", "Master Construction Cape", "Master Farming Cape", "Master Crafting Cape" };

	/**
	 * npc settings
	 */
	public static final int[] NON_WALKING_NPCS1 = { 6892, 9085, 8480, 1597, 1598, 8481, 598, 945, 8480, 720, 599, 5626, 598,
			5925, 550, 6970, 599, 266, 546, 1694, 8227, 3789, 279, 948, 219, 7530, 524, 1918, 3021, 529, 2617, 4288,
			6892, 3820, 538, 587, 5112, 11674, 1699, 2259, 552, 11678, 6070, 554, 551, 534, 585, 1597, 6361, 5626, 548,
			1167, 8556, 528, 457, 576, 5866, 11, 14240 };
	
	protected static boolean YELL_DISABLED = false;

	private static final int[] OSRS_REGIONS = new int[] {
			//home
			14377,
			12961, 9116, 9363, 9619,
			//Theater of blood lobby
			14385, 14386, 14642, 14641,
			//Zulrah
			8751,
			8752,
			9007,
			9008,
			8495,
			8496,
			9773,
			10029,
			10028,
			10284,
			9516, 9517, 9515, 9771, 10027,
			//Zeah
			4668, 4924, 5180, 5436,
			4667, 4923, 5179, 5435,
			4666, 4922, 5178, 5434,
			4665, 4921, 5177, 5433,
			5695, 5951, 6207, 6463, 6719, 6975, 7231, 7487, 7743, 5694, 5950, 6206, 6462, 6718, 6974, 7230, 7486,
			7742, 5693, 5949, 6205, 6461, 6717, 6973, 7229, 7485, 7741, 5692, 5949, 6204, 6460, 6716, 6972, 7228,
			7484, 7740, 5691, 5948, 6203, 6459, 6715, 6971, 7227, 7483, 7739, 5690, 5947, 6202, 6458, 6714, 6970,
			7226, 7482, 7738, 5689, 5946, 6201, 6457, 6713, 6969, 7225, 7481, 7737, 4664, 4920, 5176, 5432, 5688,
			5945, 6200, 6457, 6712, 6968, 7224, 7480, 7736, 4663, 4919, 5176, 5431, 5687, 5944, 6199, 6456, 6711,
			6967, 7223, 7479, 7735, 4662, 6918, 5174, 5430, 5686, 5943, 6198, 6455, 6710, 6966, 7222, 7478, 7734, 5942,
			5685, 5941, 6197, 6453, 6709, 6965, 7221, 7477, 7733, 5684, 5940, 6196, 6452, 6708, 6964, 7220, 7476, 6454, 6709,
			7732, 5175,
			//Fossile island
			14142,
			14393, 14394, 14395, 14396, 14398,
			14649, 14650, 14651, 14652, 14653, 14654,
			14905, 14906, 14907, 14908, 14909, 14910,
			15161, 15162, 15163, 15164, 15165, 15166,
			15417, 15418, 15419, 15420, 15421, 15422,
			//Cerberus
			4883,
			//Skotizo
			6810,
			//Vorkath
			9023,
			//Adamant & rune dragons
			6223,
			//Khorend dungeon
			6556,
			6557,
			6812,
			6813,
			7069,
			6301,
			6299,
			6555,
			6811,
			7067,
			7068,
			//Keymaster
			5139,
			//Inferno
			9043,
			//Myths guild
			9772,
			//Demonic gorillas
			8536, 8280,
			//Raids
			12889, 13145, 13401,
			13141, 13397,
			13140, 13396,
			13139, 13395,
			13138, 13394,
			13137, 13393,
			13136, 13392,
			9043,
			//Gargoyle boss
			6727,
			//Abyssal sire
			11851, 12107, 12363,
			11850, 12106, 12362,
			//Priff
			8501, 8757, 9013, 8500, 8756, 9012, 8499, 8755, 9011,
			12637, 12638, 12639, 12640, 12893, 12894, 12895, 12896,
			13149, 13150, 13151, 13152, 13405, 13406, 13407, 13408,
			//Gauntlet
			7512, 7768,
			//Theatre of blood
			12613, 12612, 12611, 12867, 13123, 13122, 12869, 13125,
			14679, 14680, 14681, 14937, 14936, 14935, 15191, 15192, 15193,
			//Hydra
			5022, 5023, 5024, 5279, 5535, 5280, 5536,5278, 5534,
			//smoke devil dungeon
			11559,
			15399,
			15143,
			11815,
			14887,
			5159,
			4649,
			4905,
			14661,
			8211,
			7955,
			7954,
			4962,
			11303,
			11868,
			//zalcano
			12126
	};
	
	public static final int OSRS_MODELS_OFFSET = 300000;
	public static final int OSRS_OBJECTS_OFFSET = 200000;
	public static final int OSRS_SEQ_OFFSET = 20000;
	public static final int OSRS_ITEMS_OFFSET = 30000;
	public static final int OSRS_SPOTS_OFFSET = 5000;
	public static final int OSRS_NPCS_OFFSET = 20000;
	public static final int OSRS_BAS_OFFSET = 20_000;
	public static final int OSRS_ENUM_OFFSET = 10000;

	public static boolean isOSRSRegion(int regionId) {
		for (int region : OSRS_REGIONS) {
			if (region == regionId)
				return true;
		}
		return false;
	}

	private Constants() {

	}
}
