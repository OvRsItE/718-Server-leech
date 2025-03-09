package com.rs.game.world.entity.player.content.skills.agility;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;

public class WildernessAgility {

	public static void swingOnRopeSwing(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		} else if (player.getY() != 3953) {
			player.addWalkSteps(player.getX(), 3953);
			player.getPackets().sendGameMessage("You'll need to get closer to make this jump.");
			return;
		}
		player.lock();
		player.animate(new Animation(751));
		World.sendObjectAnimation(player, object, new Animation(497));
		final Position toTile = new Position(object.getX(), 3958, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH));
		player.getSkills().addXp(Skills.AGILITY, 20);
		player.getPackets().sendGameMessage("You skillfully swing across.", true);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
				player.unlock();
				if (getStage(player) != 1) {
					removeStage(player);
				} else {
					setStage(player, 2);
				}
			}
		}, 1);
	}

	public static void walkAcrossLogBalance(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		player.lock();
		if (player.getY() != object.getY()) {
			player.addWalkSteps(3001, 3945, -1, false);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					walkAcrossLogBalanceEnd(player, object);
				}
			}, 1);
		} else {
			walkAcrossLogBalanceEnd(player, object);
		}
	}

	private static void walkAcrossLogBalanceEnd(final Player player, WorldObject object) {
		player.getPackets().sendGameMessage("You walk carefully across the slippery log...", true);
		player.lock();
		player.animate(new Animation(9908));
		final Position toTile = new Position(2994, object.getY(), object.getZ());
		player.setNextForceMovement(new ForceMovement(toTile, 10, ForceMovement.WEST));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
				player.getSkills().addXp(Skills.AGILITY, 20);
				player.getPackets().sendGameMessage("... and make it safely to the other side.", true);
				player.unlock();
				if (getStage(player) != 3) {
					removeStage(player);
				} else {
					setStage(player, 4);
				}
			}
		}, 7);
	}

	public static void jumpSteppingStones(final Player player, final WorldObject object) {
		if (player.getY() != object.getY()) {
			return;
		}
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			int x;

			@Override
			public void run() {
				if (x++ == 6) {
					player.getSkills().addXp(Skills.AGILITY, 20);
					player.unlock();
					stop();
					return;
				}
				final Position toTile = new Position(3002 - x, player.getY(), player.getZ());
				player.setNextForceMovement(new ForceMovement(toTile, 1, ForceMovement.WEST));
				player.animate(new Animation(741));
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						player.setNextPosition(toTile);
					}
				}, 0);
			}
		}, 2, 1);
		if (getStage(player) != 2) {
			removeStage(player);
		} else {
			setStage(player, 3);
		}
	}

	public static void climbUpWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		player.lock();
		player.useStairs(3378, new Position(object.getX(), 3935, 0), 7, 9);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				if (getStage(player) != 4) {
					removeStage(player);
				} else {
					player.getSkills().addXp(Skills.AGILITY, 498.9);
					player.wildycourse++;
					setStage(player, 0);
				}
				player.unlock();
			}
		}, 8);
	}

	public static void enterWildernessCourse(final Player player) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		WorldObject firstGate = new WorldObject(65365, 10, 1, 2998, 3916, 0);
		final WorldObject secondGate = new WorldObject(65367, 10, 1, 2998, 3930, 0);
		player.setNextPosition(new Position(firstGate.getX(), firstGate.getY() + 1, 0));
		player.setNextForceMovement(new ForceMovement(secondGate, 8, ForceMovement.NORTH));
		player.animate(new Animation(9908));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(secondGate);
				player.animate(new Animation(-1));
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						player.setNextPosition(new Position(secondGate.getX(), secondGate.getY() + 1, 0));
						player.unlock();
					}
				});
			}
		}, 7);
	}

	public static void exitWildernessCourse(final Player player) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		final WorldObject firstGate = new WorldObject(65365, 10, 1, 2998, 3916, 0);
		final WorldObject secondGate = new WorldObject(65367, 10, 1, 2998, 3930, 0);
		player.setNextPosition(new Position(secondGate.getX(), secondGate.getY(), 0));
		player.setNextForceMovement(new ForceMovement(new Position(firstGate.getX(), firstGate.getY() + 1, 0), 8, ForceMovement.SOUTH));
		player.animate(new Animation(9908));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(new Position(firstGate.getX(), firstGate.getY() + 1, 0));
				player.animate(new Animation(-1));
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						player.setNextPosition(new Position(firstGate.getX(), firstGate.getY() - 1, 0));
						player.unlock();
					}
				});
			}
		}, 7);
	}

	public static void enterWildernessPipe(final Player player, int objectX, int objectY) {
		if (objectX == 3004 && objectY == 3948) {
			player.sendMessage("You can't entire the pipe from the side.");
			return;
		}
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock();
		player.addWalkSteps(objectX, 3949, -1, false);
		player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearence().setRenderEmote(295);
				} else {
					player.getAppearence().setRenderEmote(-1);
					player.setRunHidden(running);
					setStage(player, 1);
					player.unlock();
					player.getSkills().addXp(Skills.AGILITY, 12.5);
					stop();
				}
			}
		}, 0, 10);
	}

	public static void removeStage(Player player) {
		player.getTemporaryAttributtes().remove("WildernessCourse");
	}

	public static void setStage(Player player, int stage) {
		player.getTemporaryAttributtes().put("WildernessCourse", stage);
	}

	public static int getStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributtes().get("WildernessCourse");
		if (stage == null) {
			return -1;
		}
		return stage;
	}
}
