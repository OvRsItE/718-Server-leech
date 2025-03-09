package com.rs.game.world.entity.npc.combat;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.npc.godwars.zaros.Nex;
import com.rs.game.world.entity.npc.instances.EliteDungeon.BossNpc;
import com.rs.game.world.entity.npc.instances.EliteDungeon.Olm;
import com.rs.game.world.entity.npc.kraken.KrakenNPC;
import com.rs.game.world.entity.npc.kraken.TentacleNPC;
import com.rs.game.world.entity.npc.pest.PestPortal;
import com.rs.game.world.entity.npc.pet.Pet;
import com.rs.game.world.entity.npc.sire.AbyssalSire;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.actions.PlayerCombat;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceMovement;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.MapAreas;
import com.rs.utility.Utils;

public final class NPCCombat {
	
    private boolean forceCheckClipAsRange(Entity target) {
    	return target instanceof PestPortal || target instanceof KrakenNPC || target instanceof TentacleNPC || target instanceof Olm || target instanceof com.rs.game.world.entity.npc.instances.TheHive.BossNpc;
    }

	private final NPC npc;
	private int combatDelay;
	private Entity target;
	private final CombatScript script;

	public NPCCombat(NPC npc) {
		this.npc = npc;
		script = CombatScriptsHandler.init(npc);
	}

	public int getCombatDelay() {
		return combatDelay;
	}

	/*
	 * returns if under combat
	 */
	public boolean process() {
		if (!npc.isCanRetaliate())
			return false;
		if (combatDelay > 0)
			combatDelay--;
		if (target != null) {
			if (!checkAll()) {
				removeTarget();
				return false;
			}
			if (combatDelay <= 0)
				combatDelay = combatAttack();
			return true;
		}
		return false;
	}

