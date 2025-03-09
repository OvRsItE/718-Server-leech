package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.DonatorRanks;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;


/**
 * @author Muda - Donator Boss Teleports
 */
public class DonaterBoss extends Dialogue {

	@Override
	public void start() {
		
		/**
		 * If player is Donator up to Devine it will send these options
		 */
			if(player.getDonationManager().hasRank(DonatorRanks.BRONZE) && !player.getDonationManager().hasRank(DonatorRanks.ADAMANT)) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Instant TP To GWD Boss", "Money Making Creatures", "Resource Store");
				stage = 1;
			}
			
		/*
		 * If player is Godlike donator it will send those options
		 */
			
			if (player.getDonationManager().hasRank(DonatorRanks.ADAMANT)) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Godwars 2", "MASS Boss Tunnels", "Money Making Creatures", "Resource Store");
				stage = 10;
			}		
		}

	@Override
	public void run(int interfaceId, int componentId) {
		
		/*
		 * Handles stages 1 ( Donator up to Devine Options )
		 */
		if (stage == 1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Bandos", "Armadyl", "Zamorak", "Saradomin");
				stage = 20;
			}
			if (componentId == OPTION_2) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Abyssal Demons", "Ice Stryke Wyrm", "Desert Stryke Wyrm", "Jungle Stryke Wyrm", "Wildy Wyrm <col=8B0000>(Deep Wildy)</col>");
				stage = 21;
			}
			if (componentId == OPTION_3) {
				end();
				ShopsHandler.openShop(player, 152);
			}
		} else
		
		/*
		 * Handles stage 20 (Instant Godwars teleport)
		 */
		if (stage == 20) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(2864, 5357, 0), new int[0]);
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(2835, 5296, 0), new int[0]);
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(2925, 5331, 0), new int[0]);
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(2923, 5255, 0), new int[0]);
			}
		} else
		
		/*
		 * Handles stage 21 (Money making tps)
		 */
		
		if (stage == 21) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(3419, 3573, 2), new int[0]);
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(3423, 5667, 0), new int[0]);
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(3373, 3170, 0), new int[0]);
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(2459, 2901, 0), new int[0]);
			}
			if (componentId == OPTION_5) {
				Magic.sendNormalTeleportSpell(player, 0, 0.0D, new Position(3170, 3885, 0), new int[0]);
			}
		} else
			
		/*
		 * Handles Godlike donator stages
		 */
		if (stage == 10) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Bandos", "Armadyl", "Zamorak", "Saradomin");
				stage = 50;
			}
			if (componentId == OPTION_2) {
				end();
				player.getPackets().sendGameMessage("This tunnel is under construction. Should be set in the next update.");
			}
			if (componentId == OPTION_3) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Abyssal Demons", "Ice Stryke Wyrm", "Desert Stryke Wyrm", "Jungle Stryke Wyrm", "Wildy Wyrm <col=8B0000>(Deep Wildy)</col>");
				stage = 21;
			}
			if (componentId == OPTION_4) {
				end();
				ShopsHandler.openShop(player, 152);
				
			}
		}
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
	
}