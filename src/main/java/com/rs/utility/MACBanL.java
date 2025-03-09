package com.rs.utility;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.game.world.entity.player.Player;

public final class MACBanL {

	public static CopyOnWriteArrayList<String> macList;

	private static final String PATH = "data/bannedMACS.ser";
	private static boolean edited;

	public static void ban(Player player, boolean loggedIn) {
		player.setPermBanned(true);
		if (loggedIn) {
			macList.add(player.getSession().getIP());
			player.getSession().getChannel().disconnect();
		} else {
			macList.add(player.getLastIP());
			SerializableFilesManager.savePlayer(player);
		}
		edited = true;
	}

	public static void checkCurrent() {
		for (String list : macList) {
			System.out.println(list);
		}
	}

	public static CopyOnWriteArrayList<String> getList() {
		return macList;
	}

	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				macList = (CopyOnWriteArrayList<String>) SerializableFilesManager
						.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		macList = new CopyOnWriteArrayList<String>();
	}

	public static boolean isBanned(String mac) {
		return macList.contains(mac);
	}

	public static final void save() {
		if (!edited)
			return;
		try {
			SerializableFilesManager.storeSerializableClass(macList, new File(
					PATH));
			edited = false;
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static void unban(Player player) {
		macList.remove(player.getMACAddress());
		edited = true;
		save();
	}

}
