package com.rs.game.world.entity.player.actions.CitadelActions;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.updating.impl.Animation;

public class CitadelGrabFish extends Action {

	@Override
	public boolean start(Player player) {
		if(checkAll(player)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	private boolean checkAll(Player player) {
		if(!player.getInventory().hasFreeSlots())
			return false;
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		player.animate(new Animation(881));
		player.getInventory().addItemMoneyPouch(new Item(3703));
		player.getPackets().sendGameMessage("You take a fish off the counter, you should cook it next.");
		return 4;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		
	}

}
