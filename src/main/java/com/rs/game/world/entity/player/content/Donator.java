package com.rs.game.world.entity.player.content;

import com.rs.game.world.entity.player.Player;
import com.rs.utility.ShopsHandler;

/**
 * 
 * @author Nexon | Fuzen Seth
 *
 */
public class Donator {
	
public static int BOARD =  72;
	
	public static void sendBoard(Player player) {
		player.getPackets().sendIComponentText(BOARD, 55, "Shops");
		player.getPackets().sendIComponentText(BOARD, 31, "Magic Shop");
		player.getPackets().sendIComponentText(BOARD, 32, "Range Shop");
		player.getPackets().sendIComponentText(BOARD, 33, "Armor Shop");
		player.getPackets().sendIComponentText(BOARD, 34, "Skilling Supplies");
		player.getPackets().sendIComponentText(BOARD, 35, "Fishing Supplies");
		player.getPackets().sendIComponentText(BOARD, 36, "Herb Supplies");
		player.getPackets().sendIComponentText(BOARD, 37, "Pure Supplies");
		player.getPackets().sendIComponentText(BOARD, 38, "Runecrafting");
		player.getPackets().sendIComponentText(BOARD, 39, "General Store");
		player.getPackets().sendIComponentText(BOARD, 40, "Next page");
		player.getInterfaceManager().sendInterface(BOARD);
		player.getInventory().refresh();
	}
    public static void handleButtons(Player player, int componentId) {
    	
        if (componentId == 31) {
        	player.closeInterfaces();

        }
        if (componentId == 32) {
        	player.closeInterfaces();

        }
        if (componentId == 33) {
        	player.closeInterfaces();
	
        }
        if (componentId == 34) {
        	player.closeInterfaces();
	
        }
        if (componentId == 35) {
        	player.closeInterfaces();

        }
        if (componentId == 36) {
        	        	player.closeInterfaces();

        }
        if (componentId == 37) {
        	player.closeInterfaces();

        }
        if (componentId == 38) {
        	player.closeInterfaces();

        }
        if (componentId == 39) {
        	player.closeInterfaces();

        }
        if (componentId == 40) {
        	player.closeInterfaces();

        }
    }
	
}
