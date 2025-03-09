package com.rs.game.world.entity.player.content.skills.dungeoneering;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.cores.FixedLengthRunnable;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.MapBuilder;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.dungeonnering.*;
import com.rs.game.world.entity.player.CombatDefinitions;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.ItemConstants;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants.KeyDoors;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants.SkillDoors;
import com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.BossRoom;
import com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.HandledPuzzleRoom;
import com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.StartRoom;
import com.rs.game.world.entity.player.content.skills.dungeoneering.rooms.puzzles.PoltergeistRoom;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.DungeoneeringFishing;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.DungeoneeringMining;
import com.rs.game.world.entity.player.content.skills.dungeoneering.skills.farming.DungeoneeringPatch;
import com.rs.game.world.entity.player.controller.impl.DungeonController;
import com.rs.utility.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class DungeonManager {

	private static final Map<Object, DungeonManager> cachedDungeons = Collections.synchronizedMap(new HashMap<Object, DungeonManager>());
	public static final AtomicLong keyMaker = new AtomicLong();

	private DungeonPartyManager party;
	private Dungeon dungeon;
	private VisibleRoom[][] visibleMap;
	private int[] boundChuncks;
	private int stage; // 0 - not loaded. 1 - loaded. 2 - new one not loaded,
						// old one loaded(rewards screen)
	private RewardsTimer rewardsTimer;
	private DestroyTimer destroyTimer;
	private long time;
	private List<DungeonConstants.KeyDoors> keyList;
	private List<String> farmKeyList;
	private List<DungeoneeringPatch> patches;
	private String key;

	private Position groupGatestone;
	private List<MastyxTrap> mastyxTraps;

	// force saving deaths
	private Map<String, Integer> partyDeaths;

	private boolean tutorialMode, skippedPuzzle;

	private DungeonBoss temporaryBoss; // must for gravecreeper, cuz... it gets
										// removed from npc list :/

	public boolean hasSkippedPuzzle() {
		return skippedPuzzle;
	}

	public void setSkippedPuzzle(boolean value) {
		this.skippedPuzzle = value;
	}
	
	// 7554
	public DungeonManager(DungeonPartyManager party) {
		this.party = party;
		tutorialMode = party.getMaxComplexity() < 6;
		load();
		keyList = new CopyOnWriteArrayList<DungeonConstants.KeyDoors>();
		farmKeyList = new CopyOnWriteArrayList<String>();
		mastyxTraps = new CopyOnWriteArrayList<MastyxTrap>();
		patches = new CopyOnWriteArrayList<DungeoneeringPatch>();
		partyDeaths = new ConcurrentHashMap<String, Integer>();
	}

	public void clearKeyList() {
		for (Player player : party.getTeam()) {
			//player.getPackets().sendRunScript(6072);
			//player.getPackets().sendGlobalConfig(1875, 0); // forces refresh
		}
	}
	
	public int[] getBoundChunks() {
		return boundChuncks;
	}

	public void setKey(DungeonConstants.KeyDoors key, boolean add) {
		if (add) {
			keyList.add(key);
			for (Player player : party.getTeam())
				player.getPackets().sendGameMessage("<col=D2691E>Your party found a key: " + ItemDefinitions.getItemDefinitions(key.getKeyId()).getName());
		} else {
			keyList.remove(key);
			for (Player player : party.getTeam())
				player.getPackets().sendGameMessage("<col=D2691E>Your party used a key: " + ItemDefinitions.getItemDefinitions(key.getKeyId()).getName());
		}
	}

	public boolean isAtBossRoom(Position tile) {
		return isAtBossRoom(tile, -1, -1, false);
	}

	public boolean isAtBossRoom(Position tile, int x, int y, boolean check) {
		Room room = getRoom(getCurrentRoomReference(tile));
		if (room == null || !(room.getRoom() instanceof BossRoom))
			return false;
		if (check) {
			BossRoom bRoom = (BossRoom) room.getRoom();
			if (x != bRoom.getChunkX() || y != bRoom.getChunkY())
				return false;
		}
		return true;
	}

	public boolean isBossOpen() {
		for (int x = 0; x < visibleMap.length; x++) {
			for (int y = 0; y < visibleMap[x].length; y++) {
				VisibleRoom room = visibleMap[x][y];
				if (room == null || !room.isLoaded())
					continue;
				if (isAtBossRoom(getRoomCenterTile(room.reference)))
					return true;
			}
		}
		return false;
	}

	/*
	 * dont use
	 */
	public void refreshKeys(Player player) {
		for (DungeonConstants.KeyDoors key : keyList)
			player.getPackets().sendGlobalConfig(1812 + key.getIndex(), 1);
		player.getPackets().sendGlobalConfig(1875, keyList.contains(DungeonConstants.KeyDoors.GOLD_SHIELD) ? 1 : 0);
	}

	public boolean hasKey(DungeonConstants.KeyDoors key) {
		return keyList.contains(key);
	}

	public boolean isKeyShare() {
		return party.isKeyShare();
	}

	/*
	 * when dung ends to make sure no1 dies lo, well they can die but still..
	 */
	public void clearGuardians() {
		for (int x = 0; x < visibleMap.length; x++)
			for (int y = 0; y < visibleMap[x].length; y++)
				if (visibleMap[x][y] != null)
					visibleMap[x][y].forceRemoveGuardians();
	}

	public int getVisibleRoomsCount() {
		int count = 0;
		for (int x = 0; x < visibleMap.length; x++)
			for (int y = 0; y < visibleMap[x].length; y++)
				if (visibleMap[x][y] != null)
					count++;
		return count;
	}

	public int getVisibleBonusRoomsCount() {
		int count = 0;
		for (int x = 0; x < visibleMap.length; x++)
			for (int y = 0; y < visibleMap[x].length; y++)
				if (visibleMap[x][y] != null && !dungeon.getRoom(new RoomReference(x, y)).isCritPath())
					count++;
		return count;
	}

	public int getLevelModPerc() {
		int totalGuardians = 0;
		int killedGuardians = 0;

		for (int x = 0; x < visibleMap.length; x++)
			for (int y = 0; y < visibleMap[x].length; y++)
				if (visibleMap[x][y] != null) {
					totalGuardians += visibleMap[x][y].getGuardiansCount();
					killedGuardians += visibleMap[x][y].getKilledGuardiansCount();
				}

		return totalGuardians == 0 ? 100 : killedGuardians * 100 / totalGuardians;
	}

	public boolean enterRoom(Player player, int x, int y) {
		if (x < 0 || y < 0 || x >= visibleMap.length || y >= visibleMap[0].length) {
			// System.out.println("ivalid room");
			return false;
		}
		RoomReference roomReference = getCurrentRoomReference(player);
		player.lock(0);
		if (visibleMap[x][y] != null) {
			if (!visibleMap[x][y].isLoaded())
				return false;
			int xOffset = x - roomReference.getX();
			int yOffset = y - roomReference.getY();
			player.setNextPosition(new Position(player.getX() + xOffset * 3, player.getY() + yOffset * 3, 0));
			if (player.getFamiliar() != null)
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						if (player.getFamiliar() != null)
							player.getFamiliar().call(false);
					}
				});
			playMusic(player, new RoomReference(x, y));
			//player.unlock();
			return true;
		} else {
			//player.unlock();
			loadRoom(x, y);
			return false;
		}
	}

	public void loadRoom(int x, int y) {
		loadRoom(new RoomReference(x, y));
	}

	public void loadRoom(final RoomReference reference) {
		final Room room = dungeon.getRoom(reference);
		if (room == null)
			return;
		VisibleRoom vr;
		if (room.getRoom() instanceof HandledPuzzleRoom) {
			HandledPuzzleRoom pr = (HandledPuzzleRoom) room.getRoom();
			vr = pr.createVisibleRoom();
		} else {
			vr = new VisibleRoom();
		}
		visibleMap[reference.getX()][reference.getY()] = vr;
		vr.init(this, reference, party.getFloorType(), room.getRoom());
		openRoom(room, reference, visibleMap[reference.getX()][reference.getY()]);
		//openroom was in slowexe, moved out. Seems unnecessary.
		/*CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		});*/
	}

	public boolean isDestroyed() {
		return dungeon == null;
	}

	public void openRoom(final Room room, final RoomReference reference, final VisibleRoom visibleRoom) {
		final int toChunkX = boundChuncks[0] + reference.getX() * 2;
		final int toChunkY = boundChuncks[1] + reference.getY() * 2;
		MapBuilder.copy2RatioSquare(room.getChunkX(party.getComplexity()), room.getChunkY(party.getFloorType()), toChunkX, toChunkY, room.getRotation(), 0, 1);
		int regionId = (((toChunkX / 8) << 8) + (toChunkY / 8));
		for (Player player : party.getTeam()) {
			if (!player.getMapRegionsIds().contains(regionId))
				continue;
			player.setForceNextMapLoadRefresh(true);
			player.loadMapRegions();
		}
		World.executeAfterLoadRegion(regionId, new Runnable() {

			@Override
			public void run() {
				if (isDestroyed())
					return;
				room.openRoom(DungeonManager.this, reference);
				visibleRoom.openRoom();
				for (int i = 0; i < room.getRoom().getDoorDirections().length; i++) {
					Door door = room.getDoor(i);
					if (door == null)
						continue;
					int rotation = (room.getRoom().getDoorDirections()[i] + room.getRotation()) & 0x3;
					if (door.getType() == DungeonConstants.KEY_DOOR) {
						KeyDoors keyDoor = KeyDoors.values()[door.getId()];
						setDoor(reference, keyDoor.getObjectId(), keyDoor.getDoorId(party.getFloorType()), rotation);
					} else if (door.getType() == DungeonConstants.GUARDIAN_DOOR) {
						setDoor(reference, -1, DungeonConstants.DUNGEON_GUARDIAN_DOORS[party.getFloorType()], rotation);
						if (visibleRoom.roomCleared()) // remove referene since
														// done
							room.setDoor(i, null);
					} else if (door.getType() == DungeonConstants.SKILL_DOOR) {
						SkillDoors skillDoor = SkillDoors.values()[door.getId()];
						int type = party.getFloorType();
						int closedId = skillDoor.getClosedObject(type);
						int openId = skillDoor.getOpenObject(type);
						setDoor(reference, openId == -1 ? closedId : -1, openId != -1 ? closedId : -1, rotation);
					}
				}
				if (room.getRoom().allowResources())
					setResources(room, reference, toChunkX, toChunkY);

				if (room.getDropId() != -1)
					setKey(room, reference, toChunkX, toChunkY);
				visibleRoom.setLoaded();
			}

		});
	}

	public void setDoor(RoomReference reference, int lockObjectId, int doorObjectId, int rotation) {
		if (lockObjectId != -1) {
			int[] xy = DungeonManager.translate(1, 7, rotation, 1, 2, 0);
			World.spawnObject(new WorldObject(lockObjectId, 10, rotation, ((boundChuncks[0] * 8) + reference.getX() * 16) + xy[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + xy[1], 0));
		}
		if (doorObjectId != -1) {
			int[] xy = DungeonManager.translate(0, 7, rotation, 1, 2, 0);
			World.spawnObject(new WorldObject(doorObjectId, 10, rotation, ((boundChuncks[0] * 8) + reference.getX() * 16) + xy[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + xy[1], 0));
		}
	}

	public void setKey(Room room, RoomReference reference, int toChunkX, int toChunkY) {
		int[] loc = room.getRoom().getKeySpot();
		if (loc != null) {
			spawnItem(reference, new Item(room.getDropId()), loc[0], loc[1], true);
			return;
		}
		spawnItem(reference, new Item(room.getDropId()), 7, 1, true);
	}

	public void setResources(Room room, RoomReference reference, int toChunkX, int toChunkY) {
		if (party.getComplexity() >= 5 && com.rs.utility.Utils.random(3) == 0) { // sets thief
																	// chest
			for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
				int rotation = com.rs.utility.Utils.random(DungeonConstants.WALL_BASE_X_Y.length);
				int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
				int x = b[0] + com.rs.utility.Utils.random(b[2]);
				int y = b[1] + com.rs.utility.Utils.random(b[3]);
				if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
					continue;
				if (!World.isFloorFree(0, toChunkX * 8 + x, toChunkY * 8 + y) || !World.isTileFree(0, toChunkX * 8 + x - Utils.ROTATION_DIR_X[((rotation + 3) & 0x3)], toChunkY * 8 + y - Utils.ROTATION_DIR_Y[((rotation + 3) & 0x3)], 0))
					continue;
				room.setThiefChest(com.rs.utility.Utils.random(10));
				World.spawnObject(new WorldObject(DungeonConstants.THIEF_CHEST_LOCKED[party.getFloorType()], 10, ((rotation + 3) & 0x3), toChunkX * 8 + x, toChunkY * 8 + y, 0));
				break;
			}
		}
		if (party.getComplexity() >= 4 && com.rs.utility.Utils.random(3) == 0) { // sets flower
			for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
				int rotation = com.rs.utility.Utils.random(DungeonConstants.WALL_BASE_X_Y.length);
				int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
				int x = b[0] + com.rs.utility.Utils.random(b[2]);
				int y = b[1] + com.rs.utility.Utils.random(b[3]);
				if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
					continue;
				if (!World.isFloorFree(0, toChunkX * 8 + x, toChunkY * 8 + y))
					continue;
				World.spawnObject(new WorldObject(DungeonUtils.getFarmingResource(com.rs.utility.Utils.random(10), party.getFloorType()), 10, rotation, toChunkX * 8 + x, toChunkY * 8 + y, 0));

				break;
			}
		}
		if (party.getComplexity() >= 3 && com.rs.utility.Utils.random(3) == 0) { // sets rock
			for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
				int rotation = com.rs.utility.Utils.random(DungeonConstants.WALL_BASE_X_Y.length);
				int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
				int x = b[0] + com.rs.utility.Utils.random(b[2]);
				int y = b[1] + com.rs.utility.Utils.random(b[3]);
				if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
					continue;
				if (!World.isFloorFree(0, toChunkX * 8 + x, toChunkY * 8 + y))
					continue;
				World.spawnObject(new WorldObject(DungeonUtils.getMiningResource(com.rs.utility.Utils.random(DungeoneeringMining.DungeoneeringRocks.values().length), party.getFloorType()), 10, rotation, toChunkX * 8 + x, toChunkY * 8 + y, 0));

				break;
			}
		}
		if (party.getComplexity() >= 2 && com.rs.utility.Utils.random(3) == 0) { // sets tree
			for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
				int rotation = com.rs.utility.Utils.random(DungeonConstants.WALL_BASE_X_Y.length);
				int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
				int x = b[0] + com.rs.utility.Utils.random(b[2]);
				int y = b[1] + com.rs.utility.Utils.random(b[3]);
				if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
					continue;
				if (!World.isWallsFree(0, toChunkX * 8 + x, toChunkY * 8 + y) || !World.isFloorFree(0, toChunkX * 8 + x, toChunkY * 8 + y))
					continue;
				x -= Utils.ROTATION_DIR_X[rotation];
				y -= Utils.ROTATION_DIR_Y[rotation];
				
				World.spawnObject(new WorldObject(DungeonUtils.getWoodcuttingResource(com.rs.utility.Utils.random(10), party.getFloorType()), 10, rotation, toChunkX * 8 + x, toChunkY * 8 + y, 0));
				break;
			}
		}
		if (party.getComplexity() >= 2) { // sets fish spot
			List<int[]> fishSpots = new ArrayList<int[]>();
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					WorldObject o = World.getObjectWithType(new Position(toChunkX * 8 + x, toChunkY * 8 + y, 0), 10);
					if (o == null || o.getId() != DungeonConstants.FISH_SPOT_OBJECT_ID)
						continue;
					fishSpots.add(new int[] { x, y });
				}
			}
			if (!fishSpots.isEmpty()) {
				int[] spot = fishSpots.get(com.rs.utility.Utils.random(fishSpots.size()));
				spawnNPC(DungeonConstants.FISH_SPOT_NPC_ID, room.getRotation(), new Position(toChunkX * 8 + spot[0], toChunkY * 8 + spot[1], 0), reference, DungeonConstants.FISH_SPOT_NPC, 1);

			}
		}
	}

	public Position getRoomCenterTile(RoomReference reference) {
		return getRoomBaseTile(reference).transform(8, 8, 0);
	}

	public Position getRoomBaseTile(RoomReference reference) {
		return new Position(((boundChuncks[0] << 3) + reference.getX() * 16), ((boundChuncks[1] << 3) + reference.getY() * 16), 0);
	}

	public RoomReference getCurrentRoomReference(Position tile) {
		if (tile == null)
			return null;
		if (boundChuncks == null)
			return null;
		return new RoomReference((tile.getChunkX() - boundChuncks[0]) / 2, ((tile.getChunkY() - boundChuncks[1]) / 2));
	}

	public Room getRoom(RoomReference reference) {
		return dungeon == null ? null : dungeon.getRoom(reference);
	}

	public VisibleRoom getVisibleRoom(RoomReference reference) {
		if (reference.getX() < 0 || reference.getY() < 0)
			return null;
		if (reference.getX() >= visibleMap.length || reference.getY() >= visibleMap[0].length)
			return null;
		return visibleMap[reference.getX()][reference.getY()];
	}

	public Position getHomeTile() {
		return getRoomCenterTile(dungeon.getStartRoomReference());
	}

	public void telePartyToRoom(RoomReference reference) {
		Position tile = getRoomCenterTile(reference);
		for (Player player : party.getTeam()) {
			player.setNextPosition(tile);
			playMusic(player, reference);
		}
	}

	public void playMusic(Player player, RoomReference reference) {
		if (reference.getX() < 0)
			return;
		if (reference.getX() >= visibleMap.length - 1 || reference.getY() >= visibleMap[reference.getX()].length)
			return;
		player.getMusicsManager().forcePlayMusic(visibleMap[reference.getX()][reference.getY()].getMusicId());
	}

	public void linkPartyToDungeon() {
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				for (Player player : party.getTeam()) {
					player.lock();
					resetItems(player, false, false);
					sendSettings(player);
					if (party.getComplexity() >= 5 && party.isLeader(player))
						player.getInventory().addItem(new Item(DungeonConstants.GROUP_GATESTONE));
					removeMark(player);
					sendStartItems(player);
					player.getPackets().sendGameMessage("");
					player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
					player.getPackets().sendGameMessage("Floor <col=641d9e>" + party.getFloor() + "    <col=ffffff>Complexity <col=641d9e>" + party.getComplexity());
					String[] sizeNames = new String[] { "Small", "Medium", "Large", "Test" };
					player.getPackets().sendGameMessage("Dungeon Size: " + "<col=641d9e>" + sizeNames[party.getSize()]);
					player.getPackets().sendGameMessage("Party Size:Difficulty <col=641d9e>" + party.getTeam().size() + ":" + party.getDifficulty());
					player.getPackets().sendGameMessage("Pre-Share: <col=641d9e>" + (isKeyShare() ? "OFF" : "ON"));

					if (party.isGuideMode()) {
						player.getPackets().sendGameMessage("<col=641d9e>Guide Mode ON");
						player.getDungeoneeringManager().refreshPartyGuideModeComponent();
					}
					player.getPackets().sendGameMessage("");
					//player.lock(2);
					player.unlock();
				}
				resetGatestone();
				
			}
		});
	}

	public void setTableItems(RoomReference room) {
		addItemToTable(room, new Item(16295));
		if (party.getComplexity() >= 2) {
			addItemToTable(room, new Item(DungeonConstants.RUSTY_COINS, 5000 + com.rs.utility.Utils.random(10000)));
			addItemToTable(room, new Item(17678));
			addItemToTable(room, new Item(16361));
			addItemToTable(room, new Item(17794));
		}
		if (party.getComplexity() >= 3) {
			int rangeTier = DungeonUtils.getTier(party.getMaxLevel(Skills.RANGE));
			if (rangeTier > 8)
				rangeTier = 8;
			addItemToTable(room, new Item(DungeonUtils.getArrows(1 + com.rs.utility.Utils.random(rangeTier)), 90 + com.rs.utility.Utils.random(30)));
			addItemToTable(room, new Item(DungeonConstants.RUNES[0], 90 + com.rs.utility.Utils.random(30)));
			addItemToTable(room, new Item(DungeonConstants.RUNE_ESSENCE, 90 + com.rs.utility.Utils.random(30)));
			addItemToTable(room, new Item(17754));
			addItemToTable(room, new Item(17883));
			if (party.getComplexity() >= 4)
				addItemToTable(room, new Item(17446));
		}
		for (@SuppressWarnings("unused")
		Player player : party.getTeam()) {
			for (int i = 0; i < 7 + com.rs.utility.Utils.random(4); i++)
				addItemToTable(room, new Item(DungeonUtils.getFood(1 + com.rs.utility.Utils.random(8))));
			if (party.getComplexity() >= 3) {
				for (int i = 0; i < 1 + com.rs.utility.Utils.random(3); i++)
					addItemToTable(room, new Item(DungeonUtils.getRandomGear(1 + com.rs.utility.Utils.random(8))));
			}
		}
	}

	public void addItemToTable(RoomReference room, Item item) {
		int slot = com.rs.utility.Utils.random(10); // 10 spaces for items
		if (slot < 6)
			spawnItem(room, item, 9 + com.rs.utility.Utils.random(3), 10 + com.rs.utility.Utils.random(2), false);
		else if (slot < 8)
			spawnItem(room, item, 10 + com.rs.utility.Utils.random(party.getComplexity() >= 5 ? 1 : 2), 14, false);
		else
			spawnItem(room, item, 14, 10 + com.rs.utility.Utils.random(party.getComplexity() >= 5 ? 1 : 2), false);
	}

	public void sendStartItems(Player player) {
		if (party.getComplexity() == 1) {
			player.getDialogueManager().startDialogue("SimpleMessage", "<col=0000FF>Complexity 1", "Combat only", "Armour and weapons allocated", "No shop stock");
		} else if (party.getComplexity() == 2) {
			player.getDialogueManager().startDialogue("SimpleMessage", "<col=0000FF>Complexity 2", "+ Fishing, Woodcutting, Firemaking, Cooking", "Armour and weapons allocated", "Minimal shop stock");
		} else if (party.getComplexity() == 3) {
			player.getDialogueManager().startDialogue("SimpleMessage", "<col=0000FF>Complexity 3", "+ Mining, Smithing weapons, Fletching, Runecrafting", "Armour allocated", "Increased shop stock");
		} else if (party.getComplexity() == 4) {
			player.getDialogueManager().startDialogue("SimpleMessage", "<col=0000FF>Complexity 4", "+ Smithing armour, Hunter, Farming textiles, Crafting", "Increased shop stock");
		} else if (party.getComplexity() == 5) {
			player.getDialogueManager().startDialogue("SimpleMessage", "<col=0000FF>Complexity 5", "All skills included", "+ Farming seeds, Herblore, Thieving, Summoning", "Complete shop stock", "Challenge rooms + skill doors");
		}
		if (party.getComplexity() <= 3) {
			int defenceTier = DungeonUtils.getTier(player.getSkills().getLevelForXp(Skills.DEFENCE));
			if (defenceTier > 8)
				defenceTier = 8;
			player.getInventory().addItem(new Item(DungeonUtils.getPlatebody(defenceTier)));
			player.getInventory().addItem(new Item(DungeonUtils.getPlatelegs(defenceTier, player.getAppearence().isMale())));
			if (party.getComplexity() <= 2) {
				int attackTier = DungeonUtils.getTier(player.getSkills().getLevelForXp(Skills.ATTACK));
				if (attackTier > 8)
					attackTier = 8;
				player.getInventory().addItem(new Item(DungeonUtils.getRapier(defenceTier)));
				player.getInventory().addItem(new Item(DungeonUtils.getBattleaxe(defenceTier)));
			}
			int magicTier = DungeonUtils.getTier(player.getSkills().getLevelForXp(Skills.MAGIC));
			if (magicTier > 8)
				magicTier = 8;
			player.getInventory().addItem(new Item(DungeonUtils.getRobeTop(defenceTier < magicTier ? defenceTier : magicTier)));
			player.getInventory().addItem(new Item(DungeonUtils.getRobeBottom(defenceTier < magicTier ? defenceTier : magicTier)));
			if (party.getComplexity() <= 2) {
				player.getInventory().addItem(new Item(DungeonConstants.RUNES[0], 90 + com.rs.utility.Utils.random(30)));
				player.getInventory().addItem(new Item(DungeonUtils.getStartRunes(player.getSkills().getLevelForXp(Skills.MAGIC)), 90 + com.rs.utility.Utils.random(30)));
				player.getInventory().addItem(new Item(DungeonUtils.getElementalStaff(magicTier)));
			}
			int rangeTier = DungeonUtils.getTier(player.getSkills().getLevelForXp(Skills.RANGE));
			if (rangeTier > 8)
				rangeTier = 8;
			player.getInventory().addItem(new Item(DungeonUtils.getLeatherBody(defenceTier < rangeTier ? defenceTier : rangeTier)));
			player.getInventory().addItem(new Item(DungeonUtils.getChaps(defenceTier < rangeTier ? defenceTier : rangeTier)));
			if (party.getComplexity() <= 2) {
				player.getInventory().addItem(new Item(DungeonUtils.getShortbow(rangeTier)));
				player.getInventory().addItem(new Item(DungeonUtils.getArrows(rangeTier), 90 + com.rs.utility.Utils.random(30)));
			}
		}
	}

	public void sendSettings(Player player) {
		player.getTemporaryAttributtes().put("dungOverload", true);
		player.reset();
		if (player.getControlerManager().getControler() instanceof DungeonController)
			((DungeonController) player.getControlerManager().getControler()).reset();
		else {
			player.getControlerManager().startControler("DungeonController", DungeonManager.this);
			player.setLargeSceneView(true);
			player.getCombatDefinitions().setSpellBook(3);
			setWorldMap(player, true);
		}
		sendRing(player);
		sendBindItems(player);
		wearInventory(player);
	}

	public void rejoinParty(Player player) {
		player.stopAll();
		player.lock(2);
		party.add(player);
		sendSettings(player);
		refreshKeys(player);
		player.setNextPosition(getHomeTile());
		playMusic(player, dungeon.getStartRoomReference());
	}

	public void sendBindItems(Player player) {
		Item ammo = player.getDungeoneeringManager().getBindedAmmo();
		if (ammo != null)
			player.getInventory().addItem(ammo);
		for (Item item : player.getDungeoneeringManager().getBindedItems().getItems()) {
			if (item == null)
				continue;
			player.getInventory().addItem(item);
		}
	}

	public void resetItems(Player player, boolean drop, boolean logout) {
		if (drop) {
			for (Item item : player.getEquipment().getItems().getItems()) {
				if (item == null || !ItemConstants.isTradeable(item))
					continue;
				World.addDungeoneeringGroundItem(party, item, new Position(player), false);
			}
			for (Item item : player.getInventory().getItems().getItems()) {
				if (item == null || !ItemConstants.isTradeable(item))
					continue;
				if (hasLoadedNoRewardScreen() & item.getId() == DungeonConstants.GROUP_GATESTONE)
					setGroupGatestone(new Position(player));
				World.addDungeoneeringGroundItem(party, item, new Position(player), false);
			}
		}
		player.getEquipment().reset();
		player.getInventory().reset();
		if (!logout)
			player.getAppearence().generateAppearenceData();
	}

	public void sendRing(Player player) {
		if (player.getEquipment().getRingId() != -1)
			player.getEquipment().deleteItem(player.getEquipment().getRingId(), 1);
		for (int i = 0; i < 28; i++) {
			Item item = player.getInventory().getItem(i);
			if (item != null)
				player.getInventory().deleteItem(item);
		}
		if (player.getControlerManager().getControler() instanceof DungeonController)
			player.getEquipment().set(Equipment.SLOT_RING, player.getRingOfKinship().getCurrentRing());
		else
			player.getInventory().addItem(new Item(15707));
		player.getEquipment().refresh(Equipment.SLOT_RING);
		player.getAppearence().generateAppearenceData();
	}

	public void wearInventory(Player player) {
		boolean worn = false;
		for (int slotId = 0; slotId < 28; slotId++) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				continue;
			/*
			 * if (ButtonHandler.wear(player, item.getId(), slotId)) worn =
			 * true;
			 */
		}
		if (worn) {
			player.getAppearence().generateAppearenceData();
			player.getInventory().getItems().shift();
			player.getInventory().refresh();
		}

	}

	public void spawnRandomNPCS(RoomReference reference) {
		int floorType = party.getFloorType();
		for (int i = 0; i < 3; i++) { // all floors creatures
			spawnNPC(reference, DungeonUtils.getGuardianCreature(), 2 + com.rs.utility.Utils.getRandom(13), 2 + com.rs.utility.Utils.getRandom(13), true, DungeonConstants.GUARDIAN_DOOR, 0.5);
		}

		for (int i = 0; i < 2; i++) { // this kind of floor creatures
			spawnNPC(reference, DungeonUtils.getGuardianCreature(floorType), 2 + com.rs.utility.Utils.getRandom(13), 2 + com.rs.utility.Utils.getRandom(13), true, DungeonConstants.GUARDIAN_DOOR, com.rs.utility.Utils.random(2) == 0 ? 0.8 : 1);
		}
		// forgotten warrior
		if (com.rs.utility.Utils.random(2) == 0)
			spawnNPC(reference, DungeonUtils.getForgottenWarrior(), 2 + com.rs.utility.Utils.getRandom(13), 2 + com.rs.utility.Utils.getRandom(13), true, DungeonConstants.FORGOTTEN_WARRIOR);
		// hunter creature
		spawnNPC(reference, DungeonUtils.getHunterCreature(), 2 + com.rs.utility.Utils.getRandom(13), 2 + com.rs.utility.Utils.getRandom(13), true, DungeonConstants.HUNTER_NPC, 0.5);
		boolean hasWarped = false;
		for (Player p : party.getTeam())
			
		if (com.rs.utility.Utils.random(hasWarped ? 3 : 5) == 0) // slayer creature
			spawnNPC(reference, DungeonUtils.getSlayerCreature(), 2 + com.rs.utility.Utils.getRandom(13), 2 + com.rs.utility.Utils.getRandom(13), true, DungeonConstants.SLAYER_NPC);
	}

	public int[] getRoomPos(Position tile) {
		int chunkX = tile.getX() / 16 * 2;
		int chunkY = tile.getY() / 16 * 2;
		int x = tile.getX() - chunkX * 8;
		int y = tile.getY() - chunkY * 8;
		Room room = getRoom(getCurrentRoomReference(tile));
		if (room == null)
			return null;
		return DungeonManager.translate(x, y, (4 - room.getRotation()) & 0x3, 1, 1, 0);
	}

	public static int[] translate(int x, int y, int mapRotation, int sizeX, int sizeY, int objectRotation) {
		int[] coords = new int[2];
		if ((objectRotation & 0x1) == 1) {
			int prevSizeX = sizeX;
			sizeX = sizeY;
			sizeY = prevSizeX;
		}
		if (mapRotation == 0) {
			coords[0] = x;
			coords[1] = y;
		} else if (mapRotation == 1) {
			coords[0] = y;
			coords[1] = 15 - x - (sizeX - 1);
		} else if (mapRotation == 2) {
			coords[0] = 15 - x - (sizeX - 1);
			coords[1] = 15 - y - (sizeY - 1);
		} else if (mapRotation == 3) {
			coords[0] = 15 - y - (sizeY - 1);
			coords[1] = x;
		}
		return coords;
	}

	public DungeonNPC spawnNPC(RoomReference reference, int id, int x, int y) {
		return spawnNPC(reference, id, x, y, false, DungeonConstants.NORMAL_NPC);
	}

	public DungeonNPC spawnNPC(RoomReference reference, int id, int x, int y, boolean check, int type) {
		return spawnNPC(reference, id, x, y, check, type, 1);
	}

	/*
	 * x 0-15, y 0-15
	 */

	public DungeonNPC spawnNPC(final RoomReference reference, final int id, int x, int y, boolean check, final int type, final double multiplier) {
		if (dungeon == null || reference == null || dungeon.getRoom(reference) == null)
			return null;
		final int rotation = dungeon.getRoom(reference).getRotation();
		final int size = NPCDefinitions.getNPCDefinitions(id).size;
		int[] coords = translate(x, y, rotation, size, size, 0);
		final Position tile = new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0);
		if (check && !World.isTileFree(0, tile.getX(), tile.getY(), size))
			return null;
		return spawnNPC(id, rotation, tile, reference, type, multiplier);
	}

	public WorldObject spawnObject(RoomReference reference, int id, int type, int rotation, int x, int y) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
		int[] coords = translate(x, y, mapRotation, defs.sizeX, defs.sizeY, rotation);
		WorldObject object = new WorldObject(id, type, (rotation + mapRotation) & 0x3, ((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0);
		World.spawnObject(object);
		return object;
	}

	public WorldObject spawnObjectForMapRotation(RoomReference reference, int id, int type, int rotation, int x, int y, int mapRotation) {
		ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
		int[] coords = translate(x, y, mapRotation, defs.sizeX, defs.sizeY, rotation);
		WorldObject object = new WorldObject(id, type, (rotation + mapRotation) & 0x3, ((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0);
		World.spawnObject(object);
		return object;
	}

	public WorldObject spawnObjectTemporary(RoomReference reference, int id, int type, int rotation, int x, int y, long time) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
		int[] coords = translate(x, y, mapRotation, defs.sizeX, defs.sizeY, rotation);
		WorldObject object = new WorldObject(id, type, (rotation + mapRotation) & 0x3, ((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0);
		World.spawnObjectTemporary(object, time);
		return object;
	}

	public void removeObject(RoomReference reference, int id, int type, int rotation, int x, int y) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
		int[] coords = translate(x, y, mapRotation, defs.sizeX, defs.sizeY, rotation);
		World.removeObject(new WorldObject(id, type, (rotation + mapRotation) & 0x3, ((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0));
	}

	public WorldObject getObject(RoomReference reference, int id, int x, int y) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
		int[] coords = translate(x, y, mapRotation, defs.sizeX, defs.sizeY, 0);
		return World.getObjectWithId(new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0), id);
	}

	public WorldObject getObjectWithType(RoomReference reference, int id, int type, int x, int y) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
		int[] coords = translate(x, y, mapRotation, defs.sizeX, defs.sizeY, 0);
		return World.getObjectWithType(new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0), type);
	}

	public WorldObject getObjectWithType(RoomReference reference, int type, int x, int y) {
		Room room = dungeon.getRoom(reference);
		if (room == null)
			return null;
		final int mapRotation = room.getRotation();
		int[] coords = translate(x, y, mapRotation, 1, 1, 0);
		return World.getObjectWithType(new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0), type);
	}

	public Position getTile(RoomReference reference, int x, int y, int sizeX, int sizeY) {
		Room room = dungeon.getRoom(reference);
		if (room == null)
			return null;
		final int mapRotation = room.getRotation();
		int[] coords = translate(x, y, mapRotation, sizeX, sizeY, 0);
		return new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0);
	}

	public Position getTile(RoomReference reference, int x, int y) {
		return getTile(reference, x, y, 1, 1);
	}

	public Position getRotatedTile(RoomReference reference, int x, int y) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		int[] coords = translate(x, y, mapRotation, 1, 1, 0);
		return new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0);
	}

	public void spawnItem(RoomReference reference, Item item, int x, int y, boolean lootbeam) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		int[] coords = translate(x, y, mapRotation, 1, 1, 0);
		World.addDungeoneeringGroundItem(party, item, new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0), lootbeam);
	}

	public boolean isFloorFree(RoomReference reference, int x, int y) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		int[] coords = translate(x, y, mapRotation, 1, 1, 0);
		return World.isFloorFree(0, ((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1]);
	}

	public Position getRoomTile(RoomReference reference) {
		final int mapRotation = dungeon.getRoom(reference).getRotation();
		int[] coords = translate(0, 0, mapRotation, 1, 1, 0);
		return new Position(((boundChuncks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChuncks[1] * 8) + reference.getY() * 16) + coords[1], 0);
	}

	public DungeonNPC spawnNPC(int id, int rotation, Position tile, RoomReference reference, int type, double multiplier) {
		DungeonNPC n = null;
		if (type == DungeonConstants.BOSS_NPC) {
			if (id == 9965)
				n = new AsteaFrostweb(id, tile, this, reference);
			else if (id == 9948)
				n = new GluttonousBehemoth(id, tile, this, reference);
			else if (id == 9912)
				n = new LuminscentIcefiend(id, tile, this, reference);
			else if (id == 10059)
				n = new HobgoblinGeomancer(id, tile, this, reference);
			else if (id == 10040)
				n = new IcyBones(id, tile, this, reference);
			else if (id == 10024)
				n = new ToKashBloodChiller(id, tile, this, reference);
			else if (id == 10058)
				n = new DivineSkinweaver(id, tile, this, reference);
			else if (id == 10630 || id == 10680 || id == 10693)
				n = new DungeonSkeletonBoss(id, tile, this, reference, multiplier);
			else if (id == 10073)
				n = new BulwarkBeast(id, tile, this, reference);
			else if (id == 9767)
				n = new Rammernaut(id, tile, this, reference);
			else if (id == 9782)
				n = new Stomp(id, tile, this, reference);
			else if (id == 9898)
				n = new LakkTheRiftSplitter(id, tile, this, reference);
			else if (id == 9753)
				n = new Sagittare(id, tile, this, reference);
			else if (id == 9725)
				n = new NightGazerKhighorahk(id, tile, this, reference);
			else if (id == 9842)
				n = new LexicusRunewright(id, tile, this, reference);
			else if (id == 10128)
				n = new BalLakThePummeler(id, tile, this, reference);
			else if (id == 10143)
				n = new ShadowForgerIhlakhizan(id, tile, this, reference);
			else if (id == 11940 || id == 11999 || id == 12044)
				n = new SkeletalAdventurer(id, tile, this, reference, multiplier);
			else if (id == 11812)
				n = new RuneboundBehemoth(id, tile, this, reference);
			else if (id == 11708)
				n = new Gravecreeper(id, tile, this, reference);
			else if (id == 11737 || id == 11895)
				n = new NecroLord(id, tile, this, reference);
			else if (id == 11925)
				n = new FleshspoilerHaasghenahk(id, tile, this, reference);
			else if (id == 11872)
				n = new YkLagorThunderous(id, tile, this, reference);
			else if (id == 12737)
				n = new WarpedGulega(id, tile, this, reference);
			else if (id == 12848)
				n = new Dreadnaut(id, tile, this, reference);
			else if (id == 12886)
				n = new HopeDevourer(id, tile, this, reference);
			else if (id == 12478)
				n = new WorldGorgerShukarhazh(id, tile, this, reference);
			else if (id == 12865)
				n = new Blink(id, tile, this, reference);
			else if (id == 12752)
				n = new KalGerWarmonger(id, tile, this, reference);
			else 
				n = new DungeonBoss(id, tile, this, reference);
		} else if (type == DungeonConstants.GUARDIAN_NPC) {
			n = new Guardian(id, tile, this, reference, multiplier);
			visibleMap[reference.getX()][reference.getY()].addGuardian(n);
		} else if (type == DungeonConstants.FORGOTTEN_WARRIOR) {
			n = new ForgottenWarrior(id, tile, this, reference, multiplier);
			visibleMap[reference.getX()][reference.getY()].addGuardian(n);
		} else if (type == DungeonConstants.FISH_SPOT_NPC)
			n = new DungeonFishSpot(id, tile, this, DungeoneeringFishing.Fish.values()[com.rs.utility.Utils.random(DungeoneeringFishing.Fish.values().length - 1)]);
		else if (type == DungeonConstants.SLAYER_NPC)
			n = new DungeonSlayerNPC(id, tile, this, multiplier);
		else if (type == DungeonConstants.HUNTER_NPC)
			n = new DungeonHunterNPC(id, tile, this, multiplier);
		else if (type == DungeonConstants.PUZZLE_NPC) {
			if (id == PoltergeistRoom.POLTERGEIST_ID)
				n = new PoltergeistRoom.Poltergeist(id, tile, this, reference);
		} else
			n = new DungeonNPC(id, tile, this, multiplier);
		n.setDirection(com.rs.utility.Utils.getAngle(Utils.ROTATION_DIR_X[(rotation + 3) & 0x3], Utils.ROTATION_DIR_Y[(rotation + 3) & 0x3]));
		return n;
	}

	public int getTargetLevel(int id, boolean boss, double multiplier) {
		double lvl = boss ? party.getCombatLevel() : party.getAverageCombatLevel();
		int randomize = party.getComplexity() * 2 * party.getTeam().size();
		lvl -= randomize;
		lvl += com.rs.utility.Utils.random(randomize * 2);
		lvl *= party.getDificultyRatio();
		lvl *= multiplier;
		lvl *= 1D - ((double) (6D - party.getComplexity()) * 0.07D);
		if (party.getTeam().size() > 2 && id != 12752 && id != 11872 && id != 11708 && id != 12865) // blink
			lvl *= 0.7;
		return (int) (lvl < 1 ? 1 : lvl);
	}

	public int[] getBonuses(boolean boss, int level) {
		int[] bonuses = new int[10];
		bonuses[CombatDefinitions.RANGE_ATTACK] = (party.getRangeLevel() + (level / 10)) * (getParty().getFloor() / 15);
		bonuses[CombatDefinitions.STAB_ATTACK] = ((int) (party.getAttackLevel() + (level / 10) / 1.2)) * (getParty().getFloor() / 15);
		bonuses[CombatDefinitions.MAGIC_ATTACK] = (int) ((party.getAttackLevel() + (level / 10) / 1.5)) * (getParty().getFloor() / 15);
		int npcDefenceLevel =  party.getDefenceLevel() / (boss ? 1 : 5);
		bonuses[CombatDefinitions.STAB_DEF] = bonuses[CombatDefinitions.CRUSH_DEF] = bonuses[CombatDefinitions.SLASH_DEF] = (int) (npcDefenceLevel + (level / 3));
		bonuses[CombatDefinitions.RANGE_DEF] = (int) (npcDefenceLevel + (level / 3));
		bonuses[CombatDefinitions.MAGIC_DEF] = (int) ((npcDefenceLevel + (level / 3)) / 1.5);
		return bonuses;
	}

	public void updateGuardian(RoomReference reference) {
		if (visibleMap[reference.getX()][reference.getY()].removeGuardians()) {
			getRoom(reference).removeGuardianDoors();
			for (Player player : party.getTeam()) {
				RoomReference playerReference = getCurrentRoomReference(player);
				if (playerReference.getX() == reference.getX() && playerReference.getY() == reference.getY())
					playMusic(player, reference);
			}
		}
	}

	public void exitDungeon(final Player player, final boolean logout) {
		party.remove(player, logout);
		player.stopAll();
		player.reset();
		player.getControlerManager().removeControlerWithoutCheck();
		player.getControlerManager().startControler("Kalaboss");
		resetItems(player, true, logout);
		resetTraps(player);
		sendRing(player);
		if (player.getFamiliar() != null)
			player.getFamiliar().sendDeath(player);
		if (logout)
			player.setLocation(new Position(DungeonConstants.OUTSIDE, 2));
		else {
			player.getDungeoneeringManager().setRejoinKey(null);
			player.useStairs(-1, new Position(DungeonConstants.OUTSIDE, 2), 0, 3);
			player.getCombatDefinitions().removeDungeonneringBook();
			
			setWorldMap(player, false);
			removeMark(player);
			player.setLargeSceneView(false);
			player.setForceMultiArea(false);
			player.getInterfaceManager().closeOverlay(true);
			player.getMusicsManager().reset();
			player.getAppearence().setRenderEmote(-1);
		}

	}

	public void setWorldMap(Player player, boolean dungIcon) {
		player.getPackets().sendConfigByFile(6090, dungIcon ? 1 : 0);
	}

	public void endFarming() {
		//for (String key : farmKeyList)
			//OwnedObjectManager.cancel(key);
		farmKeyList.clear();
		patches.clear();
	}

	private void resetTraps(Player player) {
		for (MastyxTrap trap : mastyxTraps) {
			if (!trap.getPlayerName().equals(player.getDisplayName()))
				continue;
			trap.finish();
		}
	}

	public void endMastyxTraps() {
		for (MastyxTrap trap : mastyxTraps) {
			trap.finish();
		}
		mastyxTraps.clear();
	}

	public void removeDungeon() {
		cachedDungeons.remove(key);
	}

	public void destroy() {
		synchronized (this) {
			if (isDestroyed()) // to prevent issues when shutting down forcing
				return;
			endRewardsTimer();
			endDestroyTimer();
			endFarming();
			endMastyxTraps();
			removeDungeon();
			partyDeaths.clear();
			final int[] oldboundChuncks = boundChuncks;
			for (int x = 0; x < visibleMap.length; x++) {
				for (int y = 0; y < visibleMap[x].length; y++) {
					if (visibleMap[x][y] != null) {
						visibleMap[x][y].destroy();
					}
				}
			}
			final Dungeon oldDungeon = dungeon;
			dungeon = null;
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						MapBuilder.destroyMap(oldboundChuncks[0], oldboundChuncks[1], oldDungeon.getMapWidth() * 2, oldDungeon.getMapHeight() * 2);
					} catch (Throwable e) {
						com.rs.utility.Logger.handle(e);
					}
				}
			}, 5, TimeUnit.SECONDS); // just to be safe
		}
	}

	public void nextFloor() {
		// int maxFloor =
		// DungeonUtils.getMaxFloor(party.getDungeoneeringLevel());
		if (party.getMaxFloor() > party.getFloor())
			party.setFloor(party.getFloor() + 1);
		if (tutorialMode) {
			int complexity = party.getComplexity();
			if (party.getMaxComplexity() > complexity)
				party.setComplexity(complexity + 1);
		}
		destroy();
		load();
	}

	public Integer[] getAchievements(Player player) { // TODO
		List<Integer> achievements = new ArrayList<Integer>();

		DungeonController controller = (DungeonController) player.getControlerManager().getControler();

		// solo achievements
		if (controller.isKilledBossWithLessThan10HP())
			achievements.add(6);
		if (controller.getDeaths() == 8)
			achievements.add(8);
		else if (controller.getDeaths() == 0)
			achievements.add(14);
		if (controller.getDamage() != 0 && controller.getDamageReceived() == 0)
			achievements.add(25);

		// party achievements
		boolean mostMeleeDmg = true;
		boolean mostMageDmg = true;
		boolean mostRangeDmg = true;
		boolean leastDamage = false;
		boolean mostDmgReceived = false;
		boolean mostDeaths = false;
		boolean mostHealedDmg = false;
		if (party.getTeam().size() > 1) {
			leastDamage = true;
			mostDmgReceived = true;
			mostDeaths = true;
			mostHealedDmg = true;
			for (Player teamMate : party.getTeam()) {
				if (teamMate == player)
					continue;
				DungeonController tmController = (DungeonController) teamMate.getControlerManager().getControler();
				if (tmController.getMeleeDamage() >= controller.getMeleeDamage())
					mostMeleeDmg = false;
				if (tmController.getMageDamage() >= controller.getMageDamage())
					mostMageDmg = false;
				if (tmController.getRangeDamage() >= controller.getRangeDamage())
					mostRangeDmg = false;
				if (controller.getDamage() >= tmController.getDamage())
					leastDamage = false;
				if (controller.getDamageReceived() <= tmController.getDamageReceived())
					mostDmgReceived = false;
				if (controller.getDeaths() <= tmController.getDeaths())
					mostDeaths = false;
				if (controller.getHealedDamage() <= tmController.getHealedDamage())
					mostHealedDmg = false;
			}
		} else {
			if (controller.getMeleeDamage() <= 0)
				mostMeleeDmg = false;
			if (controller.getMageDamage() <= 0)
				mostMageDmg = false;
			if (controller.getRangeDamage() <= 0)
				mostRangeDmg = false;
		}
		if (mostMeleeDmg && mostMageDmg && mostRangeDmg)
			achievements.add(1);
		if (leastDamage && mostDeaths) // leecher
			achievements.add(2);
		if (mostMeleeDmg)
			achievements.add(3);
		if (mostRangeDmg)
			achievements.add(4);
		if (mostMageDmg)
			achievements.add(5);
		if (leastDamage)
			achievements.add(7);
		if (mostDmgReceived)
			achievements.add(13);
		if (mostDeaths)
			achievements.add(15);
		if (mostHealedDmg)
			achievements.add(38);

		if (achievements.size() == 0)
			achievements.add(33);
		return achievements.toArray(new Integer[achievements.size()]);

	}

	public void loadRewards() {
		stage = 2;
		for (Player player : party.getTeam()) {
			
			player.getEquipment().reset();
			player.getInventory().reset();
			player.getAppearence().generateAppearenceData();
			player.stopAll();
			double multiplier = 1;
			if (!player.getInterfaceManager().hasRezizableScreen()) {
				player.getInterfaceManager().sendInterface(933);
			} else {
				player.getInterfaceManager().sendOverlay(933, true);
			}
			player.getPackets().sendRunScript(2275); // clears interface data
														// from last run
			for (int i = 0; i < 5; i++) {
				Player partyPlayer = i >= party.getTeam().size() ? null : party.getTeam().get(i);
				player.getPackets().sendGlobalConfig(1198 + i, partyPlayer != null ? 1 : 0); // sets
																								// true
																								// that
																								// this
																								// player
																								// exists
				if (partyPlayer == null)
					continue;
				player.getPackets().sendGlobalString(310 + i, partyPlayer.getDisplayName());
				Integer[] achievements = getAchievements(partyPlayer);
				for (int i2 = 0; i2 < (achievements.length > 6 ? 6 : achievements.length); i2++)
					player.getPackets().sendGlobalConfig(1203 + (i * 6) + i2, achievements[i2]);
			}
			player.getPackets().sendIComponentText(933, 331, Utils.formatTime((Utils.currentWorldCycle() - time) * 600));
			player.getPackets().sendGlobalConfig(1187, party.getFloor());
			player.getPackets().sendGlobalConfig(1188, party.getSize() + 1); // dungeon
																				// size,
																				// sets
																				// bonus
																				// aswell
			multiplier += DungeonConstants.DUNGEON_SIZE_BONUS[party.getSize()];
			player.getPackets().sendGlobalConfig(1191, party.getTeam().size() * 10 + party.getDifficulty()); // teamsize:dificulty
			multiplier += DungeonConstants.DUNGEON_DIFFICULTY_RATIO_BONUS[party.getTeam().size() - 1][party.getDifficulty() - 1];
			int v = 0;
			if (getVisibleBonusRoomsCount() != 0) { // no bonus rooms in c1,
													// would be divide by 0
				v = getVisibleBonusRoomsCount() * 10000 / (dungeon.getRoomsCount() - dungeon.getCritCount());
			}
			player.getPackets().sendGlobalConfig(1195, v); // dungeons rooms
															// opened, sets
															// bonus aswell
			multiplier += DungeonConstants.MAX_BONUS_ROOM * v / 10000;
			v = (getLevelModPerc() * 20) - 1000;
			player.getPackets().sendGlobalConfig(1236, v); // sets level mod
			multiplier += ((double) v) / 10000;
			player.getPackets().sendGlobalConfig(1196, party.isGuideMode() ? 1 : 0); // sets
																						// guidemode
			if (party.isGuideMode())
				multiplier -= 0.05;
			player.getPackets().sendGlobalConfig(1319, DungeonUtils.getMaxFloor(player.getSkills().getLevelForXp(Skills.DUNGEONEERING)));
			player.getPackets().sendGlobalConfig(1320, party.getComplexity());
			if (party.getComplexity() != 6)
				multiplier -= (DungeonConstants.COMPLEXIYY_PENALTY_BASE[party.getSize()] + (new Double(5 - party.getComplexity())) * 0.06);
			double levelDiffPenalty = party.getLevelDiferencePenalty(player);// party.getMaxLevelDiference()
																				// >
																				// 70
																				// ?
																				// DungeonConstants.UNBALANCED_PARTY_PENALTY
																				// :
																				// 0;
			player.getPackets().sendGlobalConfig(1321, (int) (levelDiffPenalty * 10000));
			multiplier -= levelDiffPenalty;
			double countedDeaths = Math
					.min(/* player.getVarsManager().getBitValue(7554) */0, 6);// TODO
			multiplier *= (1.0 - (countedDeaths * 0.1)); // adds FLAT 10%
															// reduction per
															// death, upto 6
			// base xp is based on a ton of factors, including opened rooms,
			// resources harvested, ... but this is most imporant one
			double floorXP = getXPForFloor(party.getFloor(), party.getSize()) * getVisibleRoomsCount() / dungeon.getRoomsCount();
			//floorXP *= player.getGorajanTrailblazer().getExperienceBoost();
			boolean tickedOff = player.getDungeoneeringManager().isTickedOff(party.getFloor());
			if (!tickedOff)
				player.getDungeoneeringManager().tickOff(party.getFloor());
			else {
				int[] range = DungeonUtils.getFloorThemeRange(party.getFloor());
				for (int floor = range[0]; floor <= range[1]; floor++) {
					if (player.getDungeoneeringManager().getMaxFloor() < floor)
						break;
					if (!player.getDungeoneeringManager().isTickedOff(floor)) {
						player.getPackets().sendGameMessage("Since you have previously completed this floor, floor " + floor + " was instead ticked-off.");
						player.getDungeoneeringManager().tickOff(floor);
						floorXP = getXPForFloor(floor, party.getSize()) * getVisibleRoomsCount() / dungeon.getRoomsCount();
						tickedOff = false;
						break;
					}
				}
			}
			double prestigeXP = tickedOff ? 0 : getXPForFloor(player.getDungeoneeringManager().getPrestige(), party.getSize()) * getVisibleRoomsCount() / dungeon.getRoomsCount();
			player.getPackets().sendConfigByFile(7550, player.getDungeoneeringManager().getCurrentProgres());
			player.getPackets().sendConfigByFile(7551, player.getDungeoneeringManager().getPreviousProgress());
			double averageXP = (floorXP + prestigeXP) / 2;
			if (party.getTeam().size() == 1 && party.getSize() != DungeonConstants.LARGE_DUNGEON)
				averageXP *= 1.5;
			multiplier = Math.max(0.1, multiplier);
			double totalXp = averageXP * multiplier;
			int tokens = (int) (totalXp / 10);
			player.getPackets().sendGlobalConfig(1237, (int) (floorXP * 10));
			player.getPackets().sendGlobalConfig(1238, (int) (prestigeXP * 10));
			player.getPackets().sendGlobalConfig(1239, (int) (averageXP * 10));
			// player.getPackets().sendIComponentText(933, 39, totalXp); 
			totalXp *= 0.1;
			player.getSkills().addXp(Skills.DUNGEONEERING, totalXp); // force rs
																		// xp,
																		// cuz
																		// we do
																		// * xp
																		// rate
																		// in
																		// calcs
																		// to
																		// make
																		// inter
																		// show
																		// correct
																		// xp
			player.getDungeoneeringManager().setTokens(player.getDungeoneeringManager().getTokens() + tokens * 10);
			player.getMusicsManager().forcePlayMusic(770);
			if (!tickedOff) {
				if (DungeonUtils.getMaxFloor(player.getSkills().getLevelForXp(Skills.DUNGEONEERING)) < party.getFloor() + 1)
					player.getPackets().sendGameMessage("The next floor is not available at your Dungeonnering level. Consider resetting your progress to gain best ongoing rate of xp.");
			} else {
				player.getPackets().sendGameMessage("<col=D80000>Warning:");
				player.getPackets().sendGameMessage("You have already completed all the available floors of this theme and thus cannot be awarded prestige xp until you reset your progress or switch theme.");
			}
			player.getPackets().sendGameMessage("Pre-Share: <col=641d9e>" + (isKeyShare() ? "OFF" : "ON"));
			if (party.getFloor() == player.getDungeoneeringManager().getMaxFloor() && party.getFloor() < DungeonUtils.getMaxFloor(player.getSkills().getLevelForXp(Skills.DUNGEONEERING)))
				player.getDungeoneeringManager().increaseMaxFloor();
			if (player.getDungeoneeringManager().getMaxComplexity() < 6)
				player.getDungeoneeringManager().increaseMaxComplexity();
			if (player.getFamiliar() != null)
				player.getFamiliar().sendDeath(player);
		}
		skippedPuzzle = false;
		clearGuardians();
	}

	public static int getXPForFloor(int floor, int type) {
		int points = 0;
		for (int i = 1; i <= floor; i++)
			points += Math.floor(i + 100.0 * Math.pow(1.3, i / 10));
		if (type == DungeonConstants.MEDIUM_DUNGEON)
			points *= 4;
		else if (type == DungeonConstants.LARGE_DUNGEON)
			points *= 10;
		/*
		 * if (World.isWeekend()) TODO double tokens during weekend. points *=
		 * 2;
		 */

		return points * DungeonConstants.XP_RATE;
	}

	public void voteToMoveOn(Player player) {
		// loadRewards();
		if (rewardsTimer == null)
			setRewardsTimer();
		if (rewardsTimer != null)
			rewardsTimer.increaseReadyCount();
	}

	public void ready(Player player) {
		int index = party.getIndex(player);
		if (rewardsTimer == null)
			return;
		rewardsTimer.increaseReadyCount();
		for (Player p2 : party.getTeam())
			p2.getPackets().sendGlobalConfig(1397 + index, 1);
	}

	public DungeonPartyManager getParty() {
		return party;
	}

	public void setRewardsTimer() {
		CoresManager.getServiceProvider().scheduleFixedLengthTask(rewardsTimer = new RewardsTimer(), 1, 5, TimeUnit.SECONDS);
	}

	public void setDestroyTimer() {
		// cant be already instanced before anyway, afterall only isntances hwen
		// party is 0 and remvoes if party not 0
		CoresManager.getServiceProvider().scheduleFixedLengthTask(destroyTimer = new DestroyTimer(), 1, 5, TimeUnit.SECONDS);
	}

	public void setMark(Entity target, boolean mark) {
		if (mark) {
		//	for (Player player : party.getTeam())
			//	player.getHintIconsManager().addHintIcon(6, target, 0, -1, true); // 6th
																					// slot
		} else
			removeMark();
		if (target instanceof DungeonNPC)
			((DungeonNPC) target).setMarked(mark);
	}

	public void setGroupGatestone(Position groupGatestone) {
		this.groupGatestone = groupGatestone;
	}

	public Position getGroupGatestone() {
		if (groupGatestone == null) {
			Player player = party.getGateStonePlayer();
			if (player != null)
				return new Position(player);
		}
		return groupGatestone;
	}

	public void resetGatestone() {
		groupGatestone = null;
	}

	public void removeMark() {
		for (Player player : party.getTeam())
			removeMark(player);
	}

	public void removeMark(Player player) {
		 //player.getHintIconsManager().removeHintIcon(6);
	}

	public void endDestroyTimer() {
		if (destroyTimer != null) {
			destroyTimer.stopNow(true);
			destroyTimer = null;
		}
	}

	public void endRewardsTimer() {
		if (rewardsTimer != null) {
			rewardsTimer.stopNow(true);
			rewardsTimer = null;
		}
	}

	private class DestroyTimer extends FixedLengthRunnable {
		private long timeLeft;

		public DestroyTimer() {
			timeLeft = 600; // 10min
		}

		@Override
		public boolean repeat() {
			try {
				if (timeLeft > 0) {
					timeLeft -= 5;
					return true;
				}
				destroy();

			} catch (Throwable e) {
				com.rs.utility.Logger.handle(e);
			}
			return false;
		}

	}

	private class RewardsTimer extends FixedLengthRunnable {

		private long timeLeft;
		private boolean gaveRewards;

		public RewardsTimer() {
			timeLeft = party.getTeam().size() * 60;
		}

		public void increaseReadyCount() {
			int reduce = (int) (gaveRewards ? ((double) 45 / (double) party.getTeam().size()) : 60);
			timeLeft = timeLeft > reduce ? timeLeft - reduce : 0;
		}

		@Override
		public boolean repeat() {
			try {
				if (timeLeft > 0) {
					for (Player player : party.getTeam())
						player.getPackets().sendGameMessage(gaveRewards ? ("Time until next dungeon: " + timeLeft) : (timeLeft + " seconds until dungeon ends."));
					timeLeft -= 5;
				} else {
					if (!gaveRewards) {
						gaveRewards = true;
						timeLeft = 45;
						loadRewards();
					} else
						nextFloor();
				}
			} catch (Throwable e) {
				com.rs.utility.Logger.handle(e);
			}
			return true;
		}

	}

	public void setDungeon() {
		key = party.getLeader() + "_" + keyMaker.getAndIncrement();
		cachedDungeons.put(key, this);
		for (Player player : party.getTeam())
			player.getDungeoneeringManager().setRejoinKey(key);
	}

	public static void checkRejoin(Player player) {
		Object key = player.getDungeoneeringManager().getRejoinKey();
		if (key == null)
			return;
		DungeonManager dungeon = cachedDungeons.get(key);
		// either doesnt exit / ur m8s moving next floor(reward screen)
		if (dungeon == null || !dungeon.hasLoadedNoRewardScreen()) {
			player.getDungeoneeringManager().setRejoinKey(null);
			return;
		}
		dungeon.rejoinParty(player);
	}

	public void load() {
		party.lockParty();
		visibleMap = new VisibleRoom[DungeonConstants.DUNGEON_RATIO[party.getSize()][0]][DungeonConstants.DUNGEON_RATIO[party.getSize()][1]];
		// slow executor loads dungeon as it may take up to few secs
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					//clearKeyList();
					// generates dungeon structure
					dungeon = new Dungeon(DungeonManager.this, party.getFloor(), party.getComplexity(), party.getSize());
					time = Utils.currentWorldCycle();
					// finds an empty map area bounds
					boundChuncks = RegionBuilder.findEmptyChunkBound(dungeon.getMapWidth() * 2, (dungeon.getMapHeight() * 2));
					// reserves all map area
					MapBuilder.cutMap(boundChuncks[0], boundChuncks[1], dungeon.getMapWidth() * 2, (dungeon.getMapHeight() * 2), 0);
					// set dungeon
					setDungeon();
					// loads start room
					loadRoom(dungeon.getStartRoomReference());
					stage = 1;
				} catch (Throwable e) {
					e.printStackTrace();
					com.rs.utility.Logger.handle(e);
				}
			}
		});
	}

	public boolean hasStarted() {
		return stage != 0;
	}

	public boolean isAtRewardsScreen() {
		return stage == 2;
	}

	public boolean hasLoadedNoRewardScreen() {
		return stage == 1;
	}

	public void openMap(Player player) {
		if (party.getSize() == DungeonConstants.TEST_DUNGEON) {
			player.getPackets().sendGameMessage("Dungeon is too big to be displayed.");
			return;
		}
		player.getInterfaceManager().sendInterface(942);
		player.getPackets().sendRunScript(3277); // clear the map if theres any
													// setted
		int protocol = party.getSize() == DungeonConstants.LARGE_DUNGEON ? 0 : party.getSize() == DungeonConstants.MEDIUM_DUNGEON ? 2 : 1;
		for (int x = 0; x < visibleMap.length; x++) {
			for (int y = 0; y < visibleMap[x].length; y++) {
				if (visibleMap[x][y] != null) { // means exists
					Room room = getRoom(new RoomReference(x, y));
					boolean highLight = party.isGuideMode() && room.isCritPath();
					player.getPackets().sendRunScript(3278, protocol, getMapIconSprite(room, highLight), y, x);
					if (room.getRoom() instanceof StartRoom)
						player.getPackets().sendRunScript(3280, protocol, y, x);
					else if (room.getRoom() instanceof BossRoom)
						player.getPackets().sendRunScript(3281, protocol, y, x);
					if (room.hasNorthDoor() && visibleMap[x][y + 1] == null) {
						Room unknownR = getRoom(new RoomReference(x, y + 1));
						highLight = party.isGuideMode() && unknownR.isCritPath();
						player.getPackets().sendRunScript(3278, protocol, getMapIconSprite(DungeonConstants.SOUTH_DOOR, highLight), y + 1, x);
					}
					if (room.hasSouthDoor() && visibleMap[x][y - 1] == null) {
						Room unknownR = getRoom(new RoomReference(x, y - 1));
						highLight = party.isGuideMode() && unknownR.isCritPath();
						player.getPackets().sendRunScript(3278, protocol, getMapIconSprite(DungeonConstants.NORTH_DOOR, highLight), y - 1, x);
					}
					if (room.hasEastDoor() && visibleMap[x + 1][y] == null) {
						Room unknownR = getRoom(new RoomReference(x + 1, y));
						highLight = party.isGuideMode() && unknownR.isCritPath();
						player.getPackets().sendRunScript(3278, protocol, getMapIconSprite(DungeonConstants.WEST_DOOR, highLight), y, x + 1);
					}
					if (room.hasWestDoor() && visibleMap[x - 1][y] == null) {
						Room unknownR = getRoom(new RoomReference(x - 1, y));
						highLight = party.isGuideMode() && unknownR.isCritPath();
						player.getPackets().sendRunScript(3278, protocol, getMapIconSprite(DungeonConstants.EAST_DOOR, highLight), y, x - 1);
					}
				}
			}
		}
		int index = 1;
		for (Player p2 : party.getTeam()) {
			RoomReference reference = getCurrentRoomReference(p2);
			player.getPackets().sendRunScript(3279, p2.getDisplayName(), protocol, index++, reference.getY(), reference.getX());
		}
	}

	public int getMapIconSprite(int direction, boolean highLight) {
		for (DungeonConstants.MapRoomIcon icon : DungeonConstants.MapRoomIcon.values()) {
			if (icon.isOpen())
				continue;
			if (icon.hasDoor(direction))
				return icon.getSpriteId() + (highLight ? DungeonConstants.MapRoomIcon.values().length : 0);
		}
		return 2879;
	}

	public int getMapIconSprite(Room room, boolean highLight) {
		for (DungeonConstants.MapRoomIcon icon : DungeonConstants.MapRoomIcon.values()) {
			if (!icon.isOpen())
				continue;
			if (icon.hasNorthDoor() == room.hasNorthDoor() && icon.hasSouthDoor() == room.hasSouthDoor() && icon.hasWestDoor() == room.hasWestDoor() && icon.hasEastDoor() == room.hasEastDoor())
				return icon.getSpriteId() + (highLight ? DungeonConstants.MapRoomIcon.values().length : 0);
		}
		return 2878;
	}

	public void openStairs(RoomReference reference) {
		Room room = getRoom(reference);
		int type = 0;

		if (room.getRoom().getChunkX() == 26 && room.getRoom().getChunkY() == 640) // unholy
																					// cursed
			type = 1;
		else if (room.getRoom().getChunkX() == 30 && room.getRoom().getChunkY() == 656) // stomp
			type = 2;
		else if ((room.getRoom().getChunkX() == 30 && room.getRoom().getChunkY() == 672) || (room.getRoom().getChunkX() == 24 && room.getRoom().getChunkY() == 690)) // necromancer)
			type = 3;
		else if (room.getRoom().getChunkX() == 26 && room.getRoom().getChunkY() == 690) // world-gorger
			type = 4;
		else if (room.getRoom().getChunkX() == 24 && room.getRoom().getChunkY() == 688) // blink
			type = 5;
		spawnObject(reference, DungeonConstants.LADDERS[party.getFloorType()], 10, (type == 2 || type == 3) ? 0 : 3, type == 4 ? 11 : type == 3 ? 15 : type == 2 ? 14 : 7, type == 5 ? 14 : (type == 3 || type == 2) ? 3 : type == 1 ? 11 : 15);
		getVisibleRoom(reference).setNoMusic();
		for (Player player : party.getTeam()) {
			if (!isAtBossRoom(player))
				continue;
			player.getPackets().sendMusicEffect(415);
			playMusic(player, reference);
		}
	}

	public List<String> getFarmKeyList() {
		return farmKeyList;
	}

	public DungeoneeringPatch getDungeoneeringPatch(WorldObject object) {
		for (DungeoneeringPatch patch : patches) {
			if (patch.getPatch().getHash() == object.getHash())
				return patch;
		}
		return null;
	}

	public void addDungeoneeringPatch(DungeoneeringPatch patch) {
		patches.add(patch);
	}

	public List<DungeoneeringPatch> getDungeoneeringPatches() {
		return patches;
	}

	public void removeDungeoneeringPatch(DungeoneeringPatch patch) {
		patches.remove(patch);
	}

	public void addMastyxTrap(MastyxTrap mastyxTrap) {
		mastyxTraps.add(mastyxTrap);
	}

	public List<MastyxTrap> getMastyxTraps() {
		return mastyxTraps;
	}

	public void removeMastyxTrap(MastyxTrap mastyxTrap) {
		mastyxTraps.remove(mastyxTrap);
		mastyxTrap.finish();
	}

	public void message(RoomReference reference, String message) {
		for (Player player : party.getTeam()) {
			if (reference.equals(getCurrentRoomReference(player))) {
				player.getPackets().sendGameMessage(message);
			}
		}
	}

	public void showBar(RoomReference reference, String name, int percentage) {
		for (Player player : party.getTeam()) {
			RoomReference current = getCurrentRoomReference(player);
			if (reference.getX() == current.getX() && reference.getY() == current.getY() && player.getControlerManager().getControler() instanceof DungeonController) {
				DungeonController c = (DungeonController) player.getControlerManager().getControler();
				c.showBar(true, name);
				c.sendBarPercentage(percentage);
			}
		}
	}

	public void hideBar(RoomReference reference) {
		for (Player player : party.getTeam()) {
			RoomReference current = getCurrentRoomReference(player);
			if (reference.getX() == current.getX() && reference.getY() == current.getY() && player.getControlerManager().getControler() instanceof DungeonController) {
				DungeonController c = (DungeonController) player.getControlerManager().getControler();
				c.showBar(false, null);
			}
		}
	}

	public Map<String, Integer> getPartyDeaths() {
		return partyDeaths;
	}

	/*
	 * Use get npc instead this being used because gravecreeper gets removed
	 * when using special :/
	 */
	@Deprecated
	public DungeonBoss getTemporaryBoss() {
		return temporaryBoss;
	}

	public void setTemporaryBoss(DungeonBoss temporaryBoss) {
		this.temporaryBoss = temporaryBoss;
	}
}
