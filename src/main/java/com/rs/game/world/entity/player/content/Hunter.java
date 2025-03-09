package com.rs.game.world.entity.player.content;

import java.util.HashMap;
import java.util.Map;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Misc;
import com.rs.utility.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Hunter {

	public static final Animation CAPTURE_ANIMATION = new Animation(6606);
	private static final int HUNTER = 21;
	private static final int BUTTERFLY_NET = 10010;

	public static void PuroPuroTeleport(final Player player, final int x, final int y, final int z) {
		if (player.isLocked()
				|| player.getControlerManager().getControler() != null) {
		player.sm("You haven't teleported to random event for being busy.");
		}
		else {	
		player.animate(new Animation(1544));
		player.setNextForceTalk(new ForceTalk("Oh my lord, what is this?"));
		player.lock(6);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.unlock();
				player.sm("Mysterious force teleports you.");
				player.setNextPosition(new Position(x, y, z));
			}
		}, 2);
	}
}
    public static final Item[] CHARMS = { new Item(12158, 1), new Item(12159, 1), new Item(12160, 1), new Item(12163, 1)};
	
	public enum FlyingEntities {
		BABY_IMPLING(1028, 11238, 20, 25, 17, new Item[] { new Item(946, 1), new Item(1755, 1), new Item(1734, 1), new Item(1733, 1), new Item(2347, 1), new Item(1985, 1) }, new Item[] { new Item(1927, 1), new Item(319, 1), new Item(2007, 1), new Item(1779, 1), new Item(7170, 1), new Item(401, 1), new Item(1438, 1) }, new Item[] { new Item(2355), new Item(1607), new Item(1743), new Item(379), new Item(1761) }, null),
		YOUNG_IMPLING(1029, 11240, 48, 65, 22, new Item[] { new Item(361, 1), new Item(1901, 1), new Item(1539, 5), new Item(1784, 4), new Item(1523, 1), new Item(7936, 1), new Item(5970, 1) }, new Item[] { new Item(855, 1), new Item(1353, 1), new Item(2293, 1), new Item(7178, 1), new Item(247, 1), new Item(453, 1), new Item(1777, 1), new Item(231, 1), new Item(2347, 1) }, new Item[] { new Item(1097, 1), new Item(1157, 1), new Item(8778, 1), new Item(133, 1), new Item(2359, 1), }, null),
		GOURMET_IMPLING(1030, 11242, 82, 113, 28, new Item[] { new Item(361, 1), new Item(365, 1), new Item(1897, 1), new Item(2007, 1), new Item(2011, 1), new Item(2293, 1), new Item(2327, 1), new Item(5970, 1) }, new Item[] { new Item(247, 1), new Item(379, 1), new Item(385, 1), new Item(1883, 1), new Item(1885, 1), new Item(5755, 1), new Item(6969, 1), new Item(7170, 1), new Item(7178, 1), new Item(7188, 1), new Item(7754, 1), new Item(8244, 1), new Item(8526, 1) }, new Item[] { new Item(373, 1), new Item(5406, 1), new Item(5755, 1), new Item(7170, 1), new Item(7178, 1), new Item(7178, 1), new Item(7188, 1), new Item(7754, 1), new Item(8244, 1), new Item(8526, 1), new Item(10136, 1), new Item(14831, 1), new Item(10137, 5) }, null),
		EARTH_IMPLING(1031, 11244, 126, 177, 36, new Item[] { new Item(444, 1), new Item(557, 32), new Item(1441, 6), new Item(1442, 1), new Item(2353, 1), new Item(5104, 1), new Item(5535, 1), new Item(6032, 1), new Item(20904, 1), new Item(20914, 1), new Item(20942, 1), new Item(24216, 1), new Item(24767, 1) }, new Item[] { new Item(237, 1), new Item(447, 1), new Item(1273, 1), new Item(454, 6), new Item(1487, 1), new Item(5311, 2), new Item(5294, 2) }, new Item[] { new Item(448, 1), new Item(1606, 2), new Item(6035, 2), }, new Item[] { new Item(1603, 1), new Item(5303, 1) }),
		ESSENCE_IMPLING(1032, 11246, 160, 225, 42, new Item[] { new Item(562, 4), new Item(554, 50), new Item(555, 30), new Item(556, 60), new Item(559, 30), new Item(1448, 1), new Item(7937, 20) }, new Item[] { new Item(564, 4), new Item(4694, 4), new Item(4696, 4), new Item(4698, 4), }, new Item[] { new Item(560, 13), new Item(561, 13), new Item(563, 13), new Item(566, 13), new Item(1442, 1) }, null),
		ECLECTIC_IMPLING(1033, 11248, 205, 289, 50, new Item[] { new Item(1273, 1), new Item(5970, 1), new Item(231, 1), new Item(556, 41), new Item(8779, 4), new Item(12111, 1) }, new Item[] { new Item(2358, 5), new Item(444, 1), new Item(4527, 1), new Item(237, 1), new Item(7937, 25) , new Item(1199, 1), new Item(2349, 1), new Item(2351, 1), new Item(2353, 1), new Item(1199, 1)}, new Item[] { new Item(2493, 1), new Item(10083, 1), new Item(1213, 1), new Item(450, 10), new Item(7208, 1), new Item(5760, 2), new Item(5321, 3), }, new Item[] { new Item(1391, 1), new Item(1601, 1) }),
		SPIRIT_IMPLING(7866, 15513, 227, 321, 54, new Item[] { new Item(12155, 25), new Item(12109, 1), new Item(12113, 1), new Item(12121, 1), new Item(12115, 1), new Item(12117, 1), new Item(12111, 1), new Item(2350, 6), new Item(2351, 5), new Item(2354, 2), new Item(2359, 1), new Item(2361, 1), new Item(2363, 1), new Item(1115, 1), new Item(1120, 2), new Item(1636, 10), new Item(2135, 25), new Item(2139, 25), new Item(9979, 15), new Item(3363, 5), new Item(10095, 1), new Item(10103, 1), new Item(10819, 7), new Item(6155, 1), new Item(7939, 1), new Item(6291, 1), new Item(6319, 1), new Item(2860, 3), new Item(237, 1), new Item(10149, 1), new Item(2151, 5), new Item(12156, 1), new Item(1934, 14), new Item(6033, 12), new Item(6010, 1), new Item(12148, 3), new Item(12134, 1), new Item(12153, 1), new Item(1520, 65), new Item(5934, 2), new Item(1964, 25), new Item(8431, 2), new Item(3138, 1), new Item(2462, 1), new Item(249, 4), new Item(951, 18), new Item(311, 14), new Item(6980, 5), new Item(1442, 1), new Item(1438, 1), new Item(572, 2), new Item(9737, 2), new Item(9976, 4), new Item(10117, 1), new Item(383, 1), new Item(1444, 1), new Item(1444, 1), }, null, null, null) {
		    @Override
		    public void effect(Player player) {
			if (Utils.getRandom(1) == 0) {
			    Item charm = CHARMS[Utils.random(CHARMS.length)];
			    int charmAmount = Utils.getRandom(charm.getAmount());
			    player.getDialogueManager().startDialogue("ItemMessage", "The impling was carrying a" + charm.getName().toLowerCase() + ".", charm.getId());
			    player.getInventory().addItem(new Item(charm.getId(), charmAmount));
			}
		    }
		},
		NATURE_IMPLING(1034, 11250, 250, 353, 58, new Item[] { new Item(5100, 1), new Item(5104, 1), new Item(5281, 1), new Item(5294, 1), new Item(6016, 1), new Item(1513, 1), new Item(253, 4), }, new Item[] { new Item(5298, 5), new Item(5299, 1), new Item(5297, 1), new Item(3051, 1), new Item(5285, 1), new Item(5286, 1), new Item(5313, 1), new Item(5974, 1), }, new Item[] { new Item(5304, 1), new Item(5295, 1) }, new Item[] { new Item(5303, 1), new Item(219, 1), new Item(269, 1) }),
		MAGPIE_IMPLING(1035, 11252, 289, 409, 65, new Item[] { new Item(1682, 3), new Item(1732, 3), new Item(2569, 3), new Item(3391, 1), new Item(5541, 1), new Item(1748, 6), }, new Item[] { new Item(1333, 1), new Item(1347, 1), new Item(2571, 5), new Item(4097, 1), new Item(4095, 1), new Item(2364, 2), new Item(1603, 1), }, new Item[] { new Item(1215, 1), new Item(1185, 1), new Item(1601, 1), new Item(5287, 1), new Item(987, 1), new Item(985, 1), }, new Item[] { new Item(5300, 1), new Item(12121, 1), new Item(993, 1) }),
		NINJA_IMPLING(6053, 11254, 339, 481, 74, new Item[] { new Item(6328, 1), new Item(3385, 1), new Item(3391, 1), new Item(4097, 1), new Item(4095, 1), new Item(3101, 1), new Item(1333, 1), new Item(1347, 1), new Item(1215, 1), new Item(6313, 1), new Item(892, 70), new Item(811, 70), new Item(868, 40), new Item(1747, 16), new Item(140, 4), new Item(805, 40), new Item(25496, 4), }, new Item[] { new Item(9342, 1), new Item(6155, 1) }, new Item[] { new Item(2363, 1), new Item(9194, 1) }, null),
		PIRATE_IMPLING(7845, 13337, 350, 497, 76, null, null, null, null),
		DRAGON_IMPLING(6054, 11256, 390, 553, 83, null, null, null, null),
		ZOMBIE_IMPLING(7902, 15515, 412, 585, 87, null, null, null, null),																																																													 																																																														 
		KINGLY_IMPLING(7903, 15517, 434, 617, 91, new Item[] { new Item(1705, Utils.random(3, 11)), new Item(1684, 3), new Item(1618, Utils.random(17, 34)), new Item(990, 2) }, new Item[] { new Item(1631, 1), new Item(1615, 1), new Item(9341, 40 + Utils.getRandom(30)), new Item(9342, 57), new Item(15511, 1), new Item(15509, 1), new Item(15505, 1), new Item(15507, 1), new Item(15503, 1), new Item(11212, 40 + Utils.random(104)), new Item(9193, 62 + Utils.random(8)), new Item(11230, Utils.random(182, 319)), new Item(11232, 70), new Item(1306, Utils.random(1, 2)), new Item(1249, 1) }, null, new Item[] { new Item(7158, 1), new Item(2366, 1), new Item(6571, 1) }),
		BUTTERFLYTEST(1, 1, 434, 617, 91, null, null, null, null) {
			@Override
			public void effect(Player player) {
				// stat boost
			}
		};
		static final Map<Short, FlyingEntities> flyingEntities = new HashMap<Short, FlyingEntities>();
		static {
			for (FlyingEntities impling : FlyingEntities.values())
				flyingEntities.put((short) impling.reward, impling);
		}
		public static FlyingEntities forItem(short reward) {
			return flyingEntities.get(reward);
		}

		private int npcId, level, reward;
		private double puroExperience, rsExperience;
		private Item[] rarleyCommon, common, rare, extremelyRare;
		private Graphics graphics;

		private FlyingEntities(int npcId, int reward, double puroExperience,
				double rsExperience, int level, Graphics graphics,
				Item[] rarleyCommon, Item[] common, Item[] rare,
				Item[] extremelyRare) {
			this.npcId = npcId;
			this.reward = reward;
			this.puroExperience = puroExperience;
			this.rsExperience = rsExperience;
			this.level = level;
			this.rarleyCommon = rarleyCommon;
			this.common = common;
			this.rare = rare;
			this.extremelyRare = extremelyRare;
			this.graphics = graphics;
		}

		private FlyingEntities(int npcId, int reward, double puroExperience, double rsExperience, int level, Item[] rarleyCommon, Item[] common, Item[] rare, Item[] extremelyRare) {
		    this.npcId = npcId;
		    this.reward = reward;
		    this.puroExperience = puroExperience;
		    this.rsExperience = rsExperience;
		    this.level = level;
		    this.rarleyCommon = rarleyCommon;
		    this.common = common;
		    this.rare = rare;
		    this.extremelyRare = extremelyRare;
		}

		public int getNpcId() {
		    return npcId;
		}

		public int getLevel() {
		    return level;
		}

		public int getReward() {
		    return reward;
		}

		public double getPuroExperience() {
		    return puroExperience;
		}

		public double getRsExperience() {
		    return rsExperience;
		}

		public Item[] getRarleyCommon() {
		    return rarleyCommon;
		}

		public Item[] getCommon() {
		    return common;
		}

		public Item[] getRare() {
		    return rare;
		}

		public Item[] getExtremelyRare() {
		    return extremelyRare;
		}

		public Graphics getGraphics() {
		    return graphics;
		}

		public void effect(Player player) {

		}

		public static FlyingEntities forId(int itemId) {
		    for (FlyingEntities entity : FlyingEntities.values()) {
			if (itemId == entity.getReward())
			    return entity;
		    }
		    return null;
		}

		public static FlyingEntities forNPCId(int npcId) {
		    for (FlyingEntities entity : FlyingEntities.values()) {
			if (npcId == entity.getNpcId())
			    return entity;
		    }
		    return null;
		}
	}

	@RequiredArgsConstructor @Getter
	public enum XericBat {

		GUANIC(7587, 1, 20870, 5),
		PRAEL(7588, 15, 20872, 9),
		GIRAL(7589, 30, 20874, 12),
		PHLUXIA(7590, 45, 20876, 15),
		KRYKET(7591, 60, 20878, 18),
		MURNG(7592, 75, 20880, 24),
		PSYKK(7593, 90, 20882, 30);

		private final int npcId, levelReq, itemId;
		private final double xp;

		public static final XericBat[] VALUES = values();

		public boolean net(Player player, NPC npc) {
			if (player.getSkills().getLevel(Skills.HUNTER) < levelReq) {
				player.sendMessage("You need a Hunter level of " + levelReq + " or higher to catch this bat.");
				return false;
			}
			if (!player.getInventory().hasItem(BUTTERFLY_NET)) {
				player.sendMessage("You'll need a butterfly net to catch bats.");
				return false;
			}
			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("You don't have enough space in your inventory.");
				return false;
			}
			player.startEvent(event -> {
				player.animate(6606);
				event.delay(2);
				if (((200 - levelReq) + player.getSkills().getLevel(Skills.HUNTER)) / 255D >= Misc.get()) {
					player.getSkills().addXp(Skills.HUNTER, xp);
					player.getInventory().addItem(Item.asOSRS(itemId), 1);
					player.getPackets().sendGameMessage("You catch the bat.", true);
					npc.setRespawnTask();
				} else {
					player.sendMessage("You fail to catch the bat.");
				}
			});
			return false;
		}

		public static XericBat forNpcId(int npcId) {
			for (XericBat bat : VALUES) {
				if (npcId == bat.npcId)
					return bat;
			}
			return null;
		}

	}

	public interface DynamicFormula {

		public int getExtraProbablity(Player player);

	}

   public static void captureFlyingEntity(final Player player, final NPC npc) {
		final String name = npc.getDefinitions().getName().toUpperCase();
		final FlyingEntities instance = FlyingEntities.forNPCId(npc.getId());
		if (instance == null)
			return;
		final boolean isImpling = name.toLowerCase().contains("impling");
		if (!player.getInventory().containsItem(isImpling ? 11260 : 10012, 1)) {
			player.getPackets().sendGameMessage("You don't have an empty " + (isImpling ? "impling jar" : "butterfly jar") + " in which to keep " + (isImpling ? "an impling" : "a butterfly") + ".");
			return;
		}
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId != 11259 && weaponId != 10010 && isImpling) {
			player.getPackets().sendGameMessage("You need to have a butterfly net equipped in order to capture an impling.");
			return;
		}
		if (player.getSkills().getLevel(Skills.HUNTER) < instance.getLevel()) {
			player.getPackets().sendGameMessage("You need a hunter level of " + instance.getLevel() + " to capture a " + name.toLowerCase() + ".");
			return;
		}
		player.lock(2);
		player.getPackets().sendGameMessage("You swing your net...");
		player.animate(CAPTURE_ANIMATION);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (isSuccessful(player, instance.getLevel(), player1 -> {
					if (player1.getEquipment().getItem(3).getId() == 11259)
						return 3;// magic net
					else if (player1.getEquipment().getItem(3).getId() == 10010)
						return 2;// regular net
					return 1;// hands
				})) {
					if (player.getInventory().addItem(new Item(instance.getReward(), 1))) {
						player.getInventory().deleteItem(new Item(11260, 1));
						player.getSkills().addXp(Skills.HUNTER, instance.getRsExperience());
						npc.setRespawnTask(); //sets loc andfinishes auto
						player.getPackets().sendGameMessage("You manage to catch the " + name.toLowerCase() + " and squeeze it into a jar.");
						return;
					}
				} else {
					if (isImpling) {
						npc.setNextForceTalk(new ForceTalk("Tehee, you missed me!"));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								Position teleTile = npc;
								for (int trycount = 0; trycount < 10; trycount++) {
									teleTile = new Position(npc, 3);
									if (World.isTileFree(npc.getZ(),
											teleTile.getX(), teleTile.getY(),
											player.getSize()))
										break;
									teleTile = npc;
								}
								npc.setNextPosition(teleTile);
							}
						});
					}
					player.getPackets()
							.sendGameMessage(
									"...you stumble and miss the "
											+ name.toLowerCase());
				}
			}
		});
	}

	public static void openJar(Player player) {
		Long lastImp = (Long) player.getTemporaryAttributtes().get("LAST_IMP");
		if (lastImp != null && lastImp + 4000 > Utils.currentTimeMillis()) {
		return;
		}
		player.animate(CAPTURE_ANIMATION);
		player.getInventory().addItem(995, 20000);
		player.sm("You capture the impling.");
		player.getSkills().addXp(HUNTER, 75);
		player.getTemporaryAttributtes().put("LAST_IMP", Utils.currentTimeMillis());
		player.lock(3);
	}
	
	public static void openJar(Player player, FlyingEntities instance, int slot) {
		boolean isImpling = instance.toString().toLowerCase().contains("impling");
		if (isImpling) {
		    int chance = Utils.getRandom(100);
		    Item item = null;
		    Item[] list = null;
		    if (chance <= 60) {
			list = instance.getRarleyCommon();
			if (list != null)
			    item = list[Utils.random(instance.getRarleyCommon().length)];
		    } else if (chance < 80) {
			list = instance.getCommon();
			if (list != null)
			    item = list[Utils.random(instance.getCommon().length)];
		    } else if (chance < 100) {
			list = instance.getRare();
			if (list != null)
			    item = list[Utils.random(instance.getRare().length)];
		    } else {
			list = instance.getRarleyCommon();
			if (item == null)
			    item = list[Utils.random(instance.getRarleyCommon().length)];
		    }
		    player.getInventory().getItem(slot).setId(isImpling ? 11260 : 11);
		    player.getInventory().refresh(slot);
		    if(item != null) 
			    player.getInventory().addItem(item.getId(), Utils.random(item.getAmount()) + 1);
		    
		}
		if (instance != null)
		    instance.effect(player);
		if (Utils.getRandom(4) == 0) {
		    player.getInventory().deleteItem(new Item(isImpling ? 11260 : 11));
		    player.getPackets().sendGameMessage("You press too hard on the jar and the glass shatters in your hands.");
		    player.applyHit(new Hit(player, 10, HitLook.REGULAR_DAMAGE));
		}
	    }

	static int[] requiredLogs = new int[] { 1151, 1153, 1155, 1157, 1519, 1521,
			13567, 1521, 2862, 10810, 6332, 12581 };

	public static void createLoggedObject(Player player, WorldObject object,
			boolean kebbits) {
		if (!player.getInventory().containsOneItem(requiredLogs)) {
			player.getPackets().sendGameMessage(
					"You need to have logs to create this trap.");
			return;
		}
		player.lock(3);
		player.getActionManager().setActionDelay(3);
		player.animate(new Animation(5208));// all animation for
														// everything :L
		if (World.removeTemporaryObject(object, 300000, false)) {// five minute
																	// delay
			World.spawnTemporaryObject(new WorldObject(kebbits ? 19206 : -1,
					object.getType(), object.getRotation(), object), 300000,
					false);// TODO
			Item item = null;
			for (int requiredLog : requiredLogs) {
				if ((item = player.getInventory().getItems()
						.lookup(requiredLog)) != null) {
					player.getInventory().deleteItem(item);
				}
			}
		}
	}

	public static boolean isSuccessful(Player player, int dataLevel,
			DynamicFormula formula) {
		int hunterlevel = player.getSkills().getLevel(Skills.HUNTER);
		int increasedProbability = formula == null ? 1 : formula.getExtraProbablity(player);
		int level = Utils.random(hunterlevel + increasedProbability) + 1;
		double ratio = level / (Utils.random(dataLevel + 4) + 1);
		if (Math.round(ratio * dataLevel * 1.20) < dataLevel) {
			return false;
		}
		return true;
	}
}