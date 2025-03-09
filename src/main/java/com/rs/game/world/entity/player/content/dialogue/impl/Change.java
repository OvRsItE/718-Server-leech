package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.PlayerLook;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;

public class Change extends Dialogue {



@Override
public void start() {
sendOptionsDialogue("Choose an Option!", "Make Over Mage",
"Thessalias Make Over", "Hair dresser Salon", "Change Display Name", "Remove Display Name");
stage = 2;
}


@Override
public void run(int interfaceId, int componentId) {

if (stage == 2) {
if (componentId == OPTION_1) {
	PlayerLook.openMageMakeOver(player);
} else if (componentId == OPTION_2) {
	PlayerLook.openThessaliasMakeOver(player);

} else if (componentId == OPTION_3) {
	PlayerLook.openHairdresserSalon(player);
	
} else if (componentId == OPTION_4) {
} else if (componentId == OPTION_5) {
} 

} else if (stage == 3) {
if (componentId == OPTION_1) {
end();
} else if (componentId == OPTION_2) {
end();
} else if (componentId == OPTION_3) {
end();
} else if (componentId == OPTION_4) {
end();
} else if (componentId == OPTION_5) {
stage = 4;
sendOptionsDialogue("Choose a another Shop!", "Runes",
"Removed", "Skillcapes Store", "More Shops");
}
} else if (stage == 4) {
if (componentId == OPTION_1) {
end();
} else if (componentId == OPTION_2) {
end();
} else if (componentId == OPTION_3) {
end();
} else if (componentId == OPTION_4) {
stage = 5;
sendOptionsDialogue("Choose a another Shop!", "Sell me your stuff",
"Back To First Shop Listing");
}
} else if (stage == 5) {
if (componentId == OPTION_1) {
end();
} else if (componentId == OPTION_2) {
stage = 2;
sendOptionsDialogue("Choose an Shop!", "Max's Skilling Shop",
"Goodie Shop", "Food, Drinks, Weapons", "Armor Shop", "More Shops");
}
}
}


@Override
public void finish() {

}
} 