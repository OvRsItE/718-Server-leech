package com.rs.game.world.entity.player.actions.CitadelActions;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.mining.MiningBase;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public final class CitadelMining extends MiningBase {

	public static enum RockDefinitions {

		Diamond_Ore(95, 120.3, -1, 100, 10, 9612, 5, 0, 1), Golden_Ore(80,
				54.1, -1, 100, 10, 9611, 5, 0, 1), Mini_Golden_Ore(76, 34.1,
				-1, 100, 10, 9607, 5, 0, 1), Mini_Golden_Ore2(76, 34.1, -1,
				100, 10, 9608, 5, 0, 1), Mini_Diamond_Ore(85, 59.8, -1, 100,
				10, 9609, 5, 0, 1), Mini_Diamond_Ore2(85, 59.8, -1, 100, 10,
				9610, 5, 0, 1), Stone_Ore(34, 20, -1, 100, 10, 9603, 5, 0, 1), Stone_Ore1(
				34, 20, -1, 100, 10, 9604, 5, 0, 1), Stone_Ore2(34, 20, -1,
				100, 10, 9606, 5, 0, 1), Stone_Ore3(34, 20, -1, 100, 10, 9605,
				5, 0, 1), IRON_Stone_Ore(34, 20, -1, 100, 10, 9604, 5, 0, 1), IRON_Stone_Ore1(
				34, 20, -1, 100, 10, 9603, 5, 0, 1), IRON_Stone_Ore2(34, 20,
				-1, 100, 10, 9606, 5, 0, 1), IRON_Stone_Ore3(34, 20, -1, 100,
				10, 9605, 5, 0, 1);

		private int level;
		private double xp;
		private int oreId;
		private int oreBaseTime;
		private int oreRandomTime;
		private int configId;
		private int respawnDelay;
		private int randomLifeProbability;
		private int configTypeGone;

		private RockDefinitions(int level, double xp, int oreId,
				int oreBaseTime, int oreRandomTime, int configId,
				int respawnDelay, int randomLifeProbability, int configTypeGone) {
			this.level = level;
			this.xp = xp;
			this.oreId = oreId;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
			this.configId = configId;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
			this.configTypeGone = configTypeGone;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getOreId() {
			return oreId;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

		public int getEmptyId() {
			return configId;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

	private WorldObject rock;
	private RockDefinitions definitions;

	public CitadelMining(WorldObject rock, RockDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage(
				"You swing your pickaxe at the rock.", true);
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int summoningBonus = 0;
		if (player.getFamiliar() != null) {
			if (player.getFamiliar().getId() == 7342
					|| player.getFamiliar().getId() == 7342)
				summoningBonus += 10;
			else if (player.getFamiliar().getId() == 6832
					|| player.getFamiliar().getId() == 6831)
				summoningBonus += 1;
		}
		int mineTimer = definitions.getOreBaseTime()
				- (player.getSkills().getLevel(Skills.MINING) + summoningBonus)
				- Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + definitions.getOreRandomTime())
			mineTimer = 1 + Utils.getRandom(definitions.getOreRandomTime());
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}

	private boolean checkAll(Player player) {
		if (!hasPickaxe(player)) {
			player.getPackets().sendGameMessage(
					"You need a pickaxe to mine this rock.");
			return false;
		}
		if (!setPickaxe(player)) {
			player.getPackets().sendGameMessage(
					"You dont have the required level to use this pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player)) {
			player.sm("You need a higher mining level to do this!");
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of " + definitions.getLevel()
							+ " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.animate(new Animation(emoteId));
		return checkRock(player);
	}

	private boolean usedDeplateAurora;

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		if (definitions.getEmptyId() != -1) {
			if (!usedDeplateAurora
					&& (1 + Math.random()) < player.getAuraManager()
							.getChanceNotDepleteMN_WC()) {
				usedDeplateAurora = true;
			} else if (Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
				World.createTemporaryConfig(definitions.configId,
						definitions.configTypeGone,
						definitions.respawnDelay * 600, player);
				player.animate(new Animation(-1));
				return -1;
			}
		}
		if (!player.getInventory().hasFreeSlots()
				&& definitions.getOreId() != -1) {
			player.animate(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay(player);
	}

	private void addOre(Player player) {
		double xpBoost = 0;
		int idSome = 0;
		double totalXp = definitions.getXp() + xpBoost;
		if (hasMiningSuit(player))
			totalXp *= 1.025;
		player.getSkills().addXp(Skills.MINING, totalXp);
		if (definitions.getOreId() != -1) {
			player.getInventory().addItemMoneyPouch(definitions.getOreId() + idSome, 1);
			String oreName = ItemDefinitions
					.getItemDefinitions(definitions.getOreId() + idSome)
					.getName().toLowerCase();
			player.getPackets().sendGameMessage(
					"You mine some " + oreName + ".", true);
		}
	}

	private boolean hasMiningSuit(Player player) {
		if (player.getEquipment().getHatId() == 20789
				&& player.getEquipment().getChestId() == 20791
				&& player.getEquipment().getLegsId() == 20790
				&& player.getEquipment().getBootsId() == 20788)
			return true;
		return false;
	}

	private boolean checkRock(Player player) {
		return World.getRegion(rock.getRegionId()).containsObject(rock.getId(),
				rock);
	}

}
