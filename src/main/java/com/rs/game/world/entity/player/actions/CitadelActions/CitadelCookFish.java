package com.rs.game.world.entity.player.actions.CitadelActions;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;

public class CitadelCookFish extends Action {

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
		if(!player.getInventory().containsItem(3703, 1))
			return false;
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		player.animate(new Animation(883));
		player.getInventory().deleteItem(new Item(3703));
		player.getSkills().addXp(Skills.COOKING, player.getCitTypeY() == 512 ? 50 : 10);
		player.getPackets().sendGameMessage("You cook the fish and put it to the side for later.");
		return 4;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		
	}

}
