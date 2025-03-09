package com.rs.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import com.rs.game.map.Position;

public class TeleportManager {

	private static HashMap<String, Position> WorldTiles = new HashMap<String, Position>();
	
	private static void add(String teleName, Position tile) {
		if(WorldTiles == null) 
			WorldTiles = new HashMap<String, Position>();
		WorldTiles.put(teleName, tile);
	}
	
	public static void addNewTeleport(String name, Position tile) {
		if(name == null || tile == null)
			return;
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter("data/teleports.txt", true));
			file.write(name + " " + tile.getX() + " " + tile.getY() + " " + tile.getZ());
			file.newLine();
			file.close();
			initiate();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static HashMap<String, Position> getHashMap() {
		return WorldTiles;
	}
	public static Position getLocation(String teleName) {
		return WorldTiles.get(teleName);
	}
	
	public static void initiate() {
		WorldTiles.clear();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/teleports.txt"));
			String line;
			while((line = reader.readLine()) != null) {
				if(line.startsWith("//"))
					continue;
				String[] text = line.split(" ");
				String name = text[0];
				int x = Integer.parseInt(text[1]);
				int y = Integer.parseInt(text[2]);
				int plane = Integer.parseInt(text[3]);
				Position tile = new Position(x, y, plane);
				add(name, tile);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		Logger.log("Launcher", WorldTiles.size() + " Teleports loaded succesfully..");
	}
}
