package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.magic.Magic;

public class TrainingTeleports extends Dialogue {
	
	@Override
	public void start() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Rock Crabs", "Yaks", "Experiments", "Goraks", "Bandit Camp");
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2674, 3712, 0));
		} 
		else if (componentId == OPTION_2) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2323, 3793, 0));
		} 
		else if (componentId == OPTION_3) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3559, 9947, 0));
		} 
		else if (componentId == OPTION_4) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3038, 5340, 0));
		} 
		else if (componentId == OPTION_5) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3172, 2982, 0));
		}
		end();
	}
	
	@Override
	public void finish() {}

}