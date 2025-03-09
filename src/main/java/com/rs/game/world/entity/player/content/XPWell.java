package com.rs.game.world.entity.player.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import com.rs.Constants;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.Timer;

/**
 * Created by Arham 4 on 6/18/14.
 */
public class XPWell {

	/**
	 * The amount of time for the WorldTask.
	 *
	 */
	public static int taskAmount = 1800000;
	public static int taskTime = 1800000;
	public static int TimeLeft = 0;

	/**
	 * Sends a dialogue for the amount to give.
	 *
	 * @param player
	 *            The Player giving the amount.
	 */
	public static void give(Player player) {
		if (World.isWellActive()) {
			player.getPackets().sendGameMessage(
					"<col=8B0000>[XP Well]</col> - The XP well is already active! Don't waste your time!");
			return;
		}

		player.getPackets().sendRunScript(109, "Progress: "
				+ NumberFormat.getNumberInstance(Locale.UK).format(World.getWellAmount()) + " GP.  Goal: 250M");
		player.getTemporaryAttributtes().put("donate_xp_well", Boolean.TRUE);
	}

	/**
	 * Donates to the well the amount to give.
	 *
	 * @param player
	 *            The Player donating.
	 * @param amount
	 *            The amount to give.
	 */
	public static int taskFinish = 5000000;
	public static int donated;

	public static void donate(Player player, int amount) {

		if (Player.isThursday() && !player.finishedTask) {
			if (amount >= 5000000) {
				player.AddToWell = 5;
			} else if (amount >= 5000000 && player.getCorpsKilled() >= 15 && player.getTasksFinished() >= 5) {
				player.sm("<col=8B0000>You have finished the daily task and been rewarded!");
				player.setSkillPoints(player.getSkillPoints() + 100);
				player.getSquealOfFortune().giveEarnedSpins(3);
				player.getInventory().addItem(9245, 200);
				player.finishedTask = true;

			}
		}

		if (amount < 0) {
			return;
		}
		if (World.getWellAmount() + amount > Constants.WELL_MAX_AMOUNT) {
			amount = Constants.WELL_MAX_AMOUNT - World.getWellAmount();
		}
		if (!player.getInventory().containsItem(995, amount) && player.money < amount) {
			player.getPackets().sendGameMessage("<col=8B0000>[XP Well]</col> - You don't have that much money!");
			return;
		}

		if (amount < 100000) {
			player.getPackets()
					.sendGameMessage("<col=8B0000>[XP Well]</col> - You must donate at least 100,000 (100K) GP!");
			return;
		}
		boolean pouch = !player.getInventory().containsItem(995, amount);
		if (!pouch) {
			player.getInventory().deleteItem(995, amount);
		} else {
			player.getPackets().sendRunScript(5561, 0, amount);
			player.money -= amount;
			player.refreshMoneyPouch();
		}
		World.addWellAmount(player.getDisplayName(), amount);
		postDonation();
	}

	/**
	 * A check after donating to the well to see if the x2 XP should start.
	 */

	private static void postDonation() {
		if (World.getWellAmount() >= Constants.WELL_MAX_AMOUNT) {
			World.sendWorldMessage(
					"<col=8B0000>[XP Well]</col> - The goal of 250M has been reached! Double XP for 5 hours begins now!",
					false);
			taskAmount = 18000;
			Constants.ATTACK_XP_RATE = Constants.ATTACK_XP_RATE * 2;
			Constants.STRENGTH_XP_RATE = Constants.STRENGTH_XP_RATE * 2;
			Constants.DEFENCE_XP_RATE = Constants.DEFENCE_XP_RATE * 2;
			Constants.HITPOINTS_XP_RATE = Constants.HITPOINTS_XP_RATE * 2;
			Constants.RANGE_XP_RATE = Constants.RANGE_XP_RATE * 2;
			Constants.MAGIC_XP_RATE = Constants.MAGIC_XP_RATE * 2;
			Constants.PRAYER_XP_RATE = Constants.PRAYER_XP_RATE * 2;
			Constants.SLAYER_XP_RATE = Constants.SLAYER_XP_RATE * 2;
			Constants.FARMING_XP_RATE = Constants.FARMING_XP_RATE * 2;
			Constants.AGILITY_XP_RATE = Constants.AGILITY_XP_RATE * 2;
			Constants.HERBLORE_XP_RATE = Constants.HERBLORE_XP_RATE * 2;
			Constants.THIEVING_XP_RATE = Constants.THIEVING_XP_RATE * 2;
			Constants.CRAFTING_XP_RATE = Constants.CRAFTING_XP_RATE * 2;
			Constants.MINING_XP_RATE = Constants.MINING_XP_RATE * 2;
			Constants.SMITHING_XP_RATE = Constants.SMITHING_XP_RATE * 2;
			Constants.FISHING_XP_RATE = Constants.FISHING_XP_RATE * 2;
			Constants.COOKING_XP_RATE = Constants.COOKING_XP_RATE * 2;
			Constants.FIREMAKING_XP_RATE = Constants.FIREMAKING_XP_RATE * 2;
			Constants.WOODCUTTING_XP_RATE = Constants.WOODCUTTING_XP_RATE * 2;
			Constants.CONSTRUCTION_XP_RATE = Constants.CONSTRUCTION_XP_RATE * 2;
			Constants.HUNTER_XP_RATE = Constants.HUNTER_XP_RATE * 2;
			Constants.SUMMONING_XP_RATE = Constants.SUMMONING_XP_RATE * 2;
			Constants.DUNGEONEERING_XP_RATE = Constants.DUNGEONEERING_XP_RATE * 2;
			Constants.FLETCHING_RATE = Constants.FLETCHING_RATE * 2;
			Constants.RUNECRAFTING_XP_RATE = Constants.RUNECRAFTING_XP_RATE * 2;

			setWellTask();
			World.setWellActive(true);
			Timer.startWellTimer();
		}
	}

	/**
	 * Sets the task for the reset of the well.
	 */
	public static void setWellTask() {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				World.setWellActive(false);
				World.resetWell();
				TimeLeft = 0;
				stop();

			}
		}, taskAmount);
	}

	/**
	 * Saves the progress of the well. If the x2 event is already active, this sends
	 * the amount left in milliseconds.
	 */
	public static void save() {
		File output = new File("./data/well/data.txt");

		if (output.canWrite()) {
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter(output, false));
				if (World.isWellActive()) {
					out.write("true " + taskTime);
				} else {
					out.write("false " + World.getWellAmount());
				}
			} catch (IOException ignored) {
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException ignored) {
					}
				}
			}
		}
	}
}