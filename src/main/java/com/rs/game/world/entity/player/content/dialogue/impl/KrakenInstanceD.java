/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * The dialogue for creating a Kraken instance.
 * @author ReverendDread
 * Jul 22, 2018
 */
public class KrakenInstanceD extends Dialogue {

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Create instance (500k).", "Nevermind.");
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#run(int, int)
	 */
	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
			case OPTION_1:
				if (player.getInventory().removeItemMoneyPouch(995, 500_000)) {
					player.getControlerManager().startControler("KrakenInstance");
				} else {
					player.sendMessage("You don't have enough coins to start an instance.");
				}
				break;
		}
		end();
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
