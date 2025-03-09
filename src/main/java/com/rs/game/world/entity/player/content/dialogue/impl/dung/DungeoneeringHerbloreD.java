package com.rs.game.world.entity.player.content.dialogue.impl.dung;


import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeoneeringSkillsDialogue;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.herblore.DungeoneeringHerblore;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.herblore.DungeoneeringHerbloreIngredients;

public class DungeoneeringHerbloreD extends Dialogue {

	private DungeoneeringHerbloreIngredients items;
	
	@Override
	public void start() {
		items = (DungeoneeringHerbloreIngredients) parameters[0];
		DungeoneeringSkillsDialogue.sendSkillsDialogue(player, "Select the amount you wish to create.", new int[] { items.getItems()[2] });
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int amount = DungeoneeringSkillsDialogue.getAmount(componentId);
		int option = DungeoneeringSkillsDialogue.getItemSlot(amount == 1 ? componentId : amount == 5 ? componentId + 1 : amount == 10 ? componentId + 2 : componentId + 3);
		if (option > 1) {
			end();
			return;
		}
		if (amount == -1) {
			player.getPackets().sendRunScript(108, new Object[] { "How many would you like to create?" });
			player.getTemporaryAttributtes().put("dungherblore", items);
			end();
			return;
		}
		player.getActionManager().setAction(new DungeoneeringHerblore(items, amount));
		end();
	}

	@Override
	public void finish() {}

}