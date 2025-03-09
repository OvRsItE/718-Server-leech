package com.rs.game.world.entity.player.actions.skilling;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;

public class HerbCleaning {

	public static enum Herbs {

		GUAM(199, 2.5, 3, 249),

		MARRENTILL(201, 3.8, 5, 251),

		TARROMIN(203, 5, 11, 253),

		HARRALANDER(205, 6.3, 20, 255),

		RANARR(207, 7.5, 25, 257),

		TOADFLAX(3049, 8, 30, 2998),

		SPIRIT_WEED(12174, 7.8, 35, 12172),

		IRIT(209, 8.8, 40, 259),

		WERGALI(14836, 9.5, 41, 14854),

		AVANTOE(211, 10, 48, 261),

		KWUARM(213, 11.3, 54, 263),

		SNAPDRAGON(3051, 11.8, 59, 3000),

		CADANTINE(215, 12.5, 65, 265),

		LANTADYME(2485, 13.1, 67, 2481),

		DWARF_WEED(217, 13.8, 70, 267),

		TORSTOL(219, 15, 75, 269),

		FELLSTALK(21626, 20, 85, 21624);

		private int herbId;
		private int level;
		private int cleanId;
		private double xp;

		Herbs(int herbId, double xp, int level, int cleanId) {
			this.herbId = herbId;
			this.xp = xp;
			this.level = level;
			this.cleanId = cleanId;
		}

		public int getHerbId() {
			return herbId;
		}

		public double getExperience() {
			return xp;
		}

		public int getLevel() {
			return level;
		}

		public int getCleanId() {
			return cleanId;
		}
	}

	public static Herbs getHerb(int id) {
		for (final Herbs herb : Herbs.values()) {
			if (herb.getHerbId() == id) {
				return herb;
			}
		}
		return null;
	}

	public static boolean clean(final Player player, Item item, final int slotId) {
		final Herbs herb = getHerb(item.getId());
		if (herb == null) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
			player.getPackets().sendGameMessage(
					"You do not have the required level to clean this.", true);
			return true;
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				Item i = player.getInventory().getItem(slotId);
				if (i == null) {
					return;
				}
				if (i.getId() != herb.getHerbId()) {
					return;
				}
				if (herb == Herbs.GUAM) {
					player.guams++;
				} else if (herb == Herbs.TARROMIN) {
					player.tarromins++;
				} else if (herb == Herbs.MARRENTILL) {
					player.marrentills++;
				} else if (herb == Herbs.HARRALANDER) {
					player.harrlanders++;
				} else if (herb == Herbs.RANARR) {
					player.ranarrs++;
				} else if (herb == Herbs.TOADFLAX) {
					player.toadflaxs++;
				} else if (herb == Herbs.IRIT) {
					player.irits++;
				} else if (herb == Herbs.AVANTOE) {
					player.avantoes++;
				} else if (herb == Herbs.KWUARM) {
					player.kwuarms++;
				} else if (herb == Herbs.SNAPDRAGON) {
					player.snapdragons++;
				} else if (herb == Herbs.CADANTINE) {
					player.cadantines++;
				} else if (herb == Herbs.LANTADYME) {
					player.lantadymes++;
				} else if (herb == Herbs.DWARF_WEED) {
					player.dwarfweeds++;
				} else if (herb == Herbs.TORSTOL) {
					player.torstols++;
				} else if (herb == Herbs.FELLSTALK) {
					player.fellstalks++;
				}
				i.setId(herb.getCleanId());
				player.getInventory().refresh(slotId);
				if (player.getEquipment().wearingSkillCape(Skills.HERBLORE)) {
					int amount = player.getInventory().getAmountOf(i.getId());
					if (amount > 1) {
						for (int index = 0; index < 28; index++) {
							if (player.getInventory().getItem(index).getId() == i.getId()) {
								player.getInventory().getItem(index).setId(herb.getCleanId());
								player.getSkills().addXp(Skills.HERBLORE, herb.getExperience());
								player.getInventory().refresh();
							}
						}
					}
				}
				player.randomevent(player);
				player.getSkills().addXp(Skills.HERBLORE, herb.getExperience());
				player.getPackets().sendGameMessage("You clean the herb.", true);
			}

		});
		return true;
	}

}
