package com.rs.game.world.entity.player;

import com.rs.Constants;
import com.rs.game.item.Item;
import com.rs.game.world.entity.player.content.ActionTab;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.social.clanchat.ClansManager;

import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings("unused")
public class InterfaceManager {

	public static final int FIXED_WINDOW_ID = 548;
	public static final int RESIZABLE_WINDOW_ID = 746;
	public static final int CHAT_BOX_TAB = 13;
	public static final int FIXED_SCREEN_TAB_ID = 27;
	public static final int RESIZABLE_SCREEN_TAB_ID = 28;
	public static final int FIXED_INV_TAB_ID = 166;
	public static final int RESIZABLE_INV_TAB_ID = 108;
	private Player player;

	private final ConcurrentHashMap<Integer, int[]> openedinterfaces = new ConcurrentHashMap<Integer, int[]>();
	private final ConcurrentHashMap<Integer, Integer> openedinterfacesb = new ConcurrentHashMap<Integer, Integer>();

	public boolean questScreen;
	private boolean resizableScreen;
	private int windowsPane;

	/*public void sendTimerInterface(Player player) {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, 3016);
		player.getPackets().sendIComponentText(3000, 5, "");
	       player.getPackets().sendIComponentText(3000, 6, "");
		player.getPackets().sendIComponentText(3000, 7, "");
		player.getPackets().sendIComponentText(3000, 8, "");
		player.getPackets().sendIComponentText(3000, 9, "");
		}*/



