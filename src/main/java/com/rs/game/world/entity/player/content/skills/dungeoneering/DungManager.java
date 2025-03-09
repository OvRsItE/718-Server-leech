package com.rs.game.world.entity.player.content.skills.dungeoneering;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.TemporaryAttributes;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.player.controller.impl.Kalaboss;

import java.io.Serializable;


public class DungManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5472153493680156393L;

	private static final byte version = 2; //termporary, should be deleted once files remade

	// dungeonering
	private int tokens;
	private boolean[] currentProgress;
	private int previousProgress;
	private int maxFloor;
	private int maxComplexity = 6;
	private ItemsContainer<Item> bindedItems;
	private Item bindedAmmo;
	private boolean[] visitedResources;

	private byte cVersion; //termporary, should be deleted once files remade

	private Object rejoinKey;

	private transient Player player;
	private transient DungeonPartyManager party;
	private transient Player invitingPlayer;

	public static enum ResourceDungeon {
		EDGEVILLE_DUNGEON(10, 1100, 52849, new Position(3132, 9933, 0), 52867, new Position(991, 4585, 0)),
		DWARVEN_MINE(15, 1500, 52855, new Position(3034, 9772, 0), 52864, new Position(1041, 4575, 0)),
		EDGEVILLE_DUNGEON_2(20, 1600, 52853, new Position(3104, 9826, 0), 52868, new Position(1135, 4589, 0)),
		KARANJA_VOLCANO(25, 2100, 52850, new Position(2845, 9557, 0), 52869, new Position(1186, 4598, 0)),
		DAEMONHEIM_PENINSULA(30, 2400, 52861, new Position(3513, 3666, 0), 52862, new Position(3498, 3633, 0)),
		BAXTORIAN_FALLS(35, 3000, 52857, new Position(2578, 9898, 0), 52873, new Position(1256, 4592, 0)),
		MINING_GUILD(45, 4400, 52856, new Position(3022, 9741, 0), 52866, new Position(1052, 4521, 0)),
		TAVERLY_DUNGEON_1(55, 6200, 52851, new Position(2854, 9841, 0), 52870, new Position(1394, 4588, 0)),
		TAVERLY_DUNGEON_2(60, 7000, 52852, new Position(2912, 9810, 0), 52865, new Position(1000, 4522, 0)),
		VARRICK_SEWERS(65, 8500, 52863, new Position(3164, 9878, 0), 52876, new Position(1312, 4590, 0)),
		CHAOS_TUNNELS(70, 9600, 52858, new Position(3160, 5521, 0), 52874, new Position(1238, 4524, 0)),
		AL_KHARID(75, 11400, 52860, new Position(3298, 3307, 0), 52872, new Position(1182, 4515, 0)),
		BRIMHAVEM_DUNGEON(80, 12800, 77579, new Position(2697, 9442, 0), 77580, new Position(1140, 4499, 0)),
		POLYPORE_DUNGEON(82, 13500, 64291, new Position(4661, 5491, 3), 64291, new Position(4695, 5625, 3)),
		ASGARNIAN_ICE_DUNGEON(85, 15000, 52859, new Position(3033, 9599, 0), 52875, new Position(1297, 4510, 0))
		//,BRAINDEATH_ISLAND(50, 0)

		;

		private ResourceDungeon(int level, int xp) {
			outsideId = insideId = -1;
		}

		private int level, outsideId, insideId;
		private double xp;
		private Position inside, outside;

		private ResourceDungeon(int level, double xp, int outsideId, Position outside, int insideId, Position inside) {
			this.level = level;
			this.xp = xp;
			this.outsideId = outsideId;
			this.outside = outside;
			this.insideId = insideId;
			this.inside = inside;
		}
	}

	public DungManager(Player player) {
		setPlayer(player);
		reset();
		this.tokens = player.getDungTokens();
	}

	public void setPlayer(Player player) {
		this.player = player;
		if (cVersion != version)
			reset();
		else if (visitedResources == null) //temporary
			visitedResources = new boolean[ResourceDungeon.values().length];
	}

	public boolean enterResourceDungeon(WorldObject object) {
		int i = 0;
		for (ResourceDungeon dung : ResourceDungeon.values()) {
			if (object.getId() == dung.outsideId || object.getId() == dung.insideId) {
				if (player.getSkills().getLevelForXp(Skills.DUNGEONEERING) < dung.level) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You need a dungeoneering level of " + dung.level + " to enter this dungeoneering resource.");
					return true;
				}
				if (dung == ResourceDungeon.POLYPORE_DUNGEON)
					Magic.sendTeleportSpell(player, 13288, 13285, 2516, 2517, 0, 0, object.getX() == 4695 && object.getY() == 5626 ? dung.outside : dung.inside, 1, false, Magic.OBJECT_TELEPORT);
				else
					Magic.sendTeleportSpell(player, 13288, 13285, 2516, 2517, 0, 0, object.getId() == dung.insideId ? dung.outside : dung.inside, 1, false, Magic.OBJECT_TELEPORT);
				if (!visitedResources[i]) {
					visitedResources[i] = true;
					player.getSkills().addXp(Skills.DUNGEONEERING, dung.xp);
				}
				return true;
			}
			i++;
		}
		return false;
	}
	
	public void addBindedItem(int itemId) {
		bindedItems.add(new Item(itemId));
	}
	
	public void setBindedAmmo(int itemId, int dg_bindedAmmoAmount) {
		if (bindedAmmo == null) {
			bindedAmmo = new Item(itemId);
		}
		bindedAmmo.setId(itemId);
		bindedAmmo.setAmount(dg_bindedAmmoAmount);
	}
	
	public void setVersion(byte version) {
		this.cVersion = version;
	}
	
	public byte getVersion() {
		return cVersion;
	}

	public void bind(Item item, int slot) {
		ItemDefinitions defs = item.getDefinitions();
		int bindId = DungeonUtils.getBindedId(item);
		if (bindId == -1)
			return;
		if (DungeonUtils.isBindAmmo(item)) {
			if (bindedAmmo != null && (!defs.isStackable() || bindedAmmo.getId() != bindId)) {
				player.getPackets().sendGameMessage("A currently bound item must be destroyed before another item may be bound.");
				return;
			}
			player.getInventory().deleteItem(slot, item);
			item.setId(bindId);
			player.getInventory().addItem(item);
			if (bindedAmmo == null)
				bindedAmmo = new Item(item);
			else
				bindedAmmo.setAmount(bindedAmmo.getAmount() + item.getAmount());
			if (bindedAmmo.getAmount() > 255)
				bindedAmmo.setAmount(255);
		} else {
			if (bindedItems.getUsedSlots() >= (DungeonUtils.getMaxBindItems(player.getSkills().getLevelForXp(Skills.DUNGEONEERING)) )) {
				player.getPackets().sendGameMessage("A currently bound item must be destroyed before another item may be bound.");
				return;
			}
			item.setId(bindId);
			player.getInventory().refresh(slot);
			bindedItems.add(new Item(item));
		}
		player.getPackets().sendGameMessage("You bind the " + defs.getName() + " to you. Check in the smuggler to manage your bound items.");
	}

	public void unbind(Item item) {
		if (bindedAmmo != null && bindedAmmo.getId() == item.getId())
			bindedAmmo = null;
		else
			bindedItems.remove(item);
	}

	public Item getBindedAmmo() {
		return bindedAmmo;
	}

	public boolean isInside() {
		return party != null && party.getDungeon() != null;
	}

	public ItemsContainer<Item> getBindedItems() {
		return bindedItems;
	}

	public void reset() {
		currentProgress = new boolean[DungeonConstants.FLOORS_COUNT];
		previousProgress = 0;
		bindedItems = new ItemsContainer<Item>(10, false);
		maxFloor  = 50;//1;
		maxComplexity = 6;
		visitedResources = new boolean[ResourceDungeon.values().length];
		cVersion = version;
	}

	public boolean isTickedOff(int floor) {
		if (floor - 1 < 0  || floor - 1 >= currentProgress.length)
			return false;
		return currentProgress[floor - 1];
	}

	public int getCurrentProgres() {
		int count = 0;
		for (boolean b : currentProgress)
			if (b)
				count++;
		return count;
	}
	
	public boolean getVisitedResourcesB(int pos) {
		return visitedResources[pos];
	}
	
	public boolean getCurrentProgresB(int pos) {
		return currentProgress[pos];
	}
	
	public void setCurrentProgres(int pos, boolean b) {
		currentProgress[pos] = b;
	}
	
	public void setVisitedResource(int pos, boolean b) {
		visitedResources[pos] = b;
	}

	public int getPreviousProgress() {
		return previousProgress;
	}
	
	public void setPreviousProgress(int progress) {
		this.previousProgress = progress;
	}

	public int getPrestige() {
		int currentProgress = getCurrentProgres();
		return currentProgress > previousProgress ? currentProgress : previousProgress;
	}

	public void tickOff(int floor) {
		if (floor - 1 < 0)
			return;
		currentProgress[floor - 1] = true;
		refreshCurrentProgress();
	}

	public void resetProgress() {
		previousProgress = getCurrentProgres();
		currentProgress = new boolean[DungeonConstants.FLOORS_COUNT];
		refreshCurrentProgress();
		refreshPreviousProgress();
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}
	
	public void addTokens(int amount) {
		this.tokens += amount;
	}

	public int getTokens() {
		return tokens;
	}

	public Object getRejoinKey() {
		return rejoinKey;
	}

	public void setRejoinKey(Object rejoinKey) {
		this.rejoinKey = rejoinKey;
	}

	public int getMaxFloor() {
		return maxFloor;
	}

	public void setMaxFloor(int maxFloor) {
		this.maxFloor = maxFloor;
	}

	public void increaseMaxFloor() {
		if (maxFloor == 60)
			return;
		maxFloor++;
	}

	public void increaseMaxComplexity() {
		maxComplexity++;
	}

	public int getMaxComplexity() {
		return maxComplexity;
	}

	public void setMaxComplexity(int maxComplexity) {
		this.maxComplexity = maxComplexity;
	}

	public void openPartyInterface() {
		//resizableScreen ? 114 : 174,
		player.getInterfaceManager().sendTab(player.getInterfaceManager().isResizableScreen() ? 114 :174, 939);//sendTab(207, 939);
		player.getPackets().sendGlobalConfig(234, 3);//Party Config Interface
		player.getPackets().sendGlobalConfig(168, 3);
		refreshFloor();
		refreshCurrentProgress();
		refreshPreviousProgress();
		refreshComplexity();
		refreshPartyDetailsComponents();
		refreshPartyGuideModeComponent();
		refreshNames();
	}

	public void refreshPartyGuideModeComponent() {
		if (!player.getInterfaceManager().containsInterface(939))
			return;
		player.getPackets().sendHideIComponent(939, 93, party == null || !party.getGuideMode());
	}

	/*
	 * called aswell when player added/removed to party
	 */
	public void refreshPartyDetailsComponents() {
		if (!player.getInterfaceManager().containsInterface(939))
			return;
		if (party != null) {
			if (party.isLeader(player)) { //is party leader stuff here
				player.getPackets().sendHideIComponent(939, 37, true);
				player.getPackets().sendHideIComponent(939, 31, false);
				player.getPackets().sendHideIComponent(939, 105, true);//Complexity change
				player.getPackets().sendHideIComponent(939, 111, true);//Floor change
				player.getPackets().sendHideIComponent(939, 38, false);
				player.getPackets().sendHideIComponent(939, 33, false);
			} else { // not party leader stuff here
				player.getPackets().sendHideIComponent(939, 37, true);
				player.getPackets().sendHideIComponent(939, 34, false);
				player.getPackets().sendHideIComponent(939, 31, true);
				player.getPackets().sendHideIComponent(939, 105, false);//Complexity change
				player.getPackets().sendHideIComponent(939, 111, false);//Floor change
				player.getPackets().sendHideIComponent(939, 38, true);
			}
		} else if (party == null) {
			player.sm("Party is null again.");
			player.getPackets().sendHideIComponent(939, 37, false);
			player.getPackets().sendHideIComponent(939, 34, true);
			player.getPackets().sendHideIComponent(939, 38, true);
			player.getPackets().sendHideIComponent(939, 36, false);
			player.getPackets().sendHideIComponent(939, 33, true);
			player.getPackets().sendHideIComponent(939, 105, false);//Complexity change
			player.getPackets().sendHideIComponent(939, 111, false);//Floor change
		}
	}

	public void pressOption(int playerIndex, int option) {
		player.stopAll();
		if (party == null || playerIndex >= party.getTeam().size())
			return;
		Player player = party.getTeam().get(playerIndex);
		if (player == null)
			return;
		DungeonManager dungeon = party.getDungeon();
		if (option == 0) {
			if (dungeon == null) {
				this.player.getPackets().sendGameMessage("You must be in a dungeon to do that.");
				return;
			}
			if (player == this.player) {
				this.player.getPackets().sendGameMessage("Why don't you just use your inventory and stat interfaces?");
				return;
			}
		} else if (option == 1) {
			if (player == this.player) {
				this.player.getPackets().sendGameMessage("You can't kick yourself!");
				return;
			}
			if (!party.isLeader(this.player)) {
				this.player.getPackets().sendGameMessage("Only your party's leader can kick a party member!");
				return;
			}
			if (player.isLocked() || dungeon != null && dungeon.isBossOpen()) {
				this.player.getPackets().sendGameMessage("You can't kick this player right now.");
				return;
			}
			player.getDungeoneeringManager().leaveParty();
		} else if (option == 2) {
			if (party.isLeader(player)) {
				this.player.getPackets().sendGameMessage("You can't promote the party leader.");
				return;
			}
			if (!party.isLeader(this.player)) {
				this.player.getPackets().sendGameMessage("Only your party's leader can promote a leader!");
				return;
			}
			party.setLeader(player);
			for (Player p2 : party.getTeam())
				party.refreshPartyDetails(p2);
		} else if (option == 3) {
			if (player != this.player) {
				this.player.getPackets().sendGameMessage("You can't switch another player shared-xp.");
				return;
			}
			player.getPackets().sendGameMessage("Shared xp is currently disabled.");
		}
	}

	public void invite() {
		if (party == null || !party.isLeader(player))
			return;
		player.stopAll();
		if (party.getDungeon() != null) {
			player.getPackets().sendGameMessage("You can't do that right now.");
			return;
		}
		player.getTemporaryAttributtes().put("DINVITE", Boolean.TRUE);
		player.getPackets().sendInputNameScript("Enter name:");
	}

	public void acceptInvite() {
		Player invitedBy = (Player) player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.DUNGEON_INVITED_BY);
		if (invitedBy == null)
			return;
		DungeonPartyManager party = invitedBy.getDungeoneeringManager().getParty();
		
		if (invitedBy.getDungeoneeringManager().invitingPlayer != player || party == null || !party.isLeader(invitedBy)) {
			player.closeInterfaces();
			player.getPackets().sendGameMessage("You can't do that right now.");
			return;
		}
		if (party.getTeam().size() >= 5) {
			player.closeInterfaces();
			player.getPackets().sendGameMessage("The party is full.");
			return;
		}
		if (party.getComplexity() > maxComplexity) {
			player.closeInterfaces();
			player.getPackets().sendGameMessage("You can't do this complexity.");
			return;
		}
		if (party.getFloor() > maxFloor) {
			player.closeInterfaces();
			player.getPackets().sendGameMessage("You can't do this floor.");
			return;
		}
		invitedBy.getDungeoneeringManager().resetInvitation();
		invitedBy.getDungeoneeringManager().getParty().setDifficulty(0);
		invitedBy.getDungeoneeringManager().getParty().add(player);
		player.stopAll();
		invitedBy.stopAll();
	}

	public void invite(String name) {
		player.stopAll();
		player.sm("invite send.");
		if (party == null) {
			final Player p2 = World.getPlayerByDisplayName(name);
			if (p2 == null) {
				player.getPackets().sendGameMessage("Unable to find " + name);
				return;
			}

			DungeonPartyManager party = p2.getDungeoneeringManager().getParty();
			if (p2.getDungeoneeringManager().invitingPlayer != player || player.getZ() != 0 || party == null || !party.isLeader(p2)) {
				player.getPackets().sendGameMessage("You can't do that right now.");
				return;
			}
			player.getTemporaryAttributtes().put(TemporaryAttributes.Key.DUNGEON_INVITED_BY, p2);
			player.getInterfaceManager().sendInterface(949);
			for (int i = 0; i < 5; i++) {
				Player teamMate = i >= party.getTeam().size() ? null : party.getTeam().get(i);
				player.getPackets().sendGlobalString(284 + i, teamMate == null ? "" : teamMate.getDisplayName());
				player.getPackets().sendGlobalConfig(1153 + i, teamMate == null ? -1 : teamMate.getSkills().getCombatLevelWithSummoning());
				player.getPackets().sendGlobalConfig(1158 + i, teamMate == null ? -1 : teamMate.getSkills().getLevelForXp(Skills.DUNGEONEERING));
				player.getPackets().sendGlobalConfig(1163 + i, teamMate == null ? -1 : teamMate.getSkills().getHighestSkillLevel());
				player.getPackets().sendGlobalConfig(1168 + i, teamMate == null ? -1 : teamMate.getSkills().getTotalLevel());
			}
			player.getPackets().sendGlobalConfig(1173, party.getFloor());
			player.getPackets().sendGlobalConfig(1174, party.getComplexity());
			player.setCloseInterfacesEvent(new Runnable() {

				@Override
				public void run() {
					p2.getDungeoneeringManager().expireInvitation();
					player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.DUNGEON_INVITED_BY);
				}

			});
		} else {
			if (!party.isLeader(player) || party.getDungeon() != null) {
				player.getPackets().sendGameMessage("You can't do that right now.");
				return;
			}
			if (party.getSize() >= 5) {
				player.getPackets().sendGameMessage("Your party is full.");
				return;
			}
			Player p2 = World.getPlayerByDisplayName(name);
			if (p2 == null) {
				player.getPackets().sendGameMessage("That player is offline, or has privacy mode enabled.");
				return;
			}
			
			/*if (!(p2.getControlerManager().getControler() instanceof Kalaboss)) {
				player.getPackets().sendGameMessage("You can only invite a player in or around Daemonheim.");
				return;
			}*/
			if (p2.getDungeoneeringManager().party != null) {
				player.getPackets().sendGameMessage(p2.getDisplayName() + " is already in a party.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade() || p2.isLocked()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			expireInvitation();
			invitingPlayer = p2;
			player.getPackets().sendGameMessage("Sending party invitation to " + p2.getDisplayName() + "...");
			p2.getPackets().sendDungeonneringRequestMessage(player);
		}

	}

	public void openResetProgress() {
		player.stopAll();
		player.getDialogueManager().startDialogue("PrestigeReset");
	}

	public void switchGuideMode() {
		if (party == null) {
			player.getPackets().sendGameMessage("You must be in a party to do that.");
			return;
		}
		if (party.getDungeon() != null) {
			player.getPackets().sendGameMessage("You cannot change the guide mode once the dungeon has started.");
			return;
		}
		if (!party.isLeader(this.player)) {
			this.player.getPackets().sendGameMessage("Only your party's leader can switch guide mode!");
			return;
		}
		player.stopAll();
		party.setGuideMode(!party.getGuideMode());
		if (party.getGuideMode())
			player.getPackets().sendGameMessage("Guide mode enabled. Your map will show you the critical path, but you will receive an xp penalty.");
		else
			player.getPackets().sendGameMessage("Guide mode disabled. Your map will no longer show the critical path.");
		for (Player p2 : party.getTeam())
			p2.getDungeoneeringManager().refreshPartyGuideModeComponent();
	}
	
	public static int PLAYER_1_FLOORS = 609;
	public static int PLAYER_2_FLOORS = 487;
	public static int PLAYER_3_FLOORS = 365;
	public static int PLAYER_4_FLOORS = 243;
	public static int PLAYER_5_FLOORS = 121;
	public static int PLAYER_1_FLOORS_COMPLETE = 670;
	public static int PLAYER_2_FLOORS_COMPLETE = 548;
	public static int PLAYER_3_FLOORS_COMPLETE = 426;
	public static int PLAYER_4_FLOORS_COMPLETE = 304;
	public static int PLAYER_5_FLOORS_COMPLETE = 183;

	public void changeFloor() {
		if (party == null) {
			player.getPackets().sendGameMessage("You must be in a party to do that.");
			return;
		}
		if (party.getDungeon() != null) {
			player.getPackets().sendGameMessage("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(this.player)) {
			this.player.getPackets().sendGameMessage("Only your party's leader can change floor!");
			return;
		}
		player.stopAll();
		player.getPackets().sendIComponentText(947, 765, ""+this.getParty().getFloor()); //MAYBE THIS BUGGED TODO
		player.getInterfaceManager().sendInterface(947);
		for (int i = 0; i < party.getMaxFloor(); i++)
			player.getPackets().sendHideIComponent(947, 48 + i, false);
		for (int index = party.getTeam().size() - 1; index >= 0; index--) {
			Player teamMate = party.getTeam().get(index);
			int startComponentCompleted = 0;
			int startComponentAvailable = 0;
			if (index == 0) {
				startComponentCompleted = PLAYER_1_FLOORS_COMPLETE;
				startComponentAvailable = PLAYER_1_FLOORS;
			} else if (index == 1) {
				startComponentCompleted = PLAYER_2_FLOORS_COMPLETE;
				startComponentAvailable = PLAYER_2_FLOORS;
			} else if (index == 2) {
				startComponentCompleted = PLAYER_3_FLOORS_COMPLETE;
				startComponentAvailable = PLAYER_3_FLOORS;
			} else if (index == 3) {
				startComponentCompleted = PLAYER_4_FLOORS_COMPLETE;
				startComponentAvailable = PLAYER_4_FLOORS;
			} else if (index == 4) {
				startComponentCompleted = PLAYER_5_FLOORS_COMPLETE;
				startComponentAvailable = PLAYER_5_FLOORS;
			}
			for (int floor = 1; floor < teamMate.getDungeoneeringManager().getMaxFloor(); floor++) {
				player.getPackets().sendHideIComponent(947, startComponentAvailable + floor - 1, false);
				if (teamMate.getDungeoneeringManager().currentProgress[floor - 1])
					player.getPackets().sendHideIComponent(947, startComponentCompleted + floor - 2, false);
			}
		}
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.DUNG_FLOOR);
			}
		});
	}

	public void selectFloor(int floor) {
		if (party == null) {
			player.getPackets().sendGameMessage("You must be in a party to do that.");
			return;
		}
		/*
		 * cant happen, cuz u cant click anyway but oh well
		 */
		if (party.getMaxFloor() < party.getMaxFloor()) {
			player.getPackets().sendGameMessage("A member in your party can't do this floor.");
			return;
		}
		player.getPackets().sendIComponentText(947, 765, "" + floor);
		player.getTemporaryAttributtes().put(TemporaryAttributes.Key.DUNG_FLOOR, floor);
	}

	public void confirmFloor() {
		Integer selectedFloor = (Integer) player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.DUNG_FLOOR);
		player.stopAll();
		if (party == null) {
			player.getPackets().sendGameMessage("You must be in a party to do that.");
			return;
		}
		if (selectedFloor == null) {
			selectedFloor = 0;
			player.sendMessage("You need to select a floor to continue.");
		}
		if (party.getMaxFloor() < party.getMaxFloor()) {
			player.getPackets().sendGameMessage("A member in your party can't do this floor.");
			return;
		}
		if (party.getDungeon() != null) {
			player.getPackets().sendGameMessage("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(player)) {
			player.getPackets().sendGameMessage("Only your party's leader can change floor!");
			return;
		}
		party.setFloor(selectedFloor);
		if (player.getTemporaryAttributtes().remove("changingfloor") != null)
			enterDungeon(true, true, false);
	}

	public void changeComplexity() {
		if (party == null) {
			player.getPackets().sendGameMessage("You must be in a party to do that.");
			return;
		}
		if (party.getDungeon() != null) {
			player.getPackets().sendGameMessage("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(player)) {
			player.getPackets().sendGameMessage("Only your party's leader can change complexity!");
			return;
		}
		player.stopAll();
		player.getInterfaceManager().sendInterface(938);
		selectComplexity(party.getComplexity());
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.DUNG_COMPLEXITY);
			}
		});
	}

	public void selectComplexity(int complexity) {
		if (party == null) {
			player.getPackets().sendGameMessage("You must be in a party to do that.");
			return;
		}
		if (party.getMaxComplexity() < complexity) {
			player.getPackets().sendGameMessage("A member in your party can't do this complexity.");
			return;
		}
		Integer selectedComplexity = (Integer) player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.DUNG_COMPLEXITY);
		if (selectedComplexity != null) {
			markComplexity(selectedComplexity, false);
		}
		markComplexity(complexity, true);
		hideSkills(complexity);
		int penalty = complexity == 6 ? 0 : ((6 - complexity) * 5 + 25);
		player.getPackets().sendIComponentText(938, 42, "" + complexity);
		player.getPackets().sendIComponentText(938, 119, penalty + "% XP Penalty");
		player.getTemporaryAttributtes().put(TemporaryAttributes.Key.DUNG_COMPLEXITY, complexity);
	}

	public void confirmComplexity() {
		Integer selectedComplexity = (Integer) player.getTemporaryAttributtes().remove(TemporaryAttributes.Key.DUNG_COMPLEXITY);
		player.stopAll();
		if (selectedComplexity == null) {
			player.sendMessage("You need to select a complexity to continue.");
			return;
		}
		if (party == null) {
			player.getPackets().sendGameMessage("You must be in a party to do that.");
			return;
		}
		if (party.getMaxComplexity() < selectedComplexity) {
			player.getPackets().sendGameMessage("A member in your party can't do this complexity.");
			return;
		}
		if (party.getDungeon() != null) {
			player.getPackets().sendGameMessage("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(player)) {
			player.getPackets().sendGameMessage("Only your party's leader can change complexity!");
			return;
		}
		party.setComplexity(selectedComplexity);
		if (player.getTemporaryAttributtes().remove("changingcomplexity") != null)
			enterDungeon(true, true, false);
	}

	private void markComplexity(int complexity, boolean mark) {
		player.getPackets().sendHideIComponent(938, 57 + ((complexity - 1) * 5), !mark);
	}

	private static final String[] COMPLEXITY_SKILLS =
		{
		"Combat",
		"Cooking",
		"Firemaking",
		"Woodcutting",
		"Fishing",
		"Creating Weapons",
		"Mining",
		"Runecrafting",
		"Farming Textiles",
		"Hunting",
		"Creating Armour",
		"Farming Seeds",
		"Herblore",
		"Thieving",
		"Summoning",
		"Construction" };

	private void hideSkills(int complexity) {
		int count = 0;
		if (complexity >= 1)
			count += 1;
		if (complexity >= 2)
			count += 4;
		if (complexity >= 3)
			count += 3;
		if (complexity >= 4)
			count += 3;
		if (complexity >= 5)
			count += 5;
		if (complexity >= 6)
			count += 1;
		for (int i = 0; i < COMPLEXITY_SKILLS.length; i++) {
			player.getPackets().sendIComponentText(938, 88 + (i * 2), (i >= count ? "<col=383838>" : "") + COMPLEXITY_SKILLS[i]);
		}
	}

	public void expireInvitation() {
		if (invitingPlayer == null)
			return;
		player.getPackets().sendGameMessage("Your dungeon party invitation to " + invitingPlayer.getDisplayName() + " has expired.");
		invitingPlayer.getPackets().sendGameMessage("A dungeon party invitation from " + player.getDisplayName() + " has expired.");
		invitingPlayer = null;
	}

	public void enterDungeon(boolean selectSize, boolean ignoreReset, boolean start) {
		player.stopAll();
		expireInvitation();
		/*if (!ignoreReset && !player.resetDg && player.getSkills().getLevelForXp(Skills.DUNGEONEERING) > 1 && player.getDungeoneeringTokens() > 0) {
			player.getDialogueManager().startDialogue("ResetDungeoneeringD");
			return;
		}*/
		if (party == null) {
		/*	party = new DungeonPartyManager();
			party.add(player);
		//	player.sm("party null");  */
			player.getDialogueManager().startDialogue("DungeonPartyStart");
			return;
		}
		if (party.getDungeon() != null) //cant happen
			return;
		if (!party.isLeader(player)) {
			player.getPackets().sendGameMessage("Only your party's leader can start a dungeon!");
			return;
		}
	/*	party.setFloor(48); // for testing
		party.setComplexity(5); 
		party.setDifficulty(5); */
		if (party.getFloor() == 0) {
			changeFloor();
			
			
			player.getTemporaryAttributtes().put("changingfloor", Boolean.TRUE);
			return;
		}
		if (party.getComplexity() == 0) {
			changeComplexity();
			player.getTemporaryAttributtes().put("changingcomplexity", Boolean.TRUE);
			return;
		}
		if (party.getDifficulty() == 0) {
			if (party.getTeam().size() == 1)
				party.setDifficulty(1);
			else {
				player.getDialogueManager().startDialogue("DungeonDifficulty", party.getTeam().size());
				return;
			}
		}

		boolean solo = party.getTeam().size() == 1;
		if (solo)
			party.setKeyShare(true);
		else if (party.getKeyType() == 0) {
			player.getDialogueManager().startDialogue("PreShareD");
			return;
		}
		if (selectSize) {
			if (party.getComplexity() == 6) {
				player.getDialogueManager().startDialogue("DungeonSize");
				return;
			} else
				party.setSize(DungeonConstants.SMALL_DUNGEON);
		}  
		for (Player p2 : party.getTeam()) {
			for (Item item : p2.getInventory().getItems().getItems()) {
				if (item != null && !item.getDefinitions().isRingOfKinship()) {
					player.getPackets().sendGameMessage(p2.getDisplayName() + " is carrying items that cannot be taken into Daemonheim.");
					return;
				}
			}
			for (Item item : p2.getEquipment().getItems().getItems()) {
				if (item != null && !item.getDefinitions().isRingOfKinship()) {
					player.getPackets().sendGameMessage(p2.getDisplayName() + " is carrying items that cannot be taken into Daemonheim.");
					return;
				}
			}
			if (!Kalaboss.isAtKalaboss(p2)) {
				player.getPackets().sendGameMessage(p2.getDisplayName() + " is too far away from Daemonheim to start the dungeon.");
				return;
			}
			if (p2.getFamiliar() != null || p2.getPet() != null) {
				player.getPackets().sendGameMessage(p2.getDisplayName() + " is carrying a familiar that cannot be taken into Daemonheim.");
				return;
			}
			if (p2.getZ() != 0 || p2.getInterfaceManager().containsScreenInter() || p2.isLocked() /*|| !(p2.getControlerManager().getControler() instanceof Kalaboss)*/) {
				player.getPackets().sendGameMessage(p2.getDisplayName() + " is busy.");
				return;
			}
		}
		if (!start) {
			player.getDialogueManager().startDialogue("DungeoneeringStartD");
			return;
		} 
		party.start();
	}

	public void setSize(int size) {
		if (party == null || !party.isLeader(player) || party.getComplexity() != 6)
			return;
		party.setSize(size);
	}

	public void setDifficulty(int dificulty) {
		if (party == null || !party.isLeader(player))
			return;
		party.setDifficulty(dificulty);
	}

	public void setKeyShare(boolean isKeyShare) {
		if (party == null || !party.isLeader(player))
			return;
		party.setKeyShare(isKeyShare);
	}

	public void resetInvitation() {
		if (invitingPlayer == null)
			return;
		invitingPlayer = null;
	}
	
	public static final int[][] CONFIGS = { { 59, 60, 292, 1175 }, { 62, 63, 293, 1176 }, { 65, 66, 294, 1177 }, { 68, 69, 295, 1178 }, { 71, 72, 296, 1179 }, };
	
	
	public void refreshNames() {
		if (party == null){
			return;
		}
		int index = 0;
		for (int i = 0; i < 5; i++) {
			Player p2 = i >= party.getTeam().size() ? null : party.getTeam().get(i);
			player.getPackets().sendHideIComponent(939, CONFIGS[index][0], p2 == null);
			player.getPackets().sendHideIComponent(939, CONFIGS[index][1], p2 == null);
			player.getPackets().sendGlobalString(CONFIGS[index][2], p2 != null ? p2.getDisplayName() : "");
			player.getPackets().sendGlobalConfig(CONFIGS[index][3], p2 == null ? 0 : 1);
			index++;
		}
	}

	public void refreshFloor() {
		player.getPackets().sendGlobalConfig(1180, party == null ? 0 : party.getFloor());
	}

	public void refreshComplexity() {
		player.getPackets().sendGlobalConfig(1183, party == null ? 0 : party.getComplexity());
	}

	public void refreshCurrentProgress() {
		player.getPackets().sendGlobalConfig(1181, getCurrentProgres());
	}

	public void refreshPreviousProgress() {
		player.getPackets().sendGlobalConfig(1182, previousProgress);
	}

	public DungeonPartyManager getParty() {
		return party;
	}

	public void setParty(DungeonPartyManager party2) {
		//player.sm("Party set.");
		this.party = party2;
	//	player.sm("Party set: "+party.getLeader()+ "  "+party2.getLeader());
	}

	//TODO location check
	public void formParty() {
		if (party != null){
			player.sm("already in a party.");
			return;
		}
		/*if (!(player.getControlerManager().getControler() instanceof Kalaboss) || player.getPlane() != 0) {
			player.getPackets().sendGameMessage("You can only form a party in or around Daemonheim.");
			return;
		}*/
		player.stopAll();
		new DungeonPartyManager().add(player);
		
	}

	public void finish() {
		if (party != null)
			party.leaveParty(player, true);
	}

	public void checkLeaveParty() {
		if (party == null)
			return;
		if (party.getDungeon() != null)
			player.getDialogueManager().startDialogue("DungeonLeaveParty");
		else
			leaveParty();
	}

	public void leaveParty() {
		if (party != null)
			party.leaveParty(player, false);
	}
}
