package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.LavaFlowMines;
import com.rs.game.world.entity.player.content.activities.RecipeforDisaster;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

public class MinigamesTele extends Dialogue {



	@Override
	public void start() {
		sendOptionsDialogue("Minigames", "Godwars Dungeon", "The RuneSpan",  "Warriors Guild", "Crucible", "Next Page" );
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Pick One", "Nex", "Godwars Dungeon");
				stage = 8;
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue("The RuneSpan", "1st Level", "2nd Level",
						"3rd Level" );
				stage = 69;
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2871, 3542, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.getControlerManager().startControler("WarriorsGuild");
				player.sm("Welcome to Warriors Guild.");

			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3120, 3519, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("Welcome to Crucible.");
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 2", "Fight Caves", "Fight Kiln",
						"Dominion Tower", "Clan Wars", "Next Page" );
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
				end();
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3360, 3082, 0));
				player.sm("Welcome to Dominion Tower.");
				end();

			} else if(componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2993, 9679, 0));
				player.sm("Welcome to Clan Wars.");
				end();

			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 3", "Castle Wars", "Troll Invasion",
						"Duel Arena", "Pest Control", "Next Page" );
				stage = 14;

			}

		} else if(stage == 14) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2442, 3090, 0));
				end();

			} else if(componentId == OPTION_2) {
				player.getControlerManager().startControler("TrollInvasion");
			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3367, 3267, 0));
				player.sm("Welcome to the Duel Arena.");
				end();
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2658, 2663, 0));
				end();
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 4", "Sorceress' Garden", "Barrows",
						"Livid Farm", "Recipe for Disaster", "Next Page" );
				stage = 15;

			}

		} else if(stage == 15) {
			if(componentId == OPTION_1) {
				player.getControlerManager().startControler("SorceressGarden");
				end();

			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3565, 3289, 0));
				player.sm("Welcome to Barrows.");
				end();

			} else if(componentId == OPTION_3) {
				player.getInventory().addItem(6950, 1);
				player.sm("Here is your Livid Farm Crystal.");
				end();
			} else if(componentId == OPTION_4) {
				RecipeforDisaster.enterRfd(player);
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames - Page 5", "Zombies", "Dark Invasion", "Lava Flow Mines", "Pest Invasion",
						"Back to Beginning" );
				stage = 58;
			}

		} else if(stage == 58) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3701, 3461, 0));
				player.sm("Welcome to Zombies.");
				end();
			} else if(componentId == OPTION_2) {
				player.getControlerManager().startControler("DarkInvasion");
			} else if (componentId == OPTION_3) {
				LavaFlowMines.Entering(player);
				end();
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4520, 5516, 0));
				end();
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Minigames", "Godwars Dungeon", "The RuneSpan",  "Warriors Guild", "Crucible", "Next Page" );
				stage = -1;
			}
		}else if(stage == 69) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3993, 6108, 1));
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();

			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4137, 6089, 1));
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();

			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4295, 6038, 1));
				player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
			}

		}

	}

	@Override
	public void finish() {

	}

}