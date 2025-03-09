package com.rs.utility.tools;

import java.io.File;
import java.io.IOException;

import com.rs.Constants;
import com.rs.cache.Cache;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.SerializableFilesManager;
import com.rs.utility.Utils;

public class UnbanAll {

	public static void main(String[] args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File dir = new File("./data/characters/");
		File[] accs = dir.listFiles();
		for (File acc : accs) {
			String name = Utils.formatPlayerNameForProtocol(acc.getName().replace(".p", ""));
			System.out.println(acc);
			if (Utils.containsInvalidCharacter(name)) {
				acc.delete();
				return;
			}
			try {
				Player player = (Player) SerializableFilesManager.loadSerializedFile(acc);
			} catch( Exception e) {
				e.printStackTrace();
			}
		}
	}
}
