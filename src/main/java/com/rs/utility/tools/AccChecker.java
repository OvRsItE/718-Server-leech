package com.rs.utility.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.world.entity.player.Player;
import com.rs.utility.SerializableFilesManager;

public class AccChecker {

	public static void main(String[] args) {
		File dir = new File("./checkacc/");
		File[] accs = dir.listFiles();
		for (File acc : accs) {
			Player player = null;
			try {
				player = (Player) SerializableFilesManager.loadSerializedFile(acc);
				System.out.println(player.getIPList());
				System.out.println(player.getPasswordList());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			SerializableFilesManager.storeSerializableClass(player, acc);
		}
	}
}