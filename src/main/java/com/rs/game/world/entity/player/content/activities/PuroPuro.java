package com.rs.game.world.entity.player.content.activities;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.Magic;
import com.rs.game.world.entity.player.content.Hunter.FlyingEntities;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.network.decoders.WorldPacketsDecoder;
import com.rs.utility.Utils;

public class PuroPuro extends Controller {

	private static final Item[][] REQUIRED = { { new Item(11238, 3), new Item(11240, 2), new Item(11242, 1) },
			{ new Item(11242, 3), new Item(11244, 2), new Item(11246, 1) },
			{ new Item(11246, 3), new Item(11248, 2), new Item(11250, 1) } };

	private static final Item[] REWARD = { new Item(11262, 1), new Item(11259, 1), new Item(11258, 1),
			new Item(11260, 3) };

	@Override
	public void start() {
		player.getPackets().sendBlackOut(2);
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11 : 8, 169);
		initPuroImplings();
	}

	@Override
	public void forceClose() {
		player.getPackets().sendBlackOut(0);
		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 8);
	}

	@Override
	public void magicTeleported(int type) {
		player.getControlerManager().forceStop();
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		case 25014:
			player.getControlerManager().forceStop();
			Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new Position(2427, 4446, 0), 9, false,
					Magic.OBJECT_TELEPORT);
			return true;
		}
		return true;
	}
	
	@Override
	public boolean processObjectClick5(WorldObject object) {
		switch (object.getId()) {
			case 25016:
			case 25029:
				pushThrough(player, object);
				return true;
		}
		return false;
	}

	public static void enterPuro(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {

			public void run() {
				player.getControlerManager().startControler("PuroPuro");
			}
			
		}, 10);
		Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new Position(2591, 4320, 0), 9, false,
				Magic.OBJECT_TELEPORT);
	}

	static boolean depositedNet(Player player) {
		if (player.getBank().getAmountOf(11259) >= 1)
			return true;
		else
			return false;
	}

	static boolean depositedRepellent(Player player) {
		if (player.getBank().getAmountOf(11262) >= 1)
			return true;
		else
			return false;
	}

	static boolean full(Player player) {
		if (player.getBank().getAmountOf(11260) >= 127)
			return true;
		else
			return false;
	}

	public static void handleButtonsShop(Player player, int componentId, int packetId) {
		if (componentId == 5) {
			if (player.getBank().getAmountOf(11259) > 0) {
				player.getBank().withdrawItem(11259, 1);
			} else {
				player.sm("You don't have a Magic butterfly net stored");
			}
		}
		if (componentId == 12) {
			if (player.getInventory().getFreeSlots() > 1 && player.getBank().getAmountOf(11262) >= 1) {
				player.getBank().withdrawItem(11262, 1);
			} else {
				player.sm("You don't have enough space to withdraw the Imp repellent.");
			}
			if (player.getBank().getAmountOf(11262) < 1) {
				player.sm("You don't have an Imp repellent stored.");
			}
		}
		if (componentId == 3) {
			if (player.getBank().getAmountOf(11260) < 0)
				player.sm("You don't have a impling jar stored.");

			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				player.getBank().withdrawItem(11260, 1);
			}
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
				player.getBank().withdrawItem(11260, 5);
			}
			if (packetId == 5) {
				if (player.getBank().getAmountOf(11260) > 0) {
					player.getBank().withdrawItem(11260, player.getInventory().getFreeSlots());
				} else {
					player.sm("You don't have any impling jar stored.");
				}
			}
			if (packetId == 55) {
				player.getPackets().sendRunScript(108, new Object[] { "How many would you like to deposit?" });
				player.getTemporaryAttributtes().put("remove_X_money", 11260);
				player.getTemporaryAttributtes().put("Puro_Deposit", Boolean.TRUE);
			}

		}
		if (componentId == 18) {

			if (player.getInventory().getNumberOf(11260) > 1
					&& player.getInventory().getNumberOf(11260) + player.getBank().getAmountOf(11260) > 127
					&& player.getBank().getAmountOf(11260) < 127) {
				player.getBank().deposit(new Item(11260,
						player.getBank().getAmountOf(11260) == 127 ? 0 : 127 - player.getBank().getAmountOf(11260)));
			} else if (player.getInventory().getNumberOf(11260) + player.getBank().getAmountOf(11260) < 127) {
				player.getBank().deposit(new Item(11260, player.getInventory().getNumberOf(11260)));
			} else if (player.getInventory().getNumberOf(11260) + player.getBank().getAmountOf(11260) > 127
					&& player.getBank().getAmountOf(11260) >= 127) {
				player.sm("You can't store more than 127 Imp jars.");
			}
			if (player.getInventory().getNumberOf(11262) >= 1 && depositedRepellent(player) == false) {
				player.getBank().deposit(new Item(11262, 1));
			} else if (player.getInventory().containsItem(11262, 1) && player.getBank().getAmountOf(11262) >= 1) {
				player.sm("You can't store more than 1 Imp repellant.");
			}
			if (player.getInventory().getNumberOf(11259) >= 1 && depositedNet(player) == false) {
				player.getBank().deposit(new Item(11259, 1));
				player.getInterfaceManager().sendInterface(592);
			} else if (player.getInventory().containsItem(11259, 1) && player.getBank().getAmountOf(11259) >= 1) {
				player.sm("You can't store more than 1 Magic butterfly net.");
			}
			player.getBank().removeItem(player.getBank().getItemSlot(11260), 1, true, false); // fix the bug for adding
																								// 1 jar too much
		}
		if (componentId == 19) {
			if (player.getBank().getAmountOf(11259) >= 1)
				player.getBank().withdrawItem(11259, 1);
			if (player.getBank().getAmountOf(11262) >= 1)
				player.getBank().withdrawItem(11262, 1);
			if (player.getBank().getAmountOf(11260) >= 1)
				player.getBank().withdrawItem(11260,
						player.getBank().getAmountOf(11260) > player.getInventory().getFreeSlots()
								? player.getInventory().getFreeSlots()
								: player.getBank().getAmountOf(11260));
			if (player.getBank().getAmountOf(11259) < 1)
				player.sm("You don't have a Magic Butterfly net stored.");
			if (player.getBank().getAmountOf(11260) < 1)
				player.sm("You don't have an Imp jar stored.");
			if (player.getBank().getAmountOf(11262) < 1)
				player.sm("You don't have an Imp repellent stored.");

		}
		player.getInterfaceManager().sendInterfaces();
		// sendShop(player);
	}

	public static void sendShop(Player player) {
		player.getInterfaceManager().sendInterface(592);
		player.getPackets().sendIComponentText(592, 4,
				depositedNet(player) ? "<col=00FF00>Magic butterfly net (1/1)" : "Magic butterfly net (0/1)");
		player.getPackets().sendIComponentText(592, 11,
				depositedRepellent(player) ? "<col=00FF00>Imp repellent       (1/1)" : "Imp repellent           (0/1)");
		player.getPackets().sendIComponentText(592, 2,
				player.getBank().getAmountOf(11260) == 0 ? "Impling jar      (0/127)"
						: full(player) ? "<col=00FF00>Impling jar     (127/127)"
								: "<col=00FF00>Impling jar         (" + player.getBank().getAmountOf(11260) + "/127)");
	}

	public static void pushThrough(final Player player, WorldObject object) {
		int objectX = object.getX();
		int objectY = object.getY();
		int direction = 0;
		if (player.getX() == objectX && player.getY() < objectY) {
			objectY = objectY + 1;
			direction = ForceMovement.NORTH;
		} else if (player.getX() == objectX && player.getY() > objectY) {
			objectY = objectY - 1;
			direction = ForceMovement.SOUTH;
		} else if (player.getY() == objectY && player.getX() < objectX) {
			objectX = objectX + 1;
			direction = ForceMovement.EAST;
		} else if (player.getY() == objectY && player.getX() > objectX) {
			objectX = objectX - 1;
			direction = ForceMovement.WEST;
		} else if (player.getX() != objectX && player.getY() > objectY) {
			objectX = object.getX();
			objectY = objectY - 1;
			direction = ForceMovement.SOUTH;
		} else if (player.getX() != objectX && player.getY() < objectY) {
			objectX = object.getX();
			objectY = objectY + 1;
			direction = ForceMovement.NORTH;
		} else if (player.getY() != objectY && player.getX() < objectX) {
			objectY = object.getY();
			objectX = objectX + 1;
			direction = ForceMovement.EAST;
		} else if (player.getY() == objectY && player.getX() > objectX) {
			objectY = object.getY();
			objectX = objectX - 1;
			direction = ForceMovement.WEST;
		}
		player.setNextFacePosition(object);
		player.getPackets()
				.sendGameMessage(Utils.getRandom(2) == 1
						? "You use your strength to push through the wheat in the most efficient fashion."
						: "You use your strength to push through the wheat.");
		player.setNextFacePosition(object);
		player.animate(new Animation(6594));
		player.lock();
		final Position tile = new Position(objectX, objectY, 0);
		player.setNextFacePosition(object);
		player.setNextForceMovement(new ForceMovement(tile, 6, direction));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				player.setNextPosition(tile);
			}
			
		}, 6);

	}

	public static void initPuroImplings() {
		for (int i = 0; i < 5; i++) {
			for (int index = 0; index < 11; index++) {
				if (i > 2) {
					if (Utils.getRandom(1) == 0)
						continue;
				}
				World.spawnNPC(FlyingEntities.values()[index].getNpcId(),
						new Position(Utils.random(2558 + 3, 2626 - 3), Utils.random(4285 + 3, 4354 - 3), 0), -1,
						0, false);
			}
		}
	}

	public static void openPuroInterface(final Player player) {
		player.getInterfaceManager().sendInterface(540); // puro puro
		for (int component = 50; component < 75; component++)
			player.getPackets().sendHideIComponent(540, component, false);
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				player.getTemporaryAttributtes().remove("puro_slot");
			}
		});
	}

	public static void handleButtons(Player player, int componentId) {
		if (componentId >= 20 && componentId <= 26) {
			handlePuroInterface(player, componentId);
			confirmPuroSelection(player);
		} else if (componentId == 69) {
			player.closeInterfaces();
		} else if (componentId == 71) {
			player.closeInterfaces();
			sendShop(player);
		}
	}

	public static void handlePuroInterface(Player player, int componentId) {
		player.getTemporaryAttributtes().put("puro_slot", (componentId - 20) / 2);
	}

	public static void confirmPuroSelection(Player player) {
		if (player.getTemporaryAttributtes().get("puro_slot") == null)
			return;
		int slot = (int) player.getTemporaryAttributtes().get("puro_slot");
		Item exchangedItem = REWARD[slot];
		Item[] requriedItems = REQUIRED[slot];
		if (slot == 3) {
			requriedItems = null;
			for (Item item : player.getInventory().getItems().getItems()) {
				if (item == null || FlyingEntities.forItem((short) item.getId()) == null)
					continue;
				requriedItems = new Item[] { item };
			}
		}
		if (requriedItems == null || !player.getInventory().containsItems(requriedItems)) {
			player.getPackets().sendGameMessage("You don't have the required items.");
			return;
		}
		if (player.getInventory().addItem(exchangedItem.getId(), exchangedItem.getAmount())) {
			player.getInventory().removeItems(requriedItems);
			player.closeInterfaces();
			player.getPackets().sendGameMessage(
					"You exchange the required items for: " + exchangedItem.getName().toLowerCase() + ".");
		}
	}
}
