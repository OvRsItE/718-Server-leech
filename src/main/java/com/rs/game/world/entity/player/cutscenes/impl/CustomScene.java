package com.rs.game.world.entity.player.cutscenes.impl;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.cutscenes.Cutscene;
import com.rs.game.world.entity.player.cutscenes.actions.CreateNPCAction;
import com.rs.game.world.entity.player.cutscenes.actions.CutsceneAction;
import com.rs.game.world.entity.player.cutscenes.actions.DestroyCachedObjectAction;
import com.rs.game.world.entity.player.cutscenes.actions.LookCameraAction;
import com.rs.game.world.entity.player.cutscenes.actions.MoveNPCAction;
import com.rs.game.world.entity.player.cutscenes.actions.MovePlayerAction;
import com.rs.game.world.entity.player.cutscenes.actions.NPCFaceTileAction;
import com.rs.game.world.entity.player.cutscenes.actions.NPCForceTalkAction;
import com.rs.game.world.entity.player.cutscenes.actions.PlayerAnimationAction;
import com.rs.game.world.entity.player.cutscenes.actions.PlayerFaceTileAction;
import com.rs.game.world.entity.player.cutscenes.actions.PlayerForceTalkAction;
import com.rs.game.world.entity.player.cutscenes.actions.PosCameraAction;
import com.rs.game.world.entity.updating.impl.Animation;

public class CustomScene extends Cutscene {

	public NPC npc;

	private Position dir;
	private int selected;


	private static int PLAYER = 1, PLAYER1 = 2, PLAYER2 = 3;
	// actionsList.add(new PosCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));
	// actionsList.add(new LookCameraAction(Local X, Local Y, Height , First Speed, Second Speed, Delay to Next Camera Movement));

	@Override
	public CutsceneAction[] getActions(Player player) {
		player.lock();
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();
	    player.getInterfaceManager().closeChatBoxInterface();
		actionsList.add(new MovePlayerAction(2608, 3100, 0, Player.TELE_MOVE_TYPE, -1)); 
	    actionsList.add(new CreateNPCAction(PLAYER, 17155, 2610, 3100, 0, -1));
	    actionsList.add(new MovePlayerAction(2618, 3106, 0, Player.TELE_MOVE_TYPE, -1));
	    actionsList.add(new PlayerAnimationAction(new Animation(4367), -1));
	    actionsList.add(new PlayerFaceTileAction(2617, 3105, -1));
	    actionsList.add(new LookCameraAction(getX(player, 58), getY(player,
				35), 1000, -1));
		actionsList.add(new PosCameraAction(getX(player,51), getY(player,
				28), 2000, 8));
		actionsList.add(new PlayerAnimationAction(new Animation(857), 2));
		actionsList.add(new PlayerForceTalkAction("Where am i??", -1));
		//actionsList.add(new PlayerAnimationAction(new Animation(857), -1));
		actionsList.add(new MoveNPCAction(PLAYER, 2617, 3106, false, 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "You just landed on the most amazing runescape private server", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "To date!", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "I will be giving you a quick walkthrough of RealityX's home sir!", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "Come with me warrior!", 2));
		actionsList.add(new MoveNPCAction(PLAYER, 2607, 3100, false, 1));
		actionsList.add(new MovePlayerAction(2612, 3101, 0, Player.WALK_MOVE_TYPE, -1));
		actionsList.add(new MovePlayerAction(2607, 3101, 0, Player.WALK_MOVE_TYPE, -1));
		actionsList.add(new LookCameraAction(getX(player, 49), getY(player, 29), 2000, -1));
		actionsList.add(new PosCameraAction(getX(player,35), getY(player, 29), 3000, 5));
		actionsList.add(new NPCFaceTileAction(PLAYER, 2606, 3103, 2));
		actionsList.add(new PlayerFaceTileAction(2606, 3103, 2));
		actionsList.add(new NPCForceTalkAction(PLAYER, "So, here is the thieving stalls! A great place to start getting some gold!", 2));
		actionsList.add(new NPCFaceTileAction(PLAYER, 2609, 3103, 0));
		actionsList.add(new PlayerFaceTileAction(2609, 3103, 1));
		actionsList.add(new NPCForceTalkAction(PLAYER, "All thieved items, can be sold to Gerald..", 2));
		actionsList.add(new MoveNPCAction(PLAYER, 2611, 3100, false, 1));
		actionsList.add(new MovePlayerAction(2610, 3100, 0, Player.WALK_MOVE_TYPE, 1));
		actionsList.add(new LookCameraAction(getX(player, 50), getY(player, 27), 1000, -1));
		actionsList.add(new PosCameraAction(getX(player,50), getY(player, 33), 2000, 2, 2, 1));

		actionsList.add(new NPCFaceTileAction(PLAYER, 2611, 3098, 0));
		actionsList.add(new PlayerFaceTileAction(2610, 3098, 1));
		actionsList.add(new NPCForceTalkAction(PLAYER, "The shops is seperated from each other..", 2));
		actionsList.add(new NPCForceTalkAction(PLAYER, "This side has all skilling shops.", 2));
		actionsList.add(new DestroyCachedObjectAction(PLAYER, 0));
		actionsList.add(new CreateNPCAction(PLAYER, 17155, 2612, 3086, 0, -1));
		actionsList.add(new NPCFaceTileAction(PLAYER, 2610, 3087, -1));
		actionsList.add(new MovePlayerAction(2610, 3085, 0, Player.TELE_MOVE_TYPE, -1)); 
		actionsList.add(new PlayerFaceTileAction(2610, 3087, -1));
		actionsList.add(new LookCameraAction(getX(player, 51), getY(player, 15), 1000, -1));
		actionsList.add(new PosCameraAction(getX(player, 46), getY(player, 11), 2000, 4, 4, 8));
		actionsList.add(new NPCForceTalkAction(PLAYER, "And the other side has the combat shops!", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "Amazing right?'", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "Let's continue into the bank!", 2));
		actionsList.add(new MoveNPCAction(PLAYER, 2606, 3086, false, 2));
		actionsList.add(new MovePlayerAction(2606, 3086, 0, Player.WALK_MOVE_TYPE, -1));
		actionsList.add(new MoveNPCAction(PLAYER, 2606, 3093, false, 0));
		actionsList.add(new MovePlayerAction(2606, 3092, 0, Player.WALK_MOVE_TYPE, -1));
		actionsList.add(new MoveNPCAction(PLAYER, 2609, 3093, false, 0));
		actionsList.add(new MovePlayerAction(2609, 3092, 0, Player.WALK_MOVE_TYPE, -1));
		actionsList.add(new LookCameraAction(getX(player, 49), getY(player, 21), 2000, -1));
		actionsList.add(new PosCameraAction(getX(player, 56), getY(player, 25), 3000, 5, 5, 16));
		actionsList.add(new NPCFaceTileAction(PLAYER, 2609, 3095, 1));
		actionsList.add(new PlayerFaceTileAction(2609, 3095, 0));
		actionsList.add(new NPCForceTalkAction(PLAYER, "This guy is the point shop guy..", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "He will sell you items for in-game points!", 3));
		actionsList.add(new PlayerForceTalkAction("Sweet..", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "We're pretty much done!", 3));
		actionsList.add(new NPCForceTalkAction(PLAYER, "You just have to pick your game mode then you're set!", 2));
		
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}
	
	

	@Override
	public boolean hiddenMinimap() {
		return false;
	}
	

}