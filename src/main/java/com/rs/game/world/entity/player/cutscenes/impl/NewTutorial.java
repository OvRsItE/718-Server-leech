package com.rs.game.world.entity.player.cutscenes.impl;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;
import com.rs.game.world.entity.player.cutscenes.actions.ConstructMapAction;
import com.rs.game.world.entity.player.cutscenes.actions.CutsceneAction;
import com.rs.game.world.entity.player.cutscenes.actions.LookCameraAction;
import com.rs.game.world.entity.player.cutscenes.actions.MovePlayerAction;
import com.rs.game.world.entity.player.cutscenes.actions.PlayerAnimationAction;
import com.rs.game.world.entity.player.cutscenes.actions.PlayerForceTalkAction;
import com.rs.game.world.entity.player.cutscenes.actions.PosCameraAction;
import com.rs.game.world.entity.updating.impl.Animation;

public class NewTutorial extends Cutscene {

	public NPC npc;

	private Position dir;
	private int selected;


	private static int MAX = 1, SUMMONING = 2, SKILLING = 3, FOOD = 4, GEN = 5,
	 				   RANGE = 6, COMBAT = 7, MAGIC = 8, POINTS = 9;
	
	// actionsList.add(new PosCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));
	// actionsList.add(new LookCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
		actionsList.add(new ConstructMapAction(322, 384, 8, 8));
		actionsList.add(new MovePlayerAction(22, 16, 0, Player.TELE_MOVE_TYPE, -1)); 
	  //  actionsList.add(new CreateNPCAction(MAX, 1552, 2598, 3088, 0, -1));
	   // actionsList.add(new NPCAnimationAction(PLAYER, new Animation(4367), -1));
	   // actionsList.add(new NPCFaceTileAction(PLAYER, 2599, 3088, -1));
	  
		//actionsList.add(new LookCameraAction(21, 16, 1000, 6, 6, -1));
		actionsList.add(new LookCameraAction(23, 16, 2000, -1));
		actionsList.add(new PosCameraAction(29, 16, 2000, 10));
	
	    
	    /**
	     * Faces play
	     */
	    
		//actionsList.add(new NPCForceTalkAction(M, "OUCH! THAT HURT!", 1));
		actionsList.add(new MovePlayerAction(2599,3088, 0, Player.WALK_MOVE_TYPE, 5)); // out
		actionsList.add(new PlayerAnimationAction(new Animation(857), 2));
		actionsList.add(new PlayerForceTalkAction("Where the F*** did you just come from??", 3));
	//	actionsList.add(new NPCForceTalkAction(PLAYER, "You would not want to hear it.. But i fell of my ride.", 3));
		actionsList.add(new MovePlayerAction(2604, 3088, 0, Player.WALK_MOVE_TYPE, 3)); // out
		actionsList.add(new MovePlayerAction(2605, 3094, 0, Player.WALK_MOVE_TYPE, -1)); // out
		/*actionsList.add(new LookCameraAction(getX(player, 45), getY(player,
				22), 1000, 6, 6, -1));
		actionsList.add(new PosCameraAction(getX(player, 45), getY(player,
				27), 1000, 6, 6, -1));
		actionsList.add(new LookCameraAction(getX(player, 45), getY(player,
				22), 1000, 6, 6, 10));*/
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}
	
	

	@Override
	public boolean hiddenMinimap() {
		return false;
	}
	

}