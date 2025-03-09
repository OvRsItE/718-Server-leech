package com.rs.game.world.entity.player.content.skills.dungeoneering;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.impl.DungeonController;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Paolo
 * handles food inside of dungeoneering
 */
public class DungFoods {

	public static enum Foods {

		/**
		 * Fish
		 */
		HEIM_CRAB(18159,20),
		RED_EYE(18161,25),
		DUSK_EEL(18163,50),
		GIANT_FLATFISH(18165,75),
		SHORT_FINNED_EEL(18167,100),
		WEB_SNIPPER(18169,125),
		BOULDABASS(18171,150),
		SALVE_EEL(18173,175),
		CAVE_MORAY(18177,250),
		BLUE_CRAB(18175,200);
		

		/**
		 * The food id
		 */
		private int id;

		/**
		 * The healing health
		 */
		private int heal;

		/**
		 * A map of object ids to foods.
		 */
		private static Map<Integer, Foods> foods = new HashMap<Integer, Foods>();

		/**
		 * Gets a food by an object id.
		 * 
		 * @param itemId
		 *            The object id.
		 * @return The food, or <code>null</code> if the object is not a food.
		 */
		public static Foods forId(int itemId) {
			return foods.get(itemId);
		}

		/**
		 * Populates the tree map.
		 */
		static {
			for (final Foods food : Foods.values()) {
				foods.put(food.id, food);
			}
		}

		/**
		 * Represents a food being eaten
		 * 
		 * @param id
		 *            The food id
		 * @param heal
		 *            The healing health received
		 */
		private Foods(int id, int heal) {
			this.id = id;
			this.heal = heal;
		}


		

		/**
		 * Gets the id.
		 * 
		 * @return The id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the exp amount.
		 * 
		 * @return The exp amount.
		 */
		public int getHeal() {
			return heal;
		}

	}


	public static final Animation EAT_ANIM = new Animation(829);

	public static boolean eat(final Player player, Item item, int slot) {
		Foods food = Foods.forId(item.getId());
		if (food == null)
			return false;
		if (player.getFoodDelay() > com.rs.utility.Utils.currentTimeMillis())
			return true;
		if(!(player.getControlerManager().getControler() instanceof DungeonController)){
			player.getInventory().deleteItem(new Item(food.getId(), 1));
			player.sm("Please contact an admin on how you smuggled this item.");
			return false ;
		}
		String name = ItemDefinitions.getItemDefinitions(food.getId())
				.getName().toLowerCase();
		player.getPackets().sendGameMessage("You eat the " + name + ".");
		player.animate(EAT_ANIM);
		long foodDelay = name.contains("half") ? 600 : 1800;
		player.getActionManager().setActionDelay((int) foodDelay / 1000);
		player.getInventory().deleteItem(new Item(food.getId(), 1));
		player.addFoodDelay(foodDelay);
		player.getActionManager().setActionDelay(
				player.getActionManager().getActionDelay() + 2);
		
		player.getInventory().refresh(slot);
		int hp = player.getMaxHitpoints();
		if (player.getHitpoints() < hp){
			player.applyHit(new Hit(player, food.getHeal(), Hit.HitLook.HEALED_DAMAGE)); // like the real dung damn boy
			player.getPackets().sendGameMessage("It heals some health.");
		}
		player.getInventory().refresh();
		
		return true;
	}
}