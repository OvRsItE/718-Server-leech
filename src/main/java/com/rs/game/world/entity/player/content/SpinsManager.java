package com.rs.game.world.entity.player.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;

public class SpinsManager {

	private transient Player player;
	private boolean gotSpins = false;
	private boolean ipSucess = false;

	public SpinsManager(Player player) {
		this.player = player;
	}

	public void checkIP() {
		try {
			ipSucess = false;
			File file1 = new File("./data/playersaves/ipcheckspin/" + getDate()
					+ " " + player.getSession().getIP() + ".txt");
			boolean success = file1.createNewFile();
			if (success) {
				ipSucess = true;
				Writer output = null;
				output = new BufferedWriter(new FileWriter(file1));
				output.write("Username: " + player.getUsername() + "");
				output.close();
			} else {
				return;
			}
		} catch (IOException e) {
		}
	}

	public void addSpins() {
		checkIP();
		try {
			gotSpins = false;
			File file = new File("./data/playersaves/spins/" + getDate() + " "
					+ player.getUsername() + ".txt");
			boolean success = file.createNewFile();
			if (success) {
				if (ipSucess == false) {
					return;
				}
				gotSpins = true;
				player.getPackets().sendGameMessage(
						"You recieved a free spin from Squeal Of Fortune.");

				World.sendWorldMessage("", success);
				Writer output = null;
				output = new BufferedWriter(new FileWriter(file));
				output.write("" + gotSpins + "");
				output.close();
			}
		} catch (IOException e) {
		}
	}

	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		date = null;
		dateFormat = null;
		return currentDate;
	}
}