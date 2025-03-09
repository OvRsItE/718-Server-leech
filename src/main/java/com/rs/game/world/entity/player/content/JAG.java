package com.rs.game.world.entity.player.content;

import java.io.Serializable;
import java.util.ArrayList;

import com.rs.Constants;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.Player;

public class JAG implements Serializable {
	private static final long serialVersionUID = -4367930754392470616L;
	
	/**
	 * The allowed IP addresses
	 */
	private ArrayList<String> addresses = new ArrayList<String>();
	
	private Player player;
	public int randomQuestion;
	public String questionAnswer;
	
	public JAG() {
	}
	
	public void init() {
		if(player.hasJAG == true) {
		if (!getMacAddresses().contains(player.getMACAddress()))
			lockUntilVerified();
		return;
	}
	}
	
	public void lockUntilVerified() {
		player.getDialogueManager().startDialogue("JAGDialogue");
		player.getTemporaryAttributtes().put("JAGGED", Boolean.TRUE);
		player.setYellOff(true);
		sendInterfaces();
		player.setNextPosition(Constants.START_PLAYER_LOCATION);
		player.getBank().depositAllEquipment(false);
		player.getBank().depositAllInventory(false);
	}
	
	public void insertCurrentAddress() {
		addresses.add(player.getMACAddress());
	}
	
	public void sendInterfaces() {
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
			player.getPackets().sendIComponentText(372, 0, "JAG Account Lockdown");
			player.getPackets().sendIComponentText(372, 1, "You have logged into your account with an registered device.");
			player.getPackets().sendIComponentText(372, 2, "Please talk to the Account guardian to enable this device.");
			player.getPackets().sendIComponentText(372, 3, "");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
	}
	public void forceClose() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		player.getMusicsManager().reset();
		player.setYellOff(false);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInterfaceManager().sendInterfaces();
				player.getInterfaceManager()
						.closeReplacedRealChatBoxInterface();
				player.getTemporaryAttributtes().remove("JAGGED");
			}
		});
	}
	public ArrayList<String> getMacAddresses() {
		return addresses;
	}
	
	public void setRandomQuestion(int randomQuestion) {
		this.randomQuestion = randomQuestion;
	}
	
	public void setQuestionAnswer(String questionAnswer) {
		this.questionAnswer = questionAnswer;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}