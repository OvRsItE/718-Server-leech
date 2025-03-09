package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.Constants;
import com.rs.game.world.entity.player.content.DisplayNameAction;
import com.rs.game.world.entity.player.content.Donator;
import com.rs.game.world.entity.player.content.PlayerLook;
import com.rs.game.world.entity.player.content.PlayerSupport;
import com.rs.game.world.entity.player.content.SerenityOptions;
import com.rs.game.world.entity.player.content.TicketSystem;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;
import com.rs.utility.ShopsHandler;



public class Noticeboard extends Dialogue {

	public Noticeboard() {
	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("What you would like to do?", "View my character settings",
				Constants.SERVER_NAME + " Options", "Open Donator Panel", "Loyalty Programme", "Next page");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Character Settings",
						"Username Options", "Yell Options",
						"Title Options",
						"I want to send a help ticket to adminstrator.", "Next page");
				stage = 2;
			} else if (componentId == OPTION_2) {

				sendOptionsDialogue(Constants.SERVER_NAME + " Game Options", "Login screen options.",
						"Experience Options", "Animation Options", "Player Support", "Leave noticeboard.");
				stage = 5;
			}
		 else if (componentId == OPTION_3) {
					Donator.sendBoard(player);
					player.getInterfaceManager().closeChatBoxInterface();
		} else if (componentId == OPTION_4) {
			player.getInterfaceManager().closeChatBoxInterface();
			}
		else if (componentId == OPTION_5) {
			sendOptionsDialogue("What you would like to do?",
					"Hiscores: Check player's stats.", "Create a Event.",
					"Customise Teleports", "Drop Log", "Leave Noticeboard.");
			stage = 12;
			}
		}
		else if (stage == 12) {
			if (componentId == OPTION_1) {
			player.getTemporaryAttributtes().put("highscores", true);
            player.getPackets().sendRunScript(109, "Enter Player's Name:");	
			} else if (componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_3) {
							sendOptionsDialogue("Choose a teleport animation.",
					"Assassin", "Gnome",
					"Demon", "Pony", "SuperJump");
			stage = 120;
			} else if (componentId == OPTION_4) {
			player.getTemporaryAttributtes().put("drop_log", true);
            player.getPackets().sendRunScript(109, "Enter Player's Name:");
			} else if (componentId == OPTION_5) {
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("You have left the noticeboard.");
			}
		}	else if (stage == 120) {
					if (componentId == OPTION_1) {
						player.Ass = true;
						player.Gnome = false;
						player.Demon = false;
						player.Pony = false;
						player.SuperJump = false;
						player.sm("<col=FF0000>Game will display assassin teleport animation");
						player.getInterfaceManager().closeChatBoxInterface();
					} else if (componentId == OPTION_2) { 
						player.Ass = false;
						player.Gnome = true;
						player.Demon = false;
						player.Pony = false;
						player.SuperJump = false;
						player.sm("<col=FF0000>Game will display gnome teleport animation");
						player.getInterfaceManager().closeChatBoxInterface();
					}  else if (componentId == OPTION_3) {
						player.Ass = false;
						player.Gnome = false;
						player.Demon = true;
						player.Pony = false;
						player.SuperJump = false;
						player.sm("<col=FF0000>Game will display demon teleport animation");
						player.getInterfaceManager().closeChatBoxInterface();
					}	else if (componentId == OPTION_4) {
						player.Ass = false;
						player.Gnome = false;
						player.Demon = false;
						player.Pony = true;
						player.SuperJump = false;
						player.sm("<col=FF0000>Game will display pony teleport animation");
						player.getInterfaceManager().closeChatBoxInterface();
					}else if (componentId == OPTION_5) {
						player.Ass = false;
						player.Gnome = false;
						player.Demon = false;
						player.Pony = false;
						player.SuperJump = true;
						player.sm("<col=FF0000>Game will display super jump teleport animation");
						player.getInterfaceManager().closeChatBoxInterface();
					}
				} else if (stage == 5) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Login Settings",
						"I want to see the news upon login.", "No, I don't want to see news upon login.",
						"Let me know when new player joins.", "Don't let me know when new player joins.", "Leave Noticeboard.");
				stage = 6;
			} else if (componentId == OPTION_2) {
				sendOptionsDialogue("Experience Settings",
						"Toggle / Untoggle Experience gaining.", "Empty Slot.",
						"Empty Slot.", "Nevermind.");
				stage = 7;
			} else if (componentId == OPTION_3) {
				sendOptionsDialogue("Animation Options",
						"Cooking Animations.",
						"Mining Animations.",
						"Fletching Animations.",
						"Smithing Animations.",
						"Leave Noticeboard.");
				stage = 13;
			} else if (componentId == OPTION_4) {
				sendOptionsDialogue("Player Support",
						"I want to read a book about Noticeboard controls.",
					 "Leave noticeboard.");
				stage = 14;
			} else if (componentId == OPTION_5) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		}
			else if (stage == 14) {
				if (componentId == OPTION_1) {
					PlayerSupport.sendSupport(player);
					player.getInterfaceManager().closeChatBoxInterface();
				} else if (componentId == OPTION_2) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("You have left the noticeboard.");
				}
				
		}	
			else if (stage == 13) {
				if (componentId == OPTION_1) { 
					sendOptionsDialogue("Cooking Animations",
							"I want to use the new cooking animations.", "I want to use the old mining animations.",
						 "Leave noticeboard.");
					stage = 18;
				} else if (componentId == OPTION_2) {
					sendOptionsDialogue("Mining Animations",
							"I want to use new mining animations.", "I want to use old mining animations.",
						 "Leave noticeboard.");
					stage = 19;
				} else if (componentId == OPTION_3) {
					sendOptionsDialogue("Fletching Animations",
							"I want to use new fletching animations.", "I want to use old fletching animations.",
						 "Leave noticeboard.");
					stage = 20;
				} else if (componentId == OPTION_4) {
					sendOptionsDialogue("Smithing Animations",
							"I want to use new Smithing animations.", "I want to use old Smithing animations.",
						 "Leave noticeboard.");
					stage = 21;
				} else if (componentId == OPTION_5) {
					player.sm("You have left the noticeboard.");
					player.getInterfaceManager().closeChatBoxInterface();
				}	
			}
		
			else if (stage == 20) {
				if (componentId == OPTION_1) {
					player.KarateFletching = true;
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("<col=FF0000>Game will display now new fletching animations.");
				} else if (componentId == OPTION_2) { 
					player.KarateFletching = false;
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("<col=FF0000>Game will display you old fletching animations.");
				}  else if (componentId == OPTION_3) {
					player.sm("You have left the noticeboard.");
					player.getInterfaceManager().closeChatBoxInterface();
				}	
			}				else if (stage == 21) {
				if (componentId == OPTION_1) {
					player.IronFistSmithing = true;
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("<col=FF0000>Game will display now new Smithing animations.");
				} else if (componentId == OPTION_2) { 
					player.IronFistSmithing = false;
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("<col=FF0000>Game will display you old Smithing animations.");
				}  else if (componentId == OPTION_3) {
					player.sm("You have left the noticeboard.");
					player.getInterfaceManager().closeChatBoxInterface();
				}	
			}
				else if (stage == 19) {
					if (componentId == OPTION_1) {
						player.ChillBlastMining = true;
						player.getInterfaceManager().closeChatBoxInterface();
						player.sm("<col=FF0000>Game will display now new mining animations.");
					} else if (componentId == OPTION_2) { 
						player.ChillBlastMining = false;
						player.getInterfaceManager().closeChatBoxInterface();
						player.sm("<col=FF0000>Game will display you old mining animations.");
					}  else if (componentId == OPTION_3) {
						player.sm("You have left the noticeboard.");
						player.getInterfaceManager().closeChatBoxInterface();
					}	
				}	
				else if (stage == 18) {
					if (componentId == OPTION_1) {
						player.SamuraiCooking = true;
						player.getAppearence().setTitle(10956);
						player.getInterfaceManager().closeChatBoxInterface();
						player.sm("<col=FF0000>Game will display now new cooking animations.");
					} else if (componentId == OPTION_2) {
						player.SamuraiCooking = false;
						player.getAppearence().setTitle(0);
						player.getInterfaceManager().closeChatBoxInterface();
						player.sm("<col=FF0000>Game will display you old cooking animations.");
					}  else if (componentId == OPTION_3) {
						player.sm("You have left the noticeboard.");
						player.getInterfaceManager().closeChatBoxInterface();
					}	
		}
				else if (stage == 7) {
			if (componentId == OPTION_1) {
				player.setXpLocked(player.isXpLocked() ? false : true);
			player.getPackets().sendGameMessage(
					"You have "
							+ (player.isXpLocked() ? "UNLOCKED" : "LOCKED")
							+ " your xp.");
			} else if (componentId == OPTION_2) {
				/*empty*/player.getInterfaceManager().closeChatBoxInterface();
			}  else if (componentId == OPTION_3) {
				/*empty*/player.getInterfaceManager().closeChatBoxInterface();
			}	else if (componentId == OPTION_4) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
		} else if (stage == 6) {
			if (componentId == OPTION_1) {
				SerenityOptions.setNewsPositive(player);
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_2) {
				player.getInterfaceManager().closeChatBoxInterface();
				SerenityOptions.setNewsNegative(player);
			} else if (componentId == OPTION_3) {
				SerenityOptions.TogglePlayerInfo(player);
				player.getInterfaceManager().closeChatBoxInterface();
			}
			 else if (componentId == OPTION_4) {
				 SerenityOptions.unTogglePlayerInfo(player);
					player.getInterfaceManager().closeChatBoxInterface();
				}
			 else if (componentId == OPTION_5) {
					player.getInterfaceManager().closeChatBoxInterface();
				}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
			 sendOptionsDialogue("Username Options", "Change Name", "Reset Name", "Hex Color",  "Shade Color",  "Rest Both");
		             stage = 105;
			} else if (componentId == OPTION_2) {
					 				  sendOptionsDialogue("Yell Options", "Yell Prefix", "Yell Color", "Yell shade");
		             stage = 107;
			} else if (componentId == OPTION_3) {
			sendOptionsDialogue("Title Options", "Change Title", "Hex Color",  "Shade Color",  "Rest Both");
		             stage = 106;
			} else if (componentId == OPTION_4) {
				TicketSystem.requestTicket(player);
				player.getInterfaceManager().closeChatBoxInterface();
				player.sm("You have sent a ticket to the staff, please wait.");
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Character Settings", "I would like to change my hair.",
						"I would like to change my clothes.", "I'd like to change my skin colour or gender.", "Close Noticeboard.");
				stage = 3;
			}
		}else  if (stage == 105) {
			        if (componentId == OPTION_1) {
				DisplayNameAction.ProcessChange(player);	
	        }
				   if (componentId == OPTION_2) {
				DisplayNameAction.RemoveDisplay(player);
	        }
	        if (componentId == OPTION_3) {
	            player.getTemporaryAttributtes().put("hex_color1", Boolean.TRUE);
	            player.getPackets().sendRunScript(109, new Object[] { "Type your Hex ID Here, for more color codes.. go to Google.com and search HTML Color codes"});
	            end();
	        }
	        if (componentId == OPTION_4) {
	            player.getTemporaryAttributtes().put("Shad_color1", Boolean.TRUE);
	            player.getPackets().sendRunScript(109, new Object[] { "Type your Hex ID Here, for more color codes.. go to Google.com and search HTML Color codes"});
	            end();
	        }
	        if (componentId == OPTION_5) {
	            player.shadCode1 = "";
	            player.hexCode1 = "";
	            end();
	        }
	    }else  if (stage == 106) {
			        if (componentId == OPTION_1) {
					player.getPackets().sendRunScript(109, new Object[] { "Please enter the title you would like." });
					player.getTemporaryAttributtes().put("customtitle", Boolean.TRUE);	
	        }
				   if (componentId == OPTION_2) {
					            player.getTemporaryAttributtes().put("titlecolor", Boolean.TRUE);
	            player.getPackets().sendRunScript(109, new Object[] { "Type your Hex ID Here, for more color codes.. go to Google.com and search HTML Color codes"});
	            end();
	        }
	        if (componentId == OPTION_3) {
	            player.getTemporaryAttributtes().put("titleshade", Boolean.TRUE);
	            player.getPackets().sendRunScript(109, new Object[] { "Type your Hex ID Here, for more color codes.. go to Google.com and search HTML Color codes"});
	            end();
	        }
	        if (componentId == OPTION_4) {
				player.titleShading = "";
	            player.hex = "";
	            end();
	        }
	    }else  if (stage == 107) {
			        if (componentId == OPTION_1) {
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellprefix", Boolean.TRUE);	
			}else if (componentId == OPTION_2) {
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);	
			}else if (componentId == OPTION_3) {
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellshade", Boolean.TRUE);	
			}
	    }  else if (stage == 3) {
			if (componentId == OPTION_1) {
				PlayerLook.openHairdresserSalon(player);
				player.sm("Make sure you don't wear ANY items!");
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_2) {
				player.sm("Make sure you don't wear ANY items!");
				PlayerLook.openThessaliasMakeOver(player);
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_3) {
				player.sm("Make sure you don't wear ANY items!");
				PlayerLook.openMageMakeOver(player);
				player.getInterfaceManager().closeChatBoxInterface();
			} else if (componentId == OPTION_4) {
				player.sm("You have closed the noticeboard.");
			}
		}
	}

	@Override
	public void finish() {
	}

}
