package com.rs.game.world.entity.player.content.skills.dungeoneering.skills.crafting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RingOfKinship;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.utility.Utils;


public class DungeoneeringSpinning extends Action {

	private DungeoneeringSpinningData data;
	private int amount;
	
	public DungeoneeringSpinning(DungeoneeringSpinningData data, int amount) {
		this.data = data;
		this.amount = amount;
	}
	
	@Override
	public boolean process(Player player) {
		if (amount <= 0)
			return false;
		if (!player.getInventory().containsItem(data.getIngredient(), 1)) {
			player.sendMessage("You've ran out of " + ItemDefinitions.getItemDefinitions(data.getIngredient()).getName().toLowerCase() + ".");
			return false;
		}
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.getLevel()) {
			player.sendMessage("You need at least level " + data.getLevel() + " Crafting to spin this.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		amount--;
		player.animate(new Animation(13742));
		if (Utils.random(100) > player.getRingOfKinship().getBoost(RingOfKinship.ARTISAN))
			player.getInventory().deleteItem(data.getIngredient(), 1);
		player.getInventory().addItemDrop(data.getProduct(), 1);
		player.getSkills().addXp(Skills.CRAFTING, data.getExperience());
		return 2;
	}

	@Override
	public boolean start(Player player) {
		if (!player.getInventory().containsItem(data.getIngredient(), 1)) {
			player.sendMessage("You need some " + ItemDefinitions.getItemDefinitions(data.getIngredient()).getName().toLowerCase() + " to spin this.");
			return false;
		}
		return true;
	}

	@Override
	public void stop(Player player) {}

}
