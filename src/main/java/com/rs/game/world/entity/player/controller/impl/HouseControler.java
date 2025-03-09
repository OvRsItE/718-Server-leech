package com.rs.game.world.entity.player.controller.impl;

import java.util.concurrent.TimeUnit;

import com.rs.Constants;
import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.objects.SitChair;
import com.rs.game.world.entity.player.actions.skilling.Cooking;
import com.rs.game.world.entity.player.actions.skilling.Cooking.Cookables;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.content.skills.construction.House;
import com.rs.game.world.entity.player.content.skills.construction.HouseConstants;
import com.rs.game.world.entity.player.content.skills.construction.HouseConstants.Builds;
import com.rs.game.world.entity.player.content.skills.construction.HouseConstants.HObject;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.NewForceMovement;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Logger;
import com.rs.utility.Utils;

public class HouseControler extends Controller {

	public static boolean handleDoor(Player player, WorldObject object,
			long timer) {
		WorldObject openedDoor = new WorldObject(object.getId(),
				object.getType(), object.getRotation() + 1, object.getX(),
				object.getY(), object.getZ());
		if (object.getId() == 13345)
			openedDoor.setId(13344);
		else if (object.getId() == 13344) {
			openedDoor.setId(13345);
			openedDoor.setRotation(object.getRotation() - 1);
		} else if (object.getId() == 13347)
			openedDoor.setId(13346);
		else if (object.getId() == 13346) {
			openedDoor.setId(13347);
			openedDoor.setRotation(object.getRotation() - 1);
		} else if (object.getId() == 13349)
			openedDoor.setId(13348);
		else if (object.getId() == 13348) {
			openedDoor.setId(13349);
			openedDoor.setRotation(object.getRotation() - 1);
		}
		/*
		 * if (object.getRotation() == 0) openedDoor.moveLocation(-1, 0, 0);
		 * else if (object.getRotation() == 1) openedDoor.moveLocation(0, 1, 0);
		 * else if (object.getRotation() == 2) openedDoor.moveLocation(1, 0, 0);
		 * else if (object.getRotation() == 3) openedDoor.moveLocation(0, -1,
		 * 0);
		 */
		if (World.removeTemporaryObject(object, timer, true)) {
			player.faceObject(openedDoor);
			World.spawnTemporaryObject(openedDoor, timer, true, object);
			return true;
		}
		return false;
	}

	// private HObject ring;

	private boolean insideCombatRing = false;
	private HObject ring;

	private House house;

	@Override
	public boolean canAttack(Entity target) {
		return true;
	}

