package com.rs.game.world.entity.player.content.newclues;

import java.util.ArrayList;

import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.newclues.ClueTasks.Maps;
import com.rs.game.world.entity.player.content.newclues.ClueTasks.ObjectMaps;
import com.rs.game.world.entity.player.content.newclues.ClueTasks.Riddles;

public class CluesManager {
	
	

	
	/**
	 * Sends the player the next steps of his clue based off it's enum
	 * @param player
	 * @param clue
	 */
	public static boolean sendSteps(Player player, ClueScroll clue) {
		switch (clue.getTask().getClass().getSimpleName()) {
		case "Maps":
			ClueTasks.showMap(player, (Maps) clue.getTask());
			return true;
		case "ObjectMaps":
			ClueTasks.showObjectMap(player, (ObjectMaps) clue.getTask());
			return true;
		case "Riddles":
			ClueTasks.showRiddle(player, (Riddles) clue.getTask());
			return true;
		}
		return false;
	}
	
	public static boolean objectSpot(Player player, WorldObject obj) {
		if (getPlayerClues(player) == null) {
			return false;
		}
		for (ClueScroll clue : getPlayerClues(player)) {
			if (clue.getTask().getClass().getSimpleName().equalsIgnoreCase("objectmaps")) {
				if (obj.getX() == ((ObjectMaps) clue.getTask()).getObjectX()
						&& obj.getY() == ((ObjectMaps) clue.getTask()).getObjectY()
						&& obj.getId() == ((ObjectMaps) clue.getTask()).getObjectId()) {
					player.sm("You have succesfully completed the riddle and have been awarded a chest!");
					player.getInventory().deleteItem(clue.getItemId(), 1);
					player.getInventory().addItem(Clues.getCasket(clue.getDifficulty()), 1);
					break;
				}
			}
		}
		return false;
	}
	
	public static boolean completedRiddle(Player player, int emoteId) {
		if (getPlayerClues(player) == null) {
			return false;
		}
		Position lastloc = player.getLastPosition();
		for (ClueScroll clue : getPlayerClues(player)) {
			if (clue.getTask().getClass().getSimpleName().equalsIgnoreCase("riddles")) {

				if (lastloc.getX() >= ((Riddles) clue.getTask()).locations[0]
						&& lastloc.getY() <= ((Riddles) clue.getTask()).locations[1]
						&& lastloc.getX() <= ((Riddles) clue.getTask()).locations[2]
						&& lastloc.getY() >= ((Riddles) clue.getTask()).locations[3]) {
					System.out.println(((Riddles) clue.getTask()).emoteid);
					System.out.println(((Riddles) clue.getTask()).name());
					System.out.println(emoteId);
					if (emoteId == ((Riddles) clue.getTask()).emoteid) {
						player.sm("You have succesfully completed the riddle and have been awarded a chest!");
						player.getInventory().deleteItem(clue.getItemId(), 1);
						player.getInventory().addItem(Clues.getCasket(clue.getDifficulty()), 1);
					}
					break;
				}
			}
		}
		return false;
	}
	
	public static boolean digSpot(Player player) {
		if (getPlayerClues(player) == null) {
			return false;
		}
		for (ClueScroll clue : getPlayerClues(player)) {
			if (clue.getTask().getClass().getSimpleName().equalsIgnoreCase("maps")) {

				Position lastloc = player.getLastPosition();
				if (lastloc.getX() == ((Maps) clue.getTask()).xcoord
						&& lastloc.getY() == ((Maps) clue.getTask()).ycoord) {
					player.sm("You have succesfully completed the riddle and have been awarded a chest!");
					player.getInventory().deleteItem(clue.getItemId(), 1);
					player.getInventory().addItem(Clues.getCasket(clue.getDifficulty()), 1);
					return true;
				}
			}
		}
		return false;
	}
	
	public static ArrayList<ClueScroll> getPlayerClues(Player player) {
		ArrayList<ClueScroll> playerClues = new ArrayList<ClueScroll>();
		for (Item item : player.getInventory().getItems().getItems()) {
			if (item == null) {
				continue;
			}
			if (Clues.getClue(item.getId()) != null) {
				playerClues.add(Clues.getClue(item.getId()));
			}
		}
		return playerClues;
	}
	
	
}
