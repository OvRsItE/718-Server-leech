package com.rs.game.world.entity.player.pets;

import java.io.Serializable;
import java.util.ArrayList;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.content.pets.Pets;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * May 8, 2018
 */
public class SkillingPetManager implements Serializable {
	
	private static final long serialVersionUID = 5948408144861522528L;
	
	private transient Player player;
	
	private ArrayList<SkillingPets> obtainedPets;
	
	public SkillingPetManager() {
		obtainedPets = new ArrayList<SkillingPets>();
	}
	
	/**
	 * Reclaim a skilling pet.
	 * @param skill
	 */
	public void claimPet(int skill) {
		SkillingPets pet = SkillingPets.forSkill(skill);
		if (!obtainedPets.contains(pet)) {
			player.sendMessage("You haven't obtained this pet yet.");
		} else if (player.getInventory().getFreeSlots() > 0) {
			player.getInventory().addItem(pet.getItemId(), 1);
			player.sendMessage("<col=00ff00>Your skilling pet has been placed in your inventory.");
		} else {
			player.getBank().addItem(pet.getItemId(), 1, true);
			player.sendMessage("<col=00ff00>Your skilling pet has been placed in your bank.");
		}
	}
	
	/**
	 * Handles the rolling for skilling pets.
	 * @param skill
	 * 				the skill being trained.
	 * @param experience
	 * 				experience amount from training (unused atm).
	 */
	public void rollSkillingPet(int skill, double experience) {	
		SkillingPets pet = SkillingPets.forSkill(skill);
		if (pet == null || obtainedPets.contains(pet))
			return;
		double rate = pet.getRate() - 
				(player.getSkills().getLevel(skill) * 25);	
		boolean rolled_pet = Utils.getRandomDouble(rate) <= 1;
		if (!rolled_pet)
			return;	
		World.sendWorldMessage("<img=4><col=00ff00>News: " + player.getDisplayName() + " has just recieved " + 
					pet.getName() + " the " + Skills.SKILL_NAME[skill] + " pet!", false);
		if (player.getFamiliar() != null || player.getPet() != null || Summoning.hasPouch(player) || Pets.hasPet(player)) {
			if (player.getInventory().getFreeSlots() > 0) {
				player.getInventory().addItem(pet.getItemId(), 1);
				player.sendMessage("<col=00ff00>Your skilling pet has been placed in your inventory.");
			} else {
				player.getBank().addItem(pet.getItemId(), 1, true);
				player.sendMessage("<col=00ff00>Your skilling pet has been placed in your bank.");
			}
		} else {
			player.getPetManager().spawnPet(pet.getItemId(), false);
			player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.");
		}
		obtainedPets.add(pet);		
	}
	
	/**
	 * Gets the players obtained skilling pets.
	 * @return
	 */
	public ArrayList<SkillingPets> getObtainedPets() {
		return obtainedPets;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
