package com.rs.game.world.entity.player.cutscenes.impl;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;
import com.rs.game.world.entity.player.cutscenes.actions.CutsceneAction;
import com.rs.game.world.entity.player.cutscenes.actions.LookCameraAction;

public class ChargeScene extends Cutscene {

	public NPC npc;

	private Position dir;
	private int selected;

	public ChargeScene(Position dir, int selected) {
		this.dir = dir;
		this.selected = selected;
	}

	// actionsList.add(new PosCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));
	// actionsList.add(new LookCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));
	@Override
	public CutsceneAction[] getActions(Player player) {
		
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		//actionsList.add(new PosCameraAction(getX(player, getBaseX(), getBaseY(), 10000, 6, 6, 5));
		actionsList.add(new LookCameraAction(getX(player, 2040), getY(player,
				5719), 6000, 5, 5, 4));

		
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}
	
	

	@Override
	public boolean hiddenMinimap() {
		return false;
	}
	

}