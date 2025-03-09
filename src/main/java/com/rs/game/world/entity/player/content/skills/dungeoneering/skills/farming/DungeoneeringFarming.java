package com.rs.game.world.entity.player.content.skills.dungeoneering.skills.farming;


import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;


public class DungeoneeringFarming {

	public static final boolean plantSeed(Player player, Item seed, WorldObject patch) {
		DungeoneeringFarmingData data = null;
		for (DungeoneeringFarmingData d : DungeoneeringFarmingData.values()) {
			if (d.getSeed() == seed.getId()) {
				data = d;
				break;
			}
		}
		if (data == null)
			return false;
		DungeoneeringPatch existingPatch = player.getDungeoneeringManager().getParty().getDungeon().getDungeoneeringPatch(patch);
		if (existingPatch != null) {
			if (existingPatch.getOwner().getDisplayName().equalsIgnoreCase(player.getDisplayName())) {
				player.sendMessage("You already have some crops growing here.");
				return true;
			} else {
				player.sendMessage(existingPatch.getOwner().getDisplayName() + " already has some crops growing here.");
				return true;
			}
		}
		player.animate(new Animation(3659));
		World.spawnObject(new WorldObject(patch.getId() + 1, patch.getType(), patch.getRotation(), patch.getX(), patch.getY(), patch.getZ()));
		DungeoneeringPatch p = new DungeoneeringPatch(patch, player, data);
		player.getDungeoneeringManager().getParty().getDungeon().addDungeoneeringPatch(p);
		initiateGrowingSequence(player, p);
		return true;
	}
	
	private static final void initiateGrowingSequence(final Player player, final DungeoneeringPatch patch) {
		player.lock(4);
		WorldTasksManager.schedule(new WorldTask() {
			int stage = 0;
			int baseId = patch.getPatch().getId() + 1 + ((patch.getData().ordinal() + 1) * 3);
			@Override
			public void run() {
				if (stage == 0) {
					player.getInventory().deleteItem(patch.getData().getSeed(), 1);
					World.spawnObject(new WorldObject(baseId - 2, patch.getPatch().getType(), patch.getPatch().getRotation(), patch.getPatch().getX(), patch.getPatch().getY(), patch.getPatch().getZ()));
					player.sendMessage("You plant a " + ItemDefinitions.getItemDefinitions(patch.getData().getSeed()).getName() + " in the patch.");
				} else if (stage == 1) {
					World.spawnObject(new WorldObject(baseId - 1, patch.getPatch().getType(), patch.getPatch().getRotation(), patch.getPatch().getX(), patch.getPatch().getY(), patch.getPatch().getZ()));
					player.sendMessage("Your " + ItemDefinitions.getItemDefinitions(patch.getData().getSeed()).getName().replaceAll(" seed", "") + " crops have slightly grown.");
				} else {
					World.spawnObject(new WorldObject(baseId, patch.getPatch().getType(), patch.getPatch().getRotation(), patch.getPatch().getX(), patch.getPatch().getY(), patch.getPatch().getZ()));
					player.sendMessage("Your " + ItemDefinitions.getItemDefinitions(patch.getData().getSeed()).getName().replaceAll(" seed", "") + " crops have fully grown and are ready for harvesting.");
					stop();
				}
				stage++;
			}
		}, 0, 50 * patch.getData().getTime());
	}
	
	public static final boolean inspectPatch(final Player player, final WorldObject object) {
		if (!object.getDefinitions().containsOption("Inspect"))
			return false;
		DungeoneeringPatch patch = player.getDungeoneeringManager().getParty().getDungeon().getDungeoneeringPatch(object);
		if (patch == null) {
			player.sendMessage("There are no crops growing in this farming patch.");
			return true;
		}
		int baseId = patch.getPatch().getId() + 1 + ((patch.getData().ordinal() + 1) * 3);
		String prefix = object.getId() == baseId - 2 ? "fresly planted" : "half-grown";
		player.sendMessage("There are some " + prefix + " " + ItemDefinitions.getItemDefinitions(patch.getData().getSeed()).getName().replaceAll(" seed", "") + " crops planted by " + patch.getOwner().getDisplayName() + " growing here.");
		return true;
	}
	
	public static final boolean harvestPatch(final Player player, final WorldObject object) {
		if (!object.getDefinitions().containsOption("Harvest"))
			return false;
		DungeoneeringPatch patch = player.getDungeoneeringManager().getParty().getDungeon().getDungeoneeringPatch(object);
		if (player.getSkills().getLevel(Skills.FARMING) < patch.getData().getLevel()) {
			player.sendMessage("You need a Farming level of at least " + patch.getData().getLevel() + " to harvest this.");
			return true;
		}
		player.animate(new Animation(3659));
		player.lock(2);
		
		
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (player.getInventory().addItemDrop(patch.getData().getProduct(), 1)) {
					player.getPackets().sendGameMessage("You pick a " + ItemDefinitions.getItemDefinitions(patch.getData().getProduct()).getName() + ".", true);
					player.getSkills().addXp(Skills.FARMING, patch.getData().getExperience());
				}
				if (patch.getCropsAmount() <= 0) {
					World.spawnObject(patch.getPatch());
					player.getDungeoneeringManager().getParty().getDungeon().removeDungeoneeringPatch(patch);
					player.sendMessage("You have depleted this resource.");
				}
			}
		}, 2);
		return true;
	}
	
	public static final boolean clearPatch(final Player player, final WorldObject object) {
		if (!object.getDefinitions().containsOption("Harvest"))
			return false;
		DungeoneeringPatch patch = player.getDungeoneeringManager().getParty().getDungeon().getDungeoneeringPatch(object);
		if (player.getSkills().getLevel(Skills.FARMING) < patch.getData().getLevel()) {
			player.sendMessage("You need a Farming level of at least " + patch.getData().getLevel() + " to clear this.");
			return true;
		}
		player.animate(new Animation(3659));
		player.lock(2);
		patch.decrementCrops();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				World.spawnObject(patch.getPatch());
				player.getDungeoneeringManager().getParty().getDungeon().removeDungeoneeringPatch(patch);
				player.sendMessage("You pick and destroy the crops.");
			}
		}, 2);
		return true;
	}
}
