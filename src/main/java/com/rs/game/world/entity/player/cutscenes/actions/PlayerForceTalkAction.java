package com.rs.game.world.entity.player.cutscenes.actions;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.ForceTalk;

public class PlayerForceTalkAction extends CutsceneAction {

	private String text;

	public PlayerForceTalkAction(String text, int actionDelay) {
		super(-1, actionDelay);
		this.text = text;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.setNextForceTalk(new ForceTalk(text));
	}

}
