package com.rs.game.world.entity.player.actions;

import java.util.ArrayList;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;

public class Crushing extends Action {
	
	public enum Items {
		
		UNICORN_HORN(237, 1, 235, 1),
		CHOCOLATE_BAR(1973, 1, 1975, 1),
		KEBBIT_TEETH(10109, 1, 10111, 1),
		BIRD_NEST(5075, 1, 6693, 1),
		GOAT_HORN(9735, 1, 9736, 1),
		DRAGON_SCALE(243, 1, 241, 1),
		MUD_RUNE(4698, 1, 9594, 1),
		GORAK_CLAW(9016, 1, 9018, 1),
		SPRING_SQ_IRK(10844, 1, 10848, 1),
		SUMMER_SQ_IRK(10845, 1, 10849, 1),
		AUTUMN_SQ_IRK(10846, 1, 10850, 1),
		WINTER_SQ_IRK(10847, 1, 10851, 1),
		CHARCOAL(973, 1, 704, 1),
		RUNE_SHARDS(6466, 1, 6467, 1),
		ASHES(592, 1, 8865, 1),
		POISON_KARAMBWAN(3146, 1, 3152, 1),
		SUQAH_TOOTH(9079, 1, 9082, 1),
		FISHING_BAIT(313, 1, 12129, 1),
		DIAMOND_ROOT(14703, 1, 14704, 1),
		BLACK_MUSHROOM(4620, 1, 4622, 1),
		WYVERN_BONES(6812, 1, 6810, 1);
	
		
		private int raw;
		private int amount;
		private int produce;
		private int xp;
		
		Items(int raw, int amount, int produce, int xp) {
			this.raw = raw;
			this.amount = amount;
			this.produce = produce;
			this.xp = xp;
		}
		
		public int getRaw() {
			return raw;
		}
		
		public int getAmount() {
			return amount;
		}
		
		public int getProduce() {
			return produce;
		}
		
		public int getXp() {
			return xp;
		}
	}
		
		private Items items;
		private static Item item;
		
		public Crushing(Items items, Item item) {
			this.items = items;
			Crushing.item = item;
		}
		
		private boolean checkAll(Player player) {
			if (!player.getInventory().containsOneItem(233)) {
				player.sm("You must have a pestle and mortar to do this.");
				return false;
			}
			if (!player.getInventory().containsItem(items.raw, 1)) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You've ran out of stuff.");
				return false;
			}
			return true;
		}
		
		public static boolean crushItem(Player player, Item item) {
			for(Items items : Items.values())
				if(items.raw == item.getId()) {
					player.getActionManager().setAction(new Crushing(items, item));
					return true;
				}
			return false;
		}
		
		public static void addClay(Player player, WorldObject object) {
			
			ArrayList<Items> possiblities = new ArrayList<Items>();
			for(Items items : Items.values())
				if(player.getInventory().containsItem(items.raw, 1)) 
					possiblities.add(items);
			Items[] items = possiblities.toArray(new Items[possiblities.size()]);
			if(items.length == 0)
				player.getPackets().sendGameMessage("You do not have any clay.");
			else if(items.length == 1)
				player.getActionManager().setAction(new Crushing(items[0], item));
		}

		@Override
		public boolean start(Player player) {
			if(checkAll(player)) {
				return true;
			}
			return false;
				
		}

		@Override
		public boolean process(Player player) {
			if(checkAll(player)) {
				return true;
			}
			return false;
		}

		@Override
		public int processWithDelay(Player player) {
			player.getInventory().deleteItem(items.raw, 1);
			player.getInventory().addItem(items.produce, 1);
			player.getSkills().addXp(Skills.HERBLORE, items.xp);
			player.randomevent(player);
			player.animate(new Animation(364));
			return 2;
		}

		@Override
		public void stop(final Player player) {
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.animate(new Animation(-1));
					player.getAppearence().setRenderEmote(-1);
					
				}
				
			}, 3);
		}
		
	}