	@Override
	public boolean canDropItem(Item item) {
		if (house.isBuildMode()) {
			player.getPackets().sendGameMessage(
					"You cannot drop items while in building mode.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		if (insideCombatRing) {
			if (ring == HouseConstants.HObject.BOXING_RING) {
				if (slotId == Equipment.SLOT_WEAPON) {
					if (itemId != 7671 && itemId != 7673) {
						player.sm("You can't use this weapon in here.");
						return false;
					}
				} else {
					player.sm("You can't this in here.");
					return false;
				}
			} else if (ring == HouseConstants.HObject.FENCING_RING) {
				if (slotId != Equipment.SLOT_WEAPON) {
					player.sm("You can't equip this in here.");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canHit(Entity entity) {
		return true;
	}

	private HObject checkRing(int objectId) {
		HObject[] rings = HouseConstants.Builds.COMBAT_RING.getPieces();
		for (HObject ring : rings)
			for (int id : ring.getIds())
				if (id == objectId)
					return ring;
		return null;
	}

	@Override
	public boolean checkWalkStep(final int lastX, final int lastY, int nextX,
			int nextY) {
		WorldObject tile = World.getObject(new Position(nextX, nextY, player.getZ()), 22);
	//	WorldObject lastTile = World.getObject(new WorldTile(lastX, lastY, player.getPlane()), 22);
		if(tile != null) {
		if (tile.getId() == 13332 || tile.getId() == 13331) 
			player.getAppearence().setRenderEmote(152);
		} else {
			//if(lastTile != null)
				player.getAppearence().setRenderEmote(-1);
		}
		return !house.isSky(nextX, nextY, player.getZ());
	}

	// shouldnt happen
	@Override
	public void forceClose() {
		house.leaveHouse(player, House.TELEPORTED);
	}

	public House getHouse() {
		return house;
	}

	@Override
	public boolean handleItemOnObject(WorldObject object, Item item) {
		if (object.getId() == HouseConstants.HObject.CLAY_FIREPLACE.getId()
				|| object.getId() == HouseConstants.HObject.STONE_FIREPLACE
						.getId()
				|| object.getId() == HouseConstants.HObject.MARBLE_FIREPLACE
						.getId()) {
			if (item.getId() != 1511) {
				player.getPackets()
						.sendGameMessage(
								"Only ordinary logs can be used to light the fireplace.");
				return false;
			}
			if (!player.getInventory().containsItem(590, 1)) {
				player.getPackets().sendGameMessage(
						"You do not have the required items to light this.");
				return false;
			}
			player.lock(2);
			player.animate(new Animation(3658));
			player.getSkills().addXp(Skills.FIREMAKING, 40);
			WorldObject objectR = new WorldObject(object);
			objectR.setId(object.getId() + 1);
			// wiki says: If you light a fire in your fireplace, then change the
			// graphic settings, the fire will disappear meaning its not realy
			// spawned
			for (Player player : house.getPlayers())
				player.getPackets().sendSpawnedObject(objectR);
			return false;
		} else if (HouseConstants.Builds.STOVE.containsObject(object)) {
			Cookables cook = Cooking.isCookingSkill(item);
			if (cook != null) {
				player.getDialogueManager().startDialogue("CookingD", cook,
						object);
				return false;
			}
			player.getDialogueManager()
					.startDialogue(
							"SimpleMessage",
							"You can't cook that on a "
									+ (object.getDefinitions().name
											.equals("Fire") ? "fire" : "range")
									+ ".");
			return false;
		}
		return true;
	}

	// shouldnt happen but lets imagine somehow in a server restart
	@Override
	public boolean login() {
		player.setNextPosition(Constants.START_PLAYER_LOCATION);
		removeControler();
		return false; // remove controller manualy since i dont want to call
						// forceclose
	}

	@Override
	public boolean logout() {
		house.leaveHouse(player, House.LOGGED_OUT);
		return false; // leave house method removes controller already
	}

	@Override
	public void magicTeleported(int type) {
		house.leaveHouse(player, House.TELEPORTED);
	}

	@Override
	public void process() {
		if (!insideCombatRing) {
			if (player.getZ() == 0 && !player.canPvp) {
				player.setCanPvp(true);
				player.setForceMultiArea(true);
			} else if (player.getZ() != 0 && player.canPvp) {
				player.setCanPvp(false);
				player.setForceMultiArea(false);
			}
		}
		final WorldObject trap = World.getObject(new Position(player.getX(),
				player.getY(), player.getZ()), 22);
		if (trap != null) { // 75
			// player.addWalkSteps(trap.getX(), trap.getY(), 100, false);
			// if(Utils.random(player.getSkills().getLevel(17)) >= 40) {
			if (trap.getId() == 13361) {
				player.setNextGraphics(new Graphics(615));
				player.applyHit(new Hit(player, Utils.random(100),
						HitLook.MELEE_DAMAGE));
				player.resetWalkSteps();
				World.removeObject(trap);
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							World.spawnObject(trap);
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}

				}, 5000, TimeUnit.MILLISECONDS);
				// return false;
			} else if (trap.getId() == 13362) {
				player.setNextGraphics(new Graphics(616));
				player.applyHit(new Hit(player, Utils.random(200),
						HitLook.MELEE_DAMAGE));
				player.resetWalkSteps();
				World.removeObject(trap);
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							World.spawnObject(trap);
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}

				}, 5000, TimeUnit.MILLISECONDS);
				// return false;
			} else if (trap.getId() == 13363) {
				player.setNextGraphics(new Graphics(617));
				player.setNextGraphics(new Graphics(618));
				player.resetWalkSteps();
				player.lock(5);
				World.removeObject(trap);
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							World.spawnObject(trap);
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}

				}, 5000, TimeUnit.MILLISECONDS);
				// return false;
			} else if (trap.getId() == 13364) {
				player.getSkills().drainLevel(17, 20);
				player.gfx(620);
				player.resetWalkSteps();
				World.removeObject(trap);
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							World.spawnObject(trap);
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}

				}, 5000, TimeUnit.MILLISECONDS);
				// return false;
			} else if (trap.getId() == 13365) {
				player.gfx(621);
				player.resetWalkSteps();
				World.removeObject(trap);
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						try {
							World.spawnObject(trap);
						} catch (Throwable e) {
							Logger.handle(e);
						}
					}

				}, 5000, TimeUnit.MILLISECONDS);
				// return false;
				// }
			} else if(trap.getId() == 13685) {
				
			}
		}
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		return true;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (player.dataStream)
			player.sm("object click 1 on object");
		if (object.getId() == HouseConstants.HObject.EXIT_PORTAL.getId())
			house.leaveHouse(player, House.KICKED);
		else if (object.getId() == HouseConstants.HObject.CLAY_FIREPLACE
				.getId()
				|| object.getId() == HouseConstants.HObject.STONE_FIREPLACE
						.getId()
				|| object.getId() == HouseConstants.HObject.MARBLE_FIREPLACE
						.getId())
			player.getPackets().sendGameMessage(
					"Use some logs on the fireplace in order to light it.");
		else if (object.getId() >= HouseConstants.HObject.CRUDE_WOODEN_CHAIR
				.getId()
				&& object.getId() <= HouseConstants.HObject.MAHOGANY_ARMCHAIR
						.getId()) {
			int chair = object.getId()
					- HouseConstants.HObject.CRUDE_WOODEN_CHAIR.getId();
			player.getActionManager().setAction(
					new SitChair(player, chair, object));
		} else if (HouseConstants.Builds.BOOKCASE.containsObject(object))
			player.getPackets().sendGameMessage(
					"You search the bookcase but find nothing.");
		else if (HouseConstants.Builds.STAIRCASE.containsObject(object)
				|| HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object)) {
			if (object.getDefinitions().getOption(1).equals("Climb"))
				player.getDialogueManager().startDialogue("ClimbHouseStairD",
						object);
			else
				house.climbStaircase(
						object,
						object.getDefinitions().getOption(1).equals("Climb-up"),
						player);
		} else if (object.getId() == HouseConstants.HObject.DUNGEON_ENTRACE
				.getId()) {
			house.climbStaircase(object, object.getDefinitions().getOption(1)
					.equals("Climb-down"), player);
		} else if (object.getId() == HouseConstants.HObject.OAK_DOOR.getIds()[0]
				|| object.getId() == HouseConstants.HObject.OAK_DOOR.getIds()[1]) {
			player.sm("You attempt to open the door...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (Utils.random(5) == 1) {
						player.sm("You manage to pry the door open for a moment. Quick! Get through!");
						handleDoor(player, object, 3000);
					} else
						player.sm("The door is locked, you couldnt get it open.");
				}
			}, 0, 2);
		} else if (object.getId() == HouseConstants.HObject.STEEL_DOOR.getIds()[0]
				|| object.getId() == HouseConstants.HObject.STEEL_DOOR.getIds()[1]) {
			player.sm("You attempt to open the door...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (Utils.random(10) == 1) {
						player.sm("You manage to pry the door open for a moment. Quick! Get through!");
						handleDoor(player, object, 3000);
					} else
						player.sm("The door is locked, you couldnt get it open.");
				}
			}, 0, 2);
		} else if (object.getId() == HouseConstants.HObject.MARBLE_DOOR
				.getIds()[0]
				|| object.getId() == HouseConstants.HObject.MARBLE_DOOR
						.getIds()[1]) {
			player.sm("You attempt to open the door...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (Utils.random(15) == 1) {
						player.sm("You manage to pry the door open for a moment. Quick! Get through!");
						handleDoor(player, object, 3000);
					} else
						player.sm("The door is locked, you couldnt get it open.");
				}
			}, 0, 2);
		} else if (HouseConstants.Builds.WEAPONS_RACK.containsObject(object)) {
			player.getDialogueManager().startDialogue("WeaponsRack");
		} else if (HouseConstants.Builds.COMBAT_RING.containsObject(object)) {
			if (checkRing(object.getId()) == HouseConstants.HObject.BOXING_RING) {
				if (player.getEquipment().wearingArmourNoWeapon()) {
					player.sm("Please take all your armour off before entering this ring.");
					return false;
				} else if (player.getEquipment().hasWeapon()
						&& !(player.getEquipment()
								.getItem(Equipment.SLOT_WEAPON).getId() == 7671 || player
								.getEquipment().getItem(Equipment.SLOT_WEAPON)
								.getId() == 7673)) {
					player.sm("Please remove any weapons, boxing gloves are allowed.");
					return false;
				}
			} else if (checkRing(object.getId()) == HouseConstants.HObject.FENCING_RING) {
				if (player.getEquipment().wearingArmourNoWeapon()) {
					player.sm("Please take all your armour off before entering this ring.");
					return false;
				}
			}
			player.faceObject(object);
			// WorldTile array
			final Position[] tile = new Position[] {
					insideCombatRing ? new Position(player.getX() + 1,
							player.getY(), player.getZ())
							: new Position(player.getX() - 1, player.getY(),
									player.getZ()),
					insideCombatRing ? new Position(player.getX(),
							player.getY() - 1, player.getZ())
							: new Position(player.getX(), player.getY() + 1,
									player.getZ()),
					insideCombatRing ? new Position(player.getX() - 1,
							player.getY(), player.getZ())
							: new Position(player.getX() + 1, player.getY(),
									player.getZ()),
					insideCombatRing ? new Position(player.getX(),
							player.getY() + 1, player.getZ())
							: new Position(player.getX(), player.getY() - 1,
									player.getZ()) };

			if ((World.getObject(tile[object.getRotation()], 22) == null)
					&& !insideCombatRing) {
				player.sm("You can't get in from here.");
				return false;
			}
			player.lock();
			player.setNextFacePosition(tile[object.getRotation()]);
			WorldTasksManager.schedule(new WorldTask() {
				private int stage;

				@Override
				public void run() {
					if (stage == 0) {
						player.animate(new Animation(839));
						player.setNextForceMovement(new NewForceMovement(
								player, 1, tile[object.getRotation()], 2,
								player.getFaceWorldTile(tile[object.getRotation()])));
					} else if (stage == 2) {
						player.setNextPosition(tile[object.getRotation()]);
						ring = checkRing(object.getId());
						insideCombatRing = !insideCombatRing;
						player.setCanPvp(insideCombatRing);
						player.setForceMultiArea(insideCombatRing);
						stop();
						player.unlock();
					}
					stage++;
				}
			}, 0, 0);
		}
		return false;
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {
		if (player.dataStream)
			player.sm("object click 2 on object");
		if (object.getId() == HouseConstants.HObject.EXIT_PORTAL.getId())
			house.switchLock(player);
		else if (HouseConstants.Builds.STAIRCASE.containsObject(object)
				|| HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object))
			house.climbStaircase(object, true, player);
		else if (object.getId() == HouseConstants.HObject.OAK_DOOR.getIds()[0]
				|| object.getId() == HouseConstants.HObject.OAK_DOOR.getIds()[1]) {
			player.sm("You pick lock the door");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (player.getSkills().getLevel(17) >= 55
							&& Utils.random(1) == 0) {
						player.sm("The locks seem to slip, and the door opens for a moment. Quick! Get through!");
						handleDoor(player, object, 5000);
					} else
						player.sm("The locks built into the door are too strong. You failed opening them.");
				}
			}, 0, 2);
		} else if (object.getId() == HouseConstants.HObject.STEEL_DOOR.getIds()[0]
				|| object.getId() == HouseConstants.HObject.STEEL_DOOR.getIds()[1]) {
			player.sm("You attempt to open the door...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (player.getSkills().getLevel(17) >= 65
							&& Utils.random(1) == 0) {
						player.sm("The locks seem to slip, and the door opens for a moment. Quick! Get through!");
						handleDoor(player, object, 5000);
					} else
						player.sm("The locks built into the door are too strong. You failed opening them.");
				}
			}, 0, 2);
		} else if (object.getId() == HouseConstants.HObject.MARBLE_DOOR
				.getIds()[0]
				|| object.getId() == HouseConstants.HObject.MARBLE_DOOR
						.getIds()[1]) {
			player.sm("You attempt to open the door...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (player.getSkills().getLevel(17) >= 75
							&& Utils.random(1) == 0) {
						player.sm("The locks seem to slip, and the door opens for a moment. Quick! Get through!");
						handleDoor(player, object, 5000);
					} else
						player.sm("The locks built into the door are too strong. You failed opening them.");
				}
			}, 0, 2);
		}
		return false;
	}

	@Override
	public boolean processObjectClick3(final WorldObject object) {
		if (player.dataStream)
			player.sm("object click 3 on object");
		if (HouseConstants.Builds.STAIRCASE.containsObject(object)
				|| HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object))
			house.climbStaircase(object, false, player);
		else if (object.getId() == HouseConstants.HObject.OAK_DOOR.getIds()[0]
				|| object.getId() == HouseConstants.HObject.OAK_DOOR.getIds()[1]) {
			player.sm("You pick lock the door");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (player.getSkills().getLevel(2) >= 55
							&& Utils.random(1) == 0) {
						player.sm("You use your strength to smash through the locks. Quick! Get through!");
						handleDoor(player, object, 5000);
					} else
						player.sm("The locks built into the door are too strong for you to break through. You failed opening them.");
				}
			}, 0, 2);
		} else if (object.getId() == HouseConstants.HObject.STEEL_DOOR.getIds()[0]
				|| object.getId() == HouseConstants.HObject.STEEL_DOOR.getIds()[1]) {
			player.sm("You attempt to open the door...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (player.getSkills().getLevel(2) >= 65
							&& Utils.random(1) == 0) {
						player.sm("You use your strength to smash through the locks. Quick! Get through!");
						handleDoor(player, object, 5000);
					} else
						player.sm("The locks built into the door are too strong for you to break through. You failed opening them.");
				}
			}, 0, 2);
		} else if (object.getId() == HouseConstants.HObject.MARBLE_DOOR
				.getIds()[0]
				|| object.getId() == HouseConstants.HObject.MARBLE_DOOR
						.getIds()[1]) {
			player.sm("You attempt to open the door...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					this.stop();
					if (player.getSkills().getLevel(2) >= 75
							&& Utils.random(1) == 0) {
						player.sm("You use your strength to smash through the locks. Quick! Get through!");
						handleDoor(player, object, 5000);
					} else
						player.sm("The locks built into the door are too strong for you to break through. You failed opening them.");
				}
			}, 0, 2);
		}
		return false;
	}

	@Override
	public boolean processObjectClick4(WorldObject object) {
		if (player.dataStream)
			player.sm("object click 4 on object");
		if (HouseConstants.Builds.STAIRCASE.containsObject(object)
				|| HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object)
				|| object.getId() == HouseConstants.HObject.DUNGEON_ENTRACE
						.getId())
			house.removeRoom();
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick5(WorldObject object) {
		if (player.dataStream)
			player.sm("object click 5 on object "+object.getDefinitions().configFileId+"");
		if (object.getDefinitions().containsOption(4, "Build")) {
			if (!house.isOwner(player)) {
				player.getPackets().sendGameMessage(
						"You can only do that in your own house.");
				return false;
			}
			if (house.isDoor(object)) {
				house.openRoomCreationMenu(object);
			} else {
				for (Builds build : HouseConstants.Builds.values()) {
					if (build.containsId(object.getId())) {
						house.openBuildInterface(object, build);
						return false;
					}
				}
			}
		} else if (object.getDefinitions().containsOption(4, "Remove")) {
			if (!house.isOwner(player)) {
				player.getPackets().sendGameMessage(
						"You can only do that in your own house.");
				return false;
			} else if (insideCombatRing) {
				player.sm("You can only do this outside of a combat ring.");
				return false;
			}
			player.faceObject(object);
			house.openRemoveBuild(object);
		}
		return false;
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.animate(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"You have been defeated!");
				} else if (loop == 3) {
					player.reset();
					house.teleportPlayer(player);
					player.animate(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void start() {
		this.house = (House) getArguments()[0];
		getArguments()[0] = null; // its was gonna be saved unless somehow in a
									// server restart but lets be safe
	}

}
