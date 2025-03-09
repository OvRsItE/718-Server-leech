package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MTHighLevelDungeons extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("High Level Dungeons/Areas", "God Wars Dungeon", "Ancient Cavern", "Forinthry Dungeon",
				"Frost Dragons", "More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2916, 3746, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2512, 3511, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3080, 10057, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1312, 4528, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("High Level Dungeons/Areas - Page 2", "Ape Atoll Temple", "Tirannwn Elf Camp",
						"Evil Chicken's Lair", "Ogre Enclave", "More");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2787, 2786, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2203, 3253, 0));
			} else if (componentId == OPTION_3) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1576, 4363, 0));
				player.sm("Disabled.");
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2589, 9411, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("High Level Dungeons/Areas - Page 2", "Gorak Plane", "Ourania Cave",
						"Smoke Devil Dungeon", "Back to Beginning");
				stage = 2;
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3038, 5346, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3271, 4861, 0));
			} else if (componentId == OPTION_3) {
				if (player.getEquipment().wearingFaceMask()) {
					player.getControlerManager().startControler("SmokeDungeonController");
				} else {
					player.getDialogueManager().startDialogue("SmokeDungeonConfirm");
				}
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("High Level Dungeons/Areas", "God Wars Dungeon", "Ancient Cavern",
						"Forinthry Dungeon", "Frost Dragons", "More");
				stage = -1;
			}
		}

	}

	@Override
	public void finish() {

	}
}