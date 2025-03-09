package com.rs.game.world.entity.player.content;

import com.rs.Constants;
import com.rs.game.world.entity.player.Player;

public class PlayerSupport {
	/*
	 * Data
	 */
public static int SUPPORT = 617;
public static int Title = 7;
public static int Smalltitle = 8;
public static int UnderText = 9;
public static int Title2 = 10;
public static int Text2= 11;
public static int NextPageTitle = 12;
public static int NextPageText = 13;
public static int Author = 14;

	public static void sendSupport(Player player) {
		player.getPackets().sendIComponentText(SUPPORT, Title, Constants.SERVER_NAME + " - Noticeboard Information");
		player.getPackets().sendIComponentText(SUPPORT, Smalltitle, "The Noticeboard");
		player.getPackets().sendIComponentText(SUPPORT, UnderText, "Noticeboard can be used for character settings, game options, changing" +
				" display names and removing them. Also you can take rid of animations in skills." +
				"  You can enable old and new animations.");
		/*
		 * Another title
		 */
		player.getPackets().sendIComponentText(SUPPORT, Title2, "Categories of Noticeboard");
		player.getPackets().sendIComponentText(SUPPORT, Text2, "The categories should be very well explained. Anyways; " +
				" To optimize your skilling animations just simple select the ApolloRealm Options. It also contains: Login screen options," +
				" Experience options and Animation options.");
		player.getPackets().sendIComponentText(SUPPORT, NextPageTitle, "");
		player.getPackets().sendIComponentText(SUPPORT, NextPageText, "");
		
		
		player.getPackets().sendIComponentText(Author, NextPageTitle, " - " + Constants.SERVER_NAME + ".");
		
		player.getInterfaceManager().sendInterface(SUPPORT);
	}
	
	
}
