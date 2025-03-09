package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class RC extends Dialogue {
	
	@Override
	public void start() {
		sendOptionsDialogue("RC teleport", "Air", "Mind", "Water", "Earth", "More");
		stage = 1;
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
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
				sendOptionsDialogue("RC teleport", "Fire", "Cosmic", "Chaos", "Astral", "More");
				stage = 2;
			}
		} else if (stage == 2) {
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
				sendOptionsDialogue("RC teleport", "Nature", "Law", "Death", "Blood", "Next");
				stage = 3;
			}
		} else if (stage == 3) {
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
				sendOptionsDialogue("RC teleport", "Soul", "Runespan");
				stage = 4;
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3302, 4809, 0));
				end();
			}
			if (componentId == OPTION_2) {
				sendOptionsDialogue("The RuneSpan", "1st Level", "2nd Level", "3rd Level");
				stage = 100;
			}
		} else if (stage == 100) {
			if (componentId == OPTION_1) {
				teleportPlayer2(3993, 6108, 1);
				player.sm("<col=FF0000>Use ::home to get out of here!");
				player.getInterfaceManager().closeChatBoxInterface();

			} else if (componentId == OPTION_2) {
				teleportPlayer2(4137, 6089, 1);
				player.sm("<col=FF0000>Use ::home to get out of here!");
				player.getInterfaceManager().closeChatBoxInterface();

			} else if (componentId == OPTION_3) {
				teleportPlayer2(4295, 6038, 1);
				player.sm("<col=FF0000>Use ::home to get out of here!");
				player.getInterfaceManager().closeChatBoxInterface();
			}
		}
	}

	@Override
	public void finish() {

	}

	private void teleportPlayer2(int x, int y, int z) {
		player.setNextPosition(new Position(x, y, z));
		player.stopAll();
		player.getControlerManager().startControler("RunespanControler");
	}

}