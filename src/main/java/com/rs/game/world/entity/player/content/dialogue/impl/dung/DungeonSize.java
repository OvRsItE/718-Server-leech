package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonPartyManager;

public class DungeonSize extends Dialogue {

	@Override
	public void start() {
		DungeonPartyManager party = player.getDungeoneeringManager().getParty();
		if (party == null || party.getTeam().size() < 3)
			sendOptionsDialogue("Would size dungeon would you like?", "Small.", "Medium.");
		else
			sendOptionsDialogue("Would size dungeon would you like?", "Small.", "Medium.", "Large.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		DungeonPartyManager party = player.getDungeoneeringManager().getParty();
		if (party != null) {
			if (componentId == OPTION_1)
				player.getDungeoneeringManager().setSize(DungeonConstants.SMALL_DUNGEON);
			else if (componentId == OPTION_2)
				player.getDungeoneeringManager().setSize(DungeonConstants.MEDIUM_DUNGEON);
			else if (componentId == OPTION_3 && party.getTeam().size() >= 3)
				player.getDungeoneeringManager().setSize(DungeonConstants.LARGE_DUNGEON);
		}
		end();
		player.getDungeoneeringManager().enterDungeon(false, true, false);
	}

	@Override
	public void finish() {

	}

}
