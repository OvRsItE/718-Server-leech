package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Timer;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * 
 * @author Muda / Ability to use better Ckey chest
 *
 */

public class AdventurerQuest extends Dialogue {

	private int npcId = 3108;
	
	@Override
	public void start() {
		if (player.finishedAdventurerQuest) {
			sendNPCDialogue(npcId, HAPPY, "We're all sleeping tight thanks to you!!");
			stage = 98;
		}else
		if (player.AdventurerQuestProgress == 19) {
			sendPlayerDialogue(NORMAL, "Bert!! I defeated the General!!");
			stage = 105;
		}else
		if (player.AdventurerQuestProgress == 16) {
				sendNPCDialogue(npcId, NORMAL, "Did you speak with the General yet?");
				stage = 99;
		}else
		if (player.AdventurerQuestProgress == 15) {
			sendNPCDialogue(npcId, NONONO_FACE, "This is terrifying!!!");
			stage = 85;
		}else
		if (player.AdventurerQuestProgress == 14) {
			if (player.getInventory().containsItem(20670, 1)) {
				sendNPCDialogue(npcId, NORMAL, "Did you get any more information?");
				stage = 80;
			}else{
				sendPlayerDialogue(NORMAL, "I forgot the note with the information..");
				stage = 98;
			}
			
			
		}else 
			if (player.AdventurerQuestProgress == 13) {
			sendNPCDialogue(npcId, NORMAL, "You spoke with your information source yet?");
			stage = 99;
		}else
		if (player.AdventurerQuestProgress == 12) {
			sendPlayerDialogue(WORRIED, "There is something you must know!");
			stage = 70;
		}else
		if (player.AdventurerQuestProgress == 8) {
			sendNPCDialogue(npcId, SCARED, "Got any information from Angor yet?");
			stage = 95;
		}else
		if (player.AdventurerQuestProgress == 7) {
			sendPlayerDialogue(NORMAL, "It worked! He seems to be kind and friendly now! He also Told me about an insider..");
			stage = 60;
		}else
		if (player.AdventurerQuestProgress == 3) {
			sendNPCDialogue(npcId, THINKING, "Did it work?");
			stage = 50;
		}else
		if (player.AdventurerQuestProgress == 2) {
			sendNPCDialogue(npcId, HAPPY, "Did you speak with him?");
			stage = 30;
		} else 
		if (player.AdventurerQuestProgress == 1) {
			sendNPCDialogue(npcId, ASKING_FACE, "You spoke to him yet?");
			stage = 99;
		} else 
		if (!player.startedAdventurerQuest){
			sendNPCDialogue(npcId, ANNOYED,  "Hello citizen of " + Constants.SERVER_NAME + "! What can i do for you?");
			stage = 0;
		}
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
			if (stage == 0) {
					sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Why do you look so annoyed?", "Who are you?", "Nevermind.");
					stage = 1;
			}else
			if (stage == 1) {
					if(componentId == OPTION_1) {
						sendPlayerDialogue(ASKING_FACE, "Why do you look to be so annoyed?");
						stage = 2;
					} else
					if(componentId == OPTION_2) {
						sendPlayerDialogue(ASKING_FACE, "Who are you?");
						stage = 3;
					} else
					if(componentId == OPTION_3) {
						end();
					}
			}else
			if (stage == 2) {
					sendNPCDialogue(npcId, WORRIED, "I feel like the Khazard kingdom is up to something, "
							+ "						i have Been trying to get in touch with the Guard Captain "
							+ "						but he Is busy in the pub.");
						stage = 4;
			}else
			if (stage == 4) {
					sendNPCDialogue(npcId, SAD, "We never know when they are going to hit, and aslong as The Captain is busy doing what he 'loves' to we might Never figure what they are up to.");
						stage = 5;
						
			}else
			if (stage == 5) {
					sendPlayerDialogue(CONFUSED, "Have you tried telling the Captain that they are up to Something?");
						stage = 6;
			}else
			if (stage == 6) {
					sendNPCDialogue(npcId, SHAKING_NO_FACE, "Indeed with no luck, he says that they wont attack us Aslong as"
							+ "								 he is in command.");
						stage = 7;
			}else
			if (stage == 7) {
					sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Help Bert.", "I am busy.");
						stage = 8;
			}else
			if (stage == 8) {
					if (componentId == OPTION_1) {
						sendPlayerDialogue(HAPPY, "I would love to help you find out what they are up to.");
						KharzardLures.handleProgressQuest(player);
						stage = 9;
					}else
					if (componentId == OPTION_2) {
						sendPlayerDialogue(NORMAL, "I am very busy at the moment, sorry.");
						stage = 98;
					}
			} else
			if (stage == 9) {
						sendNPCDialogue(npcId, HAPPY, "Thank you so much!! It means the world to me! You might Want to talk to the Captain, he might listen to what you Have to say when you are a civilian.");
						stage = 10;
			}else
			if (stage == 10) {
					sendNPCDialogue(npcId, NORMAL, "You will most likely find him in the bar.");
					stage = 98;
					
			}else
			if (stage == 99) {
					sendPlayerDialogue(NORMAL, "No, not yet.");
					stage = 98;
			}else 
			if (stage == 98) {
					end();
			}else
			if (stage == 30) {
				sendPlayerDialogue(NORMAL, "Sort of... He knew you sent me and didnt want to speak Anymore he started yelling your name, i assume you are in Trouble?");
				stage = 31;
			}else
			if (stage == 31) {
				sendNPCDialogue(npcId, SAD, "I have not been through this before, we must fix this Somehow!");
				stage = 32;
			}else
			if (stage == 32) {
				sendNPCDialogue(npcId, SAD, "What can we do??? I have not been in in kind of trouble Before!");
				stage = 33;
			}else
			if (stage == 33) {
				sendPlayerDialogue(HAPPY, "I think i know how we can deal with this!");
				stage = 34;
			} else
			if (stage == 34) {
				sendNPCDialogue(npcId, NORMAL, "How? What's your plan?");
				stage = 35;
			}else
			if (stage == 35) {
				sendPlayerDialogue(QUESTION, "Well, he likes to drink right?");
				stage = 36;
			}else
			if (stage == 36) {
				sendNPCDialogue(npcId, NORMAL, "That's what he does most of the time..");
				stage = 37;
			}else
			if (stage == 37) {
				sendPlayerDialogue(NORMAL, "Here is what i will try to do.. I will try go speak to him Again"
						+ "					  But this time over a drink, or to say drinks! That Should make him"
						+ "					  Forget most of what happend through The whole day!");
				stage = 38;
			}else
			if (stage == 38) {
				sendNPCDialogue(npcId, HAPPY, "That's a brilliant idea! I think it will work! I have this old Quality Beer!"
						+ "					 Give it to him as the last one! It will make Him forget everything he told us!");
				stage = 39;
				
			}else 
			if (stage == 39) {
				for (NPC npc : World.getNPCs()) {
				    if (npc.getId() == npcId) {
				 npc.animate(new Animation(833));
				 KharzardLures.handleProgressQuest(player);
				 player.getInventory().addItem(3803, 1);
				 sendItemDialogue(3803, "Bert gives you the Beer.");
				 stage = 40;
				    }
				}
			}else
			if (stage == 40) {
				sendPlayerDialogue(NORMAL, "This better works!");
				stage = 98;
			}else
			if (stage == 50) {
				sendPlayerDialogue(NORMAL, "No, havent visited him yet.");	
				stage = 98;
			}else
			if (stage == 60) {
				sendNPCDialogue(npcId, CONFUSED, "An insider??");
				stage = 61;
			}else
			if (stage == 61) {
				sendPlayerDialogue(NORMAL, "Yeah.. Appearently he have someone in the Kharzard Camp that informs him of what they are up to..");
				stage = 62;
			}else
			if (stage == 62) {
				sendPlayerDialogue(THINKING, "I asked him if hes insider told him about anything Suspicious was going on lately..");
				stage = 63;
			}else
			if (stage == 63) {
				sendNPCDialogue(npcId, QUESTION, "What did he answer to that?");
				stage = 64;
			}else
			if (stage == 64) {
				sendPlayerDialogue(NORMAL, "That he haven't heard from him for a while now, so i Asked for the insiders name and he told me that hes name Is Angor..");
				stage = 65;
			}else
			if (stage == 65) {
				sendPlayerDialogue(NORMAL, "He told me to go look for him and see if he had any Information");
				stage = 66;
			}else
			if (stage == 66) {
				sendNPCDialogue(npcId, BLANK_FACE, "I never knew that.. They are running such a big risk.");
				stage = 67;
			}else
			if (stage == 67) {
				sendNPCDialogue(npcId, SAD, "How can he let Angor do that! The Khazards will figure What they are up to most likely!!");
				stage = 68;
			}else
			if (stage == 68) {
				sendNPCDialogue(npcId, SCARED_FACE, "You should get going now before it's too late!");
				stage = 69;
			}else
			if (stage == 69) {
				sendPlayerDialogue(NORMAL, "Yes sure! The captain told me where to find him!");
				KharzardLures.handleProgressQuest(player);
				stage = 98;
			}else
			if (stage == 95) {
				sendPlayerDialogue(NORMAL, "No.. not yet.");
				stage = 98;
			}else
			if (stage == 70) {
				sendNPCDialogue(npcId, NORMAL, "What? you look so worried..");
				stage = 71;
			}else
			if (stage == 71) {
				sendPlayerDialogue(NORMAL, "I just found out, that the Khazards army is planning an Attack!");
				stage = 72;
			}else
			if (stage == 72) {
				sendNPCDialogue(npcId, NORMAL, "Do you know when?");
				stage = 73;
			}else
			if (stage == 73) {
				sendPlayerDialogue(NORMAL, "My information source, told me that its going down as Fast as possible!");
				stage = 74;
			}else
			if (stage == 74) {
				sendPlayerDialogue(NORMAL, "I will get specific informations later on, by my Information source.");
				stage = 75;
			}else
			if (stage == 75) {
				sendNPCDialogue(npcId, NORMAL, "Keep me updated! I think we should not tell anyone about This since it could cause panic!");
				stage = 76;
			}else
			if (stage == 76) {
				sendNPCDialogue(npcId, NORMAL, "Or even make the Karzhards change their plans!");
				stage = 77;
			}else
			if (stage == 77) {
				KharzardLures.handleProgressQuest(player);
				sendPlayerDialogue(NORMAL, "Sounds like a good idea..");
				stage = 98;
			}else
			if (stage == 80) {
				sendPlayerDialogue(NORMAL, "I was over talking with the lazy Guard, he attended in a Meeting with the Khazards and noted everything they said Down on a note");
				stage = 81;
			}else
			if (stage == 81) {
				sendNPCDialogue(npcId, QUESTION, "Is the note trust worthy? It's made by one of the Khazard members..");
				stage = 82;
			}else
			if (stage == 82) {
				sendItemDialogue(20670, "You give the note to Bert.");
				player.getInventory().deleteItem(20670, 1);
				KharzardLures.handleProgressQuest(player);
				stage = 83;
			}else
			if (stage == 83) {
				sendNPCDialogue(npcId, NORMAL, "Okay, let me take a look at this.");
				stage = 84;
			}else
			if (stage == 84) {
				end();
				Timer.BertReading(player);
			}else
			if (stage == 85) {
				sendNPCDialogue(npcId, NONONO_FACE, "They are planning to strike our town and take it as their place!! We must do something!");
				stage = 86;
			}else
			if (stage == 86) {
				sendPlayerDialogue(NORMAL, "Did the note say when the strike was going to take place?");
				stage = 87;
			}else
			if (stage == 87) {
				sendNPCDialogue(npcId, SCARED, "You wouldn't believe it.. But it's happening tonight!");
				stage = 88;
			}else
			if (stage == 88) {
				sendPlayerDialogue(NORMAL, "Can't we go talk them from doing it??");
				stage = 89;
			}else
			if (stage == 89) {
				sendNPCDialogue(npcId, NORMAL, "No.. That is not an option but what we could do it is Ambush their general");
				stage = 90;
			}else
			if (stage == 90) {
				sendPlayerDialogue(NORMAL, "As in kill him?");
				stage = 91;
			}else
			if (stage == 91) {
				sendNPCDialogue(npcId, SCARED, "I hate to say this, but yes.. Thats our only option.");
				stage = 92;
			}else
			if (stage == 92) {
				sendPlayerDialogue(NORMAL, "I will go give their general an offer if he wont take it i will Challenge him to a battle.");
				stage = 93;
			}else
			if (stage == 93) {
				KharzardLures.handleProgressQuest(player);
				sendNPCDialogue(npcId, ANNOYED, "You must take care!");
				stage = 98;
			}else
			if (stage == 105) {
				sendNPCDialogue(npcId, HAPPY, "So what? Now they wont attack?");
				stage = 106;
			}else
			if (stage == 106) {
				sendPlayerDialogue(NORMAL, "They wont, they will probably be investegating what happend to him..");
				stage = 107;
			}else
			if (stage == 107) {
				sendNPCDialogue(npcId, HAPPY, "HOLY MOLY!! I wouldnt have managed to save the city without you warrior!");
				stage = 108;
			}else
			if (stage == 108) {
				sendNPCDialogue(npcId, HAPPY, "I feel the need to reward you for your effort!!");
				stage = 109;
			}else
			if (stage == 109) {
				end();
				KharzardLures.handleQuestComplete(player);
				KharzardLures.handleQuestCompleteInterface(player);
			}
			
				
		
				
			
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
	
}