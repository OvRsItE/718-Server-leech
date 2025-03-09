package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.DonatorRanks;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class DonatorShops2 extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?");
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Shop Manager", "Bronze donator shop", "Iron donator shop", "Steel donator shop",
					"Mithril donator shop", "More...");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				if (player.getDonationManager().hasRank(DonatorRanks.BRONZE)) {
					ShopsHandler.openShop(player, 156);
					end();
				}
			}
			if (componentId == OPTION_2) {
				if (player.getDonationManager().hasRank(DonatorRanks.IRON)) {
					ShopsHandler.openShop(player, 157);
					end();
				}
			}
			if (componentId == OPTION_3) {
				if (player.getDonationManager().hasRank(DonatorRanks.STEEL)) {
					ShopsHandler.openShop(player, 158);
					end();
				}
			}
			if (componentId == OPTION_4) {
				if (player.getDonationManager().hasRank(DonatorRanks.MITHRIL)) {
					ShopsHandler.openShop(player, 159);
					end();
				}
			}
			if (componentId == OPTION_5) {
				stage = 3;
				sendOptionsDialogue("Shop Manager", "Adamant donator shop", "Rune donator shop", "Dragon donator shop",
						"Close");
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				if (player.getDonationManager().hasRank(DonatorRanks.ADAMANT)) {
					ShopsHandler.openShop(player, 160);
					end();
				}
			}
			if (componentId == OPTION_2) {
				if (player.getDonationManager().hasRank(DonatorRanks.RUNE)) {
					ShopsHandler.openShop(player, 161);
					end();
				}
			}
			if (componentId == OPTION_3) {
				if (player.getDonationManager().hasRank(DonatorRanks.DRAGON)) {
					ShopsHandler.openShop(player, 162);
					end();
				}
			}
			if (componentId == OPTION_4) {
				end();
			}
		}
	}

	@Override
	public void finish() {

	}

}