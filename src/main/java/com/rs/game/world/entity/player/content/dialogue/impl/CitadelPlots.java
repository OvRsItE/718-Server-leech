package com.rs.game.world.entity.player.content.dialogue.impl;

import java.util.Calendar;
import java.util.TimeZone;

import com.rs.game.map.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.player.content.social.citadel.Citadel;

public class CitadelPlots extends Dialogue {

	WorldObject object;
	private byte END = 100;
	private byte END2 = 125;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		stage = 1;
		if (player.getCitTypeY() == 592) {
			sendOptionsDialogue("Select a plot type", "Iron Mine - 5M",
					"Iron Cooking - 5M", "Iron Smithing - 5M",
					"Iron Obelisk - 15M", "Iron Tree - 5M");
		} else if (player.getCitTypeY() == 512) {
			sendOptionsDialogue("Select a plot type", "Golden Mine - 10M",
					"Golden Cooking - 10M", "Golden Smithing - 10M",
					"Golden Obelisk - 25M", "Golden Tree - 10M");
		}
	}

	@Override
	public void run(int interfaceId, int id) {
		if (stage == 1) {
			int x = (player.boundChunks[0] << 3);
			int y = (player.boundChunks[1] << 3);
			if (player.lockSwitch) {
				sendDialogue("You can't switch plots while an obelisk is active.");
				return;
			} else if (id == OPTION_1) {
				if (player.getCitTypeY() == 592)
					if (player.getInventory().containsItem(995, 5000000))
						player.getInventory().deleteItem(995, 5000000);
					else {
						sendDialogue("You don't have the coins required to do this in your inventory.");
						stage = END2;
						return;
					}
				else if (player.getInventory().containsItem(995, 10000000))
					player.getInventory().deleteItem(995, 10000000);
				else {
					sendDialogue("You don't have the coins required to do this in your inventory.");
					stage = END2;
					return;
				}
				if (object.getX() == x + 78 && object.getY() == y + 54) {
					if (player.getCitTypeY() == 592) {
						player.setEastTypeX(488);
						player.setEastTypeY(510);
						player.setEastNightTypeX(552);
						player.setEastNightTypeY(510);
						stage = END;
					} else {
						player.setEastTypeX(494);
						player.setEastTypeY(510);
						player.setEastNightTypeX(558);
						player.setEastNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 49 && object.getY() == y + 41) {
					if (player.getCitTypeY() == 592) {
						player.setWestTypeX(488);
						player.setWestTypeY(510);
						player.setWestNightTypeX(552);
						player.setWestNightTypeY(510);
						stage = END;
					} else {
						player.setWestTypeX(494);
						player.setWestTypeY(510);
						player.setWestNightTypeX(558);
						player.setWestNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 86 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot3X(488);
						player.setESlot3Y(510);
						player.setNESlot3X(552);
						player.setNESlot3Y(510);
						stage = END;
					} else {
						player.setESlot3X(494);
						player.setESlot3Y(510);
						player.setNESlot3X(558);
						player.setNESlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 94 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setESlot4X(488);
						player.setESlot4Y(510);
						player.setNESlot4X(552);
						player.setNESlot4Y(510);
						stage = END;
					} else {
						player.setESlot4X(494);
						player.setESlot4Y(510);
						player.setNESlot4X(558);
						player.setNESlot4Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot2X(488);
						player.setESlot2Y(510);
						player.setNESlot2X(552);
						player.setNESlot2Y(510);
						stage = END;
					} else {
						player.setESlot2X(494);
						player.setESlot2Y(510);
						player.setNESlot2X(558);
						player.setNESlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 49) {
					if (player.getCitTypeY() == 592) {
						player.setESlot1X(488);
						player.setESlot1Y(510);
						player.setNESlot1X(552);
						player.setNESlot1Y(510);
						stage = END;
					} else {
						player.setESlot1X(494);
						player.setESlot1Y(510);
						player.setNESlot1X(558);
						player.setNESlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 73 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot2X(622);
						player.setNSlot2Y(505);
						player.setNNSlot2X(687);
						player.setNNSlot2Y(505);
						stage = END;
					} else {
						player.setNSlot2X(622);
						player.setNSlot2Y(505);
						player.setNNSlot2X(686);
						player.setNNSlot2Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 41 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot1X(494);
						player.setNSlot1Y(510);
						player.setNNSlot1X(552);
						player.setNNSlot1Y(510);
						stage = END;
					} else {
						player.setNSlot1X(622);
						player.setNSlot1Y(505);
						player.setNNSlot1X(686);
						player.setNNSlot1Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 62) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot1X(494);
						player.setWSlot1Y(510);
						player.setNWSlot1X(552);
						player.setNWSlot1Y(510);
						stage = END;
					} else {
						player.setWSlot1X(494);
						player.setWSlot1Y(510);
						player.setNWSlot1X(558);
						player.setNWSlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot2X(494);
						player.setWSlot2Y(510);
						player.setNWSlot2X(552);
						player.setNWSlot2Y(510);
						stage = END;
					} else {
						player.setWSlot2X(494);
						player.setWSlot2Y(510);
						player.setNWSlot2X(558);
						player.setNWSlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 30 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot3X(494);
						player.setWSlot3Y(510);
						player.setNWSlot3X(552);
						player.setNWSlot3Y(510);
						stage = END;
					} else {
						player.setWSlot3X(494);
						player.setWSlot3Y(510);
						player.setNWSlot3X(558);
						player.setNWSlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 33 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot4X(494);
						player.setWSlot4Y(510);
						player.setNWSlot4X(552);
						player.setNWSlot4Y(510);
						stage = END;
					} else {
						player.setWSlot4X(494);
						player.setWSlot4Y(510);
						player.setNWSlot4X(558);
						player.setNWSlot4Y(510);
						stage = END;
					}
				}
			} else if (id == OPTION_2) {
				if (player.getCitTypeY() == 592)
					if (player.getInventory().containsItem(995, 5000000))
						player.getInventory().deleteItem(995, 5000000);
					else {
						sendDialogue("You don't have the coins required to do this in your inventory.");
						stage = END2;
						return;
					}
				else if (player.getInventory().containsItem(995, 10000000))
					player.getInventory().deleteItem(995, 10000000);
				else {
					sendDialogue("You don't have the coins required to do this in your inventory.");
					stage = END2;
					return;
				}
				if (object.getX() == x + 78 && object.getY() == y + 54) {
					if (player.getCitTypeY() == 592) {
						player.setEastTypeX(496);
						player.setEastTypeY(510);
						player.setEastNightTypeX(560);
						player.setEastNightTypeY(510);
						stage = END;
					} else {
						player.setEastTypeX(502);
						player.setEastTypeY(510);
						player.setEastNightTypeX(566);
						player.setEastNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 49 && object.getY() == y + 41) {
					if (player.getCitTypeY() == 592) {
						player.setWestTypeX(496);
						player.setWestTypeY(510);
						player.setWestNightTypeX(560);
						player.setWestNightTypeY(510);
						stage = END;
					} else {
						player.setWestTypeX(502);
						player.setWestTypeY(510);
						player.setWestNightTypeX(566);
						player.setWestNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 86 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot3X(496);
						player.setESlot3Y(510);
						player.setNESlot3X(560);
						player.setNESlot3Y(510);
						stage = END;
					} else {
						player.setESlot3X(502);
						player.setESlot3Y(510);
						player.setNESlot3X(566);
						player.setNESlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 94 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setESlot4X(496);
						player.setESlot4Y(510);
						player.setNESlot4X(560);
						player.setNESlot4Y(510);
						stage = END;
					} else {
						player.setESlot4X(502);
						player.setESlot4Y(510);
						player.setNESlot4X(566);
						player.setNESlot4Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot2X(496);
						player.setESlot2Y(510);
						player.setNESlot2X(560);
						player.setNESlot2Y(510);
						stage = END;
					} else {
						player.setESlot2X(502);
						player.setESlot2Y(510);
						player.setNESlot2X(566);
						player.setNESlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 49) {
					if (player.getCitTypeY() == 592) {
						player.setESlot1X(496);
						player.setESlot1Y(510);
						player.setNESlot1X(560);
						player.setNESlot1Y(510);
						stage = END;
					} else {
						player.setESlot1X(502);
						player.setESlot1Y(510);
						player.setNESlot1X(566);
						player.setNESlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 73 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot2X(502);
						player.setNSlot2Y(510);
						player.setNNSlot2X(560);
						player.setNNSlot2Y(510);
						stage = END;
					} else {
						player.setNSlot2X(630);
						player.setNSlot2Y(505);
						player.setNNSlot2X(694);
						player.setNNSlot2Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 41 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot1X(502);
						player.setNSlot1Y(510);
						player.setNNSlot1X(560);
						player.setNNSlot1Y(510);
						stage = END;
					} else {
						player.setNSlot1X(630);
						player.setNSlot1Y(505);
						player.setNNSlot1X(694);
						player.setNNSlot1Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 62) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot1X(502);
						player.setWSlot1Y(510);
						player.setNWSlot1X(560);
						player.setNWSlot1Y(510);
						stage = END;
					} else {
						player.setWSlot1X(502);
						player.setWSlot1Y(510);
						player.setNWSlot1X(566);
						player.setNWSlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot2X(502);
						player.setWSlot2Y(510);
						player.setNWSlot2X(560);
						player.setNWSlot2Y(510);
						stage = END;
					} else {
						player.setWSlot2X(502);
						player.setWSlot2Y(510);
						player.setNWSlot2X(566);
						player.setNWSlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 30 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot3X(502);
						player.setWSlot3Y(510);
						player.setNWSlot3X(560);
						player.setNWSlot3Y(510);
						stage = END;
					} else {
						player.setWSlot3X(502);
						player.setWSlot3Y(510);
						player.setNWSlot3X(566);
						player.setNWSlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 33 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot4X(502);
						player.setWSlot4Y(510);
						player.setNWSlot4X(560);
						player.setNWSlot4Y(510);
						stage = END;
					} else {
						player.setWSlot4X(502);
						player.setWSlot4Y(510);
						player.setNWSlot4X(566);
						player.setNWSlot4Y(510);
						stage = END;
					}
				}
			} else if (id == OPTION_3) {
				if (player.getCitTypeY() == 592)
					if (player.getInventory().containsItem(995, 5000000))
						player.getInventory().deleteItem(995, 5000000);
					else {
						sendDialogue("You don't have the coins required to do this in your inventory.");
						stage = END2;
						return;
					}
				else if (player.getInventory().containsItem(995, 10000000))
					player.getInventory().deleteItem(995, 10000000);
				else {
					sendDialogue("You don't have the coins required to do this in your inventory.");
					stage = END2;
					return;
				}
				if (object.getX() == x + 78 && object.getY() == y + 54) {
					if (player.getCitTypeY() == 592) {
						player.setEastTypeX(504);
						player.setEastTypeY(510);
						player.setEastNightTypeX(568);
						player.setEastNightTypeY(510);
						stage = END;
					} else {
						player.setEastTypeX(510);
						player.setEastTypeY(510);
						player.setEastNightTypeX(574);
						player.setEastNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 49 && object.getY() == y + 41) {
					if (player.getCitTypeY() == 592) {
						player.setWestTypeX(504);
						player.setWestTypeY(510);
						player.setWestNightTypeX(568);
						player.setWestNightTypeY(510);
						stage = END;
					} else {
						player.setWestTypeX(510);
						player.setWestTypeY(510);
						player.setWestNightTypeX(574);
						player.setWestNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 86 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot3X(510);
						player.setESlot3Y(510);
						player.setNESlot3X(574);
						player.setNESlot3Y(510);
						stage = END;
					} else {
						player.setESlot3X(510);
						player.setESlot3Y(510);
						player.setNESlot3X(574);
						player.setNESlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 94 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setESlot4X(510);
						player.setESlot4Y(510);
						player.setNESlot4X(574);
						player.setNESlot4Y(510);
						stage = END;
					} else {
						player.setESlot4X(510);
						player.setESlot4Y(510);
						player.setNESlot4X(574);
						player.setNESlot4Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot2X(510);
						player.setESlot2Y(510);
						player.setNESlot2X(574);
						player.setNESlot2Y(510);
						stage = END;
					} else {
						player.setESlot2X(510);
						player.setESlot2Y(510);
						player.setNESlot2X(574);
						player.setNESlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 49) {
					if (player.getCitTypeY() == 592) {
						player.setESlot1X(510);
						player.setESlot1Y(510);
						player.setNESlot1X(574);
						player.setNESlot1Y(510);
						stage = END;
					} else {
						player.setESlot1X(510);
						player.setESlot1Y(510);
						player.setNESlot1X(574);
						player.setNESlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 73 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot1X(638);
						player.setNSlot1Y(505);
						player.setNNSlot1X(703);
						player.setNNSlot1Y(505);
						stage = END;
					} else {
						player.setNSlot2X(638);
						player.setNSlot2Y(505);
						player.setNNSlot2X(702);
						player.setNNSlot2Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 41 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot1X(638);
						player.setNSlot1Y(505);
						player.setNNSlot1X(702);
						player.setNNSlot1Y(505);
						stage = END;
					} else {
						player.setNSlot1X(638);
						player.setNSlot1Y(505);
						player.setNNSlot1X(702);
						player.setNNSlot1Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 62) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot1X(510);
						player.setWSlot1Y(510);
						player.setNWSlot1X(574);
						player.setNWSlot1Y(510);
						stage = END;
					} else {
						player.setWSlot1X(510);
						player.setWSlot1Y(510);
						player.setNWSlot1X(574);
						player.setNWSlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot2X(510);
						player.setWSlot2Y(510);
						player.setNWSlot2X(574);
						player.setNWSlot2Y(510);
						stage = END;
					} else {
						player.setWSlot2X(510);
						player.setWSlot2Y(510);
						player.setNWSlot2X(574);
						player.setNWSlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 30 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot3X(510);
						player.setWSlot3Y(510);
						player.setNWSlot3X(574);
						player.setNWSlot3Y(510);
						stage = END;
					} else {
						player.setWSlot3X(510);
						player.setWSlot3Y(510);
						player.setNWSlot3X(574);
						player.setNWSlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 33 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot4X(510);
						player.setWSlot4Y(510);
						player.setNWSlot4X(574);
						player.setNWSlot4Y(510);
						stage = END;
					} else {
						player.setWSlot4X(510);
						player.setWSlot4Y(510);
						player.setNWSlot4X(574);
						player.setNWSlot4Y(510);
						stage = END;
					}
				}
			} else if (id == OPTION_4) {
				if (player.getCitTypeY() == 592)
					if (player.getInventory().containsItem(995, 15000000))
						player.getInventory().deleteItem(995, 15000000);
					else {
						sendDialogue("You don't have the coins required to do this in your inventory.");
						stage = END2;
						return;
					}
				else if (player.getInventory().containsItem(995, 25000000))
					player.getInventory().deleteItem(995, 25000000);
				else {
					sendDialogue("You don't have the coins required to do this in your inventory.");
					stage = END2;
					return;
				}
				if (object.getX() == x + 78 && object.getY() == y + 54) {
					if (player.getCitTypeY() == 592) {
						player.setEastTypeX(518);
						player.setEastTypeY(510);
						player.setEastNightTypeX(576);
						player.setEastNightTypeY(510);
						stage = END;
					} else {
						player.setEastTypeX(518);
						player.setEastTypeY(510);
						player.setEastNightTypeX(582);
						player.setEastNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 49 && object.getY() == y + 41) {
					if (player.getCitTypeY() == 592) {
						player.setWestTypeX(518);
						player.setWestTypeY(510);
						player.setWestNightTypeX(576);
						player.setWestNightTypeY(510);
						stage = END;
					} else {
						player.setWestTypeX(518);
						player.setWestTypeY(510);
						player.setWestNightTypeX(582);
						player.setWestNightTypeY(510);
						stage = END;
					}
				} else if (object.getX() == x + 86 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot3X(518);
						player.setESlot3Y(510);
						player.setNESlot3X(576);
						player.setNESlot3Y(510);
						stage = END;
					} else {
						player.setESlot3X(518);
						player.setESlot3Y(510);
						player.setNESlot3X(582);
						player.setNESlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 94 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setESlot4X(518);
						player.setESlot4Y(510);
						player.setNESlot4X(576);
						player.setNESlot4Y(510);
						stage = END;
					} else {
						player.setESlot4X(518);
						player.setESlot4Y(510);
						player.setNESlot4X(582);
						player.setNESlot4Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot2X(518);
						player.setESlot2Y(510);
						player.setNESlot2X(576);
						player.setNESlot2Y(510);
						stage = END;
					} else {
						player.setESlot2X(518);
						player.setESlot2Y(510);
						player.setNESlot2X(582);
						player.setNESlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 49) {
					if (player.getCitTypeY() == 592) {
						player.setESlot1X(518);
						player.setESlot1Y(510);
						player.setNESlot1X(576);
						player.setNESlot1Y(510);
						stage = END;
					} else {
						player.setESlot1X(518);
						player.setESlot1Y(510);
						player.setNESlot1X(582);
						player.setNESlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 73 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot1X(518);
						player.setNSlot1Y(510);
						player.setNNSlot1X(576);
						player.setNNSlot1Y(510);
						stage = END;
					} else {
						player.setNSlot2X(646);
						player.setNSlot2Y(505);
						player.setNNSlot2X(710);
						player.setNNSlot2Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 41 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot1X(646);
						player.setNSlot1Y(505);
						player.setNNSlot1X(710);
						player.setNNSlot1Y(505);
						stage = END;
					} else {
						player.setNSlot1X(646);
						player.setNSlot1Y(505);
						player.setNNSlot1X(711);
						player.setNNSlot1Y(505);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 62) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot1X(518);
						player.setWSlot1Y(510);
						player.setNWSlot1X(576);
						player.setNWSlot1Y(510);
						stage = END;
					} else {
						player.setWSlot1X(518);
						player.setWSlot1Y(510);
						player.setNWSlot1X(582);
						player.setNWSlot1Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot2X(518);
						player.setWSlot2Y(510);
						player.setNWSlot2X(576);
						player.setNWSlot2Y(510);
						stage = END;
					} else {
						player.setWSlot2X(518);
						player.setWSlot2Y(510);
						player.setNWSlot2X(582);
						player.setNWSlot2Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 30 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot3X(518);
						player.setWSlot3Y(510);
						player.setNWSlot3X(576);
						player.setNWSlot3Y(510);
						stage = END;
					} else {
						player.setWSlot3X(518);
						player.setWSlot3Y(510);
						player.setNWSlot3X(582);
						player.setNWSlot3Y(510);
						stage = END;
					}
				} else if (object.getX() == x + 33 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot4X(518);
						player.setWSlot4Y(510);
						player.setNWSlot4X(576);
						player.setNWSlot4Y(510);
						stage = END;
					} else {
						player.setWSlot4X(518);
						player.setWSlot4Y(510);
						player.setNWSlot4X(582);
						player.setNWSlot4Y(510);
						stage = END;
					}
				}
			} else if (id == OPTION_5) {
				if (player.getCitTypeY() == 592)
					if (player.getInventory().containsItem(995, 5000000))
						player.getInventory().deleteItem(995, 5000000);
					else {
						sendDialogue("You don't have the coins required to do this in your inventory.");
						stage = END2;
						return;
					}
				else if (player.getInventory().containsItem(995, 10000000))
					player.getInventory().deleteItem(995, 10000000);
				else {
					sendDialogue("You don't have the coins required to do this in your inventory.");
					stage = END2;
					return;
				}
				if (object.getX() == x + 78 && object.getY() == y + 54) {
					if (player.getCitTypeY() == 592) {
						player.setEastTypeX(488);
						player.setEastTypeY(506);
						player.setEastNightTypeX(552);
						player.setEastNightTypeY(506);
						stage = END;
					} else {
						player.setEastTypeX(494);
						player.setEastTypeY(506);
						player.setEastNightTypeX(558);
						player.setEastNightTypeY(506);
						stage = END;
					}
				} else if (object.getX() == x + 49 && object.getY() == y + 41) {
					if (player.getCitTypeY() == 592) {
						player.setWestTypeX(488);
						player.setWestTypeY(506);
						player.setWestNightTypeX(552);
						player.setWestNightTypeY(506);
						stage = END;
					} else {
						player.setWestTypeX(494);
						player.setWestTypeY(506);
						player.setWestNightTypeX(558);
						player.setWestNightTypeY(506);
						stage = END;
					}
				} else if (object.getX() == x + 86 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot3X(488);
						player.setESlot3Y(506);
						player.setNESlot3X(552);
						player.setNESlot3Y(506);
						stage = END;
					} else {
						player.setESlot3X(494);
						player.setESlot3Y(506);
						player.setNESlot3X(558);
						player.setNESlot3Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 94 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setESlot4X(488);
						player.setESlot4Y(506);
						player.setNESlot4X(552);
						player.setNESlot4Y(506);
						stage = END;
					} else {
						player.setESlot4X(494);
						player.setESlot4Y(506);
						player.setNESlot4X(558);
						player.setNESlot4Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setESlot2X(488);
						player.setESlot2Y(506);
						player.setNESlot2X(552);
						player.setNESlot2Y(506);
						stage = END;
					} else {
						player.setESlot2X(494);
						player.setESlot2Y(506);
						player.setNESlot2X(558);
						player.setNESlot2Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 102 && object.getY() == y + 49) {
					if (player.getCitTypeY() == 592) {
						player.setESlot1X(488);
						player.setESlot1Y(506);
						player.setNESlot1X(552);
						player.setNESlot1Y(506);
						stage = END;
					} else {
						player.setESlot1X(494);
						player.setESlot1Y(506);
						player.setNESlot1X(558);
						player.setNESlot1Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 73 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot2X(488);
						player.setNSlot2Y(506);
						player.setNNSlot2X(552);
						player.setNNSlot2Y(506);
						stage = END;
					} else {
						player.setNSlot2X(618);
						player.setNSlot2Y(505);
						player.setNNSlot2X(682);
						player.setNNSlot2Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 41 && object.getY() == y + 102) {
					if (player.getCitTypeY() == 592) {
						player.setNSlot1X(488);
						player.setNSlot1Y(506);
						player.setNNSlot1X(558);
						player.setNNSlot1Y(506);
						stage = END;
					} else {
						player.setNSlot1X(618);
						player.setNSlot1Y(505);
						player.setNNSlot1X(682);
						player.setNNSlot1Y(605);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 62) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot1X(488);
						player.setWSlot1Y(506);
						player.setNWSlot1X(558);
						player.setNWSlot1Y(506);
						stage = END;
					} else {
						player.setWSlot1X(494);
						player.setWSlot1Y(506);
						player.setNWSlot1X(558);
						player.setNWSlot1Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 25 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot2X(488);
						player.setWSlot2Y(506);
						player.setNWSlot2X(558);
						player.setNWSlot2Y(506);
						stage = END;
					} else {
						player.setWSlot2X(494);
						player.setWSlot2Y(506);
						player.setNWSlot2X(558);
						player.setNWSlot2Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 30 && object.getY() == y + 73) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot3X(488);
						player.setWSlot3Y(506);
						player.setNWSlot3X(558);
						player.setNWSlot3Y(506);
						stage = END;
					} else {
						player.setWSlot3X(494);
						player.setWSlot3Y(506);
						player.setNWSlot3X(558);
						player.setNWSlot3Y(506);
						stage = END;
					}
				} else if (object.getX() == x + 33 && object.getY() == y + 97) {
					if (player.getCitTypeY() == 592) {
						player.setWSlot4X(488);
						player.setWSlot4Y(506);
						player.setNWSlot4X(558);
						player.setNWSlot4Y(506);
						stage = END;
					} else {
						player.setWSlot4X(494);
						player.setWSlot4Y(506);
						player.setNWSlot4X(558);
						player.setNWSlot4Y(506);
						stage = END;
					}
				}
			}
		}
		if (stage == END) {
			end();
			final Calendar c = Calendar.getInstance();
			Calendar now = Calendar.getInstance(TimeZone
					.getTimeZone("US/Central"));
			c.setTimeZone(now.getTimeZone());
			if (c.get(Calendar.HOUR) >= 8
					&& c.get(Calendar.AM_PM) == Calendar.PM
					|| c.get(Calendar.HOUR) >= 1 && c.get(Calendar.HOUR) < 6
					&& c.get(Calendar.AM_PM) == Calendar.AM)
				Citadel.addAllNightPlots(player);
			else
				Citadel.addAllDayPlots(player);
			Citadel cit = new Citadel();
			for (WorldObject object : cit.removeObjects)
				World.destroySpawnedObject(object, false);
			cit.removeObjects.clear();
			for (Player players : World.getPlayers()) {
				if (players.withinDistance(player, 30)) {
					players.setForceNextMapLoadRefresh(true);
					players.loadMapRegions();
				}
			}
		} else if(stage == END2) {
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
