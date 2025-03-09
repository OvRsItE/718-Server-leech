package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeoneeringSkillsDialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.smithing.DungeoneeringSmelting;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.smithing.DungeoneeringSmeltingData;

public class DungeoneeringSmeltingD extends Dialogue {

	@Override
	public void start() {
		int[] items = new int[10];
		int x = 0;
		for (DungeoneeringSmeltingData d : DungeoneeringSmeltingData.values())
			items[x++] = d.getBarId();
		DungeoneeringSkillsDialogue.sendSkillsDialogue(player, "Select the bar you wish to smelt.", items);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int amount = DungeoneeringSkillsDialogue.getAmount(componentId);
		int option = DungeoneeringSkillsDialogue.getItemSlot(amount == 1 ? componentId : amount == 5 ? componentId + 1 : amount == 10 ? componentId + 2 : componentId + 3);
		if (option > 10) {
			end();
			return;
		}
		if (amount == -1) {
			player.getPackets().sendRunScript(108, new Object[] { "How many would you like to smelt?" });
			player.getTemporaryAttributtes().put("dungsmelting", DungeoneeringSmeltingData.values()[option]);
			end();
			return;
		}
		player.getActionManager().setAction(new DungeoneeringSmelting(DungeoneeringSmeltingData.values()[option], amount));
		end();
	}

	@Override
	public void finish() {}

}