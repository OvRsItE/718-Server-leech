package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.Timer;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class XPRingActivate extends Dialogue {
	
	
	@Override
	public void start() {
			sendNPCDialogue(29994, NORMAL, "Upon starting the ticket, you get one hour where all your trivia points", "Will be doubled. You can't pause this.");
			stage = 1;
		}
	
	
	@Override
	public void run(int interfaceId, int componentId) {
			if (stage == 1) {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Start Boost", "Nevermind");
				stage = 2;
			} else if (stage == 2) {
				if (componentId == OPTION_1) {
					//Timer.TriviaBoostTime();
				}
			}
		}
	
	

	@Override
	public void finish() {

	}

}