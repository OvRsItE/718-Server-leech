/**
 * 
 */
package com.rs.game.world.entity.player.content.diary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.world.entity.player.Player;
import com.rs.utility.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Handles Achievement diary related things.
 * @author ReverendDread
 * Jan 14, 2019
 */
public class DiaryManager implements Serializable {

	/** Serial UID */
	private static final long serialVersionUID = -7006932374615862795L;

	/** The player */
	@Setter private transient Player player;
	
	/** Map of achievements the player has started & completed */
	@Getter private Map<Achievement, Integer> achievements = new HashMap<Achievement, Integer>();
	
	/**
	 * Progresses in an achievement and completes it when its been finished.
	 * @param achievement
	 * 			
	 * @param increment
	 * @return
	 */
	public void progress(Achievement achievement, int increment) {
		if (!achievements.containsKey(achievement)) {
			achievements.put(achievement, 0);
		}
		for (Achievement search : achievements.keySet()) {
			boolean completed = hasCompleted(achievement);
			if (completed)
				continue;
			if (search.equals(achievement)) {
				int current = achievements.get(search);
				achievements.put(achievement, current += increment);
				if (hasCompleted(achievement)) {
					player.getPackets().sendGameMessage("<col=00ff00>You've completed an achievement in the "
							+ Utils.getFormattedEnumName(achievement.getDiary().name()) + " " 
								+ achievement.getDifficulty().name().toLowerCase() + " diaries.");
				}
			}
		}
	}
	
	/**
	 * Checks if the desired achievement has been completed.
	 * @param achievement
	 * @return
	 */
	public boolean hasCompleted(Achievement achievement) {
		if (!achievements.containsKey(achievement))
			return false;
		for (Achievement search : achievements.keySet()) {
			if (search.equals(achievement) && (achievements.get(search) >= achievement.getRequiredAmount()))
				return true;		
		}
		return false;
	}
	
	/**
	 * Represents an achievement diary (A collection of achievements).
	 */
	public enum AchievementDiary {		
		LUMBRIDGE_DRAYNOR,	
	}
	
	/**
	 * Represents an achievement difficulty level.
	 */
	public enum AchievementDifficulty {
		EASY,
		MEDIUM,
		HARD,
		ELITE
	}
	
}
