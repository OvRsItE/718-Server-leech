package com.rs.game.world.entity.player.content.dialogue.impl;


import com.rs.Constants;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;


public class pointShops extends Dialogue {


	@Override
	public void start() {
		sendOptionsDialogue("Select an Option",  
			"Donator Tokens Shop", 
			"Trivia Point Shop",
			"Vote Point Shop",
		    "Venomite Point Shop");
		stage = 1;
		
	}


	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
			case 1:
			if (componentId == OPTION_1) {
				ShopsHandler.openShop(player, 155);
				end();
			}
			if (componentId == OPTION_2) {
				ShopsHandler.openShop(player, 65);
				end();
			}
			if (componentId == OPTION_3) {
				ShopsHandler.openShop(player, 54);
				end();
			}
			if (componentId == OPTION_4) {
				ShopsHandler.openShop(player, 165);
				end();
			}
 			break;
		}
		
	}


	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}


}