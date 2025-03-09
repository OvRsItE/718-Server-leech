package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class DungeonPartyStart extends Dialogue {
	
	@Override
	public void start() {
		sendDialogue("You need a party before you can start a dungeon.");

				stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
		
			if (player.getInventory().containsItem(15707, 1) || player.getEquipment().getRingId() == 15707) {
			sendOptionsDialogue("Would you like to start a party?", "Yes.", "No.");
			stage = 0;
			} else {
				sendItemDialogue(15701,"To join or create a party, you need a ring of kinship. You can get one from the dungeoneering tutor, on the right of the entrance to Daemonheim castle.");

				stage = 1;
			}
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				player.getDungeoneeringManager().openPartyInterface();
				player.getDungeoneeringManager().formParty();
				player.getDungeoneeringManager().enterDungeon(true, true, false);
			}
			
			
			end();
		} else if (stage == 1)
			end();
	}

	

	@Override
	public void finish() {

	}

}
