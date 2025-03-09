package com.rs.game.world.entity.player.actions.skilling.thieving;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

public class SafeCracking extends Action {

	private boolean usingStethoscope;
	
	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			player.sendMessage("You attempt to crack the wall safe...");
			player.animate(new Animation(15576));
			setActionDelay(player, 3);
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
		if (isSuccesfull(player)) {
			double random = Utils.getRandomDouble(1);
			for (Rewards reward : Rewards.values()) {
				if (random >= reward.rate) {
					player.getInventory().addItem(reward.id, reward.amount);
					player.sendMessage("You successfully crack the safe and receive " + 
								(reward.amount > 1 ? "some" : "an") + " " + ItemDefinitions.forId(reward.id).getName() + ".");
					player.getSkills().addXp(Skills.THIEVING, 70);
					player.animate(new Animation(15576));
					return 5;
				}
			}
		} else {
			player.animate(new Animation(15575));
			player.lock(3);
			player.applyHit(new Hit(player, 30, HitLook.REGULAR_DAMAGE));
			player.sendMessage("You fail at cracking the wall safe, and take some damage.");
		}
		return -1;
	}

	@Override
	public void stop(Player player) {}
	
	private boolean checkAll(Player player) {
		if (!(player.getSkills().getLevel(Skills.THIEVING) >= 50)) {
			player.sendMessage("You need at least level 50 Theiving to crack a wall safe.");
			return false;
		}
		if (player.getInventory().getFreeSlots() < 1) {
			player.sendMessage("You don't have enough inventory to do that.");
			return false;
		}
		if (player.getInventory().containsItem(5560, 1) && player.getSkills().getLevel(Skills.AGILITY) >= 50 
				|| player.getSkills().getLevel(Skills.THIEVING) >= 99) {
			usingStethoscope = true;
		} else {
			usingStethoscope = false;
		}
		return true;
	}
	
	/**
	 * Checks if the player is successful at theiving the safe.
	 * @param player
	 * 			The player.
	 * @return true If the player was successful at cracking the safe, false otherwise.
	 */
	private boolean isSuccesfull(Player player) {
		int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
		int increasedChance = getIncreasedChance(player);
		int level = Utils.getRandom(thievingLevel + increasedChance);
		double ratio = level / (Utils.random(50 + 6) + 1);
		if (Math.round(ratio * thievingLevel) < 50 / (player.getAuraManager().getThievingAccurayMultiplier()))
			return false;
		return true;
	}
	

	/**
	 * Gets the increased chance for succesfully cracking a safe.
	 * @param player
	 *            The player.
	 * @return The amount of increased chance.
	 */
	private int getIncreasedChance(Player player) {
		int chance = 0;
		if (Equipment.getItemSlot(Equipment.SLOT_HANDS) == 10075)
			chance += 12;
		if (Equipment.getItemSlot(Equipment.SLOT_CAPE) == 15349)
			chance += 15;
		if (usingStethoscope)
			chance += 8;
		return chance;
	}
	
	enum Rewards {
		
		COINS(995, 2000, 0.80D),
		COINS_1(995, 5000, 0.70D),
		COINS_2(995, 7000, 0.60D),
		UNCUT_SAPPHIRE(1623, 1, 0.50D),
		UNCUT_EMERALD(1621, 1, 0.40D),
		UNCUT_RUBY(1619, 1, 0.30D),
		UNCUT_DIAMOND(1617, 1, 0.20D),
		UNCUT_DRAGONSTONE(1631, 1, 0.10D);
		
		private int id;
		private int amount;
		private double rate;
		
		private Rewards(int id, int amount, double rate) {
			this.id = id;
			this.amount = amount;
			this.rate = rate;
		}
		
	}

}

