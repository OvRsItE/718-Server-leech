/**
 * 
 */
package com.rs.game.world.entity.player.content.skills.slayer;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.slayer.SuperiorMonster;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * @author ReverendDread
 * Sep 14, 2018
 */
public class SuperiorSlayer {

	/**
	 * Rolls the spawn for a superior creature to spawn.
	 * @param player
	 * @param npc
	 */
	public static void rollSpawn(Player player, NPC npc) {
		SuperiorMonsters monster = SuperiorMonsters.forId(npc.getId());
		if (player.getSlayerManager().getCurrentTask() != null) {
			if (monster != null && player.getSlayerManager().getCurrentTask().getName().equalsIgnoreCase(npc.getName())) {
				boolean success = Utils.random(50) == 1; // 1/200 chance to spawn a superior.
				if (success) {
					new SuperiorMonster(player, monster.getSuperior(), npc.getPosition(), npc.getMapAreaNameHash(),
							npc.canBeAttackFromOutOfArea(), true);
					player.getPackets().sendGameMessage("<col=ff0000>A superior foe has appeared...");
				}
			}
		}
	}
	
	public enum SuperiorMonsters {
		
		CRAWLING_HAND(1648, 27388),
		CAVE_CRAWLER(1600, 27389),
		BANSHEE(1612, 27390),
		ROCKSLUG(1632, 27392),
		COCKATRICE(1620, 27393),
		PYRELORD(1616, 27394),
		BASILISK(1616, 27395),
		INFERNAL_MAGE(1643, 27396),
		BLOODVELD(new int[] { 1618, 6215 }, 27397),
		MUTATED_BLOODVELD(1, 27398),
		JELLY(1640, 27399),
		WARPED_JELLY(27277, 27400),
		CAVE_HORROR(new int[] {4354, 4355}, 27401),
		ABERRERNT_SPECTRE(1607, 27402),
		ABYSSAL_DEMON(1615, 27410),
		DUST_DEVIL(1624, 27404),
		SMOKE_DEVIL(16014, 27406),
		GARGOYLE(1610, 27407),
		NECHRYAEL(1613, 27411),
		DARK_BEAST(2783, 27409),
		KURASK(1609, 27405);
		
		@Getter private int[] origin;
		@Getter private int superior;
		
		public static final SuperiorMonsters[] VALUES = values();
		
		private SuperiorMonsters(int[] origin, int superior) {
			this.origin = origin;
			this.superior = superior;
		}
		
		private SuperiorMonsters(int origin, int superior) {
			this.origin = new int[] { origin };
			this.superior = superior;
		}
		
		/**
		 * Gets superior monster data for the npc id.
		 * @param original
		 * 			the original npc id.
		 * @return
		 * 			the data.
		 */
		public static SuperiorMonsters forId(int original) {
			for (SuperiorMonsters monster : SuperiorMonsters.VALUES) {			
				if (monster.getName().matches(NPCDefinitions.getNPCDefinitions(original).getName().toLowerCase())) {
					return monster;
				}		
				for (int id : monster.origin) {
					if (original == id)
						return monster;
				}
			}
			return null;
		}
		
		/**
		 * Checks if an npc id is a superior monster.
		 * @param id
		 * 			the id to check
		 * @return true if the monster is a superior monster, false otherwise.
		 */
		public static boolean isSuperiorMonster(int id) {
			for (SuperiorMonsters monster : SuperiorMonsters.VALUES) {
				if (monster.getSuperior() == id)
					return true;
			}
			return false;
		}
		
		/**
		 * Formats enum name
		 * @return
		 */
		public String getName() {
			return name().replaceAll("_", " ").toLowerCase();
		}
		
	}
	
}
