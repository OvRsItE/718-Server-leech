package com.rs.game.world.entity.npc.combat.impl;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.utility.Utils;

/**
 * @author 'Corey 2010
 */

public class DagannothSupremeCombat extends CombatScript{

	public Object[] getKeys() {
		return new Object[] { 2881 };
	}
	
	private transient long stopDelay;
	
	public long getStopDelay() {
		return stopDelay;
	}

	public void setInfiniteStopDelay() {
		stopDelay = Long.MAX_VALUE;
	}

	public void resetStopDelay() {
		stopDelay = 0;
	}

	public void addStopDelay(long delay) {
		stopDelay = Utils.currentTimeMillis() + (delay * 600);
	}

	public boolean spawnedMinions = false;
	public int doneSpecAttack = 0;
	
	private transient Position lastPosition;
	private transient Position nextPosition;
	
	public Position getNpcCurrentTile(final NPC npc) {
		return new Position(npc.getX(), npc.getY(), npc.getZ());
	}
	
	@Override
	public int attack(final NPC npc, final Entity target) {
		final int hit = Utils.getRandom(300) + 20;//294
		//final int specialHit = player.getHitpoints() / 2;//half hp
		int attackStyle = Utils.getRandom(1);
        //int distanceX = target.getX() - npc.getX();
        //int distanceY = target.getY() - npc.getY();
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = (Player) target;
		
		if (npc.withinDistance(target, 20)) {
			switch(attackStyle){
			
			case 0:
						npc.animate(new Animation(2855));
						World.sendProjectile(npc, target, 475, 34, 16, 40, 35, 16, 0); 
						delayHit(npc, 1, target, new Hit[] {
								getRangeHit(npc, hit)
						});
				break;
			case 1:
						npc.animate(new Animation(2855));
						World.sendProjectile(npc, target, 475, 34, 16, 40, 35, 16, 0); 
						delayHit(npc, 1, target, new Hit[] {
								getRangeHit(npc, hit)
						});
				break;
			}
			}
			return defs.getAttackDelay();

		}
	}