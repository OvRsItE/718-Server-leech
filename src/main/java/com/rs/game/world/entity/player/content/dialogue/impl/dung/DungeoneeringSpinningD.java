package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeoneeringSkillsDialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.crafting.DungeoneeringSpinning;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.crafting.DungeoneeringSpinningData;

public class DungeoneeringSpinningD extends Dialogue {

	private int[] items;

	@Override
	public void start() {
		items = new int[10];
		int x = 0;
		for (DungeoneeringSpinningData values : DungeoneeringSpinningData.values())
			items[x++] = values.getProduct();
		DungeoneeringSkillsDialogue.sendSkillsDialogue(player, "Select the item you wish to spin.", items);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int amount = DungeoneeringSkillsDialogue.getAmount(componentId);
		int option = DungeoneeringSkillsDialogue.getItemSlot(amount == 1 ? componentId : amount == 5 ? componentId + 1 : amount == 10 ? componentId + 2 : componentId + 3);
		if (option > items.length) {
			end();
			return;
		}
		DungeoneeringSpinningData data = null;
		for (DungeoneeringSpinningData values : DungeoneeringSpinningData.values()) {
			if (values.getProduct() == items[option]) {
				data = values;
				break;
			}
		}
		if (amount == -1) {
			player.getPackets().sendRunScript(108, new Object[] { "How many would you like to spin?" });
			player.getTemporaryAttributtes().put("dungspinning", data);
			end();
			return;
		}
		if (data != null)
			player.getActionManager().setAction(new DungeoneeringSpinning(data, amount));
		end();
	}

	@Override
	public void finish() {}

}