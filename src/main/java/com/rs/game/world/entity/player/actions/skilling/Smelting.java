package com.rs.game.world.entity.player.actions.skilling;

import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;

import java.util.HashMap;
import java.util.Map;

public class Smelting extends Action {

	public enum SmeltingBar {

		BRONZE(1, 6.2, new Item[] { new Item(436), new Item(438) }, new Item(2349), 0),

		BLURITE(8, 8.0, new Item[] { new Item(668) }, new Item(9467), 1),

		IRON(15, 12.5, new Item[] { new Item(440) }, new Item(2351), 2),

		SILVER(20, 13.7, new Item[] { new Item(442) }, new Item(2355), 3),

		STEEL(30, 17.5, new Item[] { new Item(440), new Item(453, 2) }, new Item(2353), 4),

		GOLD(40, 22.5, new Item[] { new Item(444) }, new Item(2357), 5),

		MITHRIL(50, 30, new Item[] { new Item(447), new Item(453, 4) }, new Item(2359), 6),

		ADAMANT(70, 37.5, new Item[] { new Item(449), new Item(453, 6) }, new Item(2361), 7),

		RUNE(85, 50, new Item[] { new Item(451), new Item(453, 8) }, new Item(2363), 8),

		DRAGONBANE(80, 50, new Item[] { new Item(21779) }, new Item(21783, 1), 9),

		WALLASALKIBANE(80, 50, new Item[] { new Item(21780) }, new Item(21784, 1), 10),

		BASILISKBANE(80, 50, new Item[] { new Item(21781) }, new Item(21785, 1), 11),

		ABYSSSALBANE(80, 50, new Item[] { new Item(21782) }, new Item(21786, 1), 11),

		CANNON_BALLS(35, 25.6, new Item[] { new Item(2353, 1), new Item(4, 1) }, new Item(2, 4), 1),

		ZENYTE_AMULET(98, 200, new Item[] { new Item(29143), new Item(1595), new Item(2357) }, new Item(29162), 1),

		ZENYTE_RING(89, 200, new Item[] { new Item(29143), new Item(1592), new Item(2357) }, new Item(29149), 1),

		ZENYTE_BRACELET(95, 200, new Item[] { new Item(29143), new Item(11065), new Item(2357) }, new Item(29151), 1),

		ZENYTE_NECKLACE(92, 200, new Item[] { new Item(29143), new Item(1597), new Item(2357) }, new Item(29147), 1),

		/**
		 * dung
		 */
		NOVITE(1, 7, new Item[]
				{ new Item(17630) }, new Item(17650), Skills.SMITHING),

		BATHUS(10, 13.3, new Item[]
				{ new Item(17632) }, new Item(17652), Skills.SMITHING),

		MARMAROS(20, 19.6, new Item[]
				{ new Item(17634) }, new Item(17654), Skills.SMITHING),

		KRATONITE(30, 25.9, new Item[]
				{ new Item(17636) }, new Item(17656), Skills.SMITHING),

		FRACTITE(40, 32.2, new Item[]
				{ new Item(17638) }, new Item(17658), Skills.SMITHING),

		ZEPHYRIUM(50, 38.5, new Item[]
				{ new Item(17640) }, new Item(17660), Skills.SMITHING),

		ARGONITE(60, 44.8, new Item[]
				{ new Item(17642) }, new Item(17662), Skills.SMITHING),

		KATAGON(70, 51.1, new Item[]
				{ new Item(17644) }, new Item(17664), Skills.SMITHING),

		GORGONITE(80, 57.4, new Item[]
				{ new Item(17646) }, new Item(17666), Skills.SMITHING),

		PROMETHIUM(90, 63.7, new Item[]
				{ new Item(17648) }, new Item(17668), Skills.SMITHING);



		private static Map<Integer, SmeltingBar> bars = new HashMap<Integer, SmeltingBar>();
		private static Map<Integer, SmeltingBar> forOres = new HashMap<Integer, SmeltingBar>();

		public static SmeltingBar forId(int buttonId) {
			return bars.get(buttonId);
		}

		public static SmeltingBar forOre(int oreId) {
			return forOres.get(oreId);
		}

		static {
			for (SmeltingBar bar : SmeltingBar.values()) {
				bars.put(bar.getButtonId(), bar);
			}

			for (SmeltingBar bar : SmeltingBar.values()) {
				for (Item item : bar.getItemsRequired()) {

					if (bar.getProducedBar().getId() == 2353) {
						continue;
					}
					forOres.put(item.getId(), bar);

				}
			}
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private int buttonId;
		private Item producedBar;

		private SmeltingBar(int levelRequired, double experience, Item[] itemsRequired, Item producedBar,
				int buttonId) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedBar = producedBar;
			this.buttonId = buttonId;
		}

