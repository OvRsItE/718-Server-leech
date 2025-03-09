package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.PlayerLook;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.Utils;

/**
 * @author Justin
 * 
 * 
 */


public class Looks extends Dialogue {

	public Looks() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Appearance Options", "Change Hairstyle",
				"Change Clothes", "Change Looks", "Sheath/Unsheath",
				"Nothing");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if(componentId == OPTION_1) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				PlayerLook.openHairdresserSalon(player);
			} else if(componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				PlayerLook.openThessaliasMakeOver(player);
			} else if(componentId == OPTION_3) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				PlayerLook.openMageMakeOver(player);
			} else if(componentId == OPTION_4) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
				if (player.getAttackedByDelay() + 30000 > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("Please wait until you are out of combat for 10 seconds.");
					return;
					}
			} else if (componentId == OPTION_5) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.getInterfaceManager().closeOverlay(true);
				player.getControlerManager().forceStop();
				player.getControlerManager().removeControlerWithoutCheck();
		}
					
		}
	}
			@Override
			public void finish() {
			}
	


}
