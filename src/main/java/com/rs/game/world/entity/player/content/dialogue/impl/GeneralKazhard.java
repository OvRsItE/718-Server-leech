package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.quests.impl.KharzardLures;


public class GeneralKazhard extends Dialogue {
	
	private int npcId = 7551;

	@Override
	public void start() {
		if (player.AdventurerQuestProgress == 17) {
			if (!player.getEquipment().hasWeapon() && !player.getEquipment().wearingArmour()) {
				sendNPCDialogue(npcId, NORMAL, "You impress me! I must say!");
				stage = 20;
			}else {
				sendNPCDialogue(npcId, NORMAL, "You must take off all your armour and weapon before we can fight.");
				stage = 0;
			}
		}else
		if (player.AdventurerQuestProgress == 16) {
			sendPlayerDialogue(NORMAL, "GENERAL!! You must stop what you are planning right now!");
			stage = 1;
		}else {
			sendNPCDialogue(npcId, NORMAL, "SOON.. I be the king of all kingdoms!");
			stage = 0;
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			end();
		}else
		if (stage == 1) {
			sendPlayerDialogue(NORMAL, "I know what you are up to! so does the city! They are prepared for anything that is comming!");
			stage = 2;
		}else
		if (stage == 2) {
			sendPlayerDialogue(NORMAL, "I am here to prevent anything from happening!");
			stage = 3;
		}else
		if (stage == 3) {
			sendNPCDialogue(npcId, FUNNY, "HAHA who do you think you are??");
			stage = 4;
		}else
		if (stage == 4) {
			sendNPCDialogue(npcId, NORMAL, "We are engaging tonight!");
			stage = 5;
		}else
		if (stage == 5) {
			sendPlayerDialogue(NORMAL, "No you are not, i challenge you to a fight!");
			stage = 6;
		}else
		if (stage == 6) {
			sendNPCDialogue(npcId, NORMAL, "That wont change the fact of us getting back our home tonight!");
			stage = 7;
		}else
		if (stage == 7) {
			sendPlayerDialogue(NORMAL, "I want to work a deal with you, if i defeat you, you will order your army to stop the attack!");
			stage = 8;
		}else
		if (stage == 8) {
			KharzardLures.handleProgressQuest(player);
			sendNPCDialogue(npcId, NORMAL, "I only fight to the death, and you must not wear any form of weapons or armor.");
			stage = 9;
		}else
		if (stage == 9) {
			sendNPCDialogue(npcId, NORMAL, "Come back to me when you arent wearing any weapons or armor.");
			stage = 0;
		}else
		if (stage == 20) {
			sendPlayerDialogue(NORMAL, "That's not what i am trying to..");
			stage = 21;
		}else
		if (stage == 21) {
			sendNPCDialogue(npcId, NORMAL, "Anyways, let's start the fighting!");
			stage = 22;
		}else
		if (stage == 22) {
			end();
			World.spawnNPC(7552, new Position(2717, 9669, 0), -1, 0, true, true);
			player.setNextPosition(new Position(2718, 9669, 0));
			KharzardLures.handleProgressQuest(player);
			
			for (NPC npc : World.getNPCs()) {
				if (npc.getId() == 7552) {
				npc.getCombat().setTarget(player);
				player.getHintIconsManager().addHintIcon(World.getNPC(7552), 1, -1, false);
				npc.setCombatLevel(250);
				npc.faceEntity(player);
				npc.getCombatDefinitions().setHitpoints(2000);
				npc.setHitpoints(2000);
				}
			}
			
			
		}
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		}
	}