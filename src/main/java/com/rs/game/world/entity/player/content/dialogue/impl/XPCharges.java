package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.ShopsHandler;

public final class XPCharges extends Dialogue {

	private int npcId = 3705;
	
	/**
	 * What to do when PLAYER clicks NPC 
	 */
	
@Override
public void start() {
		sendNPCDialogue(npcId, NORMAL, "Hello "+player.getDisplayName()+", what can i do for you?");
		stage = 1;
	
}

@Override
public void run(int interfaceId, int componentId) {
	/**
	 * Sends first 4 OPTIONS.
	 */
	
	if (stage == 1) {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Open Skilling Shop", "Manage XP Rings", "Mastery Capes", "Nevermind");
		stage = 2;
	} else if (stage == 2) {
			if (componentId == OPTION_1) {
				ShopsHandler.openShop(player, 150);
				end();
	} else if (componentId == OPTION_2) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Charge XP Ring", "Fetch XP Rings", "Back");
			stage = 50;
	} else if (componentId == OPTION_3) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Skillcapes", "Skillcapes (t)", "Hoods", "Next Page");
				stage = 86;
				
	} else if (componentId == OPTION_4) {
			end();
		}
	} else if (stage == 86) {
			if (componentId == OPTION_1) {
					ShopsHandler.openShop(player, 2);
					end();
				}
				if(componentId == OPTION_2) {
					ShopsHandler.openShop(player, 3);
					end();
				}
				if(componentId == OPTION_3) {
					ShopsHandler.openShop(player, 4);
					end();
				}
				if (componentId == OPTION_4) {
					sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Master Capes", "Max Cape");
					stage = 101;
				}
				
		} else if (stage == 101) {
				if (componentId == OPTION_1) {
					ShopsHandler.openShop(player, 151);
					end();
				}
				if (componentId == OPTION_2) {
					if (player.getSkills().getTotalLevel() > 2474) {
						sendNPCDialogue(npcId, HAPPY, "You have some nice stats, and are able to wear this cape! It costs 1000K however. Still interrested?");
						stage = 102;
					} else {
						sendNPCDialogue(npcId, NORMAL, "You must have a total level of 2475 to purchase this cape.");
						stage = 12;
					}
				}
		} else if (stage == 102) {
				sendOptionsDialogue("Still interrested?", "Yes.", "No.");
				stage = 11;
		} else if(stage == 10) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes Please.", "No thanks.");
			stage = 11;
	} else if (stage == 11) {
			if (componentId == OPTION_1) {
			if(player.getInventory().containsItem(995, 1000000)) { // Checks if inventory contains 1M?
			player.getInventory().deleteItem(995, 1000000);
			sendNPCDialogue(npcId, HAPPY, "Thank you!");
			player.getInventory().addItem(20767, 1);
			player.getInventory().addItem(20768, 1);	
			stage = 12;
	} else {
			sendNPCDialogue(npcId, HAPPY, "You don't have enough gold in your inventory.");
			stage = 12;
	}
	} else if (componentId == OPTION_2) {
				end();
			}
	} else if (stage == 12) {
				end();
	} else if (stage == 50) {
			if (componentId == OPTION_1) {
				if (player.xpRingCharges == 0 && player.getInventory().containsItem(29996, 1)) {
			player.xpRingCharges = 150;
			player.getInventory().deleteItem(29996, 1);
			player.getInventory().addItem(29997, 1);
			player.activatedCharges = true;
			sendNPCDialogue(npcId, HAPPY, "I have charged your XP Ring with 150 charges.");
			stage = 51;
	} else  if (player.xpRingCharges > 0 && player.getInventory().containsItem(29996, 1)){
			sendNPCDialogue(npcId, HAPPY, "You appear to already have a charged ring. Please use the fetch feature!");
			stage = 12;
		} else {
			sendNPCDialogue(npcId, HAPPY, "You must have a uncharged ring in your inventory!");
		}
	} else if (componentId == OPTION_2) {
			if (player.getInventory().containsItem(29996, 1) && !player.getInventory().containsItem(29997, 1)) {
			sendNPCDialogue(npcId, HAPPY, "You only have the uncharged ring, please have both rings in your inventory.");
			stage = 50;
	} else if(player.getInventory().containsItem(29997, 1) && !player.getInventory().containsItem(29996, 1)) {
			sendNPCDialogue(npcId, HAPPY, "You only have the charged ring in your inventory, you must have both rings.");
			stage = 50;
	} else if(player.getInventory().containsItem(29996, 1) && player.getInventory().containsItem(29997, 1)) {
			sendNPCDialogue(npcId, HAPPY, "I can see you have both rings in your inventory. So we can continue the process!");
			stage = 52;
	} else {
			sendNPCDialogue(npcId, HAPPY, "You don't have any rings in your inventory.");
			stage = 50;
		}		
	} else if (componentId == OPTION_3) {
			stage = 1;
		}
	} else if (stage == 52) {
			player.getInventory().deleteItem(29997, 1);
			player.getInventory().deleteItem(29996, 1);
		for (NPC npc : World.getNPCs()) {
		    if (npc.getId() == npcId) {
			npc.animate(new Animation(713));
			npc.setNextGraphics(new Graphics(113));
			player.getInventory().addItem(29997, 1);
			player.setxpRingPoints(player.getxpRingCharges() + 150);
			sendNPCDialogue(npcId, HAPPY, "There you go, you now have "+player.getxpRingCharges()+" charges.");
			stage = 53;
		    }
		}
	} else if (stage == 53) {
		end();
	}
	
}
@Override
public void finish() {
	// TODO Auto-generated method stub
	
	}
}

