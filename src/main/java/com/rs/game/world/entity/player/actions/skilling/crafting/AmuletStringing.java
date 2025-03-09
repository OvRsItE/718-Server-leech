/**
 * 
 */
package com.rs.game.world.entity.player.actions.skilling.crafting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;

import lombok.Getter;

/**
 * @author ReverendDread
 * Nov 27, 2018
 */
public class AmuletStringing extends Action {

	public enum Amulets {
		
		GOLD(1673, 1692, 4, 8),
		SAPPHIRE(1675, 1694, 4, 25),
		EMERALD(1677, 1696, 4, 31),
		RUBY(1679, 1698, 4, 50),
		DIAMOND(1681, 1700, 4, 70),
		DRAGONSTONE(1683, 1702, 4, 80),
		ONYX(6579, 6581, 4, 90),
		ZENYTE(29162, 29145, 4, 98);
		
		@Getter private int unstrung, strung, level;
		@Getter private double experience;
		
		private static final Amulets[] VALUES = Amulets.values();
		
		private Amulets(int unstrung, int strung, double experience, int level) {
			this.unstrung = unstrung;
			this.strung = strung;
			this.experience = experience;
			this.level = level;
		}
		
		public static Amulets forId(int unstrung) {
			for (Amulets amulet : VALUES) {
				if (amulet.getUnstrung() == unstrung)
					return amulet;
			}
			return null;
		}
		
	}
	
	/** The amulet data */
	private final Amulets amulet;
	
	/** Ball of wool id */
	private static final int BALL_OF_WOOL = 1759;
	
	/**
	 * AmuletStringing constructor.
	 * @param unstrung
	 * 			the unstrung amulet id.
	 */
	public AmuletStringing(Amulets amulet) {
		this.amulet = amulet;
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.actions.Action#start(com.rs.game.world.entity.player.Player)
	 */
	@Override
	public boolean start(Player player) {
		if (!player.getInventory().contains(new Item(amulet.getUnstrung()), new Item(BALL_OF_WOOL))) {
			player.sendMessage("You don't have the required items to make this.");
			return false;
		}
		if (!(player.getSkills().getLevel(Skills.CRAFTING) > amulet.getLevel())) {
			player.sendMessage("You don't have the required level to make this.");
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.actions.Action#process(com.rs.game.world.entity.player.Player)
	 */
	@Override
	public boolean process(Player player) {
		if (!player.getInventory().contains(new Item(amulet.getUnstrung()), new Item(BALL_OF_WOOL))) {
			player.sendMessage("You don't have the required items to make this.");
			return false;
		}
		if (!(player.getSkills().getLevel(Skills.CRAFTING) > amulet.getLevel())) {
			player.sendMessage("You don't have the required level to make this.");
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.actions.Action#processWithDelay(com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int processWithDelay(Player player) {
		player.getInventory().deleteItem(new Item(amulet.getUnstrung(), 1));
		player.getInventory().deleteItem(new Item(BALL_OF_WOOL, 1));
		player.getInventory().addItem(new Item(amulet.getStrung(), 1));
		player.getSkills().addXp(Skills.CRAFTING, amulet.getExperience());
		player.getPackets().sendGameMessage("You string the " + ItemDefinitions.forId(amulet.getUnstrung()).getName() + ".", true);
		return 2;
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.actions.Action#stop(com.rs.game.world.entity.player.Player)
	 */
	@Override
	public void stop(Player player) {
		setActionDelay(player, 2);
	}
	
}
