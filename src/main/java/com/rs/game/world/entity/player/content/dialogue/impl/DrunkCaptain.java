package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;
import com.rs.game.world.entity.updating.impl.Animation;


public class DrunkCaptain extends Dialogue {

	private int npcId = 3109;
	@Override
	public void start() {
			
			if (player.AdventurerQuestProgress == 7) {
				sendNPCDialogue(npcId, DAZED, "Ayee mate! You found him yet??");
				stage = 60;
			}else
			if (player.AdventurerQuestProgress == 6) {
				sendNPCDialogue(npcId, DAZED, "OUCH... THAT HIT ME!!!");
				stage = 50;
			}else
			if (player.AdventurerQuestProgress == 5) {
				sendNPCDialogue(npcId, DAZED, "");
				sendPlayerDialogue(DAZED, "Ready when you are!");
				stage = 42;
			}else
			if (player.AdventurerQuestProgress == 4) {
				sendNPCDialogue(npcId, DAZED, "*BUURGHH* That was like water *HIGH* One more?");
				stage = 30; 
			}else 
			if (player.AdventurerQuestProgress == 3) {
				if (player.getInventory().containsItem(3803, 1)) {
					sendNPCDialogue(npcId, DAZED, "MORE BEEERRRRR!!!!");
					stage = 20;
				} else {
					sendNPCDialogue(npcId, DAZED, "I DONT WANT TO TALK TO YOU!!");
					stage = 8;
				}
			} else 
			if (player.AdventurerQuestProgress == 2) {
				sendNPCDialogue(npcId, DAZED, "I DONT WANT TO TALK TO YOU!");
				stage = 8;
			}else
			if (player.AdventurerQuestProgress == 1) {
				sendNPCDialogue(npcId, DAZED, "Hyyyyyyeeee!! *SIGH* ..... Anthing i can do for you?");
				stage = 1;
			} else {
				sendNPCDialogue(npcId, DAZED, "Drink beer with me! *SIGH*.. ON ME!!");
				//player.getInventory().addItem(1917, 1);
				stage = 8;
			}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			sendPlayerDialogue(UPSET_FACE, "Can't you be serious for a moment?");
			stage = 2;
		}else
		if (stage == 2) {
			sendNPCDialogue(npcId, DAZED, "UGHHHHHH' Serious??? Are you serious??");
			stage = 3;
		}else
		if (stage == 3) {
			sendNPCDialogue(npcId, DAZED, "Have a drink with me! *HIG*");
			stage = 4;
		}else
		if (stage == 4) {
			sendPlayerDialogue(NORMAL, "Will that make you talk about the Kharzards?");
			stage = 5;
		}else
		if (stage == 5) {
			sendNPCDialogue(npcId, DAZED, "Ehhh? I remember being asked that today!");
			stage = 6;
		}else
		if (stage == 6) {
			sendNPCDialogue(npcId, DAZED, "BERT!!! Bert sent you to do hes job!!");
			stage = 7;
		} else
		if (stage == 7) {
			sendNPCDialogue(npcId, NONONO_FACE, "IM NOT TALKING TO YOU ANYMORE!! BERTT!!!!!");
			KharzardLures.handleProgressQuest(player);
			stage = 8;
			
		}else 
		if (stage == 8) {
			end();
		}else
		if (stage == 20) {
			sendPlayerDialogue(HAPPY, "Ey' Captain! That's just what i was thinking!");
			stage = 21;
		}else
		if (stage == 21) {
			sendNPCDialogue(npcId, DAZED, "AIIIGHT!! First one on me!");
			stage = 22;
		} else
		if (stage == 22) {
			sendItemDialogue(1917, "The Captain gives you a beer.");
			player.getInventory().addItem(1917, 1);
			stage = 23;
		}else
		if (stage == 23) {
			sendPlayerDialogue(HAPPY, "Thank you Captain!");
			stage = 24;
		}else
		if (stage == 24) {
			sendNPCDialogue(npcId, DAZED, "I wanna drink you under the table scrub!! *SIGH*");
			stage = 25;
		}else
		if (stage == 25) {
			sendPlayerDialogue(NORMAL, "Many legends have tried, but never succed!");
			stage = 26;
		}else
		if (stage == 26) {
			sendNPCDialogue(npcId, DAZED, "EHHHH! NO ONE can beat me in a drinking contest!");
			stage = 27;
		}else
		if (stage == 27) {
			sendPlayerDialogue(NORMAL, "I guess we are about to see..");
			stage = 28;
		}else
		if (stage == 28) {
			sendNPCDialogue(npcId, DAZED, "4... 2... 1... GOO!!");
			stage = 29;
		}else
		if (stage == 29) {
			for (NPC npc : World.getNPCs()) {
			    if (npc.getId() == npcId) {
			    player.getInventory().deleteItem(1917, 1);
			    npc.animate(new Animation(1327));
			    player.animate(new Animation(1327));
			    KharzardLures.handleProgressQuest(player);
			    player.getDialogueManager().startDialogue("DrunkCaptain");
			    }
			}
			//end();
			//Timer.startDrinkingContest(player);
			
		}else
		if (stage == 30) {
			sendPlayerDialogue(DAZED, "UGH.. Yeah sure!");
			stage = 31;
		}else
		if (stage == 31) {
			sendNPCDialogue(npcId, DAZED, "ON MEE AGAIN!! ");
			stage = 32;
		}else
		if (stage == 32) {
			sendItemDialogue(1917, "The Captain gives you another beer.");
			player.getInventory().addItem(1917, 1);
			stage = 33;
		}else
		if (stage == 33) {
			sendNPCDialogue(npcId, DAZED, "READY WHEN YOU ARE!!");
			stage = 34;
		}
		if (stage == 34) {
			for (NPC npc : World.getNPCs()) {
			    if (npc.getId() == npcId) {
			    player.getInventory().deleteItem(1917, 1);
			    npc.animate(new Animation(1327));
			    player.animate(new Animation(1327));
			    stage = 35;
			    }
			}
		}else
		if (stage == 35) {
			sendNPCDialogue(npcId, DAZED, "I wont lie... Im starting to feel it.");
			stage = 36;
		}else
		if (stage ==36) {
			sendPlayerDialogue(DAZED, "I was wondering, If i could ask you something though?");
			stage = 37;
		}else
		if (stage == 37) {
			sendNPCDialogue(npcId, DAZED, "Yeah sure my drinking friend! *HIG* When im done Drinking you under the table!");
			stage = 38;
		}else
		if (stage == 38) {
			sendPlayerDialogue(DAZED, "THAT WONT HAPPEND!! This one is one me !");
			stage = 39;
		}else
		if (stage == 39) {
			sendNPCDialogue(npcId, DAZED, "*HIG* Soo nice of you!");
			stage = 40;
		}else
		if (stage == 40) {
			sendItemDialogue(3803, "You give the Beer from Bert to the Captain.");
			player.getInventory().deleteItem(3803, 1);
			KharzardLures.handleProgressQuest(player);
			player.animate(new Animation(833));
			stage = 41;
		}else
		if (stage == 41) {
			sendPlayerDialogue(DAZED, "Ready when you are!");
			stage = 42;
		}else
		if (stage == 42) {
			sendNPCDialogue(npcId, DAZED, "OKAY... 3.. 2.. 0.. Ready!!");
			stage = 43;
		}else
		if (stage == 43) {
			for (NPC npc : World.getNPCs()) {
			    if (npc.getId() == npcId) {
			    player.getInventory().deleteItem(1917, 1);
			    npc.animate(new Animation(1327));
			    player.animate(new Animation(1327));
			    KharzardLures.handleProgressQuest(player);
			    player.getDialogueManager().startDialogue("DrunkCaptain");
			    }
			}
		}else
		if (stage == 50) {
			sendNPCDialogue(npcId, DAZED, "WAUUUUUW... I can see atleast two of you now! *HIG*");
			stage = 51;
		}else
		if (stage == 51) {
			sendPlayerDialogue(NORMAL, "Let's talk about the Kharzards?");
			stage = 52;
		}else
		if (stage == 52) {
			sendNPCDialogue(npcId, DAZED, "BAHH.. They fear me!!");
			stage = 53;
		}else
		if (stage == 53) {
			sendNPCDialogue(npcId, DAZED, "I got an insider that tells me what they are up to! I got Eyes in their camp!");
			stage = 54;
		}else
		if (stage == 54) {
			sendPlayerDialogue(NORMAL, "What' have he been telling you? Because they are acting As if they are up to something..");
			stage = 55;
		}else
		if (stage == 55) {
			sendNPCDialogue(npcId, DAZED, "UHMM... Actually it's been a while since i've heard from Him..");
			stage = 56;
		}else
		if (stage == 56) {
			sendPlayerDialogue(NORMAL, "If you give me a name, i might be able to head over to Their camp and gather some information?");
			stage = 57;
		}else
		if (stage == 57) {
			sendNPCDialogue(npcId, DAZED, "Hes name is Angor.. I hope everything is well.");
			stage = 58;
		}else
		if (stage == 58) {
			sendPlayerDialogue(NORMAL, "Thanks Captain'!!");
			KharzardLures.handleProgressQuest(player);
			stage = 8;
		}else
		if (stage == 60) {
			sendPlayerDialogue(NORMAL, "No.. I havent yet.");
			stage = 8;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
	
}