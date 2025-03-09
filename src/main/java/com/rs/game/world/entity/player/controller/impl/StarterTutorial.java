package com.rs.game.world.entity.player.controller.impl;

import com.rs.Constants;
import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.controller.Controller;

public class StarterTutorial extends Controller {

	@Override
	public void start() {
		player.setYellOff(true);
		refreshStage();
		player.getPackets().sendMusicEffect(13);
		player.getPackets().sendMusic(12);
	}

	public void refreshStage() {
		int stage = player.startStages;
		if (stage == 1) {
			player.getPackets().sendConfig(1021, 2);
		} else if (stage == 2) {
			player.getPackets().sendConfig(1021, 0);
		} else if (stage == 3) {

		} else if (stage == 4) {
			NPC thievingShop = findNPC(303);
			if (thievingShop != null) {
				player.getHintIconsManager().addHintIcon(thievingShop, 0, -1, false);
			}
		} else if (stage == 5) {
			player.getHintIconsManager().removeUnsavedHintIcon();
			player.getPackets().sendConfig(1021, 4);
		} else if (stage == 6) {
			NPC banker = findNPC(2759);
			if (banker != null) {
				player.getHintIconsManager().addHintIcon(banker, 0, -1, false);
			}
		} else {

		}
		sendInterfaces();
		player.getPackets().sendIComponentText(506, 2, "Home");
		if (stage == 7) {
			player.getControlerManager().startControler(null);
		}
	}

	@Override
	public void sendInterfaces() {
		int stage = player.startStages;
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		if (stage == 1) {
			player.getPackets().sendIComponentText(372, 0, "Navigating Around");
			player.getPackets().sendIComponentText(372, 1, "Navigating around on " + Constants.SERVER_NAME + " is very simple, its all done through");
			player.getPackets().sendIComponentText(372, 2, "The navigation menu, where you have the ability to teleport around");
			player.getPackets().sendIComponentText(372, 3, Constants.SERVER_NAME + "! Please open your navigation menu by clicking on the");
			player.getPackets().sendIComponentText(372, 4, "tab that is highlighted.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 2) {
			player.getPackets().sendIComponentText(372, 0, "Head to the training place.");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "Its time to do your first teleport! Click on Training to be taken");
			player.getPackets().sendIComponentText(372, 3, "To the most popular training place for new players.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 3) {
			player.getPackets().sendIComponentText(372, 0, "Rock Crabs");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "Rock crabs have been made as they are on RS, perfection is what we aim");
			player.getPackets().sendIComponentText(372, 3, "For on " + Constants.SERVER_NAME + ", this is just 1 out of many different places to train.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "Go to your teleportal tab and head back to home to continue the tutorial.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 4) {
			player.getPackets().sendIComponentText(372, 0, "Getting Started");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "Thieving is a great way to earn some gold to get started with");
			player.getPackets().sendIComponentText(372, 3, "Each stall offers different priced items, and all thieved items");
			player.getPackets().sendIComponentText(372, 4, "Can be sold to Gerald, the guy thats marked with an arrow.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "Click on Gerald to Continue.");
		} else if (stage == 5) {
			player.getPackets().sendIComponentText(372, 0, "Shops");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "To make it easy and efficient, most shops is located right here");
			player.getPackets().sendIComponentText(372, 3, "In the bank, some shops is located just outside the bank.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 6) {
			player.getPackets().sendIComponentText(372, 0, "Information Panel");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "The quest tab is the official information panel, here you will");
			player.getPackets().sendIComponentText(372, 3, "Find alot of information about points, warning marks and your");
			player.getPackets().sendIComponentText(372, 4, "Accounts game mod.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "Click on the Banker to pick your game mod and get started! ");
		} else {
			player.getInterfaceManager().closeReplacedRealChatBoxInterface();
		}
	}

	public NPC findNPC(int id) {
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id) {
				continue;
			}
			return npc;
		}
		return null;
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == 303) {
			player.getControlerManager().startControler("StarterTutorial");
			return true;
		}
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 42378 || object.getId() == 42217 || object.getId() == 42377 && player.startStages == 5) {
			return true;
		}
		return false;
	}


	public int getStage() {
		if (getArguments() == null) {
			setArguments(new Object[] { 1 }); // index 0 = stage
		}
		return (Integer) getArguments()[1];
	}

}
