/**
 * 
 */
package com.rs.game.world.entity.player.settings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.world.entity.player.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ReverendDread
 * Aug 9, 2018
 */
public class SettingsManager implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -4974370484296287946L;

	/**
	 * The player this manager is assosiated with.
	 */
	@Getter @Setter private transient Player player;
	
	/**
	 * Map containing settings and their values.
	 */
	@Getter private Map<Settings, Boolean> settings;
	
	/**
	 * The settings manager constructor
	 */
	public SettingsManager() {
		settings = new HashMap<Settings, Boolean>();
	}
	
	/**
	 * Updates the setting and sends the packet to update the client.
	 * @param setting
	 * 				the setting to update.
	 * @param value
	 * 				the settings value.
	 */
	public void update(Settings setting, boolean value) {
		settings.put(setting, value);
		player.getPackets().sendSettingPacket(setting.ordinal(), value);
	}
	
	/**
	 * Sends the settings the player has saved.
	 */
	public void init() {
		for (Settings setting : Settings.VALUES) {
			player.getPackets().sendSettingPacket(setting.ordinal(), settings.get(setting) == null ? false : settings.get(setting));
		}
	}
	
}
