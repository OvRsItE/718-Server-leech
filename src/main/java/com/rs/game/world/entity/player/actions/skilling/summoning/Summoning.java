package com.rs.game.world.entity.player.actions.skilling.summoning;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.others.DreadNip;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.summoning.SummoningReq.Requirements;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

/*
 * we gotta move this to content when we go economy and recreate all chars. To bad u putted it at wrong path.. now its to late
 */
public class Summoning {

	public static enum Pouches {

		SPIRIT_WOLF(6829, 67, 12047, 1, 0.1, 4.8, 360000, 1, 12425),

		DREADFOWL(6825, 69, 12043, 4, 0.1, 9.3, 240000, 1, 12445),

		SPIRIT_SPIDER(6841, 83, 12059, 8, 0.2, 12.6, 900000, 2, 12428),

		THORNY_SNAIL(6807, 119, 12019, 13, 0.2, 12.6, 960000, 2, 12459),

		GRANITE_CRAB(6796, 75, 12009, 16, 0.2, 21.6, 1080000, 2, 12533),

		SPIRIT_MOSQUITO(7332, 177, 12778, 17, 0.2, 46.5, 720000, 2, 12838),

		DESERT_WYRM(6832, 121, 12049, 18, 0.4, 31.2, 1140000, 1, 12460),

		SPIRIT_SCORPIAN(6838, 101, 12055, 19, 0.9, 83.2, 1020000, 2, 12432),

		SPIRIT_TZ_KIH(7362, 179, 12808, 22, 1.1, 96.8, 1080000, 3, 12839),

		ALBINO_RAT(6848, 103, 12067, 23, 2.3, 202.4, 1320000, 3, 12430),

		SPIRIT_KALPHITE(6995, 99, 12063, 25, 2.5, 220, 1320000, 3, 12446),

		COMPOST_MOUNT(6872, 137, 12091, 28, 0.6, 49.8, 1440000, 6, 12440), // TODO

		GIANT_CHINCHOMPA(7354, 165, 12800, 29, 2.5, 255.2, 1860000, 1, 12834), // TODO

		VAMPYRE_BAT(6836, 71, 12053, 31, 1.6, 136, 1980000, 4, 12447), // TODO

		HONEY_BADGER(6846, 105, 12065, 32, 1.6, 140.8, 1500000, 4, 12433), // TODO

		BEAVER(9475, 89, 12021, 33, 0.7, 57.6, 1620000, 4, 12429), // TODO

		VOID_RAVAGER(7371, 157, 12818, 34, 0.7, 59.6, 1620000, 4, 12443), // TODO

		VOID_SPINNER(7334, 157, 12780, 34, 0.7, 59.6, 1620000, 4, 12443), // TODO

		VOID_TORCHER(7352, 157, 12798, 34, 0.7, 59.6, 5640000, 4, 12443), // TODO

		VOID_SHIFTER(7369, 157, 12814, 34, 0.7, 59.6, 5640000, 4, 12443), // TODO

		BRONZE_MINOTAUR(6854, 149, 12073, 36, 2.4, 316.8, 1800000, 9, 12461), // TODO

		BULL_ANT(6969, 91, 12087, 40, 0.6, 52.8, 1800000, 5, 12431), // TODO

		MACAW(6852, 73, 12071, 41, 0.8, 72.4, 1860000, 5, 12422), // TODO

		EVIL_TURNIP(6834, 77, 12051, 42, 2.1, 184.8, 1800000, 5, 12448), // TODO

		SPIRIT_COCKATRICE(6876, 149, 12095, 43, 0.9, 75.2, 2160000, 5, 12458), // TODO

		SPIRIT_GUTHATRICE(6878, 149, 12097, 43, 0.9, 75.2, 2160000, 5, 12458), // TODO

		SPIRIT_SARATRICE(6880, 149, 12099, 43, 0.9, 75.2, 2160000, 5, 12458), // TODO

		SPIRIT_ZAMATRICE(6882, 149, 12101, 43, 0.9, 75.2, 2160000, 5, 12458), // TODO

		SPIRIT_PENGATRICE(6884, 149, 12103, 43, 0.9, 75.2, 2160000, 5, 12458), // TODO

		SPIRIT_CORAXATRICE(6886, 149, 12105, 43, 0.9, 75.2, 2160000, 5, 12458), // TODO

