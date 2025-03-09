package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class CageLobby extends Dialogue {

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(int interfaceId, int id) {
		if(stage == 1) {
		   sendNPCDialogue(7664, EmotionManager.NORMAL, "Rule 1: Don't talk about the cages, " +
		   		                                        "Rule 2: Don't smuggle items. " +
		   		                                        "Any questions?");
		   stage = 2;
		} else if(stage == 2) {
			sendOptionsDialogue("What would you like to ask?", "When can I get started", "Whats the point in using the cages?", "May I enter?");
			stage = 3;			
		} else if(stage == 3) {
			if(id == OPTION_1) {
				sendPlayerDialogue(EmotionManager.QUESTION, "When can I get started?");
				stage = 4;
			} else if(id == OPTION_2) {
				sendPlayerDialogue(EmotionManager.QUESTION, "Whats the point in using the cages?");
				stage = 5;
			} else if(id == OPTION_3) {
				sendNPCDialogue(7664, EmotionManager.NORMAL, "Yes you may, Goodluck!");
				if(player.getEquipment().wearingArmour() || player.getInventory().hasItems() || player.getFamiliar() != null) {
					player.sm("You must bank everything on you in order to do this, and rid of your familiar");
					return;
				}
				if(World.exiting_start != 0) {
					player.sm("You can't do this during an update");
					return;
				} else {
					end();
					player.getControlerManager().startControler("cages");
				}
				}
		} else if(stage == 4) {
			sendNPCDialogue(7664, EmotionManager.NORMAL, "Now if you'd like.. So what do you say "+player.getDisplayName());
			stage = 6;
		} else if(stage == 5) {
			sendNPCDialogue(7664, EmotionManager.NORMAL, "I have exclusive rewards to offer for each person you kill. I can show you if you'd like");
			stage = 7;
		} else if(stage == 6 || stage == 7) {
			sendOptionsDialogue("Select a option", "I'd like to enter the cages", "I want to see the rewards I can get");
			stage = 8;
		} else if(stage == 8) {
			if(id == OPTION_1) {
				end();
				if(player.getEquipment().wearingArmour() || player.getInventory().hasItems() || player.getFamiliar() != null) {
					player.sm("You must bank everything on you in order to do this, and rid of your familiar");
					return;
				}
				if(World.exiting_start != 0) {
					player.sm("You can't do this during an update");
					return;
			    }
				player.getControlerManager().startControler("cages");
			} else if(id == OPTION_2) {
				end();
				player.sm("You have <col=ff000>"+player.cagePoints+"</col> cage points.");
				ShopsHandler.openShop(player, 55);
			} else if (id == OPTION_3) {
				if(player.getEquipment().wearingArmour() || player.getInventory().hasItems() || player.getFamiliar() != null) {
					player.sm("You must bank everything on you in order to do this, and rid of your familiar");
					return;
				}
				if(World.exiting_start != 0) {
					player.sm("You can't do this during an update");
					return;
				} else {
					end();
					player.getControlerManager().startControler("cages");
				}
			}
		}
		
	}

	@Override
	public void start() {
		sendNPCDialogue(7664, NORMAL, "Welcome to the cages.. In order to be here you must follow the rules.");
		stage = 1;
	}

}
