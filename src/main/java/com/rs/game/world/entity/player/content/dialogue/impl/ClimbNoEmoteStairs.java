package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class ClimbNoEmoteStairs extends Dialogue {

	private Position upTile;
	private Position downTile;

	// uptile, downtile, climbup message, climbdown message, emoteid
	@Override
	public void start() {
		upTile = (Position) parameters[0];
		downTile = (Position) parameters[1];
		sendOptionsDialogue("What would you like to do?",
				(String) parameters[2], (String) parameters[3], "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.useStairs(-1, upTile, 0, 1);
		} else if (componentId == OPTION_2)
			player.useStairs(-1, downTile, 0, 1);
		end();
	}

	@Override
	public void finish() {

	}

}
