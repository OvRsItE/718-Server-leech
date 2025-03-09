package com.rs.game.world.entity.player.content;

import com.rs.Constants;
import com.rs.game.world.entity.player.Player;

/**
 * 
 * @author Xiles
 *
 */
public class News {

	public static void sendNews(Player player) {
		player.getInterfaceManager().sendInterface(1233);
		player.getPackets().sendIComponentText(1233, 1, "Welcome Back, "+ player.getDisplayName() +"!"); //Never Edit This For Updates
		player.getPackets().sendIComponentText(1233, 5, "Latest Update: "+ Constants.LASTEST_UPDATE + //Never Edit This For Updates
														" Talk to Kuradal at home for a slayer task!" +
														" Enjoy the update!");
		player.getPackets().sendIComponentText(1233, 6, "We hope you enjoy playing " + Constants.SERVER_NAME + "!");
		player.lock();
    }
	
	  public static void handleButtons(Player player, int componentId) {
	        if (componentId == 17) {
	   	        player.closeInterfaces();
	   	        player.unlock();
	        }
	  }
}
