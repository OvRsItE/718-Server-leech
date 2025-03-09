package com.rs.game.world.entity.player.content.newclues;

import lombok.Getter;

public class ClueScroll {

	@Getter private final int itemId;
	@Getter private final Difficulty difficulty; //Use an enum for the difficulty so you dont have to bother with magic numbers.
	@Getter private final Enum<?> task;
	 
	
	public ClueScroll(int itemId, Difficulty difficulty, Enum<?> givenTask) {
		this.itemId = itemId;
		this.difficulty = difficulty;
		this.task = givenTask;
	}
		
}
