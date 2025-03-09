package com.rs.game.world.entity.player.content;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.Player;

/**
 *
 * @author Chaoz
 */
public class AchievementsTab {

	public static void sendTab(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				AchievementSystem.displayEasyAchievments(player);

			}

		}, 0, 5);

	}
}