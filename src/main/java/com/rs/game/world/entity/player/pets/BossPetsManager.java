package com.rs.game.world.entity.player.pets;

import java.io.Serializable;
import java.util.ArrayList;

import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ReverendDread
 * June 7, 2018
 */
public class BossPetsManager implements Serializable {

	private static final long serialVersionUID = -448280902316153912L;

	@Setter private transient Player player;
	
	@Getter private ArrayList<BossPets> obtainedPets;
	@Getter private ArrayList<BossPets> insuredPets;
	
	public BossPetsManager() {
		obtainedPets = new ArrayList<BossPets>();
		insuredPets = new ArrayList<BossPets>();
	}
	
	/**
	 * Reclaim a skilling pet.
	 * @param itemId
	 */
	public void claimPet(int itemId) {
		BossPets pet = BossPets.forItemId(itemId);
		if (!obtainedPets.contains(pet)) {
			player.sendMessage("You haven't insured this pet.");
		} else if (player.getInventory().getFreeSlots() > 0) {
			player.getInventory().addItem(pet.getItemId(), 1);
			player.sendMessage("<col=00ff00>Your boss pet has been placed in your inventory.");
		} else {
			player.getBank().addItem(pet.getItemId(), 1, true);
			player.sendMessage("<col=00ff00>Your boss pet has been placed in your bank.");
		}
	}
	
	/**
	 * Handles the rolling for boss pets.
	 * @param bossId
	 * 			the boss npc id.
	 */
	public void rollBossPet(int bossId) {
		BossPets pet = BossPets.forBossId(bossId);
		if (pet == null || obtainedPets.contains(pet))
			return;
		double rate = pet.getRate();
		boolean rolled_pet = Utils.getRandomDouble(rate) <= 1;
		if (!rolled_pet)
			return;
		World.sendWorldMessage("<img=4><col=00ff00>News: " + player.getDisplayName() + " has just recieved a " + pet.getName() + " pet drop!", false);
		player.getItemCollectionManager().handleCollection(new Item(pet.getItemId()));
		if (player.getFamiliar() != null || player.getPet() != null || Summoning.hasPouch(player) || Pets.hasPet(player)) {
			if (player.getInventory().getFreeSlots() > 0) {
				player.getInventory().addItem(pet.getItemId(), 1);
				player.sendMessage("<col=00ff00>Your boss pet has been placed in your inventory.");
			} else {
				player.getBank().addItem(pet.getItemId(), 1, true);
				player.sendMessage("<col=00ff00>Your boss pet has been placed in your bank.");
			}
		} else {
			player.getPetManager().spawnPet(pet.getItemId(), false);
			player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.");
		}
		obtainedPets.add(pet);
	}
	
	/**
	 * Insures the players pet if they have enough gold.
	 * @param petId
	 * 			the pets item id.
	 */
	public void insurePet(int petId) {
		BossPets pet = BossPets.forItemId(petId);
		if (pet == null || !obtainedPets.contains(pet)) {
			player.sendMessage("You can't insure this pet.");
			return;
		}
		if (getInsuredPets().contains(pet)) {
			player.sendMessage("You've already insured this pet before.");
		} else if (player.getInventory().getCoinsAmount() < 500_000) {
			player.sendMessage("You can't afford to insure your pet. You need at least 500,000 gold.");
		} else {
			player.getInventory().removeItemMoneyPouch(995, 500_000);
			player.sendMessage("Your pet is now insured, if you lose it you can reclaim it for 1,000,000 gold.");
			player.getBossPetsManager().getInsuredPets().add(pet);
		}
	}
	
	/**
	 * Reclaims all the desired pets if the player has the funds.
	 * @param pets
	 * 			the pets to claim.
	 */
	public void reclaimPets(ArrayList<BossPets> pets) {
		pets.forEach((pet) -> {
			if (player.getMoneyPouch().sendDynamicInteraction(1_000_000, true))
				claimPet(pet.getItemId());
			else 
				player.sendMessage("You can't claim " + pet.getName() + ", because you don't have enough money.");
		});
	}
	
	/**
	 * Gets the ids of pets items that are insured and the player doesn't have.
	 * @return
	 * 			the ids of the pets items.
	 */
	public final ArrayList<BossPets> getReclaimablePets() {
		ArrayList<BossPets> pets = player.getBossPetsManager().getObtainedPets();
		ArrayList<BossPets> claiming = new ArrayList<BossPets>();
		for (BossPets pet : pets) {
			if (player.getInventory().containsItem(pet.getItemId(), 1) || player.getBank().containsItem(pet.getItemId(), 1))
				continue;
			if (player.getFamiliar() != null && player.getFamiliar().getId() == pet.getNpcId())
				continue;
			claiming.add(pet);
		}
		return claiming;
	}
	
}
