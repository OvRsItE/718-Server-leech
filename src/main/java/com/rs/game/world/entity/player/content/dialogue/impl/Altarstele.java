package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class Altarstele extends Dialogue {



	@Override
	public void start() {
		sendOptionsDialogue("Prayers/Spellbooks", "Ancient Magics", "Lunar Spells", "Ancient Curses");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if(componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3233, 9315, 0));
				end();

			} else if(componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2151, 3863, 0));
				player.getInterfaceManager().closeChatBoxInterface();
				end();


			} else if(componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3182, 5713, 0));
				end();

			}
		}

	}

	@Override
	public void finish() {

	}
}