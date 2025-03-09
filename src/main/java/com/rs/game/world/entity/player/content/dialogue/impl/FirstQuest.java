package com.rs.game.world.entity.player.content.dialogue.impl;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;


public class FirstQuest extends Dialogue {
	


	
	private int npcId = 747;
	
	
	@Override
	public void start() {
			
			if (player.finishedFirstQuest == false) { 
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "What are you doing here?", "Can i have some items?",
										   "You got any quests for me?", "Never mind.");
				stage = 1;
			} else if (player.finishedFirstQuest == true){
				sendNPCDialogue(npcId, HAPPY_FACE, "Thank you for all your effort!");
			}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		
		/**
		 * Sends the options to the players.
		 **/
			if (stage == 1) {
				if (componentId == OPTION_1) {
					sendPlayerDialogue(9829, "What are you doing here sir?");
					stage = 2;
				} else if (componentId == OPTION_2) {
					sendPlayerDialogue(9829, "Can i have some items? im pretty broke.");
					stage = 3;
				} else if (componentId == OPTION_3) {
					sendPlayerDialogue(9829, "You got any quests for me?");
					stage = 4;
				} else if (componentId == OPTION_4) {
					sendPlayerDialogue(9829, "Never mind..");
					stage = 5;
				}
			}
			
			/**
			 * Handles the stage "what are you doing here?"
			 **/
			
				else if (stage == 2) {
						sendNPCDialogue(npcId, UPSET_FACE, "Havent you heard?!?");
						stage = 10;
					}
				else if (stage == 10) {
						sendPlayerDialogue(9827, "Heard of what?");
						stage = 11;
				} else if (stage == 11) {
						sendNPCDialogue(npcId, THINKING_THEN_TALKING_FACE, "Lumbridge is in ruins! The dragons have opened a nest, and is rapidly",
																			"Taking control of the city! thousands have left their homes including me!");
						stage = 12;
				} else if (stage == 12) {
						sendPlayerDialogue(9775, "Haven't anyone tried fighitng back? i mean i have slain",
												 "Thousands of Dragons and i dont really see the problem?");
						stage = 13;
				} else if (stage == 13) {
						sendNPCDialogue(npcId, 9780, "The Dragon slayer of Lumbridge 'Sir Pale' was the one holding them back.",
													 "The last thing i saw him doing was to defend himself against the dragons!",
													 "I took no chances, and got out of the city immediatley..Here i am now.");
						stage = 14;
				} else if  (stage == 14) {
						sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Help Oziach", "Not Intrrested.");
				}
			
			/**
			 * Handles the stage of Can i have items?
			 **/
				else if (stage == 3) {
						sendNPCDialogue(npcId, SAD_FACE, "I perhaps more in need of some items.");
						stage = 5;
				} else if (stage == 5) {
					end();
				}
			
			/** 
			 *  Handles the stage of asking Can i have a Quest?
			 **/
			
				else if(stage == 4) {
					sendNPCDialogue(npcId, UNSURE_FACE, "With all that going down in Lumbridge, i think i could use",
														"Everybodys help.. But you will have to be carefull!");
					stage = 6;
				} 
		} 

	@Override
	public void finish() {

	}

}