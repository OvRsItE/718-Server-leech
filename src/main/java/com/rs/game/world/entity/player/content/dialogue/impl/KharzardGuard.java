package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;


public class KharzardGuard extends Dialogue {

	private int npcId = 7528;
	
	@Override
	public void start() {
		if (player.AdventurerQuestProgress == 8) {
		sendNPCDialogue(npcId, ANGRY_FACE, "Heeey!! GET OUT OF HERE!!");
		stage = 0;
		for (NPC npc : World.getNPCs()) {
		    if (npc.getId() == npcId) {
		    	npc.faceEntity(player);
		    	npc.setAttackedBy(player);
		    	npc.setTarget(player);
		    	npc.setNextFaceEntity(player);
		    	//npc.setForceAgressive(true);
		    	npc.getCombat().getTarget();
		    }
		}
		}else {
			sendNPCDialogue(npcId, NORMAL, "Get out of here!!");
			for (NPC npc : World.getNPCs()) {
			    if (npc.getId() == npcId) {
			    	npc.faceEntity(player);
			    	npc.setAttackedBy(player);
			    	npc.setTarget(player);
			    }
			}
			stage = 0;
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			end();
			    
			
		}
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
	
}