package com.rs.game.world.entity.player.content;

import java.util.Random;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;


public class ToyHorsey {

	private static String speech [][] = 
		{{"Come-on Dobbin, we can win the race!"}, 
		{"Hi-ho Silver, and away!"},
		{"Neaahhhyyy!"}};


	/**
	 * 
	 * @param player
	 */
	public static void play(Player player) {
		int rand = RandomSpeech();
		player.animate(new Animation(918));
		player.setNextForceTalk(new ForceTalk(speech[rand][0]));
	}
	
	/**
	 * 
	 * @return
	 */
	public static int RandomSpeech() {
		int random = 0;
		Random rand = new Random();
		random = rand.nextInt(speech.length);
		return random;
	}
	
}