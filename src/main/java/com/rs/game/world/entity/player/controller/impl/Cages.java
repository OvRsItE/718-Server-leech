package com.rs.game.world.entity.player.controller.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.others.CageNpc;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.activities.ForbiddenCages;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;

public class Cages extends Controller {

	@Override
	public void start() {
		ForbiddenCages.enterCage(player);
	}
	
	@Override
	public void process() {
		if(World.exiting_start != 0) {
		   safeShutdown();
		}
		player.closeInterfaces();
	}

	@Override
	public boolean canAttack(Entity target) {
		if(target instanceof Player) 
		if(((Player) target).getControlerManager().getControler() instanceof Cages)
		   return true;
		if(target instanceof CageNpc)
		   return true;
			
		return false;
	}

	@Override
	public boolean canAddInventoryItem(int itemId, int amount) {
		return true;
	}

	@Override
	public boolean canPlayerOption1(Player target) {
		if(target instanceof Player) 
			if(target.getControlerManager().getControler() instanceof Cages)
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
	public boolean canHit(Entity entity) {
		if(entity instanceof Player) 
			if(((Player) entity).getControlerManager().getControler() instanceof Cages)
			   return true;
		return false;
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
	public boolean processMagicTeleport(Position toTile) {
		ForbiddenCages.leaveCage(player, false);
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processItemTeleport(Position toTile) {
		ForbiddenCages.leaveCage(player, false);
		return false;
	}

	/**
	 * return can teleport
	 */
	@Override
	public boolean processObjectTeleport(Position toTile) {
		ForbiddenCages.leaveCage(player, false);
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
	public boolean processNPCClick1(NPC npc) {
		if(npc instanceof CageNpc)
		    return true;
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processNPCClick2(NPC npc) {
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
	 * return let default death
	 */
	@Override
	public boolean sendDeath() {
		player.animate(new Animation(836));
		Player killer = player.getMostDamageCages();
		if(killer != null) {
	        if(killer == player)
	        	return false;
			killer.sm("You killed "+player.getDisplayName());
		    player.sm("You got killed by "+killer.getDisplayName());
	    	ForbiddenCages.sendKill(player, killer, killer.cageStage);
		} else {
			ForbiddenCages.leaveCage(player, false);
		}
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 3) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
					player.reset();
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
		ForbiddenCages.leaveCage(player, true);
		return true;
	}

	@Override
	public void forceClose() {
		player.lock();
		ForbiddenCages.leaveCage(player, false);
	}
	
	public void safeShutdown() {
		player.sm("An update has been activated, the match has been canceled, it's a tie!");
		player.lock();
		ForbiddenCages.leaveCage(player, false);
	}

	@Override
	public boolean processItemOnNPC(NPC npc, Item item) {
		return false;
	}

	@Override
	public boolean canDropItem(Item item) {
		player.getInventory().deleteItem(item);
		player.sm("Dropping items isn't alowed in here");
		return false;
	}

	@Override
	public boolean canSummonFamiliar() {
		return false;
	}

	@Override
	public boolean handleItemOnObject(WorldObject object, Item item) {
		return false;
	}

	@Override
	public boolean handlePickUpItem(FloorItem item) {
		return false;
	}

}
