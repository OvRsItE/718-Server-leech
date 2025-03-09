package com.rs.game.world.entity.player.actions.CitadelActions;

import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.updating.impl.Animation;

public class CitadelGrabOre extends Action {

	private WorldObject container;

	public CitadelGrabOre(WorldObject container) {
		this.container = container;
	}

	
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
		player.getInventory().addItemMoneyPouch(container.getId() == 27201 || container.getId() == 28073 ? new Item(24517) : new Item(24515));
		player.getPackets().sendGameMessage("You take an ore from the container, use it on the furnace in order to make good use.");
		return 4;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
