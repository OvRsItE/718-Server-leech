package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.NewForceMovement;

@SuppressWarnings("serial")
public class Rammernaut extends DungeonBoss {

	private Player chargeTarget;
	private int count;
	private boolean requestSpecNormalAttack;

	public Rammernaut(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		setForceFollowClose(true);
	}

	public void fail() {
		animate(new Animation(13707));
		setNextForceTalk(new ForceTalk("Oooof!!"));
		count = -14;
	}

	public void sucess() {
		resetWalkSteps();
		animate(new Animation(13698));
		applyStunHit(chargeTarget, (int) (chargeTarget.getMaxHitpoints() * 0.6));
		requestSpecNormalAttack = true;
		count = -12;
	}

	public void applyStunHit(final Entity entity, int maxHit) {
		entity.applyHit(new Hit(this, com.rs.utility.Utils.random(maxHit) + 1, Hit.HitLook.REGULAR_DAMAGE));
		entity.setFreezeDelay(2);
		if (entity instanceof Player) {
			Player player = (Player) entity;
			player.stopAll();
			player.getPackets().sendGameMessage("You've been stunned.");
			player.setFreezeDelay(2);
			if (player.getPrayer().hasPrayersOn()) {
				player.getPackets().sendGameMessage("Your prayers have been disabled.");
				player.setPrayerDelay(1000);
			}
			final NPC npc = this;
			WorldTasksManager.schedule(new WorldTask() {
				private int ticks;
				private Position tile;

				@Override
				public void run() {
					ticks++;
					if (ticks == 1) {
						byte[] dirs = com.rs.utility.Utils.getDirection(getDirection());
						for (int distance = 6; distance >= 0; distance--) {
							tile = new Position(new Position(entity.getX() + (dirs[0] * distance), entity.getY() + (dirs[1] * distance), entity.getZ()));
							if (World.isFloorFree(tile.getZ(), tile.getX(), tile.getY()) && getManager().isAtBossRoom(tile))
								break;
							else if (distance == 0)
								tile = new Position(entity);
						}
						entity.faceEntity(npc);
						entity.animate(new Animation(10070));
						entity.setNextForceMovement(new NewForceMovement(entity, 0, tile, 2, entity.getDirection()));
					} else if (ticks == 2) {
						entity.setNextPosition(tile);
						stop();
						return;
					}
				}
			}, 0, 0);
		}
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		if (chargeTarget != null) {
			setNextFaceEntity(chargeTarget);
			if (count == 0) {
				setNextForceTalk(new ForceTalk("CHAAAAAARGE!"));
				setRun(true);
			} else if (count == -10) {
				setRun(false);
				resetWalkSteps();
				//TODO movement system
				//calcFollow(chargeTarget, true);
			} else if (count == -8) {
				setChargeTarget(null);
			} else if (count > 2) {
				resetWalkSteps();
				/*
				 * skip first step else it stucks ofc
				 */
				//TODO movement system
				//calcFollow(chargeTarget, true);

				if (count != 3 && !World.isTileFree(getZ(), getX(), getY(), getSize()))
					fail();
				else if (com.rs.utility.Utils.isOnRange(getX(), getY(), getSize(), chargeTarget.getX(), chargeTarget.getY(), chargeTarget.getSize(), 0))
					sucess();
				else if (!hasWalkSteps())
					fail();
			}
			count++;
			return;
		}
		super.processNPC();

		/*int chargeCount = getChargeCount(npc);
		if (chargeCount > 1) {
		    if (chargeCount == 2) {
			npc.setNextForceTalk(new ForceTalk("CHAAAAAARGE!"));
			npc.setRun(true);
			setChargeCount(npc, 3);
			return defs.getAttackDelay();
		    } else if (chargeCount == 3) {
			npc.calcFollow(target, true); //gotta be changed later
			if (Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize(), 0)) {
			    npc.setNextAnimation(new Animation(13705));
			    setChargeCount(npc, 0);
			    return defs.getAttackDelay();
			}
		    }
		//    System.out.println("Time to charge " + chargeCount);
		    return 0;
		}*/
	}

	public void setChargeTarget(Player target) {
		this.chargeTarget = target;
		getCombat().removeTarget();
		count = 0;
	}

	public boolean isRequestSpecNormalAttack() {
		return requestSpecNormalAttack;
	}

	public void setRequestSpecNormalAttack(boolean requestSpecNormalAttack) {
		this.requestSpecNormalAttack = requestSpecNormalAttack;
	}
}
