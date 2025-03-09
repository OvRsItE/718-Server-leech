package com.rs.game.world.entity.player.content;

import com.rs.game.world.entity.player.Player;

/**
 * 
 * @author Xiles
 *
 */
public class SerenityNews {
	
 public static int NEWS = 205;
 
	public static void MainBoard(Player player) {
		if (player.ToggleNews = true) {
			/*
			 * Category
			 */
			player.getPackets().sendIComponentText(NEWS, 61, "Latest Update");
			player.getPackets().sendIComponentText(NEWS, 57, "News");
			player.getPackets().sendIComponentText(NEWS, 53, "Events");
			player.getPackets().sendIComponentText(NEWS, 49, "Coming soon..");
			player.getPackets().sendIComponentText(NEWS, 65, "Welcome to ApolloRealm");
			player.getPackets().sendIComponentText(NEWS, 64, "Select an option from the category what you" +
					" would like to see.");
		player.getInterfaceManager().sendInterface(NEWS);
	} else {
		player.sm("You have skipped news upon login, to turn back on go to Noticeboard.");
	}
	}
	public static void sendNews(Player player) {
			player.closeInterfaces();
			player.getPackets().sendIComponentText(NEWS, 61, "Latest Update");
			player.getPackets().sendIComponentText(NEWS, 57, "News");
			player.getPackets().sendIComponentText(NEWS, 53, "Events");
			player.getPackets().sendIComponentText(NEWS, 49, "Coming soon..");
			/*Title*/
			player.getPackets().sendIComponentText(NEWS, 65, "Latest Update");
			player.getPackets().sendIComponentText(NEWS, 64, "The Fight Cave has been re-named Fight Arena " +
															 "and has been modified greatly.");
			player.getInterfaceManager().sendInterface(NEWS);
	} 
	public static void sendUpdateLogs(Player player) {
		player.closeInterfaces();
		player.getPackets().sendIComponentText(NEWS, 61, "Latest Update");
		player.getPackets().sendIComponentText(NEWS, 57, "News");
		player.getPackets().sendIComponentText(NEWS, 53, "Events");
		player.getPackets().sendIComponentText(NEWS, 49, "Coming soon..");
		/*Title*/
		player.getPackets().sendIComponentText(NEWS, 65, "News");
		player.getPackets().sendIComponentText(NEWS, 64, "We now have forums just do ;;forums");
		player.getInterfaceManager().sendInterface(NEWS);
	} 
	
	public static void sendNotifications(Player player) {
	player.closeInterfaces();
	player.getPackets().sendIComponentText(NEWS, 61, "Latest Update");
	player.getPackets().sendIComponentText(NEWS, 57, "News");
	player.getPackets().sendIComponentText(NEWS, 53, "Events");
	player.getPackets().sendIComponentText(NEWS, 49, "Coming soon..");
	/*Title*/
	player.getPackets().sendIComponentText(NEWS, 65, "Events");
	player.getPackets().sendIComponentText(NEWS, 64, "Double Xp Friday-Monday");
	player.getInterfaceManager().sendInterface(NEWS);
	} 

	public static void sendEvents(Player player) {
	player.closeInterfaces();
	player.getPackets().sendIComponentText(NEWS, 61, "Latest Update");
	player.getPackets().sendIComponentText(NEWS, 57, "News");
	player.getPackets().sendIComponentText(NEWS, 53, "Events");
	player.getPackets().sendIComponentText(NEWS, 49, "Coming soon..");
	/*Title*/
	player.getPackets().sendIComponentText(NEWS, 65, "Coming Soon");
	player.getPackets().sendIComponentText(NEWS, 64, "Nothing here.");
	player.getInterfaceManager().sendInterface(NEWS);
	} 
	 public static void handleButtons(Player player, int componentId) {
		  if (componentId == 49) {	
		        sendEvents(player);
		        
		        }
		  if (componentId == 57) {	
		        sendUpdateLogs(player);
		        }
		  if (componentId == 53) {	
		        sendNotifications(player);
		        }
	        if (componentId == 61) {	
	        sendNews(player);
	        }
	 }

	
}
