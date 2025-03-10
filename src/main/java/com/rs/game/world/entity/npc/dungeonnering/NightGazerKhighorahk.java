package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonConstants;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public final class NightGazerKhighorahk extends DungeonBoss {

	private boolean secondStage;
	private boolean usedSpecial;
	private int lightCount;

	public NightGazerKhighorahk(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		setCantFollowUnderCombat(true); //force cant walk
	}

	public boolean isSecondStage() {
		return secondStage;
	}

	@Override
	public void sendDeath(final Entity source) {
		if (!secondStage) {
			secondStage = true;
			animate(new Animation(getCombatDefinitions().getDeathEmote()));
			setNextNPCTransformation(9739);
			setCombatLevel((int) (getCombatLevel() * 0.85)); //15% nerf
			setHitpoints(getMaxHitpoints());
			resetBonuses();
			return;
		}
		super.sendDeath(source);
	}

	public boolean isUsedSpecial() {
		return usedSpecial;
	}

	public void setUsedSpecial(boolean usedSpecial) {
		this.usedSpecial = usedSpecial;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (!secondStage)
			reduceHit(hit);
		super.handleIngoingHit(hit);
	}

	public void reduceHit(Hit hit) {
		if (hit.getLook() != Hit.HitLook.MELEE_DAMAGE && hit.getLook() != Hit.HitLook.RANGE_DAMAGE && hit.getLook() != Hit.HitLook.MAGIC_DAMAGE)
			return;
		hit.setDamage((int) (hit.getDamage() * lightCount * 0.25));
	}

	public void lightPillar(Player player, WorldObject object) {
		if (lightCount >= 4)
			return;
		if (!player.getInventory().containsItem(DungeonConstants.TINDERBOX, 1)) {
			player.getPackets().sendGameMessage("You need a tinderbox to do this.");
			return;
		}
		player.animate(new Animation(833));
		final WorldObject light = new WorldObject(object);
		light.setId(object.getId() + 1);

		World.spawnObject(light);
		lightCount++;

		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				try {
					lightCount--;
					if (light != null)
						World.removeObject(light);
					for (Entity target : getPossibleTargets()) {
						if (target == null)
							continue;
						if (target.withinDistance(light, 2)) {
							target.applyHit(new Hit(NightGazerKhighorahk.this, com.rs.utility.Utils.random((int) (target.getMaxHitpoints() * 0.25)) + 1, Hit.HitLook.REGULAR_DAMAGE));
							if (target instanceof Player)
								((Player) target).getPackets().sendGameMessage("You are damaged by the shadows engulfing the pillar of light.");
						}
					}
				} catch (Throwable e) {
					com.rs.utility.Logger.handle(e);
				}
			}
			
		}, 49 - (getManager().getParty().getSize() * 5));
	}

	/*  @Override
	  public void sendDeath(final Entity source) {
	final NPCCombatDefinitions defs = getCombatDefinitions();
	resetWalkSteps();
	getCombat().removeTarget();
	setNextAnimation(null);
	WorldTasksManager.schedule(new WorldTask() {
	    int loop;

	    @Override
	    public void run() {
		if (loop == 0) {
		    setNextAnimation(new Animation(defs.getDeathEmote()));
		} else if (loop >= defs.getDeathDelay()) {
		    if (source instanceof Player)
			((Player) source).getControlerManager().processNPCDeath(NightGazerKhighorahk.this);
		    drop();
		    reset();
		    if (source.getAttackedBy() == NightGazerKhighorahk.this) { //no need to wait after u kill
			source.setAttackedByDelay(0);
			source.setAttackedBy(null);
			source.setFindTargetDelay(0);
		    }
		    setCantInteract(true);
		    setNextNPCTransformation(9781);
		    stop();
		}
		loop++;
	    }
	}, 0, 1);
	getManager().openStairs(getReference());
	  }*/

}
