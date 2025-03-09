package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Timer;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;
import com.rs.game.world.entity.player.cutscenes.impl.MerryChristmas;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

/**
 * 
 * @author Muda / Ability to use better Ckey chest
 *
 */

public class SantaClaus extends Dialogue {

	private int npcId = 1552;
	
	@Override
	public void start() {
		if (!player.startedEvent) {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "What are you doing here?", "Why is there snow everywhere?", "Nevermind");
		stage = 1;
		} else 
		if (player.foundImps == 3 && !player.finishedEvent){
			sendPlayerDialogue(HAPPY, "I have found all the imps!");
			stage = 30;
		} else
		if (player.finishedEvent) {
			sendNPCDialogue(npcId, HAPPY,  "Thank you so much for helping me!");
			stage = 100;
		} else
		if (player.eventProgress == 3 && !player.finishedEvent) {
			sendNPCDialogue(npcId, HAPPY, "I still got a gift for you, here you go!");
			stage = 33;
		} else {
			sendNPCDialogue(npcId, QUESTION, "Have you found them yet?");
			stage = 20;
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				sendPlayerDialogue(QUESTION, "What are you doing here?");
				stage = 2;
			} else
			if (componentId == OPTION_2) {
				sendPlayerDialogue(QUESTION, "Why is there snow everywhere?");
				stage = 3;	
			}else
			if (componentId == OPTION_3) {
				sendPlayerDialogue(NORMAL, "Nevermind Santa claus'!");
				stage = 100;
			}
		}else 
		if (stage == 2) {
			sendNPCDialogue(npcId, SAD, "As you probably know, Christmas is ahead of us and im Behind in schedule! i have lost the 3 most important snow Imps, without them i wont be able send gifts out for the Christmas night!");
			stage = 10;
		}else
		if (stage == 3) {
			player.getCutscenesManager().play(new MerryChristmas());
			sendNPCDialogue(npcId, HAPPY, "As you can see, christmas is a peacefull time at the year, and so is the snow that usually comes with it! ");
			stage = 100;
		}else
		if (stage == 100) {
			end();
		}else
		if (stage == 10) {
			sendNPCDialogue(npcId, SAD, "I decided to visit the home of " + Constants.SERVER_NAME + " since i know that It's a very helpfull community! and look for some help!");
			stage = 11;
		}else
		if (stage == 11) {
			sendOptionsDialogue("Help Santa Claus?", "Yes", "No");
			stage = 12;
		}else
		if (stage == 12) {
			if (componentId == OPTION_1) {
				player.startedEvent = true;
				player.eventProgress = 1;
				sendPlayerDialogue(NORMAL, "I will help you find your snow imps!");
				stage = 13;
			}else
			if (componentId == OPTION_2) {
				sendPlayerDialogue(NORMAL, "Sorry, i can't help you.");
				stage = 100;
			}
			
		}else
		if (stage == 13) {
			sendNPCDialogue(npcId, HAPPY, "I am so glad that you will put your time into helping me! They could be in any city but i got this tracker device That can be used to track what city they are in");
			stage = 14;
		}else
		if (stage == 14) {
			sendNPCDialogue(npcId, HAPPY, "You just have to look through the city!");
			stage = 15;
		}else
		if (stage == 15) {
			player.eventProgress = 2;
			sendItemDialogue(21819, "The santa claus' gives you a Strange device.");
			player.getInventory().addItem(21819, 1);
			stage = 16;
		}else
		if (stage == 16) {
			sendNPCDialogue(npcId, NORMAL, "You use it by simply clicking on it, and it will tell you what City to look in! ");
			stage = 100;
		}else
		if (stage == 20) {
			sendPlayerDialogue(NORMAL, "No.. Not yet.");
			stage = 100;
		}else
		if (stage == 30) {
			sendNPCDialogue(npcId, HAPPY, "Indeed you have! What a hero you are!! Thank you so Much for helping me out and being a part of this awesome Community! ");
			stage = 31;	
		}else
		if (stage == 31) {
			sendNPCDialogue(npcId, HAPPY, "I have a reward for you!");
			stage = 32;
		}else
		if (stage == 32) {
			player.eventProgress = 3;
			player.getInventory().addItem(14595, 1);
			player.getInventory().addItem(14602, 1);
			player.getInventory().addItem(14605, 1);
			player.getInventory().addItem(14603, 1);
			sendItemDialogue(14595, "Santa claus' gives you a <col=8B0000>Santa Costume</col> set s a reward.");
			stage = 33;
		}else
		if (stage == 33) {
			sendNPCDialogue(npcId, HAPPY, "And because you helped me get back my snow imps, im giving you a exclusive early christmas present!");
			stage = 34;
		}else
		if (stage == 34) {
			player.eventProgress = 4;
			player.getInventory().addItem(6542, 1);
			sendItemDialogue(6542, "Best of lucks on opening this!");
			player.finishedEvent = true;
			stage = 35;
		}else
		if (stage == 35) {
			sendNPCDialogue(npcId, HAPPY, "And again! Thank you so much for you effort! I knew that this community was not to doubt!");
			stage = 100;
		}
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
}