
package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.utility.ShopsHandler;

public class PrestigeOne extends Dialogue {

	private int npcId = 2253;

	@Override
	public void start() {
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
				"Hello "+player.getDisplayName()+" Would you like to learn about the Prestige system?"}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendOptionsDialogue("Would you like to prestige?", "Yes", "No.", "I'd like to check my Prestige points please.", "I would like to recieve my prestige title please.", "I would like to access the prestige shop please.");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				if (player.getEquipment().wearingArmour()) {
					player.getPackets().sendGameMessage("<col=ff0000>You must remove your amour before you can prestige.");
					end();
				} else {
					sendNPCDialogue(npcId, 9827, "The prestige system allows you to reset ALL of you're skills, you will be rewarded with one prestige point for every time you prestige, you will need every 99 in order to prestige, For every prestige you will gain one new user title. ");
					stage = 2;
				}
			}
			else if (componentId == OPTION_2) {
				end();
			}
			else if (componentId == OPTION_3) {
				player.getPackets().sendGameMessage("I Currently have : "+player.prestigeNumber+" prestige points.");
				end();
			}
			else if (componentId == OPTION_4) {
				if (player.prestigeNumber == 1) {
					player.getAppearence().setTitle(1024);
				} else if (player.prestigeNumber == 2) {
					player.getAppearence().setTitle(1024);
				} else if (player.prestigeNumber == 3) {
					player.getAppearence().setTitle(1024);
				} else if (player.prestigeNumber == 4) {
					player.getAppearence().setTitle(1024);
				} else if (player.prestigeNumber == 5) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 6) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 7) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 8) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 9) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 10) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 11) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 12) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 13) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 14) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 15) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 16) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 17) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 18) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 19) {
					player.getAppearence().setTitle(1024);
				}else if (player.prestigeNumber == 20) {
					player.getAppearence().setTitle(1347);
				} else {
					player.getPackets().sendGameMessage("You need to have prestiged to gain a prestige title.");
				}
				end();
			}
			else if (componentId == OPTION_5) {
				if (player.prestigeNumber >= 1) {
					sendOptionsDialogue("Prestige Shops", "<shad=00FF00>Prestige 1", "<shad=FD3EDA>Prestige 5", "<shad=FFCD05>Prestige 10", "<shad=0066CC>Prestige 15", "<shad=0066CC>Prestige 20");
					stage = 22;
				} else {
					player.getPackets().sendGameMessage("You need to have prestiged to access this.");
					end();
				}
			}
		} else if (stage == 2) {
			sendPlayerDialogue(9827, "Wow sounds amazing!");
			stage = 3;
		} else if (stage == 3) {
			sendNPCDialogue(npcId, 9827, "That's because it is! When you prestige for the first time ALL of your stats will be reset, but once you gain all your 99 stats back it will only reset your combat stats, such as strength and mage, it will not reset your skills.");
			stage = 4;
		} else if (stage == 4) {
			sendOptionsDialogue("Would you like to prestige?", "Yes!", "No thank you.");
			stage = 5;
		} else if (stage == 5) {
			if (componentId == OPTION_1) {
				player.prestige();
				if(!player.isPrestige1()) {
					player.setNextForceTalk(new ForceTalk("I must get all 99's in order to prestige!"));
				} else if (player.isPrestige1()) {
					if (player.prestigeNumber == 0) {
						player.setCompletedPrestigeOne();
					} else if (player.prestigeNumber == 20) {
						player.getPackets().sendGameMessage("You have completed all the prestiges.");
					} else {
						player.setCompletedPrestige2();
					}
				}
			}
			else if (componentId == OPTION_2) {
				end();
			}
			end();
		}else if (stage == 22) {
			if(componentId == OPTION_1) {
				if (player.prestigeNumber >= 1) {
					ShopsHandler.openShop(player, 45);
					end();
				} else {
					player.getPackets().sendGameMessage("You need to be prestige 1 to access this shop.");
					end();
				}
			}
			if(componentId == OPTION_2) {
				if (player.prestigeNumber >= 5) {
					ShopsHandler.openShop(player, 44);
					end();
				} else {
					player.getPackets().sendGameMessage("You need to be prestige 5 to access this shop.");
					end();
				}
			}
			if(componentId == OPTION_3) {
				if (player.prestigeNumber >= 10) {
					ShopsHandler.openShop(player, 43);
					end();
				} else {
					player.getPackets().sendGameMessage("You need to be prestige 10 to access this shop.");
					end();
				}
			}
			if(componentId == OPTION_4) {
				if (player.prestigeNumber >= 15) {
					ShopsHandler.openShop(player, 42);
					end();
				} else {
					player.getPackets().sendGameMessage("You need to be prestige 15 to access this shop.");
					end();
				}
			}
			if(componentId == OPTION_5) {
				if (player.prestigeNumber >= 15) {
					ShopsHandler.openShop(player, 41);
					end();
				} else {
					player.getPackets().sendGameMessage("You need to be prestige 20 to access this shop.");
					end();
				}        }
		}
	}

	@Override
	public void finish() {

	}
}