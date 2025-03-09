package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class TrainingCaptain extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "Hey, I can teleport you to training areas in Zamron.", "Would you like to teleport somewhere?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "I guess I could use some training.." }, IS_PLAYER, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendOptionsDialogue("Where would you like to go?", "Chickens", "Cows", "Yaks", "Armored zombies", "More Options");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3234, 3294, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3258, 3276, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2322, 3792, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3241, 10000, 0));
			} else if (componentId == OPTION_5) {
				stage = 3;
				sendOptionsDialogue("Where would you like to go?", "Slayer tower", "Frost dragons", "Living rock caverns", "Rune essence mine", "More Options");
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3421, 3537, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2931, 3899, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3654, 5115, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2897, 4845, 0));
			} else if (componentId == OPTION_5) {
				stage = 4;
				sendOptionsDialogue("Where would you like to go?", "Polypore dungeon (bottom level)", "Glacors", "Jadinko lair", "Godwars dungeon", "More Options");
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4701, 5608, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4183, 5726, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2952, 2954, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2914, 3746, 0));
			} else if (componentId == OPTION_5) {
				stage = 5;
				sendOptionsDialogue("Where would you like to go?", "Taverly dungeon", "Revenants (PVP)", "Cyclops (Defenders)", "Dagannoth Kings", "More Options");
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2892, 9784, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3079, 10058, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2843, 3535, 2));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2900, 4449, 0));
			} else if (componentId == OPTION_5) {
				stage = 6;
				sendOptionsDialogue("Where would you like to go?", "Dungeoneering", "Skeletal wyverns", "Kalphite Queen", "Barrelchest", "More Options");
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3450, 3728, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3056, 9553, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3508, 9493, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3803, 2844, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?", "Dagg Kings", "Coming Soon", "Coming Soon", "Coming Soon", "More Options");
				stage = 7;
			}
		} else if (stage == 7) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2900, 4449, 0));
			} else if (componentId == OPTION_2) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new
				// WorldTile(2322, 3792, 0));
			} else if (componentId == OPTION_3) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new
				// WorldTile(2322, 3792, 0));
			} else if (componentId == OPTION_4) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new
				// WorldTile(2322, 3792, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Where would you like to go?", "Chickens", "Cows", "Yaks", "Armored zombies", "More Options");
				stage = 2;
			}
		}
	}

	@Override
	public void finish() {

	}
}
