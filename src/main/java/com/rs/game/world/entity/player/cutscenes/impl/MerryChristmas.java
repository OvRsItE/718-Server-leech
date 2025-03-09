package com.rs.game.world.entity.player.cutscenes.impl;

import java.util.ArrayList;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;
import com.rs.game.world.entity.player.cutscenes.actions.CutsceneAction;
import com.rs.game.world.entity.player.cutscenes.actions.PosCameraAction;

public class MerryChristmas extends Cutscene {


	
	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		// actionsList.add(new PosCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));
		// actionsList.add(new LookCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));
		
		//actionsList.add(new LookCameraAction(41, 33, 2000, 1, 1, 5));
		actionsList.add(new PosCameraAction(44,36, 2000, 1, 1, 5));
		
		
		
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}
	
	

	@Override
	public boolean hiddenMinimap() {
		return false;
		
	}
	

}