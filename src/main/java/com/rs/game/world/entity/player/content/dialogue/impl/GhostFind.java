package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class GhostFind extends Dialogue {

	int npcId;

	@Override
	public void start() {
		sendNPCDialogue(457, 9827, "How are you doing finiding my skull?" );
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendPlayerDialogue( 9827, "I'm still searching");
			stage = 1;
		} else if (stage == 1) {
			sendNPCDialogue(457, 9827, "Okay, thanks for your help!" );
				end();
				player.RG = 4;
				} else if (stage == 21) {
					switch (componentId) {
					case 1:
						break;
					}
				} else {
					end();
				}
		}

			public void finish() {
			}
}