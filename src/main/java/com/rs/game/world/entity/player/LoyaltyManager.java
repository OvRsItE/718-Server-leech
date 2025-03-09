package com.rs.game.world.entity.player;

import java.util.TimerTask;

import com.rs.cores.CoresManager;

public class LoyaltyManager {

	private static final int INTERFACE_ID = 1143;
	private transient Player player;

	public LoyaltyManager(Player player) {
		this.player = player;
	}

	public void openLoyaltyStore(Player player) {
		player.getPackets().sendWindowsPane(INTERFACE_ID, 0);
	}

	public void startTimer() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			int timer = 1800;

			@Override
			public void run() {
				if (timer == 1) {
					if (player.getDonationManager().hasRank(DonatorRanks.DRAGON) ||  player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 2000);
					} else if (player.getDonationManager().hasRank(DonatorRanks.RUNE) ||  player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 1750);
					} else if (player.getDonationManager().hasRank(DonatorRanks.ADAMANT) ||  player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 1500);
					} else if (player.getDonationManager().hasRank(DonatorRanks.MITHRIL) ||  player.getRights()== 7 || player.getRights()== 2 || player.getRights()== 1 || player.isSupporter()) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 1250);
						player.getPackets().sendGameMessage("<col=8B0000>[Game Notice]</col> - You have recieved 1250 Loyalty Points for playing for 30 minutes!");
					} else if (player.getDonationManager().hasRank(DonatorRanks.STEEL)) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 500);
						player.getPackets().sendGameMessage("<col=8B0000>[Game Notice]</col> - You have recieved 500 Loyalty Points for playing for 30 minutes!");
					} else if (player.getDonationManager().hasRank(DonatorRanks.IRON)) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 400);
						player.getPackets().sendGameMessage("<col=8B0000>[Game Notice]</col> - You have recieved 400 Loyalty Points for playing for 30 minutes!");
					} else if (player.getDonationManager().hasRank(DonatorRanks.BRONZE)) {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 300);
						player.getPackets().sendGameMessage("<col=8B0000>[Game Notice]</col> - You have recieved 300 Loyalty Points for playing for 30 minutes!");
					} else {
						player.setLoyaltyPoints(player.getLoyaltyPoints() + 200);			
						player.getPackets().sendGameMessage("<col=8B0000>[Game Notice]</col>You have recieved 200 Loyalty Points for playing for 30 minutes!");
					}
					timer = 1800;
				}
				if (timer > 0) {
					timer--;
				}
			}
		}, 0L, 1000L);
	}
}