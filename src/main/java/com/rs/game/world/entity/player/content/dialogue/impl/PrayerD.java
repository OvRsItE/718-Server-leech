package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.content.BonesOnAltar;
import com.rs.game.world.entity.player.content.SkillsDialogue;
import com.rs.game.world.entity.player.content.BonesOnAltar.Bones;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class PrayerD extends Dialogue {

		private Bones bones;
		private WorldObject object;

		@Override
		public void start() {
			this.bones = (Bones) parameters[0];
			this.object = (WorldObject) parameters[1];

			SkillsDialogue
					.sendSkillsDialogue(
							player,
							SkillsDialogue.OFFER,
							"How many would you like to offer?",
							player.getInventory().getItems()
									.getNumberOf(bones.getBone()),
							new int[] { bones.getBone().getId() }, null);
		}

		@Override
		public void run(int interfaceId, int componentId) {
			player.getActionManager().setAction(
					new BonesOnAltar(object, bones.getBone(), SkillsDialogue
							.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}