		public Item[] getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public Item getProducedBar() {
			return producedBar;
		}

		public double getExperience() {
			return experience;
		}

		public int getButtonId() {
			return buttonId;
		}
	}

	public SmeltingBar bar;
	public WorldObject object;
	public int ticks;

	public Smelting(int slotId, WorldObject object, int ticks) {
		this.object = object;
		bar = SmeltingBar.forId(slotId);
		this.ticks = ticks;
	}

	public Smelting(SmeltingBar bar, WorldObject object, int ticks) {
		this.object = object;
		this.bar = bar;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (bar == null || player == null || object == null) {
			return false;
		}
		if (!player.getInventory().containsItem(bar.getItemsRequired()[0].getId(),
				bar.getItemsRequired()[0].getAmount())) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need " + bar.getItemsRequired()[0].getDefinitions().getName() + " to create a "
							+ bar.getProducedBar().getDefinitions().getName() + ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(bar.getItemsRequired()[1].getId(),
					bar.getItemsRequired()[1].getAmount())) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need " + bar.getItemsRequired()[1].getDefinitions().getName() + " to create a "
								+ bar.getProducedBar().getDefinitions().getName() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of at least "
					+ bar.getLevelRequired() + " to smelt " + bar.getProducedBar().getDefinitions().getName());
			return false;
		}
		player.getPackets()
				.sendGameMessage("You place the required ores and attempt to create a bar of "
						+ bar.getProducedBar().getDefinitions().getName().toLowerCase().replace(" bar", "") + ".",
						true);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (bar == null || player == null || object == null) {
			return false;
		}
		if (!player.getInventory().containsItem(bar.getItemsRequired()[0].getId(),
				bar.getItemsRequired()[0].getAmount())) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need " + bar.getItemsRequired()[0].getDefinitions().getName() + " to create a "
							+ bar.getProducedBar().getDefinitions().getName() + ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(bar.getItemsRequired()[1].getId(),
					bar.getItemsRequired()[1].getAmount())) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need " + bar.getItemsRequired()[1].getDefinitions().getName() + " to create a "
								+ bar.getProducedBar().getDefinitions().getName() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of at least "
					+ bar.getLevelRequired() + " to smelt " + bar.getProducedBar().getDefinitions().getName());
			return false;
		}
		player.faceObject(object);
		return true;
	}

	public boolean isSuccessFull(Player player) {
		if (bar == SmeltingBar.IRON) {
			if (player.getEquipment().getItem(Equipment.SLOT_RING) != null
					&& player.getEquipment().getItem(Equipment.SLOT_RING).getId() == 2568) {
				return true;
			} else {
				return Utils.getRandom(100) <= (player.getSkills().getLevel(Skills.SMITHING) >= 45 ? 80 : 50);
			}
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		player.animate(new Animation(3243));
		if (bar == SmeltingBar.GOLD && player.getEquipment().getGlovesId() == 776) {
			player.getSkills().addXp(Skills.SMITHING, bar.getExperience() * 2);
		} else {
			player.getSkills().addXp(Skills.SMITHING, bar.getExperience());
		}
		for (Item required : bar.getItemsRequired()) {
			if (required.getName().toLowerCase().contains("mould")) {
				continue;
			}
			player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (isSuccessFull(player)) {

			if (bar == SmeltingBar.BRONZE) {
				player.bronzebars++;
			} else if (bar == SmeltingBar.IRON) {
				player.ironbars++;
			} else if (bar == SmeltingBar.STEEL) {
				player.steelbars++;
			} else if (bar == SmeltingBar.MITHRIL) {
				player.mithbars++;
			} else if (bar == SmeltingBar.ADAMANT) {
				player.addybars++;
			} else if (bar == SmeltingBar.RUNE) {
				player.runebars++;
			} else if (bar == SmeltingBar.CANNON_BALLS) {
				player.cannonBall += 4;
				player.cannonballs++;
			}

			if (player.getEquipment().getGlovesId() == 19754) {
				if (Utils.random(10) >= 5) {
					player.getInventory().addItem(bar.getProducedBar().getId(), 3);
				}
			}
			player.getInventory().addItem(bar.getProducedBar());
			if (bar.getProducedBar().getName().contains("bar"))
				player.getPackets().sendGameMessage("You retrieve a bar of "
						+ bar.getProducedBar().getDefinitions().getName().toLowerCase().replace(" bar", "") + ".",
						true);
			else
				player.getPackets().sendGameMessage("You receive a " + bar.getProducedBar().getName() + ".", false);
		} else {
			player.getPackets().sendGameMessage("The ore is too impure and you fail to refine it.", true);
		}
		if (ticks > 0) {
			return 1;
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
}
