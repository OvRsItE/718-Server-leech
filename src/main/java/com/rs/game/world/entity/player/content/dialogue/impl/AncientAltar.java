package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class AncientAltar extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Choose your spellbook.", "Normal Spellbook", "Ancient Spellbook", "Lunar Spellbook", "Nevermind");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			sendDialogue("Your mind clears and you switch", "your spellbook to normal.");
			player.getCombatDefinitions().setSpellBook(0);
		} else if (componentId == OPTION_2) {
			sendDialogue("Your mind clears and you switch", "to the ancient spellbook.");
			player.getCombatDefinitions().setSpellBook(1);
		} else if (componentId == OPTION_3) {
			sendDialogue("Your mind clears and you switch", "to the lunar spellbook.");
			player.getCombatDefinitions().setSpellBook(2);
		} else {
			end();
		}
	}

	@Override
	public void finish() {}

}