		SPIRIT_VULATRICE(6888, 149, 12107, 43, 0.9, 75.2, 2160000, 5, 12458), // TODO

		IRON_MINOTAUR(6856, 149, 12075, 46, 4.6, 404.8, 2220000, 9, 12462), // TODO

		PYRELORD(7378, 185, 12816, 46, 2.3, 202.4, 1920000, 5, 12829), // TODO

		MAGPIE(6824, 81, 12041, 47, 0.9, 83.2, 2040000, 5, 12426), // TODO

		BLOATED_LEECH(6844, 131, 12061, 49, 2.4, 215.2, 2040000, 5, 12444), // TODO

		SPIRIT_TERRORBIRD(6795, 129, 12007, 52, 0.7, 68.4, 2160000, 6, 12441),

		ABYSSAL_PARASITE(6819, 125, 12035, 54, 1.1, 94.8, 1800000, 6, 12454), // TODO

		SPIRIT_JELLY(6993, 123, 12027, 55, 5.5, 484, 2580000, 6, 12453), // TODO

		STEEL_MINOTAUR(6858, 149, 12077, 56, 5.6, 492.8, 2760000, 9, 12463), // TODO

		IBIS(6991, 85, 12531, 56, 1.1, 98.8, 2280000, 6, 12424), // TODO

		SPIRIT_KYATT(7364, 169, 12812, 57, 5.7, 501.6, 2940000, 6, 12836), // TODO

		SPIRIT_LARUPIA(7366, 181, 12784, 57, 5.7, 501.6, 2940000, 6, 12840), // TODO

		SPIRIT_GRAAHK(7338, 167, 12810, 57, 5.6, 501.6, 2940000, 6, 12835), // TODO

		KARAMTHULU_OVERLOAD(6810, 135, 12023, 58, 5.8, 510.4, 2640000, 6, 12455), // TODO

		SMOKE_DEVIL(6866, 133, 12085, 61, 3.1, 268, 2880000, 7, 12468), // TODO

		ABYSSAL_LURKER(6821, 87, 12037, 62, 1.9, 109.6, 2460000, 7, 12427), // TODO

		SPIRIT_COBRA(6803, 115, 12015, 63, 3.1, 276.8, 3360000, 7, 12436), // TODO

		STRANGER_PLANT(6828, 141, 12045, 64, 3.2, 281.6, 2940000, 7, 12467), // TODO

		MITHRIL_MINOTAUR(6860, 149, 12079, 66, 6.6, 580.8, 3300000, 9, 12464), // TODO

		BARKER_TOAD(6890, 107, 12123, 66, 1, 87, 480000, 7, 12452), // TODO

		WAR_TORTOISE(6816, 117, 12031, 67, 0.7, 58.6, 2580000, 7, 12439), // TODO

		BUNYIP(6814, 153, 12029, 68, 1.4, 119.2, 2640000, 7, 12438),

		FRUIT_BAT(6817, 79, 12033, 69, 1.4, 121.2, 2700000, 7, 12423), // TODO

		RAVENOUS_LOCUST(7374, 97, 12820, 70, 1.5, 132.0, 1440000, 4, 12830),

		ARCTIC_BEAR(6840, 109, 12057, 71, 1.1, 93.2, 1680000, 8, 12451), // TODO

		PHEONIX(8549, -1, 14623, 72, 3, 301, 1800000, 8, -1), // TODO

		OBSIDIAN_GOLEM(7346, 173, 12792, 73, 7.3, 642.4, 3300000, 8, 12826), // TODO

		GRANITE_LOBSTER(6850, 93, 12069, 74, 3.7, 325.6, 2920000, 8, 12449), // TODO

		PRAYING_MANTIS(6799, 95, 12011, 75, 3.6, 329.6, 4140000, 8, 12450), // TODO

		FORGE_REGENT(7336, 187, 12782, 76, 1.5, 134, 2700000, 9, 12841), // TODO

		ADAMANT_MINOTAUR(6862, 149, 12081, 76, 8.6, 668.8, 3960000, 9, 12465), // TODO

		TALON_BEAST(7348, 143, 12794, 77, 3.8, 1015.2, 2940000, 9, 12831), // TODO

		GIANT_ENT(6801, 139, 12013, 78, 1.6, 136.8, 2940000, 8, 12457), // TODO

		FIRE_TITAN(7356, 159, 12802, 79, 7.9, 695.2, 3720000, 9, 12824),

