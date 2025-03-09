/**
 * 
 */
package com.rs.game.world.entity.player.teleports;

import com.rs.cache.loaders.IComponentDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Handles teleporting around the server.
 * @author ReverendDread
 * Aug 4, 2018
 */
public class TeleportManager implements Serializable {

	private static final long serialVersionUID = -4301078368066557593L;

	public static final int TELEPORT_INTER = 3201;
	private static final int TELEPORT_BTN_START = 59;
	private static final int TELEPORT_BTN_END = 205;
	private static final int FAVORITES_BTN_START = 219;
	private static final int FAVORITES_BTN_END = 255;
	private static final int MAX_FAV_SLOTS = 8;

	@Getter private Teleports[] favorites = new Teleports[MAX_FAV_SLOTS]; //max size is 8
	@Getter private transient List<Teleports> listedTeleports;
	@Getter @Setter private transient Player player;

	public void sendInterface() {
		player.getInterfaceManager().sendInterface(TELEPORT_INTER);
		showCatagory(TeleportCatagory.MONSTERS);
		showFavorites();
		player.getPackets().sendIComponentText(TELEPORT_INTER, 260, "");
	}

	public boolean chooseCatagory(int buttonId) {
		if (isCatagoryButton(buttonId)) {
			int diff = (buttonId - 23);
			int catagoryIdx = diff / 5;
			if (catagoryIdx < TeleportCatagory.VALUES.length) {
				TeleportCatagory catagory = TeleportCatagory.VALUES[catagoryIdx];
				return showCatagory(catagory);
			}
		}
		return false;
	}

	public boolean showCatagory(TeleportCatagory catagory) {
		listedTeleports = Teleports.getTeleportsForCatagory(catagory);
		for (int i = TELEPORT_BTN_START; i < TELEPORT_BTN_END; i += 5) {
			int teleIndex = (i - TELEPORT_BTN_START) / 5;
			if (teleIndex < listedTeleports.size()) {
				Teleports teleport = listedTeleports.get(teleIndex);
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i + 1, false);
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i, false);
				player.getPackets().sendIComponentText(TELEPORT_INTER, i, teleport.getName());
			} else {
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i + 1, true);
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i, true);
			}
		}
		return true;
	}

	public void showFavorites() {
		for (int i = FAVORITES_BTN_START; i < FAVORITES_BTN_END; i += 5) {
			int teleIndex = (i - FAVORITES_BTN_START) / 5;
			Teleports teleport;
			if (teleIndex < favorites.length && (teleport = favorites[teleIndex]) != null) {
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i + 1, false);
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i, false);
				player.getPackets().sendIComponentText(TELEPORT_INTER, i, teleport.getName());
			} else {
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i + 1, true);
				player.getPackets().sendHideIComponent(TELEPORT_INTER, i, true);
			}
		}
	}

	public boolean clickTeleportButton(int buttonId) {
		if (isTeleportButton(buttonId)) {
			int teleIndex = (buttonId - TELEPORT_BTN_START) / 5;
			boolean favorite = buttonId % 5 == 0;
			if (favorite)
				return addFavorite(listedTeleports.get(teleIndex));
			if (teleIndex < listedTeleports.size())
				return teleport(listedTeleports.get(teleIndex));
		}
		if (isFavoriteButton(buttonId)) {
			int teleIndex = (buttonId - FAVORITES_BTN_START) / 5;
			boolean remove = buttonId % 5 == 0;
			if (remove)
				return removeFavorite(favorites[teleIndex]);
			if (teleIndex < favorites.length)
				return teleport(favorites[teleIndex]);
		}
		return false;
	}

	public boolean teleport(Teleports teleport) {
		if (teleport != null) {
			if (teleport.getDestination() != null)
				Magic.sendNormalTeleportSpell(player, 0, 0, teleport.getDestination());
			if (teleport.getFunction() != null)
				teleport.getFunction().accept(player);
			return true;
		}
		return false;
	}

	public boolean removeFavorite(Teleports teleport) {
		int idx = indexOf(teleport, favorites);
		if (idx != -1)
			favorites[idx] = null;
		sortFavorites();
		showFavorites();
		return true;
	}

	public boolean addFavorite(Teleports teleport) {
		int slot = getNextSlot();
		if (slot != -1) {
			favorites[slot] = teleport;
			sortFavorites();
			showFavorites();
			return true;
		}
		return false;
	}

	private <T> int indexOf(T obj, T[] objects) {
		for (int index = 0; index < objects.length; index++) {
			T t = objects[index];
			if (t == obj)
				return index;
		}
		return -1;
	}

	private int getNextSlot() {
		for (int i = 0; i < MAX_FAV_SLOTS; i++) {
			Object obj = favorites[i];
			if (obj == null)
				return i;
		}
		return -1;
	}

	private void sortFavorites() {
		Arrays.sort(favorites, (o1, o2) -> {
			if (o1 == null && o2 == null)
				return -0;
			if (o1 == null)
				return 1;
			if (o2 == null)
				return -1;
			return 0;
		});
	}

	private boolean isCatagoryButton(int buttonId) {
		return buttonId >= 23 && buttonId <= 43;
	}

	private boolean isFavoriteButton(int buttonId) {
		return buttonId >= FAVORITES_BTN_START && buttonId <= FAVORITES_BTN_END;
	}

	private boolean isTeleportButton(int buttonId) {
		return buttonId >= TELEPORT_BTN_START && buttonId <= TELEPORT_BTN_END;
	}
	
}
