package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class RealityXTutorial extends Dialogue {

	private int npcId = 945;

	@Override
	public void start() {
		sendNPCDialogue(npcId, HAPPY, "Its my pleasure to show you around! " + Constants.SERVER_NAME + " is a community driven server, that offers a variety of content and endless hours of game-play!");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			sendNPCDialogue(npcId, HAPPY, "The teleportal tab can take you anywhere on " + Constants.SERVER_NAME + ", click on Training and then starter training.");
			player.getPackets().sendGlobalConfig(168, 1);
			stage = 2;
		}
		if (stage == 2) {
			sendNPCDialogue(npcId, HAPPY, "");
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		// player.setNextWorldTile(new WorldTile(2677, 3714, 0)); // crabs
	}

}
