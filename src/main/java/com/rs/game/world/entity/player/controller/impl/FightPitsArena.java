package com.rs.game.world.entity.player.controller.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.FightPits;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;

public class FightPitsArena extends Controller {

	@Override
	public void start() {
		sendInterfaces();
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 68222) {
			FightPits.leaveArena(player, 1);
			return false;
		}
		return true;
	}
	
	//fuck it dont dare touching here again or dragonkk(me) kills u irl :D btw nice code it keeps nulling, fixed
	
	@Override
	public boolean logout() {
		FightPits.leaveArena(player, 0);
		return false;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.getPackets().sendGameMessage("You can't teleport out of the arena!");
		return false;
	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		player.getPackets().sendGameMessage("You can't teleport out of the arena!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(Position toTile) {
		player.getPackets().sendGameMessage("You can't teleport out of the arena!");
		return false;
	}
	
	@Override
	public void magicTeleported(int type) {
		FightPits.leaveArena(player, 3); //teled out somehow, impossible usualy
	}
	
	@Override
	public boolean login() { //shouldnt happen
		removeControler();
		FightPits.leaveArena(player, 2);
		return false;
	}
	
	@Override
	public void forceClose() {
		FightPits.leaveArena(player, 3); 
	}
	
	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			if (canHit(target))
				return true;
			player.getPackets().sendGameMessage("You're not allowed to attack yet!");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canHit(Entity target) {
		return FightPits.canFight();
	}
	
	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("You have been defeated!");
				} else if (loop == 3) {
					player.reset();
					FightPits.leaveArena(player, 2);
					player.animate(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}
	
	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 34 : 0, 373);
		if(FightPits.currentChampion != null)
			player.getPackets().sendIComponentText(373, 10, "Current Champion: JaLYt-Ket-" + FightPits.currentChampion);
	}
}
