package com.rs.game.world.entity.player.actions.skilling;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.BirdNests;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

public final class Woodcutting extends Action {

	private static final int[] BIRD_NESTS =
		{ 5070, 5071, 5072, 5073, 5074, 5075, 7413, 11966 };

	public enum HatchetDefinitions {

		NOVITE(16361, 1, 1, 13118),

		BATHUS(16363, 10, 4, 13119),

		MARMAROS(16365, 20, 5, 13120),

		KRATONITE(16367, 30, 7, 13121),

		FRACTITE(16369, 40, 10, 13122),

		ZEPHYRIUM(16371, 50, 12, 13123),

		ARGONITE(16373, 60, 13, 13124),

		KATAGON(16373, 70, 15, 13125),

		GORGONITE(16375, 80, 17, 13126),

		PROMETHIUM(16379, 90, 19, 13127),

		PRIMAL(16381, 99, 21, 13128),

		BRONZE(1351, 1, 1, 879),

		IRON(1349, 5, 2, 877),

		STEEL(1353, 5, 3, 875),

		BLACK(1361, 11, 4, 873),

		MITHRIL(1355, 21, 5, 871),

		ADAMANT(1357, 31, 7, 869),

		RUNE(1359, 41, 10, 867),

		DRAGON(6739, 61, 13, 2846),

		INFERNO(13661, 61, 13, 10251);

		;

		private int itemId, levelRequried, axeTime, emoteId;

		private HatchetDefinitions(int itemId, int levelRequried, int axeTime, int emoteId) {
			this.itemId = itemId;
			this.levelRequried = levelRequried;
			this.axeTime = axeTime;
			this.emoteId = emoteId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getLevelRequried() {
			return levelRequried;
		}

		public int getAxeTime() {
			return axeTime;
		}

		public int getEmoteId() {
			return emoteId;
		}

	}

	public static enum TreeDefinitions {

		NORMAL(1, 25, 1511, 20, 4, 1341, 8, 0), // TODO

		DRAMEN(36, 0, 771, 20, 4, -1, 8, 0),

		EVERGREEN(1, 25, 1511, 20, 4, 57931, 8, 0),

		DEAD(1, 25, 1511, 20, 4, 12733, 8, 0),

		OAK(15, 37.5, 1521, 30, 4, 1341, 15, 15), // TODO

		WILLOW(30, 67.5, 1519, 60, 4, 5554, 51, 15), // TODO

		TEAK(35, 85, 6333, 30, 4, 9037, 60, 10),

		MAPLE(45, 100, 1517, 83, 16, 31057, 72, 10),

		MAHOGANY(50, 125, 6332, 95, 16, 9035, 83, 10),

		YEW(60, 175, 1515, 120, 17, 7402, 94, 10), // TODO

		IVY(68, 332.5, -1, 120, 17, 46319, 58, 10),

		MAGIC(75, 250, 1513, 150, 21, 37824, 121, 10),

		CURSED_MAGIC(82, 250, 1513, 150, 21, 37822, 121, 10),

		FRUIT_TREES(1, 25, -1, 20, 4, 1341, 8, 0),

		MUTATED_VINE(83, 140, 21358, 83, 16, 5, -1, 0),

		CURLY_VINE(83, 140, null, 83, 16, 12279, 72, 0),

		CURLY_VINE_COLLECTABLE(83, 140, new int[]
				{ 21350, 21350, 21350, 21350 }, 83, 16, 12283, 72, 0),

				STRAIT_VINE(83, 140, null, 83, 16, 12277, 72, 0),

				STRAIT_VINE_COLLECTABLE(83, 140, new int[]
						{ 21349, 21349, 21349, 21349 }, 83, 16, 12283, 72, 0),

						TANGLE_GUM_VINE(1, 35, 17682, 20, 4, 49706, 8, 5),

						SEEPING_ELM_TREE(10, 60, 17684, 25, 4, 49708, 12, 5),

						BLOOD_SPINDLE_TREE(20, 85, 17686, 35, 4, 49710, 16, 5),

