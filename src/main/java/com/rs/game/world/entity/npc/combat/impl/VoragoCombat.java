package com.rs.game.world.entity.npc.combat.impl;

import java.util.ArrayList;
import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.CombatScript;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.npc.vorago.Vorago;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

/**
 * 
 * @author Milzz
 *
 */

public class VoragoCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 13103 };
	}
	
	public boolean spawnLRC = false;

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int size = npc.getSize();
		if (Utils.getRandom(40) == 0) {
			Vorago beast = (Vorago) npc;
			beast.spawnVoragoMinion();
			Player player = (Player) target;
			player.getPackets().sendGameMessage("<col=FF5000>Vorago send a Living Rock Patriarch after you, kill it before it heals him!</col>");
		}
		final ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
		boolean stomp = false;
		for (Entity t : possibleTargets) {
			int distanceX = t.getX() - npc.getX();
			int distanceY = t.getY() - npc.getY();
			if (distanceX < size && distanceX > -1 && distanceY < size
					&& distanceY > -1) {
				stomp = true;
				delayHit(
						npc,
						0,
						t,
						getRegularHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MELEE, t)));
			}
		}
		
		if (stomp) {
			npc.setNextGraphics(new Graphics(1834));
			return defs.getAttackDelay();
		}
		if (Utils.getRandom(4) == 0) {
			switch (Utils.getRandom(4)) { //Vorago Taunts
			case 0:
				npc.setNextForceTalk(new ForceTalk("I will crush you!"));
				break;
			case 1:
				npc.setNextForceTalk(new ForceTalk("Brargh!"));
				npc.playSound(3209, 2);
				break;
			case 2:
				npc.setNextForceTalk(new ForceTalk("Let my magic eat your soul!"));
				break;
			case 3:
				npc.setNextForceTalk(new ForceTalk("I am Vorago! Fear me!"));
				break;
			case 4:
				npc.setNextForceTalk(new ForceTalk("Pain is... Inevitable!"));
				break;
			}
		}
		if (Utils.getRandom(5) == 0) { // Vorago's Heal
			npc.heal(2000);
			npc.setNextForceTalk(new ForceTalk("Your anguish fuels me!"));
		}
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		if (distanceX > size || distanceX < -1 || distanceY > size
				|| distanceY < -1) {//Melee Attack		
			npc.animate(new Animation(12204));
				delayHit(npc, 0, target, getMeleeHit( npc, getRandomMaxHit(npc, defs.getMaxHit(), NPCCombatDefinitions.MELEE, target)));
		
		} 
		if (Utils.getRandom(3) == 0) {
			for (Entity t : npc.getPossibleTargets()) {
				npc.animate(new Animation(12196));
				delayHit(npc, 0, t, getRegularHit(npc, getRandomMaxHit(npc, 500, NPCCombatDefinitions.RANGE_FOLLOW, t)));
				World.sendProjectile(npc, t, 3096, 41, 16, 41, 35, 16, 0);
			}
		}
		if (Utils.getRandom(10) == 0) {//Huge Ranged Attack
			for (Entity t : npc.getPossibleTargets()) {
					npc.animate(new Animation(12205));
					npc.setNextForceTalk(new ForceTalk("FACE THE WRATH OF THE EARTH SHATTERING AROUND YOU!"));
					t.setNextGraphics(new Graphics(3232));
					npc.setNextGraphics(new Graphics(3263));
					t.playSound(1097, 1);
					Player player = (Player) t;
					player.getPackets().sendGameMessage("<col=FF5000>WARNING: Vorago has broken off pieces of himself. Protect missiles now!</col>");
					CoresManager.fastExecutor.schedule(new TimerTask() {
					    @Override
					    public void run() {
					    	for (Entity t : npc.getPossibleTargets()) {
							delayHit(npc, 1, t, getRangeHit(npc, getRandomMaxHit(npc, 10000, NPCCombatDefinitions.RANGE, t)));
							World.sendProjectile(npc, target, 2954, 41, 16, 41, 35, 16, 0);
					    	}
					    }
					}, 5000);		
				}
		} else if (Utils.getRandom(10) == 0) {//Huge Mage Attack
			for (Entity t : npc.getPossibleTargets()) {
				npc.animate(new Animation(12210));
					npc.setNextForceTalk(new ForceTalk("THE GROUND WILL CONSUME YOU!"));
					t.setNextGraphics(new Graphics(3228));
					t.playSound(1097, 1);
					Player player = (Player) t;
					player.getPackets().sendGameMessage("<col=FF5000>WARNING: Vorago is channeling a large magic attack. Protect magic now!</col>");
					CoresManager.fastExecutor.schedule(new TimerTask() {
					    @Override
					    public void run() {
					    	for (Entity t : npc.getPossibleTargets()) {
					npc.setNextGraphics(new Graphics(2963));
					t.playSound(1342, 1);
						delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, 10000, NPCCombatDefinitions.MAGE, t)));
						World.sendProjectile(npc, target, 2963, 41, 16, 41, 35, 16, 0);
					    	}
					    }
					}, 5000);
				}
			}
			return defs.getAttackDelay();
		}
	}
