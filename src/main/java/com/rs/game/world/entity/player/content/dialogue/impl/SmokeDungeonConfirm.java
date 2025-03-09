/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
public class SmokeDungeonConfirm extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("<col=ff0000>Are you sure you want to continue without a facemask?</col>", "Yes, I'm sure.", "No thanks.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
			case OPTION_1:
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2916, 2503, 0));
				CoresManager.slowExecutor.schedule(() -> {
					player.getControlerManager().startControler("SmokeDungeonController");
				}, 5, TimeUnit.SECONDS);
				break;
		}
		end();
	}

	@Override
	public void finish() {}

}
