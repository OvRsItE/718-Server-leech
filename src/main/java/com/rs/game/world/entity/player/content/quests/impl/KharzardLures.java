package com.rs.game.world.entity.player.content.quests.impl;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;

public class KharzardLures {
	
	public static void handleQuestStartInterface(final Player player) {
		player.getInterfaceManager().sendInterface(275);
		for (int i = 15; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendRunScript(1207, 3);
		player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to </col><col=660000>Bert</col> <col=330099>who is</col>");
		player.getPackets().sendIComponentText(275, 12, "<col=330099>In the house north of the</col> <col=660000>Pub</col> <col=330099>at Home.");
		player.getPackets().sendIComponentText(275, 13, "");
		player.getPackets().sendIComponentText(275, 14, player.getSkills().getLevel(Skills.THIEVING) >= 80 ?
													"<col=330099><str>I must have a Thieving level of atleast 80.</col>"
												:   "<col=330099>I must have a Thieving level of atleast 80. </col>");
		player.getPackets().sendIComponentText(275, 15, player.getSkills().getLevel(Skills.COOKING) >= 60 ?
													"<col=330099><str>I must have a Cooking level of atleast 60.</col>"
												:	"<col=330099>I must have a Cooking level of atleast 60.</col>");
	}
	
	public static void handleProgressQuest(final Player player) {
		player.startedAdventurerQuest = true;
		player.AdventurerQuestProgress++;
		player.getPackets().sendConfig(160, 1);
		player.getInterfaceManager().sendInterfaces();
		player.getPackets().sendUnlockIComponentOptionSlots(190, 15, 0, 201, 0, 1, 2, 3);
	}
	
	public static void handleProgressQuestInterface(final Player player) {
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendRunScript(1207, 10);
		player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col><col=660000>Bert</col> <col=330099>who is</col>");
		player.getPackets().sendIComponentText(275, 12, "<str><col=330099>In the house north of the</col> <col=660000>Pub</col> <col=330099>at Home.");
		player.getPackets().sendIComponentText(275, 13, "");
		player.getPackets().sendIComponentText(275, 14, "<col=330099>I need to go see if the</col> <col=660000>Captain</col>");
		player.getPackets().sendIComponentText(275, 15, "<col=330099>Would listen to what i have to say."); 
if (player.AdventurerQuestProgress == 19) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>In the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I must defeat the Kazhard General in a fight"); 
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>Where i use no armour or weapons.");
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I must go back to Bert and tell him that"); 
			player.getPackets().sendIComponentText(275, 18, "<col=330099>The Kazhard General is defeated.");
			
		}else
		if (player.AdventurerQuestProgress == 17 || player.AdventurerQuestProgress == 18) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>In the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I must go find the General somewhere at the"); 
			player.getPackets().sendIComponentText(275, 15, "<str><col=660000>Kharzard's fight arena</col> <col=330099>and find a solution.");
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I must defeat the Kazhard General in a fight"); 
			player.getPackets().sendIComponentText(275, 18, "<col=330099>Where i use no armour or weapons.");
			
		}else
		if (player.AdventurerQuestProgress == 16) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I must hear what <col=660000>Bert</col> <col=330099>have to say about"); 
			player.getPackets().sendIComponentText(275, 15, "<col=330099><str>The information note that i gave him.");
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I must go find the General somewhere at the"); 
			player.getPackets().sendIComponentText(275, 18, "<col=660000>Kharzard's fight arena</col> <col=330099>and find a solution.");
			
		}else
		if (player.AdventurerQuestProgress == 15) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I must bring this note back to Bert."); 
			player.getPackets().sendIComponentText(275, 15, "");
			player.getPackets().sendIComponentText(275, 16, "<col=330099>I must hear what <col=660000>Bert</col> <col=330099>have to say about");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>The information note that i gave him."); 
			player.getPackets().sendIComponentText(275, 18, "");
			}else
		if (player.AdventurerQuestProgress == 14) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I must get back to the <col=660000>Lazy Guard</col><col=330099> for more information."); 
			player.getPackets().sendIComponentText(275, 15, "");
			player.getPackets().sendIComponentText(275, 16, "<col=330099>I must bring this note back to Bert.");
			player.getPackets().sendIComponentText(275, 17, ""); 
			player.getPackets().sendIComponentText(275, 18, "");
			player.getPackets().sendIComponentText(275, 19, "");
			}else
		if (player.AdventurerQuestProgress == 13) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I should go warn the city about the strike"); 
			player.getPackets().sendIComponentText(275, 15, "<col=330099><str>That might happend soon.");
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I must get back to the <col=660000>Lazy Guard</col><col=330099> for more information."); 
			player.getPackets().sendIComponentText(275, 18, "");
			player.getPackets().sendIComponentText(275, 19, "");
			}else
		if (player.AdventurerQuestProgress == 12) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I have payed the <col=660000>Lazy Guard</col> <col=330099> for some usefull"); 
			player.getPackets().sendIComponentText(275, 15, "<col=330099><str>Information.");
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I should go warn the city about the strike"); 
			player.getPackets().sendIComponentText(275, 18, "<col=330099>That might happend soon.");
			//player.getPackets().sendIComponentText(275, 19, "<col=330099>I must get back to the <col=660000>Lazy Guard</col><col=330099> for more information.");
			}else
				if (player.AdventurerQuestProgress == 11) {
					
					player.getInterfaceManager().sendInterface(275);
					for (int i = 0; i < 300; i++) {
						player.getPackets().sendIComponentText(275, i, "");
					}
					player.getPackets().sendRunScript(1207, 10);
					player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
					player.getPackets().sendIComponentText(275, 10, "");
					player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
					player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
					player.getPackets().sendIComponentText(275, 13, "");
					player.getPackets().sendIComponentText(275, 14, "<col=330099><str>Angor</col> <col=330099>told me about a lazy guard at the <col=660000>Khazards</col><col=330099> camp."); 
					player.getPackets().sendIComponentText(275, 15, "<col=330099><str>I might be able to 'buy' some information from him.");
					player.getPackets().sendIComponentText(275, 16, "");
					player.getPackets().sendIComponentText(275, 17, "<col=330099>I should go warn the city about the strike"); 
					player.getPackets().sendIComponentText(275, 18, "<col=330099>That might happend soon!");
					player.getPackets().sendIComponentText(275, 19, "");
					player.getPackets().sendIComponentText(275, 20, "");
				}else
					if (player.AdventurerQuestProgress == 10) {
						
						player.getInterfaceManager().sendInterface(275);
						for (int i = 0; i < 300; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendRunScript(1207, 10);
						player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
						player.getPackets().sendIComponentText(275, 10, "");
						player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
						player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
						player.getPackets().sendIComponentText(275, 13, "");
						player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I helped <col=660000>Angor</col> <col=330099>out of the Khazard jail he told"); 
						player.getPackets().sendIComponentText(275, 15, "<col=330099><str>Me to meet him at the <col=660000>Captains</col><col=330099> house.");
						player.getPackets().sendIComponentText(275, 16, "");
						player.getPackets().sendIComponentText(275, 17, "<col=660000>Angor</col> <col=330099>told me about a lazy guard at the <col=660000>Khazards</col><col=330099> camp."); 
						player.getPackets().sendIComponentText(275, 18, "<col=330099>I might be able to 'buy' some information from him.");
						player.getPackets().sendIComponentText(275, 19, "");
						player.getPackets().sendIComponentText(275, 20, "");
					}else
						if (player.AdventurerQuestProgress == 9) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>The <col=660000>Captain</col> <col=330099>said that he got a insider at the"); 
			player.getPackets().sendIComponentText(275, 15, "<col=660000><str>Fight Arena<col=330099> that goes by the name</col><col=660000> Angor.</col>");
			player.getPackets().sendIComponentText(275, 16, "<col=330099><str>I should go tell <col=660000>Bert</col><col=330099> about this.");
			player.getPackets().sendIComponentText(275, 17, ""); 
			player.getPackets().sendIComponentText(275, 18, "<col=330099>I helped <col=660000>Angor</col> <col=330099>out of the Khazard jail he told");
			player.getPackets().sendIComponentText(275, 19, "<col=330099>Me to meet him at the <col=660000>Captains</col><col=330099> house.");
			player.getPackets().sendIComponentText(275, 20, "");
			
			player.getPackets().sendIComponentText(275, 27, "");
			player.getPackets().sendIComponentText(275, 28, "");
				}else
					if (player.AdventurerQuestProgress == 8) {
						
						player.getInterfaceManager().sendInterface(275);
						for (int i = 0; i < 300; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
						player.getPackets().sendIComponentText(275, 10, "");
						player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
						player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
						player.getPackets().sendIComponentText(275, 13, "");
						player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I must somehow give the</col> <col=660000>Beer</col> <col=330099>that</col> <col=660000>Bert"); 
						player.getPackets().sendIComponentText(275, 15, "<col=330099><str>Gave me to the <col=660000>Captain.");
						player.getPackets().sendIComponentText(275, 16, "");
						player.getPackets().sendIComponentText(275, 17, "<col=330099>The <col=660000>Captain</col> <col=330099>said that he got a insider at the"); 
						player.getPackets().sendIComponentText(275, 18, "<col=660000>Fight Arena<col=330099> that goes by the name</col><col=660000> Angor.</col>");
						player.getPackets().sendIComponentText(275, 19, "<col=330099><str>I should go tell <col=660000>Bert</col><col=330099> about this.");
					}else
					if (player.AdventurerQuestProgress == 7) {
						
						player.getInterfaceManager().sendInterface(275);
						for (int i = 0; i < 300; i++) {
							player.getPackets().sendIComponentText(275, i, "");
						}
						player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
						player.getPackets().sendIComponentText(275, 10, "");
						player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
						player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
						player.getPackets().sendIComponentText(275, 13, "");
						player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I must somehow give the</col> <col=660000>Beer</col> <col=330099>that</col> <col=660000>Bert"); 
						player.getPackets().sendIComponentText(275, 15, "<col=330099><str>Gave me to the <col=660000>Captain.");
						player.getPackets().sendIComponentText(275, 16, "");
						player.getPackets().sendIComponentText(275, 17, "<col=330099>The <col=660000>Captain</col> <col=330099>said that he got a insider at the"); 
						player.getPackets().sendIComponentText(275, 18, "<col=660000>Fight Arena<col=330099> that goes by the name</col><col=660000> Angor.</col>");
						player.getPackets().sendIComponentText(275, 19, "<col=330099>I should go tell <col=660000>Bert</col><col=330099> about this.");
						
					}else
				
		if (player.AdventurerQuestProgress == 3) {
			
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to </col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>in the house north of the</col> <col=660000>Pub</col> <col=330099>at home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I need to go back and tell</col> <col=660000>Bert</col><col=330099> that"); 
			player.getPackets().sendIComponentText(275, 15, "<col=330099><str>The</col> <col=660000>Captain</col> <col=330099>knew that he sent me.</col>");
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I must somehow give the</col> <col=660000>Beer</col> <col=330099>that</col> <col=660000>Bert"); 
			player.getPackets().sendIComponentText(275, 18, "<col=330099>Gave me to the <col=660000>Captain.");
			player.getPackets().sendIComponentText(275, 19, "");
		}else
		if (player.AdventurerQuestProgress == 2) {
			player.getInterfaceManager().sendInterface(275);
			for (int i = 0; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
			player.getPackets().sendRunScript(1207, 10);
			player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to</col> <col=660000>Bert</col> <col=330099>who is</col>");
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>In the house north of the</col> <col=660000>Pub</col> <col=330099>at Home.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "<col=330099><str>I need to go see if the</col> <col=660000>Captain</col>");
			player.getPackets().sendIComponentText(275, 15, "<col=330099><str>Would listen to what i have to say.");
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I need to go back and tell</col> <col=660000>Bert</col><col=330099> that"); 
			player.getPackets().sendIComponentText(275, 18, "<col=330099>The</col> <col=660000>Captain</col> <col=330099>knew that he sent me.</col>");
		} 
		
		
		
		
		
	}
	
	public static void handleQuestComplete(final Player player) {
		player.startedAdventurerQuest = false;
		player.finishedAdventurerQuest = true;
		player.questPoints += 1;
		player.setSkillPoints(player.getSkillPoints() + 100);
		if (player.getInventory().getFreeSlots() < 1) {
			player.getBank().addItem(989, 3, true);
			player.getPackets().sendGameMessage("You do not have enough inventory space. Your reward has been sent to your bank.");
		} else {
			player.getInventory().addItem(990, 3);
		}
		player.getPackets().sendConfig(160, 2);
		player.getPackets().sendConfig(101, player.questPoints); // Quest Points
		player.getInterfaceManager().sendInterfaces();
		player.getPackets().sendUnlockIComponentOptionSlots(190, 15, 0, 201, 0, 1, 2, 3);
	}
	
	public static void handleQuestCompletionTabInterface(final Player player) {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendRunScript(1207, 12);
		player.getPackets().sendIComponentText(275, 1, "Threath Of Kharzard");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<str>I've defeated the Kazhard General and</col>");
		player.getPackets().sendIComponentText(275, 12, "<str>Prevented a strike on the town!");
		player.getPackets().sendIComponentText(275, 13, "");
		player.getPackets().sendIComponentText(275, 14, "");
		player.getPackets().sendIComponentText(275, 15, "<str>Bert thanked me for helping him and</col>");
		player.getPackets().sendIComponentText(275, 16, "<str>Rewarded me for all i did for him.</col>");
		player.getPackets().sendIComponentText(275, 17, "");
		player.getPackets().sendIComponentText(275, 18, "<col=660000>QUEST COMPLETE</col>");
		for (int i = 19; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}	
	
	public static void handleQuestCompleteInterface(final Player player) {
		player.getInterfaceManager().sendInterface(277);
		player.getPackets().sendIComponentText(277, 4, "You have completed The Kazhard Lures.");
		player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
		player.getPackets().sendIComponentText(277, 9, "You are awarded:");
		player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
		player.getPackets().sendIComponentText(277, 11, "100 Skill Pointss");
		player.getPackets().sendIComponentText(277, 12, "Access to an upgraded Crystal Chest.");
		player.getPackets().sendIComponentText(277, 13, "Two spins on the Squeal of Fortune");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, 989, 3);
		player.getPackets().sendGameMessage("");
		player.getPackets().sendGameMessage("Congratulations! You have completed the Kazhard Lures!");
		}
	}