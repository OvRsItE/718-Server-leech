package com.rs.game.world.entity.player.content.activities;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.controller.Controller;

/**
 * @author Richard
 * @author Khaled
 *
 */
public class StealingCreationLobby extends Controller {

	@Override
	public void start() {
		if ((boolean) getArguments()[0])
			StealingCreation.getRedTeam().add(player);
		else 
			StealingCreation.getRedTeam().add(player);
		sendInterfaces();
		if((boolean) getArguments()[0])
			player.sendMessage("You join the red team.");
		else
			player.sendMessage("You join the blue team.");
	}

	@Override
	public void sendInterfaces() {
		//player.getInterfaceManager().sendTab(804, player.getInterfaceManager().hasRezizableScreen() ? 11 : 27);//TODO find correct one
		StealingCreation.updateInterfaces();
	}

	//TODO object click for exit

	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage","A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		player.getControlerManager().forceStop();
	}

	@Override
	public void forceClose() {
		if ((boolean) getArguments()[0])
			StealingCreation.getRedTeam().remove(player);
		else 
			StealingCreation.getRedTeam().remove(player);
		StealingCreation.updateInterfaces();
	}
}
