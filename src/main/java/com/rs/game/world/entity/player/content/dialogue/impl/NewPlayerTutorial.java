package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * 
 * @author Jordan (Plato)
 *
 */

public class NewPlayerTutorial extends Dialogue {
    private int npcId;
	
	@Override
	public void start() {
		 npcId = (int) parameters[0];
	        sendNPCDialogue(npcId, 9753, "You're alive?");
		stage = 2;
	}
	
	/**
	 * Second Stage of Tutorial.
	 */

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			sendPlayerDialogue(9827,"Urghh! My head what happened?");
			stage = 3;
		}
		
		/**
		 * Third Stage of Tutorial.
		 */
		
		else if (stage == 3) {
			sendNPCDialogue(npcId, 9827,"Don't you remember anything about the great war?");
			stage = 4;
		}
		
		/**
		 * Fourth Stage of Tutorial.
		 */
		
		else if (stage == 4) {
			sendPlayerDialogue(9827,"Urmm not really, should I?");
			stage = 5;
		}
		
		/**
		 * Fifth Stage of Tutorial.
		 */
		
		else if (stage == 5) {
			sendNPCDialogue(npcId, 9827,"You fought in the great war against the gods",
						 "You fought with great strength and honor",
						 "You eventually won but we had to teleport you to here ");
			stage = 6;
		}
		
		/**
		 * Sixth Stage of Tutorial.
		 */
		
		else if (stage == 6) {
			sendNPCDialogue(npcId, 9827,"Your wounds where so fatal we thought we where going to lose you!",
					 "Our finest medics were able to patch up your wounds before they got the best of you"
					 );
			stage = 7;
		}
		
		/**
		 * The Seventh Stage of Tutorial.
		 */
		
		else if (stage == 7) {
			sendPlayerDialogue(9830, "That explains the weakness.");
			stage = 8;
		}
		
		/**
		 * The Eighth Stage of Tutorial.
		 */
		
		else if (stage == 8) {
			sendNPCDialogue(npcId, 9827,"Since you don't remember anything, we will need to train you from the beginning again.");
			stage = 9;
		}
		
		/**
		 * The Ninth Stage of Tutorial.
		 */
		
		else if (stage == 9) {
			sendNPCDialogue(npcId, 9827,"Our home is located at the grand exchange",
						 "From here you can train thieving and use our shops",
						 "Thieving is a great way to start making money where");
			stage = 23;
		}
		
		else if (stage == 23) {
			sendNPCDialogue(npcId, 9827,"You can sell your loot to the general store!",
						 "The quest tab has loads of teleports which will make",
						 "Skilling and teleporting to monsters easier!");
			stage = 24;
		}
		
		else if (stage == 24) {
			sendNPCDialogue(npcId, 9827,"To train summoning you need to collect your own charms",
						 "which are dropped by monsters around " + Constants.SERVER_NAME,
						 "Most skills work just like they do on RuneScape");
			stage = 25;
		}
		
		else if (stage == 25) {
			sendNPCDialogue(npcId, 9827,"Now its time to select your class",
						 "We have an iron man account where you cant trade or use shops!",
						 "We also have difficulty settings so you decide the XP you want! ");
			stage = 10;
		}
		
		/**
		 * The Tenth Stage of Tutorial.
		 */
		
		else if (stage == 10) {
			player.getInterfaceManager().sendInterfaces();
			player.closeInterfaces();
			player.starterstage = 2;
			player.getDialogueManager().startDialogue("StarterClass");
		}
		
		/**
		 * The Eleventh Stage of Tutorial.
		 */
		
		else if (stage == 11) {
			player.unlock();
			player.getInterfaceManager().sendInterfaces();
			player.closeInterfaces();
			player.welcomeInterface();
			}
		
		else if (stage == 12) {
			player.getHintIconsManager().removeUnsavedHintIcon();
			sendDialogue("Need access to new items? Go venture to the shops!",
						 "We can add more to your request.");
			stage = 13;
		}
		else if (stage == 13) {
			sendDialogue("To start off making money, I suggest you start",
						 "thieving, where you can loot many items",
						 "ranging from crap to valuable.");
			stage = 14;
		}
		else if (stage == 14) {
			sendDialogue("You can sell any of your loots from skilling,",
						 "pvm, pking, or anything to the general store.",
						 "This is how you make your money.");
			stage = 15;
		}
		else if (stage == 15) {
			sendDialogue("To begin training, it is suggested to go",
						 "to the Rock Crabs. To get around " + Constants.SERVER_NAME,
						 "use the player tab or speak to the npcs at home in the bank.");
			stage = 9;
		}
		}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