						UTUKU_TREE(30, 115, 17688, 60, 4, 49712, 51, 5),

						SPINEBEAM_TREE(40, 145, 17690, 76, 16, 49714, 68, 5),

						BOVISTRANGLER_TREE(50, 175, 17692, 85, 16, 49716, 75, 5),

						THIGAT_TREE(60, 210, 17694, 95, 16, 49718, 83, 10),

						CORPESTHORN_TREE(70, 245, 17696, 111, 16, 49720, 90, 10),

						ENTGALLOW_TREE(80, 285, 17698, 120, 17, 49722, 94, 10),

						GRAVE_CREEPER_TREE(90, 330, 17700, 150, 21, 49724, 121, 10),

						;

		private int level;
		private double xp;
		private int[] logsId;
		private int logBaseTime;
		private int logRandomTime;
		private int stumpId;
		private int respawnDelay;
		private int randomLifeProbability;

		private TreeDefinitions(int level, double xp, int[] logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.logsId = logsId;
			this.logBaseTime = logBaseTime;
			this.logRandomTime = logRandomTime;
			this.stumpId = stumpId;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		private TreeDefinitions(int level, double xp, int logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
			this(level, xp, new int[] { logsId }, logBaseTime, logRandomTime, stumpId, respawnDelay, randomLifeProbability);
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int[] getLogsId() {
			return logsId;
		}

		public int getLogBaseTime() {
			return logBaseTime;
		}

		public int getLogRandomTime() {
			return logRandomTime;
		}

		public int getStumpId() {
			return stumpId;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

	private WorldObject tree;
	private TreeDefinitions definitions;
	private HatchetDefinitions hatchet;
	private boolean usingBeaver;

	public Woodcutting(WorldObject tree, TreeDefinitions definitions, boolean usingBeaver) {
		this.tree = tree;
		this.definitions = definitions;
	}

	public Woodcutting(WorldObject tree, TreeDefinitions definitions) {
		this(tree, definitions, false);
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		player.getPackets()
		.sendGameMessage(
				usingBeaver ? "Your beaver uses its strong teeth to chop down the tree..."
						: "You swing your hatchet at the "
						+ (TreeDefinitions.IVY == definitions ? "ivy"
								: "tree") + "...", true);
		setActionDelay(player, getWoodcuttingDelay(player));
		return true;
	}
	
	private int getWoodcuttingDelay(Player player) {
		int summoningBonus = player.getFamiliar() != null ? player.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807 ? 10 : 0 : 0;
		int wcTimer = definitions.getLogBaseTime() - (player.getSkills().getLevel(8) + summoningBonus) - Utils.random(hatchet.axeTime);
		if (wcTimer < 1 + definitions.getLogRandomTime()) {
			wcTimer = 1 + Utils.random(definitions.getLogRandomTime());
		}
		wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();
		return wcTimer;
	}

	public static HatchetDefinitions getHatchet(Player player) {
		for (int i = HatchetDefinitions.values().length - 1; i >= 11; i--) { //from best to worst
			HatchetDefinitions def = HatchetDefinitions.values()[i];
			if (player.getInventory().containsItemToolBelt(def.itemId) || player.getEquipment().getWeaponId() == def.itemId) {
				if (player.getSkills().getLevel(Skills.WOODCUTTING) >= def.levelRequried) {
					return def;
				}
			}
		}
		return null;
	}

	private boolean checkAll(Player player) {
		hatchet = getHatchet(player);
		if (hatchet == null) {
			player.getPackets().sendGameMessage("You dont have the required level to use that axe or you don't have a hatchet.");
			return false;
		}
		if (!hasWoodcuttingLevel(player)) {
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasWoodcuttingLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.getPackets().sendGameMessage(
					"You need a woodcutting level of " + definitions.getLevel()
					+ " to chop down this tree.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (usingBeaver) {
			player.getFamiliar().animate(new Animation(7722));
			player.getFamiliar().setNextGraphics(new Graphics(1458));
		} else {
			player.animate(new Animation(hatchet.emoteId));
		}
		return checkTree(player);
	}

	private boolean usedDeplateAurora;

	@Override
	public int processWithDelay(Player player) {
		addLog(player);
		if (!usedDeplateAurora
				&& 1 + Math.random() < player.getAuraManager()
				.getChanceNotDepleteMN_WC()) {
			usedDeplateAurora = true;
		} else if (Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
			long time = definitions.respawnDelay * 600;
			World.temporarilyReplaceObject(tree, //Fixes issue with spawned trees not respawning properly.
					new WorldObject(definitions.getStumpId(), tree.getType(),
							tree.getRotation(), tree.getX(), tree.getY(), tree
							.getZ()), time, true);
			if (tree.getZ() < 3 && definitions != TreeDefinitions.IVY) {
				WorldObject object = World.getObject(new Position(
						tree.getX() - 1, tree.getY() - 1, tree.getZ() + 1));

				if (object == null) {
					object = World.getObject(new Position(tree.getX(), tree
							.getY() - 1, tree.getZ() + 1));
					if (object == null) {
						object = World.getObject(new Position(tree.getX() - 1,
								tree.getY(), tree.getZ() + 1));
						if (object == null) {
							object = World.getObject(new Position(tree.getX(),
									tree.getY(), tree.getZ() + 1));
						}
					}
				}

				if (object != null) {
					World.removeTemporaryObject(object, time, false);
				}
			}
			player.animate(new Animation(-1));
			return -1;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.animate(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getWoodcuttingDelay(player);
	}

	private void addLog(Player player) {
		double xpBoost = 1.00;
		BirdNests.dropNest(player);
		if (player.getEquipment().getChestId() == 10939) {
			xpBoost += 0.008;
		}
		if (player.getEquipment().getLegsId() == 10940) {
			xpBoost += 0.006;
		}
		if (player.getEquipment().getHatId() == 10941) {
			xpBoost += 0.004;
		}
		if (player.getEquipment().getBootsId() == 10933) {
			xpBoost += 0.002;
		}
		if (player.getEquipment().getChestId() == 10939
				&& player.getEquipment().getLegsId() == 10940
				&& player.getEquipment().getHatId() == 10941
				&& player.getEquipment().getBootsId() == 10933) {
			xpBoost += 0.005;
		}
		player.getSkills().addXp(8, definitions.getXp() * xpBoost);
		for (int item : definitions.getLogsId()) {
			player.getInventory().addItemDrop(item, 1);
		}
		if (definitions == TreeDefinitions.IVY) {
			player.getPackets().sendGameMessage("You succesfully cut an ivy vine.", true);
			player.choppedIvy++;
			player.IvyAchievement++;
		} else if (definitions == TreeDefinitions.NORMAL) {
			player.logscut++;
		} else if (definitions == TreeDefinitions.OAK) {
			player.oakscut++;
		} else if (definitions == TreeDefinitions.WILLOW) {
			player.willowscut++;
		} else if (definitions == TreeDefinitions.MAPLE) {
			player.maplescut++;
		} else if (definitions == TreeDefinitions.YEW) {
			player.yewscut++;
		} else if (definitions == TreeDefinitions.MAGIC) {
			player.magicscut++;
		}
		String logName = ItemDefinitions.getItemDefinitions(definitions.getLogsId()[0]).getName().toLowerCase();
		player.getPackets().sendGameMessage("You get some " + logName + ".", true);
		int inferno_chance = Utils.random(3);
		if (inferno_chance == 1 && player.getEquipment().getWeaponId() == 13661 && !(definitions == TreeDefinitions.IVY)) {
			player.getSkills().addXp(11, definitions.getXp() * 1);
			player.sendMessage("The adze's heat instantly incinerates the " + logName + ".");
			player.getInventory().deleteItem(definitions.getLogsId()[0], 1);
			player.setNextGraphics(new Graphics(1776));
		}
		// todo infernal adze	
	}

	private boolean checkTree(Player player) {
		return World.containsObjectWithId(tree, tree.getId());
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
