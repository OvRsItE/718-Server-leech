package com.rs.game.world.entity.player.actions.CitadelActions;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;

public class CitadelSmeltOre extends Action {

	private Item item;

	public CitadelSmeltOre(Item item) {
		this.item = item;
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
		if(!player.getInventory().containsItem(item.getId(), 1))
			return false;
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		player.animate(new Animation(881));
		player.getInventory().deleteItem(item);
		player.getSkills().addXp(Skills.SMITHING, item.getId() == 24517 ? 50 : 100);
		player.getPackets().sendGameMessage("You smelt the nuggets and put them into the citadels supplies.");
		return 4;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
