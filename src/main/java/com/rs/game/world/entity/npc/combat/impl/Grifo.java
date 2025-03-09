package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;
/**
 * 
 * @author Taylor, the author of all the classes that werent in matrix 718 but now are. But since
 * Eclipse doesn't auto tag i forget sometimes to tag myself but w.e who cares.
 * TODO Neem oil
 *
 */
public class Grifo extends CombatScript {
	
	

	@Override
	public Object[] getKeys() {
		return new Object[] { 14700, 14701 };
	}
	
	

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		
		Player targetPlayer = (Player) target;
		int attackStyle = Utils.getRandom(10);
			
		if (isDistant(npc, target)) {
			if (attackStyle >= 8) {
				Jump(npc, target);
			}else
				if (attackStyle <= 7) {
					
					npc.animate(new Animation(15497));
					npc.setNextGraphics(new Graphics(2034));
					//npc.setNextGraphics(new Graphics(2038));
					delayHit(npc, 2,target, getMagicHit(npc,getRandomMaxHit(npc, 180,NPCCombatDefinitions.MAGE, target)));
					World.sendProjectile(npc, target, 2035, 35, 32, 30, 50, 1, 0);
					
				} 
			
		}else
		if (!isDistant(npc, target)) {
			specialAttack(npc, target);
		}
		return defs.getAttackDelay();
	}
	
{
	
}
public void Jump(final NPC npc, final Entity target) {
		if (isDistant(npc, target)) {
		npc.setNextForceTalk(new ForceTalk ("Krrr!"));
		npc.animate(new Animation(15491));
		npc.faceEntity(target);
		npc.addWalkSteps(target.getX(), target.getY(), 1, false);
	} else {
		specialAttack(npc, target);
	}
}



public void specialAttack(final NPC npc, final Entity target) {
	move(npc, 14700);
	talk(npc, "Krrr!");
	npc.animate(new Animation(15495));
	npc.setNextGraphics(new Graphics(2036));
	//World.sendProjectile(npc, target, 2036, 35, 32, 30, 50, 1, 0);
	((Player) target).getPackets().sendGameMessage("The creature infests you with its toxic fungus");
	WorldTasksManager.schedule(new WorldTask() {
		int loop;

		@Override
		public void run() {
			if (loop == 1) {
				target.applyHit(
					new Hit(target, Utils.random(20, 50), HitLook.POISON_DAMAGE));
				target.applyHit(
						new Hit(target, Utils.random(20, 50), HitLook.POISON_DAMAGE));
				
			}
			if (isDistant(npc, target)) {
				stop();
			}
			loop++;
		}
	}, 0, 1);
}

public boolean isDistant(NPC npc, Entity target) {
	int size = npc.getSize();
	int dX = target.getX() - npc.getX();
	int dY = target.getY() - npc.getY();
	if (dX > size || dX < -1 || dY > size
		|| dY < -1) {
	return true;
	
}
return false;
}
public void talk(NPC npc, String text) {
	npc.setNextForceTalk(new ForceTalk(text));
}
public void move(NPC npc, int animation) {
	npc.animate(new Animation(animation));
}
public void gfx(NPC npc, int gfx) {
	npc.setNextGraphics(new Graphics(15491));
}



}
