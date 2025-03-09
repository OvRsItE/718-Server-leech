package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class MarketShops extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello, I'm The Shop Manager. What Category Shops Would You Like To Access?" );
	}
	
	@SuppressWarnings("unused")
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
	    int option;
		sendOptionsDialogue("Shop Manager", "<shad=00FF00>Combat Shops", "<shad=FD3EDA>Range Shops", "<shad=FFCD05>Magic/Summ Shops", "<shad=0066CC>Skilling Shops", "<shad=339933>Food & Accessories Shops");
		stage = 2;
		} else if (stage == 2) {
		if(componentId == OPTION_1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Which Combat Shop Would You Like To Access?" }, IS_NPC, npcId, 9827);
			player.getDialogueManager().startDialogue("CombatShops");
			end();
		}
		if(componentId == OPTION_2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Which Range Shop Would You Like To Access?" }, IS_NPC, npcId, 9827);
			player.getDialogueManager().startDialogue("RangeShops");
			end();
		}
        	if(componentId == OPTION_3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Which Magic Shop Would You Like To Access?" }, IS_NPC, npcId, 9827);
        		player.getDialogueManager().startDialogue("Scavo");
        	}
		if(componentId == OPTION_4) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Which Skilling Shop Would You Like To Access?" }, IS_NPC, npcId, 9827);
        		player.getDialogueManager().startDialogue("SkillingShops");
        	}
		if(componentId == OPTION_5) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Which Replenishing Shop Would You Like To Access?" }, IS_NPC, npcId, 9827);
        		player.getDialogueManager().startDialogue("Replenish");
			end();
	}
} else if (stage == 3) {
			sendOptionsDialogue("Shop Manager", "<shad=00FF00>Herblore shop", "<shad=FD3EDA>Crafting shop", "<shad=05F7FF>Fletching shop", "<shad=FFCD05>All-in-one shop");
			stage = 2;
	  }
	}

	@Override
	public void finish() {

	}

}