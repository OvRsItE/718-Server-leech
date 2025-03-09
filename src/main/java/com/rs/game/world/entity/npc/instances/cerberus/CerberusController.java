/**
 * 
 */
package com.rs.game.world.entity.npc.instances.cerberus;

import com.rs.Constants;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.randomspawns.SuperiorBosses;
import com.rs.game.world.entity.player.controller.Controller;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 21, 2018
 */
public class CerberusController extends Controller {
	
	private CerberusNPC cerberus;
	
	private CerberusInstance instance;
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.controller.Controller#start()
	 */
	@Override
	public void start() {
		instance = (CerberusInstance) getArguments()[0];
		cerberus = new CerberusNPC(25863, player, instance.getLocation(1238, 1251, 0), instance);
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
		player.setNextPosition(Constants.START_PLAYER_LOCATION);
		finish();
		return true;
	}
	
	@Override
	public boolean login() {
		player.setNextPosition(Constants.START_PLAYER_LOCATION);
		return true;
	}
	
	@Override
	public boolean sendDeath() {
		finish();
		return true;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 121772) {
			player.setNextPosition(Constants.START_PLAYER_LOCATION);
			finish();
			return false;
		}
		if (object.getId() == 123105) {
			Position destination;
			if (player.getY() >= instance.getLocation(1240, 1242, 0).getY()) {
				destination = instance.getLocation(new Position(1240, 1241));
				player.addWalkSteps(destination.getX(), destination.getY(), 3, false);
			} else {
				destination = instance.getLocation(new Position(1240, 1243));
				player.addWalkSteps(destination.getX(), destination.getY(), 3, false);
			}
			player.applyHit(new Hit(player, 50, HitLook.REGULAR_DAMAGE));
			player.getPackets().sendGameMessage("You take some damage from the flames.", true);
		}
		return true;
	}

	@Override
	public void processNPCDeath(NPC npc) {
		if (npc.getId() == 25862 &&  Utils.random(180) == 1 && player.UnlockedSuperiorBosses == 1) {
			SuperiorBosses.SpawnCerberus(player, npc);
			World.sendWorldMessage("[<col=ff0000>Boss Spawn</col>] - <col=ff0000>" + Utils.formatPlayerNameForDisplay(player.getUsername()) +
					"<col=ff0000> has spawned a superior cerberus boss they have 5 minutes to kill it. <col=ff0000>", false);
			cerberus.setSpawned(true);
		}
		if (npc.getId() == 16146) {
			cerberus.setSpawned(false);
			cerberus.setRespawnTask();
		}
		super.processNPCDeath(npc);
	}

	private void finish() {
		instance.destroyRegion();
		World.deleteObject(new WorldObject(123105, 10, 0, instance.getLocation(1239, 1242, 0)));
		World.deleteObject(new WorldObject(123105, 10, 0, instance.getLocation(1240, 1242, 0)));
		World.deleteObject(new WorldObject(123105, 10, 0, instance.getLocation(1241, 1242, 0)));
		player.setForceMultiArea(false);
		cerberus.finish();
		instance = null;
		removeControler();
	}

}
