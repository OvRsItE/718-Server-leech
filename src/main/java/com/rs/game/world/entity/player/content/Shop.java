package com.rs.game.world.entity.player.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemManager;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.ItemExamines;
import com.rs.utility.ItemSetsKeyGenerator;
import com.rs.utility.ShopsHandler;

public class Shop {

	private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995, TOKKUL = 6529;
	public static final int TOKEN = 5250;

	public static int loyaltyShop = 0;

	public static int[][] loyaltyPrices = { { 20958, 5000 }, { 22268, 10000 }, { 20962, 5000 }, { 22270, 10000 },
			{ 20967, 5000 }, { 22272, 10000 }, { 22280, 10000 }, { 22282, 5000 }, { 22284, 10000 }, { 22286, 5000 },
			{ 20966, 10000 }, { 22274, 5000 }, { 22288, 10000 }, { 22290, 10000 }, { 20965, 10000 }, { 22276, 5000 },
			{ 22292, 10000 }, { 22294, 10000 }, { 22300, 5000 }, { 22296, 5000 }, { 22298, 10000 }, { 22302, 18000 },
			{ 22899, 5000 }, { 22901, 10000 }, { 22903, 10000 }, { 23876, 20000 }, { 22905, 5000 }, { 22907, 10000 },
			{ 22909, 10000 }, { 23874, 20000 }, { 23848, 5000 }, { 23850, 10000 }, { 23852, 10000 },
			{ 23854, 20000 }, { 28641, 60000 } };

	public static int pestinvshop = 72;
	public static int[][] pestinvPrices = { };

	public static int triviashop = 65;
	public static int[][] triviaPrices = { { 29131, 350 }, { 29132, 500 }, { 29133, 500 }, { 29134, 350 },
			{ 29135, 350 }, { 29136, 400 }, { 29137, 350 }, { 29149, 250 }, { 29139, 150 }, { 29144, 750 },
			{ 29145, 750 }, { 29146, 750 }, { 24428, 50 }, { 24427, 50 }, { 24429, 50 }, { 24430, 50 }, { 21485, 75 },
			{ 21484, 75 }, { 21487, 75 }, { 21486, 75 }, { 24100, 35 }, { 24102, 35 }, { 24104, 35 }, { 24106, 35 },
			{ 22552, 35 }, { 22554, 35 }, { 22556, 35 }, { 22558, 35 }, { 21258, 35 }, { 21260, 35 }, { 21262, 35 },
			{ 21264, 35 }, { 28960, 50 }, { 23906, 50 }, { 23880, 50 }, { 23882, 50 }, { 23886, 50 }, { 23888, 50 },
			{ 23892, 50 }, { 23894, 50 }, { 29801, 200 } };

	public static int Votingshop = 54;
	public static int[][] VotingPrices = { };

	public static int VenomiteShop = 165;

	public static int[][] VenomitePrices = { { 11212, 10 }, { 9340, 10 }, { 10635, 3000 }, { 10548, 20000 },
			{ 10550, 20000 }, { 10551, 20000 }, { 10552, 20000 }, { 10555, 20000 }, { 29833, 60000 }, { 24155, 5000 }, { 989, 5000 }, { 10549, 20000 },
			{ 10553, 20000 }, { 29476, 5000 }, { 28641, 10000 }, { 11665, 5000 }, { 11664, 5000 }, { 11663, 5000 }, { 8839, 5000 }, { 8840, 5000 }, { 8842, 5000 }, { 28640, 10000 } };

	public static int skillShop = 150;
	public static int[][] skillPrices = { { 29996, 150 }, { 29995, 100 }, { 29535, 30 }, { 29536, 30 }, { 29994, 50 },
			{ 29968, 125 }, { 28951, 300 } };

	public static int PVMShop = 99;
	public static int[][] PVMPrices = { };

	public static int SlayerShop = 169;
	public static int[][] SlayerPrices = {{29618, 120}, {28703, 500}, {29535, 60}, {29536, 60}, {28951, 120}, {29112, 120}, {29113, 120}, {29110, 120}, {29106, 300}, {28940, 500}, {28946, 500}, {28945, 500}, {28944, 500}, {28943, 500}, {28942, 500}, {29476, 30}, {19705, 1000}, {29207, 1000}, {29211, 1000}};

