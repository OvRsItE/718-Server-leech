package com.rs.utility;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.game.world.entity.player.Player;

public final class IPMute {

	public static CopyOnWriteArrayList<String> ipList;

	private static final String PATH = "data/MutedIPS.ser";
	private static boolean edited;

	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				ipList = (CopyOnWriteArrayList<String>) SerializableFilesManager
						.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		ipList = new CopyOnWriteArrayList<String>();
	}

	public static final void save() {
		if (!edited)
			return;
		try {
			SerializableFilesManager.storeSerializableClass(ipList, new File(
					PATH));
			edited = false;
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static boolean isMuted(String ip) {
		return ipList.contains(ip);
	}

	public static void mute(Player player, boolean loggedIn) {
		if (loggedIn) {
			ipList.add(player.getSession().getIP());
		} else {
			ipList.add(player.getLastIP());
			SerializableFilesManager.savePlayer(player);
		}
		edited = true;
	}

	public static void unmute(Player player) {
		player.setMuted(0);
		ipList.remove(player.getLastIP());
		edited = true;
		save();
	}

	public static void checkCurrent() {
		for (String list : ipList) {
			System.out.println(list);
		}
	}

	public static CopyOnWriteArrayList<String> getList() {
		return ipList;
	}

}