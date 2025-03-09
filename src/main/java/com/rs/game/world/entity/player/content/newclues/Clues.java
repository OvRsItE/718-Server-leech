package com.rs.game.world.entity.player.content.newclues;

import java.util.ArrayList;

import com.rs.game.item.Item;
import com.rs.utility.Utils;


public class Clues {

	public static final ArrayList<ClueScroll> EASY_CLUES = new ArrayList<ClueScroll>();
	public static final ArrayList<ClueScroll> MEDIUM_CLUES = new ArrayList<ClueScroll>();
	public static final ArrayList<ClueScroll> HARD_CLUES = new ArrayList<ClueScroll>();
	public static final ArrayList<ClueScroll> ELITE_CLUES = new ArrayList<ClueScroll>();
	public static final ArrayList<ClueScroll> MASTER_CLUES = new ArrayList<ClueScroll>();
	
		
	static { //This will be called when the class is referenced for the first time, so you don't have to make a method to fill up the array with objects.
		fillEasyClues();
	}
	
	/**
	 * Gets a random next clue based on difficulty.
	 * @param difficulty
	 * 			the difficulty to get from.
	 * @return
	 * 			the new clue item.
	 */
	public Item getRandomClue(Difficulty difficulty) {
		switch (difficulty) {
			case EASY:
				return new Item(EASY_CLUES.get(Utils.random(EASY_CLUES.size())).getItemId(), 1);
			case MEDIUM:
				return new Item(EASY_CLUES.get(Utils.random(EASY_CLUES.size())).getItemId(), 1);
			case HARD:
				return new Item(EASY_CLUES.get(Utils.random(EASY_CLUES.size())).getItemId(), 1);
			case ELITE:
				return new Item(EASY_CLUES.get(Utils.random(EASY_CLUES.size())).getItemId(), 1);
			case MASTER:
				return new Item(EASY_CLUES.get(Utils.random(EASY_CLUES.size())).getItemId(), 1);	
		}
		return null;
	}
	
	public static ClueScroll getClue(int id) {
		if (id >= 2677 && id <= 2698) { // Easy clues
			for (ClueScroll clue : EASY_CLUES) {
				if (clue.getItemId() == id) {
					return clue;
				}
			}
		}
		return null;
	}
	
	public static int getCasket(Difficulty difficulty) {
		switch (difficulty) {
		case EASY:
			return 2714;
		case MEDIUM:
			return 2802;
		case HARD:
			return 2724;
		case ELITE:
			return 19039;
		default:
			break;
		}
		return -1;
	}
	
	
	/**
	 * Fills the EASY_CLUES ArrayList with different EASY clues
	 */
	public static void fillEasyClues() {
		/*
		 * Maps
		 */
		EASY_CLUES.add(new ClueScroll(2677, Difficulty.EASY, ClueTasks.Maps.Map1));
		EASY_CLUES.add(new ClueScroll(2678, Difficulty.EASY, ClueTasks.Maps.Map2));
		EASY_CLUES.add(new ClueScroll(2679, Difficulty.EASY, ClueTasks.Maps.Map3));
		EASY_CLUES.add(new ClueScroll(2680, Difficulty.EASY, ClueTasks.Maps.Map4));
		EASY_CLUES.add(new ClueScroll(2681, Difficulty.EASY, ClueTasks.Maps.Map5));
		EASY_CLUES.add(new ClueScroll(2682, Difficulty.EASY, ClueTasks.Maps.Map6));
		EASY_CLUES.add(new ClueScroll(2683, Difficulty.EASY, ClueTasks.Maps.Map7));
		EASY_CLUES.add(new ClueScroll(2684, Difficulty.EASY, ClueTasks.Maps.Map8));
		EASY_CLUES.add(new ClueScroll(2685, Difficulty.EASY, ClueTasks.Maps.Map9));
		EASY_CLUES.add(new ClueScroll(2686, Difficulty.EASY, ClueTasks.Maps.Map10));
		EASY_CLUES.add(new ClueScroll(2687, Difficulty.EASY, ClueTasks.Maps.Map11));
		EASY_CLUES.add(new ClueScroll(2688, Difficulty.EASY, ClueTasks.Maps.Map12));
		EASY_CLUES.add(new ClueScroll(2689, Difficulty.EASY, ClueTasks.Maps.Map13));
		EASY_CLUES.add(new ClueScroll(2690, Difficulty.EASY, ClueTasks.Maps.Map14));
		EASY_CLUES.add(new ClueScroll(2691, Difficulty.EASY, ClueTasks.Maps.Map15));
		/*
		 * Object Maps
		 */
		EASY_CLUES.add(new ClueScroll(2692, Difficulty.EASY, ClueTasks.ObjectMaps.Map1));
		EASY_CLUES.add(new ClueScroll(2693, Difficulty.EASY, ClueTasks.ObjectMaps.Map2));
		/*
		 * Riddles
		 */
		EASY_CLUES.add(new ClueScroll(2694, Difficulty.EASY, ClueTasks.Riddles.Riddle1));
		EASY_CLUES.add(new ClueScroll(2695, Difficulty.EASY, ClueTasks.Riddles.Riddle2));
		EASY_CLUES.add(new ClueScroll(2696, Difficulty.EASY, ClueTasks.Riddles.Riddle3));
		EASY_CLUES.add(new ClueScroll(2697, Difficulty.EASY, ClueTasks.Riddles.Riddle4));
		EASY_CLUES.add(new ClueScroll(2698, Difficulty.EASY, ClueTasks.Riddles.Riddle5));
	}
	
}
