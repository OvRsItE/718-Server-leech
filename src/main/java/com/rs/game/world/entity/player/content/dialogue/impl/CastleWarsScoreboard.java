package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.activities.CastleWars;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class CastleWarsScoreboard extends Dialogue {

	@Override
	public void start() {
		CastleWars.viewScoreBoard(player);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();

	}

	@Override
	public void finish() {

	}

}