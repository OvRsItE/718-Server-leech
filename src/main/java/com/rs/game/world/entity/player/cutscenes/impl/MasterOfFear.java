package com.rs.game.world.entity.player.cutscenes.impl;

import java.util.ArrayList;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;
import com.rs.game.world.entity.player.cutscenes.actions.CutsceneAction;
import com.rs.game.world.entity.player.cutscenes.actions.InterfaceAction;
import com.rs.game.world.entity.player.cutscenes.actions.LookCameraAction;
import com.rs.game.world.entity.player.cutscenes.actions.PosCameraAction;

public class MasterOfFear extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new InterfaceAction(115, 2));
		actionsList.add(new PosCameraAction(getX(player, player.getX() + 5), getY(player, player.getY() + 3), 1500, -1));
		actionsList.add(new LookCameraAction(getX(player, player.getX() - 2), getY(player, player.getY()), 1500, 5));
		return actionsList.toArray(new CutsceneAction[0]);
	}
}
