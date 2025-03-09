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

public class GnomeAgility {


	public static void climbUpTree(final Player player) {
		if (!Agility.hasLevel(player, 85)) {
			return;
		}
		player.lock(2);
		player.useStairs(828, new Position(2472, 3419, 3), 1, 2, "You climbed the tree succesfully.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				removeGnomeStage(player);
				player.getSkills().addXp(Skills.AGILITY, 25);
			}
		}, 1);
	}

	public static void PreSwing(final Player player, final WorldObject object) {
		if (player.getX() != 2486 || player.getY() != 3418
				|| player.getZ() != 3) {
			player.lock(3);
		}
		player.animate(new Animation(11784));
		final Position toTile = new Position(player.getX(), 3421, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;
			@Override
			public void run() {
				if(stage == 1) {
					player.setNextPosition(toTile);
					player.animate(new Animation(11785));
					Swing(player,object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}
	public static void Swing(final Player player, final WorldObject object) {
		if(!Agility.hasLevel(player, 85)) {
			return;
		}
		player.lock(4);
		final Position toTile = new Position(player.getX(), 3425, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 1, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;
			@Override
			public void run() {
				if (stage == 0) {
					player.animate(new Animation(11789));
					player.setNextPosition(toTile);
				} else if (stage == 1) {
					Swing1(player,object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	public static void Swing1(final Player player, final WorldObject object) {
		if(!Agility.hasLevel(player, 85)) {
			return;
		}
		player.lock(4);
		final Position NextTile = new Position(player.getX(), 3429, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 2, NextTile, 3, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;
			@Override
			public void run() {
				if (stage == 3) {
					player.setNextPosition(NextTile);
					Swing2(player,object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}
	public static void Swing2(final Player player, final WorldObject object) {
		if(!Agility.hasLevel(player, 85)) {
			return;
		}
		player.lock(3);
		final Position LastTile = new Position(player.getX(), 3432, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, LastTile, 1, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;
			@Override
			public void run() {
				if (stage == 2) {
					player.setNextPosition(LastTile);

					stop();
				}
				stage++;
				player.getSkills().addXp(Skills.AGILITY, 10);
			}

		}, 0, 1);
	}

	public static void jumpDown(final Player player, WorldObject object) {
		player.lock(4);
		final Position toTile = new Position(2485, 3436, 0);
		WorldTasksManager.schedule(new WorldTask() {

			boolean secondLoop;

			@Override
			public void run() {

				if (!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, ForceMovement.NORTH));
					player.animate(new Animation(2923));
					secondLoop = true;
				} else {
					player.animate(new Animation(2924));
					player.setNextPosition(toTile);
					player.gnomescourse++;
					player.getSkills().addXp(Skills.AGILITY, 630);
					if (player.agilityLapsG == 100) {
						player.getInventory().addItem(14938, 1);
						World.sendWorldMessage("<col=ff8c38><img=4>News: "+ player.getDisplayName() + " has just earned Agile legs!</col> ", false);
					}
					stop();
				}

			}

		}, 1, 2);
	}

	// advanced

	public static void preSwing(final Player player, final WorldObject object) {
		player.lock(4);
		player.animate(new Animation(11784));
		final Position toTile = new Position(player.getX(), 3421, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 1) {
					player.setNextPosition(toTile);
					player.animate(new Animation(11785));
					swing(player, object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	private static void swing(final Player player, final WorldObject object) {
		player.lock(4);
		final Position toTile = new Position(player.getX(), 3425, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 1, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.animate(new Animation(11789));
					player.setNextPosition(toTile);
				} else if (stage == 1) {
					swing1(player, object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	private static void swing1(final Player player, final WorldObject object) {
		player.lock(4);
		final Position NextTile = new Position(player.getX(), 3429, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 2, NextTile, 3, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 3) {
					player.setNextPosition(NextTile);
					swing2(player, object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	private static void swing2(final Player player, final WorldObject object) {
		player.lock(4);
		final Position LastTile = new Position(player.getX(), 3432, object.getZ());
		player.setNextForceMovement(new ForceMovement(player, 0, LastTile, 1, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 2) {
					player.getSkills().addXp(Skills.AGILITY, 25);
					player.setNextPosition(LastTile);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	public static void runGnomeBoard(final Player player, final WorldObject object) {

		player.lock(4);
		player.animate(new Animation(2922));
		final Position toTile = new Position(2484, 3418, object.getZ());
		player.setNextForceMovement(new ForceMovement(new Position(2477, 3418, 3), 1, toTile, 3, ForceMovement.EAST));
		player.getPackets().sendGameMessage("You skilfully run across the Board", true);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
				player.getSkills().addXp(Skills.AGILITY, 25);
			}

		}, 1);
	}

	// gnome course

	public static void walkGnomeLog(final Player player) {
		if (player.getX() != 2474 || player.getY() != 3436) {
			return;
		}
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(2474, 3429, -1, false);
		player.getPackets().sendGameMessage("You walk carefully across the slippery log...", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearence().setRenderEmote(155);
				} else {
					player.getAppearence().setRenderEmote(-1);
					player.setRunHidden(running);
					setGnomeStage(player, 0);
					player.getSkills().addXp(Skills.AGILITY, 7.5);
					player.getPackets().sendGameMessage("... and make it safely to the other side.", true);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void climbGnomeObstacleNet(final Player player) {
		if (player.getY() != 3426) {
			return;
		}
		player.lock(2);
		player.getPackets().sendGameMessage("You climb the netting.", true);
		player.useStairs(828, new Position(player.getX(), 3423, 1), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 0) {
					setGnomeStage(player, 1);
				}
				player.getSkills().addXp(Skills.AGILITY, 7.5);
			}
		}, 1);
	}

	public static void climbUpGnomeTreeBranch(final Player player) {
		player.getPackets().sendGameMessage("You climb the tree...", true);
		player.useStairs(828, new Position(2473, 3420, 2), 1, 2, "... to the platform above.");
		player.lock(2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 1) {
					setGnomeStage(player, 2);
				}
				player.getSkills().addXp(Skills.AGILITY, 5);
			}
		}, 1);
	}

	public static void walkBackGnomeRope(final Player player) {
		if (player.getX() != 2483 || player.getY() != 3420 || player.getZ() != 2) {
			return;
		}
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(7);
		player.addWalkSteps(2477, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearence().setRenderEmote(155);
				} else {
					player.getAppearence().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7);
					player.getPackets().sendGameMessage("You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void walkGnomeRope(final Player player) {
		if (player.getX() != 2477 || player.getY() != 3420 || player.getZ() != 2) {
			return;
		}
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(7);
		player.addWalkSteps(2483, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearence().setRenderEmote(155);
				} else {
					player.getAppearence().setRenderEmote(-1);
					player.setRunHidden(running);
					if (getGnomeStage(player) == 2) {
						setGnomeStage(player, 3);
					}
					player.getSkills().addXp(Skills.AGILITY, 7);
					player.getPackets().sendGameMessage("You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void climbDownGnomeTreeBranch(final Player player) {
		player.useStairs(828, new Position(2487, 3421, 0), 1, 2, "You climbed the tree branch succesfully.");
		player.lock(2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 3) {
					setGnomeStage(player, 4);
				}
				player.getSkills().addXp(Skills.AGILITY, 5);
			}
		}, 1);
	}

	public static void climbGnomeObstacleNet2(final Player player) {
		if (player.getY() != 3425) {
			return;
		}
		player.lock(2);
		player.getPackets().sendGameMessage("You climb the netting.", true);
		player.useStairs(828, new Position(player.getX(), player.getY() == 3425 ? 3428 : 3425, 0), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 4) {
					setGnomeStage(player, 5);
				}
				player.getSkills().addXp(Skills.AGILITY, 8);
			}
		}, 1);
	}

	public static void enterGnomePipe(final Player player, int objectX, int objectY) {
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(objectX, objectY == 3431 ? 3437 : 3430, -1, false);
		player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
		if (getGnomeStage(player) == 5) {
			removeGnomeStage(player);
			player.getSkills().addXp(Skills.AGILITY, 39.5);
			player.agilityLapsG++;
			player.gnomescourse++;
		}
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
					player.getSkills().addXp(Skills.AGILITY, 7);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void removeGnomeStage(Player player) {
		player.getTemporaryAttributtes().remove("GnomeCourse");
	}

	public static void setGnomeStage(Player player, int stage) {
		player.getTemporaryAttributtes().put("GnomeCourse", stage);
	}

	public static int getGnomeStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributtes().get("GnomeCourse");
		if (stage == null) {
			return -1;
		}
		return stage;
	}
}
