package com.rs.game.world.entity.npc.combat.impl;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Equipment;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.toxin.ToxinType;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.network.decoders.handlers.ButtonHandler;
import com.rs.utility.Utils;

public class Korasi extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 263 };
	}
	

public int attack(final NPC npc, final Entity target) {
	final Player player = (Player) target;
	final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.random(10);
		if(attackStyle == 0) { //Constant hit 100 
			npc.animate(new Animation(1979));
			npc.setNextForceTalk(new ForceTalk("I will end you!!!"));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 0;
				@Override
				public void run() {
					for(Entity t : npc.getPossibleTargets()) { //lets just loop all players for massive moves
							t.applyHit(new Hit(npc, (int) 100, HitLook.MAGIC_DAMAGE));
							delayHit(npc, 0, t, new Hit(npc, 100, HitLook.REGULAR_DAMAGE));
					}
					if(count++ == 4) {
						stop();
						return;
					}
				}
			}, 0, 0);
			
			}else if(attackStyle == 1) { //heal
				npc.setNextForceTalk(new ForceTalk("Justice!!!"));
				npc.animate(new Animation(14788));
				npc.setNextGraphics(new Graphics(1729));
				delayHit(npc, 0, player, new Hit(npc, 500, HitLook.MAGIC_DAMAGE));

				
		}else if(attackStyle == 2) { //heal
				npc.setNextForceTalk(new ForceTalk("Heal me almighty Saradomin"));
				npc.setNextGraphics(new Graphics(3067));
				player.getPackets().sendGameMessage("You've been injured and you can't use protective curses!");
				player.setPrayerDelay(Utils.getRandom(20000) + 5);
				npc.heal(500);	
				
			}else if(attackStyle == 3) { //to be decided
			  npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (Entity t : npc.getPossibleTargets()) {
						t.setNextGraphics(new Graphics(2741, 0, 100));
						//delayHit(npc, 0, t, new Hit(npc, 500, HitLook.MAGIC_DAMAGE));
						t.applyHit(new Hit(npc, (int) 400, HitLook.MAGIC_DAMAGE));
						
					}
					npc.getCombat().addCombatDelay(3);
					npc.setCantInteract(false);
					npc.setTarget(target);
				}

			}, 4);
			
		}else if(attackStyle == 4) { //Ring attack
			npc.setNextForceTalk(new ForceTalk("Gahhhh!!!!!"));
			npc.animate(new Animation(11364));
			npc.setNextGraphics(new Graphics(2600));
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (Entity t : npc.getPossibleTargets()) {
						t.applyHit(new Hit(npc, (int) (t.getHitpoints() * Math.random()), HitLook.REGULAR_DAMAGE, 0));
					}
					npc.getCombat().addCombatDelay(3);
					npc.setCantInteract(false);
					npc.setTarget(target);
				}

			}, 4);
		} else if (attackStyle == 5) {  // freeze
			npc.setNextForceTalk(new ForceTalk("Arghhh!!!"));
			npc.animate(new Animation(1979));
			playSound(171, player, target);
			target.addFreezeDelay(20000, true);
			delayHit(npc, 0, player, new Hit(npc, Utils.getRandom(200), HitLook.MAGIC_DAMAGE));
			target.getToxin().applyToxin(ToxinType.POISON);
			
		} else if (attackStyle == 6) { //Jad range attack
			npc.setNextForceTalk(new ForceTalk("This will do!"));
			target.setNextGraphics(new Graphics(3000));
			delayHit(npc, 0, player, new Hit(npc, 300, HitLook.RANGE_DAMAGE));

		} else if (attackStyle == 7) { //to be decided

			
		} else if (attackStyle == 8 && player.getInventory().getFreeSlots() > 0) { //removes weapon
			
			npc.setNextForceTalk(new ForceTalk("Your weapon is weak!!!"));
			ButtonHandler.sendRemove(player, Equipment.SLOT_WEAPON);
			delayHit(npc, 0, player, new Hit(npc, 100, HitLook.RANGE_DAMAGE));
			
		} else if (attackStyle == 9) { //teleports
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					npc.setCantInteract(false);
					npc.setTarget(target);
					int size = npc.getSize();
					int[][] dirs = Utils.getCoordOffsetsNear(size);
					for (int dir = 0; dir < dirs[0].length; dir++) {
						final Position tile = new Position(new Position(
								target.getX() + dirs[0][dir], target.getY()
										+ dirs[1][dir], target.getZ()));
						if (World.canMoveNPC(tile.getZ(), tile.getX(),
								tile.getY(), size)) { // if found done
							npc.setNextPosition(tile);
						}
					}
				}
			}, 3);
			return defs.getAttackDelay();
		}
		return 10;
	}

public void playSound(int soundId, Player player, Entity target) {
	if (soundId == -1)
		return;
	player.getPackets().sendSound(soundId, 0, 1);
	if (target instanceof Player) {
		Player p2 = (Player) target;
		p2.getPackets().sendSound(soundId, 0, 1);
	}
}

}