/**
 * 
 */
package com.rs.game.world.entity.player.actions.skilling.thieving;

import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * @author ReverendDread
 * Dec 16, 2018
 */
public class RoguesChests {

	enum Rewards {
		
		UNCUT_EMERALD(1622, 5, 5),
		UNCUT_SAPPHIRE(1624, 6, 6),
		COINS(995, 1000, 1000),
		RAW_TUNA(360, 15, 15),
		ASHES(593, 25, 25),
		TINDERBOX(591, 3, 3),
		MIND_RUNE(558, 25, 25),
		DIAMOND(1602, 1, 3),
		CHAOS_RUNE(562, 40, 40),
		DEATH_RUNE(560, 30, 30),
		FIRE_RUNE(554, 30, 30),
		PIKE(352, 10, 10),
		COAL(454, 13, 13),
		IRON_ORE(441, 10, 10),
		SHARK(386, 10, 10),
		DRAGONSTONE(1616, 2, 2);
		
		@Getter private int id, min, max;
		
		private static final Rewards[] VALUES = values();
		
		private Rewards(int id, int min, int max) {
			this.id = id;
			this.min = min;
			this.max = max;
		}
		
		public static Rewards getReward() {
			int random = Utils.getRandom(VALUES.length);
			return VALUES[random];
		}
		
	}
	
	/**
	 * Handles opening the chest.
	 * @param player
	 * 			the player.
	 * @param object
	 * 			the object.
	 */
	public static final void openChest(Player player, WorldObject object) {
		if (player.getSkills().getLevel(Skills.THIEVING) < 84) {
			player.sendMessage("You fail to open the chest, you need at least level 84 in Theiving to successfully open it.");
			player.applyHit(new Hit(player, (int) (player.getHitpoints() * 0.6), HitLook.REGULAR_DAMAGE));
			return;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.sendMessage("You don't have enough inventory spaces to do that.");
			return;
		}
		WorldObject openedChest = new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getZ());
		World.temporarilyReplaceObject(object, openedChest, 2000, false);
		player.getSkills().addXp(Skills.THIEVING, 100);
		Rewards reward = Rewards.getReward();
		player.getInventory().addItem(reward.getId(), Utils.random(reward.getMin(), reward.getMax()));
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.isDead() || npc.isFinished() || npc.withinDistance(player, 5) || npc.getId() != 26603) {
				continue;
			}
			npc.setTarget(player);
			npc.setNextForceTalk(new ForceTalk("Someone's stealing from us, get them!"));
		}	
	}
	
	//TODO
	public static final void checkForTraps() {
		
	}
	
}
