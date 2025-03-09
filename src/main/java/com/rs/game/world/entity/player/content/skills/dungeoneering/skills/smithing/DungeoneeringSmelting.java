package com.rs.game.world.entity.player.content.skills.dungeoneering.skills.smithing;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;


public class DungeoneeringSmelting extends Action {

	private DungeoneeringSmeltingData data;
	private int amount;
	
	public DungeoneeringSmelting(DungeoneeringSmeltingData data, int amount) {
		this.data = data;
		this.amount = amount;
	}

	@Override
	public boolean process(Player player) {
		if (amount <= 0)
			return false;
		if (player.getSkills().getLevel(Skills.SMITHING) < data.getLevel()) {
			player.sendMessage("You need at least level " + data.getLevel() + " to smelt a bar of " + ItemDefinitions.getItemDefinitions(data.getBarId()).getName().replace(" bar", "."));
			return false;
		}
		if (!player.getInventory().containsItem(data.getOreId(), 1)) {
			player.sendMessage("You need some " + ItemDefinitions.getItemDefinitions(data.getOreId()).getName().toLowerCase() + " to smelt this.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		amount--;
		
			player.animate(new Animation(3243));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInventory().addItem(data.getBarId(), 1);
				player.getSkills().addXp(Skills.SMITHING, data.getExperience());
			}
		}, 3);
		player.getInventory().deleteItem(data.getOreId(), 1);
		return 5;
	}

	@Override
	public boolean start(Player player) {
		if (process(player))
			return true;
		return false;
	}

	@Override
	public void stop(Player player) {

	}

}
