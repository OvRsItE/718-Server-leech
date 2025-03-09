package com.rs.network.decoders.handlers;

import com.rs.Constants;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemRequirements;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.item.ItemManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.world.entity.npc.others.GraveStone;
import com.rs.game.world.entity.player.*;
import com.rs.game.world.entity.player.actions.FightPitsViewingOrb;
import com.rs.game.world.entity.player.actions.Rest;
import com.rs.game.world.entity.player.actions.skilling.Smithing.ForgingInterface;
import com.rs.game.world.entity.player.content.collection.ItemCollectionManager;
import com.rs.game.world.entity.player.content.interfaces.spin.MysteryBox;
import com.rs.game.world.entity.player.actions.skilling.Summoning;
import com.rs.game.world.entity.player.content.*;
import com.rs.game.world.entity.player.content.CustomisedShop.MyShopItem;
import com.rs.game.world.entity.player.content.Notes.Note;
import com.rs.game.world.entity.player.content.activities.CastleWars;
import com.rs.game.world.entity.player.content.activities.CommendationExchange;
import com.rs.game.world.entity.player.content.activities.Crucible;
import com.rs.game.world.entity.player.content.activities.PuroPuro;
import com.rs.game.world.entity.player.content.activities.dueling.DuelControler;
import com.rs.game.world.entity.player.content.clues.CasketHandler;
import com.rs.game.world.entity.player.content.dialogue.impl.LevelUp;
import com.rs.game.world.entity.player.content.dialogue.impl.Transportation;
import com.rs.game.world.entity.player.content.presetsmanager.PresetManager;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.construction.House;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonRewardShop;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.smithing.DungeoneeringSmithing;
import com.rs.game.world.entity.player.content.skills.magic.Enchanting;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.content.social.clanchat.ClansManager;
import com.rs.game.world.entity.player.content.trident.impl.TridentOfTheSeas;
import com.rs.game.world.entity.player.content.trident.impl.TridentOfTheSwamp;
import com.rs.game.world.entity.player.controller.impl.*;
import com.rs.game.world.entity.player.teleports.TeleportManager;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.network.decoders.WorldPacketsDecoder;
import com.rs.network.io.InputStream;
import com.rs.utility.*;

import java.util.List;
import java.util.TimerTask;

public class ButtonHandler {

	public static void handleButtons(final Player player, InputStream stream, int packetId) {

		if (packetId == 67 || packetId == 14)
			stream.readByte(); //unused since new shift drop system

		int interfaceHash = stream.readIntV2();

		int interfaceId = interfaceHash >> 16;

		if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
			return;
		}

		if (!World.containsLobbyPlayer(player.getUsername())) {
			if (player.isDead() || !player.getInterfaceManager().containsInterface(interfaceId)) {
				return;
			}
		}
		final int componentId = interfaceHash - (interfaceId << 16);
		if (componentId != 65535 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
			return;
		}
		final int slotId2 = stream.readUnsignedShort128();
		final int slotId = stream.readUnsignedShortLE128();

		if (player.getRights() == 2) {
			System.out.println("InterfaceId " + interfaceId + ", componentId " + componentId + ", slotId " + slotId
					+ ", slotId2 " + slotId2 + ", PacketId: " + packetId);
		}
		if (!player.getControlerManager().processButtonClick(interfaceId, componentId, slotId, packetId)) {
			return;
		}

		if (!player.getControlerManager().processButtonClick(interfaceId, componentId, slotId, slotId2, packetId)) {
			return;
		}

		if (packetId == 62) {
			RessourceBox.handleGroundPick(player, stream);
		}

		switch (interfaceId) {
			case 1266:
				if (player.getAttributes().get("editting_own_store") != null) {
					if (componentId == 0) {
						switch (packetId) {
						case 14: // sell
							player.getAttributes().put("sending_mystore_item", slotId);
							player.getPackets().sendRunScript(108,
									new Object[] { "How much do you want to sell this for?" });
							break;
						}
					}
					return;
				}
				break;
			case 1161:
				player.getMysteryBoxes().handleButtons(interfaceId, componentId, slotId, packetId);
				break;
			case 1171:
				if (player.getAttributes().get("editting_own_store") != null) {
					List<MyShopItem> items = player.getCustomisedShop().getShopItems();
					if (slotId >= items.size()) {
						player.sendMessage("That item no longer exists in the shop!");
						return;
					}
					MyShopItem item = items.get(slotId);
					if (componentId == 7) {
						if (packetId == 14) {
							player.getCustomisedShop().removeItem(item, true);
							player.getInventory().addItem(item.getItem());
							player.getCustomisedShop().sendOwnShop();
						}
					}
					return;
				}
				Player target = (Player) player.getAttributes().get("viewing_player_shop");
				if (target != null) {
					List<MyShopItem> items = target.getCustomisedShop().getShopItems();
					if (slotId >= items.size()) {
						player.sendMessage("That item no longer exists in the shop!");
						return;
					}
					MyShopItem item = items.get(slotId);
					if (componentId == 7) {
						if (packetId == 14) {
							player.sendMessage(item.getItem().getName() + " costs " + Misc.format(item.getPrice())
									+ " coins in " + target.getDisplayName() + "'s shop.");
						} else {
							if (!World.containsPlayer(target.getUsername())) {
								player.sm("That player is not online to receive the money.");
								return;
							}
							if (player.takeMoney(item.getPrice())) {
								player.getInventory().addItem(item.getItem());
								target.getCustomisedShop().removeItem(item, false);
								target.getCustomisedShop().open(player);
							} else {
								player.sendMessage("You do not have enough coins to buy " + item.getItem().getName()
										+ " from " + target.getDisplayName() + "'s store!");
							}
						}
					}
				}
				break;
		}
		if (interfaceId == ItemCollectionManager.INTERFACE_ID) {
			player.getItemCollectionManager().handleClick(componentId);
			return;
		}
		if (interfaceId == StarterInterface.INTERFACE_ID) {
			player.getStarterInterface().handleClick(componentId);
			return;
		}
		if (player.getWorkbench().handleButtonClick(interfaceId, componentId))
			return;
		if (player.getAchievements().handleButtonClick(interfaceId, componentId))
			return;
		if (player.getSettingsinterface().handleButtonClick(interfaceId, componentId))
			return;
		if (interfaceId == 297) {
			SandwichLady.getInstance().handleButtons(player, interfaceId, componentId);
		}
		if (interfaceId == 1368) {
			CasketHandler.handleButtons(player, interfaceId, componentId);
		}
		if (interfaceId == 403) {
			SawMill.handleButtons(player, componentId, packetId);
		}
		if (interfaceId == 1183) {
			if (componentId == 9) {
				Item item = player.getInventory().getItem(slotId);
				player.getInventory().deleteItem(slotId2, item);
				player.getCharges().degradeCompletly(item);
				player.getPackets().sendSound(4500, 0, 1);
			}
		}
		if (interfaceId == 1011) {
			CommendationExchange.handleButtonOptions(player, componentId);
		}
		if (interfaceId == 17) {
			if (componentId == 28) {
				sendItemsKeptOnDeath(player, player.getVarsManager().getBitValue(9226) == 0);
			}
		}

		if (interfaceId == 940) {
			DungeonRewardShop.handleButtons(player, componentId, slotId, packetId);
			return;
		}

//		if(interfaceId == TeleportInterface.INTERFACE_ID){
//			player.getGlobalTeleport().handleButtons(componentId);
//		}

		if (interfaceId == TeleportManager.TELEPORT_INTER) {
			if (player.getTeleportManager().chooseCatagory(componentId))
				return;
			if (player.getTeleportManager().clickTeleportButton(componentId))
				return;
		}


		if (interfaceId == 1072) {
			ArtisanWorkshop.handleButtons(player, componentId);
		}
		// player.getpackets().Sendicomponenttext(506, 0, "Realityx panel");

