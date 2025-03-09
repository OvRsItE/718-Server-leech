/**
 * 
 */
package com.rs.game.world.entity.player.controller.impl;

import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
public class SmokeDungeonController extends Controller {

	private long damage_tick;
	
	@Override
	public void start() {}
	
	@Override
	public void process() {
		if (!player.withinArea(2816, 2497, 2943, 2559)) {
			player.getControlerManager().removeControlerWithoutCheck();
			return;
		}
		if (!player.getEquipment().wearingFaceMask() && player.getHitpoints() > 10 && damage_tick >= 30) {
			player.setNextForceTalk(new ForceTalk(Utils.random(1) == 0 ? "*cough*" : "*wheeze*"));
			player.applyHit(new Hit(null, 10, HitLook.REGULAR_DAMAGE));
			drainStats();
			damage_tick = 0;
		}
		damage_tick++;
	}
	
	@Override
	public boolean logout() {
		return false;
	}
	
	public boolean login() {
		return false;
	}
	
	@Override
	public boolean processItemTeleport(Position to) {
		player.getControlerManager().removeControlerWithoutCheck();
		return true;
	}
	
	@Override
	public boolean processMagicTeleport(Position to) {
		player.getControlerManager().removeControlerWithoutCheck();
		return true;
	}
	
	private final void drainStats() {
		player.getSkills().drainLevel(Skills.ATTACK, Utils.random(3));	
		player.getSkills().drainLevel(Skills.STRENGTH, Utils.random(3));	
		player.getSkills().drainLevel(Skills.DEFENCE, Utils.random(3));	
		player.getSkills().drainLevel(Skills.RANGE, Utils.random(3));	
		player.getSkills().drainLevel(Skills.MAGIC, Utils.random(3));	
	}
	
	@Override
	public boolean sendDeath() {
		player.getControlerManager().removeControlerWithoutCheck();
		return true;
	}

}
