package com.rs.game.world.entity.player.cutscenes.impl;

import java.util.ArrayList;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;
import com.rs.game.world.entity.player.cutscenes.actions.CutsceneAction;
import com.rs.game.world.entity.player.cutscenes.actions.LookCameraAction;
import com.rs.game.world.entity.player.cutscenes.actions.PlayerFaceTileAction;
import com.rs.game.world.entity.player.cutscenes.actions.PlayerForceTalkAction;
import com.rs.game.world.entity.player.cutscenes.actions.PosCameraAction;
import com.rs.utility.Utils;

public class HungerGames extends Cutscene {
	
	public boolean teleBack() {
		return true;
	}


	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new PlayerFaceTileAction(player.getX(), player.getY() + 1, player.getZ()));
		actionsList.add(new LookCameraAction(getX(player, 3803), getY(player,
				3530), 1000, 5, 10, -1));
		actionsList.add(new PosCameraAction(getX(player, 3803), getY(player,
				3535), 2000, 15, 20, 6));
		actionsList.add(new PlayerForceTalkAction(randomText(), 4));
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}
	
	private String randomText() {
		switch(Utils.random(4)) {
		case 0:
			return "I'll kill all of you!!";
		case 1:
			return "Be afriad before I smack you down!";
		case 2:
			return "I'm not scared of any of you wimps";
		case 3:
			return "These noobs don't have a chance!!";
		case 4:
			return "Victory is mine!";
		}
		return "I will win the Hunger Games suckers!";
	}

	@Override
	public boolean hiddenMinimap() {
		return true;
	}
}