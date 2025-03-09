package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MTHighLevelBosses extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Page - Page 1", "Godwars", "Queen Black Dragon", "King Black Dragon", "Kalphite Queen", "-- Next Page --");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2908, 3707, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1197, 6499, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3067, 10254, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3226, 3109, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Bosses  - Page 2", "Corporeal Beast", "Dagannoth Kings", "Tormented Demons", "Demonic Gorillas", "-- Next Page --");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2969, 4383, 2));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1896, 4409, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2564, 5739, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2435, 3516, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Bosses  - Page 3", "Vorkath", "Zulrah", "Wildy Wyrm", "Cerberus", "-- Next Page --");
				stage = 2;
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2641, 3697, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2200, 3056, 0));
			} else if (componentId == OPTION_3) {
				player.getDialogueManager().startDialogue("WildyWyrmTP");
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1310, 1251, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Bosses - Page 4", "Bork", "Kraken Cove", "The Alchemist", "-- Next Page --");
				stage = 3;
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				player.getControlerManager().startControler("BorkControler", 0, null);
				end();
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3044, 2500, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 1, 0, new Position(3825, 4767, 0));
				player.sendMessage("To start the fight, enter the Dark portal.");
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("Page - Page 1", "Godwars", "Queen Black Dragon", "King Black Dragon", "Kalphite Queen", "-- Next Page --");
				stage = -1;
			}
		}
	}

	@Override
	public void finish() {

	}

}
