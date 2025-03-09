package com.rs.game.world.entity.player.content.presetsmanager;

import java.io.Serializable;

import com.rs.game.item.Item;

public class PresetObject implements Serializable {
	private static final long serialVersionUID = 443121955332830916L;
	
	/**
	 * Skill indexes
	 */
	public static int ATTACK_INDEX = 0,
					STRENGTH_INDEX = 2,
					DEFENCE_INDEX = 1,
					RANGE_INDEX = 4,
					MAGIC_INDEX = 6,
					PRAYER_INDEX = 5,
					HITPOINTS_INDEX = 3;

	private int[] skills;
	private Item[] equipment;
	private Item[] inventory;
	private boolean prayer;//true = curses
	private int spellBook;
	
	public PresetObject(int[] skills, Item[] equipment, Item[] inventory, 
			boolean prayer, int spellBook) {
		this.skills = skills;
		this.equipment = equipment;
		this.inventory = inventory;
		this.prayer = prayer;
		this.spellBook = spellBook;
	}

	public int[] getSkills() {
		return skills;
	}

	public void setSkills(int[] skills) {
		this.skills = skills;
	}

	public Item[] getEquipment() {
		return equipment;
	}

	public void setEquipment(Item[] equipment) {
		this.equipment = equipment;
	}

	public Item[] getInventory() {
		return inventory;
	}

	public void setInventory(Item[] inventory) {
		this.inventory = inventory;
	}

	public boolean isPrayer() {
		return prayer;
	}

	public void setPrayer(boolean prayer) {
		this.prayer = prayer;
	}

	public int getSpellBook() {
		return spellBook;
	}

	public void setSpellBook(int spellBook) {
		this.spellBook = spellBook;
	}
	
}