		if (interfaceId == 548 && componentId == 194 || interfaceId == 746 && componentId == 204) {
			player.getMoneyPouch().switchPouch();
		}
		if (interfaceId == 746 && componentId == 207 || interfaceId == 548 && componentId == 159) {

			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				player.getMoneyPouch().switchPouch();
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				player.getMoneyPouch().withdrawPouch();
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				player.getMoneyPouch().examinePouch();
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				if (player.getInterfaceManager().containsScreenInter() && !player.isInDung()
						|| player.isLocked() && !player.isInDung()) {
					player.getPackets()
					.sendGameMessage("Please finish what you're doing before opening the price checker.");
					return;
				}
				if (player.isInDung()) {
					player.sm("You cannot do this action in here.");
					return;
				} else {
					player.stopAll();
					player.getPriceCheckManager().openPriceCheck();
				}
			}
		}
		if (interfaceId == 548 || interfaceId == 746) {
			if (interfaceId == 548 && componentId == 148 || interfaceId == 746 && componentId == 199) {
				if (player.getInterfaceManager().containsScreenInter()
						|| player.getInterfaceManager().containsInventoryInter()) {
					player.sendMessage("Please finish what you're doing before opening the "
							+ (player.getControlerManager().getControler() instanceof DungeonController
							? "Daemonheim map."
							: "world map."));
					return;
				}
				if (player.getControlerManager().getControler() instanceof DungeonController) {
					player.getDungeoneeringManager().getParty().getDungeon().openMap(player);
					return;
				}
				// world map open
				player.getPackets().sendWindowsPane(755, 0);
				player.animate(new Animation(840)); // Reading worldmap
				int posHash = player.getX() << 14 | player.getY();
				player.getPackets().sendGlobalConfig(622, posHash); // map open
				// center
				// pos
				player.getPackets().sendGlobalConfig(674, posHash); // player
				// position
				// Quests
			} else if (interfaceId == 548 && componentId == 17 || interfaceId == 746 && componentId == 54) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getSkills().switchXPDisplay();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getSkills().switchXPPopup();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getSkills().setupXPCounter();
				}
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				if (player.inDung == true) {
					player.sm("This action cannot be done right now.");
					return;
				} else if (player.getInterfaceManager().containsScreenInter()) {
					player.getPackets()
					.sendGameMessage("Please finish what you're doing before opening the price checker.");
					return;
				}

				player.stopAll();
				player.getPriceCheckManager().openPriceCheck();
			}
		}
		if (interfaceId == 934) {
			System.out.println("packetId" + packetId);
			int amount = packetId == 61 ? 1 : packetId == 64 ? 5 : packetId == 4 ? 10 : packetId == 52 ? 28 : -1;
			if (amount == -1) {
				player.getTemporaryAttributtes().put("dungsmithingc", componentId);
				player.getTemporaryAttributtes().put("dungsmithingi", slotId2);
				player.getPackets().sendRunScript(108, "How many would you like to smith?");
			} else
				player.getActionManager().setAction(new DungeoneeringSmithing(
						(int) player.getTemporaryAttributtes().get("dgmetal"), componentId, amount, slotId2));
			return;
		}
		/**
		 * dung party interface
		 */
		if (interfaceId == 939) {
			if (componentId >= 59 && componentId <= 72) {
				int playerIndex = (componentId - 59) / 3;
				if ((componentId & 0x3) != 0)
					player.getDungeoneeringManager().pressOption(playerIndex,
							packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
									: packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET ? 1 : 2);
				else
					player.getDungeoneeringManager().pressOption(playerIndex, 3);
			} else if (componentId == 45)
				player.getDungeoneeringManager().formParty();
			else if (componentId == 33 || componentId == 36)
				player.getDungeoneeringManager().checkLeaveParty();
			else if (componentId == 43)
				player.getDungeoneeringManager().invite();
			else if (componentId == 102)
				player.getDungeoneeringManager().changeComplexity();
			else if (componentId == 108)
				player.getDungeoneeringManager().changeFloor();
			else if (componentId == 87)
				player.getDungeoneeringManager().openResetProgress();
			else if (componentId == 94)
				player.getDungeoneeringManager().switchGuideMode();
			else if (componentId == 112) {
				player.getPackets().sendGlobalConfig(234, 0);
				// player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen()
				// ? 114 : 174, 34);
			}
		}
		/**
		 * dung interface
		 */
		else if (interfaceId == 949) {
			if (componentId == 65)
				player.getDungeoneeringManager().acceptInvite();
			else if (componentId == 61 || componentId == 63)
				player.closeInterfaces();
		} else if (interfaceId == 938) {
			switch (componentId) {
				case 56:
					player.getDungeoneeringManager().selectComplexity(1);
					break;
				case 61:
					player.getDungeoneeringManager().selectComplexity(2);
					break;
				case 66:
					player.getDungeoneeringManager().selectComplexity(3);
					break;
				case 71:
					player.getDungeoneeringManager().selectComplexity(4);
					break;
				case 76:
					player.getDungeoneeringManager().selectComplexity(5);
					break;
				case 81:
					player.getDungeoneeringManager().selectComplexity(6);
					break;
			}
			if (componentId == 39)
				player.getDungeoneeringManager().confirmComplexity();
		} else if (interfaceId == 947) {
			if (componentId >= 48 && componentId <= 107)
				player.getDungeoneeringManager().selectFloor((componentId - 48) + 1);
			else if (componentId == 766)
				player.getDungeoneeringManager().confirmFloor();
		}
		else if (interfaceId == 813) {
			switch (componentId) {
			case 52: // Bandos Start bossing
				player.closeInterfaces();
				player.getDialogueManager().startDialogue("Transportation", "Bandos", new Position(2916, 3746, 0),
						"Armadyl", new Position(2916, 3746, 0), "Saradomin", new Position(2916, 3746, 0), "Zamorak",
						new Position(2916, 3746, 0));
				break;
			case 70: // rfd
				player.closeInterfaces();
				player.getControlerManager().startControler("RecipeforDisaster", 1);
				break;
			case 71: // Livid Farms
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2111, 3940, 0));
				player.getPackets().sendGameMessage(
						"Take a Orb, Click on the patches for plants, Bunch 27 and deposit them for XP.");
				break;
			case 53: // Corp
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2966, 4383, 2));
				player.getControlerManager().startControler("CorpBeastControler");
				player.getPackets()
				.sendGameMessage("This is a DANGEROUS Minigame, You will lose ALL your items upon death...");
				player.closeInterfaces();
				break;
			case 54: // Merc Mage
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(214, 5515, 0));
				player.getControlerManager().startControler("CorpBeastControler");
				player.getPackets()
				.sendGameMessage("This is a DANGEROUS Minigame, You will lose ALL your items upon death...");
				break;
			case 55: // Lucien
				player.closeInterfaces();
				break;
			case 56: // Blink
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(198, 5511, 0));
				player.getControlerManager().startControler("CorpBeastControler");
				player.getPackets()
				.sendGameMessage("This is a DANGEROUS Minigame, You will lose ALL your items upon death...");
				break;
			case 60: // dag prime
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2517, 4646, 0));
				break;
			case 57: // yklag
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(246, 5509, 0));
				player.getControlerManager().startControler("CorpBeastControler");
				player.getPackets()
				.sendGameMessage("This is a DANGEROUS Minigame, You will lose ALL your items upon death...");
				break;
			case 58: // nex
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2905, 5203, 0));
				break;
			case 59: // qbd
				player.closeInterfaces();
				player.getControlerManager().startControler("QueenBlackDragonControler");
				break;
			case 61: // glacors
				if (player.getSkills().getLevel(Skills.SLAYER) < 99) {
					player.getPackets().sendGameMessage("You need an slayer level of 95 to use this teleport.");
					player.closeInterfaces();
					return;
				}
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(4183, 5729, 0));
				break;
			case 43: // Start Skilling Tab Fishing
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2616, 3845, 0));
				break;
			case 44: // Mining
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3300, 3312, 0));
				break;
			case 45: // Hunter
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2526, 2916, 0));
				player.getControlerManager().startControler("Falconry");
				break;
			case 46: // Woodcutting
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3161, 3223, 0));
				break;
			case 47: // RuneCrafting

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2598, 3163, 0));

				break;
			case 48: // Summoning

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2209, 5343, 0));

				break;
			case 49: // Agility

				player.closeInterfaces();
				player.getDialogueManager().startDialogue("Transportation", "Gnome Agility Course",
						new Position(2470, 3436, 0), "Barbarian Agility Course", new Position(2552, 3563, 0),
						"Wilderness Agility Course", new Position(2998, 3932, 0), "Home",
						new Position(2847, 5071, 0));

				break;
			case 50: // Cooking

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3033, 4957, 0));

				break;
			case 51: // Thieving

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2660, 3306, 0));

				break;
			case 37: // start minigames PC

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2656, 2593, 0));
				player.getPackets().sendGameMessage(
						"Go near the portals and kill the monsters for tickets. Exchange them for void armours!.");

				break;
			case 38: // ClanW

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2994, 9679, 0));

				break;
			case 39: // CastleW

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(CastleWars.LOBBY));

				break;
			case 40: // Barrows

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3565, 3289, 0));

				break;
			case 41: // Fight Caves

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(4610, 5130, 0));

				break;
			case 42: // zombies

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(4744, 5172, 0));

				break;
			case 64: // Start Misc Teles TrollZone

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2441, 3159, 0));
				player.getPackets().sendGameMessage("Derek is the General of Troll Zone.");

				break;
			case 65: // Chill

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(2315, 4559, 0));

				break;
			case 66: // Revs

				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3077, 10058, 0));

				break;
			case 67: // Train

				player.closeInterfaces();
				player.TrainTele();

				break;
			case 69: // polypore
				if (player.getSkills().getLevel(Skills.SLAYER) < 95) {
					player.getPackets().sendGameMessage("You need an slayer level of 95 to use this teleport.");
					player.closeInterfaces();
					return;
				}
				player.closeInterfaces();
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(4652, 5404, 0));

				break;
			case 68: // Bork

				player.closeInterfaces();
				player.getControlerManager().startControler("BorkControler", 0, null);

				break;
			}
		} else if (interfaceId == 1156) {
			if (componentId == 88) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2998, 3119, 0));
			} else if (componentId == 115) {
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3823, 4767, 0));
			} else if (componentId == 139) {
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(215, 5384, 0));
			} else if (componentId == 112) {
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(234, 5515, 0));
			} else if (componentId == 118) {
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3107, 5537, 0));
			} else if (componentId == 136) {
				Magic.sendCustomTeleportSpell(player, 0, 0, new Position(3233, 2789, 0));
			} else if (componentId == 124) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2151, 5536, 3));
			} else if (componentId == 130) {

			} else if (componentId == 127) {

			} else if (componentId == 145) {

			} else if (componentId == 148) {

			} else if (componentId == 121) {

			} else if (componentId == 133) {

			} else if (componentId == 142) {

			} else if (componentId == 151) {

			} else if (componentId == 154) {

			} else if (componentId == 169) {

			} else if (componentId == 158) {

			} else if (componentId == 162) {

			} else if (componentId == 166) {

			} else if (componentId == 172) {

			} else if (componentId == 320) {

			}


		} else if (interfaceId == 825) {
			Player player2 = player;
			switch (componentId) {
			case 93: // StartVoteShop
				if (player.votep >= 1) {
					player.getInventory().addItem(24155, 1);
					player.getPackets().sendGameMessage("You bought one!");
					player.votep -= 1;
				} else {
					player.getPackets().sendGameMessage("You need atleast 1 point to buy one! Try ::vote");
				}
				break;
			case 97: // hc
				if (player.votep >= 60) {
					player.getInventory().addItem(11755, 1);
					player.getInventory().addItem(16685, 1);
					player.getInventory().addItem(16707, 1);
					player.getInventory().addItem(16289, 1);
					player.getInventory().addItem(16356, 1);
					player.getInventory().addItem(16971, 1);
					player.votep -= 60;
					player.getPackets().sendGameMessage("You bought the Set!");
				} else {
					player.getPackets().sendGameMessage("You need atleast 60 points to buy one!");
				}
				break;
			case 101: // Jericho
				if (player.votep >= 40) {
					player.getInventory().addItem(26192, 1);
					player.votep -= 40;
					player.getPackets().sendGameMessage("You bought the Whip!");
				} else {
					player.getPackets().sendGameMessage("You need atleast 40 points to buy one!");
				}
				break;
			case 105: // barrages
				if (player.votep >= 8) {
					player.getInventory().addItem(18744, 1);
					player.getInventory().addItem(18745, 1);
					player.getInventory().addItem(18746, 1);
					player.getInventory().addItem(18747, 1);
					player.votep -= 8;
					player.getPackets().sendGameMessage("You bought the Halo's and the Shield!");
				} else {
					player.getPackets().sendGameMessage("You need atleast 8 points to buy one!");
				}
				break;
			case 109: // bolas
				if (player.votep >= 12) {
					player.getInventory().addItem(26314, 50);
					player.votep -= 18;
					player.getPackets().sendGameMessage("You bought the Soups!");
				} else {
					player.getPackets().sendGameMessage("You need atleast 12 points to buy one!");
				}
				break;
			case 113: // potsc
				if (player.votep >= 18) {
					player.getInventory().addItem(18705, 1);
					player.getInventory().addItem(18706, 1);
					player.getInventory().addItem(18707, 1);
					player.getInventory().addItem(18708, 1);
					player.getInventory().addItem(18709, 1);
					player.votep -= 18;
					player.getPackets().sendGameMessage("You bought the armours!");
				} else {
					player.getPackets().sendGameMessage("You need atleast 18 points to buy one!");
				}
				break;
			case 117: // rockz
				if (player.votep >= 5) {
					player.getInventory().addItem(25472, 1);
					player.getPackets().sendGameMessage("You bought one!");
					player.votep -= 5;
				} else {
					player.getPackets().sendGameMessage("You need atleast 5 points to buy one!");
				}
				break;
			case 121: // potsr
				if (player.votep >= 10) {
					player.getInventory().addItem(10933, 1);
					player.getInventory().addItem(10939, 1);
					player.getInventory().addItem(10940, 1);
					player.getInventory().addItem(10941, 1);
					player.getPackets().sendGameMessage("You bought one!");
					player.votep -= 10;
				} else {
					player.getPackets().sendGameMessage("You need atleast 10 points to buy one!");
				}
				break;
			case 125: // jc
				if (player.votep >= 60) {
					player.setDonator(true);
					player.getPackets().sendGameMessage("You bought one!");
					player.votep -= 60;
				} else {
					player.getPackets().sendGameMessage("You need atleast 60 points to buy one!");
				}
				break;
			case 129: // sj
				if (player.votep >= 10) {
					player.getInventory().addItem(24428, 1);
					player.getInventory().addItem(24427, 1);
					player.getInventory().addItem(24429, 1);
					player.getInventory().addItem(24430, 1);
					player.getPackets().sendGameMessage("You bought one!");
					player.votep -= 10;
				} else {
					player.getPackets().sendGameMessage("You need atleast 10 points to buy one!");
				}
				break;
			case 133: // Comm
				if (player.votep >= 10) {
					player.getInventory().addItem(21485, 1);
					player.getInventory().addItem(21484, 1);
					player.getInventory().addItem(21487, 1);
					player.getInventory().addItem(21486, 1);
					player.getPackets().sendGameMessage("You bought one!");
					player.votep -= 10;
				} else {
					player.getPackets().sendGameMessage("You need atleast 10 points to buy one!");
				}
				break;
			case 136: // warc
				if (player.votep >= 2) {
					player.getInventory().addItem(27343, 1);
					player.votep -= 2;
					player.getPackets().sendGameMessage("You bought one!");
				} else {
					player.getPackets().sendGameMessage("You need atleast 2 points to buy one!");
				}
				break;
			}
		}

		// Dark Invasion
		else if (interfaceId == 267) {
			switch (componentId) {

			case 24:
				if (player.getPlayerData().getInvasionPoints() >= 1) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 1);
					player.getSkills().addXp(Skills.ATTACK, 5);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased attack xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 49:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getSkills().addXp(Skills.ATTACK, 25);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased attack xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 56:
				if (player.getPlayerData().getInvasionPoints() >= 10) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 10);
					player.getSkills().addXp(Skills.ATTACK, 50);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased attack xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;

			case 35:
				if (player.getPlayerData().getInvasionPoints() >= 1) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 1);
					player.getSkills().addXp(Skills.STRENGTH, 5);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased strength xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 50:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getSkills().addXp(Skills.STRENGTH, 25);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased strength xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 57:
				if (player.getPlayerData().getInvasionPoints() >= 10) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 10);
					player.getSkills().addXp(Skills.STRENGTH, 50);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased strength xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * DEFENCE XP
				 */
			case 36:
				if (player.getPlayerData().getInvasionPoints() >= 1) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 1);
					player.getSkills().addXp(Skills.DEFENCE, 5);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased defence xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 51:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getSkills().addXp(Skills.DEFENCE, 25);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased defence xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 58:
				if (player.getPlayerData().getInvasionPoints() >= 10) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 10);
					player.getSkills().addXp(Skills.DEFENCE, 50);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased defence xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * RANGE XP
				 */
			case 37:
				if (player.getPlayerData().getInvasionPoints() >= 1) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 1);
					player.getSkills().addXp(Skills.RANGE, 5);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased range xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 52:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getSkills().addXp(Skills.RANGE, 25);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased range xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
			case 59:
				if (player.getPlayerData().getInvasionPoints() >= 10) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 10);
					player.getSkills().addXp(Skills.RANGE, 50);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased range xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * MAGIC XP
				 */
			case 38:
				if (player.getPlayerData().getInvasionPoints() >= 1) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 1);
					player.getSkills().addXp(Skills.MAGIC, 5);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased magic xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;

			case 53:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getSkills().addXp(Skills.MAGIC, 25);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased magic xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;

			case 60:
				if (player.getPlayerData().getInvasionPoints() >= 10) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 10);
					player.getSkills().addXp(Skills.MAGIC, 50);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased magic xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * HITPOINTS XP
				 */
			case 39:
				if (player.getPlayerData().getInvasionPoints() >= 1) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 1);
					player.getSkills().addXp(Skills.HITPOINTS, 5);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased hitpoints(hp) xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;

			case 54:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getSkills().addXp(Skills.HITPOINTS, 25);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased hitpoints(hp) xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;

			case 61:
				if (player.getPlayerData().getInvasionPoints() >= 10) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 10);
					player.getSkills().addXp(Skills.HITPOINTS, 50);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased hitpoints(hp) xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * PRAYER XP
				 */
			case 40:
				if (player.getPlayerData().getInvasionPoints() >= 1) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 1);
					player.getSkills().addXp(Skills.PRAYER, 5);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased prayer xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;

			case 55:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getSkills().addXp(Skills.PRAYER, 25);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased prayer xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;

			case 62:
				if (player.getPlayerData().getInvasionPoints() >= 10) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 10);
					player.getSkills().addXp(Skills.PRAYER, 50);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased prayer xp from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * HERBS
				 */
			case 45:
				if (player.getPlayerData().getInvasionPoints() >= 15) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 15);
					int[] herbNoted = { 995, 960 }; // CHANGE THIS LATER ON TODO
					int[] amount = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
					int herbReward = herbNoted[Utils.random(herbNoted.length - 1)];
					int herbAmount = amount[Utils.random(amount.length - 1)];
					player.getBank().addItem(herbReward, herbAmount, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased some herbs from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * ORE
				 */
			case 46:
				if (player.getPlayerData().getInvasionPoints() >= 15) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 15);
					int[] oreNoted = { 995, 960 }; // CHANGE THIS LATER ON TODO
					int[] amounts = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
					int oreReward = oreNoted[Utils.random(oreNoted.length - 1)];
					int oreAmount = amounts[Utils.random(amounts.length - 1)];
					player.getBank().addItem(oreReward, oreAmount, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased some ores from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * SEEDS
				 */
			case 48:
				if (player.getPlayerData().getInvasionPoints() >= 15) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 15);
					int[] seeds = { 995, 960 }; // CHANGE THIS LATER ON TODO
					int[] amountOfSeed = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
					int seedsReward = seeds[Utils.random(seeds.length - 1)];
					int seedAmount = amountOfSeed[Utils.random(amountOfSeed.length - 1)];
					player.getBank().addItem(seedsReward, seedAmount, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased some seeds from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID KNIGHT MACE
				 */
			case 41:
				if (player.getPlayerData().getInvasionPoints() >= 500) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 500);
					player.getBank().addItem(8841, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Knight Mace from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID KNIGHT TOP
				 */
			case 42:
				if (player.getPlayerData().getInvasionPoints() >= 500) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 500);
					player.getBank().addItem(8839, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Knight Top from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID KNIGHT ROBE
				 */
			case 43:
				if (player.getPlayerData().getInvasionPoints() >= 500) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 500);
					player.getBank().addItem(8840, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Knight Robe from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID KNIGHT GLOVES
				 */
			case 44:
				if (player.getPlayerData().getInvasionPoints() >= 500) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 500);
					player.getBank().addItem(8842, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Knight Gloves from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID MAGE HELM
				 */
			case 67:
				if (player.getPlayerData().getInvasionPoints() >= 500) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 500);
					player.getBank().addItem(11663, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Mage Helm from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID RANGE HELM
				 */
			case 68:
				if (player.getPlayerData().getInvasionPoints() >= 500) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 500);
					player.getBank().addItem(11664, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Range Helm from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID MELEE HELM
				 */
			case 69:
				if (player.getPlayerData().getInvasionPoints() >= 500) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 500);
					player.getBank().addItem(11665, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Melee Helm from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/*
				 * VOID KNIGHT COMMENDATION
				 */
			case 70:
				if (player.getPlayerData().getInvasionPoints() >= 5) {
					player.getPlayerData().setInvasionPoints(player.getPlayerData().getInvasionPoints() - 5);
					player.getBank().addItem(19642, 1, true);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You have purchased a Void Knight Commendation from the Invasion shop and now have "
									+ player.getPlayerData().getInvasionPoints() + " points.");
				} else {
					player.getPackets().sendGameMessage("Sorry, you don't have enough points to purchase this.");
				}
				break;
				/* END OF SHOP LIST */
			}
		}

		else if (interfaceId == 1019) {
			if (componentId == 16) {
				player.getPackets().sendOpenURL(Constants.WEBSITE_LINK);
			}
			if (componentId == 18) {
				if (player.isLocked() || player.getControlerManager().getControler() instanceof DungeonController) {
					player.getPackets().sendGameMessage("You can't do that here.");
				} else {
					TicketSystem.requestTicket(player);
				}
			}
		}

		if (interfaceId == 205) {
			// AchievementSystem.handleButtons(player, componentId);
		}
		if (interfaceId == 930) {
//			if (componentId == 25 && packetId == 14) {
//				AchievementSystem.displayEasyAchievments(player);
//			} else if (componentId == 25 && packetId == 67) {
//				AchievementSystem.displayMedAchievments(player);
//			} else if (componentId == 25 && packetId == 5) {
//				AchievementSystem.displayHardAchievments(player);
//			}
			player.sm("Achievements coming soon.");
		}

		if (interfaceId == 1362) {
			if (componentId == 12 && packetId == 14) {
				player.getInterfaceManager().sendTaskSystem();
			} else if (componentId == 12 && packetId == 67) {
				AchievementSystem.displayMedAchievments(player);
			} else if (componentId == 12 && packetId == 5) {
				AchievementSystem.displayHardAchievments(player);
			}
		}

		if (interfaceId == 497) {
			if (componentId == 4) {
				player.closeInterfaces();
			}
		}

		if (interfaceId == 95) { // Sailing
			if (componentId == 23) {
				player.getPackets().sendGameMessage("You sail to the port of Tyras.");
				player.setNextPosition(new Position(2268, 3244, 0));
			} else if (componentId == 33) {
				player.getPackets().sendGameMessage("You sail to the port of Ooglog.");
				player.setNextPosition(new Position(2623, 2857, 0));
			} else if (componentId == 29) {
				player.getPackets().sendGameMessage("You sail to the port of Khazard.");
				player.setNextPosition(new Position(2623, 2857, 0));
			} else if (componentId == 28) {
				player.getPackets().sendGameMessage("You sail to the port of Brimhaven.");
				player.setNextPosition(new Position(2760, 3238, 0));
			} else if (componentId == 30) {
				player.getPackets().sendGameMessage("You sail to the port of Sarim.");
				player.setNextPosition(new Position(3038, 3192, 0));
			} else if (componentId == 27) {
				player.getPackets().sendGameMessage("You sail to the port of Karamja.");
				player.setNextPosition(new Position(2954, 3158, 0));
			} else if (componentId == 26) {
				player.getPackets().sendGameMessage("You sail to the port of the Shipyard.");
				player.setNextPosition(new Position(3001, 3032, 0));
			} else if (componentId == 25) {
				player.getPackets().sendGameMessage("You sail to the port of Catherby.");
				player.setNextPosition(new Position(2792, 3414, 0));
			} else if (componentId == 32) {
				if (player.DS < 7) {
					player.getPackets().sendGameMessage("You must have completed Dragon Slayer to sail to Crandor.");
				} else {
					player.getPackets().sendGameMessage("You sail to the port of Ooglog.");
					player.setNextPosition(new Position(2623, 2857, 0));
				}
			} else if (componentId == 24) {
				player.getPackets().sendGameMessage("You sail to the port of Phasmatys.");
				player.setNextPosition(new Position(3702, 3503, 0));
			} else if (componentId == 31) {
				player.getPackets().sendGameMessage("You sail to the port of Mos Le'Harmless.");
				player.setNextPosition(new Position(3671, 2931, 0));
			}

		}


		else if (interfaceId == 506) {
			if (!player.finishedStarter)
				return;
			if (componentId == 2) {
				player.getPackets().sendOpenURL("");
			} else if (componentId == 4) {
				player.getDialogueManager().startDialogue("Altarstele", true);
			} else if (componentId == 6) {
				player.getDialogueManager().startDialogue("SkillingTeleports", true);
			} else if (componentId == 8) {
				player.getDialogueManager().startDialogue("MTHighLevelBosses", true);
			} else if (componentId == 10) {
				player.getDialogueManager().startDialogue("MinigamesTele", true);
			} else if (componentId == 12) {
				player.getDialogueManager().startDialogue("TrainingTeleports", true);
			}
			else if (componentId == 14) {
				player.getDialogueManager().startDialogue("MTSlayerDungeons", true);
			}
		}

		else if (interfaceId == 583) {
			if (player.teleports == 1) {
				if (componentId == 50) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3175, 3317, 0));
				} else if (componentId == 51) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2325, 3795, 0));
				} else if (componentId == 52) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2393, 3857, 0));
				} else if (componentId == 53) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3436, 3466, 0));
				} else if (componentId == 54) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3170, 2981, 0));
				} else if (componentId == 55) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2527, 3202, 0));
				} else if (componentId == 56) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3081, 3421, 0));
				} else if (componentId == 57) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3074, 3456, 0));
				} else if (componentId == 58) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3198, 3177, 0));
				} else if (componentId == 59) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2855, 3168, 0));
				} else if (componentId == 60) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3237, 3459, 0));
				} else if (componentId == 61) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3083, 3272, 0));
				} else if (componentId == 62) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3095, 3468, 0));
				} else if (componentId == 63) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3246, 3198, 0));
				} else if (componentId == 64) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2846, 3497, 0));
				} else if (componentId == 65) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3576, 9927, 0));
				} else if (componentId == 66) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2875, 3578, 0));
				} else if (componentId == 67) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2928, 9844, 0));
				} else if (componentId == 68) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2517, 3356, 0));
				} else if (componentId == 69) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2462, 3048, 0));
				} else if (componentId == 70) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2911, 3933, 0));
				} else if (componentId == 71) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2421, 4690, 0));
				} else if (componentId == 72) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3653, 5115, 0));
				} else if (componentId == 73) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2845, 3170, 0));
				} else if (componentId == 74) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3026, 9953, 1));
				} else if (componentId == 75) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1518, 4705, 0));
				} else if (componentId == 76) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3548, 3512, 0));
				} else if (componentId == 77) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3398, 2758, 0));
				} else if (componentId == 78) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3312, 3453, 0));
				} else if (componentId == 79) {
					player.teleports = 2;
					player.getPackets().closeInterface(583);
					if (player.teleports == 2) {
						player.getInterfaceManager().sendInterface(583);
						player.getPackets().sendIComponentText(583, 0, "Commands");
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
						player.getPackets().sendIComponentText(583, 60,
								"<col=7CFC00>Polypore dungeon (bottom level). ");
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
						player.getPackets().sendIComponentText(583, 71, "<col=8bf906>Tirannwn Elf Camp. ");
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
			} else if (player.teleports == 2) {
				if (componentId == 50) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2787, 2786, 0));
				} else if (componentId == 51) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(1576, 4363, 0));
				} else if (componentId == 52) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2589, 9411, 0));
				} else if (componentId == 53) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3038, 5346, 0));
				} else if (componentId == 54) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3271, 4861, 0));
				} else if (componentId == 55) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3234, 3294, 0));
				} else if (componentId == 56) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3241, 10000, 0));
				} else if (componentId == 57) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3421, 3537, 0));
				} else if (componentId == 58) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2931, 3899, 0));
				} else if (componentId == 59) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2897, 4845, 0));
				} else if (componentId == 60) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4651, 5389, 0));
				} else if (componentId == 61) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(4183, 5726, 0));
				} else if (componentId == 62) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2952, 2954, 0));
				} else if (componentId == 63) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2892, 9784, 0));
				} else if (componentId == 64) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3079, 10058, 0));
				} else if (componentId == 65) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2843, 3535, 2));
				} else if (componentId == 66) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2900, 4449, 0));
				} else if (componentId == 67) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3450, 3728, 0));
				} else if (componentId == 68) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3056, 9553, 0));
				} else if (componentId == 69) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3508, 9493, 0));
				} else if (componentId == 70) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(3803, 2844, 0));
				} else if (componentId == 71) {
					Magic.sendNormalTeleportSpell(player, 0, 0, new Position(2203, 3253, 0));
				} else if (componentId == 72) {

				} else if (componentId == 73) {

				} else if (componentId == 74) {

				} else if (componentId == 75) {

				} else if (componentId == 76) {

				} else if (componentId == 77) {

				} else if (componentId == 78) {

				} else if (componentId == 79) {
					player.teleports = 1;
					player.getPackets().closeInterface(583);
					if (player.teleports == 1) {
						player.getInterfaceManager().sendInterface(583);
						player.getPackets().sendIComponentText(583, 0, "Commands");
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
					}

				}
			}
		}

		if (interfaceId == 1233) {
			News.handleButtons(player, componentId);
		}

		/*
		 * if (interfaceId == 1308) if (componentId == 65) if
		 * (player.getSlayerPoints() >= 400) {
		 * player.getSkills().addXp(Skills.SLAYER, 10);
		 * player.setSlayerPoints(player.getSlayerPoints() - 400); } if
		 * (componentId == 192) if (player.getSlayerPoints() >= 75) {
		 * player.getInventory().addItem(13281, 1);
		 * player.setSlayerPoints(player.getSlayerPoints() - 75); } if
		 * (componentId == 197) if (player.getSlayerPoints() >= 35) {
		 * player.getInventory().addItem(560, 250);
		 * player.getInventory().addItem(556, 750);
		 * player.setSlayerPoints(player.getSlayerPoints() - 35); } if
		 * (componentId == 205) if (player.getSlayerPoints() >= 35) {
		 * player.getInventory().addItem(13280, 250);
		 * player.setSlayerPoints(player.getSlayerPoints() - 35); } if
		 * (componentId == 213) if (player.getSlayerPoints() >= 35) {
		 * player.getInventory().addItem(4160, 250);
		 * player.setSlayerPoints(player.getSlayerPoints() - 35); }
		 */
		if (interfaceId == 1253 || interfaceId == 1252 || interfaceId == 1139) {
			if (!player.finishedStarter)
				return;
			player.getSquealOfFortune().processClick(packetId, interfaceId, componentId, slotId, slotId2);
		}
		if (interfaceId == 548 && componentId == 68) {
			player.getPackets().sendIComponentText(1139, 10, " " + player.getSpins() + " ");
		}
		if (interfaceId == 1264) {
			if (componentId == 0) {
				player.closeInterfaces();
				player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 0);
			}

		} else if (interfaceId == 34) {
			if (packetId == 55) {
				if (componentId == 9) {
					player.getNotes().remove(slotId);
				}
			} else if (packetId == 14) {
				if (componentId == 3) {
					player.getAttributes().put("entering_note", Boolean.TRUE);
					player.getPackets().sendInputLongTextScript("Enter a new note:");
				} else if (componentId == 8) {
					player.sendMessage("Please right click the note you wish to delete instead.");
				}
			} else if (packetId == 67) {
				Note note = player.getCurNotes().get(slotId);
				player.getAttributes().put("editing_note", Boolean.TRUE);
				player.getAttributes().put("noteToEdit", note);
				player.getPackets().sendInputLongTextScript("Enter a new note:");
			} else if (packetId == 5) {
				if (componentId == 9) {
					Note note = player.getCurNotes().get(slotId);
					int color = note.getColour() == 0 ? 1 : note.getColour() == 1 ? 2 : note.getColour() == 2 ? 3 : 0;
					note.setColour(color);
					player.getNotes().refresh(note);
				}
			}
		} else if (interfaceId == 182) {
			if (player.getInterfaceManager().containsInventoryInter()) {
				return;
			}
			if (componentId == 6 || componentId == 13)
			{
				if (!player.isFinished())
				{
					player.logout(componentId == 6);
					// Hiscores.saveHighScore(player);
				}
			}
		} else if (interfaceId == 164 || interfaceId == 161 || interfaceId == 378) {
			player.getSlayerManager().handleRewardButtons(interfaceId, componentId);
		} else if (interfaceId == 1310) {
			if (componentId == 0) {
				player.getSlayerManager().createSocialGroup(true);
				player.setCloseInterfacesEvent(null);
			}
			player.closeInterfaces();
		} else if (interfaceId == 1309) {
			if (componentId == 20) {
				player.getPackets().sendGameMessage(
						"Use your enchanted stone ring onto the player that you would like to invite.", true);
			} else if (componentId == 22) {
				Player p2 = player.getSlayerManager().getSocialPlayer();
				if (p2 == null) {
					player.getPackets().sendGameMessage("You have no slayer group, invite a player to start one.");
				} else {
					player.getPackets().sendGameMessage(
							"Your current slayer group consists of you and " + p2.getDisplayName() + ".");
				}
			} else if (componentId == 24) {
				player.getSlayerManager().resetSocialGroup(true);
			}
			player.closeInterfaces();
		} else if (interfaceId == 53) {
			if (componentId == 47) {
				player.setNextPosition(new Position(3232, 3252, 0));
				player.getPackets().sendGameMessage("You travel using your canoe.");
				player.closeInterfaces();
			} else if (componentId == 48) {
				player.setNextPosition(new Position(3202, 3343, 0));
				player.getPackets().sendGameMessage("You travel using your canoe.");
				player.closeInterfaces();
			} else if (componentId == 3) {
				if (player.usingLog) {
					player.getPackets().sendGameMessage("You must be using at least a dugout canoe.");
				} else {
					player.setNextPosition(new Position(3112, 3411, 0));
					player.getPackets().sendGameMessage("You travel using your canoe.");
					player.closeInterfaces();
				}
			} else if (componentId == 6) {
				if (player.usingLog || player.usingDugout) {
					player.getPackets().sendGameMessage("You must be using at least a stable dugout canoe.");
				} else {
					player.setNextPosition(new Position(3132, 3510, 0));
					player.getPackets().sendGameMessage("You travel using your canoe.");
					player.closeInterfaces();
				}
			} else if (componentId == 49) {
				if (!player.usingWaka) {
					player.getPackets().sendGameMessage("You must be using at least a waka canoe.");
				} else {
					player.setNextPosition(new Position(3145, 3791, 0));
					player.getPackets().sendGameMessage("You travel using your canoe.");
					player.closeInterfaces();
				}
			}
		} else if (interfaceId == 403) {
			if (componentId == 12) {
				if (!player.getInventory().containsItem(1511, 1)) {
				} else if (player.getInventory().getCoinsAmount() > 100) {
				} else {
					player.getInventory().deleteItem(1511, 1);
					player.getInventory().removeItemMoneyPouch(new Item(995, 100));
					player.getInventory().addItem(960, 1);
					player.getSkills().addXp(Skills.CONSTRUCTION, 1200);
				}
			}
			if (componentId == 13) {
				if (!player.getInventory().containsItem(1521, 1)) {
				} else if (player.getInventory().getCoinsAmount() > 250) {
				} else {
					player.getInventory().deleteItem(1521, 1);
					player.getInventory().removeItemMoneyPouch(new Item(995, 250));
					player.getInventory().addItem(8778, 1);
					player.getSkills().addXp(Skills.CONSTRUCTION, 2400);
				}
			}
			if (componentId == 14) {
				if (!player.getInventory().containsItem(6333, 1)) {
				} else if (player.getInventory().getCoinsAmount() > 500) {
				} else {
					player.getInventory().deleteItem(6333, 1);
					player.getInventory().removeItemMoneyPouch(new Item(995, 500));
					player.getInventory().addItem(8780, 1);
					player.getSkills().addXp(Skills.CONSTRUCTION, 3600);
				}
			}
			if (componentId == 15) {
				if (!player.getInventory().containsItem(6332, 1)) {
				} else if (player.getInventory().getCoinsAmount() > 1500) {
				} else {
					player.getInventory().deleteItem(6332, 1);
					player.getInventory().removeItemMoneyPouch(new Item(995, 1500));
					player.getInventory().addItem(8782, 1);
					player.getSkills().addXp(Skills.CONSTRUCTION, 4800);
				}
			}
		} else if (interfaceId == 880) {
			if (componentId >= 7 && componentId <= 19) {
				Familiar.setLeftclickOption(player, (componentId - 7) / 2);
			} else if (componentId == 21) {
				Familiar.confirmLeftOption(player);
			} else if (componentId == 25) {
				Familiar.setLeftclickOption(player, 7);
			}
		} else if(interfaceId == PresetManager.INTERFACE_ID) {
			player.getPresetManager().handleButtons(componentId);
			if (componentId == 229) {
				player.getBank().openBank();
			}
		} else if (interfaceId == 662) {
			if (player.getFamiliar() == null) {
				if (player.getPet() == null) {
					return;
				}
				if (componentId == 49) {
					player.getPet().call();
				} else if (componentId == 51) {
					player.getDialogueManager().startDialogue("DismissD");
				}
				return;
			}
			if (componentId == 49) {
				player.getFamiliar().call();
			} else if (componentId == 51) {
				player.getDialogueManager().startDialogue("DismissD");
			} else if (componentId == 67) {
				player.getFamiliar().takeBob();
			} else if (componentId == 69) {
				player.getFamiliar().renewFamiliar();
			} else if (componentId == 74) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK) {
					player.getFamiliar().setSpecial(true);
				}
				if (player.getFamiliar().hasSpecialOn()) {
					player.getFamiliar().submitSpecial(player);
				}
			}
		} else if (interfaceId == 747) {
			if (componentId == 8) {
				Familiar.selectLeftOption(player);
			} else if (player.getPet() != null) {
				if (componentId == 11 || componentId == 20) {
					player.getPet().call();
				} else if (componentId == 12 || componentId == 21) {
					player.getDialogueManager().startDialogue("DismissD");
				} else if (componentId == 10 || componentId == 19) {
					player.getPet().sendFollowerDetails();
				}
			} else if (player.getFamiliar() != null) {
				if (componentId == 11 || componentId == 20) {
					player.getFamiliar().call();
				} else if (componentId == 12 || componentId == 21) {
					player.getDialogueManager().startDialogue("DismissD");
				} else if (componentId == 13 || componentId == 22) {
					player.getFamiliar().takeBob();
				} else if (componentId == 14 || componentId == 23) {
					player.getFamiliar().renewFamiliar();
				} else if (componentId == 19 || componentId == 10) {
					player.getFamiliar().sendFollowerDetails();
				} else if (componentId == 18) {
					if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK) {
						player.getFamiliar().setSpecial(true);
					}
					if (player.getFamiliar().hasSpecialOn()) {
						player.getFamiliar().submitSpecial(player);
					}
				}
			}


		} else if (interfaceId == 309) {
			PlayerLook.handleHairdresserSalonButtons(player, componentId, slotId);
		} else if (interfaceId == 729) {
			PlayerLook.handleThessaliasMakeOverButtons(player, componentId, slotId);
		} else if (interfaceId == 187) {
			if (componentId == 1) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getMusicsManager().playAnotherMusic(slotId / 2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getMusicsManager().sendHint(slotId / 2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getMusicsManager().addToPlayList(slotId / 2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getMusicsManager().removeFromPlayList(slotId / 2);
				}
			} else if (componentId == 4) {
				player.getMusicsManager().addPlayingMusicToPlayList();
			} else if (componentId == 10) {
				player.getMusicsManager().switchPlayListOn();
			} else if (componentId == 11) {
				player.getMusicsManager().clearPlayList();
			} else if (componentId == 13) {
				player.getMusicsManager().switchShuffleOn();
			}
		} else if (interfaceId == 275) {
			if (componentId == 14) {
				player.getPackets().sendOpenURL(Constants.WEBSITE_LINK);
			}
		} else if (interfaceId == 590 && componentId == 8 || interfaceId == 464) {
			player.getEmotesManager()
			.useBookEmote(interfaceId == 464 ? componentId : EmotesManager.getId(slotId, packetId));
		} else if (interfaceId == 192) {
			if (!player.finishedStarter)
				return;
			if (componentId == 2) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 9) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId == 11) {
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			} else if (componentId == 13) {
				player.getCombatDefinitions().switchShowSkillSpells();
			} else if (componentId >= 15 & componentId <= 17) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 15);
			} else {
				Magic.processNormalSpell(player, componentId, packetId);
			}

		} else if (interfaceId == 398) {
			if (!player.finishedStarter)
				return;
			if (componentId == 19) {
				player.getInterfaceManager().sendSettings();
			} else if (componentId == 15 || componentId == 1) {
				player.getHouse().setBuildMode(componentId == 15);
			} else if (componentId == 25 || componentId == 26) {
				player.getHouse().setArriveInPortal(componentId == 25);
			} else if (componentId == 27) {
				player.getHouse().expelGuests();
			} else if (componentId == 29) {
				House.leaveHouse(player);
			}
		} else if (interfaceId == 402) {
			if (componentId >= 93 && componentId <= 115) {
				player.getHouse().createRoom(componentId - 93);
			}
		} else if (interfaceId == 394 || interfaceId == 396) {
			if (componentId == 11) {
				player.getHouse().build(slotId);
			}
		} else if (interfaceId == 334) {
			if (componentId == 22) {
				player.closeInterfaces();
			} else if (componentId == 21) {
				player.getTrade().accept(false);
				/**
				 *
				 * Token Store.
				 *
				 */
			}
		} else if (interfaceId == 825) {
			switch (componentId) {
			case 93:
				if (player.getDungTokens() >= 150000) {
					player.getInventory().addItem(18349, 1);
					player.setDungTokens(player.getDungTokens() - 150000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 150,000 tokens to buy this!");
				}
				break;
			case 97:
				if (player.getDungTokens() >= 150000) {
					player.getInventory().addItem(18351, 1);
					player.setDungTokens(player.getDungTokens() - 150000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 150,000 tokens to buy this!");
				}
				break;
			case 101:
				if (player.getDungTokens() >= 150000) {
					player.getInventory().addItem(18353, 1);
					player.setDungTokens(player.getDungTokens() - 150000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 150,000 tokens to buy this!");
				}
				break;
			case 105:
				if (player.getDungTokens() >= 150000) {
					player.getInventory().addItem(18355, 1);
					player.setDungTokens(player.getDungTokens() - 150000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 150,000 tokens to buy this!");
				}
				break;
			case 109:
				if (player.getDungTokens() >= 150000) {
					player.getInventory().addItem(18357, 1);
					player.setDungTokens(player.getDungTokens() - 150000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 150,000 tokens to buy this!");
				}
				break;
			case 113:
				if (player.getDungTokens() >= 150000) {
					player.getInventory().addItem(18359, 1);
					player.getInventory().addItem(18361, 1);
					player.setDungTokens(player.getDungTokens() - 150000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 150,000 tokens to buy this!");
				}
				break;
			case 117:
				if (player.getDungTokens() >= 50000) {
					player.getInventory().addItem(18335, 1);
					player.setDungTokens(player.getDungTokens() - 50000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 50,000 tokens to buy this!");
				}
				break;
			case 121:
				if (player.getDungTokens() >= 50000) {
					player.getInventory().addItem(19669, 1);
					player.setDungTokens(player.getDungTokens() - 50000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 50,000 tokens to buy this!");
				}
				break;
			case 125:
				if (player.getDungTokens() >= 25000) {
					player.getInventory().addItem(23752, 1);
					player.setDungTokens(player.getDungTokens() - 25000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 25,000 tokens to buy this!");
				}
				break;
			case 129:
				if (player.getDungTokens() >= 50000) {
					player.getInventory().addItem(18337, 1);
					player.setDungTokens(player.getDungTokens() - 50000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 50,000 tokens to buy this!");
				}
				break;
			case 133:
				if (player.getDungTokens() >= 100000) {
					player.getInventory().addItem(28856, 1);
					player.setDungTokens(player.getDungTokens() - 100000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 100,000 tokens to buy this!");
				}
				break;
			case 136:
				if (player.getDungTokens() >= 50000) {
					player.getInventory().addItem(19675, 1);
					player.setDungTokens(player.getDungTokens() - 50000);
					player.getPackets().sendGameMessage("Purchase successful.");
				} else {
					player.getPackets().sendGameMessage("You need 50,000 tokens to buy this!");
				}
				break;
			}

			/**
			 *
			 * End of token store.
			 *
			 */
		} else if (interfaceId == 335) {
			if (componentId == 18) {
				player.getTrade().accept(true);
			} else if (componentId == 20) {
				player.closeInterfaces();
			} else if (componentId == 32) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getTrade().removeItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getTrade().removeItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getTrade().removeItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getTrade().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("trade_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("trade_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getTrade().sendValue(slotId, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getTrade().sendExamine(slotId, false);
				}
			} else if (componentId == 35) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getTrade().sendValue(slotId, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getTrade().sendExamine(slotId, true);
				}
			} else if (componentId == 53) {
				player.getTemporaryAttributtes().put("add_Money_Pouch_To_Trade", 995);
				player.getTemporaryAttributtes().put("add_money_pouch_trade", Boolean.TRUE);
				player.getPackets().sendRunScript(108,
						new Object[] { "                          Your money pouch contains "
								+ player.getMoneyPouch().getCoinsAmount() + " coins."
								+ "                           How much would you like to offer?" });
			}
		} else if (interfaceId == 336) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getTrade().addItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getTrade().addItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getTrade().addItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getTrade().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("trade_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("trade_isRemove");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getTrade().sendValue(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 300) {
			ForgingInterface.handleIComponents(player, componentId);
		} else if (interfaceId == 206 && !player.isInDung()) {
			if (componentId == 15 && !player.isInDung()) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET && !player.isInDung()) {
					player.getPriceCheckManager().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("pc_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				}
			}
		} else if (interfaceId == 672) {
			if (componentId == 16) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					Summoning.createPouch(player, slotId2, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					Summoning.createPouch(player, slotId2, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					Summoning.createPouch(player, slotId2, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					Summoning.createPouch(player, slotId2, Byte.MAX_VALUE);//all
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					Summoning.createPouch(player, slotId2, 28);// x
					player.getPackets().sendGameMessage("You currently need "
							+ ItemDefinitions.getItemDefinitions(slotId2).getCreateItemRequirements());
				}
			} else if (componentId == 19 && packetId == 14) {
				Summoning.infuseScrolls(player);
			}

		} else if (interfaceId == 666) {
			if (componentId == 16) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					Summoning.transformScrolls(player, slotId2, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					Summoning.transformScrolls(player, slotId2, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					Summoning.transformScrolls(player, slotId2, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					Summoning.transformScrolls(player, slotId2, Integer.MAX_VALUE);
				}
			} else if (componentId == 18 && packetId == 14) {
				Summoning.infusePouches(player);
			}
		} else if (interfaceId == 652) {
			if (componentId == 31) {
				GraveStoneSelection.handleSelectionInterface(player, slotId / 6);
			} else if (componentId == 34) {
				GraveStoneSelection.confirmSelection(player);
			}
		} else if (interfaceId == 207) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getPriceCheckManager().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("pc_isRemove");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 665) {
			if (player.getFamiliar() == null || player.getFamiliar().getBob() == null) {
				return;
			}
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bob_isRemove");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 732) { // Monsters/Bosses Page 1
			if (componentId == 179) {
				player.getDialogueManager().startDialogue("MTLowLevelTraining");
			} else if (componentId == 180) {
				player.getDialogueManager().startDialogue("MTMediumLevelTraining");
			} else if (componentId == 181) {
				player.getDialogueManager().startDialogue("MTLowLevelDungeons");
			} else if (componentId == 182) {
				player.getDialogueManager().startDialogue("MTMediumLevelDungeons");
			} else if (componentId == 183) {
				player.getDialogueManager().startDialogue("MTHighLevelDungeons");
			} else if (componentId == 184) {
				player.getDialogueManager().startDialogue("MTSlayerDungeons");
			} else if (componentId == 185) {
				player.getDialogueManager().startDialogue("MTMediumLevelBosses");
			} else if (componentId == 186) {
				player.getDialogueManager().startDialogue("MTHighLevelBosses");
			}

		}

		else if (interfaceId == 671) {
			if (player.getFamiliar() == null || player.getFamiliar().getBob() == null) {
				return;
			}
			if (componentId == 27) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("bob_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				}
			} else if (componentId == 29) {
				player.getFamiliar().takeBob();
			}
		} else if (interfaceId == 916) {
			SkillsDialogue.handleSetQuantityButtons(player, componentId);
		}
		else if (interfaceId == 105 || interfaceId == 107 || interfaceId ==
				109 || interfaceId == 449) {
			if (player.getGameMode().isIronman()) {
				player.sm("You are not permitted to use the Grand Exchange.");
				return;
			}
			player.getGeManager().handleButtons(interfaceId, componentId, slotId,
					packetId);
		}
		if (interfaceId == 540) {
			PuroPuro.handleButtons(player, componentId);
		} else if (interfaceId == 193) {
			if (componentId == 5) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId >= 9 && componentId <= 11) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 9);
			} else if (componentId == 18) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else {
				Magic.processAncientSpell(player, componentId, packetId);
			}
		} else if (interfaceId == 430) {
			if (player.getTemporaryAttributtes().get("JAGGED") == Boolean.TRUE) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You cannot do that until you've verified this device with JAG.");
				return;
			}
			if (componentId == 5) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId == 9) {
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			} else if (componentId >= 11 & componentId <= 13) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 11);
			} else if (componentId == 20) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else {
				Magic.processLunarSpell(player, componentId, packetId);
			}
		} else if (interfaceId == 261) {
			if (player.getInterfaceManager().containsInventoryInter()) {
				return;
			}
			if (componentId == 22) {
				if (player.getInterfaceManager().containsScreenInter()) {
					player.getPackets().sendGameMessage(
							"Please close the interface you have open before setting your graphic options.");
					return;
				}
				player.stopAll();
				player.getInterfaceManager().sendInterface(742);
			} else if (componentId == 12) {
				player.switchAllowChatEffects();
			} else if (componentId == 13) { // chat setup
				player.getInterfaceManager().sendSettings(982);
			} else if (componentId == 14) {
				player.switchMouseButtons();
			} else if (componentId == 24) {
				player.getInterfaceManager().sendSettings(429);
			} else if (componentId == 16) {
				player.getInterfaceManager().sendSettings(398);
			} else if (componentId == 11) {
				player.switchProfanityFilter();
			}
		} else if (interfaceId == 429) {
			if (componentId == 18) {
				player.getInterfaceManager().sendSettings();
			}
		} else if (interfaceId == 982) {
			if (componentId == 5) {
				player.getInterfaceManager().sendSettings();
			} else if (componentId == 41) {
				player.setPrivateChatSetup(player.getPrivateChatSetup() == 0 ? 1 : 0);
			} else if (componentId >= 17 && componentId <= 36) {
				player.setClanChatSetup(componentId - 17);
			} else if (componentId >= 97 && componentId <= 116) {
				player.setGuestChatSetup(componentId - 97);
			} else if (componentId >= 49 && componentId <= 66) {
				player.setPrivateChatSetup(componentId - 48);
			} else if (componentId >= 72 && componentId <= 91) {
				player.setFriendChatSetup(componentId - 72);
			}
		} else if (interfaceId == 271) {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (componentId == 8 || componentId == 42) {
						player.getPrayer().switchPrayer(slotId);
					} else if (componentId == 43 && player.getPrayer().isUsingQuickPrayer()) {
						player.getPrayer().switchSettingQuickPrayer();
					}
				}
			});
		} else if (interfaceId == 320) {
			if (packetId == 14) {
				player.stopAll();
				int lvlupSkill = -1;
				int skillMenu = -1;

				switch (componentId) {
				case 150: // Attack
					skillMenu = 1;
					if (player.getTemporaryAttributtes().remove("leveledUp[0]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 1);
					} else {
						lvlupSkill = 0;
						player.getPackets().sendConfig(1230, 10);
					}
					break;
				case 9: // Strength
					skillMenu = 2;
					if (player.getTemporaryAttributtes().remove("leveledUp[2]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 2);
					} else {
						lvlupSkill = 2;
						player.getPackets().sendConfig(1230, 20);
					}
					break;
				case 22: // Defence
					skillMenu = 5;
					if (player.getTemporaryAttributtes().remove("leveledUp[1]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 5);
					} else {
						lvlupSkill = 1;
						player.getPackets().sendConfig(1230, 40);
					}
					break;
				case 40: // Ranged
					skillMenu = 3;
					if (player.getTemporaryAttributtes().remove("leveledUp[4]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 3);
					} else {
						lvlupSkill = 4;
						player.getPackets().sendConfig(1230, 30);
					}
					break;
				case 58: // Prayer
					if (player.getTemporaryAttributtes().remove("leveledUp[5]") != Boolean.TRUE) {
						skillMenu = 7;
						player.getPackets().sendConfig(965, 7);
					} else {
						lvlupSkill = 5;
						player.getPackets().sendConfig(1230, 60);
					}
					break;
				case 71: // Magic
					if (player.getTemporaryAttributtes().remove("leveledUp[6]") != Boolean.TRUE) {
						skillMenu = 4;
						player.getPackets().sendConfig(965, 4);
					} else {
						lvlupSkill = 6;
						player.getPackets().sendConfig(1230, 33);
					}
					break;
				case 84: // Runecrafting
					if (player.getTemporaryAttributtes().remove("leveledUp[20]") != Boolean.TRUE) {
						skillMenu = 12;
						player.getPackets().sendConfig(965, 12);
					} else {
						lvlupSkill = 20;
						player.getPackets().sendConfig(1230, 100);
					}
					break;
				case 102: // Construction
					skillMenu = 22;
					if (player.getTemporaryAttributtes().remove("leveledUp[21]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 22);
					} else {
						lvlupSkill = 21;
						player.getPackets().sendConfig(1230, 698);
					}
					break;
				case 145: // Hitpoints
					skillMenu = 6;
					if (player.getTemporaryAttributtes().remove("leveledUp[3]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 6);
					} else {
						lvlupSkill = 3;
						player.getPackets().sendConfig(1230, 50);
					}
					break;
				case 15: // Agility
					skillMenu = 8;
					if (player.getTemporaryAttributtes().remove("leveledUp[16]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 8);
					} else {
						lvlupSkill = 16;
						player.getPackets().sendConfig(1230, 65);
					}
					break;
				case 28: // Herblore
					skillMenu = 9;
					if (player.getTemporaryAttributtes().remove("leveledUp[15]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 9);
					} else {
						lvlupSkill = 15;
						player.getPackets().sendConfig(1230, 75);
					}
					break;
				case 46: // Thieving
					skillMenu = 10;
					if (player.getTemporaryAttributtes().remove("leveledUp[17]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 10);
					} else {
						lvlupSkill = 17;
						player.getPackets().sendConfig(1230, 80);
					}
					break;
				case 64: // Crafting
					skillMenu = 11;
					if (player.getTemporaryAttributtes().remove("leveledUp[12]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 11);
					} else {
						lvlupSkill = 12;
						player.getPackets().sendConfig(1230, 90);
					}
					break;
				case 77: // Fletching
					skillMenu = 19;
					if (player.getTemporaryAttributtes().remove("leveledUp[9]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 19);
					} else {
						lvlupSkill = 9;
						player.getPackets().sendConfig(1230, 665);
					}
					break;
				case 90: // Slayer
					skillMenu = 20;
					if (player.getTemporaryAttributtes().remove("leveledUp[18]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 20);
					} else {
						lvlupSkill = 18;
						player.getPackets().sendConfig(1230, 673);
					}
					break;
				case 108: // Hunter
					skillMenu = 23;
					if (player.getTemporaryAttributtes().remove("leveledUp[22]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 23);
					} else {
						lvlupSkill = 22;
						player.getPackets().sendConfig(1230, 689);
					}
					break;
				case 140: // Mining
					skillMenu = 13;
					if (player.getTemporaryAttributtes().remove("leveledUp[14]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 13);
					} else {
						lvlupSkill = 14;
						player.getPackets().sendConfig(1230, 110);
					}
					break;
				case 135: // Smithing
					skillMenu = 14;
					if (player.getTemporaryAttributtes().remove("leveledUp[13]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 14);
					} else {
						lvlupSkill = 13;
						player.getPackets().sendConfig(1230, 115);
					}
					break;
				case 34: // Fishing
					skillMenu = 15;
					if (player.getTemporaryAttributtes().remove("leveledUp[10]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 15);
					} else {
						lvlupSkill = 10;
						player.getPackets().sendConfig(1230, 120);
					}
					break;
				case 52: // Cooking
					skillMenu = 16;
					if (player.getTemporaryAttributtes().remove("leveledUp[7]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 16);
					} else {
						lvlupSkill = 7;
						player.getPackets().sendConfig(1230, 641);
					}
					break;
				case 130: // Firemaking
					skillMenu = 17;
					if (player.getTemporaryAttributtes().remove("leveledUp[11]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 17);
					} else {
						lvlupSkill = 11;
						player.getPackets().sendConfig(1230, 649);
					}
					break;
				case 125: // Woodcutting
					skillMenu = 18;
					if (player.getTemporaryAttributtes().remove("leveledUp[8]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 18);
					} else {
						lvlupSkill = 8;
						player.getPackets().sendConfig(1230, 660);
					}
					break;
				case 96: // Farming
					skillMenu = 21;
					if (player.getTemporaryAttributtes().remove("leveledUp[19]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 21);
					} else {
						lvlupSkill = 19;
						player.getPackets().sendConfig(1230, 681);
					}
					break;
				case 114: // Summoning
					skillMenu = 24;
					if (player.getTemporaryAttributtes().remove("leveledUp[23]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 24);
					} else {
						lvlupSkill = 23;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
				case 120: // Dung
					skillMenu = 25;
					if (player.getTemporaryAttributtes().remove("leveledUp[24]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 25);
					} else {
						lvlupSkill = 24;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
				}

				player.getInterfaceManager().sendScreenInterface(317, 1218);
				player.getPackets().sendInterface(false, 1218, 1, 1217);
				if (lvlupSkill != -1) {
					LevelUp.switchFlash(player, lvlupSkill, false);
				}
				if (skillMenu != -1) {
					player.getTemporaryAttributtes().put("skillMenu", skillMenu);
				}
			} else if (packetId == 67 || packetId == 5) { // set level target,
				// set xp target
				int skillId = player.getSkills().getTargetIdByComponentId(componentId);
				boolean usingLevel = packetId == 67;
				player.getTemporaryAttributtes().put(usingLevel ? "levelSkillTarget" : "xpSkillTarget", skillId);
				player.getPackets().sendInputIntegerScript("Please enter target " + (usingLevel ? "level" : "xp") + " you want to set: ");

			} else if (packetId == 55) { // clear target
				int skillId = player.getSkills().getTargetIdByComponentId(componentId);
				player.getSkills().setSkillTargetEnabled(skillId, false);
				player.getSkills().setSkillTargetValue(skillId, 0);
				player.getSkills().setSkillTargetUsingLevelMode(skillId, false);
			}
		} else if (interfaceId == 1218) {
			if((componentId >= 33 && componentId <= 55) || componentId == 120 || componentId == 151 || componentId == 189)
				player.getPackets().sendInterface(false, 1218, 1, 1217); //seems to fix
		} else if (interfaceId == 387) {
			if (player.getInterfaceManager().containsInventoryInter()) {
				return;
			}
			if (componentId == 6) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == 24437 || hatId == 24439 || hatId == 24440 || hatId == 24441) {
						player.getDialogueManager().startDialogue("FlamingSkull",
								player.getEquipment().getItem(Equipment.SLOT_HAT), -1);
						return;
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_HAT);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_HAT);
				}
			} else if (componentId == 9) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20769 || capeId == 20771) {
						player.animate(new Animation(8502));
						player.setNextGraphics(new Graphics(1308));
						player.getPackets().sendGameMessage(
								"You restored your Summoning points with the Completionist cape!", true);
					}
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20769 || capeId == 20771) {
						SkillCapeCustomizer.startCustomizing(player, capeId);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767) {
						SkillCapeCustomizer.startCustomizing(player, capeId);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_CAPE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_CAPE);
				}
			} else if (componentId == 12) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					int amuletId = player.getEquipment().getAmuletId();
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4,
								new Position(3087, 3496, 0), new Item(amuletId))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amuletId >= 10354 && amuletId <= 10361 ? (amulet.getId() + 2) : (amulet.getId() - 2));
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					} else if (amuletId == 1704 || amuletId == 10352) {
						player.getPackets().sendGameMessage(
								"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					int amuletId = player.getEquipment().getAmuletId();
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4,
								new Position(2918, 3176, 0), new Item(amuletId))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amuletId >= 10354 && amuletId <= 10361 ? (amulet.getId() + 2) : (amulet.getId() - 2));
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					} else if (amuletId == 1704 || amuletId == 10352) {
						player.getPackets().sendGameMessage(
								"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
					}					
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					int amuletId = player.getEquipment().getAmuletId();
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4,
								new Position(3105, 3251, 0), new Item(amuletId))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amuletId >= 10354 && amuletId <= 10361 ? (amulet.getId() + 2) : (amulet.getId() - 2));
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					} else if (amuletId == 1704 || amuletId == 10352) {
						player.getPackets().sendGameMessage(
								"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					int amuletId = player.getEquipment().getAmuletId();
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4,
								new Position(3293, 3163, 0), new Item(amuletId))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amuletId >= 10354 && amuletId <= 10361 ? (amulet.getId() + 2) : (amulet.getId() - 2));
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					} else if (amuletId == 1704 || amuletId == 10352) {
						player.getPackets().sendGameMessage(
								"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_AMULET);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_AMULET);
				}
			} else if (componentId == 15) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_WEAPON);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (TridentOfTheSwamp.isToxicTrident(player.getEquipment().getWeaponId())) {
						player.getTridentOfTheSwamp().check();
					} else if (TridentOfTheSeas.isTrident(player.getEquipment().getWeaponId())) {
						player.getTridentOfTheSeas().check();
					}
					if (player.getEquipment().getWeaponId() == 15484) {
						if (player.isLocked() || player.getControlerManager().getControler() instanceof DungeonController
								|| player.getControlerManager().getControler() instanceof FightCaves
								|| player.getControlerManager().getControler() instanceof FightKiln
								|| player.getControlerManager().getControler() instanceof PestInvasion
								|| World.isSinkArea(player.getTile())) {
							player.getPackets().sendGameMessage("You can't use this item during this time.");
							return;
						} else {
							player.getDialogueManager().startDialogue("Orb");
						}
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					if (player.getEquipment().getWeaponId() == 14057) {
						SorceressGarden.teleportToSocreressGarden(player, true);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_WEAPON);
				}
			} else if (componentId == 18) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_CHEST);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_CHEST);
				}
			} else if (componentId == 21) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_SHIELD);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_SHIELD);
				}
			} else if (componentId == 24) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_LEGS);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_LEGS);
				}
			} else if (componentId == 27) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_HANDS);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_HANDS);
				}
			} else if (componentId == 30) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_FEET);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_FEET);
				}
			} else if (componentId == 33) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_RING);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_RING);
				}
			} else if (componentId == 36) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_ARROWS);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_ARROWS);
				}
			} else if (componentId == 45) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_AURA);
					player.getAuraManager().removeAura();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_AURA);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getAuraManager().activate();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getAuraManager().sendAuraRemainingTime();
				}
			} else if (componentId == 37) {
				openEquipmentBonuses(player, false);
			} else if (componentId == 40) {
				player.stopAll();
				openItemsKeptOnDeath(player);
			} else if (componentId == 41) {
				player.stopAll();
				player.getPackets().sendConfigByFile(10268, 1);
				player.getInterfaceManager().sendInterface(1178);
			}
		} else if (interfaceId == 1265) {
			Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
			if (shop == null) {
				return;
			}
			if (componentId == 49 || componentId == 50) {
				player.setVerboseShopDisplayMode(componentId == 50);
			} else if (componentId == 28 || componentId == 29) {
				Shop.setBuying(player, componentId == 28);
			} else if (componentId == 20) {
				boolean buying = Shop.isBuying(player);
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					shop.sendInfo(player, slotId, !buying);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (buying) {
						shop.buy(player, slotId, 1);
					} else {
						shop.sell(player, slotId, 1);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					if (buying) {
						shop.buy(player, slotId, 5);
					} else {
						shop.sell(player, slotId, 5);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					if (buying) {
						shop.buy(player, slotId, 10);
					} else {
						shop.sell(player, slotId, 10);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					if (buying) {
						shop.buy(player, slotId, 50);
					} else {
						shop.sell(player, slotId, 50);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					if (buying) {
						shop.buy(player, slotId, 500);
					} else {
						shop.sell(player, slotId, 500);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					if (buying) {
						shop.buyAll(player, slotId);
					}
				}
			} else if (componentId == 220) {
				shop.setTransaction(player, 1);
			} else if (componentId == 217) {
				shop.increaseTransaction(player, -5);
			} else if (componentId == 214) {
				shop.increaseTransaction(player, -1);
			} else if (componentId == 15) {
				shop.increaseTransaction(player, 1);
			} else if (componentId == 208) {
				shop.increaseTransaction(player, 5);
			} else if (componentId == 211) {
				shop.setTransaction(player, Integer.MAX_VALUE);
			} else if (componentId == 201) {
				shop.pay(player);
			}
		} else if (interfaceId == 1266) {
			if (componentId == 0) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null) {
					return;
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					shop.sendInfo(player, slotId, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					shop.sell(player, slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					shop.sell(player, slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					shop.sell(player, slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					shop.sell(player, slotId, 50);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 645) {
			if (componentId == 16) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ItemSets.sendComponents(player, slotId2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					ItemSets.exchangeSet(player, slotId2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					ItemSets.examineSet(player, slotId2);
				}
			}
		} else if (interfaceId == 644) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					ItemSets.sendComponentsBySlot(player, slotId, slotId2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					ItemSets.exchangeSet(player, slotId, slotId2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 1266) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				} else {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null) {
						return;
					}
					player.getPackets().sendConfig(2563, slotId);
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
						shop.sendValue(player, slotId);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
						shop.sell(player, slotId, 1);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
						shop.sell(player, slotId, 5);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
						shop.sell(player, slotId, 10);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						shop.sell(player, slotId, 50);
					}
				}
			}
		} else if (interfaceId == 734) {
			player.getFairyRing().handleButtons(interfaceId, componentId);
		} else if (interfaceId == 864) {
			SpiritTree.handleButtons(player, slotId);
		} else if (interfaceId == 138) {
			GnomeGlider.handleButtons(player, componentId);
		} else if (interfaceId == 640) {
			if (componentId == 18 || componentId == 22) {
				player.getTemporaryAttributtes().put("WillDuelFriendly", true);
				player.getPackets().sendConfig(283, 67108864);
			} else if (componentId == 19 || componentId == 21) {
				player.getTemporaryAttributtes().put("WillDuelFriendly", false);
				player.getPackets().sendConfig(283, 134217728);
			} else if (componentId == 20) {
				DuelControler.challenge(player);
			}
		} else if (interfaceId == 650) {
			if (componentId == 15) {
				player.stopAll();
				player.setNextPosition(new Position(2974, 4384, player.getZ()));
				player.getControlerManager().startControler("CorpBeastControler");
			} else if (componentId == 16) {
				player.closeInterfaces();
			}
		} else if (interfaceId == 667) {
			if (componentId == 14 || componentId == 9) {
				if (slotId >= 14) {
					return;
				}
				Item item = player.getEquipment().getItem(slotId);
				if (item == null) {
					return;
				}
				if (packetId == 3) {
					player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
				} else if (packetId == 216 || packetId == 14) {
					sendRemove(player, slotId);
					ButtonHandler.refreshEquipBonuses(player);
				}
			} else if (componentId == 46 && player.getTemporaryAttributtes().remove("Banking") != null) {
				player.getBank().openBank();
			}
		} else if (interfaceId == 670) {
			if (componentId == 0) {
				if (slotId >= player.getInventory().getItemsContainerSize()) {
					return;
				}
				Item item = player.getInventory().getItem(slotId);
				if (item == null) {
					return;
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					if (sendWear(player, slotId, item.getId())) {
						ButtonHandler.refreshEquipBonuses(player);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == Inventory.INVENTORY_INTERFACE) { // inventory
			if (componentId == 0) {
				if (slotId > 27 || player.getInterfaceManager().containsInventoryInter()) {
					return;
				}
				Item item = player.getInventory().getItem(slotId);
				if (item == null || item.getId() != slotId2) {
					return;
				}
				if (player.isLocked()) {
					return;
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					InventoryOptionsHandler.handleItemOption1(player, slotId, slotId2, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					InventoryOptionsHandler.handleItemOption2(player, slotId, slotId2, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					InventoryOptionsHandler.handleItemOption3(player, slotId, slotId2, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					InventoryOptionsHandler.handleItemOption4(player, slotId, slotId2, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					InventoryOptionsHandler.handleItemOption5(player, slotId, slotId2, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					InventoryOptionsHandler.handleItemOption6(player, slotId, slotId2, item);
				} else if (packetId == WorldPacketsDecoder.DROP_ITEM) {
					InventoryOptionsHandler.drop(player, slotId, slotId2, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					InventoryOptionsHandler.handleItemOption8(player, slotId, slotId2, item);
				}
			}
		} else if (interfaceId == 742) {
			if (componentId == 46) {
				player.stopAll();
			}
		} else if (interfaceId == 1253) {
			if (componentId == 0) {
				player.stopAll();
			}
		} else if (interfaceId == 743) {
			if (componentId == 20) {
				player.stopAll();
			}
		} else if (interfaceId == 741) {
			if (componentId == 9) {
				player.stopAll();
			}
		} else if (interfaceId == 675) {
			JewllerySmithing.handleButtonClick(player, componentId, packetId == 14 ? 1 : packetId == 67 ? 5 : 10);
		} else if (interfaceId == 432) {
			final int index = Enchanting.getComponentIndex(componentId);
			if (index == -1) {
				return;
			}
			Enchanting.processBoltEnchantSpell(player, index, packetId == 14 ? 1 : packetId == 67 ? 5 : 10);
		} else if (interfaceId == 749) {
			if (componentId == 4) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getPrayer().switchQuickPrayers();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getPrayer().switchSettingQuickPrayer();
				}
			}
		} else if (interfaceId == 13 || interfaceId == 14 || interfaceId == 759) {
			player.getBankPin().handleButtons(interfaceId, componentId);
		} else if (interfaceId == 750) {
			if (componentId == 4) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.toogleRun(!player.isResting());
					if (player.isResting()) {
						player.stopAll();
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (player.isResting()) {
						player.stopAll();
						return;
					}
					long currentTime = Utils.currentTimeMillis();
					if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an emote.");
						return;
					}
					if (player.isLocked()) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an action.");
						return;
					}
					player.stopAll();
					player.getActionManager().setAction(new Rest());
				}
			}
		} else if (interfaceId == 11) {
			if (componentId == 17) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().depositItem(slotId, 1, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().depositItem(slotId, 5, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getBank().depositItem(slotId, 10, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			} else if (componentId == 18) {
				player.getBank().depositAllInventory(false);
			} else if (componentId == 20) {
				player.getBank().depositAllMoneyPouch(false);
			} else if (componentId == 22) {
				player.getBank().depositAllEquipment(false);
			} else if (componentId == 24) {
				player.getBank().depositAllBob(false);
			}
		} else if (interfaceId == 762) {
			if (componentId == 15) {
				player.getBank().switchInsertItems();
			} else if (componentId == 19) {
				player.getBank().switchWithdrawNotes();
			} else if (componentId == 124) {
				player.getPresetManager().startInterface();
			} else if (componentId == 33) {
				player.getBank().depositAllInventory(true);
			} else if (componentId == 35) {
				player.getBank().depositAllMoneyPouch(true);
			} else if (componentId == 37) {
				player.getBank().depositAllEquipment(true);
			} else if (componentId == 39) {
				player.getBank().depositAllBob(true);
			} else if (componentId == 46) {
				player.closeInterfaces();
				player.getInterfaceManager().sendInterface(767);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						player.getBank().openBank();
					}
				});
			} else if (componentId >= 46 && componentId <= 64) {
				int tabId = 9 - (componentId - 46) / 2;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().setCurrentTab(tabId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().collapse(tabId);
				}
			} else if (componentId == 95) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().withdrawItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().withdrawItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getBank().withdrawItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getBank().withdrawLastAmount(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("bank_isWithdraw", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					player.getBank().withdrawItemButOne(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getBank().sendExamine(slotId);
				}

			} else if (componentId == 119) {
				openEquipmentBonuses(player, true);
			}
		} else if (interfaceId == 190) {
			if (componentId == 15) {
				if (slotId == 170) {
					player.getInterfaceManager().sendInterface(1245);
					player.getPackets().sendIComponentText(1245, 15, "The Blood Pact");
				} else if (slotId == 1) {
					player.getInterfaceManager().sendInterface(1245);
					player.getPackets().sendIComponentText(1245, 15, "Cook's Assistant");
				}
			}
		} else if (interfaceId == 763) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().depositItem(slotId, 1, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().depositItem(slotId, 5, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getBank().depositItem(slotId, 10, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getBank().depositLastAmount(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 767) {
			if (componentId == 10) {
				player.getBank().openBank();
			}
		} else if (interfaceId == 1263) {
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
		} else if (interfaceId == 400) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				TeleTabs.makeTeletab(player, componentId, 1);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				TeleTabs.makeTeletab(player, componentId, 5);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
				TeleTabs.makeTeletab(player, componentId, 10);
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
				player.getTemporaryAttributtes().put("teletab_x", componentId);
				player.getPackets().sendRunScript(108, new Object[] { "Enter Amount:" });
			} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
				int n = player.getInventory().getAmountOf(1761);
				TeleTabs.makeTeletab(player, componentId, n);
			}
		} else if (interfaceId == 60) {
			CastleWars.handleInterfaces(player, interfaceId, componentId, packetId);
		} else if (interfaceId == 884) {
			if (componentId == 4) {
				int weaponId = player.getEquipment().getWeaponId();
				if (player.hasInstantSpecial(weaponId)) {
					player.performInstantSpecial(weaponId);
					return;
				}
				submitSpecialRequest(player);
			} else if (componentId >= 7 && componentId <= 10) {
				player.getCombatDefinitions().setAttackStyle(componentId - 7);
			} else if (componentId == 11) {
				player.getCombatDefinitions().switchAutoRelatie();
			}
		} else if (interfaceId == 755) {
			if (componentId == 44)
			{
				player.animate(new Animation(-1)); // Stops reading
			}
			// worldmap
			player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 2);
			if (componentId == 42) {
				player.getHintIconsManager().removeAll();// TODO find hintIcon
				// index
				player.getPackets().sendConfig(1159, 1);
			}
		} else if (interfaceId == 20) {
			SkillCapeCustomizer.handleSkillCapeCustomizer(player, componentId);
		} else if (interfaceId == 1089) {
			if (componentId == 30) {
				player.getTemporaryAttributtes().put("clanflagselection", slotId);
			} else if (componentId == 26) {
				Integer flag = (Integer) player.getTemporaryAttributtes().remove("clanflagselection");
				player.stopAll();
				if (flag != null) {
					ClansManager.setClanFlagInterface(player, flag);
				}
			}
		} else if (interfaceId == 1096) {
			if (componentId == 41) {
				ClansManager.viewClammateDetails(player, slotId);
			} else if (componentId == 94) {
				ClansManager.switchGuestsInChatCanEnterInterface(player);
			} else if (componentId == 95) {
				ClansManager.switchGuestsInChatCanTalkInterface(player);
			} else if (componentId == 96) {
				ClansManager.switchRecruitingInterface(player);
			} else if (componentId == 97) {
				ClansManager.switchClanTimeInterface(player);
			} else if (componentId == 124) {
				ClansManager.openClanMottifInterface(player);
			} else if (componentId == 131) {
				ClansManager.openClanMottoInterface(player);
			} else if (componentId == 240) {
				ClansManager.setTimeZoneInterface(player, -720 + slotId * 10);
			} else if (componentId == 262) {
				player.getTemporaryAttributtes().put("editclanmatejob", slotId);
			} else if (componentId == 276) {
				player.getTemporaryAttributtes().put("editclanmaterank", slotId);
			} else if (componentId == 309) {
				ClansManager.kickClanmate(player);
			} else if (componentId == 318) {
				ClansManager.saveClanmateDetails(player);
			} else if (componentId == 290) {
				ClansManager.setWorldIdInterface(player, slotId);
			} else if (componentId == 297) {
				ClansManager.openForumThreadInterface(player);
			} else if (componentId == 346) {
				ClansManager.openNationalFlagInterface(player);
			} else if (componentId == 113) {
				ClansManager.showClanSettingsClanMates(player);
			} else if (componentId == 120) {
				ClansManager.showClanSettingsSettings(player);
			} else if (componentId == 386) {
				ClansManager.showClanSettingsPermissions(player);
			} else if (componentId >= 395 && componentId <= 475) {
				int selectedRank = (componentId - 395) / 8;
				if (selectedRank == 10) {
					selectedRank = 125;
				} else if (selectedRank > 5) {
					selectedRank = 100 + selectedRank - 6;
				}
				ClansManager.selectPermissionRank(player, selectedRank);
			} else if (componentId == 489) {
				ClansManager.selectPermissionTab(player, 1);
			} else if (componentId == 498) {
				ClansManager.selectPermissionTab(player, 2);
			} else if (componentId == 506) {
				ClansManager.selectPermissionTab(player, 3);
			} else if (componentId == 514) {
				ClansManager.selectPermissionTab(player, 4);
			} else if (componentId == 522) {
				ClansManager.selectPermissionTab(player, 5);
			}
		} else if (interfaceId == 1105) {
			if (componentId == 63 || componentId == 66) {
				ClansManager.setClanMottifTextureInterface(player, componentId == 66, slotId);
			} else if (componentId == 35) {
				ClansManager.openSetMottifColor(player, 0);
			} else if (componentId == 80) {
				ClansManager.openSetMottifColor(player, 1);
			} else if (componentId == 92) {
				ClansManager.openSetMottifColor(player, 2);
			} else if (componentId == 104) {
				ClansManager.openSetMottifColor(player, 3);
			} else if (componentId == 120) {
				player.stopAll();
			}
		} else if (interfaceId == 1110) {
			if (componentId == 82) {
				ClansManager.joinClanChatChannel(player);
			} else if (componentId == 75) {
				ClansManager.openClanDetails(player);
			} else if (componentId == 78) {
				ClansManager.openClanSettings(player);
			} else if (componentId == 91) {
				ClansManager.joinGuestClanChat(player);
			} else if (componentId == 95) {
				ClansManager.banPlayer(player);
			} else if (componentId == 99) {
				ClansManager.unbanPlayer(player);
			} else if (componentId == 11) {
				ClansManager.unbanPlayer(player, slotId);
			} else if (componentId == 109) {
				ClansManager.leaveClan(player);
			}
		} else if (interfaceId == 1079) {
			player.closeInterfaces();
		} else if (interfaceId == 1056) {
			if (componentId == 173) {
				player.getInterfaceManager().sendInterface(917);
			}
		} else if (interfaceId == 751) {
			if (componentId == 26) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFriendsIgnores().setPrivateStatus(0);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFriendsIgnores().setPrivateStatus(1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFriendsIgnores().setPrivateStatus(2);
				}
			} else if (componentId == 23) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.setClanStatus(0);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.setClanStatus(1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.setClanStatus(2);
				}
			} else if (componentId == 32) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.setFilterGame(false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.setFilterGame(true);
				}
			} else if (componentId == 29) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.setPublicStatus(0);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.setPublicStatus(1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.setPublicStatus(2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.setPublicStatus(3);
				}
			} else if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFriendsIgnores().setFriendsChatStatus(0);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFriendsIgnores().setFriendsChatStatus(1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFriendsIgnores().setFriendsChatStatus(2);
				}
			} else if (componentId == 20) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.setTradeStatus(0);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.setTradeStatus(1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.setTradeStatus(2);
				}
			} else if (componentId == 14) {
				player.getInterfaceManager().sendInterface(275);
				int number = 0;
				for (int i = 0; i < 100; i++) {
					player.getPackets().sendIComponentText(275, i, "");
				}
				for (Player p5 : World.getPlayers()) {
					if (p5 == null) {
						continue;
					}
					number++;
					String titles = "";
					if (p5.isOwner()) {
						titles = "<col=1589FF><img=1>[Developer] ";
					}
					else if (p5.getRights() == 2 && !p5.isOwner()) {
						titles = "<col=ffff00><img=1>[Administrator] ";
					}
					else if (p5.getRights() == 1) {
						titles = "<col=5a5a5a><img=0>[Moderator] ";
					}
					else if (p5.isGraphicDesigner()) {
						titles = "<col=EE82EE><img=9>[Graphics Leader] ";
					}
					else if (p5.isSupporter()) {
						titles = "<col=0000FF><img=13>[Supporter]";
					}
					else if (p5.getDonationManager().getRank() != DonatorRanks.NONE) {
						titles = p5.getDonationManager().getRank().getYellTitle() + " ";
					}
					player.getPackets().sendIComponentText(275, 12 + number, titles + "" + p5.getDisplayName());
				}
				player.getPackets().sendIComponentText(275, 1, Constants.SERVER_NAME);
				player.getPackets().sendIComponentText(275, 10, " ");
				player.getPackets().sendIComponentText(275, 11, "Players Online: " + number);
				player.getPackets().sendIComponentText(275, 12, " ");
				player.getPackets().sendGameMessage("There are currently " + World.getPlayers().size()
						+ " players playing " + Constants.SERVER_NAME + ".");
			} else if (componentId == 17) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.setAssistStatus(0);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.setAssistStatus(1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.setAssistStatus(2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					// ASSIST XP Earned/Time
				}
			}
		} else if (interfaceId == 1163 || interfaceId == 1164 || interfaceId == 1168 || interfaceId == 1170
				|| interfaceId == 1173) {
			player.getDominionTower().handleButtons(interfaceId, componentId);
		} else if (interfaceId == 900) {
			PlayerLook.handleMageMakeOverButtons(player, componentId);
		} else if (interfaceId == 1028) {
			PlayerLook.handleCharacterCustomizingButtons(player, componentId, slotId);
		} else if (interfaceId == 1108 || interfaceId == 1109) {
			player.getFriendsIgnores().handleFriendChatButtons(interfaceId, componentId, packetId);
		} else if (interfaceId == 1079) {
			player.closeInterfaces();
		} else if (interfaceId == 374) {
			if (componentId >= 5 && componentId <= 9) {
				player.setNextPosition(new Position(FightPitsViewingOrb.ORB_TELEPORTS[componentId - 5]));
			} else if (componentId == 15) {
				player.stopAll();
			}
		} else if (interfaceId == 1092) {
			player.getLodeStones().handleButtons(componentId);
		} else if (interfaceId == 1214) {
			player.getSkills().handleSetupXPCounter(componentId);
		}
		if (interfaceId == 1292) {
			if (componentId == 12) {
				Crucible.enterArena(player);
			} else if (componentId == 13) {
				player.closeInterfaces();
			}
		}
	}

	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15) {
			return;
		}
		player.stopAll(false, false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null || !player.getInventory().addItem(item.getId(), item.getAmount())) {
			return;
		}
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
		if (Runecrafting.isTiara(item.getId())) {
			player.getPackets().sendConfig(491, 0);
		}
		if (slotId == 3) {
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}
	}

	public static boolean sendWear(Player player, int slotId, int itemId) {
		if (player.isFinished() || player.isDead()) {
			return false;
		}
		player.stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		if (item.getId() == 9813 || item.getId() == 9814) {
			if (player.questPoints < 17) {
				player.getPackets().sendGameMessage("You must have a total of 17 quest points to wear this item.");
				return true;
			}
		}
		if (item.getId() == 29900) {
			if (player.getSkills().getXp(Skills.HITPOINTS) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Hitpoints experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29901) {
			if (player.getSkills().getXp(Skills.ATTACK) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Attack experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29813) {
			if (player.getSkills().getXp(Skills.WOODCUTTING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Woodcutting experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29902) {
			if (player.getSkills().getXp(Skills.CONSTRUCTION) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Construction experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29903) {
			if (player.getSkills().getXp(Skills.STRENGTH) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Strength experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29904) {
			if (player.getSkills().getXp(Skills.AGILITY) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Agility experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29905) {
			if (player.getSkills().getXp(Skills.COOKING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Cooking experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29906) {
			if (player.getSkills().getXp(Skills.CRAFTING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Crafting experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29907) {
			if (player.getSkills().getXp(Skills.DEFENCE) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Defence experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29908) {
			if (player.getSkills().getXp(Skills.FARMING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Farming experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29909) {
			if (player.getSkills().getXp(Skills.FIREMAKING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Firemaking experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29910) {
			if (player.getSkills().getXp(Skills.FISHING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Fishing experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29911) {
			if (player.getSkills().getXp(Skills.FLETCHING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Fletching experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29912) {
			if (player.getSkills().getXp(Skills.HERBLORE) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Herblore experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29913) {
			if (player.getSkills().getXp(Skills.HUNTER) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Hunter experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29914) {
			if (player.getSkills().getXp(Skills.MAGIC) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Magic experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29915) {
			if (player.getSkills().getXp(Skills.MINING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Mining experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29916) {
			if (player.getSkills().getXp(Skills.PRAYER) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Prayer experience to wear this.");
				return true;
			}
		}
		// 29917 is quest cape


		if (item.getId() == 29918) {
			if (player.getSkills().getXp(Skills.RANGE) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Ranged experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29919) {
			if (player.getSkills().getXp(Skills.RUNECRAFTING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Runecrafting experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29920) {
			if (player.getSkills().getXp(Skills.SLAYER) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Slayer experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29921) {
			if (player.getSkills().getXp(Skills.SMITHING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Smithing experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29922) {
			if (player.getSkills().getXp(Skills.SUMMONING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Summoning experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29923) {
			if (player.getSkills().getXp(Skills.THIEVING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Thieving experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 20767 || item.getId() == 20768) {
			if (player.getSkills().getTotalLevel() < 2475) {
				player.getPackets()
				.sendGameMessage("You must have achieved the highest level in every skill to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20754) {
			if (player.getSkills().getTotalLevel() < 260) {
				player.getPackets().sendGameMessage("You must have a total level of 260 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20755) {
			if (player.getSkills().getTotalLevel() < 520) {
				player.getPackets().sendGameMessage("You must have a total level of 520 to wear this item.");
				return true;
			}
		}

		if (item.getId() == 20756) {
			if (player.getSkills().getTotalLevel() < 780) {
				player.getPackets().sendGameMessage("You must have a total level of 780 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20757) {
			if (player.getSkills().getTotalLevel() < 1040) {
				player.getPackets().sendGameMessage("You must have a total level of 1040 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20758) {
			if (player.getSkills().getTotalLevel() < 1300) {
				player.getPackets().sendGameMessage("You must have a total level of 1300 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20759) {
			if (player.getSkills().getTotalLevel() < 1560) {
				player.getPackets().sendGameMessage("You must have a total level of 1560 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20760) {
			if (player.getSkills().getTotalLevel() < 1820) {
				player.getPackets().sendGameMessage("You must have a total level of 1820 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20761) {
			if (player.getSkills().getTotalLevel() < 2080) {
				player.getPackets().sendGameMessage("You must have a total level of 2080 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20762) {
			if (player.getSkills().getTotalLevel() < 2340) {
				player.getPackets().sendGameMessage("You must have a total level of 2340 to wear this item.");
				return true;
			}
		}
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getAppearence().isMale())) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}

		if (!ItemConstants.canWear(item, player)) {
			return true;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage("Not enough free space in your inventory.");
			return true;
		}
		if (!ItemRequirements.hasRequirements(player, item)) {
			return true;
		}
		if (player.isFighting) {
			return true;
		}
		if (!player.getControlerManager().canEquip(targetSlot, itemId)) {
			return false;
		}
		player.stopAll(false, false);
		player.getInventory().deleteItem(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(5).getId(),
						player.getEquipment().getItem(5).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(3).getId(),
						player.getEquipment().getItem(3).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId()
				|| !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
				player.getInventory().refresh(slotId);
			} else {
				player.getInventory().addItem(new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
			}
			player.getEquipment().getItems().set(targetSlot, null);
		}
		if (targetSlot == Equipment.SLOT_AURA) {
			player.getAuraManager().removeAura();
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendSound(2240, 0, 1);
		if (targetSlot == 3) {
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}
		player.getCharges().wear(targetSlot);
		return true;
	}

	public static boolean sendWear2(Player player, int slotId, int itemId) {
		if (player.isFinished() || player.isDead()) {
			return false;
		}
		player.stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		if (item.getId() == 29900) {
			if (player.getSkills().getXp(Skills.HITPOINTS) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Hitpoints experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29813) {
			if (player.getSkills().getXp(Skills.WOODCUTTING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Woodcutting experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29901) {
			if (player.getSkills().getXp(Skills.ATTACK) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Attack experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29902) {
			if (player.getSkills().getXp(Skills.CONSTRUCTION) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Construction experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29903) {
			if (player.getSkills().getXp(Skills.STRENGTH) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Strength experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29904) {
			if (player.getSkills().getXp(Skills.AGILITY) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Agility experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29905) {
			if (player.getSkills().getXp(Skills.COOKING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Cooking experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29906) {
			if (player.getSkills().getXp(Skills.CRAFTING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Crafting experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29907) {
			if (player.getSkills().getXp(Skills.DEFENCE) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Defence experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29908) {
			if (player.getSkills().getXp(Skills.FARMING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Farming experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29909) {
			if (player.getSkills().getXp(Skills.FIREMAKING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Firemaking experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29910) {
			if (player.getSkills().getXp(Skills.FISHING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Fishing experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29911) {
			if (player.getSkills().getXp(Skills.FLETCHING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Fletching experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29912) {
			if (player.getSkills().getXp(Skills.HERBLORE) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Herblore experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29913) {
			if (player.getSkills().getXp(Skills.HUNTER) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Hunter experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29914) {
			if (player.getSkills().getXp(Skills.MAGIC) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Magic experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29915) {
			if (player.getSkills().getXp(Skills.MINING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Mining experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29916) {
			if (player.getSkills().getXp(Skills.PRAYER) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Prayer experience to wear this.");
				return true;
			}
		}
		// 29917 is quest cape

		if (item.getId() == 29918) {
			if (player.getSkills().getXp(Skills.RANGE) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Ranged experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29919) {
			if (player.getSkills().getXp(Skills.RUNECRAFTING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Runecrafting experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29920) {
			if (player.getSkills().getXp(Skills.SLAYER) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Slayer experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29921) {
			if (player.getSkills().getXp(Skills.SMITHING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Smithing experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29922) {
			if (player.getSkills().getXp(Skills.SUMMONING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Summoning experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 29923) {
			if (player.getSkills().getXp(Skills.THIEVING) <= 104273167) {
				player.getPackets().sendGameMessage("You need to have atleast 104m Thieving experience to wear this.");
				return true;
			}
		}
		if (item.getId() == 9813 || item.getId() == 9814) {
			if (player.questPoints < 17) {
				player.getPackets().sendGameMessage("You must have a total of 17 quest points to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20767 || item.getId() == 20768) {
			if (player.getSkills().getTotalLevel() < 2475) {
				player.getPackets()
				.sendGameMessage("You must have achieved the highest level in every skill to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20754) {
			if (player.getSkills().getTotalLevel() < 260) {
				player.getPackets().sendGameMessage("You must have a total level of 260 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20755) {
			if (player.getSkills().getTotalLevel() < 520) {
				player.getPackets().sendGameMessage("You must have a total level of 520 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20756) {
			if (player.getSkills().getTotalLevel() < 780) {
				player.getPackets().sendGameMessage("You must have a total level of 780 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20757) {
			if (player.getSkills().getTotalLevel() < 1040) {
				player.getPackets().sendGameMessage("You must have a total level of 1040 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20758) {
			if (player.getSkills().getTotalLevel() < 1300) {
				player.getPackets().sendGameMessage("You must have a total level of 1300 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20759) {
			if (player.getSkills().getTotalLevel() < 1560) {
				player.getPackets().sendGameMessage("You must have a total level of 1560 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20760) {
			if (player.getSkills().getTotalLevel() < 1820) {
				player.getPackets().sendGameMessage("You must have a total level of 1820 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20761) {
			if (player.getSkills().getTotalLevel() < 2080) {
				player.getPackets().sendGameMessage("You must have a total level of 2080 to wear this item.");
				return true;
			}
		}
		if (item.getId() == 20762) {
			if (player.getSkills().getTotalLevel() < 2340) {
				player.getPackets().sendGameMessage("You must have a total level of 2340 to wear this item.");
				return true;
			}
		}

		if (item.getDefinitions().isNoted()
				|| !item.getDefinitions().isWearItem(player.getAppearence().isMale()) && itemId != 4084) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (itemId == 4084) {
			targetSlot = 3;
		}
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		if (!ItemConstants.canWear(item, player)) {
			return false;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage("Not enough free space in your inventory.");
			return false;
		}
		if (!ItemRequirements.hasRequirements(player, item)) {
			return false;
		}
		if (!player.getControlerManager().canEquip(targetSlot, itemId)) {
			return false;
		}
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId()
				|| !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
			} else {
				player.getInventory().getItems().add(new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
			}
			player.getEquipment().getItems().set(targetSlot, null);
		}
		if (targetSlot == Equipment.SLOT_AURA) {
			player.getAuraManager().removeAura();
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		if (targetSlot == 3) {
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}
		player.getCharges().wear(targetSlot);
		return true;
	}


	public static void submitSpecialRequest(final Player player) {
		CoresManager.fastExecutor.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getCombatDefinitions().switchUsingSpecialAttack();
						}
					}, 0);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 200);
	}

	public static void sendWear(Player player, int[] slotIds) {
		if (player.isFinished() || player.isDead()) {
			return;
		}
		boolean worn = false;
		Item[] copy = player.getInventory().getItems().getItemsCopy();
		for (int slotId : slotIds) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null) {
				continue;
			}
			if (sendWear2(player, slotId, item.getId())) {
				worn = true;
			}
		}
		player.getInventory().refreshItems(copy);
		if (worn) {
			player.getAppearence().generateAppearenceData();
			player.getPackets().sendSound(2240, 0, 1);
		}
	}

	public static void openEquipmentBonuses(final Player player, boolean banking) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendConfigByFile(8348, 0); // 0 when opening bank, 1 when opening equipment I think
				player.getPackets().sendRunScript(2319); // refresh the bank/equip interface depending on the value of varbit 8348
			}
		});
		player.stopAll();
		player.getInterfaceManager().sendInventoryInterface(670);
		player.getInterfaceManager().sendInterface(667);
		player.getPackets().sendConfigByFile(8348, banking ? 0 : 1);
		player.getPackets().sendConfigByFile(4894, banking ? 1 : 0);
		player.getPackets().sendRunScript(787, 1);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendInterSetItemsOptionsScript(670, 0, 93, 4, 7, "Equip", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(670, 0, 0, 27, 0, 1);		
		player.getPackets().sendUnlockIComponentOptionSlots(667, 9, 0, 14, 0);
		player.getPackets().sendIComponentSettings(667, 14, 0, 13, 1030);
		player.getPackets().sendGlobalConfig(779, player.getAppearence().getRenderEmote());
		//WeightManager.calculateWeight(player);
		refreshEquipBonuses(player);

		if (banking) {
		    player.getTemporaryAttributtes().put("Banking", Boolean.TRUE);
		    player.setCloseInterfacesEvent(new Runnable() {
		    	
				@Override
				public void run() {
					player.getTemporaryAttributtes().remove("Banking");
				    //player.getVarsManager().sendVarBit(4894, 0);
				    player.getPackets().sendConfigByFile(8348, 0);
				}
				
		    });
		}
	}



	public static void openItemsKeptOnDeath(Player player) {
		player.getInterfaceManager().sendInterface(17);
		sendItemsKeptOnDeath(player, false);
	}

	public static void sendItemsKeptOnDeath(Player player, boolean wilderness) {
		boolean skulled = player.hasSkull();
		Integer[][] slots = GraveStone.getItemSlotsKeptOnDeath(player, wilderness, skulled,
				player.getPrayer().isProtectingItem());
		Item[][] items = GraveStone.getItemsKeptOnDeath(player, slots);
		long riskedWealth = 0;
		long carriedWealth = 0;
		for (Item item : items[1]) {
			carriedWealth = riskedWealth += item.getDefinitions().getValue() * item.getAmount();
		}
		for (Item item : items[0]) {
			carriedWealth += item.getDefinitions().getValue() * item.getAmount();
		}
		if (slots[0].length > 0) {
			for (int i = 0; i < slots[0].length; i++) {
				player.getVarsManager().sendVarBit(9222 + i, slots[0][i]);
			}
			player.getVarsManager().sendVarBit(9227, slots[0].length);
		} else {
			player.getVarsManager().sendVarBit(9222, -1);
			player.getVarsManager().sendVarBit(9227, 1);
		}
		player.getVarsManager().sendVarBit(9226, wilderness ? 1 : 0);
		player.getVarsManager().sendVarBit(9229, skulled ? 1 : 0);
		StringBuffer text = new StringBuffer();
		text.append("The number of items kept on").append("<br>").append("death is normally 3.").append("<br>")
		.append("<br>").append("<br>");
		if (wilderness) {
			text.append("Your gravestone will not").append("<br>").append("appear.");
		} else {
			int time = GraveStone.getMaximumTicks(player.getGraveStone());
			int seconds = (int) (time * 0.6);
			int minutes = seconds / 60;
			seconds -= minutes * 60;

			text.append("Gravestone:").append("<br>")
			.append(ClientScriptMap.getMap(1099).getStringValue(player.getGraveStone())).append("<br>")
			.append("<br>").append("Initial duration:").append("<br>")
			.append(minutes + ":" + (seconds < 10 ? "0" : "") + seconds).append("<br>");
		}
		text.append("<br>").append("<br>").append("Carried wealth:").append("<br>")
		.append(carriedWealth > Integer.MAX_VALUE ? "Too high!" : Utils.getFormattedNumber((int) carriedWealth))
		.append("<br>").append("<br>").append("Risked wealth:").append("<br>")
		.append(riskedWealth > Integer.MAX_VALUE ? "Too high!" : Utils.getFormattedNumber((int) riskedWealth))
		.append("<br>").append("<br>");
		player.getPackets().sendGlobalString(352, text.toString());
	}

	public static void refreshEquipBonuses(Player player) {
		player.getPackets().sendIComponentText(667, 28,
				"Stab: " + player.getCombatDefinitions().getPrefix(0) + player.getCombatDefinitions().getBonuses()[0]);
		player.getPackets().sendIComponentText(667, 29,
				"Slash: " + player.getCombatDefinitions().getPrefix(1) + player.getCombatDefinitions().getBonuses()[1]);
		player.getPackets().sendIComponentText(667, 30,
				"Crush: " + player.getCombatDefinitions().getPrefix(2) + player.getCombatDefinitions().getBonuses()[2]);
		player.getPackets().sendIComponentText(667, 31,
				"Magic: " + player.getCombatDefinitions().getPrefix(3) + player.getCombatDefinitions().getBonuses()[3]);
		player.getPackets().sendIComponentText(667, 32,
				"Range: " + player.getCombatDefinitions().getPrefix(4) + player.getCombatDefinitions().getBonuses()[4]);
		player.getPackets().sendIComponentText(667, 33,
				"Stab: " + player.getCombatDefinitions().getPrefix(5) + player.getCombatDefinitions().getBonuses()[5]);
		player.getPackets().sendIComponentText(667, 34,
				"Slash: " + player.getCombatDefinitions().getPrefix(6) + player.getCombatDefinitions().getBonuses()[6]);
		player.getPackets().sendIComponentText(667, 35,
				"Crush: " + player.getCombatDefinitions().getPrefix(7) + player.getCombatDefinitions().getBonuses()[7]);
		player.getPackets().sendIComponentText(667, 36,
				"Magic: " + player.getCombatDefinitions().getPrefix(8) + player.getCombatDefinitions().getBonuses()[8]);
		player.getPackets().sendIComponentText(667, 37,
				"Range: " + player.getCombatDefinitions().getPrefix(9) + player.getCombatDefinitions().getBonuses()[9]);
		player.getPackets().sendIComponentText(667, 38, "Summoning: " + player.getCombatDefinitions().getPrefix(10)
				+ player.getCombatDefinitions().getBonuses()[10]);
		player.getPackets().sendIComponentText(667, 39, "Absorb Melee: " + player.getCombatDefinitions().getPrefix(11)
				+ player.getCombatDefinitions().getBonuses()[11] + "%");
		player.getPackets().sendIComponentText(667, 40, "Absorb Magic: " + player.getCombatDefinitions().getPrefix(12)
				+ player.getCombatDefinitions().getBonuses()[12] + "%");
		player.getPackets().sendIComponentText(667, 41, "Absorb Ranged: " + player.getCombatDefinitions().getPrefix(13)
				+ player.getCombatDefinitions().getBonuses()[13] + "%");
		player.getPackets().sendIComponentText(667, 42,
				"Strength: " + player.getCombatDefinitions().getBonuses()[14] + ".0");
		player.getPackets().sendIComponentText(667, 43, "Ranged Str: " + player.getCombatDefinitions().getPrefix(15)
				+ player.getCombatDefinitions().getBonuses()[15]);
		player.getPackets().sendIComponentText(667, 44, "Prayer: " + player.getCombatDefinitions().getPrefix(16)
				+ player.getCombatDefinitions().getBonuses()[16]);
		player.getPackets().sendIComponentText(667, 45, "Magic Damage: " + player.getCombatDefinitions().getPrefix(17)
				+ player.getCombatDefinitions().getBonuses()[17] + "%");
		//WeightManager.calculateWeight(player);
	}

	public static void sendMissionBoard(Player player) {
		// TODO Auto-generated method stub

	}
}