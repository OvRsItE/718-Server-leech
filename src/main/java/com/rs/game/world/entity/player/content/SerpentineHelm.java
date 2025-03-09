package com.rs.game.world.entity.player.content;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

public class SerpentineHelm implements Serializable {

	private static final long serialVersionUID = 8750207833942496007L;

	private static final DecimalFormat FORMATTER = new DecimalFormat("#.#");
	
	@Setter private transient Player player;
	
	private static final int MAX_SCALES = 11_000;
	private static final int SCALES_ID = 29898;
	public static final int CHARGED_HELM = 29929;
	private static final int UNCHARGED_HELM = 29931;
	private static final int SERPENTINE_VISAGE = 29933;
	
	@Getter private int scales;
	
	/**
	 * Handles serp helm item on item
	 * @param used
	 * 			scales
	 * @param usedWith
	 * 			serp helm
	 * @return
	 * 			if item was handled.
	 */
	public boolean handleItemOnItem(Item used, Item usedWith) {
		if (usedWith.getId() == SERPENTINE_VISAGE && used.getId() == 1755 
				|| used.getId() == SERPENTINE_VISAGE && usedWith.getId() == 1755) {
			createSerpHelm();
			return true;
		}
		if (used.getId() == SCALES_ID) {
			switch (usedWith.getId()) {	
				case CHARGED_HELM:
				case UNCHARGED_HELM:
					charge(used, usedWith);
					return true;	
			}
		}
		return false;
	}
	
	/**
	 * Handles creating serp helm.
	 */
	public void createSerpHelm() {
		if (player.getSkills().getLevel(Skills.CRAFTING) >= 52) {
			player.getInventory().deleteItem(SERPENTINE_VISAGE, 1);
			player.getInventory().addItem(UNCHARGED_HELM, 1);
			player.getSkills().addXp(Skills.CRAFTING, 120);
			player.getDialogueManager().startDialogue("ItemMessage", "You create a Serpentine helm from your Serpentine visage.", UNCHARGED_HELM);
		} else {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need at least level 53 crafting to do this.");
		}
	}
	
	/**
	 * Handles charging serp helm.
	 * @param used
	 * @param usedWith
	 */
	public void charge(Item used, Item usedWith) {
		int added = 0;
		if (used.getAmount() + scales > MAX_SCALES) {
			added = MAX_SCALES - scales;
		} else {
			added = used.getAmount();
		}
		System.out.println("Added: " + added);
		player.getInventory().deleteItem(SCALES_ID, added);
		scales += added;
		if (usedWith.getId() == UNCHARGED_HELM) {
			usedWith.setId(CHARGED_HELM);
			player.getInventory().refresh();
		}
	}
	
	/**
	 * Handles uncharing the serp helm.
	 */
	public void uncharge(Item item) {
		if (player.getInventory().containsItem(SCALES_ID, 1) || player.getInventory().hasFreeSlots()) {
			player.getInventory().addItem(SCALES_ID, scales);
			scales = 0;
		} else {
			player.sendMessage("You don't have enough inventory space to uncharge the Serpentine helm.");
		}
		item.setId(UNCHARGED_HELM);
		player.getInventory().refresh();
	}
	
	/**
	 * Checks if serp helm causes venom.
	 * @return
	 */
	public boolean causeVenom() {
		if (!wearing())
			return false;
		double base_chance = 16.7D;
		if (player.getCombatDefinitions().usingPoisonWeapon())
			base_chance = 50;
		if (player.getCombatDefinitions().usingVenomWeapon())
			base_chance = 100D;
		return Utils.randomDouble() * 100 < base_chance;
	}

	/**
	 * Gets information about serp helm.
	 */
	public void check() {
		String scales = FORMATTER.format(getScales() * 100.0 / (double) MAX_SCALES) + "%";
		player.sendMessage("Scales: <col=007F00>" + scales + "</col>");
	}
	
	/**
	 * Handles degrading for serp helm.
	 */
	public void degrade() {
		if (scales <= 0) {
			player.getEquipment().getItems().get(Equipment.SLOT_HAT).setId(UNCHARGED_HELM);
			player.getEquipment().refresh(Equipment.SLOT_HAT);
			player.sendMessage("<col=ff0000>Your Serptentine helmet has ran out of charges.</col>");
			return;
		}
		scales--;
	}
	
	/**
	 * Checks if player is wearing helm.
	 * @return
	 */
	public boolean wearing() {
		int hatId = player.getEquipment().getHatId();
		return hatId == CHARGED_HELM || hatId == 29927 || hatId == 29926;
	}
	
	/**
	 * Checks if an item is a serp helm.
	 * @param itemId
	 * @return
	 */
	public static boolean usingSerptentineHelm(int itemId) {
		return (itemId == UNCHARGED_HELM || itemId == CHARGED_HELM);
	}
	
}