		MOSS_TITAN(7358, 159, 12804, 79, 7.9, 695.2, 3720000, 9, 12824),

		ICE_TITAN(7360, 159, 12806, 79, 7.9, 695.2, 3720000, 9, 12824),

		HYDRA(6812, 145, 12025, 80, 1.6, 140.8, 2940000, 8, 12442), // TODO

		SPIRIT_DAGANNOTH(6805, 147, 12017, 83, 4.1, 364.8, 3420000, 9, 12456), // TODO

		LAVA_TITAN(7342, 171, 12788, 83, 8.3, 730.4, 3660000, 9, 12837),

		SWAMP_TITAN(7330, 155, 12776, 85, 4.2, 373.6, 3360000, 9, 12832),

		RUNE_MINOTAUR(6864, 149, 12083, 86, 8.6, 756.8, 9060000, 9, 12466), // TODO

		UNICORN_STALLION(6823, 113, 12039, 88, 1.8, 154.4, 3240000, 9, 12434),

		GEYSER_TITAN(7340, 161, 12786, 89, 8.9, 783.2, 4140000, 10, 12833),

		WOLPERTINGER(6870, 151, 12089, 92, 4.6, 404.8, 3720000, 10, 12437),

		ABYSSAL_TITAN(7350, 175, 12796, 93, 1.9, 163.2, 1920000, 10, 12827),

		IRON_TITAN(7376, 183, 12822, 95, 8.6, 417.6, 3600000, 10, 12828),

		PACK_YAK(6874, 111, 12093, 96, 4.8, 422.2, 3480000, 10, 12435),

		STEEL_TITAN(7344, 163, 12790, 99, 4.9, 435.2, 3840000, 10, 12825);

		private static final Map<Integer, Pouches> pouches = new HashMap<Integer, Pouches>();

		static {
			for (Pouches pouch : Pouches.values()) {
				pouches.put(pouch.pouchId, pouch);
			}
		}

		public static Pouches forId(int id) {
			return pouches.get(id);
		}

		public static Map<Integer, Pouches> getPouches() {
			return pouches;
		}
		private int npcId;
		private int pouchId;
		private int level;
		private int spawnCost;
		private double useExp;
		private double creationExp;
		private int configId;
		private long time;

		private int scrollId;

		private Pouches(int npcId, int configId, int pouchId, int level,
				double useExp, double creationExp, long time, int spawnCost,
				int scrollId) {
			this.npcId = npcId;
			this.pouchId = pouchId;
			this.level = level;
			this.spawnCost = spawnCost;
			this.useExp = useExp;
			this.creationExp = creationExp;
			this.time = time;
			this.scrollId = scrollId;
		}

		public int getConfigId() {
			return configId;
		}

		public double getCreationExp() {
			return creationExp;
		}

		public int getLevel() {
			return level;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getPouchId() {
			return pouchId;
		}

		public int getScrollId() {
			return scrollId;
		}

		public int getSpawnCost() {
			return spawnCost;
		}

		public long getTime() {
			return time;
		}

		public double getUseExp() {
			return useExp;
		}
	}
	
	public static int INTERFACE = 672;

	@SuppressWarnings("unused")
	private static int GOLD = 12158, GREEN = 12159, CRIMSON = 12160, ABYSSAL = 12161,
					   TALON = 12162, BLUE = 12163, RAVAGER = 12164, SHIFTER = 12165,
					   SPINNER = 12166, TORCHER = 12167, OBSIDIAN = 12168;

	public static void attackDreadnipTarget(NPC target, Player player) {
		if (target.isDead()) {
			player.getPackets().sendGameMessage("Your target is already dead!");
			return;
		} else if (player.getTemporaryAttributtes().get("hasDN") != null) {
			player.getPackets().sendGameMessage("Your target is already dead!");
			return;
		} else if(player.getFamiliar() != null && target == player.getFamiliar()) {
			player.getPackets().sendGameMessage("");
			return;
		}
		closeDreadnipInterface(player);
		DreadNip npc = new DreadNip(player, 14416, player, -1, false);
		if (target == npc)
			return;
		player.getTemporaryAttributtes().put("hasDN", npc);
		player.getTemporaryAttributtes().put("target", target);
		npc.setTarget(target);
		npc.animate(new Animation(14441));
		npc.getTemporaryAttributtes().put("hasDN", target);
		player.getInventory().deleteItem(22370, 1);
	}
	
