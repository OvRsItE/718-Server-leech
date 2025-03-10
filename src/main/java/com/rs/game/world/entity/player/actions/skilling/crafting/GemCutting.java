package com.rs.game.world.entity.player.actions.skilling.crafting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

public class GemCutting extends Action {

	/**
	 * Enum for gems
	 *
	 * @author Raghav
	 *
	 */
	public enum Gem {
		OPAL(1625, 1609, 15.0, 1, 886),

		JADE(1627, 1611, 20, 13, 886),

		RED_TOPAZ(1629, 1613, 25, 16, 887),

		SAPPHIRE(1623, 1607, 50, 20, 888),

		EMERALD(1621, 1605, 67, 27, 889),

		RUBY(1619, 1603, 85, 34, 887),

		DIAMOND(1617, 1601, 107.5, 43, 890),

		DRAGONSTONE(1631, 1615, 137.5, 55, 885),

		ONYX(6571, 6573, 167.5, 67, 2717),
		
		HYDRIX(29085, 29087, 197.5, 79, 2717),
		
		ZENYTE(29141, 29143, 200, 89, 41408),

		LIMESTONE(3211, 3420, 10, 12, 2717);

		private double experience;
		private int levelRequired;
		private int uncut, cut;

		private int emote;

		private Gem(int uncut, int cut, double experience, int levelRequired,
				int emote) {
			this.uncut = uncut;
			this.cut = cut;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.emote = emote;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public int getUncut() {
			return uncut;
		}

		public int getCut() {
			return cut;
		}

		public int getEmote() {
			return emote;
		}

		public static Gem forId(int uncut) {
			for (Gem gem : Gem.values()) {
				if (gem.getUncut() == uncut) {
					return gem;
				}
			}
			return null;
		}

	}

	public static void cut(Player player, Gem gem) {
		if (player.getInventory().getItems()
				.getNumberOf(new Item(gem.getUncut(), 1)) <= 1) {
			// 1 lets start
			player.getActionManager().setAction(new GemCutting(gem, 1));
		} else {
			player.getDialogueManager().startDialogue("GemCuttingD", gem);
		}
	}

	private Gem gem;
	private int quantity;

	public GemCutting(Gem gem, int quantity) {
		this.gem = gem;
		this.quantity = quantity;
	}

	public boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < gem.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a crafting level of " + gem.getLevelRequired() + " to cut that gem.");
			return false;
		}
		if (!player.getInventory().containsOneItem(gem.getUncut())) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You don't have any "
					+ ItemDefinitions.getItemDefinitions(gem.getUncut()).getName().toLowerCase() + " to cut.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			player.animate(new Animation(gem.getEmote()));
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		if (player.getEquipment().wearingSkillCape(Skills.CRAFTING) && Utils.getRandom(100) <= 1) {
			for (int index = 0; index < quantity; index++) {
				player.getInventory().deleteItem(gem.getUncut(), 1);
				player.getInventory().addItem(gem.getCut(), 1);
				player.getSkills().addXp(Skills.CRAFTING, gem.getExperience());
			}
			player.sendMessage("Your crafting cape magically cuts all of your gems for you!");
			return -1;
		}
		player.getInventory().deleteItem(gem.getUncut(), 1);
		player.getInventory().addItem(gem.getCut(), 1);
		if (gem.getUncut() == 1617) {
			player.diamondscut++;
		} else if (gem.getUncut() == 1619) {
			player.rubyscut++;
		} else if (gem.getUncut() == 1621) {
			player.emeraldscut++;
		} else if (gem.getUncut() == 1623) {
			player.sapphirescut++;
		} else if (gem.getUncut() == 1625) {
			player.opalscut++;
		} else if (gem.getUncut() == 1627) {
			player.jadescut++;
		} else if (gem.getUncut() == 1629) {
			player.redtopazcut++;
		} else if (gem.getUncut() == 1631) {
			player.dragonstonescut++;
		} else if (gem.getUncut() == 6571) {
			player.onyxscut++;
		}
		player.randomevent(player);
		player.getSkills().addXp(Skills.CRAFTING, gem.getExperience());
		player.getPackets().sendGameMessage(
				"You cut the "
						+ ItemDefinitions.getItemDefinitions(gem.getUncut())
						.getName().toLowerCase() + ".", true);
		quantity--;
		if (quantity <= 0) {
			return -1;
		}
		player.animate(new Animation(gem.getEmote())); // start the
		// emote and add
		// 2 delay
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}
