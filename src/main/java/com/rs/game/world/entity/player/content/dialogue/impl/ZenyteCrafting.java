/**
 * 
 */
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.player.actions.skilling.Smelting;
import com.rs.game.world.entity.player.actions.skilling.Smelting.SmeltingBar;
import com.rs.game.world.entity.player.content.SkillsDialogue;
import com.rs.game.world.entity.player.content.SkillsDialogue.ItemNameFilter;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.skills.Skills;

/**
 * @author ReverendDread
 * Nov 27, 2018
 */
public class ZenyteCrafting extends Dialogue {

	private WorldObject object;
	
	private SmeltingBar[] craftables = new SmeltingBar[] {
			SmeltingBar.ZENYTE_AMULET,
			SmeltingBar.ZENYTE_BRACELET,
			SmeltingBar.ZENYTE_RING,
			SmeltingBar.ZENYTE_NECKLACE	
	};
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#start()
	 */
	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, 
				"How many bars you would like to smelt?<br>Choose a number, then click the bar to begin.", 1,
		new int[] { craftables[0].getProducedBar().getId(), 
					craftables[1].getProducedBar().getId(), 
					craftables[2].getProducedBar().getId(), 
					craftables[3].getProducedBar().getId() 
			}, new ItemNameFilter() {
			int count = 0;

			@Override
			public String rename(String name) {
				SmeltingBar bar = SmeltingBar.values()[count++];
				if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired())
					name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + bar.getLevelRequired();
				return name;

			}
		});
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#run(int, int)
	 */
	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new Smelting(craftables[SkillsDialogue.getItemSlot(componentId)], object, SkillsDialogue.getQuantity(player)));
		end();
	}

	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.content.dialogue.Dialogue#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}

}
