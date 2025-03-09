/**
 * 
 */
package com.rs.game.world.entity.player;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Handles cooldowns.
 * @author ReverendDread
 * Jan 12, 2019
 */
public class CooldownManager implements Serializable {

	/** Serial UID */
	private static final long serialVersionUID = 8971365319851971636L;
	
	/** Active cooldowns */
	private List<Cooldown> cooldowns = new CopyOnWriteArrayList<Cooldown>();
	
	/** The player */
	@Setter private transient Player player;
	
	/**
	 * Adds a cooldown, this will replace an existing cooldown.
	 * @param cooldown
	 * 			the cooldown to add.
	 * @return
	 * 			true if the cooldown was added successfully. false otherwise.
	 */
	public boolean addCooldown(Cooldown cooldown) {
		Cooldown searched = getCooldownForKey(cooldown.key);
		cooldown.started = Utils.currentTimeMillis();
		if (searched == null)
			return cooldowns.add(cooldown);
		else {
			cooldowns.set(cooldowns.indexOf(searched), cooldown);
			return true;
		}
	}
	
	/**
	 * Checks if the player has a certain cooldown.
	 * @param key
	 * 			the cooldown key.
	 * @return
	 */
	public boolean hasCooldown(String key) {
		Cooldown searched = getCooldownForKey(key);
		return searched != null;
	}
	
	/**
	 * Removes an existing cooldown, may return false if cooldown doesn't exist.
	 * @param key
	 * 			the cooldown key to remove.
	 * @return
	 * 			true if the cooldown was removed successfully, false otherwise.
	 */
	public boolean removeCooldown(String key) {
		Cooldown searched = getCooldownForKey(key);
		if (searched == null)
			return false;
		return cooldowns.remove(searched);
	}
	
	/**
	 * Gets an existing cooldown for the desired key.
	 * @param key
	 * 			the key to search for.
	 * @return
	 * 			the cooldown with the desired key, may return null if no cooldown was found.
	 */
	public Cooldown getCooldownForKey(String key) {
		for (Cooldown search : cooldowns) {
			if (search.key.equalsIgnoreCase(key))
				return search;
		}
		return null;
	}
	
	/**
	 * Gets the duration in milliseconds remaining of the desired cooldown key.
	 * @param key
	 * 			the key to search for.
	 * @return
	 * 			cooldown time in milliseconds, 0 if cooldown doesn't exist.
	 */
	public long getDurationMillis(String key) {
		for (Cooldown search : cooldowns) {
			if (search.key.equalsIgnoreCase(key)) {
				long time = Utils.currentTimeMillis();
				return -((time - search.started) - search.duration);
			}
		}
		return 0;
	}
	
	/**
	 * Processes cooldowns.
	 */
	public void process() {
		if (player == null || player.isFinished() || cooldowns.isEmpty()) 
			return;
		for (Cooldown cooldown : cooldowns) {
			long time = Utils.currentTimeMillis();
			if ((time - cooldown.duration) > cooldown.started) {
				cooldowns.remove(cooldown);
			}
		}
	}
	
	/**
	 * Represents a cooldown.
	 * @author ReverendDread
	 * Jan 12, 2019
	 */
	public static class Cooldown implements Serializable {

		/** Serial UID */
		private static final long serialVersionUID = 3998403603792346141L;
		
		/** The cooldowns unique key */
		public final String key;
		
		/** Time the cooldown was started */
		@Getter private long started;
		
		/** Cooldown duration in milliseconds */
		@Getter private long duration;
		
		/**
		 * Constructor
		 * @param duration
		 * 			the duration of the cooldown.
		 */
		public Cooldown(String key, long duration) {
			this.key = key;
			this.duration = duration;
		}
		
	}
	
}
