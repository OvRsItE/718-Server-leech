package com.rs.game.world.entity.player.content.skills.dungeoneering.skills.firemaking;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.Region;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.Action;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.network.decoders.handlers.InventoryOptionsHandler;
import com.rs.utility.Utils;


public class DungeoneeringFiremaking extends Action {

	private DungeoneeringFiremakingData fire;

	public DungeoneeringFiremaking(DungeoneeringFiremakingData fire) {
		this.fire = fire;
	}

	public static boolean isFiremaking(Player player, int logId) {
		for (DungeoneeringFiremakingData fire : DungeoneeringFiremakingData.values()) {
			if (fire.getLogsId() == logId) {
				player.getActionManager().setAction(new DungeoneeringFiremaking(fire));
				return true;
			}
		}
		return false;
	}
	
	public static final boolean addLogs(final Player player, final WorldObject object, final Item item) {
		if (!object.getDefinitions().getName().equals("Cooking range (empty)"))
			return false;

		if (item.getId() >= 17682 && item.getId() <= 17700) {
			int logsTier = 0;
			for (int i = object.getId(); i > object.getId() - 15; i--) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(i);
				if (defs.name.equals("Cooking range (empty)")) {
					logsTier = object.getId() - defs.id - 1;
					break;
				}
			}
			if (player.getSkills().getLevel(Skills.FIREMAKING) < logsTier * 10) {
				player.sendMessage("You need at least level " + (logsTier * 10) + " Firemaking to add these logs to the range.");
				return true;
			}
			player.animate(new Animation(537));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getInventory().deleteItem(item.getId(), 1);
					World.spawnObject(new WorldObject(object.getId() + ((item.getId() - 17680 ) / 2), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getZ()));
					player.sendMessage("You add some " + item.getDefinitions().getName() + " into the cooking range.");
				}
			});
			return true;
		}
		return false;
	}
	
	public static final boolean lightRange(final Player player, final WorldObject object) {
		if (!object.getDefinitions().getName().equals("Cooking range (with logs)"))
			return false;
		int logsTier = 0;
		int objectId = 0;
		for (int i = object.getId(); i > object.getId() - 15; i--) {
			ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(i);
			if (defs.name.equals("Cooking range (empty)")) {
				logsTier = object.getId() - defs.id - 1;
				objectId = defs.id;
				break;
			}
		}
		final int finObjectId = objectId;
		final int logs = logsTier;
		if (player.getSkills().getLevel(Skills.FIREMAKING) < logsTier * 10) {
			player.sendMessage("You need at least level " + (logsTier * 10) + " Firemaking to light this range.");
			return true;
		}
		if (!player.getInventory().containsItem(DungeonConstants.TINDERBOX, 1)) {
		
				player.sendMessage("You need a tinderbox to light these logs on fire.");
				return true;
			
		}
		player.animate(new Animation(733));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				DungeoneeringFiremakingData data = DungeoneeringFiremakingData.values()[logs];
				World.spawnTemporaryObjectWithReplacement(player, new WorldObject(object.getId() + 10, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getZ()), new WorldObject(finObjectId, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getZ()), data.getTime() * 1800);
				player.sendMessage("You set the logs in the range on fire.");
				player.getSkills().addXp(Skills.FIREMAKING, data.getExperience());
			}
		});
		return false;
	}

	public static boolean isFiremaking(Player player, Item item1, Item item2) {
		Item log = InventoryOptionsHandler.contains(17678, item1, item2);
		if (log == null)
			return false;
		return isFiremaking(player, log.getId());
	}

	public boolean checkAll(Player player) {
		if (player.getInterfaceManager().containsScreenInter() || player.getInterfaceManager().containsInventoryInter()) {
			player.sendMessage("Please finish what you're doing before doing this action.");
			return false;
		}
		if (!player.getInventory().containsItem(17678, 1) ) {
			player.sendMessage("You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.sendMessage("You do not have the required level to light this.");
			return false;
		}
		if (!World.canMoveNPC(player.getZ(), player.getX(), player.getY(), 1) || World.getObjectWithSlot(player, Region.OBJECT_SLOT_FLOOR) != null) {
			player.sendMessage("You can't light a fire here; find a different area.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		final Position tile = new Position(player);
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);

		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (Utils.random(50) <= fire.ordinal()) {
					player.setNextFacePosition(tile);
					player.sendMessage("You burn your hand trying to tend the fire.");
					return;
				}
				/*final FloorItem item = World.getRegion(tile.getRegionId()).getGroundItem(fire.getLogsId(), tile, player);
				if (item == null) {
					player.sm("is nullll");
					return;
				}
				if (!World.removeGroundItem(player, item, false))
					return;*/
				World.spawnTempGroundObject(new WorldObject(fire.getFireId(), 10, 0, tile.getX(), tile.getY(), tile.getZ()), 592, fire.getTime() * 600);
				player.getSkills().addXp(Skills.FIREMAKING, fire.getExperience());
				player.setNextFacePosition(tile);
				player.getPackets().sendGameMessage("The fire catches and the logs begin to burn.", true);

			}
		}, 1);
		player.getTemporaryAttributtes().put("Fire", Utils.currentTimeMillis() + 1800);
		return -1;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.stopAll();
		player.getPackets().sendGameMessage("You attempt to light the logs.", true);
		player.getInventory().deleteItem(fire.getLogsId(), 1);
		//World.addGroundItem(new Item(fire.getLogsId(), 1,0), new WorldTile(player), player, true, 60, 0);
		Long time = (Long) player.getTemporaryAttributtes().remove("Fire");
		boolean quickFire = time != null && time > Utils.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : 2);
		if (!quickFire)
			player.animate(new Animation(733));
		return true;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}