package com.rs.game.world.entity.player.actions;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

public class CowMilkingAction extends Action {

	public static final int EMPTY_BUCKET = 1925;
	
	public CowMilkingAction() {
		
	}

	@Override
	public boolean start(Player player) {
		if (!player.getInventory().containsItem(EMPTY_BUCKET, 1)) {
			player.getDialogueManager().startDialogue("GrilleGoatsD");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return player.getInventory().hasFreeSlots() && player.getInventory().containsItem(EMPTY_BUCKET, 1);
	}

	@Override
	public int processWithDelay(Player player) {
		player.animate(new Animation(2305));
		player.getInventory().deleteItem(new Item(EMPTY_BUCKET, 1));
		player.getInventory().addItem(new Item(1927));
		player.getPackets().sendGameMessage("You milk the cow.");
		return 5;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
