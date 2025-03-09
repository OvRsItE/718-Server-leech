package com.rs.game.item;

import java.io.File;
import java.io.Serializable;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Logger;
import com.rs.utility.SerializableFilesManager;
import com.rs.utility.Utils;

/**
 * @author KingFox
 */
public class ItemManager implements Serializable {
	private static final long serialVersionUID = -9028730399175475751L;
	
	public int itemId;
	public int value;
	public String itemName;
	
	public static ItemManager[] values;
	
	public ItemManager(int itemid, int value, String name) {
		this.itemId = itemid;
		this.value = value;
		this.itemName = name;
	}

	private static final String PATH = "data/ItemPrices.ser";
	
	public static void inits() {
		File file = new File(PATH);
		if (file.exists())
			try {
				values = (ItemManager[]) SerializableFilesManager.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		values = new ItemManager[30000];
	}
	
	public static final void save() {
		try {
			SerializableFilesManager.storeSerializableClass(values, new File(PATH));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	public static String getName(int itemId) {
		return values[itemId].itemName;
	}
	
	public static boolean update(int itemId, int newPrice) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null)
				continue;
			if (values[i].itemId == itemId) {
				values[i].value = newPrice;
			}
		}
		save();
		return true;
	}
	
	
	public static int getPrice(int itemId) {
		for (int i = 0; i < values.length; i++) {
			if (ItemManager.values[i] == null) {
				continue;
			}
			if (ItemManager.values[i].itemId == itemId) {
				return values[i].value;
			}
		}switch (itemId) {
		//customs
		case 950://silk
			return 1500;
		case 4151:
			return 1;
		}
		return 1;
	}
	
	public static int getSellPrice(int itemId) {
		for (int i = 0; i < values.length; i++) {
			if (ItemManager.values[i] == null) {
				continue;
			}
			if (ItemManager.values[i].itemId == itemId) {
				return values[i].value;
			}
		}
		switch (itemId) {
		case 4151:

		}
		return 10000;
	}
	
}
