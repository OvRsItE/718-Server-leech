/**
 * 
 */
package com.rs.game.world.entity.player.content.teleport;

import java.util.concurrent.TimeUnit;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.item.Item;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.utility.NPCCombatDefinitionsL;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ReverendDread
 * Sep 21, 2018
 */
public class TeleportInterface {
	
	private Player player;
	
	private static final int INTERFACE_ID = 1364;
	
	private static final int NAME = 79;
	
	private static final int[] STATS = new int[] { 80, 83, 84 };
	
	private static final int[] INFORMATION = new int[] { 79, 62, 63, 64, 65, 66, 67, 68 };
	
	private static final int[] OPTIONS = new int[] { 33, 34, 35, 36, 37, 38, 39, 40, 41 };
	
	private static final int[] DROPS_CONTAINER = new int[] { 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95 };
	
	private static final int TELEPORT_BUTTON = 77;
	
	private static final int TITLE = 16;
	
	private static final int NEXT_BUTTON = 46;
	
	private static final int PREVIOUS_BUTTON = 48;
	
	private static final int CATAGORY_COMPONENT = 31;
	
	@Getter private Teleports[][] teleports;
	
	private int page;
	
	private int viewing;
	
	/**
	 * Teleport Interface contructor.
	 * @param player
	 * 			the player.
	 */
	public TeleportInterface(Player player) {
		this.player = player;
	}

	/**
	 * Opens teleport interface with a specific teleport displayed.
	 * @param teleport
	 * 			the displayed teleport.
	 * @param player
	 * 			the player.
	 */
	public final void open(Teleports[][] teleports) {
		
		this.teleports = teleports;
		
		player.getInterfaceManager().sendInterface(INTERFACE_ID);
		
		player.getPackets().sendIComponentText(INTERFACE_ID, TITLE, Teleports.catagoryToString(this.teleports[page][viewing].getCatagory()));		
		player.getPackets().sendIComponentText(INTERFACE_ID, CATAGORY_COMPONENT, Teleports.catagoryToString(this.teleports[page][viewing].getCatagory()));
		
		//Information
		player.getPackets().sendIComponentText(INTERFACE_ID, 82, "Information");		
		for (int index = 0; index < INFORMATION.length; index++) {	
			String information = null;
			if (index <= this.teleports[page][viewing].getInformation().length - 1) 
				information = this.teleports[page][viewing].getInformation()[index];
			player.getPackets().sendIComponentText(INTERFACE_ID, INFORMATION[index], (information == null ? "" : information));	
		}
		
		//Teleport options.
		for (int index = 0; index < OPTIONS.length; index++) {
			String name = null;
			if (index <= this.teleports[page].length - 1)
				name = this.teleports[page][index].getName();
			player.getPackets().sendIComponentText(INTERFACE_ID, OPTIONS[index], (name == null ? "" : name));	
		}
		
		//Stats
		if (this.teleports[page][viewing].getMonsterId() != -1) {
	 		player.getPackets().sendIComponentText(INTERFACE_ID, 69, "Stats");
			NPCCombatDefinitions definition = NPCCombatDefinitionsL.getNPCCombatDefinitions(this.teleports[page][viewing].getMonsterId());
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[0], "Combat Level: TODO");	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[1], "Hitpoints: " + definition.getHitpoints());	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[2], "Max Hit: " + definition.getMaxHit());	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[3], "Respawn Time: " + definition.getRespawnDelay() + " ticks.");	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[4], "");
		} else {
	 		player.getPackets().sendIComponentText(INTERFACE_ID, 69, "Stats");
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[0], "");	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[1], "");	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[2], "");	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[3], "");	
			player.getPackets().sendIComponentText(INTERFACE_ID, STATS[4], "");	
		}
	
		//Drops
		for (int index = 0; index < DROPS_CONTAINER.length; index++) {
			player.getPackets().sendIComponentSprite(INTERFACE_ID, DROPS_CONTAINER[index], -1);
		}
		
		player.getPackets().sendIComponentText(INTERFACE_ID, NAME, this.teleports[page][viewing].getName());
		
		player.setCloseInterfacesEvent(() -> {
			viewing = 0;
			page = 0;
		});
	}

	/**
	 * Handles buttons for teleports interface.
	 * @param player
	 * 			the player.
	 * @param componentId
	 * 			the componentId
	 */
	public void handleButtons(Player player, int componentId) {
		if (componentId == TELEPORT_BUTTON) {
			String name = teleports[page][viewing].getName();
			//Godwars exception.
			if (name.equalsIgnoreCase("general graardor") || name.equalsIgnoreCase("kree'arra") 
					|| name.equalsIgnoreCase("k'ril tsutsaroth") || name.equalsIgnoreCase("commander zilyana")) {
				CoresManager.slowExecutor.schedule(() -> {
					player.getControlerManager().startControler("GodWars");
				}, 2400, TimeUnit.MILLISECONDS);
			}
			Magic.sendNormalTeleportSpell(player, 0, 0, teleports[page][viewing].getLocation());
			return;
		}
		if (componentId == NEXT_BUTTON) {
			if ((page++) >= (teleports.length - 1)) {
				page = 0;
			}
			viewing = 0;
		}
		if (componentId == PREVIOUS_BUTTON) {
			if ((page--) <= 0) {
				page = 0;
			}
			viewing = 0;
		}
		int componentValue = isOptionButton(componentId);
		if (componentValue != -1 && componentValue <= (teleports[page].length - 1)) {
			viewing = componentValue;
		}
		open(this.teleports);
	}
	
	/**
	 * Returns the component index clicked on for telport selection.
	 * @param componentId
	 * 			origin componentId
	 * @return
	 * 			the index of teleport.
	 */
	private static int isOptionButton(int componentId) {
		for (int index = 0; index < OPTIONS.length; index++) {
			if (componentId == OPTIONS[index])
				return index;
		}
		return -1;
	}
	
}
