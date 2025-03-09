package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.GraveStoneSelection;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.activities.Lobby;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.TeleportManager;

public class Grim extends Dialogue {

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(int interfaceId, int id) {
		if (stage == 1) {
			sendPlayerDialogue(
					EmotionManager.QUESTION,
					"Oh no, not at all I just wondered into some black portal and here I am! What can you do for me though?");
			stage = 2;
		} else if (stage == 2) {
			sendNPCDialogue(
					6390,
					EmotionManager.ANNOYED,
					"Are you joking with Death human! A portal free to access into my domain! Nonsense! You obviously died and hit your head."
							+ "I demand you ask your question and be on your way!");
			stage = 3;
		} else if (stage == 3) {
			sendOptionsDialogue("Ask the Grim Reaper a Question",
					"I wish to alter my grave stone",
					"I want to access the forbidden Cages",
					"I want to face Lucien!",
					"Take me to the hunger games");
			stage = 4;
		} else if (stage == 4) {
			if (id == OPTION_1) {
				sendNPCDialogue(6390, EmotionManager.THINKING,
						"I see you wish to alter the tomb for your afterlife? Very well");
				stage = 5;
			} else if (id == OPTION_2) {
				sendNPCDialogue(
						6390,
						EmotionManager.QUESTION,
						"Forbidden cages? You're asking to roam around the land of the dead?! Alright...");
				stage = 6;
			} else if (id == OPTION_3) {
				sendNPCDialogue(
						6390,
						EmotionManager.QUESTION,
						"LUCIEN!? Do you like me so much you'd like to join the dead!? ...although okay I'll send you");
				stage = 7;
			} else if(id == OPTION_4) {
				sendOptionsDialogue("Are you sure you want to go to the hunger games lobby", "Yes, take me there", "No, I've changed my mind");
				stage = 10;
			}
		} else if (stage == 5) {
			GraveStoneSelection.openSelectionInterface(player);
			end();
		} else if (stage == 6) {
		//	Magic.sendObjectTeleportSpell(player, true,
		//	TeleportManager.getLocation("cagelobby"));
			end();
		} else if (stage == 7) {
			sendNPCDialogue(
					6390,
					EmotionManager.QUESTION,
					"But you must know that Lucien is in the wilderness, and he rewards you better when you fight him as a team.");
			stage = 8;
		} else if (stage == 8) {
			sendOptionsDialogue(
					"Do you want to fight Lucien? Its in the Wilderness",
					"Yes - I want to fight lucien", "No - He's to tough for me");
			stage = 9;
		} else if (stage == 9) {
			if (id == OPTION_1) {
				end();
				if (World.lucienSpot != null) {
//Magic.sendObjectTeleportSpell(player, true,
//World.lucienSpot);
//player.getControlerManager().startControler("Wilderness");
				} else {
					player.sm("Lucien got nulled somehow please report this.");
				}
			} else {
				sendNPCDialogue(6390, EmotionManager.QUESTION,
						"Very well you're smarter then you look.. Anything else?");
				stage = 4;
			}
		} else if(stage == 10) {
			if(id == OPTION_1) {
				//end();
				if (player.getEquipment().wearingArmour()) {
					sendNPCDialogue(
							6390,
							ANNOYED,
							"You must take off your armour first this minigame doesn't allow worthless human items.");
					stage = 11;
				} else if (player.getInventory().hasItems()) {
					sendNPCDialogue(
							6390,
							ANNOYED,
							"You must take off your inventory first this minigame doesn't allow worthless human items.");
					stage = 11;
				} else if(!player.canHomeTele()) {
					sendNPCDialogue(
							6390,
							ANNOYED,
							"You aren't allowed to go here at this time.");
					stage = 11;
			    } else {
				Lobby.join(player);
				end();
			    }
			} else
				end();
		} else if(stage == 11)
			end();

	}

	@Override
	public void start() {
		sendNPCDialogue(
				6390,
				EmotionManager.THINKING,
				"A new soul enters the relm of no return.. Although it seems you're not dead, therefore you seek me... Human?");
		stage = 1;

	}

}
