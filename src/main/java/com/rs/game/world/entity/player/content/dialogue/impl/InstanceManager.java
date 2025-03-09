package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.instances.Corp.CorpInstance;
import com.rs.game.world.entity.npc.instances.Godwars.ArmadylInstance;
import com.rs.game.world.entity.npc.instances.Godwars.BandosInstance;
import com.rs.game.world.entity.npc.instances.Godwars.SaradominInstance;
import com.rs.game.world.entity.npc.instances.Godwars.ZamorakInstance;
import com.rs.game.world.entity.npc.instances.newbosses.AcidicInstance;
import com.rs.game.world.entity.npc.instances.newbosses.AvaterInstance;
import com.rs.game.world.entity.npc.instances.newbosses.SunfreetInstance;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class InstanceManager extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Page - Page 1", "Bandos", "Armadyl", "Saradomin", "Zamorak", "-- Next Page --");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				BandosInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_2) {
				ArmadylInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_3) {
				SaradominInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_4) {
				ZamorakInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Bosses  - Page 2", "Corporeal Beast", "Sunfreet", "Acidic Strykewyrm", "Avatar of creation", "-- Next Page --");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				CorpInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_2) {
				SunfreetInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_3) {
				AcidicInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_4) {
				AvaterInstance.launch(player);
				player.closeInterfaces();
				end();
			} else if (componentId == OPTION_5) {
				stage = 1;
				end();
				player.closeInterfaces();
			}

		}
	}

	@Override
	public void finish() {

	}

}
