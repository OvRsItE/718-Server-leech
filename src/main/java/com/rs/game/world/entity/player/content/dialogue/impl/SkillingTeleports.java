package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class SkillingTeleports extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
			sendOptionsDialogue("Skilling Teleports", "Fishing", "Mining", "Agility", "Woodcutting", "More Options...");
			stage = 1;
		}
	}

	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			int option;
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Fishing teleport", "Barbarian", "Draynor", "<shad=FFCD05>catherby",
						"<shad=0066CC>LRC", "<shad=FF66CC>Fishing Guild");
				stage = 33;
			}
			if (componentId == OPTION_2) {
				sendOptionsDialogue("Mining teleport", "Dwarf Mine", "Coal", "LRC", "Lava flow", "more");
				stage = 44;

			}
			if (componentId == OPTION_3) {
				sendOptionsDialogue("Agility Teleports", "Gnome Agility", "Barbarian Outpost", "Agility Pyramid",
						"Wilderness Agility");
				stage = 3;
			}
			if (componentId == OPTION_4) {
				sendOptionsDialogue("Mining teleport", "Lumby", "Draynor", "Seers", "Magics", "Ivy");
				stage = 55;
			}
			if (componentId == OPTION_5) {
				stage = 2;
				sendOptionsDialogue("Skilling Teleports", "Runecrafting", "Summoning", "Hunter", "PuroPuro", "More");
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("RC teleport", "Air", "Mind", "Water", "Earth", "More");
				stage = 66;

			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2209, 5343, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2526, 2916, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2419, 4446, 0));
				end();
			}
			if (componentId == OPTION_5) {
				stage = 5;
				sendOptionsDialogue("Skilling Teleports", "Falconry", "Gemstone Rocks", "Dungeoneering", "Back");
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2470, 3436, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2552, 3563, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3354, 2828, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2998, 3915, 0));
				end();
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				player.getBank().depositAllEquipment(true);
				player.sm("All your equipped armor have been banked!!");
				player.getControlerManager().startControler("Falconry");
				end();
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2825, 2997, 0));
				end();
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3451, 3725, 0));
				end();
			} else if (componentId == OPTION_4) {
				stage = 1;
				sendOptionsDialogue("Skilling Teleports", "Fishing", "Mining", "Agility", "Woodcutting",
						"More Options...");
			}
		} else if (stage == 33) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3105, 3430, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3091, 3235, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2855, 3427, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3655, 5114, 0));
				end();
			}
			if (componentId == OPTION_5) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2587, 3422, 0));
				end();

			}
		} else if (stage == 44) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3020, 9847, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2585, 3478, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3655, 5114, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2179, 5663, 0));
				end();
			}
			if (componentId == OPTION_5) {
			}
		} else if (stage == 55) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3194, 3229, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3091, 3235, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2723, 3468, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2702, 3392, 0));
				end();
			}
			if (componentId == OPTION_5) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3216, 3501, 0));
				end();
			}
		} else if (stage == 66) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2841, 4829, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2791, 4828, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3494, 4832, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2655, 4830, 0));
				end();
			}
			if (componentId == OPTION_5) {
				stage = 118;
				sendOptionsDialogue("RC teleport", "Fire", "Cosmic", "Chaos", "Astral", "More");
			}
		} else if (stage == 118) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2577, 4844, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2162, 4833, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2281, 4837, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2153, 3868, 0));
				end();
			}
			if (componentId == OPTION_5) {
				stage = 123;
				sendOptionsDialogue("RC teleport", "Nature", "Law", "Death", "Blood", "Next");
			}
		} else if (stage == 123) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2400, 4835, 0));
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2464, 4818, 0));
				end();
			}
			if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2208, 4830, 0));
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2468, 4889, 1));
				end();
			}
			if (componentId == OPTION_5) {
				stage = 89;
				sendOptionsDialogue("RC teleport", "Soul", "Runespan");
			}
		} else if (stage == 89) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3302, 4809, 0));
				end();
			}
			if (componentId == OPTION_2) {
				sendOptionsDialogue("The RuneSpan", "1st Level", "2nd Level", "3rd Level");
				stage = 69;
			}
		} else if (stage == 69) {
			if (componentId == OPTION_1) {
				teleportPlayer2(3993, 6108, 1);
				// player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO
				// LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();

			} else if (componentId == OPTION_2) {
				teleportPlayer2(4137, 6089, 1);
				// player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO
				// LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();

			} else if (componentId == OPTION_3) {
				teleportPlayer2(4295, 6038, 1);
				// player.sm("<col=FF0000>PLEASE USE THE HOME TELEPORT USING TELEPORT CYRSTAL TO
				// LEAVE RUNESPAN!");
				player.getInterfaceManager().closeChatBoxInterface();
			}

		}
	}

	private void teleportPlayer2(int x, int y, int z) {
		player.setNextPosition(new Position(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("RunespanControler");
	}

	@Override
	public void finish() {

	}

}