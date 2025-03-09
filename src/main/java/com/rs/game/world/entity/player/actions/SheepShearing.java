package com.rs.game.world.entity.player.actions;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

public class SheepShearing extends Action {

	public static final int SHEARS = 1735;
	public SheepShearing() {
		
	}

	@Override
	public boolean start(Player player) {
		if (!player.getInventory().containsItemToolBelt(1735)) {
		    player.getPackets().sendGameMessage("You need a pair of shears in order to sheer the sheep.");
		    return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return player.getInventory().hasFreeSlots() && player.getInventory().containsItem(SHEARS, 1);
	}

	@Override
	public int processWithDelay(Player player) {
		player.lock(1);
		player.animate(new Animation(893));
		player.getInventory().addItem(1737, 1);
		
	//transformIntoNPC(1158);
	//getCombatDefinitions().getRespawnDelay(),
	//TimeUnit.MILLISECONDS);
		return 1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 1);
	}

}