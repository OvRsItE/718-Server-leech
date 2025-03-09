package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;

public class LazyGuard extends Dialogue {

	private int npcId = 7550;
	
	@Override
	public void start() {
		if (player.AdventurerQuestProgress == 14) {
			sendPlayerDialogue(NORMAL, "I think i should show this note Bert.. ");
			stage = 0;
		}
		if (player.AdventurerQuestProgress == 13) {
			sendNPCDialogue(npcId, WORRIED, "Just in time! I just got back from the meeting.");
			stage = 20;
		}else
		if (player.AdventurerQuestProgress == 12) {
				sendNPCDialogue(npcId, NORMAL, "The meeting have not started yet, you should warn your city.");
				stage = 0;
		}else
		if (player.AdventurerQuestProgress == 10) {
			sendNPCDialogue(npcId, NORMAL, "Hey there.. Im not in the mood to talk..");
			stage = 1;
		}else{
			sendNPCDialogue(npcId, NORMAL, "Im even too lazy to talk..");
			stage = 0;
		}
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			end();
		}else
		if (stage == 1) {
			sendPlayerDialogue(NORMAL, "What if i payed you for a single question?");
			stage = 2;
		}else
		if (stage == 2) {
			sendNPCDialogue(npcId, LAUGH, "HAHAHA!! We can only do this if you ask me more than One question..");
			stage = 3;
		}else
		if (stage == 3) {
			sendNPCDialogue(npcId, HAPPY, "I will be completly honest with you if you give me 100.000 Coins, you can ask all you want afterwards!");
			stage = 4;
		}else
		if (stage == 4) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Pay 100K.", "No thanks.");
			stage = 5;
		}else
		if (stage == 5) {
			if (componentId == OPTION_1) {
				if (player.getMoneyPouch().getCoinsAmount() >= 100000) {
					player.getMoneyPouch().sendDynamicInteraction(100000, true);
					sendPlayerDialogue(NORMAL, "There you go..");
					KharzardLures.handleProgressQuest(player);
					stage = 6;
				}else
				if (player.getInventory().containsItem(995, 100000)) {
					player.getInventory().deleteItem(995, 100000);
					sendPlayerDialogue(NORMAL, "There you go..");
					KharzardLures.handleProgressQuest(player);
					stage = 6;
				}else{
					sendNPCDialogue(npcId, NORMAL, "You dont have enough gold.");
					stage = 0;
				}
			}else if (componentId == OPTION_2) {
				end();
			}
		}else
		if (stage == 6) {
			sendNPCDialogue(npcId, NORMAL, "What is it that you would like to know?");
			stage = 7;
		}else
		if (stage == 7) {
			sendPlayerDialogue(NORMAL, "Well basically there is rumors going around in our city About...");
			stage = 8;
		}else
		if (stage == 8) {
			sendNPCDialogue(npcId, NORMAL, "I know where this is going...");
			stage = 9;
		}else
		if (stage == 9) {
			sendNPCDialogue(npcId, NORMAL, "The army is getting ready to gain their old home back! The Khazard general have spoken and said that its time For us to stand up and fight for what is ours!");
			stage = 10;
		}else
		if (stage == 10) {
			sendNPCDialogue(npcId, NORMAL, "He also said that the attack will happend soon..");
			stage = 11;
		}else
		if (stage == 11) {
			sendPlayerDialogue(NORMAL, "Is there no way to stop him? It will cause too much death! Tell me more!");
			stage = 12;
		}else
		if (stage == 12) {
			sendNPCDialogue(npcId, THINKING, "There is nothing you can do... And i have told you all Worth knowing.");
			stage = 13;
		}else
		if (stage == 13) {
			sendPlayerDialogue(ANGRY, "NO WAY I JUST PAYED YOU 100 THOUSAND GOLD!! For You to screw me like that!");
			stage = 14;
		}else
		if (stage == 14) {
			sendPlayerDialogue(ANGRY, "I need to know more before the strike happends!");
			stage = 15;
		}else
		if (stage == 15) {
			sendNPCDialogue(npcId, NORMAL, "There is a meeting going down later, im going to attend in It and i will note everything down for you.");
			stage = 16;
		}else
		if (stage == 16) {
			sendNPCDialogue(npcId, NORMAL, "In the mean time, go let your friends and familly know to Stay safe!");
			stage = 17;
		}else
		if (stage == 17) {
			KharzardLures.handleProgressQuest(player);
			sendPlayerDialogue(NORMAL, "I for sure will!");
			stage = 0;
		}else
		if (stage == 20) {
			sendPlayerDialogue(NORMAL, "What did you find out?");
			stage = 21;
		}else
		if (stage == 21) {
			sendNPCDialogue(npcId, WORRIED, "I noted eveything that was said down, you gave me a Fortune of gold! I wont forget that!");
			stage = 22;
		}else
		if (stage == 22) {
			sendItemDialogue(20670, "You recieve a note from the Lazy Guard.");
			player.getInventory().addItem(20670, 1);
			KharzardLures.handleProgressQuest(player);
			stage = 23;
		}else
		if (stage == 23) {
			sendNPCDialogue(npcId, NORMAL, "Everything you need to know is on that note.");
			stage = 24;
		}else
		if (stage == 24) {
			sendPlayerDialogue(NORMAL, "Thank you, i appreciate it!");
			stage = 0;
		}
	
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
	
}