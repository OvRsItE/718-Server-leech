package com.rs.game.world.entity.npc.nomad;

import java.util.List;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class FlameVortex extends NPC {

	private long explodeTime;
	
	public FlameVortex(Position tile) {
		this(9441, tile, -1, true, true);
	}
	
	public FlameVortex(int id, Position tile, int mapAreaNameHash,
                       boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		explodeTime = Utils.currentTimeMillis() + 60000;
		animate(new Animation(12720));
	}
	
	@Override
	public void processNPC() {
		if (explodeTime == -1)
			return;
		Player target = getTargetToCheck();
		if(target != null && ((target.getX() == getX() && target.getY() == getY()) || (target.getNextRunDirection() != -1 && target.getX() - Utils.DIRECTION_DELTA_X[target.getNextRunDirection()] == getX()
				&& target.getY() - Utils.DIRECTION_DELTA_Y[target.getNextRunDirection()] == getY()))) {
			explode(target, 400);
		}else if (explodeTime < Utils.currentTimeMillis()) 
			explode(target != null && withinDistance(target, 1) ? target : null, Utils.random(400, 701));
	}
	
	public void explode(final Player target, final int damage) {
		explodeTime = -1;
		final NPC npc = this;
		WorldTasksManager.schedule(new WorldTask() {
			
			private boolean secondLoop;
		
			@Override
			public void run() {
				if(!secondLoop) {
					animate(new Animation(12722));
					if(target != null) {
						target.applyHit(new Hit(npc, damage, HitLook.REGULAR_DAMAGE));
						target.setRunEnergy(0);
						target.setNextForceTalk(new ForceTalk("Aiiiiiieeeee!"));
					}
					secondLoop = true;
				}else{
					finish();
					stop();
				}
			}
		}, 0, 0);
	}
	
	
	public Player getTargetToCheck() {
		List<Integer> playerIndexes = World.getRegion(getRegionId()).getPlayerIndexes();
		if(playerIndexes != null) {
			for (int npcIndex : playerIndexes) {
				Player player = World.getPlayers().get(npcIndex);
				if (player == null
						|| player.isDead()
						|| !player.isRunning())
					continue;
				return player;
			}
		}
		return null;
	}

}
