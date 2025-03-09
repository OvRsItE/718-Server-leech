package com.rs.game.item;

import com.rs.Constants;
import com.rs.cache.loaders.ItemDefinitions;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Represents a single item.
 * <p/>
 * 
 * @author Graham / edited by Dragonkk(Alex)
 */
public class Item implements Serializable {

	private static final long serialVersionUID = -6485003878697568087L;

	private int id;
	private int amount;

	public Item(int id) {
		this(id, 1);
	}

	
	public Item(int id, int amount) {
		this(id, amount, false);
	}

	public Item(int id, int amount, boolean amt0) {
		this.id = id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public Item(Item item) {
		this.id = item.getId();
		this.amount = item.getAmount();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static Item createOSRS(int id) {
		return createOSRS(id, 1);
	}
	
	public static Item createOSRS(int id, int amount) {
		return new Item(asOSRS(id), amount);
	}

	public static int asOSRS(int id) {
		return id + Constants.OSRS_ITEMS_OFFSET;
	}

	public String getName() {
		return getDefinitions().getName();
	}
	
	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getItemDefinitions(id);
	}
	
	@Override
	public Item clone() {
		return new Item(id, amount);
	}

}
