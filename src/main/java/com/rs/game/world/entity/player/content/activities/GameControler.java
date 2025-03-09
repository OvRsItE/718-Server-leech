package com.rs.game.world.entity.player.content.activities;

import java.util.concurrent.TimeUnit;

import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.utility.Logger;

public class GameControler extends Controller {

	@Override
	public boolean canAddInventoryItem(int itemId, int amount) {
		return true;
	}
	
	@Override
	public boolean canAttack(Entity target) {
		if(target instanceof Player) 
		if(((Player) target).getControlerManager().getControler() instanceof GameControler)
		   return true;
			
		return false;
	}
	
	@Override
	public boolean canDropItem(Item item) {
		player.getInventory().deleteItem(item);
		return false;
	}

	@Override
	public boolean canHit(Entity entity) {
		if(entity instanceof Player) 
			if(((Player) entity).getControlerManager().getControler() instanceof GameControler)
			   return true;
		return false;
	}

	@Override
	public boolean canPlayerOption1(Player target) {
		if(target instanceof Player) 
			if(target.getControlerManager().getControler() instanceof GameControler)
			   return true;
		return false;

	}

	public boolean canPlayerOption2(Player target) {
		return false;
	}
	
	public boolean canPlayerOption5(Player target) {
		return false;
	}

	@Override
	public boolean canSummonFamiliar() {
		return false;
	}
	
	@Override
	public void forceClose() {
		HungerGames.leaveArena(player);
	}

	@Override
	public boolean handleItemOnObject(WorldObject object, Item item) {
		return false;
	}
	
	@Override
	public boolean handlePickUpItem(FloorItem item) {
		return true;
	}

	/**
	 * return remove controler
	 */
	@Override
	public boolean login() {
		return true;
	}

	/**
	 * return remove controler
	 */
	@Override
	public boolean logout() {
		player.lock();
		HungerGames.leaveArena(player);
		player.setLocation(Constants.HOME_PLAYER_LOCATION1);
		return true;
	}

	@Override
	public void process() {
		sendInterfaces();
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if(interfaceId == 746 || interfaceId == 271 || interfaceId == 430 || interfaceId == 884 || interfaceId == 387 || interfaceId == 193
		|| interfaceId == 192 || interfaceId == 749 || interfaceId == 750 || interfaceId == 670  || interfaceId == 679)
			return true;
		return false;
	}

	@Override
	public boolean processItemOnNPC(NPC npc, Item item) {
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processItemTeleport(Position toTile) {
		player.sm("Can't do this here");
		return false;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		player.sm("Can't do this here");
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processNPCClick1(NPC npc) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processNPCClick2(NPC npc) {
		player.sm("You cant do this");
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processNPCClick3(NPC npc) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(WorldObject object) {
		return false;
	}
	
	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick2(WorldObject object) {
		return false;
	}
	
	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick3(WorldObject object) {
		return false;
	}
	
	@Override
	public boolean processObjectClick5(WorldObject object) {
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processObjectTeleport(Position toTile) {
		player.sm("Can't do this here");
		return false;
	}

	public void safeShutdown() {
		player.sm("An update has been activated, the match has been canceled, it's a tie!");
		player.lock();
		HungerGames.leaveArena(player);
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
					HungerGames.leaveArena(player);
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
		player.getInterfaceManager().sendOverlay(1296, false);
		player.getPackets().sendIComponentText(1296, 19, "Players left:");
		player.getPackets().sendIComponentText(1296, 25, ""+HungerGames.getPlayers().size());
		player.getPackets().sendIComponentText(1296, 20, "Minutes left:");
		player.getPackets().sendIComponentText(1296, 26, ""+(11 - Lobby.minutes)+"");
		player.getPackets().sendIComponentText(1296, 24, "");
		player.getPackets().sendIComponentText(1296, 27, "");
	}

	@Override
	public void start() {
		if(player.isPublicWildEnabled())
		   player.switchPvpModes();
		sendInterfaces();
		player.getInventory().init();
		player.getEquipment().init();
		player.sm("In 10 seconds you may begin the Hunger Games");
		player.sm("When it begins you will have an additional 15 seconds to gather supplies");
		player.setDirection(ForceMovement.WEST);
		player.setCantTrade(false);
		player.lock(18);//18 because it seems to unlock before 10 seconds
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					if(player.getControlerManager().getControler() instanceof GameControler) {
					player.setCanPvp(true);
					Entity p2 = player;
					p2.setAtMultiArea(true);
					player.sm("<col=ff000> You may now fight!!!");
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 30, TimeUnit.SECONDS);
	}

}
