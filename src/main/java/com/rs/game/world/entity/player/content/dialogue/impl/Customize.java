package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.dialogue.impl.EmotionManager;


public class Customize extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an option", "Purchase/Upgrade a citadel", "Enter Friend Citadel", "Enter Citadel");
		// player.setCitTypeX(0);
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int id) {
		if (stage == 1) {
			if (id == OPTION_1) {
				if (player.getCitTypeX() == 0) {
					sendPlayerDialogue(EmotionManager.QUESTION,
							"It seems I currently don't own a citadel. I can purchase one for one coins."
									+ "Which type of citadel should I get?");
					stage = 2;
				} else {
					if (player.getCitTypeY() == 592) {
						sendPlayerDialogue(
								EmotionManager.QUESTION,
								"I have a iron citadel, do I want to upgrade to a golden citadel? It will cost 100m");
						stage = 3;
					} else if (player.getCitTypeY() == 512) {
						sendPlayerDialogue(EmotionManager.NORMAL,
								"I have the maximum citadel status.");
						stage = 100;
					}
				}
			} else if (id == OPTION_2) {
				end();
				player.getTemporaryAttributtes().put("join_citadel", true);
                player.getPackets().sendInputNameScript("Enter a player name");
			} else if (id == OPTION_4) {
				if (player.getCitTypeX() == 0) {
					sendPlayerDialogue(EmotionManager.QUESTION,
							"It seems I currently don't own a citadel. I can purchase one for one coins."
									+ "Which type of citadel should I get?");
					stage = 2;
				} else {
					if (player.getCitTypeY() == 592) {
						sendOptionsDialogue("Select a slot type", "Iron Mine",
								"Iron Cooking", "Iron Smithing",
								"Iron Obelisk", "Iron Tree");
						stage = 7;
					} else if (player.getCitTypeY() == 512) {
						sendOptionsDialogue("Select a slot type",
								"Golden Mine", "Golden Cooking",
								"Golden Smithing", "Golden Obelisk",
								"Gold Tree");
						stage = 8;
					}
				}
			} else if (id == OPTION_3) {
				if (player.getCitTypeX() == 0) {
					sendPlayerDialogue(EmotionManager.QUESTION,
							"It seems I currently don't own a citadel. I can purchase one for one coins."
									+ "Which type of citadel should I get?");
					stage = 2;
				} else {
					player.getControlerManager().startControler("citadel");
					end();
				}
			}
		} else if (stage == 2) {
			sendOptionsDialogue("Select a citadel", "Iron Citadel - 35M",
					"Golden Citadel - 50M");
			stage = 4;
		} else if (stage == 3) {
			sendOptionsDialogue("Do you want a golden citadel?", "No", "Yes");
			stage = 4;
		} else if (stage == 4) {
			if (id == OPTION_1) {
				if (player.getInventory().containsItem(995, 35000000)) {
					if (player.getCitTypeY() != 592) {
						player.setCitTypeX(488);
						player.setCitTypeY(592);
						player.setCitNightTypeX(504);
						player.setCitNightTypeY(592);
						player.getInventory().deleteItem(995, 35000000);
						sendPlayerDialogue(EmotionManager.NORMAL,
								"I have purchased the iron citadel!");
					} else {
						end();
					}
					stage = 100;
				} else {
					if (player.getCitTypeY() != 592) {
					sendPlayerDialogue(EmotionManager.NORMAL,
							"I need more money for this!");
					} else {
						end();
					}
					stage = 100;
				}
			} else {
				if (player.getInventory().containsItem(995, 50000000)) {
					player.setCitTypeX(488);
					player.setCitTypeY(512);
					player.setCitNightTypeX(504);
					player.setCitNightTypeY(512);
					player.getInventory().deleteItem(995, 50000000);
					sendPlayerDialogue(EmotionManager.NORMAL,
							"I have purchased the golden citadel!");
					stage = 100;
				} else {
					sendPlayerDialogue(EmotionManager.NORMAL,
							"I need more money for this!");
					stage = 100;
				}
			}
		} else if (stage == 5) {
			if (id == OPTION_1) {
				player.setEastTypeX(488);
				player.setEastTypeY(510);
				player.setEastNightTypeX(552);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_2) {
				player.setEastTypeX(496);
				player.setEastTypeY(510);
				player.setEastNightTypeX(560);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_3) {
				player.setEastTypeX(504);
				player.setEastTypeY(510);
				player.setEastNightTypeX(568);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_4) {
				player.setEastTypeX(518);
				player.setEastTypeY(510);
				player.setEastNightTypeX(576);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_5) {
				player.setEastTypeX(488);
				player.setEastTypeY(506);
				player.setEastNightTypeX(552);
				player.setEastNightTypeY(506);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			}
		} else if (stage == 6) {
			if (id == OPTION_1) {
				player.setEastTypeX(494);
				player.setEastTypeY(510);
				player.setEastNightTypeX(558);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_2) {
				player.setEastTypeX(502);
				player.setEastTypeY(510);
				player.setEastNightTypeX(566);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_3) {
				player.setEastTypeX(510);
				player.setEastTypeY(510);
				player.setEastNightTypeX(574);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_4) {
				player.setEastTypeX(518);
				player.setEastTypeY(510);
				player.setEastNightTypeX(582);
				player.setEastNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_5) {
				player.setEastTypeX(494);
				player.setEastTypeY(506);
				player.setEastNightTypeX(558);
				player.setEastNightTypeY(506);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			}
		} else if (stage == 7) {
			if (id == OPTION_1) {
				player.setWestTypeX(488);
				player.setWestTypeY(510);
				player.setWestNightTypeX(552);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_2) {
				player.setWestTypeX(496);
				player.setWestTypeY(510);
				player.setWestNightTypeX(560);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_3) {
				player.setWestTypeX(504);
				player.setWestTypeY(510);
				player.setWestNightTypeX(568);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_4) {
				player.setWestTypeX(518);
				player.setWestTypeY(510);
				player.setWestNightTypeX(576);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_5) {
				player.setWestTypeX(488);
				player.setWestTypeY(506);
				player.setWestNightTypeX(522);
				player.setWestNightTypeY(506);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			}
		} else if (stage == 8) {
			if (id == OPTION_1) {
				player.setWestTypeX(494);
				player.setWestTypeY(510);
				player.setWestNightTypeX(558);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_2) {
				player.setWestTypeX(502);
				player.setWestTypeY(510);
				player.setWestNightTypeX(566);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_3) {
				player.setWestTypeX(510);
				player.setWestTypeY(510);
				player.setWestNightTypeX(574);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_4) {
				player.setWestTypeX(518);
				player.setWestTypeY(510);
				player.setWestNightTypeX(582);
				player.setWestNightTypeY(510);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			} else if (id == OPTION_5) {
				player.setWestTypeX(494);
				player.setWestTypeY(506);
				player.setWestNightTypeX(558);
				player.setWestNightTypeY(506);
				sendPlayerDialogue(EmotionManager.NORMAL,
						"Your slot has been set.");
				stage = 100;
			}
		} else if (stage == 100)
			end();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
