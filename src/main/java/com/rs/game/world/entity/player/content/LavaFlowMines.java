package com.rs.game.world.entity.player.content;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;

public class LavaFlowMines {
	
	static Position INSIDE = new Position(2177, 5664, 0);
	
	public static void Entering(Player player) {
		player.stopAll();
		player.setNextPosition(INSIDE);
		player.getDialogueManager().startDialogue("SimpleNPCMessage", 13395, "Welcome to the mines " +player.getDisplayName() + "!");
		
	}
	
}
