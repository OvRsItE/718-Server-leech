/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author ReverendDread
 * Sep 8, 2018
 */
public class PrayerSwitch extends Dialogue {

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#start()
	 */
	@Override
	public void start() {
		sendOptionsDialogue("Choose your prayerbook.", "Normal Prayers", "Ancient Curses", "Nevermind");
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#run(int, int)
	 */
	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			player.getPrayer().setPrayerBook(false);
		} else if (componentId == OPTION_2) {
			player.getPrayer().setPrayerBook(true);
		} else if (componentId == OPTION_3) {
			end();
		}
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
