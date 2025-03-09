package com.rs.utility.tools;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.rs.utility.Logger;
import com.rs.utility.SerializableFilesManager;

public class PersistentData implements Serializable {

	private static final long serialVersionUID = -3070895682771959444L;

	private static final String PATH = "data/ServerData.sd";
	private static PersistentData data;
	
	private ArrayList<String> starterIps;

	private long npcsKilled;
	private long playersKilled;
	private long coinsLeft;
	private long bossesKilled;
	private long slayerTasksCompleted;
	private long bonesBuried;
	private long levelsGained;
	private long dungeonsCompleted;

	public PersistentData() {

	}
	
	public static void addStarterIp(String IP) {
		data.starterIps.add(IP);
	}
	
	public static boolean containsStarterIP(String IP) {
		int amount = 0;
		for (String string : data.starterIps) {
			if (string.equals(IP)) {
				amount++;
			}
		}
		return amount > 2;
	}

	public static void init() {
		File file = new File(PATH);
		if (file.exists())
			try {
				data = (PersistentData) SerializableFilesManager.loadSerializedFile(file);
				if (data.starterIps == null)
					data.starterIps = new ArrayList<String>();
				System.out.println("Loaded persistent data.");
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		data = new PersistentData();
		System.out.println("Failed to load persistent data! CREATING NEW FILE!");
	}

	public static final void save() {
		try {
			SerializableFilesManager.storeSerializableClass(data, new File(PATH));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static PersistentData getPersistentData() {
		return data;
	}

	public long getNpcsKilled() {
		return getPersistentData().npcsKilled;
	}

	public long getPlayersKilled() {
		return getPersistentData().playersKilled;
	}

	public long getCoinsLeft() {
		return getPersistentData().coinsLeft;
	}

	public static long getBossesKilled() {
		return getPersistentData().bossesKilled;
	}

	public long getSlayerTasksCompleted() {
		return getPersistentData().slayerTasksCompleted;
	}

	public static void incrementSlayerTasks() {
		getPersistentData().slayerTasksCompleted++;
	}

	public static void incrementNPCsKilled() {
		getPersistentData().npcsKilled++;
	}

	public static void addCoinsLeft(int amount) {
		getPersistentData().coinsLeft += amount;
	}

	public static void incrementPlayerDeaths() {
		getPersistentData().playersKilled++;
	}

	public long getBonesBuried() {
		return getPersistentData().bonesBuried;
	}

	public void setBonesBuried(long bonesBuried) {
		getPersistentData().bonesBuried = bonesBuried;
	}

	public void incrementBonesBuried() {
		getPersistentData().bonesBuried++;
	}

	public void incrementLevelsGained() {
		getPersistentData().levelsGained++;
	}

	public long getLevelsGained() {
		return getPersistentData().levelsGained;
	}

	public void setLevelsGained(long levelsGained) {
		getPersistentData().levelsGained = levelsGained;
	}

	public long getDungeonsCompleted() {
		return getPersistentData().dungeonsCompleted;
	}

	public void setDungeonsCompleted(long levelsGained) {
		getPersistentData().dungeonsCompleted = levelsGained;
	}

}
