package com.rs.game.world.entity.player.content.activities.trollinvasion;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;


public class TrollInvasionStart extends Dialogue {

	private boolean complexity;
	private TrollInvasion game;
	private int click;

	@Override
	public void start() {
		game = (TrollInvasion) parameters[0];
		click = (int) parameters[1];

	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(click) {
		case 1:
			doDialogueClickOne(interfaceId, componentId);
			break;
		case 2:
			doDialogueClickTwo(interfaceId, componentId);
		case 3:
			doDialogueClickThree(interfaceId, componentId);
		}

	}

	@Override
	public void finish() {
		
	}

	public void doDialogueClickOne(int interfaceId, int componentId) {

	}

	public void doDialogueClickTwo(int interfaceId, int componentId) {

	}

	public void doDialogueClickThree(int interfaceId, int componentId) {

	}

}
