package com.rs.game.world.entity.player.content;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.world.entity.player.Player;

public class RepairStand {
	
	public enum RepairableItem {
		
		TORVA_HELMS(new int[]    {20137, 20138}, 20135, 500000),
		TORVA_BODIES(new int[]   {20141, 20142}, 20139, 2000000),
		TORVA_LEGS(new int[]     {20145, 20146}, 20143, 1000000),
		PERNIX_HELMS(new int[]   {20149, 20150}, 20147, 500000),
		PERNIX_BODIES(new int[]  {20153, 20154}, 20151, 2000000),
		PERNIX_LEGS(new int[]    {20157, 20158}, 20155, 1000000),
		VIRTUS_HELMS(new int[]   {20161, 20162}, 20159, 500000),
		VIRTUS_BODIES(new int[]  {20165, 20166}, 20163, 2000000),
		VIRTUS_LEGS(new int[]    {20169, 20170}, 20167, 1000000),
		ZARYTE_BOW(new int[]     {20173, 20174}, 20171, 2000000),
		CHAOTIC_RAPIER(new int[] {18350}, 18349, 2000000),
		CHAOTIC_LONG(new int[]   {18352}, 18351, 2000000),
		CHAOTIC_MAUL(new int[]   {18354}, 18353, 2000000),
		CHAOTIC_STAFF(new int[]  {18356}, 18355, 2000000),
		CHAOTIC_CBOW(new int[]   {18358}, 18357, 2000000),
		CHAOTIC_KITE(new int[]   {18360}, 18359, 2000000),
		EAGLE_EYE_KITE(new int[] {18362}, 18361, 2000000),
		FARSEER_KITE(new int[]   {18364}, 18363, 2000000), 
		FROZEN_KEY(new int[] { 20120 }, 20120, 50000);
		
		private int[] id;
		private int fixedId;
		private int cost;
		private String name;

		private static Map<Integer, RepairableItem> armors = new HashMap<Integer, RepairableItem>();

		static {
			for (RepairableItem armor : RepairableItem.values()) {
				for (int i = 0;i < armor.getId().length;++i)
					armors.put(armor.getId()[i], armor);
			}
		}

		public static RepairableItem forId(int id) {
			return armors.get(id);
		}

		private RepairableItem(int[] id, int fixedId, int cost) {
			this.id = id;
			this.fixedId = fixedId;
			this.cost = cost;
			this.name = ItemDefinitions.getItemDefinitions(fixedId).getName();
		}

		public int[] getId() {
			return id;
		}
		
		public int getFixedId() {
			return fixedId;
		}
		
		public String getName() {
			return name;
		}

		public int getCost() {
			return cost;
		}
	}
	
	public static void handleRepairs(Player player, int itemId) {
		if (!player.getInventory().containsItem(itemId, 1)) {
			return;
		}
		RepairableItem item = RepairableItem.forId(itemId);
		if (item == null) {
			player.getPackets().sendGameMessage("This item cannot be repaired.");
		} else {
			player.getDialogueManager().startDialogue("RepairStandD", item, itemId);
		}
	}
}
