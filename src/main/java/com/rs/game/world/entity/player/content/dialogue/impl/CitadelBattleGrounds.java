package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.social.citadel.Citadel;

public class CitadelBattleGrounds extends Dialogue {

	Player owner;
	private byte END = 100;

	@Override
	public void start() {
		owner = (Player) parameters[0];
		String battle = owner.citadelBattleGround ? " Currently, there's a battleground occuring."
				: " Currently, there isn't a battleground occuring.";
		sendNPCDialogue(13932, EmotionManager.QUESTION,
				"Hello " + player.getDisplayName()
						+ ", I manage the Citadel BattleGround." + "" + battle
						+ "");
		stage = 1;

	}

	@Override
	public void run(int interfaceId, int id) {
		if (stage == 1) {
			sendNPCDialogue(13932, EmotionManager.QUESTION,
					"So what can I do for you, " + player.getDisplayName()
							+ "?");
			stage = 2;
		} else if (stage == 2) {
			sendOptionsDialogue("Select an option",
					owner.citadelBattleGround ? "Disable Battlefield"
							: "Activate Battlefield",
					owner.citadelBattleGround ? "Enter Battlefield"
							: "Explain Battlefield");
			stage = 3;
		} else if (stage == 3) {
			if (id == OPTION_1) {
				if (player == owner) {
					if (owner.citadelBattleGround) {
						owner.citadelBattleGround = false;
						Citadel.deleteBattleField(owner);
					} else {
						owner.citadelBattleGround = true;
						Citadel.createBattleField(owner);
					}
					sendNPCDialogue(13932, EmotionManager.NORMAL,
							"Your orders have been carried out approximately.");
					stage = END;
				} else {
					sendNPCDialogue(13932, EmotionManager.NORMAL,
							"Sorry, my orders come directly from the owner of the Citadel. "
									+ "Perhaps you can ask him to do this.");
					stage = END;
				}
			} else if(id == OPTION_2) {
				if(owner.citadelBattleGround) {
					Citadel.enterBattleField(player, owner);
				    end();
				} else {
					sendNPCDialogue(13932, EmotionManager.NORMAL, "Of course! The battlefield is an area where the owner of the citadel can activate to allow his Citadel guests to " +
							"friendly spare, or fight to the death. I will personally warn you if the battlefield is dangerous so there's no need to worry. Anything else?");
					stage = 2;
				}
			}
		} else if (stage == END)
			end();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
