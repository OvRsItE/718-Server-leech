/**
 * 
 */
package com.rs.game.world.entity.npc.instances.skotizo;

import com.rs.Constants;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.controller.Controller;

/**
 * @author ReverendDread
 * Jan 7, 2019
 */
public class SkotizoController extends Controller {

	private SkotizoInstance instance;
	
	private Skotizo skotizo;
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.player.controller.Controller#start()
	 */
	@Override
	public void start() {
		instance = (SkotizoInstance) getArguments()[0];
		player.setForceMultiArea(true);
		player.setAtMultiArea(true);
		skotizo = new Skotizo(instance, 27286, instance.getLocation(1695, 9886, 0), -1, 0, true);
		skotizo.setTarget(player);
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
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 128925) {
			player.setNextPosition(Constants.START_PLAYER_LOCATION);
			finish();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean sendDeath() {
		finish();
		return true;
	}
	
	public void finish() {
		instance.destroyRegion();
		skotizo.finish();
	}
	
}