	public static void bunyipTimer(Player p) {
		long time = Pouches.BUNYIP.time;
		int i;
		int t;
		int percentHealed = (int)(p.getMaxHitpoints() * 2.00);
		for (i = 0; i <= time; i++) {
			for (t = 0; t < 15000; t++) {
				if (t == 15000) {
				p.animate(new Animation(7660));
				p.setNextGraphics(new Graphics(1300));
				p.heal(percentHealed);
				t = 0;
				}
			}// t
		}// i
	}
	
	public static void closeDreadnipInterface(Player player) {
		player.getInterfaceManager().closeInventory();
		player.getInterfaceManager().sendInventory();
		player.getInventory().unlockInventoryOptions();
	}

	public static Familiar createFamiliar(Player player, Pouches pouch) {
		try {
			return (Familiar) Class
					.forName(
							"com.rs.game.npc.familiar."
									+ (NPCDefinitions.getNPCDefinitions(pouch
											.getNpcId())).name.replace(" ", "")
											.replace("-", ""))
					.getConstructor(Player.class, Pouches.class,
							Position.class, int.class, boolean.class)
					.newInstance(player, pouch, player, -1, true);
		} catch (Throwable e) {
			
			return null;
		}
	}

	public static int getRealPouch(Player player, int slotId) {
		switch(slotId) {
		case 2:
		return 12047;
		case 7:
		return 12043;
		case 12:
		return 12059;		
	    case 17:
	    return 12019;
		case 22:
		return 12009;
		case 27:
		return 12778;
		case 32:
		return 12049;
		case 37:
		return 12055;
		case 42:
		return 12808;
		case 47:
		return 12067;
		case 52:
		return 12063;
		case 57:
		return 12091;
		case 62:
		return 12800;
		case 67:
		return 12053;
		case 72:
		return 12065;
		case 77:
		return 12021;
		case 82:
		return 12818;
		case 87:
		return 12780;
		case 92:
		return 12798;
		case 97:
		return 12814;
		case 102:
		return 12073;
		case 107:
		return 12087;
		case 112:
		return 12071;
		case 117:
		return 12051;
		case 122:
		return 12095;
		case 127:
		return 12097;
		case 132:
		return 12099;
		case 137:
		return 12101;
		case 142:
		return 12103;
		case 147:
		return 12105;
		case 152:
		return 12107;
		case 157:
		return 12075;
		case 162:
		return 12816;
		case 167:
		return 12041;
		case 172:
		return 12061;
		case 177:
		return 12007;
		case 182:
		return 12035;
		case 187:
		return 12027;
		case 192:
		return 12077;
		case 197:
		return 12531;
		case 202:
		return 12812;
		case 207:
		return 12784;
		case 212:
		return 12810;
		case 217:
		return 12023;
		case 222:
		return 12085;
		case 227:
		return 12037;
		case 232:
		return 12015;
		case 237:
		return 12045;
		case 242:
		return 12079;
		case 247:
		return 12123;
		case 252:
		return 12031;
		case 257:
		return 12029;
		case 262:
		return 12033;
		case 267:
		return 12820;
		case 272:
		return 12057;
		case 277:
		return 14623;
		case 282:
		return 12792;
		case 287:
		return 12069;
		case 292:
		return 12011;
		case 297:
		return 12782;
		case 302:
		return 12081;
		case 307:
		return 12794;
		case 312:
		return 12013;
		case 317:
		return 12802;
		case 322:
		return 12804;
		case 327:
		return 12806;
		case 332:
		return 12025;
		case 337:
		return 12017;
		case 342:
		return 12788;
		case 347:
		return 12776;
		case 352:
		return 12083;
		case 357:
		return 12039;
		case 362:
		return 12786;
		case 367:
		return 12089;
		case 372:
		return 12796;
		case 377:
		return 12822;
		case 382:
		return 12093;
		case 387:
		return 12790;
		default:
			return -1;
		}
	}
	public static boolean hasAll(Player player, int itemId) {
		HashMap<Integer, Integer> map = ItemDefinitions.getItemDefinitions(itemId).getCreateItemRequirements();
		for(int reqId : map.keySet()) {
			int amount = reqId == 131 ? 1 : reqId;
			int id = map.get(reqId);
			if (!player.getInventory().containsItem(id, amount)) {
				String name = ItemDefinitions.getItemDefinitions(id)
						.getName();
				player.getPackets().sendGameMessage(
						"You need to have a"
								+ (name.startsWith("a") ? "n" : "")
								+ " " + name + " X " + amount + ".");
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasPouch(Player player) {
		for (Pouches pouch : Pouches.values())
			if(player.getInventory().containsOneItem(pouch.pouchId))
				return true;
		return false;
	}
	
	public static void infusePouches(Player player) {
		player.getInterfaceManager().sendInterface(INTERFACE);// close to this
		Object[] options = new Object[] { 78, 1, "List<col=FF9040>",
				"Infuse-X<col=FF9040>", "Infuse-All<col=FF9040>",
				"Infuse-10<col=FF9040>", "Infuse-5<col=FF9040>",
				"Infuse<col=FF9040>", 10, 8, INTERFACE << 16 | 16 };
		// to slot, from slot, options, width, height, component
		player.getPackets().sendRunScript(757, options);
		player.getPackets().sendIComponentSettings(INTERFACE, 16, 0, 430, 190);
	}

	public static boolean normalSummon(int npcId) {
		switch(npcId) {
		case 14846:
			return true;
		case 3604:
			return true;
		}
		return false;
	}

	public static void openDreadnipInterface(Player player) {
		player.getInterfaceManager().closeInventory();
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 115 : 175, 1165);
	}

	public static boolean sendCreatePouch(Player player, int itemId, int count) {
		ItemDefinitions def = ItemDefinitions.getItemDefinitions(itemId);
		Requirements req = Requirements.forId(itemId);
		
		if(req == null) {
			player.sendMessage("This pouch is temporarily unavailable.");
			return true;
		}
		
		int item = 0;
		int amount = 0;
		
		if(player.getSkills().getLevelForXp(23) < req.getLevel()) {
			player.sendMessage("You need to have a Summoning level of " + req.getLevel() +
						" to make this pouch.");
			return true;
		}
		
		for(int i = 0; i < req.getReq().length; i += 2) {
				item = req.getReq()[i];
				amount = req.getReq()[i + 1];
			
			if(!player.getInventory().containsItem(item, amount)) {
				player.sendMessage("You do not have all the items required too make this pouch.");
				return true;
			}
			
		}
		
		for(int i = 0; i < req.getReq().length; i += 2) {
			item = req.getReq()[i];
			amount = req.getReq()[i + 1];
			player.getInventory().deleteItem(item, amount);
		}
		
		player.getInventory().addItemMoneyPouch(req.getPouchId(), amount);
		player.getSkills().addXp(23, req.getExp());
		player.sendMessage("You have made a " + def.getName() + ".");
		
		return true;
	}
	
	@SuppressWarnings("unused")
	public static void spawnFamiliar(Player player, Pouches pouch) {
		if (player.getFamiliar() != null)
			return;
		if (!player.getControlerManager().canSummonFamiliar())
			return;
		ItemDefinitions def = ItemDefinitions.getItemDefinitions(pouch.getPouchId());
		if (def == null)
			return;
		HashMap<Integer, Integer> skillReq = def.getWearingSkillRequiriments();
	//	System.out.println(skillReq);
		boolean hasRequiriments = true;
		String name = Skills.SKILL_NAME[23].toLowerCase();
		int level = pouch.getLevel();
		if(player.getSkills().getLevelForXp(23) < level) {
		player.getPackets().sendGameMessage(
				"You need to have a"
						+ (name.startsWith("a") ? "n" : "") + " "
						+ name + " level of " + level
						+ " to summon this.");
			return;
		}
		if (!hasRequiriments
				|| player.getSkills().getLevel(Skills.SUMMONING) < pouch
						.getSpawnCost())
			return;
		final Familiar npc = createFamiliar(player, pouch);
		//player.Send(""+npc.getId());
		player.getInventory().deleteItem(pouch.getPouchId(), 1);
		player.getSkills().drainSummoning(pouch.getSpawnCost());
		player.setFamiliar(npc);
		if(pouch.getPouchId() == 12029) {
			bunyipTimer(player);
		}
	}
	
	private Pouches pouches;
	
	public Summoning(Pouches pouches) {
		this.setPouches(pouches);
	}
	
	public Pouches getPouches() {
		return pouches;
	}
	
	public void setPouches(Pouches pouches) {
		this.pouches = pouches;
	}
	
}
