package com.rs.game.world.entity.player.content.dialogue.impl;

import java.util.ArrayList;

import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.utility.Utils;

public class CatInteract extends Dialogue {
	
	@Override
	public void start() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Play with", "Chase-vermin", "Shoo away");
		stage = 0;
	}

	//Emotes
	//9163 mouse catch
	//9160 attack emote
	//9161 def emote
	//9166 roll around
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			if (componentId == OPTION_1) {
				if (player.getInventory().containsItem(1759, 1)) {
					player.faceEntity(player.getPet());
					player.getPet().faceEntity(player);
					player.getPet().animate(new Animation(-1));
					player.animate(new Animation(-1));
					end();
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "Maybe you should get a ball of wool first.");
				}
			} else if (componentId == OPTION_2) {
				if (!chase_vermin()) {
					sendDialogue("There are no vermin in the area.");
					stage = 2;
				}
			} else if (componentId == OPTION_3) {
				sendOptionsDialogue("Are you sure you want to shoo your cat away?", "Yes", "No");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				shoo();
			} else if (componentId == OPTION_2) {
				end();
			}
		} else if (stage == 2) {
			end();
		}
	}

	@Override
	public void finish() {

	}
	
	private boolean chase_vermin() {
		ArrayList<NPC> rats = new ArrayList<NPC>();
		for (NPC npc : World.getNPCs()) {
			if (!npc.withinDistance(player.getPet(), 12) || !isRat(npc) || npc.isDead())
				continue;
			rats.add(npc);
		}
		if (rats.isEmpty())
			return false;
		NPC rat = rats.get(Utils.random(rats.size()));
		player.setNextForceTalk(new ForceTalk("Go get'em puss!"));
		player.getPet().setNextForceTalk(new ForceTalk("Meow!"));
		player.getPet().setChasing(rat);
		return true;
	}
	
	public boolean isRat(NPC npc) {
		if (npc.getName().equalsIgnoreCase("hell-rat") || npc.getName().equalsIgnoreCase("rat")) 
			return true;
		return false;
	}
	
	private void shoo() {
		player.getPet().sendDeath(player);
		player.setPet(null);
	}

}
