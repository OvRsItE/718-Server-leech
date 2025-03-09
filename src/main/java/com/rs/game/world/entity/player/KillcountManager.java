/**
 * 
 */
package com.rs.game.world.entity.player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.NPCDefinitions;

import lombok.Setter;

/**
* @author Andy || ReverendDread Sep 2, 2017
*/
public class KillcountManager implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -2857474148328074243L;
	
	/**
	 * The map of kills.
	 */
	private Map<String, Integer> kills;
	
	/**
	 * The player
	 */
	@Setter private transient Player player;

	/**
	 * Constructor
	 * @param player {@link Player} the player.
	 */
	public KillcountManager() {
		kills = new HashMap<String, Integer>();
	}
	
	/**
	 * Increases count by 1 and returns the amount.
	 * @param id
	 * @return
	 */
	public int incremenetAndGet(int id) {
		String name = NPCDefinitions.getNPCDefinitions(id).getName();
		Integer current = kills.get(name) == null ? 0 : kills.get(name);
		kills.put(name, current += 1); 
		player.getPackets().sendGameMessage("You now have: <col=ff0000>" + kills.get(name) + 
				"</col>x " + NPCDefinitions.getNPCDefinitions(id).getName() + " kills.", true);
		return kills.get(name);
	}
	
	/**
	 * Decreases count by 1 and returns the amount.
	 * @param id
	 * @return
	 */
	public int deincrementAndGet(int id) {
		String name = NPCDefinitions.getNPCDefinitions(id).getName();
		Integer current = kills.get(name) == null ? 0 : kills.get(name);
		kills.put(name, current -= 1); 
		player.getPackets().sendGameMessage("You now have: <col=ff0000>" + kills.get(name) + 
				"</col>x " + NPCDefinitions.getNPCDefinitions(id).getName() + " kills.", true);
		return kills.get(name);
	}
	
	/**
	 * Sets count to amount and returns the amount.
	 * @param id
	 * @return
	 */
	public int setAndGet(int id, int amount) {
		String name = NPCDefinitions.getNPCDefinitions(id).getName();
		kills.put(name, amount); 
		player.getPackets().sendGameMessage("You now have: <col=ff0000>" + kills.get(name) + 
					"</col>x " + NPCDefinitions.getNPCDefinitions(id).getName() + " kills.", true);
		return kills.get(name);
	}
	
	/**
	 * Increases count by 1 and returns the amount.
	 * @param id
	 * @return
	 */
	public int incremenetAndGet(int id, int amount) {
		String name = NPCDefinitions.getNPCDefinitions(id).getName();
		kills.put(name, + amount); 
		player.getPackets().sendGameMessage("You now have: <col=ff0000>" + kills.get(name) + 
					"</col>x " + NPCDefinitions.getNPCDefinitions(id).getName() + " kills.", true);
		return kills.get(name);
	}
	
	/**
	 * Decreases count by 1 and returns the amount.
	 * @param id
	 * @return
	 */
	public int deincrementAndGet(int id, int amount) {
		String name = NPCDefinitions.getNPCDefinitions(id).getName();
		kills.put(name, - amount); 
		return kills.get(name);
	}

	public int getKillcount(int id) {
		String name = NPCDefinitions.getNPCDefinitions(id).getName();
		return kills.getOrDefault(name, 0);
	}
	
	/**
	 * Checks if player has the amount of kills on the id.
	 * @param id
	 * @param amount
	 * @return
	 */
	public boolean hasKillcount(int id, int amount) {
		String name = NPCDefinitions.getNPCDefinitions(id).getName();
		if (kills.get(name) < amount)
			return false;
		return true;
	}

}
