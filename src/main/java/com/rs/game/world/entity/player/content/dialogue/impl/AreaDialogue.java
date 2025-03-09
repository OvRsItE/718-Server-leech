package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.QuestManager.Quests;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.activities.CastleWars;
import com.rs.game.world.entity.player.content.activities.RecipeforDisaster;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.controller.impl.FightCaves;
import com.rs.game.world.entity.player.controller.impl.FightKiln;
import com.rs.game.world.entity.player.controller.impl.NomadsRequiem;
import com.rs.game.world.entity.updating.impl.Graphics;

public class AreaDialogue extends Dialogue {


	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Where would you like to go?", "<shad=2EFEF7>Training Teleports</shad>", "<shad=0000ff>Skilling Teleports</shad>",
						"<shad=01DF01>Minigames Teleports</shad>", "<shad=FF00FF>Boss Teleports</shad>", "<shad=ff0000>PvP Teleports</shad>");
		
	}

	@Override
	public void run(int interfaceId, int componentId) {
 if (stage == 1) {
	 if (componentId == OPTION_1) {
			sendOptionsDialogue("Where would you like to go?", "Rock Crabs",
					"Yaks", "Living Rock Caverns", "Mature Grotworms");
			stage = 2;
	 } else if( componentId == OPTION_2) {
		 sendOptionsDialogue("Where would you like to go?", "Smithing", "Farming", "Summoning", "WoodCutting", 
					"More Options");
			stage = 11;
	 } else if( componentId == OPTION_3) {
			sendOptionsDialogue("would you like to go?",
					"SoulWars", "Clan Wars", "Duel Arena",
					"Castle Wars", "More Options");
			stage = 3;
	 } else if(componentId == OPTION_4) {
		 sendOptionsDialogue("Where would you like to go?",
					"Armadyl", "Zamorak", "Bandos", "Sara", "More Options");
			stage = 4;
	 } else if(componentId == OPTION_5) {
		 sendOptionsDialogue("Where would you like to go?",
					"Revenant Caves", "Magic Bank", "Multi Area PvP", "Wests PvP", "Dungeoneering PvP");
			stage = 6;
	 }
 } else if (stage == 9) {
			if (componentId == OPTION_1) {
				teleportPlayer(4608, 5061, 0);
			} else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, FightKiln.OUTSIDE);
 			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, FightCaves.OUTSIDE);
			}	else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3358, 6112, 0));
			}	else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Zombies", "Trivia", "Dominion Tower", "RuneSpan", "More Options");
				stage = 21;
			}
 } else if (stage == 22) {
		if (componentId == OPTION_1) {
			end();
			RecipeforDisaster.enterRfd(player);
		} else if (componentId == OPTION_2) {
			teleportPlayer(2843, 3535, 2);
		} else if (componentId == OPTION_3) {
			teleportPlayer(2877, 3566, 0);
		} else if (componentId == OPTION_4) {
			teleportPlayer(2662, 2654, 0);
		} else if (componentId == OPTION_5) {
			stage = 26;
		sendOptionsDialogue("Where would you like to go?", "Barrows", "Hunger Games", "Beginning Options");
		}
} else if(stage == 21) {
	if (componentId == OPTION_1) {
		teleportPlayer(3689, 3503, 0);
	} else if (componentId == OPTION_2) {
		teleportPlayer(2647, 9378, 0);
	} else if (componentId == OPTION_3) {
		teleportPlayer(3743, 6421, 0);
	} else if(componentId == OPTION_4) {
			player.sm("Disabled atm."); 
	} else if (componentId == OPTION_5) {
		stage = 22;
		sendOptionsDialogue("Where would you like to go?", "RFD", "Warrior's Guild", "Troll Invansion", "Pest Control",
				"More Options");
		}
} else if (stage == 11) {
	if (componentId == OPTION_1) {
		teleportPlayer(3277, 3186, 0);
	} else if (componentId == OPTION_2){
		teleportPlayer(2813, 3462, 0);
	} else if (componentId == OPTION_3){
		teleportPlayer(2926, 3444, 0);
	} else if (componentId == OPTION_4){
		teleportPlayer(2724, 3485, 0);
	} else if (componentId == OPTION_5){
		sendOptionsDialogue("Where would you like to go?", "Hunter", "Agility", "Runecrafting", "Mining", 
				"More Options");
		stage = 12;
		}
 } else if (stage == 12) {
		if (componentId == OPTION_1) {
			teleportPlayer(2570, 2916, 0);
		} else if (componentId == OPTION_2){
			sendOptionsDialogue("Where would you like to go?",
					"Gnome Agility", "Barbariang Agility", "Beggening Options");
				stage = 13;
		} else if (componentId == OPTION_3){
			sendOptionsDialogue("Where would you like to go?",
					"Air Altar", "Mind Altar", "Water Altar", "Earth Altar", "More Optiona");
				stage = 14;
		} else if (componentId == OPTION_4){
			teleportPlayer(3294, 3297, 0);
		} else if (componentId == OPTION_5){
			sendOptionsDialogue("Where would you like to go?", "Fishing", "Construction", "Dungeoneering", 
					"Beginning Options");
			stage = 23;
		}
 } else if (stage == 23) {
		if (componentId == OPTION_1) {
			teleportPlayer(2586, 3422, 0);
		} else if (componentId == OPTION_2) {
			teleportPlayer(3475, 9807, 0);
		} else if (componentId == OPTION_3) {
			teleportPlayer(3450, 3719, 0);
		} else if (componentId == OPTION_4) {
			sendOptionsDialogue("Where would you like to go?", "Hunter", "Agility", "Runecrafting", "Mining", 
					"More Options");
			stage = 12;
			}
 } else if (stage == 13) {
		if (componentId == OPTION_1) {
			teleportPlayer(2470, 3438, 0);
		} else if (componentId == OPTION_2){
			teleportPlayer(2552, 3561, 0);
		} else if (componentId == OPTION_3){
			sendOptionsDialogue("Where would you like to go?", "Hunter", "Agility", "Runecrafting", "Mining", 
					"Previous Options");
			stage = 12;
			}
 } else if(stage == 14) {
		if (componentId == OPTION_1) {
			teleportPlayer(2842, 4835, 0);
		} else if (componentId == OPTION_2) {
			teleportPlayer(2784, 4840, 0);
		} else if (componentId == OPTION_3) {
			teleportPlayer(2721, 4828, 0);
		} else if (componentId == OPTION_4) {
			teleportPlayer(2660, 4839, 0);
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Where would you like to go?",
					"Fire Altar", "Body Altar", "Cosmic Altar", "Chaos Altar", "More Options");
			stage = 15;
				}
	} else if(stage == 15) {
		if (componentId == OPTION_1) {
			teleportPlayer(2584, 4836, 0);
		} else if (componentId == OPTION_2) {
			teleportPlayer(2527, 4833, 0);
		} else if (componentId == OPTION_3) {
			teleportPlayer(2142, 4834, 0);
		} else if (componentId == OPTION_4) {
			teleportPlayer(2269, 4843, 0);
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Where would you like to go?",
					"Nature Altar", "Law Altar", "Death Altar", "Blood Altar", "Beginning Options");
			stage = 16;
				}
	} else if(stage == 16) {
		if (componentId == OPTION_1) {
			teleportPlayer(2398, 4841, 0);
		} else if (componentId == OPTION_2) {
			teleportPlayer(2464, 4834, 0);
		} else if (componentId == OPTION_3) {
			teleportPlayer(2207, 4836, 0);
		} else if (componentId == OPTION_4) {
			teleportPlayer(2462, 4891, 1);
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Where would you like to go?",
					"Air Altar", "Mind Altar", "Water Altar", "Earth Altar", "More Optiona");
				stage = 14;
				}
			} else if (stage == 2) {
		if (componentId == OPTION_1) {
			teleportPlayer(2709, 3706, 0);
		} else if (componentId == OPTION_2){
			teleportPlayer(2326, 3805, 0);
		} else if (componentId == OPTION_3){
			teleportPlayer(3653, 5115, 0);
		} else if (componentId == OPTION_4){
			teleportPlayer(1093, 6496, 0);
		}
			} else if (stage == 3) {
			if (componentId == OPTION_1) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1890, 3177, 0));
			} else if (componentId == OPTION_2) {
				teleportPlayer(2994, 9679, 0);
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3365,
						3275, 0));
				player.getControlerManager().startControler("DuelControler");
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, CastleWars.LOBBY);
			}
			else if (componentId == OPTION_5) {
				stage = 9;
				sendOptionsDialogue("Where would you like to go?",
						"Fight Pits", "Fight Kiln", "Fight Caves",
						"Crucible", "More Options");
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				teleportPlayer(2828, 5302, 0);
				//player.getControlerManager().startControler("GodWars");
			} else if (componentId == OPTION_2) {
				teleportPlayer(2926, 5325, 0);
				//player.getControlerManager().startControler("GodWars");
			} else if (componentId == OPTION_3) {
				teleportPlayer(2870, 5363, 0);
				//player.getControlerManager().startControler("GodWars");
			} else if (componentId == OPTION_4) {
				teleportPlayer(2923, 5250, 0);
				//player.getControlerManager().startControler("GodWars");
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Queen Black Dragon", "Nex", "Tormented Deamons", "Jadinkos", "More Options");
				stage = 5;
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {//qbdplayer.getInterfaceManager().closeChatBoxInterface(); 			player.getControlerManager().startControler("QueenBlackDragonControler");
				player.getControlerManager().startControler("QueenBlackDragonControler");
				player.getInterfaceManager().closeChatBoxInterface(); 
			} else if (componentId == OPTION_2)
				teleportPlayer(2905, 5203, 0); 
			else if (componentId == OPTION_3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2562,
						5739, 0));
				else if (componentId == OPTION_4) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3011, 9274, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Kalphite Queen", "Gano Beasts", "Polypore Dungeon", "King Black Dragon",
						"More Options");
				stage = 7;
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3070,
						3649, 0));
			player.getControlerManager().startControler("Wilderness");
			}
			else if (componentId == OPTION_2) 
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2538,
						
						4715, 0));
		else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3240,
						3611, 0));
				player.getControlerManager().startControler("Wilderness");
		}
			else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2984,
						3596, 0));
				player.getControlerManager().startControler("Wilderness");
			}
			else if (componentId == OPTION_5) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3389,
						3616, 0));
			}
		} else if (stage == 7) {
			if (componentId == OPTION_1) 
				teleportPlayer(3507, 9494, 0);
			else if (componentId == OPTION_2)
				teleportPlayer(4628, 5448, 0);
			else if(componentId == OPTION_3) 
				teleportPlayer(3410, 3324, 0);
			else if(componentId == OPTION_4)
				teleportPlayer(3069, 10255, 0);
			else if (componentId == OPTION_5) {
					sendOptionsDialogue("Where would you like to go?",
							"Slayer Tower", "Brimhaven Dungeon", "Taverly Dungeon", "Glacors", "More Options");
					stage = 8;
				}
		} else if (stage == 8) {
			if (componentId == OPTION_1) {
				teleportPlayer(3429, 3538, 0);
				//stage = 27;
				//sendOptionsDialogue("Where would you like to go?",
						//"Slayer Tower", "Mazchna (Medium)", "Kuradal (Hard)", "Back");
			}
			else if (componentId == OPTION_2)
				teleportPlayer(2710, 9466, 0);
			else if(componentId == OPTION_3)
				teleportPlayer(2884, 9798, 0);
			else if(componentId == OPTION_4)
				teleportPlayer(4185, 5734, 0);
			else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?",
						"Frost Dragons", "Corporeal Beast", "Strykewyrms", "Nomad",
				"Beginning Options");
				stage = 24;
			}
		} else if (stage == 24) {
			if (componentId == OPTION_1) 
				teleportPlayer(1298, 4512, 0);
			else if (componentId == OPTION_2)
				teleportPlayer(2968, 4384, 2);
			else if (componentId == OPTION_3) {
			sendOptionsDialogue("Where would you like to go?",
					"Jungle strykewyrm", "Desert strykewyrm", "Ice strykewyrm", "Back");
			stage = 25;
			} else if (componentId == OPTION_4) {
				if (!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
					NomadsRequiem.enterNomadsRequiem(player);
				} else {
					sendDialogue("You already completed nomad. You can only do this once.");
				}
			} else if (componentId == OPTION_5) {
				stage = 1;
				sendOptionsDialogue("Where would you like to go?", "<shad=2EFEF7>Training Teleports</shad>", "<shad=0000ff>Skilling Teleports</shad>",
								"<shad=01DF01>Minigames Teleports</shad>", "<shad=FF00FF>Boss Teleports</shad>", "<shad=ff0000>PvP Teleports</shad>");
					}
		} else if (stage == 25) {
			if (componentId == OPTION_1) 
				teleportPlayer(2457, 2898, 0);
			else if (componentId == OPTION_2)
				teleportPlayer(3387, 3166, 0);
			else if (componentId == OPTION_3) {
				teleportPlayer(2866, 3928, 0);
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("Where would you like to go?",
						"Armadyl", "Zamorak", "Bandos", "Sara", "More Options");
				stage = 4;
					}
		} else if (stage == 26) {
			if (componentId == OPTION_1)
				teleportPlayer(3565, 3312, 0);
			else if (componentId == OPTION_2) {
			} else if (componentId == OPTION_3) {
				sendOptionsDialogue("Where would you like to go?", "<shad=2EFEF7>Training Teleports</shad>", "<shad=0000ff>Skilling Teleports</shad>",
						"<shad=01DF01>Minigames Teleports</shad>", "<shad=FF00FF>Boss Teleports</shad>", "<shad=ff0000>PvP Teleports</shad>");
				stage = 1;
					}
		} else if (stage == 27) {
			if (componentId == OPTION_1)
				teleportPlayer(3429, 3538, 0);
			else if (componentId == OPTION_2) 
				teleportPlayer(3509, 3507, 0);
			else if (componentId == OPTION_3) {
				teleportPlayer(1743, 5309, 1);
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("Where would you like to go?",
						"Slayer Teleports", "Brimhaven Dungeon", "Taverly Dungeon", "Glacors", "More Options");
				stage = 8;
					}
			} else if (stage == 9) {
				if (componentId == OPTION_1) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4608,
							5061, 0));
					} else if (componentId == OPTION_2) {
					Magic.sendNormalTeleportSpell(player, 0, 0, FightKiln.OUTSIDE);
					} else if (componentId == OPTION_3) {
						Magic.sendNormalTeleportSpell(player, 0, 0, FightCaves.OUTSIDE);
					} else if (componentId == OPTION_4) {
						Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3358, 6112, 0));
					} else if (componentId == OPTION_5) {
						sendOptionsDialogue("Where would you like to go?",
								"Zombies", "Trivia", "Dominion Tower", "RuneSpan", "More Options");
						stage = 21;
					}
			}
	}					

		
	

	private void teleportPlayer(int x, int y, int z) {
		player.setNextGraphics(new Graphics(111));
		player.setNextPosition(new Position(x, y, z));
		player.stopAll();

	}

	@Override
	public void finish() {
	}


}
