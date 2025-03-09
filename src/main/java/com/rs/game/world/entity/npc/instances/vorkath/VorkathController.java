/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * @author ReverendDread
 * Sep 10, 2018
 */
public class VorkathController extends Controller {

	private Position OUTSIDE = new Position(2272, 4052, 0);
	
	private VorkathNPC vorkath;
	
	private VorkathInstance instance;
	
	@Override
	public void start() {
		instance = (VorkathInstance) getArguments()[0];
		vorkath = new VorkathNPC(28059, instance.getLocation(2269, 4062, 0), -1, true, true, instance);
		player.setForceMultiArea(true);
	}
	
	@Override
	public boolean processItemTeleport(Position destination) {
		finish();
		return true;
	}
	
	@Override
	public boolean processObjectTeleport(Position destination) {
		finish();
		return true;
	}
	
	@Override
	public boolean processMagicTeleport(Position destination) {
		finish();
		return true;
	}
	
	@Override
	public boolean logout() {
		player.setNextPosition(OUTSIDE);
		finish();
		return true;
	}
	
	@Override
	public boolean login() {
		player.setNextPosition(OUTSIDE);
		return true;
	}
	
	@Override
	public boolean sendDeath() {
		finish();
		return true;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == WorldObject.asOSRS(31990)) {
			player.animate(new Animation(839));
			player.setNextPosition(OUTSIDE);
			finish();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == 28059) {
			player.animate(827);
			npc.transformIntoNPC(NPC.asOSRS(8058));
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;
				
				@Override
				public void run() {
					ticks++;
					if (ticks == 1) {
						npc.animate(Animation.createOSRS(7950));
					} else if (ticks == 4) {
						npc.transformIntoNPC(28061);
					} else if (ticks == 5) {
						npc.setTarget(player);
						stop();
					}
				}
				
			}, 1, 1);
			return false;
		}
		return true;
	}
	
	private void finish() {
		instance.destroyRegion();
		player.setForceMultiArea(false);
		vorkath.finish();
		instance = null;
		removeControler();
	}

}
