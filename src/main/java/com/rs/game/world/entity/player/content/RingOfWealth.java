package com.rs.game.world.entity.player.content;

import com.rs.game.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

public class RingOfWealth {

	public enum rare_drop {
		COMMON(new Object[][] { { 995, 100000, 200000 }, { 989, 1, 1 }, { 565, 100, 350 }, { 5698, 1, 1 }, { 4131, 1, 1 },
								{ 1520, 50, 150 }, { 1620, 3, 5 }, { 208, 3, 6 }, }), 
		UNCOMMON(new Object[][] { { 995, 150000, 300000 }, { 990, 1, 2 }, { 565, 250, 500 }, { 5699, 5, 5 },
								{ 1520, 100, 200 }, { 1620, 10, 10 }, { 208, 10, 10 }, { 1631, 1, 1}, }), 
		RARE(new Object[][] { { 537, 20, 30 }, { 18831, 20, 30 },
								 { 990, 5, 5 }, { 4087, 1, 1 },
								{ 9191, 100, 200},  { 15271, 30, 100}, { 384, 30, 100}, { 1516, 30, 100},}), 
		ULTRARARE(new Object[][] {{ 995, 5000000, 1000000 }, { 990, 3, 3 }, { 7776, 2, 2},
								{ 208, 30,100 }, { 9242, 1000, 1000 },});

		@SuppressWarnings("unused")
		private int id;
		private Object[][] data;

		@SuppressWarnings("unused")
		private rare_drop drops1;

		private rare_drop(Object[][] data) {
			this.data = data;
		}

	}


	public static RingOfWealth random(Player player, rare_drop drops1) {
		RingOfWealth drop = null;
		while (true) {
			int random = Utils.random(drops1.data.length - 1);
			int itemid = (Integer) drops1.data[random][0];
			int min = (Integer) drops1.data[random][1];
			int max = (Integer) drops1.data[random][2];
			if (drop == null) {
				drop = new RingOfWealth(drops1, itemid, Utils.random(min, max));
			}
			break;
		}
		return drop;
	}
	@SuppressWarnings("unused")
	private rare_drop drop;
	private int itemId;

	private int Amount;

	public RingOfWealth(rare_drop drops1, int itemId, int Amount) {
		this.drop = drops1;
		this.itemId = itemId;
		this.Amount = Amount;
	}

	public int getAmount() {
		return Amount;
	}

	public int getItemId() {
		return itemId;
	}

}