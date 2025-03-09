package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.Utils;

public class StarSprite1 extends Dialogue {

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	private void giveReward() {
		player.sm("You exchange your star dust for a random reward.");
		int starDustAmount = player.getInventory().getNumberOf(13727);
		player.getInventory().deleteItem(13727, Integer.MAX_VALUE);
        if(starDustAmount >= 1)
        	player.getInventory().addItemMoneyPouch(995, Utils.random(1000 * starDustAmount));
        if(starDustAmount >= 50)
        	player.getInventory().addItemMoneyPouch(445, Utils.random(25));
        if(starDustAmount >= 100)
        	player.getInventory().addItemMoneyPouch(24300, 1);
        if(starDustAmount >= 200) {
        	player.getInventory().addItemMoneyPouch(9075, Utils.random(150) + starDustAmount);
            player.exchangedStarDust ++;
        }
        
	}

	@Override
	public void run(int interfaceId, int id) {
	   if(stage == 2) {
		   sendNPCDialogue(8091, NORMAL, "Goodie, I need them to restore my house!");
	      stage = 3;
	   } else if(stage == 3) {
		   sendOptionsDialogue("Select an Option", "Exchange Star Dust", "NeverMind");
		   stage = 4;
		
	   } else if(stage == 4) {
		   if(id == OPTION_1) {
			  giveReward();
		   }
		   end();
	   }
		
	}
	
	@Override
	public void start() {
	  sendPlayerDialogue(NORMAL, "I want to exchange my StarDust");
	  stage = 2;
	}

}
