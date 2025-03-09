package com.rs.game.world.entity.player.controller.impl;

import java.util.ArrayList;
import java.util.Arrays;

import com.rs.Constants;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.skilling.thieving.Thieving;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.utility.Utils;

public class Wilderness extends Controller {

	private boolean showingSkull;

	@Override
	public void start() {
		checkBoosts(player);
	}

	public static void checkBoosts(Player player) {
		boolean changed = false;
		int level = player.getSkills().getLevelForXp(Skills.ATTACK);
		int maxLevel = (int) (level + 5 + level * 0.15);
		if (maxLevel < player.getSkills().getLevel(Skills.ATTACK)) {
			player.getSkills().set(Skills.ATTACK, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.STRENGTH);
		maxLevel = (int) (level + 5 + level * 0.15);
		if (maxLevel < player.getSkills().getLevel(Skills.STRENGTH)) {
			player.getSkills().set(Skills.STRENGTH, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.DEFENCE);
		maxLevel = (int) (level + 5 + level * 0.15);
		if (maxLevel < player.getSkills().getLevel(Skills.DEFENCE)) {
			player.getSkills().set(Skills.DEFENCE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.RANGE);
		maxLevel = (int) (level + 5 + level * 0.1);
		if (maxLevel < player.getSkills().getLevel(Skills.RANGE)) {
			player.getSkills().set(Skills.RANGE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.MAGIC);
		maxLevel = level + 5;
		if (maxLevel < player.getSkills().getLevel(Skills.MAGIC)) {
			player.getSkills().set(Skills.MAGIC, maxLevel);
			changed = true;
		}
		if (changed) {
			player.getPackets().sendGameMessage(
					"Your extreme potion bonus has been reduced.");
		}
	}

	@Override
	public boolean login() {
		moved();
		return false;
	}

	@Override
	public boolean keepCombating(Entity target) {
		if (target instanceof NPC) {
			return true;
		}
		if (!canAttack(target)) {
			return false;
		}
		if (target.getAttackedBy() != player
				&& player.getAttackedBy() != target) {
			player.setWildernessSkull();
		}
		if (player.getCombatDefinitions().getSpellId() <= 0 && Utils.inCircle(new Position(3105, 3933, 0), target, 24)) {
			player.getPackets().sendGameMessage("You can only use magic in the arena.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (player.isCanPvp() && !p2.isCanPvp()) {
				player.getPackets().sendGameMessage(
						"That player is not in the wilderness.");
				return false;
			}
			if (canHit(target)) {
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC) {
			return true;
		}
		Player p2 = (Player) target;
		if (Math.abs(player.getSkills().getCombatLevel()
				- p2.getSkills().getCombatLevel()) > getWildLevel()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(Position toTile) {
		if (getWildLevel() < 41 && player.petManager.getNpcId() == 16154) {
			return true;
		}
		if (getWildLevel() > 20) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;

	}

	@Override
	public boolean processItemTeleport(Position toTile) {
		if (getWildLevel() > 20) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processItemTeleport(Position toTile, Item item) {
		ArrayList<Integer> specialJelwery = new ArrayList<Integer>();
		Integer[] dragonJewlery = { 1712, 1710, 1708, 1706, 20659, 20657, 20655, 20653, 11118, 11120, 11122, 11124,
				11105, 11107, 11109, 11111 };
		specialJelwery.addAll(Arrays.asList(dragonJewlery));
		
		if (specialJelwery.contains(item.getId())) {
			if (getWildLevel() > 30) {
				player.sm("A mysterious force prevents you from teleporting.");
				return false;
			} else {
				return true;
			}
		}
		if (getWildLevel() > 20) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}


	@Override
	public boolean processObjectTeleport(Position toTile) {
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	public void showSkull() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 11: 0,	381);
	}

	public static boolean isDitch(int id) {
		return id >= 1440 && id <= 1444 || id >= 65076 && id <= 65087;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (isDitch(object.getId())) {
			player.lock();
			player.animate(new Animation(6132));
			final Position toTile = new Position(object.getRotation() == 1 || object.getRotation() == 3 ? object.getX() + 2 : player.getX(),
					object.getRotation() == 0 || object.getRotation() == 2 ? object.getY() - 1 : player.getY(), object.getZ());

			player.setNextForceMovement(new ForceMovement(
					new Position(player), 1, toTile, 2, object.getRotation() == 0 || object.getRotation() == 2 ? ForceMovement.SOUTH : ForceMovement.EAST));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextPosition(toTile);
					player.faceObject(object);
					removeIcon();
					removeControler();
					player.resetReceivedDamage();
					player.unlock();
				}
			}, 2);
			return false;
		} else if (object.getId() == 2557 || object.getId() == 65717) {
			player.getPackets().sendGameMessage("It seems it is locked, maybe you should try something else.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {
		if (object.getId() == 2557 || object.getId() == 65717) {
			Thieving.pickDoor(player, object);
			return false;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		if (isAtWild(player)) {
			showSkull();
		}
	}
	@Override
	public boolean sendDeath() {

		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					Player killer = player.getMostDamageReceivedSourcePlayer();
					if (killer != null) {
						killer.removeDamage(player);
						killer.increaseKillCount(player);
					}
					player.sendItemsOnDeath(killer);
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setNextPosition(new Position(Constants.RESPAWN_DEATH_LOCATION));
					player.animate(new Animation(-1));
				} else if (loop == 4) {
					removeIcon();
					removeControler();
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}


	@Override
	public void moved() {
		boolean isAtWild = isAtWild(player);
		boolean isAtWildSafe = isAtWildSafe();
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.setCanPvp(true);
			showSkull();
			player.getAppearence().generateAppearenceData();
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			removeIcon();
		} else if (!isAtWildSafe && !isAtWild) {
			player.setCanPvp(false);
			removeIcon();
			removeControler();
		}
	}

	public void removeIcon() {
		if (showingSkull) {
			showingSkull = false;
			player.setCanPvp(false);
			player.getPackets()
			.closeInterface(
					player.getInterfaceManager().hasRezizableScreen() ? 11
							: 0);
			player.getAppearence().generateAppearenceData();
			player.getEquipment().refresh(null);
		}
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public void forceClose() {
		removeIcon();
	}

	public static final boolean isAtWild(Position tile) {//TODO fix this
		return tile.getX() >= 3011 && tile.getX() <= 3132
				&& tile.getY() >= 10052 && tile.getY() <= 10175 //fortihrny dungeon
				|| 	tile.getX() >= 2940 && tile.getX() <= 3395
				&& tile.getY() >= 3525 && tile.getY() <= 4000
				|| tile.getX() >= 3264 && tile.getX() <= 3279
				&& tile.getY() >= 3279 && tile.getY() <= 3672
				|| tile.getX() >= 2756 && tile.getX() <= 2875
				&& tile.getY() >= 5512 && tile.getY() <= 5627
				|| tile.getX() >= 3158 && tile.getX() <= 3181
				&& tile.getY() >= 3679 && tile.getY() <= 3697
				|| tile.getX() >= 3280 && tile.getX() <= 3183
				&& tile.getY() >= 3885 && tile.getY() <= 3888 || tile.getX() >= 3012 && tile.getX() <= 3059
				&& tile.getY() >= 10303 && tile.getY() <= 10351 
				|| tile.withinArea(2067, 2522, 2096, 2545); //Scorpia
	}

	public boolean isAtWildSafe() {
		return player.getX() >= 2940 && player.getX() <= 3395
				&& player.getY() <= 3524 && player.getY() >= 3523;
	}

	public int getWildLevel() {
		if (player.isPublicWildEnabled()) {
			return 50;
		}
		if (player.getY() > 9900) {
			return (player.getY() - 9912) / 8 + 1;
		}
		if (player.getX() >= 2067 && player.getY() <= 2096 && player.getY() >= 2522 && player.getY() <= 2545) { //Scorpia
			return 53;
		}
		return (player.getY() - 3520) / 8 + 1;
	}

}
