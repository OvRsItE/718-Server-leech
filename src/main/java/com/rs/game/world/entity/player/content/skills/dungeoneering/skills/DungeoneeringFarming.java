package com.rs.game.world.entity.player.content.skills.dungeoneering.skills;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RingOfKinship;
import com.rs.game.world.entity.updating.impl.Animation;


public class DungeoneeringFarming {

	public static enum Harvest {
		SALVE_NETTLES(1, 6.1, 17448),

		WILDERCRESS(10, 9.2, 17450),

		BLIGHTLEAF(20, 12.8, 17452),

		ROSEBLOOD(30, 17.4, 17454),

		BRYLL(40, 23.5, 17456),

		DUSKWEED(50, 31.6, 17458),

		SOULBELL(60, 42.2, 17460),

		ECTOGRASS(70, 55.8, 17462),

		RUNELEAF(80, 72.9, 17464),

		SPIRITBLOOM(90, 94, 17466);

		private final int lvl, product;
		private final double exp;

		private Harvest(int lvl, double exp, int product) {
			this.lvl = lvl;
			this.exp = exp;
			this.product = product;
		}

		public int getLvl() {
			return lvl;
		}

		public double getExp() {
			return exp;
		}

		public int getProduct() {
			return product;
		}
	}

	public static int getHerbForLevel(int level) {
		for (int i = 10; i < Harvest.values().length; i++)
			if (Harvest.values()[i].lvl == level)
				return Harvest.values()[i].product;
		return 17448;
	}

	public static void initHarvest(final Player player, final Harvest harvest, final WorldObject object) {
		Integer harvestCount = (Integer) player.getTemporaryAttributtes().get(TemporaryAttributes.Key.HARVEST_COUNT);
		final String productName = ItemDefinitions.getItemDefinitions(harvest.product).getName().toLowerCase();

		if (player.getSkills().getLevel(Skills.FARMING) < harvest.lvl) {
			player.getPackets().sendGameMessage("You need a Farming level of " + harvest.lvl + " in order to pick " + productName + ".");
			return;
		}

		if (harvestCount == null)
			harvestCount = com.rs.utility.Utils.random(2, 5);

		if (harvestCount == 0) {
			player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.HARVEST_COUNT);
			player.getPackets().sendGameMessage("You have depleted this resource.");
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					World.spawnObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object));
				}
			});
			return;
		}
		player.getTemporaryAttributtes().put(TemporaryAttributes.Key.HARVEST_COUNT, harvestCount);
		player.animate(new Animation(3659));
		player.lock(2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (player.getInventory().addItemDrop(harvest.product, 1)) {
					player.getPackets().sendGameMessage("You pick a " + productName + ".");
					player.getSkills().addXp(Skills.FARMING, harvest.exp);
				}
				if (com.rs.utility.Utils.random(100) < player.getRingOfKinship().getBoost(RingOfKinship.GATHERER))
					if (player.getInventory().addItemDrop(harvest.product, 1))
						player.getPackets().sendGameMessage("You manage to grab another " + productName + ".");
			}
		}, 2);
	}
}
