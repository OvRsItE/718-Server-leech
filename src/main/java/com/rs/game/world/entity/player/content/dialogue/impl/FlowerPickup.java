package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.Animation;

public class FlowerPickup extends Dialogue {

	WorldObject flowerObject;
	
	public int getFlowerId(int objectId) {
		return 2460+((objectId-2980)*2);
	}
	
	@Override
	public void start() {
		flowerObject = (WorldObject) parameters[0];
		sendOptionsDialogue("What do you want to do with the flowers?", "Pick", "Leave them");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == 11) {
				player.animate(new Animation(827));
				player.getInventory().addItem(getFlowerId(flowerObject.getId()), 1);
				player.getInventory().refresh();
				World.removeObject(flowerObject, false);
			}
			end();
		}
	}

	@Override
	public void finish() {

	}
}