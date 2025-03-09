package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.teleports.TeleportManager;

public class GodwarsTeleports extends Dialogue {
	
	@Override
	public void start() {
		stage = 1;
		if (stage == 1) {
		    sendOptionsDialogue("Godwars Teleports", "Bandos", "Zamorak", "Armadyl","Saradomin", "Nex");
			stage = 1; 
		}
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				player.getControlerManager().startControler("GodWars");
				player.setNextPosition(new Position(2861, 5357, 0));
				player.closeInterfaces();
				end();
			}
			if (componentId == OPTION_2) {
				player.getControlerManager().startControler("GodWars");
				player.setNextPosition(new Position(2925, 5333, 0));
				player.closeInterfaces();
				end();
			}
			if (componentId == OPTION_3) {
				player.getControlerManager().startControler("GodWars");
				player.setNextPosition(new Position(2835, 5293, 0));
				player.closeInterfaces();
				end();
			}
			if (componentId == OPTION_4) {
				player.getControlerManager().startControler("GodWars");
				player.setNextPosition(new Position(2923, 5258, 0));
				player.closeInterfaces();
				end();

			}
			if (componentId == OPTION_5) {
				player.getControlerManager().startControler("GodWars");
				player.setNextPosition(new Position(2908, 5203, 0));
				player.closeInterfaces();
				end();

			}
		}
	}

	@Override
	public void finish() {
		

	}

}