/**
 * 
 */
package com.rs.game.world.entity.player.content.diary;

import com.rs.game.world.entity.player.content.diary.DiaryManager.AchievementDiary;
import com.rs.game.world.entity.player.content.diary.DiaryManager.AchievementDifficulty;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * @author ReverendDread
 * Jan 14, 2019
 */
@Getter
public enum Achievement {
	
	CAVE_BUG("Slay a cave bug in the Lumbridge Swamp Caves.", 10, AchievementDifficulty.EASY, AchievementDiary.LUMBRIDGE_DRAYNOR),
	
	
	;
	
	private final String description;
	private final int requiredAmount;
	private final AchievementDifficulty difficulty;
	private final AchievementDiary diary;
	
	private Achievement(String description, int requiredAmount, AchievementDifficulty difficulty, AchievementDiary diary) {
		this.description = description;
		this.requiredAmount = requiredAmount;
		this.difficulty = difficulty;
		this.diary = diary;
	}
	
}
