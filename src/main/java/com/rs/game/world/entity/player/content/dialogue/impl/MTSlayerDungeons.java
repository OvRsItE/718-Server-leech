package com.rs.game.world.entity.player.content.dialogue.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MTSlayerDungeons extends Dialogue {
	
	

	@Override
	public void start() {
		sendOptionsDialogue("Slayer Locations",
				"Slayer Tower",
				"Edgeville Dungeon",
				"Glacor Cave",
				"Jadinko Lair",
				"More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3428, 3534, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3097, 9870, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4180, 5731, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3025, 9224, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
						"Strykwyrms",
						"Asgarnian ice dungeon",
						"Fremmenik Slayer Dungeon",
						"Kuradel's Slayer Dungeon",
						"More");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				player.getDialogueManager().startDialogue("Stryke");
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3006, 9549, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2806, 10004, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1738, 5313, 1));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
						"Temple Of Light",
						"Smoke Dungeon",
						"Taverley Slayer Dungeon",
						"Brine Rat Cavern",
						"More");
				stage = 2;
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2009, 4639, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3308, 2962, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2884, 9797, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2696, 10120, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
						"Desert Lizards",
						"Meiyerditch Dungeon",
						"Poison Waste Slayer Dungeon",
						"Mudskipper Point",
						"More");
				stage = 3;
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3418, 3039, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3627, 9741, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2321, 3100, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2992, 3112, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations", "Tarn's Lair", "Mos Le'Harmless Jungle", "Chaos alter dungeon","Sea trollqueen" , "More");
				stage = 4;
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3185, 4598, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3739, 2989, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3227, 10011, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1312, 4528, 0));
			}else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
				"God Wars Dungeon",
				"Ancient Cavern",
				"Forinthry Dungeon",
				"Frost Dragons",
				"More");
				stage = 5;
			}
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2916, 3746, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2512, 3511, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3080, 10057, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3033, 9597, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations",
				"Taverly Dungeon",
				"Brimhaven Dungeon",
				"Waterfall Dungeon",
				"Grotworm Lair",
				"More");
				stage = 6;
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2886, 9797, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2744, 3152, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2574, 9864, 0));
			} else if (componentId == OPTION_4) {
				//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile());
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations", "Legends Guild Dungeon", "Vyrewatch's", "Smoke Devil Dungeon", "Adamant/Rune Dragons", "More");
				stage = 7;
			}
		} else if (stage == 7) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2722, 9774, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3613, 3370, 0));
			} else if (componentId == OPTION_3) {
				if (player.getEquipment().wearingFaceMask()) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2916, 2503, 0));
					CoresManager.slowExecutor.schedule(() -> {
						player.getControlerManager().startControler("SmokeDungeonController");
					}, 5, TimeUnit.SECONDS);
				} else {
					player.getDialogueManager().startDialogue("SmokeDungeonConfirm");
				}
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1568, 5062, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Locations", "Khourend Dungeon", "More");
				stage = 8;
			}
		} else if (stage == 8) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1640, 3673, 0));
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue("Slayer Locations",
						"Slayer Tower",
						"Edgeville Dungeon",
						"Glacor Cave",
						"Jadinko Lair",
						"More");
				stage = -1;
			}
		}
	}

	@Override
	public void finish() {

	}
}