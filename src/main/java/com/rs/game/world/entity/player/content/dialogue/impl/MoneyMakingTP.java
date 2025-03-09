package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.activities.RecipeforDisaster;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MoneyMakingTP extends Dialogue {

    @Override
    public void start() {
	sendOptionsDialogue("would you like to enter the RFD?", "Yes", "No");
	stage = 1;
    }

    @Override
    public void run(int interfaceId, int componentId) {
	if (stage == 1) {
		if (componentId == OPTION_1) {
			sendDialogue("You cannot use any prayer in this minigame!",
					         "Are you sure you want to continue?");
			stage = 2;
		} else if (componentId == OPTION_2) {
			end();
		}
	} else if (stage == 2) {
		sendOptionsDialogue("", "Yes im fearless!", "No way!");
		stage = 3;
	} else if (stage == 3) {
		 if (componentId == OPTION_1) {
			 RecipeforDisaster.enterRfd(player);
			 end();
			 player.sm("TIP: They have different weakneses..");
		 } else if (componentId == OPTION_2) {
			 end();
		 }
	 }
 }

    @Override
    public void finish() {

    }

}
