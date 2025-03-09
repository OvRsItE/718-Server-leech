package com.rs.game.world.entity.player.cutscenes.actions;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Graphics;

public class PlayerGraphicAction extends CutsceneAction {

	private Graphics gfx;

	public PlayerGraphicAction(Graphics gfx, int actionDelay) {
		super(-1, actionDelay);
		this.gfx = gfx;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.setNextGraphics(gfx);
	}

}
