package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.ForceTalk;

public class BorkControler extends Controller {
	
	
	public static int borkStage;
	public NPC bork;

	@Override
	public void start() {
		borkStage = (int) getArguments()[0];
		bork = (NPC) getArguments()[1];
		process();
	}
	
	int stage = 0;
	
	@Override
	public void process() {
		if (borkStage == 0) {	
			if (stage == 0) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3114,
					5528, 0));
			}
			if (stage == 5) {
				sendInterfaces();
			}
			if (stage == 18) {
				player.getPackets().closeInterface(
					player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
				player.getDialogueManager().startDialogue("DagonHai", 7137, player, -1);
				player.getPackets().sendGameMessage("The choas teleporter transports you to an unknown portal.");
				removeControler();
			}
		} else if (borkStage == 1) {
			if (stage == 4) {
				//sendInterfaces();
				//bork.setCantInteract(true);
			} else if (stage == 14) {
				World.spawnNPC(7135, new Position(bork, 1), -1, 0, true, true);
				World.spawnNPC(7135, new Position(bork, 1), -1, 0, true, true);
				World.spawnNPC(7135, new Position(bork, 1), -1, 0, true, true);
				player.getPackets().closeInterface(
					player.getInterfaceManager().hasRezizableScreen() ? 1 : 11);
				bork.setCantInteract(false);
				bork.setNextForceTalk(new ForceTalk("Destroy the intruder, my Legions!"));
				removeControler();
			}
		}
			stage++;
	}
	
	@Override
	public void sendInterfaces() {
		if (borkStage == 0) {
			player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 692);
		} else if (borkStage == 1) {
			for (Entity t : bork.getPossibleTargets()) {
				Player pl = (Player) t;
				pl.getInterfaceManager().sendTab(
					pl.getInterfaceManager().hasRezizableScreen() ? 1 : 11, 691);
			}
		}
	}
	
	@Override
	public boolean processMagicTeleport(Position toTile) {
		return true;
	}
	
	@Override
	public boolean keepCombating(Entity target) {
		if (borkStage == 1 && stage == 4)
			return false;
		return true;
	}
	
	@Override
	public boolean canEquip(int slotId, int itemId) {
		if (borkStage == 1 && stage == 4)
			return false;
		return true;
	}
	
	@Override
	public boolean canAttack(Entity target) {
		if (borkStage == 1 && stage == 4)
			return false;
		return true;
	}
	
	@Override
	public boolean canMove(int dir) {
		if (borkStage == 1 && stage == 4)
			return false;
		return true;
	}

}
