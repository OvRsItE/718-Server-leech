package com.rs.game.world.entity.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;

/**
 * @author Andy || ReverendDread Aug 4, 2017
 * 
 * Handles dyable items.
 */
public class DyeHandler {

	private static final int[] BASE_ITEMS = {
			//santa hat		//red phat		//yellow phat
				1050, 				1038, 			1040, 
			//blue phat		//green phat	//pure phat
				1042, 				1044, 			1046, 
			//white partyhat   //green h'ween	//blue h'ween
				1048, 				1053, 			1055,
			//red h'ween   //drygore longsword		drygore mace
				1057, 				29754, 			29752,
			//Dry rap			//seismic wand		//seismic singularity
				29967, 				29885, 			29883,
			//Seismic wand	//Seismic singularity
				28617, 				28621, 
			//Ascension cbow
				28437
	};
	
	//TODO Blood dyed items
	enum Dyes {
		
		/**
		 * Add them in the same position in base items as dyed items otherwise it'll fuck up.
		 */

		BARROWS(29804, new int[] { -1, -1, -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1, 29554 , 29552 , 29550, 29768, 29764}),

		SHADOW(29805, new int[] { 29136, 29134, 29134, 29134, 29134, 29134, 29134, 29132, 29132, 29132, 29584, 29582, 29580, 29772, 29770  }),
		
		THIRD_AGE(29806, new int[] {29241,  -1, -1 , -1 , -1 , -1 , -1, -1 , -1 , 29560, 29558 , 29556, 29776, 29774 }),
		
		CAMO(29562, new int[] {29182,  29180, 29180, 29180, 29180, 29180, 29180, 29178, 29178, 29178, -1 , -1 , -1}),
		
		RAINBOW(29563, new int[] {29174, 29172, 29172, 29172, 29172, 29172, 29172, 29170, 29170, 29170, -1 , -1 , -1}),
		
		INFERNAL(29564, new int[] {29248, 29246, 29246, 29246, 29246, 29246, 29246, 29244, 29244, 29244, -1 , -1 , -1}),
		
		LAVA(29565, new int[] {29130, 29128, 29128, 29128, 29128, 29128, 29128, 29126, 29126, 29126, -1 , -1 , -1});
		
		
		private int dye;
		private int[] dyedItems;
		
		private Dyes(int dye, int[] dyedItems) {
			this.dye = dye;
			this.dyedItems = dyedItems;
		}
		
		public int getDye() {
			return dye;
		}
		
		public int[] getDyedItems() {
			return dyedItems;
		}
	
	}
	
	/**
	 * Handles the dyeing of items.
	 * @param player 
	 * 			{@link Player} the player dying the item.
	 * @param dye	
	 * 			{@link Item} the dye.
	 * @param item	
	 * 			{@link Item} the item used on.
	 * @return
	 * 			true if the item was dyed, false otherwise.
	 */
	public static boolean usingDye(Player player, Item dye, Item item) {
		int position = 0;
		if (!player.getInventory().contains(dye, item))
			return false;
		for (Dyes data : Dyes.values()) {
			if (data.getDye() == dye.getId()) {
				for (int base : BASE_ITEMS) {
					if (item.getId() == base) {
						if (data.getDyedItems()[position] == -1) //Makes blank spots not delete the dye
                            continue;
						player.getInventory().deleteItem(item);
						player.getInventory().deleteItem(dye);
						player.getInventory().addItem(data.getDyedItems()[position], 1);
						player.sendMessage("You pour the " + dye.getName() + " on your " + ItemDefinitions.getItemDefinitions(base).getName() + ".");
						return true;
					}
					position++;
				}
			}
		}
		return false;
	}
	
}
