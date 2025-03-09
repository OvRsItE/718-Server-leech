package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.SkillsDialogue;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;

public class ConBench extends Dialogue {
	public static int [] [] LEVELS = {{1}, {32}, {62} };
	//public static int [] EXP = {96, 180, 840};
	public static final double[][] EXP = { { 96 }, { 180 }, { 840 }};
	public static int [] REQUIRED_BASE_ITEMS = {960, 8778, 8782};
	private static final Item[][] PRODUCTS = { {new Item (8496, 1) }, {new Item (8550, 1) }, {new Item (8558, 1) } };
	private int index;


	@Override
	public void start() {
		index = (int) parameters[0];
		int[] ids = new int[PRODUCTS[index].length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = PRODUCTS[index][i].getId();
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "Choose how many you wish to make,<br>then click on the item to begin.", 28, ids, new SkillsDialogue.ItemNameFilter() {

			int count = 0;

			@Override
			public String rename(String name) {
				int levelRequired = LEVELS[index][count++];
				if (player.getSkills().getLevel(Skills.CRAFTING) < levelRequired)
					name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + levelRequired;
				return name;
			}
		});
	}

	@Override
	public void run(int interfaceId, int componentId) {
		final int componentIndex = SkillsDialogue.getItemSlot(componentId);
		if (componentIndex > REQUIRED_BASE_ITEMS.length) {
			end();
			return;
		}
		player.getActionManager().setAction(new Action() {

			int ticks;

			@Override
			public boolean start(final Player player) {
				if (!checkAll(player))
					return false;
				int leatherAmount = player.getInventory().getAmountOf(REQUIRED_BASE_ITEMS[index]);
				int requestedAmount = SkillsDialogue.getQuantity(player);
				if (requestedAmount > leatherAmount)
					requestedAmount = leatherAmount;
				setTicks(requestedAmount);
				return true;
			}

			public void setTicks(int ticks) {
				this.ticks = ticks;
				player.getInventory().deleteItem(1734, 1);
			}

			public boolean checkAll(Player player) {
				final int levelReq = LEVELS[index][componentIndex];
				if (player.getSkills().getLevel(Skills.CONSTRUCTION) < levelReq) {
					player.getPackets().sendGameMessage("You need a Contruction level of " + levelReq + " to craft this flatpack.");
					return false;
				}
				if (player.getInventory().getItems().getNumberOf(REQUIRED_BASE_ITEMS[index]) < PRODUCTS[index][componentIndex].getAmount()) {
					player.getPackets().sendGameMessage("You don't have enough planks in your inventory.");
					return false;
				}

				return true;
			}

			@Override
			public boolean process(Player player) {
				return checkAll(player) && ticks > 0;
			}

			@Override
			public int processWithDelay(Player player) {
				ticks--;
				if (ticks % 4 == 0)// will use one every time AT LEAST
					;
				Item item = PRODUCTS[index][componentIndex];
				player.getInventory().deleteItem(new Item(REQUIRED_BASE_ITEMS[index], item.getAmount()));
				player.getInventory().addItem(item.getId(), 1);
				player.getSkills().addXp(Skills.CONSTRUCTION, EXP[index][componentIndex]);
				player.animate(new Animation(898));
				return 3;
			}

			@Override
			public void stop(Player player) {
				setActionDelay(player, 3);
			}
		});
		end();
	}

	public static int getIndex(int requestedId) {
		for (int index = 0; index < REQUIRED_BASE_ITEMS.length; index++) {
			int baseId = REQUIRED_BASE_ITEMS[index];
			if (requestedId == baseId)
				return index;
		}
		return -1;

				}




	@Override
	public void finish() {

	}

}