	public static int DonatorShop = 66;
	public static int DonatorShop2 = 67;
	public static int Donatorshop3 = 68;
	public static int[][] DonatorPrices = {};


	public static int reaperShop = 666;
	public static int[][] reaperPrices = { { 29115, 400 }, { 29103, 360 }, { 29100, 360 }, { 29097, 360 },
			{ 29803, 360 }, { 29802, 30 }, { 29109, 1000 }, { 29833, 360 } };

	private String name;
	private Item[] mainStock;
	private int[] defaultQuantity;
	private Item[] generalStock;
	private int money;
	private CopyOnWriteArrayList<Player> viewingPlayers;

	public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.name = name;
		this.money = money;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++) {
			defaultQuantity[i] = mainStock[i].getAmount();
		}
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS) {
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
		}
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				viewingPlayers.remove(player);
				player.getTemporaryAttributtes().remove("Shop");
				player.getTemporaryAttributtes().remove("shop_transaction");
				player.getTemporaryAttributtes().remove("isShopBuying");
				player.getTemporaryAttributtes().remove("ShopSelectedSlot");
				player.getTemporaryAttributtes().remove("ShopSelectedInventory");
			}
		});
		player.refreshVerboseShopDisplayMode();
		player.getVarsManager().sendVar(118, generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY);
		player.getVarsManager().sendVar(1496, -1); // sample items container id (TODO: add support for it)
		player.getVarsManager().sendVar(532, money);
		resetSelected(player);
		sendStore(player);
		player.getInterfaceManager().sendInterface(1265); // opens shop
		resetTransaction(player);
		setBuying(player, true);
		if (generalStock != null) {
			player.getPackets().sendHideIComponent(1265, 19, false); // unlocks general store icon
		}
		player.getPackets().sendIComponentSettings(1265, 20, 0, getStoreSize(), 1150); // unlocks stock slots
		sendInventory(player);
		player.getPackets().sendIComponentText(1265, 85, name);
	}

	public void resetTransaction(Player player) {
		setTransaction(player, 1);
	}

	public void increaseTransaction(Player player, int amount) {
		setTransaction(player, getTransaction(player) + amount);
	}

	public int getTransaction(Player player) {
		Integer transaction = (Integer) player.getTemporaryAttributtes().get("shop_transaction");
		return transaction == null ? 1 : transaction;
	}

	public void pay(Player player) {
		Integer selectedSlot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
		Boolean inventory = (Boolean) player.getTemporaryAttributtes().get("ShopSelectedInventory");
		if (selectedSlot == null || inventory == null) {
			return;
		}
		int amount = getTransaction(player);
		if (inventory) {
			sell(player, selectedSlot, amount);
		} else {
			buy(player, selectedSlot, amount);
		}
	}

	public int getSelectedMaxAmount(Player player) {
		Integer selectedSlot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
		Boolean inventory = (Boolean) player.getTemporaryAttributtes().get("ShopSelectedInventory");
		if (selectedSlot == null || inventory == null) {
			return 1;
		}
		if (inventory) {
			Item item = player.getInventory().getItem(selectedSlot);
			if (item == null) {
				return 1;
			}
			return player.getInventory().getAmountOf(item.getId());
		} else {
			if (selectedSlot >= getStoreSize()) {
				return 1;
			}
			Item item = selectedSlot >= mainStock.length ? generalStock[selectedSlot - mainStock.length]
					: mainStock[selectedSlot];
			if (item == null) {
				return 1;
			}
			return item.getAmount();
		}
	}

	public void setTransaction(Player player, int amount) {
		int max = getSelectedMaxAmount(player);
		if (amount > max) {
			amount = max;
		} else if (amount < 1) {
			amount = 1;
		}
		player.getTemporaryAttributtes().put("shop_transaction", amount);
		player.getVarsManager().sendVar(2564, amount);
	}

	public static void setBuying(Player player, boolean buying) {
		player.getTemporaryAttributtes().put("isShopBuying", buying);
		player.getVarsManager().sendVar(2565, buying ? 0 : 1);
	}

	public static boolean isBuying(Player player) {
		Boolean isBuying = (Boolean) player.getTemporaryAttributtes().get("isShopBuying");
		return isBuying != null && isBuying;
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 27, 0, 1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7, "Value", "Sell 1", "Sell 5", "Sell 10",
				"Sell 50", "Examine");
	}

	public void buyAll(Player player, int slotId) {
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		buy(player, slotId, item.getAmount());
	}

	public void buy(Player player, int slotId, int quantity) {
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage("There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item);
		if (price <= 0) {
			return;
		}
		int amountCoins = money == COINS ? player.getInventory().getCoinsAmount()
				: player.getInventory().getItems().getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();

		boolean enoughCoins = maxQuantity >= buyQ;
		if (money != 995) {
			for (int[] loyaltyPrice : loyaltyPrices) {
				loyaltyShop = 1;
				if (item.getId() == loyaltyPrice[0] && name.contains("Xuans Loyalty shop")) {
					if (player.getLoyaltyPoints() < loyaltyPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + loyaltyPrice[1] + " Loyalty Points to buy this item!");
						return;
					} else {
						loyaltyShop = 1;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the loyalty store.");
					player.getInventory().addItem(loyaltyPrice[0], 1);
					player.setLoyaltyPoints(player.getLoyaltyPoints() - loyaltyPrice[1]);
					return;
				}
			}
			for (int[] votingPrice : VotingPrices) {
				Votingshop = 54;
				if (item.getId() == votingPrice[0]) {
					if (player.getVotePoints() < votingPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + votingPrice[1] + " Vote points to buy this item!");
						player.getPackets().sendGameMessage("You only have " + player.getVotePoints() + " Vote points");
						return;
					} else {
						Votingshop = 54;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the Voting points Store.");
					player.getInventory().addItem(votingPrice[0], 1);
					player.setVotePoints(player.getVotePoints() - votingPrice[1]);
					return;
				}
			}
			for (int[] pvmPrice : PVMPrices) {
				PVMShop = 19;
				if (item.getId() == pvmPrice[0]) {
					if (player.getPvmPoints() < pvmPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + pvmPrice[1] + " PVM points to buy this item!");
						player.getPackets().sendGameMessage("You only have " + player.pvmPoints + " PVM points");
						return;
					} else {
						PVMShop = 19;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the PVM store.");
					player.getInventory().addItem(pvmPrice[0], 1);
					player.setPvmPoints(player.getPvmPoints() - pvmPrice[1]);
					return;
				}
			}
			for (int[] Valiusprice : VenomitePrices) {
				VenomiteShop = 165;
				if (item.getId() == Valiusprice[0] && name.contains("Venomite Points Store")) {
					if (player.getPvmPoints() < Valiusprice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + Valiusprice[1] + " venomite points to buy this item!");
						player.getPackets().sendGameMessage("You only have " + player.pvmPoints + " venomite points");
						return;
					} else {
						VenomiteShop = 165;
					}

					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName()
							+ " from the venomite points Store.");
					player.getInventory().addItem(Valiusprice[0], 1);
					player.setPvmPoints(player.getPvmPoints() - Valiusprice[1]);
					return;
				}
			}
			for (int[] SlayerPrice : SlayerPrices) {
				SlayerShop = 169;
				if (item.getId() == SlayerPrice[0] && name.contains("Slayer Point Store")) {
					if (player.getSlayerManager().getSlayerPoints() < SlayerPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + SlayerPrice[1] + " slayer points to buy this item!");
						player.getPackets().sendGameMessage("You only have " + player.getSlayerManager().slayerPoints + " slayer points");
						return;
					} else {
						SlayerShop = 169;
					}

					player.getPackets().sendGameMessage("You have bought a " + item.getDefinitions().getName()
							+ " from the slayer points Store.");
					player.getInventory().addItem(SlayerPrice[0], 1);
					player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - SlayerPrice[1]);
					return;
				}
			}
			for (int[] skillPrice : skillPrices) {
				skillShop = 150;
				if (item.getId() == skillPrice[0] && name.contains("Skilling Points Store")) {
					if (player.getSkillPoints() < skillPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + skillPrice[1] + " Skill points to buy this item!");
						player.getPackets().sendGameMessage("You only have " + player.skillPoints + " Skill points");
						return;
					} else {
						skillShop = 150;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the Skill points Store.");
					player.getInventory().addItem(skillPrice[0], 1);
					player.setSkillPoints(player.getSkillPoints() - skillPrice[1]);
					return;
				}
			}
			for (int i11 = 0; i11 < pestinvPrices.length; i11++) {
				pestinvshop = 72;
				if (item.getId() == pestinvPrices[i11][0]) {
					if (player.getPestinvPoints() < pestinvPrices[i11][1] * quantity) {
						player.getPackets().sendGameMessage(
								"You need " + pestinvPrices[i11][1] + " PestInvasion points to buy this item!");
						player.getPackets()
								.sendGameMessage("You only have " + player.pestinvasionpoints + " PestInvasion points");
						return;
					} else {
						PVMShop = 19;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the PVM store.");
					player.getInventory().addItem(PVMPrices[i11][0], 1);
					player.setPvmPoints(player.getPvmPoints() - PVMPrices[i11][1]);
					return;
				}
			}
			for (int[] triviaPrice : triviaPrices) {
				triviashop = 65;
				if (item.getId() == triviaPrice[0] && name.contains("Trivia Store")) {
					if (player.getTriviaPoints() < triviaPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + triviaPrice[1] + " Trivia Points to buy this item!");
						player.getPackets()
								.sendGameMessage("You only have " + player.getTriviaPoints() + " Trivia points");
						return;
					} else {
						triviashop = 65;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the Trivia store.");
					player.getInventory().addItem(triviaPrice[0], 1);
					player.setTriviaPoints(player.getTriviaPoints() - triviaPrice[1]);
					return;
				}
			}
			for (int[] votingPrice : VotingPrices) {
				Votingshop = 54;
				if (item.getId() == votingPrice[0]) {
					if (player.getVotePoints() < votingPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + votingPrice[1] + " Vote Points to buy this item!");
						player.getPackets().sendGameMessage("You only have " + player.getVotePoints() + " Vote points");
						return;
					} else {
						Votingshop = 54;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the Voting store.");
					player.getInventory().addItem(votingPrice[0], 1);
					player.setVotePoints(player.getVotePoints() - votingPrice[1]);
					return;
				}
			}
			for (int[] reaperPrice : reaperPrices) {
				reaperShop = 666;
				if (item.getId() == reaperPrice[0] && name.contains("Deaths Store")) {
					if (player.getBossSlayer().getReaperPoints() < reaperPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + reaperPrice[1] + " Reaper points to buy this item!");
						player.getPackets().sendGameMessage(
								"You only have " + player.getBossSlayer().getReaperPoints() + " Reaper points");
						return;
					} else {
						reaperShop = 666;
					}
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the Reaper store.");
					player.getInventory().addItem(reaperPrice[0], 1);
					player.getBossSlayer().removeReaperPoints(reaperPrice[1]);
					return;
				}
			}
			for (int[] donatorPrice : DonatorPrices) {
				DonatorShop = 66;
				DonatorShop2 = 67;
				Donatorshop3 = 68;
				if (item.getId() == donatorPrice[0]) {
					if (player.getInventory().contains(5250) < donatorPrice[1] * quantity) {
						player.getPackets()
								.sendGameMessage("You need " + donatorPrice[1] + " Donator Tokens to buy this item!");
						return;
					} else {
						DonatorShop = 66;
					}
					DonatorShop2 = 67;
					Donatorshop3 = 68;
					player.getPackets().sendGameMessage(
							"You have bought a " + item.getDefinitions().getName() + " from the Donator Store");
					player.getInventory().addItem(donatorPrice[0], 1);
					player.getInventory().removeItems(new Item(5250, donatorPrice[1]));
					return;
				}
			}
		}
		/*
		 * if (player.isIronMan && isGeneralStore()) {
		 * player.sm("IronMan Accs are not allowed to use the general store."); return;
		 * }
		 */

		if (!enoughCoins) {
			player.getPackets().sendGameMessage(
					"You don't have enough " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
			buyQ = maxQuantity;
		} else if (quantity > buyQ) {
			player.getPackets().sendGameMessage("The shop has run out of stock.");
		}
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			if (player.getInventory().removeItemMoneyPouch(new Item(money, totalPrice))) {
				player.shopLog(player, item.getId(), item.getAmount(), false);
				player.getInventory().addItem(item.getId(), buyQ);
				item.setAmount(item.getAmount() - buyQ);
				if (item.getAmount() <= 0 && slotId >= mainStock.length) {
					generalStock[slotId - mainStock.length] = null;
				}
				refreshShop();
				resetSelected(player);
			}
		}
	}

	public void restoreItems() {
		boolean needRefresh = true;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null) {
					continue;
				}
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0) {
					generalStock[i] = null;
				}
				needRefresh = false;
			}
		}
		if (needRefresh) {
			refreshShop();
		}
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null) {
					continue;
				}
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId) {
			return;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null) {
			return;
		}
		int originalId = item.getId();
		if (item.getDefinitions().isNoted() && item.getDefinitions().getCertId() != -1) {
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		}
		if (!ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage("You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item);
		if (price <= 0) {
			return;
		}
		int numberOff = player.getInventory().getItems().getNumberOf(originalId);
		if (quantity > numberOff) {
			quantity = numberOff;
		}
		if (!addItem(item.getId(), quantity) && !isGeneralStore()) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		// if (player.getRights()== 2) {
		// player.sm("Pkers are not allowed to use the general store.");
		// return;
		// }
		player.shopLog(player, item.getId(), item.getAmount(), true);
		player.getInventory().deleteItem(originalId, quantity);
		refreshShop();
		resetSelected(player);
		if (price == 0) {
			return;
		}
		player.getInventory().addItemMoneyPouch(new Item(995, price * quantity));
	}

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId) {
			return;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null) {
			return;
		}
		if (item.getDefinitions().isNoted()) {
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		}
		if (!ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage("You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item);
		if (price <= 0) {
			return;
		}
		player.getPackets()
				.sendGameMessage(item.getDefinitions().getName() + ": shop will buy for: " + price + " "
						+ ItemDefinitions.getItemDefinitions(money).getName().toLowerCase()
						+ ". Right-click the item to sell.");
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i].getId() == itemId) {
				return defaultQuantity[i];
			}
		}
		return -1;
	}

	public void resetSelected(Player player) {
		player.getTemporaryAttributtes().remove("ShopSelectedSlot");
		player.getVarsManager().sendVar(2563, -1);
	}

	public void sendInfo(Player player, int slotId, boolean inventory) {
		if (!inventory && slotId >= getStoreSize()) {
			return;
		}
		Item item = inventory ? player.getInventory().getItem(slotId)
				: slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		if (item.getDefinitions().isNoted()) {
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		}
		if (inventory && (!ItemConstants.isTradeable(item) || item.getId() == money)) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			resetSelected(player);
			return;
		}
		for (int[] loyaltyPrice : loyaltyPrices) {
			if (item.getId() == loyaltyPrice[0] && name.contains("Xuans Loyalty shop")) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + loyaltyPrice[1] + " loyalty points.");
				player.getPackets().sendConfig(2564, loyaltyPrice[1]);
				return;
			}
		}
		for (int[] pvmPrice : PVMPrices) {
			if (item.getId() == pvmPrice[0]) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + pvmPrice[1] + " PVM points.");
				player.getPackets().sendConfig(2564, pvmPrice[1]);
				return;
			}
		}
		for (int[] VenomitePrices : VenomitePrices) {
			if (item.getId() == VenomitePrices[0] && name.contains("Venomite Points Store")) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + VenomitePrices[1] + " Venomite Points.");
				player.getPackets().sendConfig(2564, VenomitePrices[1]);
				return;
			}
		}
		for (int[] SlayerPrices : SlayerPrices) {
			if (item.getId() == SlayerPrices[0] && name.contains("Slayer Point Store")) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + SlayerPrices[1] + " Slayer Points.");
				player.getPackets().sendConfig(2564, SlayerPrices[1]);
				return;
			}
		}
		for (int[] skillPrice : skillPrices) {
			if (item.getId() == skillPrice[0] && name.contains("Skilling Points Store")) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + skillPrice[1] + " Skill Points.");
				player.getPackets().sendConfig(2564, skillPrice[1]);
				return;
			}
		}
		for (int[] votingPrice : VotingPrices) {
			if (item.getId() == votingPrice[0]) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + votingPrice[1] + " Vote points.");
				player.getPackets().sendConfig(2564, votingPrice[1]);
				return;
			}
		}
		for (int[] triviaPrice : triviaPrices) {
			if (item.getId() == triviaPrice[0] && name.contains("Trivia Store")) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + triviaPrice[1] + " Trivia points.");
				player.getPackets().sendConfig(2564, triviaPrice[1]);
				return;
			}
		}
		for (int[] donatorPrice : DonatorPrices) {
			if (item.getId() == donatorPrice[0]) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + donatorPrice[1] + " Donator Tokens");
				player.getPackets().sendConfig(2564, donatorPrice[1]);
				return;
			}
		}
		for (int[] reaperPrice : reaperPrices) {
			if (item.getId() == reaperPrice[0] && name.contains("Deaths Store")) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + reaperPrice[1] + " Reaper points");
				player.getPackets().sendConfig(2564, reaperPrice[1]);
				return;
			}
		}
		for (int[] pestinvPrice : pestinvPrices) {
			if (item.getId() == pestinvPrice[0]) {
				player.getPackets().sendGameMessage(
						"" + item.getDefinitions().getName() + " costs " + pestinvPrice[1] + " PestInvasion points.");
				player.getPackets().sendConfig(2564, pestinvPrice[1]);
				return;
			}
		}
		resetTransaction(player);
		player.getTemporaryAttributtes().put("ShopSelectedSlot", slotId);
		player.getTemporaryAttributtes().put("ShopSelectedInventory", inventory);
		player.getVarsManager().sendVar(2561, inventory ? 93 : generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY); // inv
																													// key
		player.getVarsManager().sendVar(2562, item.getId());
		player.getVarsManager().sendVar(2563, slotId);
		player.getPackets().sendGlobalString(362, ItemExamines.getExamine(item));
		player.getPackets().sendGlobalConfig(1876, item.getDefinitions().isWearItem() ? 0 : -1); // TODO item pos or
																									// usage if has one,
																									// setting 0 to
																									// allow see stats
		int price = inventory ? getSellPrice(item) : getBuyPrice(item);
		player.getPackets()
				.sendGameMessage(item.getDefinitions().getName() + ": shop will " + (inventory ? "buy" : "sell")
						+ " for: " + price + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase());
	}

	public static int getSellPrice(Item item) { // What player sells for

		switch (item.getId()) {
		case 7947:
		case 7946:
		item.getDefinitions().setValue(600);
		break;
		case 386:
		case 385:
		item.getDefinitions().setValue(2000);
		break;
		case 391:
		case 392:
		item.getDefinitions().setValue(3000);
		break;
		case 2436:
		case 2437:
		item.getDefinitions().setValue(5000);
		break;
		case 2440:
		case 2441:
		item.getDefinitions().setValue(6000);
		break;
		case 2442:
		case 2443:
		item.getDefinitions().setValue(8000);
		break;
		case 2444:
		case 2445:
		item.getDefinitions().setValue(9000);
		break;
		case 3040:
		case 3041:
		item.getDefinitions().setValue(7500);
		break;
		case 2434:
		case 2435:
		item.getDefinitions().setValue(4750);
		break;
		case 12140:
		case 12141:
		item.getDefinitions().setValue(4750);
		break;
		case 6685:
		case 6686:
		item.getDefinitions().setValue(5000);
		break;
		case 3024:
		case 3025:
		item.getDefinitions().setValue(7500);
		break;
		case 2452:
		case 2453:
		item.getDefinitions().setValue(8250);
		break;
		//end of new store items
		//blood money store
				case 24455:
				item.getDefinitions().setValue(1000000);
				break;
			case 28652:
				item.getDefinitions().setValue(100000);
				break;
				case 24457:
				item.getDefinitions().setValue(1000000);
				break;
			    case 24456:
				item.getDefinitions().setValue(1000000);
				break;
			    case 42791:
				item.getDefinitions().setValue(25000);
				break;
			    case 29463:
				item.getDefinitions().setValue(200000);
				break;
			case 28832:
				item.getDefinitions().setValue(12500);
				break;
			case 42783:
				item.getDefinitions().setValue(100000);
				break;
			case 28999:
				item.getDefinitions().setValue(100000);
				break;
			case 28835:
				item.getDefinitions().setValue(500000);
				break;
			case 28833:
				item.getDefinitions().setValue(10000);
				break;
			case 29106:
				item.getDefinitions().setValue(25000);
				break;
				//blood money store end
		        //vote shop
				case 1053:
				case 1055:
				case 1057:
				case 29488:
					item.getDefinitions().setValue(60);
					break;
				case 18744:
				case 18745:
				case 18746:
					item.getDefinitions().setValue(60);
					break;
				case 7671:
				case 7673:
				case 5608:
				case 5607:
				case 6666:
				case 24092:
				case 24094:
				case 24096:
				case 20950:
				case 20951:
				case 20952:
				case 5609:
				case 24098:
			case 20949:
					item.getDefinitions().setValue(34);
					break;
			case 10551:
			case 20072:
			case 28641:
				item.getDefinitions().setValue(14);
				break;
			case 24115:
			case 989:
				item.getDefinitions().setValue(2);
				break;
			case 28755:
				item.getDefinitions().setValue(120);
				break;
			case 29110:
				item.getDefinitions().setValue(4);
				break;
				case 8800:
					item.getDefinitions().setValue(140);
					break;
					//vote shop end
					//donator shop
				case 1050://santa hat
				case 29004://santa hat
			    case 1037://bunny ears
			    case 28961:// eye of the keeper
					item.getDefinitions().setValue(40);
					break;
			case 29941://black santa hat
			case 29012://black santa hat normal
			case 29002://black h'ween mask
			case 29000://black phat
				item.getDefinitions().setValue(100);
				break;
			case 11694://ags
			case 13738://arcane spirit shield
			case 14484://dragon claws
			case 15098:
				item.getDefinitions().setValue(30);
				break;
			case 13742://ely
			case 962://c cracker
			case 29834://custom cracker
			case 19580://third prayer
			case 11858://third age range
			case 11860://third age mage
			case 11862://third age melee
				item.getDefinitions().setValue(50);
				break;
			case 13744://spectral
			case 11283://dfs
			case 21371://vine whip
			case 11724://bcp
			case 11726://tassets
			case 11720://acp
			case 11722://acs
				item.getDefinitions().setValue(15);
				break;
			case 11718://arma helm
			case 29484://toxic mbox
				item.getDefinitions().setValue(10);
				break;
			case 6199://mbox
				item.getDefinitions().setValue(5);
				break;

				//donator shop end
		case 950:
		case 951:
			item.getDefinitions().setValue(500);
			break;

		case 1739:
		case 1740:
			item.getDefinitions().setValue(1500);
			break;

		case 773:
			item.getDefinitions().setValue(2250);
			break;

		case 7650:
		case 7651:
			item.getDefinitions().setValue(3250);
			break;

		case 774:
		case 775:
			item.getDefinitions().setValue(4250);

			break;

		}

		return item.getDefinitions().getValue();
	}

	public int getBuyPrice(Item item) { // ys for
		switch (item.getId()) {
			case 15707:
				item.getDefinitions().setValue(1);
				break;
		//potions and food value
		case 7947:
		case 7946:
		item.getDefinitions().setValue(600);
		break;
		case 386:
		case 385:
		item.getDefinitions().setValue(2000);
		break;
		case 391:
		case 392:
		item.getDefinitions().setValue(3000);
		break;
		case 2436:
		case 2437:
		item.getDefinitions().setValue(5000);
		break;
		case 2440:
		case 2441:
		item.getDefinitions().setValue(6000);
		break;
		case 2442:
		case 2443:
		item.getDefinitions().setValue(8000);
		break;
		case 2444:
		case 2445:
		item.getDefinitions().setValue(9000);
		break;
		case 3040:
		case 3041:
		item.getDefinitions().setValue(7500);
		break;
		case 2434:
		case 2435:
		item.getDefinitions().setValue(4750);
		break;
		case 12140:
		case 12141:
		item.getDefinitions().setValue(4750);
		break;
		case 6685:
		case 6686:
		item.getDefinitions().setValue(5000);
		break;
		case 3024:
		case 3025:
		item.getDefinitions().setValue(7500);
		break;
		case 2452:
		case 2453:
		item.getDefinitions().setValue(8250);
		break;
		//end of new store items
		//blood money store
		case 24455:
		item.getDefinitions().setValue(1000000);
		break;
			case 28652:
				item.getDefinitions().setValue(100000);
				break;
		case 24457:
		item.getDefinitions().setValue(1000000);
		break;
	    case 24456:
		item.getDefinitions().setValue(1000000);
		break;
	    case 42791:
		item.getDefinitions().setValue(25000);
		break;
	    case 29463:
		item.getDefinitions().setValue(200000);
		break;
			case 28832:
				item.getDefinitions().setValue(12500);
				break;
			case 42783:
				item.getDefinitions().setValue(100000);
				break;
			case 28999:
				item.getDefinitions().setValue(100000);
				break;
			case 28835:
				item.getDefinitions().setValue(500000);
				break;
			case 28833:
				item.getDefinitions().setValue(10000);
				break;
			case 29106:
				item.getDefinitions().setValue(25000);
				break;
		//blood money store end
		//donator shop
			case 1050://santa hat
			case 29004://santa hat
			case 1037://bunny ears
			case 28961:// eye of the keeper
				item.getDefinitions().setValue(40);
				break;
			case 29941://black santa hat
			case 29012://black santa hat normal
			case 29002://black h'ween mask
			case 29000://black phat
				item.getDefinitions().setValue(100);
				break;
			case 11694://ags
			case 13738://arcane spirit shield
			case 14484://dragon claws
			case 15098:
				item.getDefinitions().setValue(30);
				break;
			case 13742://ely
			case 962://c cracker
			case 29834://custom cracker
			case 19580://third prayer
			case 11858://third age range
			case 11860://third age mage
			case 11862://third age melee
				item.getDefinitions().setValue(50);
				break;
			case 13744://spectral
			case 11283://dfs
			case 21371://vine whip
			case 11724://bcp
			case 11726://tassets
			case 11720://acp
			case 11722://acs
				item.getDefinitions().setValue(15);
				break;
			case 11718://arma helm
			case 29484://toxic mbox
				item.getDefinitions().setValue(10);
				break;
			case 6199://mbox
				item.getDefinitions().setValue(5);
				break;
		//donator shop end
		//vote shop
			case 1053:
			case 1055:
			case 1057:
			case 29488:
				item.getDefinitions().setValue(60);
				break;
			case 18744:
			case 18745:
			case 18746:
				item.getDefinitions().setValue(60);
				break;
			case 7671:
			case 7673:
			case 5608:
			case 5607:
			case 6666:
			case 24092:
			case 24094:
			case 24096:
			case 20950:
			case 20951:
			case 20952:
			case 5609:
			case 24098:
			case 20949:
				item.getDefinitions().setValue(34);
				break;
			case 10551:
			case 20072:
			case 28641:
				item.getDefinitions().setValue(14);
				break;
			case 24115:
			case 989:
				item.getDefinitions().setValue(2);
				break;
			case 28755:
				item.getDefinitions().setValue(120);
				break;
			case 29110:
				item.getDefinitions().setValue(4);
				break;
		case 8800:
			item.getDefinitions().setValue(140);
			break;
		//vote shop end
		case 950:
		case 951:
			item.getDefinitions().setValue(1500);
			break;
		case 1739:
		case 1740:
			item.getDefinitions().setValue(3000);
			break;
		case 773:
			item.getDefinitions().setValue(4500);
			break;
		case 7650:
		case 7651:
			item.getDefinitions().setValue(6500);
			break;

		case 774:
		case 775:
			item.getDefinitions().setValue(8500);
			break;
		}
		return item.getDefinitions().getValue();
	}

	public static int getASPrice(Item item) {
		return ItemManager.getSellPrice(item.getId());
	}

	public void sendExamine(Player player, int slotId) {
		if (slotId >= getStoreSize()) {
			return;
		}
		Item item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
		if (item == null) {
			return;
		}
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0, getStoreSize() * 6, 1150);
		}
	}

	public int getStoreSize() {
		return mainStock.length + (generalStock != null ? generalStock.length : 0);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length + (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null) {
			System.arraycopy(generalStock, 0, stock, mainStock.length, generalStock.length);
		}
		player.getPackets().sendItems(generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY, stock);
	}

}