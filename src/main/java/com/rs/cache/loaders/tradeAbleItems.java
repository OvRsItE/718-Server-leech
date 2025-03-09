package com.rs.cache.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;



/**
 * A class holding all the range weapon definitions.
 *
 */
public final class tradeAbleItems {
	
	/**
	 * The un-sellable items mapping.
	 */
	private static final Map<Integer, tradeAbleItems> CANT_SELL = new HashMap<Integer, tradeAbleItems>();

	/**
	 * The item id.
	 */
	private final int itemId;
	
	/**
	 * Constructs a new {@code cantSell} {@code Object}.
	 * @param itemId The item id.
	 */
	private tradeAbleItems(int itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * Populates the un-Sellable items mapping.
	 * @return {@code True}.
	 */
	public static boolean initialize() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("data/items/tradeableItems.txt"));
		String string;
        while ((string = reader.readLine()) != null) {
            String[] data = string.split(":");          
            int id = Integer.parseInt(data[0]);
			CANT_SELL.put(id, new tradeAbleItems(id));
			ItemDefinitions item = ItemDefinitions.getItemDefinitions(id);
			if(!item.isStackable()) {
				ItemDefinitions item2 = ItemDefinitions.getItemDefinitions(id + 1);
				if(item2.isNoted()) {
					CANT_SELL.put(id + 1, new tradeAbleItems(id + 1));
				}
			}
        }    
		reader.close();
		return true;
	}

	/**
	 * Gets a un-Sellable items instance from the mapping.
	 * @param id The item id.
	 * @return The instance.
	 */
	public static tradeAbleItems get(int id) {
		return CANT_SELL.get(id);
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}
	
}