	/*
	 * return combatDelay
	 */
	private int combatAttack() {
		
		Entity target = this.target; // prevents multithread issues
		if (target == null)
			return 0;
		// if hes frooze not gonna attack
		if (npc.isCanBeFrozen() && npc.getFreezeDelay() >= Utils.currentTimeMillis())
			return 0;
		if (npc.isLocked())
			return 0;
		// check if close to target, if not let it just walk and dont attack
		// this gameticket
		
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		int maxDistance = attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.MAGE_FOLLOW ? 0 : npc.getAttackDistance();
		if ((!(npc instanceof Nex)) && (!(npc instanceof AbyssalSire)) && !npc.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target))) {
		    return 0;
		}
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getSize();
		if (distanceX > size + maxDistance || distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			return 0;
		}

		addAttackedByDelay(target);
		return script.attack(npc, target);
	}


	protected void doDefenceEmote(Entity target) {
		/*
		 * if (target.getNextAnimation() != null) // if has att emote already
		 * return;
		 */
		target.setNextAnimationNoPriority(new Animation(Combat
				.getDefenceEmote(target)));
	}

	public Entity getTarget() {
		return target;
	}

	public void addAttackedByDelay(Entity target) { // prevents multithread issues
		target.setAttackedBy(npc);
		target.setAttackedByDelay(Utils.currentTimeMillis()
				+ npc.getCombatDefinitions().getAttackDelay() * 600 + 600); // 8seconds
	}

	public void setTarget(Entity target) {
		this.target = target;
		npc.setNextFaceEntity(target);
		if (!checkAll())
			removeTarget();
	}

	public boolean checkAll() {
		Entity target = this.target; // prevents multithread issues
		if (target == null)
			return false;
		if (npc.isDead() || npc.isFinished() || npc.isForceWalking()
				|| target.isDead() || target.isFinished()
				|| npc.getZ() != target.getZ())
			return false;
		
		if (npc.isCanBeFrozen() && npc.getFreezeDelay() >= Utils.currentTimeMillis())
			return true; // if freeze cant move ofc
		int distanceX = npc.getX() - npc.getSpawnPosition().getX();
		int distanceY = npc.getY() - npc.getSpawnPosition().getY();
		int size = npc.getSize();
		int maxDistance;
		int agroRatio = npc.getCombatDefinitions().getAgroRatio();
		if (!npc.isNoDistanceCheck() && !npc.isCantFollowUnderCombat()) {
			maxDistance = Math.max(agroRatio, 12);
			if (!(npc instanceof Familiar || npc instanceof Pet)) {

				if (npc.getMapAreaNameHash() != -1) {
					// if out his area
					if (!MapAreas.isAtArea(npc.getMapAreaNameHash(), npc)
							|| (!npc.canBeAttackFromOutOfArea() && !MapAreas
									.isAtArea(npc.getMapAreaNameHash(), target))) {
						npc.forceWalkRespawnTile();
						return false;
					}
				} else if (distanceX > size + maxDistance
						|| distanceX < -1 - maxDistance
						|| distanceY > size + maxDistance
						|| distanceY < -1 - maxDistance) {
					// if more than 64 distance from respawn place
					npc.forceWalkRespawnTile();
					return false;
				}
			}
			maxDistance = Math.max(agroRatio, 16);
			distanceX = target.getX() - npc.getX();
			distanceY = target.getY() - npc.getY();
			if (distanceX > size + maxDistance || distanceX < -1 - maxDistance
					|| distanceY > size + maxDistance
					|| distanceY < -1 - maxDistance)
				return false; // if target distance higher 16
		} else {
			distanceX = target.getX() - npc.getX();
			distanceY = target.getY() - npc.getY();
		}
		// checks for no multi area :)
		if (npc instanceof Familiar || npc instanceof Pet) {
			
		} else {
			if (!npc.isForceMultiAttacked()) {
				if (!target.isAtMultiArea() || !npc.isAtMultiArea()) {
					if (npc.getAttackedBy() != target
							&& npc.getAttackedByDelay() > Utils
									.currentTimeMillis())
						return false;
					if (target.getAttackedBy() != npc
							&& target.getAttackedByDelay() > Utils
									.currentTimeMillis())
						return false;
				}
			}
		}
		if (!npc.isCantFollowUnderCombat()) {
			// if is under
			int targetSize = target.getSize();
			if (distanceX < size && distanceX > -targetSize && distanceY < size
					&& distanceY > -targetSize && !target.hasWalkSteps()) {
				combatDelay = 1; //Delay comabt till target is attackable
				npc.resetWalkSteps();
				if (!npc.addWalkSteps(target.getX() + 1, npc.getY())) {
					npc.resetWalkSteps();
					if (!npc.addWalkSteps(target.getX() - size, npc.getY())) {
						npc.resetWalkSteps();
						if (!npc.addWalkSteps(npc.getX(), target.getY() + 1)) {
							npc.resetWalkSteps();
							if (!npc.addWalkSteps(npc.getX(), target.getY()
									- size)) {
								return true;
							}
						}
					}
				}
				return true;
			}
			if (npc.getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE
					&& targetSize == 1
					&& size == 1
					&& Math.abs(npc.getX() - target.getX()) == 1
					&& Math.abs(npc.getY() - target.getY()) == 1
					&& !target.hasWalkSteps()) {

				if (!npc.addWalkSteps(target.getX(), npc.getY(), 1))
					npc.addWalkSteps(npc.getX(), target.getY(), 1);
				return true;
			}

			int attackStyle = npc.getCombatDefinitions().getAttackStyle();
			if (npc instanceof Nex) {
				Nex nex = (Nex) npc;
				maxDistance = nex.isFollowTarget() ? 0 : 7;
				if (nex.getFlyTime() == 0 && (!npc.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target))) || !Utils.isOnRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize, maxDistance)) {
					npc.resetWalkSteps();
					npc.addWalkStepsInteract(target.getX(), target.getY(), 2,
							size, true);
					if (!npc.hasWalkSteps()) {
						int[][] dirs = Utils.getCoordOffsetsNear(size);
						for (int dir = 0; dir < dirs[0].length; dir++) {
							final Position tile = new Position(new Position(
									target.getX() + dirs[0][dir], target.getY()
											+ dirs[1][dir], target.getZ()));
							if (World.canMoveNPC(tile.getZ(), tile.getX(),
									tile.getY(), size)) { // if found done
								nex.setFlyTime(4);
								npc.setNextForceMovement(new ForceMovement(
										new Position(npc), 0, tile, 1, Utils
												.getMoveDirection(
														tile.getX()
																- npc.getX(),
														tile.getY()
																- npc.getY())));
								npc.animate(new Animation(17408));
								npc.setNextPosition(tile);
								return true;
							}
						}
					}
					return true;
				} else
					// if doesnt need to move more stop moving
					npc.resetWalkSteps();
			} else {
				maxDistance = npc.isForceFollowClose() ? 0 : (attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.MAGE_FOLLOW) ? 0 : npc.followDistance > 0 ? npc.followDistance : npc.getAttackDistance();
				npc.resetWalkSteps();
				// is far from target, moves to it till can attack
				
				if ((!npc.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target))) || !Utils.isOnRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize, maxDistance)) {
					if (!npc.addWalkStepsInteract(target.getX(), target.getY(),	2, size, true) && combatDelay < 3)
						combatDelay = 3;
					return true;
				}
				
				// if under target, moves

			}
		}
		return true;
	}

	public void addCombatDelay(int delay) {
		combatDelay += delay;
	}

	public void setCombatDelay(int delay) {
		combatDelay = delay;
	}

	public boolean underCombat() {
		return target != null;
	}

	public void removeTarget() {
		this.target = null;
		npc.setNextFaceEntity(null);
	}

	public void reset() {
		combatDelay = 0;
		target = null;
	}

	public void delayHit(NPC npc, int delay, final Entity target,
    	    final Hit... hits) {
    	npc.getCombat().addAttackedByDelay(target);
    	WorldTasksManager.schedule(new WorldTask() {

    	    @Override
    	    public void run() {
    		for (Hit hit : hits) {
    		    NPC npc = (NPC) hit.getSource();
    		    if (npc.isDead() || npc.isFinished() || target.isDead()
    			    || target.isFinished())
    			return;
    		    target.applyHit(hit);
    		    npc.getCombat().doDefenceEmote(target);
    		    if (target instanceof Player) {
    			Player p2 = (Player) target;
    			p2.closeInterfaces();
    			if (p2.getCombatDefinitions().isAutoRelatie()
    				&& !p2.getActionManager().hasSkillWorking()
    				&& !p2.hasWalkSteps())
    			    p2.getActionManager().setAction(
    				    new PlayerCombat(npc));
    		    } else {
    			NPC n = (NPC) target;
    			if (!n.isUnderCombat()
    				|| n.canBeAttackedByAutoRelatie())
    			    n.setTarget(npc);
    		    }

    		}
    	    }

    	}, delay);
        }

}
