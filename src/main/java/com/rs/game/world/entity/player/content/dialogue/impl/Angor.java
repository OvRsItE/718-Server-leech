package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;


public class Angor extends Dialogue {

	private int npcId = 259;
	
	@Override
	public void start() {
	if (player.AdventurerQuestProgress == 9) {
				sendNPCDialogue(npcId, HAPPY, "There comes the HEROE! Took you awhile to get here..");
				stage = 10;
	}else
	if (player.AdventurerQuestProgress == 8) {
		sendNPCDialogue(npcId, SCARED, "Who are you?");
		stage = 0;
			}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			sendPlayerDialogue(NORMAL, "The Captain sent me to find you, are you okay?");
			stage = 1;
		}else
		if (stage == 1) {
			sendNPCDialogue(npcId, NORMAL, "THANK GOD! I've been in here for a while now.. The Khazards are planning something against our city!");
			stage = 2;
		}else
		if (stage == 2) {
			sendPlayerDialogue(QUESTION, "Why did you end up here in jail?");
			stage = 3;
		}else
		if (stage == 3) {
			sendNPCDialogue(npcId, SAD_FACE, "I wish i knew why, they suddenly gathered around me and Arrested me.. I remember hearing them talking about Taking over our city");
			stage = 4;
		}else
		if (stage == 4) {
			sendPlayerDialogue(QUESTION, "You are saying 'them', can you more specific?");
			stage = 5;
		}else
		if (stage == 5) {
			sendNPCDialogue(npcId, SCARED, "We better get out of here right now before it's too late!! Meet me at the Captains house!");
			
			stage = 6;
		}else
		if (stage == 6) {
			end();
			KharzardLures.handleProgressQuest(player);
			for (NPC npc : World.getNPCs()) {
				 if (npc.getId() == 259) {
					 
					 npc.setNextPosition(new Position(2555,3079,1));
					 npc.setRespawnTask();
				  }
			}
			
		}else
		if (stage == 10) {
			sendPlayerDialogue(NORMAL, "No problems.. I just want to know what they are up to the Khazards..");
			stage = 11;
		}else
		if (stage == 11) {
			sendNPCDialogue(npcId, NORMAL, "My information is very limited, but i think i know someone That can tell us exactly what they are up to!");
			stage = 12;
		}else
		if (stage == 12) {
			sendNPCDialogue(npcId, NORMAL, "Before they jailed me, i noticed that they have a very Lazy guard.. I think you can make him talk for some Goods..");
			stage = 13;
		}else
		if (stage == 13) {
			sendPlayerDialogue(QUESTION, "What do you mean by goods?");
			stage = 14;
		}else
		if (stage == 14) {
			sendNPCDialogue(npcId, NORMAL, "Im saying if you pay him some gold, he will probably let us Know some worthy information.");
			stage = 15;
		}else
		if (stage == 15) {
			sendPlayerDialogue(NORMAL, "Mhm okay.. Do you think it will work?");
			stage = 16;
		}else
		if (stage == 16) {
			sendNPCDialogue(npcId, NORMAL, "Yeah i do, the Khazards barely gets payed.. I defenetly Think it will work!");
			stage = 17;
		}else
		if (stage == 17) {
			sendPlayerDialogue(NORMAL, "Okay, i better get going!");
			KharzardLures.handleProgressQuest(player);
			stage = 18;
		}else
		if (stage == 18) {
			sendNPCDialogue(npcId, NORMAL, "Yeah me to..");
			for (NPC npc : World.getNPCs()) {
				 if (npc.getId() == 259) {
					 
					 npc.setNextPosition(new Position(2598,3144,0));
					
				  }
			}
			end();
		}
	}
	

	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}
	}