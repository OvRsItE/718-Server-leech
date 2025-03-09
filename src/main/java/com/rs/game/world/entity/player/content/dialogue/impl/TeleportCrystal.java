package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.LavaFlowMines;
import com.rs.game.world.entity.player.content.activities.RecipeforDisaster;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

/**
 * @author Justin
 * @author Xiles
 * @author Plato
 * @author Adam
 */


public class TeleportCrystal extends Dialogue {

	public TeleportCrystal() {
	}

	@Override
	public void start() {

		stage = 1;
		sendOptionsDialogue("Teleport Options", "Home",
				"Combat Training", "Skilling Locations", "-------",
				"Next Page");
			

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) { //home
			//	Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3221, 3219, 0));
				player.sm("Welcome home, "+ player.getDisplayName() + ".");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				stage = 2;
			} else if (componentId == OPTION_2) {//Combat Training
				sendOptionsDialogue("Combat Training", "Rock Crabs",
						"Bandit Camp", "Ghouls", "Experiments", "Cancel");
				stage = 3;
			} else if (componentId == OPTION_3) {//Skilling locations
				sendOptionsDialogue("Skilling Locations", "Woodcutting",
						"Thieving", "Summoning",
						"Hunter", "Next Page");
				stage = 4;
			} else if (componentId == OPTION_4) {//Bossing Teleports
				///player.getInterfaceManager().MonsterTelePorts();
			} else if (componentId == OPTION_5) {//page 2 of teleport options
				sendOptionsDialogue("Teleport Options",
						"Minigames",
						"PvP", "Prayers/Spellbooks");
				stage = 6;
			}
			
			
			/**
			 * Combat Training Teleports
			 */
			
			
		} else if (stage == 3) { 
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2674, 3715, 0));
				player.sm("Welcome to Rock Crabs!");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3168, 2983, 0));
				player.sm("Welcome to the Bandit Camp!");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3412, 3512, 0));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to Ghouls!");
		} else if (componentId == OPTION_4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3577, 9927, 0));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
				player.sm("You teleport to the experiment dungeon");
		} else if (componentId == OPTION_5) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Coming soon..");
		}
			
			
			/**
			 * Skilling Teleports
			 */
			
			
		} else if (stage == 4) {
			if (componentId == OPTION_1) { 
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2725, 3491, 0));
				player.sm("Welcome to Woodcutting.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2662, 3303, 0));
				player.sm("Welcome to Thieving.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_4) {
			sendOptionsDialogue("Hunter", "Birds/Chins", "Grenwalls");
			stage = 98;
			
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Skilling Teleports - Page 2", "Agility", "Mining", 
					"Smithing", "Farming", "More" );
			stage = 10;
		}
			
		} else if (stage == 98) {
			if (componentId == OPTION_1) { 
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2525, 2915, 0));
				player.sm("Welcome to the red Chins hunter area.");
		} else if (componentId == OPTION_2) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2267, 3241, 0));
			player.sm("Welcome to the Grenwall hunter area.");
		}
			
		} else if (stage == 28) {
			if (componentId == OPTION_1) { 
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2806, 3463, 0));
				player.sm("Welcome to the Catherby Farming Patch.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2664, 3374, 0));
				player.sm("Welcome to the Ardougne Farming Patch.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3599, 3523, 0));
				player.sm("Welcome to the Canifs Farming Patch.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3052, 3304, 0));
				player.sm("Welcome to the Falador Farming Patch.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
	}
			
		} else if (stage == 29) {
			if (componentId == OPTION_1) { 
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3196, 3231, 0));
				player.sm("Welcome to the Lumbridge Tree Patch.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3229, 3456, 0));
				player.sm("Welcome to the Varrock Tree Patch.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3001, 3373, 0));
				player.sm("Welcome to the Falador Tree Patch.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		}
			
		} else if (stage == 27) {
			if (componentId == OPTION_1) { 
				sendOptionsDialogue("Major Patches", "Catherby", "Ardougne", 
						"Canifs", "Falador" );
				stage = 28;
		} else if (componentId == OPTION_2) {
				sendOptionsDialogue("Tree Patches", "Lumbridge", "Varrock", 
						 "Falador" );
				stage = 29;
		} else if (componentId == OPTION_3) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
	}
			
		} else if (stage == 24) {
			if (componentId == OPTION_1) { 
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2468, 3437, 0));
				player.sm("Welcome to the Gnome Agility Course.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2552, 3557, 0));
				player.sm("Welcome to the Barbarian Agility Course.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2998, 3933, 0));
				player.sm("Welcome to the Wilderness Agility Course.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_4) {
			player.sm("Coming soon..");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
	}
			
			/**
			 * Skilling Telepoerts p2
			 */
			
		} else if (stage == 10) {
			if (componentId == OPTION_1) { 
				sendOptionsDialogue("Agility Teleports", "Gnome", "Barbarian", 
						"Wilderness", "None" );
				stage = 24;
		} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3298, 3283, 0));
				player.sm("Welcome to Mining.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2614, 3082, 0));
				player.sm("Welcome to Smithing.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_4) {
			sendOptionsDialogue("Farming Teleports", "Major Patches", "Tree Patches", 
					"Farming Shop", "None" );
			stage = 27;
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Skilling Teleports - Page 3", "Abyssal Runecrafting", "Dungeoneering", 
					 "None" );
			stage = 81;
	}
		} else if (stage == 81) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3039,
						4834, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.getControlerManager().startControler("Abyss");
		} else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3450,
					3719, 0));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
			
		}
		
			
			
			/**
			 * Bossing Teleports
			 */
			
			
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3067, 10255, 0));
				player.sendMessage("Welcome to the King Black Dragon.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
			player.getControlerManager().startControler("BorkControler", 0, null);
		} else if (componentId == OPTION_3) {
			if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
				player.getPackets().sendGameMessage("You need a summoning level of 60 to go to this monster.");
				player.getControlerManager().removeControlerWithoutCheck();
			} else {
			player.lock();
			player.getControlerManager().startControler("QueenBlackDragonControler");
			}
		} else if (componentId == OPTION_4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2965, 4381, 2));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the Corporeal Beast.");
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Bossing - Page 2", "Kalphite Queen", "Wildy Wyrm", 
					"Dagganoth Kings", "Tormented Demons", "Next" );
			stage = 11;
		}
			
		} else if (stage == 11) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3226, 3108, 0));
				player.sendMessage("Welcome to the Kalphite Queen Lair.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3170, 3872, 0));
			player.sendMessage("Welcome to the Wildy Wyrm.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2545, 10143, 0));
			player.sendMessage("Welcome to the Dagganoth Lair.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2564, 5740, 0));
			player.sendMessage("Welcome to the Tormented Demons.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Bossing - Page 3", "Blink", "Yk'Lagor - DANGEROUS", 
					"None" );
			stage = 30;
	}
			
		} else if (stage == 30) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1368, 6623, 0));
				player.sendMessage("Welcome to Blink.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2521, 5232, 0));
			player.sendMessage("Welcome to Yk'Lagor the Thunderous.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();

	}	
			
			//PvP
		} else if(stage == 17) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3081, 3523, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the Edgeville Ditch.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3128, 3719, 0));
				player.sm("Welcome to the Wilderness Volcano.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2539, 4716, 0));
				player.sm("Welcome to the Mage Bank.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3240, 3611, 0));
				player.sm("Welcome to the Multi PvP Area.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("PvP Teleports - Page 2", "Easts", "Wests"
					 );
				stage = 18;
			
			}
			
		} else if(stage == 18) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3360, 3658, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the East Dragons.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3000, 3623, 0));
				player.sm("Welcome to the West Dragons.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} 


			/**
			 * Godwars Dungeon Teleport
			 */
			
			
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Minigames", "Godwars Dungeon", "The RuneSpan", 
						"Warriors Guild", "Crucible", "Next Page" );
				stage = 7;
					
				
				

		} else if (componentId == OPTION_2) {
			sendOptionsDialogue("PvP Teleports", "Edgeville Ditch", "Wilderness Volcano", "Mage Bank", "Multi Area", "More");
			stage = 17;	
		}
			
		 else if (componentId == OPTION_3) {
			sendOptionsDialogue("Prayers/Spellbooks", "Ancient Magics", "Lunar Spells", "Ancient Curses");
			stage = 19;
		 }
			
			//prayers/spellbook
		} else if(stage == 19) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3233, 9315, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the Ancient Magics Altar.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2151, 3863, 0));
				player.sm("Welcome to the Lunar Magic Altar.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			
		} else if(componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3182, 5713, 0));
			player.sm("Welcome to the Ancient Curses Altar.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
			
		} 
		
		
			

			
			//monsters
		} else if(stage == 20) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1313, 4527, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to Frost Dragons.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2914, 3922, 0));
				player.sm("Welcome to Iron Dragons.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2420, 4689, 0));
				player.sm("Welcome to Bronze Dragons.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3056, 10289, 0));
				player.sm("Welcome to the Lava Dungeon.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Monsters - Page 2", "Forinthry Dungeon", "Taverly Dungeon", 
						"Brimhaven Dungeon", "Wilderness Dungeon", "More" );
				stage = 21;
			
			}
			
		} else if(stage == 21) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3080, 10057, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the Forinthry Dungeon.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2884, 9798, 0));
				player.sm("Welcome to Taverly Dungeon.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2710, 9466, 0));
				player.sm("Welcome to Brimhaven Dungeon.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3039, 3765, 0));
				player.sm("Welcome to the Wilderness Dungeon.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Monsters - Page 3", "Chaos Druids", "Elite Knights", 
						"Lumbridge Swamp", "Living Rock Caverns", "More" );
				stage = 23;
			
			}
			
		} else if(stage == 23) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2932, 9848, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to Chaos Druids.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3025, 9951, 1));
				player.sm("Welcome to Elite Knights.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3168, 9570, 0));
				player.sm("Welcome to Lumbridge Swamp.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3655, 5114, 0));
				player.sm("Welcome to the Living Rock Caverns.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Monsters - Page 4", "Grotworm Lair", "Ancient Cavern", 
						"Fire Giants", "More to come.." );
				stage = 26;
			}
		} else if(stage == 26) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1206, 6372, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the Grotworm Lair.");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1763, 5365, 1));
				player.sm("Welcome to the Ancient Cavern.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2575, 9862, 0));
				player.sm("Welcome to the waterfall dungeon.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
			
			} else if(componentId == OPTION_4) {
				player.sm("More will be added, suggest something on the forums.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
			
			}
			
			
			/**
			 * Godwars Dungeon Teleports
			 */
			
			
			
			
		} else if (stage == 7) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Pick One", "Nex", "Godwars Dungeon");
				stage = 8;
						
			
			/**
			 * The RuneSpan
			 */
			
			
		} else if (componentId == OPTION_2) {
			sendOptionsDialogue("The RuneSpan", "1st Level", "2nd Level", 
					"3rd Level" );
			stage = 69;
			
			
			/**
			 * Warriors Guild
			 */
			
			
		} else if (componentId == OPTION_3) {
			teleportPlayer3(2871, 3542, 0);
			player.getInterfaceManager().closeChatBoxInterface();
			player.sm("Welcome to Warriors Guild.");
			
		} else if (componentId == OPTION_4) {
			teleportPlayer3(3120, 3519, 0);
			player.getInterfaceManager().closeChatBoxInterface();
			player.sm("Welcome to Crucible.");
		} else if (componentId == OPTION_5) {
			sendOptionsDialogue("Minigames - Page 2", "Fight Caves", "Fight Kiln", 
					"Dominion Tower", "Clan Wars", "More" );
			stage = 13;
		}
			
		} else if(stage == 13) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4612, 5129, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to Fight Caves");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4743, 5161, 0));
				player.sm("Welcome to Fight Kiln.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3360, 3082, 0));
				player.sm("Welcome to Dominion Tower.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2993, 9679, 0));
				player.sm("Welcome to Clan Wars.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 3", "Castle Wars", "Troll Invasion", 
						"Dominion Tower", "Duel Arena", "More" );
				stage = 14;
			
			}
			
		} else if(stage == 14) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2442, 3090, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to Castle Wars");
				
			} else if(componentId == OPTION_2) {
				player.getControlerManager().startControler("TrollInvasion");
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3360, 3082, 0));
				player.sm("Welcome to Dominion Tower.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3367, 3267, 0));
				player.sm("Welcome to the Duel Arena.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 4", "Sorceress' Garden", "Barrows", 
						"Livid Farm", "Recipe for Disaster", "More" );
				stage = 15;
			
			}
			
		} else if(stage == 15) {
			if(componentId == OPTION_1) {//Slayer Tower
				player.getControlerManager().startControler("SorceressGarden");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3565, 3289, 0));
				player.sm("Welcome to Barrows.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				player.getInventory().addItem(6950, 1);
				player.sm("Here is your Livid Farm Crystal.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
			} else if(componentId == OPTION_4) {
            RecipeforDisaster.enterRfd(player);
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 5", "Zombies", "Dark Invasion", "Lava Flow Mines", "Pest Invasion",
						"None" );
				stage = 58;
			}
				
			} else if(stage == 58) {
				if(componentId == OPTION_1) { //Nex
		            Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3701, 3461, 0));
					player.sm("Welcome to Zombies.");
					player.getInterfaceManager().closeChatBoxInterface();
					player.getInterfaceManager().closeOverlay(true);
					player.getControlerManager().forceStop();
					player.getControlerManager().removeControlerWithoutCheck();
		} else if(componentId == OPTION_2) {
			player.getControlerManager().startControler("DarkInvasion");
		} else if (componentId == OPTION_3) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		LavaFlowMines.Entering(player);
		} else if (componentId == OPTION_4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4520, 5516, 0));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_5) {
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
	}
			
			
			
			
		
			
			
			
			/**
			 * Godwars Dungeon Teleports
			 */
			
			
		} else if(stage == 8) {
			if (componentId == OPTION_1) { // Nex
				teleportPlayer(2904, 5203, 0);
				player.getInterfaceManager().closeChatBoxInterface();

			} else if (componentId == OPTION_2) { // GWD
				teleportPlayer(2881, 5311, 0);
				player.getInterfaceManager().closeChatBoxInterface();
			}

			
			/**
			 * RuneSpan Teleports
			 */
			
		} else if(stage == 69) {
			if(componentId == OPTION_1) {
				teleportPlayer2(3993, 6108, 1);
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
				
			} else if(componentId == OPTION_2) {
				teleportPlayer2(4137, 6089, 1);
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
				
			} else if(componentId == OPTION_3) {
				teleportPlayer2(4295, 6038, 1);
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
			}
			
		} else if(stage == 25) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3424, 5667, 0));
				player.sm("<col=FF0000>Welcome to the Ice Strykwyrms!");
				player.getInterfaceManager().closeChatBoxInterface();
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3373, 3160, 0));
				player.sm("<col=FF0000>Welcome to the Desert Strykwyrms!");
				player.getInterfaceManager().closeChatBoxInterface();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2459, 2901, 0));
				player.sm("<col=FF0000>Welcome to the Jungle Strykwyrms!");
				player.getInterfaceManager().closeChatBoxInterface();
			}
			
			
			/**
			 * Slayer Teleports
			 */
			
		} else if (stage == 12) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Strykewyrms", "Ice Strykewyrms", "Desert Strykewyrms", "Jungle Strykewyrms" );
				stage = 25;
		} else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2808, 10002, 0));
			player.sendMessage("Welcome to the Fremmenik Slayer Dungeon.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1736, 5313, 1));
			player.sendMessage("Welcome to Kuradel's Slayer Dungeon.");
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
		} else if (componentId == OPTION_4) {
			player.sm("You teleport to the skeletal wyverns dungeon!");
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3007, 9550, 0));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControlerManager().forceStop();
			player.getControlerManager().removeControlerWithoutCheck();
	}

			
		} else if(stage == 9) {
			if(componentId == OPTION_1) {//Slayer Tower
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3429, 3534, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				player.sm("Welcome to the Slayer Tower");
				
			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4646, 5405, 0));
				player.sm("Welcome to the Polypore Dungeon.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4182, 5731, 0));
				player.sm("Welcome to Glacors.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3023, 9224, 0));
				player.sm("Welcome to Jadinkos.");
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer - Page 2", "Strykewyrms", "Fremmenik Slayer Dungeon", "Kuradel's Slayer Dungeon",
						"skeletal wyverns" );
				stage = 12;
			
			}
		}
	}
			@Override
			public void finish() {
			}
	
	private void teleportPlayer(int x, int y, int z) {
		player.setNextPosition(new Position(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("GodWars");
	}

	private void teleportPlayer2(int x, int y, int z) {
		player.setNextPosition(new Position(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("RunespanControler");
	}

	private void teleportPlayer3(int x, int y, int z) {
		player.setNextPosition(new Position(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("WGuildControler");
	}

}
