package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class CombatShops extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello " + player.getUsername() + ", would you like to buy something from me?" );
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
		sendOptionsDialogue("Combat Shops", "Armoury Shop", "Weapons Shop", "Combat Accessories", "Amulets & Rings", "Pure Shop");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			ShopsHandler.openShop(player, 13);
			end();
		}
		if(componentId == OPTION_2) {
			ShopsHandler.openShop(player, 14);
			end();
		}
        if(componentId == OPTION_3) {
        	ShopsHandler.openShop(player, 15);
			end();
        }
		if(componentId == OPTION_4) {
        	ShopsHandler.openShop(player, 16);
			end();
        }
		if(componentId == OPTION_5) {
			ShopsHandler.openShop(player, 17);
			end ();
			}
		} 
	}

	@Override
	public void finish() {

	}

}