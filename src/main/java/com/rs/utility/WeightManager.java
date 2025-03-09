package com.rs.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;

/**
 * 
 * @author Cody
 *
 */

public class WeightManager {

	private static Map<Integer, Double> itemWeight = new HashMap<>();

	private static final int[] WEIGHT_REDUCERS = { 88, 10069, 10073,
		10663, 10071, 10074, 10664, 10553, 10554, 24210, 24211, 14938,
		14939, 24208, 24209, 14936, 14937, 24206, 24207, 24560, 24561,
		24562, 24563, 24654, 24801, 24802, 24803, 24804, 24805 };

	public static void init() {
		try (BufferedReader reader = new BufferedReader(new FileReader("./data/items/weights.txt"))) {
			while (true) {
				String file = reader.readLine();
				if (file == null) {
					break;
				}
				if (file.startsWith("//")) {
					continue;
				}
				String[] values = file.split(" - ");
				itemWeight.put(Integer.valueOf(values[0]), Double.parseDouble(values[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double calculateWeight(Player player) {
		player.setWeight(0);
		for (int REDUCERS : WEIGHT_REDUCERS) {
			if (player.getEquipment().getItems().contains(new Item(REDUCERS))) {
				player.setWeight(player.getWeight() - getWeight(REDUCERS));
			}
		} for (int i = 0; i <= Utils.getItemDefinitionsSize(); i++) {
			if (player.getInventory().containsItem(i, 1)) {
				player.setWeight(player.getWeight() + getWeight(i) * player.getInventory().getNumberOf(i));
			}
			else if (player.getEquipment().getItems().contains(new Item(i))) {
				player.setWeight(player.getWeight() + getWeight(i));
			}
		}
		player.getPackets().sendWeight(player.getWeight());
		return player.getWeight();
	}

	public static double getWeight(int itemId) {
		return itemWeight.get(itemId);
	}

}