	public void sendCountDown() {
		player.getInterfaceManager().sendOverlay(57, false);
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 34 : 0, 57);
	}

	public void sendTimerInter() {
		player.getInterfaceManager().sendOverlay(373, false);
		player.getPackets().sendIComponentText(373, 0, "Please pick a game mode.");
		player.getPackets().sendIComponentText(373, 10, "Please pick a game mode.");
	}

	public void sendLoginInter() {
		player.lock();
		player.getInterfaceManager().sendInterface(1026);
		player.getPackets().sendIComponentText(1026, 18, "Please pick a game mode.");
		player.getPackets().sendIComponentText(1026, 22, "Regular Mode");
		player.getPackets().sendIComponentText(1026, 24, "<img=5>Ironman Mode");
		player.getPackets().sendIComponentText(1026, 27, "Continue");
	}

	public void sendConfirmInter() {
		player.lock();
		player.getInterfaceManager().sendInterface(1021);
		player.getPackets().sendIComponentText(1021, 21, "Confirm choice.");
		player.getPackets().sendIComponentText(1021, 23, "This is a permenent choice.");
		player.getPackets().sendIComponentText(1021, 24, "Are u sure?");
		player.getPackets().sendIComponentText(1021, 27, "Back");
		player.getPackets().sendIComponentText(1021, 31, "Confirm");
		player.getPackets().sendIComponentText(1021, 29, "Upon confirming, you wont be able to view this menu again.");
	}

	public void sendTeleportsInter() {
		player.getInterfaceManager().sendInterface(813);
		player.getPackets().sendItemOnIComponent(813, 33, 1050, 1);
		player.getPackets().sendIComponentText(813, 80,  Constants.SERVER_NAME); //Title
		player.getPackets().sendIComponentText(813, 15,  "Where would you like to go, "+player.getDisplayName()+"?"); //Desc
		player.getPackets().sendIComponentText(813, 29,  "Bossing"); //Boss Teleports
		player.getPackets().sendIComponentText(813, 52,  "Godwars");
		player.getPackets().sendIComponentText(813, 53,  "Corporeal Beast");
		player.getPackets().sendIComponentText(813, 54,  "Mercenary Mage");
		player.getPackets().sendIComponentText(813, 55,  "Lucien's Game of Demon's");
		player.getPackets().sendIComponentText(813, 56,  "Blink");
		player.getPackets().sendIComponentText(813, 60,  "Dagannoth Prime");
		player.getPackets().sendIComponentText(813, 57,  "Yk'Lagor The Thunderous");
		player.getPackets().sendIComponentText(813, 58,  "Nex");
		player.getPackets().sendIComponentText(813, 59,  "QBD");
		player.getPackets().sendIComponentText(813, 61,  "Glacors");

		player.getPackets().sendIComponentText(813, 30,  "Skilling");  //Skilling Teleports
		player.getPackets().sendIComponentText(813, 43,  "Fishing");
		player.getPackets().sendIComponentText(813, 44,  "Mining");
		player.getPackets().sendIComponentText(813, 45,  "Hunter");
		player.getPackets().sendIComponentText(813, 46,  "Woodcutting");
		player.getPackets().sendIComponentText(813, 47,  "RuneCrafting");
		player.getPackets().sendIComponentText(813, 48,  "Summoning");
		player.getPackets().sendIComponentText(813, 49,  "Agility Courses");
		player.getPackets().sendIComponentText(813, 50,  "Cooking");
		player.getPackets().sendIComponentText(813, 51,  "Thieving");

		player.getPackets().sendIComponentText(813, 31,  "Minigames"); //Minigames
		player.getPackets().sendIComponentText(813, 37,  "Pest Control");
		player.getPackets().sendIComponentText(813, 38,  "Clan Wars");
		player.getPackets().sendIComponentText(813, 39,  "Castle Wars");
		player.getPackets().sendIComponentText(813, 40,  "Barrows");
		player.getPackets().sendIComponentText(813, 71,  "Livid Farm - Farming");
		player.getPackets().sendIComponentText(813, 41,  "Fight Caves");
		player.getPackets().sendIComponentText(813, 42,  "Fight Kiln");

		player.getPackets().sendIComponentText(813, 32,  "Misc Teleports"); //Misc Teles
		player.getPackets().sendIComponentText(813, 64,  "Troll Zone");
		player.getPackets().sendIComponentText(813, 65,  "Chill Zone");
		player.getPackets().sendIComponentText(813, 66,  "Forinthry Dungeon");
		player.getPackets().sendIComponentText(813, 67,  "Training Cave");
		player.getPackets().sendIComponentText(813, 68,  "Bork");
		player.getPackets().sendIComponentText(813, 69,  "PolyPore");
		player.getPackets().sendIComponentText(813, 70,  "Recipe For Disaster");

		player.getPackets().sendIComponentText(813, 36,  "Points");
		//player.getPackets().sendIComponentText(813, 105,  "DT: "+player.dungTokens+"");
		player.getPackets().sendIComponentText(813, 107,  "Cmb: "+player.getSkills().getCombatLevelWithSummoning()+"");
	}

	public void SendMoreBosses() {
		player.getInterfaceManager().sendInterface(1156);

		player.getPackets().sendIComponentText(1156, 108,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 109,  ""); //Description
		player.getPackets().sendIComponentText(1156, 90,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 113,  "Hope Devourer"); //Title
		player.getPackets().sendIComponentText(1156, 114,  ""); //Description
		player.getPackets().sendIComponentText(1156, 206,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 137,  "Sunfreet"); //Title
		player.getPackets().sendIComponentText(1156, 138,  ""); //Description
		player.getPackets().sendIComponentText(1156, 254,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 110,  "Har'lakk"); //Title
		player.getPackets().sendIComponentText(1156, 111,  ""); //Description
		player.getPackets().sendIComponentText(1156, 200,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 116,  "Madara"); //Title
		player.getPackets().sendIComponentText(1156, 117,  "OP Boss be warned!"); //Description
		player.getPackets().sendIComponentText(1156, 212,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 134,  "Mith Dragons"); //Title
		player.getPackets().sendIComponentText(1156, 135,  ""); //Description
		player.getPackets().sendIComponentText(1156, 248,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 122,  "Jelvis"); //Title
		player.getPackets().sendIComponentText(1156, 123,  ""); //Description
		player.getPackets().sendIComponentText(1156, 230,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 128,  "Madara"); //Title
		player.getPackets().sendIComponentText(1156, 129,  "OP Boss be warned!"); //Description
		player.getPackets().sendIComponentText(1156, 236,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 128,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 129,  ""); //Description
		player.getPackets().sendIComponentText(1156, 236,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 125,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 126,  ""); //Description
		player.getPackets().sendIComponentText(1156, 224,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 146,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 147,  ""); //Description
		player.getPackets().sendIComponentText(1156, 272,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 143,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 144,  ""); //Description
		player.getPackets().sendIComponentText(1156, 266,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 119,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 120,  ""); //Description
		player.getPackets().sendIComponentText(1156, 218,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 131,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 132,  ""); //Description
		player.getPackets().sendIComponentText(1156, 242,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 140,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 141,  ""); //Description
		player.getPackets().sendIComponentText(1156, 260,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 149,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 150,  ""); //Description
		player.getPackets().sendIComponentText(1156, 278,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 152,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 153,  ""); //Description
		player.getPackets().sendIComponentText(1156, 284,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 167,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 168,  ""); //Description
		player.getPackets().sendIComponentText(1156, 308,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 155,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 157,  "Sometimes I moo"); //Description
		player.getPackets().sendIComponentText(1156, 290,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 159,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 161,  "Sometimes I moo"); //Description
		player.getPackets().sendIComponentText(1156, 296,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 163,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 165,  ""); //Description
		player.getPackets().sendIComponentText(1156, 302,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 170,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 171,  ""); //Description
		player.getPackets().sendIComponentText(1156, 314,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 318,  "Sea Troll Queen"); //Title
		player.getPackets().sendIComponentText(1156, 319,  ""); //Description
		player.getPackets().sendIComponentText(1156, 326,  "Teleport!"); //Button Name

		player.getPackets().sendIComponentText(1156, 164,  "null");
	}

	public boolean isResizableScreen() {
		return resizableScreen;
	}

	public InterfaceManager(Player player) {
		this.player = player;
	}

	public void sendSquealOverlay() {
		setWindowInterface(resizableScreen ? 0 : 10, 1252); // TODO not working for fixed
	}

	public void closeSquealOverlay() {
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
	}


	private void clearChilds(int parentInterfaceId) {
		for (int key : openedinterfaces.keySet()) {
			if(key >> 16 == parentInterfaceId) {
				openedinterfaces.remove(key);
			}
		}
	}

	private void clearChildsB(int parentInterfaceId) {
		for (int key : openedinterfaces.keySet()) {
			if(key >> 16 == parentInterfaceId) {
				openedinterfaces.remove(key);
			}
		}
	}


	public void sendTab(int tabId, int interfaceId) {
		player.getPackets().sendInterface(true,
				resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, tabId,
						interfaceId);
	}

	public void sendChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, CHAT_BOX_TAB, interfaceId);
	}

	public void closeChatBoxInterface() {
		player.getPackets().closeInterface(CHAT_BOX_TAB);
	}

	public void sendOverlay(int interfaceId, boolean fullScreen) {
		sendTab(resizableScreen ? fullScreen ? 1 : 11 : 0, interfaceId);
	}

	public void closeOverlay(boolean fullScreen) {
		player.getPackets().closeInterface(resizableScreen ? fullScreen ? 1 : 11 : 0);
	}

	public void sendInterface(int interfaceId) {
		player.getPackets()
		.sendInterface(
				false,
				resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID,
						resizableScreen ? RESIZABLE_SCREEN_TAB_ID
								: FIXED_SCREEN_TAB_ID, interfaceId);
	}
	
	public void sendOverlay(int interfaceId, int tab, boolean fullScreen) {
		player.getPackets().sendInterface(false, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, tab, interfaceId);
	}

	public void setInterface(boolean clickThrought, int parentInterfaceId, int parentInterfaceComponentId, int interfaceId) {
		int parentUID = getComponentUId(parentInterfaceId, parentInterfaceComponentId);
		Integer oldInterface = openedinterfacesb.get(parentUID);
		if(oldInterface != null) {
			clearChildsB(oldInterface);
		}
		openedinterfacesb.put(parentUID, interfaceId); //replaces inter if theres one in that component already
		player.getPackets().sendInterface(clickThrought, parentUID, interfaceId);
	}


	public static int getComponentUId(int interfaceId, int componentId) {
		return interfaceId << 16 | componentId;
	}

	public void sendKillCount() {
		player.getInterfaceManager().sendInterface(601);
		player.getPackets().sendIComponentText(164, 8,  ""+ player.armadyl +"");
		player.getPackets().sendIComponentText(164, 9,  ""+ player.bandos +"");
		player.getPackets().sendIComponentText(164, 10,  ""+ player.saradomin +"");
		player.getPackets().sendIComponentText(164, 11,  ""+ player.zamorak +"");
	}

	public void sendHelp() {
		player.getInterfaceManager().sendInterface(1225);
		player.getPackets().sendIComponentText(1225, 8, "Latest " + Constants.SERVER_NAME + " Updates!");
		player.getPackets().sendIComponentText(1225, 5, "");
		player.getPackets().sendIComponentText(1225, 21, "If you are new to the area and would like to train, there are plenty of monsters around here!");
		player.getPackets().sendIComponentText(1225, 22, "There are goblins, cows, and more around here! If you travel north of the castle and head down the trap door, you will find a sweet training area filled with Rock Crabs and more!");
		player.getPackets().sendIComponentText(1225, 20, "Close!");
	}

	public void sendRuneMysteries() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Rune Mysteries");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to the</col> <col=660000>Duke</col>");
		player.getPackets().sendIComponentText(275, 12, "<col=330099>who can be found in the </col><col=660000>Lumbridge Castle.</col>");
		player.getPackets().sendIComponentText(275, 13, "");
		if (player.RM == 1) {
			player.getPackets().sendIComponentText(275, 14, "<col=330099>I should take this talisman to the head wizard who</col>");
			player.getPackets().sendIComponentText(275, 15, "<col=330099>can be found at the </col><col=660000>Wizard's Guild.</col>");
		} else if (player.RM == 2 || player.RM == 3) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should take this talisman to the head wizard who</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>can be found at the </col><col=660000>Wizard's Guild.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<col=330099>I have given the talisman to the head wizard. Now I must deliver </col>");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>this parcel to </col><col=660000>Aubury</col><col=330099> in Varrock.</col>");
		} else if (player.RM == 4) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should take this talisman to the head wizard who</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>can be found at the </col><col=660000>Wizard's Guild.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>I have given the talisman to the head wizard. Now I must deliver </col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>this parcel to </col><col=660000>Aubury</col><col=330099> in Varrock.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "");
			player.getPackets().sendIComponentText(275, 19, "QUEST COMPLETE!");
		}
		for (int i = 20; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

	public void sendAchievementInterface() {
		player.getInterfaceManager().sendInterface(1156);
		player.getPackets().sendIComponentText(1156, 190, ""+ player.getUsername() + "'s Achievements");
		player.getPackets().sendIComponentText(1156, 108, "Varrock Armour (1)");
		player.getPackets().sendIComponentText(1156, 109, "Varrock Achievement: <col=1E0AD1>Beginner</col>");
		player.getPackets().sendIComponentText(1156, 90, "View");
		player.getPackets().sendIComponentText(1156, 113, "Varrock Armour (2)");
		player.getPackets().sendIComponentText(1156, 114, "Varrock Achievement: <col=1ED10A>Easy</col>");
		player.getPackets().sendIComponentText(1156, 206, "View");
		player.getPackets().sendIComponentText(1156, 137, "Varrock Armour (3)");
		player.getPackets().sendIComponentText(1156, 138, "Varrock Achievement: <col=ff0000>Hard</col>");
		player.getPackets().sendIComponentText(1156, 254, "View");
		player.getPackets().sendIComponentText(1156, 110, "Varrock Armour (4)");
		player.getPackets().sendIComponentText(1156, 111, "Varrock Achievement: <col=8D1AC7>Elite</col>");
		player.getPackets().sendIComponentText(1156, 200, "View");
		player.getPackets().sendIComponentText(1156, 116, "Dwarven Army Axe");
		player.getPackets().sendIComponentText(1156, 117, "Varrock Achievement: <col=ff0000>Hard</col>");
		player.getPackets().sendIComponentText(1156, 212, "View");
		player.getPackets().sendIComponentText(1156, 134, "About Varrock Achievements");
		player.getPackets().sendIComponentText(1156, 135, "Learn about the varrock Achievement System");
		player.getPackets().sendIComponentText(1156, 248, "View");
		player.getPackets().sendIComponentText(1156, 119, "Falador shield (1)");
		player.getPackets().sendIComponentText(1156, 120, "Falador Achievement: <col=1E0AD1>Beginner</col>");
		player.getPackets().sendIComponentText(1156, 218, "View");
		player.getPackets().sendIComponentText(1156, 131, "Falador shield (2)");
		player.getPackets().sendIComponentText(1156, 132, "Falador Achievement: <col=1ED10A>Easy</col>");
		player.getPackets().sendIComponentText(1156, 242, "View");
		player.getPackets().sendIComponentText(1156, 140, "Falador shield (3)");
		player.getPackets().sendIComponentText(1156, 141, "Falador Achievement: <col=ff0000>Hard</col>");
		player.getPackets().sendIComponentText(1156, 260, "View");
		player.getPackets().sendIComponentText(1156, 149, "Falador shield (4)");
		player.getPackets().sendIComponentText(1156, 150, "Falador Achievement: <col=8D1AC7>Elite</col>");
		player.getPackets().sendIComponentText(1156, 278, "View");
		player.getPackets().sendIComponentText(1156, 152, "About Falador Achievements");
		player.getPackets().sendIComponentText(1156, 153, "Learn about Falador's Achievements");
		player.getPackets().sendIComponentText(1156, 284, "View");
		player.getPackets().sendIComponentText(1156, 122, "Explorer's Ring (1)");
		player.getPackets().sendIComponentText(1156, 123, "Lumbridge and Draynor Achievement: <col=1E0AD1>Beginner</col>");
		player.getPackets().sendIComponentText(1156, 230, "View");
		player.getPackets().sendIComponentText(1156, 128, "Explorer's Ring (2)");
		player.getPackets().sendIComponentText(1156, 129, "Lumbridge and Draynor Achievement: <col=1ED10A>Easy</col>");
		player.getPackets().sendIComponentText(1156, 236, "View");
		player.getPackets().sendIComponentText(1156, 125, "Explorer's Ring (3)");
		player.getPackets().sendIComponentText(1156, 126, "Lumbridge and Draynor Achievement: <col=ff0000>Hard</col>");
		player.getPackets().sendIComponentText(1156, 224, "View");
		player.getPackets().sendIComponentText(1156, 143, "Explorer's Ring (4)");
		player.getPackets().sendIComponentText(1156, 144, "Lumbridge and Draynor Achievement: <col=8D1AC7>Elite</col>");
		player.getPackets().sendIComponentText(1156, 266, "View");
		player.getPackets().sendIComponentText(1156, 146, "About Lumbridge and Draynor Achievements");
		player.getPackets().sendIComponentText(1156, 147, "Learn about the achievements of Lumbridge and Draynor");
		player.getPackets().sendIComponentText(1156, 272, "View");
		player.getPackets().sendIComponentText(1156, 167, "Ardougne Cloak (1)");
		player.getPackets().sendIComponentText(1156, 168, "Ardougne Achievement: <col=1E0AD1>Beginner</col>");
		player.getPackets().sendIComponentText(1156, 308, "View");
		player.getPackets().sendIComponentText(1156, 155, "Ardougne Cloak (2)");
		player.getPackets().sendIComponentText(1156, 157, "Ardougne Achievement: <col=1ED10A>Easy</col>");
		player.getPackets().sendHideIComponent(1156, 156, true);//hide
		player.getPackets().sendIComponentText(1156, 290, "View");
		player.getPackets().sendIComponentText(1156, 159, "Ardougne Cloak (3)");
		player.getPackets().sendHideIComponent(1156, 160, true);//hide
		player.getPackets().sendIComponentText(1156, 161, "Ardougne Achievement: <col=ff0000>Hard</col>");
		player.getPackets().sendIComponentText(1156, 296, "View");
		player.getPackets().sendIComponentText(1156, 163, "Ardougne Cloak (4)");
		player.getPackets().sendHideIComponent(1156, 164, true);//hide
		player.getPackets().sendIComponentText(1156, 165, "Ardougne Achievement: <col=8D1AC7>Elite</col>");
		player.getPackets().sendIComponentText(1156, 302, "View");
		player.getPackets().sendIComponentText(1156, 170, "Seer's Headband (1-4)");
		player.getPackets().sendIComponentText(1156, 171, "Ardougne and Seer's Village Achievement: <col=8D1AC7>Elite</col>");
		player.getPackets().sendIComponentText(1156, 314, "View");
		player.getPackets().sendIComponentText(1156, 318, "About Ardougne Achievements");
		player.getPackets().sendIComponentText(1156, 319, "Learn about Ardougne Achievements");
		player.getPackets().sendIComponentText(1156, 326, "View");


	}

	public void sendCompCape() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Completionist Requirements");
		player.getPackets().sendIComponentText(275, 10, "");
		if (player.isCompletedFightCaves()) {
			player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I must have completed the Fight Caves minigame. (1/1)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 11, "<col=330099>I must have completed the Fight Caves minigame. (0/1)</col>");
		}
		if (player.isCompletedFightKiln()) {
			player.getPackets().sendIComponentText(275, 12, "<str><col=330099>I must have completed the Fight Kiln minigame. (1/1)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 12, "<col=330099>I must have completed the Fight Kiln minigame. (0/1)</col>");
		}
		if (player.getSkills().getTotalLevel() >= 2496) {
			player.getPackets().sendIComponentText(275, 13, "<str><col=330099>I must have the highest possible level in every skill. ("+player.getSkills().getTotalLevel()+"/2496)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 13, "<col=330099>I must have the highest possible level in every skill. ("+player.getSkills().getTotalLevel()+"/2496)</col>");
		}
		player.getPackets().sendIComponentText(275, 14, "");
		if (player.sinkholes >= 5) {
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>I must have raided Sink Holes when they appeared. ("+player.sinkholes+"/5)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 15, "<col=330099>I must have raided Sink Holes when they appeared. ("+player.sinkholes+"/5)</col>");
		}
		if (player.totalTreeDamage >= 1000) {
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>I must have dealt lots of damage towards the Evil Tree. ("+player.totalTreeDamage+"/1000)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 16, "<col=330099>I must have dealt lots of damage towards the Evil Tree. ("+player.totalTreeDamage+"/1000)</col>");
		}
		if (player.barrowsLoot >= 55) {
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>I must have looted the chest in Barrows. ("+player.barrowsLoot+"/55)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I must have looted the chest in Barrows. ("+player.barrowsLoot+"/55)</col>");
		}
		if (player.domCount >= 150) {
			player.getPackets().sendIComponentText(275, 18, "<str><col=330099>I must have killed any of the bosses in the Dominion Tower. ("+player.domCount+"/150)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 18, "<col=330099>I must have killed any of the bosses in the Dominion Tower. ("+player.domCount+"/150)</col>");
		}
		if (player.rfd5) {
			player.getPackets().sendIComponentText(275, 19, "<str><col=330099>I must have defeated the final boss of Recipe for Disaster. (1/1)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 19, "<col=330099>I must have defeated the final boss of Recipe for Disaster. (0/1)</col>");
		}
		player.getPackets().sendIComponentText(275, 20, "");
		if (player.implingCount >= 120) {
			player.getPackets().sendIComponentText(275, 21, "<str><col=330099>I must have looted any type of impling. ("+player.implingCount+"/120)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 21, "<col=330099>I must have looted any type of impling. ("+player.implingCount+"/120)</col>");
		}
		if (player.advancedagilitylaps >= 100) {
			player.getPackets().sendIComponentText(275, 22, "<str><col=330099>I must have completed laps around the Advanced Barb Course. ("+player.advancedagilitylaps+"/100)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 22, "<col=330099>I must have completed laps around the Advanced Barb Course. ("+player.advancedagilitylaps+"/100)</col>");
		}
		if (player.heroSteals >= 150) {
			player.getPackets().sendIComponentText(275, 23, "<str><col=330099>I must have stolen from a Hero in Ardougne. ("+player.heroSteals+"/150)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 23, "<col=330099>I must have stolen from a Hero in Ardougne. ("+player.heroSteals+"/150)</col>");
		}
		if (player.cutDiamonds >= 500) {
			player.getPackets().sendIComponentText(275, 24, "<str><col=330099>I must have cut a large amount of Diamonds. ("+player.cutDiamonds+"/500)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 24, "<col=330099>I must have cut a large amount of Diamonds. ("+player.cutDiamonds+"/500)</col>");
		}
		if (player.runiteOre >= 50) {
			player.getPackets().sendIComponentText(275, 25, "<str><col=330099>I must have mined a large amount of Runite Ore. ("+player.runiteOre+"/50)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 25, "<col=330099>I must have mined a large amount of Runite Ore. ("+player.runiteOre+"/50)</col>");
		}
		if (player.cookedFish >= 500) {
			player.getPackets().sendIComponentText(275, 26, "<str><col=330099>I must have successfully cooked any type of fish. ("+player.cookedFish+"/500)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 26, "<col=330099>I must have successfully cooked any type of fish. ("+player.cookedFish+"/500)</col>");
		}
		if (player.burntLogs >= 150) {
			player.getPackets().sendIComponentText(275, 27, "<str><col=330099>I must have burnt a large amount of logs. ("+player.burntLogs+"/150)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 27, "<col=330099>I must have burnt a large amount of logs. ("+player.burntLogs+"/150)</col>");
		}
		if (player.choppedIvy >= 150) {
			player.getPackets().sendIComponentText(275, 28, "<str><col=330099>I must have chopped walls of Choking Ivy. ("+player.choppedIvy+"/150)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 28, "<col=330099>I must have chopped walls of Choking Ivy. ("+player.choppedIvy+"/150)</col>");
		}
		if (player.infusedPouches >= 300) {
			player.getPackets().sendIComponentText(275, 29, "<str><col=330099>I must have infused and created Summoning Pouches. ("+player.infusedPouches+"/300)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 29, "<col=330099>I must have infused and created Summoning Pouches. ("+player.infusedPouches+"/300)</col>");
		}
		if (player.crystalChest >= 20) {
			player.getPackets().sendIComponentText(275, 30, "<str><col=330099>I must have looted the Crystal Chest. ("+player.crystalChest+"/20)</col></str>");
		} else {
			player.getPackets().sendIComponentText(275, 30, "<col=330099>I must have looted the Crystal Chest. ("+player.crystalChest+"/20)</col>");
		}
		for (int i = 31; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

	public void HandleNewPlayer(Player p) {
		int interFace = 1214;


	}

	public void sendErnestChicken() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Ernest the Chicken");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to the</col> <col=660000>Veronica</col>");
		player.getPackets().sendIComponentText(275, 12, "<col=330099>outside of the </col><col=660000>Draynor Manor.</col>");
		player.getPackets().sendIComponentText(275, 13, "");
		if (player.EC == 1) {
			player.getPackets().sendIComponentText(275, 14, "<col=330099>Veronica has told me her fiance has been lost</col>");
			player.getPackets().sendIComponentText(275, 15, "<col=330099>in the manor. I should expore the manor.</col>");
		} else if (player.EC == 2) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>Veronica has told me her fiance has been lost</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>in the manor. I should expore the manor.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<col=330099>Professor Oddenstein told me about Ernest </col>");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>The gremlins stole his equipment and I</col>");
			player.getPackets().sendIComponentText(275, 18, "<col=330099>must retrieve the following:</col>");
			player.getPackets().sendIComponentText(275, 19, "<col=330099>1x Pressure Gauge</col>");
			player.getPackets().sendIComponentText(275, 20, "<col=330099>1x Rubber Tube</col>");
			player.getPackets().sendIComponentText(275, 21, "<col=330099>1x Oil Can</col>");
			player.getPackets().sendIComponentText(275, 22, "");
			player.getPackets().sendIComponentText(275, 23, "<col=330099>He also told me that the items will be scattered in the manor.</col>");
		} else if (player.EC == 3) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>Veronica has told me her fiance has been lost</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>in the manor. I should expore the manor.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>Professor Oddenstein told me about Ernest </col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>The gremlins stole his equipment and I</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<str><col=330099>must retrieve the following:</col></str>");
			player.getPackets().sendIComponentText(275, 19, "<str><col=330099>1x Pressure Gauge</col></str>");
			player.getPackets().sendIComponentText(275, 20, "<str><col=330099>1x Rubber Tube</col></str>");
			player.getPackets().sendIComponentText(275, 21, "<str><col=330099>1x Oil Can</col></str>");
			player.getPackets().sendIComponentText(275, 22, "");
			player.getPackets().sendIComponentText(275, 23, "<str><col=330099>He also told me that the items will be scattered in the manor.</col></str>");
			player.getPackets().sendIComponentText(275, 24, "");
			player.getPackets().sendIComponentText(275, 25, "QUEST COMPLETE!");
		}
		for (int i = 26; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

	public void sendSkype() {
		player.getInterfaceManager().sendInterface(72);
		player.getInventory().refresh();
	}

	public void sendDragonSlayer() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Dragon Slayer");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to the</col> <col=660000>Guild Master</col>");
		player.getPackets().sendIComponentText(275, 12, "<col=330099>in the </col><col=660000>Champion's Guild.</col>");
		player.getPackets().sendIComponentText(275, 13, "");
		if (player.DS == 1) {
			player.getPackets().sendIComponentText(275, 14, "<col=330099>I was told I should speak to</col> <col=660000>Oziach</col>");
			player.getPackets().sendIComponentText(275, 15, "<col=330099>located in the north-west of Edgeville.</col>");
		} else if (player.DS == 2) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I was told I should speak to</col> <col=660000>Oziach</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>located in the north-west of Edgeville.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<col=330099>I should speak with the <col=660000>Guildmaster</col><col=330099> again and find out about </col>");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>how I can kill this dragon.</col>");
		} else if (player.DS == 3 || player.DS == 4) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I was told I should speak to</col> <col=660000>Oziach</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>located in the north-west of Edgeville.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>I should speak with the <col=660000>Guildmaster</col><col=330099> again and find out about </col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>how I can kill this dragon.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<col=330099>I must find the pieces of the map, once I place them together I should </col>");
			player.getPackets().sendIComponentText(275, 19, "<col=330099>find and purchase a ship. Lastly I need to find a captain that will </col>");
			player.getPackets().sendIComponentText(275, 20, "<col=330099>sail me to the island of <col=660000>Crandor</col><col=330099>.</col>");
		} else if (player.DS == 5 || player.DS == 6) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I was told I should speak to</col> <col=660000>Oziach</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>located in the north-west of Edgeville.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>I should speak with the <col=660000>Guildmaster</col><col=330099> again and find out about </col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>how I can kill this dragon.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<str><col=330099>I must find the pieces of the map, once I place them together I should </col></str>");
			player.getPackets().sendIComponentText(275, 19, "<str><col=330099>find and purchase a ship. Lastly I need to find a captain that will </col></str>");
			player.getPackets().sendIComponentText(275, 20, "<str><col=330099>sail me to the island of <col=660000>Crandor</col><col=330099>.</col></str>");
			player.getPackets().sendIComponentText(275, 21, "<col=330099>I should meet with <col=660000>Ned</col> <col=330099>on the ship.</col>");
			player.getPackets().sendIComponentText(275, 22, "<col=330099>I should find and slay Elvarg then bring his head to <col=660000>Oziach</col><col=330099>.<col=330099>.</col>");
		} else if (player.DS == 7) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I was told I should speak to</col> <col=660000>Oziach</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>located in the north-west of Edgeville.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>I should speak with the <col=660000>Guildmaster</col><col=330099> again and find out about </col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>how I can kill this dragon.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<str><col=330099>I find the pieces of the map, once I place them together I should </col></str>");
			player.getPackets().sendIComponentText(275, 19, "<str><col=330099>find and purchase a ship. Lastly I need to find a captain that will </col></str>");
			player.getPackets().sendIComponentText(275, 20, "<str><col=330099>sail me to the island of <col=660000>Crandor</col><col=330099>.</col></str>");
			player.getPackets().sendIComponentText(275, 21, "<str><col=330099>I should meet with <col=660000>Ned</col> <col=330099>on the ship.</col></str>");
			player.getPackets().sendIComponentText(275, 22, "<str><col=330099>I should find and slay Elvarg then bring his head to <col=660000>Oziach</col><col=330099>.<col=330099>.</col></str>");
			player.getPackets().sendIComponentText(275, 23, "");
			player.getPackets().sendIComponentText(275, 24, "QUEST COMPLETE!");
		}
		for (int i = 25; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

	public void sendVampyreSlayer() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Vampyre Slayer");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to </col> <col=660000>Morgan</col> <col=330099>in the </col><col=660000>Draynor Village.</col>");
		player.getPackets().sendIComponentText(275, 12, "");
		player.getPackets().sendIComponentText(275, 13, "");
		if (player.VS == 1) {
			player.getPackets().sendIComponentText(275, 14, "<col=330099>I should find</col> <col=660000>Dr. Harlow</col> <col=330099>who is located in the Blue Moon Inn.</col>");
			player.getPackets().sendIComponentText(275, 15, "<col=330099>He should have some information on getting rid of this vampyre.</col>");
		} else if (player.VS == 2) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Dr. Harlow</col> <col=330099>who is located in the Blue Moon Inn.</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this vampyre.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<col=330099>Dr. Harlow requested a beer, I should go bring one to him.</col>");
		} else if (player.VS == 3) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Dr. Harlow</col> <col=330099>who is located in the Blue Moon Inn.</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this vampyre.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>Dr. Harlow requested a beer, I should go bring one to him.</col></str>");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>He has given me a stake and a stake hammer, now I can slay this vampyre.</col>");
		} else if (player.VS == 4) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Dr. Harlow</col> <col=330099>who is located in the Blue Moon Inn.</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this vampyre.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>Dr. Harlow requested a beer, I should go bring one to him.</col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>He has given me a stake and a stake hammer, I can now find and slay this vampyre.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<col=330099>I should report back to Morgan and tell him the news.</col>");
		} else if (player.VS == 5) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Dr. Harlow</col> <col=330099>who is located in the Blue Moon Inn.</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this vampyre.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>Dr. Harlow requested a beer, I should go bring one to him.</col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>He has given me a stake and a stake hammer, I can now find and slay this vampyre.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<str><col=330099>I should report back to Morgan and tell him the news.</col></str>");
			player.getPackets().sendIComponentText(275, 19, "");
			player.getPackets().sendIComponentText(275, 20, "QUEST COMPLETE!");
		}

		for (int i = 21; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

	public void sendLostCity() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Lost-City Quest");
		player.getPackets().sendIComponentText(275, 10, "");
		if (player.spokeToWarrior == false) {
			player.getPackets().sendIComponentText(275, 11, "Speak to the Warrior East of Draynor");
			player.getPackets().sendIComponentText(275, 12, "<u>Requirements</u>");
			player.getPackets().sendIComponentText(275, 13, "<col=ffff00>31 Crafting, 36 Woodcutting</col>");
		} else if (player.spokeToWarrior == true && player.spokeToShamus == false) {
			player.getPackets().sendIComponentText(275, 11, "Shamus appears to be in one of the trees around this location.");
			player.getPackets().sendIComponentText(275, 12, "The Warrior told me the tree displays 'Chop Tree'");
			player.getPackets().sendIComponentText(275, 13, "");
		} else if (player.spokeToWarrior == true && player.spokeToShamus == true) {
			player.getPackets().sendIComponentText(275, 11, "<str>Shamus appears to be in one of the trees around this location.</str>");
			player.getPackets().sendIComponentText(275, 12, "<str>The Warrior told me the tree displays 'Chop Tree'</str>");
			player.getPackets().sendIComponentText(275, 13, "");
		}
		if (player.spokeToWarrior == true && player.spokeToShamus == true && player.spokeToMonk == false) {
			player.getPackets().sendIComponentText(275, 14, "I should go find the Monk of Entrana, Who is located at Port Sarim.");
		} else if (player.spokeToWarrior == true && player.spokeToShamus == true && player.spokeToMonk == true) {
			player.getPackets().sendIComponentText(275, 14, "<str>I should go find the Monk of Entrana, Who is located at Port Sarim.</str>");
		} else if (player.spokeToWarrior == false || player.spokeToShamus == false){
			player.getPackets().sendIComponentText(275, 14, "");
		}
		player.getPackets().sendIComponentText(275, 15, "");
		if (player.spokeToWarrior == true && player.spokeToShamus == true && player.spokeToMonk == true && player.lostCity == 0) {
			player.getPackets().sendIComponentText(275, 16, "The other side of Entrana is a ladder which leads to a cave");
			player.getPackets().sendIComponentText(275, 17, "I should go down the ladder and search for the dramen tree.");
			player.getPackets().sendIComponentText(275, 18, "In order to chop the dramen tree I must have a axe.");
			player.getPackets().sendIComponentText(275, 19, "The zombies must drop a axe of some sort.");
			player.getPackets().sendIComponentText(275, 20, "");
			player.getPackets().sendIComponentText(275, 21, "");
		} else if (player.lostCity == 1) {
			player.getPackets().sendIComponentText(275, 16, "<str>The other side of Entrana is a ladder which leads to a cave</str>");
			player.getPackets().sendIComponentText(275, 17, "<str>I should go down the ladder and search for the dramen tree.</str>");
			player.getPackets().sendIComponentText(275, 18, "<str>In order to chop the dramen tree I must have a axe.</str>");
			player.getPackets().sendIComponentText(275, 19, "<str>The zombies must drop a axe of some sort.</str>");
			player.getPackets().sendIComponentText(275, 20, "");
			player.getPackets().sendIComponentText(275, 21, "Congratulations Quest Complete!");
		} else {
			player.getPackets().sendIComponentText(275, 16, "");
			player.getPackets().sendIComponentText(275, 17, "");
			player.getPackets().sendIComponentText(275, 18, "");
			player.getPackets().sendIComponentText(275, 19, "");
			player.getPackets().sendIComponentText(275, 20, "");
			player.getPackets().sendIComponentText(275, 21, "");
			for (int i = 22; i < 300; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		}
	}

	public static void sendLostCityComplete(final Player player) {
		if (player.lostCity == 0) {
			player.questPoints += 3;
			player.getInterfaceManager().sendInterface(275);
			player.getPackets().sendIComponentText(275, 1, "Quest Complete!");
			player.getPackets().sendIComponentText(275, 10, "");
			player.getPackets().sendIComponentText(275, 11, "Congratulations you have completed the quest: Lost City");
			player.getPackets().sendIComponentText(275, 12, "You may now purchase the dragon longsword,");
			player.getPackets().sendIComponentText(275, 13, "dragon dagger, and many other items.");
			player.getPackets().sendIComponentText(275, 14, "You may now access the lost city of Zanaris.");
			player.getPackets().sendIComponentText(275, 15, "You may now use the slayer master Chaeldar.");
			player.getPackets().sendIComponentText(275, 16, "You are awarded 3 Quest Points.");
			player.getPackets().sendIComponentText(275, 17, "Well Done!");
			player.getPackets().sendIComponentText(275, 18, "");
			player.getPackets().sendIComponentText(275, 19, "");
			player.getPackets().sendIComponentText(275, 20, "");
			player.lostCity = 1;
			player.getPackets().sendGameMessage(
					"<col=ff0000>You have completed quest: Lost City.");
		}
	}

	public static void sendRestlessStart(final Player player) {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendRunScript(1207, 3);
		player.getPackets().sendIComponentText(275, 1, "Restless Ghost");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to </col> <col=660000>Father Aereck</col> <col=330099>in the</col>");
		player.getPackets().sendIComponentText(275, 12, "<col=660000>church</col> <col=330099>outside of the</col> <col=660000>Lumbridge Castle.</col>");
		for (int i = 13; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

	public static void handleRestlessCompletedInterface(final Player player) {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendRunScript(1207, 10);
		player.getPackets().sendIComponentText(275, 1, "Restless Ghost");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to </col> <col=660000>Father Aereck</col> <col=330099>in the</col>");
		player.getPackets().sendIComponentText(275, 12, "<col=660000>church</col> <col=330099>outside of the</col> <col=660000>Lumbridge Castle.</col>");
		player.getPackets().sendIComponentText(275, 13, "");
		player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Father Urhney</col> <col=330099>who is located in Lumbridge Swamp.</col></str>");
		player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this ghost.</col></str>");
		player.getPackets().sendIComponentText(275, 16, "<str><col=330099>Now that I have this ghost amulet, I should check back in with </col> <col=660000>Father Aereck</col><col=330099>.</col></str>");
		player.getPackets().sendIComponentText(275, 17, "<str><col=330099>I should try checking out this ghost Father Aereck is talking about.</col></str>");
		player.getPackets().sendIComponentText(275, 18, "<str><col=330099>I now need to find this so called skull. I should look around the Lumbridge Swamp.</col></str>");
		player.getPackets().sendIComponentText(275, 19, "<str><col=330099>When I find this I should place the skull on the coffin.</col></str>");
		player.getPackets().sendIComponentText(275, 20, "");
		player.getPackets().sendIComponentText(275, 21, "<col=660000>QUEST COMPLETE</col>");
	}

	public void handleVampyreQuestInterface() {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 2;
		player.getInventory().addItem(24155, 1);
		player.getInventory().addItemMoneyPouch(new Item(995, 15000));
		player.getSkills().addXp(Skills.SLAYER, 250);
		player.getSkills().addXp(Skills.STRENGTH, 450);
		player.getPackets().sendIComponentText(277, 4, "You have completed Vampyre Slayer Quest.");
		player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
		player.getPackets().sendIComponentText(277, 9, "You are awarded:");
		player.getPackets().sendIComponentText(277, 10, "2 Quest Points");
		player.getPackets().sendIComponentText(277, 11, "Slayer XP");
		player.getPackets().sendIComponentText(277, 12, "Strength XP");
		player.getPackets().sendIComponentText(277, 13, "15,000 Coins");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "Two spins on the Squeal of Fortune");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, 1549, 1);
		player.getPackets().sendGameMessage("Congratulations! You have completed the Vampyre Slayer quest!");
	}

	public void sendRuneComplete() {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 2;
		player.getInventory().addItem(24155, 1);
		player.getInventory().addItemMoneyPouch(new Item(995, 20000));
		player.getSkills().addXp(Skills.RUNECRAFTING, 300);
		player.getPackets().sendIComponentText(277, 4, "You have completed Rune Mysteries Quest.");
		player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
		player.getPackets().sendIComponentText(277, 9, "You are awarded:");
		player.getPackets().sendIComponentText(277, 10, "2 Quest Points");
		player.getPackets().sendIComponentText(277, 11, "20,000 Coins");
		player.getPackets().sendIComponentText(277, 12, "Runecrafting XP");
		player.getPackets().sendIComponentText(277, 13, "Ability to mine Rune and Pure Essence");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "Two spins on the Squeal of Fortune");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, 1436, 1);
		player.getPackets().sendGameMessage("Congratulations! You have completed the Rune Mysteries quest!");
	}

	public void sendErnestComplete() {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 1;
		player.getInventory().addItem(24155, 1);
		player.getInventory().addItemMoneyPouch(new Item(995, 10000));
		player.getPackets().sendIComponentText(277, 4, "You have completed Ernest the Chicken Quest.");
		player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
		player.getPackets().sendIComponentText(277, 9, "You are awarded:");
		player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
		player.getPackets().sendIComponentText(277, 11, "10,000 Coins");
		player.getPackets().sendIComponentText(277, 12, "");
		player.getPackets().sendIComponentText(277, 13, "");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "Two spins on the Squeal of Fortune");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, 314, 1);
		player.getPackets().sendGameMessage("Congratulations! You have completed the Ernest the Chicken quest!");
	}

	public void handleDragonQuestInterface() {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 4;
		player.getInventory().addItem(24155, 1);
		player.getInventory().addItemMoneyPouch(new Item(995, 125000));
		player.getSkills().addXp(Skills.STRENGTH, 750);
		player.getSkills().addXp(Skills.DEFENCE, 600);
		player.getPackets().sendIComponentText(277, 4, "You have completed Dragon Slayer Quest.");
		player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
		player.getPackets().sendIComponentText(277, 9, "You are awarded:");
		player.getPackets().sendIComponentText(277, 10, "4 Quest Points");
		player.getPackets().sendIComponentText(277, 11, "Strength XP");
		player.getPackets().sendIComponentText(277, 12, "Defence XP");
		player.getPackets().sendIComponentText(277, 13, "125,000 Coins");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "Two spins on the Squeal of Fortune");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, 11279, 1);
		player.getPackets().sendGameMessage("Congratulations! You have completed the Dragon Slayer quest!");
	}

	public static void handleRestlessProgress(final Player player) {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendRunScript(1207, 10);
		player.getPackets().sendIComponentText(275, 1, "Restless Ghost");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to </col> <col=660000>Father Aereck</col> <col=330099>in the</col>");
		player.getPackets().sendIComponentText(275, 12, "<col=660000>church</col> <col=330099>outside of the</col> <col=660000>Lumbridge Castle.</col>");
		player.getPackets().sendIComponentText(275, 13, "");
		if (player.RG <= 2) {
			player.getPackets().sendIComponentText(275, 14, "<col=330099>I should find</col> <col=660000>Father Urhney</col> <col=330099>who is located in Lumbridge Swamp.</col>");
			player.getPackets().sendIComponentText(275, 15, "<col=330099>He should have some information on getting rid of this ghost.</col>");
		} else if (player.RG == 3) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Father Urhney</col> <col=330099>who is located in Lumbridge Swamp.</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this ghost.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<col=330099>Now that I have this ghost amulet, I should check back in with </col> <col=660000>Father Aereck</col><col=330099>.</col>");
			player.getPackets().sendIComponentText(275, 17, "<col=330099>I should try checking out this ghost Father Aereck is talking about.</col>");
		} else if (player.RG == 4) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Father Urhney</col> <col=330099>who is located in Lumbridge Swamp.</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this ghost.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>Now that I have this ghost amulet, I should check back in with </col> <col=660000>Father Aereck</col><col=330099>.</col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>I should try checking out this ghost Father Aereck is talking about.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<col=330099>I now need to find this so called skull. I should look around the Lumbridge Swamp.</col>");
			player.getPackets().sendIComponentText(275, 19, "<col=330099>When I find this I should talk to the ghost again.</col>");
		} else if (player.RG == 5) {
			player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I should find</col> <col=660000>Father Urhney</col> <col=330099>who is located in Lumbridge Swamp.</col></str>");
			player.getPackets().sendIComponentText(275, 15, "<str><col=330099>He should have some information on getting rid of this ghost.</col></str>");
			player.getPackets().sendIComponentText(275, 16, "<str><col=330099>Now that I have this ghost amulet, I should check back in with </col> <col=660000>Father Aereck</col><col=330099>.</col></str>");
			player.getPackets().sendIComponentText(275, 17, "<str><col=330099>I should try checking out this ghost Father Aereck is talking about.</col></str>");
			player.getPackets().sendIComponentText(275, 18, "<str><col=330099>I now need to find this so called skull. I should look around the Lumbridge Swamp.</col></str>");
			player.getPackets().sendIComponentText(275, 19, "<col=330099>When I find this I should place the skull on the coffin.</col>");
		}

		for (int i = 20; i < 300; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}

	public static void handleRestlessCompleted(final Player player) {
		player.questPoints += 2;
		player.getSkills().addXp(Skills.PRAYER, 1500);
		player.getInventory().addItemMoneyPouch(new Item(995, 15000));
		player.getPackets().sendConfig(29, 2);
		player.getPackets().sendConfig(101, player.questPoints); // Quest Points
		player.getInterfaceManager().sendInterfaces();
		player.getPackets().sendUnlockIComponentOptionSlots(190, 15, 0, 201, 0, 1, 2, 3);
	}

	public static void handleRestlessCompleteInterface(final Player player) {
		player.getInterfaceManager().sendInterface(277);
		player.getPackets().sendIComponentText(277, 4, "You have completed Restless Ghost.");
		player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
		player.getPackets().sendIComponentText(277, 9, "You are awarded:");
		player.getPackets().sendIComponentText(277, 10, "2 Quest Points");
		player.getPackets().sendIComponentText(277, 11, "1500 Cooking XP");
		player.getPackets().sendIComponentText(277, 12, "15000 Coins");
		player.getPackets().sendIComponentText(277, 13, "A Ghost Speak Amulet");
		player.getPackets().sendIComponentText(277, 14, "Two spins on the Squeal of Fortune");
		player.getPackets().sendIComponentText(277, 15, "");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, 552, 1);
		player.getPackets().sendGameMessage("Congratulations! You have completed the Restless Ghost quest!");
	}

	public void sendItem(String name, String guide, int id, String street, String uses) {
		player.getInterfaceManager().sendInterface(277);
		player.getPackets().sendIComponentText(277, 3, "Item: "+name+"");
		player.getPackets().sendIComponentText(277, 4, "Item Information");
		player.getPackets().sendIComponentText(277, 6, "Item ID:");
		player.getPackets().sendIComponentText(277, 7, ""+id+"");
		player.getPackets().sendIComponentText(277, 9, "How to Obtain:");
		player.getPackets().sendIComponentText(277, 10, guide);
		player.getPackets().sendIComponentText(277, 11, "");
		player.getPackets().sendIComponentText(277, 12, "Uses:");
		player.getPackets().sendIComponentText(277, 13, uses);
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "Street Price (average):");
		player.getPackets().sendIComponentText(277, 16, ""+street+"gp");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, id, 1);
	}

	/*public void sendSlayerShop() {
		player.getInterfaceManager().sendInterface(164);
			player.getPackets().sendItemOnIComponent(164, 27, 22530, 1); //Full slayer helm
			player.getPackets().sendItemOnIComponent(164, 30, 15220, 1); //bers ring
			player.getPackets().sendItemOnIComponent(164, 38, 10551, 1); //Fighter torso
			player.getPackets().sendItemOnIComponent(164, 40, 12862, 1); //Drag Slayer Gloves
			player.getPackets().sendItemOnIComponent(164, 29, 15520, 1);
		        player.getPackets().sendIComponentText(164, 20,  " "+ player.slayerPoints +" ");
		        player.getPackets().sendIComponentText(164, 26,  "Full Slayer Helm (e)");
		        player.getPackets().sendIComponentText(164, 28,  "Berserker Ring (i)");
		        player.getPackets().sendIComponentText(164, 37,  "Fighter Torso");
		        player.getPackets().sendIComponentText(164, 39,  "Dragon Slayer Gloves");
		        player.getPackets().sendIComponentText(164, 31,  "Slayer Points:");
		        player.getPackets().sendIComponentText(164, 32,  "50 Points");
		        player.getPackets().sendIComponentText(164, 33,  "4000 Points");
		        player.getPackets().sendIComponentText(164, 36,  "3500 Points");
		        player.getPackets().sendIComponentText(164, 34,  "3200 Points");
		        player.getPackets().sendIComponentText(164, 35,  "3000 Points");
		        player.getPackets().sendIComponentText(164, 22,  "More");
		        player.getPackets().sendIComponentText(164, 23,  "Coming Soon");
		    }

		public void sendSlayerShop2() {
		player.getInterfaceManager().sendInterface(378);
			player.getPackets().sendItemOnIComponent(378, 92, 22374, 1); //gorilla
			player.getPackets().sendItemOnIComponent(378, 93, 995, 1);
			player.getPackets().sendItemOnIComponent(378, 94, 995, 1);
			player.getPackets().sendItemOnIComponent(378, 95, 995, 1);
			player.getPackets().sendItemOnIComponent(378, 96, 995, 1);
			player.getPackets().sendItemOnIComponent(378, 102, 24317, 1); //Monkey Cape
			player.getPackets().sendItemOnIComponent(378, 103, 18337, 1); //bonecrusher
			player.getPackets().sendItemOnIComponent(378, 104, 18337, 1);
			player.getPackets().sendItemOnIComponent(378, 105, 19675, 1); //herbicide
			player.getPackets().sendItemOnIComponent(378, 101, 4212, 1); // crystal bow
		        player.getPackets().sendIComponentText(378, 79,  " "+ player.slayerPoints +" ");
		        player.getPackets().sendIComponentText(378, 82,  "Coming Soon.");
		        player.getPackets().sendIComponentText(378, 81,  "More");
		        player.getPackets().sendIComponentText(378, 83,  "BoneCrusher.");
		        player.getPackets().sendIComponentText(378, 90,  "4500 Points.");
		        player.getPackets().sendIComponentText(378, 87,  "Herbicide.");
		        player.getPackets().sendIComponentText(378, 99,  "4500 Points.");
		        player.getPackets().sendIComponentText(378, 88,  "Crystal Bow.");
		        player.getPackets().sendIComponentText(378, 100,  "3700 Points.");
		        player.getPackets().sendIComponentText(378, 84,  "Gorilla Mask.");
		        player.getPackets().sendIComponentText(378, 91,  "2500 Points.");
		        player.getPackets().sendIComponentText(378, 85,  "1mil Coins.");
		        player.getPackets().sendIComponentText(378, 97,  "150 Points.");
		        player.getPackets().sendIComponentText(378, 86,  "Monkey Cape.");
		        player.getPackets().sendIComponentText(378, 98,  "5000 Points.");
		    }*/

	public void sendQuestTab() {
		sendTab(resizableScreen ? 114 : 190, 190);
		player.getPackets().sendIComponentText(190, 1, "Free Quests");
		player.getPackets().sendIComponentText(190, 2, "Quest 1");
	}

	public void sendDungShop() {
		player.getInterfaceManager().sendInterface(825);
		player.getPackets().sendIComponentText(825, 95,  "Chaotic rapier");
		player.getPackets().sendIComponentText(825, 96,  "150K");
		player.getPackets().sendIComponentText(825, 99,  "Chaotic longsword");
		player.getPackets().sendIComponentText(825, 100,  "150K");
		player.getPackets().sendIComponentText(825, 103,  "Chaotic maul");
		player.getPackets().sendIComponentText(825, 104,  "150K");
		player.getPackets().sendIComponentText(825, 107,  "Chaotic staff");
		player.getPackets().sendIComponentText(825, 108,  "150K");
		player.getPackets().sendIComponentText(825, 111,  "Chaotic crossbow");
		player.getPackets().sendIComponentText(825, 112,  "150K");
		player.getPackets().sendIComponentText(825, 115,  "Chaotic & Eagle-eye shield");
		player.getPackets().sendIComponentText(825, 116,  "150k");
		player.getPackets().sendIComponentText(825, 119,  "Arcane stream necklace");
		player.getPackets().sendIComponentText(825, 120,  "50K");
		player.getPackets().sendIComponentText(825, 123,  "Ring of vigour");
		player.getPackets().sendIComponentText(825, 124,  "50K");
		player.getPackets().sendIComponentText(825, 127,  "Dung. EXP");
		player.getPackets().sendIComponentText(825, 128,  "25K");
		player.getPackets().sendIComponentText(825, 131,  "Bone Crusher");
		player.getPackets().sendIComponentText(825, 132,  "50K");
		player.getPackets().sendIComponentText(825, 134,  "Chaotic Claws");
		player.getPackets().sendIComponentText(825, 135,  "100K");
		player.getPackets().sendIComponentText(825, 138,  "Herbicide");
		player.getPackets().sendIComponentText(825, 139,  "50K");
		player.getPackets().sendIComponentText(825, 54,  "Your Tokens:");
		player.getPackets().sendIComponentText(825, 55,  ""+player.getDungTokens()+"");
		player.getPackets().sendIComponentText(825, 66,  "");
		player.getPackets().sendIComponentText(825, 67,  "Welcome to the token store. Here, you can buy rewards with your dungeoneering tokens.");
	}

	public void sendInventoryInterface(int childId) {
		player.getPackets().sendInterface(false,
				resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID,
						resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID,
								childId);
	}

	public void sendInvasionShop() {
		player.getInterfaceManager().sendInterface(267);
		player.getPackets().sendIComponentText(267, 93, "Dark Invasion Rewards (Items will be banked)");
		player.getPackets().sendHideIComponent(267, 101, true);
		player.getPackets().sendIComponentText(267, 105, "You have: " + player.getPlayerData().getInvasionPoints() + " points.");
		player.getPackets().sendHideIComponent(267, 104, true);
		/*
	    OPTION NAMES
		 */
		player.getPackets().sendIComponentText(267, 10, "Attack Xp");
		player.getPackets().sendIComponentText(267, 11, "Strength Xp");
		player.getPackets().sendIComponentText(267, 12, "Defence Xp");
		player.getPackets().sendIComponentText(267, 13, "Range Xp");
		player.getPackets().sendIComponentText(267, 14, "Magic Xp");
		player.getPackets().sendIComponentText(267, 15, "Const. Xp");
		player.getPackets().sendIComponentText(267, 16, "Prayer Xp");
		player.getPackets().sendIComponentText(267, 32, "Herbs");
		player.getPackets().sendIComponentText(267, 47, "Ores");
		player.getPackets().sendIComponentText(267, 33, "Seeds");
		player.getPackets().sendIComponentText(267, 28, "Void Knight Mace");
		player.getPackets().sendIComponentText(267, 29, "Void Knight Top");
		player.getPackets().sendIComponentText(267, 30, "Void Knight Bottom");
		player.getPackets().sendIComponentText(267, 31, "Void Knight Gloves");
		player.getPackets().sendIComponentText(267, 63, "Void Mage Helm");
		player.getPackets().sendIComponentText(267, 64, "Void Ranger Helm");
		player.getPackets().sendIComponentText(267, 65, "Void Melee Helm");
		player.getPackets().sendIComponentText(267, 66, "Void Commendations");

		/*
            ATTACK XP
		 */
		player.getPackets().sendIComponentText(267, 34, "1X");
		player.getPackets().sendIComponentText(267, 49, "5X");
		player.getPackets().sendIComponentText(267, 56, "10X");
		/*
             STRENGTH XP
		 */
		player.getPackets().sendIComponentText(267, 35, "1X");
		player.getPackets().sendIComponentText(267, 50, "5X");
		player.getPackets().sendIComponentText(267, 57, "10X");
		/*
             DEFENCE XP
		 */
		player.getPackets().sendIComponentText(267, 36, "1X");
		player.getPackets().sendIComponentText(267, 51, "5X");
		player.getPackets().sendIComponentText(267, 58, "10X");

		/*
              RANGE XP
		 */
		player.getPackets().sendIComponentText(267, 37, "1X");
		player.getPackets().sendIComponentText(267, 52, "5X");
		player.getPackets().sendIComponentText(267, 59, "10X");

		/*
              MAGIC XP
		 */
		player.getPackets().sendIComponentText(267, 38, "1X");
		player.getPackets().sendIComponentText(267, 53, "5X");
		player.getPackets().sendIComponentText(267, 60, "10X");

		/*
            HITPOINTS XP
		 */
		player.getPackets().sendIComponentText(267, 39, "1X");
		player.getPackets().sendIComponentText(267, 54, "5X");
		player.getPackets().sendIComponentText(267, 61, "10X");
		/*
             PRAYER XP
		 */
		player.getPackets().sendIComponentText(267, 40, "1X");
		player.getPackets().sendIComponentText(267, 55, "5X");
		player.getPackets().sendIComponentText(267, 62, "10X");
		/*
	       HERB
		 */
		player.getPackets().sendIComponentText(267, 45, "Buy");
		/*
		ORE
		 */
		player.getPackets().sendIComponentText(267, 46, "Buy");
		/*
	       SEEDS
		 */
		player.getPackets().sendIComponentText(267, 48, "Buy");
		/*
	       VOID
		 */
		player.getPackets().sendIComponentText(267, 41, "Buy(500)");//Void Knight Mace
		player.getPackets().sendIComponentText(267, 42, "Buy(500)");//Void Knight Top
		player.getPackets().sendIComponentText(267, 43, "Buy(500)");//Void Knight Robe
		player.getPackets().sendIComponentText(267, 44, "Buy(500)");//Void Knight Gloves
		player.getPackets().sendIComponentText(267, 67, "Buy(500)");//Void Mage Helm
		player.getPackets().sendIComponentText(267, 68, "Buy(500)");//Void Range Helm
		player.getPackets().sendIComponentText(267, 69, "Buy(500)");//Void Melee Helm
		player.getPackets().sendIComponentText(267, 70, "Buy");//Void Commendations
		/*
	   CHARMS NAME
		 */
		player.getPackets().sendIComponentText(267, 71, "Not Available");
		player.getPackets().sendIComponentText(267, 72, "Not Available");
		player.getPackets().sendIComponentText(267, 73, "Not Available");
		player.getPackets().sendIComponentText(267, 74, "Not Available");
	}

	public final void sendInterfaces() {
		if (player.getDisplayMode() == 2 || player.getDisplayMode() == 3) {
			resizableScreen = true;
			sendFullScreenInterfaces();
		} else {
			resizableScreen = false;
			sendFixedInterfaces();
		}
		player.getSkills().sendInterfaces();
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getMusicsManager().unlockMusicPlayer();
		player.getEmotesManager().unlockEmotesBook();
		player.getInventory().unlockInventoryOptions();
		player.getPrayer().unlockPrayerBookButtons();
		ClansManager.unlockBanList(player);
		if (player.getFamiliar() != null && player.isRunning()) {
			player.getFamiliar().unlock();
		}
		player.getControlerManager().sendInterfaces();
	}

	public void replaceRealChatBoxInterface(int interfaceId) {
		player.getPackets().sendInterface(true, 752, 11, interfaceId);
	}

	public void closeReplacedRealChatBoxInterface() {
		player.getPackets().closeInterface(752, 11);
	}

	public void sendWindowPane() {
		player.getPackets().sendWindowsPane(resizableScreen ? 746 : 548, 0);
	}

	public void sendFullScreenInterfaces() {
		player.getPackets().sendWindowsPane(746, 0);
		sendTab(21, 752);
		sendTab(22, 751);
		sendTab(15, 745);
		sendTab(25, 754);
		sendTab(195, 748);
		sendTab(196, 749);
		sendTab(197, 750);
		sendTab(198, 747);
		sendTab(114, 930);
		player.getPackets().sendInterface(true, 752, 9, 137);
		sendCombatStyles();
		sendTaskTab();
		sendTaskSystem();
		sendSkills();
		//sendPlayerCustom();
		sendInventory();
		sendEquipment();
		sendPrayerBook();
		sendMagicBook();
		sendTab(120, 550); // friend list
		sendTab(121, 1109); // 551 ignore now friendchat
		sendTab(122, 1110); // 589 old clan chat now new clan chat
		sendSettings();
		sendEmotes();
		sendTab(125, 187); // music
		sendTab(126, 34); // notes
		sendTab(129, 182); // logout*/
		sendSquealOfFortuneTab();
	}

	public void sendFixedInterfaces() {
		player.getPackets().sendWindowsPane(548, 0);
		sendTab(161, 752);
		sendTab(37, 751);
		sendTab(23, 745);
		sendTab(25, 754);
		sendTab(155, 747);
		sendTab(151, 748);
		sendTab(152, 749);
		sendTab(153, 750);
		player.getPackets().sendInterface(true, 752, 9, 137);
		player.getPackets().sendInterface(true, 548, 9, 167);
		sendMagicBook();
		sendPrayerBook();
		sendEquipment();
		sendInventory();
		sendTaskSystem();
		//sendPlayerCustom();
		sendTab(174, 930);//quest
		sendTab(181, 1109);// 551 ignore now friendchat
		sendTab(182, 1110);// 589 old clan chat now new clan chat
		sendTab(180, 550);// friend list
		sendTab(185, 187);// music
		sendTab(186, 34); // notes
		sendTab(189, 182);
		sendSkills();
		sendEmotes();
		sendSettings();
		sendTaskTab();
		sendCombatStyles();
		sendSquealOfFortuneTab();
	}

	public void sendSquealOfFortuneTab() {
		player.getSquealOfFortune().sendSpinCounts();
		player.getPackets().sendGlobalConfig(823, 1); // this config used in cs2 to tell current extra tab type (0 - none, 1 - sof, 2 - armies minigame tab)
		setWindowInterface(resizableScreen ? 119 : 179, 1139);
	}

	public void closeSquealOfFortuneTab() {
		removeWindowInterface(resizableScreen ? 119 : 179);
		player.getPackets().sendGlobalConfig(823, 0); // this config used in cs2 to tell current extra tab type (0 - none, 1 - sof, 2 - armies minigame tab)
	}

	public void sendXPPopup() {
		sendTab(resizableScreen ? 38 : 10, 1213); //xp
	}

	public void sendXPDisplay() {
		sendXPDisplay(1215);  //xp counter
	}

	public void sendXPDisplay(int interfaceId) {
		sendTab(resizableScreen ? 27 : 29, interfaceId);  //xp counter
	}

	public void closeXPPopup() {
		player.getPackets().closeInterface(resizableScreen ? 38 : 10);
	}

	public void closeXPDisplay() {
		player.getPackets().closeInterface(resizableScreen ? 27 : 29);
	}

	public void sendEquipment() {
		sendTab(resizableScreen ? 116 : 176, 387);
	}

	public void closeInterface(int one, int two) {
		player.getPackets().closeInterface(resizableScreen ? two : one);
	}

	public void closeEquipment() {
		player.getPackets().closeInterface(resizableScreen ? 116 : 176);
	}

	public void sendInventory() {
		sendTab(resizableScreen ? 115 : 175, Inventory.INVENTORY_INTERFACE);
	}

	public void closeInventory() {
		player.getPackets().closeInterface(resizableScreen ? 115 : 175);
	}

	public void closeSkills() {
		player.getPackets().closeInterface(resizableScreen ? 113 : 173);
	}

	public void closeCombatStyles() {
		player.getPackets().closeInterface(resizableScreen ? 111 : 171);
	}

	public void closePlayerSupport() {
		player.getPackets().closeInterface(resizableScreen ? 112 : 205);
	}

	public void sendCombatStyles() {
		sendTab(resizableScreen ? 111 : 171, 884);
	}

	public void sendTaskSystem() {
		ActionTab.sendTab(player);
		sendTab(resizableScreen ? 114 : 174, 930);
	}

	public void sendAchievments() {
		sendTab(resizableScreen ? 114 : 174, 1362);
	}

	// mouse over - 45;-2147483645;14784543;
	// 45;-2147483645;12547357;
	public void sendTaskTab() {
		sendTab(resizableScreen ? 112 : 172, 1056); //506
//		player.getPackets().sendIComponentText(506, 0, "<col=00FF00><shad=000000>Navigation Menu");
//		player.getPackets().sendIComponentText(506, 2,  "Donate Features");
//		player.getPackets().sendIComponentText(506, 12, "Training");
//		player.getPackets().sendIComponentText(506, 8,  "Boss / PVM");
//		player.getPackets().sendIComponentText(506, 6,  "Skilling");
//		player.getPackets().sendIComponentText(506, 10, "Minigames");
//		player.getPackets().sendIComponentText(506, 4,  "Altars");
//		player.getPackets().sendIComponentText(506, 14, "Slayer Dungeons");
	}
	
	/*public void sendPoints() {
		player.getInterfaceManager().sendInterface(410); //THE INTERFACE IT OPENS
		player.getPackets().sendIComponentText(410, 9, "~My points~"); //Title
		player.getPackets().sendIComponentText(410, 5, "Prestige: " + player.prestigeTokens + "");
		player.getPackets().sendIComponentText(410, 6, "Loyalty Points: " + player.getLoyaltyPoints() + "");
		player.getPackets().sendIComponentText(410, 7, "Trivia Points: " + player.getTriviaPoints() + "");
		player.getPackets().sendIComponentText(410, 8, "Dungeoneering Tokens: " + player.getDungTokens() + "");
       }*/

	public void sendBarcrawl() {

		player.getInterfaceManager().sendInterface(220); //THE INTERFACE IT OPENS
		player.getPackets().sendIComponentText(220, 1, "<col=24246B>The " + Constants.SERVER_NAME + " Barcrawl!"); //Title

		if (player.BlueMoonInn == 0) {
			player.getPackets().sendIComponentText(220, 3, "<col=E60000>BlueMoon Inn - Not Completed...");
		}
		else if (player.BlueMoonInn == 1) {
			player.getPackets().sendIComponentText(220, 3, "<col=19FF19>BlueMoon Inn - Completed!");
		}

		if (player.BlurberrysBar == 0) {
			player.getPackets().sendIComponentText(220, 4, "<col=E60000>Blurberry's Bar - Not Completed...");
		}
		else if (player.BlurberrysBar == 1) {
			player.getPackets().sendIComponentText(220, 4, "<col=19FF19>Blurberry's Bar - Completed!");
		}

		if (player.DeadMansChest == 0) {
			player.getPackets().sendIComponentText(220, 5, "<col=E60000>Dead Man's Chest - Not Completed...");
		}
		else if (player.DeadMansChest == 1) {
			player.getPackets().sendIComponentText(220, 5, "<col=19FF19>Dead Man's Chest - Completed!");
		}

		if (player.DragonInn == 0) {
			player.getPackets().sendIComponentText(220, 6, "<col=E60000>Dragon Inn - Not Completed...");
		}
		else if (player.DragonInn == 1) {
			player.getPackets().sendIComponentText(220, 6, "<col=19FF19>Dragon Inn - Completed!");
		}

		if (player.FlyingHorseInn == 0) {
			player.getPackets().sendIComponentText(220, 7, "<col=E60000>Flying Horse Inn - Not Completed...");
		}
		else if (player.FlyingHorseInn == 1) {
			player.getPackets().sendIComponentText(220, 7, "<col=19FF19>Flying Horse Inn - Completed!");
		}

		if (player.ForestersArms == 0) {
			player.getPackets().sendIComponentText(220, 8, "<col=E60000>Foresters Arms - Not Completed...");
		}
		else if (player.ForestersArms == 1) {
			player.getPackets().sendIComponentText(220, 8, "<col=19FF19>Foresters Arms - Completed!");
		}

		if (player.JollyBoarInn == 0) {
			player.getPackets().sendIComponentText(220, 9, "<col=E60000>Jolly Boar Inn - Not Completed...");
		}
		else if (player.JollyBoarInn == 1) {
			player.getPackets().sendIComponentText(220, 9, "<col=19FF19>Jolly Boar Inn - Completed!");
		}

		if (player.KaramjaSpiritsBar == 0) {
			player.getPackets().sendIComponentText(220, 10, "<col=E60000>Karamja Spirits Bar - Not Completed...");
		}
		else if (player.KaramjaSpiritsBar == 1) {
			player.getPackets().sendIComponentText(220, 10, "<col=19FF19>Karamja Spirits Bar - Completed!");
		}

		if (player.RisingSun == 0) {
			player.getPackets().sendIComponentText(220, 11, "<col=E60000>Rising Sun Inn - Not Completed...");
		}
		else if (player.RisingSun == 1) {
			player.getPackets().sendIComponentText(220, 11, "<col=19FF19>Rising Sun Inn - Completed!");
		}

		if (player.RustyAnchor == 0) {
			player.getPackets().sendIComponentText(220, 12, "<col=E60000>Rusty Anchor Inn - Not Completed...");
		}
		else if (player.RustyAnchor == 1) {
			player.getPackets().sendIComponentText(220, 12, "<col=19FF19>Rusty Anchor Inn - Completed!");
		}




	}//end of bar crawl

	/**
	 * Help Manual
	 */

	public void sendHelpBook() {


		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "<img=13>" + Constants.SERVER_NAME + " Help<img=13>");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "How can I start to make money?");
		player.getPackets().sendIComponentText(275, 12, "Killing slayer monsters is a great way for a main accounts");
		player.getPackets().sendIComponentText(275, 13, "Many items that they drop can be sold to the General store");
		player.getPackets().sendIComponentText(275, 14, "Thieving is a great way of starting out to make money aswell.");
		player.getPackets().sendIComponentText(275, 15, "");
		player.getPackets().sendIComponentText(275, 16, "<col=990099>General Information");
		player.getPackets().sendIComponentText(275, 17, "The task tab, is the official teleportal system it offers");
		player.getPackets().sendIComponentText(275, 18, "A big variety of training monsters to bossing monsters");
		player.getPackets().sendIComponentText(275, 19, "Your quest tab is the information panel, every points you get");
		player.getPackets().sendIComponentText(275, 20, "Through the game will be shown in there.");
		player.getPackets().sendIComponentText(275, 21, "");
		player.getPackets().sendIComponentText(275, 22, "");
		player.getPackets().sendIComponentText(275, 23, "<col=990099>How can I recieve help?");
		player.getPackets().sendIComponentText(275, 24, "If you join friends chat <shad=990099>'help'</shad> there wiill be");
		player.getPackets().sendIComponentText(275, 25, "Plenty of other players that can help you, or you can use the");
		player.getPackets().sendIComponentText(275, 26, "forums for more detailed guides!");
		player.getPackets().sendIComponentText(275, 27, "Need more help? Check out our forums at " + Constants.FORUMS_LINK);

		for (int i = 27; i < 310; i++) {
			player.getPackets().sendIComponentText(275, i, "");
		}
	}


	public void sendDwarfCannon() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Dwarf Cannon Quest");
		player.getPackets().sendIComponentText(275, 10, "");
		player.getPackets().sendIComponentText(275, 11, "Speak to the Captain Lawgof at the Kingdom of Kandarin");
		player.getPackets().sendIComponentText(275, 12, "I have fixed "+player.fixedRailings+"/6 of the railings.");
		player.getPackets().sendIComponentText(275, 13, "");
		player.getPackets().sendIComponentText(275, 14, "");
		player.getPackets().sendIComponentText(275, 15, "");
		player.getPackets().sendIComponentText(275, 16, "");
		player.getPackets().sendIComponentText(275, 17, "");
		if (player.fixedRailings >= 6) {
			player.getPackets().sendIComponentText(275, 12, "<str>I have fixed "+player.fixedRailings+"/6 of the railings.");
		}
		if (player.completedRailingTask == true) {
			player.getPackets().sendIComponentText(275, 14, "I should find 'Nulodion' who is located at the Dwarven Mine.");
		}
		if (player.completedDwarfCannonQuest == true) {
			player.getPackets().sendIComponentText(275, 11, "<str>Speak to the Captain Lawgof at the Kingdom of Kandarin");
			player.getPackets().sendIComponentText(275, 14, "<str>I should find 'Nulodion' who is located at the Dwarven Mine.");
			player.getPackets().sendIComponentText(275, 15, "");
			player.getPackets().sendIComponentText(275, 16, "<u>Quest Complete.</u>");
			player.getPackets().sendIComponentText(275, 17, "Use a steel bar on any furnace to make cannonballs.");
			player.getPackets().sendIComponentText(275, 18, "You can now use Dwarf Cannons.");
		}
		player.getPackets().sendIComponentText(275, 18, "");
		player.getPackets().sendIComponentText(275, 19, "");
		player.getPackets().sendIComponentText(275, 20, "");
	}
	public void sendSmokingKills() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "Smoking Kills");
		player.getPackets().sendIComponentText(275, 10, "");
		if (player.sKQuest == 0) {
			player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to </col> <col=660000>Sumona</col> <col=330099>In</col> <col=330099>Pollnivneach </col>");
			player.getPackets().sendIComponentText(275, 12, "");
			player.getPackets().sendIComponentText(275, 13, "<col=33099>Requirements: </col>");
			player.getPackets().sendIComponentText(275, 14, "35 Slayer");
			player.getPackets().sendIComponentText(275, 15, "25 Crafting");
			player.getPackets().sendIComponentText(275, 16, "Must have completed Restless Ghost Quest");
			player.getPackets().sendIComponentText(275, 17, "Must be able to fight off multiple high level monsters at once");
			for (int i = 18; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		}

	}
	public void sendGertrudesCat() {
		player.getInterfaceManager().sendInterface(275);

		player.getPackets().sendIComponentText(275, 1, "Gertrude's Cat");
		player.getPackets().sendIComponentText(275, 10, "");
		if (player.gertCat == 0){
			player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to</col> <col=660000>Gertrude</col> <col=330099>In</col> <col=330099>Varrock City </col>");
			player.getPackets().sendIComponentText(275, 12, "");
			player.getPackets().sendIComponentText(275, 13, "<col=33099>Requirements: </col>");
			player.getPackets().sendIComponentText(275, 14, "There are no requirements for this quest.");
			player.getPackets().sendIComponentText(275, 15, "");
			for (int i = 16; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		} else if (player.gertCat == 1){
			player.getPackets().sendIComponentText(275, 11, "Gertrude said I should talk to <col=660000>Shilop and Wilough</col> In <col=330099>Varrock square </col>");
			player.getPackets().sendIComponentText(275, 12, "to find out where her lost cat, <col=660000>Fluffs</col>Might be.");
			player.getPackets().sendIComponentText(275, 13, "");
			player.getPackets().sendIComponentText(275, 14, "");
			player.getPackets().sendIComponentText(275, 15, "");
			for (int i = 16; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		} else if (player.gertCat == 2){
			player.getPackets().sendIComponentText(275, 11, "<str>Gertrude said I should talk to <col=660000>Shilop and Wilough</col> In <col=330099>Varrock square </col>");
			player.getPackets().sendIComponentText(275, 12, "<str>to find out where her lost cat, <col=660000>Fluffs</col>Might be.");
			player.getPackets().sendIComponentText(275, 13, "Gertrude's sons said that <col=660000>Fluffs</col> is in the <col=330099>Lumber Yard </col>");
			player.getPackets().sendIComponentText(275, 14, "Which is <col=660000>east of Varrock</col>.");
			player.getPackets().sendIComponentText(275, 15, "");
			for (int i = 16; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		} else if (player.gertCat == 3){
			player.getPackets().sendIComponentText(275, 11, "<str>Gertrude said I should talk to <col=660000>Shilop and Wilough</col> In <col=330099>Varrock square </col>");
			player.getPackets().sendIComponentText(275, 12, "<str>to find out where her lost cat, <col=660000>Fluffs</col>Might be.");
			player.getPackets().sendIComponentText(275, 13, "<str>Gertrude's sons said that <col=660000>Fluffs</col> is in the <col=330099>Lumber Yard </col>");
			player.getPackets().sendIComponentText(275, 14, "<str>Which is <col=660000>east of Varrock</col>.");
			player.getPackets().sendIComponentText(275, 15, "<col=660000>Fluffs</col> refuses to move. Perhaps she's <col=660000>thirsty</col>?");
			for (int i = 16; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		} else if (player.gertCat == 4){
			player.getPackets().sendIComponentText(275, 11, "<str>Gertrude said I should talk to <col=660000>Shilop and Wilough</col> In <col=330099>Varrock square </col>");
			player.getPackets().sendIComponentText(275, 12, "<str>to find out where her lost cat, <col=660000>Fluffs</col>Might be.");
			player.getPackets().sendIComponentText(275, 13, "<str>Gertrude's sons said that <col=660000>Fluffs</col> is in the <col=330099>Lumber Yard </col>");
			player.getPackets().sendIComponentText(275, 14, "<str>Which is <col=660000>east of Varrock</col>.");
			player.getPackets().sendIComponentText(275, 15, "<str><col=660000>Fluffs</col> refuses to move. Perhaps she's <col=660000>thirsty</col>?");
			player.getPackets().sendIComponentText(275, 16, "<col=660000>Fluffs</col> still refuses to move. Perhaps she's <col=660000>hungry</col>?");

			for (int i = 17; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		} else if (player.gertCat == 5){
			player.getPackets().sendIComponentText(275, 11, "<str>Gertrude said I should talk to <col=660000>Shilop and Wilough</col> In <col=330099>Varrock square </col>");
			player.getPackets().sendIComponentText(275, 12, "<str>to find out where her lost cat, <col=660000>Fluffs</col>Might be.");
			player.getPackets().sendIComponentText(275, 13, "<str>Gertrude's sons said that <col=660000>Fluffs</col> is in the <col=330099>Lumber Yard </col>");
			player.getPackets().sendIComponentText(275, 14, "<str>Which is <col=660000>east of Varrock</col>.");
			player.getPackets().sendIComponentText(275, 15, "<str><col=660000>Fluffs</col> refuses to move. Perhaps she's <col=660000>thirsty</col>?");
			player.getPackets().sendIComponentText(275, 16, "<str><col=660000>Fluffs</col> still refuses to move. Perhaps she's <col=660000>hungry</col>?");
			player.getPackets().sendIComponentText(275, 17, "<col=660000>Fluffs</col> still refuses to move. You need to find her nearby <col=660000>kittens</col>.");
			for (int i = 18; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		} else if (player.gertCat == 6){
			player.getPackets().sendIComponentText(275, 11, "<str>Gertrude said I should talk to <col=660000>Shilop and Wilough</col> In <col=330099>Varrock square </col>");
			player.getPackets().sendIComponentText(275, 12, "<str>to find out where her lost cat, <col=660000>Fluffs</col>Might be.");
			player.getPackets().sendIComponentText(275, 13, "<str>Gertrude's sons said that <col=660000>Fluffs</col> is in the <col=330099>Lumber Yard </col>");
			player.getPackets().sendIComponentText(275, 14, "<str>Which is <col=660000>east of Varrock</col>.");
			player.getPackets().sendIComponentText(275, 15, "<str><col=660000>Fluffs</col> refuses to move. Perhaps she's <col=660000>thirsty</col>?");
			player.getPackets().sendIComponentText(275, 16, "<str><col=660000>Fluffs</col> still refuses to move. Perhaps she's <col=660000>hungry</col>?");
			player.getPackets().sendIComponentText(275, 17, "<str><col=660000>Fluffs</col> still refuses to move. You need to find her nearby <col=660000>kittens</col>.");
			player.getPackets().sendIComponentText(275, 18, "<col=660000>Fluffs</col> has gone home. You should report back to <col=660000>Gertrude</col>.");
			for (int i = 19; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		}
		else if (player.gertCat == 7){
			player.getPackets().sendIComponentText(275, 11, "<str>Gertrude said I should talk to <col=660000>Shilop and Wilough</col> In <col=330099>Varrock square </col>");
			player.getPackets().sendIComponentText(275, 12, "<str>to find out where her lost cat, <col=660000>Fluffs</col>Might be.");
			player.getPackets().sendIComponentText(275, 13, "<str>Gertrude's sons said that <col=660000>Fluffs</col> is in the <col=330099>Lumber Yard </col>");
			player.getPackets().sendIComponentText(275, 14, "<str>Which is <col=660000>east of Varrock</col>.");
			player.getPackets().sendIComponentText(275, 15, "<str><col=660000>Fluffs</col> refuses to move. Perhaps she's <col=660000>thirsty</col>?");
			player.getPackets().sendIComponentText(275, 16, "<str><col=660000>Fluffs</col> still refuses to move. Perhaps she's <col=660000>hungry</col>?");
			player.getPackets().sendIComponentText(275, 17, "<str><col=660000>Fluffs</col> still refuses to move. You need to find her nearby <col=660000>kittens</col>.");
			player.getPackets().sendIComponentText(275, 18, "<str><col=660000>Fluffs</col> has gone home. You should report back to <col=660000>Gertrude</col>.");
			player.getPackets().sendIComponentText(275, 19, "");
			player.getPackets().sendIComponentText(275, 20, "<u>Quest Complete.</u>");
			for (int i = 21; i < 310; i++) {
				player.getPackets().sendIComponentText(275, i, "");
			}
		}

	}
	public void handleGertrudesCatFinish() {
		player.getInterfaceManager().sendInterface(277);
		player.getPackets().sendIComponentText(277, 4, "You have completed Gertrude's Cat");
		player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
		player.getPackets().sendIComponentText(277, 9, "You are awarded:");
		player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
		player.getPackets().sendIComponentText(277, 11, "1525 Cooking XP");
		player.getPackets().sendIComponentText(277, 12, "A chocolate cake");
		player.getPackets().sendIComponentText(277, 13, "A bowl of stew");
		player.getPackets().sendIComponentText(277, 14, "Ability to Raise Cats");
		player.getPackets().sendIComponentText(277, 15, "Two spins on the Squeal of Fortune");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getPackets().sendItemOnIComponent(277, 5, 1561, 1);
		player.getPackets().sendGameMessage("Congratulations! You have completed the Gertrude's Cat quest!");
	}

	public void sendJewelryCrafting() {
		player.getInterfaceManager().sendInterface(446); //THE INTERFACE IT OPENS
		player.getPackets().sendIComponentText(446, 98, "<col=CC9900>------   <col=0000FFO>------   <col=00FF00>------   <col=FF0000>------   <col=FFFFFF>------   <col=CC33FF>------   <col=000000>------   <col=9966FF>------"); //Title

		player.getPackets().sendIComponentText(446, 22, "<col=CC9900>------   <col=0000FFO>------   <col=00FF00>------   <col=FF0000>------   <col=FFFFFF>------   <col=CC33FF>------   <col=000000>------");
		player.getPackets().sendIComponentText(446, 66, "<col=CC9900>------   <col=0000FFO>------   <col=00FF00>------   <col=FF0000>------   <col=FFFFFF>------   <col=CC33FF>------   <col=000000>------");
		player.getPackets().sendIComponentText(446, 51, "<col=CC9900>------   <col=0000FFO>------   <col=00FF00>------   <col=FF0000>------   <col=FFFFFF>------   <col=CC33FF>------   <col=000000>------");

	}

	public void sendSpinningWheel() {
		player.getInterfaceManager().sendInterface(438); //THE INTERFACE IT OPENS
		player.getPackets().sendIComponentText(438, 13, "What would you like to spin?"); //Title
		player.getPackets().sendItemOnIComponent(438, 30, 1759, 1);
		//player.getPackets().sendItems(30, 1759);
		player.getPackets().sendItemOnIComponent(438, 44, 1777, 1);
		player.getPackets().sendItemOnIComponent(438, 59, 6038, 1);
		player.getPackets().sendIComponentText(438, 32, "Ball Of Wool");
		player.getPackets().sendIComponentText(438, 46, "Bow String");
		player.getPackets().sendIComponentText(438, 61, "Magic String");
		player.getPackets().sendItemOnIComponent(438, 37, 9438, 1);
		player.getPackets().sendItemOnIComponent(438, 52, 9438, 1);
		player.getPackets().sendItemOnIComponent(438, 66, 954, 1);
		player.getPackets().sendIComponentText(438, 39, "C'Bow String");
		player.getPackets().sendIComponentText(438, 54, "C'Bow String");
		player.getPackets().sendIComponentText(438, 68, "Rope");

		player.getPackets().sendIComponentText(438, 16, "");
		player.getPackets().sendIComponentText(438, 18, "");
		player.getPackets().sendIComponentText(438, 23, "");
		player.getPackets().sendIComponentText(438, 25, "");
		player.getPackets().sendIComponentText(438, 73, "");
		player.getPackets().sendIComponentText(438, 75, "");
		player.getPackets().sendIComponentText(438, 79, "");
		player.getPackets().sendIComponentText(438, 81, "");
	}

	public void sendMonsterTwo() {
		player.getPackets().sendIComponentText(732, 226, "Monster/Boss Teleports"); //Title

		player.getPackets().sendIComponentText(732, 175, "<- Previous");
		player.getPackets().sendIComponentText(732, 176, "");
		player.getPackets().sendIComponentText(732, 177, "");
		player.getPackets().sendIComponentText(732, 171, "Demons");
		player.getPackets().sendIComponentText(732, 172, "NOTIC Buy stuff with sticks");
		player.getPackets().sendIComponentText(732, 173, "Drops: Trading Sticks");

		player.getPackets().sendIComponentText(732, 167, "");
		player.getPackets().sendIComponentText(732, 169, "");
		player.getPackets().sendIComponentText(732, 168, "");
		player.getPackets().sendIComponentText(732, 40, "");
		player.getPackets().sendIComponentText(732, 41, "");
		player.getPackets().sendIComponentText(732, 42, "");
		player.getPackets().sendIComponentText(732, 163, "");
		player.getPackets().sendIComponentText(732, 164, "");
		player.getPackets().sendIComponentText(732, 165, "");
		player.getPackets().sendIComponentText(732, 39, "");
		player.getPackets().sendIComponentText(732, 160, "");
		player.getPackets().sendIComponentText(732, 161, "");
		player.getPackets().sendIComponentText(732, 156, "");
		player.getPackets().sendIComponentText(732, 157, "");
		player.getPackets().sendIComponentText(732, 158, "");
		player.getPackets().sendIComponentText(732, 152, "");
		player.getPackets().sendIComponentText(732, 153, "");
		player.getPackets().sendIComponentText(732, 154, "");
		player.getPackets().sendIComponentText(732, 148, "");
		player.getPackets().sendIComponentText(732, 149, "");
		player.getPackets().sendIComponentText(732, 150, "");
		player.getPackets().sendIComponentText(732, 227, "Drops - The most noteable drops");
	}


	public void sendMonster() {
		player.getInterfaceManager().sendInterface(732); //THE INTERFACE IT OPENS
		player.getPackets().sendIComponentText(732, 226, "Monster/Boss Teleports"); //Title
		player.getPackets().sendItemOnIComponent(732, 174, 11286, 1);
		player.getPackets().sendIComponentText(732, 175, "King Black Dragon");
		player.getPackets().sendIComponentText(732, 176, "DANGER: In Wilderness");
		player.getPackets().sendIComponentText(732, 177, "Drops: Visage");
		player.getPackets().sendItemOnIComponent(732, 170, 12163, 1);
		player.getPackets().sendIComponentText(732, 171, "Bork");
		player.getPackets().sendIComponentText(732, 172, "NOTICE: Fight Once Per Day");
		player.getPackets().sendIComponentText(732, 173, "Drops: Charms/Goods");
		player.getPackets().sendItemOnIComponent(732, 166, 24338, 1);
		player.getPackets().sendIComponentText(732, 167, "Queen Black Dragon");
		player.getPackets().sendIComponentText(732, 169, "DANGER: Extremely Tough");
		player.getPackets().sendIComponentText(732, 168, "Drops: Royal");
		player.getPackets().sendIComponentText(732, 40, "Corporeal Beast");
		player.getPackets().sendIComponentText(732, 41, "DANGER: Challenging");
		player.getPackets().sendIComponentText(732, 42, "Drops: Spirit Shields");
		player.getPackets().sendItemOnIComponent(732, 162, 3140, 1);
		player.getPackets().sendIComponentText(732, 163, "Kalphite Queen");
		player.getPackets().sendIComponentText(732, 164, "NOTICE: Bring Anti-Poison");
		player.getPackets().sendIComponentText(732, 165, "Drops: Dragon Chainbody");
		player.getPackets().sendItemOnIComponent(732, 38, 19785, 1);
		player.getPackets().sendIComponentText(732, 39, "Wildy Wyrm");
		player.getPackets().sendIComponentText(732, 160, "DANGER: Hard/In Wildy");
		player.getPackets().sendIComponentText(732, 161, "Drops: Elite Void/Others");
		player.getPackets().sendItemOnIComponent(732, 155, 13429, 1);
		player.getPackets().sendIComponentText(732, 156, "Dagganoth Kings");
		player.getPackets().sendIComponentText(732, 157, "NOTICE: All 3 Attack At Once");
		player.getPackets().sendIComponentText(732, 158, "Drops: Rings/D Hatchet");
		player.getPackets().sendItemOnIComponent(732, 151, 14484, 1);
		player.getPackets().sendIComponentText(732, 152, "Tormented Demons");
		player.getPackets().sendIComponentText(732, 153, "NOTICE: They Use Prayers");
		player.getPackets().sendIComponentText(732, 154, "Drops: Dragon Claws");
		player.getPackets().sendItemOnIComponent(732, 147, 16995, 1);
		player.getPackets().sendIComponentText(732, 148, "Yk'Lagor The Thunderous");
		player.getPackets().sendIComponentText(732, 149, "DANGER: Group Recommended");
		player.getPackets().sendIComponentText(732, 150, "Drops: Dungeoneering Items");
		player.getPackets().sendIComponentText(732, 144, "Blink");
		player.getPackets().sendIComponentText(732, 145, "DANGER: Challenging");
		player.getPackets().sendIComponentText(732, 146, "Drops: Offhands");
		player.getPackets().sendIComponentText(732, 140, "Slayer Tower");
		player.getPackets().sendIComponentText(732, 141, "Varieties of Monsters");
		player.getPackets().sendIComponentText(732, 142, "Drops: Abby Whip/Dark Bow");
		player.getPackets().sendIComponentText(732, 136, "Polypore Dungeon");
		player.getPackets().sendIComponentText(732, 137, "Requires 82 Slayer");
		player.getPackets().sendIComponentText(732, 138, "Drops: Ganodermic/Polypore");
		player.getPackets().sendIComponentText(732, 132, "Glacor Cave");
		player.getPackets().sendIComponentText(732, 133, "Very Dangerous Monsters");
		player.getPackets().sendIComponentText(732, 134, "Drops: Special Boots");
		player.getPackets().sendIComponentText(732, 244, "Jadinko Lair");
		player.getPackets().sendIComponentText(732, 245, "Requires 80 Slayer");
		player.getPackets().sendIComponentText(732, 246, "Drops: Vine Whip");
		player.getPackets().sendIComponentText(732, 128, "Strykewyrms");
		player.getPackets().sendIComponentText(732, 129, "Fun Slayer Monsters");
		player.getPackets().sendIComponentText(732, 130, "Drops: Staff of Light");
		player.getPackets().sendIComponentText(732, 124, "Fremmenick Slayer Dungeon");
		player.getPackets().sendIComponentText(732, 125, "Many Slayer Monsters");
		player.getPackets().sendIComponentText(732, 126, "Drops: Loads of Items");
		player.getPackets().sendIComponentText(732, 120, "Kuradel's Slayer Dungeon");
		player.getPackets().sendIComponentText(732, 121, "Requires high slayer level");
		player.getPackets().sendIComponentText(732, 122, "Drops: High valued Items");
		player.getPackets().sendIComponentText(732, 120, "High Level Slayer");
		player.getPackets().sendIComponentText(732, 116, "Skeletal Wyverns");
		player.getPackets().sendIComponentText(732, 117, "Requires 72 Slayer");
		player.getPackets().sendIComponentText(732, 118, "Drops: Visage/Dragon C'bow");
		player.getPackets().sendIComponentText(732, 227, "Drops - The Most Notable Drops");
		player.getPackets().sendIComponentText(732, 112, "Frost Dragons");
		player.getPackets().sendIComponentText(732, 113, "Epic Prayer EXP");
		player.getPackets().sendIComponentText(732, 114, "Drops: Visage/Frost Bones");
		player.getPackets().sendIComponentText(732, 108, "Iron Dragons");
		player.getPackets().sendIComponentText(732, 109, "Strong Metal Dragons");
		player.getPackets().sendIComponentText(732, 110, "Drops: Visage/Dragon");
		player.getPackets().sendIComponentText(732, 104, "Bronze Dragons");
		player.getPackets().sendIComponentText(732, 105, "Weaker Metal Dragons");
		player.getPackets().sendIComponentText(732, 106, "Drops: Dragon");
		player.getPackets().sendIComponentText(732, 100, "Lava Dungeon");
		player.getPackets().sendIComponentText(732, 101, "Access to Black Dragons");
		player.getPackets().sendIComponentText(732, 102, "Drops: Valuable Items");
		player.getPackets().sendIComponentText(732, 96, "Forinthry Dungeon");
		player.getPackets().sendIComponentText(732, 97, "Revants, Dragons & More");
		player.getPackets().sendIComponentText(732, 98, "Drops: PvP Armor");
		player.getPackets().sendIComponentText(732, 93, "Taverly Dungeon");
		player.getPackets().sendIComponentText(732, 92, "Varieties of Monsters");
		player.getPackets().sendIComponentText(732, 94, "Drops: Many Items");
		player.getPackets().sendIComponentText(732, 88, "Brimhaven Dungeon");
		player.getPackets().sendIComponentText(732, 89, "Dragons, Demons, and More");
		player.getPackets().sendIComponentText(732, 90, "Drops: Visage/Dragon");
		player.getPackets().sendIComponentText(732, 84, "Chaos Druids");
		player.getPackets().sendIComponentText(732, 85, "Ultimate Herb Resource");
		player.getPackets().sendIComponentText(732, 86, "Drops: Herbs");
		player.getPackets().sendIComponentText(732, 80, "Elite Knights");
		player.getPackets().sendIComponentText(732, 81, "Armies of Elite Warriors");
		player.getPackets().sendIComponentText(732, 82, "Drops: Elite Armor");
		player.getPackets().sendIComponentText(732, 76, "Lumbridge Swamp");
		player.getPackets().sendIComponentText(732, 77, "Swamp Infested Cave");
		player.getPackets().sendIComponentText(732, 78, "Drops: Varities of Items");
		player.getPackets().sendIComponentText(732, 72, "Living Rock Caverns");
		player.getPackets().sendIComponentText(732, 73, "Living Rock Creatures");
		player.getPackets().sendIComponentText(732, 74, "Drops: Living Rock Minerals");
		player.getPackets().sendIComponentText(732, 68, "Grotworm Lair");
		player.getPackets().sendIComponentText(732, 69, "Gross Grotworms");
		player.getPackets().sendIComponentText(732, 70, "Drops: High Valued Items");
		player.getPackets().sendIComponentText(732, 64, "Ancient Cavern");
		player.getPackets().sendIComponentText(732, 65, "Mithril/Brutal Dragons");
		player.getPackets().sendIComponentText(732, 66, "Drops: DFH/Visage");
		player.getPackets().sendIComponentText(732, 60, "Fire Giants");
		player.getPackets().sendIComponentText(732, 61, "Great Training");
		player.getPackets().sendIComponentText(732, 62, "Drops: Charms");
		player.getPackets().sendIComponentText(732, 56, "Sunfreet");
		player.getPackets().sendIComponentText(732, 57, "Dangerous Boss");
		player.getPackets().sendIComponentText(732, 58, "Drops: Dominion Items");
		player.getPackets().sendIComponentText(732, 52, "Chaos Dwogre Battlefield");
		player.getPackets().sendIComponentText(732, 53, "Multi Warfield");
		player.getPackets().sendIComponentText(732, 54, "Drops: Handcannon/D Pickaxe");
		player.getPackets().sendIComponentText(732, 48, "Zogre Training Grounds");
		player.getPackets().sendIComponentText(732, 49, "Great cb/pray exp");
		player.getPackets().sendIComponentText(732, 50, "Ogre Coffin Keys/Good bones");
		player.getPackets().sendIComponentText(732, 44, "Demons");
		player.getPackets().sendIComponentText(732, 45, "Good exp");
		player.getPackets().sendIComponentText(732, 46, "Drops: trading sticks for shop");

	}

	public void sendTrainingTelePorts() {
		if (player.teleports == 1) {
			player.getInterfaceManager().sendInterface(583);
			player.getPackets().sendIComponentText(583, 14, "Training Teleports Page");
			player.getPackets().sendIComponentText(583, 50, "<col=7CFC00>Cows. ");
			player.getPackets().sendIComponentText(583, 51, "<col=7CFC00>Yaks. ");
			player.getPackets().sendIComponentText(583, 52, "<col=7CFC00>Rock Crabs");
			player.getPackets().sendIComponentText(583, 53, "<col=7CFC00>Ghouls. ");
			player.getPackets().sendIComponentText(583, 54, "<col=7CFC00>Bandit Camp. ");
			player.getPackets().sendIComponentText(583, 55, "<col=7CFC00>Gnome Battlefield. ");
			player.getPackets().sendIComponentText(583, 56, "<col=7CFC00>Stronghold of Security. ");
			player.getPackets().sendIComponentText(583, 57, "<col=7CFC00>Stronghold of Player Safety. ");
			player.getPackets().sendIComponentText(583, 58, "<col=7CFC00>Lumbridge Swamp. ");
			player.getPackets().sendIComponentText(583, 59, "<col=7CFC00>Karamja Volcano ");
			player.getPackets().sendIComponentText(583, 60, "<col=7CFC00>Varrock Sewer. ");
			player.getPackets().sendIComponentText(583, 61, "<col=7CFC00>Draynor Sewer. ");
			player.getPackets().sendIComponentText(583, 62, "<col=c93030>Edgeville Dungeon. ");
			player.getPackets().sendIComponentText(583, 63, "<col=c93030>Lumbridge Catacombs. ");
			player.getPackets().sendIComponentText(583, 64, "<col=c93030>White Wolf Mountain. ");
			player.getPackets().sendIComponentText(583, 65, "<col=c93030>Experiments. ");
			player.getPackets().sendIComponentText(583, 66, "<col=8bf906>Mountain Trolls. ");
			player.getPackets().sendIComponentText(583, 67, "<col=8bf906>Chaos Druids. ");
			player.getPackets().sendIComponentText(583, 68, "<col=8bf906>Ardougne Training Camp. ");
			player.getPackets().sendIComponentText(583, 69, "<col=8bf906>Zogre Infestation. ");
			player.getPackets().sendIComponentText(583, 70, "<col=8bf906>Iron Dragons. ");
			player.getPackets().sendIComponentText(583, 71, "<col=8bf906>Bronze Dragons. ");
			player.getPackets().sendIComponentText(583, 72, "<col=8bf906>Living Rock Caverns. ");
			player.getPackets().sendIComponentText(583, 73, "<col=8bf906>Tzhaar City. ");
			player.getPackets().sendIComponentText(583, 74, "<col=8bf906>Elite Knights. ");
			player.getPackets().sendIComponentText(583, 75, "<col=e5ff00>Chaos Battlefield. ");
			player.getPackets().sendIComponentText(583, 76, "<col=e5ff00>Haunted Woods. ");
			player.getPackets().sendIComponentText(583, 77, "<col=e5ff00>Scabaras Swamp. ");
			player.getPackets().sendIComponentText(583, 78, "<col=e5ff00>Tolna's Rift. ");
			player.getPackets().sendIComponentText(583, 79, "<col=e5ff00>Page 2. ");
		} else if (player.teleports == 2) {
			player.getInterfaceManager().sendInterface(583);
			player.getPackets().sendIComponentText(583, 14, "Training Teleports Page");
			player.getPackets().sendIComponentText(583, 50, "<col=7CFC00>Ape Atoll Temple.");
			player.getPackets().sendIComponentText(583, 51, "<col=7CFC00>Evil Chicken's Lair.");
			player.getPackets().sendIComponentText(583, 52, "<col=7CFC00>Ogre Enclave");
			player.getPackets().sendIComponentText(583, 53, "<col=7CFC00>Gorak Plane. ");
			player.getPackets().sendIComponentText(583, 54, "<col=7CFC00>Ourania Cave. ");
			player.getPackets().sendIComponentText(583, 55, "<col=7CFC00>Chickens. ");
			player.getPackets().sendIComponentText(583, 56, "<col=7CFC00>Armored zombies. ");
			player.getPackets().sendIComponentText(583, 57, "<col=7CFC00>Slayer tower. ");
			player.getPackets().sendIComponentText(583, 58, "<col=7CFC00>Frost dragons. ");
			player.getPackets().sendIComponentText(583, 59, "<col=7CFC00>Rune essence mine ");
			player.getPackets().sendIComponentText(583, 60, "<col=7CFC00>Polypore dungeon (bottom level). ");
			player.getPackets().sendIComponentText(583, 61, "<col=7CFC00>Glacors. ");
			player.getPackets().sendIComponentText(583, 62, "<col=c93030>Jadinko lair. ");
			player.getPackets().sendIComponentText(583, 63, "<col=c93030>Taverly dungeon. ");
			player.getPackets().sendIComponentText(583, 64, "<col=c93030>Revenants (PVP). ");
			player.getPackets().sendIComponentText(583, 65, "<col=c93030>Cyclops (Defenders). ");
			player.getPackets().sendIComponentText(583, 66, "<col=8bf906>Dagannoth Kings. ");
			player.getPackets().sendIComponentText(583, 67, "<col=8bf906>Dungeoneering. ");
			player.getPackets().sendIComponentText(583, 68, "<col=8bf906>Skeletal wyverns. ");
			player.getPackets().sendIComponentText(583, 69, "<col=8bf906>Kalphite Queen. ");
			player.getPackets().sendIComponentText(583, 70, "<col=8bf906>Barrelchest. ");
			player.getPackets().sendIComponentText(583, 71, "<col=8bf906>Coming soon. ");
			player.getPackets().sendIComponentText(583, 72, "<col=8bf906>Coming soon. ");
			player.getPackets().sendIComponentText(583, 73, "<col=8bf906>Coming soon. ");
			player.getPackets().sendIComponentText(583, 74, "<col=8bf906>Coming soon. ");
			player.getPackets().sendIComponentText(583, 75, "<col=e5ff00>Coming soon. ");
			player.getPackets().sendIComponentText(583, 76, "<col=e5ff00>Coming soon ");
			player.getPackets().sendIComponentText(583, 77, "<col=e5ff00>Coming soon. ");
			player.getPackets().sendIComponentText(583, 78, "<col=e5ff00>Coming soon. ");
			player.getPackets().sendIComponentText(583, 79, "<col=e5ff00>Page 2. ");
		}

	}

	public void MonsterTelePorts() {//Replacement for current monster interface
		player.getInterfaceManager().sendInterface(732);
		player.getPackets().sendIComponentText(732, 226, "Monster/Boss Teleports"); //Title

		player.getPackets().sendItemOnIComponent(732, 174, 11286, 1);
		player.getPackets().sendIComponentText(732, 175, "Low Level Training");
		player.getPackets().sendIComponentText(732, 176, "Training for Players under 50 Combat");
		player.getPackets().sendIComponentText(732, 177, "");
		player.getPackets().sendItemOnIComponent(732, 170, 12163, 1);
		player.getPackets().sendIComponentText(732, 171, "Medium Level Training");
		player.getPackets().sendIComponentText(732, 172, "Training for Players above 50 Combat");
		player.getPackets().sendIComponentText(732, 173, "");
		player.getPackets().sendItemOnIComponent(732, 166, 24338, 1);
		player.getPackets().sendIComponentText(732, 167, "Low Level Areas");
		player.getPackets().sendIComponentText(732, 169, "Dungeons/Areas for Players under 60 Combat");
		player.getPackets().sendIComponentText(732, 168, "");
		player.getPackets().sendIComponentText(732, 40, "Medium Level Areas");
		player.getPackets().sendIComponentText(732, 41, "Dungeons/Areas for Players 60-100 Combat");
		player.getPackets().sendIComponentText(732, 42, "");
		player.getPackets().sendItemOnIComponent(732, 162, 3140, 1);
		player.getPackets().sendIComponentText(732, 163, "High level Dungeons/Areas");
		player.getPackets().sendIComponentText(732, 164, "Dungeons/Areas for Players above 100 Combat");
		player.getPackets().sendIComponentText(732, 165, "");
		player.getPackets().sendItemOnIComponent(732, 38, 19785, 1);
		player.getPackets().sendIComponentText(732, 39, "Slayer Locations");
		player.getPackets().sendIComponentText(732, 160, "Various Locations for Slayer Monsters/Tasks");
		player.getPackets().sendIComponentText(732, 161, "");
		player.getPackets().sendItemOnIComponent(732, 155, 13429, 1);
		player.getPackets().sendIComponentText(732, 156, "Medium Level Bosses");
		player.getPackets().sendIComponentText(732, 157, "Bosses that are not very hard to kill");
		player.getPackets().sendIComponentText(732, 158, "");
		player.getPackets().sendItemOnIComponent(732, 151, 14484, 1);
		player.getPackets().sendIComponentText(732, 152, "High Level Bosses");
		player.getPackets().sendIComponentText(732, 153, "Very challenging bosses that require multiple players");
		player.getPackets().sendIComponentText(732, 154, "");
		player.getPackets().sendItemOnIComponent(732, 147, 16995, 1);
		player.getPackets().sendIComponentText(732, 148, "");
		player.getPackets().sendIComponentText(732, 149, "");
		player.getPackets().sendIComponentText(732, 150, "");
		player.getPackets().sendIComponentText(732, 144, "");
		player.getPackets().sendIComponentText(732, 145, "");
		player.getPackets().sendIComponentText(732, 146, "");
		player.getPackets().sendIComponentText(732, 140, "");
		player.getPackets().sendIComponentText(732, 141, "");
		player.getPackets().sendIComponentText(732, 142, "");
		player.getPackets().sendIComponentText(732, 136, "");
		player.getPackets().sendIComponentText(732, 137, "");
		player.getPackets().sendIComponentText(732, 138, "");
		player.getPackets().sendIComponentText(732, 132, "");
		player.getPackets().sendIComponentText(732, 133, "");
		player.getPackets().sendIComponentText(732, 134, "");
		player.getPackets().sendIComponentText(732, 244, "");
		player.getPackets().sendIComponentText(732, 245, "");
		player.getPackets().sendIComponentText(732, 246, "");
		player.getPackets().sendIComponentText(732, 128, "");
		player.getPackets().sendIComponentText(732, 129, "");
		player.getPackets().sendIComponentText(732, 130, "");
		player.getPackets().sendIComponentText(732, 124, "");
		player.getPackets().sendIComponentText(732, 125, "");
		player.getPackets().sendIComponentText(732, 126, "");
		player.getPackets().sendIComponentText(732, 120, "");
		player.getPackets().sendIComponentText(732, 121, "");
		player.getPackets().sendIComponentText(732, 122, "");
		player.getPackets().sendIComponentText(732, 120, "");
		player.getPackets().sendIComponentText(732, 116, "");
		player.getPackets().sendIComponentText(732, 117, "");
		player.getPackets().sendIComponentText(732, 118, "");
		player.getPackets().sendIComponentText(732, 112, "");
		player.getPackets().sendIComponentText(732, 113, "");
		player.getPackets().sendIComponentText(732, 114, "");
		player.getPackets().sendIComponentText(732, 108, "");
		player.getPackets().sendIComponentText(732, 109, "");
		player.getPackets().sendIComponentText(732, 110, "");
		player.getPackets().sendIComponentText(732, 104, "");
		player.getPackets().sendIComponentText(732, 105, "");
		player.getPackets().sendIComponentText(732, 106, "");
		player.getPackets().sendIComponentText(732, 100, "");
		player.getPackets().sendIComponentText(732, 101, "");
		player.getPackets().sendIComponentText(732, 102, "");
		player.getPackets().sendIComponentText(732, 96, "");
		player.getPackets().sendIComponentText(732, 97, "");
		player.getPackets().sendIComponentText(732, 98, "");
		player.getPackets().sendIComponentText(732, 93, "");
		player.getPackets().sendIComponentText(732, 92, "");
		player.getPackets().sendIComponentText(732, 94, "");
		player.getPackets().sendIComponentText(732, 88, "");
		player.getPackets().sendIComponentText(732, 89, "");
		player.getPackets().sendIComponentText(732, 90, "");
		player.getPackets().sendIComponentText(732, 84, "");
		player.getPackets().sendIComponentText(732, 85, "");
		player.getPackets().sendIComponentText(732, 86, "");
		player.getPackets().sendIComponentText(732, 80, "");
		player.getPackets().sendIComponentText(732, 81, "");
		player.getPackets().sendIComponentText(732, 82, "");
		player.getPackets().sendIComponentText(732, 76, "");
		player.getPackets().sendIComponentText(732, 77, "");
		player.getPackets().sendIComponentText(732, 78, "");
		player.getPackets().sendIComponentText(732, 72, "");
		player.getPackets().sendIComponentText(732, 73, "");
		player.getPackets().sendIComponentText(732, 74, "");
		player.getPackets().sendIComponentText(732, 68, "");
		player.getPackets().sendIComponentText(732, 69, "");
		player.getPackets().sendIComponentText(732, 70, "");
		player.getPackets().sendIComponentText(732, 64, "");
		player.getPackets().sendIComponentText(732, 65, "");
		player.getPackets().sendIComponentText(732, 66, "");
		player.getPackets().sendIComponentText(732, 60, "");
		player.getPackets().sendIComponentText(732, 61, "");
		player.getPackets().sendIComponentText(732, 62, "");
		player.getPackets().sendIComponentText(732, 56, "");
		player.getPackets().sendIComponentText(732, 57, "");
		player.getPackets().sendIComponentText(732, 58, "");
		player.getPackets().sendIComponentText(732, 52, "");
		player.getPackets().sendIComponentText(732, 53, "");
		player.getPackets().sendIComponentText(732, 54, "");
		player.getPackets().sendIComponentText(732, 48, "");
		player.getPackets().sendIComponentText(732, 49, "");
		player.getPackets().sendIComponentText(732, 50, "");
		player.getPackets().sendIComponentText(732, 44, "");
		player.getPackets().sendIComponentText(732, 45, "");
		player.getPackets().sendIComponentText(732, 46, "");
		player.getPackets().sendIComponentText(732, 227, "Select a catagory");
	}
	public void sendPlayerCustom() {
		sendTab(resizableScreen ? 114 : 172, 506);
		player.getPackets().sendIComponentText(506, 0, "<col=00FF00><shad=000000>~ Valius ~");
		player.getPackets().sendIComponentText(506, 2,  "Donate Features");
		player.getPackets().sendIComponentText(506, 12, "Training Teleports");
		player.getPackets().sendIComponentText(506, 8,  "Boss      Teleports");
		player.getPackets().sendIComponentText(506, 6,  "Skilling Teleports");
		player.getPackets().sendIComponentText(506, 10, "Minigames");
		player.getPackets().sendIComponentText(506, 4,  "Altars");
		player.getPackets().sendIComponentText(506, 14, "Slayer     Dungeons");
	}

	public void sendSkills() {
		sendTab(resizableScreen ? 113 : 173, 320);
	}

	public void sendSettings() {
		sendSettings(261);
	}

	public void closeSettings() {
		player.getPackets().closeInterface(resizableScreen ? 123 : 183);
	}

	public void sendSettings(int interfaceId) {
		sendTab(resizableScreen ? 123 : 183, interfaceId);
	}

	public void sendPrayerBook() {
		sendTab(resizableScreen ? 117 : 177, 271);
	}

	public void closePrayerBook() {
		player.getPackets().closeInterface(resizableScreen ? 117 : 177);
	}

	public void closePlayerCustom() {
		player.getPackets().closeInterface(resizableScreen ? 114 : 174);
	}

	public void sendMagicBook() {
		sendTab(resizableScreen ? 118 : 178, player.getCombatDefinitions()
				.getSpellBook());
	}

	public void closeMagicBook() {
		player.getPackets().closeInterface(resizableScreen ? 118 : 178);
	}

	public void sendEmotes() {
		sendTab(resizableScreen ? 124 : 184, 590);
	}

	public void closeEmotes() {
		player.getPackets().closeInterface(resizableScreen ? 124 : 184);
	}

	public boolean addInterface(int windowId, int tabId, int childId) {
		if (openedinterfaces.containsKey(tabId)) {
			player.getPackets().closeInterface(tabId);
		}
		openedinterfaces.put(tabId, new int[] { childId, windowId });
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public boolean containsInterface(int tabId, int childId) {
		if (childId == windowsPane) {
			return true;
		}
		if (!openedinterfaces.containsKey(tabId)) {
			return false;
		}
		return openedinterfaces.get(tabId)[0] == childId;
	}

	public int getTabWindow(int tabId) {
		if (!openedinterfaces.containsKey(tabId)) {
			return FIXED_WINDOW_ID;
		}
		return openedinterfaces.get(tabId)[1];
	}

	public boolean containsInterface(int childId) {
		if (childId == windowsPane) {
			return true;
		}
		for (int[] value : openedinterfaces.values()) {
			if (value[0] == childId) {
				return true;
			}
		}
		return false;
	}

	public boolean containsTab(int tabId) {
		return openedinterfaces.containsKey(tabId);
	}

	public void removeAll() {
		openedinterfaces.clear();
	}

	public boolean containsScreenInter() {
		return containsTab(resizableScreen ? RESIZABLE_SCREEN_TAB_ID
				: FIXED_SCREEN_TAB_ID);
	}

	public void removeScreenInterface() {
		removeWindowInterface(resizableScreen ? RESIZABLE_SCREEN_TAB_ID : FIXED_SCREEN_TAB_ID);
	}

	public void removeWindowInterface(int componentId) {
		removeInterface(resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, componentId);
	}

	public void closeScreenInterface() {
		player.getPackets()
		.closeInterface(
				resizableScreen ? RESIZABLE_SCREEN_TAB_ID
						: FIXED_SCREEN_TAB_ID);
	}

	public boolean containsInventoryInter() {
		return containsTab(resizableScreen ? RESIZABLE_INV_TAB_ID
				: FIXED_INV_TAB_ID);
	}

	public void closeInventoryInterface() {
		player.getPackets().closeInterface(
				resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}

	public boolean containsChatBoxInter() {
		return containsTab(CHAT_BOX_TAB);
	}

	public boolean removeTab(int tabId) {
		return openedinterfaces.remove(tabId) != null;
	}

	public boolean removeInterface(int tabId, int childId) {
		if (!openedinterfaces.containsKey(tabId)) {
			return false;
		}
		if (openedinterfaces.get(tabId)[0] != childId) {
			return false;
		}
		return openedinterfaces.remove(tabId) != null;
	}

	public void sendFadingInterface(int backgroundInterface) {
		if (hasRezizableScreen()) {
			player.getPackets().sendInterface(true, RESIZABLE_WINDOW_ID, 12,backgroundInterface);
		} else {
			player.getPackets().sendInterface(true, FIXED_WINDOW_ID, 11,backgroundInterface);
		}
	}

	public void closeFadingInterface() {
		if (hasRezizableScreen()) {
			player.getPackets().closeInterface(12);
		} else {
			player.getPackets().closeInterface(11);
		}
	}

	public void sendScreenInterface(int backgroundInterface, int interfaceId) {
		player.getInterfaceManager().closeScreenInterface();

		if (hasRezizableScreen()) {
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 40,
					backgroundInterface);
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 41,
					interfaceId);
		} else {
			player.getPackets().sendInterface(false, FIXED_WINDOW_ID, 200,
					backgroundInterface);
			player.getPackets().sendInterface(false, FIXED_WINDOW_ID, 201,
					interfaceId);

		}

		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				if (hasRezizableScreen()) {
					player.getPackets().closeInterface(40);
					player.getPackets().closeInterface(41);
				} else {
					player.getPackets().closeInterface(200);
					player.getPackets().closeInterface(201);
				}
			}
		});
	}

	public boolean hasRezizableScreen() {
		return resizableScreen;
	}

	public void setWindowsPane(int windowsPane) {
		this.windowsPane = windowsPane;
	}

	public int getWindowsPane() {
		return windowsPane;
	}

	public void closeSqueal() {
		player.getPackets().closeInterface(resizableScreen ? 119 : 179);
	}

	public void gazeOrbOfOculus() {
		player.getPackets().sendWindowsPane(475, 0);
		player.getPackets().sendInterface(true, 475, 57, 751);
		player.getPackets().sendInterface(true, 475, 55, 752);
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				player.getPackets().sendResetCamera();
			}

		});
	}

	public void sendStatistics() {
		player.getInterfaceManager().sendInterface(275);
		player.getPackets().sendIComponentText(275, 1, "<img=13><col=00FF00><shad=000000>Player Statistics<img=13>");
		player.getPackets().sendIComponentText(275, 2, "");
		player.getPackets().sendIComponentText(275, 10, "[Skills]");
		player.getPackets().sendIComponentText(275, 11, "<col=00FF00>[Herblore]</col>");
		player.getPackets().sendIComponentText(275, 12, "Guam's Cleaned: "+player.guams);
		player.getPackets().sendIComponentText(275, 13, "Tarromin's Cleaned: "+player.tarromins);
		player.getPackets().sendIComponentText(275, 14, "Marrentill's Cleaned: "+player.marrentills);
		player.getPackets().sendIComponentText(275, 15, "Harralander's Cleaned: "+player.harrlanders);
		player.getPackets().sendIComponentText(275, 16, "Ranarr's Cleaned: "+player.ranarrs);
		player.getPackets().sendIComponentText(275, 17, "Toadflax's Cleaned: "+player.toadflaxs);
		player.getPackets().sendIComponentText(275, 18, "Irit's Cleaned: "+player.irits);
		player.getPackets().sendIComponentText(275, 19, "Avantoe's Cleaned: "+player.avantoes);
		player.getPackets().sendIComponentText(275, 20, "Kwuarm's Cleaned: "+player.kwuarms);
		player.getPackets().sendIComponentText(275, 21, "Snapdragon's Cleaned: "+player.snapdragons);
		player.getPackets().sendIComponentText(275, 22, "Cadantine's Cleaned: "+player.cadantines);
		player.getPackets().sendIComponentText(275, 23, "Lantadyme's Cleaned: "+player.lantadymes);
		player.getPackets().sendIComponentText(275, 24, "Dwarf Weed's Cleaned: "+player.dwarfweeds);
		player.getPackets().sendIComponentText(275, 25, "Torstol's Cleaned: "+player.torstols);
		player.getPackets().sendIComponentText(275, 26, "Fellstalk's Cleaned: "+player.fellstalks);
		player.getPackets().sendIComponentText(275, 27, "");
		player.getPackets().sendIComponentText(275, 28, "[Melee]");
		player.getPackets().sendIComponentText(275, 29, "NPC'S Killed: "+player.npcs);
		player.getPackets().sendIComponentText(275, 30, "");
		player.getPackets().sendIComponentText(275, 31, "[Mining]");
		player.getPackets().sendIComponentText(275, 32, "Tin Ore: "+player.tinores);
		player.getPackets().sendIComponentText(275, 33, "Copper Ore: "+player.copperores);
		player.getPackets().sendIComponentText(275, 34, "Iron Ore: "+player.ironores);
		player.getPackets().sendIComponentText(275, 35, "Sandstone Ore: "+player.steelores);
		player.getPackets().sendIComponentText(275, 36, "Coal: "+player.coals);
		player.getPackets().sendIComponentText(275, 37, "Pure Essence: "+player.pureessence);
		player.getPackets().sendIComponentText(275, 38, "Mithril Ore: "+player.mithore);
		player.getPackets().sendIComponentText(275, 39, ""+player.addyore);
		player.getPackets().sendIComponentText(275, 40, "Runite Ore: "+player.runeore);
		player.getPackets().sendIComponentText(275, 41, "");
		player.getPackets().sendIComponentText(275, 42, "[Smithing]");
		player.getPackets().sendIComponentText(275, 43, "Bronze Bars: "+player.bronzebars);
		player.getPackets().sendIComponentText(275, 44, "iron Bars: "+player.ironbars);
		player.getPackets().sendIComponentText(275, 45, "Cannonballs: "+player.cannonballs);
		player.getPackets().sendIComponentText(275, 46, "Steel Bars: "+player.steelbars);
		player.getPackets().sendIComponentText(275, 47, "Mithril Bars: "+player.mithbars);
		player.getPackets().sendIComponentText(275, 48, "Adamantite Bars: "+player.addybars);
		player.getPackets().sendIComponentText(275, 49, "Runite Bars: "+player.runebars);
		player.getPackets().sendIComponentText(275, 50, "");
		player.getPackets().sendIComponentText(275, 51, "[Woodcutting]");
		player.getPackets().sendIComponentText(275, 52, "Logs Cut"+player.logscut);
		player.getPackets().sendIComponentText(275, 53, "Oaks Cut: "+player.oakscut);
		player.getPackets().sendIComponentText(275, 54, "Willows Cut: "+player.willowscut);
		player.getPackets().sendIComponentText(275, 55, "Maples Cut: "+player.maplescut);
		player.getPackets().sendIComponentText(275, 56, "Yews Cut: "+player.yewscut);
		player.getPackets().sendIComponentText(275, 57, "Magics Cut: "+player.magicscut);
		player.getPackets().sendIComponentText(275, 58, "Nests Fallen: "+player.nestsfallen);
		player.getPackets().sendIComponentText(275, 59, "");
		player.getPackets().sendIComponentText(275, 60, "[Fishing]");
		player.getPackets().sendIComponentText(275, 61, "Lobsters Fished: "+player.lobbyscaught);
		player.getPackets().sendIComponentText(275, 62, "Monkfishs Fished: "+player.monkscaught);
		player.getPackets().sendIComponentText(275, 63, "Sharks Fished: "+player.sharkscaught);
		player.getPackets().sendIComponentText(275, 64, "Cavefishs Fished: "+player.cavefishcaught);
		player.getPackets().sendIComponentText(275, 65, "Rocktails Fished: "+player.rocktailcaught);
		player.getPackets().sendIComponentText(275, 66, "");
		player.getPackets().sendIComponentText(275, 67, "[Slayer]");
		player.getPackets().sendIComponentText(275, 68, "Tasks Completed: "+player.tasksdone);
		player.getPackets().sendIComponentText(275, 69, "");
		player.getPackets().sendIComponentText(275, 70, "[Firemaking]");
		player.getPackets().sendIComponentText(275, 71, "Fires Lit: 0"+player.fireslit);
		player.getPackets().sendIComponentText(275, 72, "");
		player.getPackets().sendIComponentText(275, 73, "[Agility]");
		player.getPackets().sendIComponentText(275, 74, "Gnome Courses Ran: "+player.gnomescourse);
		player.getPackets().sendIComponentText(275, 75, "Barbarian Courses Ran: "+player.barbariancourse);
		player.getPackets().sendIComponentText(275, 76, "Wilderness Courses Ran: "+player.wildycourse);
		player.getPackets().sendIComponentText(275, 77, "Agility Pyramids Ran: "+player.agilitypyramid);
		player.getPackets().sendIComponentText(275, 78, "");
		player.getPackets().sendIComponentText(275, 79, "[Prayer]");
		player.getPackets().sendIComponentText(275, 80, "Bones Buried: "+player.bonesburried);
		player.getPackets().sendIComponentText(275, 81, "Bones Sacrificed: "+player.bonessacrificed);
		player.getPackets().sendIComponentText(275, 82, "");
		player.getPackets().sendIComponentText(275, 83, "[Fletching]");
		player.getPackets().sendIComponentText(275, 84, "Logs cut: "+player.logscutt);
		player.getPackets().sendIComponentText(275, 85, "Bows Made: "+player.bowsmade);
		player.getPackets().sendIComponentText(275, 86, "Bows Strung: "+player.bowsstrung);
		player.getPackets().sendIComponentText(275, 87, "");
		player.getPackets().sendIComponentText(275, 88, "[Crafting]");
		player.getPackets().sendIComponentText(275, 89, "Opals Cut: "+player.opalscut);
		player.getPackets().sendIComponentText(275, 90, "Jades Cut: "+player.jadescut);
		player.getPackets().sendIComponentText(275, 91, "Red Topazs Cut: "+player.redtopazcut);
		player.getPackets().sendIComponentText(275, 92, "Sapphires Cut: "+player.sapphirescut);
		player.getPackets().sendIComponentText(275, 93, "Emeralds Cut: "+player.emeraldscut);
		player.getPackets().sendIComponentText(275, 94, "Rubys Cut: "+player.rubyscut);
		player.getPackets().sendIComponentText(275, 95, "Diamonds Cut: "+player.diamondscut);
		player.getPackets().sendIComponentText(275, 96, "Dragnostones Cut: "+player.dragonstonescut);
		player.getPackets().sendIComponentText(275, 97, "Onyxs Cut: "+player.onyxscut);
		player.getPackets().sendIComponentText(275, 98, "");
		player.getPackets().sendIComponentText(275, 99, "[Cooking]");
		player.getPackets().sendIComponentText(275, 100, "Lobsters Cooked: "+player.lobbyscooked);
		player.getPackets().sendIComponentText(275, 101, "Swordfishs Cooked: "+player.swordfishcooked);
		player.getPackets().sendIComponentText(275, 102, "Monkfishs Cooked: "+player.monkscooked);
		player.getPackets().sendIComponentText(275, 103, "Sharks Cooked: "+player.sharkscooked);
		player.getPackets().sendIComponentText(275, 104, "Manta Rays Cooked: "+player.mantascooked);
		player.getPackets().sendIComponentText(275, 105, "Rocktails Cooked: "+player.rocktailscooked);
		player.getPackets().sendIComponentText(275, 106, "");
		player.getPackets().sendIComponentText(275, 107, "[Thieving]");
		player.getPackets().sendIComponentText(275, 108, "Stalls: "+player.stalls);
		player.getPackets().sendIComponentText(275, 109, "Men/woman: "+player.menwoman);
		player.getPackets().sendIComponentText(275, 110, "Paladins: "+player.paladins);
		player.getPackets().sendIComponentText(275, 111, "Heroes: "+player.heroes);
		player.getPackets().sendIComponentText(275, 112, "");
		player.getPackets().sendIComponentText(275, 113, "[Dungeoneering]");
		player.getPackets().sendIComponentText(275, 114, "Dungeons Completed: "+player.dungeons);
		player.getPackets().sendIComponentText(275, 115, "");
		player.getPackets().sendIComponentText(275, 116, "[Summoning]");
		player.getPackets().sendIComponentText(275, 117, "Pouches Made: "+player.pouches1);

	}

	/*
	 * returns lastGameTab
	 */
	public int openGameTab(int tabId) {
		player.getPackets().sendGlobalConfig(168, tabId);
		int lastTab = 4; // tabId
		// tab = tabId;
		return lastTab;
	}

	public void sendSquealOfFortune() {
		sendTab(resizableScreen ? 119 : 179, 1139);
		player.getPackets().sendGlobalConfig(823, 1);
	}

	public void setWindowInterface(int componentId, int interfaceId) {
		player.getPackets().sendInterface(true, resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, componentId, interfaceId);
	}



}
