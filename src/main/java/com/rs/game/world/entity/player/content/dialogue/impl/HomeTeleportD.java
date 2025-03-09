package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.actions.HomeTeleport;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author Trey2k
 *
 * Creation Date: Oct 1, 2017 - 7:49:36 PM
 */
public class HomeTeleportD extends Dialogue {

	/* (non-Javadoc)
	 * @see com.rs.game.player.dialogues.Dialogue#start()
	 */
	@Override
	public void start() {
		sendOptionsDialogue("Choose An Option", "Teleport Home", "Open Lodestone");
        stage = 1;
	}

	/* (non-Javadoc)
	 * @see com.rs.game.player.dialogues.Dialogue#run(int, int)
	 */
	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		
		case 1:
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, Constants.HOME_PLAYER_LOCATION[0]);
			} else {
				player.stopAll();
				player.getInterfaceManager().sendInterface(1092);
			}
			end();
			return;
		}

	}

	/* (non-Javadoc)
	 * @see com.rs.game.player.dialogues.Dialogue#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
