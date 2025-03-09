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



@SuppressWarnings("unused")
public class GanodermicBeast extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 14696, 14697 };
	}

	@Override
	public int attack(NPC npc, Entity target) {

		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Player player = (Player) target;
		if (!isDistant(npc, target)) {
			if (Utils.random(10) <= 8) {
				specialAttack(npc, target);	
				return defs.getAttackDelay();
			} else {
				npc.animate(new Animation(15466));
				delayHit(
						npc,
						1,
						target,
						getMeleeHit(
								npc,
								getRandomMaxHit(npc, 400,
										NPCCombatDefinitions.MELEE, target)));
				return defs.getAttackDelay();
				
			}
		} else if (isDistant(npc, target)) {
			if (Utils.random(3) == 0) {
				Jump(npc, target);
				return defs.getAttackDelay();
			} else {
				npc.animate(new Animation(15470));
				npc.setNextGraphics(new Graphics(2038));
				//npc.setNextGraphics(new Graphics(2032));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, 400,
									NPCCombatDefinitions.MAGE, target)));
			
			World.sendProjectile(npc, target, 2035, 50, 32, 30, 50, 1, 0);
			}
		}
		return defs.getAttackDelay();
	}
	
	public void Jump(final NPC npc, final Entity target) {
		if (isDistant(npc, target)) {
		npc.addWalkSteps(target.getX(), target.getY(), 1, false);
		npc.setNextForceTalk(new ForceTalk ("Krrr!"));
		npc.faceEntity(target);
		npc.animate(new Animation(15465));
	} else {
		specialAttack(npc, target);
	}
		
}



public void specialAttack(final NPC npc, final Entity target) {
	talk(npc, "Krrr!");
	npc.animate(new Animation(15466));
	//target.setNextGraphics(new Graphics(2036));
	//World.sendProjectile(npc, target, 2036, 35, 32, 30, 50, 1, 0);
	((Player) target).getPackets().sendGameMessage("The creature infests you with its toxic fungus");
	WorldTasksManager.schedule(new WorldTask() {
		int loop;

		@Override
		public void run() {
			if (loop != 0) {
				target.applyHit(
					new Hit(target, Utils.random(30, 30), HitLook.POISON_DAMAGE));
				
			}
			
			if (isDistant(npc, target)) {
				stop();
			}
			
			loop++;
		}
		
	}, 0, 2);
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
