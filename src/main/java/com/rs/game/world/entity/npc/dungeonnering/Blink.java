package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.CoresManager;
import com.rs.cores.FixedLengthRunnable;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

import java.util.concurrent.TimeUnit;


@SuppressWarnings("serial")
public class Blink extends DungeonBoss {

	private static final int[][] RUSH_COORDINATES =
			{
					{ 2, 3, 13, 3 },
					{ 2, 6, 13, 6 },
					{ 2, 9, 13, 9 },
					{ 2, 12, 13, 12 },
					{ 3, 2, 3, 13 },
					{ 6, 2, 6, 13 },
					{ 9, 2, 9, 13 },
					{ 12, 2, 12, 13 }, };
	private static final int[] FAILURE_SOUNDS = new int[]
			{ 3005, 3006, 3010, 3014, 3048, 2978 };
	private static final int[] RUSH_SOUNDS =
			{ 2982, 2987, 2988, 2989, 2990, 2992, 2998, 3002, 3004, 3009, 3015, 3017, 3018, 3021, 3026, 3027, 3031, 3042, 3043, 3047, 3049 };
	private static final String[] RUSH_MESSAGES =
			{
					"Grrrr...",
					"More t...tea Alice?",
					"Where...who?",
					"H..here it comes!",
					"See you all next year!",
					"",
					"",
					"",
					"Coo-coo-ca-choo!",
					"Ah! Grrrr...",
					"Aha! Huh? Ahaha!",
					"",
					"",
					"A face! A huuuge face!",
					"Aaahaahaha!",
					"C...can't catch me!",
					"A whole new world!",
					"Over here!",
					"There's no place like home.",
					"The...spire...doors...everywhere..." };

	private int rushCount, rushStage;
	private int[] selectedPath;
	private boolean inversedPath, specialRequired, checkingPath;
	private Position toPath, activePillar;

	public Blink(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		setForceFollowClose(true);
		setRun(true);
		rushCount = 0;
		rushStage = 4;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (rushCount == -1)
			super.handleIngoingHit(hit);
		if (getHitpoints() <= getMaxHitpoints() * (rushStage * .2125)) {
			rushStage--;
			rushCount = 0;
		}
	}

	@SuppressWarnings("deprecation")
	private void checkForPillars() {
		checkingPath = true;
		try {
			CoresManager.scheduleRepeatedTask(new FixedLengthRunnable() {
				@Override
				public boolean repeat() {
					try {
						if (isDead() || hasFinished()) {
							checkingPath = false;
							return false;
						}
						canMove(Math.round(getDirection() / 2048));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
			}, 0, 100, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 1.0;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.8;
	}

	private void stopRushAttack() {
		this.resetWalkSteps();
		rushCount = -1;//stops the rush
		//	playSoundEffect(FAILURE_SOUNDS[Utils.random(FAILURE_SOUNDS.length)]);
		setNextForceTalk(new ForceTalk("Oof!"));
		animate(new Animation(14946));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				setSpecialRequired(true);
				setCantInteract(false);
			}
		});
	}
	//TODO FIX THIS SHIT

	@Override
	public boolean canMove(int dir) {
		if (!hasActivePillar() || rushCount < 11)
			return true;
		int nextX = getX();
		int nextY = getY();
		for (int i = 1; i < 3; i++) {
			if (dir == 0)
				nextY--;
			else if (dir == 2)
				nextX--;
			else if (dir == 4)
				nextY++;
			else
				nextX++;
			if (nextX == activePillar.getX() && nextY == activePillar.getY()) {
				stopRushAttack();
				return false;
			}
		}
		return true;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!checkingPath)
			checkForPillars();
		if (rushCount > -1) {
			if (getManager().isDestroyed() || isDead())
				return;
			rushCount++;
			if (rushCount == 1) {
				getCombat().setTarget(null);
				resetWalkSteps();
				setNextFaceEntity(null);
				resetCombat();
				setTarget(null);
				setCantInteract(true);
			} else if (rushCount == 3) {
				setNextForceTalk(new ForceTalk("He saw me!"));
				//playSoundEffect(3017);
			} else if (rushCount == 4) {
				animate(new Animation(14994));
				setNextGraphics(new Graphics(2868));
			} else if (rushCount == 15 || rushCount == 5) {
				if (rushCount == 15)
					rushCount = 5;
				setNextNPCTransformation(1957);
			} else if (rushCount == 8) {
				setNextPosition(getNextPath());
			} else if (rushCount == 9) {
				setNextNPCTransformation(12865);
				toPath = getManager().getTile(getReference(), selectedPath[inversedPath ? 2 : 0], selectedPath[inversedPath ? 3 : 1]);
				addWalkSteps(toPath.getX(), toPath.getY(), 1, false);
			} else if (rushCount == 10) {
				addWalkSteps(toPath.getX(), toPath.getY(), -1, false);
				int index = com.rs.utility.Utils.random(RUSH_MESSAGES.length);
				setNextForceTalk(new ForceTalk(RUSH_MESSAGES[index]));
				//playSoundEffect(RUSH_SOUNDS[index]);
			} else if (rushCount == 11) {
				setNextGraphics(new Graphics(2869));
				for (Player player : getManager().getParty().getTeam()) {
					if(!getManager().getCurrentRoomReference(this).equals(getManager().getCurrentRoomReference(player))) {
						continue;
					}
					if (!com.rs.utility.Utils.isOnRange(player.getX(), player.getY(), 1, getX(), getY(), 1, 4))
						continue;
					int damage = com.rs.utility.Utils.random(200, 600);
					if (player.getPrayer().isMageProtecting() || player.getPrayer().isRangeProtecting())
						damage *= .5D;
					player.setNextGraphics(new Graphics(2854));
					player.applyHit(new Hit(this, damage, HitLook.REGULAR_DAMAGE, 35));

				}
			}
		}
	}

	public Position getNextPath() {
		selectedPath = RUSH_COORDINATES[com.rs.utility.Utils.random(RUSH_COORDINATES.length)];
		inversedPath = com.rs.utility.Utils.random(2) == 0;
		return getManager().getTile(getReference(), selectedPath[inversedPath ? 0 : 2], selectedPath[inversedPath ? 1 : 3]);
	}

	public void raisePillar(WorldObject selectedPillar) {
		final WorldObject newPillar = new WorldObject(selectedPillar);
		newPillar.setId(32196);//Our little secret :D
		activePillar = new Position(selectedPillar);
		World.spawnObjectTemporary(newPillar, 2500);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				activePillar = null;
			}
		}, 4);
	}

	public boolean hasActivePillar() {
		return activePillar != null;
	}

	public boolean isSpecialRequired() {
		return specialRequired;
	}

	public void setSpecialRequired(boolean specialRequired) {
		this.specialRequired = specialRequired;
	}
}
